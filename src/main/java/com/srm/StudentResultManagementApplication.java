package com.srm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Student Result Management System.
 *
 * Run this class to start the Spring Boot server on http://localhost:8080
 *
 * H2 Database Console: http://localhost:8080/h2-console
 *   JDBC URL : jdbc:h2:file:./data/srm-db
 *   Username : sa
 *   Password : (leave blank)
 */
@SpringBootApplication
public class StudentResultManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentResultManagementApplication.class, args);
    }
}
