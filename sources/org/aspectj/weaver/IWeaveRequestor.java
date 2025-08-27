package org.aspectj.weaver;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/IWeaveRequestor.class */
public interface IWeaveRequestor {
    void acceptResult(IUnwovenClassFile iUnwovenClassFile);

    void processingReweavableState();

    void addingTypeMungers();

    void weavingAspects();

    void weavingClasses();

    void weaveCompleted();
}
