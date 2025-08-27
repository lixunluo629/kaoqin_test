package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.file.PathUtils;

@Deprecated
/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/filefilter/WildcardFilter.class */
public class WildcardFilter extends AbstractFileFilter implements Serializable {
    private static final long serialVersionUID = -5037645902506953517L;
    private final String[] wildcards;

    public WildcardFilter(List<String> wildcards) {
        Objects.requireNonNull(wildcards, "wildcards");
        this.wildcards = (String[]) wildcards.toArray(EMPTY_STRING_ARRAY);
    }

    public WildcardFilter(String wildcard) {
        Objects.requireNonNull(wildcard, "wildcard");
        this.wildcards = new String[]{wildcard};
    }

    public WildcardFilter(String... wildcards) {
        Objects.requireNonNull(wildcards, "wildcards");
        this.wildcards = (String[]) wildcards.clone();
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return false;
        }
        return Stream.of((Object[]) this.wildcards).anyMatch(wildcard -> {
            return FilenameUtils.wildcardMatch(file.getName(), wildcard);
        });
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FilenameFilter
    public boolean accept(File dir, String name) {
        if (dir != null && new File(dir, name).isDirectory()) {
            return false;
        }
        return Stream.of((Object[]) this.wildcards).anyMatch(wildcard -> {
            return FilenameUtils.wildcardMatch(name, wildcard);
        });
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, org.apache.commons.io.file.PathFilter
    public FileVisitResult accept(Path path, BasicFileAttributes attributes) {
        if (Files.isDirectory(path, new LinkOption[0])) {
            return FileVisitResult.TERMINATE;
        }
        return toDefaultFileVisitResult(Stream.of((Object[]) this.wildcards).anyMatch(wildcard -> {
            return FilenameUtils.wildcardMatch(PathUtils.getFileNameString(path), wildcard);
        }));
    }
}
