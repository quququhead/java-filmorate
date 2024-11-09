package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmGenreRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.LikeRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dal.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.dal.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

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

    public Collection<Film> findAllFilms() {
        return prepare(filmStorage.getAllFilms());
    }

    public Collection<Film> findTopFilms(Integer count) {
        return prepare(filmStorage.getAllFilmsBy(count));
    }

    public Film findFilm(long filmId) {
        Film film = filmStorage.getFilm(filmId);
        checkFilmNotNull(film);
        return prepare(film);
    }

    public Film createFilm(Film film) {
        film.setId(filmStorage.addFilm(film));
        return process(film);
    }

    public Film updateFilm(Film newFilm) {
        checkFilmNotNull(filmStorage.getFilm(newFilm.getId()));
        filmGenreRepository.deleteGenresFromFilm(newFilm.getId());
        return process(filmStorage.updateFilm(newFilm));
    }

    public void setLike(long id, long userId) {
        userStorage.getUser(userId);
        likeRepository.insert(id, userId);
    }

    public void deleteLike(long id, long userId) {
        userStorage.getUser(userId);
        likeRepository.delete(id, userId);
    }

    private void checkFilmNotNull(Film film) {
        if (film == null) {
            throw new NoSuchElementException("Фильм не найден");
        }
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
