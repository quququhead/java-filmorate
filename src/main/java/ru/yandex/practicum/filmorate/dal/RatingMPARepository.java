package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.Collection;

@Repository
public class RatingMPARepository extends BaseRepository<RatingMPA> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM rating_mpas";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM rating_mpas WHERE rating_mpa_id = ?";

    public RatingMPARepository(JdbcTemplate jdbc, RowMapper<RatingMPA> mapper) {
        super(jdbc, mapper);
    }

    public Collection<RatingMPA> getAllRatingMPAs() {
        return findMany(FIND_ALL_QUERY);
    }

    public RatingMPA getRatingMPA(long ratingMPAId) {
        return findOne(FIND_BY_ID_QUERY, ratingMPAId);
    }
}
