package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaRepository mpaRepository;

    public Collection<Mpa> findAllMpas() {
        return mpaRepository.getAllMpas();
    }

    public Mpa findMpa(long mpaId) {
        Mpa mpa = mpaRepository.getMpa(mpaId);
        checkMpaNotNull(mpa);
        return mpa;
    }

    private void checkMpaNotNull(Mpa mpa) {
        if (mpa == null) {
            throw new NotFoundException("Рейтинг с не найден");
        }
    }
}
