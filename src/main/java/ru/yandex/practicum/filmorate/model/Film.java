package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private final Set<Integer> likes = new HashSet<>(); // сохраняем id пользователя, поставившего лайк

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
}
