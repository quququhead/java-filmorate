package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.interfaces.BaseInsertDeleteRepository;

@Repository
public class FriendRepository extends BaseInsertDeleteRepository {
    public FriendRepository(JdbcTemplate jdbc) {
        super(jdbc, "friends", "user_id", "friend_id");
    }
}
