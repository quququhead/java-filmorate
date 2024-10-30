package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.Collection;

@Repository
public class LikeRepository extends BaseRepository<Like> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM likes";
    private static final String FIND_ALL_BY_FILM_ID_QUERY = "SELECT * FROM likes WHERE film_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String ADD_QUERY = "INSERT INTO likes(film_id, user_id) VALUES (?, ?)";

    public LikeRepository(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Like> getAllLikes() {
        return findMany(FIND_ALL_QUERY);
    }

    public Collection<Like> getAllLikes(long filmId) {
        return findMany(FIND_ALL_BY_FILM_ID_QUERY, filmId);
    }

    public void addLikeToFilm(long filmId, long userId) {
        update(ADD_QUERY, filmId, userId);
    }

    public void deleteLikeFromFilm(long filmId, long userId) {
        update(DELETE_QUERY, filmId, userId);
    }
}
