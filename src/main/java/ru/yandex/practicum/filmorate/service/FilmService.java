package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.dal.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.dal.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;
import ru.yandex.practicum.filmorate.model.enums.SearchingMethod;
import ru.yandex.practicum.filmorate.model.enums.SortingMethod;

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
    private final FilmDirectorRepository filmDirectorRepository;
    private final DirectorRepository directorRepository;

    public Collection<Film> findAllFilms() {
        return prepare(filmStorage.getAllFilms());
    }

    public Film findFilm(long filmId) {
        return prepare(getFilmNotNull(filmId));
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

    public Collection<Film> findAllFilmsOfDirectorId(long directorId, String sortBy) {
        getDirectorNotNull(directorId);
        SortingMethod sortingMethod = SortingMethod.valueOf(sortBy.toUpperCase());
        return switch (sortingMethod) {
            case YEAR -> prepare(filmStorage.getAllFilmsOfDirectorSortedByYear(directorId));
            case LIKES -> prepare(filmStorage.getAllFilmsOfDirectorSortedByLikes(directorId));
        };
    }

    public Collection<Film> findAllFilmsBySearch(String query, List<String> by) {
        if (query.isBlank()) {
            return Collections.emptyList();
        }
        List<SearchingMethod> searchingMethods = by.stream()
                .map(String::toUpperCase)
                .map(SearchingMethod::searchBy)
                .toList();
        if (searchingMethods.contains(SearchingMethod.DIRECTOR) && searchingMethods.contains(SearchingMethod.TITLE)) {
            return prepare(filmStorage.getAllFilmsBySearchingOfDirectorAndTitle(query));
        }
        if (searchingMethods.contains(SearchingMethod.DIRECTOR)) {
            return prepare(filmStorage.getAllFilmsBySearchingOfDirector(query));
        }
        if (searchingMethods.contains(SearchingMethod.TITLE)) {
            return prepare(filmStorage.getAllFilmsBySearchingOfTitle(query));
        }
        throw new NoSuchElementException("Способ поиска не найден");
    }

    public Film createFilm(Film film) {
        film.setId(filmStorage.addFilm(film));
        return process(film);
    }

    public Film updateFilm(Film newFilm) {
        getFilmNotNull(newFilm.getId());
        filmGenreRepository.deleteGenresFromFilm(newFilm.getId());
        filmDirectorRepository.deleteDirectorsFromFilm(newFilm.getId());
        return process(filmStorage.updateFilm(newFilm));
    }

    public void deleteFilm(long filmId) {
        filmStorage.deleteFilm(filmId);
    }

    public void setLike(long id, long userId) {
        getUserNotNull(userId);
        likeRepository.insert(id, userId);
        feedRepository.create(new Feed(userId, EventType.LIKE, Operation.ADD, id, Instant.now().toEpochMilli()));
    }

    public void deleteLike(long id, long userId) {
        getUserNotNull(userId);
        likeRepository.delete(id, userId);
        feedRepository.create(new Feed(userId, EventType.LIKE, Operation.REMOVE, id, Instant.now().toEpochMilli()));
    }

    public Collection<Film> getCommonFilms(long userId, long friendId) {
        getUserNotNull(userId);
        getUserNotNull(friendId);
        return prepare(filmStorage.getCommonFilms(userId, friendId));
    }

    private void getUserNotNull(long userId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NoSuchElementException(String.format("Юзер с id {%s} не найден", userId));
        }
    }

    private Film getFilmNotNull(long filmId) {
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new NoSuchElementException(String.format("Фильм с id {%s} не найден", filmId));
        }
        return film;
    }

    private void getDirectorNotNull(long directorId) {
        Director director = directorRepository.getDirector(directorId);
        if (director == null) {
            throw new NoSuchElementException(String.format("Режиссер с id {%s} не найден", directorId));
        }
    }

    private Film process(Film film) {
        if (Objects.nonNull(film.getGenres())) {
            filmGenreRepository.addGenresToFilm(film.getGenres().stream()
                    .map(genre -> new Object[]{film.getId(), genre.getId()})
                    .toList());
        }
        if (Objects.nonNull(film.getDirectors())) {
            filmDirectorRepository.addDirectorsToFilm(film.getDirectors().stream()
                    .map(director -> new Object[]{film.getId(), director.getId()})
                    .toList());
        }
        return prepare(film);
    }

    private Film prepare(Film film) {
        film.setGenres(new LinkedHashSet<>(genreRepository.getAllGenresByFilmId(film.getId())));
        film.setDirectors(new LinkedHashSet<>(directorRepository.getAllDirectorsByFilmId(film.getId())));
        film.getMpa().setName(mpaRepository.getMpa(film.getMpa().getId()).getName());
        return film;
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
