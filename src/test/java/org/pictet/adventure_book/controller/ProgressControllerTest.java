package org.pictet.adventure_book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.pictet.adventure_book.dto.ProgressDto;
import org.pictet.adventure_book.dto.SaveProgressRequest;
import org.pictet.adventure_book.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProgressController.class)
class ProgressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProgressService progressService;

    @Test
    void saveReturnsSavedProgress() throws Exception {
        ProgressDto dto = new ProgressDto(20, 7, Instant.parse("2026-01-01T00:00:00Z"));
        given(progressService.save(eq("the-prisoner"), any())).willReturn(dto);

        mockMvc.perform(put("/api/books/the-prisoner/progress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SaveProgressRequest(20, 7))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentSectionId").value(20))
                .andExpect(jsonPath("$.health").value(7));
    }

    @Test
    void saveReturns400WhenServiceRejectsRequest() throws Exception {
        given(progressService.save(eq("the-prisoner"), any()))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "health must be between 0 and 10"));

        mockMvc.perform(put("/api/books/the-prisoner/progress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SaveProgressRequest(20, 99))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getReturnsProgressWhenSaved() throws Exception {
        ProgressDto dto = new ProgressDto(20, 7, Instant.parse("2026-01-01T00:00:00Z"));
        given(progressService.find("the-prisoner")).willReturn(Optional.of(dto));

        mockMvc.perform(get("/api/books/the-prisoner/progress"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentSectionId").value(20));
    }

    @Test
    void getReturns200WithNullBodyWhenNothingSaved() throws Exception {
        given(progressService.find("the-prisoner")).willReturn(Optional.empty());

        mockMvc.perform(get("/api/books/the-prisoner/progress"))
                .andExpect(status().isOk())
                .andExpect(content().string("null"));
    }

    @Test
    void deleteReturns204AndClearsProgress() throws Exception {
        mockMvc.perform(delete("/api/books/the-prisoner/progress"))
                .andExpect(status().isNoContent());

        verify(progressService).clear("the-prisoner");
    }
}
