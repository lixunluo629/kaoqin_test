package net.coobird.thumbnailator.resizers.configurations;

import java.awt.RenderingHints;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/resizers/configurations/Dithering.class */
public enum Dithering implements ResizerConfiguration {
    ENABLE(RenderingHints.VALUE_DITHER_ENABLE),
    DISABLE(RenderingHints.VALUE_DITHER_DISABLE),
    DEFAULT(RenderingHints.VALUE_DITHER_DEFAULT);

    private final Object value;

    Dithering(Object obj) {
        this.value = obj;
    }

    @Override // net.coobird.thumbnailator.resizers.configurations.ResizerConfiguration
    public RenderingHints.Key getKey() {
        return RenderingHints.KEY_DITHERING;
    }

    @Override // net.coobird.thumbnailator.resizers.configurations.ResizerConfiguration
    public Object getValue() {
        return this.value;
    }
}
