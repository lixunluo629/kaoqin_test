package net.dongliu.apk.parser;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import net.dongliu.apk.parser.bean.ApkMeta;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/ApkParsers.class */
public class ApkParsers {
    private static boolean useBouncyCastle;

    public static boolean useBouncyCastle() {
        return useBouncyCastle;
    }

    public static void useBouncyCastle(boolean useBouncyCastle2) {
        useBouncyCastle = useBouncyCastle2;
    }

    public static ApkMeta getMetaInfo(String apkFilePath) throws IOException {
        ApkFile apkFile = new ApkFile(apkFilePath);
        Throwable th = null;
        try {
            ApkMeta apkMeta = apkFile.getApkMeta();
            if (apkFile != null) {
                if (0 != 0) {
                    try {
                        apkFile.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    apkFile.close();
                }
            }
            return apkMeta;
        } catch (Throwable th3) {
            if (apkFile != null) {
                if (0 != 0) {
                    try {
                        apkFile.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    apkFile.close();
                }
            }
            throw th3;
        }
    }

    public static ApkMeta getMetaInfo(File file) throws IOException {
        ApkFile apkFile = new ApkFile(file);
        Throwable th = null;
        try {
            ApkMeta apkMeta = apkFile.getApkMeta();
            if (apkFile != null) {
                if (0 != 0) {
                    try {
                        apkFile.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    apkFile.close();
                }
            }
            return apkMeta;
        } catch (Throwable th3) {
            if (apkFile != null) {
                if (0 != 0) {
                    try {
                        apkFile.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    apkFile.close();
                }
            }
            throw th3;
        }
    }

    public static ApkMeta getMetaInfo(byte[] apkData) throws IOException {
        ByteArrayApkFile apkFile = new ByteArrayApkFile(apkData);
        Throwable th = null;
        try {
            ApkMeta apkMeta = apkFile.getApkMeta();
            if (apkFile != null) {
                if (0 != 0) {
                    try {
                        apkFile.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    apkFile.close();
                }
            }
            return apkMeta;
        } catch (Throwable th3) {
            if (apkFile != null) {
                if (0 != 0) {
                    try {
                        apkFile.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    apkFile.close();
                }
            }
            throw th3;
        }
    }

    public static ApkMeta getMetaInfo(String apkFilePath, Locale locale) throws IOException {
        ApkFile apkFile = new ApkFile(apkFilePath);
        Throwable th = null;
        try {
            try {
                apkFile.setPreferredLocale(locale);
                ApkMeta apkMeta = apkFile.getApkMeta();
                if (apkFile != null) {
                    if (0 != 0) {
                        try {
                            apkFile.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        apkFile.close();
                    }
                }
                return apkMeta;
            } finally {
            }
        } catch (Throwable th3) {
            if (apkFile != null) {
                if (th != null) {
                    try {
                        apkFile.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    apkFile.close();
                }
            }
            throw th3;
        }
    }

    public static ApkMeta getMetaInfo(File file, Locale locale) throws IOException {
        ApkFile apkFile = new ApkFile(file);
        Throwable th = null;
        try {
            try {
                apkFile.setPreferredLocale(locale);
                ApkMeta apkMeta = apkFile.getApkMeta();
                if (apkFile != null) {
                    if (0 != 0) {
                        try {
                            apkFile.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        apkFile.close();
                    }
                }
                return apkMeta;
            } finally {
            }
        } catch (Throwable th3) {
            if (apkFile != null) {
                if (th != null) {
                    try {
                        apkFile.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    apkFile.close();
                }
            }
            throw th3;
        }
    }

    public static ApkMeta getMetaInfo(byte[] apkData, Locale locale) throws IOException {
        ByteArrayApkFile apkFile = new ByteArrayApkFile(apkData);
        Throwable th = null;
        try {
            try {
                apkFile.setPreferredLocale(locale);
                ApkMeta apkMeta = apkFile.getApkMeta();
                if (apkFile != null) {
                    if (0 != 0) {
                        try {
                            apkFile.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        apkFile.close();
                    }
                }
                return apkMeta;
            } finally {
            }
        } catch (Throwable th3) {
            if (apkFile != null) {
                if (th != null) {
                    try {
                        apkFile.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    apkFile.close();
                }
            }
            throw th3;
        }
    }

    public static String getManifestXml(String apkFilePath) throws IOException {
        ApkFile apkFile = new ApkFile(apkFilePath);
        Throwable th = null;
        try {
            String manifestXml = apkFile.getManifestXml();
            if (apkFile != null) {
                if (0 != 0) {
                    try {
                        apkFile.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    apkFile.close();
                }
            }
            return manifestXml;
        } catch (Throwable th3) {
            if (apkFile != null) {
                if (0 != 0) {
                    try {
                        apkFile.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    apkFile.close();
                }
            }
            throw th3;
        }
    }

    public static String getManifestXml(File file) throws IOException {
        ApkFile apkFile = new ApkFile(file);
        Throwable th = null;
        try {
            String manifestXml = apkFile.getManifestXml();
            if (apkFile != null) {
                if (0 != 0) {
                    try {
                        apkFile.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    apkFile.close();
                }
            }
            return manifestXml;
        } catch (Throwable th3) {
            if (apkFile != null) {
                if (0 != 0) {
                    try {
                        apkFile.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    apkFile.close();
                }
            }
            throw th3;
        }
    }

    public static String getManifestXml(byte[] apkData) throws IOException {
        ByteArrayApkFile apkFile = new ByteArrayApkFile(apkData);
        Throwable th = null;
        try {
            String manifestXml = apkFile.getManifestXml();
            if (apkFile != null) {
                if (0 != 0) {
                    try {
                        apkFile.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    apkFile.close();
                }
            }
            return manifestXml;
        } catch (Throwable th3) {
            if (apkFile != null) {
                if (0 != 0) {
                    try {
                        apkFile.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    apkFile.close();
                }
            }
            throw th3;
        }
    }

    public static String getManifestXml(String apkFilePath, Locale locale) throws IOException {
        ApkFile apkFile = new ApkFile(apkFilePath);
        Throwable th = null;
        try {
            try {
                apkFile.setPreferredLocale(locale);
                String manifestXml = apkFile.getManifestXml();
                if (apkFile != null) {
                    if (0 != 0) {
                        try {
                            apkFile.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        apkFile.close();
                    }
                }
                return manifestXml;
            } finally {
            }
        } catch (Throwable th3) {
            if (apkFile != null) {
                if (th != null) {
                    try {
                        apkFile.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    apkFile.close();
                }
            }
            throw th3;
        }
    }

    public static String getManifestXml(File file, Locale locale) throws IOException {
        ApkFile apkFile = new ApkFile(file);
        Throwable th = null;
        try {
            try {
                apkFile.setPreferredLocale(locale);
                String manifestXml = apkFile.getManifestXml();
                if (apkFile != null) {
                    if (0 != 0) {
                        try {
                            apkFile.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        apkFile.close();
                    }
                }
                return manifestXml;
            } finally {
            }
        } catch (Throwable th3) {
            if (apkFile != null) {
                if (th != null) {
                    try {
                        apkFile.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    apkFile.close();
                }
            }
            throw th3;
        }
    }

    public static String getManifestXml(byte[] apkData, Locale locale) throws IOException {
        ByteArrayApkFile apkFile = new ByteArrayApkFile(apkData);
        Throwable th = null;
        try {
            try {
                apkFile.setPreferredLocale(locale);
                String manifestXml = apkFile.getManifestXml();
                if (apkFile != null) {
                    if (0 != 0) {
                        try {
                            apkFile.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        apkFile.close();
                    }
                }
                return manifestXml;
            } finally {
            }
        } catch (Throwable th3) {
            if (apkFile != null) {
                if (th != null) {
                    try {
                        apkFile.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    apkFile.close();
                }
            }
            throw th3;
        }
    }
}
