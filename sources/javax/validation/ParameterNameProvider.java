package javax.validation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/ParameterNameProvider.class */
public interface ParameterNameProvider {
    List<String> getParameterNames(Constructor<?> constructor);

    List<String> getParameterNames(Method method);
}
