package com.assesment.authentification.implementation;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AccountApiConfiguration {
    @Autowired
    private ServerProperties properties;

    @Bean
    public RestTemplate restTemplate() throws Exception {
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(buildHttpClient()));
    }

    private HttpClient buildHttpClient() throws Exception {
        return HttpClients.custom()
                .setSSLContext(buildSslContext())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
    }

    private SSLContext buildSslContext() throws Exception {
        return SSLContextBuilder
                .create()
                .setKeyStoreType(properties.getSsl().getKeyStoreType())
                .loadKeyMaterial(
                        ResourceUtils.getURL(properties.getSsl().getKeyStore()),
                        properties.getSsl().getKeyStorePassword().toCharArray(),
                        properties.getSsl().getKeyPassword().toCharArray()
                )
                .loadTrustMaterial(
                        ResourceUtils.getURL(properties.getSsl().getTrustStore()),
                        properties.getSsl().getTrustStorePassword().toCharArray()
                )
                .build();
    }
}
