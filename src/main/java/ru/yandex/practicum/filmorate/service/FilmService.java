package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.time.Month.DECEMBER;

@Service
@RequiredArgsConstructor
public class FilmService {

    private static final LocalDate FIRST_RELEASE_DATE = LocalDate.of(1895, DECEMBER, 28);

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> findAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film findFilm(long filmId) {
        Film film = filmStorage.getFilm(filmId);
        checkFilmNotNull(film);
        return film;
    }

    public Collection<Film> findTopFilms(Integer count) {
        return new TreeSet<>(filmStorage.getAllFilms()).stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film createFilm(Film film) {
        validate(film);
        return filmStorage.addFilm(film);
    }

    public void setLike(long id, long userId) {
        userStorage.getUser(userId);
        filmStorage.setLike(id, userId);
    }

    public Film updateFilm(Film newFilm) {
        validate(newFilm);
        checkFilmNotNull(filmStorage.getFilm(newFilm.getId()));
        return filmStorage.updateFilm(newFilm);
    }

    public void deleteLike(long id, long userId) {
        userStorage.getUser(userId);
        filmStorage.deleteLike(id, userId);
    }

    private void checkFilmNotNull(Film film) {
        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }
    }

    private void validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(FIRST_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }
}
