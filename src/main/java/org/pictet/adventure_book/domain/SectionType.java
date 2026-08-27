package org.pictet.adventure_book.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SectionType {
    BEGIN,
    NODE,
    END,
    GAME_OVER;

    @JsonCreator
    public static SectionType fromJson(String value) {
        if ("BEGIN".equalsIgnoreCase(value)) {
            return BEGIN;
        }
        if ("END".equalsIgnoreCase(value)) {
            return END;
        }
        if ("GAME_OVER".equalsIgnoreCase(value)) {
            return GAME_OVER;
        }
        return NODE;
    }
}
