package org.apache.poi.hpsf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.apache.bcel.Constants;

/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/Variant.class */
public class Variant {
    public static final int VT_EMPTY = 0;
    public static final int VT_NULL = 1;
    public static final int VT_I2 = 2;
    public static final int VT_I4 = 3;
    public static final int VT_R4 = 4;
    public static final int VT_R8 = 5;
    public static final int VT_CY = 6;
    public static final int VT_DATE = 7;
    public static final int VT_BSTR = 8;
    public static final int VT_DISPATCH = 9;
    public static final int VT_ERROR = 10;
    public static final int VT_BOOL = 11;
    public static final int VT_VARIANT = 12;
    public static final int VT_UNKNOWN = 13;
    public static final int VT_DECIMAL = 14;
    public static final int VT_I1 = 16;
    public static final int VT_UI1 = 17;
    public static final int VT_UI2 = 18;
    public static final int VT_UI4 = 19;
    public static final int VT_I8 = 20;
    public static final int VT_UI8 = 21;
    public static final int VT_INT = 22;
    public static final int VT_UINT = 23;
    public static final int VT_VOID = 24;
    public static final int VT_HRESULT = 25;
    public static final int VT_PTR = 26;
    public static final int VT_SAFEARRAY = 27;
    public static final int VT_CARRAY = 28;
    public static final int VT_USERDEFINED = 29;
    public static final int VT_LPSTR = 30;
    public static final int VT_LPWSTR = 31;
    public static final int VT_FILETIME = 64;
    public static final int VT_BLOB = 65;
    public static final int VT_STREAM = 66;
    public static final int VT_STORAGE = 67;
    public static final int VT_STREAMED_OBJECT = 68;
    public static final int VT_STORED_OBJECT = 69;
    public static final int VT_BLOB_OBJECT = 70;
    public static final int VT_CF = 71;
    public static final int VT_CLSID = 72;
    public static final int VT_VERSIONED_STREAM = 73;
    public static final int VT_VECTOR = 4096;
    public static final int VT_ARRAY = 8192;
    public static final int VT_BYREF = 16384;
    public static final int VT_RESERVED = 32768;
    public static final int VT_ILLEGAL = 65535;
    public static final int VT_ILLEGALMASKED = 4095;
    public static final int VT_TYPEMASK = 4095;
    private static final Map<Long, String> numberToName;
    private static final Map<Long, Integer> numberToLength;
    public static final Integer LENGTH_UNKNOWN = -2;
    public static final Integer LENGTH_VARIABLE = -1;
    public static final Integer LENGTH_0 = 0;
    public static final Integer LENGTH_2 = 2;
    public static final Integer LENGTH_4 = 4;
    public static final Integer LENGTH_8 = 8;
    private static final Object[][] NUMBER_TO_NAME_LIST = {new Object[]{0L, "VT_EMPTY", LENGTH_0}, new Object[]{1L, "VT_NULL", LENGTH_UNKNOWN}, new Object[]{2L, "VT_I2", LENGTH_2}, new Object[]{3L, "VT_I4", LENGTH_4}, new Object[]{4L, "VT_R4", LENGTH_4}, new Object[]{5L, "VT_R8", LENGTH_8}, new Object[]{6L, "VT_CY", LENGTH_UNKNOWN}, new Object[]{7L, "VT_DATE", LENGTH_UNKNOWN}, new Object[]{8L, "VT_BSTR", LENGTH_UNKNOWN}, new Object[]{9L, "VT_DISPATCH", LENGTH_UNKNOWN}, new Object[]{10L, "VT_ERROR", LENGTH_UNKNOWN}, new Object[]{11L, "VT_BOOL", LENGTH_UNKNOWN}, new Object[]{12L, "VT_VARIANT", LENGTH_UNKNOWN}, new Object[]{13L, "VT_UNKNOWN", LENGTH_UNKNOWN}, new Object[]{14L, "VT_DECIMAL", LENGTH_UNKNOWN}, new Object[]{16L, "VT_I1", LENGTH_UNKNOWN}, new Object[]{17L, "VT_UI1", LENGTH_UNKNOWN}, new Object[]{18L, "VT_UI2", LENGTH_UNKNOWN}, new Object[]{19L, "VT_UI4", LENGTH_UNKNOWN}, new Object[]{20L, "VT_I8", LENGTH_UNKNOWN}, new Object[]{21L, "VT_UI8", LENGTH_UNKNOWN}, new Object[]{22L, "VT_INT", LENGTH_UNKNOWN}, new Object[]{23L, "VT_UINT", LENGTH_UNKNOWN}, new Object[]{24L, "VT_VOID", LENGTH_UNKNOWN}, new Object[]{25L, "VT_HRESULT", LENGTH_UNKNOWN}, new Object[]{26L, "VT_PTR", LENGTH_UNKNOWN}, new Object[]{27L, "VT_SAFEARRAY", LENGTH_UNKNOWN}, new Object[]{28L, "VT_CARRAY", LENGTH_UNKNOWN}, new Object[]{29L, "VT_USERDEFINED", LENGTH_UNKNOWN}, new Object[]{30L, "VT_LPSTR", LENGTH_VARIABLE}, new Object[]{31L, "VT_LPWSTR", LENGTH_UNKNOWN}, new Object[]{64L, "VT_FILETIME", LENGTH_8}, new Object[]{65L, "VT_BLOB", LENGTH_UNKNOWN}, new Object[]{66L, "VT_STREAM", LENGTH_UNKNOWN}, new Object[]{67L, "VT_STORAGE", LENGTH_UNKNOWN}, new Object[]{68L, "VT_STREAMED_OBJECT", LENGTH_UNKNOWN}, new Object[]{69L, "VT_STORED_OBJECT", LENGTH_UNKNOWN}, new Object[]{70L, "VT_BLOB_OBJECT", LENGTH_UNKNOWN}, new Object[]{71L, "VT_CF", LENGTH_UNKNOWN}, new Object[]{72L, "VT_CLSID", LENGTH_UNKNOWN}};

    /* JADX WARN: Type inference failed for: r0v13, types: [java.lang.Object[], java.lang.Object[][]] */
    static {
        Map<Long, String> number2Name = new HashMap<>(NUMBER_TO_NAME_LIST.length, 1.0f);
        Map<Long, Integer> number2Len = new HashMap<>(NUMBER_TO_NAME_LIST.length, 1.0f);
        Object[][] arr$ = NUMBER_TO_NAME_LIST;
        for (Object[] nn : arr$) {
            number2Name.put((Long) nn[0], (String) nn[1]);
            number2Len.put((Long) nn[0], (Integer) nn[2]);
        }
        numberToName = Collections.unmodifiableMap(number2Name);
        numberToLength = Collections.unmodifiableMap(number2Len);
    }

    public static String getVariantName(long variantType) {
        long vt = variantType;
        String name = "";
        if ((vt & Constants.NEGATABLE) != 0) {
            name = "Vector of ";
            vt -= Constants.NEGATABLE;
        } else if ((vt & 8192) != 0) {
            name = "Array of ";
            vt -= 8192;
        } else if ((vt & 16384) != 0) {
            name = "ByRef of ";
            vt -= 16384;
        }
        String name2 = name + numberToName.get(Long.valueOf(vt));
        return (name2 == null || "".equals(name2)) ? "unknown variant type" : name2;
    }

    public static int getVariantLength(long variantType) {
        Integer length = numberToLength.get(Long.valueOf(variantType));
        return (length != null ? length : LENGTH_UNKNOWN).intValue();
    }
}
