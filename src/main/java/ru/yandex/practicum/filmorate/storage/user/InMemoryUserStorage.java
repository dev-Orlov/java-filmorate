package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.userExeption.UnknownUserException;
import ru.yandex.practicum.filmorate.exception.userExeption.UserValidationException;
import ru.yandex.practicum.filmorate.model.user.User;
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
    public List<User> getFriendList(int userId) {
        List<User> friendList = new ArrayList<>();
        for (int friend_id : getUser(userId).getFriendList()) {
            friendList.add(getUser(friend_id));
        }
        return friendList;
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
    @Override
    public void addFriend(int userId, int friendId) {
        User user1 = getUser(userId);
        User user2 = getUser(friendId);
        if (!user1.addFriend(user2.getId())) {
            log.error("Пользователь {} уже добавил пользователя {} в друзья!", user1, user2);
            throw new UserValidationException("попытка добавить существующего друга");
        }
        log.debug("Пользователи {} и {} теперь состоят в друзьях!", user1, user2);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        User user1 = getUser(userId);
        User user2 = getUser(friendId);
        if (!user1.removeFriend(user2.getId()) || !user2.removeFriend(user1.getId())) {
            log.error("Пользователи {} и {} не состоят в друзьях!", user1, user2);
            throw new UnknownUserException("попытка удалить несуществующего друга");
        }
        log.debug("Пользователи {} и {} теперь не состоят в друзьях!", user1, user2);
    }

    @Override
    public List<User> getCommonFriendList(int userId, int friendId) {
        List<Integer> friendList = getUser(userId).getFriendList();
        friendList.retainAll(getUser(friendId).getFriendList());
        List<User> commonFriendList = new ArrayList<>();
        for (int findId : friendList) {
            commonFriendList.add(getUser(findId));
        }
        return commonFriendList;
    }
}
