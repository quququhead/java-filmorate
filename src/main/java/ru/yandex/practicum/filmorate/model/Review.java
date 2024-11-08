package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Review(
        Long reviewId,
        @NotBlank String content,
        @NotNull Boolean isPositive,
        @NotNull Long userId,
        @NotNull Long filmId,
        Long useful
) {
}
