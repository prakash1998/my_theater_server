package com.pra.theater.websocket;

import lombok.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Theater {

    @NonNull
    private String roomId;
    @NonNull
    private UnicastProcessor<RoomMessage> publisher;
    @NonNull
    private Flux<String> subscription;

    private int memberCount = 0;

    public void addMember(){
        memberCount++;
    }

    public void removeMember(){
        memberCount--;
    }

}
