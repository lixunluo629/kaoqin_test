package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import java.text.DecimalFormat;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/lang/GeoLocation.class */
public final class GeoLocation {
    private final double _latitude;
    private final double _longitude;

    public GeoLocation(double latitude, double longitude) {
        this._latitude = latitude;
        this._longitude = longitude;
    }

    public double getLatitude() {
        return this._latitude;
    }

    public double getLongitude() {
        return this._longitude;
    }

    public boolean isZero() {
        return this._latitude == 0.0d && this._longitude == 0.0d;
    }

    @NotNull
    public static String decimalToDegreesMinutesSecondsString(double decimal) {
        double[] dms = decimalToDegreesMinutesSeconds(decimal);
        DecimalFormat format = new DecimalFormat("0.##");
        return String.format("%s° %s' %s\"", format.format(dms[0]), format.format(dms[1]), format.format(dms[2]));
    }

    @NotNull
    public static double[] decimalToDegreesMinutesSeconds(double decimal) {
        int d = (int) decimal;
        double m = Math.abs((decimal % 1.0d) * 60.0d);
        double s = (m % 1.0d) * 60.0d;
        return new double[]{d, (int) m, s};
    }

    @Nullable
    public static Double degreesMinutesSecondsToDecimal(@NotNull Rational degs, @NotNull Rational mins, @NotNull Rational secs, boolean isNegative) {
        double decimal = Math.abs(degs.doubleValue()) + (mins.doubleValue() / 60.0d) + (secs.doubleValue() / 3600.0d);
        if (Double.isNaN(decimal)) {
            return null;
        }
        if (isNegative) {
            decimal *= -1.0d;
        }
        return Double.valueOf(decimal);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GeoLocation that = (GeoLocation) o;
        return Double.compare(that._latitude, this._latitude) == 0 && Double.compare(that._longitude, this._longitude) == 0;
    }

    public int hashCode() {
        long temp = this._latitude != 0.0d ? Double.doubleToLongBits(this._latitude) : 0L;
        int result = (int) (temp ^ (temp >>> 32));
        long temp2 = this._longitude != 0.0d ? Double.doubleToLongBits(this._longitude) : 0L;
        return (31 * result) + ((int) (temp2 ^ (temp2 >>> 32)));
    }

    @NotNull
    public String toString() {
        return this._latitude + ", " + this._longitude;
    }

    @NotNull
    public String toDMSString() {
        return decimalToDegreesMinutesSecondsString(this._latitude) + ", " + decimalToDegreesMinutesSecondsString(this._longitude);
    }
}
