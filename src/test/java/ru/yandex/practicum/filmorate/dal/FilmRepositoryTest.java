package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmRepository.class, FilmRowMapper.class})
class FilmRepositoryTest {
    private final FilmRepository filmRepository;

    private Film createFilm() {
        Film film = new Film();
        film.setName("Fight Club");
        film.setDescription("Film about the unnamed Narrator.");
        film.setReleaseDate(LocalDate.of(1999, 9, 10));
        film.setDuration(139);
        film.setId(1);
        film.setMpa(Mpa.builder().id(1).name("R").build());
        return film;
    }

    @Test
    void shouldGetEmptyFilms() {
        Collection<Film> films = filmRepository.getAllFilms();
        assertTrue(films.isEmpty());
    }

    @Test
    void shouldGetAllFilms() {
        Film film = createFilm();
        filmRepository.addFilm(film);
        Collection<Film> films = filmRepository.getAllFilms();
        assertEquals(1, films.size());
    }

    @Test
    void shouldGetNullFilm() {
        Film film = filmRepository.getFilm(1);
        assertNull(film);
    }

    @Test
    void shouldGetFilm() {
        Film film1 = createFilm();
        Film film2 = filmRepository.getFilm(filmRepository.addFilm(film1));
        assertEquals(film1.getName(), film2.getName());
    }

    @Test
    void shouldAddFilm() {
        Film film = createFilm();
        assertNotEquals(filmRepository.addFilm(film), film.getId());
    }

    @Test
    void shouldUpdateFilm() {
        Film film1 = createFilm();
        film1.setId(filmRepository.addFilm(film1));
        film1.setName("American Psycho");
        film1.setDescription("Film about Patrick Bateman");
        film1.setReleaseDate(LocalDate.of(2000, 1, 21));
        film1.setDuration(102);
        filmRepository.updateFilm(film1);
        Film film2 = filmRepository.getFilm(film1.getId());
        assertEquals(film1.getName(), film2.getName());
        assertEquals(film1.getDescription(), film2.getDescription());
        assertEquals(film1.getReleaseDate(), film2.getReleaseDate());
        assertEquals(film1.getDuration(), film2.getDuration());
    }
}
