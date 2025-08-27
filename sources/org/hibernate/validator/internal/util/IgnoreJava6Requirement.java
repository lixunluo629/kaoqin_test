package org.hibernate.validator.internal.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/IgnoreJava6Requirement.class */
public @interface IgnoreJava6Requirement {
}
