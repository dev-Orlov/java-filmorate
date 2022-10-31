package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.filmExeption.UnknownFilmException;
import ru.yandex.practicum.filmorate.model.Film;
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
}
