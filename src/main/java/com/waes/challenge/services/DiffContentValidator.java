package com.waes.challenge.services;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.challenge.exceptions.Base64EncodedContentException;
import com.waes.challenge.exceptions.JsonDeserializationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
public class DiffContentValidator {

    private final ObjectMapper objectMapper;

    public DiffContentValidator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public byte[] validate(byte[] content) {

        try {
            var decoded = Base64.getDecoder().decode(content);
            objectMapper.readTree(decoded);
            return content;
        }
        catch (JsonParseException e) {
            throw new JsonDeserializationException();
        }
        catch (IllegalArgumentException e) {
            throw new Base64EncodedContentException();
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to read request content");
        }
    }
}
