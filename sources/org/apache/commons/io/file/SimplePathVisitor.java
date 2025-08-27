package org.apache.commons.io.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.Objects;
import org.apache.commons.io.function.IOBiFunction;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/file/SimplePathVisitor.class */
public abstract class SimplePathVisitor extends SimpleFileVisitor<Path> implements PathVisitor {
    private final IOBiFunction<Path, IOException, FileVisitResult> visitFileFailedFunction;

    protected SimplePathVisitor() {
        this.visitFileFailedFunction = (x$0, x$1) -> {
            return super.visitFileFailed((SimplePathVisitor) x$0, x$1);
        };
    }

    protected SimplePathVisitor(IOBiFunction<Path, IOException, FileVisitResult> visitFileFailed) {
        this.visitFileFailedFunction = (IOBiFunction) Objects.requireNonNull(visitFileFailed, "visitFileFailed");
    }

    @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return this.visitFileFailedFunction.apply(file, exc);
    }
}
