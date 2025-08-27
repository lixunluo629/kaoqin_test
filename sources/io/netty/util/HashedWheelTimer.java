package io.netty.util;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Collections;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.tomcat.jni.Time;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/HashedWheelTimer.class */
public class HashedWheelTimer implements Timer {
    static final InternalLogger logger;
    private static final AtomicInteger INSTANCE_COUNTER;
    private static final AtomicBoolean WARNED_TOO_MANY_INSTANCES;
    private static final int INSTANCE_COUNT_LIMIT = 64;
    private static final long MILLISECOND_NANOS;
    private static final ResourceLeakDetector<HashedWheelTimer> leakDetector;
    private static final AtomicIntegerFieldUpdater<HashedWheelTimer> WORKER_STATE_UPDATER;
    private final ResourceLeakTracker<HashedWheelTimer> leak;
    private final Worker worker;
    private final Thread workerThread;
    public static final int WORKER_STATE_INIT = 0;
    public static final int WORKER_STATE_STARTED = 1;
    public static final int WORKER_STATE_SHUTDOWN = 2;
    private volatile int workerState;
    private final long tickDuration;
    private final HashedWheelBucket[] wheel;
    private final int mask;
    private final CountDownLatch startTimeInitialized;
    private final Queue<HashedWheelTimeout> timeouts;
    private final Queue<HashedWheelTimeout> cancelledTimeouts;
    private final AtomicLong pendingTimeouts;
    private final long maxPendingTimeouts;
    private volatile long startTime;
    static final /* synthetic */ boolean $assertionsDisabled;

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$202(io.netty.util.HashedWheelTimer r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.startTime = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.util.HashedWheelTimer.access$202(io.netty.util.HashedWheelTimer, long):long");
    }

    static {
        $assertionsDisabled = !HashedWheelTimer.class.desiredAssertionStatus();
        logger = InternalLoggerFactory.getInstance((Class<?>) HashedWheelTimer.class);
        INSTANCE_COUNTER = new AtomicInteger();
        WARNED_TOO_MANY_INSTANCES = new AtomicBoolean();
        MILLISECOND_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
        leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(HashedWheelTimer.class, 1);
        WORKER_STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimer.class, "workerState");
    }

    public HashedWheelTimer() {
        this(Executors.defaultThreadFactory());
    }

    public HashedWheelTimer(long tickDuration, TimeUnit unit) {
        this(Executors.defaultThreadFactory(), tickDuration, unit);
    }

    public HashedWheelTimer(long tickDuration, TimeUnit unit, int ticksPerWheel) {
        this(Executors.defaultThreadFactory(), tickDuration, unit, ticksPerWheel);
    }

    public HashedWheelTimer(ThreadFactory threadFactory) {
        this(threadFactory, 100L, TimeUnit.MILLISECONDS);
    }

    public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit) {
        this(threadFactory, tickDuration, unit, 512);
    }

    public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel) {
        this(threadFactory, tickDuration, unit, ticksPerWheel, true);
    }

    public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel, boolean leakDetection) {
        this(threadFactory, tickDuration, unit, ticksPerWheel, leakDetection, -1L);
    }

    public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel, boolean leakDetection, long maxPendingTimeouts) {
        this.worker = new Worker();
        this.startTimeInitialized = new CountDownLatch(1);
        this.timeouts = PlatformDependent.newMpscQueue();
        this.cancelledTimeouts = PlatformDependent.newMpscQueue();
        this.pendingTimeouts = new AtomicLong(0L);
        ObjectUtil.checkNotNull(threadFactory, "threadFactory");
        ObjectUtil.checkNotNull(unit, "unit");
        ObjectUtil.checkPositive(tickDuration, "tickDuration");
        ObjectUtil.checkPositive(ticksPerWheel, "ticksPerWheel");
        this.wheel = createWheel(ticksPerWheel);
        this.mask = this.wheel.length - 1;
        long duration = unit.toNanos(tickDuration);
        if (duration >= Long.MAX_VALUE / this.wheel.length) {
            throw new IllegalArgumentException(String.format("tickDuration: %d (expected: 0 < tickDuration in nanos < %d", Long.valueOf(tickDuration), Long.valueOf(Long.MAX_VALUE / this.wheel.length)));
        }
        if (duration < MILLISECOND_NANOS) {
            logger.warn("Configured tickDuration {} smaller then {}, using 1ms.", Long.valueOf(tickDuration), Long.valueOf(MILLISECOND_NANOS));
            this.tickDuration = MILLISECOND_NANOS;
        } else {
            this.tickDuration = duration;
        }
        this.workerThread = threadFactory.newThread(this.worker);
        this.leak = (leakDetection || !this.workerThread.isDaemon()) ? leakDetector.track(this) : null;
        this.maxPendingTimeouts = maxPendingTimeouts;
        if (INSTANCE_COUNTER.incrementAndGet() > 64 && WARNED_TOO_MANY_INSTANCES.compareAndSet(false, true)) {
            reportTooManyInstances();
        }
    }

    protected void finalize() throws Throwable {
        try {
            super.finalize();
            if (WORKER_STATE_UPDATER.getAndSet(this, 2) != 2) {
                INSTANCE_COUNTER.decrementAndGet();
            }
        } catch (Throwable th) {
            if (WORKER_STATE_UPDATER.getAndSet(this, 2) != 2) {
                INSTANCE_COUNTER.decrementAndGet();
            }
            throw th;
        }
    }

    private static HashedWheelBucket[] createWheel(int ticksPerWheel) {
        if (ticksPerWheel <= 0) {
            throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + ticksPerWheel);
        }
        if (ticksPerWheel > 1073741824) {
            throw new IllegalArgumentException("ticksPerWheel may not be greater than 2^30: " + ticksPerWheel);
        }
        HashedWheelBucket[] wheel = new HashedWheelBucket[normalizeTicksPerWheel(ticksPerWheel)];
        for (int i = 0; i < wheel.length; i++) {
            wheel[i] = new HashedWheelBucket();
        }
        return wheel;
    }

    private static int normalizeTicksPerWheel(int ticksPerWheel) {
        int i = 1;
        while (true) {
            int normalizedTicksPerWheel = i;
            if (normalizedTicksPerWheel < ticksPerWheel) {
                i = normalizedTicksPerWheel << 1;
            } else {
                return normalizedTicksPerWheel;
            }
        }
    }

    public void start() throws InterruptedException {
        switch (WORKER_STATE_UPDATER.get(this)) {
            case 0:
                if (WORKER_STATE_UPDATER.compareAndSet(this, 0, 1)) {
                    this.workerThread.start();
                    break;
                }
                break;
            case 1:
                break;
            case 2:
                throw new IllegalStateException("cannot be started once stopped");
            default:
                throw new Error("Invalid WorkerState");
        }
        while (this.startTime == 0) {
            try {
                this.startTimeInitialized.await();
            } catch (InterruptedException e) {
            }
        }
    }

    @Override // io.netty.util.Timer
    public Set<Timeout> stop() {
        if (Thread.currentThread() == this.workerThread) {
            throw new IllegalStateException(HashedWheelTimer.class.getSimpleName() + ".stop() cannot be called from " + TimerTask.class.getSimpleName());
        }
        if (!WORKER_STATE_UPDATER.compareAndSet(this, 1, 2)) {
            if (WORKER_STATE_UPDATER.getAndSet(this, 2) != 2) {
                INSTANCE_COUNTER.decrementAndGet();
                if (this.leak != null) {
                    boolean closed = this.leak.close(this);
                    if (!$assertionsDisabled && !closed) {
                        throw new AssertionError();
                    }
                }
            }
            return Collections.emptySet();
        }
        boolean interrupted = false;
        while (this.workerThread.isAlive()) {
            try {
                this.workerThread.interrupt();
                try {
                    this.workerThread.join(100L);
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            } catch (Throwable th) {
                INSTANCE_COUNTER.decrementAndGet();
                if (this.leak != null) {
                    boolean closed2 = this.leak.close(this);
                    if (!$assertionsDisabled && !closed2) {
                        throw new AssertionError();
                    }
                }
                throw th;
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        INSTANCE_COUNTER.decrementAndGet();
        if (this.leak != null) {
            boolean closed3 = this.leak.close(this);
            if (!$assertionsDisabled && !closed3) {
                throw new AssertionError();
            }
        }
        return this.worker.unprocessedTimeouts();
    }

    @Override // io.netty.util.Timer
    public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) throws InterruptedException {
        ObjectUtil.checkNotNull(task, "task");
        ObjectUtil.checkNotNull(unit, "unit");
        long pendingTimeoutsCount = this.pendingTimeouts.incrementAndGet();
        if (this.maxPendingTimeouts > 0 && pendingTimeoutsCount > this.maxPendingTimeouts) {
            this.pendingTimeouts.decrementAndGet();
            throw new RejectedExecutionException("Number of pending timeouts (" + pendingTimeoutsCount + ") is greater than or equal to maximum allowed pending timeouts (" + this.maxPendingTimeouts + ")");
        }
        start();
        long deadline = (System.nanoTime() + unit.toNanos(delay)) - this.startTime;
        if (delay > 0 && deadline < 0) {
            deadline = Long.MAX_VALUE;
        }
        HashedWheelTimeout timeout = new HashedWheelTimeout(this, task, deadline);
        this.timeouts.add(timeout);
        return timeout;
    }

    public long pendingTimeouts() {
        return this.pendingTimeouts.get();
    }

    private static void reportTooManyInstances() {
        if (logger.isErrorEnabled()) {
            String resourceType = StringUtil.simpleClassName((Class<?>) HashedWheelTimer.class);
            logger.error("You are creating too many " + resourceType + " instances. " + resourceType + " is a shared resource that must be reused across the JVM,so that only a few instances are created.");
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/HashedWheelTimer$Worker.class */
    private final class Worker implements Runnable {
        private final Set<Timeout> unprocessedTimeouts;
        private long tick;

        private Worker() {
            this.unprocessedTimeouts = new HashSet();
        }

        /* JADX WARN: Failed to check method for inline after forced processio.netty.util.HashedWheelTimer.access$202(io.netty.util.HashedWheelTimer, long):long */
        @Override // java.lang.Runnable
        public void run() throws InterruptedException {
            HashedWheelTimer.access$202(HashedWheelTimer.this, System.nanoTime());
            if (HashedWheelTimer.this.startTime == 0) {
                HashedWheelTimer.access$202(HashedWheelTimer.this, 1L);
            }
            HashedWheelTimer.this.startTimeInitialized.countDown();
            do {
                long deadline = waitForNextTick();
                if (deadline > 0) {
                    int idx = (int) (this.tick & HashedWheelTimer.this.mask);
                    processCancelledTasks();
                    HashedWheelBucket bucket = HashedWheelTimer.this.wheel[idx];
                    transferTimeoutsToBuckets();
                    bucket.expireTimeouts(deadline);
                    this.tick++;
                }
            } while (HashedWheelTimer.WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == 1);
            for (HashedWheelBucket bucket2 : HashedWheelTimer.this.wheel) {
                bucket2.clearTimeouts(this.unprocessedTimeouts);
            }
            while (true) {
                HashedWheelTimeout timeout = (HashedWheelTimeout) HashedWheelTimer.this.timeouts.poll();
                if (timeout != null) {
                    if (!timeout.isCancelled()) {
                        this.unprocessedTimeouts.add(timeout);
                    }
                } else {
                    processCancelledTasks();
                    return;
                }
            }
        }

        private void transferTimeoutsToBuckets() {
            HashedWheelTimeout timeout;
            for (int i = 0; i < 100000 && (timeout = (HashedWheelTimeout) HashedWheelTimer.this.timeouts.poll()) != null; i++) {
                if (timeout.state() != 1) {
                    long calculated = timeout.deadline / HashedWheelTimer.this.tickDuration;
                    timeout.remainingRounds = (calculated - this.tick) / HashedWheelTimer.this.wheel.length;
                    long ticks = Math.max(calculated, this.tick);
                    int stopIndex = (int) (ticks & HashedWheelTimer.this.mask);
                    HashedWheelBucket bucket = HashedWheelTimer.this.wheel[stopIndex];
                    bucket.addTimeout(timeout);
                }
            }
        }

        private void processCancelledTasks() {
            while (true) {
                HashedWheelTimeout timeout = (HashedWheelTimeout) HashedWheelTimer.this.cancelledTimeouts.poll();
                if (timeout != null) {
                    try {
                        timeout.remove();
                    } catch (Throwable t) {
                        if (HashedWheelTimer.logger.isWarnEnabled()) {
                            HashedWheelTimer.logger.warn("An exception was thrown while process a cancellation task", t);
                        }
                    }
                } else {
                    return;
                }
            }
        }

        private long waitForNextTick() throws InterruptedException {
            long deadline = HashedWheelTimer.this.tickDuration * (this.tick + 1);
            while (true) {
                long currentTime = System.nanoTime() - HashedWheelTimer.this.startTime;
                long sleepTimeMs = ((deadline - currentTime) + 999999) / Time.APR_USEC_PER_SEC;
                if (sleepTimeMs <= 0) {
                    if (currentTime == Long.MIN_VALUE) {
                        return -9223372036854775807L;
                    }
                    return currentTime;
                }
                if (PlatformDependent.isWindows()) {
                    sleepTimeMs = (sleepTimeMs / 10) * 10;
                    if (sleepTimeMs == 0) {
                        sleepTimeMs = 1;
                    }
                }
                try {
                    Thread.sleep(sleepTimeMs);
                } catch (InterruptedException e) {
                    if (HashedWheelTimer.WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == 2) {
                        return Long.MIN_VALUE;
                    }
                }
            }
        }

        public Set<Timeout> unprocessedTimeouts() {
            return Collections.unmodifiableSet(this.unprocessedTimeouts);
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/HashedWheelTimer$HashedWheelTimeout.class */
    private static final class HashedWheelTimeout implements Timeout {
        private static final int ST_INIT = 0;
        private static final int ST_CANCELLED = 1;
        private static final int ST_EXPIRED = 2;
        private static final AtomicIntegerFieldUpdater<HashedWheelTimeout> STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimeout.class, "state");
        private final HashedWheelTimer timer;
        private final TimerTask task;
        private final long deadline;
        private volatile int state = 0;
        long remainingRounds;
        HashedWheelTimeout next;
        HashedWheelTimeout prev;
        HashedWheelBucket bucket;

        HashedWheelTimeout(HashedWheelTimer timer, TimerTask task, long deadline) {
            this.timer = timer;
            this.task = task;
            this.deadline = deadline;
        }

        @Override // io.netty.util.Timeout
        public Timer timer() {
            return this.timer;
        }

        @Override // io.netty.util.Timeout
        public TimerTask task() {
            return this.task;
        }

        @Override // io.netty.util.Timeout
        public boolean cancel() {
            if (compareAndSetState(0, 1)) {
                this.timer.cancelledTimeouts.add(this);
                return true;
            }
            return false;
        }

        void remove() {
            HashedWheelBucket bucket = this.bucket;
            if (bucket == null) {
                this.timer.pendingTimeouts.decrementAndGet();
            } else {
                bucket.remove(this);
            }
        }

        public boolean compareAndSetState(int expected, int state) {
            return STATE_UPDATER.compareAndSet(this, expected, state);
        }

        public int state() {
            return this.state;
        }

        @Override // io.netty.util.Timeout
        public boolean isCancelled() {
            return state() == 1;
        }

        @Override // io.netty.util.Timeout
        public boolean isExpired() {
            return state() == 2;
        }

        public void expire() {
            if (!compareAndSetState(0, 2)) {
                return;
            }
            try {
                this.task.run(this);
            } catch (Throwable t) {
                if (HashedWheelTimer.logger.isWarnEnabled()) {
                    HashedWheelTimer.logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName() + '.', t);
                }
            }
        }

        public String toString() {
            long currentTime = System.nanoTime();
            long remaining = (this.deadline - currentTime) + this.timer.startTime;
            StringBuilder buf = new StringBuilder(192).append(StringUtil.simpleClassName(this)).append('(').append("deadline: ");
            if (remaining > 0) {
                buf.append(remaining).append(" ns later");
            } else if (remaining < 0) {
                buf.append(-remaining).append(" ns ago");
            } else {
                buf.append("now");
            }
            if (isCancelled()) {
                buf.append(", cancelled");
            }
            return buf.append(", task: ").append(task()).append(')').toString();
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/HashedWheelTimer$HashedWheelBucket.class */
    private static final class HashedWheelBucket {
        private HashedWheelTimeout head;
        private HashedWheelTimeout tail;
        static final /* synthetic */ boolean $assertionsDisabled;

        private HashedWheelBucket() {
        }

        static {
            $assertionsDisabled = !HashedWheelTimer.class.desiredAssertionStatus();
        }

        public void addTimeout(HashedWheelTimeout timeout) {
            if (!$assertionsDisabled && timeout.bucket != null) {
                throw new AssertionError();
            }
            timeout.bucket = this;
            if (this.head == null) {
                this.tail = timeout;
                this.head = timeout;
            } else {
                this.tail.next = timeout;
                timeout.prev = this.tail;
                this.tail = timeout;
            }
        }

        public void expireTimeouts(long deadline) {
            HashedWheelTimeout hashedWheelTimeout = this.head;
            while (true) {
                HashedWheelTimeout timeout = hashedWheelTimeout;
                if (timeout != null) {
                    HashedWheelTimeout next = timeout.next;
                    if (timeout.remainingRounds <= 0) {
                        next = remove(timeout);
                        if (timeout.deadline <= deadline) {
                            timeout.expire();
                        } else {
                            throw new IllegalStateException(String.format("timeout.deadline (%d) > deadline (%d)", Long.valueOf(timeout.deadline), Long.valueOf(deadline)));
                        }
                    } else if (timeout.isCancelled()) {
                        next = remove(timeout);
                    } else {
                        timeout.remainingRounds--;
                    }
                    hashedWheelTimeout = next;
                } else {
                    return;
                }
            }
        }

        public HashedWheelTimeout remove(HashedWheelTimeout timeout) {
            HashedWheelTimeout next = timeout.next;
            if (timeout.prev != null) {
                timeout.prev.next = next;
            }
            if (timeout.next != null) {
                timeout.next.prev = timeout.prev;
            }
            if (timeout == this.head) {
                if (timeout == this.tail) {
                    this.tail = null;
                    this.head = null;
                } else {
                    this.head = next;
                }
            } else if (timeout == this.tail) {
                this.tail = timeout.prev;
            }
            timeout.prev = null;
            timeout.next = null;
            timeout.bucket = null;
            timeout.timer.pendingTimeouts.decrementAndGet();
            return next;
        }

        public void clearTimeouts(Set<Timeout> set) {
            while (true) {
                HashedWheelTimeout timeout = pollTimeout();
                if (timeout == null) {
                    return;
                }
                if (!timeout.isExpired() && !timeout.isCancelled()) {
                    set.add(timeout);
                }
            }
        }

        private HashedWheelTimeout pollTimeout() {
            HashedWheelTimeout head = this.head;
            if (head == null) {
                return null;
            }
            HashedWheelTimeout next = head.next;
            if (next == null) {
                this.head = null;
                this.tail = null;
            } else {
                this.head = next;
                next.prev = null;
            }
            head.next = null;
            head.prev = null;
            head.bucket = null;
            return head;
        }
    }
}
