package org.apache.commons.io.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.AccessDeniedException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.time.Duration;
import java.time.Instant;
import java.time.chrono.ChronoZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.httpclient.cookie.Cookie2;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.RandomAccessFileMode;
import org.apache.commons.io.RandomAccessFiles;
import org.apache.commons.io.ThreadUtils;
import org.apache.commons.io.file.Counters;
import org.apache.commons.io.file.attribute.FileTimes;
import org.apache.commons.io.function.IOFunction;
import org.apache.commons.io.function.IOSupplier;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/file/PathUtils.class */
public final class PathUtils {
    private static final OpenOption[] OPEN_OPTIONS_TRUNCATE = {StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
    private static final OpenOption[] OPEN_OPTIONS_APPEND = {StandardOpenOption.CREATE, StandardOpenOption.APPEND};
    public static final CopyOption[] EMPTY_COPY_OPTIONS = new CopyOption[0];
    public static final DeleteOption[] EMPTY_DELETE_OPTION_ARRAY = new DeleteOption[0];
    public static final FileAttribute<?>[] EMPTY_FILE_ATTRIBUTE_ARRAY = new FileAttribute[0];
    public static final FileVisitOption[] EMPTY_FILE_VISIT_OPTION_ARRAY = new FileVisitOption[0];
    public static final LinkOption[] EMPTY_LINK_OPTION_ARRAY = new LinkOption[0];

    @Deprecated
    public static final LinkOption[] NOFOLLOW_LINK_OPTION_ARRAY = {LinkOption.NOFOLLOW_LINKS};
    static final LinkOption NULL_LINK_OPTION = null;
    public static final OpenOption[] EMPTY_OPEN_OPTION_ARRAY = new OpenOption[0];
    public static final Path[] EMPTY_PATH_ARRAY = new Path[0];

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/file/PathUtils$RelativeSortedPaths.class */
    private static final class RelativeSortedPaths {
        final boolean equals;
        final List<Path> relativeFileList1;
        final List<Path> relativeFileList2;

        private RelativeSortedPaths(Path dir1, Path dir2, int maxDepth, LinkOption[] linkOptions, FileVisitOption[] fileVisitOptions) throws IOException {
            List<Path> tmpRelativeFileList1 = null;
            List<Path> tmpRelativeFileList2 = null;
            if (dir1 == null && dir2 == null) {
                this.equals = true;
            } else {
                if ((dir1 == null) ^ (dir2 == null)) {
                    this.equals = false;
                } else {
                    boolean parentDirNotExists1 = Files.notExists(dir1, linkOptions);
                    boolean parentDirNotExists2 = Files.notExists(dir2, linkOptions);
                    if (!parentDirNotExists1 && !parentDirNotExists2) {
                        AccumulatorPathVisitor visitor1 = PathUtils.accumulate(dir1, maxDepth, fileVisitOptions);
                        AccumulatorPathVisitor visitor2 = PathUtils.accumulate(dir2, maxDepth, fileVisitOptions);
                        if (visitor1.getDirList().size() != visitor2.getDirList().size() || visitor1.getFileList().size() != visitor2.getFileList().size()) {
                            this.equals = false;
                        } else {
                            List<Path> tmpRelativeDirList1 = visitor1.relativizeDirectories(dir1, true, null);
                            List<Path> tmpRelativeDirList2 = visitor2.relativizeDirectories(dir2, true, null);
                            if (!tmpRelativeDirList1.equals(tmpRelativeDirList2)) {
                                this.equals = false;
                            } else {
                                tmpRelativeFileList1 = visitor1.relativizeFiles(dir1, true, null);
                                tmpRelativeFileList2 = visitor2.relativizeFiles(dir2, true, null);
                                this.equals = tmpRelativeFileList1.equals(tmpRelativeFileList2);
                            }
                        }
                    } else {
                        this.equals = parentDirNotExists1 && parentDirNotExists2;
                    }
                }
            }
            this.relativeFileList1 = tmpRelativeFileList1;
            this.relativeFileList2 = tmpRelativeFileList2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AccumulatorPathVisitor accumulate(Path directory, int maxDepth, FileVisitOption[] fileVisitOptions) throws IOException {
        return (AccumulatorPathVisitor) visitFileTree(AccumulatorPathVisitor.withLongCounters(), directory, toFileVisitOptionSet(fileVisitOptions), maxDepth);
    }

    public static Counters.PathCounters cleanDirectory(Path directory) throws IOException {
        return cleanDirectory(directory, EMPTY_DELETE_OPTION_ARRAY);
    }

    public static Counters.PathCounters cleanDirectory(Path directory, DeleteOption... deleteOptions) throws IOException {
        return ((CleaningPathVisitor) visitFileTree(new CleaningPathVisitor(Counters.longPathCounters(), deleteOptions, new String[0]), directory)).getPathCounters();
    }

    private static int compareLastModifiedTimeTo(Path file, FileTime fileTime, LinkOption... options) throws IOException {
        return getLastModifiedTime(file, options).compareTo(fileTime);
    }

    public static long copy(IOSupplier<InputStream> in, Path target, CopyOption... copyOptions) throws IOException {
        InputStream inputStream = in.get();
        try {
            long jCopy = Files.copy(inputStream, target, copyOptions);
            if (inputStream != null) {
                inputStream.close();
            }
            return jCopy;
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public static Counters.PathCounters copyDirectory(Path sourceDirectory, Path targetDirectory, CopyOption... copyOptions) throws IOException {
        Path absoluteSource = sourceDirectory.toAbsolutePath();
        return ((CopyDirectoryVisitor) visitFileTree(new CopyDirectoryVisitor(Counters.longPathCounters(), absoluteSource, targetDirectory, copyOptions), absoluteSource)).getPathCounters();
    }

    public static Path copyFile(URL sourceFile, Path targetFile, CopyOption... copyOptions) throws IOException {
        Objects.requireNonNull(sourceFile);
        copy(sourceFile::openStream, targetFile, copyOptions);
        return targetFile;
    }

    public static Path copyFileToDirectory(Path sourceFile, Path targetDirectory, CopyOption... copyOptions) throws IOException {
        return Files.copy(sourceFile, targetDirectory.resolve(sourceFile.getFileName()), copyOptions);
    }

    public static Path copyFileToDirectory(URL sourceFile, Path targetDirectory, CopyOption... copyOptions) throws IOException {
        Path resolve = targetDirectory.resolve(FilenameUtils.getName(sourceFile.getFile()));
        Objects.requireNonNull(sourceFile);
        copy(sourceFile::openStream, resolve, copyOptions);
        return resolve;
    }

    public static Counters.PathCounters countDirectory(Path directory) throws IOException {
        return ((CountingPathVisitor) visitFileTree(CountingPathVisitor.withLongCounters(), directory)).getPathCounters();
    }

    public static Counters.PathCounters countDirectoryAsBigInteger(Path directory) throws IOException {
        return ((CountingPathVisitor) visitFileTree(CountingPathVisitor.withBigIntegerCounters(), directory)).getPathCounters();
    }

    public static Path createParentDirectories(Path path, FileAttribute<?>... attrs) throws IOException {
        return createParentDirectories(path, LinkOption.NOFOLLOW_LINKS, attrs);
    }

    public static Path createParentDirectories(Path path, LinkOption linkOption, FileAttribute<?>... attrs) throws IOException {
        Path parent = getParent(path);
        Path parent2 = linkOption == LinkOption.NOFOLLOW_LINKS ? parent : readIfSymbolicLink(parent);
        if (parent2 == null) {
            return null;
        }
        boolean exists = linkOption == null ? Files.exists(parent2, new LinkOption[0]) : Files.exists(parent2, linkOption);
        return exists ? parent2 : Files.createDirectories(parent2, attrs);
    }

    public static Path current() {
        return Paths.get(".", new String[0]);
    }

    public static Counters.PathCounters delete(Path path) throws IOException {
        return delete(path, EMPTY_DELETE_OPTION_ARRAY);
    }

    public static Counters.PathCounters delete(Path path, DeleteOption... deleteOptions) throws IOException {
        return Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS) ? deleteDirectory(path, deleteOptions) : deleteFile(path, deleteOptions);
    }

    public static Counters.PathCounters delete(Path path, LinkOption[] linkOptions, DeleteOption... deleteOptions) throws IOException {
        return Files.isDirectory(path, linkOptions) ? deleteDirectory(path, linkOptions, deleteOptions) : deleteFile(path, linkOptions, deleteOptions);
    }

    public static Counters.PathCounters deleteDirectory(Path directory) throws IOException {
        return deleteDirectory(directory, EMPTY_DELETE_OPTION_ARRAY);
    }

    public static Counters.PathCounters deleteDirectory(Path directory, DeleteOption... deleteOptions) throws IOException {
        LinkOption[] linkOptions = noFollowLinkOptionArray();
        return (Counters.PathCounters) withPosixFileAttributes(getParent(directory), linkOptions, overrideReadOnly(deleteOptions), pfa -> {
            return ((DeletingPathVisitor) visitFileTree(new DeletingPathVisitor(Counters.longPathCounters(), linkOptions, deleteOptions, new String[0]), directory)).getPathCounters();
        });
    }

    public static Counters.PathCounters deleteDirectory(Path directory, LinkOption[] linkOptions, DeleteOption... deleteOptions) throws IOException {
        return ((DeletingPathVisitor) visitFileTree(new DeletingPathVisitor(Counters.longPathCounters(), linkOptions, deleteOptions, new String[0]), directory)).getPathCounters();
    }

    public static Counters.PathCounters deleteFile(Path file) throws IOException {
        return deleteFile(file, EMPTY_DELETE_OPTION_ARRAY);
    }

    public static Counters.PathCounters deleteFile(Path file, DeleteOption... deleteOptions) throws IOException {
        return deleteFile(file, noFollowLinkOptionArray(), deleteOptions);
    }

    public static Counters.PathCounters deleteFile(Path file, LinkOption[] linkOptions, DeleteOption... deleteOptions) throws IOException {
        if (Files.isDirectory(file, linkOptions)) {
            throw new NoSuchFileException(file.toString());
        }
        Counters.PathCounters pathCounts = Counters.longPathCounters();
        boolean exists = exists(file, linkOptions);
        long size = (!exists || Files.isSymbolicLink(file)) ? 0L : Files.size(file);
        try {
            if (Files.deleteIfExists(file)) {
                pathCounts.getFileCounter().increment();
                pathCounts.getByteCounter().add(size);
                return pathCounts;
            }
        } catch (AccessDeniedException e) {
        }
        Path parent = getParent(file);
        PosixFileAttributes posixFileAttributes = null;
        try {
            if (overrideReadOnly(deleteOptions)) {
                posixFileAttributes = readPosixFileAttributes(parent, linkOptions);
                setReadOnly(file, false, linkOptions);
            }
            boolean exists2 = exists(file, linkOptions);
            long size2 = (!exists2 || Files.isSymbolicLink(file)) ? 0L : Files.size(file);
            if (Files.deleteIfExists(file)) {
                pathCounts.getFileCounter().increment();
                pathCounts.getByteCounter().add(size2);
            }
            return pathCounts;
        } finally {
            if (posixFileAttributes != null) {
                Files.setPosixFilePermissions(parent, posixFileAttributes.permissions());
            }
        }
    }

    public static void deleteOnExit(Path path) {
        ((Path) Objects.requireNonNull(path)).toFile().deleteOnExit();
    }

    public static boolean directoryAndFileContentEquals(Path path1, Path path2) throws IOException {
        return directoryAndFileContentEquals(path1, path2, EMPTY_LINK_OPTION_ARRAY, EMPTY_OPEN_OPTION_ARRAY, EMPTY_FILE_VISIT_OPTION_ARRAY);
    }

    public static boolean directoryAndFileContentEquals(Path path1, Path path2, LinkOption[] linkOptions, OpenOption[] openOptions, FileVisitOption[] fileVisitOption) throws IOException {
        if (path1 == null && path2 == null) {
            return true;
        }
        if (path1 == null || path2 == null) {
            return false;
        }
        if (notExists(path1, new LinkOption[0]) && notExists(path2, new LinkOption[0])) {
            return true;
        }
        RelativeSortedPaths relativeSortedPaths = new RelativeSortedPaths(path1, path2, Integer.MAX_VALUE, linkOptions, fileVisitOption);
        if (!relativeSortedPaths.equals) {
            return false;
        }
        List<Path> fileList1 = relativeSortedPaths.relativeFileList1;
        List<Path> fileList2 = relativeSortedPaths.relativeFileList2;
        for (Path path : fileList1) {
            int binarySearch = Collections.binarySearch(fileList2, path);
            if (binarySearch <= -1) {
                throw new IllegalStateException("Unexpected mismatch.");
            }
            if (!fileContentEquals(path1.resolve(path), path2.resolve(path), linkOptions, openOptions)) {
                return false;
            }
        }
        return true;
    }

    public static boolean directoryContentEquals(Path path1, Path path2) throws IOException {
        return directoryContentEquals(path1, path2, Integer.MAX_VALUE, EMPTY_LINK_OPTION_ARRAY, EMPTY_FILE_VISIT_OPTION_ARRAY);
    }

    public static boolean directoryContentEquals(Path path1, Path path2, int maxDepth, LinkOption[] linkOptions, FileVisitOption[] fileVisitOptions) throws IOException {
        return new RelativeSortedPaths(path1, path2, maxDepth, linkOptions, fileVisitOptions).equals;
    }

    private static boolean exists(Path path, LinkOption... options) {
        return path != null && (options == null ? Files.exists(path, new LinkOption[0]) : Files.exists(path, options));
    }

    public static boolean fileContentEquals(Path path1, Path path2) throws IOException {
        return fileContentEquals(path1, path2, EMPTY_LINK_OPTION_ARRAY, EMPTY_OPEN_OPTION_ARRAY);
    }

    public static boolean fileContentEquals(Path path1, Path path2, LinkOption[] linkOptions, OpenOption[] openOptions) throws IOException {
        if (path1 == null && path2 == null) {
            return true;
        }
        if (path1 == null || path2 == null) {
            return false;
        }
        Path nPath1 = path1.normalize();
        Path nPath2 = path2.normalize();
        boolean path1Exists = exists(nPath1, linkOptions);
        if (path1Exists != exists(nPath2, linkOptions)) {
            return false;
        }
        if (!path1Exists) {
            return true;
        }
        if (Files.isDirectory(nPath1, linkOptions)) {
            throw new IOException("Can't compare directories, only files: " + nPath1);
        }
        if (Files.isDirectory(nPath2, linkOptions)) {
            throw new IOException("Can't compare directories, only files: " + nPath2);
        }
        if (Files.size(nPath1) != Files.size(nPath2)) {
            return false;
        }
        if (path1.equals(path2)) {
            return true;
        }
        try {
            RandomAccessFile raf1 = RandomAccessFileMode.READ_ONLY.create(path1.toRealPath(linkOptions));
            try {
                RandomAccessFile raf2 = RandomAccessFileMode.READ_ONLY.create(path2.toRealPath(linkOptions));
                try {
                    boolean zContentEquals = RandomAccessFiles.contentEquals(raf1, raf2);
                    if (raf2 != null) {
                        raf2.close();
                    }
                    if (raf1 != null) {
                        raf1.close();
                    }
                    return zContentEquals;
                } catch (Throwable th) {
                    if (raf2 != null) {
                        try {
                            raf2.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } finally {
            }
        } catch (UnsupportedOperationException e) {
            InputStream inputStream1 = Files.newInputStream(nPath1, openOptions);
            try {
                InputStream inputStream2 = Files.newInputStream(nPath2, openOptions);
                try {
                    boolean zContentEquals2 = IOUtils.contentEquals(inputStream1, inputStream2);
                    if (inputStream2 != null) {
                        inputStream2.close();
                    }
                    if (inputStream1 != null) {
                        inputStream1.close();
                    }
                    return zContentEquals2;
                } catch (Throwable th3) {
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                        } catch (Throwable th4) {
                            th3.addSuppressed(th4);
                        }
                    }
                    throw th3;
                }
            } catch (Throwable th5) {
                if (inputStream1 != null) {
                    try {
                        inputStream1.close();
                    } catch (Throwable th6) {
                        th5.addSuppressed(th6);
                    }
                }
                throw th5;
            }
        }
    }

    public static Path[] filter(PathFilter filter, Path... paths) {
        Objects.requireNonNull(filter, "filter");
        if (paths == null) {
            return EMPTY_PATH_ARRAY;
        }
        return (Path[]) ((List) filterPaths(filter, Stream.of((Object[]) paths), Collectors.toList())).toArray(EMPTY_PATH_ARRAY);
    }

    private static <R, A> R filterPaths(PathFilter pathFilter, Stream<Path> stream, Collector<? super Path, A, R> collector) {
        Objects.requireNonNull(pathFilter, "filter");
        Objects.requireNonNull(collector, "collector");
        if (stream == null) {
            return (R) Stream.empty().collect(collector);
        }
        return (R) stream.filter(p -> {
            if (p != null) {
                try {
                    if (pathFilter.accept(p, readBasicFileAttributes(p)) == FileVisitResult.CONTINUE) {
                        return true;
                    }
                } catch (IOException e) {
                    return false;
                }
            }
            return false;
        }).collect(collector);
    }

    public static List<AclEntry> getAclEntryList(Path sourcePath) throws IOException {
        AclFileAttributeView fileAttributeView = getAclFileAttributeView(sourcePath, new LinkOption[0]);
        if (fileAttributeView == null) {
            return null;
        }
        return fileAttributeView.getAcl();
    }

    public static AclFileAttributeView getAclFileAttributeView(Path path, LinkOption... options) {
        return (AclFileAttributeView) Files.getFileAttributeView(path, AclFileAttributeView.class, options);
    }

    public static String getBaseName(Path path) {
        Path fileName;
        if (path == null || (fileName = path.getFileName()) == null) {
            return null;
        }
        return FilenameUtils.removeExtension(fileName.toString());
    }

    public static DosFileAttributeView getDosFileAttributeView(Path path, LinkOption... options) {
        return (DosFileAttributeView) Files.getFileAttributeView(path, DosFileAttributeView.class, options);
    }

    public static String getExtension(Path path) {
        String fileName = getFileNameString(path);
        if (fileName != null) {
            return FilenameUtils.getExtension(fileName);
        }
        return null;
    }

    public static <R> R getFileName(Path path, Function<Path, R> function) {
        Path fileName = path != null ? path.getFileName() : null;
        if (fileName != null) {
            return function.apply(fileName);
        }
        return null;
    }

    public static String getFileNameString(Path path) {
        return (String) getFileName(path, (v0) -> {
            return v0.toString();
        });
    }

    public static FileTime getLastModifiedFileTime(File file) throws IOException {
        return getLastModifiedFileTime(file.toPath(), null, EMPTY_LINK_OPTION_ARRAY);
    }

    public static FileTime getLastModifiedFileTime(Path path, FileTime defaultIfAbsent, LinkOption... options) throws IOException {
        return Files.exists(path, new LinkOption[0]) ? getLastModifiedTime(path, options) : defaultIfAbsent;
    }

    public static FileTime getLastModifiedFileTime(Path path, LinkOption... options) throws IOException {
        return getLastModifiedFileTime(path, null, options);
    }

    public static FileTime getLastModifiedFileTime(URI uri) throws IOException {
        return getLastModifiedFileTime(Paths.get(uri), null, EMPTY_LINK_OPTION_ARRAY);
    }

    public static FileTime getLastModifiedFileTime(URL url) throws URISyntaxException, IOException {
        return getLastModifiedFileTime(url.toURI());
    }

    private static FileTime getLastModifiedTime(Path path, LinkOption... options) throws IOException {
        return Files.getLastModifiedTime((Path) Objects.requireNonNull(path, Cookie2.PATH), options);
    }

    private static Path getParent(Path path) {
        if (path == null) {
            return null;
        }
        return path.getParent();
    }

    public static PosixFileAttributeView getPosixFileAttributeView(Path path, LinkOption... options) {
        return (PosixFileAttributeView) Files.getFileAttributeView(path, PosixFileAttributeView.class, options);
    }

    public static Path getTempDirectory() {
        return Paths.get(FileUtils.getTempDirectoryPath(), new String[0]);
    }

    public static boolean isDirectory(Path path, LinkOption... options) {
        return path != null && Files.isDirectory(path, options);
    }

    public static boolean isEmpty(Path path) throws IOException {
        return Files.isDirectory(path, new LinkOption[0]) ? isEmptyDirectory(path) : isEmptyFile(path);
    }

    public static boolean isEmptyDirectory(Path directory) throws IOException {
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory);
        try {
            boolean z = !directoryStream.iterator().hasNext();
            if (directoryStream != null) {
                directoryStream.close();
            }
            return z;
        } catch (Throwable th) {
            if (directoryStream != null) {
                try {
                    directoryStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public static boolean isEmptyFile(Path file) throws IOException {
        return Files.size(file) <= 0;
    }

    public static boolean isNewer(Path file, ChronoZonedDateTime<?> czdt, LinkOption... options) throws IOException {
        Objects.requireNonNull(czdt, "czdt");
        return isNewer(file, czdt.toInstant(), options);
    }

    public static boolean isNewer(Path file, FileTime fileTime, LinkOption... options) throws IOException {
        return !notExists(file, new LinkOption[0]) && compareLastModifiedTimeTo(file, fileTime, options) > 0;
    }

    public static boolean isNewer(Path file, Instant instant, LinkOption... options) throws IOException {
        return isNewer(file, FileTime.from(instant), options);
    }

    public static boolean isNewer(Path file, long timeMillis, LinkOption... options) throws IOException {
        return isNewer(file, FileTime.fromMillis(timeMillis), options);
    }

    public static boolean isNewer(Path file, Path reference) throws IOException {
        return isNewer(file, getLastModifiedTime(reference, new LinkOption[0]), new LinkOption[0]);
    }

    public static boolean isOlder(Path file, FileTime fileTime, LinkOption... options) throws IOException {
        return !notExists(file, new LinkOption[0]) && compareLastModifiedTimeTo(file, fileTime, options) < 0;
    }

    public static boolean isOlder(Path file, Instant instant, LinkOption... options) throws IOException {
        return isOlder(file, FileTime.from(instant), options);
    }

    public static boolean isOlder(Path file, long timeMillis, LinkOption... options) throws IOException {
        return isOlder(file, FileTime.fromMillis(timeMillis), options);
    }

    public static boolean isOlder(Path file, Path reference) throws IOException {
        return isOlder(file, getLastModifiedTime(reference, new LinkOption[0]), new LinkOption[0]);
    }

    public static boolean isPosix(Path test, LinkOption... options) {
        return exists(test, options) && readPosixFileAttributes(test, options) != null;
    }

    public static boolean isRegularFile(Path path, LinkOption... options) {
        return path != null && Files.isRegularFile(path, options);
    }

    public static DirectoryStream<Path> newDirectoryStream(Path dir, PathFilter pathFilter) throws IOException {
        return Files.newDirectoryStream(dir, new DirectoryStreamFilter(pathFilter));
    }

    public static OutputStream newOutputStream(Path path, boolean append) throws IOException {
        return newOutputStream(path, EMPTY_LINK_OPTION_ARRAY, append ? OPEN_OPTIONS_APPEND : OPEN_OPTIONS_TRUNCATE);
    }

    static OutputStream newOutputStream(Path path, LinkOption[] linkOptions, OpenOption... openOptions) throws IOException {
        if (!exists(path, linkOptions)) {
            createParentDirectories(path, (linkOptions == null || linkOptions.length <= 0) ? NULL_LINK_OPTION : linkOptions[0], new FileAttribute[0]);
        }
        List<OpenOption> list = new ArrayList<>(Arrays.asList(openOptions != null ? openOptions : EMPTY_OPEN_OPTION_ARRAY));
        list.addAll(Arrays.asList(linkOptions != null ? linkOptions : EMPTY_LINK_OPTION_ARRAY));
        return Files.newOutputStream(path, (OpenOption[]) list.toArray(EMPTY_OPEN_OPTION_ARRAY));
    }

    public static LinkOption[] noFollowLinkOptionArray() {
        return (LinkOption[]) NOFOLLOW_LINK_OPTION_ARRAY.clone();
    }

    private static boolean notExists(Path path, LinkOption... options) {
        return Files.notExists((Path) Objects.requireNonNull(path, Cookie2.PATH), options);
    }

    private static boolean overrideReadOnly(DeleteOption... deleteOptions) {
        if (deleteOptions == null) {
            return false;
        }
        return Stream.of((Object[]) deleteOptions).anyMatch(e -> {
            return e == StandardDeleteOption.OVERRIDE_READ_ONLY;
        });
    }

    public static <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> cls, LinkOption... linkOptionArr) {
        if (path == null) {
            return null;
        }
        try {
            return (A) Files.readAttributes(path, cls, linkOptionArr);
        } catch (IOException | UnsupportedOperationException e) {
            return null;
        }
    }

    public static BasicFileAttributes readBasicFileAttributes(Path path) throws IOException {
        return Files.readAttributes(path, BasicFileAttributes.class, new LinkOption[0]);
    }

    public static BasicFileAttributes readBasicFileAttributes(Path path, LinkOption... options) {
        return readAttributes(path, BasicFileAttributes.class, options);
    }

    @Deprecated
    public static BasicFileAttributes readBasicFileAttributesUnchecked(Path path) {
        return readBasicFileAttributes(path, EMPTY_LINK_OPTION_ARRAY);
    }

    public static DosFileAttributes readDosFileAttributes(Path path, LinkOption... options) {
        return (DosFileAttributes) readAttributes(path, DosFileAttributes.class, options);
    }

    private static Path readIfSymbolicLink(Path path) throws IOException {
        if (path != null) {
            return Files.isSymbolicLink(path) ? Files.readSymbolicLink(path) : path;
        }
        return null;
    }

    public static BasicFileAttributes readOsFileAttributes(Path path, LinkOption... options) {
        PosixFileAttributes fileAttributes = readPosixFileAttributes(path, options);
        return fileAttributes != null ? fileAttributes : readDosFileAttributes(path, options);
    }

    public static PosixFileAttributes readPosixFileAttributes(Path path, LinkOption... options) {
        return (PosixFileAttributes) readAttributes(path, PosixFileAttributes.class, options);
    }

    public static String readString(Path path, Charset charset) throws IOException {
        return new String(Files.readAllBytes(path), Charsets.toCharset(charset));
    }

    static List<Path> relativize(Collection<Path> collection, Path parent, boolean sort, Comparator<? super Path> comparator) {
        Stream<Path> stream = collection.stream();
        Objects.requireNonNull(parent);
        Stream map = stream.map(parent::relativize);
        if (sort) {
            map = comparator == null ? map.sorted() : map.sorted(comparator);
        }
        return (List) map.collect(Collectors.toList());
    }

    private static Path requireExists(Path file, String fileParamName, LinkOption... options) {
        Objects.requireNonNull(file, fileParamName);
        if (!exists(file, options)) {
            throw new IllegalArgumentException("File system element for parameter '" + fileParamName + "' does not exist: '" + file + "'");
        }
        return file;
    }

    private static boolean setDosReadOnly(Path path, boolean readOnly, LinkOption... linkOptions) throws IOException {
        DosFileAttributeView dosFileAttributeView = getDosFileAttributeView(path, linkOptions);
        if (dosFileAttributeView != null) {
            dosFileAttributeView.setReadOnly(readOnly);
            return true;
        }
        return false;
    }

    public static void setLastModifiedTime(Path sourceFile, Path targetFile) throws IOException {
        Objects.requireNonNull(sourceFile, "sourceFile");
        Files.setLastModifiedTime(targetFile, getLastModifiedTime(sourceFile, new LinkOption[0]));
    }

    private static boolean setPosixDeletePermissions(Path parent, boolean enableDeleteChildren, LinkOption... linkOptions) throws IOException {
        return setPosixPermissions(parent, enableDeleteChildren, Arrays.asList(PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE), linkOptions);
    }

    private static boolean setPosixPermissions(Path path, boolean addPermissions, List<PosixFilePermission> updatePermissions, LinkOption... linkOptions) throws IOException {
        if (path != null) {
            Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(path, linkOptions);
            Set<PosixFilePermission> newPermissions = new HashSet<>(permissions);
            if (addPermissions) {
                newPermissions.addAll(updatePermissions);
            } else {
                newPermissions.removeAll(updatePermissions);
            }
            if (!newPermissions.equals(permissions)) {
                Files.setPosixFilePermissions(path, newPermissions);
                return true;
            }
            return true;
        }
        return false;
    }

    private static void setPosixReadOnlyFile(Path path, boolean readOnly, LinkOption... linkOptions) throws IOException {
        Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(path, linkOptions);
        List<PosixFilePermission> readPermissions = Arrays.asList(PosixFilePermission.OWNER_READ);
        List<PosixFilePermission> writePermissions = Arrays.asList(PosixFilePermission.OWNER_WRITE);
        if (readOnly) {
            permissions.addAll(readPermissions);
            permissions.removeAll(writePermissions);
        } else {
            permissions.addAll(readPermissions);
            permissions.addAll(writePermissions);
        }
        Files.setPosixFilePermissions(path, permissions);
    }

    public static Path setReadOnly(Path path, boolean readOnly, LinkOption... linkOptions) throws IOException {
        try {
            if (setDosReadOnly(path, readOnly, linkOptions)) {
                return path;
            }
        } catch (IOException e) {
        }
        Path parent = getParent(path);
        if (!isPosix(parent, linkOptions)) {
            throw new IOException(String.format("DOS or POSIX file operations not available for '%s', linkOptions %s", path, Arrays.toString(linkOptions)));
        }
        if (readOnly) {
            setPosixReadOnlyFile(path, readOnly, linkOptions);
            setPosixDeletePermissions(parent, false, linkOptions);
        } else {
            setPosixDeletePermissions(parent, true, linkOptions);
        }
        return path;
    }

    public static long sizeOf(Path path) throws IOException {
        requireExists(path, Cookie2.PATH, new LinkOption[0]);
        return Files.isDirectory(path, new LinkOption[0]) ? sizeOfDirectory(path) : Files.size(path);
    }

    public static BigInteger sizeOfAsBigInteger(Path path) throws IOException {
        requireExists(path, Cookie2.PATH, new LinkOption[0]);
        return Files.isDirectory(path, new LinkOption[0]) ? sizeOfDirectoryAsBigInteger(path) : BigInteger.valueOf(Files.size(path));
    }

    public static long sizeOfDirectory(Path directory) throws IOException {
        return countDirectory(directory).getByteCounter().getLong().longValue();
    }

    public static BigInteger sizeOfDirectoryAsBigInteger(Path directory) throws IOException {
        return countDirectoryAsBigInteger(directory).getByteCounter().getBigInteger();
    }

    static Set<FileVisitOption> toFileVisitOptionSet(FileVisitOption... fileVisitOptions) {
        return fileVisitOptions == null ? EnumSet.noneOf(FileVisitOption.class) : (Set) Stream.of((Object[]) fileVisitOptions).collect(Collectors.toSet());
    }

    public static Path touch(Path file) throws IOException {
        Objects.requireNonNull(file, "file");
        if (!Files.exists(file, new LinkOption[0])) {
            createParentDirectories(file, new FileAttribute[0]);
            Files.createFile(file, new FileAttribute[0]);
        } else {
            FileTimes.setLastModifiedTime(file);
        }
        return file;
    }

    public static <T extends FileVisitor<? super Path>> T visitFileTree(T visitor, Path directory) throws IOException {
        Files.walkFileTree(directory, visitor);
        return visitor;
    }

    public static <T extends FileVisitor<? super Path>> T visitFileTree(T visitor, Path start, Set<FileVisitOption> options, int maxDepth) throws IOException {
        Files.walkFileTree(start, options, maxDepth, visitor);
        return visitor;
    }

    public static <T extends FileVisitor<? super Path>> T visitFileTree(T t, String str, String... strArr) throws IOException {
        return (T) visitFileTree(t, Paths.get(str, strArr));
    }

    public static <T extends FileVisitor<? super Path>> T visitFileTree(T t, URI uri) throws IOException {
        return (T) visitFileTree(t, Paths.get(uri));
    }

    public static boolean waitFor(Path file, Duration timeout, LinkOption... options) {
        Objects.requireNonNull(file, "file");
        Instant finishInstant = Instant.now().plus((TemporalAmount) timeout);
        boolean interrupted = false;
        while (!exists(file, options)) {
            try {
                Instant now = Instant.now();
                if (!now.isAfter(finishInstant)) {
                    try {
                        ThreadUtils.sleep(Duration.ofMillis(Math.min(100L, finishInstant.minusMillis(now.toEpochMilli()).toEpochMilli())));
                    } catch (InterruptedException e) {
                        interrupted = true;
                    } catch (Exception e2) {
                    }
                } else {
                    return false;
                }
            } finally {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        return exists(file, options);
    }

    public static Stream<Path> walk(Path start, PathFilter pathFilter, int maxDepth, boolean readAttributes, FileVisitOption... options) throws IOException {
        return Files.walk(start, maxDepth, options).filter(path -> {
            return pathFilter.accept(path, readAttributes ? readBasicFileAttributesUnchecked(path) : null) == FileVisitResult.CONTINUE;
        });
    }

    private static <R> R withPosixFileAttributes(Path path, LinkOption[] linkOptions, boolean overrideReadOnly, IOFunction<PosixFileAttributes, R> function) throws IOException {
        PosixFileAttributes posixFileAttributes = overrideReadOnly ? readPosixFileAttributes(path, linkOptions) : null;
        try {
            R rApply = function.apply(posixFileAttributes);
            if (posixFileAttributes != null && path != null && Files.exists(path, linkOptions)) {
                Files.setPosixFilePermissions(path, posixFileAttributes.permissions());
            }
            return rApply;
        } catch (Throwable th) {
            if (posixFileAttributes != null && path != null && Files.exists(path, linkOptions)) {
                Files.setPosixFilePermissions(path, posixFileAttributes.permissions());
            }
            throw th;
        }
    }

    public static Path writeString(Path path, CharSequence charSequence, Charset charset, OpenOption... openOptions) throws IOException {
        Objects.requireNonNull(path, Cookie2.PATH);
        Objects.requireNonNull(charSequence, "charSequence");
        Files.write(path, String.valueOf(charSequence).getBytes(Charsets.toCharset(charset)), openOptions);
        return path;
    }

    private PathUtils() {
    }
}
