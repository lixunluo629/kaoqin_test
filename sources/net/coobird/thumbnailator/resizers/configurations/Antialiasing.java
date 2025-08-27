package net.coobird.thumbnailator.resizers.configurations;

import java.awt.RenderingHints;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/resizers/configurations/Antialiasing.class */
public enum Antialiasing implements ResizerConfiguration {
    ON(RenderingHints.VALUE_ANTIALIAS_ON),
    OFF(RenderingHints.VALUE_ANTIALIAS_OFF),
    DEFAULT(RenderingHints.VALUE_ANTIALIAS_DEFAULT);

    private final Object value;

    Antialiasing(Object obj) {
        this.value = obj;
    }

    @Override // net.coobird.thumbnailator.resizers.configurations.ResizerConfiguration
    public RenderingHints.Key getKey() {
        return RenderingHints.KEY_ANTIALIASING;
    }

    @Override // net.coobird.thumbnailator.resizers.configurations.ResizerConfiguration
    public Object getValue() {
        return this.value;
    }
}
