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
    private Set<Long> likes;
    private Integer likesCount;

    public Film() {
        this.likesCount = 0;
    }

    public void setLikes(Long userId) {
        if (likes == null) {
            likes = new HashSet<>();
            setLikesCount(0);
        }
        likes.add(userId);
        setLikesCount(likes.size());
    }

    public void deleteLike(Long userId) {
        likes.remove(userId);
        likesCount--;
    }
}
