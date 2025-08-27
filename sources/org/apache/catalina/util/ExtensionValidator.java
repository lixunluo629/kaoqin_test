package org.apache.catalina.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import org.apache.catalina.Context;
import org.apache.catalina.WebResource;
import org.apache.catalina.WebResourceRoot;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/util/ExtensionValidator.class */
public final class ExtensionValidator {
    private static final Log log = LogFactory.getLog((Class<?>) ExtensionValidator.class);
    private static final StringManager sm = StringManager.getManager("org.apache.catalina.util");
    private static volatile ArrayList<Extension> containerAvailableExtensions = null;
    private static final ArrayList<ManifestResource> containerManifestResources = new ArrayList<>();

    static {
        String systemClasspath = System.getProperty("java.class.path");
        StringTokenizer strTok = new StringTokenizer(systemClasspath, File.pathSeparator);
        while (strTok.hasMoreTokens()) {
            String classpathItem = strTok.nextToken();
            if (classpathItem.toLowerCase(Locale.ENGLISH).endsWith(".jar")) {
                File item = new File(classpathItem);
                if (item.isFile()) {
                    try {
                        addSystemResource(item);
                    } catch (IOException e) {
                        log.error(sm.getString("extensionValidator.failload", item), e);
                    }
                }
            }
        }
        addFolderList("java.ext.dirs");
    }

    public static synchronized boolean validateApplication(WebResourceRoot resources, Context context) throws IOException {
        String appName = context.getName();
        ArrayList<ManifestResource> appManifestResources = new ArrayList<>();
        WebResource resource = resources.getResource("/META-INF/MANIFEST.MF");
        if (resource.isFile()) {
            InputStream inputStream = resource.getInputStream();
            Throwable th = null;
            try {
                try {
                    Manifest manifest = new Manifest(inputStream);
                    ManifestResource mre = new ManifestResource(sm.getString("extensionValidator.web-application-manifest"), manifest, 2);
                    appManifestResources.add(mre);
                    if (inputStream != null) {
                        if (0 != 0) {
                            try {
                                inputStream.close();
                            } catch (Throwable x2) {
                                th.addSuppressed(x2);
                            }
                        } else {
                            inputStream.close();
                        }
                    }
                } finally {
                }
            } catch (Throwable th2) {
                if (inputStream != null) {
                    if (th != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable x22) {
                            th.addSuppressed(x22);
                        }
                    } else {
                        inputStream.close();
                    }
                }
                throw th2;
            }
        }
        WebResource[] manifestResources = resources.getClassLoaderResources("/META-INF/MANIFEST.MF");
        for (WebResource manifestResource : manifestResources) {
            if (manifestResource.isFile()) {
                String jarName = manifestResource.getURL().toExternalForm();
                Manifest jmanifest = manifestResource.getManifest();
                if (jmanifest != null) {
                    ManifestResource mre2 = new ManifestResource(jarName, jmanifest, 3);
                    appManifestResources.add(mre2);
                }
            }
        }
        return validateManifestResources(appName, appManifestResources);
    }

    public static void addSystemResource(File jarFile) throws IOException {
        InputStream is = new FileInputStream(jarFile);
        Throwable th = null;
        try {
            Manifest manifest = getManifest(is);
            if (manifest != null) {
                ManifestResource mre = new ManifestResource(jarFile.getAbsolutePath(), manifest, 1);
                containerManifestResources.add(mre);
            }
            if (is != null) {
                if (0 != 0) {
                    try {
                        is.close();
                        return;
                    } catch (Throwable x2) {
                        th.addSuppressed(x2);
                        return;
                    }
                }
                is.close();
            }
        } catch (Throwable th2) {
            if (is != null) {
                if (0 != 0) {
                    try {
                        is.close();
                    } catch (Throwable x22) {
                        th.addSuppressed(x22);
                    }
                } else {
                    is.close();
                }
            }
            throw th2;
        }
    }

    private static boolean validateManifestResources(String appName, ArrayList<ManifestResource> resources) {
        boolean passes = true;
        int failureCount = 0;
        ArrayList<Extension> availableExtensions = null;
        Iterator i$ = resources.iterator();
        while (i$.hasNext()) {
            ManifestResource mre = i$.next();
            ArrayList<Extension> requiredList = mre.getRequiredExtensions();
            if (requiredList != null) {
                if (availableExtensions == null) {
                    availableExtensions = buildAvailableExtensionsList(resources);
                }
                if (containerAvailableExtensions == null) {
                    containerAvailableExtensions = buildAvailableExtensionsList(containerManifestResources);
                }
                Iterator i$2 = requiredList.iterator();
                while (i$2.hasNext()) {
                    Extension requiredExt = i$2.next();
                    boolean found = false;
                    if (availableExtensions != null) {
                        Iterator i$3 = availableExtensions.iterator();
                        while (true) {
                            if (!i$3.hasNext()) {
                                break;
                            }
                            Extension targetExt = i$3.next();
                            if (targetExt.isCompatibleWith(requiredExt)) {
                                requiredExt.setFulfilled(true);
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found && containerAvailableExtensions != null) {
                        Iterator i$4 = containerAvailableExtensions.iterator();
                        while (true) {
                            if (!i$4.hasNext()) {
                                break;
                            }
                            Extension targetExt2 = i$4.next();
                            if (targetExt2.isCompatibleWith(requiredExt)) {
                                requiredExt.setFulfilled(true);
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        log.info(sm.getString("extensionValidator.extension-not-found-error", appName, mre.getResourceName(), requiredExt.getExtensionName()));
                        passes = false;
                        failureCount++;
                    }
                }
            }
        }
        if (!passes) {
            log.info(sm.getString("extensionValidator.extension-validation-error", appName, failureCount + ""));
        }
        return passes;
    }

    private static ArrayList<Extension> buildAvailableExtensionsList(ArrayList<ManifestResource> resources) {
        ArrayList<Extension> availableList = null;
        Iterator i$ = resources.iterator();
        while (i$.hasNext()) {
            ManifestResource mre = i$.next();
            ArrayList<Extension> list = mre.getAvailableExtensions();
            if (list != null) {
                Iterator i$2 = list.iterator();
                while (i$2.hasNext()) {
                    Extension ext = i$2.next();
                    if (availableList == null) {
                        availableList = new ArrayList<>();
                        availableList.add(ext);
                    } else {
                        availableList.add(ext);
                    }
                }
            }
        }
        return availableList;
    }

    private static Manifest getManifest(InputStream inStream) throws IOException {
        JarInputStream jin = new JarInputStream(inStream);
        Throwable th = null;
        try {
            try {
                Manifest manifest = jin.getManifest();
                if (jin != null) {
                    if (0 != 0) {
                        try {
                            jin.close();
                        } catch (Throwable x2) {
                            th.addSuppressed(x2);
                        }
                    } else {
                        jin.close();
                    }
                }
                return manifest;
            } finally {
            }
        } catch (Throwable th2) {
            if (jin != null) {
                if (th != null) {
                    try {
                        jin.close();
                    } catch (Throwable x22) {
                        th.addSuppressed(x22);
                    }
                } else {
                    jin.close();
                }
            }
            throw th2;
        }
    }

    private static void addFolderList(String property) {
        File[] files;
        String extensionsDir = System.getProperty(property);
        if (extensionsDir != null) {
            StringTokenizer extensionsTok = new StringTokenizer(extensionsDir, File.pathSeparator);
            while (extensionsTok.hasMoreTokens()) {
                File targetDir = new File(extensionsTok.nextToken());
                if (targetDir.isDirectory() && (files = targetDir.listFiles()) != null) {
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].getName().toLowerCase(Locale.ENGLISH).endsWith(".jar") && files[i].isFile()) {
                            try {
                                addSystemResource(files[i]);
                            } catch (IOException e) {
                                log.error(sm.getString("extensionValidator.failload", files[i]), e);
                            }
                        }
                    }
                }
            }
        }
    }
}
