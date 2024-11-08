package ru.yandex.practicum.filmorate.dal;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.interfaces.BaseRepository;
import ru.yandex.practicum.filmorate.dal.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.util.Collection;

@Repository
@Primary
public class UserRepository extends BaseRepository<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String USER_FRIENDS_QUERY = "SELECT * FROM users " +
            "WHERE user_id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
    private static final String USER_MUTUAL_FRIENDS_QUERY = "SELECT * FROM users " +
            "WHERE user_id IN (SELECT friend_id FROM friends WHERE user_id = ?) AND " +
            "user_id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
    private static final String INSERT_QUERY = "INSERT INTO users(user_name, login, email, birthday_date)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET user_name = ?, login = ?, email = ?," +
            " birthday_date = ? WHERE user_id = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<User> getAllUsers() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public User getUser(long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    @Override
    public Collection<User> getUserFriends(long userId) {
        return findMany(USER_FRIENDS_QUERY, userId);
    }

    @Override
    public Collection<User> getMutualFriends(long userId, long otherUserId) {
        return findMany(USER_MUTUAL_FRIENDS_QUERY, userId, otherUserId);
    }

    @Override
    public long addUser(User user) {
        return insert(
                INSERT_QUERY,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                Date.valueOf(user.getBirthday())
        );
    }

    @Override
    public User updateUser(User newUser) {
        update(
                UPDATE_QUERY,
                newUser.getName(),
                newUser.getLogin(),
                newUser.getEmail(),
                Date.valueOf(newUser.getBirthday()),
                newUser.getId()
        );
        return newUser;
    }
}
