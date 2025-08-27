package com.itextpdf.io.source;

import com.itextpdf.io.LogMessageConstant;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/source/PagedChannelRandomAccessSource.class */
class PagedChannelRandomAccessSource extends GroupedRandomAccessSource implements IRandomAccessSource {
    public static final int DEFAULT_TOTAL_BUFSIZE = 67108864;
    public static final int DEFAULT_MAX_OPEN_BUFFERS = 16;
    private static final long serialVersionUID = 4297575388315637274L;
    private final int bufferSize;
    private final FileChannel channel;
    private final MRU<IRandomAccessSource> mru;

    public PagedChannelRandomAccessSource(FileChannel channel) throws IOException {
        this(channel, 67108864, 16);
    }

    public PagedChannelRandomAccessSource(FileChannel channel, int totalBufferSize, int maxOpenBuffers) throws IOException {
        super(buildSources(channel, totalBufferSize / maxOpenBuffers));
        this.channel = channel;
        this.bufferSize = totalBufferSize / maxOpenBuffers;
        this.mru = new MRU<>(maxOpenBuffers);
    }

    private static IRandomAccessSource[] buildSources(FileChannel channel, int bufferSize) throws IOException {
        long size = channel.size();
        if (size <= 0) {
            throw new IOException("File size must be greater than zero");
        }
        int bufferCount = ((int) (size / bufferSize)) + (size % ((long) bufferSize) == 0 ? 0 : 1);
        MappedChannelRandomAccessSource[] sources = new MappedChannelRandomAccessSource[bufferCount];
        for (int i = 0; i < bufferCount; i++) {
            long pageOffset = i * bufferSize;
            long pageLength = Math.min(size - pageOffset, bufferSize);
            sources[i] = new MappedChannelRandomAccessSource(channel, pageOffset, pageLength);
        }
        return sources;
    }

    @Override // com.itextpdf.io.source.GroupedRandomAccessSource
    protected int getStartingSourceIndex(long offset) {
        return (int) (offset / this.bufferSize);
    }

    @Override // com.itextpdf.io.source.GroupedRandomAccessSource
    protected void sourceReleased(IRandomAccessSource source) throws IOException {
        IRandomAccessSource old = this.mru.enqueue(source);
        if (old != null) {
            old.close();
        }
    }

    @Override // com.itextpdf.io.source.GroupedRandomAccessSource
    protected void sourceInUse(IRandomAccessSource source) throws IOException {
        ((MappedChannelRandomAccessSource) source).open();
    }

    @Override // com.itextpdf.io.source.GroupedRandomAccessSource, com.itextpdf.io.source.IRandomAccessSource
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            try {
                this.channel.close();
            } catch (Exception ex) {
                Logger logger = LoggerFactory.getLogger((Class<?>) PagedChannelRandomAccessSource.class);
                logger.error(LogMessageConstant.FILE_CHANNEL_CLOSING_FAILED, (Throwable) ex);
            }
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        throw new NotSerializableException(getClass().toString());
    }

    private void readObject(ObjectInputStream in) throws IOException {
        throw new NotSerializableException(getClass().toString());
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/source/PagedChannelRandomAccessSource$MRU.class */
    private static class MRU<E> {
        private final int limit;
        private LinkedList<E> queue = new LinkedList<>();

        public MRU(int limit) {
            this.limit = limit;
        }

        public E enqueue(E newElement) {
            if (this.queue.size() > 0 && this.queue.getFirst() == newElement) {
                return null;
            }
            Iterator<E> it = this.queue.iterator();
            while (it.hasNext()) {
                E element = it.next();
                if (newElement == element) {
                    it.remove();
                    this.queue.addFirst(newElement);
                    return null;
                }
            }
            this.queue.addFirst(newElement);
            if (this.queue.size() > this.limit) {
                return this.queue.removeLast();
            }
            return null;
        }
    }
}
