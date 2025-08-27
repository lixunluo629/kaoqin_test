package org.aspectj.weaver.loadtime;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import org.aspectj.weaver.tools.WeavingAdaptor;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/IWeavingContext.class */
public interface IWeavingContext {
    Enumeration getResources(String str) throws IOException;

    String getBundleIdFromURL(URL url);

    String getClassLoaderName();

    ClassLoader getClassLoader();

    String getFile(URL url);

    String getId();

    boolean isLocallyDefined(String str);

    List getDefinitions(ClassLoader classLoader, WeavingAdaptor weavingAdaptor);
}
