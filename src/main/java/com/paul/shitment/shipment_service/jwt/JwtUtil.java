package com.paul.shitment.shipment_service.jwt;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.exceptions.validation.JwtValidationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtUtil(
            @Value("${spring.security.jwt.secret}") String secretKey,
            @Value("${spring.security.jwt.expiration}") long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.expirationMs = expirationMs;
    }

    public String generatedToken(UUID uuid, String username, UUID officeId, List<String> roles) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .subject(username) // identifica al usuario → nombre en el boleto
                .issuedAt(new Date(now)) // cuándo se emitió → fecha de emisión del boleto
                .expiration(new Date(now + expirationMs)) // cuándo expira → fecha de caducidad del boleto
                .claim("uuid", uuid) // agrega UUID → número único del boleto
                .claim("officeId", officeId)// agreag officeId en el token
                .claim("roles", roles) // agrega roles → permisos del usuario en el boleto
                .signWith(secretKey) // firma segura → sello que evita falsificación
                .compact(); // genera el token → boleto listo para entregar
    }

    public Claims getClaims(String token) {
        return Jwts.parser() // prepara el analizador → como un portero que revisa boletos
                .verifyWith(secretKey) // verifica la firma → revisa el sello de seguridad del boleto
                .build() // finaliza la configuración → el portero ya está listo para usar
                .parseSignedClaims(token) // decodifica y valida → abre el boleto y revisa que no esté falsificado
                .getPayload(); // devuelve claims → saca la información del boleto (asiento, nombre, roles)
    }

    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public UUID getOfficeIdFromToken(String token) {
        String officeId = getClaims(token).get("officeId", String.class);
        return UUID.fromString(officeId);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            Claims claims = getClaims(token); // abre el boleto y revisa que no esté falsificado
            boolean notExpired = claims.getExpiration().after(new Date()); // revisa la fecha de caducidad del boleto
            boolean usernameMatches = claims.getSubject().equals(userDetails.getUsername());
            // verifica que el boleto pertenezca al usuario correcto → el nombre en el
            // boleto coincide con el dueño
            return notExpired && usernameMatches; // el boleto es válido solo si no está vencido y pertenece al usuario
        } catch (JwtValidationException ex) {
            log.error("Error validando token: {}", ex.getMessage()); // el portero anota cualquier error al revisar el boleto
            return false; // el boleto no pasa la validación
        }
    }
}
