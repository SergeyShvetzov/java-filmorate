package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;

@RestController
@RequestMapping({"/films"})
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    // получить список фильмов
    @GetMapping
    public Collection<Film> getFilm() {
        return filmService.getFilm();
    }

    // получение фильма по id
    @GetMapping("/{filmId}")
    public Film findFilmById(@PathVariable long filmId) {
        return filmService.findFilmById(filmId);
    }

    // добавление фильма
    @PostMapping
    public Film postFilm(@RequestBody Film film) {
        return filmService.postFilm(film);
    }

    // обновление данных фильма
    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    // удаление всех фильмов
    @DeleteMapping
    public String deleteFilm() {
        return filmService.deleteAllFilms();
    }

    // удаление фильма по id
    @DeleteMapping("/{filmId}")
    public String deleteFilmById(@PathVariable long filmId) {
        return filmService.deleteFilmById(filmId);
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
    public Collection<Film> getFilmCountLikes(@RequestParam(defaultValue = "10", required = false) Integer count)   {
        return filmService.getFilmCountLikes(count);
    }
}
