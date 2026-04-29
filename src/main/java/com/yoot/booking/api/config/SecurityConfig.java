package com.yoot.booking.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoot.booking.api.dto.Common.ResultDTO;
import com.yoot.booking.api.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                // enable CORS
                .cors(cors -> {})

                .authorizeHttpRequests(auth -> auth

                        // cho phép preflight request
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Swagger & Auth — public
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger/**",
                                "/auth/**"
                        ).permitAll()

                        // Staff
                        .requestMatchers(HttpMethod.GET, "/staff/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/staff/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/staff/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/staff/**").hasRole("ADMIN")

                        // Schedule
                        .requestMatchers(HttpMethod.GET, "/schedules/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/schedules/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/schedules/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/schedules/**").hasRole("ADMIN")

                        // các API khác
                        .anyRequest().authenticated()
                )

                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // handle lỗi auth
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write(
                                    objectMapper.writeValueAsString(
                                            ResultDTO.fail("Chưa xác thực. Vui lòng đăng nhập.")
                                    )
                            );
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write(
                                    objectMapper.writeValueAsString(
                                            ResultDTO.fail("Bạn không có quyền thực hiện thao tác này.")
                                    )
                            );
                        })
                )

                // JWT filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .formLogin(form -> form.disable());

        return http.build();
    }

    // ================= CORS CONFIG =================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("*"));

        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept"
        ));

        // để frontend đọc được header
        config.setExposedHeaders(List.of("Authorization"));

        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }

    // ================= PASSWORD =================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}