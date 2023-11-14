package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@Data
public class Film {

    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<String> likes;
    private Integer likesCount;

    public Film() {
        this.likesCount = 0;
    }

    public void setLikes(String userEmail) {
        if (likes == null) {
            likes = new HashSet<>();
            setLikesCount(0);
        }
        likes.add(userEmail);
        setLikesCount(likes.size());
    }

    public void deleteLike(String userEmail) {
        likes.remove(userEmail);
    }
}
