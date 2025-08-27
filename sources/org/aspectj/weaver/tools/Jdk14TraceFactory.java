package org.aspectj.weaver.tools;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/Jdk14TraceFactory.class */
public class Jdk14TraceFactory extends TraceFactory {
    @Override // org.aspectj.weaver.tools.TraceFactory
    public Trace getTrace(Class clazz) {
        return new Jdk14Trace(clazz);
    }
}
