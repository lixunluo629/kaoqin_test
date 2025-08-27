package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/filefilter/EmptyFileFilter.class */
public class EmptyFileFilter extends AbstractFileFilter implements Serializable {
    public static final IOFileFilter EMPTY = new EmptyFileFilter();
    public static final IOFileFilter NOT_EMPTY = EMPTY.negate();
    private static final long serialVersionUID = 3631422087512832211L;

    protected EmptyFileFilter() {
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        if (file == null) {
            return true;
        }
        if (!file.isDirectory()) {
            return file.length() == 0;
        }
        File[] files = file.listFiles();
        return IOUtils.length(files) == 0;
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, org.apache.commons.io.file.PathFilter
    public FileVisitResult accept(Path file, BasicFileAttributes attributes) {
        if (file == null) {
            return toFileVisitResult(true);
        }
        return get(() -> {
            if (Files.isDirectory(file, new LinkOption[0])) {
                Stream<Path> stream = Files.list(file);
                try {
                    FileVisitResult fileVisitResult = toFileVisitResult(!stream.findFirst().isPresent());
                    if (stream != null) {
                        stream.close();
                    }
                    return fileVisitResult;
                } catch (Throwable th) {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            }
            return toFileVisitResult(Files.size(file) == 0);
        });
    }
}
