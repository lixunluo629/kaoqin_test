package net.coobird.thumbnailator.resizers.configurations;

import java.awt.RenderingHints;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/resizers/configurations/Rendering.class */
public enum Rendering implements ResizerConfiguration {
    SPEED(RenderingHints.VALUE_RENDER_SPEED),
    QUALITY(RenderingHints.VALUE_RENDER_QUALITY),
    DEFAULT(RenderingHints.VALUE_RENDER_DEFAULT);

    private final Object value;

    Rendering(Object obj) {
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
