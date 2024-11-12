package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.interfaces.BaseRepository;
import ru.yandex.practicum.filmorate.model.FilmDirector;

import java.util.Collection;
import java.util.List;

@Component
public class FilmDirectorRepository extends BaseRepository<FilmDirector> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM film_directors ORDER BY director_id";
    private static final String INSERT_QUERY = "INSERT INTO film_directors(film_id, director_id) VALUES (?, ?)";
    private static final String DELETE_ALL_FROM_FILM_QUERY = "DELETE FROM film_directors WHERE film_id = ?";

    public FilmDirectorRepository(JdbcTemplate jdbc, RowMapper<FilmDirector> mapper) {
        super(jdbc, mapper);
    }

    public Collection<FilmDirector> getAllDirectors() {
        return findMany(FIND_ALL_QUERY);
    }

    public void addDirectorsToFilm(List<Object[]> batch) {
        batchUpdateBase(INSERT_QUERY, batch);
    }

    public void deleteDirectorsFromFilm(long filmId) {
        update(DELETE_ALL_FROM_FILM_QUERY, filmId);
    }
}
