package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> findAllUsers() {
        return userStorage.getAllUsers();
    }

    public User findUser(Long id) {
        return userStorage.getUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
    }

    public Collection<User> findUserFriends(Long id) {
        User user = userStorage.getUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
        return user.getFriends().stream()
                .map(userStorage::getUser)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public Collection<User> findMutualFriends(Long id, Long otherId) {
        User user1 = userStorage.getUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
        User user2 = userStorage.getUser(otherId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + otherId + " не найден"));
        return user1.getFriends().stream()
                .filter(ids -> user2.getFriends().contains(ids))
                .map(userStorage::getUser)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public User addFriend(Long id, Long friendId) {
        User user1 = userStorage.getUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
        User user2 = userStorage.getUser(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + friendId + " не найден"));
        user1.addFriend(friendId);
        user2.addFriend(id);
        return user1;
    }

    public User create(User user) {
        return userStorage.addUser(user);
    }

    public User update(User newUser) {
        User oldUser = userStorage.getUser(newUser.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден"));
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setName(newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        return oldUser;
    }

    public User deleteUserFriend(Long id, Long friendId) {
        User user1 = userStorage.getUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
        User user2 = userStorage.getUser(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + friendId + " не найден"));
        user1.deleteFriend(friendId);
        user2.deleteFriend(id);
        return user1;
    }
}
