package org.springframework.data.repository.core.support;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.repository.Repository;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/DefaultRepositoryMetadata.class */
public class DefaultRepositoryMetadata extends AbstractRepositoryMetadata {
    private static final String MUST_BE_A_REPOSITORY = String.format("Given type must be assignable to %s!", Repository.class);
    private final Class<? extends Serializable> idType;
    private final Class<?> domainType;

    public DefaultRepositoryMetadata(Class<?> repositoryInterface) {
        super(repositoryInterface);
        Assert.isTrue(Repository.class.isAssignableFrom(repositoryInterface), MUST_BE_A_REPOSITORY);
        this.idType = resolveIdType(repositoryInterface);
        this.domainType = resolveDomainType(repositoryInterface);
    }

    @Override // org.springframework.data.repository.core.RepositoryMetadata
    public Class<?> getDomainType() {
        return this.domainType;
    }

    @Override // org.springframework.data.repository.core.RepositoryMetadata
    public Class<? extends Serializable> getIdType() {
        return this.idType;
    }

    private Class<? extends Serializable> resolveIdType(Class<?> repositoryInterface) {
        TypeInformation<?> information = ClassTypeInformation.from(repositoryInterface);
        List<TypeInformation<?>> arguments = information.getSuperTypeInformation(Repository.class).getTypeArguments();
        if (arguments.size() < 2 || arguments.get(1) == null) {
            throw new IllegalArgumentException(String.format("Could not resolve id type of %s!", repositoryInterface));
        }
        return arguments.get(1).getType();
    }

    private Class<?> resolveDomainType(Class<?> repositoryInterface) {
        TypeInformation<?> information = ClassTypeInformation.from(repositoryInterface);
        List<TypeInformation<?>> arguments = information.getSuperTypeInformation(Repository.class).getTypeArguments();
        if (arguments.isEmpty() || arguments.get(0) == null) {
            throw new IllegalArgumentException(String.format("Could not resolve domain type of %s!", repositoryInterface));
        }
        return arguments.get(0).getType();
    }
}
