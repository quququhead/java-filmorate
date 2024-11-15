package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.interfaces.BaseRepository;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.Collection;
import java.util.List;

@Repository
public class FilmGenreRepository extends BaseRepository<FilmGenre> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM film_genres ORDER BY genre_id";
    private static final String INSERT_QUERY = "INSERT INTO film_genres(film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_ALL_FROM_FILM_QUERY = "DELETE FROM film_genres WHERE film_id = ?";

    public FilmGenreRepository(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);
    }

    public Collection<FilmGenre> getAllGenres() {
        return findMany(FIND_ALL_QUERY);
    }

    public void addGenresToFilm(List<Object[]> batch) {
        batchUpdateBase(INSERT_QUERY, batch);
    }

    public void deleteGenresFromFilm(long filmId) {
        delete(DELETE_ALL_FROM_FILM_QUERY, filmId);
    }
}
