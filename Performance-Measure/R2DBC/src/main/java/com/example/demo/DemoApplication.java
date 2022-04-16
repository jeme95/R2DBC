package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);

        // get the number of processors available to the Java virtual machine
        int numberOfProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("Number of processors available to this JVM: " + numberOfProcessors);
    }


}