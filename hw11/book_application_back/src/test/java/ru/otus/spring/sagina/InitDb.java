package ru.otus.spring.sagina;

import com.github.mongobee.Mongobee;
import com.github.mongobee.exception.MongobeeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.spring.sagina.config.MongoBeeConfig;
import ru.otus.spring.sagina.config.TestAppConfiguration;

@ContextConfiguration(classes = TestAppConfiguration.class)
//@TestPropertySource("/application.yml")
@Import(MongoBeeConfig.class)
@DataMongoTest
@Component
public class InitDb {
    @Autowired
    protected MongoTemplate mongoTemplate;
    @Autowired
    private Mongobee mongobee;

    @BeforeEach
    public void initDb() {
        try {
            mongobee.execute();
        } catch (MongobeeException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void cleanDb() {
        mongoTemplate.getDb().drop();
    }
}
