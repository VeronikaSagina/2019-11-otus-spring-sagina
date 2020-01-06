package ru.otus.spring.sagina.dao;

import ru.otus.spring.sagina.domain.TestItem;

import java.util.List;

public interface CsvReaderDao {
    List<TestItem> getTestItems();

    TestItem getTestItem(int questionNumber);

    int getSize();
}
