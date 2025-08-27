package org.hibernate.validator.internal.engine.valuehandling;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import java.lang.reflect.Type;
import org.hibernate.validator.internal.util.TypeResolutionHelper;
import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/valuehandling/TypeResolverBasedValueUnwrapper.class */
public abstract class TypeResolverBasedValueUnwrapper<T> extends ValidatedValueUnwrapper<T> {
    private final Class<?> clazz;
    private final TypeResolver typeResolver;

    TypeResolverBasedValueUnwrapper(TypeResolutionHelper typeResolutionHelper) {
        this.typeResolver = typeResolutionHelper.getTypeResolver();
        this.clazz = resolveSingleTypeParameter(this.typeResolver, getClass(), ValidatedValueUnwrapper.class);
    }

    @Override // org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper
    public Type getValidatedValueType(Type valueType) {
        return resolveSingleTypeParameter(this.typeResolver, valueType, this.clazz);
    }

    private static Class<?> resolveSingleTypeParameter(TypeResolver typeResolver, Type subType, Class<?> target) throws NegativeArraySizeException {
        ResolvedType resolvedType = typeResolver.resolve(subType, new Type[0]);
        return resolvedType.typeParametersFor(target).get(0).getErasedType();
    }
}
