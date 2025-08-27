package io.netty.util;

import io.netty.util.internal.ObjectUtil;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/DefaultAttributeMap.class */
public class DefaultAttributeMap implements AttributeMap {
    private static final AtomicReferenceFieldUpdater<DefaultAttributeMap, AtomicReferenceArray> updater = AtomicReferenceFieldUpdater.newUpdater(DefaultAttributeMap.class, AtomicReferenceArray.class, "attributes");
    private static final int BUCKET_SIZE = 4;
    private static final int MASK = 3;
    private volatile AtomicReferenceArray<DefaultAttribute<?>> attributes;

    @Override // io.netty.util.AttributeMap
    public <T> Attribute<T> attr(AttributeKey<T> key) {
        ObjectUtil.checkNotNull(key, "key");
        AtomicReferenceArray<DefaultAttribute<?>> attributes = this.attributes;
        if (attributes == null) {
            attributes = new AtomicReferenceArray<>(4);
            if (!updater.compareAndSet(this, null, attributes)) {
                attributes = this.attributes;
            }
        }
        int i = index(key);
        DefaultAttribute<?> head = attributes.get(i);
        if (head == null) {
            DefaultAttribute<?> head2 = new DefaultAttribute<>();
            DefaultAttribute<T> attr = new DefaultAttribute<>(head2, key);
            ((DefaultAttribute) head2).next = attr;
            ((DefaultAttribute) attr).prev = head2;
            if (attributes.compareAndSet(i, null, head2)) {
                return attr;
            }
            head = attributes.get(i);
        }
        synchronized (head) {
            DefaultAttribute<?> curr = head;
            while (true) {
                DefaultAttribute<?> next = ((DefaultAttribute) curr).next;
                if (next == null) {
                    DefaultAttribute<T> attr2 = new DefaultAttribute<>(head, key);
                    ((DefaultAttribute) curr).next = attr2;
                    ((DefaultAttribute) attr2).prev = curr;
                    return attr2;
                }
                if (((DefaultAttribute) next).key == key && !((DefaultAttribute) next).removed) {
                    return next;
                }
                curr = next;
            }
        }
    }

    @Override // io.netty.util.AttributeMap
    public <T> boolean hasAttr(AttributeKey<T> key) {
        ObjectUtil.checkNotNull(key, "key");
        AtomicReferenceArray<DefaultAttribute<?>> attributes = this.attributes;
        if (attributes == null) {
            return false;
        }
        int i = index(key);
        DefaultAttribute<?> head = attributes.get(i);
        if (head == null) {
            return false;
        }
        synchronized (head) {
            for (DefaultAttribute<?> curr = ((DefaultAttribute) head).next; curr != null; curr = ((DefaultAttribute) curr).next) {
                if (((DefaultAttribute) curr).key == key && !((DefaultAttribute) curr).removed) {
                    return true;
                }
            }
            return false;
        }
    }

    private static int index(AttributeKey<?> key) {
        return key.id() & 3;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/DefaultAttributeMap$DefaultAttribute.class */
    private static final class DefaultAttribute<T> extends AtomicReference<T> implements Attribute<T> {
        private static final long serialVersionUID = -2661411462200283011L;
        private final DefaultAttribute<?> head;
        private final AttributeKey<T> key;
        private DefaultAttribute<?> prev;
        private DefaultAttribute<?> next;
        private volatile boolean removed;

        DefaultAttribute(DefaultAttribute<?> head, AttributeKey<T> key) {
            this.head = head;
            this.key = key;
        }

        /* JADX WARN: Multi-variable type inference failed */
        DefaultAttribute() {
            this.head = this;
            this.key = null;
        }

        @Override // io.netty.util.Attribute
        public AttributeKey<T> key() {
            return this.key;
        }

        @Override // io.netty.util.Attribute
        public T setIfAbsent(T value) {
            while (!compareAndSet(null, value)) {
                T old = get();
                if (old != null) {
                    return old;
                }
            }
            return null;
        }

        @Override // io.netty.util.Attribute
        public T getAndRemove() {
            this.removed = true;
            T oldValue = getAndSet(null);
            remove0();
            return oldValue;
        }

        @Override // io.netty.util.Attribute
        public void remove() {
            this.removed = true;
            set(null);
            remove0();
        }

        private void remove0() {
            synchronized (this.head) {
                if (this.prev == null) {
                    return;
                }
                this.prev.next = this.next;
                if (this.next != null) {
                    this.next.prev = this.prev;
                }
                this.prev = null;
                this.next = null;
            }
        }
    }
}
