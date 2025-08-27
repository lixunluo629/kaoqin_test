package com.google.common.reflect;

import com.google.common.collect.Sets;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Set;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
/* loaded from: guava-18.0.jar:com/google/common/reflect/TypeVisitor.class */
abstract class TypeVisitor {
    private final Set<Type> visited = Sets.newHashSet();

    TypeVisitor() {
    }

    public final void visit(Type... types) {
        int len$ = types.length;
        for (int i$ = 0; i$ < len$; i$++) {
            Type type = types[i$];
            if (type != null && this.visited.add(type)) {
                boolean succeeded = false;
                try {
                    if (type instanceof TypeVariable) {
                        visitTypeVariable((TypeVariable) type);
                    } else if (type instanceof WildcardType) {
                        visitWildcardType((WildcardType) type);
                    } else if (type instanceof ParameterizedType) {
                        visitParameterizedType((ParameterizedType) type);
                    } else if (type instanceof Class) {
                        visitClass((Class) type);
                    } else if (type instanceof GenericArrayType) {
                        visitGenericArrayType((GenericArrayType) type);
                    } else {
                        String strValueOf = String.valueOf(String.valueOf(type));
                        throw new AssertionError(new StringBuilder(14 + strValueOf.length()).append("Unknown type: ").append(strValueOf).toString());
                    }
                    succeeded = true;
                } finally {
                    if (!succeeded) {
                        this.visited.remove(type);
                    }
                }
            }
        }
    }

    void visitClass(Class<?> t) {
    }

    void visitGenericArrayType(GenericArrayType t) {
    }

    void visitParameterizedType(ParameterizedType t) {
    }

    void visitTypeVariable(TypeVariable<?> t) {
    }

    void visitWildcardType(WildcardType t) {
    }
}
