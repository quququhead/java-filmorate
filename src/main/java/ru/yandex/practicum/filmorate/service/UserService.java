package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> findAllUsers() {
        return userStorage.getAllUsers();
    }

    public User findUser(long userId) {
        User user = userStorage.getUser(userId);
        checkUserNotNull(user);
        return user;
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

    public void addFriend(long userId, long friendId) {
        checkUserNotNull(userStorage.getUser(userId));
        checkUserNotNull(userStorage.getUser(friendId));
        userStorage.addUserFriend(userId, friendId);
    }

    public User createUser(User user) {
        validate(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User newUser) {
        validate(newUser);
        return userStorage.updateUser(newUser);
    }

    public void deleteUserFriend(long userId, long friendId) {
        checkUserNotNull(userStorage.getUser(userId));
        checkUserNotNull(userStorage.getUser(friendId));
        userStorage.deleteUserFriend(userId, friendId);
    }

    private void checkUserNotNull(User user) {
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    private void validate(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
