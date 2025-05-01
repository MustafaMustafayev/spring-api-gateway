# Spring Cloud API Gateway

A robust API Gateway implementation using Spring Cloud Gateway with built-in resilience patterns including Circuit Breaker and Retry mechanisms.

## Features

- **API Gateway**: Centralized entry point for all microservices
- **Circuit Breaker**: Implemented using Resilience4j to prevent cascading failures
- **Retry Mechanism**: Automatic retry for failed requests with configurable backoff
- **Global Resilience**: Circuit breaker and retry patterns applied across all routes
- **Reactive Architecture**: Built on Spring WebFlux for non-blocking I/O

## Technical Stack

- Java 17
- Spring Boot 3.4.5
- Spring Cloud Gateway 2024.0.1
- Resilience4j for Circuit Breaker
- Spring WebFlux

## Configuration

### Circuit Breaker Configuration
```yaml
resilience4j:
  circuitbreaker:
    configs:
      default:
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        permittedNumberOfCallsInHalfOpenState: 4
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 5s
        failureRateThreshold: 50
```

### Retry Configuration
```yaml
resilience:
  retry:
    attempts: 2
    methods: [GET, POST]
    backoff:
      first-delay-ms: 100
      max-delay-ms: 1000
      factor: 2
      jitter: true
```

## Project Structure

```
src/main/java/com/fintech/apigateway/
├── config/         # Configuration classes
├── controller/     # API controllers
├── filter/         # Gateway filters
├── model/          # Data models
├── routes/         # Route configurations
└── security/       # Security configurations
```

## Getting Started

1. **Prerequisites**
   - Java 17
   - Maven

2. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. **Default Port**
   - The API Gateway runs on port 8095

## Example Route Configuration

The gateway is configured to route requests to various microservices. For example:

```java
@Bean
public RouteLocator addRoutes(RouteLocatorBuilder builder) {
    return builder.routes()
            .route("user-service", r -> r.path("/api/v1/users/**")
                    .filters(f -> retryAndCircuitBreaker.apply(f, "userServiceCircuitBreaker", "/fallback"))
                    .uri("http://localhost:8091"))
            .build();
}
```

## Resilience Features

### Circuit Breaker
- Monitors for failures and opens the circuit when threshold is reached
- Automatically transitions to half-open state after configured duration
- Provides fallback responses when circuit is open

### Retry Mechanism
- Configurable number of retry attempts
- Exponential backoff with jitter
- Supports specific HTTP methods (GET, POST)
- Configurable delay between retries

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request
