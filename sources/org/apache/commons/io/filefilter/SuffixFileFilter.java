package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.file.PathUtils;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/filefilter/SuffixFileFilter.class */
public class SuffixFileFilter extends AbstractFileFilter implements Serializable {
    private static final long serialVersionUID = -3389157631240246157L;
    private final String[] suffixes;
    private final IOCase ioCase;

    public SuffixFileFilter(List<String> suffixes) {
        this(suffixes, IOCase.SENSITIVE);
    }

    public SuffixFileFilter(List<String> suffixes, IOCase ioCase) {
        Objects.requireNonNull(suffixes, "suffixes");
        this.suffixes = (String[]) suffixes.toArray(EMPTY_STRING_ARRAY);
        this.ioCase = IOCase.value(ioCase, IOCase.SENSITIVE);
    }

    public SuffixFileFilter(String suffix) {
        this(suffix, IOCase.SENSITIVE);
    }

    public SuffixFileFilter(String... suffixes) {
        this(suffixes, IOCase.SENSITIVE);
    }

    public SuffixFileFilter(String suffix, IOCase ioCase) {
        Objects.requireNonNull(suffix, "suffix");
        this.suffixes = new String[]{suffix};
        this.ioCase = IOCase.value(ioCase, IOCase.SENSITIVE);
    }

    public SuffixFileFilter(String[] suffixes, IOCase ioCase) {
        Objects.requireNonNull(suffixes, "suffixes");
        this.suffixes = (String[]) suffixes.clone();
        this.ioCase = IOCase.value(ioCase, IOCase.SENSITIVE);
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return accept(file.getName());
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FilenameFilter
    public boolean accept(File file, String name) {
        return accept(name);
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, org.apache.commons.io.file.PathFilter
    public FileVisitResult accept(Path path, BasicFileAttributes attributes) {
        return toFileVisitResult(accept(PathUtils.getFileNameString(path)));
    }

    private boolean accept(String name) {
        return Stream.of((Object[]) this.suffixes).anyMatch(suffix -> {
            return this.ioCase.checkEndsWith(name, suffix);
        });
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("(");
        append(this.suffixes, buffer);
        buffer.append(")");
        return buffer.toString();
    }
}
