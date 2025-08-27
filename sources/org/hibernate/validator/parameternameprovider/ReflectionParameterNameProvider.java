package org.hibernate.validator.parameternameprovider;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import javax.validation.ParameterNameProvider;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.IgnoreJava6Requirement;

@IgnoreJava6Requirement
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/parameternameprovider/ReflectionParameterNameProvider.class */
public class ReflectionParameterNameProvider implements ParameterNameProvider {
    @Override // javax.validation.ParameterNameProvider
    public List<String> getParameterNames(Constructor<?> constructor) {
        return getParameterNames(constructor.getParameters());
    }

    @Override // javax.validation.ParameterNameProvider
    public List<String> getParameterNames(Method method) {
        return getParameterNames(method.getParameters());
    }

    private List<String> getParameterNames(Parameter[] parameters) {
        List<String> parameterNames = CollectionHelper.newArrayList();
        for (Parameter parameter : parameters) {
            parameterNames.add(parameter.getName());
        }
        return parameterNames;
    }
}
