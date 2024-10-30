package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getUser(long userId) {
        return users.get(userId);
    }

    @Override
    public Collection<User> getUserFriends(long userId) {
        User user = users.get(userId);
        checkNotNull(user);
        return user.getFriends().stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<User> getMutualFriends(long userId, long otherUserId) {
        User user = users.get(userId);
        User friend = users.get(otherUserId);
        checkNotNull(user);
        checkNotNull(friend);
        return user.getFriends().stream()
                .filter(ids -> friend.getFriends().contains(ids))
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public void addUserFriend(long userId, long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        checkNotNull(user);
        checkNotNull(friend);
        user.addFriend(friendId);
    }

    @Override
    public User addUser(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        User oldUser = users.get(newUser.getId());
        checkNotNull(oldUser);
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setName(newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        return oldUser;
    }

    @Override
    public void deleteUserFriend(long userId, long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        checkNotNull(user);
        checkNotNull(friend);
        user.deleteFriend(friendId);
    }

    private void checkNotNull(User user) {
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
