package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import ru.yandex.practicum.filmorate.model.Film;

@RestController
@RequestMapping({"/film"})
public class FilmController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, Film> films = new HashMap();

    public FilmController() {
    }

    @GetMapping
    public Collection<Film> getFilm() {
        log.info("Был вызван метод GET /film.");
        log.info("Текущее количество фильмов - " + this.films.keySet().size() + "\n");
        return this.films.values();
    }

    @PostMapping
    public Film postFilm(@RequestBody Film film) {
        log.info("Был вызван метод POST /film.");
        validateFilm(film);
        this.films.put(film.getId(), film);
        log.info("Новый фильм - " + film.getName() + " успешно добавлен.");
        log.info("Текущее количество фильмов - " + this.films.keySet().size() + "\n");
        return film;
    }

    private static void validateFilm(Film film) {
        String message;
        if (film.getName() != null && !film.getName().isBlank()) {
            if (film.getDescription().length() > 200) {
                message = "Максимальная длина описания больше 200 символов ";
                log.error(message);
                throw new ValidationException(message + "символов. Длина описания - " + film.getDescription().length());
            } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                message = "Указанная дата релиза раньше 28 декабря 1895 года. ";
                log.error(message);
                throw new ValidationException(message + "Указанная дата - " + film.getReleaseDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            } else if (film.getDuration() < 0) {
                message = "Продолжительность фильма меньше 0";
                log.error(message);
                throw new ValidationException(message);
            }
        } else {
            message = "Указано пустое название фильма.";
            log.error(message);
            throw new ValidationException(message);
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Был вызван метод PUT /film.");
        if (!this.films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с id " + film.getId() + " не был найден.");
        } else {
            this.films.put(film.getId(), film);
            log.info("Фильм c id - " + film.getId() + " был успешно обновлен.\n");
            return film;
        }
    }
}
