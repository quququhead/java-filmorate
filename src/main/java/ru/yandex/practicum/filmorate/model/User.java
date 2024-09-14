package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {

    @NotBlank(message = "email cannot be null, empty or blank")
    @Email(message = "email should be valid")
    private String email;

    @NotBlank(message = "login cannot be null, empty or blank")
    private String login;

    @NotNull(message = "birthday cannot be null")
    @PastOrPresent(message = "birthday cannot be in the future")
    private LocalDate birthday;

    private long id;
    private String name;
}
