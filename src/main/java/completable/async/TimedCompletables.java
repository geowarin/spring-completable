package completable.async;

import com.google.common.util.concurrent.UncheckedTimeoutException;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

public class TimedCompletables {
    public static <T> CompletableFuture<T> failAfter(Duration duration) {
        return CompletableFuture.supplyAsync(() -> {
            AsyncUtil.sleep(duration);
            throw new UncheckedTimeoutException("Time out " + duration);
        });
    }

    public static Executor timed(ExecutorService executorService, Duration duration) {
        return new TimeOutExecutorService(executorService, duration);
    }

    static class TimeOutExecutorService extends CompletableExecutors.DelegatingCompletableExecutorService {
        private final Duration timeout;

        TimeOutExecutorService(ExecutorService delegate, Duration timeout) {
            super(delegate);
            this.timeout = timeout;
        }

        @Override public <T> CompletableFuture<T> submit(Callable<T> task) {
            CompletableFuture<T> cf = super.submit(task);
            return cf.applyToEither(failAfter(timeout), Function.identity());
        }
    }
}
