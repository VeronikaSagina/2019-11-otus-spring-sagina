package ru.otus.spring.sagina.config.mongo;

import com.github.mongobee.Mongobee;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoBeeConfig {
    private final MongoTemplate mongoTemplate;
    private final MongoClient mongo;

    public MongoBeeConfig(MongoTemplate mongoTemplate, MongoClient mongo) {
        this.mongoTemplate = mongoTemplate;
        this.mongo = mongo;
    }

    @Bean
    public Mongobee mongobee(Environment environment) {
        Mongobee runner = new Mongobee(mongo);
        runner.setMongoTemplate(mongoTemplate);
        runner.setDbName("book_service");
        runner.setChangeLogsScanPackage(DatabaseChangelog.class.getPackage().getName());
        runner.setSpringEnvironment(environment);
        return runner;
    }
}
