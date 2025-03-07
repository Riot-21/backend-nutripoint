package com.example.backend_nutripoint.jwt;

import static com.example.backend_nutripoint.config.TokenJwtConfig.*;
import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
              
        try {
            String bearerToken = request.getHeader(HEADER_AUTHORIZATION);

            if (bearerToken == null || !bearerToken.startsWith(PREFIX_TOKEN)) {
                filterChain.doFilter(request, response);
                System.out.println("Token no presente o formato incorrecto");
                return;
            }
            String jwt = bearerToken.substring(7);
            Claims claims = jwtService.getTokenClaims(jwt);
            // System.out.println(jwt);
            if (claims == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                return;
            }

            String email = claims.getSubject();
            var userDetails = userService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "No se pudo autenticar al usuario: " + ex.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

}
