package org.ehcache.jsr107;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import javax.cache.integration.CompletionListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.EntryProcessorResult;
import org.ehcache.Cache;
import org.ehcache.Status;
import org.ehcache.core.InternalCache;
import org.ehcache.core.Jsr107Cache;
import org.ehcache.core.exceptions.StorePassThroughException;
import org.ehcache.core.spi.function.BiFunction;
import org.ehcache.core.spi.function.Function;
import org.ehcache.core.spi.function.NullaryFunction;
import org.ehcache.event.EventFiring;
import org.ehcache.event.EventOrdering;
import org.ehcache.jsr107.EventListenerAdaptors;
import org.ehcache.jsr107.internal.Jsr107CacheLoaderWriter;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.ehcache.spi.loaderwriter.CacheLoadingException;
import org.ehcache.spi.loaderwriter.CacheWritingException;
import org.springframework.beans.PropertyAccessor;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107Cache.class */
class Eh107Cache<K, V> implements Cache<K, V> {
    private final InternalCache<K, V> ehCache;
    private final Jsr107Cache<K, V> jsr107Cache;
    private final Eh107CacheManager cacheManager;
    private final String name;
    private final AtomicBoolean hypotheticallyClosed = new AtomicBoolean();
    private final CacheResources<K, V> cacheResources;
    private final Eh107CacheMXBean managementBean;
    private final Eh107CacheStatisticsMXBean statisticsBean;
    private final Eh107Configuration<K, V> config;
    private final Jsr107CacheLoaderWriter<? super K, V> cacheLoaderWriter;
    private static final Object UNDEFINED = new Object();

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107Cache$MutableEntryOperation.class */
    private enum MutableEntryOperation {
        NONE,
        ACCESS,
        CREATE,
        LOAD,
        REMOVE,
        UPDATE
    }

    Eh107Cache(String name, Eh107Configuration<K, V> config, CacheResources<K, V> cacheResources, InternalCache<K, V> ehCache, Eh107CacheManager cacheManager) {
        this.cacheLoaderWriter = cacheResources.getCacheLoaderWriter();
        this.config = config;
        this.ehCache = ehCache;
        this.cacheManager = cacheManager;
        this.name = name;
        this.cacheResources = cacheResources;
        this.managementBean = new Eh107CacheMXBean(name, cacheManager, config);
        this.statisticsBean = new Eh107CacheStatisticsMXBean(name, cacheManager, ehCache);
        for (Map.Entry<CacheEntryListenerConfiguration<K, V>, ListenerResources<K, V>> entry : cacheResources.getListenerResources().entrySet()) {
            registerEhcacheListeners(entry.getKey(), entry.getValue());
        }
        this.jsr107Cache = ehCache.getJsr107Cache();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.integration.CacheLoaderException */
    public V get(K key) throws CacheLoaderException {
        checkClosed();
        try {
            return this.ehCache.get(key);
        } catch (CacheLoadingException e) {
            throw jsr107CacheLoaderException(e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.integration.CacheLoaderException */
    public Map<K, V> getAll(Set<? extends K> keys) throws CacheLoaderException {
        checkClosed();
        try {
            return this.jsr107Cache.getAll(keys);
        } catch (CacheLoadingException e) {
            throw jsr107CacheLoaderException(e);
        }
    }

    public boolean containsKey(K key) {
        checkClosed();
        return this.ehCache.containsKey(key);
    }

    public void loadAll(Set<? extends K> keys, boolean replaceExistingValues, CompletionListener completionListener) {
        CacheLoaderException cle;
        checkClosed();
        if (keys == null) {
            throw new NullPointerException();
        }
        for (K key : keys) {
            if (key == null) {
                throw new NullPointerException();
            }
        }
        CompletionListener completionListener2 = completionListener != null ? completionListener : NullCompletionListener.INSTANCE;
        if (this.cacheLoaderWriter == null) {
            completionListener2.onCompletion();
            return;
        }
        try {
            this.jsr107Cache.loadAll(keys, replaceExistingValues, new Function<Iterable<? extends K>, Map<K, V>>() { // from class: org.ehcache.jsr107.Eh107Cache.1
                /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.integration.CacheLoaderException */
                @Override // org.ehcache.core.spi.function.Function
                public Map<K, V> apply(Iterable<? extends K> keys2) throws CacheLoaderException {
                    CacheLoaderException cle2;
                    try {
                        Map<K, V> mapLoadAllAlways = Eh107Cache.this.cacheLoaderWriter.loadAllAlways(keys2);
                        HashMap<K, V> resultMap = new HashMap<>();
                        for (K key2 : keys2) {
                            resultMap.put(key2, mapLoadAllAlways.get(key2));
                        }
                        return resultMap;
                    } catch (Exception e) {
                        if (e instanceof CacheLoaderException) {
                            cle2 = e;
                        } else if (e.getCause() instanceof CacheLoaderException) {
                            cle2 = e.getCause();
                        } else {
                            cle2 = new CacheLoaderException(e);
                        }
                        throw cle2;
                    }
                }
            });
            completionListener2.onCompletion();
        } catch (Exception e) {
            if (e instanceof CacheLoaderException) {
                cle = e;
            } else if (e.getCause() instanceof CacheLoaderException) {
                cle = e.getCause();
            } else {
                cle = new CacheLoaderException(e);
            }
            completionListener2.onException(cle);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.integration.CacheWriterException */
    public void put(K key, V value) throws CacheWriterException {
        checkClosed();
        try {
            this.ehCache.put(key, value);
        } catch (CacheWritingException cwe) {
            throw jsr107CacheWriterException(cwe);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.integration.CacheWriterException */
    public V getAndPut(K key, V value) throws CacheWriterException {
        checkClosed();
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        try {
            return this.jsr107Cache.getAndPut(key, value);
        } catch (CacheWritingException e) {
            throw jsr107CacheWriterException(e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.integration.CacheWriterException */
    public void putAll(Map<? extends K, ? extends V> map) throws CacheWriterException {
        checkClosed();
        try {
            this.ehCache.putAll(map);
        } catch (CacheWritingException e) {
            throw jsr107CacheWriterException(e);
        }
    }

    public boolean putIfAbsent(K key, V value) {
        checkClosed();
        try {
            try {
                this.cacheResources.getExpiryPolicy().enableShortCircuitAccessCalls();
                return this.ehCache.putIfAbsent(key, value) == null;
            } catch (CacheWritingException e) {
                throw jsr107CacheWriterException(e);
            }
        } finally {
            this.cacheResources.getExpiryPolicy().disableShortCircuitAccessCalls();
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.integration.CacheWriterException */
    public boolean remove(K key) throws CacheWriterException {
        checkClosed();
        if (key == null) {
            throw new NullPointerException();
        }
        try {
            return this.jsr107Cache.remove(key);
        } catch (CacheWritingException e) {
            throw jsr107CacheWriterException(e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.integration.CacheWriterException */
    public boolean remove(K key, V oldValue) throws CacheWriterException {
        checkClosed();
        try {
            return this.ehCache.remove(key, oldValue);
        } catch (CacheWritingException e) {
            throw jsr107CacheWriterException(e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.integration.CacheWriterException */
    public V getAndRemove(K key) throws CacheWriterException {
        checkClosed();
        if (key == null) {
            throw new NullPointerException();
        }
        try {
            return this.jsr107Cache.getAndRemove(key);
        } catch (CacheWritingException e) {
            throw jsr107CacheWriterException(e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.integration.CacheWriterException */
    public boolean replace(K key, V oldValue, V newValue) throws CacheWriterException {
        checkClosed();
        try {
            return this.ehCache.replace(key, oldValue, newValue);
        } catch (CacheWritingException e) {
            throw jsr107CacheWriterException(e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.integration.CacheWriterException */
    public boolean replace(K key, V value) throws CacheWriterException {
        checkClosed();
        try {
            return this.ehCache.replace(key, value) != null;
        } catch (CacheWritingException e) {
            throw jsr107CacheWriterException(e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.integration.CacheWriterException */
    public V getAndReplace(K key, V value) throws CacheWriterException {
        try {
            checkClosed();
            return this.ehCache.replace(key, value);
        } catch (CacheWritingException e) {
            throw jsr107CacheWriterException(e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.integration.CacheWriterException */
    public void removeAll(Set<? extends K> keys) throws CacheWriterException {
        checkClosed();
        try {
            this.ehCache.removeAll(keys);
        } catch (CacheWritingException e) {
            throw jsr107CacheWriterException(e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.integration.CacheWriterException */
    public void removeAll() throws CacheWriterException {
        checkClosed();
        try {
            this.jsr107Cache.removeAll();
        } catch (CacheWritingException e) {
            throw jsr107CacheWriterException(e);
        }
    }

    public void clear() {
        clear(true);
    }

    private void clear(boolean checkClosed) {
        if (checkClosed) {
            checkClosed();
        }
        this.ehCache.clear();
    }

    public <C extends Configuration<K, V>> C getConfiguration(Class<C> clazz) {
        checkClosed();
        return (C) this.config.unwrap(clazz);
    }

    /* JADX WARN: Type inference failed for: r0v9, types: [T, java.lang.Object] */
    public <T> T invoke(K k, final EntryProcessor<K, V, T> entryProcessor, final Object... objArr) throws EntryProcessorException {
        checkClosed();
        if (k == null || entryProcessor == null) {
            throw new NullPointerException();
        }
        final AtomicReference atomicReference = new AtomicReference();
        final AtomicReference atomicReference2 = new AtomicReference();
        this.jsr107Cache.compute(k, new BiFunction<K, V, V>() { // from class: org.ehcache.jsr107.Eh107Cache.2
            /* JADX WARN: Multi-variable type inference failed */
            @Override // org.ehcache.core.spi.function.BiFunction
            public V apply(K mappedKey, V mappedValue) {
                Eh107Cache<K, V>.MutableEntry mutableEntry = new MutableEntry(mappedKey, mappedValue);
                atomicReference.set(mutableEntry);
                try {
                    atomicReference2.set(entryProcessor.process(mutableEntry, objArr));
                    return mutableEntry.apply(Eh107Cache.this.config.isWriteThrough(), Eh107Cache.this.cacheLoaderWriter);
                } catch (Exception e) {
                    if (e instanceof EntryProcessorException) {
                        throw new StorePassThroughException(e);
                    }
                    throw new StorePassThroughException(new EntryProcessorException(e));
                }
            }
        }, new NullaryFunction<Boolean>() { // from class: org.ehcache.jsr107.Eh107Cache.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.ehcache.core.spi.function.NullaryFunction
            public Boolean apply() {
                Eh107Cache<K, V>.MutableEntry mutableEntry = (MutableEntry) atomicReference.get();
                return Boolean.valueOf(mutableEntry.shouldReplace());
            }
        }, new NullaryFunction<Boolean>() { // from class: org.ehcache.jsr107.Eh107Cache.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.ehcache.core.spi.function.NullaryFunction
            public Boolean apply() {
                Eh107Cache<K, V>.MutableEntry mutableEntry = (MutableEntry) atomicReference.get();
                return Boolean.valueOf(mutableEntry.shouldInvokeWriter());
            }
        }, new NullaryFunction<Boolean>() { // from class: org.ehcache.jsr107.Eh107Cache.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.ehcache.core.spi.function.NullaryFunction
            public Boolean apply() {
                Eh107Cache<K, V>.MutableEntry mutableEntry = (MutableEntry) atomicReference.get();
                return Boolean.valueOf(mutableEntry.shouldGenerateEvent());
            }
        });
        return atomicReference2.get();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v18, types: [javax.cache.processor.EntryProcessorResult] */
    /* JADX WARN: Type inference failed for: r0v26, types: [javax.cache.processor.EntryProcessorResult] */
    public <T> Map<K, EntryProcessorResult<T>> invokeAll(Set<? extends K> keys, EntryProcessor<K, V, T> entryProcessor, Object... arguments) throws EntryProcessorException {
        checkClosed();
        if (keys == null || entryProcessor == null) {
            throw new NullPointerException();
        }
        Iterator i$ = keys.iterator();
        while (i$.hasNext()) {
            if (i$.next() == null) {
                throw new NullPointerException();
            }
        }
        Map<K, EntryProcessorResult<T>> results = new HashMap<>(keys.size());
        for (K key : keys) {
            V vNewErrorThrowingEntryProcessorResult = null;
            try {
                Object objInvoke = invoke(key, entryProcessor, arguments);
                if (objInvoke != null) {
                    vNewErrorThrowingEntryProcessorResult = newEntryProcessorResult(objInvoke);
                }
            } catch (Exception e) {
                vNewErrorThrowingEntryProcessorResult = newErrorThrowingEntryProcessorResult(e);
            }
            if (vNewErrorThrowingEntryProcessorResult != null) {
                results.put(key, vNewErrorThrowingEntryProcessorResult);
            }
        }
        return results;
    }

    public String getName() {
        return this.name;
    }

    public CacheManager getCacheManager() {
        return this.cacheManager;
    }

    public void close() throws CacheException {
        MultiCacheException closeException = new MultiCacheException();
        this.cacheManager.close(this, closeException);
        closeException.throwIfNotEmpty();
    }

    public boolean isClosed() {
        return syncedIsClose();
    }

    void closeInternal(MultiCacheException closeException) {
        closeInternal(false, closeException);
    }

    private void closeInternal(boolean destroy, MultiCacheException closeException) {
        if (this.hypotheticallyClosed.compareAndSet(false, true)) {
            if (destroy) {
                try {
                    clear(false);
                } catch (Throwable t) {
                    closeException.addThrowable(t);
                }
            }
            this.cacheResources.closeResources(closeException);
        }
    }

    private boolean syncedIsClose() throws CacheException {
        if (this.ehCache.getStatus() == Status.UNINITIALIZED && !this.hypotheticallyClosed.get()) {
            close();
        }
        return this.hypotheticallyClosed.get();
    }

    void destroy(MultiCacheException destroyException) {
        closeInternal(true, destroyException);
    }

    public <T> T unwrap(Class<T> cls) {
        return (T) Unwrap.unwrap(cls, this, this.ehCache);
    }

    public void registerCacheEntryListener(CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) throws CacheException {
        checkClosed();
        if (cacheEntryListenerConfiguration == null) {
            throw new NullPointerException();
        }
        ListenerResources<K, V> resources = this.cacheResources.registerCacheEntryListener(cacheEntryListenerConfiguration);
        this.config.addCacheEntryListenerConfiguration(cacheEntryListenerConfiguration);
        registerEhcacheListeners(cacheEntryListenerConfiguration, resources);
    }

    private void registerEhcacheListeners(CacheEntryListenerConfiguration<K, V> config, ListenerResources<K, V> resources) {
        boolean synchronous = config.isSynchronous();
        EventOrdering ordering = synchronous ? EventOrdering.ORDERED : EventOrdering.UNORDERED;
        EventFiring firing = synchronous ? EventFiring.SYNCHRONOUS : EventFiring.ASYNCHRONOUS;
        boolean requestsOld = config.isOldValueRequired();
        for (EventListenerAdaptors.EventListenerAdaptor<K, V> ehcacheListener : resources.getEhcacheListeners(this, requestsOld)) {
            this.ehCache.getRuntimeConfiguration().registerCacheEventListener(ehcacheListener, ordering, firing, EnumSet.of(ehcacheListener.getEhcacheEventType()));
        }
    }

    public void deregisterCacheEntryListener(CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) throws CacheException {
        checkClosed();
        if (cacheEntryListenerConfiguration == null) {
            throw new NullPointerException();
        }
        ListenerResources<K, V> resources = this.cacheResources.deregisterCacheEntryListener(cacheEntryListenerConfiguration);
        if (resources != null) {
            this.config.removeCacheEntryListenerConfiguration(cacheEntryListenerConfiguration);
            for (EventListenerAdaptors.EventListenerAdaptor<K, V> ehListener : resources.getEhcacheListeners(this, cacheEntryListenerConfiguration.isOldValueRequired())) {
                this.ehCache.getRuntimeConfiguration().deregisterCacheEventListener(ehListener);
            }
        }
    }

    public Iterator<Cache.Entry<K, V>> iterator() {
        checkClosed();
        final Iterator<Cache.Entry<K, V>> specIterator = this.jsr107Cache.specIterator();
        return new Iterator<Cache.Entry<K, V>>() { // from class: org.ehcache.jsr107.Eh107Cache.6
            @Override // java.util.Iterator
            public boolean hasNext() {
                Eh107Cache.this.checkClosed();
                return specIterator.hasNext();
            }

            @Override // java.util.Iterator
            public Cache.Entry<K, V> next() {
                Eh107Cache.this.checkClosed();
                Cache.Entry<K, V> next = (Cache.Entry) specIterator.next();
                if (next == null) {
                    return null;
                }
                return new WrappedEhcacheEntry(next);
            }

            @Override // java.util.Iterator
            public void remove() {
                Eh107Cache.this.checkClosed();
                specIterator.remove();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkClosed() {
        if (syncedIsClose()) {
            throw new IllegalStateException("Cache[" + this.name + "] is closed");
        }
    }

    public String toString() {
        return getClass().getSimpleName() + PropertyAccessor.PROPERTY_KEY_PREFIX + this.name + "]";
    }

    Eh107MXBean getManagementMBean() {
        return this.managementBean;
    }

    Eh107MXBean getStatisticsMBean() {
        return this.statisticsBean;
    }

    void setStatisticsEnabled(boolean enabled) {
        this.config.setStatisticsEnabled(enabled);
    }

    void setManagementEnabled(boolean enabled) {
        this.config.setManagementEnabled(enabled);
    }

    private static CacheLoaderException jsr107CacheLoaderException(CacheLoadingException e) {
        if (e.getCause() instanceof CacheLoaderException) {
            return e.getCause();
        }
        return new CacheLoaderException(e);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.integration.CacheWriterException */
    private static CacheWriterException jsr107CacheWriterException(CacheWritingException e) throws CacheWriterException {
        if (e.getCause() instanceof CacheWriterException) {
            return e.getCause();
        }
        throw new CacheWriterException(e);
    }

    private static <T> EntryProcessorResult<T> newEntryProcessorResult(final T result) {
        if (result == null) {
            throw new NullPointerException();
        }
        return new EntryProcessorResult<T>() { // from class: org.ehcache.jsr107.Eh107Cache.7
            public T get() throws EntryProcessorException {
                return (T) result;
            }
        };
    }

    private static <T> EntryProcessorResult<T> newErrorThrowingEntryProcessorResult(final Exception e) {
        return new EntryProcessorResult<T>() { // from class: org.ehcache.jsr107.Eh107Cache.8
            /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.processor.EntryProcessorException */
            public T get() throws EntryProcessorException {
                if (e instanceof EntryProcessorException) {
                    throw e;
                }
                throw new EntryProcessorException(e);
            }
        };
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107Cache$WrappedEhcacheEntry.class */
    private static class WrappedEhcacheEntry<K, V> implements Cache.Entry<K, V> {
        private final Cache.Entry<K, V> ehEntry;

        WrappedEhcacheEntry(Cache.Entry<K, V> ehEntry) {
            this.ehEntry = ehEntry;
        }

        public K getKey() {
            return this.ehEntry.getKey();
        }

        public V getValue() {
            return this.ehEntry.getValue();
        }

        public <T> T unwrap(Class<T> cls) {
            return (T) Unwrap.unwrap(cls, this, this.ehEntry);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107Cache$MutableEntry.class */
    private class MutableEntry implements javax.cache.processor.MutableEntry<K, V> {
        private final K key;
        private final V initialValue;
        private volatile V finalValue = (V) undefined();
        private volatile MutableEntryOperation operation = MutableEntryOperation.NONE;

        MutableEntry(K k, V v) {
            this.key = k;
            this.initialValue = v;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            if (this.finalValue == Eh107Cache.UNDEFINED) {
                if (this.initialValue == null && Eh107Cache.this.config.isReadThrough() && Eh107Cache.this.cacheLoaderWriter != null) {
                    this.finalValue = (V) tryLoad();
                    if (this.finalValue != null) {
                        this.operation = MutableEntryOperation.LOAD;
                    }
                } else {
                    this.finalValue = this.initialValue;
                    this.operation = MutableEntryOperation.ACCESS;
                }
            }
            return this.finalValue;
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.integration.CacheLoaderException */
        private V tryLoad() throws CacheLoaderException {
            try {
                return Eh107Cache.this.cacheLoaderWriter.load(this.key);
            } catch (Exception e) {
                if (e instanceof CacheLoaderException) {
                    throw e;
                }
                throw new CacheLoaderException(e);
            }
        }

        public boolean exists() {
            return this.finalValue == Eh107Cache.UNDEFINED ? this.initialValue != null : this.finalValue != null;
        }

        public void remove() {
            if (this.operation == MutableEntryOperation.CREATE) {
                this.operation = MutableEntryOperation.NONE;
            } else {
                this.operation = MutableEntryOperation.REMOVE;
            }
            this.finalValue = null;
        }

        public void setValue(V value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.operation = this.initialValue == null ? MutableEntryOperation.CREATE : MutableEntryOperation.UPDATE;
            this.finalValue = value;
        }

        V apply(boolean isWriteThrough, CacheLoaderWriter<? super K, ? super V> cacheLoaderWriter) {
            switch (this.operation) {
                case NONE:
                case ACCESS:
                    return this.initialValue;
                case LOAD:
                case CREATE:
                case UPDATE:
                    return this.finalValue;
                case REMOVE:
                    return null;
                default:
                    throw new AssertionError("unhandled case: " + this.operation);
            }
        }

        boolean shouldReplace() {
            switch (this.operation) {
                case NONE:
                case ACCESS:
                    return false;
                case LOAD:
                case CREATE:
                case UPDATE:
                case REMOVE:
                    return true;
                default:
                    throw new AssertionError("unhandled case: " + this.operation);
            }
        }

        boolean shouldGenerateEvent() {
            switch (this.operation) {
                case NONE:
                case ACCESS:
                case LOAD:
                    return false;
                case CREATE:
                case UPDATE:
                case REMOVE:
                    return true;
                default:
                    throw new AssertionError("unhandled case: " + this.operation);
            }
        }

        boolean shouldInvokeWriter() {
            switch (this.operation) {
                case NONE:
                case ACCESS:
                case LOAD:
                    return false;
                case CREATE:
                case UPDATE:
                case REMOVE:
                    return true;
                default:
                    throw new AssertionError("unhandled case: " + this.operation);
            }
        }

        private V undefined() {
            return (V) Eh107Cache.UNDEFINED;
        }

        public <T> T unwrap(Class<T> clazz) {
            throw new IllegalArgumentException();
        }
    }
}
