package org.apache.xmlbeans;

import org.apache.xmlbeans.XmlFactoryHook;
import org.apache.xmlbeans.impl.common.SystemCache;
import org.apache.xmlbeans.impl.schema.StscState;
import org.apache.xmlbeans.impl.store.CharUtil;
import org.apache.xmlbeans.impl.store.Locale;
import org.apache.xmlbeans.impl.values.NamespaceContext;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/ThreadLocalUtil.class */
public class ThreadLocalUtil {
    public static void clearAllThreadLocals() {
        XmlBeans.clearThreadLocals();
        XmlFactoryHook.ThreadContext.clearThreadLocals();
        StscState.clearThreadLocals();
        CharUtil.clearThreadLocals();
        Locale.clearThreadLocals();
        NamespaceContext.clearThreadLocals();
        SystemCache systemCache = SystemCache.get();
        systemCache.clearThreadLocals();
    }
}
