package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.ReviewDislikeRepository;
import ru.yandex.practicum.filmorate.dal.ReviewLikeRepository;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;
import ru.yandex.practicum.filmorate.dal.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.dal.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewDislikeRepository reviewDislikeRepository;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public List<Review> findAllBy(long filmId, long count) {
        return reviewRepository.findAllBy(filmId, count);
    }

    public Review getReviewById(long id) {
        log.debug("getReviewById {}", id);
        return notNull(reviewRepository.findOneById(id));
    }

    public Review create(Review review) {
        log.debug("create review {}", review);
        notNull(filmStorage.getFilm(review.getFilmId()));
        notNull(userStorage.getUser(review.getUserId()));
        review.setReviewId(reviewRepository.create(review));
        return review;
    }

    public Review update(Review review) {
        log.debug("update review {}", review);
        reviewRepository.update(review);
        return notNull(reviewRepository.findOneById(review.getReviewId()));
    }

    public void delete(long id) {
        log.debug("delete review {}", id);
        reviewRepository.delete(id);
    }

    public void addLike(long reviewId, long userId) {
        log.debug("{} addLike {}", reviewId, userId);
        reviewLikeRepository.insert(reviewId, userId);
        if (reviewDislikeRepository.exists(reviewId, userId) != 0) {
            removeDislike(reviewId, userId);
        }
    }

    public void removeLike(long reviewId, long userId) {
        log.debug("{} removeLike {}", reviewId, userId);
        reviewLikeRepository.delete(reviewId, userId);
    }

    public void addDislike(long reviewId, long userId) {
        log.debug("{} addDislike {}", reviewId, userId);
        reviewDislikeRepository.insert(reviewId, userId);
        if (reviewLikeRepository.exists(reviewId, userId) != 0) {
            removeLike(reviewId, userId);
        }
    }

    public void removeDislike(long reviewId, long userId) {
        log.debug("{} removeDislike {}", reviewId, userId);
        reviewDislikeRepository.delete(reviewId, userId);
    }

    private Review notNull(Review review) {
        if (review == null) {
            throw new NoSuchElementException("Отзыв не найден");
        }
        return review;
    }

    private void notNull(Film film) {
        if (film == null) {
            throw new NoSuchElementException("Фильм не найден");
        }
    }

    private void notNull(User user) {
        if (user == null) {
            throw new NoSuchElementException("Юзер не найден");
        }
    }
}
