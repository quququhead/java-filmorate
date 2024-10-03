package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private final Set<Long> friends = new HashSet<>();
    private String name;

    public void addFriend(Long id) {
        friends.add(id);
    }

    public void deleteFriend(Long id) {
        friends.remove(id);
    }
}
