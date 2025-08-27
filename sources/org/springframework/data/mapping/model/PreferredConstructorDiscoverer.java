package org.springframework.data.mapping.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PreferredConstructor;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/PreferredConstructorDiscoverer.class */
public class PreferredConstructorDiscoverer<T, P extends PersistentProperty<P>> {
    private final ParameterNameDiscoverer discoverer;
    private PreferredConstructor<T, P> constructor;

    public PreferredConstructorDiscoverer(Class<T> type) {
        this(ClassTypeInformation.from(type), null);
    }

    public PreferredConstructorDiscoverer(PersistentEntity<T, P> entity) {
        this(entity.getTypeInformation(), entity);
    }

    protected PreferredConstructorDiscoverer(TypeInformation<T> type, PersistentEntity<T, P> entity) throws SecurityException {
        this.discoverer = new DefaultParameterNameDiscoverer();
        boolean noArgConstructorFound = false;
        int numberOfArgConstructors = 0;
        for (Constructor<?> candidate : type.getType().getDeclaredConstructors()) {
            PreferredConstructor<T, P> preferredConstructor = buildPreferredConstructor(candidate, type, entity);
            if (!preferredConstructor.getConstructor().isSynthetic()) {
                if (preferredConstructor.isExplicitlyAnnotated()) {
                    this.constructor = preferredConstructor;
                    return;
                }
                if (this.constructor == null || preferredConstructor.isNoArgConstructor()) {
                    this.constructor = preferredConstructor;
                }
                if (preferredConstructor.isNoArgConstructor()) {
                    noArgConstructorFound = true;
                } else {
                    numberOfArgConstructors++;
                }
            }
        }
        if (!noArgConstructorFound && numberOfArgConstructors > 1) {
            this.constructor = null;
        }
    }

    private PreferredConstructor<T, P> buildPreferredConstructor(Constructor<?> constructor, TypeInformation<T> typeInformation, PersistentEntity<T, P> entity) {
        List<TypeInformation<?>> parameterTypes = typeInformation.getParameterTypes(constructor);
        if (parameterTypes.isEmpty()) {
            return new PreferredConstructor<>(constructor, new PreferredConstructor.Parameter[0]);
        }
        String[] parameterNames = this.discoverer.getParameterNames(constructor);
        PreferredConstructor.Parameter<Object, P>[] parameters = new PreferredConstructor.Parameter[parameterTypes.size()];
        Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
        for (int i = 0; i < parameterTypes.size(); i++) {
            String name = parameterNames == null ? null : parameterNames[i];
            TypeInformation<?> type = parameterTypes.get(i);
            Annotation[] annotations = parameterAnnotations[i];
            parameters[i] = new PreferredConstructor.Parameter<>(name, type, annotations, entity);
        }
        return new PreferredConstructor<>(constructor, parameters);
    }

    public PreferredConstructor<T, P> getConstructor() {
        return this.constructor;
    }
}
