package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film implements Comparable<Film> {

    @NotBlank(message = "description cannot be null, empty or blank")
    private String name;

    @NotBlank(message = "description cannot be null, empty or blank")
    @Size(max = 200, message = "description mustn't be over 200 characters")
    private String description;

    @NotNull(message = "releaseDate cannot be null")
    private LocalDate releaseDate;

    @Positive
    private int duration;

    private long id;
    private RatingMPA ratingMPA;
    private final Set<Long> likes = new HashSet<>();
    private final Set<Genre> genres = new HashSet<>();

    public void addLike(Long id) {
        likes.add(id);
    }

    public void deleteLike(Long id) {
        likes.remove(id);
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void deleteGenre(Genre genre) {
        genres.remove(genre);
    }

    @Override
    public int compareTo(Film film) {
        return (this.getLikes().size() - film.getLikes().size()) * -1;
    }
}
