package org.apache.commons.compress.utils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/utils/MultiReadOnlySeekableByteChannel.class */
public class MultiReadOnlySeekableByteChannel implements SeekableByteChannel {
    private final List<SeekableByteChannel> channels;
    private long globalPosition;
    private int currentChannelIdx;

    public MultiReadOnlySeekableByteChannel(List<SeekableByteChannel> channels) {
        this.channels = Collections.unmodifiableList(new ArrayList((Collection) Objects.requireNonNull(channels, "channels must not be null")));
    }

    @Override // java.nio.channels.SeekableByteChannel, java.nio.channels.ReadableByteChannel
    public synchronized int read(ByteBuffer dst) throws IOException {
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
        if (!dst.hasRemaining()) {
            return 0;
        }
        int totalBytesRead = 0;
        while (dst.hasRemaining() && this.currentChannelIdx < this.channels.size()) {
            SeekableByteChannel currentChannel = this.channels.get(this.currentChannelIdx);
            int newBytesRead = currentChannel.read(dst);
            if (newBytesRead == -1) {
                this.currentChannelIdx++;
            } else {
                if (currentChannel.position() >= currentChannel.size()) {
                    this.currentChannelIdx++;
                }
                totalBytesRead += newBytesRead;
            }
        }
        if (totalBytesRead > 0) {
            this.globalPosition += totalBytesRead;
            return totalBytesRead;
        }
        return -1;
    }

    @Override // java.nio.channels.Channel, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        IOException first = null;
        for (SeekableByteChannel ch2 : this.channels) {
            try {
                ch2.close();
            } catch (IOException ex) {
                if (first == null) {
                    first = ex;
                }
            }
        }
        if (first != null) {
            throw new IOException("failed to close wrapped channel", first);
        }
    }

    @Override // java.nio.channels.Channel
    public boolean isOpen() {
        for (SeekableByteChannel ch2 : this.channels) {
            if (!ch2.isOpen()) {
                return false;
            }
        }
        return true;
    }

    @Override // java.nio.channels.SeekableByteChannel
    public long position() {
        return this.globalPosition;
    }

    @Override // java.nio.channels.SeekableByteChannel
    public long size() throws IOException {
        long acc = 0;
        for (SeekableByteChannel ch2 : this.channels) {
            acc += ch2.size();
        }
        return acc;
    }

    @Override // java.nio.channels.SeekableByteChannel
    public SeekableByteChannel truncate(long size) {
        throw new NonWritableChannelException();
    }

    @Override // java.nio.channels.SeekableByteChannel, java.nio.channels.WritableByteChannel
    public int write(ByteBuffer src) {
        throw new NonWritableChannelException();
    }

    @Override // java.nio.channels.SeekableByteChannel
    public synchronized SeekableByteChannel position(long newPosition) throws IOException {
        long j;
        if (newPosition < 0) {
            throw new IllegalArgumentException("Negative position: " + newPosition);
        }
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
        this.globalPosition = newPosition;
        long pos = newPosition;
        for (int i = 0; i < this.channels.size(); i++) {
            SeekableByteChannel currentChannel = this.channels.get(i);
            long size = currentChannel.size();
            if (pos == -1) {
                j = 0;
            } else if (pos <= size) {
                this.currentChannelIdx = i;
                long tmp = pos;
                pos = -1;
                j = tmp;
            } else {
                pos -= size;
                j = size;
            }
            long newChannelPos = j;
            currentChannel.position(newChannelPos);
        }
        return this;
    }

    public static SeekableByteChannel forSeekableByteChannels(SeekableByteChannel... channels) {
        if (((SeekableByteChannel[]) Objects.requireNonNull(channels, "channels must not be null")).length == 1) {
            return channels[0];
        }
        return new MultiReadOnlySeekableByteChannel(Arrays.asList(channels));
    }

    public static SeekableByteChannel forFiles(File... files) throws IOException {
        List<SeekableByteChannel> channels = new ArrayList<>();
        for (File f : (File[]) Objects.requireNonNull(files, "files must not be null")) {
            channels.add(Files.newByteChannel(f.toPath(), StandardOpenOption.READ));
        }
        if (channels.size() == 1) {
            return channels.get(0);
        }
        return new MultiReadOnlySeekableByteChannel(channels);
    }
}
