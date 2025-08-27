package org.ehcache.config;

import org.ehcache.config.ResourcePool;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/ResourceType.class */
public interface ResourceType<T extends ResourcePool> {
    Class<T> getResourcePoolClass();

    boolean isPersistable();

    boolean requiresSerialization();

    int getTierHeight();

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/config/ResourceType$Core.class */
    public enum Core implements ResourceType<SizedResourcePool> {
        HEAP(false, false, 10000),
        OFFHEAP(false, true, 1000),
        DISK(true, true, 100);

        private final boolean persistable;
        private final boolean requiresSerialization;
        private final int tierHeight;

        Core(boolean persistable, boolean requiresSerialization, int tierHeight) {
            this.persistable = persistable;
            this.requiresSerialization = requiresSerialization;
            this.tierHeight = tierHeight;
        }

        @Override // org.ehcache.config.ResourceType
        public Class<SizedResourcePool> getResourcePoolClass() {
            return SizedResourcePool.class;
        }

        @Override // org.ehcache.config.ResourceType
        public boolean isPersistable() {
            return this.persistable;
        }

        @Override // org.ehcache.config.ResourceType
        public boolean requiresSerialization() {
            return this.requiresSerialization;
        }

        @Override // org.ehcache.config.ResourceType
        public int getTierHeight() {
            return this.tierHeight;
        }

        @Override // java.lang.Enum
        public String toString() {
            return name().toLowerCase();
        }
    }
}
