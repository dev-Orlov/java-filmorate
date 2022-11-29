package ru.yandex.practicum.filmorate.model.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.*;

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
    private final Set<Integer> friends = new HashSet<>();

    public boolean addFriend(Integer friendId) {
        return friends.add(friendId);
    }

    public boolean removeFriend(Integer friendId) {
        boolean checkFriend = friends.contains(friendId);
        friends.remove(friendId);
        return checkFriend;
    }

    public List<Integer> getFriendList() {
        return new ArrayList<>(friends);
    }
}
