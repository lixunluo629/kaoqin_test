package org.hibernate.validator.internal.metadata.location;

import java.lang.reflect.Member;
import java.lang.reflect.Type;
import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
import org.hibernate.validator.internal.util.ReflectionHelper;
import org.hibernate.validator.internal.util.TypeHelper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/location/ConstraintLocation.class */
public class ConstraintLocation {
    private final Member member;
    private final Class<?> declaringClass;
    private final Type typeForValidatorResolution;

    public static ConstraintLocation forClass(Class<?> declaringClass) {
        Type type = declaringClass.getTypeParameters().length == 0 ? declaringClass : TypeHelper.parameterizedType(declaringClass, declaringClass.getTypeParameters());
        return new ConstraintLocation(declaringClass, null, type);
    }

    public static ConstraintLocation forProperty(Member member) {
        return new ConstraintLocation(member.getDeclaringClass(), member, ReflectionHelper.typeOf(member));
    }

    public static ConstraintLocation forTypeArgument(Member member, Type type) {
        return new ConstraintLocation(member.getDeclaringClass(), member, type);
    }

    public static ConstraintLocation forReturnValue(ExecutableElement executable) {
        return new ConstraintLocation(executable.getMember().getDeclaringClass(), executable.getMember(), ReflectionHelper.typeOf(executable.getMember()));
    }

    public static ConstraintLocation forCrossParameter(ExecutableElement executable) {
        return new ConstraintLocation(executable.getMember().getDeclaringClass(), executable.getMember(), Object[].class);
    }

    public static ConstraintLocation forParameter(ExecutableElement executable, int index) {
        return new ConstraintLocation(executable.getMember().getDeclaringClass(), executable.getMember(), ReflectionHelper.typeOf(executable, index));
    }

    private ConstraintLocation(Class<?> declaringClass, Member member, Type typeOfAnnotatedElement) {
        this.declaringClass = declaringClass;
        this.member = member;
        if ((typeOfAnnotatedElement instanceof Class) && ((Class) typeOfAnnotatedElement).isPrimitive()) {
            this.typeForValidatorResolution = ReflectionHelper.boxedType((Class) typeOfAnnotatedElement);
        } else {
            this.typeForValidatorResolution = typeOfAnnotatedElement;
        }
    }

    public Class<?> getDeclaringClass() {
        return this.declaringClass;
    }

    public Member getMember() {
        return this.member;
    }

    public Type getTypeForValidatorResolution() {
        return this.typeForValidatorResolution;
    }

    public String toString() {
        return "ConstraintLocation [member=" + this.member + ", declaringClass=" + this.declaringClass + ", typeForValidatorResolution=" + this.typeForValidatorResolution + "]";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConstraintLocation that = (ConstraintLocation) o;
        if (!this.declaringClass.equals(that.declaringClass)) {
            return false;
        }
        if (this.member != null) {
            if (!this.member.equals(that.member)) {
                return false;
            }
        } else if (that.member != null) {
            return false;
        }
        if (!this.typeForValidatorResolution.equals(that.typeForValidatorResolution)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.member != null ? this.member.hashCode() : 0;
        return (31 * ((31 * result) + this.declaringClass.hashCode())) + this.typeForValidatorResolution.hashCode();
    }
}
