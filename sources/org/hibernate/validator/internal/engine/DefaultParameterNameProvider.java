package org.hibernate.validator.internal.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import javax.validation.ParameterNameProvider;
import org.hibernate.validator.internal.util.CollectionHelper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/DefaultParameterNameProvider.class */
public class DefaultParameterNameProvider implements ParameterNameProvider {
    @Override // javax.validation.ParameterNameProvider
    public List<String> getParameterNames(Constructor<?> constructor) {
        return getParameterNames(constructor.getParameterTypes().length);
    }

    @Override // javax.validation.ParameterNameProvider
    public List<String> getParameterNames(Method method) {
        return getParameterNames(method.getParameterTypes().length);
    }

    private List<String> getParameterNames(int parameterCount) {
        List<String> parameterNames = CollectionHelper.newArrayList();
        for (int i = 0; i < parameterCount; i++) {
            parameterNames.add(getPrefix() + i);
        }
        return parameterNames;
    }

    protected String getPrefix() {
        return "arg";
    }
}
