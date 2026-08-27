package org.pictet.adventure_book.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookFile(String title, String author, Difficulty difficulty, List<String> genres, List<Section> sections) {

    public BookFile {
        genres = genres == null ? List.of() : List.copyOf(genres);
        sections = sections == null ? List.of() : List.copyOf(sections);
    }
}
