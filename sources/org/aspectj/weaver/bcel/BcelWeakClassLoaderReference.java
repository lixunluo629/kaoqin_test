package org.aspectj.weaver.bcel;

import org.aspectj.apache.bcel.util.ClassLoaderReference;
import org.aspectj.weaver.WeakClassLoaderReference;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelWeakClassLoaderReference.class */
public class BcelWeakClassLoaderReference extends WeakClassLoaderReference implements ClassLoaderReference {
    public BcelWeakClassLoaderReference(ClassLoader loader) {
        super(loader);
    }

    @Override // org.aspectj.weaver.WeakClassLoaderReference
    public boolean equals(Object obj) {
        if (!(obj instanceof BcelWeakClassLoaderReference)) {
            return false;
        }
        BcelWeakClassLoaderReference other = (BcelWeakClassLoaderReference) obj;
        return other.hashcode == this.hashcode;
    }
}
