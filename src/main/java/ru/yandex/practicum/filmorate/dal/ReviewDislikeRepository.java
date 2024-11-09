package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.interfaces.BaseInsertDeleteRepository;

@Repository
public class ReviewDislikeRepository extends BaseInsertDeleteRepository {
    public ReviewDislikeRepository(JdbcTemplate jdbc) {
        super(jdbc, "reviews_dislikes", "review_id", "user_id");
    }
}
