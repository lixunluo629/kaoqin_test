package lombok.core.configuration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import lombok.ConfigurationKeys;
import lombok.core.configuration.ConfigurationSource;
import lombok.core.debug.ProblemReporter;
import org.springframework.util.ResourceUtils;

/* loaded from: lombok-1.16.22.jar:lombok/core/configuration/FileSystemSourceCache.SCL.lombok */
public class FileSystemSourceCache {
    private static final String LOMBOK_CONFIG_FILENAME = "lombok.config";
    private static final long NEVER_CHECKED = -1;
    private static final long MISSING = -88;
    private final ConcurrentMap<File, Content> dirCache = new ConcurrentHashMap();
    private final ConcurrentMap<URI, File> uriCache = new ConcurrentHashMap();
    private volatile long lastCacheClear = System.currentTimeMillis();
    private static final long FULL_CACHE_CLEAR_INTERVAL = TimeUnit.MINUTES.toMillis(30);
    private static final long RECHECK_FILESYSTEM = TimeUnit.SECONDS.toMillis(2);
    private static final ThreadLocal<byte[]> buffers = new ThreadLocal<byte[]>() { // from class: lombok.core.configuration.FileSystemSourceCache.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public byte[] initialValue() {
            return new byte[65536];
        }
    };

    private void cacheClear() {
        long now = System.currentTimeMillis();
        long delta = now - this.lastCacheClear;
        if (delta > FULL_CACHE_CLEAR_INTERVAL) {
            this.lastCacheClear = now;
            this.dirCache.clear();
            this.uriCache.clear();
        }
    }

    public Iterable<ConfigurationSource> sourcesForJavaFile(URI javaFile, ConfigurationProblemReporter reporter) {
        if (javaFile == null) {
            return Collections.emptyList();
        }
        cacheClear();
        File dir = this.uriCache.get(javaFile);
        if (dir == null) {
            URI uri = javaFile.normalize();
            if (!uri.isAbsolute()) {
                uri = URI.create(ResourceUtils.FILE_URL_PREFIX + uri.toString());
            }
            try {
                File file = new File(uri);
                if (!file.exists()) {
                    throw new IllegalArgumentException("File does not exist: " + uri);
                }
                dir = file.isDirectory() ? file : file.getParentFile();
                if (dir != null) {
                    this.uriCache.put(javaFile, dir);
                }
            } catch (IllegalArgumentException unused) {
            } catch (Exception e) {
                ProblemReporter.error("Can't find absolute path of file being compiled: " + javaFile, e);
            }
        }
        if (dir != null) {
            try {
                return sourcesForDirectory(dir, reporter);
            } catch (Exception e2) {
                ProblemReporter.error("Can't resolve config stack for dir: " + dir.getAbsolutePath(), e2);
            }
        }
        return Collections.emptyList();
    }

    public Iterable<ConfigurationSource> sourcesForDirectory(URI directory, ConfigurationProblemReporter reporter) {
        return sourcesForJavaFile(directory, reporter);
    }

    private Iterable<ConfigurationSource> sourcesForDirectory(final File directory, final ConfigurationProblemReporter reporter) {
        return new Iterable<ConfigurationSource>() { // from class: lombok.core.configuration.FileSystemSourceCache.2
            @Override // java.lang.Iterable
            public Iterator<ConfigurationSource> iterator() {
                return new Iterator<ConfigurationSource>(directory, reporter) { // from class: lombok.core.configuration.FileSystemSourceCache.2.1
                    File currentDirectory;
                    ConfigurationSource next;
                    boolean stopBubbling = false;
                    private final /* synthetic */ ConfigurationProblemReporter val$reporter;

                    {
                        this.val$reporter = configurationProblemReporter;
                        this.currentDirectory = file;
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        if (this.next != null) {
                            return true;
                        }
                        if (this.stopBubbling) {
                            return false;
                        }
                        this.next = findNext();
                        return this.next != null;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public ConfigurationSource next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        ConfigurationSource result = this.next;
                        this.next = null;
                        return result;
                    }

                    private ConfigurationSource findNext() {
                        while (this.currentDirectory != null && this.next == null) {
                            this.next = FileSystemSourceCache.this.getSourceForDirectory(this.currentDirectory, this.val$reporter);
                            this.currentDirectory = this.currentDirectory.getParentFile();
                        }
                        if (this.next != null) {
                            ConfigurationSource.Result stop = this.next.resolve(ConfigurationKeys.STOP_BUBBLING);
                            this.stopBubbling = stop != null && Boolean.TRUE.equals(stop.getValue());
                        }
                        return this.next;
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Throwable, lombok.core.configuration.FileSystemSourceCache$Content] */
    ConfigurationSource getSourceForDirectory(File directory, ConfigurationProblemReporter reporter) {
        long now = System.currentTimeMillis();
        File configFile = new File(directory, LOMBOK_CONFIG_FILENAME);
        ?? EnsureContent = ensureContent(directory);
        synchronized (EnsureContent) {
            if (EnsureContent.lastChecked != -1 && now - EnsureContent.lastChecked < RECHECK_FILESYSTEM) {
                return EnsureContent.source;
            }
            EnsureContent.lastChecked = now;
            long previouslyModified = EnsureContent.lastModified;
            EnsureContent.lastModified = getLastModifiedOrMissing(configFile);
            if (EnsureContent.lastModified != previouslyModified) {
                EnsureContent.source = EnsureContent.lastModified == MISSING ? null : parse(configFile, reporter);
            }
            return EnsureContent.source;
        }
    }

    private Content ensureContent(File directory) {
        Content content = this.dirCache.get(directory);
        if (content != null) {
            return content;
        }
        this.dirCache.putIfAbsent(directory, Content.empty());
        return this.dirCache.get(directory);
    }

    private ConfigurationSource parse(File configFile, ConfigurationProblemReporter reporter) {
        String contentDescription = configFile.getAbsolutePath();
        try {
            return StringConfigurationSource.forString(fileToString(configFile), reporter, contentDescription);
        } catch (Exception e) {
            reporter.report(contentDescription, "Exception while reading file: " + e.getMessage(), 0, null);
            return null;
        }
    }

    static String fileToString(File configFile) throws Exception {
        byte[] b = buffers.get();
        FileInputStream fis = new FileInputStream(configFile);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while (true) {
                int r = fis.read(b);
                if (r != -1) {
                    out.write(b, 0, r);
                } else {
                    return new String(out.toByteArray(), "UTF-8");
                }
            }
        } finally {
            fis.close();
        }
    }

    private static final long getLastModifiedOrMissing(File file) {
        return (file.exists() && file.isFile()) ? file.lastModified() : MISSING;
    }

    /* loaded from: lombok-1.16.22.jar:lombok/core/configuration/FileSystemSourceCache$Content.SCL.lombok */
    private static class Content {
        ConfigurationSource source;
        long lastModified;
        long lastChecked;

        private Content(ConfigurationSource source, long lastModified, long lastChecked) {
            this.source = source;
            this.lastModified = lastModified;
            this.lastChecked = lastChecked;
        }

        static Content empty() {
            return new Content(null, FileSystemSourceCache.MISSING, -1L);
        }
    }
}
