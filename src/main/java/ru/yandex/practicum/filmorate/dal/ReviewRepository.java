package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.interfaces.BaseRepository;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Repository
public class ReviewRepository extends BaseRepository<Review> {
    private static final String FIND_ONE_BY_ID_QUERY =
            "SELECT r.*, COUNT(l.user_id) - COUNT(d.user_id) AS useful " +
            "FROM reviews AS r " +
            "LEFT JOIN reviews_likes AS l ON l.review_id = r.id " +
            "LEFT JOIN reviews_dislikes AS d ON d.review_id = r.id " +
            "WHERE r.id = ? " +
            "GROUP BY r.id ";
    private static final String FIND_BY_FILM_AND_COUNT_QUERY =
            "SELECT r.*, COUNT(l.user_id) - COUNT(d.user_id) AS useful " +
            "FROM reviews AS r " +
            "LEFT JOIN reviews_likes AS l ON l.review_id = r.id " +
            "LEFT JOIN reviews_dislikes AS d ON d.review_id = r.id " +
            "%s" +
            "GROUP BY r.id " +
            "ORDER BY useful DESC " +
            "LIMIT ?";
    private static final String INSERT_QUERY =
            "INSERT INTO reviews (content, is_positive, user_id, film_id) " +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY =
            "UPDATE reviews " +
            "SET content = ?, is_positive = ? " +
            "WHERE id = ?";
    private static final String DELETE_QUERY =
            "DELETE FROM reviews WHERE id = ?";

    public ReviewRepository(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    public Review findOneById(long id) {
        return findOne(FIND_ONE_BY_ID_QUERY, id);
    }

    public List<Review> findAllBy(long filmId, long count) {
        Object[] params;
        StringBuilder sb = new StringBuilder();
        if (filmId == 0) {
            params = new Object[]{count};
        } else {
            sb.append("WHERE r.film_id = ? ");
            params = new Object[]{filmId, count};
        }
        return findMany(String.format(FIND_BY_FILM_AND_COUNT_QUERY, sb), params);
    }

    public long create(Review review) {
        return insert(
                INSERT_QUERY,
                review.content(),
                review.isPositive(),
                review.userId(),
                review.filmId()
        );
    }

    public void update(Review review) {
        update(
                UPDATE_QUERY,
                review.content(),
                review.isPositive(),
                review.reviewId()
        );
    }

    public void delete(long id) {
        delete(DELETE_QUERY, id);
    }
}
