package org.hibernate.validator.cfg.context;

import java.lang.annotation.ElementType;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/context/PropertyTarget.class */
public interface PropertyTarget {
    PropertyConstraintMappingContext property(String str, ElementType elementType);
}
