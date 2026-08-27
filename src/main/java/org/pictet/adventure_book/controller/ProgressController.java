package org.pictet.adventure_book.controller;

import org.pictet.adventure_book.dto.ProgressDto;
import org.pictet.adventure_book.dto.SaveProgressRequest;
import org.pictet.adventure_book.service.ProgressService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/books/{id}/progress")
@CrossOrigin(origins = "http://localhost:4200")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @PutMapping
    public ProgressDto save(@PathVariable String id, @RequestBody SaveProgressRequest request) {
        return progressService.save(id, request);
    }

    @GetMapping
    public Optional<ProgressDto> get(@PathVariable String id) {
        return progressService.find(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        progressService.clear(id);
    }
}
