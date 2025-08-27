package org.springframework.expression.spel.ast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.expression.PropertyAccessor;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/ast/AstUtils.class */
public abstract class AstUtils {
    public static List<PropertyAccessor> getPropertyAccessorsToTry(Class<?> targetType, List<PropertyAccessor> propertyAccessors) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (PropertyAccessor resolver : propertyAccessors) {
            Class<?>[] targets = resolver.getSpecificTargetClasses();
            if (targets == null) {
                arrayList2.add(resolver);
            } else if (targetType != null) {
                int pos = 0;
                for (Class<?> clazz : targets) {
                    if (clazz == targetType) {
                        int i = pos;
                        pos++;
                        arrayList.add(i, resolver);
                    } else if (clazz.isAssignableFrom(targetType)) {
                        arrayList2.add(resolver);
                    }
                }
            }
        }
        List<PropertyAccessor> resolvers = new LinkedList<>();
        resolvers.addAll(arrayList);
        resolvers.addAll(arrayList2);
        return resolvers;
    }
}
