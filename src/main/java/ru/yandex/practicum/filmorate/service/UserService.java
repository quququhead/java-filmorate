package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> findAllUsers() {
        return userStorage.getAllUsers();
    }

    public User findUser(Long id) {
        return receiveUser(id);
    }

    public Collection<User> findUserFriends(Long id) {
        User user = receiveUser(id);
        return user.getFriends().stream()
                .map(this::receiveUser)
                .collect(Collectors.toList());
    }

    public Collection<User> findMutualFriends(Long id, Long otherId) {
        User user1 = receiveUser(id);
        User user2 = receiveUser(otherId);
        return user1.getFriends().stream()
                .filter(ids -> user2.getFriends().contains(ids))
                .map(this::receiveUser)
                .collect(Collectors.toList());
    }

    public User addFriend(Long id, Long friendId) {
        User user1 = receiveUser(id);
        User user2 = receiveUser(friendId);
        user1.addFriend(friendId);
        user2.addFriend(id);
        return user1;
    }

    public User create(User user) {
        return userStorage.addUser(user);
    }

    public User update(User newUser) {
        User oldUser = receiveUser(newUser.getId());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setName(newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        return oldUser;
    }

    public User deleteUserFriend(Long id, Long friendId) {
        User user1 = receiveUser(id);
        User user2 = receiveUser(friendId);
        user1.deleteFriend(friendId);
        user2.deleteFriend(id);
        return user1;
    }

    User receiveUser(Long id) {
        return userStorage.getUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
    }
}
