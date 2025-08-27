package org.apache.xmlbeans;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/PrePostExtension.class */
public interface PrePostExtension {
    public static final int OPERATION_SET = 1;
    public static final int OPERATION_INSERT = 2;
    public static final int OPERATION_REMOVE = 3;

    String getStaticHandler();

    boolean hasPreCall();

    boolean hasPostCall();
}
