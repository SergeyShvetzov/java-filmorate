package ru.yandex.practicum.filmorate.exceptions;

import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    @Generated
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidEmailException(final InvalidEmailException e) {
        String message = "Email не соответствует требованиям.";
        log.error(message);
        return new ErrorResponse(e.getMessage(), message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlePostNotFoundException(final FilmNotFoundException e) {
        String message = "Фильм не найден.";
        log.error(message);
        return new ErrorResponse(e.getMessage(), message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
        String message = "Пользователь не найден.";
        log.error(message);
        return new ErrorResponse(e.getMessage(), message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlePostNotFoundException(final UserAlreadyExistException e) {
        String message = "Пользователь с данным email уже существует.";
        log.error(message);
        return new ErrorResponse(e.getMessage(), message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        String message = "Произошла непредвиденная ошибка.";
        log.error(message);
        return new ErrorResponse(e.getMessage(), message);
    }
}