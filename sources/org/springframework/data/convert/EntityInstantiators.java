package org.springframework.data.convert;

import java.util.Collections;
import java.util.Map;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/EntityInstantiators.class */
public class EntityInstantiators {
    private final EntityInstantiator fallback;
    private final Map<Class<?>, EntityInstantiator> customInstantiators;

    public EntityInstantiators() {
        this((Map<Class<?>, EntityInstantiator>) Collections.emptyMap());
    }

    public EntityInstantiators(EntityInstantiator fallback) {
        this(fallback, Collections.emptyMap());
    }

    public EntityInstantiators(Map<Class<?>, EntityInstantiator> customInstantiators) {
        this(new ClassGeneratingEntityInstantiator(), customInstantiators);
    }

    public EntityInstantiators(EntityInstantiator defaultInstantiator, Map<Class<?>, EntityInstantiator> customInstantiators) {
        Assert.notNull(defaultInstantiator, "DefaultInstantiator must not be null!");
        Assert.notNull(customInstantiators, "CustomInstantiators must not be null!");
        this.fallback = defaultInstantiator;
        this.customInstantiators = customInstantiators;
    }

    public EntityInstantiator getInstantiatorFor(PersistentEntity<?, ?> entity) {
        Assert.notNull(entity, "Entity must not be null!");
        Class<?> type = entity.getType();
        if (!this.customInstantiators.containsKey(type)) {
            return this.fallback;
        }
        EntityInstantiator instantiator = this.customInstantiators.get(entity.getType());
        return instantiator == null ? this.fallback : instantiator;
    }
}
