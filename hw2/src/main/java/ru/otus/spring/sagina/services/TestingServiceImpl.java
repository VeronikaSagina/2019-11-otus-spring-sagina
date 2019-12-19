package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dao.CsvReaderDao;
import ru.otus.spring.sagina.domain.Student;
import ru.otus.spring.sagina.domain.TestItem;

@Service
public class TestingServiceImpl implements TestingService {

    private final CsvReaderDao csvReaderDao;
    private final MessageService messageService;

    public TestingServiceImpl(CsvReaderDao csvReaderDao, MessageService messageService) {
        this.csvReaderDao = csvReaderDao;

        this.messageService = messageService;
    }

    public void testing() {
        messageService.writeMessage(messageService.getMessage("greeting"));
        int correctAnswers = 0;
        int nextQuestion = 0;
        int allQuestions = testSize();
        Student student = new Student(messageService.readMessage());
        while (nextQuestion != allQuestions) {
            askQuestions(nextQuestion);
            Integer answer = readAnswer();
            if (answer != null) {
                if (isCorrectAnswer(answer, nextQuestion)) {
                    correctAnswers += 1;
                }
                nextQuestion += 1;
            }
        }
        messageService.writeMessage(
                messageService.getMessage(
                        "resume",
                        new Object[]{student.getName(), correctAnswers, allQuestions}));
        messageService.closeResources();
    }

    private boolean isCorrectAnswer(int answer, int question) {
        return csvReaderDao.getTestItems()
                .get(question)
                .getAnswer() == answer;
    }

    private int testSize() {
        return csvReaderDao.getTestItems().size();
    }

    private void askQuestions(int nextQuestion) {
        TestItem testItem = csvReaderDao.getTestItems()
                .get(nextQuestion);
        messageService.writeMessage(testItem.getQuestion());
        for (String variant : testItem.getVariants()) {
            messageService.writeMessage(variant);
        }
    }

    private Integer readAnswer() {
        try {
            String answer = messageService.readMessage();
            return Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            messageService.writeMessage(messageService.getMessage("wrong_format"));
            return null;
        }
    }
}
