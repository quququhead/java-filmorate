package ru.yandex.practicum.filmorate.exception;

public class ConstrainsViolationException extends RuntimeException {
    public ConstrainsViolationException(String message) {
        super(message);
    }
}
