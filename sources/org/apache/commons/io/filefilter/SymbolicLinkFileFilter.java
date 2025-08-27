package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/filefilter/SymbolicLinkFileFilter.class */
public class SymbolicLinkFileFilter extends AbstractFileFilter implements Serializable {
    public static final SymbolicLinkFileFilter INSTANCE = new SymbolicLinkFileFilter();
    private static final long serialVersionUID = 1;

    protected SymbolicLinkFileFilter() {
    }

    public SymbolicLinkFileFilter(FileVisitResult onAccept, FileVisitResult onReject) {
        super(onAccept, onReject);
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return isSymbolicLink(file.toPath());
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, org.apache.commons.io.file.PathFilter
    public FileVisitResult accept(Path path, BasicFileAttributes attributes) {
        return toFileVisitResult(isSymbolicLink(path));
    }

    boolean isSymbolicLink(Path filePath) {
        return Files.isSymbolicLink(filePath);
    }
}
