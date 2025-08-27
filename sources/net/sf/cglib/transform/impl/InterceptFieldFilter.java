package net.sf.cglib.transform.impl;

import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/transform/impl/InterceptFieldFilter.class */
public interface InterceptFieldFilter {
    boolean acceptRead(Type type, String str);

    boolean acceptWrite(Type type, String str);
}
