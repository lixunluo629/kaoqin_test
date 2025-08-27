package org.springframework.expression.spel.support;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.MethodExecutor;
import org.springframework.expression.MethodFilter;
import org.springframework.expression.MethodResolver;
import org.springframework.expression.TypeConverter;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelMessage;
import org.springframework.expression.spel.support.ReflectionHelper;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/support/ReflectiveMethodResolver.class */
public class ReflectiveMethodResolver implements MethodResolver {
    private final boolean useDistance;
    private Map<Class<?>, MethodFilter> filters;

    public ReflectiveMethodResolver() {
        this.useDistance = true;
    }

    public ReflectiveMethodResolver(boolean useDistance) {
        this.useDistance = useDistance;
    }

    public void registerMethodFilter(Class<?> type, MethodFilter filter) {
        if (this.filters == null) {
            this.filters = new HashMap();
        }
        if (filter != null) {
            this.filters.put(type, filter);
        } else {
            this.filters.remove(type);
        }
    }

    @Override // org.springframework.expression.MethodResolver
    public MethodExecutor resolve(EvaluationContext context, Object targetObject, String name, List<TypeDescriptor> argumentTypes) throws AccessException {
        try {
            TypeConverter typeConverter = context.getTypeConverter();
            Class<?> type = targetObject instanceof Class ? (Class) targetObject : targetObject.getClass();
            List<Method> methods = new ArrayList<>(getMethods(type, targetObject));
            MethodFilter filter = this.filters != null ? this.filters.get(type) : null;
            if (filter != null) {
                List<Method> filtered = filter.filter(methods);
                methods = filtered instanceof ArrayList ? filtered : new ArrayList<>(filtered);
            }
            if (methods.size() > 1) {
                Collections.sort(methods, new Comparator<Method>() { // from class: org.springframework.expression.spel.support.ReflectiveMethodResolver.1
                    @Override // java.util.Comparator
                    public int compare(Method m1, Method m2) {
                        int m1pl = m1.getParameterTypes().length;
                        int m2pl = m2.getParameterTypes().length;
                        if (m1pl == m2pl) {
                            if (!m1.isVarArgs() && m2.isVarArgs()) {
                                return -1;
                            }
                            if (m1.isVarArgs() && !m2.isVarArgs()) {
                                return 1;
                            }
                            return 0;
                        }
                        if (m1pl < m2pl) {
                            return -1;
                        }
                        return m1pl > m2pl ? 1 : 0;
                    }
                });
            }
            for (int i = 0; i < methods.size(); i++) {
                methods.set(i, BridgeMethodResolver.findBridgedMethod(methods.get(i)));
            }
            Set<Method> methodsToIterate = new LinkedHashSet<>(methods);
            Method closeMatch = null;
            int closeMatchDistance = Integer.MAX_VALUE;
            Method matchRequiringConversion = null;
            boolean multipleOptions = false;
            for (Method method : methodsToIterate) {
                if (method.getName().equals(name)) {
                    Class<?>[] paramTypes = method.getParameterTypes();
                    List<TypeDescriptor> paramDescriptors = new ArrayList<>(paramTypes.length);
                    for (int i2 = 0; i2 < paramTypes.length; i2++) {
                        paramDescriptors.add(new TypeDescriptor(new MethodParameter(method, i2)));
                    }
                    ReflectionHelper.ArgumentsMatchInfo matchInfo = null;
                    if (method.isVarArgs() && argumentTypes.size() >= paramTypes.length - 1) {
                        matchInfo = ReflectionHelper.compareArgumentsVarargs(paramDescriptors, argumentTypes, typeConverter);
                    } else if (paramTypes.length == argumentTypes.size()) {
                        matchInfo = ReflectionHelper.compareArguments(paramDescriptors, argumentTypes, typeConverter);
                    }
                    if (matchInfo == null) {
                        continue;
                    } else {
                        if (matchInfo.isExactMatch()) {
                            return new ReflectiveMethodExecutor(method);
                        }
                        if (matchInfo.isCloseMatch()) {
                            if (this.useDistance) {
                                int matchDistance = ReflectionHelper.getTypeDifferenceWeight(paramDescriptors, argumentTypes);
                                if (closeMatch == null || matchDistance < closeMatchDistance) {
                                    closeMatch = method;
                                    closeMatchDistance = matchDistance;
                                }
                            } else if (closeMatch == null) {
                                closeMatch = method;
                            }
                        } else if (matchInfo.isMatchRequiringConversion()) {
                            if (matchRequiringConversion != null) {
                                multipleOptions = true;
                            }
                            matchRequiringConversion = method;
                        }
                    }
                }
            }
            if (closeMatch != null) {
                return new ReflectiveMethodExecutor(closeMatch);
            }
            if (matchRequiringConversion != null) {
                if (multipleOptions) {
                    throw new SpelEvaluationException(SpelMessage.MULTIPLE_POSSIBLE_METHODS, name);
                }
                return new ReflectiveMethodExecutor(matchRequiringConversion);
            }
            return null;
        } catch (EvaluationException ex) {
            throw new AccessException("Failed to resolve method", ex);
        }
    }

    private Set<Method> getMethods(Class<?> type, Object targetObject) {
        if (targetObject instanceof Class) {
            Set<Method> result = new LinkedHashSet<>();
            Method[] methods = getMethods(type);
            for (Method method : methods) {
                if (Modifier.isStatic(method.getModifiers())) {
                    result.add(method);
                }
            }
            result.addAll(Arrays.asList(getMethods(Class.class)));
            return result;
        }
        if (Proxy.isProxyClass(type)) {
            Set<Method> result2 = new LinkedHashSet<>();
            for (Class<?> ifc : type.getInterfaces()) {
                Method[] methods2 = getMethods(ifc);
                for (Method method2 : methods2) {
                    if (isCandidateForInvocation(method2, type)) {
                        result2.add(method2);
                    }
                }
            }
            return result2;
        }
        Set<Method> result3 = new LinkedHashSet<>();
        Method[] methods3 = getMethods(type);
        for (Method method3 : methods3) {
            if (isCandidateForInvocation(method3, type)) {
                result3.add(method3);
            }
        }
        return result3;
    }

    protected Method[] getMethods(Class<?> type) {
        return type.getMethods();
    }

    protected boolean isCandidateForInvocation(Method method, Class<?> targetClass) {
        return true;
    }
}
