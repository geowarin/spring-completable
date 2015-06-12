package completable.time;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.concurrent.CompletableFuture;

@Aspect
@Component
public class ServiceProfiler {

    @Pointcut("execution(java.util.concurrent.CompletableFuture completable.service.*.*(..))")
    public void serviceMethods() {
    }

    @Around("serviceMethods()")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object output = pjp.proceed();
        stopWatch.stop();
        if (output instanceof CompletableFuture) {
            CompletableFuture future = (CompletableFuture) output;
            String debug = String.format("(%d ms)", stopWatch.getTotalTimeMillis());
//            return future.thenApply(o -> CompletableFuture.completedFuture(o + debug));
            future.thenAccept(o -> System.out.println(o + " - " + debug));
        }
        return output;
    }

}