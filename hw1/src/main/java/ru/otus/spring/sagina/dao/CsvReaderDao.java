package ru.otus.spring.sagina.dao;

import ru.otus.spring.sagina.entity.TestItem;

import java.util.List;

public interface CsvReaderDao {
    List<TestItem> getTestItems();
}
