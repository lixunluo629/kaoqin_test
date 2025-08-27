package org.terracotta.offheapstore.buffersource;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.offheapstore.util.FindbugsSuppressWarnings;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/buffersource/TimingBufferSource.class */
public class TimingBufferSource implements BufferSource {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) TimingBufferSource.class);
    private final BufferSource delegate;
    private final long slowNanos;
    private final long criticalNanos;
    private final boolean haltOnCritical;

    public TimingBufferSource(BufferSource source, long slow, TimeUnit slowUnit, long critical, TimeUnit criticalUnit, boolean haltOnCritical) {
        this.delegate = source;
        this.slowNanos = slowUnit.toNanos(slow);
        this.criticalNanos = criticalUnit.toNanos(critical);
        this.haltOnCritical = haltOnCritical;
    }

    @Override // org.terracotta.offheapstore.buffersource.BufferSource
    public ByteBuffer allocateBuffer(int size) {
        long beforeAllocationTime = System.nanoTime();
        try {
            ByteBuffer byteBufferAllocateBuffer = this.delegate.allocateBuffer(size);
            long allocationDelay = System.nanoTime() - beforeAllocationTime;
            if (allocationDelay >= this.criticalNanos) {
                if (this.haltOnCritical) {
                    LOGGER.error("Off heap memory allocation is way too slow - attempting to halt VM to prevent swap depletion. Please review your -XX:MaxDirectMemorySize and make sure the OS has sufficient resources.");
                    commitSuicide("attempted VM halt");
                } else {
                    LOGGER.error("Off heap memory allocation is way too slow. Please review your -XX:MaxDirectMemorySize and make sure the OS has sufficient resources.");
                }
            } else if (allocationDelay > this.slowNanos) {
                LOGGER.warn("Off heap memory allocation is too slow - is the OS swapping?");
            }
            return byteBufferAllocateBuffer;
        } catch (Throwable th) {
            long allocationDelay2 = System.nanoTime() - beforeAllocationTime;
            if (allocationDelay2 >= this.criticalNanos) {
                if (this.haltOnCritical) {
                    LOGGER.error("Off heap memory allocation is way too slow - attempting to halt VM to prevent swap depletion. Please review your -XX:MaxDirectMemorySize and make sure the OS has sufficient resources.");
                    commitSuicide("attempted VM halt");
                } else {
                    LOGGER.error("Off heap memory allocation is way too slow. Please review your -XX:MaxDirectMemorySize and make sure the OS has sufficient resources.");
                }
            } else if (allocationDelay2 > this.slowNanos) {
                LOGGER.warn("Off heap memory allocation is too slow - is the OS swapping?");
            }
            throw th;
        }
    }

    private static void commitSuicide(String msg) {
        Thread t = new Thread() { // from class: org.terracotta.offheapstore.buffersource.TimingBufferSource.1
            @Override // java.lang.Thread, java.lang.Runnable
            @FindbugsSuppressWarnings({"DM_EXIT"})
            public void run() throws InterruptedException {
                try {
                    System.exit(-1);
                } catch (SecurityException ex) {
                    TimingBufferSource.LOGGER.info("SecurityException prevented system exit", (Throwable) ex);
                }
                while (true) {
                    try {
                        Thread.sleep(5000L);
                        TimingBufferSource.LOGGER.error("VM is in an unreliable state - please abort it!");
                    } catch (InterruptedException e) {
                        TimingBufferSource.LOGGER.info("JVM Instability logger terminated by interrupt");
                        return;
                    }
                }
            }
        };
        t.setDaemon(true);
        t.start();
        throw new Error(msg);
    }
}
