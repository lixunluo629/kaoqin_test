package org.apache.xmlbeans.impl.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/JarHelper.class */
public class JarHelper {
    private static final int BUFFER_SIZE = 2156;
    private byte[] mBuffer = new byte[BUFFER_SIZE];
    private int mByteCount = 0;
    private boolean mVerbose = false;
    private String mDestJarName = "";
    private static final char SEP = '/';

    public void jarDir(File dirOrFile2Jar, File destJar) throws IOException {
        if (dirOrFile2Jar == null || destJar == null) {
            throw new IllegalArgumentException();
        }
        this.mDestJarName = destJar.getCanonicalPath();
        FileOutputStream fout = new FileOutputStream(destJar);
        JarOutputStream jout = new JarOutputStream(fout);
        try {
            try {
                jarDir(dirOrFile2Jar, jout, null);
                jout.close();
                fout.close();
            } catch (IOException ioe) {
                throw ioe;
            }
        } catch (Throwable th) {
            jout.close();
            fout.close();
            throw th;
        }
    }

    public void unjarDir(File jarFile, File destDir) throws IOException {
        FileInputStream fis = new FileInputStream(jarFile);
        unjar(fis, destDir);
    }

    public void unjar(InputStream in, File destDir) throws IOException {
        JarInputStream jis = new JarInputStream(in);
        while (true) {
            JarEntry entry = jis.getNextJarEntry();
            if (entry != null) {
                if (entry.isDirectory()) {
                    File dir = new File(destDir, entry.getName());
                    dir.mkdir();
                    if (entry.getTime() != -1) {
                        dir.setLastModified(entry.getTime());
                    }
                } else {
                    byte[] data = new byte[BUFFER_SIZE];
                    File destFile = new File(destDir, entry.getName());
                    if (this.mVerbose) {
                        System.out.println("unjarring " + destFile + " from " + entry.getName());
                    }
                    FileOutputStream fos = new FileOutputStream(destFile);
                    BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE);
                    while (true) {
                        int count = jis.read(data, 0, BUFFER_SIZE);
                        if (count == -1) {
                            break;
                        } else {
                            dest.write(data, 0, count);
                        }
                    }
                    dest.flush();
                    dest.close();
                    if (entry.getTime() != -1) {
                        destFile.setLastModified(entry.getTime());
                    }
                }
            } else {
                jis.close();
                return;
            }
        }
    }

    public void setVerbose(boolean b) {
        this.mVerbose = b;
    }

    private void jarDir(File dirOrFile2jar, JarOutputStream jos, String path) throws IOException {
        if (this.mVerbose) {
            System.out.println("checking " + dirOrFile2jar);
        }
        if (dirOrFile2jar.isDirectory()) {
            String[] dirList = dirOrFile2jar.list();
            String subPath = path == null ? "" : path + dirOrFile2jar.getName() + '/';
            if (path != null) {
                JarEntry je = new JarEntry(subPath);
                je.setTime(dirOrFile2jar.lastModified());
                jos.putNextEntry(je);
                jos.flush();
                jos.closeEntry();
            }
            for (String str : dirList) {
                File f = new File(dirOrFile2jar, str);
                jarDir(f, jos, subPath);
            }
            return;
        }
        if (dirOrFile2jar.getCanonicalPath().equals(this.mDestJarName)) {
            if (this.mVerbose) {
                System.out.println("skipping " + dirOrFile2jar.getPath());
                return;
            }
            return;
        }
        if (this.mVerbose) {
            System.out.println("adding " + dirOrFile2jar.getPath());
        }
        FileInputStream fis = new FileInputStream(dirOrFile2jar);
        try {
            try {
                JarEntry entry = new JarEntry(path + dirOrFile2jar.getName());
                entry.setTime(dirOrFile2jar.lastModified());
                jos.putNextEntry(entry);
                while (true) {
                    int i = fis.read(this.mBuffer);
                    this.mByteCount = i;
                    if (i != -1) {
                        jos.write(this.mBuffer, 0, this.mByteCount);
                        if (this.mVerbose) {
                            System.out.println("wrote " + this.mByteCount + " bytes");
                        }
                    } else {
                        jos.flush();
                        jos.closeEntry();
                        fis.close();
                        return;
                    }
                }
            } catch (IOException ioe) {
                throw ioe;
            }
        } catch (Throwable th) {
            fis.close();
            throw th;
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: JarHelper jarname.jar directory");
            return;
        }
        JarHelper jarHelper = new JarHelper();
        jarHelper.mVerbose = true;
        File destJar = new File(args[0]);
        File dirOrFile2Jar = new File(args[1]);
        jarHelper.jarDir(dirOrFile2Jar, destJar);
    }
}
