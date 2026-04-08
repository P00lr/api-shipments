package com.paul.shitment.shipment_service.security.service.impl;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.paul.shitment.shipment_service.security.service.LoginAttemptService;

@Service
public class LocalLoginAttemptService implements LoginAttemptService {

    // Número máximo de intentos permitidos antes de bloquear
    private final int maxAttempts;

    // Tiempo que dura el bloqueo (en milisegundos)
    private final long lockTimeMs;

    // Mapa en memoria que guarda los intentos por usuario
    // key   -> username
    // value -> información del intento (contador + tiempo de bloqueo)
    private final ConcurrentHashMap<String, LoginAttempt> attempts = new ConcurrentHashMap<>();

    // Se inyectan los valores desde application.properties / yml
    public LocalLoginAttemptService(
            @Value("${login.max-attempts}") int maxAttempts,
            @Value("${login.lock-time-ms}") long lockTimeMs) {
        this.maxAttempts = maxAttempts;
        this.lockTimeMs = lockTimeMs;
    }

     //Se ejecuta cuando el usuario falla el login
    @Override
    public void loginFailed(String username) {
        long now = System.currentTimeMillis(); // tiempo actual

        // compute permite actualizar el valor de forma segura (thread-safe)
        attempts.compute(username, (key, attempt) -> {

            // Si no existe registro previo → primer intento fallido
            if (attempt == null)
                return new LoginAttempt(1, 0);

            // Si estaba bloqueado pero ya pasó el tiempo de castigo
            if (attempt.lockTime > 0 && now - attempt.lockTime > lockTimeMs) {
                return new LoginAttempt(1, 0); // reinicia contador
            }

            // Aumenta el número de intentos fallidos
            int count = attempt.count + 1;

            // Si supera el máximo → se bloquea
            long lockTime = count >= maxAttempts ? now : 0;

            return new LoginAttempt(count, lockTime);
        });
    }

    //Se ejecuta cuando el usuario inicia sesión correctamente
    @Override
    public void loginSucceeded(String username) {
        // Limpia cualquier bloqueo o conteo previo
        attempts.remove(username);
    }

    @Override
    public boolean isBlocked(String username) {

        // Busca si este usuario tiene intentos registrados
        LoginAttempt attempt = attempts.get(username);

        // Si no existe, nunca ha fallado → no está bloqueado
        if (attempt == null)
            return false;

        // Tiempo actual
        long now = System.currentTimeMillis();

        // Si ya estaba bloqueado PERO ya pasó el tiempo de castigo
        if (attempt.lockTime > 0 && now - attempt.lockTime > lockTimeMs) {
            attempts.remove(username); // se borra el castigo
            return false; // ya puede intentar otra vez
        }

        // Si llegó aquí, sigue bloqueado si alcanzó el máximo de intentos
        return attempt.count >= maxAttempts;
    }

    private static class LoginAttempt {
        int count; // cuántos intentos fallidos lleva
        long lockTime; // cuándo se bloqueó (en milisegundos)

        LoginAttempt(int count, long lockTime) {
            this.count = count;
            this.lockTime = lockTime;
        }
    }

}
