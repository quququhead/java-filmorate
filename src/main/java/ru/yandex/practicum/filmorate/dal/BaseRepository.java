package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.ConstrainsViolationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@RequiredArgsConstructor
public class BaseRepository<T> {
    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;

    protected T findOne(String query, Object... params) {
        try {
            jdbc.queryForObject(query, mapper, params);
            return jdbc.queryForObject(query, mapper, params);
        } catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }

    protected List<T> findMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    public void delete(String query, Object... params) {
        jdbc.update(query, params);
    }

    protected long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbc.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                for (int idx = 0; idx < params.length; idx++) {
                    ps.setObject(idx + 1, params[idx]);
                }
                return ps;
            }, keyHolder);
        } catch (DataIntegrityViolationException e) {
            throw new ConstrainsViolationException(e.getMessage());
        }

        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            return id;
        } else {
            throw new NotFoundException("Не удалось сохранить данные");
        }
    }

    protected void update(String query, Object... params) {
        try {
            int rowsUpdated = jdbc.update(query, params);
            if (rowsUpdated == 0) {
                throw new NotFoundException("Не удалось обновить данные");
            }
        } catch (DataIntegrityViolationException e) {
            throw new ConstrainsViolationException(e.getMessage());
        }
    }

    protected void batchUpdateBase(String query, BatchPreparedStatementSetter bps) {
        try {
            int[] rowsUpdated = jdbc.batchUpdate(query, bps);
            if (rowsUpdated.length == 0) {
                throw new NotFoundException("Не удалось обновить данные");
            }
        } catch (DataIntegrityViolationException e) {
            throw new ConstrainsViolationException(e.getMessage());
        }
    }
}
