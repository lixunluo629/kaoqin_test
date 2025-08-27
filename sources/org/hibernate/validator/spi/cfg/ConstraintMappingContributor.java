package org.hibernate.validator.spi.cfg;

import org.hibernate.validator.cfg.ConstraintMapping;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/spi/cfg/ConstraintMappingContributor.class */
public interface ConstraintMappingContributor {

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/spi/cfg/ConstraintMappingContributor$ConstraintMappingBuilder.class */
    public interface ConstraintMappingBuilder {
        ConstraintMapping addConstraintMapping();
    }

    void createConstraintMappings(ConstraintMappingBuilder constraintMappingBuilder);
}
