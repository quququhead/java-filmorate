package ru.yandex.practicum.filmorate.dal.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getAllUsers();

    User getUser(long userId);

    Collection<User> getUserFriends(long userId);

    Collection<User> getMutualFriends(long userId, long otherUserId);

    long addUser(User user);

    User updateUser(User newUser);

    void deleteUser(long userId);
}
