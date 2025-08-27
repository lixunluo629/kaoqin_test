package org.apache.xmlbeans;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/InterfaceExtension.class */
public interface InterfaceExtension {

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/InterfaceExtension$MethodSignature.class */
    public interface MethodSignature {
        String getName();

        String getReturnType();

        String[] getParameterTypes();

        String[] getExceptionTypes();
    }

    String getInterface();

    String getStaticHandler();

    MethodSignature[] getMethods();
}
