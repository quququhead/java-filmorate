package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.interfaces.BaseRepository;
import ru.yandex.practicum.filmorate.model.Feed;

import java.util.List;

@Repository
public class FeedRepository extends BaseRepository<Feed> {
    private static final String FIND_FEED_QUERY = "SELECT * FROM feed WHERE user_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO feed (user_id, type, operation, entity_id, updated) VALUES (?, ?, ?, ?, ?)";

    public FeedRepository(JdbcTemplate jdbc, RowMapper<Feed> mapper) {
        super(jdbc, mapper);
    }

    public List<Feed> findAllBy(long userId) {
        return findMany(FIND_FEED_QUERY, userId);
    }

    public void create(Feed feed) {
        insert(
                INSERT_QUERY,
                feed.userId(),
                feed.eventType().name(),
                feed.operation().name(),
                feed.entityId(),
                feed.timestamp()
        );
    }
}
