package com.waes.challenge.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.challenge.controllers.models.ErrorResponse;
import com.waes.challenge.domain.Diff;
import com.waes.challenge.domain.DiffContent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;
import java.util.List;

import static com.waes.challenge.domain.DiffComparison.NOT_EQUALS;
import static com.waes.challenge.domain.DiffSide.LEFT;
import static com.waes.challenge.domain.DiffSide.RIGHT;
import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class DiffControllerIntegrationTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void shouldReturnBadRequestWhenContentIsNotBase64() throws Exception {

        var id = 1;
        var diffSide = LEFT.toString().toLowerCase();
        var content = "Not base64 encoded content".getBytes();
        var exceptionMessage = "Content data is not a valid Base64 encoded value";

        mvc.perform(post(format("/v1/diff/%d/%s", id, diffSide))
        .contentType(MediaType.TEXT_PLAIN)
        .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(mapper.writeValueAsString(new ErrorResponse(exceptionMessage))));
    }

    @Test
    void shouldReturnBadRequestWhenContentIsNotJson() throws Exception {

        var id = 1;
        var diffSide = LEFT.toString().toLowerCase();
        var content = Base64.getEncoder().encode("Not base64 encoded content".getBytes());
        var exceptionMessage = "Unable to deserialize JSON content";

        mvc.perform(post(format("/v1/diff/%d/%s", id, diffSide))
        .contentType(MediaType.TEXT_PLAIN)
        .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(mapper.writeValueAsString(new ErrorResponse(exceptionMessage))));
    }

    @Test
    void shouldReturnCreatedWhenContentIsBase64EncodedJson() throws Exception {

        var id = 1;
        var diffSide = LEFT.toString().toLowerCase();
        var content = Base64.getEncoder().encode("{ \"property\": \"propertyValue\"}".getBytes());

        mvc.perform(post(format("/v1/diff/%d/%s", id, diffSide))
        .contentType(MediaType.TEXT_PLAIN)
        .content(content))
        .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn404IfDiffNotFound() throws Exception {

        mvc.perform(get("/v1/diff/1"))
        .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnDiffContentWhenDiffIDExists() throws Exception {

        var id = 1;
        var diffSideLeft = LEFT.toString().toLowerCase();
        var contentLeft = Base64.getEncoder().encode("{ \"property\": \"AABBCC\"}".getBytes());

        mvc.perform(post(format("/v1/diff/%d/%s", id, diffSideLeft))
        .contentType(MediaType.TEXT_PLAIN)
        .content(contentLeft))
        .andExpect(status().isCreated());

        var diffSideRight = RIGHT.toString().toLowerCase();
        var contentRight = Base64.getEncoder().encode("{ \"property\": \"AADDCC\"}".getBytes());

        mvc.perform(post(format("/v1/diff/%d/%s", id, diffSideRight))
        .contentType(MediaType.TEXT_PLAIN)
        .content(contentRight))
        .andExpect(status().isCreated());

        var diffContent = new DiffContent(NOT_EQUALS, List.of(new Diff(23, 3)));

        mvc.perform(get(format("/v1/diff/%d", id)))
        .andExpect(status().isOk())
        .andExpect(content().string(mapper.writeValueAsString(diffContent)));
    }
}
