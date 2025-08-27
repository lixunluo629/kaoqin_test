package org.apache.xmlbeans.impl.jam.internal;

import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JamClassIterator;
import org.apache.xmlbeans.impl.jam.JamClassLoader;
import org.apache.xmlbeans.impl.jam.JamService;
import org.apache.xmlbeans.impl.jam.internal.elements.ElementContext;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/JamServiceImpl.class */
public class JamServiceImpl implements JamService {
    private ElementContext mContext;
    private String[] mClassNames;

    public JamServiceImpl(ElementContext ctx, String[] classes) {
        if (ctx == null) {
            throw new IllegalArgumentException("null jcl");
        }
        if (classes == null) {
            throw new IllegalArgumentException("null classes");
        }
        this.mContext = ctx;
        this.mClassNames = classes;
    }

    @Override // org.apache.xmlbeans.impl.jam.JamService
    public JamClassLoader getClassLoader() {
        return this.mContext.getClassLoader();
    }

    @Override // org.apache.xmlbeans.impl.jam.JamService
    public String[] getClassNames() {
        return this.mClassNames;
    }

    @Override // org.apache.xmlbeans.impl.jam.JamService
    public JamClassIterator getClasses() {
        return new JamClassIterator(getClassLoader(), getClassNames());
    }

    @Override // org.apache.xmlbeans.impl.jam.JamService
    public JClass[] getAllClasses() {
        JClass[] out = new JClass[this.mClassNames.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = getClassLoader().loadClass(this.mClassNames[i]);
        }
        return out;
    }

    public void setClassNames(String[] names) {
        this.mClassNames = names;
    }
}
