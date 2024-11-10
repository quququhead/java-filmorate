package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.interfaces.BaseRepository;
import ru.yandex.practicum.filmorate.dal.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.util.Collection;

@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_MOST_POPULAR_QUERY = "SELECT f.* FROM films AS f LEFT JOIN likes AS l ON l.film_id = f.film_id " +
            "GROUP BY f.film_id ORDER BY COUNT(l.user_id) DESC LIMIT ?";
    private static final String FIND_MOST_POPULAR_BY_YEAR_AND_GENRE_QUERY = "SELECT f.* FROM films f" +
            " LEFT JOIN likes l ON l.film_id = f.film_id LEFT JOIN film_genres fg ON fg.film_id = f.film_id" +
            " WHERE fg.genre_id = ? AND EXTRACT (YEAR FROM f.release_date) = ?" +
            " GROUP BY f.film_id ORDER BY COUNT(l.user_id) DESC LIMIT ?";
    private static final String FIND_MOST_POPULAR_BY_GENRE_QUERY = "SELECT f.* FROM films f" +
            " LEFT JOIN likes l ON l.film_id = f.film_id LEFT JOIN film_genres fg ON fg.film_id = f.film_id" +
            " WHERE fg.genre_id = ? GROUP BY f.film_id ORDER BY COUNT(l.user_id) DESC LIMIT ?";
    private static final String FIND_MOST_POPULAR_BY_YEAR_QUERY = "SELECT f.* FROM films f LEFT JOIN likes l ON l.film_id = f.film_id" +
            " WHERE EXTRACT (YEAR FROM f.release_date) = ? GROUP BY f.film_id ORDER BY COUNT(l.user_id) DESC LIMIT ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films(film_name, description, release_date, duration, rating_mpa_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET film_name = ?, description = ?, release_date = ?," +
            " duration = ?, rating_mpa_id = ? WHERE film_id = ?";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Collection<Film> getAllFilmsBy(long count) {
        return findMany(FIND_MOST_POPULAR_QUERY, count);
    }

    @Override
    public Collection<Film> getAllFilmsBy(int genreId, int year, long count) {
        return findMany(FIND_MOST_POPULAR_BY_YEAR_AND_GENRE_QUERY, genreId, year, count);
    }

    @Override
    public Collection<Film> getAllFilmsByGenreId(int genreId, long count) {
        return findMany(FIND_MOST_POPULAR_BY_GENRE_QUERY, genreId, count);
    }

    @Override
    public Collection<Film> getAllFilmsByYearRelease(int year, long count) {
        return findMany(FIND_MOST_POPULAR_BY_YEAR_QUERY, year, count);
    }

    @Override
    public Film getFilm(long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    @Override
    public long addFilm(Film film) {
        return insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId()
        );
    }

    @Override
    public Film updateFilm(Film newFilm) {
        update(
                UPDATE_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                Date.valueOf(newFilm.getReleaseDate()),
                newFilm.getDuration(),
                newFilm.getMpa().getId(),
                newFilm.getId()
        );
        return newFilm;
    }
}
