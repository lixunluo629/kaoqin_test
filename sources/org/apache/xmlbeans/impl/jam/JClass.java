package org.apache.xmlbeans.impl.jam;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/JClass.class */
public interface JClass extends JMember {
    JPackage getContainingPackage();

    JClass getSuperclass();

    JClass[] getInterfaces();

    JField[] getFields();

    JField[] getDeclaredFields();

    JMethod[] getMethods();

    JMethod[] getDeclaredMethods();

    JConstructor[] getConstructors();

    JProperty[] getProperties();

    JProperty[] getDeclaredProperties();

    boolean isInterface();

    boolean isAnnotationType();

    boolean isPrimitiveType();

    boolean isBuiltinType();

    Class getPrimitiveClass();

    boolean isFinal();

    boolean isStatic();

    boolean isAbstract();

    boolean isVoidType();

    boolean isObjectType();

    boolean isArrayType();

    JClass getArrayComponentType();

    int getArrayDimensions();

    boolean isAssignableFrom(JClass jClass);

    boolean equals(Object obj);

    JClass[] getClasses();

    @Override // org.apache.xmlbeans.impl.jam.JMember
    JClass getContainingClass();

    String getFieldDescriptor();

    boolean isEnumType();

    JamClassLoader getClassLoader();

    JClass forName(String str);

    JClass[] getImportedClasses();

    JPackage[] getImportedPackages();

    boolean isUnresolvedType();
}
