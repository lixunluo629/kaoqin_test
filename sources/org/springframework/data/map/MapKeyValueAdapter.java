package org.springframework.data.map;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.core.CollectionFactory;
import org.springframework.data.keyvalue.core.AbstractKeyValueAdapter;
import org.springframework.data.keyvalue.core.ForwardingCloseableIterator;
import org.springframework.data.util.CloseableIterator;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/map/MapKeyValueAdapter.class */
public class MapKeyValueAdapter extends AbstractKeyValueAdapter {
    private final Class<? extends Map> keySpaceMapType;
    private final Map<Serializable, Map<Serializable, Object>> store;

    public MapKeyValueAdapter() {
        this((Class<? extends Map>) ConcurrentHashMap.class);
    }

    public MapKeyValueAdapter(Class<? extends Map> mapType) {
        this(CollectionFactory.createMap(mapType, 100), mapType);
    }

    public MapKeyValueAdapter(Map<Serializable, Map<Serializable, Object>> store) {
        this(store, ClassUtils.getUserClass(store));
    }

    private MapKeyValueAdapter(Map<Serializable, Map<Serializable, Object>> store, Class<? extends Map> keySpaceMapType) {
        Assert.notNull(store, "Store must not be null.");
        Assert.notNull(keySpaceMapType, "Map type to be used for key spaces must not be null!");
        this.store = store;
        this.keySpaceMapType = keySpaceMapType;
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public Object put(Serializable id, Object item, Serializable keyspace) {
        Assert.notNull(id, "Cannot add item with null id.");
        Assert.notNull(keyspace, "Cannot add item for null collection.");
        return getKeySpaceMap(keyspace).put(id, item);
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public boolean contains(Serializable id, Serializable keyspace) {
        return get(id, keyspace) != null;
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public long count(Serializable keyspace) {
        return getKeySpaceMap(keyspace).size();
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public Object get(Serializable id, Serializable keyspace) {
        Assert.notNull(id, "Cannot get item with null id.");
        return getKeySpaceMap(keyspace).get(id);
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public Object delete(Serializable id, Serializable keyspace) {
        Assert.notNull(id, "Cannot delete item with null id.");
        return getKeySpaceMap(keyspace).remove(id);
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public Collection<?> getAllOf(Serializable keyspace) {
        return getKeySpaceMap(keyspace).values();
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public CloseableIterator<Map.Entry<Serializable, Object>> entries(Serializable keyspace) {
        return new ForwardingCloseableIterator(getKeySpaceMap(keyspace).entrySet().iterator());
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public void deleteAllOf(Serializable keyspace) {
        getKeySpaceMap(keyspace).clear();
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public void clear() {
        this.store.clear();
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() throws Exception {
        clear();
    }

    protected Map<Serializable, Object> getKeySpaceMap(Serializable keyspace) {
        Assert.notNull(keyspace, "Collection must not be null for lookup.");
        Map<Serializable, Object> map = this.store.get(keyspace);
        if (map != null) {
            return map;
        }
        addMapForKeySpace(keyspace);
        return this.store.get(keyspace);
    }

    private void addMapForKeySpace(Serializable keyspace) {
        this.store.put(keyspace, CollectionFactory.createMap(this.keySpaceMapType, 1000));
    }
}
