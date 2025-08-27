package org.apache.xmlbeans.impl.jam.annotation;

import org.apache.xmlbeans.impl.jam.JAnnotationValue;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.provider.JamLogger;
import org.apache.xmlbeans.impl.jam.provider.JamServiceContext;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/annotation/AnnotationProxy.class */
public abstract class AnnotationProxy {
    public static final String SINGLE_MEMBER_NAME = "value";
    private static final String DEFAULT_NVPAIR_DELIMS = "\n\r";
    protected JamServiceContext mContext;

    public abstract void setValue(String str, Object obj, JClass jClass);

    public abstract JAnnotationValue[] getValues();

    public void init(JamServiceContext ctx) {
        if (ctx == null) {
            throw new IllegalArgumentException("null logger");
        }
        this.mContext = ctx;
    }

    public JAnnotationValue getValue(String named) {
        if (named == null) {
            throw new IllegalArgumentException("null name");
        }
        String named2 = named.trim();
        JAnnotationValue[] values = getValues();
        for (int i = 0; i < values.length; i++) {
            if (named2.equals(values[i].getName())) {
                return values[i];
            }
        }
        return null;
    }

    protected JamLogger getLogger() {
        return this.mContext.getLogger();
    }
}
