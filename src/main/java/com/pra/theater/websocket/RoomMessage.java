package com.pra.theater.websocket;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomMessage {

    private boolean playing;
    private double seek;

    public static RoomMessage fromJSON(String json) {
        try {
            return Utils.getJsonMapper().readValue(json, RoomMessage.class);
        } catch (IOException e) {
            throw new RuntimeException("Invalid JSON:" + json, e);
        }
    }

    public String toJSON() {
        try {
            return Utils.getJsonMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}