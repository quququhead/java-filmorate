package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.PastDate;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class Film {
    @NotBlank(message = "description cannot be null, empty or blank")
    private String name;

    @NotBlank(message = "description cannot be null, empty or blank")
    @Size(max = 200, message = "description mustn't be over 200 characters")
    private String description;

    @PastDate("1895-12-28")
    private LocalDate releaseDate;

    @Positive
    private int duration;

    private long id;
    private Mpa mpa;
    private Set<Genre> genres = new LinkedHashSet<>();
    private Set<Director> directors = new LinkedHashSet<>();
}
