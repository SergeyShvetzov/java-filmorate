package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

public class UserTest<T extends UserStorage> {

    private UserService userService;

    @BeforeEach
    public void setUp() {
        InMemoryUserStorage userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
    }

    @Test
    public void userReceiptCheck() {
        User user = new User(1, "email@", "login", "name", LocalDate.now(), null);
        userService.postUser(user);
        Collection<User> filmList = userService.getUsers();
        Assertions.assertNotNull(filmList);
        Assertions.assertEquals(1, filmList.size());
    }

    @Test
    public void gettingUserByIdShouldReturnOne() {
        User user = new User(1, "email@", "login", "name", LocalDate.now(), null);
        userService.postUser(user);
        User savedUser = userService.findUserById(1);
        Assertions.assertNotNull(savedUser, "Пользователь не возвращен.");
        Assertions.assertEquals(user, savedUser, "Пользователи не равны.");
    }

    @Test
    public void userUpdateAndCheckResult() {
        User user = new User(1, "email@", "login", "name", LocalDate.now(), null);
        userService.postUser(user);
        User user1 = new User(1, "email@", "login", "Sergey", LocalDate.now(), null);
        userService.updateUser(user1);
        User user2 = new User(3, "email@", "login", "Sergey", LocalDate.now(), null);
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(user2);
        });
        long userId = user.getId();
        long user1Id = user1.getId();
        Assertions.assertEquals(userId, user1Id);
        String filmName = user.getName();
        String film1Name = user1.getName();
        Assertions.assertNotEquals(user, user1);
        Assertions.assertNotEquals(filmName, film1Name);
    }

    @Test
    public void deletingAllUsersShouldReturnAnEmptyList() {
        User user = new User(1, "email@", "login", "name", LocalDate.now(), null);
        userService.postUser(user);
        User user2 = new User(2, "email.2@", "login 2", "name 2", LocalDate.now(), null);
        userService.postUser(user2);
        Collection<User> usersList = userService.getUsers();
        Assertions.assertEquals(2, usersList.size(), "Список не равен количеству пользователей");
        userService.deleteAllUsers();
        Assertions.assertEquals(0, usersList.size(), "Пользователи не удалены");
    }

    @Test
    public void deleteUserByIdTheListMustBeLessThanOneLong() {
        User user = new User(1, "email@", "login", "name", LocalDate.now(), null);
        userService.postUser(user);
        User user2 = new User(2, "email.2@", "login 2", "name 2", LocalDate.now(), null);
        userService.postUser(user2);
        Collection<User> usersList = userService.getUsers();
        Assertions.assertEquals(2, usersList.size(), "Список не равен количеству пользователей.");
        userService.deleteUserById(1);
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUserById(3);
        });
        Assertions.assertEquals(1, usersList.size(), "Пользователь не удален.");
    }

    @Test
    public void addingUserTwoAsAFriendToUserOne() {
        User user = new User(1, "email@", "login", "name", LocalDate.now(), null);
        userService.postUser(user);
        User user2 = new User(2, "email.2@", "login 2", "name 2", LocalDate.now(), null);
        userService.postUser(user2);
        userService.addInFriend(1, 2);
        Set<Long> friendsUser1 = user.getFriends();
        Assertions.assertEquals(1, friendsUser1.size(), "Количество друзей не совпадает.");
        Set<Long> friendsUser2 = user2.getFriends();
        Assertions.assertEquals(1, friendsUser2.size(), "Пользователь 1 не добавился " +
                "в друзья пользователю 2");
    }

    @Test
    public void unfriendingAnotherUser() {
        User user = new User(1, "email@", "login", "name", LocalDate.now(), null);
        userService.postUser(user);
        User user2 = new User(2, "email.2@", "login 2", "name 2", LocalDate.now(), null);
        userService.postUser(user2);
        userService.addInFriend(1, 2);
        Set<Long> friendsUser1 = user.getFriends();
        Assertions.assertEquals(1, friendsUser1.size(), "Количество друзей не совпадает.");
        userService.removeFromFriends(1, 2);
        Assertions.assertEquals(0, friendsUser1.size(), "Друг не был удален.");
        Set<Long> friendsUser2 = user2.getFriends();
        Assertions.assertEquals(0, friendsUser2.size(), "Пользователь 1 автоматически " +
                "не удалился из друзей пользователя 2");
    }

    @Test
    public void gettingListOfAllTheUsersFriends() {
        User user = new User(1, "email@", "login", "name", LocalDate.now(), null);
        userService.postUser(user);
        User user2 = new User(2, "email.2@", "login 2", "name 2", LocalDate.now(), null);
        userService.postUser(user2);
        userService.addInFriend(1, 2);
        Collection<User> userFriends = userService.getUserFriends(1);
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.getUserFriends(3);
        });
        Assertions.assertEquals(1, userFriends.size(), "Количество друзей не совпадает.");
    }

    @Test
    public void gettingListOfMutualFriendsOfAnotherUser() {
        User user = new User(1, "email@", "login", "name", LocalDate.now(), null);
        userService.postUser(user);
        User user2 = new User(2, "email.2@", "login 2", "name 2", LocalDate.now(), null);
        userService.postUser(user2);
        User user3 = new User(3, "email.3@", "login 3", "name 3", LocalDate.now(), null);
        userService.postUser(user3);
        User user4 = new User(4, "email.4@", "login 4", "name 4", LocalDate.now(), null);
        userService.postUser(user4);
        userService.addInFriend(1, 2);
        userService.addInFriend(1, 3);
        userService.addInFriend(2, 2);
        userService.addInFriend(2, 3);
        userService.addInFriend(2, 4);
        Collection<User> userFriends = userService.getListMutualFriends(1, 2);
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.getListMutualFriends(1, 5);
        });
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.getListMutualFriends(5, 1);
        });
        Assertions.assertEquals(2, userFriends.size(), "Количество общих друзей не совпадает.");
    }

    @Test
    public void userEmptyEmail() {
        User user = new User(1, "", "login", "name", LocalDate.now(), null);
        Assertions.assertThrows(ValidationException.class, () -> {
            userService.postUser(user);
        });
        User user1 = new User(1, "           ", "login", "name", LocalDate.now(), null);
        Assertions.assertThrows(ValidationException.class, () -> {
            userService.postUser(user1);
        });
    }

    @Test
    public void checkingEmailForSymbol() {
        User user = new User(1, "email", "login", "name",
                LocalDate.now().plusDays(1L), null);
        Assertions.assertThrows(ValidationException.class, () -> {
            userService.postUser(user);
        });
    }

    @Test
    public void loginMustNotBeEmptyOrContainSpaces() {
        User user = new User(1, "email@", "", "name",
                LocalDate.now().plusDays(1L), null);
        Assertions.assertThrows(ValidationException.class, () -> {
            userService.postUser(user);
        });
        User user1 = new User(1, "email@", "    ", "name",
                LocalDate.now().plusDays(1L), null);
        Assertions.assertThrows(ValidationException.class, () -> {
            userService.postUser(user1);
        });
    }

    @Test
    public void ifThNameIsEmptyThenItShouldBeTheSameAsTheLogin() {
        User user = new User(1, "email@", "login", "", LocalDate.now(), null);
        userService.postUser(user);
        Assertions.assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void checkDateOfBirthForFutureTense() {
        User user = new User(1, "email@", "login", "name",
                LocalDate.now().plusDays(1L), null);
        Assertions.assertThrows(ValidationException.class, () -> {
            userService.postUser(user);
        });
    }
}
