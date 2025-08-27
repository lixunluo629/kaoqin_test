package org.springframework.data.mapping.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/context/PersistentEntities.class */
public class PersistentEntities implements Iterable<PersistentEntity<?, ?>> {
    private final Iterable<? extends MappingContext<?, ?>> contexts;

    public PersistentEntities(Iterable<? extends MappingContext<?, ?>> contexts) {
        Assert.notNull(contexts, "MappingContexts must not be null!");
        this.contexts = contexts;
    }

    public PersistentEntity<?, ?> getPersistentEntity(Class<?> type) {
        for (MappingContext<?, ?> context : this.contexts) {
            if (context.hasPersistentEntityFor(type)) {
                return context.getPersistentEntity(type);
            }
        }
        return null;
    }

    public Iterable<TypeInformation<?>> getManagedTypes() {
        Set<TypeInformation<?>> informations = new HashSet<>();
        for (MappingContext<?, ?> context : this.contexts) {
            informations.addAll(context.getManagedTypes());
        }
        return Collections.unmodifiableSet(informations);
    }

    @Override // java.lang.Iterable
    public Iterator<PersistentEntity<?, ?>> iterator() {
        ArrayList arrayList = new ArrayList();
        for (MappingContext<?, ?> context : this.contexts) {
            arrayList.addAll(context.getPersistentEntities());
        }
        return arrayList.iterator();
    }
}
