package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/filefilter/OrFileFilter.class */
public class OrFileFilter extends AbstractFileFilter implements ConditionalFileFilter, Serializable {
    private static final long serialVersionUID = 5767770777065432721L;
    private final List<IOFileFilter> fileFilters;

    public OrFileFilter() {
        this(0);
    }

    private OrFileFilter(ArrayList<IOFileFilter> initialList) {
        this.fileFilters = (List) Objects.requireNonNull(initialList, "initialList");
    }

    private OrFileFilter(int initialCapacity) {
        this((ArrayList<IOFileFilter>) new ArrayList(initialCapacity));
    }

    public OrFileFilter(IOFileFilter... fileFilters) {
        this(((IOFileFilter[]) Objects.requireNonNull(fileFilters, "fileFilters")).length);
        addFileFilter(fileFilters);
    }

    public OrFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
        this(2);
        addFileFilter(filter1);
        addFileFilter(filter2);
    }

    public OrFileFilter(List<IOFileFilter> fileFilters) {
        this((ArrayList<IOFileFilter>) new ArrayList((Collection) Objects.requireNonNull(fileFilters, "fileFilters")));
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return this.fileFilters.stream().anyMatch(fileFilter -> {
            return fileFilter.accept(file);
        });
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FilenameFilter
    public boolean accept(File file, String name) {
        return this.fileFilters.stream().anyMatch(fileFilter -> {
            return fileFilter.accept(file, name);
        });
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, org.apache.commons.io.file.PathFilter
    public FileVisitResult accept(Path file, BasicFileAttributes attributes) {
        return toDefaultFileVisitResult(this.fileFilters.stream().anyMatch(fileFilter -> {
            return fileFilter.accept(file, attributes) == FileVisitResult.CONTINUE;
        }));
    }

    @Override // org.apache.commons.io.filefilter.ConditionalFileFilter
    public void addFileFilter(IOFileFilter fileFilter) {
        this.fileFilters.add((IOFileFilter) Objects.requireNonNull(fileFilter, "fileFilter"));
    }

    public void addFileFilter(IOFileFilter... fileFilters) {
        Stream.of((Object[]) Objects.requireNonNull(fileFilters, "fileFilters")).forEach(this::addFileFilter);
    }

    @Override // org.apache.commons.io.filefilter.ConditionalFileFilter
    public List<IOFileFilter> getFileFilters() {
        return Collections.unmodifiableList(this.fileFilters);
    }

    @Override // org.apache.commons.io.filefilter.ConditionalFileFilter
    public boolean removeFileFilter(IOFileFilter fileFilter) {
        return this.fileFilters.remove(fileFilter);
    }

    @Override // org.apache.commons.io.filefilter.ConditionalFileFilter
    public void setFileFilters(List<IOFileFilter> fileFilters) {
        this.fileFilters.clear();
        this.fileFilters.addAll((Collection) Objects.requireNonNull(fileFilters, "fileFilters"));
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("(");
        append(this.fileFilters, buffer);
        buffer.append(")");
        return buffer.toString();
    }
}
