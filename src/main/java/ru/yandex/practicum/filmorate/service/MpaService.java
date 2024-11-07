package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaRepository mpaRepository;

    public Collection<Mpa> findAllMpas() {
        return mpaRepository.getAllMpas();
    }

    public Mpa findMpa(long mpaId) {
        Mpa mpa = mpaRepository.getMpa(mpaId);
        if (mpa == null) {
            throw new NoSuchElementException("Рейтинг не найден");
        }
        return mpa;
    }
}
