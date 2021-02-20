package com.waes.challenge.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.challenge.exceptions.Base64EncodedContentException;
import com.waes.challenge.exceptions.JsonDeserializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class DiffContentValidatorTest {

    private DiffContentValidator diffContentValidator;

    @BeforeEach
    void setup() {
        diffContentValidator = new DiffContentValidator(new ObjectMapper());
    }

    @Test
    void shouldThrowBase64EncodedContentExceptionIfContentIsNotBase64Encoded() {

        var content = "Not base64 encoded content";

        assertThatExceptionOfType(Base64EncodedContentException.class)
        .isThrownBy(() -> diffContentValidator.validate(content.getBytes()))
        .withMessage("Content data is not a valid Base64 encoded value");
    }

    @Test
    void shouldJsonDeserializationExceptionIfContentIsNotParsable() {

        var base64Encoder = Base64.getEncoder();
        var content = "Not parsable base64 encoded json";
        var encodedContent = base64Encoder.encode(content.getBytes());

        assertThatExceptionOfType(JsonDeserializationException.class)
        .isThrownBy(() -> diffContentValidator.validate(encodedContent))
        .withMessage("Unable to deserialize JSON content");
    }

    @Test
    void shouldThrowRuntimeExceptionWhenTheresIOException() throws IOException {

        var objectMapper = mock(ObjectMapper.class);
        diffContentValidator = new DiffContentValidator(objectMapper);

        when(objectMapper.readTree(any(byte[].class)))
        .thenThrow(new IOException());

        var base64Encoder = Base64.getEncoder();
        var content = "{}";
        var encodedContent = base64Encoder.encode(content.getBytes());

        assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> diffContentValidator.validate(encodedContent))
        .withMessage("Failed to read request content");
    }

    @Test
    void shouldReturnContentBackWhenSuccessfullyValidated() {

        var base64Encoder = Base64.getEncoder();
        var contentBytes = "{ \"property\": \"propertyValue\"}".getBytes();
        var encodedContent = base64Encoder.encode(contentBytes);

        assertThat(diffContentValidator.validate(encodedContent))
        .isEqualTo(encodedContent);
    }

}