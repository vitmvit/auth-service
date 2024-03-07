package ru.clevertec.news.util;

import lombok.experimental.UtilityClass;

/**
 * Утилитный класс для работы со строками.
 */
@UtilityClass
public class StringUtils {

    /**
     * Проверяет, является ли строка пустой (null или пустая).
     *
     * @param line строка для проверки
     * @return true, если строка пустая, иначе false
     */
    public boolean isEmpty(CharSequence line) {
        return line == null || line.isEmpty();
    }

    /**
     * Проверяет, является ли строка не пустой.
     *
     * @param line строка для проверки
     * @return true, если строка не пустая, иначе false
     */
    public boolean isNotEmpty(CharSequence line) {
        return !isEmpty(line);
    }
}
