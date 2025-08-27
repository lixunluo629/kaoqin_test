package net.sf.cglib.core;

import org.objectweb.asm.Label;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/ObjectSwitchCallback.class */
public interface ObjectSwitchCallback {
    void processCase(Object obj, Label label) throws Exception;

    void processDefault() throws Exception;
}
