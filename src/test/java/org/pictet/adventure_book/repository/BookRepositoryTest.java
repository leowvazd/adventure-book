package org.pictet.adventure_book.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pictet.adventure_book.domain.Book;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class BookRepositoryTest {

    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository = new BookRepository(new ObjectMapper());
    }

    @Test
    void findAllReturnsParseableBooksSortedByTitle() {
        List<Book> books = bookRepository.findAll();

        assertThat(books).extracting(Book::title).containsExactly(
                "Pirates of the Jade Sea",
                "The Crystal Caverns",
                "The Prisoner"
        );
    }

    @Test
    void findByIdReturnsMatchingBook() {
        Optional<Book> book = bookRepository.findById("the-prisoner");

        assertThat(book).isPresent();
        assertThat(book.get().title()).isEqualTo("The Prisoner");
    }

    @Test
    void findByIdReturnsEmptyForUnknownId() {
        assertThat(bookRepository.findById("does-not-exist")).isEmpty();
    }

    @Test
    void skipsUnparseableBookFile() {
        assertThat(bookRepository.findById("dragon-quest")).isEmpty();
    }

    @Test
    void wrapsResourceScanningFailureAsUnchecked() throws IOException {
        ResourcePatternResolver resourcePatternResolver = mock(ResourcePatternResolver.class);
        given(resourcePatternResolver.getResources(anyString())).willThrow(new IOException("boom"));

        assertThatThrownBy(() -> BookRepository.loadBooks(new ObjectMapper(), resourcePatternResolver))
                .isInstanceOf(UncheckedIOException.class)
                .hasCauseInstanceOf(IOException.class);
    }
}
