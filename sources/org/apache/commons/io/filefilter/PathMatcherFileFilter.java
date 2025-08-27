package org.apache.commons.io.filefilter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Objects;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/filefilter/PathMatcherFileFilter.class */
public class PathMatcherFileFilter extends AbstractFileFilter {
    private final PathMatcher pathMatcher;

    public PathMatcherFileFilter(PathMatcher pathMatcher) {
        this.pathMatcher = (PathMatcher) Objects.requireNonNull(pathMatcher, "pathMatcher");
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return file != null && matches(file.toPath());
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, java.nio.file.PathMatcher
    public boolean matches(Path path) {
        return this.pathMatcher.matches(path);
    }
}
