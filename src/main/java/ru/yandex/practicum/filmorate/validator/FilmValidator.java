package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.filmExeption.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidator {

    private static final LocalDate DATE_OF_FIRST_MOVIE = LocalDate.of(1895, 12, 28);
    private static int genId = 0;

    public static void validate(final Film film) {
        if (film.getDescription().length() > 200) {
            log.error("описание фильма превышено на {} символов", (film.getDescription().length() - 200));
            throw new FilmValidationException("максимальная длина описания — 200 символов");
        } else if (film.getReleaseDate().isBefore(DATE_OF_FIRST_MOVIE) ) {
            log.error("дата релиза {} - это раньше рождения кинематографа", film.getReleaseDate());
            throw new FilmValidationException("дата релиза не может быть раньше 28 декабря 1895 года");
        } else if (film.getDuration() < 0 ) {
            log.error("продолжительность фильма должна быть положительной {}", film.getDuration());
            throw new FilmValidationException("продолжительность фильма должна быть положительной");
        }
        if (film.getId() == 0) {
            film.setId(++genId);
        }
    }
}
