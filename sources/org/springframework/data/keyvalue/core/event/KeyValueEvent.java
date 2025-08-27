package org.springframework.data.keyvalue.core.event;

import java.io.Serializable;
import org.springframework.context.ApplicationEvent;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/event/KeyValueEvent.class */
public class KeyValueEvent<T> extends ApplicationEvent {
    private static final long serialVersionUID = -7128527253428193044L;
    private final String keyspace;

    protected KeyValueEvent(Object source, String keyspace) {
        super(source);
        this.keyspace = keyspace;
    }

    public String getKeyspace() {
        return this.keyspace;
    }

    @Override // java.util.EventObject
    public String toString() {
        return "KeyValueEvent [keyspace=" + this.keyspace + ", source=" + getSource() + "]";
    }

    public static <T> BeforeGetEvent<T> beforeGet(Serializable id, String keySpace, Class<T> type) {
        return new BeforeGetEvent<>(id, keySpace, type);
    }

    public static <T> AfterGetEvent<T> afterGet(Serializable id, String keySpace, Class<T> type, T value) {
        return new AfterGetEvent<>(id, keySpace, type, value);
    }

    public static <T> BeforeInsertEvent<T> beforeInsert(Serializable id, String keySpace, Class<? extends T> type, T value) {
        return new BeforeInsertEvent<>(id, keySpace, type, value);
    }

    public static <T> AfterInsertEvent<T> afterInsert(Serializable id, String keySpace, Class<? extends T> type, T value) {
        return new AfterInsertEvent<>(id, keySpace, type, value);
    }

    public static <T> BeforeUpdateEvent<T> beforeUpdate(Serializable id, String keySpace, Class<? extends T> type, T value) {
        return new BeforeUpdateEvent<>(id, keySpace, type, value);
    }

    public static <T> AfterUpdateEvent<T> afterUpdate(Serializable id, String keySpace, Class<? extends T> type, T actualValue, Object previousValue) {
        return new AfterUpdateEvent<>(id, keySpace, type, actualValue, previousValue);
    }

    public static <T> BeforeDropKeySpaceEvent<T> beforeDropKeySpace(String keySpace, Class<? extends T> type) {
        return new BeforeDropKeySpaceEvent<>(keySpace, type);
    }

    public static <T> AfterDropKeySpaceEvent<T> afterDropKeySpace(String keySpace, Class<? extends T> type) {
        return new AfterDropKeySpaceEvent<>(keySpace, type);
    }

    public static <T> BeforeDeleteEvent<T> beforeDelete(Serializable id, String keySpace, Class<? extends T> type) {
        return new BeforeDeleteEvent<>(id, keySpace, type);
    }

    public static <T> AfterDeleteEvent<T> afterDelete(Serializable id, String keySpace, Class<? extends T> type, T value) {
        return new AfterDeleteEvent<>(id, keySpace, type, value);
    }

    /* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/event/KeyValueEvent$KeyBasedEvent.class */
    static abstract class KeyBasedEvent<T> extends KeyValueEvent<T> {
        private Serializable key;
        private Class<? extends T> type;

        protected KeyBasedEvent(Serializable key, String keySpace, Class<? extends T> type) {
            super(type, keySpace);
            this.key = key;
        }

        public Serializable getKey() {
            return this.key;
        }

        @Override // java.util.EventObject
        public Serializable getSource() {
            return getKey();
        }

        public Class<? extends T> getType() {
            return this.type;
        }
    }

    /* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/event/KeyValueEvent$KeyBasedEventWithPayload.class */
    static abstract class KeyBasedEventWithPayload<T> extends KeyBasedEvent<T> {
        private final T payload;

        public KeyBasedEventWithPayload(Serializable key, String keySpace, Class<? extends T> type, T payload) {
            super(key, keySpace, type);
            this.payload = payload;
        }

        public T getPayload() {
            return this.payload;
        }
    }

    /* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/event/KeyValueEvent$BeforeGetEvent.class */
    public static class BeforeGetEvent<T> extends KeyBasedEvent<T> {
        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent
        public /* bridge */ /* synthetic */ Class getType() {
            return super.getType();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent, java.util.EventObject
        public /* bridge */ /* synthetic */ Serializable getSource() {
            return super.getSource();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent
        public /* bridge */ /* synthetic */ Serializable getKey() {
            return super.getKey();
        }

        protected BeforeGetEvent(Serializable key, String keySpace, Class<T> type) {
            super(key, keySpace, type);
        }
    }

    /* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/event/KeyValueEvent$AfterGetEvent.class */
    public static class AfterGetEvent<T> extends KeyBasedEventWithPayload<T> {
        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEventWithPayload
        public /* bridge */ /* synthetic */ Object getPayload() {
            return super.getPayload();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent
        public /* bridge */ /* synthetic */ Class getType() {
            return super.getType();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent, java.util.EventObject
        public /* bridge */ /* synthetic */ Serializable getSource() {
            return super.getSource();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent
        public /* bridge */ /* synthetic */ Serializable getKey() {
            return super.getKey();
        }

        protected AfterGetEvent(Serializable key, String keyspace, Class<T> type, T payload) {
            super(key, keyspace, type, payload);
        }
    }

    /* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/event/KeyValueEvent$BeforeInsertEvent.class */
    public static class BeforeInsertEvent<T> extends KeyBasedEventWithPayload<T> {
        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEventWithPayload
        public /* bridge */ /* synthetic */ Object getPayload() {
            return super.getPayload();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent
        public /* bridge */ /* synthetic */ Class getType() {
            return super.getType();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent, java.util.EventObject
        public /* bridge */ /* synthetic */ Serializable getSource() {
            return super.getSource();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent
        public /* bridge */ /* synthetic */ Serializable getKey() {
            return super.getKey();
        }

        public BeforeInsertEvent(Serializable key, String keySpace, Class<? extends T> type, T payload) {
            super(key, keySpace, type, payload);
        }
    }

    /* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/event/KeyValueEvent$AfterInsertEvent.class */
    public static class AfterInsertEvent<T> extends KeyBasedEventWithPayload<T> {
        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEventWithPayload
        public /* bridge */ /* synthetic */ Object getPayload() {
            return super.getPayload();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent
        public /* bridge */ /* synthetic */ Class getType() {
            return super.getType();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent, java.util.EventObject
        public /* bridge */ /* synthetic */ Serializable getSource() {
            return super.getSource();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent
        public /* bridge */ /* synthetic */ Serializable getKey() {
            return super.getKey();
        }

        public AfterInsertEvent(Serializable key, String keySpace, Class<? extends T> type, T payload) {
            super(key, keySpace, type, payload);
        }
    }

    /* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/event/KeyValueEvent$BeforeUpdateEvent.class */
    public static class BeforeUpdateEvent<T> extends KeyBasedEventWithPayload<T> {
        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEventWithPayload
        public /* bridge */ /* synthetic */ Object getPayload() {
            return super.getPayload();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent
        public /* bridge */ /* synthetic */ Class getType() {
            return super.getType();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent, java.util.EventObject
        public /* bridge */ /* synthetic */ Serializable getSource() {
            return super.getSource();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent
        public /* bridge */ /* synthetic */ Serializable getKey() {
            return super.getKey();
        }

        public BeforeUpdateEvent(Serializable key, String keySpace, Class<? extends T> type, T payload) {
            super(key, keySpace, type, payload);
        }
    }

    /* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/event/KeyValueEvent$AfterUpdateEvent.class */
    public static class AfterUpdateEvent<T> extends KeyBasedEventWithPayload<T> {
        private final Object existing;

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEventWithPayload
        public /* bridge */ /* synthetic */ Object getPayload() {
            return super.getPayload();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent
        public /* bridge */ /* synthetic */ Class getType() {
            return super.getType();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent, java.util.EventObject
        public /* bridge */ /* synthetic */ Serializable getSource() {
            return super.getSource();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent
        public /* bridge */ /* synthetic */ Serializable getKey() {
            return super.getKey();
        }

        public AfterUpdateEvent(Serializable key, String keySpace, Class<? extends T> type, T payload, Object existing) {
            super(key, keySpace, type, payload);
            this.existing = existing;
        }

        public Object before() {
            return this.existing;
        }

        public T after() {
            return (T) getPayload();
        }
    }

    /* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/event/KeyValueEvent$BeforeDeleteEvent.class */
    public static class BeforeDeleteEvent<T> extends KeyBasedEvent<T> {
        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent
        public /* bridge */ /* synthetic */ Class getType() {
            return super.getType();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent, java.util.EventObject
        public /* bridge */ /* synthetic */ Serializable getSource() {
            return super.getSource();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent
        public /* bridge */ /* synthetic */ Serializable getKey() {
            return super.getKey();
        }

        public BeforeDeleteEvent(Serializable key, String keySpace, Class<? extends T> type) {
            super(key, keySpace, type);
        }
    }

    /* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/event/KeyValueEvent$AfterDeleteEvent.class */
    public static class AfterDeleteEvent<T> extends KeyBasedEventWithPayload<T> {
        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEventWithPayload
        public /* bridge */ /* synthetic */ Object getPayload() {
            return super.getPayload();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent
        public /* bridge */ /* synthetic */ Class getType() {
            return super.getType();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent, java.util.EventObject
        public /* bridge */ /* synthetic */ Serializable getSource() {
            return super.getSource();
        }

        @Override // org.springframework.data.keyvalue.core.event.KeyValueEvent.KeyBasedEvent
        public /* bridge */ /* synthetic */ Serializable getKey() {
            return super.getKey();
        }

        public AfterDeleteEvent(Serializable key, String keySpace, Class<? extends T> type, T payload) {
            super(key, keySpace, type, payload);
        }
    }

    /* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/event/KeyValueEvent$BeforeDropKeySpaceEvent.class */
    public static class BeforeDropKeySpaceEvent<T> extends KeyValueEvent<T> {
        public BeforeDropKeySpaceEvent(String keySpace, Class<? extends T> type) {
            super(type, keySpace);
        }

        @Override // java.util.EventObject
        public Class<T> getSource() {
            return (Class) super.getSource();
        }
    }

    /* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/event/KeyValueEvent$AfterDropKeySpaceEvent.class */
    public static class AfterDropKeySpaceEvent<T> extends KeyValueEvent<T> {
        public AfterDropKeySpaceEvent(String keySpace, Class<? extends T> type) {
            super(type, keySpace);
        }

        @Override // java.util.EventObject
        public Class<T> getSource() {
            return (Class) super.getSource();
        }
    }
}
