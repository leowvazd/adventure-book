package org.pictet.adventure_book.domain;

import java.util.List;

public record Book(String id, String title, String author, Difficulty difficulty, List<String> genres, List<Section> sections) {
}
