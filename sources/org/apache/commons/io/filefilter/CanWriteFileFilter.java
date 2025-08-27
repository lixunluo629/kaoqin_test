package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/filefilter/CanWriteFileFilter.class */
public class CanWriteFileFilter extends AbstractFileFilter implements Serializable {
    public static final IOFileFilter CAN_WRITE = new CanWriteFileFilter();
    public static final IOFileFilter CANNOT_WRITE = CAN_WRITE.negate();
    private static final long serialVersionUID = 5132005214688990379L;

    protected CanWriteFileFilter() {
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return file != null && file.canWrite();
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, org.apache.commons.io.file.PathFilter
    public FileVisitResult accept(Path file, BasicFileAttributes attributes) {
        return toFileVisitResult(file != null && Files.isWritable(file));
    }
}
