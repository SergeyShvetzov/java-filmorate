package ru.yandex.practicum.filmorate.service.user;

import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
public class UserService implements UserStorage {

    @Generated
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addInFriend(long userId, long friendId) {
        log.info("Был вызван метод PUT /{id}/friends/{friendId}");
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        user.setFriends(friendId);
        friend.setFriends(userId);
        log.info(String.format("Друг с id %s успешно добавлен в друзья.", friend.getId()));
        return userStorage.updateUser(user);
    }

    public User removeFromFriends(long userId, long friendId) {
        log.info("Был вызван метод DELETE /{id}/friends/{friendId}");
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
        log.info(String.format("Друг с id %s успешно удален из друзей.", friend.getId()));
        return userStorage.updateUser(user);
    }

    public Collection<User> getUserFriends(long id) {
        log.info("Был вызван метод GET /{id}/friends");
        return userStorage.getUserFriends(id);
    }

    public Collection<User> getListMutualFriends(long id, long otherId) {
        log.info("Был вызван метод GET /{id}/friends/common/{otherId}");
        return userStorage.getListMutualFriends(id, otherId);
    }

    @Override
    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    @Override
    public User findUserById(long userId) {
        return userStorage.findUserById(userId);
    }

    @Override
    public User postUser(User user) {
        return userStorage.postUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public String deleteAllUsers() {
        return userStorage.deleteAllUsers();
    }

    @Override
    public String deleteUserById(long userId) {
        return userStorage.deleteUserById(userId);
    }
}
