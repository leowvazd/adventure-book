package org.pictet.adventure_book.dto;

import org.pictet.adventure_book.entity.SavedProgress;

import java.time.Instant;

public record ProgressDto(int currentSectionId, int health, Instant updatedAt) {

    public static ProgressDto from(SavedProgress progress) {
        return new ProgressDto(progress.getCurrentSectionId(), progress.getHealth(), progress.getUpdatedAt());
    }
}
