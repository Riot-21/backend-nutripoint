package com.example.backend_nutripoint.jwt;

import org.springframework.stereotype.Service;

import com.example.backend_nutripoint.models.Usuario;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
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

    public String createJwtToken(Usuario user){
        byte[] ketBytes = Decoders.BASE64.decode(secretKey);
        var key = Keys.hmacShaKeyFor(ketBytes);
        return Jwts
            .builder()
            .subject(user.getEmail())
            .issuedAt(new Date(System.currentTimeMillis()))
            .issuer(issuer)
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .signWith(key)
            .compact();
    }

    public Claims getTokenClaims(String token){
        byte[] ketBytes = Decoders.BASE64.decode(secretKey);
        var key = Keys.hmacShaKeyFor(ketBytes);
        try{
            var claims = Jwts
                        .parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
            Date expDate = claims.getExpiration();
            Date curretnDate = new Date();
            if(curretnDate.before(expDate)){
                return claims;
            }
        }catch(Exception ex){
            throw ex;
        }
        return null;
    }


}
