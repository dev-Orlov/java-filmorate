package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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
}
