package ru.yandex.practicum.filmorate.storage.film;

import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    @Generated
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    public final Map<Long, Film> films = new HashMap<>();
    private long generatorId = 1;

    @Override
    public Collection<Film> getFilm() {
        log.info("Был вызван метод GET /films.");
        log.info("Текущее количество фильмов - " + films.keySet().size() + "\n");
        return films.values();
    }

    @Override
    public Film findFilmById(long filmId) {
        log.info("Был вызван метод GET /films/film/{filmId}.");
        return films.values().stream()
                .filter(film -> film.getId() == filmId)
                .findFirst()
                .orElseThrow(() -> new FilmNotFoundException("Фильм не был найден."));
    }

    @Override
    public Film postFilm(Film film) {
        log.info("Был вызван метод POST /films.");
        validateFilm(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Новый фильм - " + film.getName() + " успешно добавлен.");
        log.info("Текущее количество фильмов - " + films.keySet().size() + "\n");
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

    @Override
    public Film updateFilm(Film film) {
        log.info("Был вызван метод PUT /films.");
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с id " + film.getId() + " не был найден.");
        } else {
            validateFilm(film);
            films.put(film.getId(), film);
            log.info("Фильм c id - " + film.getId() + " был успешно обновлен.\n");
            return film;
        }
    }

    @Override
    public String deleteAllFilms() {
        log.info("Был вызван метод DELETE /films");
        films.clear();
        return "Все фильмы удалены.";
    }

    @Override
    public String deleteFilmById(long filmId) {
        log.info("Был вызван метод DELETE /films/film/{filmId}.");
        if (!(films.containsKey(filmId))) {
            throw new FilmNotFoundException(String.format("Фильм № %s не найден", filmId));
        }
        String savedFilmName = films.get(filmId).getName();
        films.remove(filmId);
        log.info("Фильм с id - " + filmId + " удален.");
        return String.format("Фильм %s был успешно удален.", savedFilmName);
    }

    private long generateId() {
        return generatorId++;
    }
}
