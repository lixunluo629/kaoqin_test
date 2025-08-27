package org.springframework.data.repository.init;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.repository.support.DefaultRepositoryInvokerFactory;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.repository.support.RepositoryInvoker;
import org.springframework.data.repository.support.RepositoryInvokerFactory;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/init/ResourceReaderRepositoryPopulator.class */
public class ResourceReaderRepositoryPopulator implements RepositoryPopulator, ApplicationEventPublisherAware {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) ResourceReaderRepositoryPopulator.class);
    private final ResourcePatternResolver resolver;
    private final ResourceReader reader;
    private final ClassLoader classLoader;
    private ApplicationEventPublisher publisher;
    private Collection<Resource> resources;

    public ResourceReaderRepositoryPopulator(ResourceReader reader) {
        this(reader, null);
    }

    public ResourceReaderRepositoryPopulator(ResourceReader reader, ClassLoader classLoader) {
        Assert.notNull(reader, "Reader must not be null!");
        this.reader = reader;
        this.classLoader = classLoader;
        this.resolver = classLoader == null ? new PathMatchingResourcePatternResolver() : new PathMatchingResourcePatternResolver(classLoader);
    }

    public void setResourceLocation(String location) throws IOException {
        Assert.hasText(location, "Location must not be null!");
        setResources(this.resolver.getResources(location));
    }

    public void setResources(Resource... resources) {
        this.resources = Arrays.asList(resources);
    }

    @Override // org.springframework.context.ApplicationEventPublisherAware
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override // org.springframework.data.repository.init.RepositoryPopulator
    public void populate(Repositories repositories) {
        RepositoryInvokerFactory invokerFactory = new DefaultRepositoryInvokerFactory(repositories);
        for (Resource resource : this.resources) {
            LOGGER.info(String.format("Reading resource: %s", resource));
            Object result = readObjectFrom(resource);
            if (result instanceof Collection) {
                for (Object element : (Collection) result) {
                    if (element != null) {
                        persist(element, invokerFactory);
                    } else {
                        LOGGER.info("Skipping null element found in unmarshal result!");
                    }
                }
            } else {
                persist(result, invokerFactory);
            }
        }
        if (this.publisher != null) {
            this.publisher.publishEvent((ApplicationEvent) new RepositoriesPopulatedEvent(this, repositories));
        }
    }

    private Object readObjectFrom(Resource resource) {
        try {
            return this.reader.readFrom(resource, this.classLoader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void persist(Object object, RepositoryInvokerFactory invokerFactory) {
        RepositoryInvoker invoker = invokerFactory.getInvokerFor(object.getClass());
        LOGGER.debug(String.format("Persisting %s using repository %s", object, invoker));
        invoker.invokeSave(object);
    }
}
