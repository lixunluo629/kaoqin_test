package com.itextpdf.io.util;

import java.util.ArrayList;
import java.util.List;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/util/GenericArray.class */
public class GenericArray<T> {
    private List<T> array;

    public GenericArray(int size) {
        this.array = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            this.array.add(null);
        }
    }

    public T get(int index) {
        return this.array.get(index);
    }

    public T set(int index, T element) {
        return this.array.set(index, element);
    }
}
