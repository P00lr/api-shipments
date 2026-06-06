package com.paul.shitment.shipment_service.security.service.impl;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Service
public class RateLimiterService {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createBucket(String endpoint) {

        Bandwidth limit;

        switch (endpoint) {
            case "/api/auth/login" ->
                limit = Bandwidth.builder()
                        .capacity(10)
                        .refillGreedy(10, Duration.ofMinutes(3))
                        .build();
            case "/api/v1/persons/documentNumber" ->
                limit = Bandwidth.builder()
                        .capacity(30)
                        .refillGreedy(30, Duration.ofMinutes(1))
                        .build();
            default ->
                limit = Bandwidth.builder()
                        .capacity(100)
                        .refillGreedy(100, Duration.ofMinutes(1))
                        .build();
        }

        return Bucket.builder()
                .addLimit(limit)
                .build();

    }

    public boolean allowRequest(String ip, String endpoint) {

        String key = ip + ":" + endpoint;

        Bucket bucket = buckets.computeIfAbsent(
                key,
                k -> createBucket(endpoint));

        return bucket.tryConsume(1);
    }
}
