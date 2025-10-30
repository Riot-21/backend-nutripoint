package com.example.backend_nutripoint.jwt;

import static com.example.backend_nutripoint.config.TokenJwtConfig.*;
import java.io.IOException;

import org.springframework.lang.NonNull;
// import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
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
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String bearerToken = request.getHeader(HEADER_AUTHORIZATION);

            if (bearerToken == null || !bearerToken.startsWith(PREFIX_TOKEN)) {
                filterChain.doFilter(request, response);
                // System.out.println("Token no presente o formato incorrecto");
                return;
            }
            String jwt = bearerToken.substring(7); // PREFIX_TOKEN.length()
            Claims claims = jwtService.getTokenClaims(jwt);
            // System.out.println(jwt);
            if (claims == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                return;
            }

            String email = claims.getSubject();
            var userDetails = userService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException ex) {
            SecurityContextHolder.clearContext();
            throw new BadCredentialsException("Token inválido o expirado", ex);
        }

        filterChain.doFilter(request, response);
    }

}
