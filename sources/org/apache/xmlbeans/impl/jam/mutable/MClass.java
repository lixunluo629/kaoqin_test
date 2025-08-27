package org.apache.xmlbeans.impl.jam.mutable;

import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JMethod;
import org.apache.xmlbeans.impl.jam.JProperty;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/mutable/MClass.class */
public interface MClass extends MMember, JClass {
    void setIsInterface(boolean z);

    void setIsAnnotationType(boolean z);

    void setIsEnumType(boolean z);

    void setSuperclass(String str);

    void setSuperclassUnqualified(String str);

    void setSuperclass(JClass jClass);

    void addInterface(String str);

    void addInterfaceUnqualified(String str);

    void addInterface(JClass jClass);

    void removeInterface(String str);

    void removeInterface(JClass jClass);

    MConstructor addNewConstructor();

    void removeConstructor(MConstructor mConstructor);

    MConstructor[] getMutableConstructors();

    MField addNewField();

    void removeField(MField mField);

    MField[] getMutableFields();

    MMethod addNewMethod();

    void removeMethod(MMethod mMethod);

    MMethod[] getMutableMethods();

    JProperty addNewProperty(String str, JMethod jMethod, JMethod jMethod2);

    void removeProperty(JProperty jProperty);

    JProperty addNewDeclaredProperty(String str, JMethod jMethod, JMethod jMethod2);

    void removeDeclaredProperty(JProperty jProperty);

    MClass addNewInnerClass(String str);

    void removeInnerClass(MClass mClass);
}
