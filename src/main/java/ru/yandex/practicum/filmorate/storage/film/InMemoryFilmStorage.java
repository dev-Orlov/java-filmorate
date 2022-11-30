package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.filmExeption.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.filmExeption.UnknownFilmException;
import ru.yandex.practicum.filmorate.exception.userExeption.UnknownUserException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.mpa.Mpa;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            log.error("фильма с id={} не существует", id);
            throw new UnknownFilmException("попытка получить несуществующий фильм");
        }
        return films.get(id);
    }

    @Override
    public List<Mpa> getFilmGenres(int id) {
        return null;
    }

    @Override
    public HashMap<Integer, String> getMapGenres() {
        return null;
    }

    @Override
    public Mpa getFilmRating(int id) {
        return null;
    }

    @Override
    public Film create(Film film) {
        FilmValidator.validate(film);
        films.put(film.getId(), film);
        log.debug("Создан объект фильма: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        FilmValidator.validate(film);
        if (!films.containsKey(film.getId())) {
            log.error("фильма с id={} не существует", film.getId());
            throw new UnknownFilmException("попытка обновить несуществующий фильм");
        }
        films.put(film.getId(), film);
        log.debug("Изменён объект фильма: {}", film);
        return film;
    }

    @Override
    public Film remove(Film film) {
        FilmValidator.validate(film);
        if (!films.containsKey(film.getId())) {
            log.error("фильма с id={} не существует", film.getId());
            throw new UnknownFilmException("попытка удалить несуществующий фильм");
        }
        films.remove(film.getId());
        log.debug("Удалён объект фильма: {}", film);
        return film;
    }

    @Override
    public void addLike(int filmId, int userId) {
        Film film = getFilm(filmId);
        if (!film.addLike(userId)) {
            log.error("Пользователь id={} уже поставил лайк фильму {}", userId, film);
            throw new FilmValidationException("попытка поставить двойной лайк");
        }
        log.debug("Пользователь id={} поставил лайк фильму {}", userId, film);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        Film film = getFilm(filmId);
        if (!film.removeLike(userId)) {
            log.error("Не найден пользователь id={} или он ещё не поставил лайк фильму {}", userId, film);
            throw new UnknownUserException("попытка удалить несуществующий лайк");
        }
        log.debug("Пользователь id={} убрал лайк у фильма {}", userId, film);
    }

    @Override
    public List<Integer> getFilmLikes(int filmId) {
        return null;
    }

    @Override
    public List<Mpa> getAllRatings() {
        return null;
    }

    @Override
    public Mpa getRatingById(int ratingId) {
        return null;
    }

    @Override
    public List<Mpa> getAllGenres() {
        return null;
    }

    @Override
    public Mpa getGenreById(int genreId) {
        return null;
    }
}
