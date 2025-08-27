package javax.validation;

import java.util.List;
import javax.validation.spi.ValidationProvider;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/ValidationProviderResolver.class */
public interface ValidationProviderResolver {
    List<ValidationProvider<?>> getValidationProviders();
}
