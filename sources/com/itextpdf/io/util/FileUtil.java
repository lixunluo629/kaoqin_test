package com.itextpdf.io.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.slf4j.LoggerFactory;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/util/FileUtil.class */
public final class FileUtil {
    private FileUtil() {
    }

    public static String getFontsDir() {
        try {
            String winDir = System.getenv("windir");
            String fileSeparator = System.getProperty("file.separator");
            return winDir + fileSeparator + "fonts";
        } catch (SecurityException e) {
            LoggerFactory.getLogger((Class<?>) FileUtil.class).warn("Can't access System.getenv(\"windir\") to load fonts. Please, add RuntimePermission for getenv.windir.");
            return null;
        }
    }

    public static boolean fileExists(String path) {
        if (path != null) {
            File f = new File(path);
            return f.exists() && f.isFile();
        }
        return false;
    }

    public static boolean directoryExists(String path) {
        if (path != null) {
            File f = new File(path);
            return f.exists() && f.isDirectory();
        }
        return false;
    }

    public static String[] listFilesInDirectory(String path, boolean recursive) {
        File[] files;
        if (path != null) {
            File root = new File(path);
            if (root.exists() && root.isDirectory() && (files = root.listFiles()) != null) {
                Arrays.sort(files, new CaseSensitiveFileComparator());
                List<String> list = new ArrayList<>();
                for (File file : files) {
                    if (file.isDirectory() && recursive) {
                        listAllFiles(file.getAbsolutePath(), list);
                    } else {
                        list.add(file.getAbsolutePath());
                    }
                }
                return (String[]) list.toArray(new String[list.size()]);
            }
            return null;
        }
        return null;
    }

    public static File[] listFilesInDirectoryByFilter(String outPath, FileFilter fileFilter) {
        File[] result = null;
        if (outPath != null && !outPath.isEmpty()) {
            result = new File(outPath).listFiles(fileFilter);
        }
        if (result != null) {
            Arrays.sort(result, new CaseSensitiveFileComparator());
        }
        return result;
    }

    private static void listAllFiles(String dir, List<String> list) {
        File[] files = new File(dir).listFiles();
        if (files != null) {
            Arrays.sort(files, new CaseSensitiveFileComparator());
            for (File file : files) {
                if (file.isDirectory()) {
                    listAllFiles(file.getAbsolutePath(), list);
                } else {
                    list.add(file.getAbsolutePath());
                }
            }
        }
    }

    public static PrintWriter createPrintWriter(OutputStream output, String encoding) throws UnsupportedEncodingException {
        return new PrintWriter(new OutputStreamWriter(output, encoding));
    }

    public static OutputStream getBufferedOutputStream(String filename) throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(filename));
    }

    public static OutputStream wrapWithBufferedOutputStream(OutputStream outputStream) {
        if ((outputStream instanceof ByteArrayOutputStream) || (outputStream instanceof BufferedOutputStream)) {
            return outputStream;
        }
        return new BufferedOutputStream(outputStream);
    }

    public static File createTempFile(String path) throws IOException {
        File tempFile = new File(path);
        if (tempFile.isDirectory()) {
            tempFile = File.createTempFile("pdf", null, tempFile);
        }
        return tempFile;
    }

    public static FileOutputStream getFileOutputStream(File tempFile) throws FileNotFoundException {
        return new FileOutputStream(tempFile);
    }

    public static RandomAccessFile getRandomAccessFile(File tempFile) throws FileNotFoundException {
        return new RandomAccessFile(tempFile, "rw");
    }

    public static void createDirectories(String outPath) {
        new File(outPath).mkdirs();
    }

    @Deprecated
    public static String getParentDirectory(String file) {
        return new File(file).getParent();
    }

    public static String getParentDirectory(File file) throws MalformedURLException {
        return file != null ? Paths.get(file.getParent(), new String[0]).toUri().toURL().toExternalForm() : "";
    }

    public static boolean deleteFile(File file) {
        return file.delete();
    }

    public static String parentDirectory(URL url) throws URISyntaxException {
        return url.toURI().resolve(".").toString();
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/util/FileUtil$CaseSensitiveFileComparator.class */
    private static class CaseSensitiveFileComparator implements Comparator<File> {
        private CaseSensitiveFileComparator() {
        }

        @Override // java.util.Comparator
        public int compare(File f1, File f2) {
            return f1.getPath().compareTo(f2.getPath());
        }
    }
}
