package org.apache.catalina.webresources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResource;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.ExpandWar;
import org.apache.catalina.util.IOTools;
import org.apache.tomcat.util.res.StringManager;
import org.apache.tomcat.util.scan.Constants;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/webresources/ExtractingRoot.class */
public class ExtractingRoot extends StandardRoot {
    private static final StringManager sm = StringManager.getManager((Class<?>) ExtractingRoot.class);
    private static final String APPLICATION_JARS_DIR = "application-jars";

    @Override // org.apache.catalina.webresources.StandardRoot
    protected void processWebInfLib() throws LifecycleException, IOException {
        if (!super.isPackedWarFile()) {
            super.processWebInfLib();
            return;
        }
        File expansionTarget = getExpansionTarget();
        if (!expansionTarget.isDirectory() && !expansionTarget.mkdirs()) {
            throw new LifecycleException(sm.getString("extractingRoot.targetFailed", expansionTarget));
        }
        WebResource[] possibleJars = listResources("/WEB-INF/lib", false);
        for (WebResource possibleJar : possibleJars) {
            if (possibleJar.isFile() && possibleJar.getName().endsWith(".jar")) {
                try {
                    File dest = new File(expansionTarget, possibleJar.getName()).getCanonicalFile();
                    InputStream sourceStream = possibleJar.getInputStream();
                    Throwable th = null;
                    try {
                        try {
                            OutputStream destStream = new FileOutputStream(dest);
                            Throwable th2 = null;
                            try {
                                try {
                                    IOTools.flow(sourceStream, destStream);
                                    if (destStream != null) {
                                        if (0 != 0) {
                                            try {
                                                destStream.close();
                                            } catch (Throwable x2) {
                                                th2.addSuppressed(x2);
                                            }
                                        } else {
                                            destStream.close();
                                        }
                                    }
                                    if (sourceStream != null) {
                                        if (0 != 0) {
                                            try {
                                                sourceStream.close();
                                            } catch (Throwable x22) {
                                                th.addSuppressed(x22);
                                            }
                                        } else {
                                            sourceStream.close();
                                        }
                                    }
                                    createWebResourceSet(WebResourceRoot.ResourceSetType.CLASSES_JAR, Constants.WEB_INF_CLASSES, dest.toURI().toURL(), "/");
                                } finally {
                                }
                            } finally {
                            }
                        } finally {
                        }
                    } finally {
                    }
                } catch (IOException ioe) {
                    throw new LifecycleException(sm.getString("extractingRoot.jarFailed", possibleJar.getName()), ioe);
                }
            }
        }
    }

    private File getExpansionTarget() {
        File tmpDir = (File) getContext().getServletContext().getAttribute("javax.servlet.context.tempdir");
        File expansionTarget = new File(tmpDir, APPLICATION_JARS_DIR);
        return expansionTarget;
    }

    @Override // org.apache.catalina.webresources.StandardRoot
    protected boolean isPackedWarFile() {
        return false;
    }

    @Override // org.apache.catalina.webresources.StandardRoot, org.apache.catalina.util.LifecycleBase
    protected void stopInternal() throws LifecycleException {
        super.stopInternal();
        if (super.isPackedWarFile()) {
            File expansionTarget = getExpansionTarget();
            ExpandWar.delete(expansionTarget);
        }
    }
}
