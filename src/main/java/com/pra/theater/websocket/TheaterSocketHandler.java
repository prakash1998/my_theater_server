package com.pra.theater.websocket;


import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TheaterSocketHandler implements WebSocketHandler {

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        Map<String, String> queryParams =
                Optional.ofNullable(session.getHandshakeInfo().getUri().getQuery())
                        .map(params->Stream.of(params.split("&"))
                                            .map(item -> item.split("="))
                                            .collect(Collectors.toMap(item -> item[0],item -> item.length > 1 ? item[1]: "global")))
                        .orElse(new HashMap<>());

        String roomId = queryParams.getOrDefault("room","global");

        var roomSubscriber = new RoomSubscriber(roomId);
        session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(RoomMessage::fromJSON)
                .subscribe(roomSubscriber::onNext,roomSubscriber::onError,roomSubscriber::onComplete);

        return session.send(roomSubscriber.getRoom().getSubscription().map(session::textMessage));
    }

}