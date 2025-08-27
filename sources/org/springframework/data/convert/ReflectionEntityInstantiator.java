package org.springframework.data.convert;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PreferredConstructor;
import org.springframework.data.mapping.model.MappingInstantiationException;
import org.springframework.data.mapping.model.ParameterValueProvider;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ReflectionEntityInstantiator.class */
public enum ReflectionEntityInstantiator implements EntityInstantiator {
    INSTANCE;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.springframework.data.convert.EntityInstantiator
    public <T, E extends PersistentEntity<? extends T, P>, P extends PersistentProperty<P>> T createInstance(E e, ParameterValueProvider<P> parameterValueProvider) {
        PreferredConstructor<T, P> persistenceConstructor = e.getPersistenceConstructor();
        if (persistenceConstructor == null) {
            try {
                Class<T> type = e.getType();
                if (type.isArray()) {
                    Class<T> componentType = type;
                    int i = 0;
                    while (componentType.isArray()) {
                        componentType = componentType.getComponentType();
                        i++;
                    }
                    return (T) Array.newInstance((Class<?>) type, i);
                }
                return (T) BeanUtils.instantiateClass(e.getType());
            } catch (BeanInstantiationException e2) {
                throw new MappingInstantiationException(e, Collections.emptyList(), e2);
            }
        }
        ArrayList arrayList = new ArrayList();
        if (0 != parameterValueProvider && persistenceConstructor.hasParameters()) {
            Iterator<PreferredConstructor.Parameter<Object, P>> it = persistenceConstructor.getParameters().iterator();
            while (it.hasNext()) {
                arrayList.add(parameterValueProvider.getParameterValue(it.next()));
            }
        }
        try {
            return (T) BeanUtils.instantiateClass(persistenceConstructor.getConstructor(), arrayList.toArray());
        } catch (BeanInstantiationException e3) {
            throw new MappingInstantiationException(e, arrayList, e3);
        }
    }
}
