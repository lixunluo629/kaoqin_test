package org.apache.xmlbeans.impl.jam.provider;

import org.apache.xmlbeans.impl.jam.internal.elements.ClassImpl;
import org.apache.xmlbeans.impl.jam.internal.elements.ElementContext;
import org.apache.xmlbeans.impl.jam.mutable.MClass;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/provider/JamClassBuilder.class */
public abstract class JamClassBuilder {
    private ElementContext mContext = null;

    public abstract MClass build(String str, String str2);

    public void init(ElementContext ctx) {
        if (this.mContext != null) {
            throw new IllegalStateException("init called more than once");
        }
        if (ctx == null) {
            throw new IllegalArgumentException("null ctx");
        }
        this.mContext = ctx;
    }

    protected MClass createClassToBuild(String packageName, String className, String[] importSpecs, JamClassPopulator pop) {
        if (this.mContext == null) {
            throw new IllegalStateException("init not called");
        }
        if (packageName == null) {
            throw new IllegalArgumentException("null pkg");
        }
        if (className == null) {
            throw new IllegalArgumentException("null class");
        }
        if (pop == null) {
            throw new IllegalArgumentException("null pop");
        }
        assertInitialized();
        ClassImpl out = new ClassImpl(packageName, className.replace('.', '$'), this.mContext, importSpecs, pop);
        return out;
    }

    protected MClass createClassToBuild(String packageName, String className, String[] importSpecs) {
        if (this.mContext == null) {
            throw new IllegalStateException("init not called");
        }
        if (packageName == null) {
            throw new IllegalArgumentException("null pkg");
        }
        if (className == null) {
            throw new IllegalArgumentException("null class");
        }
        assertInitialized();
        ClassImpl out = new ClassImpl(packageName, className.replace('.', '$'), this.mContext, importSpecs);
        return out;
    }

    protected JamLogger getLogger() {
        return this.mContext;
    }

    protected final void assertInitialized() {
        if (this.mContext == null) {
            throw new IllegalStateException(this + " not yet initialized.");
        }
    }
}
