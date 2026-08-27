package org.pictet.adventure_book.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Option(String description, int gotoId, Consequence consequence) {
}
