package springfox.documentation.schema;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeBindings;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Iterator;
import java.util.List;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/schema/WildcardType.class */
public class WildcardType {
    private WildcardType() {
        throw new UnsupportedOperationException();
    }

    public static boolean hasWildcards(ResolvedType type) {
        return Iterables.any(type.getTypeBindings().getTypeParameters(), thatAreWildcards());
    }

    public static boolean exactMatch(ResolvedType first, ResolvedType second) {
        return first.equals(second);
    }

    public static boolean wildcardMatch(ResolvedType toMatch, ResolvedType wildcardType) {
        if (!toMatch.getErasedType().equals(wildcardType.getErasedType())) {
            return false;
        }
        TypeBindings wildcardTypeBindings = wildcardType.getTypeBindings();
        TypeBindings bindingsToMatch = toMatch.getTypeBindings();
        for (int index = 0; index < bindingsToMatch.size(); index++) {
            ResolvedType wildcardTypeBindingsBoundType = wildcardTypeBindings.getBoundType(index);
            ResolvedType bindingsToMatchBoundType = bindingsToMatch.getBoundType(index);
            if (!wildcardTypeBindingsBoundType.getErasedType().equals(WildcardType.class) && !wildcardMatch(bindingsToMatchBoundType, wildcardTypeBindingsBoundType)) {
                return false;
            }
        }
        return true;
    }

    static ResolvedType replaceWildcardsFrom(Iterable<ResolvedType> replaceables, ResolvedType wildcardType) {
        Iterator<ResolvedType> replaceableIterator = replaceables.iterator();
        return breadthFirstReplace(replaceableIterator, wildcardType);
    }

    static List<ResolvedType> collectReplaceables(ResolvedType replacingType, ResolvedType wildcardType) {
        return breadthFirstSearch(replacingType, wildcardType);
    }

    private static Predicate<ResolvedType> thatAreWildcards() {
        return new Predicate<ResolvedType>() { // from class: springfox.documentation.schema.WildcardType.1
            @Override // com.google.common.base.Predicate
            public boolean apply(ResolvedType input) {
                return WildcardType.isWildcardType(input) || WildcardType.hasWildcards(input);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isWildcardType(ResolvedType input) {
        return WildcardType.class.equals(input.getErasedType());
    }

    private static ResolvedType breadthFirstReplace(Iterator<ResolvedType> replaceableIterator, ResolvedType wildcardType) {
        if (isWildcardType(wildcardType)) {
            if (replaceableIterator.hasNext()) {
                return replaceableIterator.next();
            }
            throw new IllegalStateException("Expecting the same number of wildcard types as the replaceables");
        }
        TypeBindings wildcardTypeBindings = wildcardType.getTypeBindings();
        List<Type> bindings = Lists.newArrayList();
        for (int index = 0; index < wildcardTypeBindings.size(); index++) {
            if (isWildcardType(wildcardTypeBindings.getBoundType(index))) {
                if (replaceableIterator.hasNext()) {
                    bindings.add(replaceableIterator.next());
                } else {
                    throw new IllegalStateException("Count of wildcards to candidates do not match");
                }
            } else {
                bindings.add(breadthFirstReplace(replaceableIterator, wildcardTypeBindings.getBoundType(index)));
            }
        }
        return new TypeResolver().resolve(wildcardType.getErasedType(), (Type[]) Iterables.toArray(bindings, Type.class));
    }

    private static List<ResolvedType> breadthFirstSearch(ResolvedType replacingType, ResolvedType wildcardType) {
        TypeBindings wildcardTypeBindings = wildcardType.getTypeBindings();
        TypeBindings bindingsToMatch = adjustedTypeBindings(replacingType);
        List<ResolvedType> bindings = Lists.newArrayList();
        for (int index = 0; index < bindingsToMatch.size(); index++) {
            if (isWildcardType(wildcardTypeBindings.getBoundType(index))) {
                bindings.add(bindingsToMatch.getBoundType(index));
            } else {
                bindings.addAll(breadthFirstSearch(bindingsToMatch.getBoundType(index), wildcardTypeBindings.getBoundType(index)));
            }
        }
        return bindings;
    }

    private static TypeBindings adjustedTypeBindings(ResolvedType replacingType) {
        TypeBindings typeBindings = replacingType.getTypeBindings();
        TypeVariable[] typeVariables = replacingType.getErasedType().getTypeParameters();
        FluentIterable<TypeVariable> remaining = FluentIterable.from(Lists.newArrayList(typeVariables)).skip(replacingType.getTypeBindings().size());
        Iterator<TypeVariable> it = remaining.iterator();
        while (it.hasNext()) {
            TypeVariable each = it.next();
            typeBindings = typeBindings.withAdditionalBinding(each.getName(), new TypeResolver().resolve(Object.class, new Type[0]));
        }
        return typeBindings;
    }
}
