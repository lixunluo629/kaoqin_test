package org.springframework.objenesis;

import org.springframework.objenesis.strategy.SerializingInstantiatorStrategy;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/objenesis/ObjenesisSerializer.class */
public class ObjenesisSerializer extends ObjenesisBase {
    public ObjenesisSerializer() {
        super(new SerializingInstantiatorStrategy());
    }

    public ObjenesisSerializer(boolean useCache) {
        super(new SerializingInstantiatorStrategy(), useCache);
    }
}
