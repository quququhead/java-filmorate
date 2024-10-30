package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    @Autowired
    UserController userController;

    @Test
    void userShouldValidateAndCreate() {
        User user = new User("quququhead@gmail.com", "quhead",
                LocalDate.of(2001, 1, 20), 1, "Gleb");
        assertEquals(user, userController.createUser(user));
    }

    @Test
    void userShouldValidateAndUpdate() {
        User user1 = new User("quququhead@gmail.com", "quhead",
                LocalDate.of(2001, 1, 20), 1, "Gleb");
        User user2 = new User("bossshelby@yandex.ru", "bossshelby",
                LocalDate.of(1976, 5, 25), 1, "quququhead");
        userController.createUser(user1);
        assertEquals(user2, userController.updateUser(user2));
    }
}