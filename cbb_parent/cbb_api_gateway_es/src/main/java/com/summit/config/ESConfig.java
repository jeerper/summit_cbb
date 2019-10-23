package com.summit.config;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


@Configuration
@PropertySource(value = "classpath:esconfig.properties")
public class ESConfig {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${elasticsearch.sql.url}")
    public static String url;


    @Value("${elasticsearch.cluster.nodes}")
    private String esCluster;


    @Value("${elasticsearch.clustername}")
    private String esClusterName;

    @Value("${elasticsearch.cluster.port}")
    private int clusterPort;

    private List<TransportAddress> list = new ArrayList<>();

    @Bean
    public TransportClient client() throws Exception {
        Settings esSettings = Settings.builder().put("cluster.name", esClusterName)
                .put("client.transport.sniff", true).build();
        TransportClient transportClient = null;
        transportClient = new PreBuiltTransportClient(esSettings);
        transportClient.addTransportAddresses(getTransportAddresses());
        return transportClient;
    }

    private TransportAddress[] getTransportAddresses() throws UnknownHostException {
        if (StringUtils.isNotBlank(esCluster)) {
            String[] urllist = esCluster.split(",");
            for (String str : urllist) {
                TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(str), clusterPort);
                list.add(transportAddress);
            }
        }
        return list.toArray(new TransportAddress[list.size()]);
    }

	/*@Value("${elasticsearch.firstIp}")
	private String firstIp;
	@Value("${elasticsearch.secondIp}")
	private String secondIp;
	@Value("${elasticsearch.thirdIp}")
	private String thirdIp;
	@Value("${elasticsearch.firstPort}")
	private String firstPort;
	@Value("${elasticsearch.secondPort}")
	private String secondPort;
	@Value("${elasticsearch.thirdPort}")
	private String thirdPort;
	@Value("${elasticsearch.clusterName}")
	private String clusterName;

	@Bean
	public TransportClient getTransportClient() {
		logger.info("ElasticSearch初始化开始。。");
		logger.info("要连接的节点1的ip是{}，端口是{}，集群名为{}", firstIp, firstPort, clusterName);
		logger.info("要连接的节点2的ip是{}，端口是{}，集群名为{}", secondIp, secondPort, clusterName);
		logger.info("要连接的节点3的ip是{}，端口是{}，集群名为{}", thirdIp, thirdPort, clusterName);
		TransportClient transportClient = null;
		try {
			Settings settings = Settings.builder().put("cluster.name", clusterName).put("client.transport.sniff", true)
					.build();
			transportClient = new PreBuiltTransportClient(settings);
			TransportAddress firstAddress = new TransportAddress(InetAddress.getByName(firstIp),
					Integer.parseInt(firstPort));
			TransportAddress secondAddress = new TransportAddress(InetAddress.getByName(secondIp),
					Integer.parseInt(secondPort));
			TransportAddress thirdAddress = new TransportAddress(InetAddress.getByName(thirdIp),
					Integer.parseInt(thirdPort));
			transportClient.addTransportAddress(firstAddress);
			transportClient.addTransportAddress(secondAddress);
			transportClient.addTransportAddress(thirdAddress);
			logger.info("ElasticSearch初始化完成。。");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ElasticSearch初始化失败：" + e.getMessage(), e);
		}
		return transportClient;
	}*/
}