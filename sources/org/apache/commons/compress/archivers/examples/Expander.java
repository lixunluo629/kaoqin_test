package org.apache.commons.compress.archivers.examples;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.Enumeration;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/examples/Expander.class */
public class Expander {

    /* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/examples/Expander$ArchiveEntrySupplier.class */
    private interface ArchiveEntrySupplier {
        ArchiveEntry getNextReadableEntry() throws IOException;
    }

    /* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/examples/Expander$EntryWriter.class */
    private interface EntryWriter {
        void writeEntryDataTo(ArchiveEntry archiveEntry, OutputStream outputStream) throws IOException;
    }

    public void expand(File archive, File targetDirectory) throws ArchiveException, IOException {
        InputStream i = new BufferedInputStream(Files.newInputStream(archive.toPath(), new OpenOption[0]));
        Throwable th = null;
        try {
            try {
                new ArchiveStreamFactory();
                String format = ArchiveStreamFactory.detect(i);
                if (i != null) {
                    if (0 != 0) {
                        try {
                            i.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        i.close();
                    }
                }
                expand(format, archive, targetDirectory);
            } finally {
            }
        } catch (Throwable th3) {
            if (i != null) {
                if (th != null) {
                    try {
                        i.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    i.close();
                }
            }
            throw th3;
        }
    }

    public void expand(String format, File archive, File targetDirectory) throws ArchiveException, IOException {
        if (prefersSeekableByteChannel(format)) {
            SeekableByteChannel c = FileChannel.open(archive.toPath(), StandardOpenOption.READ);
            Throwable th = null;
            try {
                try {
                    expand(format, c, targetDirectory, CloseableConsumer.CLOSING_CONSUMER);
                    if (c != null) {
                        if (0 != 0) {
                            try {
                                c.close();
                                return;
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                                return;
                            }
                        }
                        c.close();
                        return;
                    }
                    return;
                } catch (Throwable th3) {
                    if (c != null) {
                        if (th != null) {
                            try {
                                c.close();
                            } catch (Throwable th4) {
                                th.addSuppressed(th4);
                            }
                        } else {
                            c.close();
                        }
                    }
                    throw th3;
                }
            } catch (Throwable th5) {
                th = th5;
                throw th5;
            }
        }
        InputStream i = new BufferedInputStream(Files.newInputStream(archive.toPath(), new OpenOption[0]));
        Throwable th6 = null;
        try {
            try {
                expand(format, i, targetDirectory, CloseableConsumer.CLOSING_CONSUMER);
                if (i != null) {
                    if (0 != 0) {
                        try {
                            i.close();
                            return;
                        } catch (Throwable th7) {
                            th6.addSuppressed(th7);
                            return;
                        }
                    }
                    i.close();
                }
            } catch (Throwable th8) {
                if (i != null) {
                    if (th6 != null) {
                        try {
                            i.close();
                        } catch (Throwable th9) {
                            th6.addSuppressed(th9);
                        }
                    } else {
                        i.close();
                    }
                }
                throw th8;
            }
        } catch (Throwable th10) {
            th6 = th10;
            throw th10;
        }
    }

    @Deprecated
    public void expand(InputStream archive, File targetDirectory) throws ArchiveException, IOException {
        expand(archive, targetDirectory, CloseableConsumer.NULL_CONSUMER);
    }

    public void expand(InputStream archive, File targetDirectory, CloseableConsumer closeableConsumer) throws ArchiveException, IOException {
        CloseableConsumerAdapter c = new CloseableConsumerAdapter(closeableConsumer);
        Throwable th = null;
        try {
            try {
                expand((ArchiveInputStream) c.track(new ArchiveStreamFactory().createArchiveInputStream(archive)), targetDirectory);
                if (c != null) {
                    if (0 != 0) {
                        try {
                            c.close();
                            return;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            return;
                        }
                    }
                    c.close();
                }
            } catch (Throwable th3) {
                th = th3;
                throw th3;
            }
        } catch (Throwable th4) {
            if (c != null) {
                if (th != null) {
                    try {
                        c.close();
                    } catch (Throwable th5) {
                        th.addSuppressed(th5);
                    }
                } else {
                    c.close();
                }
            }
            throw th4;
        }
    }

    @Deprecated
    public void expand(String format, InputStream archive, File targetDirectory) throws ArchiveException, IOException {
        expand(format, archive, targetDirectory, CloseableConsumer.NULL_CONSUMER);
    }

    public void expand(String format, InputStream archive, File targetDirectory, CloseableConsumer closeableConsumer) throws ArchiveException, IOException {
        CloseableConsumerAdapter c = new CloseableConsumerAdapter(closeableConsumer);
        Throwable th = null;
        try {
            try {
                expand((ArchiveInputStream) c.track(new ArchiveStreamFactory().createArchiveInputStream(format, archive)), targetDirectory);
                if (c != null) {
                    if (0 != 0) {
                        try {
                            c.close();
                            return;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            return;
                        }
                    }
                    c.close();
                }
            } catch (Throwable th3) {
                th = th3;
                throw th3;
            }
        } catch (Throwable th4) {
            if (c != null) {
                if (th != null) {
                    try {
                        c.close();
                    } catch (Throwable th5) {
                        th.addSuppressed(th5);
                    }
                } else {
                    c.close();
                }
            }
            throw th4;
        }
    }

    @Deprecated
    public void expand(String format, SeekableByteChannel archive, File targetDirectory) throws ArchiveException, IOException {
        expand(format, archive, targetDirectory, CloseableConsumer.NULL_CONSUMER);
    }

    public void expand(String format, SeekableByteChannel archive, File targetDirectory, CloseableConsumer closeableConsumer) throws ArchiveException, IOException {
        CloseableConsumerAdapter c = new CloseableConsumerAdapter(closeableConsumer);
        Throwable th = null;
        try {
            if (!prefersSeekableByteChannel(format)) {
                expand(format, (InputStream) c.track(Channels.newInputStream(archive)), targetDirectory);
            } else if ("zip".equalsIgnoreCase(format)) {
                expand((ZipFile) c.track(new ZipFile(archive)), targetDirectory);
            } else if (ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(format)) {
                expand((SevenZFile) c.track(new SevenZFile(archive)), targetDirectory);
            } else {
                throw new ArchiveException("Don't know how to handle format " + format);
            }
            if (c != null) {
                if (0 != 0) {
                    try {
                        c.close();
                        return;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        return;
                    }
                }
                c.close();
            }
        } catch (Throwable th3) {
            if (c != null) {
                if (0 != 0) {
                    try {
                        c.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    c.close();
                }
            }
            throw th3;
        }
    }

    public void expand(final ArchiveInputStream archive, File targetDirectory) throws ArchiveException, IOException {
        expand(new ArchiveEntrySupplier() { // from class: org.apache.commons.compress.archivers.examples.Expander.1
            @Override // org.apache.commons.compress.archivers.examples.Expander.ArchiveEntrySupplier
            public ArchiveEntry getNextReadableEntry() throws IOException {
                ArchiveEntry next;
                ArchiveEntry nextEntry = archive.getNextEntry();
                while (true) {
                    next = nextEntry;
                    if (next == null || archive.canReadEntryData(next)) {
                        break;
                    }
                    nextEntry = archive.getNextEntry();
                }
                return next;
            }
        }, new EntryWriter() { // from class: org.apache.commons.compress.archivers.examples.Expander.2
            @Override // org.apache.commons.compress.archivers.examples.Expander.EntryWriter
            public void writeEntryDataTo(ArchiveEntry entry, OutputStream out) throws IOException {
                IOUtils.copy(archive, out);
            }
        }, targetDirectory);
    }

    public void expand(final ZipFile archive, File targetDirectory) throws ArchiveException, IOException {
        final Enumeration<ZipArchiveEntry> entries = archive.getEntries();
        expand(new ArchiveEntrySupplier() { // from class: org.apache.commons.compress.archivers.examples.Expander.3
            @Override // org.apache.commons.compress.archivers.examples.Expander.ArchiveEntrySupplier
            public ArchiveEntry getNextReadableEntry() throws IOException {
                ZipArchiveEntry next;
                ZipArchiveEntry zipArchiveEntry = entries.hasMoreElements() ? (ZipArchiveEntry) entries.nextElement() : null;
                while (true) {
                    next = zipArchiveEntry;
                    if (next == null || archive.canReadEntryData(next)) {
                        break;
                    }
                    zipArchiveEntry = entries.hasMoreElements() ? (ZipArchiveEntry) entries.nextElement() : null;
                }
                return next;
            }
        }, new EntryWriter() { // from class: org.apache.commons.compress.archivers.examples.Expander.4
            @Override // org.apache.commons.compress.archivers.examples.Expander.EntryWriter
            public void writeEntryDataTo(ArchiveEntry entry, OutputStream out) throws IOException {
                InputStream in = archive.getInputStream((ZipArchiveEntry) entry);
                Throwable th = null;
                try {
                    IOUtils.copy(in, out);
                    if (in != null) {
                        if (0 != 0) {
                            try {
                                in.close();
                                return;
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                                return;
                            }
                        }
                        in.close();
                    }
                } catch (Throwable th3) {
                    if (in != null) {
                        if (0 != 0) {
                            try {
                                in.close();
                            } catch (Throwable th4) {
                                th.addSuppressed(th4);
                            }
                        } else {
                            in.close();
                        }
                    }
                    throw th3;
                }
            }
        }, targetDirectory);
    }

    public void expand(final SevenZFile archive, File targetDirectory) throws ArchiveException, IOException {
        expand(new ArchiveEntrySupplier() { // from class: org.apache.commons.compress.archivers.examples.Expander.5
            @Override // org.apache.commons.compress.archivers.examples.Expander.ArchiveEntrySupplier
            public ArchiveEntry getNextReadableEntry() throws IOException {
                return archive.getNextEntry();
            }
        }, new EntryWriter() { // from class: org.apache.commons.compress.archivers.examples.Expander.6
            @Override // org.apache.commons.compress.archivers.examples.Expander.EntryWriter
            public void writeEntryDataTo(ArchiveEntry entry, OutputStream out) throws IOException {
                byte[] buffer = new byte[8024];
                while (true) {
                    int n = archive.read(buffer);
                    if (-1 != n) {
                        out.write(buffer, 0, n);
                    } else {
                        return;
                    }
                }
            }
        }, targetDirectory);
    }

    private boolean prefersSeekableByteChannel(String format) {
        return "zip".equalsIgnoreCase(format) || ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(format);
    }

    private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory) throws IOException {
        String targetDirPath = targetDirectory.getCanonicalPath();
        if (!targetDirPath.endsWith(File.separator)) {
            targetDirPath = targetDirPath + File.separator;
        }
        ArchiveEntry nextReadableEntry = supplier.getNextReadableEntry();
        while (true) {
            ArchiveEntry nextEntry = nextReadableEntry;
            if (nextEntry != null) {
                File f = new File(targetDirectory, nextEntry.getName());
                if (!f.getCanonicalPath().startsWith(targetDirPath)) {
                    throw new IOException("Expanding " + nextEntry.getName() + " would create file outside of " + targetDirectory);
                }
                if (nextEntry.isDirectory()) {
                    if (!f.isDirectory() && !f.mkdirs()) {
                        throw new IOException("Failed to create directory " + f);
                    }
                } else {
                    File parent = f.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }
                    OutputStream o = Files.newOutputStream(f.toPath(), new OpenOption[0]);
                    Throwable th = null;
                    try {
                        try {
                            writer.writeEntryDataTo(nextEntry, o);
                            if (o != null) {
                                if (0 != 0) {
                                    try {
                                        o.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                } else {
                                    o.close();
                                }
                            }
                        } finally {
                        }
                    } catch (Throwable th3) {
                        if (o != null) {
                            if (th != null) {
                                try {
                                    o.close();
                                } catch (Throwable th4) {
                                    th.addSuppressed(th4);
                                }
                            } else {
                                o.close();
                            }
                        }
                        throw th3;
                    }
                }
                nextReadableEntry = supplier.getNextReadableEntry();
            } else {
                return;
            }
        }
    }
}
