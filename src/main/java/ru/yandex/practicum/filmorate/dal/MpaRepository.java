package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Repository
public class MpaRepository extends BaseRepository<Mpa> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM rating_mpas";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM rating_mpas WHERE rating_mpa_id = ?";

    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Mpa> getAllMpas() {
        return findMany(FIND_ALL_QUERY);
    }

    public Mpa getMpa(long mpaId) {
        return findOne(FIND_BY_ID_QUERY, mpaId);
    }
}
