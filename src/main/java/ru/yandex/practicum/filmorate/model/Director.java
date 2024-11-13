package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Director {
    private long id;
    @NotBlank(message = "email cannot be null, empty or blank")
    private String name;
}
