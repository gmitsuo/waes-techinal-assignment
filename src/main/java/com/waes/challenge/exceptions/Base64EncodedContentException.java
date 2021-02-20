package com.waes.challenge.exceptions;

public class Base64EncodedContentException extends RuntimeException {

    public Base64EncodedContentException() {
        super("Content data is not a valid Base64 encoded value");
    }
}
