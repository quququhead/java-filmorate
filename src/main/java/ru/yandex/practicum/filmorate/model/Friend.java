package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friend {
    private long userId;
    private long friendId;
}
