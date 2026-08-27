package org.pictet.adventure_book.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
public class SavedProgress {

    @Id
    private String bookId;

    private int currentSectionId;
    private int health;
    private Instant updatedAt;

    protected SavedProgress() {

    }

    public SavedProgress(String bookId, int currentSectionId, int health, Instant updatedAt) {
        this.bookId = bookId;
        this.currentSectionId = currentSectionId;
        this.health = health;
        this.updatedAt = updatedAt;
    }

    public void update(int currentSectionId, int health, Instant updatedAt) {
        this.currentSectionId = currentSectionId;
        this.health = health;
        this.updatedAt = updatedAt;
    }

    public String getBookId() {
        return bookId;
    }

    public int getCurrentSectionId() {
        return currentSectionId;
    }

    public int getHealth() {
        return health;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
