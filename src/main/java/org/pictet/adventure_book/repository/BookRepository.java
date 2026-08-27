package org.pictet.adventure_book.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pictet.adventure_book.domain.Book;
import org.pictet.adventure_book.domain.BookFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Repository
public class BookRepository {

    private static final Logger log = LoggerFactory.getLogger(BookRepository.class);
    private static final String BOOKS_LOCATION_PATTERN = "classpath:books/*.json";

    private final Map<String, Book> booksById;

    public BookRepository(ObjectMapper objectMapper) {
        this.booksById = loadBooks(objectMapper, new PathMatchingResourcePatternResolver());
    }

    public List<Book> findAll() {
        return booksById.values().stream()
                .sorted(Comparator.comparing(Book::title))
                .toList();
    }

    public Optional<Book> findById(String id) {
        return Optional.ofNullable(booksById.get(id));
    }

    static Map<String, Book> loadBooks(ObjectMapper objectMapper, ResourcePatternResolver resourcePatternResolver) {
        try {
            Resource[] resources = resourcePatternResolver.getResources(BOOKS_LOCATION_PATTERN);

            return Arrays.stream(resources)
                    .map(resource -> toBook(objectMapper, resource))
                    .flatMap(Optional::stream)
                    .collect(Collectors.toMap(Book::id, book -> book));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to scan book resources", e);
        }
    }

    private static Optional<Book> toBook(ObjectMapper objectMapper, Resource resource) {
        String filename = requireNonNull(resource.getFilename());
        try {
            BookFile bookFile = objectMapper.readValue(resource.getInputStream(), BookFile.class);
            String id = idFromFilename(filename);
            return Optional.of(new Book(id, bookFile.title(), bookFile.author(), bookFile.difficulty(), bookFile.genres(), bookFile.sections()));
        } catch (IOException e) {
            log.warn("Skipping unreadable book file '{}': {}", filename, e.getMessage());
            return Optional.empty();
        }
    }

    private static String idFromFilename(String filename) {
        return filename.replaceFirst("\\.json$", "");
    }
}
