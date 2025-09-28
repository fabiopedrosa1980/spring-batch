package br.com.pedrosa.batch;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

@SpringBootApplication
public class BatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }

    @Bean
    FlatFileItemReader<Dog> dogFlatFileItemReader(@Value("classpath:/dogs.csv") Resource resource) {
        return new FlatFileItemReaderBuilder<Dog>()
                .linesToSkip(1)
                .resource(resource)
                .name("dogsCsvToDb")
                .fieldSetMapper(fieldSet -> new Dog(
                        fieldSet.readInt("id"),
                        fieldSet.readString("name"),
                        fieldSet.readString("description"),
                        fieldSet.readString("owner")))
                .delimited().names("id", "name", "description", "owner")
                .build();
    }

    @Bean
    JdbcBatchItemWriter<Dog> dogJdbcBatchItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Dog>()
                .dataSource(dataSource)
                .assertUpdates(true)
                .sql("insert into dog (id,name,description,owner) values (?,?,?,?)")
                .itemPreparedStatementSetter((item, ps) -> {
                    ps.setInt(1, item.id());
                    ps.setString(2, item.name);
                    ps.setString(3, item.description);
                    ps.setString(4, item.owner);
                })
                .build();
    }

    @Bean
    Step csvToDbStep(JobRepository jobRepository,
                     FlatFileItemReader<Dog> dogFlatFileItemReader,
                     JdbcBatchItemWriter<Dog> dogJdbcBatchItemWriter) {
        return new StepBuilder("csvToDbStep", jobRepository)
                .<Dog, Dog>chunk(10)
                .reader(dogFlatFileItemReader)
                .writer(dogJdbcBatchItemWriter)
                .build();
    }

    @Bean
    Job csvToDb(JobRepository jobRepository, Step step) {
        return new JobBuilder("csvToDb", jobRepository)
                .start(step)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    public record Dog(int id, String name, String description, String owner) {
    }
}
