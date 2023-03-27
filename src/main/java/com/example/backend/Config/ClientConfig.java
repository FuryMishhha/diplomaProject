package com.example.backend.Config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

@Component
public class ClientConfig {
    public static final int TIMEOUT = 1000;
    private static final String BASE_URL1 = "http://localhost:8081";
    private static final String BASE_URL2 = "http://localhost:8082";
    private static final String BASE_URL3 = "http://localhost:8083";

    public static WebClient webClientWithTimeout(int type) {
        String url;

        switch (type) {
            case 1:
                url = BASE_URL1;
                break;
            case 2:
                url = BASE_URL2;
                break;
            case 3:
                url = BASE_URL3;
                break;
            default:
                throw new IllegalArgumentException("Type " + type + " unresolved");
        }

        final var tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                });

        return WebClient.builder()
                .baseUrl(url)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build();
    }
}
