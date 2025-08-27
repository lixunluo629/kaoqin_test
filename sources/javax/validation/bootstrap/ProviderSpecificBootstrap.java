package javax.validation.bootstrap;

import javax.validation.Configuration;
import javax.validation.ValidationProviderResolver;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/bootstrap/ProviderSpecificBootstrap.class */
public interface ProviderSpecificBootstrap<T extends Configuration<T>> {
    ProviderSpecificBootstrap<T> providerResolver(ValidationProviderResolver validationProviderResolver);

    T configure();
}
