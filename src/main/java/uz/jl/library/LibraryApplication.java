package uz.jl.library;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return (args -> {
            try {
                Path uploads = Paths.get("/uploads");
                if (Files.isDirectory(uploads) && !Files.exists(uploads)) {
                    Files.createDirectories(uploads);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
