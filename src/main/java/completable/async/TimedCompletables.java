package completable.async;

import java.time.Duration;
import java.util.concurrent.*;

public class TimedCompletables {

    public static Executor timed(ExecutorService executorService, Duration duration) {
        return new TimeOutExecutorService(executorService, duration);
    }

    static class TimeOutExecutorService extends CompletableExecutors.DelegatingCompletableExecutorService {
        private final Duration timeout;
        private final ScheduledExecutorService schedulerExecutor;

        TimeOutExecutorService(ExecutorService delegate, Duration timeout) {
            super(delegate);
            this.timeout = timeout;
            schedulerExecutor = Executors.newScheduledThreadPool(1);
        }

        // http://stackoverflow.com/questions/23575067/timeout-with-default-value-in-java-8-completablefuture/24457111#24457111
        @Override public <T> CompletableFuture<T> submit(Callable<T> task) {
            CompletableFuture<T> cf = new CompletableFuture<>();
            Future<?> future = delegate.submit(() -> {
                try {
                    cf.complete(task.call());
                } catch (CancellationException e) {
                    cf.cancel(true);
                } catch (Throwable ex) {
                    cf.completeExceptionally(ex);
                }
            });

            schedulerExecutor.schedule(() -> {
                if (!cf.isDone()) {
                    cf.completeExceptionally(new TimeoutException("Timeout after " + timeout));
                    future.cancel(true);
                }
            }, timeout.toMillis(), TimeUnit.MILLISECONDS);
            return cf;
        }
    }
}
