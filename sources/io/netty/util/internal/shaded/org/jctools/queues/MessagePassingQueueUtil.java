package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import io.netty.util.internal.shaded.org.jctools.util.PortableJvmInfo;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/shaded/org/jctools/queues/MessagePassingQueueUtil.class */
public final class MessagePassingQueueUtil {
    private MessagePassingQueueUtil() {
    }

    public static <E> int drain(MessagePassingQueue<E> queue, MessagePassingQueue.Consumer<E> c, int limit) {
        E e;
        if (null == c) {
            throw new IllegalArgumentException("c is null");
        }
        if (limit < 0) {
            throw new IllegalArgumentException("limit is negative: " + limit);
        }
        if (limit == 0) {
            return 0;
        }
        int i = 0;
        while (i < limit && (e = queue.relaxedPoll()) != null) {
            c.accept(e);
            i++;
        }
        return i;
    }

    public static <E> int drain(MessagePassingQueue<E> queue, MessagePassingQueue.Consumer<E> c) {
        if (null == c) {
            throw new IllegalArgumentException("c is null");
        }
        int i = 0;
        while (true) {
            E e = queue.relaxedPoll();
            if (e != null) {
                i++;
                c.accept(e);
            } else {
                return i;
            }
        }
    }

    public static <E> void drain(MessagePassingQueue<E> queue, MessagePassingQueue.Consumer<E> c, MessagePassingQueue.WaitStrategy wait, MessagePassingQueue.ExitCondition exit) {
        if (null == c) {
            throw new IllegalArgumentException("c is null");
        }
        if (null == wait) {
            throw new IllegalArgumentException("wait is null");
        }
        if (null == exit) {
            throw new IllegalArgumentException("exit condition is null");
        }
        int idleCounter = 0;
        while (exit.keepRunning()) {
            E e = queue.relaxedPoll();
            if (e == null) {
                idleCounter = wait.idle(idleCounter);
            } else {
                idleCounter = 0;
                c.accept(e);
            }
        }
    }

    public static <E> void fill(MessagePassingQueue<E> q, MessagePassingQueue.Supplier<E> s, MessagePassingQueue.WaitStrategy wait, MessagePassingQueue.ExitCondition exit) {
        if (null == wait) {
            throw new IllegalArgumentException("waiter is null");
        }
        if (null == exit) {
            throw new IllegalArgumentException("exit condition is null");
        }
        int iIdle = 0;
        while (true) {
            int idleCounter = iIdle;
            if (exit.keepRunning()) {
                if (q.fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH) == 0) {
                    iIdle = wait.idle(idleCounter);
                } else {
                    iIdle = 0;
                }
            } else {
                return;
            }
        }
    }

    public static <E> int fillBounded(MessagePassingQueue<E> q, MessagePassingQueue.Supplier<E> s) {
        return fillInBatchesToLimit(q, s, PortableJvmInfo.RECOMENDED_OFFER_BATCH, q.capacity());
    }

    public static <E> int fillInBatchesToLimit(MessagePassingQueue<E> q, MessagePassingQueue.Supplier<E> s, int batch, int limit) {
        long result = 0;
        do {
            int filled = q.fill(s, batch);
            if (filled == 0) {
                return (int) result;
            }
            result += filled;
        } while (result <= limit);
        return (int) result;
    }

    public static <E> int fillUnbounded(MessagePassingQueue<E> q, MessagePassingQueue.Supplier<E> s) {
        return fillInBatchesToLimit(q, s, PortableJvmInfo.RECOMENDED_OFFER_BATCH, 4096);
    }
}
