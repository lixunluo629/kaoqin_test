package org.apache.commons.io;

import com.itextpdf.io.font.constants.FontStretches;
import java.io.File;
import java.io.IOException;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/FileDeleteStrategy.class */
public class FileDeleteStrategy {
    public static final FileDeleteStrategy NORMAL = new FileDeleteStrategy(FontStretches.NORMAL);
    public static final FileDeleteStrategy FORCE = new ForceFileDeleteStrategy();
    private final String name;

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/FileDeleteStrategy$ForceFileDeleteStrategy.class */
    static class ForceFileDeleteStrategy extends FileDeleteStrategy {
        ForceFileDeleteStrategy() {
            super("Force");
        }

        @Override // org.apache.commons.io.FileDeleteStrategy
        protected boolean doDelete(File fileToDelete) throws IOException {
            FileUtils.forceDelete(fileToDelete);
            return true;
        }
    }

    protected FileDeleteStrategy(String name) {
        this.name = name;
    }

    public void delete(File fileToDelete) throws IOException {
        if (fileToDelete.exists() && !doDelete(fileToDelete)) {
            throw new IOException("Deletion failed: " + fileToDelete);
        }
    }

    public boolean deleteQuietly(File fileToDelete) {
        if (fileToDelete == null || !fileToDelete.exists()) {
            return true;
        }
        try {
            return doDelete(fileToDelete);
        } catch (IOException e) {
            return false;
        }
    }

    protected boolean doDelete(File file) throws IOException {
        FileUtils.delete(file);
        return true;
    }

    public String toString() {
        return "FileDeleteStrategy[" + this.name + "]";
    }
}
