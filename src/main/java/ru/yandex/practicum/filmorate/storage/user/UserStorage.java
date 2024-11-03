package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getAllUsers();

    User getUser(long userId);

    Collection<User> getUserFriends(long userId);

    Collection<User> getMutualFriends(long userId, long otherUserId);

    void addUserFriend(long userId, long friendId);

    User addUser(User user);

    User updateUser(User newUser);

    void deleteUserFriend(long userId, long friendId);
}
