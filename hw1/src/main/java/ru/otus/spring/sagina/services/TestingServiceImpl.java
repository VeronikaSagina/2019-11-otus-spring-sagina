package ru.otus.spring.sagina.services;

import ru.otus.spring.sagina.dao.CsvReaderDao;
import ru.otus.spring.sagina.entity.Student;
import ru.otus.spring.sagina.entity.TestItem;

import java.io.*;

public class TestingServiceImpl implements TestingService{

    private final CsvReaderDao csvReaderDao;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public TestingServiceImpl(CsvReaderDao csvReaderDao) {
        this.csvReaderDao = csvReaderDao;
    }

    public void testing() {
        writeMessage("Введите имя и фамилию");
        int correctAnswers = 0;
        int nextQuestion = 0;
        Student student = new Student(readMessage());
        while (nextQuestion != 5) {
            askQuestions(nextQuestion);
            Integer answer = readAnswer();
            if (answer != null) {
                if (isCorrectAnswer(answer, nextQuestion)) {
                    correctAnswers += 1;
                }
                nextQuestion += 1;
            }
        }
        writeMessage(String.format("%s вы ответили правильно на %s из 5 вопросов", student.getName(), correctAnswers));
        closeResources();
    }

    private boolean isCorrectAnswer(int answer, int question) {
        return csvReaderDao.getTestItems()
                .get(question)
                .getAnswer() == answer;
    }

    private void askQuestions(int nextQuestion) {
        TestItem testItem = csvReaderDao.getTestItems()
                .get(nextQuestion);
        writeMessage(testItem.getQuestion());
        for (String variant : testItem.getVariants()) {
            writeMessage(variant);
        }
    }

    private Integer readAnswer() {
        try {
            String answer = readMessage();
            return Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            writeMessage("Вы ввели не число");
            return null;
        }
    }

    private void writeMessage(String message) {
        System.out.println(message);
    }

    private String readMessage() {
        try {
            String message = "";
            while (message.isBlank()){
                message = reader.readLine();
                if (message.isBlank()){
                    writeMessage("повторите ввод");
                }
            }
            return message;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeResources() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
