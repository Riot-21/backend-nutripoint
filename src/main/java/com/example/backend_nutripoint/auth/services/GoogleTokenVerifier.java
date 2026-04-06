package com.example.backend_nutripoint.auth.services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

@Service
public class GoogleTokenVerifier {

    private final GoogleIdTokenVerifier verifier;

    // @Value("${google-client-id}")
    // private String clientId;

    public GoogleTokenVerifier(@Value("${google-client-id}") String clientId) {
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalStateException("google-client-id no está configurado");
        }
        this.verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory())
                .setAudience(List.of(clientId))
                .build();
    }

    public GoogleIdToken.Payload verify(String idTokenString)
            throws GeneralSecurityException, IOException {

        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken == null) {
            throw new IllegalArgumentException("Token Google inválido");
        }

        return idToken.getPayload();
    }
}

