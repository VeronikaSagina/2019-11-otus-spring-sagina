package ru.otus.spring.sagina.dao;

import com.opencsv.CSVReader;
import ru.otus.spring.sagina.entity.TestItem;
import ru.otus.spring.sagina.exceptions.NotFoundException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvReaderDaoImpl implements CsvReaderDao {

    private final String csvFileName;
    private List<TestItem> testItems = new ArrayList<>();

    public CsvReaderDaoImpl(String csvFileName) {
        this.csvFileName = csvFileName;
    }

    public List<TestItem> getTestItems() {
        if (testItems.isEmpty()) {
            initTestItems();
        }
        return testItems;
    }

    private void initTestItems() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(csvFileName);
        if (inputStream == null) {
            throw new NotFoundException("не найден файл с ресурсами");
        }
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                testItems.add(new TestItem(
                        line[0],
                        Integer.parseInt(line[1]),
                        Arrays.asList(line[2], line[3], line[4])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
