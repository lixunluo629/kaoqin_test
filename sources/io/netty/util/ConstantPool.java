package io.netty.util;

import io.netty.util.Constant;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/ConstantPool.class */
public abstract class ConstantPool<T extends Constant<T>> {
    private final ConcurrentMap<String, T> constants = PlatformDependent.newConcurrentHashMap();
    private final AtomicInteger nextId = new AtomicInteger(1);

    protected abstract T newConstant(int i, String str);

    public T valueOf(Class<?> cls, String str) {
        return (T) valueOf(((Class) ObjectUtil.checkNotNull(cls, "firstNameComponent")).getName() + '#' + ((String) ObjectUtil.checkNotNull(str, "secondNameComponent")));
    }

    public T valueOf(String str) {
        checkNotNullAndNotEmpty(str);
        return (T) getOrCreate(str);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11, types: [io.netty.util.Constant] */
    private T getOrCreate(String str) {
        T t = this.constants.get(str);
        if (t == null) {
            T t2 = (T) newConstant(nextId(), str);
            t = (Constant) this.constants.putIfAbsent(str, t2);
            if (t == null) {
                return t2;
            }
        }
        return t;
    }

    public boolean exists(String name) {
        checkNotNullAndNotEmpty(name);
        return this.constants.containsKey(name);
    }

    public T newInstance(String str) {
        checkNotNullAndNotEmpty(str);
        return (T) createOrThrow(str);
    }

    private T createOrThrow(String str) {
        if (this.constants.get(str) == null) {
            T t = (T) newConstant(nextId(), str);
            if (((Constant) this.constants.putIfAbsent(str, t)) == null) {
                return t;
            }
        }
        throw new IllegalArgumentException(String.format("'%s' is already in use", str));
    }

    private static String checkNotNullAndNotEmpty(String name) {
        ObjectUtil.checkNotNull(name, "name");
        if (name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        return name;
    }

    @Deprecated
    public final int nextId() {
        return this.nextId.getAndIncrement();
    }
}
