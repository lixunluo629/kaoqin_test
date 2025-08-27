package org.hibernate.validator.internal.cfg.context;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.util.CollectionHelper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/cfg/context/ConstraintMappingContextImplBase.class */
abstract class ConstraintMappingContextImplBase extends ConstraintContextImplBase {
    private final Set<ConfiguredConstraint<?>> constraints;

    protected abstract ConstraintDescriptorImpl.ConstraintType getConstraintType();

    ConstraintMappingContextImplBase(DefaultConstraintMapping mapping) {
        super(mapping);
        this.constraints = CollectionHelper.newHashSet();
    }

    protected DefaultConstraintMapping getConstraintMapping() {
        return this.mapping;
    }

    protected void addConstraint(ConfiguredConstraint<?> constraint) {
        this.constraints.add(constraint);
    }

    protected Set<MetaConstraint<?>> getConstraints(ConstraintHelper constraintHelper) {
        if (this.constraints == null) {
            return Collections.emptySet();
        }
        Set<MetaConstraint<?>> metaConstraints = CollectionHelper.newHashSet();
        for (ConfiguredConstraint<?> configuredConstraint : this.constraints) {
            metaConstraints.add(asMetaConstraint(configuredConstraint, constraintHelper));
        }
        return metaConstraints;
    }

    private <A extends Annotation> MetaConstraint<A> asMetaConstraint(ConfiguredConstraint<A> config, ConstraintHelper constraintHelper) {
        ConstraintDescriptorImpl<A> constraintDescriptor = new ConstraintDescriptorImpl<>(constraintHelper, config.getLocation().getMember(), config.createAnnotationProxy(), config.getElementType(), getConstraintType());
        return new MetaConstraint<>(constraintDescriptor, config.getLocation());
    }
}
