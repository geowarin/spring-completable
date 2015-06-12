package completable.service;

import completable.async.AsyncUtil;
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
        AsyncUtil.randomSleep(3, TimeUnit.SECONDS);
        return CompletableFuture.completedFuture(AsyncUtil.getThreadName() + " - " + random("Hello", "Salute", "Greetings"));
    }

    @Async("timeoutExecutor")
    public CompletableFuture<String> asyncTimeoutGreeting() {
        AsyncUtil.randomSleep(3, TimeUnit.SECONDS);
        return CompletableFuture.completedFuture(AsyncUtil.getThreadName() + " - " + random("Hello", "Salute", "Greetings"));
    }

    @SafeVarargs public final <T> T random(T... elements) {
        LinkedList<T> greetings = new LinkedList<>(Arrays.asList(elements));
        Collections.shuffle(greetings, random);
        return greetings.getFirst();
    }
}
