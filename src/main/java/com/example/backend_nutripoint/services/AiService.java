package com.example.backend_nutripoint.services;

import java.time.Duration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder builder){
        this.chatClient = builder.build();
    }

    public Flux<String> preguntar(String mensaje){
        return chatClient
                .prompt()
                .system("Eres un experto en nutricion deportiva, especializado en suplementos. ")
                .user(mensaje)
                .stream()
                // .call()
                .content()
                .bufferTimeout(5, Duration.ofMillis(200))
                .map(list -> String.join("", list))
                .onErrorReturn("Error al procesar la solicitud");
    }

}
