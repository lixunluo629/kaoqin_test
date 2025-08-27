package org.hibernate.validator.spi.group;

import java.util.List;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/spi/group/DefaultGroupSequenceProvider.class */
public interface DefaultGroupSequenceProvider<T> {
    List<Class<?>> getValidationGroups(T t);
}
