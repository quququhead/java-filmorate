package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Validated
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable long id) {
        return filmService.findFilm(id);
    }

    @GetMapping("/popular")
    public Collection<Film> findTopFilms(@RequestParam(defaultValue = "10") Integer count,
                                         @RequestParam(required = false) Integer genreId,
                                         @RequestParam(required = false) Integer year) {
        return filmService.findTopFilms(genreId, year, count);
    }

    @GetMapping("/director/{directorId}")
    public Collection<Film> findAllFilmsOfDirectorId(@PathVariable long directorId,
                                                     @RequestParam String sortBy) {
        return filmService.findAllFilmsOfDirectorId(directorId, sortBy);
    }

    @GetMapping("/search")
    public Collection<Film> findAllFilmsBySearch(@RequestParam String query,
                                                 @RequestParam List<String> by) {
        return filmService.findAllFilmsBySearch(query, by);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void setLike(@PathVariable long id, @PathVariable long userId) {
        filmService.setLike(id, userId);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        return filmService.updateFilm(newFilm);
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilm(@PathVariable long filmId) {
        filmService.deleteFilm(filmId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/common")
    public Collection<Film> getCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }
}
