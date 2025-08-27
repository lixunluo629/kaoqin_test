package org.springframework.data.util;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/StreamUtils.class */
public class StreamUtils {
    private StreamUtils() {
    }

    public static <T> Stream<T> createStreamFromIterator(Iterator<T> iterator) {
        Assert.notNull(iterator, "Iterator must not be null!");
        Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator, 256);
        Stream<T> stream = StreamSupport.stream(spliterator, false);
        return iterator instanceof CloseableIterator ? (Stream) stream.onClose(new CloseableIteratorDisposingRunnable((CloseableIterator) iterator)) : stream;
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/StreamUtils$CloseableIteratorDisposingRunnable.class */
    private static class CloseableIteratorDisposingRunnable implements Runnable {
        private CloseableIterator<?> closeable;

        public CloseableIteratorDisposingRunnable(CloseableIterator<?> closeable) {
            this.closeable = closeable;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.closeable != null) {
                this.closeable.close();
            }
        }
    }
}
