package org.pictet.adventure_book.repository;

import org.pictet.adventure_book.entity.SavedProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedProgressRepository extends JpaRepository<SavedProgress, String> {
}
