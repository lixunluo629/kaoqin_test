package org.springframework.data.mapping;

import org.springframework.data.mapping.PersistentProperty;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/Association.class */
public class Association<P extends PersistentProperty<P>> {
    private final P inverse;
    private final P obverse;

    public Association(P inverse, P obverse) {
        this.inverse = inverse;
        this.obverse = obverse;
    }

    public P getInverse() {
        return this.inverse;
    }

    public P getObverse() {
        return this.obverse;
    }
}
