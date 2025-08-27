package org.ehcache.core.internal.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.ehcache.config.Builder;
import org.ehcache.core.internal.util.ClassLoading;
import org.ehcache.core.spi.service.ServiceFactory;
import org.ehcache.spi.service.PluralService;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.spi.service.ServiceCreationConfiguration;
import org.ehcache.spi.service.ServiceDependencies;
import org.ehcache.spi.service.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/internal/service/ServiceLocator.class */
public final class ServiceLocator implements ServiceProvider<Service> {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) ServiceLocator.class);
    private final ServiceMap services;
    private final ReadWriteLock runningLock;
    private final AtomicBoolean running;

    public static DependencySet dependencySet() {
        return new DependencySet();
    }

    private ServiceLocator(ServiceMap services) {
        this.runningLock = new ReentrantReadWriteLock();
        this.running = new AtomicBoolean(false);
        this.services = services;
    }

    @Override // org.ehcache.spi.service.ServiceProvider
    public <T extends Service> T getService(Class<T> serviceType) {
        if (serviceType.isAnnotationPresent(PluralService.class)) {
            throw new IllegalArgumentException(serviceType.getName() + " is marked as a PluralService");
        }
        Collection<T> registeredServices = getServicesOfType(serviceType);
        if (registeredServices.size() > 1) {
            throw new AssertionError("The non-PluralService type" + serviceType.getName() + " has more than one service registered");
        }
        if (registeredServices.isEmpty()) {
            return null;
        }
        return registeredServices.iterator().next();
    }

    @Override // org.ehcache.spi.service.ServiceProvider
    public <T extends Service> Collection<T> getServicesOfType(Class<T> serviceType) {
        return this.services.get(serviceType);
    }

    public boolean knowsServiceFor(ServiceConfiguration<?> serviceConfig) {
        return this.services.contains(serviceConfig.getServiceType());
    }

    public void startAllServices() throws Exception {
        Deque<Service> started = new LinkedList<>();
        Lock lock = this.runningLock.writeLock();
        lock.lock();
        try {
            try {
                if (!this.running.compareAndSet(false, true)) {
                    throw new IllegalStateException("Already started!");
                }
                LinkedList<Service> unstarted = new LinkedList<>(this.services.all());
                int totalServices = unstarted.size();
                long start = System.currentTimeMillis();
                LOGGER.debug("Starting {} Services...", Integer.valueOf(totalServices));
                while (!unstarted.isEmpty()) {
                    boolean startedSomething = false;
                    Iterator<Service> it = unstarted.iterator();
                    while (it.hasNext()) {
                        Service s = it.next();
                        if (hasUnstartedDependencies(s, unstarted)) {
                            LOGGER.trace("Delaying starting {}", s);
                        } else {
                            LOGGER.trace("Starting {}", s);
                            s.start(this);
                            started.push(s);
                            it.remove();
                            startedSomething = true;
                        }
                    }
                    if (startedSomething) {
                        LOGGER.trace("Cycle complete: " + unstarted.size() + " Services remaining");
                    } else {
                        throw new IllegalStateException("Cyclic dependency in Service set: " + unstarted);
                    }
                }
                LOGGER.debug("All Services successfully started, {} Services in {}ms", Integer.valueOf(totalServices), Long.valueOf(System.currentTimeMillis() - start));
                lock.unlock();
            } catch (Exception e) {
                while (!started.isEmpty()) {
                    Service toBeStopped = started.pop();
                    try {
                        toBeStopped.stop();
                    } catch (Exception e1) {
                        LOGGER.error("Stopping Service failed due to ", (Throwable) e1);
                    }
                }
                throw e;
            }
        } catch (Throwable th) {
            lock.unlock();
            throw th;
        }
    }

    public void stopAllServices() throws Exception {
        Exception firstException = null;
        Lock lock = this.runningLock.writeLock();
        lock.lock();
        try {
            if (!this.running.compareAndSet(true, false)) {
                throw new IllegalStateException("Already stopped!");
            }
            Collection<Service> running = new LinkedList<>(this.services.all());
            int totalServices = running.size();
            long start = System.currentTimeMillis();
            LOGGER.debug("Stopping {} Services...", Integer.valueOf(totalServices));
            while (!running.isEmpty()) {
                boolean stoppedSomething = false;
                Iterator<Service> it = running.iterator();
                while (it.hasNext()) {
                    Service s = it.next();
                    if (hasRunningDependencies(s, running)) {
                        LOGGER.trace("Delaying stopping {}", s);
                    } else {
                        LOGGER.trace("Stopping {}", s);
                        try {
                            s.stop();
                        } catch (Exception e) {
                            if (firstException == null) {
                                firstException = e;
                            } else {
                                LOGGER.error("Stopping Service failed due to ", (Throwable) e);
                            }
                        }
                        it.remove();
                        stoppedSomething = true;
                    }
                }
                if (stoppedSomething) {
                    LOGGER.trace("Cycle complete: " + running.size() + " Services remaining");
                } else {
                    throw new AssertionError("Cyclic dependency in Service set: " + running);
                }
            }
            LOGGER.debug("All Services successfully stopped, {} Services in {}ms", Integer.valueOf(totalServices), Long.valueOf(System.currentTimeMillis() - start));
            lock.unlock();
            if (firstException != null) {
                throw firstException;
            }
        } catch (Throwable th) {
            lock.unlock();
            throw th;
        }
    }

    private boolean hasUnstartedDependencies(Service service, Iterable<Service> unstarted) {
        for (Class<? extends Service> dep : identifyTransitiveDependenciesOf(service.getClass())) {
            for (Service s : unstarted) {
                if (dep.isInstance(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasRunningDependencies(Service service, Iterable<Service> running) {
        for (Class<? extends Service> dep : identifyTransitiveDependenciesOf(service.getClass())) {
            for (Service s : running) {
                if (dep.isInstance(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/internal/service/ServiceLocator$DependencySet.class */
    public static class DependencySet implements Builder<ServiceLocator> {
        private final ServiceLoader<ServiceFactory> serviceLoader = ClassLoading.libraryServiceLoaderFor(ServiceFactory.class);
        private final ServiceMap provided = new ServiceMap();
        private final Set<Class<? extends Service>> requested = new HashSet();

        public DependencySet with(Service service) {
            this.provided.add(service);
            return this;
        }

        public DependencySet with(Iterable<? extends Service> services) {
            for (Service s : services) {
                with(s);
            }
            return this;
        }

        public <T extends Service> DependencySet with(ServiceCreationConfiguration<T> config) {
            Class<T> serviceType = config.getServiceType();
            if (!this.provided.contains(serviceType) || serviceType.isAnnotationPresent(PluralService.class)) {
                Iterable<ServiceFactory<Service>> serviceFactories = ServiceLocator.getServiceFactories(this.serviceLoader);
                boolean success = false;
                for (ServiceFactory<Service> factory : serviceFactories) {
                    Class<?> factoryServiceType = factory.getServiceType();
                    if (serviceType.isAssignableFrom(factoryServiceType)) {
                        with(factory.create(config));
                        success = true;
                    }
                }
                if (success) {
                    return this;
                }
                throw new IllegalStateException("No factories exist for " + serviceType);
            }
            return this;
        }

        public DependencySet with(Class<? extends Service> clazz) {
            this.requested.add(clazz);
            return this;
        }

        public boolean contains(Class<? extends Service> serviceClass) {
            return this.provided.contains(serviceClass);
        }

        public <T extends Service> T providerOf(Class<T> serviceClass) {
            if (serviceClass.isAnnotationPresent(PluralService.class)) {
                throw new IllegalArgumentException("Cannot retrieve single provider for plural service");
            }
            Collection<T> providers = providersOf(serviceClass);
            switch (providers.size()) {
                case 0:
                    return null;
                case 1:
                    return providers.iterator().next();
                default:
                    throw new AssertionError();
            }
        }

        public <T extends Service> Collection<T> providersOf(Class<T> serviceClass) {
            return this.provided.get(serviceClass);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.ehcache.config.Builder
        /* renamed from: build */
        public ServiceLocator build2() throws DependencyException {
            try {
                ServiceMap resolvedServices = new ServiceMap();
                for (Service service : this.provided.all()) {
                    resolvedServices = lookupDependenciesOf(resolvedServices, service.getClass()).add(service);
                }
                for (Class<? extends Service> request : this.requested) {
                    if (request.isAnnotationPresent(PluralService.class)) {
                        try {
                            resolvedServices = lookupService(resolvedServices, request);
                        } catch (DependencyException e) {
                            if (!resolvedServices.contains(request)) {
                                throw e;
                            }
                        }
                    } else if (!resolvedServices.contains(request)) {
                        resolvedServices = lookupService(resolvedServices, request);
                    }
                }
                return new ServiceLocator(resolvedServices);
            } catch (DependencyException e2) {
                throw new IllegalStateException(e2);
            }
        }

        ServiceMap lookupDependenciesOf(ServiceMap resolved, Class<? extends Service> requested) throws DependencyException {
            for (Class<? extends Service> dependency : ServiceLocator.identifyImmediateDependenciesOf(requested)) {
                resolved = lookupService(resolved, dependency);
            }
            return resolved;
        }

        private <T extends Service> ServiceMap lookupService(ServiceMap resolved, Class<T> requested) throws DependencyException {
            if (resolved.contains(requested) && !requested.isAnnotationPresent(PluralService.class)) {
                return resolved;
            }
            ServiceMap resolved2 = new ServiceMap(resolved).addAll(this.provided.get(requested));
            if (resolved2.contains(requested) && !requested.isAnnotationPresent(PluralService.class)) {
                return resolved2;
            }
            Collection<ServiceFactory<? extends T>> serviceFactories = discoverServices(resolved2, requested);
            if (serviceFactories.size() > 1 && !requested.isAnnotationPresent(PluralService.class)) {
                throw new DependencyException("Multiple factories for non-plural service");
            }
            for (ServiceFactory<? extends T> factory : serviceFactories) {
                if (!resolved2.contains(factory.getServiceType())) {
                    try {
                        resolved2 = lookupDependenciesOf(resolved2, factory.getServiceType());
                        resolved2 = new ServiceMap(resolved2).add(factory.create(null));
                    } catch (DependencyException e) {
                    }
                }
            }
            if (resolved2.contains(requested)) {
                return resolved2;
            }
            throw new DependencyException("Failed to find provider with satisfied dependency set for " + requested + " [candidates " + serviceFactories + "]");
        }

        private <T> Collection<ServiceFactory<? extends T>> discoverServices(ServiceMap resolved, Class<T> serviceClass) {
            Collection<ServiceFactory<? extends T>> serviceFactories = new ArrayList<>();
            for (ServiceFactory<? extends T> serviceFactory : ServiceLocator.getServiceFactories(this.serviceLoader)) {
                Class<? extends Service> factoryServiceType = serviceFactory.getServiceType();
                if (serviceClass.isAssignableFrom(factoryServiceType) && !serviceFactory.getClass().isAnnotationPresent(ServiceFactory.RequiresConfiguration.class) && !this.provided.contains(factoryServiceType) && !resolved.contains(factoryServiceType)) {
                    serviceFactories.add(serviceFactory);
                }
            }
            return serviceFactories;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Collection<Class<?>> getAllInterfaces(Class<?> clazz) {
        ArrayList<Class<?>> interfaces = new ArrayList<>();
        Class<?> superclass = clazz;
        while (true) {
            Class<?> c = superclass;
            if (c != null) {
                Class<?>[] arr$ = c.getInterfaces();
                for (Class<?> i : arr$) {
                    interfaces.add(i);
                    interfaces.addAll(getAllInterfaces(i));
                }
                superclass = c.getSuperclass();
            } else {
                return interfaces;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Set<Class<? extends Service>> identifyImmediateDependenciesOf(Class<?> clazz) {
        if (clazz == null) {
            return Collections.emptySet();
        }
        Set<Class<? extends Service>> dependencies = new HashSet<>();
        ServiceDependencies annotation = (ServiceDependencies) clazz.getAnnotation(ServiceDependencies.class);
        if (annotation != null) {
            Class[] arr$ = annotation.value();
            for (Class cls : arr$) {
                if (Service.class.isAssignableFrom(cls)) {
                    dependencies.add(cls);
                } else {
                    throw new IllegalStateException("Service dependency declared by " + clazz.getName() + " is not a Service: " + cls.getName());
                }
            }
        }
        Class<?>[] arr$2 = clazz.getInterfaces();
        for (Class<?> interfaceClazz : arr$2) {
            if (Service.class.isAssignableFrom(interfaceClazz)) {
                dependencies.addAll(identifyImmediateDependenciesOf((Class) Service.class.getClass().cast(interfaceClazz)));
            }
        }
        dependencies.addAll(identifyImmediateDependenciesOf(clazz.getSuperclass()));
        return dependencies;
    }

    private static Set<Class<? extends Service>> identifyTransitiveDependenciesOf(Class<?> clazz) {
        Set<Class<? extends Service>> transitive = new HashSet<>();
        Set<Class<? extends Service>> dependencies = identifyImmediateDependenciesOf(clazz);
        transitive.addAll(dependencies);
        for (Class<? extends Service> klazz : dependencies) {
            transitive.addAll(identifyTransitiveDependenciesOf(klazz));
        }
        return transitive;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T extends Service> Iterable<ServiceFactory<T>> getServiceFactories(ServiceLoader<ServiceFactory> serviceFactory) {
        List<ServiceFactory<T>> list = new ArrayList<>();
        Iterator i$ = serviceFactory.iterator();
        while (i$.hasNext()) {
            list.add(i$.next());
        }
        return list;
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/internal/service/ServiceLocator$DependencyException.class */
    private static class DependencyException extends Exception {
        public DependencyException(String s) {
            super(s);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/internal/service/ServiceLocator$ServiceMap.class */
    private static class ServiceMap {
        private final Map<Class<? extends Service>, Set<Service>> services = new HashMap();

        public ServiceMap(ServiceMap resolved) {
            for (Map.Entry<Class<? extends Service>, Set<Service>> e : resolved.services.entrySet()) {
                Set<Service> copy = Collections.newSetFromMap(new IdentityHashMap());
                copy.addAll(e.getValue());
                this.services.put(e.getKey(), copy);
            }
        }

        public ServiceMap() {
        }

        public <T extends Service> Set<T> get(Class<T> serviceType) {
            Set<Service> set = this.services.get(serviceType);
            if (set == null) {
                return Collections.emptySet();
            }
            return Collections.unmodifiableSet(set);
        }

        public ServiceMap addAll(Iterable<? extends Service> services) {
            for (Service s : services) {
                add(s);
            }
            return this;
        }

        public ServiceMap add(Service service) {
            HashSet<Class<? extends Service>> hashSet = new HashSet();
            hashSet.add(service.getClass());
            for (Class<?> i : ServiceLocator.getAllInterfaces(service.getClass())) {
                if (Service.class != i && Service.class.isAssignableFrom(i)) {
                    hashSet.add(i);
                }
            }
            for (Class<? extends Service> serviceClazz : hashSet) {
                if (serviceClazz.isAnnotationPresent(PluralService.class)) {
                    Set<Service> registeredServices = this.services.get(serviceClazz);
                    if (registeredServices == null) {
                        registeredServices = new LinkedHashSet();
                        this.services.put(serviceClazz, registeredServices);
                    }
                    registeredServices.add(service);
                } else {
                    Set<Service> registeredServices2 = this.services.get(serviceClazz);
                    if (registeredServices2 == null || registeredServices2.isEmpty()) {
                        this.services.put(serviceClazz, Collections.singleton(service));
                    } else if (!registeredServices2.contains(service)) {
                        StringBuilder message = new StringBuilder("Duplicate service implementation(s) found for ").append(service.getClass());
                        for (Class<? extends Service> serviceClass : hashSet) {
                            if (!serviceClass.isAnnotationPresent(PluralService.class)) {
                                Set<Service> s = this.services.get(serviceClass);
                                Service declaredService = s == null ? null : s.iterator().next();
                                if (declaredService != null) {
                                    message.append("\n\t\t- ").append(serviceClass).append(" already has ").append(declaredService.getClass());
                                }
                            }
                        }
                        throw new IllegalStateException(message.toString());
                    }
                }
            }
            return this;
        }

        public Set<Service> all() {
            Set<Service> all = Collections.newSetFromMap(new IdentityHashMap());
            for (Set<Service> s : this.services.values()) {
                all.addAll(s);
            }
            return Collections.unmodifiableSet(all);
        }

        public boolean contains(Class<? extends Service> request) {
            return this.services.containsKey(request);
        }
    }
}
