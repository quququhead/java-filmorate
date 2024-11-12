package ru.yandex.practicum.filmorate.dal.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Collection<Film> getAllFilmsBy(long count);

    Collection<Film> getAllFilmsBy(int genreId, int year, long count);

    Collection<Film> getAllFilmsByGenreId(int genreId, long count);

    Collection<Film> getAllFilmsByYearRelease(int year, long count);

    Collection<Film> getAllFilmsOfDirectorSortedByYear(long directorId);

    Collection<Film> getAllFilmsOfDirectorSortedByLikes(long directorId);

    Film getFilm(long filmId);

    long addFilm(Film film);

    Film updateFilm(Film newFilm);

    Collection<Film> getRecommendedFilms(long userId);
}
