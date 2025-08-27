package org.hyperic.jni;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/* loaded from: sigar-1.6.4.jar:org/hyperic/jni/ArchNameTask.class */
public class ArchNameTask extends Task {
    public void execute() throws NumberFormatException, BuildException {
        String compiler;
        File[] sdks;
        String osArch = System.getProperty("os.arch");
        String osVers = System.getProperty("os.version");
        if (getProject().getProperty("jni.dmalloc") != null) {
            ArchName.useDmalloc = true;
        }
        try {
            String archName = ArchName.getName();
            System.out.println(archName);
            getProject().setProperty("jni.libarch", archName);
            getProject().setProperty("jni.libpre", ArchLoader.getLibraryPrefix());
            getProject().setProperty("jni.libext", ArchLoader.getLibraryExtension());
            if (ArchLoader.IS_WIN32) {
                compiler = "msvc";
            } else if (ArchLoader.IS_HPUX) {
                compiler = "hp";
            } else if (ArchLoader.IS_AIX) {
                compiler = "xlc_r";
            } else {
                compiler = "gcc";
                getProject().setProperty("jni.compiler.isgcc", "true");
            }
            getProject().setProperty("jni.compiler", compiler);
            if (ArchName.is64()) {
                getProject().setProperty("jni.arch64", "true");
                if (ArchLoader.IS_LINUX && !osArch.equals("ia64")) {
                    getProject().setProperty("jni.gccm", "-m64");
                }
            } else if (ArchLoader.IS_LINUX && osArch.equals("s390")) {
                getProject().setProperty("jni.gccm", "-m31");
            }
            if (ArchLoader.IS_DARWIN && (sdks = new File("/Developer/SDKs").listFiles(new FileFilter(this) { // from class: org.hyperic.jni.ArchNameTask.1
                private final ArchNameTask this$0;

                {
                    this.this$0 = this;
                }

                @Override // java.io.FileFilter
                public boolean accept(File file) {
                    String name = file.getName();
                    return name.startsWith("MacOSX10.") && name.endsWith(".sdk");
                }
            })) != null) {
                Arrays.sort(sdks);
                String sdk = getProject().getProperty("uni.sdk");
                String defaultMin = "10.3";
                if (sdk == null) {
                    int ix = sdks.length - 1;
                    sdk = sdks[ix].getPath();
                    if (sdk.indexOf("10.6") != -1 && ix > 0) {
                        sdk = sdks[ix - 1].getPath();
                        defaultMin = "10.5";
                    }
                    getProject().setProperty("uni.sdk", sdk);
                }
                String version = osVers.substring(0, 4);
                int minorVers = Integer.parseInt(osVers.substring(3, 4));
                boolean usingLatestSDK = sdk.indexOf(version) != -1;
                System.out.println(new StringBuffer().append("Using SDK=").append(sdk).toString());
                if (minorVers >= 6 && ArchName.is64() && usingLatestSDK) {
                    getProject().setProperty("jni.cc", "jni-cc");
                    getProject().setProperty("uni.arch", "i386");
                    System.out.println("Note: SDK version does not support ppc64");
                }
                String min = getProject().getProperty("osx.min");
                if (min == null) {
                    min = defaultMin;
                    getProject().setProperty("osx.min", min);
                }
                System.out.println(new StringBuffer().append("Using -mmacosx-version-min=").append(min).toString());
            }
            getProject().setProperty("jni.scmrev", getSourceRevision());
            String home = getProject().getProperty("jni.javahome");
            if (home == null) {
                home = System.getProperty("java.home");
            }
            File dir = new File(home);
            if (!new File(dir, "include").exists()) {
                dir = dir.getParentFile();
            }
            getProject().setProperty("jni.javahome", dir.getPath());
        } catch (ArchNotSupportedException e) {
            System.out.println(e.getMessage());
        }
    }

    private String readLine(String filename) throws IOException {
        Reader reader = null;
        try {
            reader = new FileReader(filename);
            String line = new BufferedReader(reader).readLine();
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
            return line;
        } catch (Exception e2) {
            if (reader != null) {
                try {
                    reader.close();
                    return null;
                } catch (Exception e3) {
                    return null;
                }
            }
            return null;
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e4) {
                }
            }
            throw th;
        }
    }

    private String getSourceRevision() throws IOException {
        String sha1 = getGitSourceRevision();
        if (sha1 == null) {
            return "exported";
        }
        return sha1;
    }

    private String getGitSourceRevision() throws IOException {
        String head;
        String sha1;
        String git = getProject().getProperty("jni.git");
        if (git == null) {
            git = ".git";
        }
        if (new File(git).exists() && (head = readLine(new StringBuffer().append(git).append("/HEAD").toString())) != null) {
            if (head.startsWith("ref: ")) {
                String ref = head.substring("ref: ".length()).trim();
                sha1 = readLine(new StringBuffer().append(git).append("/").append(ref).toString());
            } else {
                sha1 = head;
            }
            return sha1.substring(0, 7);
        }
        return null;
    }
}
