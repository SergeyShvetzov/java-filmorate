package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Data
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends;

    public Set<Long> getFriends() {
        return friends;
    }

    public void setFriends(long friendId) {
        if (friends == null) {
            friends = new HashSet<>();
        }
        friends.add(friendId);
    }

    public void deleteFriend(long friendId) {
        friends.remove(friendId);
    }
}
