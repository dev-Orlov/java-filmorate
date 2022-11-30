package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.filmExeption.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.filmExeption.UnknownFilmException;
import ru.yandex.practicum.filmorate.exception.userExeption.UnknownUserException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.mpa.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @ExceptionHandler({UnknownFilmException.class, UnknownUserException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUnknownFilm(final RuntimeException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNegativeValidation(final FilmValidationException e) {
        return Map.of("error", e.getMessage());
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable("id") int filmId) {
        return filmService.getFilm(filmId);
    }

    @GetMapping("/films")
    public List<Film> getAll() {
        return filmService.getAll();
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @DeleteMapping("/films")
    public Film remove(@Valid @RequestBody Film film) {
        return filmService.remove(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false) Integer count) {
        return filmService.getPopularFilms(Objects.requireNonNullElse(count, 10));
    }

    @GetMapping("/mpa")
    public List<Mpa> getAllRatings() {
        return filmService.getAllRatings();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getRatingById(@PathVariable("id") int ratingId) {
        return filmService.getRatingById(ratingId);
    }

    @GetMapping("/genres")
    public List<Mpa> getAllGenres() {
        return filmService.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Mpa getGenreById(@PathVariable("id") int genreId) {
        return filmService.getGenreById(genreId);
    }
}
