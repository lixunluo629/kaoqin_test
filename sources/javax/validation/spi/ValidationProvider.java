package javax.validation.spi;

import javax.validation.Configuration;
import javax.validation.ValidatorFactory;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/spi/ValidationProvider.class */
public interface ValidationProvider<T extends Configuration<T>> {
    T createSpecializedConfiguration(BootstrapState bootstrapState);

    Configuration<?> createGenericConfiguration(BootstrapState bootstrapState);

    ValidatorFactory buildValidatorFactory(ConfigurationState configurationState);
}
