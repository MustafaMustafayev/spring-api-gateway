package com.fintech.apigateway.config.circuitbreaker;

import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.util.function.Function;

@Configuration
public class ResilienceConfig {

    private final ResilienceProperties properties;

    public ResilienceConfig(ResilienceProperties properties) {
        this.properties = properties;
    }

    @Bean
    public Function<GatewayFilterSpec, GatewayFilterSpec> resilientRouteFilter() {
        return f -> f
                .retry(config -> config
                        .setRetries(properties.getRetry().getAttempts())
                        .setMethods(properties.getRetry().getMethods().toArray(new HttpMethod[0]))
                        .setBackoff(
                                Duration.ofMillis(properties.getRetry().getBackoff().getFirstDelayMs()),
                                Duration.ofMillis(properties.getRetry().getBackoff().getMaxDelayMs()),
                                properties.getRetry().getBackoff().getFactor(),
                                properties.getRetry().getBackoff().isJitter()
                        )
                )
                .circuitBreaker(config -> config
                        .setName(properties.getDefaultCircuitBreaker())
                        .setFallbackUri("forward:" + properties.getDefaultFallbackUri())
                );
    }

    @Bean
    public TriFunction<GatewayFilterSpec, String, String, GatewayFilterSpec> applyRetryAndCircuitBreaker() {
        return (f, cbName, fallbackUri) -> f
                .retry(config -> config
                        .setRetries(properties.getRetry().getAttempts())
                        .setMethods(properties.getRetry().getMethods().toArray(new HttpMethod[0]))
                        .setBackoff(
                                Duration.ofMillis(properties.getRetry().getBackoff().getFirstDelayMs()),
                                Duration.ofMillis(properties.getRetry().getBackoff().getMaxDelayMs()),
                                properties.getRetry().getBackoff().getFactor(),
                                properties.getRetry().getBackoff().isJitter()
                        )
                )
                .circuitBreaker(config -> config
                        .setName(cbName)
                        .setFallbackUri("forward:" + fallbackUri)
                );
    }
}
