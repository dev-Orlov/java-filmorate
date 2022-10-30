package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.userExeption.UnknownUserException;
import ru.yandex.practicum.filmorate.exception.userExeption.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;


    public void addFriend(int userId, int friendId) {
        User user1 = userStorage.getUser(userId);
        User user2 = userStorage.getUser(friendId);
        if (!user1.addFriend(user2.getId()) || !user2.addFriend(user1.getId())) {
            log.error("Пользователи {} и {} уже состоят в друзьях!", user1, user2);
            throw new UserValidationException("попытка добавить существующего друга");
        }
        log.debug("Пользователи {} и {} теперь состоят в друзьях!", user1, user2);
    }

    public void removeFriend(int userId, int friendId) {
        User user1 = userStorage.getUser(userId);
        User user2 = userStorage.getUser(friendId);
        if (!user1.removeFriend(user2.getId()) || !user2.removeFriend(user1.getId())) {
            log.error("Пользователи {} и {} не состоят в друзьях!", user1, user2);
            throw new UnknownUserException("попытка удалить несуществующего друга");
        }
        log.debug("Пользователи {} и {} теперь не состоят в друзьях!", user1, user2);
    }

    public List<User> getFriendList(int userId) {
        List<User> friendList = new ArrayList<>();
        for (int findId : userStorage.getUser(userId).getFriendList()) {
            friendList.add(userStorage.getUser(findId));
        }
        return friendList;
    }

    public List<User> getCommonFriendList(int userId,  int friendId) {
        List<Integer> commonFriendList = userStorage.getUser(userId).getFriendList();
        commonFriendList.retainAll(userStorage.getUser(friendId).getFriendList());
        List<User> friendList = new ArrayList<>();
        for (int findId : commonFriendList) {
            friendList.add(userStorage.getUser(findId));
        }
        return friendList;
    }

    public User getUser(int userId) {
        return userStorage.getUser(userId);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User remove(User user) {
        return userStorage.remove(user);
    }
}
