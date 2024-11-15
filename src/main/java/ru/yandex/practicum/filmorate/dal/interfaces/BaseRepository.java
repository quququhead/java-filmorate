package ru.yandex.practicum.filmorate.dal.interfaces;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class BaseRepository<T> extends BaseUpdateExecutor {
    private final RowMapper<T> mapper;

    public BaseRepository(JdbcTemplate jdbc, RowMapper<T> mapper) {
        super(jdbc);
        this.mapper = mapper;
    }

    protected T findOne(String query, Object... params) {
        try {
            return jdbc.queryForObject(query, mapper, params);
        } catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }

    protected List<T> findMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    protected long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            return id;
        } else {
            throw new NoSuchElementException("Не удалось сохранить данные");
        }
    }

    protected void batchUpdateBase(String query, List<Object[]> batch) {
        jdbc.batchUpdate(query, batch);
    }
}
