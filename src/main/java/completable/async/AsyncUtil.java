package completable.async;

import com.google.common.base.Throwables;

import java.time.Duration;

public class AsyncUtil {
    static void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Throwables.propagate(e);
        }
    }
}
