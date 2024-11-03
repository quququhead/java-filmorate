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

        User user1 = new User();
        user1.setEmail("quququhead@gmail.com");
        user1.setLogin("quhead");
        user1.setBirthday(LocalDate.of(2001, 1, 20));
        user1.setId(1);
        user1.setName("Gleb");

        User user2 = new User();
        user2.setEmail("bossshelby@yandex.ru");
        user2.setLogin("bossshelby");
        user2.setBirthday(LocalDate.of(1976, 5, 25));
        user2.setId(2);
        user2.setName("quququhead");

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
    void shouldGetAllUsers() {
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

        User user = new User();
        user.setEmail("bob@gmail.com");
        user.setLogin("bigbob");
        user.setBirthday(LocalDate.of(2001, 4, 6));
        user.setId(3);
        user.setName("bob");

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
        User user = new User();
        user.setEmail("bob@gmail.com");
        user.setLogin("bigbob");
        user.setBirthday(LocalDate.of(2001, 4, 6));
        user.setId(3);
        user.setName("bob");
        userRepository.addUser(user);
        assertEquals(1, userRepository.getAllUsers().size());
    }

    @Test
    void shouldUpdateUser() {
        createUsers();
        User user = myUsers.getFirst();
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
