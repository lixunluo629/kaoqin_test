package org.apache.commons.compress.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/utils/ServiceLoaderIterator.class */
public class ServiceLoaderIterator<E> implements Iterator<E> {
    private E nextServiceLoader;
    private final Class<E> service;
    private final Iterator<E> serviceLoaderIterator;

    public ServiceLoaderIterator(Class<E> service) {
        this(service, ClassLoader.getSystemClassLoader());
    }

    public ServiceLoaderIterator(Class<E> service, ClassLoader classLoader) {
        this.service = service;
        ServiceLoader<E> serviceLoader = ServiceLoader.load(service, classLoader);
        this.serviceLoaderIterator = serviceLoader.iterator();
        this.nextServiceLoader = null;
    }

    private boolean getNextServiceLoader() {
        while (this.nextServiceLoader == null) {
            try {
            } catch (ServiceConfigurationError e) {
                if (!(e.getCause() instanceof SecurityException)) {
                    throw e;
                }
            }
            if (!this.serviceLoaderIterator.hasNext()) {
                return false;
            }
            this.nextServiceLoader = this.serviceLoaderIterator.next();
        }
        return true;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return getNextServiceLoader();
    }

    @Override // java.util.Iterator
    public E next() {
        if (!getNextServiceLoader()) {
            throw new NoSuchElementException("No more elements for service " + this.service.getName());
        }
        E tempNext = this.nextServiceLoader;
        this.nextServiceLoader = null;
        return tempNext;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("service=" + this.service.getName());
    }
}
