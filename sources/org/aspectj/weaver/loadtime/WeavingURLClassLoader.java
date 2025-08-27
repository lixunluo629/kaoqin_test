package org.aspectj.weaver.loadtime;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.aspectj.bridge.AbortException;
import org.aspectj.weaver.bcel.ExtensibleURLClassLoader;
import org.aspectj.weaver.tools.Trace;
import org.aspectj.weaver.tools.TraceFactory;
import org.aspectj.weaver.tools.WeavingAdaptor;
import org.aspectj.weaver.tools.WeavingClassLoader;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/WeavingURLClassLoader.class */
public class WeavingURLClassLoader extends ExtensibleURLClassLoader implements WeavingClassLoader {
    public static final String WEAVING_CLASS_PATH = "aj.class.path";
    public static final String WEAVING_ASPECT_PATH = "aj.aspect.path";
    private URL[] aspectURLs;
    private WeavingAdaptor adaptor;
    private boolean initializingAdaptor;
    private Map generatedClasses;
    private static Trace trace = TraceFactory.getTraceFactory().getTrace(WeavingURLClassLoader.class);

    public WeavingURLClassLoader(ClassLoader parent) {
        this(getURLs(getClassPath()), getURLs(getAspectPath()), parent);
    }

    public WeavingURLClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        this.generatedClasses = new HashMap();
        if (trace.isTraceEnabled()) {
            trace.enter("<init>", (Object) this, new Object[]{urls, parent});
        }
        if (trace.isTraceEnabled()) {
            trace.exit("<init>");
        }
    }

    public WeavingURLClassLoader(URL[] classURLs, URL[] aspectURLs, ClassLoader parent) {
        super(classURLs, parent);
        this.generatedClasses = new HashMap();
        this.aspectURLs = aspectURLs;
        if (this.aspectURLs.length > 0 || (getParent() instanceof WeavingClassLoader)) {
            try {
                this.adaptor = new WeavingAdaptor(this);
            } catch (ExceptionInInitializerError ex) {
                ex.printStackTrace(System.out);
                throw ex;
            }
        }
    }

    private static String getAspectPath() {
        return System.getProperty(WEAVING_ASPECT_PATH, "");
    }

    private static String getClassPath() {
        return System.getProperty(WEAVING_CLASS_PATH, "");
    }

    private static URL[] getURLs(String path) {
        URL url;
        List urlList = new ArrayList();
        StringTokenizer t = new StringTokenizer(path, File.pathSeparator);
        while (t.hasMoreTokens()) {
            File f = new File(t.nextToken().trim());
            try {
                if (f.exists() && (url = f.toURL()) != null) {
                    urlList.add(url);
                }
            } catch (MalformedURLException e) {
            }
        }
        URL[] urls = new URL[urlList.size()];
        urlList.toArray(urls);
        return urls;
    }

    @Override // org.aspectj.weaver.bcel.ExtensibleURLClassLoader, java.net.URLClassLoader
    protected void addURL(URL url) {
        if (this.adaptor == null) {
            createAdaptor();
        }
        this.adaptor.addURL(url);
        super.addURL(url);
    }

    @Override // org.aspectj.weaver.bcel.ExtensibleURLClassLoader
    protected Class defineClass(String name, byte[] b, CodeSource cs) throws IOException {
        Class clazz;
        if (trace.isTraceEnabled()) {
            trace.enter("defineClass", (Object) this, new Object[]{name, b, cs});
        }
        if (!this.initializingAdaptor) {
            if (this.adaptor == null && !this.initializingAdaptor) {
                createAdaptor();
            }
            try {
                b = this.adaptor.weaveClass(name, b, false);
            } catch (AbortException ex) {
                trace.error("defineClass", ex);
                throw ex;
            } catch (Throwable th) {
                trace.error("defineClass", th);
            }
        }
        try {
            clazz = super.defineClass(name, b, cs);
        } catch (Throwable th2) {
            trace.error("Weaving class problem. Original class has been returned. The error was caused because of: " + th2, th2);
            clazz = super.defineClass(name, b, cs);
        }
        if (trace.isTraceEnabled()) {
            trace.exit("defineClass", clazz);
        }
        return clazz;
    }

    private void createAdaptor() {
        DefaultWeavingContext weavingContext = new DefaultWeavingContext(this) { // from class: org.aspectj.weaver.loadtime.WeavingURLClassLoader.1
            @Override // org.aspectj.weaver.loadtime.DefaultWeavingContext, org.aspectj.weaver.loadtime.IWeavingContext
            public String getClassLoaderName() {
                ClassLoader loader = getClassLoader();
                return loader.getClass().getName();
            }
        };
        ClassLoaderWeavingAdaptor clwAdaptor = new ClassLoaderWeavingAdaptor();
        this.initializingAdaptor = true;
        clwAdaptor.initialize(this, weavingContext);
        this.initializingAdaptor = false;
        this.adaptor = clwAdaptor;
    }

    @Override // org.aspectj.weaver.bcel.ExtensibleURLClassLoader
    protected byte[] getBytes(String name) throws IOException {
        byte[] bytes = super.getBytes(name);
        if (bytes == null) {
            return (byte[]) this.generatedClasses.remove(name);
        }
        return bytes;
    }

    @Override // org.aspectj.weaver.tools.WeavingClassLoader
    public URL[] getAspectURLs() {
        return this.aspectURLs;
    }

    @Override // org.aspectj.weaver.tools.GeneratedClassHandler
    public void acceptClass(String name, byte[] classBytes, byte[] weavedBytes) {
        this.generatedClasses.put(name, weavedBytes);
    }
}
