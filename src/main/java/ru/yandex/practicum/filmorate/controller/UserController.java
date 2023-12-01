package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // получение списка всех пользователей
    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    // получение пользователя по id
    @GetMapping("/{userId}")
    public User findUserById(@PathVariable long userId) {
        return userService.findUserById(userId);
    }

    // добавление пользователя
    @PostMapping
    public User postUser(@RequestBody User user) {
        return userService.postUser(user);
    }

    // обновление данных пользователя
    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    // удалить всех пользователей
    @DeleteMapping
    public String deleteAllUsers() {
        return userService.deleteAllUsers();
    }

    // удалить пользователя по id
    @DeleteMapping("/user/{userId}")
    public String deleteUserById(@PathVariable long userId) {
        return userService.deleteUserById(userId);
    }

    // PUT /users/{id}/friends/{friendId} — добавление в друзья.
    @PutMapping("/{id}/friends/{friendId}")
    public User addInFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.addInFriend(id, friendId);
    }

    // DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFromFriends(@PathVariable long id, @PathVariable long friendId) {
        return userService.removeFromFriends(id, friendId);
    }

    // GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    @GetMapping("/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable long id) {
        return userService.getUserFriends(id);
    }

    // GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getListMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getListMutualFriends(id, otherId);
    }
}