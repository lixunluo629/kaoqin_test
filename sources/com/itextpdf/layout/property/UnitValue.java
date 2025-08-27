package com.itextpdf.layout.property;

import com.itextpdf.io.util.MessageFormatUtil;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/property/UnitValue.class */
public class UnitValue {
    public static final int POINT = 1;
    public static final int PERCENT = 2;
    protected int unitType;
    protected float value;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !UnitValue.class.desiredAssertionStatus();
    }

    public UnitValue(int unitType, float value) {
        this.unitType = unitType;
        if (!$assertionsDisabled && Float.isNaN(value)) {
            throw new AssertionError();
        }
        this.value = value;
    }

    public UnitValue(UnitValue unitValue) {
        this(unitValue.unitType, unitValue.value);
    }

    public static UnitValue createPointValue(float value) {
        return new UnitValue(1, value);
    }

    public static UnitValue createPercentValue(float value) {
        return new UnitValue(2, value);
    }

    public static UnitValue[] createPercentArray(float[] values) {
        UnitValue[] resultArray = new UnitValue[values.length];
        float sum = 0.0f;
        for (float val : values) {
            sum += val;
        }
        for (int i = 0; i < values.length; i++) {
            resultArray[i] = createPercentValue((100.0f * values[i]) / sum);
        }
        return resultArray;
    }

    public static UnitValue[] createPercentArray(int size) {
        UnitValue[] resultArray = new UnitValue[size];
        for (int i = 0; i < size; i++) {
            resultArray[i] = createPercentValue(100.0f / size);
        }
        return resultArray;
    }

    public static UnitValue[] createPointArray(float[] values) {
        UnitValue[] resultArray = new UnitValue[values.length];
        for (int i = 0; i < values.length; i++) {
            resultArray[i] = createPointValue(values[i]);
        }
        return resultArray;
    }

    public int getUnitType() {
        return this.unitType;
    }

    public void setUnitType(int unitType) {
        this.unitType = unitType;
    }

    public float getValue() {
        return this.value;
    }

    public void setValue(float value) {
        if (!$assertionsDisabled && Float.isNaN(value)) {
            throw new AssertionError();
        }
        this.value = value;
    }

    public boolean isPointValue() {
        return this.unitType == 1;
    }

    public boolean isPercentValue() {
        return this.unitType == 2;
    }

    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }
        UnitValue other = (UnitValue) obj;
        return Integer.compare(this.unitType, other.unitType) == 0 && Float.compare(this.value, other.value) == 0;
    }

    public int hashCode() {
        int hash = (71 * 7) + this.unitType;
        return (71 * hash) + Float.floatToIntBits(this.value);
    }

    public String toString() {
        return MessageFormatUtil.format(this.unitType == 2 ? "{0}%" : "{0}pt", Float.valueOf(this.value));
    }
}
