package org.springframework.data.redis.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/PartialUpdate.class */
public class PartialUpdate<T> {
    private final Object id;
    private final Class<T> target;
    private final T value;
    private boolean refreshTtl;
    private final List<PropertyUpdate> propertyUpdates;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/PartialUpdate$UpdateCommand.class */
    public enum UpdateCommand {
        SET,
        DEL
    }

    private PartialUpdate(Object id, Class<T> target, T value, boolean refreshTtl, List<PropertyUpdate> propertyUpdates) {
        this.refreshTtl = false;
        this.propertyUpdates = new ArrayList();
        this.id = id;
        this.target = target;
        this.value = value;
        this.refreshTtl = refreshTtl;
        this.propertyUpdates.addAll(propertyUpdates);
    }

    public PartialUpdate(Object obj, Class<T> cls) {
        this.refreshTtl = false;
        this.propertyUpdates = new ArrayList();
        Assert.notNull(obj, "Id must not be null!");
        Assert.notNull(cls, "TargetType must not be null!");
        this.id = obj;
        this.target = (Class<T>) ClassUtils.getUserClass((Class<?>) cls);
        this.value = null;
    }

    public PartialUpdate(Object obj, T t) {
        this.refreshTtl = false;
        this.propertyUpdates = new ArrayList();
        Assert.notNull(obj, "Id must not be null!");
        Assert.notNull(t, "Value must not be null!");
        this.id = obj;
        this.target = (Class<T>) ClassUtils.getUserClass(t.getClass());
        this.value = t;
    }

    public static <S> PartialUpdate<S> newPartialUpdate(Object id, Class<S> targetType) {
        return new PartialUpdate<>(id, (Class) targetType);
    }

    public T getValue() {
        return this.value;
    }

    public PartialUpdate<T> set(String path, Object value) {
        Assert.hasText(path, "Path to set must not be null or empty!");
        PartialUpdate<T> update = new PartialUpdate<>(this.id, this.target, this.value, this.refreshTtl, this.propertyUpdates);
        update.propertyUpdates.add(new PropertyUpdate(UpdateCommand.SET, path, value));
        return update;
    }

    public PartialUpdate<T> del(String path) {
        Assert.hasText(path, "Path to remove must not be null or empty!");
        PartialUpdate<T> update = new PartialUpdate<>(this.id, this.target, this.value, this.refreshTtl, this.propertyUpdates);
        update.propertyUpdates.add(new PropertyUpdate(UpdateCommand.DEL, path));
        return update;
    }

    public Class<T> getTarget() {
        return this.target;
    }

    public Object getId() {
        return this.id;
    }

    public List<PropertyUpdate> getPropertyUpdates() {
        return Collections.unmodifiableList(this.propertyUpdates);
    }

    public boolean isRefreshTtl() {
        return this.refreshTtl;
    }

    public PartialUpdate<T> refreshTtl(boolean refreshTtl) {
        return new PartialUpdate<>(this.id, this.target, this.value, refreshTtl, this.propertyUpdates);
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/PartialUpdate$PropertyUpdate.class */
    public static class PropertyUpdate {
        private final UpdateCommand cmd;
        private final String propertyPath;
        private final Object value;

        private PropertyUpdate(UpdateCommand cmd, String propertyPath) {
            this(cmd, propertyPath, (Object) null);
        }

        private PropertyUpdate(UpdateCommand cmd, String propertyPath, Object value) {
            this.cmd = cmd;
            this.propertyPath = propertyPath;
            this.value = value;
        }

        public UpdateCommand getCmd() {
            return this.cmd;
        }

        public String getPropertyPath() {
            return this.propertyPath;
        }

        public Object getValue() {
            return this.value;
        }
    }
}
