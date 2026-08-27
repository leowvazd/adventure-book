package org.pictet.adventure_book.repository;

import org.junit.jupiter.api.Test;
import org.pictet.adventure_book.entity.SavedProgress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SavedProgressRepositoryTest {

    @Autowired
    private SavedProgressRepository savedProgressRepository;

    @Test
    void savesAndFindsProgressByBookId() {
        savedProgressRepository.save(new SavedProgress("the-prisoner", 20, 7, Instant.now()));

        Optional<SavedProgress> found = savedProgressRepository.findById("the-prisoner");

        assertThat(found).isPresent();
        assertThat(found.get().getCurrentSectionId()).isEqualTo(20);
        assertThat(found.get().getHealth()).isEqualTo(7);
    }

    @Test
    void findByIdReturnsEmptyWhenNothingSaved() {
        assertThat(savedProgressRepository.findById("unknown-book")).isEmpty();
    }

    @Test
    void savingWithSameIdOverwritesPreviousProgress() {
        savedProgressRepository.save(new SavedProgress("crystal-caverns", 1, 10, Instant.now()));
        savedProgressRepository.save(new SavedProgress("crystal-caverns", 900, 4, Instant.now()));

        Optional<SavedProgress> found = savedProgressRepository.findById("crystal-caverns");

        assertThat(found).isPresent();
        assertThat(found.get().getCurrentSectionId()).isEqualTo(900);
        assertThat(found.get().getHealth()).isEqualTo(4);
    }

    @Test
    void deletesProgressById() {
        savedProgressRepository.save(new SavedProgress("crystal-caverns", 1, 10, Instant.now()));

        savedProgressRepository.deleteById("crystal-caverns");

        assertThat(savedProgressRepository.findById("crystal-caverns")).isEmpty();
    }
}
