package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    @NotBlank(message = "email cannot be null, empty or blank")
    @Email(message = "email should be valid")
    private String email;

    @NotBlank(message = "login cannot be null, empty or blank")
    @Pattern(regexp = "\\S+")
    private String login;

    @NotNull(message = "birthday cannot be null")
    @PastOrPresent(message = "birthday cannot be in the future")
    private LocalDate birthday;

    private long id;
    private String name;

    public String getName() {
        return name == null || name.isBlank() ? login : name;
    }
}
