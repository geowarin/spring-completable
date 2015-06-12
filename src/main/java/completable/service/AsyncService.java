package completable.service;

import completable.async.AsyncUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
public class AsyncService {

    private static String[] greetings = new String[]{
            "hallo", "hallo", "hej", "hej", "bonjour", "hola",
            "ciao", "shalom", "fáilte", "kaixo", "konnichiwa",
            "saluton", "päivää", "selamat pagi", "gut de", "olá"
    };

    @Async
    public CompletableFuture<String> asyncGreeting() {
        AsyncUtil.randomSleep(3000, TimeUnit.MILLISECONDS);
        String result = AsyncUtil.getThreadName() + " - " + random(greetings);
        return CompletableFuture.completedFuture(result);
    }

    @Async("timed")
    public CompletableFuture<String> asyncTimeoutGreeting() {
        AsyncUtil.randomSleep(3000, TimeUnit.MILLISECONDS);
        String result = AsyncUtil.getThreadName() + " - " + random(greetings);
        return CompletableFuture.completedFuture(result);
    }

    @SafeVarargs public final <T> T random(T... elements) {
        LinkedList<T> greetings = new LinkedList<>(Arrays.asList(elements));
        Collections.shuffle(greetings, ThreadLocalRandom.current());
        return greetings.getFirst();
    }
}
