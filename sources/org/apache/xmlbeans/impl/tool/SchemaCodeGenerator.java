package org.apache.xmlbeans.impl.tool;

import io.netty.handler.codec.http.multipart.DiskFileUpload;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.poi.util.TempFile;
import org.apache.xmlbeans.Filer;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.SystemProperties;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.repackage.Repackager;
import org.apache.xmlbeans.impl.util.FilerImpl;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/SchemaCodeGenerator.class */
public class SchemaCodeGenerator {
    private static Set deleteFileQueue;
    private static int triesRemaining;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SchemaCodeGenerator.class.desiredAssertionStatus();
        deleteFileQueue = new HashSet();
        triesRemaining = 0;
    }

    public static void saveTypeSystem(SchemaTypeSystem system, File classesDir, File sourceFile, Repackager repackager, XmlOptions options) throws IOException {
        Filer filer = new FilerImpl(classesDir, null, repackager, false, false);
        system.save(filer);
    }

    static void deleteObsoleteFiles(File rootDir, File srcDir, Set seenFiles) {
        if (!rootDir.isDirectory() || !srcDir.isDirectory()) {
            throw new IllegalArgumentException();
        }
        String absolutePath = srcDir.getAbsolutePath();
        if (absolutePath.length() <= 5) {
            return;
        }
        if (absolutePath.startsWith("/home/") && (absolutePath.indexOf("/", 6) >= absolutePath.length() - 1 || absolutePath.indexOf("/", 6) < 0)) {
            return;
        }
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                deleteObsoleteFiles(rootDir, files[i], seenFiles);
            } else if (!seenFiles.contains(files[i])) {
                deleteXmlBeansFile(files[i]);
                deleteDirRecursively(rootDir, files[i].getParentFile());
            }
        }
    }

    private static void deleteXmlBeansFile(File file) {
        if (file.getName().endsWith(".java")) {
            file.delete();
        }
    }

    private static void deleteDirRecursively(File root, File dir) {
        String[] list = dir.list();
        while (true) {
            String[] list2 = list;
            if (list2 != null && list2.length == 0 && !dir.equals(root)) {
                dir.delete();
                dir = dir.getParentFile();
                list = dir.list();
            } else {
                return;
            }
        }
    }

    protected static File createTempDir() throws IOException {
        String string;
        try {
            File tmpDirFile = new File(SystemProperties.getProperty(TempFile.JAVA_IO_TMPDIR));
            tmpDirFile.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        File tmpFile = File.createTempFile("xbean", null);
        String path = tmpFile.getAbsolutePath();
        if (!path.endsWith(DiskFileUpload.postfix)) {
            throw new IOException("Error: createTempFile did not create a file ending with .tmp");
        }
        String path2 = path.substring(0, path.length() - 4);
        File tmpSrcDir = null;
        int count = 0;
        while (true) {
            if (count >= 100) {
                break;
            }
            StringBuilder sbAppend = new StringBuilder().append(path2).append(".d");
            if (count == 0) {
                string = "";
            } else {
                int i = count;
                count++;
                string = Integer.toString(i);
            }
            String name = sbAppend.append(string).toString();
            tmpSrcDir = new File(name);
            if (tmpSrcDir.exists()) {
                count++;
            } else {
                boolean created = tmpSrcDir.mkdirs();
                if (!$assertionsDisabled && !created) {
                    throw new AssertionError("Could not create " + tmpSrcDir.getAbsolutePath());
                }
            }
        }
        tmpFile.deleteOnExit();
        return tmpSrcDir;
    }

    protected static void tryHardToDelete(File dir) {
        tryToDelete(dir);
        if (dir.exists()) {
            tryToDeleteLater(dir);
        }
    }

    private static void tryToDelete(File dir) {
        String[] list;
        if (dir.exists()) {
            if (dir.isDirectory() && (list = dir.list()) != null) {
                for (String str : list) {
                    tryToDelete(new File(dir, str));
                }
            }
            if (!dir.delete()) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean tryNowThatItsLater() {
        List<File> files;
        boolean z;
        synchronized (deleteFileQueue) {
            files = new ArrayList(deleteFileQueue);
            deleteFileQueue.clear();
        }
        ArrayList arrayList = new ArrayList();
        for (File file : files) {
            tryToDelete(file);
            if (file.exists()) {
                arrayList.add(file);
            }
        }
        synchronized (deleteFileQueue) {
            if (triesRemaining > 0) {
                triesRemaining--;
            }
            if (triesRemaining <= 0 || arrayList.size() == 0) {
                triesRemaining = 0;
            } else {
                deleteFileQueue.addAll(arrayList);
            }
            z = triesRemaining <= 0;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void giveUp() {
        synchronized (deleteFileQueue) {
            deleteFileQueue.clear();
            triesRemaining = 0;
        }
    }

    private static void tryToDeleteLater(File dir) {
        synchronized (deleteFileQueue) {
            deleteFileQueue.add(dir);
            if (triesRemaining == 0) {
                new Thread() { // from class: org.apache.xmlbeans.impl.tool.SchemaCodeGenerator.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() throws InterruptedException {
                        while (!SchemaCodeGenerator.tryNowThatItsLater()) {
                            try {
                                Thread.sleep(3000L);
                            } catch (InterruptedException e) {
                                SchemaCodeGenerator.giveUp();
                                return;
                            }
                        }
                    }
                };
            }
            if (triesRemaining < 10) {
                triesRemaining = 10;
            }
        }
    }
}
