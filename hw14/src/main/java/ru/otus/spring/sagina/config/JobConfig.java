package ru.otus.spring.sagina.config;

import com.mongodb.lang.Nullable;
import lombok.extern.java.Log;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.sagina.model.Author;
import ru.otus.spring.sagina.model.Book;
import ru.otus.spring.sagina.model.BookComment;
import ru.otus.spring.sagina.model.Genre;

import javax.sql.DataSource;
import java.util.Map;
import java.util.function.Function;

@EnableBatchProcessing
@Configuration
@Log
public class JobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MongoTemplate mongoTemplate;
    private final DataSource dataSource;

    public JobConfig(MongoTemplate mongoTemplate,
                     DataSource dataSource,
                     JobBuilderFactory jobBuilderFactory,
                     StepBuilderFactory stepBuilderFactory) {
        this.mongoTemplate = mongoTemplate;
        this.dataSource = dataSource;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job importBookJob(Step authorLoadStep,
                             Step genreLoadStep,
                             Step bookLoadStep,
                             Step bookGenreLoadStep,
                             Step commentLoadStep) {
        return jobBuilderFactory.get("importAuthorJob")
                .incrementer(new RunIdIncrementer())
                .start(authorLoadStep)
                .next(genreLoadStep)
                .next(bookLoadStep)
                .next(bookGenreLoadStep)
                .next(commentLoadStep)
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@Nullable JobExecution jobExecution) {
                        log.info("Начало job" + jobExecution);
                    }

                    @Override
                    public void afterJob(@Nullable JobExecution jobExecution) {
                        log.info("Конец job " + jobExecution);
                    }
                })
                .build();
    }

    @Bean
    public Step authorLoadStep(MongoItemReader<Author> authorReader, ItemWriter<Author> authorWriter) {
        return stepBuilderFactory.get("authorLoadStep")
                .allowStartIfComplete(true)
                .<Author, Author>chunk(5)
                .reader(authorReader)
                .processor(Function.identity())
                .writer(authorWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public Step genreLoadStep(MongoItemReader<Genre> genreReader, ItemWriter<Genre> genreWriter) {
        return stepBuilderFactory.get("genreLoadStep")
                .allowStartIfComplete(true)
                .<Genre, Genre>chunk(5)
                .reader(genreReader)
                .processor(Function.identity())
                .writer(genreWriter)
                .build();
    }

    @Bean
    public Step bookLoadStep(MongoItemReader<Book> bookReader, ItemWriter<Book> bookWriter) {
        return stepBuilderFactory.get("bookLoadStep")
                .allowStartIfComplete(true)
                .<Book, Book>chunk(5)
                .reader(bookReader)
                .processor(Function.identity())
                .writer(bookWriter)
                .build();
    }

    @Bean
    public Step bookGenreLoadStep(MongoItemReader<Book> bookGenreReader, ItemWriter<Book> bookGenreWriter) {
        return stepBuilderFactory.get("bookGenreLoadStep")
                .allowStartIfComplete(true)
                .<Book, Book>chunk(5)
                .reader(bookGenreReader)
                .processor(Function.identity())
                .writer(bookGenreWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public Step commentLoadStep(MongoItemReader<BookComment> commentReader, ItemWriter<BookComment> commentWriter) {
        return stepBuilderFactory.get("commentLoadStep")
                .allowStartIfComplete(true)
                .<BookComment, BookComment>chunk(5)
                .reader(commentReader)
                .processor(Function.identity())
                .writer(commentWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public MongoItemReader<Author> authorReader() {
        return getMongoItemReader(Author.class, "authorReader");
    }

    @Bean
    public MongoItemReader<Genre> genreReader() {
        return getMongoItemReader(Genre.class, "genreReader");
    }

    @Bean
    public MongoItemReader<Book> bookReader() {
        return getMongoItemReader(Book.class, "bookReader");
    }

    @Bean
    public MongoItemReader<Book> bookGenreReader() {
        return getMongoItemReader(Book.class, "bookGenreReader");
    }

    @Bean
    public MongoItemReader<BookComment> commentReader() {
        return getMongoItemReader(BookComment.class, "commentReader");
    }

    @Bean
    public ItemWriter<Author> authorWriter() {
        return new JdbcBatchItemWriterBuilder<Author>()
                .sql("INSERT INTO author (author_id, name) VALUES (:id, :name)")
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public ItemWriter<Genre> genreWriter() {
        return new JdbcBatchItemWriterBuilder<Genre>()
                .sql("INSERT INTO genre (genre_id, name) VALUES (:id, :name)")
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public ItemWriter<Book> bookWriter() {
        return new JdbcBatchItemWriterBuilder<Book>()
                .sql("INSERT INTO book (book_id, name, author_id, description)"
                        + " VALUES (:id, :name, :author.id, :description)")
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public ItemWriter<Book> bookGenreWriter() {
        return new BookGenreItemWriter(dataSource);
    }

    @Bean
    public ItemWriter<BookComment> commentWriter() {
        return new JdbcBatchItemWriterBuilder<BookComment>()
                .sql("INSERT INTO book_comment (comment_id, message, book_id)"
                        + " VALUES (:id, :message, :book.id)")
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .dataSource(dataSource)
                .build();
    }

    private <T> MongoItemReader<T> getMongoItemReader(Class<T> clazz, String readerName) {
        return new MongoItemReaderBuilder<T>()
                .targetType(clazz)
                .name(readerName)
                .template(mongoTemplate)
                .jsonQuery("{}")
                .sorts(Map.of("name", Sort.Direction.ASC))
                .build();
    }
}
