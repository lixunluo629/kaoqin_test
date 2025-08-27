package io.netty.util.internal;

import com.itextpdf.kernel.xmp.PdfConst;
import io.netty.util.Recycler;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/ObjectPool.class */
public abstract class ObjectPool<T> {

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/ObjectPool$Handle.class */
    public interface Handle<T> {
        void recycle(T t);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/ObjectPool$ObjectCreator.class */
    public interface ObjectCreator<T> {
        T newObject(Handle<T> handle);
    }

    public abstract T get();

    ObjectPool() {
    }

    public static <T> ObjectPool<T> newPool(ObjectCreator<T> creator) {
        return new RecyclerObjectPool((ObjectCreator) ObjectUtil.checkNotNull(creator, PdfConst.Creator));
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/ObjectPool$RecyclerObjectPool.class */
    private static final class RecyclerObjectPool<T> extends ObjectPool<T> {
        private final Recycler<T> recycler;

        RecyclerObjectPool(final ObjectCreator<T> creator) {
            this.recycler = new Recycler<T>() { // from class: io.netty.util.internal.ObjectPool.RecyclerObjectPool.1
                @Override // io.netty.util.Recycler
                protected T newObject(Recycler.Handle<T> handle) {
                    return (T) creator.newObject(handle);
                }
            };
        }

        @Override // io.netty.util.internal.ObjectPool
        public T get() {
            return this.recycler.get();
        }
    }
}
