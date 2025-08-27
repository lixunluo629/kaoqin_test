package org.apache.commons.io.output;

import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.io.input.QueueInputStream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/QueueOutputStream.class */
public class QueueOutputStream extends OutputStream {
    private final BlockingQueue<Integer> blockingQueue;

    public QueueOutputStream() {
        this(new LinkedBlockingQueue());
    }

    public QueueOutputStream(BlockingQueue<Integer> blockingQueue) {
        this.blockingQueue = (BlockingQueue) Objects.requireNonNull(blockingQueue, "blockingQueue");
    }

    public QueueInputStream newQueueInputStream() {
        return QueueInputStream.builder().setBlockingQueue(this.blockingQueue).get();
    }

    @Override // java.io.OutputStream
    public void write(int b) throws InterruptedException, InterruptedIOException {
        try {
            this.blockingQueue.put(Integer.valueOf(255 & b));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            InterruptedIOException interruptedIoException = new InterruptedIOException();
            interruptedIoException.initCause(e);
            throw interruptedIoException;
        }
    }
}
