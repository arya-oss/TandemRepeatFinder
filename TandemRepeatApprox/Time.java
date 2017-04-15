import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.TimeUnit;

public class Time {
    /** Get user time in milliseconds. */
    public static long currentUser() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ? TimeUnit.NANOSECONDS.toMillis(bean.getCurrentThreadUserTime()) : 0L;
    }
}
