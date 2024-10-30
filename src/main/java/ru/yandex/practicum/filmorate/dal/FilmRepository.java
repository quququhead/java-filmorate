package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Repository
@Primary
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films (film_name, description, release_date, duration, rating_mpa_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET film_name = ?, description = ?, release_date = ?," +
            " duration = ?, rating_mpa_id = ? WHERE film_id = ?";

    private final FilmGenreRepository filmGenreRepository;
    private final GenreRepository genreRepository;
    private final LikeRepository likeRepository;
    private final RatingMPARepository ratingMPARepository;

    @Autowired
    public FilmRepository(JdbcTemplate jdbc,
                          RowMapper<Film> mapper,
                          FilmGenreRepository filmGenreRepository,
                          GenreRepository genreRepository,
                          LikeRepository likeRepository,
                          RatingMPARepository ratingMPARepository) {
        super(jdbc, mapper);
        this.filmGenreRepository = filmGenreRepository;
        this.genreRepository = genreRepository;
        this.likeRepository = likeRepository;
        this.ratingMPARepository = ratingMPARepository;
    }

    @Override
    public Collection<Film> getAllFilms() {
        Collection<Film> films = findMany(FIND_ALL_QUERY);
        if (!films.isEmpty()) {
            Collection<FilmGenre> filmGenres = filmGenreRepository.getAllGenres();
            Collection<Genre> genres = genreRepository.getAllGenres();
            Collection<Like> likes = likeRepository.getAllLikes();
            Collection<RatingMPA> ratingMPAs = ratingMPARepository.getAllRatingMPAs();
            for (Film film : films) {
                film.getGenres().addAll(filmGenres
                        .stream()
                        .filter(filmGenre -> Objects.equals(film.getId(), filmGenre.getFilmId()))
                        .map(filmGenre -> Genre.builder()
                                .id(filmGenre.getGenreId())
                                .name(genres
                                        .stream()
                                        .filter(genre -> genre.getId() == (filmGenre.getGenreId())).findFirst().get().getName())
                                .build())
                        .toList());
                film.getLikes().addAll(likes.stream().filter(like -> Objects.equals(like.getFilmId(), film.getId())).map(Like::getUserId).toList());
                film.getRatingMPA().setName(ratingMPAs.stream().filter(mpa -> Objects.equals(film.getRatingMPA().getId(), mpa.getId())).findFirst().get().getName());
            }
        }
        return films;
    }

    @Override
    public Film getFilm(long filmId) {
        Film film = findOne(FIND_BY_ID_QUERY, filmId);
        if (film != null) {
            Collection<Genre> genres = genreRepository.getAllGenres();
            film.getGenres().addAll(filmGenreRepository
                    .getAllGenres(film.getId())
                    .stream()
                    .map(filmGenre -> Genre.builder()
                            .id(filmGenre.getGenreId())
                            .name(genres.stream().filter(genre -> genre.getId() == (filmGenre.getGenreId())).findFirst().get().getName())
                            .build())
                    .toList());
            film.getLikes().addAll(likeRepository.getAllLikes(film.getId()).stream().map(Like::getUserId).toList());
            film.getRatingMPA().setName(ratingMPARepository.getRatingMPA(film.getRatingMPA().getId()).getName());
        }
        return film;
    }

    @Override
    public Film addFilm(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getRatingMPA().getId()
        );
        if (!film.getGenres().isEmpty()) {
            filmGenreRepository.addGenresToFilm(id, film.getGenres().stream().map(Genre::getId).toList());
        }
        film.setId(id);
        return film;
    }

    @Override
    public void setLike(long filmId, long userId) {
        likeRepository.addLikeToFilm(filmId, userId);
    }

    @Override
    public Film updateFilm(Film newFilm) {
        update(
                UPDATE_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                Date.valueOf(newFilm.getReleaseDate()),
                newFilm.getDuration(),
                newFilm.getRatingMPA().getId(),
                newFilm.getId()
        );
        if (!newFilm.getGenres().isEmpty()) {
            filmGenreRepository.deleteGenresFromFilm(newFilm.getId());
            filmGenreRepository.addGenresToFilm(newFilm.getId(), newFilm.getGenres().stream().map(Genre::getId).toList());
        }
        return newFilm;
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        likeRepository.deleteLikeFromFilm(filmId, userId);
    }
}
