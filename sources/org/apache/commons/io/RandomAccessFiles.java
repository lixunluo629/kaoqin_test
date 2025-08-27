package org.apache.commons.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Objects;
import org.apache.commons.io.channels.FileChannels;
import org.apache.commons.io.function.IOTriFunction;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/RandomAccessFiles.class */
public class RandomAccessFiles {
    public static boolean contentEquals(RandomAccessFile raf1, RandomAccessFile raf2) throws IOException {
        if (Objects.equals(raf1, raf2)) {
            return true;
        }
        long length1 = length(raf1);
        long length2 = length(raf2);
        if (length1 != length2) {
            return false;
        }
        if (length1 == 0 && length2 == 0) {
            return true;
        }
        FileChannel channel1 = raf1.getChannel();
        FileChannel channel2 = raf2.getChannel();
        return FileChannels.contentEquals(channel1, channel2, 8192);
    }

    private static long length(RandomAccessFile raf) throws IOException {
        if (raf != null) {
            return raf.length();
        }
        return 0L;
    }

    public static byte[] read(RandomAccessFile input, long position, int length) throws IOException {
        input.seek(position);
        Objects.requireNonNull(input);
        return IOUtils.toByteArray((IOTriFunction<byte[], Integer, Integer, Integer>) (v1, v2, v3) -> {
            return r0.read(v1, v2, v3);
        }, length);
    }

    public static RandomAccessFile reset(RandomAccessFile raf) throws IOException {
        raf.seek(0L);
        return raf;
    }

    @Deprecated
    public RandomAccessFiles() {
    }
}
