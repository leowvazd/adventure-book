package org.pictet.adventure_book.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pictet.adventure_book.domain.Book;
import org.pictet.adventure_book.domain.Difficulty;
import org.pictet.adventure_book.dto.BookSummaryDto;
import org.pictet.adventure_book.exception.BookNotFoundException;
import org.pictet.adventure_book.repository.BookRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private static Book book(String id, String title) {
        return new Book(id, title, "Author", Difficulty.EASY, List.of("Fantasy"), List.of());
    }

    @Test
    void getAllBooksMapsRepositoryBooksToSummaries() {
        given(bookRepository.findAll()).willReturn(List.of(book("crystal-caverns", "The Crystal Caverns")));

        List<BookSummaryDto> summaries = bookService.getAllBooks();

        assertThat(summaries).containsExactly(
                new BookSummaryDto("crystal-caverns", "The Crystal Caverns", "Author", Difficulty.EASY, List.of("Fantasy"))
        );
    }

    @Test
    void getAllBooksReturnsEmptyListWhenRepositoryIsEmpty() {
        given(bookRepository.findAll()).willReturn(List.of());

        assertThat(bookService.getAllBooks()).isEmpty();
    }

    @Test
    void getBookReturnsBookWhenFound() {
        Book book = book("the-prisoner", "The Prisoner");
        given(bookRepository.findById("the-prisoner")).willReturn(Optional.of(book));

        assertThat(bookService.getBook("the-prisoner")).isEqualTo(book);
    }

    @Test
    void getBookThrowsWhenNotFound() {
        given(bookRepository.findById("unknown")).willReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBook("unknown"))
                .isInstanceOf(BookNotFoundException.class);
    }
}
