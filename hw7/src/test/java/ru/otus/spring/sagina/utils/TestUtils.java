package ru.otus.spring.sagina.utils;

import java.util.Comparator;
import java.util.List;

public class TestUtils {
    public static <T> boolean compare(List<T> expected, List<T> actual, Comparator<T> comparator) {
        if (expected.size() != actual.size()) {
            return false;
        }
        boolean result = true;
        for (int i = 0; i < expected.size() && result; i++) {
            result = comparator.compare(expected.get(i), actual.get(i)) == 0;
        }
        return result;
    }
}
