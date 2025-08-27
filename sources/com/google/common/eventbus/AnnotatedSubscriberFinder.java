package com.google.common.eventbus;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

/* loaded from: guava-18.0.jar:com/google/common/eventbus/AnnotatedSubscriberFinder.class */
class AnnotatedSubscriberFinder implements SubscriberFindingStrategy {
    private static final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<Class<?>, ImmutableList<Method>>() { // from class: com.google.common.eventbus.AnnotatedSubscriberFinder.1
        @Override // com.google.common.cache.CacheLoader
        public ImmutableList<Method> load(Class<?> concreteClass) throws Exception {
            return AnnotatedSubscriberFinder.getAnnotatedMethodsInternal(concreteClass);
        }
    });

    AnnotatedSubscriberFinder() {
    }

    @Override // com.google.common.eventbus.SubscriberFindingStrategy
    public Multimap<Class<?>, EventSubscriber> findAllSubscribers(Object listener) {
        Multimap<Class<?>, EventSubscriber> methodsInListener = HashMultimap.create();
        Class<?> clazz = listener.getClass();
        Iterator i$ = getAnnotatedMethods(clazz).iterator();
        while (i$.hasNext()) {
            Method method = (Method) i$.next();
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> eventType = parameterTypes[0];
            EventSubscriber subscriber = makeSubscriber(listener, method);
            methodsInListener.put(eventType, subscriber);
        }
        return methodsInListener;
    }

    private static ImmutableList<Method> getAnnotatedMethods(Class<?> clazz) {
        try {
            return subscriberMethodsCache.getUnchecked(clazz);
        } catch (UncheckedExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/eventbus/AnnotatedSubscriberFinder$MethodIdentifier.class */
    private static final class MethodIdentifier {
        private final String name;
        private final List<Class<?>> parameterTypes;

        MethodIdentifier(Method method) {
            this.name = method.getName();
            this.parameterTypes = Arrays.asList(method.getParameterTypes());
        }

        public int hashCode() {
            return Objects.hashCode(this.name, this.parameterTypes);
        }

        public boolean equals(@Nullable Object o) {
            if (o instanceof MethodIdentifier) {
                MethodIdentifier ident = (MethodIdentifier) o;
                return this.name.equals(ident.name) && this.parameterTypes.equals(ident.parameterTypes);
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ImmutableList<Method> getAnnotatedMethodsInternal(Class<?> clazz) throws SecurityException {
        Set<? extends Class<?>> supers = TypeToken.of((Class) clazz).getTypes().rawTypes();
        Map<MethodIdentifier, Method> identifiers = Maps.newHashMap();
        for (Class<?> superClazz : supers) {
            Method[] arr$ = superClazz.getMethods();
            for (Method superClazzMethod : arr$) {
                if (superClazzMethod.isAnnotationPresent(Subscribe.class) && !superClazzMethod.isBridge()) {
                    Class<?>[] parameterTypes = superClazzMethod.getParameterTypes();
                    if (parameterTypes.length != 1) {
                        String strValueOf = String.valueOf(String.valueOf(superClazzMethod));
                        throw new IllegalArgumentException(new StringBuilder(128 + strValueOf.length()).append("Method ").append(strValueOf).append(" has @Subscribe annotation, but requires ").append(parameterTypes.length).append(" arguments.  Event subscriber methods must require a single argument.").toString());
                    }
                    MethodIdentifier ident = new MethodIdentifier(superClazzMethod);
                    if (!identifiers.containsKey(ident)) {
                        identifiers.put(ident, superClazzMethod);
                    }
                }
            }
        }
        return ImmutableList.copyOf((Collection) identifiers.values());
    }

    private static EventSubscriber makeSubscriber(Object listener, Method method) {
        EventSubscriber wrapper;
        if (methodIsDeclaredThreadSafe(method)) {
            wrapper = new EventSubscriber(listener, method);
        } else {
            wrapper = new SynchronizedEventSubscriber(listener, method);
        }
        return wrapper;
    }

    private static boolean methodIsDeclaredThreadSafe(Method method) {
        return method.getAnnotation(AllowConcurrentEvents.class) != null;
    }
}
