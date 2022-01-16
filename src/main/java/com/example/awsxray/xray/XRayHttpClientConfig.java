package com.example.awsxray.xray;

import com.amazonaws.xray.proxies.apache.http.HttpClientBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class XRayHttpClientConfig {
    private static final int DEFAULT_MAX_PER_ROUTE = 200;
    private static final int MAX_TOTAL = 50;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    private HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
        manager.setMaxTotal(MAX_TOTAL);

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setConnectionManager(manager).build();
        factory.setHttpClient(httpClient);
        return factory;
    }
}
