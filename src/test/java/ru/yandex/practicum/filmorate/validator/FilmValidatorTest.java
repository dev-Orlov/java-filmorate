package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.filmExeption.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {

    private Film initCorrectFilm() {
        return Film.builder()
                .name("фильм")
                .id(10)
                .description("описание")
                .releaseDate(LocalDate.of(2020, 11, 12))
                .duration(120)
                .build();
    }

    @Test
    @DisplayName("Тест валидации корректного объекта фильма")
    void createValidateFilm() {
        Film film1 = initCorrectFilm();
        Film film2 = film1;
        FilmValidator.validate(film1);
        assertEquals(film1, film2);
    }

    @Test
    @DisplayName("Тест валидации фильма с описанием больше 200 символов")
    void createNotValidateFilmWithLongDescription() {
        Film film1 = initCorrectFilm().toBuilder()
                .description("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. " +
                        "Aenean commodo ligula eget dolor. Aenean massa. " +
                        "Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. " +
                        "Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem.")
                .build();
        FilmValidationException exception = assertThrows(FilmValidationException.class,
                () -> FilmValidator.validate(film1));

        assertEquals("максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    @DisplayName("Тест валидации фильма с датой раньше 28 декабря 1895")
    void createNotValidateFilmWithImpossibleDate() {
        Film film1 = initCorrectFilm().toBuilder()
                .releaseDate(LocalDate.of(1225, 11, 12))
                .build();
        FilmValidationException exception = assertThrows(FilmValidationException.class,
                () -> FilmValidator.validate(film1));

        assertEquals("дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    @DisplayName("Тест валидации фильма с отрицательной продолжительностью")
    void createNotValidateFilmWithNegativeDuration() {
        Film film1 = initCorrectFilm().toBuilder()
                .duration(-100)
                .build();
        FilmValidationException exception = assertThrows(FilmValidationException.class,
                () -> FilmValidator.validate(film1));

        assertEquals("продолжительность фильма должна быть положительной", exception.getMessage());
    }
}