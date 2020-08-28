package com.pra.theater.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

    private static ObjectMapper mapper = new ObjectMapper();

    public static ObjectMapper getJsonMapper() {
        return mapper;
    }

}
