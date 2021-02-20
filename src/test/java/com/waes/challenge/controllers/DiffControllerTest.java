package com.waes.challenge.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.challenge.controllers.models.ErrorResponse;
import com.waes.challenge.domain.DiffContent;
import com.waes.challenge.services.DiffService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.waes.challenge.domain.DiffComparison.EQUALS;
import static com.waes.challenge.domain.DiffSide.LEFT;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiffController.class)
@ExtendWith(SpringExtension.class)
class DiffControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    DiffService diffService;

    @Test
    void shouldReturn404IfDiffNotFound() throws Exception {

        var id = 1;
        when(diffService.getDiffById(id))
        .thenReturn(Optional.empty());

        mvc.perform(get(format("/v1/diff/%d", id)))
        .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnDiffContentWhenFound() throws Exception {

        var id = 1;
        var diffContent = new DiffContent(EQUALS, emptyList());

        when(diffService.getDiffById(id))
        .thenReturn(Optional.of(diffContent));

        mvc.perform(get(format("/v1/diff/%d", id)))
        .andExpect(status().isOk())
        .andExpect(content().string(mapper.writeValueAsString(diffContent)));
    }

    @Test
    void shouldReturnExceptionMessageWhenFailingToCreateDiff() throws Exception {

        var id = 1;
        var diffSide = LEFT;
        var content = "content".getBytes();
        var exceptionMessage = "Exception Message";

        when(diffService.createDiff(id, diffSide, content))
        .thenThrow(new RuntimeException(exceptionMessage));

        mvc.perform(post(format("/v1/diff/%d/%s", id, diffSide))
        .contentType(MediaType.TEXT_PLAIN)
        .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(mapper.writeValueAsString(new ErrorResponse(exceptionMessage))));
    }

    @Test
    void shouldReturnCreatedStatusWhenSuccessfullyCreatingDiff() throws Exception {

        var id = 1;
        var content = "content".getBytes();

        mvc.perform(post(format("/v1/diff/%d/%s", id, LEFT))
        .contentType(MediaType.TEXT_PLAIN)
        .content(content))
        .andExpect(status().isCreated());
    }

    @Test
    void shouldAcceptLowerCaseDiffSide() throws Exception {
        var id = 1;
        var content = "content".getBytes();
        var diffSide = LEFT.toString().toLowerCase();

        mvc.perform(post(format("/v1/diff/%d/%s", id, diffSide))
        .contentType(MediaType.TEXT_PLAIN)
        .content(content))
        .andExpect(status().isCreated());
    }
}