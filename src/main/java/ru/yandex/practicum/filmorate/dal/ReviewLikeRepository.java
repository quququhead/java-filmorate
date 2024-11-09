package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.interfaces.BaseInsertDeleteRepository;

@Repository
public class ReviewLikeRepository extends BaseInsertDeleteRepository {
    public ReviewLikeRepository(JdbcTemplate jdbc) {
        super(jdbc, "reviews_likes", "review_id", "user_id");
    }
}
