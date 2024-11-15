package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.interfaces.BaseRepository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Repository
public class GenreRepository extends BaseRepository<Genre> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE genre_id = ?";
    private static final String FIND_FILM_GENRES_QUERY = "SELECT g.* FROM genres AS g " +
            "JOIN film_genres AS fg ON g.genre_id = fg.genre_id WHERE fg.film_id = ? GROUP BY g.genre_id ORDER BY g.genre_id ASC";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Genre> getAllGenres() {
        return findMany(FIND_ALL_QUERY);
    }

    public Collection<Genre> getAllGenresByFilmId(long filmId) {
        return findMany(FIND_FILM_GENRES_QUERY, filmId);
    }

    public Genre getGenre(long genreId) {
        return findOne(FIND_BY_ID_QUERY, genreId);
    }
}
