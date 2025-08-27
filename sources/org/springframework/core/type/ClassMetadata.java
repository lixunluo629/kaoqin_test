package org.springframework.core.type;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/type/ClassMetadata.class */
public interface ClassMetadata {
    String getClassName();

    boolean isInterface();

    boolean isAnnotation();

    boolean isAbstract();

    boolean isConcrete();

    boolean isFinal();

    boolean isIndependent();

    boolean hasEnclosingClass();

    String getEnclosingClassName();

    boolean hasSuperClass();

    String getSuperClassName();

    String[] getInterfaceNames();

    String[] getMemberClassNames();
}
