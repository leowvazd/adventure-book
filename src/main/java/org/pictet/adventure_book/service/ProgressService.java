package org.pictet.adventure_book.service;

import org.pictet.adventure_book.domain.Book;
import org.pictet.adventure_book.dto.ProgressDto;
import org.pictet.adventure_book.dto.SaveProgressRequest;
import org.pictet.adventure_book.entity.SavedProgress;
import org.pictet.adventure_book.repository.SavedProgressRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

@Service
public class ProgressService {

    private static final int MAX_HEALTH = 10;

    private final SavedProgressRepository savedProgressRepository;
    private final BookService bookService;

    public ProgressService(SavedProgressRepository savedProgressRepository, BookService bookService) {
        this.savedProgressRepository = savedProgressRepository;
        this.bookService = bookService;
    }

    public ProgressDto save(String bookId, SaveProgressRequest request) {
        Book book = bookService.getBook(bookId);

        boolean sectionExists = book.sections().stream()
                .anyMatch(section -> section.id() == request.currentSectionId());
        if (!sectionExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Book '" + bookId + "' has no section " + request.currentSectionId());
        }
        if (request.health() < 0 || request.health() > MAX_HEALTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "health must be between 0 and " + MAX_HEALTH);
        }

        Instant now = Instant.now();
        SavedProgress progress = savedProgressRepository.findById(bookId)
                .map(existing -> {
                    existing.update(request.currentSectionId(), request.health(), now);
                    return existing;
                })
                .orElseGet(() -> new SavedProgress(bookId, request.currentSectionId(), request.health(), now));

        return ProgressDto.from(savedProgressRepository.save(progress));
    }

    public Optional<ProgressDto> find(String bookId) {
        return savedProgressRepository.findById(bookId).map(ProgressDto::from);
    }

    public void clear(String bookId) {
        savedProgressRepository.findById(bookId).ifPresent(savedProgressRepository::delete);
    }
}
