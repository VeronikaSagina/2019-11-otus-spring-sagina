package ru.otus.spring.sagina;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.sagina.services.TestingServiceImpl;

@Configuration
@ComponentScan
public class SpringApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringApplication.class);
        TestingServiceImpl testingService = context.getBean(TestingServiceImpl.class);
        testingService.testing();
    }

}
