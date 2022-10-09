package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class User {

    @Email
    private String email;
    private int id;
    @NotNull
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
