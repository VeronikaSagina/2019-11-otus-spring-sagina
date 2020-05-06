package ru.otus.spring.sagina.config;

import com.github.mongobee.Mongobee;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoBeeConfig {
    private final MongoTemplate mongoTemplate;
    private final MongoClient mongo;
    private final String dbName;

    public MongoBeeConfig(MongoTemplate mongoTemplate,
                          MongoClient mongo,
                          @Value("${spring.data.mongodb.database}") String dbName) {
        this.mongoTemplate = mongoTemplate;
        this.mongo = mongo;
        this.dbName = dbName;
    }

    @Bean
    public Mongobee mongobee(Environment environment) {
        Mongobee runner = new Mongobee(mongo);
        runner.setMongoTemplate(mongoTemplate);
        runner.setDbName(dbName);
        runner.setChangeLogsScanPackage(DatabaseChangelog.class.getPackage().getName());
        runner.setSpringEnvironment(environment);
        return runner;
    }
}
