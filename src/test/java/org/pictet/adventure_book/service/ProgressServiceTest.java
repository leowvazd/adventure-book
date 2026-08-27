package org.pictet.adventure_book.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pictet.adventure_book.domain.Book;
import org.pictet.adventure_book.domain.Difficulty;
import org.pictet.adventure_book.domain.Section;
import org.pictet.adventure_book.domain.SectionType;
import org.pictet.adventure_book.dto.ProgressDto;
import org.pictet.adventure_book.dto.SaveProgressRequest;
import org.pictet.adventure_book.entity.SavedProgress;
import org.pictet.adventure_book.repository.SavedProgressRepository;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProgressServiceTest {

    @Mock
    private SavedProgressRepository savedProgressRepository;

    @Mock
    private BookService bookService;

    @InjectMocks
    private ProgressService progressService;

    private static Book bookWithSection(int sectionId) {
        return new Book("the-prisoner", "The Prisoner", "Daniel El Fuego", Difficulty.HARD, List.of(),
                List.of(new Section(sectionId, "text", SectionType.BEGIN, List.of())));
    }

    @Test
    void saveCreatesNewProgressWhenNoneExists() {
        given(bookService.getBook("the-prisoner")).willReturn(bookWithSection(1));
        given(savedProgressRepository.findById("the-prisoner")).willReturn(Optional.empty());
        given(savedProgressRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        ProgressDto result = progressService.save("the-prisoner", new SaveProgressRequest(1, 8));

        assertThat(result.currentSectionId()).isEqualTo(1);
        assertThat(result.health()).isEqualTo(8);
    }

    @Test
    void saveUpdatesExistingProgressEntity() {
        SavedProgress existing = new SavedProgress("the-prisoner", 1, 10, Instant.now());
        given(bookService.getBook("the-prisoner")).willReturn(bookWithSection(1));
        given(savedProgressRepository.findById("the-prisoner")).willReturn(Optional.of(existing));
        given(savedProgressRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        ProgressDto result = progressService.save("the-prisoner", new SaveProgressRequest(1, 4));

        assertThat(result.currentSectionId()).isEqualTo(1);
        assertThat(result.health()).isEqualTo(4);
        verify(savedProgressRepository).save(existing);
    }

    @Test
    void saveRejectsUnknownSectionId() {
        given(bookService.getBook("the-prisoner")).willReturn(bookWithSection(1));

        assertThatThrownBy(() -> progressService.save("the-prisoner", new SaveProgressRequest(999, 5)))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void saveRejectsHealthAboveMax() {
        given(bookService.getBook("the-prisoner")).willReturn(bookWithSection(1));

        assertThatThrownBy(() -> progressService.save("the-prisoner", new SaveProgressRequest(1, 11)))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void saveRejectsNegativeHealth() {
        given(bookService.getBook("the-prisoner")).willReturn(bookWithSection(1));

        assertThatThrownBy(() -> progressService.save("the-prisoner", new SaveProgressRequest(1, -1)))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void findReturnsEmptyWhenNothingSaved() {
        given(savedProgressRepository.findById("the-prisoner")).willReturn(Optional.empty());

        assertThat(progressService.find("the-prisoner")).isEmpty();
    }

    @Test
    void findReturnsDtoWhenSaved() {
        SavedProgress saved = new SavedProgress("the-prisoner", 20, 7, Instant.now());
        given(savedProgressRepository.findById("the-prisoner")).willReturn(Optional.of(saved));

        assertThat(progressService.find("the-prisoner"))
                .contains(new ProgressDto(20, 7, saved.getUpdatedAt()));
    }

    @Test
    void clearDeletesExistingProgress() {
        SavedProgress saved = new SavedProgress("the-prisoner", 20, 7, Instant.now());
        given(savedProgressRepository.findById("the-prisoner")).willReturn(Optional.of(saved));

        progressService.clear("the-prisoner");

        verify(savedProgressRepository).delete(saved);
    }

    @Test
    void clearIsNoOpWhenNothingSaved() {
        given(savedProgressRepository.findById("the-prisoner")).willReturn(Optional.empty());

        progressService.clear("the-prisoner");

        verify(savedProgressRepository, never()).delete(any());
    }
}
