package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.user.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    @Test
    @DisplayName("Тест валидации корректного объекта пользователя")
    void createValidateUser() {
        User user1 = User.builder()
                .email("test@yandex.ru")
                .id(10)
                .login("логин")
                .name("имя")
                .birthday(LocalDate.of(1990, 11, 12))
                .build();
        User user2 = user1;
        UserValidator.validate(user1);
        assertEquals(user1, user2);
    }

    @Test
    @DisplayName("Тест валидации объекта пользователя c пустым именем")
    void createValidateUserWithNullName() {
        User user1 = User.builder()
                .email("test@yandex.ru")
                .id(10)
                .login("логин")
                .name(null)
                .birthday(LocalDate.of(1990, 11, 12))
                .build();
        User user2 = user1.toBuilder().name("").build();
        UserValidator.validate(user1);
        assertEquals(user1.getLogin(), user1.getName());

        UserValidator.validate(user2);
        assertEquals(user2.getLogin(), user2.getName());
    }

}