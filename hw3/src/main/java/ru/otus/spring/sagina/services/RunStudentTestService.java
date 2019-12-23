package ru.otus.spring.sagina.services;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.domain.Student;
import ru.otus.spring.sagina.domain.StudentResult;

@Service
@Profile("!test")
public class RunStudentTestService implements CommandLineRunner {
    private final LocaleChanger localeChanger;
    private final TestingService testingService;
    private final MessageService messageService;
    private final ResultService resultService;

    public RunStudentTestService(LocaleChanger localeChanger,
                                 TestingService testingService,
                                 MessageService messageService,
                                 ResultService resultService) {
        this.localeChanger = localeChanger;
        this.testingService = testingService;
        this.messageService = messageService;
        this.resultService = resultService;
    }

    @Override
    public void run(String... args) {
        localeChanger.setLocale();
        messageService.writeMessage(messageService.getMessage("greeting"));
        Student student = new Student(messageService.readMessage());
        testingService.testing(student);
        StudentResult result = resultService.get(student);
        messageService.writeMessage(
                messageService.getMessage(
                        "resume",
                        new Object[]{result.getName(), result.getCorrectCount(), result.getCurrentQuestionNumber()}));
    }
}
