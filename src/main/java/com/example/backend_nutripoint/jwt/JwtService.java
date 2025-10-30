package com.example.backend_nutripoint.jwt;

import org.springframework.stereotype.Service;

import com.example.backend_nutripoint.models.Usuario;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Value("${security.jwt.expiration-time-ms}")
    private int expirationTimeMs;
    @Value("${security.jwt.issuer}")
    private String issuer;

    public String createJwtToken(Usuario user) {
        byte[] ketBytes = Decoders.BASE64.decode(secretKey);
        var key = Keys.hmacShaKeyFor(ketBytes);
        // var key = getSigningKey();
        return Jwts
                .builder()
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .issuer(issuer)
                .expiration(new Date(System.currentTimeMillis() + expirationTimeMs))
                .signWith(key)
                .compact();
    }


    public Claims getTokenClaims(String token) {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            var key = Keys.hmacShaKeyFor(keyBytes);

            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            
            throw new JwtException("El token ha expirado");
        } catch (io.jsonwebtoken.security.SignatureException e) {
            
            throw new JwtException("Firma JWT no válida");
        } catch (JwtException e) {
            // Cualquier otro error JWT
            throw new JwtException("Token inválido: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar el token: " + e.getMessage());
        }
    }

    // public static boolean validateToken(String token) {
    // try {
    // Jwts.parserBuilder().getSigningKey(key).build().parseClaimsJws(token);
    // return true;
    // } catch (Exception e) {
    // return false;
    // }
    // }

}
