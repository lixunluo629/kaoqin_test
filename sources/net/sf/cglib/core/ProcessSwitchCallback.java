package net.sf.cglib.core;

import org.objectweb.asm.Label;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/ProcessSwitchCallback.class */
public interface ProcessSwitchCallback {
    void processCase(int i, Label label) throws Exception;

    void processDefault() throws Exception;
}
