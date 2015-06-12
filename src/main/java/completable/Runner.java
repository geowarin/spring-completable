package completable;

import completable.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class Runner implements CommandLineRunner {

    @Autowired
    private AsyncService asyncService;

    @Override public void run(String... args) throws Exception {

        IntStream.rangeClosed(1, 10)
                .mapToObj(__ -> asyncService.asyncTimeoutGreeting().exceptionally(Throwable::getMessage))
                .forEach(this::printResult);
    }

    private void printResult(CompletableFuture<String> future) {
        future.thenRun(() -> System.out.println(future.join()));
    }
}
