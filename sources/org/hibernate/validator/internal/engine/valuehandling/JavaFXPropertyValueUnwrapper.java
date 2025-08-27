package org.hibernate.validator.internal.engine.valuehandling;

import javafx.beans.value.ObservableValue;
import org.hibernate.validator.internal.util.IgnoreJava6Requirement;
import org.hibernate.validator.internal.util.TypeResolutionHelper;

@IgnoreJava6Requirement
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/valuehandling/JavaFXPropertyValueUnwrapper.class */
public class JavaFXPropertyValueUnwrapper extends TypeResolverBasedValueUnwrapper<ObservableValue<?>> {
    public JavaFXPropertyValueUnwrapper(TypeResolutionHelper typeResolutionHelper) {
        super(typeResolutionHelper);
    }

    @Override // org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper
    public Object handleValidatedValue(ObservableValue<?> value) {
        if (value != null) {
            return value.getValue();
        }
        return value;
    }
}
