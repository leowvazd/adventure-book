package org.pictet.adventure_book.dto;

import org.pictet.adventure_book.domain.Book;
import org.pictet.adventure_book.domain.Difficulty;

import java.util.List;

public record BookSummaryDto(String id, String title, String author, Difficulty difficulty, List<String> genres) {

    public static BookSummaryDto from(Book book) {
        return new BookSummaryDto(book.id(), book.title(), book.author(), book.difficulty(), book.genres());
    }
}
