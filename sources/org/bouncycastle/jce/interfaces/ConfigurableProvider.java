package org.bouncycastle.jce.interfaces;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/interfaces/ConfigurableProvider.class */
public interface ConfigurableProvider {
    public static final String THREAD_LOCAL_EC_IMPLICITLY_CA = "threadLocalEcImplicitlyCa";
    public static final String EC_IMPLICITLY_CA = "ecImplicitlyCa";

    void setParameter(String str, Object obj);
}
