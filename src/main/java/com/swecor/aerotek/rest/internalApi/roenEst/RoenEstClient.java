package com.swecor.aerotek.rest.internalApi.roenEst;

import com.swecor.aerotek.model.selection.integration.heaterExchanger.RoenEstRequest;
import com.swecor.aerotek.model.selection.integration.heaterExchanger.RoenEstResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RoenEstClient {

    private final WebClient webClient;

    public RoenEstClient(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("http://localhost:1211").build();
    }

    public RoenEstResponse getHeatExchanger(RoenEstRequest request) {
            return webClient
                    .post()
                    .uri("/calculate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(RoenEstResponse.class)
                    .block();
    }
}
