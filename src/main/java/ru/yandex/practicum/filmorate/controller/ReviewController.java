package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService service;

    @GetMapping
    public List<Review> findAllBy(@RequestParam(defaultValue = "0") long filmId, @RequestParam(defaultValue = "10") long count) {
        return service.findAllBy(filmId, count);
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable long id) {
        return service.getReviewById(id);
    }

    @PostMapping
    public Review create(@Valid @RequestBody Review review) {
        return service.create(review);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        return service.update(review);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        service.delete(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        service.removeLike(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislike(@PathVariable long id, @PathVariable long userId) {
        service.addDislike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void removeDislike(@PathVariable long id, @PathVariable long userId) {
        service.removeDislike(id, userId);
    }
}
