package com.swecor.aerotek.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfiguration {

    public static final int TIMEOUT = 10000;

    @Bean
    public WebClient.Builder webClientWithTimeout() {
        final var tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                });

        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder().codecs(this::acceptedCodecs).build())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)));
    }

    private void acceptedCodecs(ClientCodecConfigurer clientCodecConfigurer) {
        clientCodecConfigurer.customCodecs().register(new Jackson2JsonEncoder(new ObjectMapper(), MediaType.TEXT_HTML));
        clientCodecConfigurer.customCodecs().register(new Jackson2JsonDecoder(new ObjectMapper(), MediaType.TEXT_HTML));
    }
}
