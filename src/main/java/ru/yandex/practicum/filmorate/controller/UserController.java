package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        UserValidator.validate(user);
        users.put(user.getId(), user);
        log.debug("Создан объект пользователя: {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        UserValidator.validate(user);
        if (!users.containsKey(user.getId())) {
            log.error("пользователя с id={} не существует", user.getId());
            throw new UserValidationException("попытка обновить несуществующего пользователя");
        }
        users.put(user.getId(), user);
        log.debug("Изменён объект пользователя: {}", user);
        return user;
    }
}
