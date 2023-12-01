package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> getFilm(); // отправляет список всех фильмов

    Film findFilmById(@PathVariable long filmId); // отправляет фильм по Id

    Film postFilm(@RequestBody Film film); // добавляет фильм

    Film updateFilm(@RequestBody Film film); // обновляет данные фильма

    String deleteAllFilms(); // удаляет все фильмы

    String deleteFilmById(@PathVariable long filmId); // удаляет фильм по Id
}
