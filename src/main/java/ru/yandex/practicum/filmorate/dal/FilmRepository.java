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
    private static final String FIND_RECOMMENDED_FILMS = "SELECT DISTINCT f.* FROM films f JOIN likes l1 ON f.film_id = l1.film_id" +
            " LEFT JOIN likes l2 ON l1.film_id = l2.film_id AND l2.user_id = ? WHERE l2.film_id IS NULL" +
            " AND l1.user_id = (SELECT l1.user_id FROM likes l1 JOIN likes l2 ON l1.film_id = l2.film_id" +
            " WHERE l1.user_id <> ? AND l2.user_id = ? GROUP BY l1.user_id" +
            " ORDER BY COUNT(l1.film_id) DESC LIMIT 1)";
    private static final String FIND_ALL_OF_DIRECTOR_SORTED_BY_YEAR_QUERY = "SELECT f.* FROM films AS f " +
            "WHERE f.film_id IN (SELECT fd.film_id FROM film_directors AS fd WHERE fd.director_id = ?) " +
            "ORDER BY EXTRACT(YEAR FROM f.release_date) ASC";
    private static final String FIND_ALL_OF_DIRECTOR_SORTED_BY_LIKES_QUERY = "SELECT f.* FROM films AS f " +
            "LEFT JOIN likes AS l ON l.film_id = f.film_id " +
            "WHERE f.film_id IN (SELECT film_id FROM film_directors WHERE director_id = ?) " +
            "GROUP BY f.film_id ORDER BY COUNT(l.user_id) DESC";
    private static final String FIND_COMMON_FILMS = "SELECT f.* FROM films AS f LEFT JOIN likes AS l ON f.film_id = l.film_id" +
            " WHERE l.film_id IN (SELECT film_id FROM likes WHERE user_id = ?)" +
            " AND l.film_id IN (SELECT film_id FROM likes WHERE user_id = ?)" +
            " GROUP BY l.film_id" +
            " ORDER BY COUNT(l.user_id) DESC";
    private static final String DELETE_FILM_QUERY = "DELETE FROM films WHERE film_id = ?";
    private static final String FIND_ALL_OF_BY_DIRECTOR_AND_TITLE_QUERY = "SELECT f.* FROM films AS f " +
            "LEFT JOIN likes AS l ON l.film_id = f.film_id " +
            "LEFT JOIN film_directors AS fd ON fd.film_id = f.film_id " +
            "LEFT JOIN directors AS d ON d.director_id = fd.director_id " +
            "WHERE d.director_name ILIKE ? OR f.film_name ILIKE ? " +
            "GROUP BY f.film_id " +
            "ORDER BY COUNT(l.user_id) DESC";
    private static final String FIND_ALL_OF_BY_DIRECTOR_QUERY = "SELECT f.* FROM films AS f " +
            "LEFT JOIN likes AS l ON l.film_id = f.film_id " +
            "LEFT JOIN film_directors AS fd ON fd.film_id = f.film_id " +
            "LEFT JOIN directors AS d ON d.director_id = fd.director_id " +
            "WHERE d.director_name ILIKE ? " +
            "GROUP BY f.film_id " +
            "ORDER BY COUNT(l.user_id) DESC";
    private static final String FIND_ALL_OF_BY_TITLE_QUERY = "SELECT f.* FROM films AS f " +
            "LEFT JOIN likes AS l ON l.film_id = f.film_id " +
            "WHERE f.film_name ILIKE ? " +
            "GROUP BY f.film_id " +
            "ORDER BY COUNT(l.user_id) DESC";

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
    public Collection<Film> getRecommendedFilms(long userId) {
        return findMany(FIND_RECOMMENDED_FILMS, userId, userId, userId);
    }

    @Override
    public Collection<Film> getCommonFilms(long userId, long friendId) {
        return findMany(FIND_COMMON_FILMS, userId, friendId);
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
    public Collection<Film> getAllFilmsOfDirectorSortedByYear(long directorId) {
        return findMany(FIND_ALL_OF_DIRECTOR_SORTED_BY_YEAR_QUERY, directorId);
    }

    @Override
    public Collection<Film> getAllFilmsOfDirectorSortedByLikes(long directorId) {
        return findMany(FIND_ALL_OF_DIRECTOR_SORTED_BY_LIKES_QUERY, directorId);
    }

    @Override
    public Collection<Film> getAllFilmsBySearchingOfDirectorAndTitle(String query) {
        return findMany(FIND_ALL_OF_BY_DIRECTOR_AND_TITLE_QUERY, prepareQuery(query), prepareQuery(query));
    }

    @Override
    public Collection<Film> getAllFilmsBySearchingOfDirector(String query) {
        return findMany(FIND_ALL_OF_BY_DIRECTOR_QUERY, prepareQuery(query));
    }

    @Override
    public Collection<Film> getAllFilmsBySearchingOfTitle(String query) {
        return findMany(FIND_ALL_OF_BY_TITLE_QUERY, prepareQuery(query));
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

    @Override
    public void deleteFilm(long filmId) {
        delete(DELETE_FILM_QUERY, filmId);
    }

    private String prepareQuery(String query) {
        return "%" + query + "%";
    }
}
