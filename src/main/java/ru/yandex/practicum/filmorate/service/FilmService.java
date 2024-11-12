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
import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmGenreRepository filmGenreRepository;
    private final GenreRepository genreRepository;
    private final LikeRepository likeRepository;
    private final MpaRepository mpaRepository;
    private final FeedRepository feedRepository;

    public Collection<Film> findAllFilms() {
        return prepare(filmStorage.getAllFilms());
    }

    public Collection<Film> findTopFilms(Integer genreId, Integer year, Integer count) {
        if (genreId != null && year != null) {
            return prepare(filmStorage.getAllFilmsBy(genreId, year, count));
        }
        if (genreId != null) {
            return prepare(filmStorage.getAllFilmsByGenreId(genreId, count));
        }
        if (year != null) {
            return prepare(filmStorage.getAllFilmsByYearRelease(year, count));
        }
        return prepare(filmStorage.getAllFilmsBy(count));
    }

    public Film findFilm(long filmId) {
        return prepare(notNull(filmStorage.getFilm(filmId)));
    }

    public Collection<Film> getRecommendedFilms(long userId) {
        return prepare(filmStorage.getRecommendedFilms(userId));
    }

    public Film createFilm(Film film) {
        film.setId(filmStorage.addFilm(film));
        return process(film);
    }

    public Film updateFilm(Film newFilm) {
        notNull(filmStorage.getFilm(newFilm.getId()));
        filmGenreRepository.deleteGenresFromFilm(newFilm.getId());
        return process(filmStorage.updateFilm(newFilm));
    }

    public void deleteFilm(Integer filmId){
        filmStorage.deleteFilm(filmId);
    }

    public void setLike(long id, long userId) {
        notNull(userStorage.getUser(userId));
        likeRepository.insert(id, userId);
        feedRepository.create(new Feed(userId, EventType.LIKE, Operation.ADD, id, Instant.now().toEpochMilli()));
    }

    public void deleteLike(long id, long userId) {
        notNull(userStorage.getUser(userId));
        likeRepository.delete(id, userId);
        feedRepository.create(new Feed(userId, EventType.LIKE, Operation.REMOVE, id, Instant.now().toEpochMilli()));
    }

    private void notNull(User user) {
        if (user == null) {
            throw new NoSuchElementException("Юзер не найден");
        }
    }

    private Film notNull(Film film) {
        if (film == null) {
            throw new NoSuchElementException("Фильм не найден");
        }
        return film;
    }

    private Film process(Film film) {
        if (Objects.nonNull(film.getGenres())) {
            filmGenreRepository.addGenresToFilm(film.getGenres().stream()
                    .map(genre -> new Object[]{film.getId(), genre.getId()})
                    .toList());
        }
        return prepare(film);
    }

    private Film prepare(Film film) {
        film.setGenres(new LinkedHashSet<>(genreRepository.getAllGenresByFilmId(film.getId())));
        film.getMpa().setName(mpaRepository.getMpa(film.getMpa().getId()).getName());
        return film;
    }

    private List<Film> prepare(Collection<Film> films) {
        if (films.isEmpty()) {
            return Collections.emptyList();
        }

        Collection<FilmGenre> filmGenres = filmGenreRepository.getAllGenres();
        Collection<Genre> genres = genreRepository.getAllGenres();
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
                    film.getMpa().setName(mpas.stream()
                            .filter(mpa -> film.getMpa().getId() == mpa.getId())
                            .findFirst().orElseThrow().getName());
                }).toList();
    }
}
