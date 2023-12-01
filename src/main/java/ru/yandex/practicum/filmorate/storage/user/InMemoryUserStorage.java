package ru.yandex.practicum.filmorate.storage.user;

import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    @Generated
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);
    private final Map<Long, User> users = new HashMap<>();

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
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь с id %d не найден", userId)));
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

    private void validateUser(User user) {
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
            String message = String.format("Пользователь с id %s не был найден.", user.getId());
            log.info(message);
            throw new UserNotFoundException(message);
        } else {
            validateUser(user);
            users.put(user.getId(), user);
            String message = String.format("Данные пользователя c id - " + user.getId()
                    + " были успешно обновлены.", user.getId());
            log.info(message);
            return user;
        }
    }

    @Override
    public String deleteAllUsers() {
        log.info("Был вызван метод DELETE /users");
        users.clear();
        String message = "Все пользователи успешно удалены.";
        log.info(message);
        return message;
    }

    @Override
    public String deleteUserById(long userId) {
        log.info("Был вызван метод DELETE /users/user/{userId}.");
        if (!(users.containsKey(userId))) {
            String message = String.format("Пользователь № %s не найден", userId);
            log.error(message);
            throw new UserNotFoundException(message);
        }
        users.remove(userId);
        String message = String.format("Пользователь с id %s был успешно удален.", userId);
        log.info(message);
        return message;
    }

    public Collection<User> getUserFriends(long userId) {
        if (!(users.containsKey(userId))) {
            String message = String.format("Пользователь с id %s не зарегистрирован.", userId);
            log.error(message);
            throw new UserNotFoundException(message);
        }
        Set<Long> friends = users.get(userId).getFriends();
        return friends.stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    public Collection<User> getListMutualFriends(long id, long otherId) {
        if (!(users.containsKey(id))) {
            String message = String.format("Пользователь с id %s не зарегистрирован.", id);
            log.error(message);
            throw new UserNotFoundException(message);
        }
        if (!(users.containsKey(otherId))) {
            String message = String.format("Пользователь с id %s не зарегистрирован.", otherId);
            log.error(message);
            throw new UserNotFoundException(message);
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
