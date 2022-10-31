package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.userExeption.UnknownUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{

    private final HashMap<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(int id) {
        if (!users.containsKey(id)) {
            log.error("пользователя с id={} не существует", id);
            throw new UnknownUserException("попытка получить несуществующего пользователя");
        }
        return users.get(id);
    }

    @Override
    public User create(User user) {
        UserValidator.validate(user);
        users.put(user.getId(), user);
        log.debug("Создан объект пользователя: {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        UserValidator.validate(user);
        if (!users.containsKey(user.getId())) {
            log.error("пользователя с id={} не существует", user.getId());
            throw new UnknownUserException("попытка обновить несуществующего пользователя");
        }
        users.put(user.getId(), user);
        log.debug("Изменён объект пользователя: {}", user);
        return user;
    }

    @Override
    public User remove(User user) {
        UserValidator.validate(user);
        if (!users.containsKey(user.getId())) {
            log.error("пользователя с id={} не существует", user.getId());
            throw new UnknownUserException("попытка удалить несуществующего пользователя");
        }
        users.remove(user.getId());
        log.debug("Удалён объект пользователя: {}", user);
        return user;
    }
}
