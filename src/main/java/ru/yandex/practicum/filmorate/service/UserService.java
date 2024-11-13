package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.dal.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.dal.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final FilmDirectorRepository filmDirectorRepository;
    private final FilmGenreRepository filmGenreRepository;
    private final DirectorRepository directorRepository;
    private final FriendRepository friendRepository;
    private final GenreRepository genreRepository;
    private final FeedRepository feedRepository;
    private final MpaRepository mpaRepository;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

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
        return prepare(filmStorage.getRecommendedFilms(userId));
    }

    public User createUser(User user) {
        user.setId(userStorage.addUser(user));
        return user;
    }

    public User updateUser(User newUser) {
        return userStorage.updateUser(newUser);
    }

    public void deleteUser(long userId) {
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

    private List<Film> prepare(Collection<Film> films) {
        if (films.isEmpty()) {
            return Collections.emptyList();
        }

        Collection<FilmGenre> filmGenres = filmGenreRepository.getAllGenres();
        Collection<Genre> genres = genreRepository.getAllGenres();
        Collection<FilmDirector> filmDirectors = filmDirectorRepository.getAllDirectors();
        Collection<Director> directors = directorRepository.getAllDirectors();
        Collection<Mpa> mpas = mpaRepository.getAllMpas();

        return films.stream()
                .peek(film -> {
                    film.getGenres().addAll(filmGenres.stream()
                            .filter(filmGenre -> film.getId() == filmGenre.getFilmId())
                            .map(filmGenre -> Genre.builder()
                                    .id(filmGenre.getGenreId())
                                    .name(genres.stream()
                                            .filter(genre -> genre.getId() == filmGenre.getGenreId())
                                            .findFirst().orElseThrow().getName())
                                    .build())
                            .toList());
                    film.getDirectors().addAll(filmDirectors.stream()
                            .filter(filmDirector -> film.getId() == filmDirector.getFilmId())
                            .map(filmDirector -> Director.builder()
                                    .id(filmDirector.getDirectorId())
                                    .name(directors.stream()
                                            .filter(director -> director.getId() == filmDirector.getDirectorId())
                                            .findFirst().orElseThrow().getName())
                                    .build())
                            .toList());
                    film.getMpa().setName(mpas.stream()
                            .filter(mpa -> film.getMpa().getId() == mpa.getId())
                            .findFirst().orElseThrow().getName());
                }).toList();
    }
}
