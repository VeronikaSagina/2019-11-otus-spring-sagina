package ru.otus.spring.sagina;

import com.github.mongobee.Mongobee;
import com.github.mongobee.exception.MongobeeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import ru.otus.spring.sagina.config.MongoBeeConfig;
import ru.otus.spring.sagina.config.TestAppConfiguration;

@ContextConfiguration(classes = TestAppConfiguration.class)
@Import(MongoBeeConfig.class)
@DataMongoTest
public abstract class TestLifecycle {
    @Autowired
    protected MongoTemplate mongoTemplate;
    @Autowired
    private Mongobee mongobee;

    public static final CharacterEncodingFilter FILTER = new CharacterEncodingFilter("UTF-8", true);

    protected MockMvc initMockMvc(Object... controllers) {
        return MockMvcBuilders
                .standaloneSetup(controllers)
                .addFilter(FILTER)
                .build();
    }

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
