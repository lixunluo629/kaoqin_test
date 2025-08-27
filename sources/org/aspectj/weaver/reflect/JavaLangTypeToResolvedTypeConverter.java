package org.aspectj.weaver.reflect;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.weaver.BoundedReferenceType;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.TypeFactory;
import org.aspectj.weaver.TypeVariableReferenceType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/JavaLangTypeToResolvedTypeConverter.class */
public class JavaLangTypeToResolvedTypeConverter {
    private Map<Type, TypeVariableReferenceType> typeVariablesInProgress = new HashMap();
    private final World world;

    public JavaLangTypeToResolvedTypeConverter(World aWorld) {
        this.world = aWorld;
    }

    private World getWorld() {
        return this.world;
    }

    public ResolvedType fromType(Type type) {
        ResolvedType bound;
        if (type instanceof Class) {
            Class clazz = (Class) type;
            String name = clazz.getName();
            if (clazz.isArray()) {
                UnresolvedType ut = UnresolvedType.forSignature(name.replace('.', '/'));
                return getWorld().resolve(ut);
            }
            return getWorld().resolve(name);
        }
        if (type instanceof ParameterizedType) {
            Type ownerType = ((ParameterizedType) type).getOwnerType();
            ParameterizedType parameterizedType = (ParameterizedType) type;
            ResolvedType baseType = fromType(parameterizedType.getRawType());
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            if (baseType.isSimpleType() && typeArguments.length == 0 && ownerType != null) {
                return baseType;
            }
            ResolvedType[] resolvedTypeArguments = fromTypes(typeArguments);
            return TypeFactory.createParameterizedType(baseType, resolvedTypeArguments, getWorld());
        }
        if (type instanceof TypeVariable) {
            TypeVariableReferenceType inprogressVar = this.typeVariablesInProgress.get(type);
            if (inprogressVar != null) {
                return inprogressVar;
            }
            TypeVariable tv = (TypeVariable) type;
            org.aspectj.weaver.TypeVariable rt_tv = new org.aspectj.weaver.TypeVariable(tv.getName());
            TypeVariableReferenceType tvrt = new TypeVariableReferenceType(rt_tv, getWorld());
            this.typeVariablesInProgress.put(type, tvrt);
            Type[] bounds = tv.getBounds();
            ResolvedType[] resBounds = fromTypes(bounds);
            ResolvedType upperBound = resBounds[0];
            ResolvedType[] additionalBounds = new ResolvedType[0];
            if (resBounds.length > 1) {
                additionalBounds = new ResolvedType[resBounds.length - 1];
                System.arraycopy(resBounds, 1, additionalBounds, 0, additionalBounds.length);
            }
            rt_tv.setUpperBound(upperBound);
            rt_tv.setAdditionalInterfaceBounds(additionalBounds);
            this.typeVariablesInProgress.remove(type);
            return tvrt;
        }
        if (type instanceof WildcardType) {
            WildcardType wildType = (WildcardType) type;
            Type[] lowerBounds = wildType.getLowerBounds();
            Type[] upperBounds = wildType.getUpperBounds();
            boolean isExtends = lowerBounds.length == 0;
            if (isExtends) {
                bound = fromType(upperBounds[0]);
            } else {
                bound = fromType(lowerBounds[0]);
            }
            return new BoundedReferenceType((ReferenceType) bound, isExtends, getWorld());
        }
        if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType) type;
            Type componentType = genericArrayType.getGenericComponentType();
            return UnresolvedType.makeArray(fromType(componentType), 1).resolve(getWorld());
        }
        return ResolvedType.MISSING;
    }

    public ResolvedType[] fromTypes(Type[] types) {
        ResolvedType[] ret = new ResolvedType[types.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = fromType(types[i]);
        }
        return ret;
    }
}
