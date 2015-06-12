package completable;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import completable.async.CompletableExecutors;
import completable.async.TimedCompletables;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer {
    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    public Executor getAsyncExecutor() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("async-%d").build();
        return CompletableExecutors.completable(Executors.newFixedThreadPool(10, threadFactory));
    }

    @Bean(name = "timed")
    public Executor timeoutExecutor() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("timed-%d").build();
        return TimedCompletables.timed(Executors.newFixedThreadPool(10, threadFactory), Duration.ofSeconds(2));
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> logger.error("Uncaught async error", ex);
    }
}