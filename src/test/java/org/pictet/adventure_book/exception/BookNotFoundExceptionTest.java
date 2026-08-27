package org.pictet.adventure_book.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.assertj.core.api.Assertions.assertThat;

class BookNotFoundExceptionTest {

    @Test
    void messageIncludesTheRequestedId() {
        BookNotFoundException exception = new BookNotFoundException("unknown-book");

        assertThat(exception.getMessage()).contains("unknown-book");
    }

    @Test
    void isAnnotatedAsNotFound() {
        ResponseStatus responseStatus = BookNotFoundException.class.getAnnotation(ResponseStatus.class);

        assertThat(responseStatus).isNotNull();
        assertThat(responseStatus.value()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
