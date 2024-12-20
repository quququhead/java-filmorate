package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FeedRepository;
import ru.yandex.practicum.filmorate.dal.ReviewDislikeRepository;
import ru.yandex.practicum.filmorate.dal.ReviewLikeRepository;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;
import ru.yandex.practicum.filmorate.dal.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.dal.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final FeedRepository feedRepository;
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
        return getReviewNotNull(id);
    }

    public Review create(Review review) {
        log.debug("create review {}", review);
        filmExists(review.filmId());
        userExists(review.userId());
        long reviewId = reviewRepository.create(review);
        feedRepository.create(new Feed(review.userId(), EventType.REVIEW, Operation.ADD, reviewId, Instant.now().toEpochMilli()));
        return new Review(
                reviewId,
                review.content(),
                review.isPositive(),
                review.userId(),
                review.filmId(),
                review.useful()
        );
    }

    public Review update(Review review) {
        log.debug("update review {}", review);
        reviewRepository.update(review);
        Review updatedReview = getReviewNotNull(review.reviewId());
        feedRepository.create(new Feed(updatedReview.userId(), EventType.REVIEW, Operation.UPDATE, updatedReview.reviewId(), Instant.now().toEpochMilli()));
        return updatedReview;
    }

    public void delete(long id) {
        log.debug("delete review {}", id);
        Review review = getReviewNotNull(id);
        feedRepository.create(new Feed(review.userId(), EventType.REVIEW, Operation.REMOVE, id, Instant.now().toEpochMilli()));
        reviewRepository.delete(id);
    }

    public void addLike(long reviewId, long userId) {
        log.debug("{} addLike {}", reviewId, userId);
        entitiesExist(reviewId, userId);
        reviewLikeRepository.insert(reviewId, userId);
        if (reviewDislikeRepository.exists(reviewId, userId) != 0) {
            removeDislike(reviewId, userId);
        }
    }

    public void removeLike(long reviewId, long userId) {
        log.debug("{} removeLike {}", reviewId, userId);
        entitiesExist(reviewId, userId);
        reviewLikeRepository.delete(reviewId, userId);
    }

    public void addDislike(long reviewId, long userId) {
        log.debug("{} addDislike {}", reviewId, userId);
        entitiesExist(reviewId, userId);
        reviewDislikeRepository.insert(reviewId, userId);
        if (reviewLikeRepository.exists(reviewId, userId) != 0) {
            removeLike(reviewId, userId);
        }
    }

    public void removeDislike(long reviewId, long userId) {
        log.debug("{} removeDislike {}", reviewId, userId);
        entitiesExist(reviewId, userId);
        reviewDislikeRepository.delete(reviewId, userId);
    }

    private void entitiesExist(long reviewId, long userId) {
        getReviewNotNull(reviewId);
        userExists(userId);
    }

    private Review getReviewNotNull(long reviewId) {
        Review review = reviewRepository.findOneById(reviewId);
        if (review == null) {
            throw new NoSuchElementException(String.format("Отзыв с id {%s} не найден", reviewId));
        }
        return review;
    }

    private void filmExists(long filmId) {
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new NoSuchElementException(String.format("Фильм с id {%s} не найден", filmId));
        }
    }

    private void userExists(long userId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NoSuchElementException(String.format("Юзер с id {%s} не найден", userId));
        }
    }
}
