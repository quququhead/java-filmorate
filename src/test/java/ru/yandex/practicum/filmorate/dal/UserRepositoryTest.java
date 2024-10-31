package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mappers.FriendRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FriendRepository.class, FriendRowMapper.class,
        UserRepository.class, UserRowMapper.class
})
class UserRepositoryTest {
    private final UserRepository userRepository;
    private final List<User> myUsers = new ArrayList<>();

    private void createUsers() {
        myUsers.clear();
        User user1 = new User("quququhead@gmail.com", "quhead",
                LocalDate.of(2001, 1, 20), 1, "Gleb");
        User user2 = new User("bossshelby@yandex.ru", "bossshelby",
                LocalDate.of(1976, 5, 25), 2, "quququhead");
        user1 = userRepository.addUser(user1);
        user2 = userRepository.addUser(user2);
        myUsers.add(user1);
        myUsers.add(user2);
    }

    @Test
    void shouldGetEmptyUsers() {
        Collection<User> users = userRepository.getAllUsers();
        assertTrue(users.isEmpty());
    }

    @Test
    void shouldGetUsers() {
        createUsers();
        Collection<User> users = userRepository.getAllUsers();
        assertEquals(2, users.size());
        assertEquals(myUsers.get(0).getName(), users.stream().toList().get(0).getName());
    }

    @Test
    void shouldGetNullUser() {
        User user = userRepository.getUser(1);
        assertNull(user);
    }

    @Test
    void shouldGetUser() {
        createUsers();
        User user = userRepository.getUser(myUsers.get(0).getId());
        assertNotNull(user);
        assertEquals(myUsers.get(0).getName(), user.getName());
    }

    @Test
    void shouldGetUserFriends() {
        createUsers();
        userRepository.addUserFriend(myUsers.get(0).getId(), myUsers.get(1).getId());
        Collection<User> users = userRepository.getUserFriends(myUsers.get(0).getId());
        assertEquals(1, users.size());
    }

    @Test
    void shouldGetMutualFriends() {
        createUsers();
        User user = new User("bob@gmail.com", "bigbob",
                LocalDate.of(2001, 4, 6), 3, "bob");
        user = userRepository.addUser(user);
        userRepository.addUserFriend(myUsers.get(0).getId(), myUsers.get(1).getId());
        userRepository.addUserFriend(user.getId(), myUsers.get(1).getId());
        Collection<User> users = userRepository.getMutualFriends(myUsers.get(0).getId(), user.getId());
        assertEquals(1, users.size());
    }

    @Test
    void shouldAddUserFriend() {
        createUsers();
        userRepository.addUserFriend(myUsers.get(0).getId(), myUsers.get(1).getId());
        assertEquals(1, userRepository.getUser(myUsers.get(0).getId()).getFriends().size());
        assertEquals(0, userRepository.getUser(myUsers.get(1).getId()).getFriends().size());
    }

    @Test
    void shouldAddUser() {
        User user = new User("bob@gmail.com", "bigbob",
                LocalDate.of(2001, 4, 6), 3, "bob");
        userRepository.addUser(user);
        assertEquals(1, userRepository.getAllUsers().size());
    }

    @Test
    void shouldUpdateUser() {
        createUsers();
        User user = myUsers.get(0);
        user.setEmail("bob@gmail.com");
        user.setLogin("bigbob");
        user.setBirthday(LocalDate.of(2001, 4, 6));
        user.setName("bob");
        userRepository.updateUser(user);
        assertEquals(user, userRepository.getUser(user.getId()));
    }

    @Test
    void shouldDeleteUserFriend() {
        createUsers();
        userRepository.addUserFriend(myUsers.get(0).getId(), myUsers.get(1).getId());
        userRepository.deleteUserFriend(myUsers.get(0).getId(), myUsers.get(1).getId());
        assertEquals(0, userRepository.getUser(myUsers.get(0).getId()).getFriends().size());
        assertEquals(0, userRepository.getUser(myUsers.get(1).getId()).getFriends().size());
    }
}
