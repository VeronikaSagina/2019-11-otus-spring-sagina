package ru.otus.spring.sagina.dao;

import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.PropertyConfiguration.CsvConfig;
import ru.otus.spring.sagina.domain.Answer;
import ru.otus.spring.sagina.domain.Question;
import ru.otus.spring.sagina.domain.TestItem;
import ru.otus.spring.sagina.exceptions.NotFoundException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class CsvReaderDaoImpl implements CsvReaderDao {
    private List<TestItem> testItems;
    private final CsvConfig csvConfig;

    public CsvReaderDaoImpl(CsvConfig csvConfig) {
        this.csvConfig = csvConfig;
    }

    @Override
    public List<TestItem> getTestItems() {
        if (testItems == null) {
            initTestItems();
        }
        return testItems;
    }

    @Override
    public TestItem getTestItem(int questionNumber) {
        return getTestItems()
                .get(questionNumber);
    }

    @Override
    public int getSize() {
        return getTestItems().size();
    }

    private void initTestItems() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        String csvFileName = Locale.getDefault().equals(Locale.ENGLISH)
                ? csvConfig.getFileNameEn()
                : csvConfig.getFileNameRu();
        InputStream inputStream = classLoader.getResourceAsStream(csvFileName);
        if (inputStream == null) {
            throw new NotFoundException(String.format("Resource file %s not found", csvFileName));
        }
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream))) {
            List<TestItem> result = new ArrayList<>();
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                result.add(new TestItem(
                        new Question(line[0], Arrays.asList(line[2], line[3], line[4])),
                        new Answer(Integer.parseInt(line[1]))));
            }
            testItems = Collections.unmodifiableList(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
