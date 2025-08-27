package com.drew.imaging;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/imaging/PhotographicConversions.class */
public final class PhotographicConversions {
    public static final double ROOT_TWO = Math.sqrt(2.0d);

    private PhotographicConversions() throws Exception {
        throw new Exception("Not intended for instantiation.");
    }

    public static double apertureToFStop(double aperture) {
        return Math.pow(ROOT_TWO, aperture);
    }

    public static double shutterSpeedToExposureTime(double shutterSpeed) {
        return (float) (1.0d / Math.exp(shutterSpeed * Math.log(2.0d)));
    }
}
