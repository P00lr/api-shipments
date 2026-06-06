package com.paul.shitment.shipment_service.security.service.impl;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.paul.shitment.shipment_service.security.service.LoginAttemptService;

@Service
public class LocalLoginAttemptService implements LoginAttemptService {

    private final int maxAttempts;
    private final long lockTimeMs;
    // Ahora la clave es la IP, no el username
    private final ConcurrentHashMap<String, LoginAttempt> attempts = new ConcurrentHashMap<>();

    public LocalLoginAttemptService(
            @Value("${login.max-attempts}") int maxAttempts,
            @Value("${login.lock-time-ms}") long lockTimeMs) {
        this.maxAttempts = maxAttempts;
        this.lockTimeMs = lockTimeMs;
    }

    @Override
    public void loginFailed(String key) { // key = IP
        long now = System.currentTimeMillis();
        attempts.compute(key, (k, attempt) -> {
            if (attempt == null)
                return new LoginAttempt(1, 0);

            if (isLockExpired(attempt, now))
                return new LoginAttempt(1, 0);

            int newCount = attempt.count + 1;
            long lockTime = (newCount >= maxAttempts && attempt.lockTime == 0) ? now : attempt.lockTime;
            
            return new LoginAttempt(newCount, lockTime);
        });
    }

    @Override
    public void loginSucceeded(String key) {
        attempts.remove(key);
    }

    @Override
    public boolean isBlocked(String key) {
        LoginAttempt attempt = attempts.get(key);
        if (attempt == null)
            return false;
        long now = System.currentTimeMillis();
        if (isLockExpired(attempt, now)) {
            attempts.remove(key);
            return false;
        }
        return attempt.count >= maxAttempts;
    }

    private boolean isLockExpired(LoginAttempt attempt, long now) {
        return attempt.lockTime > 0 && (now - attempt.lockTime) > lockTimeMs;
    }

    private record LoginAttempt(int count, long lockTime) {
    }
}