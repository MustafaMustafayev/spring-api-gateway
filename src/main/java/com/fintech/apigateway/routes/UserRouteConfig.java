package com.fintech.apigateway.routes;

import com.fintech.apigateway.config.circuitbreaker.TriFunction;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRouteConfig {

    private final TriFunction<GatewayFilterSpec, String, String, GatewayFilterSpec> retryAndCircuitBreaker;

    public UserRouteConfig(TriFunction<GatewayFilterSpec, String, String, GatewayFilterSpec> retryAndCircuitBreaker) {
        this.retryAndCircuitBreaker = retryAndCircuitBreaker;
    }

    @Bean
    public RouteLocator addRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/api/v1/users/**")
                        .filters(f -> retryAndCircuitBreaker.apply(f, "userServiceCircuitBreaker", "/fallback"))
                        .uri("http://localhost:8091"))
                .build();
    }
}
