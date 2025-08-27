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
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/examples/Archiver.class */
public class Archiver {

    /* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/examples/Archiver$ArchiveEntryConsumer.class */
    private interface ArchiveEntryConsumer {
        void accept(File file, ArchiveEntry archiveEntry) throws IOException;
    }

    /* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/examples/Archiver$ArchiveEntryCreator.class */
    private interface ArchiveEntryCreator {
        ArchiveEntry create(File file, String str) throws IOException;
    }

    /* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/examples/Archiver$Finisher.class */
    private interface Finisher {
        void finish() throws IOException;
    }

    public void create(String format, File target, File directory) throws ArchiveException, IOException {
        if (prefersSeekableByteChannel(format)) {
            SeekableByteChannel c = FileChannel.open(target.toPath(), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Throwable th = null;
            try {
                try {
                    create(format, c, directory, CloseableConsumer.CLOSING_CONSUMER);
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
        OutputStream o = Files.newOutputStream(target.toPath(), new OpenOption[0]);
        Throwable th6 = null;
        try {
            try {
                create(format, o, directory, CloseableConsumer.CLOSING_CONSUMER);
                if (o != null) {
                    if (0 != 0) {
                        try {
                            o.close();
                            return;
                        } catch (Throwable th7) {
                            th6.addSuppressed(th7);
                            return;
                        }
                    }
                    o.close();
                }
            } catch (Throwable th8) {
                if (o != null) {
                    if (th6 != null) {
                        try {
                            o.close();
                        } catch (Throwable th9) {
                            th6.addSuppressed(th9);
                        }
                    } else {
                        o.close();
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
    public void create(String format, OutputStream target, File directory) throws ArchiveException, IOException {
        create(format, target, directory, CloseableConsumer.NULL_CONSUMER);
    }

    public void create(String format, OutputStream target, File directory, CloseableConsumer closeableConsumer) throws ArchiveException, IOException {
        CloseableConsumerAdapter c = new CloseableConsumerAdapter(closeableConsumer);
        Throwable th = null;
        try {
            try {
                create((ArchiveOutputStream) c.track(new ArchiveStreamFactory().createArchiveOutputStream(format, target)), directory);
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
    public void create(String format, SeekableByteChannel target, File directory) throws ArchiveException, IOException {
        create(format, target, directory, CloseableConsumer.NULL_CONSUMER);
    }

    public void create(String format, SeekableByteChannel target, File directory, CloseableConsumer closeableConsumer) throws ArchiveException, IOException {
        CloseableConsumerAdapter c = new CloseableConsumerAdapter(closeableConsumer);
        Throwable th = null;
        try {
            if (!prefersSeekableByteChannel(format)) {
                create(format, (OutputStream) c.track(Channels.newOutputStream(target)), directory);
            } else if ("zip".equalsIgnoreCase(format)) {
                create((ArchiveOutputStream) c.track(new ZipArchiveOutputStream(target)), directory);
            } else if (ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(format)) {
                create((SevenZOutputFile) c.track(new SevenZOutputFile(target)), directory);
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

    public void create(final ArchiveOutputStream target, File directory) throws ArchiveException, IOException {
        create(directory, new ArchiveEntryCreator() { // from class: org.apache.commons.compress.archivers.examples.Archiver.1
            @Override // org.apache.commons.compress.archivers.examples.Archiver.ArchiveEntryCreator
            public ArchiveEntry create(File f, String entryName) throws IOException {
                return target.createArchiveEntry(f, entryName);
            }
        }, new ArchiveEntryConsumer() { // from class: org.apache.commons.compress.archivers.examples.Archiver.2
            @Override // org.apache.commons.compress.archivers.examples.Archiver.ArchiveEntryConsumer
            public void accept(File source, ArchiveEntry e) throws IOException {
                target.putArchiveEntry(e);
                if (!e.isDirectory()) {
                    InputStream in = new BufferedInputStream(Files.newInputStream(source.toPath(), new OpenOption[0]));
                    Throwable th = null;
                    try {
                        try {
                            IOUtils.copy(in, target);
                            if (in != null) {
                                if (0 != 0) {
                                    try {
                                        in.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                } else {
                                    in.close();
                                }
                            }
                        } finally {
                        }
                    } catch (Throwable th3) {
                        if (in != null) {
                            if (th != null) {
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
                target.closeArchiveEntry();
            }
        }, new Finisher() { // from class: org.apache.commons.compress.archivers.examples.Archiver.3
            @Override // org.apache.commons.compress.archivers.examples.Archiver.Finisher
            public void finish() throws IOException {
                target.finish();
            }
        });
    }

    public void create(final SevenZOutputFile target, File directory) throws IOException {
        create(directory, new ArchiveEntryCreator() { // from class: org.apache.commons.compress.archivers.examples.Archiver.4
            @Override // org.apache.commons.compress.archivers.examples.Archiver.ArchiveEntryCreator
            public ArchiveEntry create(File f, String entryName) throws IOException {
                return target.createArchiveEntry(f, entryName);
            }
        }, new ArchiveEntryConsumer() { // from class: org.apache.commons.compress.archivers.examples.Archiver.5
            @Override // org.apache.commons.compress.archivers.examples.Archiver.ArchiveEntryConsumer
            public void accept(File source, ArchiveEntry e) throws IOException {
                target.putArchiveEntry(e);
                if (!e.isDirectory()) {
                    byte[] buffer = new byte[8024];
                    long count = 0;
                    InputStream in = new BufferedInputStream(Files.newInputStream(source.toPath(), new OpenOption[0]));
                    Throwable th = null;
                    while (true) {
                        try {
                            try {
                                int n = in.read(buffer);
                                if (-1 == n) {
                                    break;
                                }
                                target.write(buffer, 0, n);
                                count += n;
                            } finally {
                            }
                        } catch (Throwable th2) {
                            if (in != null) {
                                if (th != null) {
                                    try {
                                        in.close();
                                    } catch (Throwable th3) {
                                        th.addSuppressed(th3);
                                    }
                                } else {
                                    in.close();
                                }
                            }
                            throw th2;
                        }
                    }
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
                }
                target.closeArchiveEntry();
            }
        }, new Finisher() { // from class: org.apache.commons.compress.archivers.examples.Archiver.6
            @Override // org.apache.commons.compress.archivers.examples.Archiver.Finisher
            public void finish() throws IOException {
                target.finish();
            }
        });
    }

    private boolean prefersSeekableByteChannel(String format) {
        return "zip".equalsIgnoreCase(format) || ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(format);
    }

    private void create(File directory, ArchiveEntryCreator creator, ArchiveEntryConsumer consumer, Finisher finisher) throws IOException {
        create("", directory, creator, consumer);
        finisher.finish();
    }

    private void create(String prefix, File directory, ArchiveEntryCreator creator, ArchiveEntryConsumer consumer) throws IOException {
        File[] children = directory.listFiles();
        if (children == null) {
            return;
        }
        for (File f : children) {
            String entryName = prefix + f.getName() + (f.isDirectory() ? "/" : "");
            consumer.accept(f, creator.create(f, entryName));
            if (f.isDirectory()) {
                create(entryName, f, creator, consumer);
            }
        }
    }
}
