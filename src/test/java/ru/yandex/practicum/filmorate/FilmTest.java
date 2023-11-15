
package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectCountException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Collection;

public class FilmTest <T extends FilmStorage> {
    private FilmService filmService;
    @BeforeEach
    public void setUp() {
        InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage);
    }

    @Test
    public void filmReceiptCheck() {
        Film film = new Film(1, "Титаник", "Описание", LocalDate.now(), 60, null, 0);
        filmService.postFilm(film);
        Collection<Film> filmList = filmService.getFilm();
        Assertions.assertNotNull(filmList);
        Assertions.assertEquals(1, filmList.size());
    }

    @Test
    public void filmUpdateAndCheckResult() {
        Film film = new Film(1, "Титаник", "Описание", LocalDate.now(), 60, null, 0);
        filmService.postFilm(film);
        Film film1 = new Film(1, "Интерстеллар", "Описание", LocalDate.now(), 90, null, 0);
        filmService.updateFilm(film1);
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
        Film film = new Film(1, "", "Описание", LocalDate.now(), 60, null, 0);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmService.postFilm(film);
        });
        Film film1 = new Film(1, " ", "Описание", LocalDate.now(), 60, null, 0);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmService.postFilm(film1);
        });
    }

    @Test
    public void checkingTheMaximumLengthOf200CharactersForAMovieDescription() {
        String symbol201 = "";
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < 104; ++i) {
            symbol201 = String.valueOf(i);
            result.append(symbol201);
        }

        Film film = new Film(1, "Интерстеллар", result.toString(), LocalDate.now(),
                60, null, 0);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmService.postFilm(film);
        });
    }

    @Test
    public void releaseDateCheckMustNotBeEarlierThan28December1895year() {
        Film film = new Film(1, "Интерстеллар", "Описание",
                LocalDate.of(1895, 12, 27), 60, null, 0);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmService.postFilm(film);
        });
    }

    @Test
    public void checkingMovieDurationForANegativeValue() {
        Film film = new Film(1, "Интерстеллар", "Описание", LocalDate.now(),
                -1, null, 0);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmService.postFilm(film);
        });
    }

    @Test
    public void getFilmByIdMustReturn1Film() {
        Film film = new Film(1, "Интерстеллар", "Описание", LocalDate.now(),
                120, null, 0);
        filmService.postFilm(film);
        Film savedFilm = filmService.findFilmById(film.getId());
        Assertions.assertNotNull(savedFilm, "Фильм не вернулся");
        Assertions.assertEquals(film, savedFilm, "Фильмы не равны");
    }

    @Test
    public void checkingToReturnFilmWithAnIncorrectIdShouldGiveAnError() {
        Film film = new Film(1, "Интерстеллар", "Описание", LocalDate.now(),
                120, null, 0);
        filmService.postFilm(film);
        Film film2 = new Film(0, "Интерстеллар 2", "Описание", LocalDate.now(),
                120, null, 0);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmService.updateFilm(film2);
        });
    }

    @Test
    public void deletingAllFilmsTheListMustBeEmpty() {
        Film film = new Film(1, "Интерстеллар", "Описание", LocalDate.now(),
                120, null, 0);
        filmService.postFilm(film);
        Film film2 = new Film(2, "Интерстеллар 2", "Описание", LocalDate.now(),
                120, null, 0);
        filmService.postFilm(film2);
        Collection<Film> filmList = filmService.getFilm();
        filmService.deleteAllFilms();
        Assertions.assertEquals(0, filmList.size(), "Список после удаления всех фильмов не пуст");
    }

    @Test
    public void deleteFilmByIdTheListMustBeLessThanOneLong() {
        Film film = new Film(1, "Интерстеллар", "Описание", LocalDate.now(),
                120, null, 0);
        filmService.postFilm(film);
        Film film2 = new Film(2, "Интерстеллар 2", "Описание", LocalDate.now(),
                120, null, 0);
        filmService.postFilm(film2);
        Assertions.assertThrows(FilmNotFoundException.class, () -> {
            filmService.deleteFilmById(3);
        });
        Collection<Film> filmList = filmService.getFilm();
        Assertions.assertEquals(2, filmList.size(), "Список не равен 2");
        filmService.deleteFilmById(2);
        Assertions.assertEquals(1, filmList.size(), "Фильм не был удален, длина списка 2");
    }

    @Test
    public void addingALikeToAFilm() {
        Film film = new Film(1, "Интерстеллар", "Описание", LocalDate.now(),
                120, null, 0);
        filmService.postFilm(film);
        filmService.putLikeFilm(1, 1);
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            filmService.putLikeFilm(1, -1);
        });
        Integer likesCount = film.getLikesCount();
        Assertions.assertEquals(1, likesCount, "Количество лайков не соответствует");
    }

    @Test
    public void removingAFIlmLike() {
        Film film = new Film(1, "Интерстеллар", "Описание", LocalDate.now(),
                120, null, 0);
        filmService.postFilm(film);
        filmService.putLikeFilm(1, 1);
        Integer savedlikesCount = film.getLikesCount();
        Assertions.assertEquals(1, savedlikesCount, "Количество лайков не соответствует");
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            filmService.deleteLike(1, -1);
        });
        filmService.deleteLike(1, 1);
        Integer likesCount = film.getLikesCount();
        Assertions.assertEquals(0, likesCount, "Лайк не удален");
    }

    @Test
    public void gettingListOfFilmsByNumberOfLikes() {
        Film film = new Film(1, "Интерстеллар", "Описание", LocalDate.now(),
                120, null, 0);
        filmService.postFilm(film);
        Film film2 = new Film(2, "Интерстеллар 2", "Описание", LocalDate.now(),
                120, null, 0);
        filmService.postFilm(film2);
        filmService.putLikeFilm(1, 1);
        filmService.putLikeFilm(2, 2);
        Assertions.assertThrows(IncorrectCountException.class, () -> {
            filmService.getFilmCountLikes(null);
        });
        Assertions.assertThrows(IncorrectCountException.class, () -> {
            filmService.getFilmCountLikes(-1);
        });
        Collection<Film> listLikesFilm = filmService.getFilmCountLikes(2);
        Assertions.assertEquals(2, listLikesFilm.size(), "Количество списка не соответствует.");
    }
}

