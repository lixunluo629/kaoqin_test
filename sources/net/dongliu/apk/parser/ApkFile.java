package net.dongliu.apk.parser;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.dongliu.apk.parser.AbstractApkFile;
import net.dongliu.apk.parser.bean.ApkSignStatus;
import net.dongliu.apk.parser.utils.Inputs;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/ApkFile.class */
public class ApkFile extends AbstractApkFile implements Closeable {
    private final ZipFile zf;
    private File apkFile;

    public ApkFile(File apkFile) throws IOException {
        this.apkFile = apkFile;
        this.zf = new ZipFile(apkFile);
    }

    public ApkFile(String filePath) throws IOException {
        this(new File(filePath));
    }

    @Override // net.dongliu.apk.parser.AbstractApkFile
    protected List<AbstractApkFile.CertificateFile> getAllCertificateData() throws IOException {
        Enumeration<? extends ZipEntry> enu = this.zf.entries();
        List<AbstractApkFile.CertificateFile> list = new ArrayList<>();
        while (enu.hasMoreElements()) {
            ZipEntry ne = enu.nextElement();
            if (!ne.isDirectory()) {
                String name = ne.getName().toUpperCase();
                if (name.endsWith(".RSA") || name.endsWith(".DSA")) {
                    list.add(new AbstractApkFile.CertificateFile(name, Inputs.readAll(this.zf.getInputStream(ne))));
                }
            }
        }
        return list;
    }

    @Override // net.dongliu.apk.parser.AbstractApkFile
    public byte[] getFileData(String path) throws IOException {
        ZipEntry entry = this.zf.getEntry(path);
        if (entry == null) {
            return null;
        }
        InputStream inputStream = this.zf.getInputStream(entry);
        return Inputs.readAll(inputStream);
    }

    @Override // net.dongliu.apk.parser.AbstractApkFile
    protected ByteBuffer fileData() throws IOException {
        FileChannel channel = new FileInputStream(this.apkFile).getChannel();
        return channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size());
    }

    @Override // net.dongliu.apk.parser.AbstractApkFile
    @Deprecated
    public ApkSignStatus verifyApk() throws IOException {
        ZipEntry entry = this.zf.getEntry("META-INF/MANIFEST.MF");
        if (entry == null) {
            return ApkSignStatus.notSigned;
        }
        JarFile jarFile = new JarFile(this.apkFile);
        Throwable th = null;
        try {
            Enumeration<JarEntry> entries = jarFile.entries();
            byte[] buffer = new byte[8192];
            while (entries.hasMoreElements()) {
                JarEntry e = entries.nextElement();
                if (!e.isDirectory()) {
                    try {
                        InputStream in = jarFile.getInputStream(e);
                        Throwable th2 = null;
                        do {
                            try {
                                try {
                                } catch (Throwable th3) {
                                    if (in != null) {
                                        if (th2 != null) {
                                            try {
                                                in.close();
                                            } catch (Throwable th4) {
                                                th2.addSuppressed(th4);
                                            }
                                        } else {
                                            in.close();
                                        }
                                    }
                                    throw th3;
                                }
                            } finally {
                            }
                        } while (in.read(buffer, 0, buffer.length) != -1);
                        if (in != null) {
                            if (0 != 0) {
                                try {
                                    in.close();
                                } catch (Throwable th5) {
                                    th2.addSuppressed(th5);
                                }
                            } else {
                                in.close();
                            }
                        }
                    } catch (SecurityException e2) {
                        ApkSignStatus apkSignStatus = ApkSignStatus.incorrect;
                        if (jarFile != null) {
                            if (0 != 0) {
                                try {
                                    jarFile.close();
                                } catch (Throwable th6) {
                                    th.addSuppressed(th6);
                                }
                            } else {
                                jarFile.close();
                            }
                        }
                        return apkSignStatus;
                    }
                }
            }
            return ApkSignStatus.signed;
        } finally {
            if (jarFile != null) {
                if (0 != 0) {
                    try {
                        jarFile.close();
                    } catch (Throwable th7) {
                        th.addSuppressed(th7);
                    }
                } else {
                    jarFile.close();
                }
            }
        }
    }

    @Override // net.dongliu.apk.parser.AbstractApkFile, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.zf.close();
    }
}
