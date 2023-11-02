package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@RestController
@RequestMapping({"/user"})
public class UserController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> users = new HashMap<>();

    public UserController() {
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Был вызван метод GET /user.");
        log.info("Текущее количество пользователей - " + this.users.keySet().size() + "\n");
        return this.users.values();
    }

    @PostMapping
    public User postUser(@RequestBody User user) {
        log.info("Был вызван метод POST /user.");
        validateUser(user);
        this.users.put(user.getId(), user);
        log.info("Новый пользователь с логином - " + user.getLogin() + " успешно добавлен.");
        log.info("Текущее количество пользователей - " + this.users.keySet().size() + "\n");
        return user;
    }

    private static void validateUser(User user) {
        String message;
        if (!user.getEmail().isBlank() && user.getEmail() != null) {
            if (!user.getEmail().contains("@")) {
                message = "Адрес электронной почты должен содержать символ '@'. ";
                log.error(message);
                throw new ValidationException(message + "Ваш адрес - " + user.getEmail());
            } else if (user.getLogin() != null && !user.getLogin().isBlank()) {
                if (user.getName() == null || user.getName().isBlank()) {
                    user.setName(user.getLogin());
                }

                if (user.getBirthday().isAfter(LocalDate.now())) {
                    message = "Дата рождения указана в будущем времени. ";
                    log.error(message);
                    throw new ValidationException(message + "Указанная дата - " + user.getBirthday());
                }
            } else {
                message = "Логин не может быть пустым или содержать пробелы.";
                log.error(message);
                throw new ValidationException(message);
            }
        } else {
            message = "Адрес электронной почты не может быть пустым.";
            log.error(message);
            throw new ValidationException(message);
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Был вызван метод PUT /user.");
        if (!this.users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с id " + user.getId() + " не был найден.");
        } else {
            this.users.put(user.getId(), user);
            log.info("Данные пользователя c id - " + user.getId() + " были успешно обновлены.\n");
            return user;
        }
    }
}