package ru.yandex.practicum.filmorate;

import java.time.LocalDate;
import java.util.Collection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

public class UserTest {
    private UserController userController;

    public UserTest() {
    }

    @BeforeEach
    public void setUp() {
        this.userController = new UserController();
    }

    @Test
    public void filmReceiptCheck() {
        User user = new User(1, "email@", "login", "name", LocalDate.now());
        this.userController.postUser(user);
        Collection<User> filmList = this.userController.getUsers();
        Assertions.assertNotNull(filmList);
        Assertions.assertEquals(1, filmList.size());
    }

    @Test
    public void filmUpdateAndCheckResult() {
        User user = new User(1, "email@", "login", "name", LocalDate.now());
        this.userController.postUser(user);
        User user1 = new User(1, "email@", "login", "Sergey", LocalDate.now().plusDays(1L));
        this.userController.updateUser(user1);
        int userId = user.getId();
        int user1Id = user1.getId();
        Assertions.assertEquals(userId, user1Id);
        String filmName = user.getName();
        String film1Name = user1.getName();
        Assertions.assertNotEquals(user, user1);
        Assertions.assertNotEquals(filmName, film1Name);
    }

    @Test
    public void userEmptyEmail() {
        User user = new User(1, "", "login", "name", LocalDate.now());
        Assertions.assertThrows(ValidationException.class, () -> {
            this.userController.postUser(user);
        });
        User user1 = new User(1, "           ", "login", "name", LocalDate.now());
        Assertions.assertThrows(ValidationException.class, () -> {
            this.userController.postUser(user1);
        });
    }

    @Test
    public void checkingEmailForSymbol() {
        User user = new User(1, "email", "login", "name", LocalDate.now().plusDays(1L));
        Assertions.assertThrows(ValidationException.class, () -> {
            this.userController.postUser(user);
        });
    }

    @Test
    public void loginMustNotBeEmptyOrContainSpaces() {
        User user = new User(1, "email@", "", "name", LocalDate.now().plusDays(1L));
        Assertions.assertThrows(ValidationException.class, () -> {
            this.userController.postUser(user);
        });
        User user1 = new User(1, "email@", "    ", "name", LocalDate.now().plusDays(1L));
        Assertions.assertThrows(ValidationException.class, () -> {
            this.userController.postUser(user1);
        });
    }

    @Test
    public void ifThNameIsEmptyThenItShouldBeTheSameAsTheLogin() {
        User user = new User(1, "email@", "login", "", LocalDate.now());
        this.userController.postUser(user);
        Assertions.assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void checkDateOfBirthForFutureTense() {
        User user = new User(1, "email@", "login", "name", LocalDate.now().plusDays(1L));
        Assertions.assertThrows(ValidationException.class, () -> {
            this.userController.postUser(user);
        });
    }
}