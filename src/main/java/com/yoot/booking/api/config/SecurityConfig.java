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

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // Swagger & Auth — public
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger/**",
                                "/auth/**"
                        ).permitAll()

                        // Staff — GET public, CUD yêu cầu ADMIN
                        .requestMatchers(HttpMethod.GET, "/staff/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/staff/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/staff/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/staff/**").hasRole("ADMIN")

                        // Tất cả còn lại cần authenticated
                        .anyRequest().authenticated()
                )

                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Xử lý lỗi 401 / 403 → trả về JSON ResultDTO
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write(
                                    objectMapper.writeValueAsString(ResultDTO.fail("Chưa xác thực. Vui lòng đăng nhập."))
                            );
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write(
                                    objectMapper.writeValueAsString(ResultDTO.fail("Bạn không có quyền thực hiện thao tác này."))
                            );
                        })
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .formLogin(form -> form.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}