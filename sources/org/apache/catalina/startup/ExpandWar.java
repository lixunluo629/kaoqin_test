package org.apache.catalina.startup;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;
import org.apache.catalina.Host;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/startup/ExpandWar.class */
public class ExpandWar {
    private static final Log log = LogFactory.getLog((Class<?>) ExpandWar.class);
    protected static final StringManager sm = StringManager.getManager(Constants.Package);

    /* JADX WARN: Failed to calculate best type for var: r23v1 ??
    java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "changeArg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:439)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:232)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:212)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:183)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:112)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:83)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:56)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:156)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:133)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:238)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:221)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Failed to calculate best type for var: r23v1 ??
    java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "changeArg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:439)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:232)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:212)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:183)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:112)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:83)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:56)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:145)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:123)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$1(TypeInferenceVisitor.java:101)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:101)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Not initialized variable reg: 23, insn: 0x035c: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r23 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:118:0x035c */
    /* JADX WARN: Type inference failed for: r21v0, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r21v1 */
    /* JADX WARN: Type inference failed for: r21v2 */
    /* JADX WARN: Type inference failed for: r22v4, types: [java.lang.Throwable, java.util.jar.JarFile] */
    /* JADX WARN: Type inference failed for: r23v1, types: [java.lang.Throwable] */
    public static String expand(Host host, URL war, String pathname) throws IOException {
        ?? r23;
        JarURLConnection juc = (JarURLConnection) war.openConnection();
        juc.setUseCaches(false);
        URL jarFileUrl = juc.getJarFileURL();
        URLConnection jfuc = jarFileUrl.openConnection();
        boolean success = false;
        File docBase = new File(host.getAppBaseFile(), pathname);
        File warTracker = new File(host.getAppBaseFile(), pathname + Constants.WarTracker);
        InputStream is = jfuc.getInputStream();
        boolean z = 0;
        try {
            try {
                long warLastModified = jfuc.getLastModified();
                if (is != null) {
                    if (0 != 0) {
                        try {
                            is.close();
                        } catch (Throwable th) {
                            z.addSuppressed(th);
                        }
                    } else {
                        is.close();
                    }
                }
                if (docBase.exists()) {
                    if (!warTracker.exists() || warTracker.lastModified() == warLastModified) {
                        return docBase.getAbsolutePath();
                    }
                    log.info(sm.getString("expandWar.deleteOld", docBase));
                    if (!delete(docBase)) {
                        throw new IOException(sm.getString("expandWar.deleteFailed", docBase));
                    }
                }
                if (!docBase.mkdir() && !docBase.isDirectory()) {
                    throw new IOException(sm.getString("expandWar.createFailed", docBase));
                }
                String canonicalDocBasePrefix = docBase.getCanonicalPath();
                if (!canonicalDocBasePrefix.endsWith(File.separator)) {
                    canonicalDocBasePrefix = canonicalDocBasePrefix + File.separator;
                }
                File warTrackerParent = warTracker.getParentFile();
                if (!warTrackerParent.isDirectory() && !warTrackerParent.mkdirs()) {
                    throw new IOException(sm.getString("expandWar.createFailed", warTrackerParent.getAbsolutePath()));
                }
                try {
                    try {
                        try {
                            JarFile jarFile = juc.getJarFile();
                            Throwable th2 = null;
                            Enumeration<JarEntry> jarEntries = jarFile.entries();
                            while (jarEntries.hasMoreElements()) {
                                JarEntry jarEntry = jarEntries.nextElement();
                                String name = jarEntry.getName();
                                File expandedFile = new File(docBase, name);
                                if (!expandedFile.getCanonicalPath().startsWith(canonicalDocBasePrefix)) {
                                    throw new IllegalArgumentException(sm.getString("expandWar.illegalPath", war, name, expandedFile.getCanonicalPath(), canonicalDocBasePrefix));
                                }
                                int last = name.lastIndexOf(47);
                                if (last >= 0) {
                                    File parent = new File(docBase, name.substring(0, last));
                                    if (!parent.mkdirs() && !parent.isDirectory()) {
                                        throw new IOException(sm.getString("expandWar.createFailed", parent));
                                    }
                                }
                                if (!name.endsWith("/")) {
                                    InputStream input = jarFile.getInputStream(jarEntry);
                                    Throwable th3 = null;
                                    if (null == input) {
                                        throw new ZipException(sm.getString("expandWar.missingJarEntry", jarEntry.getName()));
                                    }
                                    try {
                                        try {
                                            expand(input, expandedFile);
                                            long lastModified = jarEntry.getTime();
                                            if (lastModified != -1 && lastModified != 0) {
                                                expandedFile.setLastModified(lastModified);
                                            }
                                            if (input != null) {
                                                if (0 != 0) {
                                                    try {
                                                        input.close();
                                                    } catch (Throwable x2) {
                                                        th3.addSuppressed(x2);
                                                    }
                                                } else {
                                                    input.close();
                                                }
                                            }
                                        } catch (Throwable th4) {
                                            if (input != null) {
                                                if (th3 != null) {
                                                    try {
                                                        input.close();
                                                    } catch (Throwable x22) {
                                                        th3.addSuppressed(x22);
                                                    }
                                                } else {
                                                    input.close();
                                                }
                                            }
                                            throw th4;
                                        }
                                    } finally {
                                    }
                                }
                            }
                            warTracker.createNewFile();
                            warTracker.setLastModified(warLastModified);
                            success = true;
                            if (jarFile != null) {
                                if (0 != 0) {
                                    try {
                                        jarFile.close();
                                    } catch (Throwable x23) {
                                        th2.addSuppressed(x23);
                                    }
                                } else {
                                    jarFile.close();
                                }
                            }
                            return docBase.getAbsolutePath();
                        } catch (Throwable th5) {
                            if (th != 0) {
                                if (r23 != 0) {
                                    try {
                                        th.close();
                                    } catch (Throwable x24) {
                                        r23.addSuppressed(x24);
                                    }
                                } else {
                                    th.close();
                                }
                            }
                            throw th5;
                        }
                    } finally {
                        if (!success) {
                            deleteDir(docBase);
                        }
                    }
                } catch (IOException e) {
                    throw e;
                }
            } catch (Throwable th6) {
                if (is != null) {
                    if (z) {
                        try {
                            is.close();
                        } catch (Throwable x25) {
                            z.addSuppressed(x25);
                        }
                    } else {
                        is.close();
                    }
                }
                throw th6;
            }
        } finally {
        }
    }

    public static void validate(Host host, URL war, String pathname) throws IOException {
        File docBase = new File(host.getAppBaseFile(), pathname);
        String canonicalDocBasePrefix = docBase.getCanonicalPath();
        if (!canonicalDocBasePrefix.endsWith(File.separator)) {
            canonicalDocBasePrefix = canonicalDocBasePrefix + File.separator;
        }
        JarURLConnection juc = (JarURLConnection) war.openConnection();
        juc.setUseCaches(false);
        try {
            JarFile jarFile = juc.getJarFile();
            Throwable th = null;
            try {
                try {
                    Enumeration<JarEntry> jarEntries = jarFile.entries();
                    while (jarEntries.hasMoreElements()) {
                        JarEntry jarEntry = jarEntries.nextElement();
                        String name = jarEntry.getName();
                        File expandedFile = new File(docBase, name);
                        if (!expandedFile.getCanonicalPath().startsWith(canonicalDocBasePrefix)) {
                            throw new IllegalArgumentException(sm.getString("expandWar.illegalPath", war, name, expandedFile.getCanonicalPath(), canonicalDocBasePrefix));
                        }
                    }
                    if (jarFile != null) {
                        if (0 != 0) {
                            try {
                                jarFile.close();
                            } catch (Throwable x2) {
                                th.addSuppressed(x2);
                            }
                        } else {
                            jarFile.close();
                        }
                    }
                } finally {
                }
            } finally {
            }
        } catch (IOException e) {
            throw e;
        }
    }

    public static boolean copy(File src, File dest) {
        String[] files;
        boolean result = true;
        if (src.isDirectory()) {
            files = src.list();
            result = dest.mkdir();
        } else {
            files = new String[]{""};
        }
        if (files == null) {
            files = new String[0];
        }
        for (int i = 0; i < files.length && result; i++) {
            File fileSrc = new File(src, files[i]);
            File fileDest = new File(dest, files[i]);
            if (fileSrc.isDirectory()) {
                result = copy(fileSrc, fileDest);
            } else {
                try {
                    FileChannel ic = new FileInputStream(fileSrc).getChannel();
                    Throwable th = null;
                    try {
                        try {
                            FileChannel oc = new FileOutputStream(fileDest).getChannel();
                            Throwable th2 = null;
                            try {
                                try {
                                    ic.transferTo(0L, ic.size(), oc);
                                    if (oc != null) {
                                        if (0 != 0) {
                                            try {
                                                oc.close();
                                            } catch (Throwable x2) {
                                                th2.addSuppressed(x2);
                                            }
                                        } else {
                                            oc.close();
                                        }
                                    }
                                    if (ic != null) {
                                        if (0 != 0) {
                                            try {
                                                ic.close();
                                            } catch (Throwable x22) {
                                                th.addSuppressed(x22);
                                            }
                                        } else {
                                            ic.close();
                                        }
                                    }
                                } catch (Throwable th3) {
                                    if (oc != null) {
                                        if (th2 != null) {
                                            try {
                                                oc.close();
                                            } catch (Throwable x23) {
                                                th2.addSuppressed(x23);
                                            }
                                        } else {
                                            oc.close();
                                        }
                                    }
                                    throw th3;
                                }
                            } catch (Throwable th4) {
                                th2 = th4;
                                throw th4;
                            }
                        } finally {
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        throw th5;
                    }
                } catch (IOException e) {
                    log.error(sm.getString("expandWar.copy", fileSrc, fileDest), e);
                    result = false;
                }
            }
        }
        return result;
    }

    public static boolean delete(File dir) {
        return delete(dir, true);
    }

    public static boolean delete(File dir, boolean logFailure) {
        boolean result;
        if (dir.isDirectory()) {
            result = deleteDir(dir, logFailure);
        } else if (dir.exists()) {
            result = dir.delete();
        } else {
            result = true;
        }
        if (logFailure && !result) {
            log.error(sm.getString("expandWar.deleteFailed", dir.getAbsolutePath()));
        }
        return result;
    }

    public static boolean deleteDir(File dir) {
        return deleteDir(dir, true);
    }

    public static boolean deleteDir(File dir, boolean logFailure) {
        boolean result;
        String[] files = dir.list();
        if (files == null) {
            files = new String[0];
        }
        for (String str : files) {
            File file = new File(dir, str);
            if (file.isDirectory()) {
                deleteDir(file, logFailure);
            } else {
                file.delete();
            }
        }
        if (dir.exists()) {
            result = dir.delete();
        } else {
            result = true;
        }
        if (logFailure && !result) {
            log.error(sm.getString("expandWar.deleteFailed", dir.getAbsolutePath()));
        }
        return result;
    }

    private static void expand(InputStream input, File file) throws IOException {
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
        Throwable th = null;
        try {
            try {
                byte[] buffer = new byte[2048];
                while (true) {
                    int n = input.read(buffer);
                    if (n <= 0) {
                        break;
                    } else {
                        output.write(buffer, 0, n);
                    }
                }
                if (output != null) {
                    if (0 != 0) {
                        try {
                            output.close();
                            return;
                        } catch (Throwable x2) {
                            th.addSuppressed(x2);
                            return;
                        }
                    }
                    output.close();
                }
            } catch (Throwable th2) {
                th = th2;
                throw th2;
            }
        } catch (Throwable th3) {
            if (output != null) {
                if (th != null) {
                    try {
                        output.close();
                    } catch (Throwable x22) {
                        th.addSuppressed(x22);
                    }
                } else {
                    output.close();
                }
            }
            throw th3;
        }
    }
}
