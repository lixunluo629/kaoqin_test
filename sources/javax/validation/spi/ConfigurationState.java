package javax.validation.spi;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/spi/ConfigurationState.class */
public interface ConfigurationState {
    boolean isIgnoreXmlConfiguration();

    MessageInterpolator getMessageInterpolator();

    Set<InputStream> getMappingStreams();

    ConstraintValidatorFactory getConstraintValidatorFactory();

    TraversableResolver getTraversableResolver();

    ParameterNameProvider getParameterNameProvider();

    Map<String, String> getProperties();
}
