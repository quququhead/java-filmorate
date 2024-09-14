package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {

    @NotBlank(message = "description cannot be null, empty or blank")
    private String name;

    @NotBlank(message = "description cannot be null, empty or blank")
    @Size(max = 200, message = "description mustnot be over 200 characters")
    private String description;

    @NotNull(message = "releaseDate cannot be null")
    private LocalDate releaseDate;

    @PositiveOrZero
    private int duration;

    private long id;
}
