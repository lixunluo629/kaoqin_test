package io.netty.util.internal.shaded.org.jctools.queues;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/shaded/org/jctools/queues/IndexedQueueSizeUtil.class */
public final class IndexedQueueSizeUtil {

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/shaded/org/jctools/queues/IndexedQueueSizeUtil$IndexedQueue.class */
    public interface IndexedQueue {
        long lvConsumerIndex();

        long lvProducerIndex();
    }

    public static int size(IndexedQueue iq) {
        long before;
        long currentProducerIndex;
        long after = iq.lvConsumerIndex();
        do {
            before = after;
            currentProducerIndex = iq.lvProducerIndex();
            after = iq.lvConsumerIndex();
        } while (before != after);
        long size = currentProducerIndex - after;
        if (size > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) size;
    }

    public static boolean isEmpty(IndexedQueue iq) {
        return iq.lvConsumerIndex() == iq.lvProducerIndex();
    }
}
