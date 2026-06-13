package com.yoot.booking.api.config;

import com.yoot.booking.api.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(

            ServerHttpRequest request,

            org.springframework.http.server.ServerHttpResponse response,

            WebSocketHandler wsHandler,

            Map<String, Object> attributes
    ) {

        try {

            if (
                    request instanceof
                            ServletServerHttpRequest servletRequest
            ) {

                String token =
                        servletRequest
                                .getServletRequest()
                                .getParameter("token");

                if (
                        token == null ||
                                token.isBlank()
                ) {

                    return false;
                }

                String email = jwtUtil.extractEmail(token);

                attributes.put(
                        "principal",
                        new SocketPrincipal(email)
                );

                return true;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void afterHandshake(

            ServerHttpRequest request,

            org.springframework.http.server.ServerHttpResponse response,

            WebSocketHandler wsHandler,

            Exception exception
    ) {

    }
}