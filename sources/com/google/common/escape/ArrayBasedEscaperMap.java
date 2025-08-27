package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

@Beta
@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/escape/ArrayBasedEscaperMap.class */
public final class ArrayBasedEscaperMap {
    private final char[][] replacementArray;
    private static final char[][] EMPTY_REPLACEMENT_ARRAY = new char[0][0];

    public static ArrayBasedEscaperMap create(Map<Character, String> replacements) {
        return new ArrayBasedEscaperMap(createReplacementArray(replacements));
    }

    private ArrayBasedEscaperMap(char[][] replacementArray) {
        this.replacementArray = replacementArray;
    }

    char[][] getReplacementArray() {
        return this.replacementArray;
    }

    /* JADX WARN: Type inference failed for: r0v11, types: [char[], char[][]] */
    @VisibleForTesting
    static char[][] createReplacementArray(Map<Character, String> map) {
        Preconditions.checkNotNull(map);
        if (map.isEmpty()) {
            return EMPTY_REPLACEMENT_ARRAY;
        }
        char max = ((Character) Collections.max(map.keySet())).charValue();
        ?? r0 = new char[max + 1];
        Iterator i$ = map.keySet().iterator();
        while (i$.hasNext()) {
            char c = i$.next().charValue();
            r0[c] = map.get(Character.valueOf(c)).toCharArray();
        }
        return r0;
    }
}
