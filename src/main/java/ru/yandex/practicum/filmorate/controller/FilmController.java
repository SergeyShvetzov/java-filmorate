package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectCountException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@RestController
@RequestMapping({"/films"})
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    // получить список фильмов
    @GetMapping
    public Collection<Film> getFilm() {
        return filmStorage.getFilm();
    }

    // получение фильма по id
    @GetMapping("/{filmId}")
    public Film findFilmById(@PathVariable long filmId) {
        return filmStorage.findFilmById(filmId);
    }

    // добавление фильма
    @PostMapping
    public Film postFilm(@RequestBody Film film) {
        return filmStorage.postFilm(film);
    }

    // обновление данных фильма
    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    // удаление всех фильмов
    @DeleteMapping
    public String deleteFilm() {
        return filmStorage.deleteAllFilms();
    }

    // удаление фильма по id
    @DeleteMapping("/{filmId}")
    public String deleteFilmById(@PathVariable long filmId) {
        return filmStorage.deleteFilmById(filmId);
    }

    // PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.
    @PutMapping("/{id}/like/{userId}")
    public Film putLikeFilm(@PathVariable long id, @PathVariable long userId) {
        return filmService.putLikeFilm(id, userId);
    }

    // DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLikeFilm(@PathVariable long id, @PathVariable long userId) {
        return filmService.deleteLike(id, userId);
    }

    // GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
    @GetMapping("/popular")
    public Collection<Film> getFilmCountLikes(@RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count == null) {
            throw new IncorrectCountException("Параметр count равен null.");
        }
        if (count <= 0) {
            throw new IncorrectCountException("Параметр count имеет отрицательное значение.");
        }

        return filmService.getFilmCountLikes(count);
    }
}
