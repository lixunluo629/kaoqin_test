package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.file.PathUtils;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/filefilter/RegexFileFilter.class */
public class RegexFileFilter extends AbstractFileFilter implements Serializable {
    private static final long serialVersionUID = 4269646126155225062L;
    private final Pattern pattern;
    private final transient Function<Path, String> pathToString;

    private static /* synthetic */ Object $deserializeLambda$(SerializedLambda lambda) {
        switch (lambda.getImplMethodName()) {
            case "getFileNameString":
                if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("java/util/function/Function") && lambda.getFunctionalInterfaceMethodName().equals("apply") && lambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && lambda.getImplClass().equals("org/apache/commons/io/file/PathUtils") && lambda.getImplMethodSignature().equals("(Ljava/nio/file/Path;)Ljava/lang/String;")) {
                    return PathUtils::getFileNameString;
                }
                break;
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }

    private static Pattern compile(String pattern, int flags) {
        Objects.requireNonNull(pattern, "pattern");
        return Pattern.compile(pattern, flags);
    }

    private static int toFlags(IOCase ioCase) {
        return IOCase.isCaseSensitive(ioCase) ? 0 : 2;
    }

    public RegexFileFilter(Pattern pattern) {
        this(pattern, (Function<Path, String>) ((Serializable) PathUtils::getFileNameString));
    }

    public RegexFileFilter(Pattern pattern, Function<Path, String> pathToString) {
        Objects.requireNonNull(pattern, "pattern");
        this.pattern = pattern;
        this.pathToString = pathToString != null ? pathToString : (v0) -> {
            return Objects.toString(v0);
        };
    }

    public RegexFileFilter(String pattern) {
        this(pattern, 0);
    }

    public RegexFileFilter(String pattern, int flags) {
        this(compile(pattern, flags));
    }

    public RegexFileFilter(String pattern, IOCase ioCase) {
        this(compile(pattern, toFlags(ioCase)));
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FilenameFilter
    public boolean accept(File dir, String name) {
        return this.pattern.matcher(name).matches();
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, org.apache.commons.io.file.PathFilter
    public FileVisitResult accept(Path path, BasicFileAttributes attributes) {
        String result = this.pathToString.apply(path);
        return toFileVisitResult(result != null && this.pattern.matcher(result).matches());
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter
    public String toString() {
        return "RegexFileFilter [pattern=" + this.pattern + "]";
    }
}
