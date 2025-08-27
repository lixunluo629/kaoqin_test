package org.slf4j;

/* loaded from: slf4j-api-1.7.26.jar:org/slf4j/IMarkerFactory.class */
public interface IMarkerFactory {
    Marker getMarker(String str);

    boolean exists(String str);

    boolean detachMarker(String str);

    Marker getDetachedMarker(String str);
}
