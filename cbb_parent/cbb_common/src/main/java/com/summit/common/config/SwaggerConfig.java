package com.summit.common.config;

import com.google.common.base.Predicate;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * swagger 配置
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String OAuth2Name = "SummitOauth2";

    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Collections.singletonList(securitySchema()))
                .securityContexts(newArrayList(securityContexts()));
    }

    private List<SecurityContext> securityContexts() {
        return newArrayList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(securityPaths())
                        .build()
        );
    }

    private OAuth securitySchema() {
        ArrayList<GrantType> grantTypes = new ArrayList<>();
        grantTypes.add(new ResourceOwnerPasswordCredentialsGrant("/oauth/token"));
        return new OAuth(OAuth2Name, getAuthorizationScopeList(), grantTypes);
    }

    private List<SecurityReference> defaultAuth() {
        ArrayList<AuthorizationScope> authorizationScopeList =
                (ArrayList<AuthorizationScope>) getAuthorizationScopeList();

        AuthorizationScope[] authorizationScopes = new AuthorizationScope[authorizationScopeList.size()];
        authorizationScopeList.toArray(authorizationScopes);

        return newArrayList(new SecurityReference(OAuth2Name, authorizationScopes));
    }

    private List<AuthorizationScope> getAuthorizationScopeList() {
        ArrayList<AuthorizationScope> authorizationScopeList = new ArrayList<>();
        authorizationScopeList.add(new AuthorizationScope("server", ""));
        return authorizationScopeList;
    }


    private Predicate<String> securityPaths() {
        return and(regex("^(?!oauth).*$"));
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("山脉科技共享组件平台 Restful API")
                .description("接口说明与调试界面")
                .termsOfServiceUrl("http://www.summit.com.cn/")
                .contact(new Contact("Summit", "http://www.summit.com.cn/", ""))
                .version("1.0")
                .build();
    }

}