package org.aspectj.apache.bcel.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.springframework.util.ClassUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/util/ClassPath.class */
public class ClassPath implements Serializable {
    private static ClassPath SYSTEM_CLASS_PATH = null;
    private PathEntry[] paths;
    private String class_path;

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/util/ClassPath$ClassFile.class */
    public interface ClassFile {
        InputStream getInputStream() throws IOException;

        String getPath();

        String getBase();

        long getTime();

        long getSize();
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/util/ClassPath$Dir.class */
    private static class Dir extends PathEntry {
        private String dir;

        Dir(String str) {
            super();
            this.dir = str;
        }

        @Override // org.aspectj.apache.bcel.util.ClassPath.PathEntry
        ClassFile getClassFile(String str, String str2) throws IOException {
            final File file = new File(this.dir + File.separatorChar + str.replace('.', File.separatorChar) + str2);
            if (file.exists()) {
                return new ClassFile() { // from class: org.aspectj.apache.bcel.util.ClassPath.Dir.1
                    @Override // org.aspectj.apache.bcel.util.ClassPath.ClassFile
                    public InputStream getInputStream() throws IOException {
                        return new FileInputStream(file);
                    }

                    @Override // org.aspectj.apache.bcel.util.ClassPath.ClassFile
                    public String getPath() {
                        try {
                            return file.getCanonicalPath();
                        } catch (IOException e) {
                            return null;
                        }
                    }

                    @Override // org.aspectj.apache.bcel.util.ClassPath.ClassFile
                    public long getTime() {
                        return file.lastModified();
                    }

                    @Override // org.aspectj.apache.bcel.util.ClassPath.ClassFile
                    public long getSize() {
                        return file.length();
                    }

                    @Override // org.aspectj.apache.bcel.util.ClassPath.ClassFile
                    public String getBase() {
                        return Dir.this.dir;
                    }
                };
            }
            return null;
        }

        public String toString() {
            return this.dir;
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/util/ClassPath$PathEntry.class */
    private static abstract class PathEntry implements Serializable {
        private PathEntry() {
        }

        abstract ClassFile getClassFile(String str, String str2) throws IOException;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/util/ClassPath$Zip.class */
    private static class Zip extends PathEntry {
        private ZipFile zip;

        Zip(ZipFile zipFile) {
            super();
            this.zip = zipFile;
        }

        @Override // org.aspectj.apache.bcel.util.ClassPath.PathEntry
        ClassFile getClassFile(String str, String str2) throws IOException {
            final ZipEntry entry = this.zip.getEntry(str.replace('.', '/') + str2);
            if (entry != null) {
                return new ClassFile() { // from class: org.aspectj.apache.bcel.util.ClassPath.Zip.1
                    @Override // org.aspectj.apache.bcel.util.ClassPath.ClassFile
                    public InputStream getInputStream() throws IOException {
                        return Zip.this.zip.getInputStream(entry);
                    }

                    @Override // org.aspectj.apache.bcel.util.ClassPath.ClassFile
                    public String getPath() {
                        return entry.toString();
                    }

                    @Override // org.aspectj.apache.bcel.util.ClassPath.ClassFile
                    public long getTime() {
                        return entry.getTime();
                    }

                    @Override // org.aspectj.apache.bcel.util.ClassPath.ClassFile
                    public long getSize() {
                        return entry.getSize();
                    }

                    @Override // org.aspectj.apache.bcel.util.ClassPath.ClassFile
                    public String getBase() {
                        return Zip.this.zip.getName();
                    }
                };
            }
            return null;
        }
    }

    public static ClassPath getSystemClassPath() {
        if (SYSTEM_CLASS_PATH == null) {
            SYSTEM_CLASS_PATH = new ClassPath();
        }
        return SYSTEM_CLASS_PATH;
    }

    public ClassPath(String str) {
        this.class_path = str;
        ArrayList arrayList = new ArrayList();
        StringTokenizer stringTokenizer = new StringTokenizer(str, System.getProperty("path.separator"));
        while (stringTokenizer.hasMoreTokens()) {
            String strNextToken = stringTokenizer.nextToken();
            if (!strNextToken.equals("")) {
                File file = new File(strNextToken);
                try {
                    if (file.exists()) {
                        if (file.isDirectory()) {
                            arrayList.add(new Dir(strNextToken));
                        } else {
                            arrayList.add(new Zip(new ZipFile(file)));
                        }
                    }
                } catch (IOException e) {
                    System.err.println("CLASSPATH component " + file + ": " + e);
                }
            }
        }
        this.paths = new PathEntry[arrayList.size()];
        arrayList.toArray(this.paths);
    }

    public ClassPath() {
        this(getClassPath());
    }

    public String toString() {
        return this.class_path;
    }

    public int hashCode() {
        return this.class_path.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof ClassPath) {
            return this.class_path.equals(((ClassPath) obj).class_path);
        }
        return false;
    }

    private static final void getPathComponents(String str, ArrayList<String> arrayList) {
        if (str != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(str, File.pathSeparator);
            while (stringTokenizer.hasMoreTokens()) {
                String strNextToken = stringTokenizer.nextToken();
                if (new File(strNextToken).exists()) {
                    arrayList.add(strNextToken);
                }
            }
        }
    }

    public static final String getClassPath() {
        String property = System.getProperty("java.class.path");
        String property2 = System.getProperty("sun.boot.class.path");
        String property3 = System.getProperty("java.ext.dirs");
        ArrayList arrayList = new ArrayList();
        getPathComponents(property, arrayList);
        getPathComponents(property2, arrayList);
        ArrayList arrayList2 = new ArrayList();
        getPathComponents(property3, arrayList2);
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            String[] list = new File((String) it.next()).list(new FilenameFilter() { // from class: org.aspectj.apache.bcel.util.ClassPath.1
                @Override // java.io.FilenameFilter
                public boolean accept(File file, String str) {
                    String lowerCase = str.toLowerCase();
                    return lowerCase.endsWith(".zip") || lowerCase.endsWith(".jar");
                }
            });
            if (list != null) {
                for (String str : list) {
                    arrayList.add(property3 + File.separatorChar + str);
                }
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            stringBuffer.append((String) it2.next());
            if (it2.hasNext()) {
                stringBuffer.append(File.pathSeparatorChar);
            }
        }
        return stringBuffer.toString().intern();
    }

    public InputStream getInputStream(String str) throws IOException {
        return getInputStream(str, ClassUtils.CLASS_FILE_SUFFIX);
    }

    public InputStream getInputStream(String str, String str2) throws IOException {
        InputStream resourceAsStream = null;
        try {
            resourceAsStream = getClass().getClassLoader().getResourceAsStream(str + str2);
        } catch (Exception e) {
        }
        return resourceAsStream != null ? resourceAsStream : getClassFile(str, str2).getInputStream();
    }

    public ClassFile getClassFile(String str, String str2) throws IOException {
        for (int i = 0; i < this.paths.length; i++) {
            ClassFile classFile = this.paths[i].getClassFile(str, str2);
            if (classFile != null) {
                return classFile;
            }
        }
        throw new IOException("Couldn't find: " + str + str2);
    }

    public ClassFile getClassFile(String str) throws IOException {
        return getClassFile(str, ClassUtils.CLASS_FILE_SUFFIX);
    }

    public byte[] getBytes(String str, String str2) throws IOException {
        InputStream inputStream = getInputStream(str, str2);
        if (inputStream == null) {
            throw new IOException("Couldn't find: " + str + str2);
        }
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        byte[] bArr = new byte[inputStream.available()];
        dataInputStream.readFully(bArr);
        dataInputStream.close();
        inputStream.close();
        return bArr;
    }

    public byte[] getBytes(String str) throws IOException {
        return getBytes(str, ClassUtils.CLASS_FILE_SUFFIX);
    }

    public String getPath(String str) throws IOException {
        int iLastIndexOf = str.lastIndexOf(46);
        String strSubstring = "";
        if (iLastIndexOf > 0) {
            strSubstring = str.substring(iLastIndexOf);
            str = str.substring(0, iLastIndexOf);
        }
        return getPath(str, strSubstring);
    }

    public String getPath(String str, String str2) throws IOException {
        return getClassFile(str, str2).getPath();
    }
}
