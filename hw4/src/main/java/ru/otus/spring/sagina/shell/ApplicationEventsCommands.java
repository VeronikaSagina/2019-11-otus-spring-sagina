package ru.otus.spring.sagina.shell;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.sagina.domain.Question;
import ru.otus.spring.sagina.domain.Student;
import ru.otus.spring.sagina.services.LocaleChanger;
import ru.otus.spring.sagina.services.MessageService;
import ru.otus.spring.sagina.services.TestingService;

import javax.validation.constraints.NotNull;

@ShellComponent
public class ApplicationEventsCommands {
    private final LocaleChanger localeChanger;
    private final TestingService testingService;
    private final MessageService messageService;
    private Student student;

    public ApplicationEventsCommands(LocaleChanger localeChanger,
                                     TestingService testingService,
                                     MessageService messageService) {
        this.localeChanger = localeChanger;
        this.testingService = testingService;
        this.messageService = messageService;
    }

    @ShellMethod(value = "Login command", key = {"l", "log", "login"})
    public String login(@ShellOption String name) {
        student = new Student(name);
        return messageService.getMessage("greeting", new String[]{name});
    }

    @ShellMethod(value = "Set default locale en/ru", key = {"loc", "setLocale"})
    public String setLocale(@ShellOption String locale) {
        return localeChanger.setLocale(locale);
    }

    @ShellMethod(value = "Getting next question", key = {"q", "n", "nextQuestion"})
    @ShellMethodAvailability(value = "isPublishEventCommandAvailable")
    public Question getNextQuestion() {
        return testingService.getNextQuestion(student);
    }

    @ShellMethod(value = "Saving the answer", key = {"s", "a", "saveAnswer"})
    @ShellMethodAvailability(value = "isPublishEventCommandAvailable")
    public void saveAnswer(@ShellOption @NotNull int answer) {
        testingService.saveAnswer(student, answer);
    }

    @ShellMethod(value = "Getting result", key = {"r", "getResult"})
    @ShellMethodAvailability(value = "isPublishEventCommandAvailable")
    public String getResult() {
        return testingService.getResult(student);
    }

    @ShellMethod(value = "restart testing", key = {"again", "restart"})
    @ShellMethodAvailability(value = "isPublishEventCommandAvailable")
    public void restart() {
        testingService.restart(student);
    }

    private Availability isPublishEventCommandAvailable() {
        return student == null
                ? Availability.unavailable(messageService.getMessage("not_login"))
                : Availability.available();
    }
}
