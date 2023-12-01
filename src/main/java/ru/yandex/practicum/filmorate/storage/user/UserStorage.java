package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> getUsers(); // отправляет список всех пользователей

    User findUserById(@PathVariable long userId); // отправляет пользователя по id

    User postUser(@RequestBody User user); // добавляет пользователя

    User updateUser(@RequestBody User user); // обновляет данные пользователя

    String deleteAllUsers(); // удалет список всех пользователей

    String deleteUserById(@PathVariable long userId);// удаляет пользователя по id

    Collection<User> getUserFriends(long userId); //  получение списка друзей пользователя

    Collection<User> getListMutualFriends(long id, long otherId); // получение списка общего списка друзей другого пользователя
}
