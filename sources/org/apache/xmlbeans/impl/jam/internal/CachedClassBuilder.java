package org.apache.xmlbeans.impl.jam.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.impl.jam.mutable.MClass;
import org.apache.xmlbeans.impl.jam.provider.JamClassBuilder;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/CachedClassBuilder.class */
public class CachedClassBuilder extends JamClassBuilder {
    private Map mQcname2jclass = null;
    private List mClassNames = new ArrayList();

    @Override // org.apache.xmlbeans.impl.jam.provider.JamClassBuilder
    public MClass build(String packageName, String className) {
        if (this.mQcname2jclass == null) {
            return null;
        }
        if (packageName.trim().length() > 0) {
            className = packageName + '.' + className;
        }
        return (MClass) this.mQcname2jclass.get(className);
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamClassBuilder
    public MClass createClassToBuild(String packageName, String className, String[] importSpecs) {
        String qualifiedName;
        if (packageName.trim().length() > 0) {
            qualifiedName = packageName + '.' + className;
        } else {
            qualifiedName = className;
        }
        if (this.mQcname2jclass != null) {
            MClass out = (MClass) this.mQcname2jclass.get(qualifiedName);
            if (out != null) {
                return out;
            }
        } else {
            this.mQcname2jclass = new HashMap();
        }
        MClass out2 = super.createClassToBuild(packageName, className, importSpecs);
        this.mQcname2jclass.put(qualifiedName, out2);
        this.mClassNames.add(qualifiedName);
        return out2;
    }

    public String[] getClassNames() {
        String[] out = new String[this.mClassNames.size()];
        this.mClassNames.toArray(out);
        return out;
    }
}
