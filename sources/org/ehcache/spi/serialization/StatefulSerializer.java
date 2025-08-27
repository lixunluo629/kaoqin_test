package org.ehcache.spi.serialization;

import org.ehcache.spi.persistence.StateRepository;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/serialization/StatefulSerializer.class */
public interface StatefulSerializer<T> extends Serializer<T> {
    void init(StateRepository stateRepository);
}
