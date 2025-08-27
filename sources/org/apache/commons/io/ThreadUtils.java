package org.apache.commons.io;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import org.apache.xmlbeans.SchemaType;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/ThreadUtils.class */
public final class ThreadUtils {
    private static int getNanosOfMilli(Duration duration) {
        return duration.getNano() % SchemaType.SIZE_BIG_INTEGER;
    }

    public static void sleep(Duration duration) throws InterruptedException {
        Instant finishInstant = Instant.now().plus((TemporalAmount) duration);
        Duration remainingDuration = duration;
        do {
            Thread.sleep(remainingDuration.toMillis(), getNanosOfMilli(remainingDuration));
            remainingDuration = Duration.between(Instant.now(), finishInstant);
        } while (!remainingDuration.isNegative());
    }

    @Deprecated
    public ThreadUtils() {
    }
}
