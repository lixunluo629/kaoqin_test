package org.aspectj.weaver.loadtime;

import java.security.ProtectionDomain;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/ClassPreProcessor.class */
public interface ClassPreProcessor {
    void initialize();

    byte[] preProcess(String str, byte[] bArr, ClassLoader classLoader, ProtectionDomain protectionDomain);
}
