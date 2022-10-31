package ru.yandex.practicum.filmorate.exception.userExeption;

public class UserValidationException extends RuntimeException {

    public UserValidationException(String s) {
        super(s);
    }
}
