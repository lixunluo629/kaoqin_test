package org.hyperic.sigar.shell;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Iterator;
import org.hyperic.sigar.SigarLoader;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/shell/FileCompleter.class */
public class FileCompleter extends CollectionCompleter implements FilenameFilter {
    private static final String HOME = System.getProperty("user.home");
    private String name;

    public FileCompleter() {
    }

    public FileCompleter(ShellBase shell) {
        super(shell);
    }

    public static String expand(String name) {
        if (name.startsWith("~")) {
            return new StringBuffer().append(HOME).append(name.substring(1, name.length())).toString();
        }
        return name;
    }

    @Override // java.io.FilenameFilter
    public boolean accept(File dir, String name) {
        if (name.equals(".") || name.equals("..")) {
            return false;
        }
        return name.startsWith(this.name);
    }

    @Override // org.hyperic.sigar.shell.CollectionCompleter
    public Iterator getIterator() {
        return null;
    }

    private String appendSep(String name) {
        if (name.endsWith(File.separator)) {
            return name;
        }
        return new StringBuffer().append(name).append(File.separator).toString();
    }

    private boolean isDotFile(File file) {
        return file.getName().equals(".") && file.getParentFile() != null;
    }

    @Override // org.hyperic.sigar.shell.CollectionCompleter, org.hyperic.sigar.util.GetlineCompleter
    public String complete(String line) {
        File dir;
        String[] list;
        String fileName = line;
        boolean isHome = false;
        if (line.length() == 0) {
            return appendSep(".");
        }
        if (fileName.startsWith("~")) {
            isHome = true;
            fileName = expand(fileName);
        }
        File file = new File(fileName);
        if (file.exists() && !isDotFile(file)) {
            if (file.isDirectory()) {
                this.name = null;
                dir = file;
                if (!fileName.endsWith(File.separator)) {
                    return new StringBuffer().append(line).append(File.separator).toString();
                }
            } else {
                return line;
            }
        } else {
            this.name = file.getName();
            dir = file.getParentFile();
            if (dir == null) {
                if (SigarLoader.IS_WIN32 && line.length() == 1 && Character.isLetter(line.charAt(0))) {
                    return new StringBuffer().append(line).append(":\\").toString();
                }
                return line;
            }
            if (!dir.exists() || !dir.isDirectory()) {
                return line;
            }
        }
        if (this.name == null) {
            list = dir.list();
        } else {
            list = dir.list(this);
        }
        if (list.length == 1) {
            String fileName2 = new StringBuffer().append(appendSep(dir.toString())).append(list[0]).toString();
            if (new File(fileName2).isDirectory()) {
                fileName2 = appendSep(fileName2);
            }
            if (isHome) {
                return new StringBuffer().append("~").append(fileName2.substring(HOME.length(), fileName2.length())).toString();
            }
            return fileName2;
        }
        String partial = displayPossible(list);
        if (partial != null) {
            return new StringBuffer().append(appendSep(dir.toString())).append(partial).toString();
        }
        return line;
    }

    public static void main(String[] args) throws Exception {
        String line = new FileCompleter().complete(args[0]);
        System.out.println(new StringBuffer().append("\nsigar> '").append(line).append("'").toString());
    }
}
