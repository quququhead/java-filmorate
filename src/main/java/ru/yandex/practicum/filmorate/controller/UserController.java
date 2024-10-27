package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable Long id) {
        return userService.findUser(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findUserFriends(@PathVariable Long id) {
        return userService.findUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.findMutualFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.addFriend(id, friendId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) throws ValidationException {
        validate(user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) throws NotFoundException, ValidationException {
        validate(newUser);
        return userService.update(newUser);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteUserFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.deleteUserFriend(id, friendId);
    }

    private void validate(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
