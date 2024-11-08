package ru.yandex.practicum.filmorate.dal.interfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
public abstract class BaseUpdateExecutor {
    final JdbcTemplate jdbc;

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated == 0) {
            throw new NoSuchElementException("Не удалось обновить данные");
        }
    }

    public void delete(String query, Object... params) {
        jdbc.update(query, params);
    }

    protected Integer count(String query, Object... params) {
        return jdbc.queryForObject(query, Integer.class, params);
    }
}
