package org.apache.commons.compress.archivers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Enumeration;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/Lister.class */
public final class Lister {
    private static final ArchiveStreamFactory factory = new ArchiveStreamFactory();

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            usage();
            return;
        }
        System.out.println("Analysing " + args[0]);
        File f = new File(args[0]);
        if (!f.isFile()) {
            System.err.println(f + " doesn't exist or is a directory");
        }
        String format = args.length > 1 ? args[1] : detectFormat(f);
        if (ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(format)) {
            list7z(f);
        } else if ("zipfile".equals(format)) {
            listZipUsingZipFile(f);
        } else {
            listStream(f, args);
        }
    }

    private static void listStream(File f, String[] args) throws ArchiveException, IOException {
        InputStream fis = new BufferedInputStream(Files.newInputStream(f.toPath(), new OpenOption[0]));
        Throwable th = null;
        try {
            ArchiveInputStream ais = createArchiveInputStream(args, fis);
            Throwable th2 = null;
            try {
                try {
                    System.out.println("Created " + ais.toString());
                    while (true) {
                        ArchiveEntry ae = ais.getNextEntry();
                        if (ae == null) {
                            break;
                        } else {
                            System.out.println(ae.getName());
                        }
                    }
                    if (ais != null) {
                        if (0 != 0) {
                            try {
                                ais.close();
                            } catch (Throwable th3) {
                                th2.addSuppressed(th3);
                            }
                        } else {
                            ais.close();
                        }
                    }
                    if (fis != null) {
                        if (0 == 0) {
                            fis.close();
                            return;
                        }
                        try {
                            fis.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    }
                } catch (Throwable th5) {
                    if (ais != null) {
                        if (th2 != null) {
                            try {
                                ais.close();
                            } catch (Throwable th6) {
                                th2.addSuppressed(th6);
                            }
                        } else {
                            ais.close();
                        }
                    }
                    throw th5;
                }
            } catch (Throwable th7) {
                th2 = th7;
                throw th7;
            }
        } catch (Throwable th8) {
            if (fis != null) {
                if (0 != 0) {
                    try {
                        fis.close();
                    } catch (Throwable th9) {
                        th.addSuppressed(th9);
                    }
                } else {
                    fis.close();
                }
            }
            throw th8;
        }
    }

    private static ArchiveInputStream createArchiveInputStream(String[] args, InputStream fis) throws ArchiveException {
        if (args.length > 1) {
            return factory.createArchiveInputStream(args[1], fis);
        }
        return factory.createArchiveInputStream(fis);
    }

    private static String detectFormat(File f) throws ArchiveException, IOException {
        InputStream fis = new BufferedInputStream(Files.newInputStream(f.toPath(), new OpenOption[0]));
        Throwable th = null;
        try {
            ArchiveStreamFactory archiveStreamFactory = factory;
            String strDetect = ArchiveStreamFactory.detect(fis);
            if (fis != null) {
                if (0 != 0) {
                    try {
                        fis.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    fis.close();
                }
            }
            return strDetect;
        } catch (Throwable th3) {
            if (fis != null) {
                if (0 != 0) {
                    try {
                        fis.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    fis.close();
                }
            }
            throw th3;
        }
    }

    private static void list7z(File f) throws ArchiveException, IOException {
        SevenZFile z = new SevenZFile(f);
        Throwable th = null;
        try {
            System.out.println("Created " + z.toString());
            while (true) {
                ArchiveEntry ae = z.getNextEntry();
                if (ae == null) {
                    break;
                }
                String name = ae.getName() == null ? z.getDefaultName() + " (entry name was null)" : ae.getName();
                System.out.println(name);
            }
            if (z != null) {
                if (0 != 0) {
                    try {
                        z.close();
                        return;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        return;
                    }
                }
                z.close();
            }
        } catch (Throwable th3) {
            if (z != null) {
                if (0 != 0) {
                    try {
                        z.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    z.close();
                }
            }
            throw th3;
        }
    }

    private static void listZipUsingZipFile(File f) throws ArchiveException, IOException {
        ZipFile z = new ZipFile(f);
        Throwable th = null;
        try {
            System.out.println("Created " + z.toString());
            Enumeration<ZipArchiveEntry> en = z.getEntries();
            while (en.hasMoreElements()) {
                System.out.println(en.nextElement().getName());
            }
            if (z != null) {
                if (0 != 0) {
                    try {
                        z.close();
                        return;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        return;
                    }
                }
                z.close();
            }
        } catch (Throwable th3) {
            if (z != null) {
                if (0 != 0) {
                    try {
                        z.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    z.close();
                }
            }
            throw th3;
        }
    }

    private static void usage() {
        System.out.println("Parameters: archive-name [archive-type]\n");
        System.out.println("the magic archive-type 'zipfile' prefers ZipFile over ZipArchiveInputStream");
    }
}
