package javax.validation.bootstrap;

import javax.validation.Configuration;
import javax.validation.ValidationProviderResolver;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/bootstrap/GenericBootstrap.class */
public interface GenericBootstrap {
    GenericBootstrap providerResolver(ValidationProviderResolver validationProviderResolver);

    Configuration<?> configure();
}
