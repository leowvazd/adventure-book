package org.pictet.adventure_book.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookFileTest {

    @Test
    void nullGenresAndSectionsBecomeEmptyLists() {
        BookFile bookFile = new BookFile("Title", "Author", Difficulty.EASY, null, null);

        assertThat(bookFile.genres()).isEmpty();
        assertThat(bookFile.sections()).isEmpty();
    }

    @Test
    void nonNullGenresAndSectionsArePreserved() {
        List<String> genres = List.of("Fantasy");
        List<Section> sections = List.of(new Section(1, "text", SectionType.BEGIN, List.of()));

        BookFile bookFile = new BookFile("Title", "Author", Difficulty.EASY, genres, sections);

        assertThat(bookFile.genres()).isEqualTo(genres);
        assertThat(bookFile.sections()).isEqualTo(sections);
    }
}
