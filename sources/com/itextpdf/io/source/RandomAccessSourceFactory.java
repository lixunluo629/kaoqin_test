package com.itextpdf.io.source;

import com.alibaba.excel.constant.ExcelXmlConstants;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.io.util.ResourceUtil;
import com.itextpdf.io.util.StreamUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.net.URL;
import java.nio.channels.FileChannel;
import org.springframework.util.ResourceUtils;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/source/RandomAccessSourceFactory.class */
public final class RandomAccessSourceFactory implements Serializable {
    private static final long serialVersionUID = -8958482579413233761L;
    private boolean forceRead = false;
    private boolean usePlainRandomAccess = false;
    private boolean exclusivelyLockFile = false;

    public RandomAccessSourceFactory setForceRead(boolean forceRead) {
        this.forceRead = forceRead;
        return this;
    }

    public RandomAccessSourceFactory setUsePlainRandomAccess(boolean usePlainRandomAccess) {
        this.usePlainRandomAccess = usePlainRandomAccess;
        return this;
    }

    public RandomAccessSourceFactory setExclusivelyLockFile(boolean exclusivelyLockFile) {
        this.exclusivelyLockFile = exclusivelyLockFile;
        return this;
    }

    public IRandomAccessSource createSource(byte[] data) {
        return new ArrayRandomAccessSource(data);
    }

    public IRandomAccessSource createSource(RandomAccessFile raf) throws IOException {
        return new RAFRandomAccessSource(raf);
    }

    public IRandomAccessSource createSource(URL url) throws IOException {
        InputStream stream = url.openStream();
        try {
            return createSource(stream);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
            }
        }
    }

    public IRandomAccessSource createSource(InputStream inputStream) throws IOException {
        return createSource(StreamUtil.inputStreamToArray(inputStream));
    }

    public IRandomAccessSource createBestSource(String filename) throws Exception {
        File file = new File(filename);
        if (!file.canRead()) {
            if (filename.startsWith("file:/") || filename.startsWith("http://") || filename.startsWith("https://") || filename.startsWith(ResourceUtils.JAR_URL_PREFIX) || filename.startsWith("wsjar:") || filename.startsWith("wsjar:") || filename.startsWith("vfszip:")) {
                return createSource(new URL(filename));
            }
            return createByReadingToMemory(filename);
        }
        if (this.forceRead) {
            return createByReadingToMemory(new FileInputStream(filename));
        }
        String openMode = this.exclusivelyLockFile ? "rw" : ExcelXmlConstants.POSITION;
        RandomAccessFile raf = new RandomAccessFile(file, openMode);
        if (this.exclusivelyLockFile) {
            raf.getChannel().lock();
        }
        if (this.usePlainRandomAccess) {
            return new RAFRandomAccessSource(raf);
        }
        try {
            if (raf.length() <= 0) {
                return new RAFRandomAccessSource(raf);
            }
            try {
                return createBestSource(raf.getChannel());
            } catch (IOException e) {
                if (exceptionIsMapFailureException(e)) {
                    return new RAFRandomAccessSource(raf);
                }
                throw e;
            }
        } catch (Exception e2) {
            try {
                raf.close();
            } catch (IOException e3) {
            }
            throw e2;
        }
    }

    public IRandomAccessSource createBestSource(FileChannel channel) throws IOException {
        if (channel.size() <= 67108864) {
            return new GetBufferedRandomAccessSource(new FileChannelRandomAccessSource(channel));
        }
        return new GetBufferedRandomAccessSource(new PagedChannelRandomAccessSource(channel));
    }

    public IRandomAccessSource createRanged(IRandomAccessSource source, long[] ranges) throws IOException {
        IRandomAccessSource[] sources = new IRandomAccessSource[ranges.length / 2];
        for (int i = 0; i < ranges.length; i += 2) {
            sources[i / 2] = new WindowRandomAccessSource(source, ranges[i], ranges[i + 1]);
        }
        return new GroupedRandomAccessSource(sources);
    }

    private IRandomAccessSource createByReadingToMemory(String filename) throws IOException {
        InputStream stream = ResourceUtil.getResourceStream(filename);
        if (stream == null) {
            throw new IOException(MessageFormatUtil.format(com.itextpdf.io.IOException._1NotFoundAsFileOrResource, filename));
        }
        return createByReadingToMemory(stream);
    }

    private IRandomAccessSource createByReadingToMemory(InputStream stream) throws IOException {
        try {
            return new ArrayRandomAccessSource(StreamUtil.inputStreamToArray(stream));
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
            }
        }
    }

    private static boolean exceptionIsMapFailureException(IOException e) {
        if (e.getMessage() != null && e.getMessage().contains("Map failed")) {
            return true;
        }
        return false;
    }
}
