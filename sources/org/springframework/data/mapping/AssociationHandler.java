package org.springframework.data.mapping;

import org.springframework.data.mapping.PersistentProperty;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/AssociationHandler.class */
public interface AssociationHandler<P extends PersistentProperty<P>> {
    void doWithAssociation(Association<P> association);
}
