package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public Collection<Genre> findAllGenres() {
        return genreRepository.getAllGenres();
    }

    public Genre findGenre(long genreId) {
        Genre genre = genreRepository.getGenre(genreId);
        checkGenreNotNull(genre);
        return genre;
    }

    private void checkGenreNotNull(Genre genre) {
        if (genre == null) {
            throw new NotFoundException("Жанр с не найден");
        }
    }
}
