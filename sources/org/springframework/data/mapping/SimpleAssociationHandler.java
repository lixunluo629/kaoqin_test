package org.springframework.data.mapping;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/SimpleAssociationHandler.class */
public interface SimpleAssociationHandler {
    void doWithAssociation(Association<? extends PersistentProperty<?>> association);
}
