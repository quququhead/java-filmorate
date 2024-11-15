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

    Collection<Film> getAllFilmsBySearchingOfDirectorAndTitle(String query);

    Collection<Film> getAllFilmsBySearchingOfDirector(String query);

    Collection<Film> getAllFilmsBySearchingOfTitle(String query);

    Film getFilm(long filmId);

    long addFilm(Film film);

    Film updateFilm(Film newFilm);

    void deleteFilm(long id);

    Collection<Film> getRecommendedFilms(long userId);

    Collection<Film> getCommonFilms(long userId, long friendId);
}
