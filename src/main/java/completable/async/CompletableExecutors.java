package completable.async;


import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static java.util.concurrent.Executors.callable;

/**
 * Inspired by:
 * http://binkley.blogspot.fr/2014/12/completablefuture-and-executorservice.html
 */
public final class CompletableExecutors {

    public static CompletableExecutorService completable(ExecutorService delegate) {
        return new DelegatingCompletableExecutorService(delegate);
    }

    /**
     * DelegatingCompletableExecutorService {@code ExecutorService} to covariantly return {@code
     * CompletableFuture} in place of {@code Future}.
     */
    public interface CompletableExecutorService extends ExecutorService {
        /**
         * @return a completable future representing pending completion of the
         * task, never missing
         */
        @Override <T> CompletableFuture<T> submit(Callable<T> task);

        /**
         * @return a completable future representing pending completion of the
         * task, never missing
         */
        @Override <T> CompletableFuture<T> submit(Runnable task, T result);

        /**
         * @return a completable future representing pending completion of the
         * task, never missing
         */
        @Override CompletableFuture<?> submit(Runnable task);
    }

    static class DelegatingCompletableExecutorService extends DelegatingExecutorService implements CompletableExecutorService {

        DelegatingCompletableExecutorService(ExecutorService threads) {
            super(threads);
        }

        @Override public <T> CompletableFuture<T> submit(Callable<T> task) {
            final CompletableFuture<T> cf = new CompletableFuture<>();
            delegate.submit(() -> {
                try {
                    cf.complete(task.call());
                } catch (CancellationException e) {
                    cf.cancel(true);
                } catch (Exception e) {
                    cf.completeExceptionally(e);
                }
            });
            return cf;
        }

        @Override public <T> CompletableFuture<T> submit(Runnable task, T result) {
            return submit(callable(task, result));
        }

        @Override public CompletableFuture<?> submit(Runnable task) {
            return submit(callable(task));
        }
    }

    /**
     * Executor service that delegates everything to another one
     */
    static class DelegatingExecutorService implements ExecutorService {
        protected ExecutorService delegate;

        public DelegatingExecutorService(ExecutorService executorService) {
            this.delegate = executorService;
        }

        @Override public <T> Future<T> submit(Callable<T> task) {
            return delegate.submit(task);
        }

        @Override public <T> Future<T> submit(Runnable task, T result) {
            return delegate.submit(task, result);
        }

        @Override public Future<?> submit(Runnable task) {
            return delegate.submit(task);
        }

        @Override public void shutdown() {
            delegate.shutdown();
        }

        @Override public List<Runnable> shutdownNow() {
            return delegate.shutdownNow();
        }

        @Override public boolean isShutdown() {
            return delegate.isShutdown();
        }

        @Override public boolean isTerminated() {
            return delegate.isTerminated();
        }

        @Override public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return delegate.awaitTermination(timeout, unit);
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
            return delegate.invokeAll(tasks);
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
            return delegate.invokeAll(tasks, timeout, unit);
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
            return delegate.invokeAny(tasks);
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return delegate.invokeAny(tasks, timeout, unit);
        }

        @Override public void execute(Runnable command) {
            delegate.execute(command);
        }
    }
}
