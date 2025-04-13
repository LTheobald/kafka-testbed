package uk.co.ltheobald.kafkatestbed;

import org.springframework.boot.SpringApplication;

public class LocalLauncher {
    public static void main(String... args) {
        SpringApplication
                .from(LocalLauncher::main)
                .with(TestContainerConfiguration.class)
                .run(args);
    }
}
