package completable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class SpringCompletableApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCompletableApplication.class, args);
    }

    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public Runner runner() {
        return new Runner();
    }
}
