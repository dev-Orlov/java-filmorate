package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.mpa.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public void addLike(int filmId, int userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> allFilms = filmStorage.getAll();
        int filmsSize = Math.min(allFilms.size(), count);

        return allFilms.stream()
                .sorted(Comparator.comparingInt(Film::getRate))
                .collect(Collectors.toList())
                .subList(0, filmsSize);
    }

    public Film getFilm(int filmId) {
        return filmStorage.getFilm(filmId);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film remove(Film film) {
        return filmStorage.remove(film);
    }

    public List<Mpa> getAllRatings() {
        return filmStorage.getAllRatings();
    }

    public Mpa getRatingById(int ratingId) {
        return filmStorage.getRatingById(ratingId);
    }

    public List<Mpa> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public Mpa getGenreById(int genreId) {
        return filmStorage.getGenreById(genreId);
    }
}
