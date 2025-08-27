package org.hibernate.validator.internal.engine.valuehandling;

import java.util.Optional;
import org.hibernate.validator.internal.util.IgnoreJava6Requirement;
import org.hibernate.validator.internal.util.TypeResolutionHelper;

@IgnoreJava6Requirement
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/valuehandling/OptionalValueUnwrapper.class */
public class OptionalValueUnwrapper extends TypeResolverBasedValueUnwrapper<Optional<?>> {
    public OptionalValueUnwrapper(TypeResolutionHelper typeResolutionHelper) {
        super(typeResolutionHelper);
    }

    @Override // org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper
    public Object handleValidatedValue(Optional<?> value) {
        if (value != null && value.isPresent()) {
            return value.get();
        }
        return null;
    }
}
