package org.pictet.adventure_book.controller;

import org.junit.jupiter.api.Test;
import org.pictet.adventure_book.domain.Book;
import org.pictet.adventure_book.domain.Difficulty;
import org.pictet.adventure_book.dto.BookSummaryDto;
import org.pictet.adventure_book.exception.BookNotFoundException;
import org.pictet.adventure_book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    void getAllBooksReturnsSummariesFromService() throws Exception {
        given(bookService.getAllBooks()).willReturn(List.of(
                new BookSummaryDto("crystal-caverns", "The Crystal Caverns", "Evelyn Stormrider", Difficulty.EASY, List.of("Fantasy"))
        ));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("crystal-caverns"))
                .andExpect(jsonPath("$[0].title").value("The Crystal Caverns"))
                .andExpect(jsonPath("$[0].difficulty").value("EASY"));
    }

    @Test
    void getBookReturnsBookFromService() throws Exception {
        Book book = new Book("the-prisoner", "The Prisoner", "Daniel El Fuego", Difficulty.HARD, List.of("High Fantasy"), List.of());
        given(bookService.getBook("the-prisoner")).willReturn(book);

        mockMvc.perform(get("/api/books/the-prisoner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Prisoner"))
                .andExpect(jsonPath("$.author").value("Daniel El Fuego"));
    }

    @Test
    void getBookReturns404WhenNotFound() throws Exception {
        given(bookService.getBook("unknown")).willThrow(new BookNotFoundException("unknown"));

        mockMvc.perform(get("/api/books/unknown"))
                .andExpect(status().isNotFound());
    }
}
