package org.apache.catalina.startup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.JarFactory;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/startup/WebappServiceLoader.class */
public class WebappServiceLoader<T> {
    private static final String LIB = "/WEB-INF/lib/";
    private static final String SERVICES = "META-INF/services/";
    private final Context context;
    private final ServletContext servletContext;
    private final Pattern containerSciFilterPattern;

    public WebappServiceLoader(Context context) {
        this.context = context;
        this.servletContext = context.getServletContext();
        String containerSciFilter = context.getContainerSciFilter();
        if (containerSciFilter != null && containerSciFilter.length() > 0) {
            this.containerSciFilterPattern = Pattern.compile(containerSciFilter);
        } else {
            this.containerSciFilterPattern = null;
        }
    }

    public List<T> load(Class<T> serviceType) throws IOException {
        Enumeration<URL> resources;
        URL jarEntryURL;
        String configFile = SERVICES + serviceType.getName();
        LinkedHashSet<String> applicationServicesFound = new LinkedHashSet<>();
        LinkedHashSet<String> containerServicesFound = new LinkedHashSet<>();
        ClassLoader loader = this.servletContext.getClassLoader();
        List<String> orderedLibs = (List) this.servletContext.getAttribute(ServletContext.ORDERED_LIBS);
        if (orderedLibs != null) {
            for (String lib : orderedLibs) {
                URL jarUrl = this.servletContext.getResource("/WEB-INF/lib/" + lib);
                if (jarUrl != null) {
                    String base = jarUrl.toExternalForm();
                    if (base.endsWith("/")) {
                        jarEntryURL = new URL(base + configFile);
                    } else {
                        jarEntryURL = JarFactory.getJarEntryURL(jarUrl, configFile);
                    }
                    try {
                        URL url = jarEntryURL;
                        parseConfigFile(applicationServicesFound, url);
                    } catch (FileNotFoundException e) {
                    }
                }
            }
            loader = this.context.getParentClassLoader();
        }
        if (loader == null) {
            resources = ClassLoader.getSystemResources(configFile);
        } else {
            resources = loader.getResources(configFile);
        }
        while (resources.hasMoreElements()) {
            parseConfigFile(containerServicesFound, resources.nextElement());
        }
        if (this.containerSciFilterPattern != null) {
            Iterator<String> iter = containerServicesFound.iterator();
            while (iter.hasNext()) {
                if (this.containerSciFilterPattern.matcher(iter.next()).find()) {
                    iter.remove();
                }
            }
        }
        containerServicesFound.addAll(applicationServicesFound);
        if (containerServicesFound.isEmpty()) {
            return Collections.emptyList();
        }
        return loadServices(serviceType, containerServicesFound);
    }

    /* JADX WARN: Failed to apply debug info
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r10v1 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r11v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Not initialized variable reg: 10, insn: 0x00ed: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r10 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('in' java.io.InputStreamReader)]) A[TRY_LEAVE], block:B:48:0x00ed */
    /* JADX WARN: Not initialized variable reg: 11, insn: 0x00f2: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r11 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:50:0x00f2 */
    /* JADX WARN: Type inference failed for: r10v1, names: [in], types: [java.io.InputStreamReader] */
    /* JADX WARN: Type inference failed for: r11v0, types: [java.lang.Throwable] */
    void parseConfigFile(LinkedHashSet<String> servicesFound, URL url) throws IOException {
        ?? r10;
        ?? r11;
        InputStream is = url.openStream();
        Throwable th = null;
        try {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
                Throwable th2 = null;
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                Throwable th3 = null;
                while (true) {
                    try {
                        try {
                            String line = bufferedReader.readLine();
                            String strSubstring = line;
                            if (line == null) {
                                break;
                            }
                            int iIndexOf = strSubstring.indexOf(35);
                            if (iIndexOf >= 0) {
                                strSubstring = strSubstring.substring(0, iIndexOf);
                            }
                            String strTrim = strSubstring.trim();
                            if (strTrim.length() != 0) {
                                servicesFound.add(strTrim);
                            }
                        } catch (Throwable th4) {
                            if (bufferedReader != null) {
                                if (th3 != null) {
                                    try {
                                        bufferedReader.close();
                                    } catch (Throwable th5) {
                                        th3.addSuppressed(th5);
                                    }
                                } else {
                                    bufferedReader.close();
                                }
                            }
                            throw th4;
                        }
                    } catch (Throwable th6) {
                        th3 = th6;
                        throw th6;
                    }
                }
                if (bufferedReader != null) {
                    if (0 != 0) {
                        try {
                            bufferedReader.close();
                        } catch (Throwable th7) {
                            th3.addSuppressed(th7);
                        }
                    } else {
                        bufferedReader.close();
                    }
                }
                if (inputStreamReader != null) {
                    if (0 != 0) {
                        try {
                            inputStreamReader.close();
                        } catch (Throwable th8) {
                            th2.addSuppressed(th8);
                        }
                    } else {
                        inputStreamReader.close();
                    }
                }
                if (is != null) {
                    if (0 == 0) {
                        is.close();
                        return;
                    }
                    try {
                        is.close();
                    } catch (Throwable th9) {
                        th.addSuppressed(th9);
                    }
                }
            } catch (Throwable th10) {
                if (r10 != 0) {
                    if (r11 != 0) {
                        try {
                            r10.close();
                        } catch (Throwable th11) {
                            r11.addSuppressed(th11);
                        }
                    } else {
                        r10.close();
                    }
                }
                throw th10;
            }
        } catch (Throwable th12) {
            if (is != null) {
                if (0 != 0) {
                    try {
                        is.close();
                    } catch (Throwable x2) {
                        th.addSuppressed(x2);
                    }
                } else {
                    is.close();
                }
            }
            throw th12;
        }
    }

    List<T> loadServices(Class<T> serviceType, LinkedHashSet<String> servicesFound) throws ClassNotFoundException, IOException {
        ClassLoader loader = this.servletContext.getClassLoader();
        List<T> services = new ArrayList<>(servicesFound.size());
        Iterator i$ = servicesFound.iterator();
        while (i$.hasNext()) {
            String serviceClass = i$.next();
            try {
                Class<?> clazz = Class.forName(serviceClass, true, loader);
                services.add(serviceType.cast(clazz.getConstructor(new Class[0]).newInstance(new Object[0])));
            } catch (ClassCastException | ReflectiveOperationException e) {
                throw new IOException(e);
            }
        }
        return Collections.unmodifiableList(services);
    }
}
