package org.apache.commons.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Deprecated
/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/FileSystemUtils.class */
public class FileSystemUtils {
    @Deprecated
    public static long freeSpace(String path) throws IOException {
        return getFreeSpace(path);
    }

    @Deprecated
    public static long freeSpaceKb() throws IOException {
        return freeSpaceKb(-1L);
    }

    @Deprecated
    public static long freeSpaceKb(long timeout) throws IOException {
        return freeSpaceKb(FileUtils.current().getAbsolutePath(), timeout);
    }

    @Deprecated
    public static long freeSpaceKb(String path) throws IOException {
        return freeSpaceKb(path, -1L);
    }

    @Deprecated
    public static long freeSpaceKb(String path, long timeout) throws IOException {
        return getFreeSpace(path) / 1024;
    }

    static long getFreeSpace(String pathStr) throws IOException {
        Path path = Paths.get((String) Objects.requireNonNull(pathStr, "pathStr"), new String[0]);
        if (Files.exists(path, new LinkOption[0])) {
            return Files.getFileStore(path.toAbsolutePath()).getUsableSpace();
        }
        throw new IllegalArgumentException(path.toString());
    }

    @Deprecated
    public FileSystemUtils() {
    }
}
