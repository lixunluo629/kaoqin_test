package org.apache.commons.collections4.functors;

import java.io.Serializable;
import org.apache.commons.collections4.Transformer;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/functors/StringValueTransformer.class */
public final class StringValueTransformer<T> implements Transformer<T, String>, Serializable {
    private static final long serialVersionUID = 7511110693171758606L;
    private static final Transformer<Object, String> INSTANCE = new StringValueTransformer();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.collections4.Transformer
    public /* bridge */ /* synthetic */ String transform(Object obj) {
        return transform((StringValueTransformer<T>) obj);
    }

    public static <T> Transformer<T, String> stringValueTransformer() {
        return (Transformer<T, String>) INSTANCE;
    }

    private StringValueTransformer() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.collections4.Transformer
    public String transform(T input) {
        return String.valueOf(input);
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
