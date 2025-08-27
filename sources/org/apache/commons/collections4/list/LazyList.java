package org.apache.commons.collections4.list;

import java.util.List;
import org.apache.commons.collections4.Factory;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/list/LazyList.class */
public class LazyList<E> extends AbstractSerializableListDecorator<E> {
    private static final long serialVersionUID = -1708388017160694542L;
    private final Factory<? extends E> factory;

    public static <E> LazyList<E> lazyList(List<E> list, Factory<? extends E> factory) {
        return new LazyList<>(list, factory);
    }

    protected LazyList(List<E> list, Factory<? extends E> factory) {
        super(list);
        if (factory == null) {
            throw new IllegalArgumentException("Factory must not be null");
        }
        this.factory = factory;
    }

    @Override // org.apache.commons.collections4.list.AbstractListDecorator, java.util.List
    public E get(int index) {
        int size = decorated().size();
        if (index < size) {
            E object = decorated().get(index);
            if (object == null) {
                E object2 = this.factory.create();
                decorated().set(index, object2);
                return object2;
            }
            return object;
        }
        for (int i = size; i < index; i++) {
            decorated().add(null);
        }
        E object3 = this.factory.create();
        decorated().add(object3);
        return object3;
    }

    @Override // org.apache.commons.collections4.list.AbstractListDecorator, java.util.List
    public List<E> subList(int fromIndex, int toIndex) {
        List<E> sub = decorated().subList(fromIndex, toIndex);
        return new LazyList(sub, this.factory);
    }
}
