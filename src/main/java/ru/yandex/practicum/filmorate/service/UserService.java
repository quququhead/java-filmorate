package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FeedRepository;
import ru.yandex.practicum.filmorate.dal.FriendRepository;
import ru.yandex.practicum.filmorate.dal.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.dal.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendRepository friendRepository;
    private final FeedRepository feedRepository;
    private final FilmStorage filmStorage;

    public List<Feed> getFeed(long userId) {
        return feedRepository.findAllBy(notNull(userStorage.getUser(userId)).getId());
    }

    public Collection<User> findAllUsers() {
        return userStorage.getAllUsers();
    }

    public Collection<User> findUserFriends(long userId) {
        notNull(userStorage.getUser(userId));
        return userStorage.getUserFriends(userId);
    }

    public Collection<User> findMutualFriends(long userId, long otherUserId) {
        notNull(userStorage.getUser(userId));
        notNull(userStorage.getUser(otherUserId));
        return userStorage.getMutualFriends(userId, otherUserId);
    }

    public User findUser(long userId) {
        return notNull(userStorage.getUser(userId));
    }

    public Collection<Film> getRecommendedFilms(long userId) {
        return filmStorage.getRecommendedFilms(userId);
    }

    public User createUser(User user) {
        user.setId(userStorage.addUser(user));
        return user;
    }

    public User updateUser(User newUser) {
        return userStorage.updateUser(newUser);
    }

    public void deleteUser(Integer userId){
        userStorage.deleteUser(userId);
    }

    public void addFriend(long userId, long friendId) {
        notNull(userStorage.getUser(userId));
        notNull(userStorage.getUser(friendId));
        friendRepository.insert(userId, friendId);
        feedRepository.create(new Feed(userId, EventType.FRIEND, Operation.ADD, friendId, Instant.now().toEpochMilli()));
    }

    public void deleteUserFriend(long userId, long friendId) {
        notNull(userStorage.getUser(userId));
        notNull(userStorage.getUser(friendId));
        friendRepository.delete(userId, friendId);
        feedRepository.create(new Feed(userId, EventType.FRIEND, Operation.REMOVE, friendId, Instant.now().toEpochMilli()));
    }

    private User notNull(User user) {
        if (user == null) {
            throw new NoSuchElementException("Пользователь не найден");
        }
        return user;
    }
}
