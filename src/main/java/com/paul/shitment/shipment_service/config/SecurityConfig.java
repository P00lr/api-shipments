package com.paul.shitment.shipment_service.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.paul.shitment.shipment_service.jwt.JwtAuthenticationFilter;
import com.paul.shitment.shipment_service.security.exception.RestAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final AuthenticationProvider authenticationProvider;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final RestAuthenticationEntryPoint entryPoint;
        private final AccessDeniedHandler accessDeniedHandler;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)// JWT no usa cookies → CSRF innecesario
                                .cors(Customizer.withDefaults()) // permite solicitudes desde otros dominios (frontend
                                                                 // separado)

                                .sessionManagement(session -> session // no se guarda sesión → cada request se valida
                                                                      // solo con el boleto
                                                                      // (JWT)
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint(entryPoint)// si no hay boleto o es inválido →
                                                                                     // 401
                                                .accessDeniedHandler(accessDeniedHandler))// si el boleto es válido pero
                                                                                          // no tiene permisos → 403

                                .authorizeHttpRequests(authorize -> authorize

                                                // rutas públicas de autenticación (login, register, logout)
                                                .requestMatchers("/api/auth/**").permitAll()
                                                .requestMatchers("/error").permitAll()

                                                // rutas públicas solo de lectura (cualquiera puede ver)
                                                .requestMatchers(HttpMethod.GET,
                                                                "/api/v1/shipments", "/api/v1/shipments/**")
                                                .hasAnyRole("ADMIN", "USER") // usuarios autenticados pueden ver envíos

                                                .requestMatchers(HttpMethod.GET,
                                                                "/api/v1/transportCooperatives",
                                                                "/api/v1/transportCooperatives/**",

                                                                "/api/v1/offices", "/api/v1/offices/**",

                                                                "/api/v1/persons", "/api/v1/persons/**",

                                                                "/api/v1/users", "/api/v1/users",

                                                                "/api/v1/permissions", "/api/v1/permissions/**",
                                                                
                                                                "/api/v1/roles", "/api/v1/roles/**")
                                        
                                                .hasRole("ADMIN") // solo ADMIN puede ver datos de
                                                                  // configuración/usuarios/roles

                                                // rutas protegidas  solo ADMIN puede crear/modificar/eliminar
                                                .requestMatchers(HttpMethod.POST, "/api/v1/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/api/v1/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/api/v1/**").hasRole("ADMIN")

                                                .anyRequest().authenticated())// cualquier otra ruta requiere
                                                                              // autenticación

                                .authenticationProvider(authenticationProvider)// registra el portero de login
                                                                               // usuario/contraseña

                                // el portero JWT revisa el boleto antes que cualquier otro
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration config = new CorsConfiguration();

                // orígenes permitidos (frontends conocidos)
                config.setAllowedOrigins(List.of(
                                "http://localhost:3000",
                                "http://localhost:5173",
                                "http://localhost:4200",
                                "https://guileless-gumdrop-0b2da7.netlify.app"));

                // métodos HTTP permitidos
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

                // headers permitidos en la request
                config.setAllowedHeaders(List.of("*"));

                // headers que el frontend puede leer (ej. JWT)
                config.setExposedHeaders(List.of("Authorization", "Content-Type"));

                // permite enviar cookies o credenciales si es necesario
                config.setAllowCredentials(true);

                // tiempo de cacheo de configuración CORS
                config.setMaxAge(3600L);

                // aplica CORS a todas las rutas
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                return source;
        }
}
