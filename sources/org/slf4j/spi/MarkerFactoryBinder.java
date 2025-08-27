package org.slf4j.spi;

import org.slf4j.IMarkerFactory;

/* loaded from: slf4j-api-1.7.26.jar:org/slf4j/spi/MarkerFactoryBinder.class */
public interface MarkerFactoryBinder {
    IMarkerFactory getMarkerFactory();

    String getMarkerFactoryClassStr();
}
