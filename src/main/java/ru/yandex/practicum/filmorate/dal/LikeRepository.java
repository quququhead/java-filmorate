package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.interfaces.BaseInsertDeleteRepository;

@Repository
public class LikeRepository extends BaseInsertDeleteRepository {
    public LikeRepository(JdbcTemplate jdbc) {
        super(jdbc, "likes", "film_id", "user_id");
    }
}
