package org.aspectj.weaver.tools;

import org.apache.commons.logging.LogFactory;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/CommonsTraceFactory.class */
public class CommonsTraceFactory extends TraceFactory {
    private LogFactory logFactory = LogFactory.getFactory();

    @Override // org.aspectj.weaver.tools.TraceFactory
    public Trace getTrace(Class clazz) {
        return new CommonsTrace(clazz);
    }
}
