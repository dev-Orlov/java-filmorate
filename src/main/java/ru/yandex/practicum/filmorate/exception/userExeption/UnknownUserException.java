package ru.yandex.practicum.filmorate.exception.userExeption;

public class UnknownUserException extends RuntimeException {

    public UnknownUserException(String s) {
        super(s);
    }
}
