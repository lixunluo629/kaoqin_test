package net.dongliu.apk.parser;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.dongliu.apk.parser.AbstractApkFile;
import net.dongliu.apk.parser.bean.ApkSignStatus;
import net.dongliu.apk.parser.utils.Inputs;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/ByteArrayApkFile.class */
public class ByteArrayApkFile extends AbstractApkFile implements Closeable {
    private byte[] apkData;

    public ByteArrayApkFile(byte[] apkData) {
        this.apkData = apkData;
    }

    @Override // net.dongliu.apk.parser.AbstractApkFile
    protected List<AbstractApkFile.CertificateFile> getAllCertificateData() throws IOException {
        List<AbstractApkFile.CertificateFile> list = new ArrayList<>();
        InputStream in = new ByteArrayInputStream(this.apkData);
        Throwable th = null;
        try {
            ZipInputStream zis = new ZipInputStream(in);
            Throwable th2 = null;
            while (true) {
                try {
                    try {
                        ZipEntry entry = zis.getNextEntry();
                        if (entry == null) {
                            break;
                        }
                        String name = entry.getName();
                        if (name.toUpperCase().endsWith(".RSA") || name.toUpperCase().endsWith(".DSA")) {
                            list.add(new AbstractApkFile.CertificateFile(name, Inputs.readAll(zis)));
                        }
                    } catch (Throwable th3) {
                        if (zis != null) {
                            if (th2 != null) {
                                try {
                                    zis.close();
                                } catch (Throwable th4) {
                                    th2.addSuppressed(th4);
                                }
                            } else {
                                zis.close();
                            }
                        }
                        throw th3;
                    }
                } finally {
                }
            }
            if (zis != null) {
                if (0 != 0) {
                    try {
                        zis.close();
                    } catch (Throwable th5) {
                        th2.addSuppressed(th5);
                    }
                } else {
                    zis.close();
                }
            }
            return list;
        } finally {
            if (in != null) {
                if (0 != 0) {
                    try {
                        in.close();
                    } catch (Throwable th6) {
                        th.addSuppressed(th6);
                    }
                } else {
                    in.close();
                }
            }
        }
    }

    @Override // net.dongliu.apk.parser.AbstractApkFile
    public byte[] getFileData(String path) throws IOException {
        ZipEntry entry;
        InputStream in = new ByteArrayInputStream(this.apkData);
        Throwable th = null;
        try {
            ZipInputStream zis = new ZipInputStream(in);
            Throwable th2 = null;
            do {
                try {
                    try {
                        entry = zis.getNextEntry();
                        if (entry == null) {
                            if (zis != null) {
                                if (0 != 0) {
                                    try {
                                        zis.close();
                                    } catch (Throwable th3) {
                                        th2.addSuppressed(th3);
                                    }
                                } else {
                                    zis.close();
                                }
                            }
                            if (in == null) {
                                return null;
                            }
                            if (0 == 0) {
                                in.close();
                                return null;
                            }
                            try {
                                in.close();
                                return null;
                            } catch (Throwable th4) {
                                th.addSuppressed(th4);
                                return null;
                            }
                        }
                    } catch (Throwable th5) {
                        if (zis != null) {
                            if (th2 != null) {
                                try {
                                    zis.close();
                                } catch (Throwable th6) {
                                    th2.addSuppressed(th6);
                                }
                            } else {
                                zis.close();
                            }
                        }
                        throw th5;
                    }
                } catch (Throwable th7) {
                    th2 = th7;
                    throw th7;
                }
            } while (!path.equals(entry.getName()));
            byte[] all = Inputs.readAll(zis);
            if (zis != null) {
                if (0 != 0) {
                    try {
                        zis.close();
                    } catch (Throwable th8) {
                        th2.addSuppressed(th8);
                    }
                } else {
                    zis.close();
                }
            }
            return all;
        } finally {
            if (in != null) {
                if (0 != 0) {
                    try {
                        in.close();
                    } catch (Throwable th9) {
                        th.addSuppressed(th9);
                    }
                } else {
                    in.close();
                }
            }
        }
    }

    @Override // net.dongliu.apk.parser.AbstractApkFile
    protected ByteBuffer fileData() {
        return ByteBuffer.wrap(this.apkData).asReadOnlyBuffer();
    }

    @Override // net.dongliu.apk.parser.AbstractApkFile
    public ApkSignStatus verifyApk() {
        throw new UnsupportedOperationException();
    }

    @Override // net.dongliu.apk.parser.AbstractApkFile, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.apkData = null;
    }
}
