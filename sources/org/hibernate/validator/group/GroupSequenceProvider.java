package org.hibernate.validator.group;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/group/GroupSequenceProvider.class */
public @interface GroupSequenceProvider {
    Class<? extends DefaultGroupSequenceProvider<?>> value();
}
