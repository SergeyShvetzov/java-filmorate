package ru.yandex.practicum.filmorate.storage.user;

import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    @Generated
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    public final Map<Long, User> users = new HashMap<>();

    private long generatorId = 1;


    @Override
    public Collection<User> getUsers() {
        log.info("Был вызван метод GET /users.");
        log.info("Текущее количество пользователей - " + users.keySet().size() + "\n");
        return users.values();
    }

    @Override
    public User findUserById(long userId) {
        return users.values().stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь № %d не найден", userId)));
    }

    @Override
    public User postUser(User user) {
        log.info("Был вызван метод POST /users.");
        validateUser(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Новый пользователь с логином - " + user.getLogin() + " успешно добавлен.");
        log.info("Текущее количество пользователей - " + users.keySet().size() + "\n");
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

    @Override
    public User updateUser(User user) {
        log.info("Был вызван метод PUT /users.");
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с id " + user.getId() + " не был найден.");
        } else {
            validateUser(user);
            users.put(user.getId(), user);
            log.info("Данные пользователя c id - " + user.getId() + " были успешно обновлены.\n");
            return user;
        }
    }

    @Override
    public String deleteAllUsers() {
        log.info("Был вызван метод DELETE /users");
        users.clear();
        return "Все пользователи удалены.";
    }

    @Override
    public String deleteUserById(long userId) {
        log.info("Был вызван метод DELETE /users/user/{userId}.");
        if (!(users.containsKey(userId))) {
            throw new UserNotFoundException(String.format("Пользователь № %s не найден", userId));
        }
        String savedFilmName = users.get(userId).getName();
        users.remove(userId);
        log.info("Пользователь с id - " + userId + " удален.");
        return String.format("Пользователь %s был успешно удален.", savedFilmName);
    }

    public User getUser(long userId) {
        if (!(users.containsKey(userId))) {
            throw new UserNotFoundException("Такого пользователя не существует");
        }
        return users.get(userId);
    }

    public Collection<User> getUserFriends(long userId) {
        if (!(users.containsKey(userId))) {
            throw new UserNotFoundException("Такого пользователя не существует");
        }
        Set<Long> friends = users.get(userId).getFriends();
        return friends.stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    public Collection<User> getListMutualFriends(long id, long otherId) {
        if (!(users.containsKey(id))) {
            throw new UserNotFoundException("Пользователя с id - " + id + " не существует");
        }
        if (!(users.containsKey(otherId))) {
            throw new UserNotFoundException("Пользователя с id - " + otherId + " не существует");
        }
        Set<Long> user = users.get(id).getFriends();
        Set<Long> otherUser = users.get(otherId).getFriends();
        if (user == null || otherUser == null) {
            return Collections.unmodifiableList(new ArrayList<>());
        }
        Set<Long> common = new HashSet<>(user);
        common.retainAll(otherUser);
        return common.stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    private long generateId() {
        return generatorId++;
    }
}
