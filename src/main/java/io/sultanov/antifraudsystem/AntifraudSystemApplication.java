package io.sultanov.antifraudsystem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@SpringBootApplication
public class AntifraudSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AntifraudSystemApplication.class, args);
    }
}
