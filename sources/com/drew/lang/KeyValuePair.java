package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.StringValue;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/lang/KeyValuePair.class */
public class KeyValuePair {
    private final String _key;
    private final StringValue _value;

    public KeyValuePair(@NotNull String key, @NotNull StringValue value) {
        this._key = key;
        this._value = value;
    }

    @NotNull
    public String getKey() {
        return this._key;
    }

    @NotNull
    public StringValue getValue() {
        return this._value;
    }
}
