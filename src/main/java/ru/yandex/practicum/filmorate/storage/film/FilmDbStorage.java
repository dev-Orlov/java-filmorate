package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.filmExeption.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.filmExeption.UnknownFilmException;
import ru.yandex.practicum.filmorate.exception.userExeption.UnknownUserException;
import ru.yandex.practicum.filmorate.exception.userExeption.UserValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.mpa.Mpa;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
@Primary
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        FilmValidator.validate(film);
        String sqlQueryFilm = "INSERT INTO films(film_name, description, release_date, duration, rating_id, rate) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder1 = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQueryFilm, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            stmt.setLong(6, film.getRate());
            return stmt;
        }, keyHolder1);
        addFilmGenres(film);
        log.debug("Создан объект фильма: {}", film);
        return getFilm(Objects.requireNonNull(keyHolder1.getKey()).intValue());
    }

    @Override
    public Film update(Film film) {
        FilmValidator.validate(film);
        try {
            String sqlQuery = "UPDATE films set " +
                    "film_name = ?, description = ?, release_date = ?, duration = ?, rating_id = ?, rate = ? " +
                    "where film_id = ?";
            jdbcTemplate.update(sqlQuery
                    , film.getName()
                    , film.getDescription()
                    , film.getReleaseDate()
                    , film.getDuration()
                    , film.getMpa().getId()
                    , film.getRate()
                    , film.getId());

            removeGenresFromFilm(film.getId()); // очищаем список жанров
            addFilmGenres(film); // добавляем жанры в базу данных

            return getFilm(film.getId());
        } catch (UnknownFilmException e) {
            log.error("фильма с id={} не существует", film.getId());
            throw new UnknownFilmException("попытка обновить несуществующий фильм");
        }
    }

    private void removeGenresFromFilm(int filmId) {
        try {
            String sqlQuery = "DELETE FROM film_genre WHERE film_id = ?";
            jdbcTemplate.update(sqlQuery, filmId);
        } catch (UnknownFilmException e) {
            log.error("фильма с id={} не существует", filmId);
            throw new UnknownFilmException("попытка удалить жанры у несуществующего фильма");
        }
    }

    private void addFilmGenres(Film film) {
        if (film.getGenres().size() > 0) {
            for (Mpa genre : film.getGenres()) {
                String sqlQueryGenres = "INSERT INTO film_genre(film_id, genre_id) " +
                        "VALUES (?, ?)";
                KeyHolder keyHolder2 = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sqlQueryGenres,
                            new String[]{"film_id", "genre_id"});
                    stmt.setInt(1, film.getId());
                    stmt.setInt(2, genre.getId());
                    return stmt;
                }, keyHolder2);
            }
        }
    }

    @Override
    public Film remove(Film film) {
        FilmValidator.validate(film);
        try {
            getFilm(film.getId());
            String sqlQuery = "DELETE FROM films WHERE film_id = ?";
            jdbcTemplate.update(sqlQuery, film.getId());
            log.debug("Удалён объект фильма: {}", film);
            return film;
        } catch (UnknownFilmException e) {
            log.error("фильма с id={} не существует", film.getId());
            throw new UnknownFilmException("попытка удалить несуществующий фильм");
        }
    }

    @Override
    public List<Film> getAll() {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT film_id FROM films " +
                "GROUP BY film_id ORDER BY film_id");
        List<Film> filmList = new ArrayList<>();

        while (filmRows.next()) {
            filmList.add(getFilm(filmRows.getInt("film_id")));
        }
        return filmList;
    }

    @Override
    public Film getFilm(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE film_id = ? ", id);

        if(filmRows.next()) {
            Film film = Film.builder()
                    .name(filmRows.getString("film_name"))
                    .id(filmRows.getInt("film_id"))
                    .description(filmRows.getString("description"))
                    .releaseDate(Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate())
                    .duration(filmRows.getInt("duration"))
                    .mpa(getFilmRating(id))
                    .rate(filmRows.getLong("rate"))
                    .build();
            film.setGenres(getFilmGenres(id));
            for (int checkLikeId : getFilmLikes(id)) {
                film.addLike(checkLikeId);
            }
            return film;

        } else {
            log.error("фильма с id={} не существует", id);
            throw new UnknownFilmException("попытка получить несуществующий фильм");
        }
    }

    @Override
    public List<Mpa> getFilmGenres(int id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres AS g " +
                "RIGHT JOIN film_genre AS fg ON g.genre_id = fg.genre_id " +
                "GROUP BY fg.film_id, g.genre_id HAVING fg.film_id = ?", id);
        List<Mpa> mpaList = new ArrayList<>();

        while (mpaRows.next()) {
            mpaList.add(Mpa.builder().
                    id(mpaRows.getInt("genre_id")).
                    name(mpaRows.getString("genre_title"))
                    .build());
        }
        return mpaList;
    }

    @Override
    public HashMap<Integer, String> getMapGenres() {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres");
        HashMap<Integer, String> genresMap = new HashMap<>();

        while (mpaRows.next()) {
            genresMap.put(mpaRows.getInt("genre_id"), mpaRows.getString("genre_title"));
        }
        return genresMap;
    }

    @Override
    public Mpa getFilmRating(int id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM rating AS r " +
                "RIGHT JOIN films AS f ON r.rating_id = f.rating_id " +
                "GROUP BY f.film_id HAVING f.film_id = ?", id);
        if(mpaRows.next()) {
            return Mpa.builder().
                    id(mpaRows.getInt("rating_id")).
                    name(mpaRows.getString("name"))
                    .build();
        } else {
            throw new FilmValidationException("У фильма не указан рейтинг");
        }
    }
    @Override
    public void addLike(int filmId, int userId) {
        Film film = getFilm(filmId);
        if (getFilmLikes(filmId).contains(userId)) {
            log.error("Пользователь id={} уже поставил лайк фильму {}", userId, film);
            throw new UserValidationException("попытка добавить существующего друга");
        } else {
            String sqlQuery = "INSERT INTO likes(film_id, liked_user_id) " +
                    "VALUES (?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery,
                        new String[]{"film_id", "liked_user_id"});
                stmt.setInt(1, filmId);
                stmt.setInt(2, userId);
                return stmt;
            }, keyHolder);
            log.debug("Пользователь id={} поставил лайк фильму {}", userId, film);
        }
    }

    @Override
    public void removeLike(int filmId, int userId) {
        Film film = getFilm(filmId);
        if (!getFilmLikes(filmId).contains(userId)) {
            log.error("Не найден пользователь id={} или он ещё не поставил лайк фильму {}", userId, film);
            throw new UnknownUserException("попытка удалить несуществующий лайк");
        } else {
            String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND liked_user_id = ?";
            jdbcTemplate.update(sqlQuery, filmId, userId);
            log.debug("Удалён лайк к фильму {}", film);
        }
    }

    @Override
    public List<Integer> getFilmLikes(int filmId) {
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet("SELECT * FROM likes WHERE film_id = ?", filmId);
        List<Integer> likeList = new ArrayList<>();

        while (likeRows.next()) {
            likeList.add(likeRows.getInt("liked_user_id"));
        }
        return likeList;
    }

    @Override
    public List<Mpa> getAllRatings() {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM rating");
        List<Mpa> rainingList = new ArrayList<>();

        while (mpaRows.next()) {
            rainingList.add(Mpa.builder().
                    id(mpaRows.getInt("rating_id")).
                    name(mpaRows.getString("name")).
                    build());
        }
        return rainingList;
    }

    @Override
    public Mpa getRatingById(int ratingId) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM rating WHERE rating_id = ?", ratingId);
        if (mpaRows.next()) {
            return Mpa.builder().
                    id(mpaRows.getInt("rating_id")).
                    name(mpaRows.getString("name")).
                    build();
        } else {
            throw new UnknownFilmException("Рейтинга фильма с таким ID не существует");
        }
    }

    @Override
    public List<Mpa> getAllGenres() {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres");
        List<Mpa> genresList = new ArrayList<>();

        while (mpaRows.next()) {
            genresList.add(Mpa.builder().
                    id(mpaRows.getInt("genre_id")).
                    name(mpaRows.getString("genre_title")).
                    build());
        }
        return genresList;
    }

    @Override
    public Mpa getGenreById(int genreId) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE genre_id = ?", genreId);
        if (mpaRows.next()) {
            return Mpa.builder().
                    id(mpaRows.getInt("genre_id")).
                    name(mpaRows.getString("genre_title")).
                    build();
        } else {
            throw new UnknownFilmException("Жанра фильма с таким ID не существует");
        }
    }
}
