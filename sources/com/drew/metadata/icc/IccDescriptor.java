package com.drew.metadata.icc;

import com.drew.lang.ByteArrayReader;
import com.drew.lang.RandomAccessReader;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import org.apache.commons.httpclient.auth.NTLM;
import org.aspectj.weaver.Dump;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/icc/IccDescriptor.class */
public class IccDescriptor extends TagDescriptor<IccDirectory> {
    private static final int ICC_TAG_TYPE_TEXT = 1952807028;
    private static final int ICC_TAG_TYPE_DESC = 1684370275;
    private static final int ICC_TAG_TYPE_SIG = 1936287520;
    private static final int ICC_TAG_TYPE_MEAS = 1835360627;
    private static final int ICC_TAG_TYPE_XYZ_ARRAY = 1482250784;
    private static final int ICC_TAG_TYPE_MLUC = 1835824483;
    private static final int ICC_TAG_TYPE_CURV = 1668641398;

    public IccDescriptor(@NotNull IccDirectory directory) {
        super(directory);
    }

    @Override // com.drew.metadata.TagDescriptor
    public String getDescription(int tagType) {
        switch (tagType) {
            case 8:
                return getProfileVersionDescription();
            case 12:
                return getProfileClassDescription();
            case 40:
                return getPlatformDescription();
            case 64:
                return getRenderingIntentDescription();
            default:
                if (tagType > 538976288 && tagType < 2054847098) {
                    return getTagDataString(tagType);
                }
                return super.getDescription(tagType);
        }
    }

    @Nullable
    private String getTagDataString(int tagType) {
        byte[] bytes;
        String name;
        String observerString;
        String geometryString;
        String illuminantString;
        try {
            bytes = ((IccDirectory) this._directory).getByteArray(tagType);
        } catch (IOException e) {
            return null;
        }
        if (bytes == null) {
            return ((IccDirectory) this._directory).getString(tagType);
        }
        RandomAccessReader reader = new ByteArrayReader(bytes);
        int iccTagType = reader.getInt32(0);
        switch (iccTagType) {
            case ICC_TAG_TYPE_XYZ_ARRAY /* 1482250784 */:
                StringBuilder res = new StringBuilder();
                DecimalFormat format = new DecimalFormat("0.####");
                int count = (bytes.length - 8) / 12;
                for (int i = 0; i < count; i++) {
                    float x = reader.getS15Fixed16(8 + (i * 12));
                    float y = reader.getS15Fixed16(8 + (i * 12) + 4);
                    float z = reader.getS15Fixed16(8 + (i * 12) + 8);
                    if (i > 0) {
                        res.append(", ");
                    }
                    res.append("(").append(format.format(x)).append(", ").append(format.format(y)).append(", ").append(format.format(z)).append(")");
                }
                return res.toString();
            case ICC_TAG_TYPE_CURV /* 1668641398 */:
                int num = reader.getInt32(8);
                StringBuilder res2 = new StringBuilder();
                for (int i2 = 0; i2 < num; i2++) {
                    if (i2 != 0) {
                        res2.append(", ");
                    }
                    res2.append(formatDoubleAsString(reader.getUInt16(12 + (i2 * 2)) / 65535.0d, 7, false));
                }
                return res2.toString();
            case 1684370275:
                int stringLength = reader.getInt32(8);
                return new String(bytes, 12, stringLength - 1);
            case 1835360627:
                int observerType = reader.getInt32(8);
                float x2 = reader.getS15Fixed16(12);
                float y2 = reader.getS15Fixed16(16);
                float z2 = reader.getS15Fixed16(20);
                int geometryType = reader.getInt32(24);
                float flare = reader.getS15Fixed16(28);
                int illuminantType = reader.getInt32(32);
                switch (observerType) {
                    case 0:
                        observerString = Dump.UNKNOWN_FILENAME;
                        break;
                    case 1:
                        observerString = "1931 2°";
                        break;
                    case 2:
                        observerString = "1964 10°";
                        break;
                    default:
                        observerString = String.format("Unknown %d", Integer.valueOf(observerType));
                        break;
                }
                switch (geometryType) {
                    case 0:
                        geometryString = Dump.UNKNOWN_FILENAME;
                        break;
                    case 1:
                        geometryString = "0/45 or 45/0";
                        break;
                    case 2:
                        geometryString = "0/d or d/0";
                        break;
                    default:
                        geometryString = String.format("Unknown %d", Integer.valueOf(observerType));
                        break;
                }
                switch (illuminantType) {
                    case 0:
                        illuminantString = "unknown";
                        break;
                    case 1:
                        illuminantString = "D50";
                        break;
                    case 2:
                        illuminantString = "D65";
                        break;
                    case 3:
                        illuminantString = "D93";
                        break;
                    case 4:
                        illuminantString = "F2";
                        break;
                    case 5:
                        illuminantString = "D55";
                        break;
                    case 6:
                        illuminantString = "A";
                        break;
                    case 7:
                        illuminantString = "Equi-Power (E)";
                        break;
                    case 8:
                        illuminantString = "F8";
                        break;
                    default:
                        illuminantString = String.format("Unknown %d", Integer.valueOf(illuminantType));
                        break;
                }
                DecimalFormat format2 = new DecimalFormat("0.###");
                return String.format("%s Observer, Backing (%s, %s, %s), Geometry %s, Flare %d%%, Illuminant %s", observerString, format2.format(x2), format2.format(y2), format2.format(z2), geometryString, Integer.valueOf(Math.round(flare * 100.0f)), illuminantString);
            case ICC_TAG_TYPE_MLUC /* 1835824483 */:
                int int1 = reader.getInt32(8);
                StringBuilder res3 = new StringBuilder();
                res3.append(int1);
                for (int i3 = 0; i3 < int1; i3++) {
                    String str = IccReader.getStringFromInt32(reader.getInt32(16 + (i3 * 12)));
                    int len = reader.getInt32(16 + (i3 * 12) + 4);
                    int ofs = reader.getInt32(16 + (i3 * 12) + 8);
                    try {
                        name = new String(bytes, ofs, len, "UTF-16BE");
                    } catch (UnsupportedEncodingException e2) {
                        name = new String(bytes, ofs, len);
                    }
                    res3.append(SymbolConstants.SPACE_SYMBOL).append(str).append("(").append(name).append(")");
                }
                return res3.toString();
            case ICC_TAG_TYPE_SIG /* 1936287520 */:
                return IccReader.getStringFromInt32(reader.getInt32(8));
            case ICC_TAG_TYPE_TEXT /* 1952807028 */:
                try {
                    return new String(bytes, 8, (bytes.length - 8) - 1, NTLM.DEFAULT_CHARSET);
                } catch (UnsupportedEncodingException e3) {
                    return new String(bytes, 8, (bytes.length - 8) - 1);
                }
            default:
                return String.format("%s (0x%08X): %d bytes", IccReader.getStringFromInt32(iccTagType), Integer.valueOf(iccTagType), Integer.valueOf(bytes.length));
        }
        return null;
    }

    @NotNull
    public static String formatDoubleAsString(double value, int precision, boolean zeroes) {
        if (precision < 1) {
            return "" + Math.round(value);
        }
        long intPart = Math.abs((long) value);
        long rest = (int) Math.round((Math.abs(value) - intPart) * Math.pow(10.0d, precision));
        String res = "";
        for (int i = precision; i > 0; i--) {
            byte cour = (byte) Math.abs(rest % 10);
            rest /= 10;
            if (res.length() > 0 || zeroes || cour != 0 || i == 1) {
                res = ((int) cour) + res;
            }
        }
        long intPart2 = intPart + rest;
        boolean isNegative = value < 0.0d && !(intPart2 == 0 && rest == 0);
        return (isNegative ? "-" : "") + intPart2 + "." + res;
    }

    @Nullable
    private String getRenderingIntentDescription() {
        return getIndexedDescription(64, "Perceptual", "Media-Relative Colorimetric", "Saturation", "ICC-Absolute Colorimetric");
    }

    @Nullable
    private String getPlatformDescription() {
        String str = ((IccDirectory) this._directory).getString(40);
        if (str == null) {
            return null;
        }
        try {
            int i = getInt32FromString(str);
            switch (i) {
                case 1095782476:
                    return "Apple Computer, Inc.";
                case 1297303124:
                    return "Microsoft Corporation";
                case 1397180704:
                    return "Silicon Graphics, Inc.";
                case 1398099543:
                    return "Sun Microsystems, Inc.";
                case 1413959252:
                    return "Taligent, Inc.";
                default:
                    return String.format("Unknown (%s)", str);
            }
        } catch (IOException e) {
            return str;
        }
    }

    @Nullable
    private String getProfileClassDescription() {
        String str = ((IccDirectory) this._directory).getString(12);
        if (str == null) {
            return null;
        }
        try {
            int i = getInt32FromString(str);
            switch (i) {
                case 1633842036:
                    return "Abstract";
                case 1818848875:
                    return "DeviceLink";
                case 1835955314:
                    return "Display Device";
                case 1852662636:
                    return "Named Color";
                case 1886549106:
                    return "Output Device";
                case 1935896178:
                    return "Input Device";
                case 1936744803:
                    return "ColorSpace Conversion";
                default:
                    return String.format("Unknown (%s)", str);
            }
        } catch (IOException e) {
            return str;
        }
    }

    @Nullable
    private String getProfileVersionDescription() {
        Integer value = ((IccDirectory) this._directory).getInteger(8);
        if (value == null) {
            return null;
        }
        int m = (value.intValue() & (-16777216)) >> 24;
        int r = (value.intValue() & 15728640) >> 20;
        int R = (value.intValue() & 983040) >> 16;
        return String.format("%d.%d.%d", Integer.valueOf(m), Integer.valueOf(r), Integer.valueOf(R));
    }

    private static int getInt32FromString(@NotNull String string) throws IOException {
        byte[] bytes = string.getBytes();
        return new ByteArrayReader(bytes).getInt32(0);
    }
}
