package org.aspectj.util;

import com.mysql.jdbc.util.ServerController;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.httpclient.cookie.Cookie2;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/FileUtil.class */
public class FileUtil {
    public static final File DEFAULT_PARENT = new File(".");
    public static final List<String> SOURCE_SUFFIXES = Collections.unmodifiableList(Arrays.asList(".java", ".aj"));
    public static final FileFilter ZIP_FILTER = new FileFilter() { // from class: org.aspectj.util.FileUtil.1
        @Override // java.io.FileFilter
        public boolean accept(File file) {
            return FileUtil.isZipFile(file);
        }

        public String toString() {
            return "ZIP_FILTER";
        }
    };
    static final int[] INT_RA = new int[0];
    public static final FileFilter ALL = new FileFilter() { // from class: org.aspectj.util.FileUtil.2
        @Override // java.io.FileFilter
        public boolean accept(File f) {
            return true;
        }
    };
    public static final FileFilter DIRS_AND_WRITABLE_CLASSES = new FileFilter() { // from class: org.aspectj.util.FileUtil.3
        @Override // java.io.FileFilter
        public boolean accept(File file) {
            return null != file && (file.isDirectory() || (file.canWrite() && file.getName().toLowerCase().endsWith(ClassUtils.CLASS_FILE_SUFFIX)));
        }
    };
    private static final boolean PERMIT_CVS;
    public static final FileFilter aspectjSourceFileFilter;
    static final String FILECHARS = "abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    static {
        String name = FileUtil.class.getName() + ".PERMIT_CVS";
        PERMIT_CVS = LangUtil.getBoolean(name, false);
        aspectjSourceFileFilter = new FileFilter() { // from class: org.aspectj.util.FileUtil.5
            @Override // java.io.FileFilter
            public boolean accept(File pathname) {
                String name2 = pathname.getName().toLowerCase();
                return name2.endsWith(".java") || name2.endsWith(".aj");
            }
        };
    }

    public static boolean isZipFile(File file) {
        if (null != file) {
            try {
                if (new ZipFile(file) != null) {
                    return true;
                }
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    public static int zipSuffixLength(File file) {
        if (null == file) {
            return 0;
        }
        return zipSuffixLength(file.getPath());
    }

    public static int zipSuffixLength(String path) {
        if (null != path && 4 < path.length()) {
            String test = path.substring(path.length() - 4).toLowerCase();
            if (".zip".equals(test) || ".jar".equals(test)) {
                return 4;
            }
            return 0;
        }
        return 0;
    }

    public static boolean hasSourceSuffix(File file) {
        return null != file && hasSourceSuffix(file.getPath());
    }

    public static boolean hasSourceSuffix(String path) {
        return (null == path || 0 == sourceSuffixLength(path)) ? false : true;
    }

    public static int sourceSuffixLength(File file) {
        if (null == file) {
            return 0;
        }
        return sourceSuffixLength(file.getPath());
    }

    public static int sourceSuffixLength(String path) {
        if (LangUtil.isEmpty(path)) {
            return 0;
        }
        for (String suffix : SOURCE_SUFFIXES) {
            if (path.endsWith(suffix) || path.toLowerCase().endsWith(suffix)) {
                return suffix.length();
            }
        }
        return 0;
    }

    public static boolean canReadDir(File dir) {
        return null != dir && dir.canRead() && dir.isDirectory();
    }

    public static boolean canReadFile(File file) {
        return null != file && file.canRead() && file.isFile();
    }

    public static boolean canWriteDir(File dir) {
        return null != dir && dir.canWrite() && dir.isDirectory();
    }

    public static boolean canWriteFile(File file) {
        return null != file && file.canWrite() && file.isFile();
    }

    public static void throwIaxUnlessCanReadDir(File dir, String label) {
        if (!canReadDir(dir)) {
            throw new IllegalArgumentException(label + " not readable dir: " + dir);
        }
    }

    public static void throwIaxUnlessCanWriteFile(File file, String label) {
        if (!canWriteFile(file)) {
            throw new IllegalArgumentException(label + " not writable file: " + file);
        }
    }

    public static void throwIaxUnlessCanWriteDir(File dir, String label) {
        if (!canWriteDir(dir)) {
            throw new IllegalArgumentException(label + " not writable dir: " + dir);
        }
    }

    public static String[] getPaths(File[] files) {
        if (null == files || 0 == files.length) {
            return new String[0];
        }
        String[] result = new String[files.length];
        for (int i = 0; i < result.length; i++) {
            if (null != files[i]) {
                result[i] = files[i].getPath();
            }
        }
        return result;
    }

    public static String[] getPaths(List<File> files) {
        int size = null == files ? 0 : files.size();
        if (0 == size) {
            return new String[0];
        }
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            File file = files.get(i);
            if (null != file) {
                result[i] = file.getPath();
            }
        }
        return result;
    }

    public static String fileToClassName(File basedir, File classFile) {
        int loc;
        LangUtil.throwIaxIfNull(classFile, "classFile");
        String classFilePath = normalizedPath(classFile);
        if (!classFilePath.endsWith(ClassUtils.CLASS_FILE_SUFFIX)) {
            String m = classFile + " does not end with .class";
            throw new IllegalArgumentException(m);
        }
        String classFilePath2 = classFilePath.substring(0, classFilePath.length() - 6);
        if (null != basedir) {
            String basePath = normalizedPath(basedir);
            if (!classFilePath2.startsWith(basePath)) {
                String m2 = classFile + " does not start with " + basedir;
                throw new IllegalArgumentException(m2);
            }
            classFilePath2 = classFilePath2.substring(basePath.length() + 1);
        } else {
            String[] suffixes = {"com", "org", "java", "javax"};
            boolean found = false;
            for (int i = 0; !found && i < suffixes.length; i++) {
                int loc2 = classFilePath2.indexOf(suffixes[i] + "/");
                if (0 == loc2 || (-1 != loc2 && '/' == classFilePath2.charAt(loc2 - 1))) {
                    classFilePath2 = classFilePath2.substring(loc2);
                    found = true;
                }
            }
            if (!found && -1 != (loc = classFilePath2.lastIndexOf("/"))) {
                classFilePath2 = classFilePath2.substring(loc + 1);
            }
        }
        return classFilePath2.replace('/', '.');
    }

    public static String normalizedPath(File file, File basedir) {
        String filePath = normalizedPath(file);
        if (null != basedir) {
            String basePath = normalizedPath(basedir);
            if (filePath.startsWith(basePath)) {
                filePath = filePath.substring(basePath.length());
                if (filePath.startsWith("/")) {
                    filePath = filePath.substring(1);
                }
            }
        }
        return filePath;
    }

    public static String flatten(File[] files, String infix) {
        if (LangUtil.isEmpty(files)) {
            return "";
        }
        return flatten(getPaths(files), infix);
    }

    public static String flatten(String[] paths, String infix) {
        if (null == infix) {
            infix = File.pathSeparator;
        }
        StringBuffer result = new StringBuffer();
        boolean first = true;
        for (String path : paths) {
            if (null != path) {
                if (first) {
                    first = false;
                } else {
                    result.append(infix);
                }
                result.append(path);
            }
        }
        return result.toString();
    }

    public static String normalizedPath(File file) {
        return null == file ? "" : weakNormalize(file.getAbsolutePath());
    }

    public static String weakNormalize(String path) {
        if (null != path) {
            path = path.replace('\\', '/').trim();
        }
        return path;
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x0043 A[EXC_TOP_SPLITTER, PHI: r7
  0x0043: PHI (r7v1 'path' java.lang.String) = (r7v0 'path' java.lang.String), (r7v3 'path' java.lang.String) binds: [B:15:0x0028, B:20:0x003d] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.io.File getBestFile(java.lang.String[] r4) {
        /*
            r0 = 0
            r1 = r4
            if (r0 != r1) goto L7
            r0 = 0
            return r0
        L7:
            r0 = 0
            r5 = r0
            r0 = 0
            r6 = r0
        Lb:
            r0 = 0
            r1 = r5
            if (r0 != r1) goto L6e
            r0 = r6
            r1 = r4
            int r1 = r1.length
            if (r0 >= r1) goto L6e
            r0 = r4
            r1 = r6
            r0 = r0[r1]
            r7 = r0
            r0 = 0
            r1 = r7
            if (r0 != r1) goto L22
            goto L68
        L22:
            r0 = r7
            java.lang.String r1 = "sp:"
            boolean r0 = r0.startsWith(r1)
            if (r0 == 0) goto L43
            r0 = r7
            r1 = 3
            java.lang.String r0 = r0.substring(r1)     // Catch: java.lang.Throwable -> L37
            java.lang.String r0 = java.lang.System.getProperty(r0)     // Catch: java.lang.Throwable -> L37
            r7 = r0
            goto L3b
        L37:
            r8 = move-exception
            r0 = 0
            r7 = r0
        L3b:
            r0 = 0
            r1 = r7
            if (r0 != r1) goto L43
            goto L68
        L43:
            java.io.File r0 = new java.io.File     // Catch: java.lang.Throwable -> L66
            r1 = r0
            r2 = r7
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L66
            r8 = r0
            r0 = r8
            boolean r0 = r0.exists()     // Catch: java.lang.Throwable -> L66
            if (r0 == 0) goto L63
            r0 = r8
            boolean r0 = r0.canRead()     // Catch: java.lang.Throwable -> L66
            if (r0 == 0) goto L63
            r0 = r8
            java.io.File r0 = getBestFile(r0)     // Catch: java.lang.Throwable -> L66
            r5 = r0
        L63:
            goto L68
        L66:
            r8 = move-exception
        L68:
            int r6 = r6 + 1
            goto Lb
        L6e:
            r0 = r5
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.aspectj.util.FileUtil.getBestFile(java.lang.String[]):java.io.File");
    }

    public static File getBestFile(File file) {
        LangUtil.throwIaxIfNull(file, "file");
        if (file.exists()) {
            try {
                return file.getCanonicalFile();
            } catch (IOException e) {
                return file.getAbsoluteFile();
            }
        }
        return file;
    }

    public static String getBestPath(File file) {
        LangUtil.throwIaxIfNull(file, "file");
        if (file.exists()) {
            try {
                return file.getCanonicalPath();
            } catch (IOException e) {
                return file.getAbsolutePath();
            }
        }
        return file.getPath();
    }

    public static String[] getAbsolutePaths(File[] files) {
        if (null == files || 0 == files.length) {
            return new String[0];
        }
        String[] result = new String[files.length];
        for (int i = 0; i < result.length; i++) {
            if (null != files[i]) {
                result[i] = files[i].getAbsolutePath();
            }
        }
        return result;
    }

    public static int deleteContents(File dir) {
        return deleteContents(dir, ALL);
    }

    public static int deleteContents(File dir, FileFilter filter) {
        return deleteContents(dir, filter, true);
    }

    public static int deleteContents(File dir, FileFilter filter, boolean deleteEmptyDirs) {
        if (null == dir) {
            throw new IllegalArgumentException("null dir");
        }
        if (!dir.exists() || !dir.canWrite()) {
            return 0;
        }
        if (!dir.isDirectory()) {
            dir.delete();
            return 1;
        }
        String[] fromFiles = dir.list();
        if (fromFiles == null) {
            return 0;
        }
        int result = 0;
        for (String string : fromFiles) {
            File file = new File(dir, string);
            if (null == filter || filter.accept(file)) {
                if (file.isDirectory()) {
                    result += deleteContents(file, filter, deleteEmptyDirs);
                    String[] fileContent = file.list();
                    if (deleteEmptyDirs && fileContent != null && 0 == fileContent.length) {
                        file.delete();
                    }
                } else {
                    file.delete();
                    result++;
                }
            }
        }
        return result;
    }

    public static int copyDir(File fromDir, File toDir) throws IOException {
        return copyDir(fromDir, toDir, null, null);
    }

    public static int copyDir(File fromDir, File toDir, String fromSuffix, String toSuffix) throws IOException {
        return copyDir(fromDir, toDir, fromSuffix, toSuffix, (FileFilter) null);
    }

    public static int copyDir(File fromDir, File toDir, final String fromSuffix, String toSuffix, FileFilter delegate) throws IOException {
        String[] fromFiles;
        if (null == fromDir || !fromDir.canRead()) {
            return 0;
        }
        boolean haveSuffix = null != fromSuffix && 0 < fromSuffix.length();
        int slen = !haveSuffix ? 0 : fromSuffix.length();
        if (!toDir.exists()) {
            toDir.mkdirs();
        }
        if (!haveSuffix) {
            fromFiles = fromDir.list();
        } else {
            FilenameFilter filter = new FilenameFilter() { // from class: org.aspectj.util.FileUtil.4
                @Override // java.io.FilenameFilter
                public boolean accept(File dir, String name) {
                    return new File(dir, name).isDirectory() || name.endsWith(fromSuffix);
                }
            };
            fromFiles = fromDir.list(filter);
        }
        int result = 0;
        int MAX = null == fromFiles ? 0 : fromFiles.length;
        for (int i = 0; i < MAX; i++) {
            String filename = fromFiles[i];
            File fromFile = new File(fromDir, filename);
            if (fromFile.canRead()) {
                if (fromFile.isDirectory()) {
                    result += copyDir(fromFile, new File(toDir, filename), fromSuffix, toSuffix, delegate);
                } else if (fromFile.isFile()) {
                    if (haveSuffix) {
                        filename = filename.substring(0, filename.length() - slen);
                    }
                    if (null != toSuffix) {
                        filename = filename + toSuffix;
                    }
                    File targetFile = new File(toDir, filename);
                    if (null == delegate || delegate.accept(targetFile)) {
                        copyFile(fromFile, targetFile);
                    }
                    result++;
                }
            }
        }
        return result;
    }

    public static String[] listFiles(File srcDir) {
        ArrayList<String> result = new ArrayList<>();
        if (null != srcDir && srcDir.canRead()) {
            listFiles(srcDir, (String) null, result);
        }
        return (String[]) result.toArray(new String[0]);
    }

    public static File[] listFiles(File srcDir, FileFilter fileFilter) {
        ArrayList<File> result = new ArrayList<>();
        if (null != srcDir && srcDir.canRead()) {
            listFiles(srcDir, result, fileFilter);
        }
        return (File[]) result.toArray(new File[result.size()]);
    }

    public static List<File> listClassFiles(File dir) {
        ArrayList<File> result = new ArrayList<>();
        if (null != dir && dir.canRead()) {
            listClassFiles(dir, result);
        }
        return result;
    }

    public static File[] getBaseDirFiles(File basedir, String[] paths) {
        return getBaseDirFiles(basedir, paths, (String[]) null);
    }

    public static File[] getBaseDirFiles(File basedir, String[] paths, String[] suffixes) {
        File[] result;
        LangUtil.throwIaxIfNull(basedir, ServerController.BASEDIR_KEY);
        LangUtil.throwIaxIfNull(paths, "paths");
        if (!LangUtil.isEmpty(suffixes)) {
            ArrayList<File> list = new ArrayList<>();
            for (int i = 0; i < paths.length; i++) {
                String path = paths[i];
                int j = 0;
                while (true) {
                    if (j >= suffixes.length) {
                        break;
                    }
                    if (!path.endsWith(suffixes[j])) {
                        j++;
                    } else {
                        list.add(new File(basedir, paths[i]));
                        break;
                    }
                }
            }
            result = (File[]) list.toArray(new File[0]);
        } else {
            result = new File[paths.length];
            for (int i2 = 0; i2 < result.length; i2++) {
                result[i2] = newFile(basedir, paths[i2]);
            }
        }
        return result;
    }

    private static File newFile(File dir, String path) {
        if (".".equals(path)) {
            return dir;
        }
        if ("..".equals(path)) {
            File parentDir = dir.getParentFile();
            if (null != parentDir) {
                return parentDir;
            }
            return new File(dir, "..");
        }
        return new File(dir, path);
    }

    public static File[] copyFiles(File srcDir, String[] relativePaths, File destDir) throws IOException, IllegalArgumentException {
        throwIaxUnlessCanReadDir(srcDir, "srcDir");
        throwIaxUnlessCanWriteDir(destDir, "destDir");
        LangUtil.throwIaxIfNull(relativePaths, "relativePaths");
        File[] result = new File[relativePaths.length];
        for (int i = 0; i < relativePaths.length; i++) {
            String path = relativePaths[i];
            LangUtil.throwIaxIfNull(path, "relativePaths-entry");
            File src = newFile(srcDir, relativePaths[i]);
            File dest = newFile(destDir, path);
            File destParent = dest.getParentFile();
            if (!destParent.exists()) {
                destParent.mkdirs();
            }
            LangUtil.throwIaxIfFalse(canWriteDir(destParent), "dest-entry-parent");
            copyFile(src, dest);
            result[i] = dest;
        }
        return result;
    }

    public static void copyFile(File fromFile, File toFile) throws IOException {
        LangUtil.throwIaxIfNull(fromFile, "fromFile");
        LangUtil.throwIaxIfNull(toFile, "toFile");
        LangUtil.throwIaxIfFalse(!toFile.equals(fromFile), "same file");
        if (toFile.isDirectory()) {
            throwIaxUnlessCanWriteDir(toFile, "toFile");
            if (fromFile.isFile()) {
                File targFile = new File(toFile, fromFile.getName());
                copyValidFiles(fromFile, targFile);
                return;
            } else if (fromFile.isDirectory()) {
                copyDir(fromFile, toFile);
                return;
            } else {
                LangUtil.throwIaxIfFalse(false, "not dir or file: " + fromFile);
                return;
            }
        }
        if (toFile.isFile()) {
            if (fromFile.isDirectory()) {
                LangUtil.throwIaxIfFalse(false, "can't copy to file dir: " + fromFile);
            }
            copyValidFiles(fromFile, toFile);
            return;
        }
        ensureParentWritable(toFile);
        if (fromFile.isFile()) {
            copyValidFiles(fromFile, toFile);
        } else {
            if (fromFile.isDirectory()) {
                toFile.mkdirs();
                throwIaxUnlessCanWriteDir(toFile, "toFile");
                copyDir(fromFile, toFile);
                return;
            }
            LangUtil.throwIaxIfFalse(false, "not dir or file: " + fromFile);
        }
    }

    public static File ensureParentWritable(File path) {
        LangUtil.throwIaxIfNull(path, Cookie2.PATH);
        File pathParent = path.getParentFile();
        if (null == pathParent) {
            pathParent = DEFAULT_PARENT;
        }
        if (!pathParent.canWrite()) {
            pathParent.mkdirs();
        }
        throwIaxUnlessCanWriteDir(pathParent, "pathParent");
        return pathParent;
    }

    public static void copyValidFiles(File fromFile, File toFile) throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(fromFile);
            out = new FileOutputStream(toFile);
            copyStream(in, out);
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (Throwable th) {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            throw th;
        }
    }

    public static void copyStream(DataInputStream in, PrintStream out) throws IOException {
        LangUtil.throwIaxIfNull(in, "in");
        LangUtil.throwIaxIfNull(in, "out");
        while (true) {
            String s = in.readLine();
            if (null != s) {
                out.println(s);
            } else {
                return;
            }
        }
    }

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[4096];
        int i = in.read(buf, 0, 4096);
        while (true) {
            int bytesRead = i;
            if (bytesRead != -1) {
                out.write(buf, 0, bytesRead);
                i = in.read(buf, 0, 4096);
            } else {
                return;
            }
        }
    }

    public static void copyStream(Reader in, Writer out) throws IOException {
        char[] buf = new char[4096];
        int i = in.read(buf, 0, 4096);
        while (true) {
            int bytesRead = i;
            if (bytesRead != -1) {
                out.write(buf, 0, bytesRead);
                i = in.read(buf, 0, 4096);
            } else {
                return;
            }
        }
    }

    public static File makeNewChildDir(File parent, String child) {
        if (null == parent || !parent.canWrite() || !parent.isDirectory()) {
            throw new IllegalArgumentException("bad parent: " + parent);
        }
        if (null == child) {
            child = "makeNewChildDir";
        } else if (!isValidFileName(child)) {
            throw new IllegalArgumentException("bad child: " + child);
        }
        File result = new File(parent, child);
        int safety = 1000;
        String strRandomFileString = randomFileString();
        while (true) {
            String suffix = strRandomFileString;
            safety--;
            if (0 >= safety || !result.exists()) {
                break;
            }
            result = new File(parent, child + suffix);
            strRandomFileString = randomFileString();
        }
        if (result.exists()) {
            System.err.println("exhausted files for child dir in " + parent);
            return null;
        }
        if (result.mkdirs() && result.exists()) {
            return result;
        }
        return null;
    }

    public static File getTempDir(String name) {
        File result;
        if (null == name) {
            name = "FileUtil_getTempDir";
        } else if (!isValidFileName(name)) {
            throw new IllegalArgumentException(" invalid: " + name);
        }
        File tempFile = null;
        try {
            try {
                tempFile = File.createTempFile("ignoreMe", ".txt");
                File tempParent = tempFile.getParentFile();
                result = makeNewChildDir(tempParent, name);
                if (null != tempFile) {
                    tempFile.delete();
                }
            } catch (IOException e) {
                result = makeNewChildDir(new File("."), name);
                if (null != tempFile) {
                    tempFile.delete();
                }
            }
            return result;
        } catch (Throwable th) {
            if (null != tempFile) {
                tempFile.delete();
            }
            throw th;
        }
    }

    public static URL[] getFileURLs(File[] files) {
        if (null == files || 0 == files.length) {
            return new URL[0];
        }
        URL[] result = new URL[files.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = getFileURL(files[i]);
        }
        return result;
    }

    public static URL getFileURL(File file) throws MalformedURLException {
        URL result;
        LangUtil.throwIaxIfNull(file, "file");
        URL result2 = null;
        try {
            result = file.toURL();
        } catch (MalformedURLException e) {
            String m = "Util.makeURL(\"" + file.getPath() + "\" MUE " + e.getMessage();
            System.err.println(m);
        }
        if (null != result) {
            return result;
        }
        String url = ResourceUtils.FILE_URL_PREFIX + file.getAbsolutePath().replace('\\', '/');
        result2 = new URL(url + (file.isDirectory() ? "/" : ""));
        return result2;
    }

    public static String writeAsString(File file, String contents) throws IOException {
        LangUtil.throwIaxIfNull(file, "file");
        if (null == contents) {
            contents = "";
        }
        Writer out = null;
        try {
            try {
                File parentDir = file.getParentFile();
                if (!parentDir.exists() && !parentDir.mkdirs()) {
                    String str = "unable to make parent dir for " + file;
                    if (0 != 0) {
                        try {
                            out.close();
                        } catch (IOException e) {
                        }
                    }
                    return str;
                }
                Reader in = new StringReader(contents);
                Writer out2 = new FileWriter(file);
                copyStream(in, out2);
                if (null != out2) {
                    try {
                        out2.close();
                    } catch (IOException e2) {
                    }
                }
                return null;
            } catch (IOException e3) {
                String str2 = LangUtil.unqualifiedClassName(e3) + " writing " + file + ": " + e3.getMessage();
                if (0 != 0) {
                    try {
                        out.close();
                    } catch (IOException e4) {
                    }
                }
                return str2;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    out.close();
                } catch (IOException e5) {
                }
            }
            throw th;
        }
    }

    public static boolean[] readBooleanArray(DataInputStream s) throws IOException {
        int len = s.readInt();
        boolean[] ret = new boolean[len];
        for (int i = 0; i < len; i++) {
            ret[i] = s.readBoolean();
        }
        return ret;
    }

    public static void writeBooleanArray(boolean[] a, DataOutputStream s) throws IOException {
        int len = a.length;
        s.writeInt(len);
        for (boolean z : a) {
            s.writeBoolean(z);
        }
    }

    public static int[] readIntArray(DataInputStream s) throws IOException {
        int len = s.readInt();
        int[] ret = new int[len];
        for (int i = 0; i < len; i++) {
            ret[i] = s.readInt();
        }
        return ret;
    }

    public static void writeIntArray(int[] a, DataOutputStream s) throws IOException {
        int len = a.length;
        s.writeInt(len);
        for (int i : a) {
            s.writeInt(i);
        }
    }

    public static String[] readStringArray(DataInputStream s) throws IOException {
        int len = s.readInt();
        String[] ret = new String[len];
        for (int i = 0; i < len; i++) {
            ret[i] = s.readUTF();
        }
        return ret;
    }

    public static void writeStringArray(String[] a, DataOutputStream s) throws IOException {
        if (a == null) {
            s.writeInt(0);
            return;
        }
        int len = a.length;
        s.writeInt(len);
        for (String str : a) {
            s.writeUTF(str);
        }
    }

    public static String readAsString(File file) throws IOException {
        BufferedReader r = new BufferedReader(new FileReader(file));
        StringBuffer b = new StringBuffer();
        while (true) {
            int ch2 = r.read();
            if (ch2 != -1) {
                b.append((char) ch2);
            } else {
                r.close();
                return b.toString();
            }
        }
    }

    public static byte[] readAsByteArray(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        byte[] ret = readAsByteArray(in);
        in.close();
        return ret;
    }

    public static byte[] readAsByteArray(InputStream inStream) throws IOException {
        int size = 1024;
        byte[] ba = new byte[1024];
        int readSoFar = 0;
        while (true) {
            int nRead = inStream.read(ba, readSoFar, size - readSoFar);
            if (nRead != -1) {
                readSoFar += nRead;
                if (readSoFar == size) {
                    int newSize = size * 2;
                    byte[] newBa = new byte[newSize];
                    System.arraycopy(ba, 0, newBa, 0, size);
                    ba = newBa;
                    size = newSize;
                }
            } else {
                byte[] newBa2 = new byte[readSoFar];
                System.arraycopy(ba, 0, newBa2, 0, readSoFar);
                return newBa2;
            }
        }
    }

    static String randomFileString() {
        double FILECHARS_length = FILECHARS.length();
        char[] result = new char[6];
        int index = (int) (Math.random() * 6.0d);
        for (int i = 0; i < 6; i++) {
            if (index >= 6) {
                index = 0;
            }
            int i2 = index;
            index++;
            result[i2] = FILECHARS.charAt((int) (Math.random() * FILECHARS_length));
        }
        return new String(result);
    }

    public static InputStream getStreamFromZip(String zipFile, String name) {
        try {
            ZipFile zf = new ZipFile(zipFile);
            ZipEntry entry = zf.getEntry(name);
            return zf.getInputStream(entry);
        } catch (IOException e) {
            return null;
        }
    }

    public static List<String> lineSeek(String sought, List<String> sources, boolean listAll, PrintStream errorSink) {
        if (LangUtil.isEmpty(sought) || LangUtil.isEmpty(sources)) {
            return Collections.emptyList();
        }
        ArrayList<String> result = new ArrayList<>();
        for (String path : sources) {
            String error = lineSeek(sought, path, listAll, result);
            if (null != error && null != errorSink) {
                errorSink.println(error);
            }
        }
        return result;
    }

    public static String lineSeek(String sought, String sourcePath, boolean listAll, ArrayList<String> sink) {
        if (LangUtil.isEmpty(sought) || LangUtil.isEmpty(sourcePath)) {
            return "nothing sought";
        }
        if (LangUtil.isEmpty(sourcePath)) {
            return "no sourcePath";
        }
        File file = new File(sourcePath);
        if (!file.canRead() || !file.isFile()) {
            return "sourcePath not a readable file";
        }
        int lineNum = 0;
        FileReader fin = null;
        try {
            try {
                fin = new FileReader(file);
                BufferedReader reader = new BufferedReader(fin);
                while (true) {
                    String line = reader.readLine();
                    if (null == line) {
                        break;
                    }
                    lineNum++;
                    int loc = line.indexOf(sought);
                    if (-1 != loc) {
                        sink.add(sourcePath + ":" + lineNum + ":" + loc);
                        if (!listAll) {
                            break;
                        }
                    }
                }
                if (null != fin) {
                    try {
                        fin.close();
                    } catch (IOException e) {
                        return null;
                    }
                }
                return null;
            } catch (IOException e2) {
                String str = LangUtil.unqualifiedClassName(e2) + " reading " + sourcePath + ":" + lineNum;
                if (null != fin) {
                    try {
                        fin.close();
                    } catch (IOException e3) {
                        return str;
                    }
                }
                return str;
            }
        } catch (Throwable th) {
            if (null != fin) {
                try {
                    fin.close();
                } catch (IOException e4) {
                    throw th;
                }
            }
            throw th;
        }
    }

    public static BufferedOutputStream makeOutputStream(File file) throws FileNotFoundException {
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        return new BufferedOutputStream(new FileOutputStream(file));
    }

    public static boolean sleepPastFinalModifiedTime(File[] files) {
        if (null == files || 0 == files.length) {
            return true;
        }
        long delayUntil = System.currentTimeMillis();
        for (File file : files) {
            if (null != file && file.exists()) {
                long nextModTime = file.lastModified();
                if (nextModTime > delayUntil) {
                    delayUntil = nextModTime;
                }
            }
        }
        return LangUtil.sleepUntil(delayUntil + 1);
    }

    private static void listClassFiles(File baseDir, ArrayList<File> result) {
        File[] files = baseDir.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                listClassFiles(f, result);
            } else if (f.getName().endsWith(ClassUtils.CLASS_FILE_SUFFIX)) {
                result.add(f);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0057  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void listFiles(java.io.File r4, java.util.ArrayList<java.io.File> r5, java.io.FileFilter r6) {
        /*
            r0 = r4
            java.io.File[] r0 = r0.listFiles()
            r7 = r0
            boolean r0 = org.aspectj.util.FileUtil.PERMIT_CVS
            if (r0 != 0) goto L16
            r0 = r6
            java.io.FileFilter r1 = org.aspectj.util.FileUtil.aspectjSourceFileFilter
            if (r0 != r1) goto L16
            r0 = 1
            goto L17
        L16:
            r0 = 0
        L17:
            r8 = r0
            r0 = 0
            r9 = r0
        L1c:
            r0 = r9
            r1 = r7
            int r1 = r1.length
            if (r0 >= r1) goto L79
            r0 = r7
            r1 = r9
            r0 = r0[r1]
            r10 = r0
            r0 = r10
            boolean r0 = r0.isDirectory()
            if (r0 == 0) goto L61
            r0 = r8
            if (r0 == 0) goto L57
            r0 = r10
            java.lang.String r0 = r0.getName()
            java.lang.String r0 = r0.toLowerCase()
            r11 = r0
            java.lang.String r0 = "cvs"
            r1 = r11
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L73
            java.lang.String r0 = "sccs"
            r1 = r11
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L57
            goto L73
        L57:
            r0 = r10
            r1 = r5
            r2 = r6
            listFiles(r0, r1, r2)
            goto L73
        L61:
            r0 = r6
            r1 = r10
            boolean r0 = r0.accept(r1)
            if (r0 == 0) goto L73
            r0 = r5
            r1 = r10
            boolean r0 = r0.add(r1)
        L73:
            int r9 = r9 + 1
            goto L1c
        L79:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.aspectj.util.FileUtil.listFiles(java.io.File, java.util.ArrayList, java.io.FileFilter):void");
    }

    private static boolean isValidFileName(String input) {
        return null != input && -1 == input.indexOf(File.pathSeparator);
    }

    private static void listFiles(File baseDir, String dir, ArrayList<String> result) {
        String dirPrefix = null == dir ? "" : dir + "/";
        File dirFile = null == dir ? baseDir : new File(baseDir.getPath() + "/" + dir);
        String[] files = dirFile.list();
        for (int i = 0; i < files.length; i++) {
            File f = new File(dirFile, files[i]);
            String path = dirPrefix + files[i];
            if (f.isDirectory()) {
                listFiles(baseDir, path, result);
            } else {
                result.add(path);
            }
        }
    }

    private FileUtil() {
    }

    public static List<String> makeClasspath(URL[] urls) {
        List<String> ret = new LinkedList<>();
        if (urls != null) {
            for (URL url : urls) {
                ret.add(toPathString(url));
            }
        }
        return ret;
    }

    private static String toPathString(URL url) {
        try {
            return url.toURI().getPath();
        } catch (URISyntaxException e) {
            System.err.println("Warning!! Malformed URL may cause problems: " + url);
            return url.getPath();
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/FileUtil$Pipe.class */
    public static class Pipe implements Runnable {
        private final InputStream in;
        private final OutputStream out;
        private final long sleep;
        private ByteArrayOutputStream snoop;
        private long totalWritten;
        private Throwable thrown;
        private boolean halt;
        private final boolean closeInput;
        private final boolean closeOutput;
        private boolean finishStream;
        private boolean done;

        Pipe(InputStream in, OutputStream out) {
            this(in, out, 100L, false, false);
        }

        Pipe(InputStream in, OutputStream out, long sleep, boolean closeInput, boolean closeOutput) {
            LangUtil.throwIaxIfNull(in, "in");
            LangUtil.throwIaxIfNull(out, "out");
            this.in = in;
            this.out = out;
            this.closeInput = closeInput;
            this.closeOutput = closeOutput;
            this.sleep = Math.min(0L, Math.max(60000L, sleep));
        }

        public void setSnoop(ByteArrayOutputStream snoop) {
            this.snoop = snoop;
        }

        @Override // java.lang.Runnable
        public void run() throws IOException {
            this.totalWritten = 0L;
            try {
                if (this.halt) {
                    return;
                }
                try {
                    byte[] buf = new byte[4096];
                    int count = this.in.read(buf, 0, 4096);
                    while (true) {
                        if ((this.halt && this.finishStream && 0 < count) || (!this.halt && -1 != count)) {
                            this.out.write(buf, 0, count);
                            ByteArrayOutputStream mySnoop = this.snoop;
                            if (null != mySnoop) {
                                mySnoop.write(buf, 0, count);
                            }
                            this.totalWritten += count;
                            if (this.halt && !this.finishStream) {
                                break;
                            }
                            if (!this.halt && 0 < this.sleep) {
                                Thread.sleep(this.sleep);
                            }
                            if (this.halt && !this.finishStream) {
                                break;
                            } else {
                                count = this.in.read(buf, 0, 4096);
                            }
                        } else {
                            break;
                        }
                    }
                    this.halt = true;
                    if (this.closeInput) {
                        try {
                            this.in.close();
                        } catch (IOException e) {
                        }
                    }
                    if (this.closeOutput) {
                        try {
                            this.out.close();
                        } catch (IOException e2) {
                        }
                    }
                    this.done = true;
                    completing(this.totalWritten, this.thrown);
                } catch (Throwable e3) {
                    this.thrown = e3;
                    this.halt = true;
                    if (this.closeInput) {
                        try {
                            this.in.close();
                        } catch (IOException e4) {
                        }
                    }
                    if (this.closeOutput) {
                        try {
                            this.out.close();
                        } catch (IOException e5) {
                        }
                    }
                    this.done = true;
                    completing(this.totalWritten, this.thrown);
                }
            } catch (Throwable th) {
                this.halt = true;
                if (this.closeInput) {
                    try {
                        this.in.close();
                    } catch (IOException e6) {
                    }
                }
                if (this.closeOutput) {
                    try {
                        this.out.close();
                    } catch (IOException e7) {
                    }
                }
                this.done = true;
                completing(this.totalWritten, this.thrown);
                throw th;
            }
        }

        public boolean halt(boolean wait, boolean finishStream) throws InterruptedException {
            if (!this.halt) {
                this.halt = true;
            }
            if (wait) {
                while (!this.done) {
                    synchronized (this) {
                        notifyAll();
                    }
                    if (!this.done) {
                        try {
                            Thread.sleep(5L);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
            return this.halt;
        }

        public long totalWritten() {
            return this.totalWritten;
        }

        public Throwable getThrown() {
            return this.thrown;
        }

        protected void completing(long totalWritten, Throwable thrown) {
        }
    }
}
