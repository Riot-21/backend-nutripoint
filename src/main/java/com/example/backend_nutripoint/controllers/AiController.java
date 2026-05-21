package com.example.backend_nutripoint.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_nutripoint.services.AiService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class AiController {
    
    private final AiService aiService;

    public AiController(AiService aiService){
        this.aiService = aiService;
    }

    @GetMapping(value = "/preguntar", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    // @GetMapping("/preguntar")
    public Flux<String> preguntar(String mensaje){
        return aiService.preguntar(mensaje);
    }
}
