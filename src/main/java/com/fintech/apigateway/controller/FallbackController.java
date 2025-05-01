package com.fintech.apigateway.controller;

import com.fintech.apigateway.model.FallbackResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
public class FallbackController {

    @GetMapping("/fallback")
    public Mono<ResponseEntity<FallbackResponse>> fallback() {
        System.out.println("----fallback called-----");

        FallbackResponse response = new FallbackResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Service is temporarily unavailable. Please try again later.",
                LocalDateTime.now()
        );

        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response));
    }
}
