package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/film")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilm() {
        log.info("Был вызван метод GET /film.");
        log.info("Текущее количество фильмов - " + films.keySet().size() + "\n");
        return films.values();
    }


    /*
    Проверьте данные, которые приходят в запросе на добавление нового фильма или пользователя.
    Эти данные должны соответствовать определённым критериям.

    - название не может быть пустым  !!! Сделано !!!
    - максимальная длина описания — 200 символов; !!! Сделано !!!
    - дата релиза — не раньше 28 декабря 1895 года; !!! Сделано !!!
    - продолжительность фильма должна быть положительной. !!! Сделано !!!
    */
    @PostMapping
    public Film postFilm(@RequestBody Film film) {

        log.info("Был вызван метод POST /film.");

        if (film.getName() == null || film.getName().isBlank()) {

            throw new ValidationException("Название фильма не может быть пустым.");
        }

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания фильма не может превышать 200 " +
                    "символов. Длина описания - " + film.getDescription().length());
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года. " +
                    "Указанная дата - " + film.getReleaseDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        }

        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма не может быть меньше 0.");
        }

        films.put(film.getId(), film);

        log.info("Новый фильм - " + film.getName() + " успешно добавлен.");
        log.info("Текущее количество фильмов - " + films.keySet().size() + "\n");

        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {

        log.info("Был вызван метод PUT /film.");

        if (!(films.containsKey(film.getId()))) {
            throw new ValidationException("Фильм с id " + film.getId() + " не был найден.");
        }
        films.put(film.getId(), film);

        log.info("Фильм c id - " + film.getId() + " был успешно обновлен.\n");

        return film;
    }
}
