package com.elfn.inactiveuserarchiver;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class InactiveUserArchiverApplication {

    public static void main(String[] args) {
        SpringApplication.run(InactiveUserArchiverApplication.class, args);
    }

}
