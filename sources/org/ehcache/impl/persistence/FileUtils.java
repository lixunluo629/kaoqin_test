package org.ehcache.impl.persistence;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.ehcache.CachePersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/persistence/FileUtils.class */
final class FileUtils {
    private static final int DEL = 127;
    private static final char ESCAPE = '%';
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) FileUtils.class);
    private static final Charset UTF8 = Charset.forName("UTF8");
    private static final Set<Character> ILLEGALS = new HashSet();

    FileUtils() {
    }

    static {
        ILLEGALS.add('/');
        ILLEGALS.add('\\');
        ILLEGALS.add('<');
        ILLEGALS.add('>');
        ILLEGALS.add(':');
        ILLEGALS.add('\"');
        ILLEGALS.add('|');
        ILLEGALS.add('?');
        ILLEGALS.add('*');
        ILLEGALS.add('.');
    }

    static void createLocationIfRequiredAndVerify(File rootDirectory) {
        if (!rootDirectory.exists()) {
            if (!rootDirectory.mkdirs()) {
                throw new IllegalArgumentException("Directory couldn't be created: " + rootDirectory.getAbsolutePath());
            }
        } else if (!rootDirectory.isDirectory()) {
            throw new IllegalArgumentException("Location is not a directory: " + rootDirectory.getAbsolutePath());
        }
        if (!rootDirectory.canWrite()) {
            throw new IllegalArgumentException("Location isn't writable: " + rootDirectory.getAbsolutePath());
        }
    }

    static File createSubDirectory(File mainDirectory, String name) throws CachePersistenceException {
        validateName(name);
        File subDirectory = new File(mainDirectory, name);
        create(subDirectory);
        return subDirectory;
    }

    static void validateName(String name) {
        if (!name.matches("[a-zA-Z0-9\\-_]+")) {
            throw new IllegalArgumentException("Name is invalid for persistence context: " + name);
        }
    }

    static void create(File directory) throws CachePersistenceException {
        if (directory.isDirectory()) {
            LOGGER.debug("Reusing {}", directory.getAbsolutePath());
        } else if (directory.mkdir()) {
            LOGGER.debug("Created {}", directory.getAbsolutePath());
        } else {
            if (directory.isDirectory()) {
                LOGGER.debug("Reusing {}", directory.getAbsolutePath());
                return;
            }
            throw new CachePersistenceException("Unable to create or reuse directory: " + directory.getAbsolutePath());
        }
    }

    static boolean recursiveDeleteDirectoryContent(File file) {
        File[] contents = file.listFiles();
        if (contents == null) {
            throw new IllegalArgumentException("File " + file.getAbsolutePath() + " is not a directory");
        }
        boolean deleteSuccessful = true;
        for (File f : contents) {
            deleteSuccessful &= tryRecursiveDelete(f);
        }
        return deleteSuccessful;
    }

    private static boolean recursiveDelete(File file) {
        Deque<File> toDelete = new ArrayDeque<>();
        toDelete.push(file);
        while (!toDelete.isEmpty()) {
            File target = toDelete.pop();
            File[] contents = target.listFiles();
            if (contents == null || contents.length == 0) {
                if (target.exists() && !target.delete()) {
                    return false;
                }
            } else {
                toDelete.push(target);
                for (File f : contents) {
                    toDelete.push(f);
                }
            }
        }
        return true;
    }

    @SuppressFBWarnings({"DM_GC"})
    static boolean tryRecursiveDelete(File file) {
        boolean interrupted = false;
        for (int i = 0; i < 5; i++) {
            try {
                if (!recursiveDelete(file) && isWindows()) {
                    System.gc();
                    System.runFinalization();
                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException e) {
                        interrupted = true;
                    }
                } else {
                    return true;
                }
            } finally {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
            return false;
        }
        return false;
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase(Locale.ENGLISH).contains("windows");
    }

    static String safeIdentifier(String name) {
        return safeIdentifier(name, true);
    }

    static String safeIdentifier(String name, boolean withSha1) {
        int len = name.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = name.charAt(i);
            if (c <= ' ' || c >= 127 || ILLEGALS.contains(Character.valueOf(c)) || c == '%') {
                sb.append('%');
                sb.append(String.format("%04x", Integer.valueOf(c)));
            } else {
                sb.append(c);
            }
        }
        if (withSha1) {
            sb.append("_").append(sha1(name));
        }
        return sb.toString();
    }

    private static String sha1(String input) {
        StringBuilder sb = new StringBuilder();
        byte[] arr$ = getSha1Digest().digest(input.getBytes(UTF8));
        for (byte b : arr$) {
            sb.append(Integer.toHexString((b & 240) >>> 4));
            sb.append(Integer.toHexString(b & 15));
        }
        return sb.toString();
    }

    private static MessageDigest getSha1Digest() {
        try {
            return MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("All JDKs must have SHA-1");
        }
    }
}
