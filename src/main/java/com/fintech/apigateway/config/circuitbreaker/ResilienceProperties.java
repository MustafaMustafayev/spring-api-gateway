package com.fintech.apigateway.config.circuitbreaker;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "resilience")
@Getter
@Setter
public class ResilienceProperties {
    private String defaultCircuitBreaker;
    private String defaultFallbackUri;
    private Retry retry = new Retry();

    @Getter
    @Setter
    public static class Retry {
        private int attempts;
        private List<HttpMethod> methods;
        private Backoff backoff = new Backoff();

        @Getter
        @Setter
        public static class Backoff {
            private long firstDelayMs;
            private long maxDelayMs;
            private int factor;
            private boolean jitter;
        }
    }
}
