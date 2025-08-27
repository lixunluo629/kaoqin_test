package org.apache.commons.compress.archivers.sevenz;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/sevenz/CLI.class */
public class CLI {

    /* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/sevenz/CLI$Mode.class */
    private enum Mode {
        LIST("Analysing") { // from class: org.apache.commons.compress.archivers.sevenz.CLI.Mode.1
            @Override // org.apache.commons.compress.archivers.sevenz.CLI.Mode
            public void takeAction(SevenZFile archive, SevenZArchiveEntry entry) {
                System.out.print(entry.getName());
                if (entry.isDirectory()) {
                    System.out.print(" dir");
                } else {
                    System.out.print(SymbolConstants.SPACE_SYMBOL + entry.getCompressedSize() + "/" + entry.getSize());
                }
                if (entry.getHasLastModifiedDate()) {
                    System.out.print(SymbolConstants.SPACE_SYMBOL + entry.getLastModifiedDate());
                } else {
                    System.out.print(" no last modified date");
                }
                if (!entry.isDirectory()) {
                    System.out.println(SymbolConstants.SPACE_SYMBOL + getContentMethods(entry));
                } else {
                    System.out.println("");
                }
            }

            private String getContentMethods(SevenZArchiveEntry entry) {
                StringBuilder sb = new StringBuilder();
                boolean first = true;
                for (SevenZMethodConfiguration m : entry.getContentMethods()) {
                    if (!first) {
                        sb.append(", ");
                    }
                    first = false;
                    sb.append(m.getMethod());
                    if (m.getOptions() != null) {
                        sb.append("(").append(m.getOptions()).append(")");
                    }
                }
                return sb.toString();
            }
        },
        EXTRACT("Extracting") { // from class: org.apache.commons.compress.archivers.sevenz.CLI.Mode.2
            private final byte[] buf = new byte[8192];

            @Override // org.apache.commons.compress.archivers.sevenz.CLI.Mode
            public void takeAction(SevenZFile archive, SevenZArchiveEntry entry) throws IOException {
                File outFile = new File(entry.getName());
                if (entry.isDirectory()) {
                    if (!outFile.isDirectory() && !outFile.mkdirs()) {
                        throw new IOException("Cannot create directory " + outFile);
                    }
                    System.out.println("created directory " + outFile);
                    return;
                }
                System.out.println("extracting to " + outFile);
                File parent = outFile.getParentFile();
                if (parent != null && !parent.exists() && !parent.mkdirs()) {
                    throw new IOException("Cannot create " + parent);
                }
                OutputStream fos = Files.newOutputStream(outFile.toPath(), new OpenOption[0]);
                Throwable th = null;
                try {
                    try {
                        long total = entry.getSize();
                        long off = 0;
                        while (off < total) {
                            int toRead = (int) Math.min(total - off, this.buf.length);
                            int bytesRead = archive.read(this.buf, 0, toRead);
                            if (bytesRead < 1) {
                                throw new IOException("Reached end of entry " + entry.getName() + " after " + off + " bytes, expected " + total);
                            }
                            off += bytesRead;
                            fos.write(this.buf, 0, bytesRead);
                        }
                        if (fos != null) {
                            if (0 != 0) {
                                try {
                                    fos.close();
                                    return;
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                    return;
                                }
                            }
                            fos.close();
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        throw th3;
                    }
                } catch (Throwable th4) {
                    if (fos != null) {
                        if (th != null) {
                            try {
                                fos.close();
                            } catch (Throwable th5) {
                                th.addSuppressed(th5);
                            }
                        } else {
                            fos.close();
                        }
                    }
                    throw th4;
                }
            }
        };

        private final String message;

        public abstract void takeAction(SevenZFile sevenZFile, SevenZArchiveEntry sevenZArchiveEntry) throws IOException;

        Mode(String message) {
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            usage();
            return;
        }
        Mode mode = grabMode(args);
        System.out.println(mode.getMessage() + SymbolConstants.SPACE_SYMBOL + args[0]);
        File f = new File(args[0]);
        if (!f.isFile()) {
            System.err.println(f + " doesn't exist or is a directory");
        }
        SevenZFile archive = new SevenZFile(f);
        Throwable th = null;
        while (true) {
            try {
                try {
                    SevenZArchiveEntry ae = archive.getNextEntry();
                    if (ae == null) {
                        break;
                    } else {
                        mode.takeAction(archive, ae);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    throw th2;
                }
            } catch (Throwable th3) {
                if (archive != null) {
                    if (th != null) {
                        try {
                            archive.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        archive.close();
                    }
                }
                throw th3;
            }
        }
        if (archive != null) {
            if (0 != 0) {
                try {
                    archive.close();
                    return;
                } catch (Throwable th5) {
                    th.addSuppressed(th5);
                    return;
                }
            }
            archive.close();
        }
    }

    private static void usage() {
        System.out.println("Parameters: archive-name [list|extract]");
    }

    private static Mode grabMode(String[] args) {
        if (args.length < 2) {
            return Mode.LIST;
        }
        return (Mode) Enum.valueOf(Mode.class, args[1].toUpperCase());
    }
}
