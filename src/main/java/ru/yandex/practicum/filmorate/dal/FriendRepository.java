package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friend;

import java.util.Collection;

@Repository
public class FriendRepository extends BaseRepository<Friend> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM friends";
    private static final String FIND_ALL_BY_USER_ID_QUERY = "SELECT * FROM friends WHERE user_id = ?";
    private static final String ADD_QUERY = "INSERT INTO friends(user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

    public FriendRepository(JdbcTemplate jdbc, RowMapper<Friend> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Friend> getAllFriends() {
        return findMany(FIND_ALL_QUERY);
    }

    public Collection<Friend> getAllFriends(long userId) {
        return findMany(FIND_ALL_BY_USER_ID_QUERY, userId);
    }

    public void addUserFriend(long userId, long friendId) {
        update(ADD_QUERY, userId, friendId);
    }

    public void deleteUserFriend(long userId, long friendId) {
        delete(DELETE_QUERY, userId, friendId);
    }
}
