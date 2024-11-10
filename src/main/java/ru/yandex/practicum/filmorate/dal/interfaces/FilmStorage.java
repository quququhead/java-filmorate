package ru.yandex.practicum.filmorate.dal.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Collection<Film> getAllFilmsBy(long count);

    Film getFilm(long filmId);

    long addFilm(Film film);

    Film updateFilm(Film newFilm);

    Collection<Film> getRecommendedFilms(long userId);
}
