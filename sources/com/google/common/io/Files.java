package com.google.common.io;

import com.alibaba.excel.constant.ExcelXmlConstants;
import com.google.common.annotations.Beta;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.TreeTraverser;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.poi.util.TempFile;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/io/Files.class */
public final class Files {
    private static final int TEMP_DIR_ATTEMPTS = 10000;
    private static final TreeTraverser<File> FILE_TREE_TRAVERSER = new TreeTraverser<File>() { // from class: com.google.common.io.Files.2
        @Override // com.google.common.collect.TreeTraverser
        public Iterable<File> children(File file) {
            File[] files;
            if (file.isDirectory() && (files = file.listFiles()) != null) {
                return Collections.unmodifiableList(Arrays.asList(files));
            }
            return Collections.emptyList();
        }

        public String toString() {
            return "Files.fileTreeTraverser()";
        }
    };

    /* loaded from: guava-18.0.jar:com/google/common/io/Files$FilePredicate.class */
    private enum FilePredicate implements Predicate<File> {
        IS_DIRECTORY { // from class: com.google.common.io.Files.FilePredicate.1
            @Override // com.google.common.base.Predicate
            public boolean apply(File file) {
                return file.isDirectory();
            }

            @Override // java.lang.Enum
            public String toString() {
                return "Files.isDirectory()";
            }
        },
        IS_FILE { // from class: com.google.common.io.Files.FilePredicate.2
            @Override // com.google.common.base.Predicate
            public boolean apply(File file) {
                return file.isFile();
            }

            @Override // java.lang.Enum
            public String toString() {
                return "Files.isFile()";
            }
        }
    }

    private Files() {
    }

    public static BufferedReader newReader(File file, Charset charset) throws FileNotFoundException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(charset);
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
    }

    public static BufferedWriter newWriter(File file, Charset charset) throws FileNotFoundException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(charset);
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
    }

    public static ByteSource asByteSource(File file) {
        return new FileByteSource(file);
    }

    /* loaded from: guava-18.0.jar:com/google/common/io/Files$FileByteSource.class */
    private static final class FileByteSource extends ByteSource {
        private final File file;

        private FileByteSource(File file) {
            this.file = (File) Preconditions.checkNotNull(file);
        }

        @Override // com.google.common.io.ByteSource
        public FileInputStream openStream() throws IOException {
            return new FileInputStream(this.file);
        }

        @Override // com.google.common.io.ByteSource
        public long size() throws IOException {
            if (!this.file.isFile()) {
                throw new FileNotFoundException(this.file.toString());
            }
            return this.file.length();
        }

        @Override // com.google.common.io.ByteSource
        public byte[] read() throws Throwable {
            Closer closer = Closer.create();
            try {
                try {
                    FileInputStream in = (FileInputStream) closer.register(openStream());
                    byte[] file = Files.readFile(in, in.getChannel().size());
                    closer.close();
                    return file;
                } catch (Throwable e) {
                    throw closer.rethrow(e);
                }
            } catch (Throwable th) {
                closer.close();
                throw th;
            }
        }

        public String toString() {
            String strValueOf = String.valueOf(String.valueOf(this.file));
            return new StringBuilder(20 + strValueOf.length()).append("Files.asByteSource(").append(strValueOf).append(")").toString();
        }
    }

    static byte[] readFile(InputStream in, long expectedSize) throws IOException {
        if (expectedSize > 2147483647L) {
            throw new OutOfMemoryError(new StringBuilder(68).append("file is too large to fit in a byte array: ").append(expectedSize).append(" bytes").toString());
        }
        return expectedSize == 0 ? ByteStreams.toByteArray(in) : ByteStreams.toByteArray(in, (int) expectedSize);
    }

    public static ByteSink asByteSink(File file, FileWriteMode... modes) {
        return new FileByteSink(file, modes);
    }

    /* loaded from: guava-18.0.jar:com/google/common/io/Files$FileByteSink.class */
    private static final class FileByteSink extends ByteSink {
        private final File file;
        private final ImmutableSet<FileWriteMode> modes;

        private FileByteSink(File file, FileWriteMode... modes) {
            this.file = (File) Preconditions.checkNotNull(file);
            this.modes = ImmutableSet.copyOf(modes);
        }

        @Override // com.google.common.io.ByteSink
        public FileOutputStream openStream() throws IOException {
            return new FileOutputStream(this.file, this.modes.contains(FileWriteMode.APPEND));
        }

        public String toString() {
            String strValueOf = String.valueOf(String.valueOf(this.file));
            String strValueOf2 = String.valueOf(String.valueOf(this.modes));
            return new StringBuilder(20 + strValueOf.length() + strValueOf2.length()).append("Files.asByteSink(").append(strValueOf).append(", ").append(strValueOf2).append(")").toString();
        }
    }

    public static CharSource asCharSource(File file, Charset charset) {
        return asByteSource(file).asCharSource(charset);
    }

    public static CharSink asCharSink(File file, Charset charset, FileWriteMode... modes) {
        return asByteSink(file, modes).asCharSink(charset);
    }

    private static FileWriteMode[] modes(boolean append) {
        return append ? new FileWriteMode[]{FileWriteMode.APPEND} : new FileWriteMode[0];
    }

    public static byte[] toByteArray(File file) throws IOException {
        return asByteSource(file).read();
    }

    public static String toString(File file, Charset charset) throws IOException {
        return asCharSource(file, charset).read();
    }

    public static void write(byte[] from, File to) throws Throwable {
        asByteSink(to, new FileWriteMode[0]).write(from);
    }

    public static void copy(File from, OutputStream to) throws Throwable {
        asByteSource(from).copyTo(to);
    }

    public static void copy(File from, File to) throws Throwable {
        Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
        asByteSource(from).copyTo(asByteSink(to, new FileWriteMode[0]));
    }

    public static void write(CharSequence from, File to, Charset charset) throws Throwable {
        asCharSink(to, charset, new FileWriteMode[0]).write(from);
    }

    public static void append(CharSequence from, File to, Charset charset) throws Throwable {
        write(from, to, charset, true);
    }

    private static void write(CharSequence from, File to, Charset charset, boolean append) throws Throwable {
        asCharSink(to, charset, modes(append)).write(from);
    }

    public static void copy(File from, Charset charset, Appendable to) throws Throwable {
        asCharSource(from, charset).copyTo(to);
    }

    public static boolean equal(File file1, File file2) throws IOException {
        Preconditions.checkNotNull(file1);
        Preconditions.checkNotNull(file2);
        if (file1 == file2 || file1.equals(file2)) {
            return true;
        }
        long len1 = file1.length();
        long len2 = file2.length();
        if (len1 != 0 && len2 != 0 && len1 != len2) {
            return false;
        }
        return asByteSource(file1).contentEquals(asByteSource(file2));
    }

    public static File createTempDir() {
        File baseDir = new File(System.getProperty(TempFile.JAVA_IO_TMPDIR));
        String baseName = new StringBuilder(21).append(System.currentTimeMillis()).append("-").toString();
        for (int counter = 0; counter < 10000; counter++) {
            String strValueOf = String.valueOf(String.valueOf(baseName));
            File tempDir = new File(baseDir, new StringBuilder(11 + strValueOf.length()).append(strValueOf).append(counter).toString());
            if (tempDir.mkdir()) {
                return tempDir;
            }
        }
        String strValueOf2 = String.valueOf(String.valueOf("Failed to create directory within 10000 attempts (tried "));
        String strValueOf3 = String.valueOf(String.valueOf(baseName));
        String strValueOf4 = String.valueOf(String.valueOf(baseName));
        throw new IllegalStateException(new StringBuilder(17 + strValueOf2.length() + strValueOf3.length() + strValueOf4.length()).append(strValueOf2).append(strValueOf3).append("0 to ").append(strValueOf4).append(9999).append(")").toString());
    }

    public static void touch(File file) throws IOException {
        Preconditions.checkNotNull(file);
        if (!file.createNewFile() && !file.setLastModified(System.currentTimeMillis())) {
            String strValueOf = String.valueOf(String.valueOf(file));
            throw new IOException(new StringBuilder(38 + strValueOf.length()).append("Unable to update modification time of ").append(strValueOf).toString());
        }
    }

    public static void createParentDirs(File file) throws IOException {
        Preconditions.checkNotNull(file);
        File parent = file.getCanonicalFile().getParentFile();
        if (parent == null) {
            return;
        }
        parent.mkdirs();
        if (!parent.isDirectory()) {
            String strValueOf = String.valueOf(String.valueOf(file));
            throw new IOException(new StringBuilder(39 + strValueOf.length()).append("Unable to create parent directories of ").append(strValueOf).toString());
        }
    }

    public static void move(File from, File to) throws Throwable {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
        if (!from.renameTo(to)) {
            copy(from, to);
            if (!from.delete()) {
                if (!to.delete()) {
                    String strValueOf = String.valueOf(String.valueOf(to));
                    throw new IOException(new StringBuilder(17 + strValueOf.length()).append("Unable to delete ").append(strValueOf).toString());
                }
                String strValueOf2 = String.valueOf(String.valueOf(from));
                throw new IOException(new StringBuilder(17 + strValueOf2.length()).append("Unable to delete ").append(strValueOf2).toString());
            }
        }
    }

    public static String readFirstLine(File file, Charset charset) throws IOException {
        return asCharSource(file, charset).readFirstLine();
    }

    public static List<String> readLines(File file, Charset charset) throws IOException {
        return (List) readLines(file, charset, new LineProcessor<List<String>>() { // from class: com.google.common.io.Files.1
            final List<String> result = Lists.newArrayList();

            @Override // com.google.common.io.LineProcessor
            public boolean processLine(String line) {
                this.result.add(line);
                return true;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.io.LineProcessor
            public List<String> getResult() {
                return this.result;
            }
        });
    }

    public static <T> T readLines(File file, Charset charset, LineProcessor<T> lineProcessor) throws IOException {
        return (T) asCharSource(file, charset).readLines(lineProcessor);
    }

    public static <T> T readBytes(File file, ByteProcessor<T> byteProcessor) throws IOException {
        return (T) asByteSource(file).read(byteProcessor);
    }

    public static HashCode hash(File file, HashFunction hashFunction) throws IOException {
        return asByteSource(file).hash(hashFunction);
    }

    public static MappedByteBuffer map(File file) throws IOException {
        Preconditions.checkNotNull(file);
        return map(file, FileChannel.MapMode.READ_ONLY);
    }

    public static MappedByteBuffer map(File file, FileChannel.MapMode mode) throws IOException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(mode);
        if (!file.exists()) {
            throw new FileNotFoundException(file.toString());
        }
        return map(file, mode, file.length());
    }

    public static MappedByteBuffer map(File file, FileChannel.MapMode mode, long size) throws Throwable {
        RuntimeException runtimeExceptionRethrow;
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(mode);
        Closer closer = Closer.create();
        try {
            try {
                RandomAccessFile raf = (RandomAccessFile) closer.register(new RandomAccessFile(file, mode == FileChannel.MapMode.READ_ONLY ? ExcelXmlConstants.POSITION : "rw"));
                MappedByteBuffer map = map(raf, mode, size);
                closer.close();
                return map;
            } finally {
            }
        } catch (Throwable th) {
            closer.close();
            throw th;
        }
    }

    private static MappedByteBuffer map(RandomAccessFile raf, FileChannel.MapMode mode, long size) throws Throwable {
        RuntimeException runtimeExceptionRethrow;
        Closer closer = Closer.create();
        try {
            try {
                FileChannel channel = (FileChannel) closer.register(raf.getChannel());
                MappedByteBuffer map = channel.map(mode, 0L, size);
                closer.close();
                return map;
            } finally {
            }
        } catch (Throwable th) {
            closer.close();
            throw th;
        }
    }

    public static String simplifyPath(String pathname) {
        String strConcat;
        Preconditions.checkNotNull(pathname);
        if (pathname.length() == 0) {
            return ".";
        }
        Iterable<String> components = Splitter.on('/').omitEmptyStrings().split(pathname);
        List<String> path = new ArrayList<>();
        for (String component : components) {
            if (!component.equals(".")) {
                if (component.equals("..")) {
                    if (path.size() > 0 && !path.get(path.size() - 1).equals("..")) {
                        path.remove(path.size() - 1);
                    } else {
                        path.add("..");
                    }
                } else {
                    path.add(component);
                }
            }
        }
        String result = Joiner.on('/').join(path);
        if (pathname.charAt(0) == '/') {
            String strValueOf = String.valueOf(result);
            if (strValueOf.length() != 0) {
                strConcat = "/".concat(strValueOf);
            } else {
                strConcat = str;
                String str = new String("/");
            }
            result = strConcat;
        }
        while (result.startsWith("/../")) {
            result = result.substring(3);
        }
        if (result.equals("/..")) {
            result = "/";
        } else if ("".equals(result)) {
            result = ".";
        }
        return result;
    }

    public static String getFileExtension(String fullName) {
        Preconditions.checkNotNull(fullName);
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf(46);
        return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
    }

    public static String getNameWithoutExtension(String file) {
        Preconditions.checkNotNull(file);
        String fileName = new File(file).getName();
        int dotIndex = fileName.lastIndexOf(46);
        return dotIndex == -1 ? fileName : fileName.substring(0, dotIndex);
    }

    public static TreeTraverser<File> fileTreeTraverser() {
        return FILE_TREE_TRAVERSER;
    }

    public static Predicate<File> isDirectory() {
        return FilePredicate.IS_DIRECTORY;
    }

    public static Predicate<File> isFile() {
        return FilePredicate.IS_FILE;
    }
}
