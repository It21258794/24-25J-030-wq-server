package com.waterboard.waterqualityprediction.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class ChemicalController {

    private final WebClient webClient = WebClient.create("http://localhost:5000");
    private final StandardServletMultipartResolver standardServletMultipartResolver;

    public ChemicalController(StandardServletMultipartResolver standardServletMultipartResolver) {
        this.standardServletMultipartResolver = standardServletMultipartResolver;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/predict")
    public Mono<ResponseEntity<String>> getPrediction() {
        System.out.println("getPrediction");
        // Step 1: Fetch data from Flask `/dailyusage`
        return webClient.get()
                .uri("/dailyusage")
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> ResponseEntity.ok().body(response));
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/future-predict")
    public Mono<ResponseEntity<String>> postFuturePrediction(@RequestBody Map<String, Object> requestData) {
        return webClient.post()
                .uri("/predict/future")  // Flask API endpoint
                .bodyValue(requestData)  // Send the received JSON data
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> ResponseEntity.ok().body(response));
    }

//    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/lastdaysusages")
    public Mono<ResponseEntity<String>> getLastUsage() {
        // Step 1: Fetch data from Flask `/dailyusage`
        return webClient.get()
                .uri("/lastusage")
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> ResponseEntity.ok().body(response));
    }
}
