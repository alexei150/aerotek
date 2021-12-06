package com.swecor.aerotek.rest.externalApi.ziehlAbegg;

import com.swecor.aerotek.model.selection.fanSelect.ZiehlAbeggFansRequest;
import com.swecor.aerotek.model.selection.fanSelect.ZiehlAbeggFansResponse;
import com.swecor.aerotek.model.selection.fanSelect.ZiehlAbeggSessionIdRequest;
import com.swecor.aerotek.model.selection.fanSelect.ZiehlAbeggSessionIdResponse;
import com.swecor.aerotek.rest.exceptions.selection.ZiehlAbeggSessionIdException;
import com.swecor.aerotek.rest.exceptions.selection.ZiehlAbeggSessionIdNullException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ZiehlAbeggClient {

    private final WebClient webClient;

    public ZiehlAbeggClient(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("http://fanselect.net:8079").build();
    }

    public String getSessionId(ZiehlAbeggSessionIdRequest loginRequest) {
        try {
            return webClient
                    .post()
                    .uri("/FSWebService")
                    .bodyValue(loginRequest)
                    .retrieve()
                    .bodyToMono(ZiehlAbeggSessionIdResponse.class)
                    .block().getSessionId();
        } catch (NullPointerException e){
            throw new ZiehlAbeggSessionIdNullException();
        } catch (Exception e){
            throw new ZiehlAbeggSessionIdException();
        }
    }

    public List<ZiehlAbeggFansResponse> getFans(ZiehlAbeggFansRequest ziehlAbeggRequest) {

        ziehlAbeggRequest.setSessionId(getSessionId(new ZiehlAbeggSessionIdRequest()));

        return webClient
                .post()
                .uri("/FSWebService")
                .bodyValue(ziehlAbeggRequest)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ZiehlAbeggFansResponse>>() {
                })
                .block();
    }
}
