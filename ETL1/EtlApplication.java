package com.example.sqlspringbatch.ETL1;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing
public class EtlApplication {

    public static void main(String[] args) {
        SpringApplication.run(EtlApplication.class, args);
    }

    @Bean
    CommandLineRunner run(EtlService etlService) {
        return args -> {
            etlService.processFile1("C:\\Users\\hp\\Downloads\\SQL-SpringBatch\\src\\main\\java\\com\\example\\sqlspringbatch\\file1.csv");
            etlService.processFile2("C:\\Users\\hp\\Downloads\\SQL-SpringBatch\\src\\main\\java\\com\\example\\sqlspringbatch\\file2.csv");
            etlService.processOrderFile("C:\\Users\\hp\\Downloads\\SQL-SpringBatch\\src\\main\\java\\com\\example\\sqlspringbatch\\file1.csv",
                    "C:\\Users\\hp\\Downloads\\SQL-SpringBatch\\src\\main\\java\\com\\example\\sqlspringbatch\\file2.csv");
        };
    }
}