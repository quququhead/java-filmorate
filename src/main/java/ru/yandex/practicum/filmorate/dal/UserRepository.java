package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

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
            "id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
    private static final String INSERT_QUERY = "INSERT INTO users(user_name, login, email, birthday_date)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET user_name = ?, login = ?, email = ?," +
            " birthday_date = ? WHERE user_id = ?";

    private final FriendRepository friendRepository;

    @Autowired
    public UserRepository(JdbcTemplate jdbc,
                          RowMapper<User> mapper,
                          FriendRepository friendRepository) {
        super(jdbc, mapper);
        this.friendRepository = friendRepository;
    }

    @Override
    public Collection<User> getAllUsers() {
        Collection<User> users = findMany(FIND_ALL_QUERY);
        return addFriends(users);
    }

    @Override
    public User getUser(long userId) {
        User user = findOne(FIND_BY_ID_QUERY, userId);
        if (user != null) {
            user.getFriends().addAll(friendRepository.getAllFriends(user.getId())
                    .stream()
                    .map(Friend::getFriendId)
                    .toList()
            );
        }
        return user;
    }

    @Override
    public Collection<User> getUserFriends(long userId) {
        Collection<User> users = findMany(USER_FRIENDS_QUERY, userId);
        return addFriends(users);
    }

    @Override
    public Collection<User> getMutualFriends(long userId, long otherUserId) {
        Collection<User> users = findMany(USER_MUTUAL_FRIENDS_QUERY, userId, otherUserId);
        return addFriends(users);
    }

    @Override
    public void addUserFriend(long userId, long friendId) {
        friendRepository.addUserFriend(userId, friendId);
    }

    @Override
    public User addUser(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                Date.valueOf(user.getBirthday())
        );
        user.setId(id);
        return user;
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

    @Override
    public void deleteUserFriend(long userId, long friendId) {
        friendRepository.deleteUserFriend(userId, friendId);
    }

    private Collection<User> addFriends(Collection<User> users) {
        if (!users.isEmpty()) {
            Collection<Friend> friends = friendRepository.getAllFriends();
            for (User user : users) {
                user.getFriends().addAll(friends
                        .stream()
                        .filter(friend -> friend.getUserId() == (user.getId()))
                        .map(Friend::getFriendId)
                        .toList()
                );
            }
        }
        return users;
    }
}
