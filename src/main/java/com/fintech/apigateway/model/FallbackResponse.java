package com.fintech.apigateway.model;

import java.time.LocalDateTime;

public record FallbackResponse(
        int status,
        String message,
        LocalDateTime timestamp
) {}

