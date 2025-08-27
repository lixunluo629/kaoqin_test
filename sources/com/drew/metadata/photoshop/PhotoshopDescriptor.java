package com.drew.metadata.photoshop;

import com.drew.lang.ByteArrayReader;
import com.drew.lang.RandomAccessReader;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;
import com.mysql.jdbc.MysqlErrorNumbers;
import java.io.IOException;
import java.text.DecimalFormat;
import net.dongliu.apk.parser.struct.resource.Densities;
import org.aspectj.weaver.Dump;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/photoshop/PhotoshopDescriptor.class */
public class PhotoshopDescriptor extends TagDescriptor<PhotoshopDirectory> {
    public PhotoshopDescriptor(@NotNull PhotoshopDirectory directory) {
        super(directory);
    }

    @Override // com.drew.metadata.TagDescriptor
    public String getDescription(int tagType) {
        switch (tagType) {
            case 1002:
            case 1035:
                return getSimpleString(tagType);
            case 1003:
            case MysqlErrorNumbers.ER_CANT_CREATE_FILE /* 1004 */:
            case 1006:
            case 1007:
            case 1008:
            case 1009:
            case 1010:
            case 1011:
            case 1012:
            case 1013:
            case 1014:
            case 1015:
            case 1016:
            case 1017:
            case 1018:
            case 1019:
            case MysqlErrorNumbers.ER_CHECKREAD /* 1020 */:
            case 1021:
            case 1022:
            case 1023:
            case 1024:
            case 1025:
            case 1026:
            case 1027:
            case 1029:
            case 1031:
            case 1032:
            case MysqlErrorNumbers.ER_OUT_OF_SORTMEMORY /* 1038 */:
            case 1039:
            case 1040:
            case 1041:
            case 1042:
            case 1043:
            case 1045:
            case 1046:
            case 1047:
            case 1048:
            case 1051:
            case 1052:
            case 1053:
            case 1055:
            case MysqlErrorNumbers.ER_WRONG_GROUP_FIELD /* 1056 */:
            case 1058:
            case 1059:
            case 1060:
            case 1061:
            case MysqlErrorNumbers.ER_WRONG_FIELD_SPEC /* 1063 */:
            default:
                return super.getDescription(tagType);
            case 1005:
                return getResolutionInfoDescription();
            case 1028:
                return getBinaryDataString(tagType);
            case 1030:
                return getJpegQualityString();
            case 1033:
            case 1036:
                return getThumbnailDescription(tagType);
            case 1034:
                return getBooleanString(tagType);
            case 1037:
            case 1044:
            case 1049:
            case 1054:
                return get32BitNumberString(tagType);
            case 1050:
                return getSlicesDescription();
            case 1057:
                return getVersionDescription();
            case 1062:
                return getPrintScaleDescription();
            case 1064:
                return getPixelAspectRatioString();
        }
    }

    @Nullable
    public String getJpegQualityString() {
        String quality;
        String format;
        try {
            byte[] b = ((PhotoshopDirectory) this._directory).getByteArray(1030);
            if (b == null) {
                return ((PhotoshopDirectory) this._directory).getString(1030);
            }
            RandomAccessReader reader = new ByteArrayReader(b);
            int q = reader.getUInt16(0);
            int f = reader.getUInt16(2);
            int s = reader.getUInt16(4);
            int q1 = (q > 65535 || q < 65533) ? q <= 8 ? q + 4 : q : q - 65532;
            switch (q) {
                case 0:
                case 65533:
                case Densities.ANY /* 65534 */:
                case 65535:
                    quality = "Low";
                    break;
                case 1:
                case 2:
                case 3:
                    quality = "Medium";
                    break;
                case 4:
                case 5:
                    quality = "High";
                    break;
                case 6:
                case 7:
                case 8:
                    quality = "Maximum";
                    break;
                default:
                    quality = Dump.UNKNOWN_FILENAME;
                    break;
            }
            switch (f) {
                case 0:
                    format = "Standard";
                    break;
                case 1:
                    format = "Optimised";
                    break;
                case 257:
                    format = "Progressive";
                    break;
                default:
                    format = String.format("Unknown 0x%04X", Integer.valueOf(f));
                    break;
            }
            String scans = (s < 1 || s > 3) ? String.format("Unknown 0x%04X", Integer.valueOf(s)) : String.format("%d", Integer.valueOf(s + 2));
            return String.format("%d (%s), %s format, %s scans", Integer.valueOf(q1), quality, format, scans);
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    public String getPixelAspectRatioString() {
        try {
            byte[] bytes = ((PhotoshopDirectory) this._directory).getByteArray(1064);
            if (bytes == null) {
                return null;
            }
            RandomAccessReader reader = new ByteArrayReader(bytes);
            double d = reader.getDouble64(4);
            return Double.toString(d);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public String getPrintScaleDescription() {
        try {
            byte[] bytes = ((PhotoshopDirectory) this._directory).getByteArray(1062);
            if (bytes == null) {
                return null;
            }
            RandomAccessReader reader = new ByteArrayReader(bytes);
            int style = reader.getInt32(0);
            float locX = reader.getFloat32(2);
            float locY = reader.getFloat32(6);
            float scale = reader.getFloat32(10);
            switch (style) {
                case 0:
                    return "Centered, Scale " + scale;
                case 1:
                    return "Size to fit";
                case 2:
                    return String.format("User defined, X:%s Y:%s, Scale:%s", Float.valueOf(locX), Float.valueOf(locY), Float.valueOf(scale));
                default:
                    return String.format("Unknown %04X, X:%s Y:%s, Scale:%s", Integer.valueOf(style), Float.valueOf(locX), Float.valueOf(locY), Float.valueOf(scale));
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public String getResolutionInfoDescription() {
        try {
            byte[] bytes = ((PhotoshopDirectory) this._directory).getByteArray(1005);
            if (bytes == null) {
                return null;
            }
            RandomAccessReader reader = new ByteArrayReader(bytes);
            float resX = reader.getS15Fixed16(0);
            float resY = reader.getS15Fixed16(8);
            DecimalFormat format = new DecimalFormat("0.##");
            return format.format(resX) + "x" + format.format(resY) + " DPI";
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public String getVersionDescription() {
        try {
            byte[] bytes = ((PhotoshopDirectory) this._directory).getByteArray(1057);
            if (bytes == null) {
                return null;
            }
            RandomAccessReader reader = new ByteArrayReader(bytes);
            int ver = reader.getInt32(0);
            int readerLength = reader.getInt32(5);
            String readerStr = reader.getString(9, readerLength * 2, "UTF-16");
            int pos = 0 + 4 + 1 + 4 + (readerLength * 2);
            int writerLength = reader.getInt32(pos);
            int pos2 = pos + 4;
            String writerStr = reader.getString(pos2, writerLength * 2, "UTF-16");
            int fileVersion = reader.getInt32(pos2 + (writerLength * 2));
            return String.format("%d (%s, %s) %d", Integer.valueOf(ver), readerStr, writerStr, Integer.valueOf(fileVersion));
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    public String getSlicesDescription() {
        try {
            byte[] bytes = ((PhotoshopDirectory) this._directory).getByteArray(1050);
            if (bytes == null) {
                return null;
            }
            RandomAccessReader reader = new ByteArrayReader(bytes);
            int nameLength = reader.getInt32(20);
            String name = reader.getString(24, nameLength * 2, "UTF-16");
            int pos = 24 + (nameLength * 2);
            int sliceCount = reader.getInt32(pos);
            return String.format("%s (%d,%d,%d,%d) %d Slices", name, Integer.valueOf(reader.getInt32(4)), Integer.valueOf(reader.getInt32(8)), Integer.valueOf(reader.getInt32(12)), Integer.valueOf(reader.getInt32(16)), Integer.valueOf(sliceCount));
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    public String getThumbnailDescription(int tagType) {
        try {
            byte[] v = ((PhotoshopDirectory) this._directory).getByteArray(tagType);
            if (v == null) {
                return null;
            }
            RandomAccessReader reader = new ByteArrayReader(v);
            int format = reader.getInt32(0);
            int width = reader.getInt32(4);
            int height = reader.getInt32(8);
            int totalSize = reader.getInt32(16);
            int compSize = reader.getInt32(20);
            int bpp = reader.getInt32(24);
            Object[] objArr = new Object[6];
            objArr[0] = format == 1 ? "JpegRGB" : "RawRGB";
            objArr[1] = Integer.valueOf(width);
            objArr[2] = Integer.valueOf(height);
            objArr[3] = Integer.valueOf(totalSize);
            objArr[4] = Integer.valueOf(bpp);
            objArr[5] = Integer.valueOf(compSize);
            return String.format("%s, %dx%d, Decomp %d bytes, %d bpp, %d bytes", objArr);
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    private String getBooleanString(int tag) {
        byte[] bytes = ((PhotoshopDirectory) this._directory).getByteArray(tag);
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return bytes[0] == 0 ? "No" : "Yes";
    }

    @Nullable
    private String get32BitNumberString(int tag) {
        byte[] bytes = ((PhotoshopDirectory) this._directory).getByteArray(tag);
        if (bytes == null) {
            return null;
        }
        RandomAccessReader reader = new ByteArrayReader(bytes);
        try {
            return String.format("%d", Integer.valueOf(reader.getInt32(0)));
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    private String getSimpleString(int tagType) {
        byte[] bytes = ((PhotoshopDirectory) this._directory).getByteArray(tagType);
        if (bytes == null) {
            return null;
        }
        return new String(bytes);
    }

    @Nullable
    private String getBinaryDataString(int tagType) {
        byte[] bytes = ((PhotoshopDirectory) this._directory).getByteArray(tagType);
        if (bytes == null) {
            return null;
        }
        return String.format("%d bytes binary data", Integer.valueOf(bytes.length));
    }
}
