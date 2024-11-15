package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.interfaces.BaseRepository;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;

@Repository
public class DirectorRepository extends BaseRepository<Director> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM directors";
    private static final String FIND_FILM_DIRECTORS_QUERY = "SELECT d.* FROM directors AS d " +
            "JOIN film_directors AS fd ON d.director_id = fd.director_id WHERE fd.film_id = ? GROUP BY d.director_id ORDER BY d.director_id ASC";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM directors WHERE director_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO directors(director_name)" +
            "VALUES (?)";
    private static final String UPDATE_QUERY = "UPDATE directors SET director_name = ? WHERE director_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM directors WHERE director_id = ?";

    public DirectorRepository(JdbcTemplate jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Director> getAllDirectors() {
        return findMany(FIND_ALL_QUERY);
    }

    public Collection<Director> getAllDirectorsByFilmId(long filmId) {
        return findMany(FIND_FILM_DIRECTORS_QUERY, filmId);
    }

    public Director getDirector(long directorId) {
        return findOne(FIND_BY_ID_QUERY, directorId);
    }

    public long addDirector(Director director) {
        return insert(
                INSERT_QUERY,
                director.getName()
        );
    }

    public Director updateDirector(Director newDirector) {
        update(
                UPDATE_QUERY,
                newDirector.getName(),
                newDirector.getId()
        );
        return newDirector;
    }

    public void deleteDirector(long directorId) {
        delete(DELETE_QUERY, directorId);
    }
}
