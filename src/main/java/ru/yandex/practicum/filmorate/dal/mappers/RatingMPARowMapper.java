package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RatingMPARowMapper implements RowMapper<RatingMPA> {
    @Override
    public RatingMPA mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new RatingMPA(
                resultSet.getLong("rating_mpa_id"),
                resultSet.getString("rating_mpa_name")
        );
    }
}
