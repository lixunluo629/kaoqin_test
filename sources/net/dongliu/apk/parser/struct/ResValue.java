package net.dongliu.apk.parser.struct;

import javax.annotation.Nullable;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/ResValue.class */
public class ResValue {
    private int size;
    private short res0;
    private short dataType;

    @Nullable
    private ResourceValue data;

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/ResValue$ResDataCOMPLEX.class */
    public static class ResDataCOMPLEX {
        public static final short UNIT_SHIFT = 0;
        public static final short UNIT_MASK = 15;
        public static final short UNIT_PX = 0;
        public static final short UNIT_DIP = 1;
        public static final short UNIT_SP = 2;
        public static final short UNIT_PT = 3;
        public static final short UNIT_IN = 4;
        public static final short UNIT_MM = 5;
        public static final short UNIT_FRACTION = 0;
        public static final short UNIT_FRACTION_PARENT = 1;
        public static final short RADIX_SHIFT = 4;
        public static final short RADIX_MASK = 3;
        public static final short RADIX_23p0 = 0;
        public static final short RADIX_16p7 = 1;
        public static final short RADIX_8p15 = 2;
        public static final short RADIX_0p23 = 3;
        public static final short MANTISSA_SHIFT = 8;
        public static final int MANTISSA_MASK = 16777215;
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/ResValue$ResType.class */
    public static class ResType {
        public static final short NULL = 0;
        public static final short REFERENCE = 1;
        public static final short ATTRIBUTE = 2;
        public static final short STRING = 3;
        public static final short FLOAT = 4;
        public static final short DIMENSION = 5;
        public static final short FRACTION = 6;
        public static final short FIRST_INT = 16;
        public static final short INT_DEC = 16;
        public static final short INT_HEX = 17;
        public static final short INT_BOOLEAN = 18;
        public static final short FIRST_COLOR_INT = 28;
        public static final short INT_COLOR_ARGB8 = 28;
        public static final short INT_COLOR_RGB8 = 29;
        public static final short INT_COLOR_ARGB4 = 30;
        public static final short INT_COLOR_RGB4 = 31;
        public static final short LAST_COLOR_INT = 31;
        public static final short LAST_INT = 31;
    }

    public String toString() {
        return "ResValue{size=" + this.size + ", res0=" + ((int) this.res0) + ", dataType=" + ((int) this.dataType) + ", data=" + this.data + '}';
    }
}
