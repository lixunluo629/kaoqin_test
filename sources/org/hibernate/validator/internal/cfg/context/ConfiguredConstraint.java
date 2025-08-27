package org.hibernate.validator.internal.cfg.context;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Map;
import org.hibernate.validator.cfg.ConstraintDef;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
import org.hibernate.validator.internal.util.annotationfactory.AnnotationDescriptor;
import org.hibernate.validator.internal.util.annotationfactory.AnnotationFactory;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/cfg/context/ConfiguredConstraint.class */
class ConfiguredConstraint<A extends Annotation> {
    private static final Log log = LoggerFactory.make();
    private final ConstraintDefAccessor<A> constraint;
    private final ConstraintLocation location;
    private final ElementType elementType;

    private ConfiguredConstraint(ConstraintDef<?, A> constraint, ConstraintLocation location, ElementType elementType) {
        this.constraint = new ConstraintDefAccessor<>(constraint);
        this.location = location;
        this.elementType = elementType;
    }

    static <A extends Annotation> ConfiguredConstraint<A> forType(ConstraintDef<?, A> constraint, Class<?> beanType) {
        return new ConfiguredConstraint<>(constraint, ConstraintLocation.forClass(beanType), ElementType.TYPE);
    }

    static <A extends Annotation> ConfiguredConstraint<A> forProperty(ConstraintDef<?, A> constraint, Member member) {
        return new ConfiguredConstraint<>(constraint, ConstraintLocation.forProperty(member), member instanceof Field ? ElementType.FIELD : ElementType.METHOD);
    }

    public static <A extends Annotation> ConfiguredConstraint<A> forParameter(ConstraintDef<?, A> constraint, ExecutableElement executable, int parameterIndex) {
        return new ConfiguredConstraint<>(constraint, ConstraintLocation.forParameter(executable, parameterIndex), executable.getElementType());
    }

    public static <A extends Annotation> ConfiguredConstraint<A> forReturnValue(ConstraintDef<?, A> constraint, ExecutableElement executable) {
        return new ConfiguredConstraint<>(constraint, ConstraintLocation.forReturnValue(executable), executable.getElementType());
    }

    public static <A extends Annotation> ConfiguredConstraint<A> forCrossParameter(ConstraintDef<?, A> constraint, ExecutableElement executable) {
        return new ConfiguredConstraint<>(constraint, ConstraintLocation.forCrossParameter(executable), executable.getElementType());
    }

    public ConstraintDef<?, A> getConstraint() {
        return this.constraint;
    }

    public ConstraintLocation getLocation() {
        return this.location;
    }

    public Class<A> getConstraintType() {
        return this.constraint.getConstraintType();
    }

    public Map<String, Object> getParameters() {
        return this.constraint.getParameters();
    }

    public A createAnnotationProxy() {
        AnnotationDescriptor annotationDescriptor = new AnnotationDescriptor(getConstraintType());
        for (Map.Entry<String, Object> entry : getParameters().entrySet()) {
            annotationDescriptor.setValue(entry.getKey(), entry.getValue());
        }
        try {
            return (A) AnnotationFactory.create(annotationDescriptor);
        } catch (RuntimeException e) {
            throw log.getUnableToCreateAnnotationForConfiguredConstraintException(e);
        }
    }

    public String toString() {
        return this.constraint.toString();
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/cfg/context/ConfiguredConstraint$ConstraintDefAccessor.class */
    private static class ConstraintDefAccessor<A extends Annotation> extends ConstraintDef<ConstraintDefAccessor<A>, A> {
        private ConstraintDefAccessor(ConstraintDef<?, A> original) {
            super(original);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Class<A> getConstraintType() {
            return this.constraintType;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Map<String, Object> getParameters() {
            return this.parameters;
        }
    }

    public ElementType getElementType() {
        return this.elementType;
    }
}
