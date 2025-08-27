package org.springframework.data.repository.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/DefaultRepositoryInvokerFactory.class */
public class DefaultRepositoryInvokerFactory implements RepositoryInvokerFactory {
    private final Repositories repositories;
    private final ConversionService conversionService;
    private final Map<Class<?>, RepositoryInvoker> invokers;

    public DefaultRepositoryInvokerFactory(Repositories repositories) {
        this(repositories, new DefaultFormattingConversionService());
    }

    public DefaultRepositoryInvokerFactory(Repositories repositories, ConversionService conversionService) {
        Assert.notNull(repositories, "Repositories must not be null!");
        Assert.notNull(conversionService, "ConversionService must not be null!");
        this.repositories = repositories;
        this.conversionService = conversionService;
        this.invokers = new ConcurrentHashMap();
    }

    @Override // org.springframework.data.repository.support.RepositoryInvokerFactory
    public RepositoryInvoker getInvokerFor(Class<?> domainType) {
        RepositoryInvoker invoker = this.invokers.get(domainType);
        if (invoker != null) {
            return invoker;
        }
        RepositoryInvoker invoker2 = prepareInvokers(domainType);
        this.invokers.put(domainType, invoker2);
        return invoker2;
    }

    private RepositoryInvoker prepareInvokers(Class<?> domainType) {
        Object repository = this.repositories.getRepositoryFor(domainType);
        Assert.notNull(repository, String.format("No repository found for domain type: %s", domainType));
        RepositoryInformation information = this.repositories.getRepositoryInformationFor(domainType);
        return createInvoker(information, repository);
    }

    protected RepositoryInvoker createInvoker(RepositoryInformation information, Object repository) {
        if (repository instanceof PagingAndSortingRepository) {
            return new PagingAndSortingRepositoryInvoker((PagingAndSortingRepository) repository, information, this.conversionService);
        }
        if (repository instanceof CrudRepository) {
            return new CrudRepositoryInvoker((CrudRepository) repository, information, this.conversionService);
        }
        return new ReflectionRepositoryInvoker(repository, information, this.conversionService);
    }
}
