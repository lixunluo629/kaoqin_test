package javax.validation.spi;

import javax.validation.ValidationProviderResolver;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/spi/BootstrapState.class */
public interface BootstrapState {
    ValidationProviderResolver getValidationProviderResolver();

    ValidationProviderResolver getDefaultValidationProviderResolver();
}
