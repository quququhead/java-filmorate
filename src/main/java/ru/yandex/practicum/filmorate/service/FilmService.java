package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    public Collection<Film> findAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film findFilm(Long id) {
        return receiveFilm(id);
    }

    public Collection<Film> findTopFilms(Integer count) {
        return new TreeSet<>(filmStorage.getAllFilms()).stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film create(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film setLike(Long id, Long userId) {
        User user = userService.receiveUser(userId);
        Film film = receiveFilm(id);
        film.addLike(userId);
        return film;
    }

    public Film update(Film newFilm) {
        Film oldFilm = receiveFilm(newFilm.getId());
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        return oldFilm;
    }

    public Film deleteLike(Long id, Long userId) {
        User user = userService.receiveUser(userId);
        Film film = receiveFilm(id);
        film.deleteLike(id);
        return film;
    }

    Film receiveFilm(Long id) {
        return filmStorage.getFilm(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id + " + id + " не найден"));
    }
}
