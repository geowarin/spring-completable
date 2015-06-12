package completable.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class AsyncService {

    @Autowired
    private Random random;

    @Async
    public CompletableFuture<String> asyncGreeting() {
        return CompletableFuture.supplyAsync(() -> {
            randomSleep(3, TimeUnit.SECONDS);
            return random("Hello", "Salute", "Greetings");
        });
    }

    @Async("timeoutExecutor")
    public CompletableFuture<String> asyncTimeoutGreeting() {
        System.out.println("service1 - " + Thread.currentThread().getName());

        return CompletableFuture.supplyAsync(() -> {
            System.out.println("service2 - " + Thread.currentThread().getName());
            randomSleep(3, TimeUnit.SECONDS);
            return random("Hello", "Salute", "Greetings");
        });
    }

    private void randomSleep(int duration, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(random.nextInt(duration));
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    @SafeVarargs public final <T> T random(T... elements) {
        LinkedList<T> greetings = new LinkedList<>(Arrays.asList(elements));
        Collections.shuffle(greetings, random);
        return greetings.getFirst();
    }
}
