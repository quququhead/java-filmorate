package ru.yandex.practicum.filmorate.model.enums;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum SearchingMethod {
    DIRECTOR, TITLE;

    public static SearchingMethod searchBy(String value) {
        for (SearchingMethod searchingMethod : values()) {
            if (searchingMethod.name().equals(value)) {
                return searchingMethod;
            }
        }
        throw new NoSuchElementException(String.format("Задан некорректный критерий поиска {%s}, возможные варианты %s", value, Arrays.toString(values())));
    }
}
