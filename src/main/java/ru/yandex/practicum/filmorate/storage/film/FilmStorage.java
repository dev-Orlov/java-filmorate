package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.mpa.Mpa;

import java.util.List;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    Film remove(Film film);

    List<Film> getAll();

    Film getFilm(int id);

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Mpa> getAllRatings();

    Mpa getRatingById(int ratingId);

    List<Mpa> getAllGenres();

    Mpa getGenreById(int genreId);
}
