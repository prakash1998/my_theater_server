package com.pra.theater.websocket;

import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;
import reactor.util.context.Context;

import java.util.HashMap;
import java.util.Map;

public class RoomSubscriber implements CoreSubscriber<RoomMessage> {

    private static Map<String, Theater> roomIdRoomMap = new HashMap<>();

    private Theater room;
    private UnicastProcessor<RoomMessage> publisher;

    public Theater getRoom(){
        return this.room;
    }


    public RoomSubscriber(String roomId){
        this.room = roomIdRoomMap.computeIfAbsent(roomId,(key) -> {
            UnicastProcessor<RoomMessage> pub = UnicastProcessor.create();
            Flux<String> sub = pub
                    .replay(25)
                    .autoConnect()
                    .share()
                    .map(RoomMessage::toJSON);
            return new Theater(key,pub,sub);
        });
        this.publisher = this.room.getPublisher();

        System.out.println("connected to .. "+room.getRoomId());
        this.room.addMember();
    }

    @Override
    public Context currentContext() {
        return Context.empty();
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        System.out.println("connected to .. "+room.getRoomId());
        this.room.addMember();
    }

    @Override
    public void onNext(RoomMessage message) {
        System.out.println("got message .. "+message);
        this.publisher.onNext(message);
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("got error .. "+t);
        this.publisher.onError(t);
    }

    @Override
    public void onComplete() {
        System.out.println("disconnected from .. "+this.room.getRoomId());
        this.room.removeMember();
        if(this.room.getMemberCount() <= 0){
            System.out.println("cleaning empty room...");
            roomIdRoomMap.remove(this.room.getRoomId());
        }
    }
}
