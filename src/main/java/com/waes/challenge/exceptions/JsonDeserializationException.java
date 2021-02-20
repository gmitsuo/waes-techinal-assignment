package com.waes.challenge.exceptions;

public class JsonDeserializationException extends RuntimeException {

    public JsonDeserializationException() {
        super("Unable to deserialize JSON content");
    }
}
