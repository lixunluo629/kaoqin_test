package org.hibernate.validator.internal.metadata.aggregated;

import org.hibernate.validator.internal.metadata.facets.Cascadable;
import org.hibernate.validator.internal.metadata.facets.Validatable;
import org.hibernate.validator.internal.util.CollectionHelper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/ValidatableParametersMetaData.class */
public class ValidatableParametersMetaData implements Validatable {
    private final Iterable<Cascadable> cascadables;

    public ValidatableParametersMetaData(Iterable<? extends Cascadable> cascadables) {
        this.cascadables = CollectionHelper.newHashSet(cascadables);
    }

    @Override // org.hibernate.validator.internal.metadata.facets.Validatable
    public Iterable<Cascadable> getCascadables() {
        return this.cascadables;
    }
}
