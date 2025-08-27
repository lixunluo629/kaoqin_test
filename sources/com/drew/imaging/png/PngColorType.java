package com.drew.imaging.png;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/imaging/png/PngColorType.class */
public enum PngColorType {
    Greyscale(0, "Greyscale", 1, 2, 4, 8, 16),
    TrueColor(2, "True Color", 8, 16),
    IndexedColor(3, "Indexed Color", 1, 2, 4, 8),
    GreyscaleWithAlpha(4, "Greyscale with Alpha", 8, 16),
    TrueColorWithAlpha(6, "True Color with Alpha", 8, 16);

    private final int _numericValue;

    @NotNull
    private final String _description;

    @NotNull
    private final int[] _allowedBitDepths;

    @Nullable
    public static PngColorType fromNumericValue(int numericValue) {
        PngColorType[] colorTypes = (PngColorType[]) PngColorType.class.getEnumConstants();
        for (PngColorType colorType : colorTypes) {
            if (colorType.getNumericValue() == numericValue) {
                return colorType;
            }
        }
        return null;
    }

    PngColorType(int numericValue, @NotNull String description, @NotNull int... allowedBitDepths) {
        this._numericValue = numericValue;
        this._description = description;
        this._allowedBitDepths = allowedBitDepths;
    }

    public int getNumericValue() {
        return this._numericValue;
    }

    @NotNull
    public String getDescription() {
        return this._description;
    }

    @NotNull
    public int[] getAllowedBitDepths() {
        return this._allowedBitDepths;
    }
}
