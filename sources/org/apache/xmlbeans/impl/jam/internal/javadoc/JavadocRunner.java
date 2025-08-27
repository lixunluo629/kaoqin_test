package org.apache.xmlbeans.impl.jam.internal.javadoc;

import com.sun.javadoc.Doclet;
import com.sun.javadoc.RootDoc;
import com.sun.tools.javadoc.Main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.xmlbeans.impl.jam.provider.JamLogger;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/javadoc/JavadocRunner.class */
public class JavadocRunner extends Doclet {
    private static final String JAVADOC_RUNNER_150 = "org.apache.xmlbeans.impl.jam.internal.javadoc.JavadocRunner_150";

    public static JavadocRunner newInstance() throws ClassNotFoundException {
        try {
            Class.forName("com.sun.javadoc.AnnotationDesc");
            try {
                Class onefive = Class.forName(JAVADOC_RUNNER_150);
                return (JavadocRunner) onefive.newInstance();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                return new JavadocRunner();
            }
        } catch (ClassNotFoundException e2) {
            return new JavadocRunner();
        }
    }

    synchronized RootDoc run(File[] files, PrintWriter out, String sourcePath, String classPath, String[] javadocArgs, JamLogger logger) throws IOException {
        PrintWriter spewWriter;
        if (files == null || files.length == 0) {
            throw new FileNotFoundException("No input files found.");
        }
        List argList = new ArrayList();
        if (javadocArgs != null) {
            argList.addAll(Arrays.asList(javadocArgs));
        }
        argList.add("-private");
        if (sourcePath != null) {
            argList.add("-sourcepath");
            argList.add(sourcePath);
        }
        if (classPath != null) {
            argList.add("-classpath");
            argList.add(classPath);
            argList.add("-docletpath");
            argList.add(classPath);
        }
        for (int i = 0; i < files.length; i++) {
            argList.add(files[i].toString());
            if (out != null) {
                out.println(files[i].toString());
            }
        }
        String[] args = new String[argList.size()];
        argList.toArray(args);
        StringWriter spew = null;
        if (out == null) {
            StringWriter stringWriter = new StringWriter();
            spew = stringWriter;
            spewWriter = new PrintWriter(stringWriter);
        } else {
            spewWriter = out;
        }
        ClassLoader originalCCL = Thread.currentThread().getContextClassLoader();
        try {
            try {
                JavadocResults.prepare();
                if (logger.isVerbose(this)) {
                    logger.verbose("Invoking javadoc.  Command line equivalent is: ");
                    StringWriter sw = new StringWriter();
                    sw.write("javadoc ");
                    for (String str : args) {
                        sw.write("'");
                        sw.write(str);
                        sw.write("' ");
                    }
                    logger.verbose("  " + sw.toString());
                }
                int result = Main.execute("JAM", spewWriter, spewWriter, spewWriter, getClass().getName(), args);
                RootDoc root = JavadocResults.getRoot();
                if (result != 0 || root == null) {
                    spewWriter.flush();
                    if (JavadocClassloadingException.isClassloadingProblemPresent()) {
                        throw new JavadocClassloadingException();
                    }
                    throw new RuntimeException("Unknown javadoc problem: result=" + result + ", root=" + root + ":\n" + (spew == null ? "" : spew.toString()));
                }
                Thread.currentThread().setContextClassLoader(originalCCL);
                return root;
            } catch (RuntimeException e) {
                throw e;
            }
        } catch (Throwable th) {
            Thread.currentThread().setContextClassLoader(originalCCL);
            throw th;
        }
    }

    public static boolean start(RootDoc root) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        JavadocResults.setRoot(root);
        return true;
    }
}
