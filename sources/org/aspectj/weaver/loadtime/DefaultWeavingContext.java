package org.aspectj.weaver.loadtime;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import org.aspectj.weaver.bcel.BcelWeakClassLoaderReference;
import org.aspectj.weaver.loadtime.definition.Definition;
import org.aspectj.weaver.tools.Trace;
import org.aspectj.weaver.tools.TraceFactory;
import org.aspectj.weaver.tools.WeavingAdaptor;
import org.springframework.util.ClassUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/DefaultWeavingContext.class */
public class DefaultWeavingContext implements IWeavingContext {
    protected BcelWeakClassLoaderReference loaderRef;
    private String shortName;
    private static Trace trace = TraceFactory.getTraceFactory().getTrace(DefaultWeavingContext.class);

    public DefaultWeavingContext(ClassLoader loader) {
        this.loaderRef = new BcelWeakClassLoaderReference(loader);
    }

    @Override // org.aspectj.weaver.loadtime.IWeavingContext
    public Enumeration getResources(String name) throws IOException {
        return getClassLoader().getResources(name);
    }

    @Override // org.aspectj.weaver.loadtime.IWeavingContext
    public String getBundleIdFromURL(URL url) {
        return "";
    }

    @Override // org.aspectj.weaver.loadtime.IWeavingContext
    public String getClassLoaderName() {
        ClassLoader loader = getClassLoader();
        return loader != null ? loader.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(loader)) : "null";
    }

    @Override // org.aspectj.weaver.loadtime.IWeavingContext
    public ClassLoader getClassLoader() {
        return this.loaderRef.getClassLoader();
    }

    @Override // org.aspectj.weaver.loadtime.IWeavingContext
    public String getFile(URL url) {
        return url.getFile();
    }

    @Override // org.aspectj.weaver.loadtime.IWeavingContext
    public String getId() {
        if (this.shortName == null) {
            this.shortName = getClassLoaderName().replace('$', '.');
            int index = this.shortName.lastIndexOf(".");
            if (index != -1) {
                this.shortName = this.shortName.substring(index + 1);
            }
        }
        return this.shortName;
    }

    public String getSuffix() {
        return getClassLoaderName();
    }

    @Override // org.aspectj.weaver.loadtime.IWeavingContext
    public boolean isLocallyDefined(String classname) {
        String asResource = classname.replace('.', '/').concat(ClassUtils.CLASS_FILE_SUFFIX);
        ClassLoader loader = getClassLoader();
        URL localURL = loader.getResource(asResource);
        if (localURL == null) {
            return false;
        }
        boolean isLocallyDefined = true;
        ClassLoader parent = loader.getParent();
        if (parent != null) {
            URL parentURL = parent.getResource(asResource);
            if (localURL.equals(parentURL)) {
                isLocallyDefined = false;
            }
        }
        return isLocallyDefined;
    }

    @Override // org.aspectj.weaver.loadtime.IWeavingContext
    public List<Definition> getDefinitions(ClassLoader loader, WeavingAdaptor adaptor) {
        if (trace.isTraceEnabled()) {
            trace.enter("getDefinitions", (Object) this, new Object[]{"goo", adaptor});
        }
        List<Definition> definitions = ((ClassLoaderWeavingAdaptor) adaptor).parseDefinitions(loader);
        if (trace.isTraceEnabled()) {
            trace.exit("getDefinitions", definitions);
        }
        return definitions;
    }
}
