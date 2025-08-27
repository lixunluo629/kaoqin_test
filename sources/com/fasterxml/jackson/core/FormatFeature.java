package com.fasterxml.jackson.core;

/* loaded from: jackson-core-2.11.2.jar:com/fasterxml/jackson/core/FormatFeature.class */
public interface FormatFeature {
    boolean enabledByDefault();

    int getMask();

    boolean enabledIn(int i);
}
