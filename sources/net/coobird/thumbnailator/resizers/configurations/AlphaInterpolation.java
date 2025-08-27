package net.coobird.thumbnailator.resizers.configurations;

import java.awt.RenderingHints;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/resizers/configurations/AlphaInterpolation.class */
public enum AlphaInterpolation implements ResizerConfiguration {
    SPEED(RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED),
    QUALITY(RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY),
    DEFAULT(RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);

    private final Object value;

    AlphaInterpolation(Object obj) {
        this.value = obj;
    }

    @Override // net.coobird.thumbnailator.resizers.configurations.ResizerConfiguration
    public RenderingHints.Key getKey() {
        return RenderingHints.KEY_ALPHA_INTERPOLATION;
    }

    @Override // net.coobird.thumbnailator.resizers.configurations.ResizerConfiguration
    public Object getValue() {
        return this.value;
    }
}
