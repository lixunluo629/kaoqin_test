package org.springframework.util;

import java.io.File;
import java.io.IOException;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/FileSystemUtils.class */
public abstract class FileSystemUtils {
    public static boolean deleteRecursively(File root) {
        File[] children;
        if (root != null && root.exists()) {
            if (root.isDirectory() && (children = root.listFiles()) != null) {
                for (File child : children) {
                    deleteRecursively(child);
                }
            }
            return root.delete();
        }
        return false;
    }

    public static void copyRecursively(File src, File dest) throws IOException {
        Assert.isTrue(src != null && (src.isDirectory() || src.isFile()), "Source File must denote a directory or file");
        Assert.notNull(dest, "Destination File must not be null");
        doCopyRecursively(src, dest);
    }

    private static void doCopyRecursively(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            dest.mkdir();
            File[] entries = src.listFiles();
            if (entries == null) {
                throw new IOException("Could not list files in directory: " + src);
            }
            for (File entry : entries) {
                doCopyRecursively(entry, new File(dest, entry.getName()));
            }
            return;
        }
        if (src.isFile()) {
            try {
                dest.createNewFile();
                FileCopyUtils.copy(src, dest);
            } catch (IOException ex) {
                throw new IOException("Failed to create file: " + dest, ex);
            }
        }
    }
}
