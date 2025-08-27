package org.apache.xmlbeans.impl.repackage;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/repackage/Repackage.class */
public class Repackage {
    private File _sourceBase;
    private File _targetBase;
    private List _fromPackages;
    private List _toPackages;
    private Pattern _packagePattern;
    private Repackager _repackager;
    private Map _movedDirs;
    private List _moveAlongFiles;
    private int _skippedFiles;

    public static void main(String[] args) throws Exception {
        new Repackage(args).repackage();
    }

    private Repackage(String[] args) {
        String sourceDir = null;
        String targetDir = null;
        String repackageSpec = null;
        boolean failure = false;
        int i = 0;
        while (i < args.length) {
            if (args[i].equals("-repackage") && i + 1 < args.length) {
                i++;
                repackageSpec = args[i];
            } else if (args[i].equals("-f") && i + 1 < args.length) {
                i++;
                sourceDir = args[i];
            } else if (args[i].equals("-t") && i + 1 < args.length) {
                i++;
                targetDir = args[i];
            } else {
                failure = true;
            }
            i++;
        }
        if (!failure && repackageSpec != null) {
            if (!((sourceDir == null) ^ (targetDir == null))) {
                this._repackager = new Repackager(repackageSpec);
                if (sourceDir == null || targetDir == null) {
                    return;
                }
                this._sourceBase = new File(sourceDir);
                this._targetBase = new File(targetDir);
                return;
            }
        }
        throw new RuntimeException("Usage: repackage -repackage [spec] [ -f [sourcedir] -t [targetdir] ]");
    }

    public void repackage() throws Exception {
        if (this._sourceBase == null || this._targetBase == null) {
            System.out.println(this._repackager.repackage(readInputStream(System.in)).toString());
            return;
        }
        this._fromPackages = this._repackager.getFromPackages();
        this._toPackages = this._repackager.getToPackages();
        this._packagePattern = Pattern.compile("^\\s*package\\s+((?:\\w|\\.)*)\\s*;", 8);
        this._moveAlongFiles = new ArrayList();
        this._movedDirs = new HashMap();
        this._targetBase.mkdirs();
        ArrayList files = new ArrayList();
        fillFiles(files, this._sourceBase);
        System.out.println("Repackaging " + files.size() + " files ...");
        int prefixLength = this._sourceBase.getCanonicalPath().length();
        for (int i = 0; i < files.size(); i++) {
            File from = (File) files.get(i);
            String name = from.getCanonicalPath().substring(prefixLength + 1);
            repackageFile(name);
        }
        finishMovingFiles();
        if (this._skippedFiles > 0) {
            System.out.println("Skipped " + this._skippedFiles + " unmodified files.");
        }
    }

    private boolean fileIsUnchanged(String name) {
        File sourceFile = new File(this._sourceBase, name);
        File targetFile = new File(this._targetBase, name);
        return sourceFile.lastModified() < targetFile.lastModified();
    }

    public void repackageFile(String name) throws IOException {
        if (name.endsWith(".java")) {
            repackageJavaFile(name);
            return;
        }
        if (name.endsWith(".xsdconfig") || name.endsWith(".xml") || name.endsWith(".g")) {
            repackageNonJavaFile(name);
        } else if (name.startsWith("bin" + File.separatorChar)) {
            repackageNonJavaFile(name);
        } else {
            moveAlongWithJavaFiles(name);
        }
    }

    public void moveAlongWithJavaFiles(String name) {
        this._moveAlongFiles.add(name);
    }

    public void finishMovingFiles() throws IOException {
        for (String name : this._moveAlongFiles) {
            String toName = name;
            String srcDir = Repackager.dirForPath(name);
            String toDir = (String) this._movedDirs.get(srcDir);
            if (toDir != null) {
                toName = new File(toDir, new File(name).getName()).toString();
            }
            if (name.endsWith(".html")) {
                repackageNonJavaFile(name, toName);
            } else {
                justMoveNonJavaFile(name, toName);
            }
        }
    }

    public void repackageNonJavaFile(String name) throws IOException {
        File sourceFile = new File(this._sourceBase, name);
        File targetFile = new File(this._targetBase, name);
        if (sourceFile.lastModified() < targetFile.lastModified()) {
            this._skippedFiles++;
        } else {
            writeFile(targetFile, this._repackager.repackage(readFile(sourceFile)));
        }
    }

    public void repackageNonJavaFile(String sourceName, String targetName) throws IOException {
        File sourceFile = new File(this._sourceBase, sourceName);
        File targetFile = new File(this._targetBase, targetName);
        if (sourceFile.lastModified() < targetFile.lastModified()) {
            this._skippedFiles++;
        } else {
            writeFile(targetFile, this._repackager.repackage(readFile(sourceFile)));
        }
    }

    public void justMoveNonJavaFile(String sourceName, String targetName) throws IOException {
        File sourceFile = new File(this._sourceBase, sourceName);
        File targetFile = new File(this._targetBase, targetName);
        if (sourceFile.lastModified() < targetFile.lastModified()) {
            this._skippedFiles++;
        } else {
            copyFile(sourceFile, targetFile);
        }
    }

    public void repackageJavaFile(String name) throws IOException {
        boolean swapped;
        File sourceFile = new File(this._sourceBase, name);
        StringBuffer sb = readFile(sourceFile);
        Matcher packageMatcher = this._packagePattern.matcher(sb);
        if (packageMatcher.find()) {
            String pkg = packageMatcher.group(1);
            int pkgStart = packageMatcher.start(1);
            int pkgEnd = packageMatcher.end(1);
            if (packageMatcher.find()) {
                throw new RuntimeException("Two package specifications found: " + name);
            }
            List filePath = Repackager.splitPath(name, File.separatorChar);
            String srcDir = Repackager.dirForPath(name);
            do {
                swapped = false;
                for (int i = 1; i < filePath.size(); i++) {
                    String spec1 = (String) filePath.get(i - 1);
                    String spec2 = (String) filePath.get(i);
                    if (spec1.indexOf(58) < spec2.indexOf(58)) {
                        filePath.set(i - 1, spec2);
                        filePath.set(i, spec1);
                        swapped = true;
                    }
                }
            } while (swapped);
            List pkgPath = Repackager.splitPath(pkg, '.');
            int f = filePath.size() - 2;
            if (f < 0 || filePath.size() - 1 < pkgPath.size()) {
                throw new RuntimeException("Package spec differs from file path: " + name);
            }
            for (int i2 = pkgPath.size() - 1; i2 >= 0; i2--) {
                if (!pkgPath.get(i2).equals(filePath.get(f))) {
                    throw new RuntimeException("Package spec differs from file path: " + name);
                }
                f--;
            }
            List changeTo = null;
            List changeFrom = null;
            int i3 = 0;
            loop3: while (true) {
                if (i3 >= this._fromPackages.size()) {
                    break;
                }
                List from = (List) this._fromPackages.get(i3);
                if (from.size() <= pkgPath.size()) {
                    for (int j = 0; j < from.size(); j++) {
                        if (!from.get(j).equals(pkgPath.get(j))) {
                            break;
                        }
                    }
                    changeFrom = from;
                    changeTo = (List) this._toPackages.get(i3);
                    break loop3;
                }
                i3++;
            }
            if (changeTo != null) {
                String newPkg = "";
                String newName = "";
                for (int i4 = 0; i4 < changeTo.size(); i4++) {
                    if (i4 > 0) {
                        newPkg = newPkg + ".";
                        newName = newName + File.separatorChar;
                    }
                    newPkg = newPkg + changeTo.get(i4);
                    newName = newName + changeTo.get(i4);
                }
                for (int i5 = (filePath.size() - pkgPath.size()) - 2; i5 >= 0; i5--) {
                    newName = ((String) filePath.get(i5)) + File.separatorChar + newName;
                }
                for (int i6 = changeFrom.size(); i6 < pkgPath.size(); i6++) {
                    newName = newName + File.separatorChar + ((String) pkgPath.get(i6));
                    newPkg = newPkg + '.' + ((String) pkgPath.get(i6));
                }
                String newName2 = newName + File.separatorChar + ((String) filePath.get(filePath.size() - 1));
                sb.replace(pkgStart, pkgEnd, newPkg);
                name = newName2;
                String newDir = Repackager.dirForPath(name);
                if (!srcDir.equals(newDir)) {
                    this._movedDirs.put(srcDir, newDir);
                }
            }
        }
        File targetFile = new File(this._targetBase, name);
        if (sourceFile.lastModified() < targetFile.lastModified()) {
            this._skippedFiles++;
        } else {
            writeFile(new File(this._targetBase, name), this._repackager.repackage(sb));
        }
    }

    void writeFile(File f, StringBuffer chars) throws IOException {
        f.getParentFile().mkdirs();
        OutputStream out = new FileOutputStream(f);
        Writer w = new OutputStreamWriter(out);
        Reader r = new StringReader(chars.toString());
        copy(r, w);
        r.close();
        w.close();
        out.close();
    }

    StringBuffer readFile(File f) throws IOException {
        InputStream in = new FileInputStream(f);
        Reader r = new InputStreamReader(in);
        StringWriter w = new StringWriter();
        copy(r, w);
        w.close();
        r.close();
        in.close();
        return w.getBuffer();
    }

    StringBuffer readInputStream(InputStream is) throws IOException {
        Reader r = new InputStreamReader(is);
        StringWriter w = new StringWriter();
        copy(r, w);
        w.close();
        r.close();
        return w.getBuffer();
    }

    public static void copyFile(File from, File to) throws IOException {
        to.getParentFile().mkdirs();
        FileInputStream in = new FileInputStream(from);
        FileOutputStream out = new FileOutputStream(to);
        copy(in, out);
        out.close();
        in.close();
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[16384];
        while (true) {
            int n = in.read(buffer, 0, buffer.length);
            if (n >= 0) {
                out.write(buffer, 0, n);
            } else {
                return;
            }
        }
    }

    public static void copy(Reader r, Writer w) throws IOException {
        char[] buffer = new char[16384];
        while (true) {
            int n = r.read(buffer, 0, buffer.length);
            if (n >= 0) {
                w.write(buffer, 0, n);
            } else {
                return;
            }
        }
    }

    public void fillFiles(ArrayList files, File file) throws IOException {
        if (!file.isDirectory()) {
            files.add(file);
            return;
        }
        if (file.getName().equals(JsonPOJOBuilder.DEFAULT_BUILD_METHOD) || file.getName().equals("CVS")) {
            return;
        }
        String[] entries = file.list();
        for (String str : entries) {
            fillFiles(files, new File(file, str));
        }
    }

    public void recursiveDelete(File file) throws IOException {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            String[] entries = file.list();
            for (String str : entries) {
                recursiveDelete(new File(file, str));
            }
        }
        file.delete();
    }
}
