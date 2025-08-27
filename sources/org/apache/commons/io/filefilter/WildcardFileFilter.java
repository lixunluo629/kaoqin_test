package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.build.AbstractSupplier;
import org.apache.commons.io.file.PathUtils;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/filefilter/WildcardFileFilter.class */
public class WildcardFileFilter extends AbstractFileFilter implements Serializable {
    private static final long serialVersionUID = -7426486598995782105L;
    private final String[] wildcards;
    private final IOCase ioCase;

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/filefilter/WildcardFileFilter$Builder.class */
    public static class Builder extends AbstractSupplier<WildcardFileFilter, Builder> {
        private String[] wildcards;
        private IOCase ioCase = IOCase.SENSITIVE;

        @Override // org.apache.commons.io.function.IOSupplier
        public WildcardFileFilter get() {
            return new WildcardFileFilter(this.ioCase, this.wildcards);
        }

        public Builder setIoCase(IOCase ioCase) {
            this.ioCase = IOCase.value(ioCase, IOCase.SENSITIVE);
            return this;
        }

        public Builder setWildcards(List<String> wildcards) {
            setWildcards((String[]) ((List) WildcardFileFilter.requireWildcards(wildcards)).toArray(IOFileFilter.EMPTY_STRING_ARRAY));
            return this;
        }

        public Builder setWildcards(String... wildcards) {
            this.wildcards = (String[]) WildcardFileFilter.requireWildcards(wildcards);
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> T requireWildcards(T t) {
        return (T) Objects.requireNonNull(t, "wildcards");
    }

    private WildcardFileFilter(IOCase ioCase, String... wildcards) {
        this.wildcards = (String[]) ((String[]) requireWildcards(wildcards)).clone();
        this.ioCase = IOCase.value(ioCase, IOCase.SENSITIVE);
    }

    @Deprecated
    public WildcardFileFilter(List<String> wildcards) {
        this(wildcards, IOCase.SENSITIVE);
    }

    @Deprecated
    public WildcardFileFilter(List<String> wildcards, IOCase ioCase) {
        this(ioCase, (String[]) ((List) requireWildcards(wildcards)).toArray(EMPTY_STRING_ARRAY));
    }

    @Deprecated
    public WildcardFileFilter(String wildcard) {
        this(IOCase.SENSITIVE, (String) requireWildcards(wildcard));
    }

    @Deprecated
    public WildcardFileFilter(String... wildcards) {
        this(IOCase.SENSITIVE, wildcards);
    }

    @Deprecated
    public WildcardFileFilter(String wildcard, IOCase ioCase) {
        this(ioCase, wildcard);
    }

    @Deprecated
    public WildcardFileFilter(String[] wildcards, IOCase ioCase) {
        this(ioCase, wildcards);
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return accept(file.getName());
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FilenameFilter
    public boolean accept(File dir, String name) {
        return accept(name);
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, org.apache.commons.io.file.PathFilter
    public FileVisitResult accept(Path path, BasicFileAttributes attributes) {
        return toFileVisitResult(accept(PathUtils.getFileNameString(path)));
    }

    private boolean accept(String name) {
        return Stream.of((Object[]) this.wildcards).anyMatch(wildcard -> {
            return FilenameUtils.wildcardMatch(name, wildcard, this.ioCase);
        });
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("(");
        append(this.wildcards, buffer);
        buffer.append(")");
        return buffer.toString();
    }
}
