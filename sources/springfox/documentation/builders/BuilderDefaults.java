package springfox.documentation.builders;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Optional;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/BuilderDefaults.class */
public class BuilderDefaults {
    private BuilderDefaults() {
        throw new UnsupportedOperationException();
    }

    public static <T> T defaultIfAbsent(T newValue, T defaultValue) {
        return Optional.fromNullable(newValue).or((Optional) Optional.fromNullable(defaultValue)).orNull();
    }

    public static <T> List<T> nullToEmptyList(Collection<T> newValue) {
        if (newValue == null) {
            return Lists.newArrayList();
        }
        return Lists.newArrayList(newValue);
    }

    public static <K, V> Map<K, V> nullToEmptyMap(Map<K, V> newValue) {
        if (newValue == null) {
            return Maps.newHashMap();
        }
        return newValue;
    }

    public static <K, V> Multimap<K, V> nullToEmptyMultimap(Multimap<K, V> newValue) {
        if (newValue == null) {
            return LinkedListMultimap.create();
        }
        return newValue;
    }

    public static <T> Set<T> nullToEmptySet(Set<T> newValue) {
        if (newValue == null) {
            return Sets.newHashSet();
        }
        return newValue;
    }

    public static ResolvedType replaceIfMoreSpecific(ResolvedType replacement, ResolvedType defaultValue) {
        ResolvedType replacement2 = (ResolvedType) defaultIfAbsent(replacement, defaultValue);
        if (isObject(replacement2) && isNotObject(defaultValue)) {
            return defaultValue;
        }
        return replacement2;
    }

    private static boolean isNotObject(ResolvedType defaultValue) {
        return (defaultValue == null || Object.class.equals(defaultValue.getErasedType())) ? false : true;
    }

    private static boolean isObject(ResolvedType replacement) {
        return replacement != null && Object.class.equals(replacement.getErasedType());
    }
}
