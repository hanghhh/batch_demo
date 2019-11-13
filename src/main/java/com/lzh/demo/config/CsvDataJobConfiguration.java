package com.lzh.demo.config;


import com.lzh.demo.entity.CsvData;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.io.File;

@Configuration
public class CsvDataJobConfiguration {
    private static final int CHUNK_SIZE = 10000;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;




    /**
     * 配置job
     * @param csvDataStep
     * @return
     */
    @Bean
    public Job csvDataJob(@Qualifier("csvDataStep") Step csvDataStep) {
        return jobBuilderFactory.get("csvDataJob").listener(csvJobListener())
                .start(csvDataStep)
                .build();
    }

    /**
     * 配置step
     * @param csvReader
     * @param csvItemWriter
     * @return
     */
    @Bean
    public Step csvDataStep(@Qualifier("csvReader") FlatFileItemReader<CsvData> csvReader,
                            @Qualifier("csvItemWriter") JdbcBatchItemWriter<CsvData> csvItemWriter) {
        return stepBuilderFactory.get("csvDataStep")
                .<CsvData, CsvData>chunk(CHUNK_SIZE)
                .reader(csvReader).faultTolerant()
                .listener(csvReaderListener())
                .writer(csvItemWriter)
                .listener(csvDataWriteListener())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<CsvData> csvReader(@Value("#{jobParameters[path]}") String path) {
        FlatFileItemReader<CsvData> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(new File(path)));
        reader.setLineMapper(new CsvDataLineMapper());
        return reader;
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<CsvData> csvItemWriter() {
        JdbcBatchItemWriter<CsvData> jdbcBatchItemWriter = new JdbcBatchItemWriter<CsvData>();
        jdbcBatchItemWriter.setDataSource(dataSource);
        jdbcBatchItemWriter.setSql("INSERT INTO lzh_test "
                + "(data_line) values "
                + "(:dataLine)");
        jdbcBatchItemWriter
                .setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<CsvData>());
        return jdbcBatchItemWriter;
    }


    @Bean
    public CsvJobListener csvJobListener() {
        return new CsvJobListener();
    }

    @Bean
    @StepScope
    public CsvReaderListener csvReaderListener() {
        return new CsvReaderListener();
    }

    @Bean
    @StepScope
    public CsvDataWriteListener csvDataWriteListener() {
        return new CsvDataWriteListener();
    }

}
