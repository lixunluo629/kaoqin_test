package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/filefilter/HiddenFileFilter.class */
public class HiddenFileFilter extends AbstractFileFilter implements Serializable {
    private static final long serialVersionUID = 8930842316112759062L;
    public static final IOFileFilter HIDDEN = new HiddenFileFilter();
    public static final IOFileFilter VISIBLE = HIDDEN.negate();

    protected HiddenFileFilter() {
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return file == null || file.isHidden();
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, org.apache.commons.io.file.PathFilter
    public FileVisitResult accept(Path file, BasicFileAttributes attributes) {
        return get(() -> {
            return toFileVisitResult(file == null || Files.isHidden(file));
        });
    }
}
