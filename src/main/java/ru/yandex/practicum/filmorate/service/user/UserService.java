package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addInFriend(long userId, long friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        user.setFriends(friendId);
        friend.setFriends(userId);
        return userStorage.updateUser(user);
    }

    public User removeFromFriends(long userId, long friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
        return userStorage.updateUser(user);
    }

    public Collection<User> getUserFriends(long id) {
        return userStorage.getUserFriends(id);
    }

    public Collection<User> getListMutualFriends(long id, long otherId) {
        return userStorage.getListMutualFriends(id, otherId);
    }
}
