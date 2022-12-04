package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exception.filmExeption.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.filmExeption.UnknownFilmException;
import ru.yandex.practicum.filmorate.exception.userExeption.UnknownUserException;
import ru.yandex.practicum.filmorate.exception.userExeption.UserValidationException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({UnknownFilmException.class, UnknownUserException.class,
            UnknownFilmException.class, UnknownUserException.class})
    public ResponseEntity<AppError> catchResourceNotFoundException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserValidationException.class, FilmValidationException.class})
    public ResponseEntity<AppError> catchResourceBadRequestException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
