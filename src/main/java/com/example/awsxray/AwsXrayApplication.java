package com.example.awsxray;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class AwsXrayApplication {
    public static void main(String[] args) {
        SpringApplication.run(AwsXrayApplication.class, args);
    }
}

