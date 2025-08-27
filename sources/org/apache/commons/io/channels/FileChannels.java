package org.apache.commons.io.channels;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/channels/FileChannels.class */
public final class FileChannels {
    public static boolean contentEquals(FileChannel channel1, FileChannel channel2, int byteBufferSize) throws IOException {
        if (Objects.equals(channel1, channel2)) {
            return true;
        }
        long size1 = size(channel1);
        long size2 = size(channel2);
        if (size1 != size2) {
            return false;
        }
        if (size1 == 0 && size2 == 0) {
            return true;
        }
        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(byteBufferSize);
        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(byteBufferSize);
        do {
            int read1 = channel1.read(byteBuffer1);
            int read2 = channel2.read(byteBuffer2);
            byteBuffer1.clear();
            byteBuffer2.clear();
            if (read1 == -1 && read2 == -1) {
                return byteBuffer1.equals(byteBuffer2);
            }
            if (read1 != read2) {
                return false;
            }
        } while (byteBuffer1.equals(byteBuffer2));
        return false;
    }

    private static long size(FileChannel channel) throws IOException {
        if (channel != null) {
            return channel.size();
        }
        return 0L;
    }

    private FileChannels() {
    }
}
