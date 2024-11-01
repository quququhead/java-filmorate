package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film getFilm(long filmId) {
        return films.get(filmId);
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void setLike(long id, long userId) {
        Film film = films.get(id);
        checkNotNull(film);
        film.addLike(userId);
    }

    @Override
    public Film updateFilm(Film newFilm) {
        Film oldFilm = films.get(newFilm.getId());
        checkNotNull(oldFilm);
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        return oldFilm;
    }

    @Override
    public void deleteLike(long id, long userId) {
        Film film = films.get(id);
        checkNotNull(film);
        film.deleteLike(userId);
    }

    private void checkNotNull(Film film) {
        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
