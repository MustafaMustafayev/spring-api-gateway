package com.fintech.apigateway.config.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;


@Component
public class ResilienceMonitor {

    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RetryRegistry retryRegistry;

    public ResilienceMonitor(CircuitBreakerRegistry circuitBreakerRegistry, RetryRegistry retryRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
        this.retryRegistry = retryRegistry;
    }

    @PostConstruct
    public void init() {
        // Circuit Breaker Monitoring
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("userServiceCircuitBreaker");

        circuitBreaker.getEventPublisher()
                .onStateTransition(event -> {
                    System.out.println("[CIRCUIT BREAKER] State changed from: " +
                            event.getStateTransition().getFromState() +
                            " to: " + event.getStateTransition().getToState());
                })
                .onError(event -> {
                    System.out.println("[CIRCUIT BREAKER] Error: " +
                            event.getThrowable().getMessage() +
                            ", elapsed time: " + event.getElapsedDuration().toMillis() + "ms");
                })
                .onSuccess(event -> {
                    System.out.println("[CIRCUIT BREAKER] Call success, elapsed time: " +
                            event.getElapsedDuration().toMillis() + "ms");
                })
                .onCallNotPermitted(event -> {
                    System.out.println("[CIRCUIT BREAKER] Call not permitted - circuit is open");
                })
                .onIgnoredError(event -> {
                    System.out.println("[CIRCUIT BREAKER] Ignored error: " +
                            event.getThrowable().getMessage());
                });

        System.out.println("[CIRCUIT BREAKER] '" + circuitBreaker.getName() +
                "' initialized with state: " + circuitBreaker.getState());

        // Retry Monitoring
        Retry retry = retryRegistry.retry("userServiceCircuitBreaker");

        retry.getEventPublisher()
                .onRetry(event -> {
                    System.out.println("[RETRY] Attempt #" + event.getNumberOfRetryAttempts() +
                            " failed with: " + event.getLastThrowable().getMessage() +
                            ", will retry in " + event.getWaitInterval().toMillis() + "ms");
                })
                .onSuccess(event -> {
                    System.out.println("[RETRY] Successful after " + event.getNumberOfRetryAttempts() +
                            " attempts, elapsed time: ");
                })
                .onError(event -> {
                    System.out.println("[RETRY] Failed after " + event.getNumberOfRetryAttempts() +
                            " attempts with: " + event.getLastThrowable().getMessage());
                });

        System.out.println("[RETRY] '" + retry.getName() + "' initialized");
    }
}