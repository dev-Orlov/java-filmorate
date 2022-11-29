package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

public interface UserStorage {

    User create(User user);

    User update(User user);

    User remove(User user);

    List<User> getAll();

    User getUser(int id);

    List<User> getFriendList(int userId);

    void addFriend(int userId, int friendId);

    void removeFriend(int userId, int friendId);

    List<User> getCommonFriendList(int userId, int friendId);
}
