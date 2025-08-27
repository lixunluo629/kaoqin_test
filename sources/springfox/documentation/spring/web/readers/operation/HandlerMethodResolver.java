package springfox.documentation.spring.web.readers.operation;

import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedMethod;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spring.web.HandlerMethodReturnTypes;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/operation/HandlerMethodResolver.class */
public class HandlerMethodResolver {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) HandlerMethodResolver.class);
    private final TypeResolver typeResolver;

    public HandlerMethodResolver(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    public List<ResolvedMethodParameter> methodParameters(HandlerMethod methodToResolve) throws NegativeArraySizeException {
        Class hostClass = HandlerMethodReturnTypes.useType(methodToResolve.getBeanType()).or((Optional<Class>) methodToResolve.getMethod().getDeclaringClass());
        ResolvedMethod resolvedMethod = getResolvedMethod(methodToResolve.getMethod(), hostClass);
        List<ResolvedMethodParameter> parameters = Lists.newArrayList();
        MethodParameter[] methodParameters = methodToResolve.getMethodParameters();
        if (resolvedMethod != null) {
            if (methodParameters.length == resolvedMethod.getArgumentCount()) {
                for (int index = 0; index < resolvedMethod.getArgumentCount(); index++) {
                    MethodParameter methodParameter = methodParameters[index];
                    methodParameter.initParameterNameDiscovery(new LocalVariableTableParameterNameDiscoverer());
                    parameters.add(new ResolvedMethodParameter(methodParameter, resolvedMethod.getArgumentType(index)));
                }
            } else {
                log.warn(String.format("Problem trying to resolve a method named %s", methodToResolve.getMethod().getName()));
                log.warn(String.format("Method parameter count %s does not match resolved method argument count %s", Integer.valueOf(methodParameters.length), Integer.valueOf(resolvedMethod.getArgumentCount())));
            }
        }
        return parameters;
    }

    public ResolvedType methodReturnType(Method methodToResolve, Class<?> actualClass) throws NegativeArraySizeException {
        ResolvedMethod resolvedMethod = getResolvedMethod(methodToResolve, actualClass);
        if (resolvedMethod != null) {
            return returnTypeOrVoid(resolvedMethod);
        }
        return this.typeResolver.resolve(methodToResolve.getReturnType(), new Type[0]);
    }

    private static Predicate<ResolvedMethod> methodNamesAreSame(final Method methodToResolve) {
        return new Predicate<ResolvedMethod>() { // from class: springfox.documentation.spring.web.readers.operation.HandlerMethodResolver.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.base.Predicate
            public boolean apply(ResolvedMethod input) {
                return ((Method) input.getRawMember()).getName().equals(methodToResolve.getName());
            }
        };
    }

    @VisibleForTesting
    static Ordering<ResolvedMethod> byArgumentCount() {
        return Ordering.from(new Comparator<ResolvedMethod>() { // from class: springfox.documentation.spring.web.readers.operation.HandlerMethodResolver.2
            @Override // java.util.Comparator
            public int compare(ResolvedMethod first, ResolvedMethod second) {
                return Ints.compare(first.getArgumentCount(), second.getArgumentCount());
            }
        });
    }

    private static Iterable<ResolvedMethod> methodsWithSameNumberOfParams(Iterable<ResolvedMethod> filtered, final Method methodToResolve) {
        return Iterables.filter(filtered, new Predicate<ResolvedMethod>() { // from class: springfox.documentation.spring.web.readers.operation.HandlerMethodResolver.3
            @Override // com.google.common.base.Predicate
            public boolean apply(ResolvedMethod input) {
                return input.getArgumentCount() == methodToResolve.getParameterTypes().length;
            }
        });
    }

    @VisibleForTesting
    ResolvedMethod getResolvedMethod(Method methodToResolve, Class<?> beanType) throws NegativeArraySizeException {
        ResolvedType enclosingType = this.typeResolver.resolve(beanType, new Type[0]);
        MemberResolver resolver = new MemberResolver(this.typeResolver);
        resolver.setIncludeLangObject(false);
        ResolvedTypeWithMembers typeWithMembers = resolver.resolve(enclosingType, null, null);
        Iterable<ResolvedMethod> filtered = Iterables.filter(Lists.newArrayList(typeWithMembers.getMemberMethods()), methodNamesAreSame(methodToResolve));
        return resolveToMethodWithMaxResolvedTypes(filtered, methodToResolve);
    }

    private ResolvedMethod resolveToMethodWithMaxResolvedTypes(Iterable<ResolvedMethod> filtered, Method methodToResolve) {
        if (Iterables.size(filtered) > 1) {
            Iterable<ResolvedMethod> covariantMethods = covariantMethods(filtered, methodToResolve);
            if (Iterables.size(covariantMethods) == 0) {
                return (ResolvedMethod) byArgumentCount().max(filtered);
            }
            if (Iterables.size(covariantMethods) == 1) {
                return (ResolvedMethod) Iterables.getFirst(covariantMethods, null);
            }
            return (ResolvedMethod) byArgumentCount().max(covariantMethods);
        }
        return (ResolvedMethod) Iterables.getFirst(filtered, null);
    }

    private Iterable<ResolvedMethod> covariantMethods(Iterable<ResolvedMethod> filtered, Method methodToResolve) {
        return Iterables.filter(methodsWithSameNumberOfParams(filtered, methodToResolve), onlyCovariantMethods(methodToResolve));
    }

    private Predicate<ResolvedMethod> onlyCovariantMethods(final Method methodToResolve) {
        return new Predicate<ResolvedMethod>() { // from class: springfox.documentation.spring.web.readers.operation.HandlerMethodResolver.4
            @Override // com.google.common.base.Predicate
            public boolean apply(ResolvedMethod input) throws NegativeArraySizeException {
                for (int index = 0; index < input.getArgumentCount(); index++) {
                    if (!HandlerMethodResolver.this.covariant(input.getArgumentType(index), methodToResolve.getGenericParameterTypes()[index])) {
                        return false;
                    }
                }
                ResolvedType candidateMethodReturnValue = HandlerMethodResolver.this.returnTypeOrVoid(input);
                return HandlerMethodResolver.this.bothAreVoids(candidateMethodReturnValue, methodToResolve.getGenericReturnType()) || HandlerMethodResolver.this.contravariant(candidateMethodReturnValue, methodToResolve.getGenericReturnType());
            }
        };
    }

    @VisibleForTesting
    boolean bothAreVoids(ResolvedType candidateMethodReturnValue, Type returnType) {
        return (Void.class == candidateMethodReturnValue.getErasedType() || Void.TYPE == candidateMethodReturnValue.getErasedType()) && (Void.TYPE == returnType || Void.class == returnType);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ResolvedType returnTypeOrVoid(ResolvedMethod input) throws NegativeArraySizeException {
        ResolvedType returnType = input.getReturnType();
        if (returnType == null) {
            returnType = this.typeResolver.resolve(Void.class, new Type[0]);
        }
        return returnType;
    }

    boolean contravariant(ResolvedType candidateMethodReturnValue, Type returnValueOnMethod) {
        return isSubClass(candidateMethodReturnValue, returnValueOnMethod) || isGenericTypeSubclass(candidateMethodReturnValue, returnValueOnMethod);
    }

    @VisibleForTesting
    boolean isGenericTypeSubclass(ResolvedType candidateMethodReturnValue, Type returnValueOnMethod) {
        return (returnValueOnMethod instanceof ParameterizedType) && candidateMethodReturnValue.getErasedType().isAssignableFrom((Class) ((ParameterizedType) returnValueOnMethod).getRawType());
    }

    @VisibleForTesting
    boolean isSubClass(ResolvedType candidateMethodReturnValue, Type returnValueOnMethod) {
        return (returnValueOnMethod instanceof Class) && candidateMethodReturnValue.getErasedType().isAssignableFrom((Class) returnValueOnMethod);
    }

    @VisibleForTesting
    boolean covariant(ResolvedType candidateMethodArgument, Type argumentOnMethod) {
        return isSuperClass(candidateMethodArgument, argumentOnMethod) || isGenericTypeSuperClass(candidateMethodArgument, argumentOnMethod);
    }

    @VisibleForTesting
    boolean isGenericTypeSuperClass(ResolvedType candidateMethodArgument, Type argumentOnMethod) {
        return (argumentOnMethod instanceof ParameterizedType) && ((Class) ((ParameterizedType) argumentOnMethod).getRawType()).isAssignableFrom(candidateMethodArgument.getErasedType());
    }

    @VisibleForTesting
    boolean isSuperClass(ResolvedType candidateMethodArgument, Type argumentOnMethod) {
        return (argumentOnMethod instanceof Class) && ((Class) argumentOnMethod).isAssignableFrom(candidateMethodArgument.getErasedType());
    }
}
