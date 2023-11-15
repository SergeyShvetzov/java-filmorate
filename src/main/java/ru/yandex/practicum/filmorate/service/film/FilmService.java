package ru.yandex.practicum.filmorate.service.film;

import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IncorrectCountException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService implements FilmStorage {

    @Generated
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film putLikeFilm(long filmId, long userId) {
        log.info("Был вызван метод PUT /films/{id}/like/{userId}");
        Film film = filmStorage.findFilmById(filmId);
        if (userId < 0) {
            String message = "Введен некорректный id пользователя";
            log.info(message);
            throw new UserNotFoundException(message);
        }
        film.setLikes(userId);
        log.info(String.format("Лайк пользователя с id %s к фильму %s успешно добавлен.", userId, film.getName()));
        return film;
    }

    public Film deleteLike(long filmId, long userId) {
        log.info("Был вызван метод DELETE /films/{id}/like/{userId}");
        Film film = filmStorage.findFilmById(filmId);
        if (userId < 0) {
            String message = "Введен некорректный id пользователя";
            log.info(message);
            throw new UserNotFoundException(message);
        }
        film.deleteLike(userId);
        log.info(String.format("Лайк пользователя с id %s к фильму %s успешно удален.", userId, film.getName()));
        return film;
    }

    public Collection<Film> getFilmCountLikes(Integer count) {
        log.info("Был вызван метод GET /popular?count={count}");
        if (count == null) {
            String message = "Параметр count равен null.";
            log.error(message);
            throw new IncorrectCountException(message);
        }
        if (count <= 0) {
            String message = "Параметр count имеет отрицательное значение.";
            log.error(message);
            throw new IncorrectCountException(message);
        }
        List<Film> films = new ArrayList<>(filmStorage.getFilm());
        return films.stream()
                .sorted((Comparator.comparingInt(Film::getLikesCount).reversed()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Film> getFilm() {
        return filmStorage.getFilm();
    }

    @Override
    public Film findFilmById(long filmId) {
        return filmStorage.findFilmById(filmId);
    }

    @Override
    public Film postFilm(Film film) {
        return filmStorage.postFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public String deleteAllFilms() {
        return filmStorage.deleteAllFilms();
    }

    @Override
    public String deleteFilmById(long filmId) {
        return filmStorage.deleteFilmById(filmId);
    }
}