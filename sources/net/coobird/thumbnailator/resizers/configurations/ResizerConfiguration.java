package net.coobird.thumbnailator.resizers.configurations;

import java.awt.RenderingHints;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/resizers/configurations/ResizerConfiguration.class */
public interface ResizerConfiguration {
    RenderingHints.Key getKey();

    Object getValue();
}
