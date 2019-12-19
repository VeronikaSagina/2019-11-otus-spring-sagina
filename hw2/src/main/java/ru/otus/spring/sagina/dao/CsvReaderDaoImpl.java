package ru.otus.spring.sagina.dao;

import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.domain.TestItem;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.services.MessageService;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CsvReaderDaoImpl implements CsvReaderDao {
    private final MessageService messageService;
    private List<TestItem> testItems = new ArrayList<>();

    public CsvReaderDaoImpl(MessageService messageService) {
        this.messageService = messageService;
    }

    public List<TestItem> getTestItems() {
        if (testItems.isEmpty()) {
            initTestItems();
        }
        return testItems;
    }

    private void initTestItems() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        String csvFileName = messageService.getMessage("csv.file");
        InputStream inputStream = classLoader.getResourceAsStream(csvFileName);
        if (inputStream == null) {
            throw new NotFoundException(messageService.getMessage("not_found_message.file"));
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
