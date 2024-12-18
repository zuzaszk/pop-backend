package com.pop.backend.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.oauth.OAuth10aService;

@Configuration
public class USOSConfig {

    @Value("${usos.api.key}")
    private String apiKey;

    @Value("${usos.api.secret}")
    private String apiSecret;

    @Value("${usos.api.base.url}")
    private String baseUrl;

    @Value("$backend_url")
    private String backendUrl;

    @Bean
    OAuth10aService oauthService() {
        return new ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .callback(backendUrl + "/usos/callback")
                .build(new DefaultApi10a() {
                    @Override
                    public String getAccessTokenEndpoint() {
                        return baseUrl + "/oauth/access_token";
                    }

                    @Override
                    public String getRequestTokenEndpoint() {
                        return baseUrl + "/oauth/request_token";
                    }

                    @Override
                    protected String getAuthorizationBaseUrl() {
                        return baseUrl + "/oauth/authorize";
                    }
                });
    }
}
