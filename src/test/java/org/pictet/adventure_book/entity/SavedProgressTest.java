package org.pictet.adventure_book.entity;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class SavedProgressTest {

    @Test
    void storesConstructorValues() {
        Instant now = Instant.now();
        SavedProgress progress = new SavedProgress("the-prisoner", 20, 7, now);

        assertThat(progress.getBookId()).isEqualTo("the-prisoner");
        assertThat(progress.getCurrentSectionId()).isEqualTo(20);
        assertThat(progress.getHealth()).isEqualTo(7);
        assertThat(progress.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void updateOverwritesSectionHealthAndTimestamp() {
        SavedProgress progress = new SavedProgress("the-prisoner", 1, 10, Instant.EPOCH);
        Instant now = Instant.now();

        progress.update(20, 7, now);

        assertThat(progress.getCurrentSectionId()).isEqualTo(20);
        assertThat(progress.getHealth()).isEqualTo(7);
        assertThat(progress.getUpdatedAt()).isEqualTo(now);
    }
}
