package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/collect/CollectPreconditions.class */
final class CollectPreconditions {
    CollectPreconditions() {
    }

    static void checkEntryNotNull(Object key, Object value) {
        if (key == null) {
            String strValueOf = String.valueOf(String.valueOf(value));
            throw new NullPointerException(new StringBuilder(24 + strValueOf.length()).append("null key in entry: null=").append(strValueOf).toString());
        }
        if (value == null) {
            String strValueOf2 = String.valueOf(String.valueOf(key));
            throw new NullPointerException(new StringBuilder(26 + strValueOf2.length()).append("null value in entry: ").append(strValueOf2).append("=null").toString());
        }
    }

    static int checkNonnegative(int value, String name) {
        if (value < 0) {
            String strValueOf = String.valueOf(String.valueOf(name));
            throw new IllegalArgumentException(new StringBuilder(40 + strValueOf.length()).append(strValueOf).append(" cannot be negative but was: ").append(value).toString());
        }
        return value;
    }

    static void checkRemove(boolean canRemove) {
        Preconditions.checkState(canRemove, "no calls to next() since the last call to remove()");
    }
}
