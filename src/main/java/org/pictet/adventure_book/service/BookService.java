package org.pictet.adventure_book.service;

import org.pictet.adventure_book.domain.Book;
import org.pictet.adventure_book.dto.BookSummaryDto;
import org.pictet.adventure_book.exception.BookNotFoundException;
import org.pictet.adventure_book.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookSummaryDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookSummaryDto::from)
                .toList();
    }

    public Book getBook(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }
}
