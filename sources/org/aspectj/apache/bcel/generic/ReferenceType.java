package org.aspectj.apache.bcel.generic;

import org.aspectj.apache.bcel.Constants;
import org.aspectj.apache.bcel.Repository;
import org.aspectj.apache.bcel.classfile.JavaClass;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/ReferenceType.class */
public abstract class ReferenceType extends Type {
    protected ReferenceType(byte b, String str) {
        super(b, str);
    }

    ReferenceType() {
        super((byte) 14, "<null object>");
    }

    public boolean isCastableTo(Type type) {
        if (equals(Type.NULL)) {
            return true;
        }
        return isAssignmentCompatibleWith(type);
    }

    public boolean isAssignmentCompatibleWith(Type type) {
        if (!(type instanceof ReferenceType)) {
            return false;
        }
        ReferenceType referenceType = (ReferenceType) type;
        if (equals(Type.NULL)) {
            return true;
        }
        if ((this instanceof ObjectType) && ((ObjectType) this).referencesClass()) {
            if ((referenceType instanceof ObjectType) && ((ObjectType) referenceType).referencesClass() && (equals(referenceType) || Repository.instanceOf(((ObjectType) this).getClassName(), ((ObjectType) referenceType).getClassName()))) {
                return true;
            }
            if ((referenceType instanceof ObjectType) && ((ObjectType) referenceType).referencesInterface() && Repository.implementationOf(((ObjectType) this).getClassName(), ((ObjectType) referenceType).getClassName())) {
                return true;
            }
        }
        if ((this instanceof ObjectType) && ((ObjectType) this).referencesInterface()) {
            if ((referenceType instanceof ObjectType) && ((ObjectType) referenceType).referencesClass() && referenceType.equals(Type.OBJECT)) {
                return true;
            }
            if ((referenceType instanceof ObjectType) && ((ObjectType) referenceType).referencesInterface() && (equals(referenceType) || Repository.implementationOf(((ObjectType) this).getClassName(), ((ObjectType) referenceType).getClassName()))) {
                return true;
            }
        }
        if (!(this instanceof ArrayType)) {
            return false;
        }
        if ((referenceType instanceof ObjectType) && ((ObjectType) referenceType).referencesClass() && referenceType.equals(Type.OBJECT)) {
            return true;
        }
        if (referenceType instanceof ArrayType) {
            Type elementType = ((ArrayType) this).getElementType();
            Type elementType2 = ((ArrayType) this).getElementType();
            if ((elementType instanceof BasicType) && (elementType2 instanceof BasicType) && elementType.equals(elementType2)) {
                return true;
            }
            if ((elementType2 instanceof ReferenceType) && (elementType instanceof ReferenceType) && ((ReferenceType) elementType).isAssignmentCompatibleWith(elementType2)) {
                return true;
            }
        }
        if (!(referenceType instanceof ObjectType) || !((ObjectType) referenceType).referencesInterface()) {
            return false;
        }
        for (int i = 0; i < Constants.INTERFACES_IMPLEMENTED_BY_ARRAYS.length; i++) {
            if (referenceType.equals(new ObjectType(Constants.INTERFACES_IMPLEMENTED_BY_ARRAYS[i]))) {
                return true;
            }
        }
        return false;
    }

    public ReferenceType getFirstCommonSuperclass(ReferenceType referenceType) {
        if (equals(Type.NULL)) {
            return referenceType;
        }
        if (!referenceType.equals(Type.NULL) && !equals(referenceType)) {
            if ((this instanceof ArrayType) && (referenceType instanceof ArrayType)) {
                ArrayType arrayType = (ArrayType) this;
                ArrayType arrayType2 = (ArrayType) referenceType;
                if (arrayType.getDimensions() == arrayType2.getDimensions() && (arrayType.getBasicType() instanceof ObjectType) && (arrayType2.getBasicType() instanceof ObjectType)) {
                    return new ArrayType(((ObjectType) arrayType.getBasicType()).getFirstCommonSuperclass((ObjectType) arrayType2.getBasicType()), arrayType.getDimensions());
                }
            }
            if ((this instanceof ArrayType) || (referenceType instanceof ArrayType)) {
                return Type.OBJECT;
            }
            if (((this instanceof ObjectType) && ((ObjectType) this).referencesInterface()) || ((referenceType instanceof ObjectType) && ((ObjectType) referenceType).referencesInterface())) {
                return Type.OBJECT;
            }
            ObjectType objectType = (ObjectType) this;
            ObjectType objectType2 = (ObjectType) referenceType;
            JavaClass[] superClasses = Repository.lookupClass(objectType.getClassName()).getSuperClasses();
            JavaClass[] superClasses2 = Repository.lookupClass(objectType2.getClassName()).getSuperClasses();
            if (superClasses == null || superClasses2 == null) {
                return null;
            }
            JavaClass[] javaClassArr = new JavaClass[superClasses.length + 1];
            JavaClass[] javaClassArr2 = new JavaClass[superClasses2.length + 1];
            System.arraycopy(superClasses, 0, javaClassArr, 1, superClasses.length);
            System.arraycopy(superClasses2, 0, javaClassArr2, 1, superClasses2.length);
            javaClassArr[0] = Repository.lookupClass(objectType.getClassName());
            javaClassArr2[0] = Repository.lookupClass(objectType2.getClassName());
            for (JavaClass javaClass : javaClassArr2) {
                for (int i = 0; i < javaClassArr.length; i++) {
                    if (javaClassArr[i].equals(javaClass)) {
                        return new ObjectType(javaClassArr[i].getClassName());
                    }
                }
            }
            return null;
        }
        return this;
    }
}
