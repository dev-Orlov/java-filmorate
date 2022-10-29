package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.userExeption.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
public class UserValidator {

    private static int genId = 0;

    public static void validate(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("логин не может содержать пробелы: {}", user.getLogin());
            throw new UserValidationException("логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        if (user.getId() == 0) {
            user.setId(++genId);
        }
    }
}
