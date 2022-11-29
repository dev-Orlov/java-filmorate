package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.userExeption.UnknownUserException;
import ru.yandex.practicum.filmorate.exception.userExeption.UserValidationException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Primary
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        UserValidator.validate(user);
        String sqlQuery = "INSERT INTO users(email, login, user_name, birthday) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        log.debug("Создан объект пользователя: {}", user);
        return getUser(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public User update(User user) {
        UserValidator.validate(user);
        try {
            String sqlQuery = "UPDATE users set " +
                    "email = ?, login = ?, user_name = ?, birthday = ? " +
                    "where user_id = ?";
            jdbcTemplate.update(sqlQuery
                    , user.getEmail()
                    , user.getLogin()
                    , user.getName()
                    , user.getBirthday()
                    , user.getId());
            return getUser(user.getId());
        } catch (UnknownUserException e) {
            log.error("пользователя с id={} не существует", user.getId());
            throw new UnknownUserException("попытка обновить несуществующего пользователя");
        }
    }

    @Override
    public User remove(User user) {
        UserValidator.validate(user);
        try {
            getUser(user.getId());
            String sqlQuery = "DELETE FROM users WHERE user_id = ?";
            jdbcTemplate.update(sqlQuery, user.getId());
            log.debug("Удалён объект пользователя: {}", user);
            return user;
        } catch (UnknownUserException e) {
            log.error("пользователя с id={} не существует", user.getId());
            throw new UnknownUserException("попытка удалить несуществующего пользователя");
        }
    }

    @Override
    public List<User> getAll() {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT user_id FROM users");
        List<User> userList = new ArrayList<>();

        while (userRows.next()) {
            userList.add(getUser(userRows.getInt("user_id")));
        }
        return userList;
    }

    @Override
    public User getUser(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id = ?", id);

        if(userRows.next()) {
            return User.builder()
                    .email(userRows.getString("email"))
                    .id(userRows.getInt("user_id"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("user_name"))
                    .birthday(Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate())
                    .build();
        } else {
            log.error("пользователя с id={} не существует", id);
            throw new UnknownUserException("попытка получить несуществующего пользователя");
        }
    }

    @Override
    public void addFriend(int userId, int friendId) {
        User user1 = getUser(userId);
        User user2 = getUser(friendId);
        if (user1.getFriends().contains(friendId)) {
            log.error("Пользователь {} уже добавил пользователя {} в друзья!", user1, user2);
            throw new UserValidationException("попытка добавить существующего друга");
        }
        String sqlQuery = "INSERT INTO friends(user_id, friend_id, status) " +
                "VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        boolean checkStatus = getFriendList(friendId).contains(getUser(userId));
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id", "friend_id"});
            stmt.setInt(1, userId);
            stmt.setInt(2, friendId);
            stmt.setBoolean(3, checkStatus);
            return stmt;
        }, keyHolder);
        log.debug("Пользователи {} и {} теперь состоят в друзьях!", user1, user2);
    }

    @Override
    public List<User> getFriendList(int userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT friend_id FROM friends WHERE user_id = ?", userId);
        List<User> friendList = new ArrayList<>();

        while (userRows.next()) {
            friendList.add(getUser(userRows.getInt("friend_id")));
        }
        return friendList;
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        User user1 = getUser(userId);
        User user2 = getUser(friendId);
        if (!getFriendList(userId).contains(user2)) {
            log.error("Пользователь {} ещё не добавил пользователя {} в друзья!", user1, user2);
            throw new UserValidationException("попытка удалить несуществующего друга");
        }
            String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
            jdbcTemplate.update(sqlQuery, userId, friendId);
        log.debug("Пользователи {} и {} теперь не состоят в друзьях!", user1, user2);
    }

    @Override
    public List<User> getCommonFriendList(int userId,  int friendId) {
        List<User> commonFriendList = getFriendList(friendId);
        commonFriendList.retainAll(getFriendList(friendId));
        return commonFriendList;
    }
}
