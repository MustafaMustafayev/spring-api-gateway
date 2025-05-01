package com.fintech.apigateway.config.circuitbreaker;

@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}

