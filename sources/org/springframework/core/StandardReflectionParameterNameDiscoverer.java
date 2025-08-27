package org.springframework.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import org.springframework.lang.UsesJava8;

@UsesJava8
/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/StandardReflectionParameterNameDiscoverer.class */
public class StandardReflectionParameterNameDiscoverer implements ParameterNameDiscoverer {
    @Override // org.springframework.core.ParameterNameDiscoverer
    public String[] getParameterNames(Method method) {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            if (!param.isNamePresent()) {
                return null;
            }
            parameterNames[i] = param.getName();
        }
        return parameterNames;
    }

    @Override // org.springframework.core.ParameterNameDiscoverer
    public String[] getParameterNames(Constructor<?> ctor) {
        Parameter[] parameters = ctor.getParameters();
        String[] parameterNames = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            if (!param.isNamePresent()) {
                return null;
            }
            parameterNames[i] = param.getName();
        }
        return parameterNames;
    }
}
