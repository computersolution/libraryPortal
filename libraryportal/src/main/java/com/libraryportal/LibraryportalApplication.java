package com.libraryportal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryportalApplication {
    private static final Logger logger = LoggerFactory.getLogger(LibraryportalApplication.class);

    /**
     * Entry point of the Library Portal Application.
     * 
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        logger.info("Starting Library Portal Application...");
        SpringApplication.run(LibraryportalApplication.class, args);
        logger.info("Library Portal Application started successfully.");
    }
}
