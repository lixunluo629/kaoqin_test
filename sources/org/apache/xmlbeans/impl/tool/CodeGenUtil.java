package org.apache.xmlbeans.impl.tool;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.xmlbeans.SystemProperties;
import org.hyperic.sigar.win32.Win32;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/CodeGenUtil.class */
public class CodeGenUtil {
    public static String DEFAULT_MEM_START;
    public static String DEFAULT_MEM_MAX;
    public static String DEFAULT_COMPILER;
    public static String DEFAULT_JAR;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CodeGenUtil.class.desiredAssertionStatus();
        DEFAULT_MEM_START = "8m";
        DEFAULT_MEM_MAX = "256m";
        DEFAULT_COMPILER = "javac";
        DEFAULT_JAR = "jar";
    }

    public static URI resolve(URI base, URI child) {
        URI ruri = base.resolve(child);
        if ("file".equals(ruri.getScheme()) && !child.equals(ruri) && base.getPath().startsWith("//") && !ruri.getPath().startsWith("//")) {
            String path = "///".concat(ruri.getPath());
            try {
                ruri = new URI("file", null, path, ruri.getQuery(), ruri.getFragment());
            } catch (URISyntaxException e) {
            }
        }
        return ruri;
    }

    static void addAllJavaFiles(List srcFiles, List args) {
        Iterator i = srcFiles.iterator();
        while (i.hasNext()) {
            File f = (File) i.next();
            if (!f.isDirectory()) {
                args.add(quoteAndEscapeFilename(f.getAbsolutePath()));
            } else {
                List inside = Arrays.asList(f.listFiles(new FileFilter() { // from class: org.apache.xmlbeans.impl.tool.CodeGenUtil.1
                    @Override // java.io.FileFilter
                    public boolean accept(File file) {
                        return (file.isFile() && file.getName().endsWith(".java")) || file.isDirectory();
                    }
                }));
                addAllJavaFiles(inside, args);
            }
        }
    }

    private static String quoteAndEscapeFilename(String filename) {
        if (filename.indexOf(SymbolConstants.SPACE_SYMBOL) < 0) {
            return filename;
        }
        return SymbolConstants.QUOTES_SYMBOL + filename.replaceAll("\\\\", "\\\\\\\\") + SymbolConstants.QUOTES_SYMBOL;
    }

    private static String quoteNoEscapeFilename(String filename) {
        if (filename.indexOf(SymbolConstants.SPACE_SYMBOL) < 0 || File.separatorChar == '/') {
            return filename;
        }
        return SymbolConstants.QUOTES_SYMBOL + filename + SymbolConstants.QUOTES_SYMBOL;
    }

    public static boolean externalCompile(List srcFiles, File outdir, File[] cp, boolean debug) {
        return externalCompile(srcFiles, outdir, cp, debug, DEFAULT_COMPILER, null, DEFAULT_MEM_START, DEFAULT_MEM_MAX, false, false);
    }

    public static boolean externalCompile(List srcFiles, File outdir, File[] cp, boolean debug, String javacPath, String memStart, String memMax, boolean quiet, boolean verbose) {
        return externalCompile(srcFiles, outdir, cp, debug, javacPath, null, memStart, memMax, quiet, verbose);
    }

    public static boolean externalCompile(List srcFiles, File outdir, File[] cp, boolean debug, String javacPath, String genver, String memStart, String memMax, boolean quiet, boolean verbose) throws IOException {
        List args = new ArrayList();
        File javac = findJavaTool(javacPath == null ? DEFAULT_COMPILER : javacPath);
        if (!$assertionsDisabled && !javac.exists()) {
            throw new AssertionError("compiler not found " + javac);
        }
        args.add(javac.getAbsolutePath());
        if (outdir == null) {
            outdir = new File(".");
        } else {
            args.add("-d");
            args.add(quoteAndEscapeFilename(outdir.getAbsolutePath()));
        }
        if (cp == null) {
            cp = systemClasspath();
        }
        if (cp.length > 0) {
            StringBuffer classPath = new StringBuffer();
            classPath.append(outdir.getAbsolutePath());
            for (File file : cp) {
                classPath.append(File.pathSeparator);
                classPath.append(file.getAbsolutePath());
            }
            args.add("-classpath");
            args.add(quoteAndEscapeFilename(classPath.toString()));
        }
        if (genver == null) {
            genver = "1.6";
        }
        args.add("-source");
        args.add(genver);
        args.add("-target");
        args.add(genver);
        args.add(debug ? "-g" : "-g:none");
        if (verbose) {
            args.add("-verbose");
        }
        addAllJavaFiles(srcFiles, args);
        File clFile = null;
        try {
            clFile = File.createTempFile("javac", "");
            FileWriter fw = new FileWriter(clFile);
            Iterator i = args.iterator();
            i.next();
            while (i.hasNext()) {
                String arg = (String) i.next();
                fw.write(arg);
                fw.write(10);
            }
            fw.close();
            List newargs = new ArrayList();
            newargs.add(args.get(0));
            if (memStart != null && memStart.length() != 0) {
                newargs.add("-J-Xms" + memStart);
            }
            if (memMax != null && memMax.length() != 0) {
                newargs.add("-J-Xmx" + memMax);
            }
            newargs.add("@" + clFile.getAbsolutePath());
            args = newargs;
        } catch (Exception e) {
            System.err.println("Could not create command-line file for javac");
        }
        try {
            String[] strArgs = (String[]) args.toArray(new String[args.size()]);
            if (verbose) {
                System.out.print("compile command:");
                for (String str : strArgs) {
                    System.out.print(SymbolConstants.SPACE_SYMBOL + str);
                }
                System.out.println();
            }
            Process proc = Runtime.getRuntime().exec(strArgs);
            StringBuffer errorBuffer = new StringBuffer();
            StringBuffer outputBuffer = new StringBuffer();
            new ThreadedReader(proc.getInputStream(), outputBuffer);
            new ThreadedReader(proc.getErrorStream(), errorBuffer);
            proc.waitFor();
            if (verbose || proc.exitValue() != 0) {
                if (outputBuffer.length() > 0) {
                    System.out.println(outputBuffer.toString());
                    System.out.flush();
                }
                if (errorBuffer.length() > 0) {
                    System.err.println(errorBuffer.toString());
                    System.err.flush();
                }
                if (proc.exitValue() != 0) {
                    return false;
                }
            }
            if (clFile != null) {
                clFile.delete();
                return true;
            }
            return true;
        } catch (Throwable e2) {
            System.err.println(e2.toString());
            System.err.println(e2.getCause());
            e2.printStackTrace(System.err);
            return false;
        }
    }

    public static File[] systemClasspath() {
        List cp = new ArrayList();
        String[] systemcp = SystemProperties.getProperty("java.class.path").split(File.pathSeparator);
        for (String str : systemcp) {
            cp.add(new File(str));
        }
        return (File[]) cp.toArray(new File[cp.size()]);
    }

    public static boolean externalJar(File srcdir, File outfile) {
        return externalJar(srcdir, outfile, DEFAULT_JAR, false, false);
    }

    public static boolean externalJar(File srcdir, File outfile, String jarPath, boolean quiet, boolean verbose) {
        List args = new ArrayList();
        File jar = findJavaTool(jarPath == null ? DEFAULT_JAR : jarPath);
        if (!$assertionsDisabled && !jar.exists()) {
            throw new AssertionError("jar not found " + jar);
        }
        args.add(jar.getAbsolutePath());
        args.add("cf");
        args.add(quoteNoEscapeFilename(outfile.getAbsolutePath()));
        args.add("-C");
        args.add(quoteNoEscapeFilename(srcdir.getAbsolutePath()));
        args.add(".");
        try {
            String[] strArgs = (String[]) args.toArray(new String[args.size()]);
            if (verbose) {
                System.out.print("jar command:");
                for (String str : strArgs) {
                    System.out.print(SymbolConstants.SPACE_SYMBOL + str);
                }
                System.out.println();
            }
            Process proc = Runtime.getRuntime().exec(strArgs);
            StringBuffer errorBuffer = new StringBuffer();
            StringBuffer outputBuffer = new StringBuffer();
            new ThreadedReader(proc.getInputStream(), outputBuffer);
            new ThreadedReader(proc.getErrorStream(), errorBuffer);
            proc.waitFor();
            if (verbose || proc.exitValue() != 0) {
                if (outputBuffer.length() > 0) {
                    System.out.println(outputBuffer.toString());
                    System.out.flush();
                }
                if (errorBuffer.length() > 0) {
                    System.err.println(errorBuffer.toString());
                    System.err.flush();
                }
                if (proc.exitValue() != 0) {
                    return false;
                }
                return true;
            }
            return true;
        } catch (Throwable e) {
            e.printStackTrace(System.err);
            return false;
        }
    }

    private static File findJavaTool(String tool) {
        File toolFile = new File(tool);
        if (toolFile.isFile()) {
            return toolFile;
        }
        File result = new File(tool + Win32.EXE_EXT);
        if (result.isFile()) {
            return result;
        }
        String home = SystemProperties.getProperty("java.home");
        String sep = File.separator;
        File result2 = new File(home + sep + ".." + sep + "bin", tool);
        if (result2.isFile()) {
            return result2;
        }
        File result3 = new File(result2.getPath() + Win32.EXE_EXT);
        if (result3.isFile()) {
            return result3;
        }
        File result4 = new File(home + sep + "bin", tool);
        if (result4.isFile()) {
            return result4;
        }
        File result5 = new File(result4.getPath() + Win32.EXE_EXT);
        if (result5.isFile()) {
            return result5;
        }
        return toolFile;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/CodeGenUtil$ThreadedReader.class */
    private static class ThreadedReader {
        public ThreadedReader(InputStream stream, final StringBuffer output) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            Thread readerThread = new Thread(new Runnable() { // from class: org.apache.xmlbeans.impl.tool.CodeGenUtil.ThreadedReader.1
                @Override // java.lang.Runnable
                public void run() throws IOException {
                    while (true) {
                        try {
                            String s = reader.readLine();
                            if (s != null) {
                                output.append(s + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                            } else {
                                return;
                            }
                        } catch (Exception e) {
                            return;
                        }
                    }
                }
            });
            readerThread.start();
        }
    }
}
