package com.swecor.aerotek.rest.internalApi.roenEst;

import com.swecor.aerotek.model.selection.integration.klingenburg.KlingenburgRequest;
import com.swecor.aerotek.model.selection.integration.klingenburg.KlingenburgResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KlingenburgClient {

    private final WebClient webClient;

    public KlingenburgClient(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("http://localhost:1210").build();
    }

    public KlingenburgResponse getRotor(KlingenburgRequest request) {
            return webClient
                    .post()
                    .uri("/calculate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(KlingenburgResponse.class)
                    .block();
    }
}
