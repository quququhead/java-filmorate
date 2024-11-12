package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.DirectorRepository;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorRepository directorRepository;

    public Collection<Director> findAllDirectors() {
        return directorRepository.getAllDirectors();
    }

    public Director findDirector(long directorId) {
        return notNull(directorRepository.getDirector(directorId));
    }

    public Director createDirector(Director director) {
        director.setId(directorRepository.addDirector(director));
        return director;
    }

    public Director updateDirector(Director newDirector) {
        return directorRepository.updateDirector(newDirector);
    }

    public void deleteDirector(long directorId) {
        directorRepository.deleteDirector(directorId);
    }

    private Director notNull(Director director) {
        if (director == null) {
            throw new NoSuchElementException("Режиссер не найден");
        }
        return director;
    }
}
