package ru.yandex.practicum.filmorate;

import java.time.LocalDate;
import java.util.Collection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmTest {
    private FilmController filmController;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
    }

    @Test
    public void filmReceiptCheck() {
        Film film = new Film(1, "Титаник", "Описание", LocalDate.now(), 60);
        filmController.postFilm(film);
        Collection<Film> filmList = filmController.getFilm();
        Assertions.assertNotNull(filmList);
        Assertions.assertEquals(1, filmList.size());
    }

    @Test
    public void filmUpdateAndCheckResult() {
        Film film = new Film(0, "Титаник", "Описание", LocalDate.now(), 60);
        filmController.postFilm(film);
        Film film1 = new Film(0, "Интерстеллар", "Описание", LocalDate.now(), 90);
        filmController.updateFilm(film1);
        long filmId = film.getId();
        long film1Id = film1.getId();
        Assertions.assertEquals(filmId, film1Id);
        String filmName = film.getName();
        String film1Name = film1.getName();
        Assertions.assertNotEquals(film, film1);
        Assertions.assertNotEquals(filmName, film1Name);
    }

    @Test
    public void filmEmptyName() {
        Film film = new Film(1, "", "Описание", LocalDate.now(), 60);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.postFilm(film);
        });
        Film film1 = new Film(1, "        ", "Описание", LocalDate.now(), 60);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.postFilm(film1);
        });
    }

    @Test
    public void checkingTheMaximumLengthOf200CharactersForAMovieDescription() {
        String symbol201 = "";
        StringBuilder result = new StringBuilder();

        for(int i = 0; i < 104; ++i) {
            symbol201 = String.valueOf(i);
            result.append(symbol201);
        }

        Film film = new Film(1, "Интерстеллар", result.toString(), LocalDate.now(), 60);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.postFilm(film);
        });
    }

    @Test
    public void releaseDateCheckMustNotBeEarlierThan28December1895year() {
        Film film = new Film(1, "Интерстеллар", "Описание", LocalDate.of(1895, 12, 27), 60);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.postFilm(film);
        });
    }

    @Test
    public void checkingMovieDurationForANegativeValue() {
        Film film = new Film(1, "Интерстеллар", "Описание", LocalDate.now(), -1);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.postFilm(film);
        });
    }
}
