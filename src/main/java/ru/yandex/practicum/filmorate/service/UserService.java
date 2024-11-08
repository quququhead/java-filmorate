package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FriendRepository;
import ru.yandex.practicum.filmorate.dal.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FriendRepository friendRepository;

    public Collection<User> findAllUsers() {
        return userStorage.getAllUsers();
    }

    public Collection<User> findUserFriends(long userId) {
        checkUserNotNull(userStorage.getUser(userId));
        return userStorage.getUserFriends(userId);
    }

    public Collection<User> findMutualFriends(long userId, long otherUserId) {
        checkUserNotNull(userStorage.getUser(userId));
        checkUserNotNull(userStorage.getUser(otherUserId));
        return userStorage.getMutualFriends(userId, otherUserId);
    }

    public User findUser(long userId) {
        User user = userStorage.getUser(userId);
        checkUserNotNull(user);
        return user;
    }

    public User createUser(User user) {
        user.setId(userStorage.addUser(user));
        return user;
    }

    public User updateUser(User newUser) {
        return userStorage.updateUser(newUser);
    }

    public void addFriend(long userId, long friendId) {
        checkUserNotNull(userStorage.getUser(userId));
        checkUserNotNull(userStorage.getUser(friendId));
        friendRepository.insert(userId, friendId);
    }

    public void deleteUserFriend(long userId, long friendId) {
        checkUserNotNull(userStorage.getUser(userId));
        checkUserNotNull(userStorage.getUser(friendId));
        friendRepository.delete(userId, friendId);
    }

    private void checkUserNotNull(User user) {
        if (user == null) {
            throw new NoSuchElementException("Пользователь не найден");
        }
    }
}
