package com.summit.config;

import cn.hutool.core.util.StrUtil;
import com.summit.dao.entity.GatewayRouteInfo;
import com.summit.dao.repository.GatewayRouteInfoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@DependsOn("flywayInitializer")
public class DynamicRouteLocator extends DiscoveryClientRouteLocator {

    @Autowired
    ZuulProperties properties;
    @Autowired
    GatewayRouteInfoDao gatewayRouteInfoDao;
    private DiscoveryClient discovery;

    @Autowired
    public DynamicRouteLocator(DispatcherServletPath dispatcherServletPath, DiscoveryClient discovery,
                               ZuulProperties zuulProperties,
                               Registration registration) {
        super(dispatcherServletPath.getPrefix(), discovery, zuulProperties, registration);
        this.discovery = discovery;
    }

    /**
     * 加载路由配置
     *
     * @return 路由表
     */
    @Override
    protected LinkedHashMap<String, ZuulProperties.ZuulRoute> locateRoutes() {

        LinkedHashMap<String, ZuulProperties.ZuulRoute> routesMap = new LinkedHashMap<>();

        log.debug("加载配置文件和注册中心的路由信息");
        routesMap.putAll(locateRoutesFromYmlAndRegisterCenter());

        log.debug("加载数据库中的路由信息");
        routesMap.putAll(locateRoutesFromDb());

        LinkedHashMap<String, ZuulProperties.ZuulRoute> values = new LinkedHashMap<>();
        for (Map.Entry<String, ZuulProperties.ZuulRoute> entry : routesMap.entrySet()) {
            String path = entry.getKey();
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StrUtil.isNotBlank(this.properties.getPrefix())) {
                path = this.properties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }
            values.put(path, entry.getValue());
        }
        return values;
    }

    /**
     * 加载配置文件和注册中心的路由信息
     *
     * @return
     */
    private Map<String, ZuulProperties.ZuulRoute> locateRoutesFromYmlAndRegisterCenter() {
        Map<String, ZuulProperties.ZuulRoute> routesMap = new LinkedHashMap<>();
        //读取properties中的路由配置

        for (ZuulProperties.ZuulRoute route : this.properties.getRoutes().values()) {
            routesMap.put(route.getPath(), route);
        }
        //读取注册中心的路由信息
        if (this.discovery != null) {
            Map<String, ZuulProperties.ZuulRoute> staticServices = new LinkedHashMap<>();
            for (ZuulProperties.ZuulRoute route : routesMap.values()) {
                String serviceId = route.getServiceId();
                if (serviceId == null) {
                    serviceId = route.getId();
                }
                if (serviceId != null) {
                    staticServices.put(serviceId, route);
                }
            }
            // Add routes for discovery services by default
            List<String> services = this.discovery.getServices();
            String[] ignored = this.properties.getIgnoredServices()
                    .toArray(new String[0]);
            for (String serviceId : services) {
                // Ignore specifically ignored services and those that were manually
                // configured
                String key = "/" + mapRouteToService(serviceId) + "/**";
                if (staticServices.containsKey(serviceId)
                        && staticServices.get(serviceId).getUrl() == null) {
                    // Explicitly configured with no URL, cannot be ignored
                    // all static routes are already in routesMap
                    // Update location using serviceId if location is null
                    ZuulProperties.ZuulRoute staticRoute = staticServices.get(serviceId);
                    if (!StringUtils.hasText(staticRoute.getLocation())) {
                        staticRoute.setLocation(serviceId);
                    }
                }
                if (!PatternMatchUtils.simpleMatch(ignored, serviceId)
                        && !routesMap.containsKey(key)) {
                    // Not ignored
                    routesMap.put(key, new ZuulProperties.ZuulRoute(key, serviceId));
                }
            }
        }
        if (routesMap.get(DEFAULT_ROUTE) != null) {
            ZuulProperties.ZuulRoute defaultRoute = routesMap.get(DEFAULT_ROUTE);
            // Move the defaultServiceId to the end
            routesMap.remove(DEFAULT_ROUTE);
            routesMap.put(DEFAULT_ROUTE, defaultRoute);
        }
        return routesMap;

    }

    /**
     * 加载数据库中的路由信息
     *
     * @return
     */
    private Map<String, ZuulProperties.ZuulRoute> locateRoutesFromDb() {
        Map<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<>();
        try {
            List<GatewayRouteInfo> gatewayRouteInfoList = gatewayRouteInfoDao.selectList(null);
            for (GatewayRouteInfo gatewayRouteInfo : gatewayRouteInfoList) {
                if (StrUtil.isBlank(gatewayRouteInfo.getPath())) {
                    continue;
                }
                if (gatewayRouteInfo.getEnabled() == 0) {
                    continue;
                }
                ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
                zuulRoute.setId(gatewayRouteInfo.getServiceId());
                zuulRoute.setPath(gatewayRouteInfo.getPath());
                zuulRoute.setServiceId(gatewayRouteInfo.getServiceId());
                zuulRoute.setStripPrefix(gatewayRouteInfo.getStripPrefix() == 1 ? Boolean.TRUE : Boolean.FALSE);
                zuulRoute.setRetryable(gatewayRouteInfo.getRetryable() == 1 ? Boolean.TRUE : Boolean.FALSE);

                log.debug("添加数据库的路由配置,serviceId：{}，path:{}", zuulRoute.getServiceId(), zuulRoute.getPath());
                routes.put(zuulRoute.getPath(), zuulRoute);
            }
        } catch (Exception e) {
            log.error("从数据库加载路由配置异常", e);
        }
        return routes;
    }

    @Override
    public void refresh() {
        doRefresh();
    }
}
