package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.service.RatingMPAService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingMPAController {

    private final RatingMPAService ratingMPAService;

    @GetMapping
    public Collection<RatingMPA> findAllRatingMPAs() {
        return ratingMPAService.findAllRatingMPAs();
    }

    @GetMapping("/{id}")
    public RatingMPA findRatingMPA(@PathVariable long id) {
        return ratingMPAService.findRatingMPA(id);
    }
}
