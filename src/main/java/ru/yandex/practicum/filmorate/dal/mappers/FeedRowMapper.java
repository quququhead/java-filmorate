package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FeedRowMapper implements RowMapper<Feed> {
    @Override
    public Feed mapRow(ResultSet rs, int ignored) throws SQLException {
        return new Feed(
                rs.getLong("id"),
                rs.getLong("user_id"),
                EventType.valueOf(rs.getString("type")),
                Operation.valueOf(rs.getString("operation")),
                rs.getLong("entity_id"),
                rs.getLong("updated")
        );
    }
}
