package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Был вызван метод GET /user.");
        log.info("Текущее количество пользователей - " + users.keySet().size() + "\n");
        return users.values();
    }

    /*
    - электронная почта не может быть пустой и должна содержать символ @; !!! Сделано !!!
    - логин не может быть пустым и содержать пробелы; !!! Сделано !!!
    - имя для отображения может быть пустым — в таком случае будет использован логин; !!! Сделано !!!
    - дата рождения не может быть в будущем. !!! Сделано !!!
    */
    @PostMapping
    public User postUser(@RequestBody User user) {

        log.info("Был вызван метод POST /user.");

        if (user.getEmail().isBlank() || user.getEmail() == null) {
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }

        if (!(user.getEmail().contains("@"))) {
            throw new ValidationException("Адрес электронной почты должен содержать символ '@'. " +
                    "Ваш адрес - " + user.getEmail());
        }

        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым или содержать пробелы.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем времени. " +
                    "Указанная дата - " + user.getBirthday());
        }
        users.put(user.getId(), user);

        log.info("Новый пользователь с логином - " + user.getLogin() + " успешно добавлен.");
        log.info("Текущее количество пользователей - " + users.keySet().size() + "\n");

        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {

        log.info("Был вызван метод PUT /user.");

        if (!(users.containsKey(user.getId()))) {
            throw new ValidationException("Пользователь с id " + user.getId() + " не был найден.");
        }
        users.put(user.getId(), user);

        log.info("Данные пользователя c id - " + user.getId() + " были успешно обновлены.\n");

        return user;
    }
}
