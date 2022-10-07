package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAll() {
        List<Film> filmsList = new ArrayList<>();
        for (int filmId : films.keySet()) {
            filmsList.add(films.get(filmId));
        }
        return filmsList;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        FilmValidator.validate(film);
        films.put(film.getId(), film);
        log.debug("Создан объект фильма: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        FilmValidator.validate(film);
        if (!films.containsKey(film.getId())) {
            log.error("фильма с id={} не существует", film.getId());
            throw new FilmValidationException("попытка обновить несуществующий фильм");
        }
        films.put(film.getId(), film);
        log.debug("Изменён объект фильма: {}", film);
        return film;
    }
}
