package org.apache.xmlbeans.impl.jam.internal.elements;

import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JConstructor;
import org.apache.xmlbeans.impl.jam.JField;
import org.apache.xmlbeans.impl.jam.JMethod;
import org.apache.xmlbeans.impl.jam.JPackage;
import org.apache.xmlbeans.impl.jam.JProperty;
import org.apache.xmlbeans.impl.jam.JSourcePosition;
import org.apache.xmlbeans.impl.jam.mutable.MClass;
import org.apache.xmlbeans.impl.jam.mutable.MConstructor;
import org.apache.xmlbeans.impl.jam.mutable.MField;
import org.apache.xmlbeans.impl.jam.mutable.MMethod;
import org.apache.xmlbeans.impl.jam.visitor.JVisitor;
import org.apache.xmlbeans.impl.jam.visitor.MVisitor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/BuiltinClassImpl.class */
public abstract class BuiltinClassImpl extends AnnotatedElementImpl implements MClass {
    protected BuiltinClassImpl(ElementContext ctx) {
        super(ctx);
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MElement
    public void accept(MVisitor visitor) {
        visitor.visit(this);
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public void accept(JVisitor visitor) {
        visitor.visit(this);
    }

    public String getQualifiedName() {
        return this.mSimpleName;
    }

    public String getFieldDescriptor() {
        return this.mSimpleName;
    }

    @Override // org.apache.xmlbeans.impl.jam.JMember
    public int getModifiers() {
        return Object.class.getModifiers();
    }

    @Override // org.apache.xmlbeans.impl.jam.JMember
    public boolean isPublic() {
        return true;
    }

    @Override // org.apache.xmlbeans.impl.jam.JMember
    public boolean isPackagePrivate() {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.JMember
    public boolean isProtected() {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.JMember
    public boolean isPrivate() {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.ElementImpl, org.apache.xmlbeans.impl.jam.JElement
    public JSourcePosition getSourcePosition() {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.JMember
    public JClass getContainingClass() {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JClass forName(String fd) {
        return getClassLoader().loadClass(fd);
    }

    public JClass getArrayComponentType() {
        return null;
    }

    public int getArrayDimensions() {
        return 0;
    }

    public JClass getSuperclass() {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JClass[] getInterfaces() {
        return NO_CLASS;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JField[] getFields() {
        return NO_FIELD;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JField[] getDeclaredFields() {
        return NO_FIELD;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JConstructor[] getConstructors() {
        return NO_CONSTRUCTOR;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JMethod[] getMethods() {
        return NO_METHOD;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JMethod[] getDeclaredMethods() {
        return NO_METHOD;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JPackage getContainingPackage() {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isInterface() {
        return false;
    }

    public boolean isArrayType() {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isAnnotationType() {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isPrimitiveType() {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isBuiltinType() {
        return true;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isUnresolvedType() {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isObjectType() {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isVoidType() {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isEnumType() {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public Class getPrimitiveClass() {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isAbstract() {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isFinal() {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isStatic() {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JClass[] getClasses() {
        return NO_CLASS;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JProperty[] getProperties() {
        return NO_PROPERTY;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JProperty[] getDeclaredProperties() {
        return NO_PROPERTY;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JPackage[] getImportedPackages() {
        return NO_PACKAGE;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JClass[] getImportedClasses() {
        return NO_CLASS;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public MField[] getMutableFields() {
        return NO_FIELD;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public MConstructor[] getMutableConstructors() {
        return NO_CONSTRUCTOR;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public MMethod[] getMutableMethods() {
        return NO_METHOD;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.ElementImpl, org.apache.xmlbeans.impl.jam.mutable.MElement
    public void setSimpleName(String s) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void setIsAnnotationType(boolean b) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void setIsInterface(boolean b) {
        nocando();
    }

    public void setIsUnresolvedType(boolean b) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void setIsEnumType(boolean b) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void setSuperclass(String qualifiedClassName) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void setSuperclassUnqualified(String unqualifiedClassName) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void setSuperclass(JClass clazz) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void addInterface(String className) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void addInterfaceUnqualified(String unqualifiedClassName) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void addInterface(JClass interf) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void removeInterface(String className) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void removeInterface(JClass interf) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public MConstructor addNewConstructor() {
        nocando();
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void removeConstructor(MConstructor constr) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public MField addNewField() {
        nocando();
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void removeField(MField field) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public MMethod addNewMethod() {
        nocando();
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void removeMethod(MMethod method) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MMember
    public void setModifiers(int modifiers) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public MClass addNewInnerClass(String named) {
        nocando();
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void removeInnerClass(MClass inner) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public JProperty addNewProperty(String name, JMethod m, JMethod x) {
        nocando();
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void removeProperty(JProperty prop) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public JProperty addNewDeclaredProperty(String name, JMethod m, JMethod x) {
        nocando();
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void removeDeclaredProperty(JProperty prop) {
        nocando();
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.ElementImpl, org.apache.xmlbeans.impl.jam.JClass
    public boolean equals(Object o) {
        if (o instanceof JClass) {
            return ((JClass) o).getFieldDescriptor().equals(getFieldDescriptor());
        }
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.ElementImpl
    public int hashCode() {
        return getFieldDescriptor().hashCode();
    }

    protected void reallySetSimpleName(String name) {
        super.setSimpleName(name);
    }

    private void nocando() {
        throw new UnsupportedOperationException("Cannot alter builtin types");
    }
}
