package completable;

import completable.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.concurrent.CompletableFuture;

public class Runner implements CommandLineRunner {

    @Autowired
    private AsyncService asyncService;

    @Override public void run(String... args) throws Exception {
        CompletableFuture<String> futureTimedGreeting = asyncService.asyncTimeoutGreeting()
                .exceptionally(Throwable::getMessage);

        System.out.println(futureTimedGreeting.join());
    }
}
