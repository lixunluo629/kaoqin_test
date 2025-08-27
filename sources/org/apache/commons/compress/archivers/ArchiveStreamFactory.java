package org.apache.commons.compress.archivers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.compress.utils.ServiceLoaderIterator;
import org.apache.commons.compress.utils.Sets;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/ArchiveStreamFactory.class */
public class ArchiveStreamFactory implements ArchiveStreamProvider {
    private static final int TAR_HEADER_SIZE = 512;
    private static final int DUMP_SIGNATURE_SIZE = 32;
    private static final int SIGNATURE_SIZE = 12;
    private static final ArchiveStreamFactory SINGLETON = new ArchiveStreamFactory();
    public static final String AR = "ar";
    public static final String ARJ = "arj";
    public static final String CPIO = "cpio";
    public static final String DUMP = "dump";
    public static final String JAR = "jar";
    public static final String TAR = "tar";
    public static final String ZIP = "zip";
    public static final String SEVEN_Z = "7z";
    private final String encoding;
    private volatile String entryEncoding;
    private SortedMap<String, ArchiveStreamProvider> archiveInputStreamProviders;
    private SortedMap<String, ArchiveStreamProvider> archiveOutputStreamProviders;

    /* JADX INFO: Access modifiers changed from: private */
    public static ArrayList<ArchiveStreamProvider> findArchiveStreamProviders() {
        return Lists.newArrayList(serviceLoaderIterator());
    }

    static void putAll(Set<String> names, ArchiveStreamProvider provider, TreeMap<String, ArchiveStreamProvider> map) {
        for (String name : names) {
            map.put(toKey(name), provider);
        }
    }

    private static Iterator<ArchiveStreamProvider> serviceLoaderIterator() {
        return new ServiceLoaderIterator(ArchiveStreamProvider.class);
    }

    private static String toKey(String name) {
        return name.toUpperCase(Locale.ROOT);
    }

    public static SortedMap<String, ArchiveStreamProvider> findAvailableArchiveInputStreamProviders() {
        return (SortedMap) AccessController.doPrivileged(new PrivilegedAction<SortedMap<String, ArchiveStreamProvider>>() { // from class: org.apache.commons.compress.archivers.ArchiveStreamFactory.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public SortedMap<String, ArchiveStreamProvider> run() {
                TreeMap<String, ArchiveStreamProvider> map = new TreeMap<>();
                ArchiveStreamFactory.putAll(ArchiveStreamFactory.SINGLETON.getInputStreamArchiveNames(), ArchiveStreamFactory.SINGLETON, map);
                Iterator it = ArchiveStreamFactory.findArchiveStreamProviders().iterator();
                while (it.hasNext()) {
                    ArchiveStreamProvider provider = (ArchiveStreamProvider) it.next();
                    ArchiveStreamFactory.putAll(provider.getInputStreamArchiveNames(), provider, map);
                }
                return map;
            }
        });
    }

    public static SortedMap<String, ArchiveStreamProvider> findAvailableArchiveOutputStreamProviders() {
        return (SortedMap) AccessController.doPrivileged(new PrivilegedAction<SortedMap<String, ArchiveStreamProvider>>() { // from class: org.apache.commons.compress.archivers.ArchiveStreamFactory.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public SortedMap<String, ArchiveStreamProvider> run() {
                TreeMap<String, ArchiveStreamProvider> map = new TreeMap<>();
                ArchiveStreamFactory.putAll(ArchiveStreamFactory.SINGLETON.getOutputStreamArchiveNames(), ArchiveStreamFactory.SINGLETON, map);
                Iterator it = ArchiveStreamFactory.findArchiveStreamProviders().iterator();
                while (it.hasNext()) {
                    ArchiveStreamProvider provider = (ArchiveStreamProvider) it.next();
                    ArchiveStreamFactory.putAll(provider.getOutputStreamArchiveNames(), provider, map);
                }
                return map;
            }
        });
    }

    public ArchiveStreamFactory() {
        this(null);
    }

    public ArchiveStreamFactory(String encoding) {
        this.encoding = encoding;
        this.entryEncoding = encoding;
    }

    public String getEntryEncoding() {
        return this.entryEncoding;
    }

    @Deprecated
    public void setEntryEncoding(String entryEncoding) {
        if (this.encoding != null) {
            throw new IllegalStateException("Cannot overide encoding set by the constructor");
        }
        this.entryEncoding = entryEncoding;
    }

    public ArchiveInputStream createArchiveInputStream(String archiverName, InputStream in) throws ArchiveException {
        return createArchiveInputStream(archiverName, in, this.entryEncoding);
    }

    @Override // org.apache.commons.compress.archivers.ArchiveStreamProvider
    public ArchiveInputStream createArchiveInputStream(String archiverName, InputStream in, String actualEncoding) throws ArchiveException {
        if (archiverName == null) {
            throw new IllegalArgumentException("Archivername must not be null.");
        }
        if (in == null) {
            throw new IllegalArgumentException("InputStream must not be null.");
        }
        if (AR.equalsIgnoreCase(archiverName)) {
            return new ArArchiveInputStream(in);
        }
        if (ARJ.equalsIgnoreCase(archiverName)) {
            if (actualEncoding != null) {
                return new ArjArchiveInputStream(in, actualEncoding);
            }
            return new ArjArchiveInputStream(in);
        }
        if ("zip".equalsIgnoreCase(archiverName)) {
            if (actualEncoding != null) {
                return new ZipArchiveInputStream(in, actualEncoding);
            }
            return new ZipArchiveInputStream(in);
        }
        if (TAR.equalsIgnoreCase(archiverName)) {
            if (actualEncoding != null) {
                return new TarArchiveInputStream(in, actualEncoding);
            }
            return new TarArchiveInputStream(in);
        }
        if ("jar".equalsIgnoreCase(archiverName)) {
            if (actualEncoding != null) {
                return new JarArchiveInputStream(in, actualEncoding);
            }
            return new JarArchiveInputStream(in);
        }
        if (CPIO.equalsIgnoreCase(archiverName)) {
            if (actualEncoding != null) {
                return new CpioArchiveInputStream(in, actualEncoding);
            }
            return new CpioArchiveInputStream(in);
        }
        if (DUMP.equalsIgnoreCase(archiverName)) {
            if (actualEncoding != null) {
                return new DumpArchiveInputStream(in, actualEncoding);
            }
            return new DumpArchiveInputStream(in);
        }
        if (SEVEN_Z.equalsIgnoreCase(archiverName)) {
            throw new StreamingNotSupportedException(SEVEN_Z);
        }
        ArchiveStreamProvider archiveStreamProvider = getArchiveInputStreamProviders().get(toKey(archiverName));
        if (archiveStreamProvider != null) {
            return archiveStreamProvider.createArchiveInputStream(archiverName, in, actualEncoding);
        }
        throw new ArchiveException("Archiver: " + archiverName + " not found.");
    }

    public ArchiveOutputStream createArchiveOutputStream(String archiverName, OutputStream out) throws ArchiveException {
        return createArchiveOutputStream(archiverName, out, this.entryEncoding);
    }

    @Override // org.apache.commons.compress.archivers.ArchiveStreamProvider
    public ArchiveOutputStream createArchiveOutputStream(String archiverName, OutputStream out, String actualEncoding) throws ArchiveException {
        if (archiverName == null) {
            throw new IllegalArgumentException("Archivername must not be null.");
        }
        if (out == null) {
            throw new IllegalArgumentException("OutputStream must not be null.");
        }
        if (AR.equalsIgnoreCase(archiverName)) {
            return new ArArchiveOutputStream(out);
        }
        if ("zip".equalsIgnoreCase(archiverName)) {
            ZipArchiveOutputStream zip = new ZipArchiveOutputStream(out);
            if (actualEncoding != null) {
                zip.setEncoding(actualEncoding);
            }
            return zip;
        }
        if (TAR.equalsIgnoreCase(archiverName)) {
            if (actualEncoding != null) {
                return new TarArchiveOutputStream(out, actualEncoding);
            }
            return new TarArchiveOutputStream(out);
        }
        if ("jar".equalsIgnoreCase(archiverName)) {
            if (actualEncoding != null) {
                return new JarArchiveOutputStream(out, actualEncoding);
            }
            return new JarArchiveOutputStream(out);
        }
        if (CPIO.equalsIgnoreCase(archiverName)) {
            if (actualEncoding != null) {
                return new CpioArchiveOutputStream(out, actualEncoding);
            }
            return new CpioArchiveOutputStream(out);
        }
        if (SEVEN_Z.equalsIgnoreCase(archiverName)) {
            throw new StreamingNotSupportedException(SEVEN_Z);
        }
        ArchiveStreamProvider archiveStreamProvider = getArchiveOutputStreamProviders().get(toKey(archiverName));
        if (archiveStreamProvider != null) {
            return archiveStreamProvider.createArchiveOutputStream(archiverName, out, actualEncoding);
        }
        throw new ArchiveException("Archiver: " + archiverName + " not found.");
    }

    public ArchiveInputStream createArchiveInputStream(InputStream in) throws ArchiveException {
        return createArchiveInputStream(detect(in), in);
    }

    public static String detect(InputStream in) throws ArchiveException, IOException {
        if (in == null) {
            throw new IllegalArgumentException("Stream must not be null.");
        }
        if (!in.markSupported()) {
            throw new IllegalArgumentException("Mark is not supported.");
        }
        byte[] signature = new byte[12];
        in.mark(signature.length);
        try {
            int signatureLength = IOUtils.readFully(in, signature);
            in.reset();
            if (ZipArchiveInputStream.matches(signature, signatureLength)) {
                return "zip";
            }
            if (JarArchiveInputStream.matches(signature, signatureLength)) {
                return "jar";
            }
            if (ArArchiveInputStream.matches(signature, signatureLength)) {
                return AR;
            }
            if (CpioArchiveInputStream.matches(signature, signatureLength)) {
                return CPIO;
            }
            if (ArjArchiveInputStream.matches(signature, signatureLength)) {
                return ARJ;
            }
            if (SevenZFile.matches(signature, signatureLength)) {
                return SEVEN_Z;
            }
            byte[] dumpsig = new byte[32];
            in.mark(dumpsig.length);
            try {
                int signatureLength2 = IOUtils.readFully(in, dumpsig);
                in.reset();
                if (DumpArchiveInputStream.matches(dumpsig, signatureLength2)) {
                    return DUMP;
                }
                byte[] tarHeader = new byte[512];
                in.mark(tarHeader.length);
                try {
                    int signatureLength3 = IOUtils.readFully(in, tarHeader);
                    in.reset();
                    if (TarArchiveInputStream.matches(tarHeader, signatureLength3)) {
                        return TAR;
                    }
                    if (signatureLength3 >= 512) {
                        TarArchiveInputStream tais = null;
                        try {
                            tais = new TarArchiveInputStream(new ByteArrayInputStream(tarHeader));
                            if (tais.getNextTarEntry().isCheckSumOK()) {
                                IOUtils.closeQuietly(tais);
                                return TAR;
                            }
                            IOUtils.closeQuietly(tais);
                        } catch (Exception e) {
                            IOUtils.closeQuietly(tais);
                        } catch (Throwable th) {
                            IOUtils.closeQuietly(tais);
                            throw th;
                        }
                    }
                    throw new ArchiveException("No Archiver found for the stream signature");
                } catch (IOException e2) {
                    throw new ArchiveException("IOException while reading tar signature", e2);
                }
            } catch (IOException e3) {
                throw new ArchiveException("IOException while reading dump signature", e3);
            }
        } catch (IOException e4) {
            throw new ArchiveException("IOException while reading signature.", e4);
        }
    }

    public SortedMap<String, ArchiveStreamProvider> getArchiveInputStreamProviders() {
        if (this.archiveInputStreamProviders == null) {
            this.archiveInputStreamProviders = Collections.unmodifiableSortedMap(findAvailableArchiveInputStreamProviders());
        }
        return this.archiveInputStreamProviders;
    }

    public SortedMap<String, ArchiveStreamProvider> getArchiveOutputStreamProviders() {
        if (this.archiveOutputStreamProviders == null) {
            this.archiveOutputStreamProviders = Collections.unmodifiableSortedMap(findAvailableArchiveOutputStreamProviders());
        }
        return this.archiveOutputStreamProviders;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveStreamProvider
    public Set<String> getInputStreamArchiveNames() {
        return Sets.newHashSet(AR, ARJ, "zip", TAR, "jar", CPIO, DUMP, SEVEN_Z);
    }

    @Override // org.apache.commons.compress.archivers.ArchiveStreamProvider
    public Set<String> getOutputStreamArchiveNames() {
        return Sets.newHashSet(AR, "zip", TAR, "jar", CPIO, SEVEN_Z);
    }
}
