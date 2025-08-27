package org.apache.xmlbeans.impl.jam.provider;

import org.apache.xmlbeans.impl.jam.internal.elements.ElementContext;
import org.apache.xmlbeans.impl.jam.mutable.MClass;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/provider/CompositeJamClassBuilder.class */
public class CompositeJamClassBuilder extends JamClassBuilder {
    private JamClassBuilder[] mBuilders;

    public CompositeJamClassBuilder(JamClassBuilder[] builders) {
        if (builders == null) {
            throw new IllegalArgumentException("null builders");
        }
        this.mBuilders = builders;
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamClassBuilder
    public void init(ElementContext ctx) {
        for (int i = 0; i < this.mBuilders.length; i++) {
            this.mBuilders[i].init(ctx);
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamClassBuilder
    public MClass build(String pkg, String cname) {
        for (int i = 0; i < this.mBuilders.length; i++) {
            MClass out = this.mBuilders[i].build(pkg, cname);
            if (out != null) {
                return out;
            }
        }
        return null;
    }
}
