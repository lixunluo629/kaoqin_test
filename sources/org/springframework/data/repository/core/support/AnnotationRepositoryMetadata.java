package org.springframework.data.repository.core.support;

import java.io.Serializable;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/AnnotationRepositoryMetadata.class */
public class AnnotationRepositoryMetadata extends AbstractRepositoryMetadata {
    private static final String NO_ANNOTATION_FOUND = String.format("Interface must be annotated with @%s!", RepositoryDefinition.class.getName());
    private final Class<? extends Serializable> idType;
    private final Class<?> domainType;

    public AnnotationRepositoryMetadata(Class<?> repositoryInterface) {
        super(repositoryInterface);
        Assert.isTrue(repositoryInterface.isAnnotationPresent(RepositoryDefinition.class), NO_ANNOTATION_FOUND);
        this.idType = resolveIdType(repositoryInterface);
        this.domainType = resolveDomainType(repositoryInterface);
    }

    @Override // org.springframework.data.repository.core.RepositoryMetadata
    public Class<? extends Serializable> getIdType() {
        return this.idType;
    }

    @Override // org.springframework.data.repository.core.RepositoryMetadata
    public Class<?> getDomainType() {
        return this.domainType;
    }

    private Class<? extends Serializable> resolveIdType(Class<?> repositoryInterface) {
        RepositoryDefinition annotation = (RepositoryDefinition) repositoryInterface.getAnnotation(RepositoryDefinition.class);
        if (annotation == null || annotation.idClass() == null) {
            throw new IllegalArgumentException(String.format("Could not resolve id type of %s!", repositoryInterface));
        }
        return annotation.idClass();
    }

    private Class<?> resolveDomainType(Class<?> repositoryInterface) {
        RepositoryDefinition annotation = (RepositoryDefinition) repositoryInterface.getAnnotation(RepositoryDefinition.class);
        if (annotation == null || annotation.domainClass() == null) {
            throw new IllegalArgumentException(String.format("Could not resolve domain type of %s!", repositoryInterface));
        }
        return annotation.domainClass();
    }
}
