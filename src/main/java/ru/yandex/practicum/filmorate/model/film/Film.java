package ru.yandex.practicum.filmorate.model.film;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.film.mpa.Mpa;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

@Data
@Builder(toBuilder = true)
public class Film {

    @NotNull
    @NotBlank
    private String name;
    private int id;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private final Set<Mpa> genres = new HashSet<>(); // сохраняем жанры фильма
    private Mpa mpa;  //сохраняем рейтинг фильма
    private final Set<Integer> likes = new HashSet<>(); // сохраняем id пользователя, поставившего лайк
    private long rate;

    public boolean addLike(Integer userId) {
        return likes.add(userId);
    }

    public boolean removeLike(Integer userId) {
        boolean checkLike = likes.contains(userId);
        likes.remove(userId);
        return checkLike;
    }

    public List<Integer> getLikeList() {
        return new ArrayList<>(likes);
    }

    public int getRate() {
        return -likes.size();
    }

    public List<Mpa> getGenres() {
        return new ArrayList<>(genres);
    }

    public void setGenres(List<Mpa> filmGenres) {
        genres.addAll(filmGenres);
    }
}
