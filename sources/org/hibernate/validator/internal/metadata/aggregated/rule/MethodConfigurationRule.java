package org.hibernate.validator.internal.metadata.aggregated.rule;

import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/rule/MethodConfigurationRule.class */
public abstract class MethodConfigurationRule {
    protected static final Log log = LoggerFactory.make();

    public abstract void apply(ConstrainedExecutable constrainedExecutable, ConstrainedExecutable constrainedExecutable2);

    protected boolean isStrictSubType(Class<?> clazz, Class<?> otherClazz) {
        return clazz.isAssignableFrom(otherClazz) && !clazz.equals(otherClazz);
    }

    protected boolean isDefinedOnSubType(ConstrainedExecutable executable, ConstrainedExecutable otherExecutable) {
        Class<?> clazz = executable.getLocation().getDeclaringClass();
        Class<?> otherClazz = otherExecutable.getLocation().getDeclaringClass();
        return isStrictSubType(clazz, otherClazz);
    }

    protected boolean isDefinedOnParallelType(ConstrainedExecutable executable, ConstrainedExecutable otherExecutable) {
        Class<?> clazz = executable.getLocation().getDeclaringClass();
        Class<?> otherClazz = otherExecutable.getLocation().getDeclaringClass();
        return (clazz.isAssignableFrom(otherClazz) || otherClazz.isAssignableFrom(clazz)) ? false : true;
    }
}
