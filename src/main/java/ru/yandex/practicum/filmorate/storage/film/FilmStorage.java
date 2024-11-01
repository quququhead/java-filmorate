package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film getFilm(long filmId);

    Film addFilm(Film film);

    void setLike(long filmId, long userId);

    Film updateFilm(Film newFilm);

    void deleteLike(long filmId, long userId);
}
