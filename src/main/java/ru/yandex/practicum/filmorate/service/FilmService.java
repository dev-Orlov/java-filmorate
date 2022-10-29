package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.filmExeption.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.userExeption.UnknownUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getFilm(filmId);
        if (!film.addLike(userId)) {
            log.error("Пользователь id={} уже поставил лайк фильму {}", userId, film);
            throw new FilmValidationException("попытка поставить двойной лайк");
        }
        log.debug("Пользователь id={} поставил лайк фильму {}", userId, film);
    }

    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilm(filmId);
        if (!film.removeLike(userId)) {
            log.error("Не найден пользователь id={} или он ещё не поставил лайк фильму {}", userId, film);
            throw new UnknownUserException("попытка удалить несуществующий лайк");
        }
        log.debug("Пользователь id={} убрал лайк у фильма {}", userId, film);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> allFilms = filmStorage.getAll();
        int filmsSize = Math.min(allFilms.size(), count);

        return allFilms.stream()
                .sorted(Comparator.comparingInt(Film::getRate))
                .collect(Collectors.toList())
                .subList(0, filmsSize);
    }
}
