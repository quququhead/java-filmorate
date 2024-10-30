package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.RatingMPARepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RatingMPAService {

    private final RatingMPARepository ratingMPARepository;

    public Collection<RatingMPA> findAllRatingMPAs() {
        return ratingMPARepository.getAllRatingMPAs();
    }

    public RatingMPA findRatingMPA(long ratingMPAId) {
        RatingMPA ratingMPA = ratingMPARepository.getRatingMPA(ratingMPAId);
        checkRatingMPANotNull(ratingMPA);
        return ratingMPA;
    }

    private void checkRatingMPANotNull(RatingMPA ratingMPA) {
        if (ratingMPA == null) {
            throw new NotFoundException("Рейтинг с не найден");
        }
    }
}
