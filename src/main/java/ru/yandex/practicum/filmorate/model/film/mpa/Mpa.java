package ru.yandex.practicum.filmorate.model.film.mpa;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Mpa {
    private int id;
    private String name;
}
