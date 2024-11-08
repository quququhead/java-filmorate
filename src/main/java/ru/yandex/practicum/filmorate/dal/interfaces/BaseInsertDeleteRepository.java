package ru.yandex.practicum.filmorate.dal.interfaces;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class BaseInsertDeleteRepository extends BaseUpdateExecutor {
    private static final String insert =
            "INSERT INTO %s (%s, %s) " +
            "VALUES (?, ?)";
    private static final String delete =
            "DELETE FROM %s " +
            "WHERE %s = ? AND %s = ?";
    private static final String exists =
            "SELECT COUNT(*) " +
            "FROM %s " +
            "WHERE %s = ? AND %s = ?";

    private final String tableName;
    private final String firstColumn;
    private final String secondColumn;

    public BaseInsertDeleteRepository(JdbcTemplate jdbc, String tableName, String firstColumn, String secondColumn) {
        super(jdbc);
        this.tableName = tableName;
        this.firstColumn = firstColumn;
        this.secondColumn = secondColumn;
    }

    public void insert(long entityId, long otherId) {
        update(String.format(insert, tableName, firstColumn, secondColumn), entityId, otherId);
    }

    public void delete(long entityId, long otherId) {
        delete(String.format(delete, tableName, firstColumn, secondColumn), entityId, otherId);
    }

    public int exists(long entityId, long otherId) {
        return count(String.format(exists, tableName, firstColumn, secondColumn), entityId, otherId);
    }
}
