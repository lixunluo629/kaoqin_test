package org.aspectj.weaver.bcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.tools.Trace;
import org.aspectj.weaver.tools.TraceFactory;
import org.springframework.util.ClassUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/ClassPathManager.class */
public class ClassPathManager {
    private static Trace trace = TraceFactory.getTraceFactory().getTrace(ClassPathManager.class);
    private List<Entry> entries;
    private List<ZipFile> openArchives = new ArrayList();
    private static int maxOpenArchives;
    private static final int MAXOPEN_DEFAULT = 1000;

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/ClassPathManager$ClassFile.class */
    public static abstract class ClassFile {
        public abstract InputStream getInputStream() throws IOException;

        public abstract String getPath();

        public abstract void close();
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/ClassPathManager$Entry.class */
    public static abstract class Entry {
        public abstract ClassFile find(String str) throws IOException;
    }

    static {
        maxOpenArchives = -1;
        String openzipsString = getSystemPropertyWithoutSecurityException("org.aspectj.weaver.openarchives", Integer.toString(1000));
        maxOpenArchives = Integer.parseInt(openzipsString);
        if (maxOpenArchives < 20) {
            maxOpenArchives = 1000;
        }
    }

    public ClassPathManager(List<String> classpath, IMessageHandler handler) {
        if (trace.isTraceEnabled()) {
            Trace trace2 = trace;
            Object[] objArr = new Object[2];
            objArr[0] = classpath == null ? "null" : classpath.toString();
            objArr[1] = handler;
            trace2.enter("<init>", (Object) this, objArr);
        }
        this.entries = new ArrayList();
        for (String classpathEntry : classpath) {
            addPath(classpathEntry, handler);
        }
        if (trace.isTraceEnabled()) {
            trace.exit("<init>");
        }
    }

    protected ClassPathManager() {
    }

    public void addPath(String name, IMessageHandler handler) {
        File f = new File(name);
        String lc = name.toLowerCase();
        if (!f.isDirectory()) {
            if (!f.isFile()) {
                if (!lc.endsWith(".jar") || lc.endsWith(".zip")) {
                    MessageUtil.info(handler, WeaverMessages.format(WeaverMessages.ZIPFILE_ENTRY_MISSING, name));
                    return;
                } else {
                    MessageUtil.info(handler, WeaverMessages.format(WeaverMessages.DIRECTORY_ENTRY_MISSING, name));
                    return;
                }
            }
            try {
                this.entries.add(new ZipFileEntry(f));
                return;
            } catch (IOException ioe) {
                MessageUtil.warn(handler, WeaverMessages.format(WeaverMessages.ZIPFILE_ENTRY_INVALID, name, ioe.getMessage()));
                return;
            }
        }
        this.entries.add(new DirEntry(f));
    }

    public ClassFile find(UnresolvedType type) {
        ClassFile ret;
        if (trace.isTraceEnabled()) {
            trace.enter("find", this, type);
        }
        String name = type.getName();
        Iterator<Entry> i = this.entries.iterator();
        while (i.hasNext()) {
            Entry entry = i.next();
            try {
                ret = entry.find(name);
                if (trace.isTraceEnabled()) {
                    trace.event("searching for " + type + " in " + entry.toString());
                }
            } catch (IOException ioe) {
                if (trace.isTraceEnabled()) {
                    trace.error("Removing classpath entry for " + entry, ioe);
                }
                i.remove();
            }
            if (ret != null) {
                if (trace.isTraceEnabled()) {
                    trace.exit("find", ret);
                }
                return ret;
            }
            continue;
        }
        if (trace.isTraceEnabled()) {
            trace.exit("find", (Throwable) null);
            return null;
        }
        return null;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        boolean start = true;
        Iterator<Entry> i = this.entries.iterator();
        while (i.hasNext()) {
            if (start) {
                start = false;
            } else {
                buf.append(File.pathSeparator);
            }
            buf.append(i.next());
        }
        return buf.toString();
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/ClassPathManager$FileClassFile.class */
    private static class FileClassFile extends ClassFile {
        private File file;
        private FileInputStream fis;

        public FileClassFile(File file) {
            this.file = file;
        }

        @Override // org.aspectj.weaver.bcel.ClassPathManager.ClassFile
        public InputStream getInputStream() throws IOException {
            this.fis = new FileInputStream(this.file);
            return this.fis;
        }

        @Override // org.aspectj.weaver.bcel.ClassPathManager.ClassFile
        public void close() {
            try {
                try {
                    if (this.fis != null) {
                        this.fis.close();
                    }
                } catch (IOException ioe) {
                    throw new BCException("Can't close class file : " + this.file.getName(), ioe);
                }
            } finally {
                this.fis = null;
            }
        }

        @Override // org.aspectj.weaver.bcel.ClassPathManager.ClassFile
        public String getPath() {
            return this.file.getPath();
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/ClassPathManager$DirEntry.class */
    public class DirEntry extends Entry {
        private String dirPath;

        public DirEntry(File dir) {
            this.dirPath = dir.getPath();
        }

        public DirEntry(String dirPath) {
            this.dirPath = dirPath;
        }

        @Override // org.aspectj.weaver.bcel.ClassPathManager.Entry
        public ClassFile find(String name) {
            File f = new File(this.dirPath + File.separator + name.replace('.', File.separatorChar) + ClassUtils.CLASS_FILE_SUFFIX);
            if (f.isFile()) {
                return new FileClassFile(f);
            }
            return null;
        }

        public String toString() {
            return this.dirPath;
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/ClassPathManager$ZipEntryClassFile.class */
    private static class ZipEntryClassFile extends ClassFile {
        private ZipEntry entry;
        private ZipFileEntry zipFile;
        private InputStream is;

        public ZipEntryClassFile(ZipFileEntry zipFile, ZipEntry entry) {
            this.zipFile = zipFile;
            this.entry = entry;
        }

        @Override // org.aspectj.weaver.bcel.ClassPathManager.ClassFile
        public InputStream getInputStream() throws IOException {
            this.is = this.zipFile.getZipFile().getInputStream(this.entry);
            return this.is;
        }

        @Override // org.aspectj.weaver.bcel.ClassPathManager.ClassFile
        public void close() {
            try {
                if (this.is != null) {
                    this.is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.is = null;
            }
        }

        @Override // org.aspectj.weaver.bcel.ClassPathManager.ClassFile
        public String getPath() {
            return this.entry.getName();
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/ClassPathManager$ZipFileEntry.class */
    public class ZipFileEntry extends Entry {
        private File file;
        private ZipFile zipFile;

        public ZipFileEntry(File file) throws IOException {
            this.file = file;
        }

        public ZipFileEntry(ZipFile zipFile) {
            this.zipFile = zipFile;
        }

        public ZipFile getZipFile() {
            return this.zipFile;
        }

        @Override // org.aspectj.weaver.bcel.ClassPathManager.Entry
        public ClassFile find(String name) throws IOException {
            ensureOpen();
            String key = name.replace('.', '/') + ClassUtils.CLASS_FILE_SUFFIX;
            ZipEntry entry = this.zipFile.getEntry(key);
            if (entry != null) {
                return new ZipEntryClassFile(this, entry);
            }
            return null;
        }

        public List<ZipEntryClassFile> getAllClassFiles() throws IOException {
            ensureOpen();
            List<ZipEntryClassFile> ret = new ArrayList<>();
            Enumeration<? extends ZipEntry> e = this.zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();
                String name = entry.getName();
                if (ClassPathManager.hasClassExtension(name)) {
                    ret.add(new ZipEntryClassFile(this, entry));
                }
            }
            return ret;
        }

        private void ensureOpen() throws IOException {
            if (this.zipFile == null || !ClassPathManager.this.openArchives.contains(this.zipFile) || !isReallyOpen()) {
                if (ClassPathManager.this.openArchives.size() >= ClassPathManager.maxOpenArchives) {
                    closeSomeArchives(ClassPathManager.this.openArchives.size() / 10);
                }
                this.zipFile = new ZipFile(this.file);
                if (isReallyOpen()) {
                    ClassPathManager.this.openArchives.add(this.zipFile);
                    return;
                }
                throw new FileNotFoundException("Can't open archive: " + this.file.getName() + " (size() check failed)");
            }
        }

        private boolean isReallyOpen() {
            try {
                this.zipFile.size();
                return true;
            } catch (IllegalStateException e) {
                return false;
            }
        }

        public void closeSomeArchives(int n) throws IOException {
            for (int i = n - 1; i >= 0; i--) {
                ZipFile zf = (ZipFile) ClassPathManager.this.openArchives.get(i);
                try {
                    zf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ClassPathManager.this.openArchives.remove(i);
            }
        }

        public void close() {
            if (this.zipFile != null) {
                try {
                    try {
                        ClassPathManager.this.openArchives.remove(this.zipFile);
                        this.zipFile.close();
                        this.zipFile = null;
                    } catch (IOException ioe) {
                        throw new BCException("Can't close archive: " + this.file.getName(), ioe);
                    }
                } catch (Throwable th) {
                    this.zipFile = null;
                    throw th;
                }
            }
        }

        public String toString() {
            return this.file.getName();
        }
    }

    static boolean hasClassExtension(String name) {
        return name.toLowerCase().endsWith(ClassUtils.CLASS_FILE_SUFFIX);
    }

    public void closeArchives() {
        for (Entry entry : this.entries) {
            if (entry instanceof ZipFileEntry) {
                ((ZipFileEntry) entry).close();
            }
            this.openArchives.clear();
        }
    }

    private static String getSystemPropertyWithoutSecurityException(String aPropertyName, String aDefaultValue) {
        try {
            return System.getProperty(aPropertyName, aDefaultValue);
        } catch (SecurityException e) {
            return aDefaultValue;
        }
    }
}
