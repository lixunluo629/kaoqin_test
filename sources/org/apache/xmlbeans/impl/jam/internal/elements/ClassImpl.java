package org.apache.xmlbeans.impl.jam.internal.elements;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.xmlbeans.impl.jam.JAnnotation;
import org.apache.xmlbeans.impl.jam.JAnnotationValue;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JComment;
import org.apache.xmlbeans.impl.jam.JConstructor;
import org.apache.xmlbeans.impl.jam.JField;
import org.apache.xmlbeans.impl.jam.JMethod;
import org.apache.xmlbeans.impl.jam.JPackage;
import org.apache.xmlbeans.impl.jam.JProperty;
import org.apache.xmlbeans.impl.jam.JSourcePosition;
import org.apache.xmlbeans.impl.jam.internal.JamClassLoaderImpl;
import org.apache.xmlbeans.impl.jam.internal.classrefs.JClassRef;
import org.apache.xmlbeans.impl.jam.internal.classrefs.JClassRefContext;
import org.apache.xmlbeans.impl.jam.internal.classrefs.QualifiedJClassRef;
import org.apache.xmlbeans.impl.jam.internal.classrefs.UnqualifiedJClassRef;
import org.apache.xmlbeans.impl.jam.mutable.MClass;
import org.apache.xmlbeans.impl.jam.mutable.MConstructor;
import org.apache.xmlbeans.impl.jam.mutable.MField;
import org.apache.xmlbeans.impl.jam.mutable.MMethod;
import org.apache.xmlbeans.impl.jam.provider.JamClassPopulator;
import org.apache.xmlbeans.impl.jam.visitor.JVisitor;
import org.apache.xmlbeans.impl.jam.visitor.MVisitor;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/ClassImpl.class */
public class ClassImpl extends MemberImpl implements MClass, JClassRef, JClassRefContext {
    public static final int NEW = 1;
    public static final int UNPOPULATED = 2;
    public static final int POPULATING = 3;
    public static final int UNINITIALIZED = 4;
    public static final int INITIALIZING = 5;
    public static final int LOADED = 6;
    private int mState;
    private boolean mIsAnnotationType;
    private boolean mIsInterface;
    private boolean mIsEnum;
    private String mPackageName;
    private JClassRef mSuperClassRef;
    private ArrayList mInterfaceRefs;
    private ArrayList mFields;
    private ArrayList mMethods;
    private ArrayList mConstructors;
    private ArrayList mProperties;
    private ArrayList mDeclaredProperties;
    private ArrayList mInnerClasses;
    private String[] mImports;
    private JamClassPopulator mPopulator;

    public ClassImpl(String packageName, String simpleName, ElementContext ctx, String[] importSpecs, JamClassPopulator populator) {
        super(ctx);
        this.mState = 1;
        this.mIsAnnotationType = false;
        this.mIsInterface = false;
        this.mIsEnum = false;
        this.mPackageName = null;
        this.mSuperClassRef = null;
        this.mInterfaceRefs = null;
        this.mFields = null;
        this.mMethods = null;
        this.mConstructors = null;
        this.mProperties = null;
        this.mDeclaredProperties = null;
        this.mInnerClasses = null;
        this.mImports = null;
        super.setSimpleName(simpleName);
        this.mPackageName = packageName.trim();
        this.mImports = importSpecs;
        this.mPopulator = populator;
        setState(2);
    }

    public ClassImpl(String packageName, String simpleName, ElementContext ctx, String[] importSpecs) {
        super(ctx);
        this.mState = 1;
        this.mIsAnnotationType = false;
        this.mIsInterface = false;
        this.mIsEnum = false;
        this.mPackageName = null;
        this.mSuperClassRef = null;
        this.mInterfaceRefs = null;
        this.mFields = null;
        this.mMethods = null;
        this.mConstructors = null;
        this.mProperties = null;
        this.mDeclaredProperties = null;
        this.mInnerClasses = null;
        this.mImports = null;
        super.setSimpleName(simpleName);
        this.mPackageName = packageName.trim();
        this.mImports = importSpecs;
        this.mPopulator = null;
        setState(4);
    }

    private ClassImpl(String packageName, String simpleName, String[] importSpecs, ClassImpl parent) {
        super(parent);
        this.mState = 1;
        this.mIsAnnotationType = false;
        this.mIsInterface = false;
        this.mIsEnum = false;
        this.mPackageName = null;
        this.mSuperClassRef = null;
        this.mInterfaceRefs = null;
        this.mFields = null;
        this.mMethods = null;
        this.mConstructors = null;
        this.mProperties = null;
        this.mDeclaredProperties = null;
        this.mInnerClasses = null;
        this.mImports = null;
        super.setSimpleName(simpleName);
        this.mPackageName = packageName.trim();
        this.mImports = importSpecs;
        this.mPopulator = null;
        setState(4);
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JPackage getContainingPackage() {
        return getClassLoader().getPackage(this.mPackageName);
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JClass getSuperclass() {
        ensureLoaded();
        if (this.mSuperClassRef == null) {
            return null;
        }
        return this.mSuperClassRef.getRefClass();
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JClass[] getInterfaces() {
        ensureLoaded();
        if (this.mInterfaceRefs == null || this.mInterfaceRefs.size() == 0) {
            return new JClass[0];
        }
        JClass[] out = new JClass[this.mInterfaceRefs.size()];
        for (int i = 0; i < out.length; i++) {
            out[i] = ((JClassRef) this.mInterfaceRefs.get(i)).getRefClass();
        }
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JField[] getFields() {
        ensureLoaded();
        List list = new ArrayList();
        addFieldsRecursively(this, list);
        JField[] out = new JField[list.size()];
        list.toArray(out);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JField[] getDeclaredFields() {
        ensureLoaded();
        return getMutableFields();
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JMethod[] getMethods() {
        ensureLoaded();
        List list = new ArrayList();
        addMethodsRecursively(this, list);
        JMethod[] out = new JMethod[list.size()];
        list.toArray(out);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JProperty[] getProperties() {
        ensureLoaded();
        if (this.mProperties == null) {
            return new JProperty[0];
        }
        JProperty[] out = new JProperty[this.mProperties.size()];
        this.mProperties.toArray(out);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JProperty[] getDeclaredProperties() {
        ensureLoaded();
        if (this.mDeclaredProperties == null) {
            return new JProperty[0];
        }
        JProperty[] out = new JProperty[this.mDeclaredProperties.size()];
        this.mDeclaredProperties.toArray(out);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JMethod[] getDeclaredMethods() {
        ensureLoaded();
        return getMutableMethods();
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JConstructor[] getConstructors() {
        ensureLoaded();
        return getMutableConstructors();
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isInterface() {
        ensureLoaded();
        return this.mIsInterface;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isAnnotationType() {
        ensureLoaded();
        return this.mIsAnnotationType;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isEnumType() {
        ensureLoaded();
        return this.mIsEnum;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.MemberImpl, org.apache.xmlbeans.impl.jam.JMember
    public int getModifiers() {
        ensureLoaded();
        return super.getModifiers();
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isFinal() {
        return Modifier.isFinal(getModifiers());
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isAbstract() {
        return Modifier.isAbstract(getModifiers());
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isAssignableFrom(JClass arg) {
        ensureLoaded();
        if (isPrimitiveType() || arg.isPrimitiveType()) {
            return getQualifiedName().equals(arg.getQualifiedName());
        }
        return isAssignableFromRecursively(arg);
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JClass[] getClasses() {
        ensureLoaded();
        if (this.mInnerClasses == null) {
            return new JClass[0];
        }
        JClass[] out = new JClass[this.mInnerClasses.size()];
        this.mInnerClasses.toArray(out);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public String getFieldDescriptor() {
        return getQualifiedName();
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JClass forName(String name) {
        return getClassLoader().loadClass(name);
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JPackage[] getImportedPackages() {
        ensureLoaded();
        Set set = new TreeSet();
        JClass[] importedClasses = getImportedClasses();
        for (JClass jClass : importedClasses) {
            JPackage c = jClass.getContainingPackage();
            if (c != null) {
                set.add(c);
            }
        }
        String[] imports = getImportSpecs();
        if (imports != null) {
            for (int i = 0; i < imports.length; i++) {
                if (imports[i].endsWith(".*")) {
                    set.add(getClassLoader().getPackage(imports[i].substring(0, imports[i].length() - 2)));
                }
            }
        }
        JPackage[] array = new JPackage[set.size()];
        set.toArray(array);
        return array;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JClass[] getImportedClasses() {
        ensureLoaded();
        String[] imports = getImportSpecs();
        if (imports == null) {
            return new JClass[0];
        }
        List list = new ArrayList();
        for (int i = 0; i < imports.length; i++) {
            if (!imports[i].endsWith("*")) {
                list.add(getClassLoader().loadClass(imports[i]));
            }
        }
        JClass[] out = new JClass[list.size()];
        list.toArray(out);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MElement
    public void accept(MVisitor visitor) {
        visitor.visit(this);
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public void accept(JVisitor visitor) {
        visitor.visit(this);
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.ElementImpl, org.apache.xmlbeans.impl.jam.mutable.MElement
    public void setSimpleName(String name) {
        throw new UnsupportedOperationException("Class names cannot be changed");
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public Class getPrimitiveClass() {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isPrimitiveType() {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isBuiltinType() {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isVoidType() {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isUnresolvedType() {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isObjectType() {
        return getQualifiedName().equals("java.lang.Object");
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isArrayType() {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public JClass getArrayComponentType() {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public int getArrayDimensions() {
        return 0;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.AnnotatedElementImpl, org.apache.xmlbeans.impl.jam.JAnnotatedElement
    public JAnnotation[] getAnnotations() {
        ensureLoaded();
        return super.getAnnotations();
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.AnnotatedElementImpl, org.apache.xmlbeans.impl.jam.JAnnotatedElement
    public JAnnotation getAnnotation(Class proxyClass) {
        ensureLoaded();
        return super.getAnnotation(proxyClass);
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.AnnotatedElementImpl, org.apache.xmlbeans.impl.jam.JAnnotatedElement
    public JAnnotation getAnnotation(String named) {
        ensureLoaded();
        return super.getAnnotation(named);
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.AnnotatedElementImpl, org.apache.xmlbeans.impl.jam.JAnnotatedElement
    public JAnnotationValue getAnnotationValue(String valueId) {
        ensureLoaded();
        return super.getAnnotationValue(valueId);
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.AnnotatedElementImpl, org.apache.xmlbeans.impl.jam.JAnnotatedElement
    public Object getAnnotationProxy(Class proxyClass) {
        ensureLoaded();
        return super.getAnnotationProxy(proxyClass);
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.AnnotatedElementImpl, org.apache.xmlbeans.impl.jam.JAnnotatedElement
    public JComment getComment() {
        ensureLoaded();
        return super.getComment();
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.AnnotatedElementImpl, org.apache.xmlbeans.impl.jam.JAnnotatedElement
    public JAnnotation[] getAllJavadocTags() {
        ensureLoaded();
        return super.getAllJavadocTags();
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.ElementImpl, org.apache.xmlbeans.impl.jam.JElement
    public JSourcePosition getSourcePosition() {
        ensureLoaded();
        return super.getSourcePosition();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void setSuperclass(String qualifiedClassName) {
        if (qualifiedClassName == null) {
            this.mSuperClassRef = null;
        } else {
            if (qualifiedClassName.equals(getQualifiedName())) {
                throw new IllegalArgumentException("A class cannot be it's own superclass: '" + qualifiedClassName + "'");
            }
            this.mSuperClassRef = QualifiedJClassRef.create(qualifiedClassName, this);
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void setSuperclassUnqualified(String unqualifiedClassName) {
        this.mSuperClassRef = UnqualifiedJClassRef.create(unqualifiedClassName, this);
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void setSuperclass(JClass clazz) {
        if (clazz == null) {
            this.mSuperClassRef = null;
        } else {
            setSuperclass(clazz.getQualifiedName());
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void addInterface(JClass interf) {
        if (interf == null) {
            throw new IllegalArgumentException("null interf");
        }
        addInterface(interf.getQualifiedName());
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void addInterface(String qcName) {
        if (this.mInterfaceRefs == null) {
            this.mInterfaceRefs = new ArrayList();
        }
        if (qcName.equals(getQualifiedName())) {
            throw new IllegalArgumentException("A class cannot implement itself: '" + qcName + "'");
        }
        this.mInterfaceRefs.add(QualifiedJClassRef.create(qcName, this));
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void addInterfaceUnqualified(String ucname) {
        if (this.mInterfaceRefs == null) {
            this.mInterfaceRefs = new ArrayList();
        }
        this.mInterfaceRefs.add(UnqualifiedJClassRef.create(ucname, this));
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void removeInterface(JClass interf) {
        if (interf == null) {
            throw new IllegalArgumentException("null interf");
        }
        removeInterface(interf.getQualifiedName());
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void removeInterface(String qcname) {
        if (qcname == null) {
            throw new IllegalArgumentException("null classname");
        }
        if (this.mInterfaceRefs == null) {
            return;
        }
        for (int i = 0; i < this.mInterfaceRefs.size(); i++) {
            if (qcname.equals(((JClassRef) this.mInterfaceRefs.get(i)).getQualifiedName())) {
                this.mInterfaceRefs.remove(i);
            }
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public MConstructor addNewConstructor() {
        if (this.mConstructors == null) {
            this.mConstructors = new ArrayList();
        }
        ConstructorImpl constructorImpl = new ConstructorImpl(this);
        this.mConstructors.add(constructorImpl);
        return constructorImpl;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void removeConstructor(MConstructor constr) {
        if (this.mConstructors == null) {
            return;
        }
        this.mConstructors.remove(constr);
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public MConstructor[] getMutableConstructors() {
        if (this.mConstructors == null || this.mConstructors.size() == 0) {
            return new MConstructor[0];
        }
        MConstructor[] out = new MConstructor[this.mConstructors.size()];
        this.mConstructors.toArray(out);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public MField addNewField() {
        if (this.mFields == null) {
            this.mFields = new ArrayList();
        }
        FieldImpl fieldImpl = new FieldImpl(defaultName(this.mFields.size()), this, "java.lang.Object");
        this.mFields.add(fieldImpl);
        return fieldImpl;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void removeField(MField field) {
        if (this.mFields == null) {
            return;
        }
        this.mFields.remove(field);
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public MField[] getMutableFields() {
        if (this.mFields == null || this.mFields.size() == 0) {
            return new MField[0];
        }
        MField[] out = new MField[this.mFields.size()];
        this.mFields.toArray(out);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public MMethod addNewMethod() {
        if (this.mMethods == null) {
            this.mMethods = new ArrayList();
        }
        MethodImpl methodImpl = new MethodImpl(defaultName(this.mMethods.size()), this);
        this.mMethods.add(methodImpl);
        return methodImpl;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void removeMethod(MMethod method) {
        if (this.mMethods == null) {
            return;
        }
        this.mMethods.remove(method);
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public MMethod[] getMutableMethods() {
        if (this.mMethods == null || this.mMethods.size() == 0) {
            return new MMethod[0];
        }
        MMethod[] out = new MMethod[this.mMethods.size()];
        this.mMethods.toArray(out);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public JProperty addNewProperty(String name, JMethod getter, JMethod setter) {
        if (this.mProperties == null) {
            this.mProperties = new ArrayList();
        }
        String typeName = getter != null ? getter.getReturnType().getFieldDescriptor() : setter.getParameters()[0].getType().getFieldDescriptor();
        PropertyImpl propertyImpl = new PropertyImpl(name, getter, setter, typeName);
        this.mProperties.add(propertyImpl);
        return propertyImpl;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void removeProperty(JProperty p) {
        if (this.mProperties != null) {
            this.mProperties.remove(p);
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public JProperty addNewDeclaredProperty(String name, JMethod getter, JMethod setter) {
        if (this.mDeclaredProperties == null) {
            this.mDeclaredProperties = new ArrayList();
        }
        String typeName = getter != null ? getter.getReturnType().getFieldDescriptor() : setter.getParameters()[0].getType().getFieldDescriptor();
        PropertyImpl propertyImpl = new PropertyImpl(name, getter, setter, typeName);
        this.mDeclaredProperties.add(propertyImpl);
        return propertyImpl;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void removeDeclaredProperty(JProperty p) {
        if (this.mDeclaredProperties != null) {
            this.mDeclaredProperties.remove(p);
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public MClass addNewInnerClass(String name) {
        int lastDot = name.lastIndexOf(46);
        if (lastDot == -1) {
            lastDot = name.lastIndexOf(36);
        }
        if (lastDot != -1) {
            name = name.substring(lastDot + 1);
        }
        ClassImpl inner = new ClassImpl(this.mPackageName, getSimpleName() + PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX + name, getImportSpecs(), this);
        if (this.mInnerClasses == null) {
            this.mInnerClasses = new ArrayList();
        }
        this.mInnerClasses.add(inner);
        inner.setState(6);
        ((JamClassLoaderImpl) getClassLoader()).addToCache(inner);
        return inner;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void removeInnerClass(MClass clazz) {
        if (this.mInnerClasses == null) {
            return;
        }
        this.mInnerClasses.remove(clazz);
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void setIsInterface(boolean b) {
        this.mIsInterface = b;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void setIsAnnotationType(boolean b) {
        this.mIsAnnotationType = b;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MClass
    public void setIsEnumType(boolean b) {
        this.mIsEnum = b;
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public String getQualifiedName() {
        return (this.mPackageName.length() > 0 ? this.mPackageName + '.' : "") + this.mSimpleName;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.classrefs.JClassRef
    public JClass getRefClass() {
        return this;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.classrefs.JClassRefContext
    public String getPackageName() {
        return this.mPackageName;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.classrefs.JClassRefContext
    public String[] getImportSpecs() {
        ensureLoaded();
        return this.mImports == null ? new String[0] : this.mImports;
    }

    public void setState(int state) {
        this.mState = state;
    }

    public static void validateClassName(String className) throws IllegalArgumentException {
        if (className == null) {
            throw new IllegalArgumentException("null class name specified");
        }
        if (!Character.isJavaIdentifierStart(className.charAt(0))) {
            throw new IllegalArgumentException("Invalid first character in class name: " + className);
        }
        for (int i = 1; i < className.length(); i++) {
            char c = className.charAt(i);
            if (c == '.') {
                if (className.charAt(i - 1) == '.') {
                    throw new IllegalArgumentException("'..' not allowed in class name: " + className);
                }
                if (i == className.length() - 1) {
                    throw new IllegalArgumentException("'.' not allowed at end of class name: " + className);
                }
            } else if (!Character.isJavaIdentifierPart(c)) {
                throw new IllegalArgumentException("Illegal character '" + c + "' in class name: " + className);
            }
        }
    }

    private boolean isAssignableFromRecursively(JClass arg) {
        if (getQualifiedName().equals(arg.getQualifiedName())) {
            return true;
        }
        JClass[] interfaces = arg.getInterfaces();
        if (interfaces != null) {
            for (JClass jClass : interfaces) {
                if (isAssignableFromRecursively(jClass)) {
                    return true;
                }
            }
        }
        JClass arg2 = arg.getSuperclass();
        return arg2 != null && isAssignableFromRecursively(arg2);
    }

    private void addFieldsRecursively(JClass clazz, Collection out) {
        JField[] fields = clazz.getDeclaredFields();
        for (JField jField : fields) {
            out.add(jField);
        }
        JClass[] ints = clazz.getInterfaces();
        for (JClass jClass : ints) {
            addFieldsRecursively(jClass, out);
        }
        JClass clazz2 = clazz.getSuperclass();
        if (clazz2 != null) {
            addFieldsRecursively(clazz2, out);
        }
    }

    private void addMethodsRecursively(JClass clazz, Collection out) {
        JMethod[] methods = clazz.getDeclaredMethods();
        for (JMethod jMethod : methods) {
            out.add(jMethod);
        }
        JClass[] ints = clazz.getInterfaces();
        for (JClass jClass : ints) {
            addMethodsRecursively(jClass, out);
        }
        JClass clazz2 = clazz.getSuperclass();
        if (clazz2 != null) {
            addMethodsRecursively(clazz2, out);
        }
    }

    public void ensureLoaded() {
        if (this.mState == 6) {
            return;
        }
        if (this.mState == 2) {
            if (this.mPopulator == null) {
                throw new IllegalStateException("null populator");
            }
            setState(3);
            this.mPopulator.populate(this);
            setState(4);
        }
        if (this.mState == 4) {
            setState(5);
            ((JamClassLoaderImpl) getClassLoader()).initialize(this);
        }
        setState(6);
    }
}
