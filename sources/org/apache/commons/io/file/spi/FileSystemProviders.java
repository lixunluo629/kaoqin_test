package org.apache.commons.io.file.spi;

import io.netty.handler.codec.rtsp.RtspHeaders;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.commons.httpclient.cookie.Cookie2;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/file/spi/FileSystemProviders.class */
public class FileSystemProviders {
    private static final String SCHEME_FILE = "file";
    private static final FileSystemProviders INSTALLED = new FileSystemProviders(FileSystemProvider.installedProviders());
    private final List<FileSystemProvider> providers;

    public static FileSystemProvider getFileSystemProvider(Path path) {
        return ((Path) Objects.requireNonNull(path, Cookie2.PATH)).getFileSystem().provider();
    }

    public static FileSystemProviders installed() {
        return INSTALLED;
    }

    private FileSystemProviders(List<FileSystemProvider> providers) {
        this.providers = providers != null ? providers : Collections.emptyList();
    }

    public FileSystemProvider getFileSystemProvider(String scheme) {
        Objects.requireNonNull(scheme, "scheme");
        if (scheme.equalsIgnoreCase("file")) {
            return FileSystems.getDefault().provider();
        }
        return this.providers.stream().filter(provider -> {
            return provider.getScheme().equalsIgnoreCase(scheme);
        }).findFirst().orElse(null);
    }

    public FileSystemProvider getFileSystemProvider(URI uri) {
        return getFileSystemProvider(((URI) Objects.requireNonNull(uri, "uri")).getScheme());
    }

    public FileSystemProvider getFileSystemProvider(URL url) {
        return getFileSystemProvider(((URL) Objects.requireNonNull(url, RtspHeaders.Values.URL)).getProtocol());
    }
}
