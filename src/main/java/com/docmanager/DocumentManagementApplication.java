package com.docmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.docmanager.repository")
public class DocumentManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(DocumentManagementApplication.class, args);
    }
}