package javax.validation;

import java.util.Locale;
import javax.validation.metadata.ConstraintDescriptor;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/MessageInterpolator.class */
public interface MessageInterpolator {

    /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/MessageInterpolator$Context.class */
    public interface Context {
        ConstraintDescriptor<?> getConstraintDescriptor();

        Object getValidatedValue();

        <T> T unwrap(Class<T> cls);
    }

    String interpolate(String str, Context context);

    String interpolate(String str, Context context, Locale locale);
}
