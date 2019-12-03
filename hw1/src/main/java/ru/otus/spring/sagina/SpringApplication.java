package ru.otus.spring.sagina;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.sagina.services.TestingServiceImpl;

public class SpringApplication {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        TestingServiceImpl testingService = context.getBean(TestingServiceImpl.class);
        testingService.testing();
    }

}
