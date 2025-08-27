package org.springframework.cglib.core;

import org.springframework.asm.Label;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/core/ProcessSwitchCallback.class */
public interface ProcessSwitchCallback {
    void processCase(int i, Label label) throws Exception;

    void processDefault() throws Exception;
}
