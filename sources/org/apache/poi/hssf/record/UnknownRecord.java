package org.apache.poi.hssf.record;

import com.drew.metadata.exif.makernotes.OlympusImageProcessingMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusMakernoteDirectory;
import java.util.Locale;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.hssf.record.chart.AxisOptionsRecord;
import org.apache.poi.hssf.record.chart.AxisParentRecord;
import org.apache.poi.hssf.record.chart.AxisUsedRecord;
import org.apache.poi.hssf.record.chart.DatRecord;
import org.apache.poi.hssf.record.chart.FontBasisRecord;
import org.apache.poi.hssf.record.chart.FrameRecord;
import org.apache.poi.hssf.record.chart.LinkedDataRecord;
import org.apache.poi.hssf.record.chart.NumberFormatIndexRecord;
import org.apache.poi.hssf.record.chart.PlotGrowthRecord;
import org.apache.poi.hssf.record.chart.SeriesIndexRecord;
import org.apache.poi.hssf.record.chart.SheetPropertiesRecord;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.aspectj.apache.bcel.Constants;
import org.springframework.beans.PropertyAccessor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/UnknownRecord.class */
public final class UnknownRecord extends StandardRecord {
    public static final int PRINTSIZE_0033 = 51;
    public static final int PLS_004D = 77;
    public static final int SHEETPR_0081 = 129;
    public static final int SORT_0090 = 144;
    public static final int STANDARDWIDTH_0099 = 153;
    public static final int SCL_00A0 = 160;
    public static final int BITMAP_00E9 = 233;
    public static final int PHONETICPR_00EF = 239;
    public static final int LABELRANGES_015F = 351;
    public static final int QUICKTIP_0800 = 2048;
    public static final int SHEETEXT_0862 = 2146;
    public static final int SHEETPROTECTION_0867 = 2151;
    public static final int HEADER_FOOTER_089C = 2204;
    public static final int CODENAME_1BA = 442;
    public static final int PLV_MAC = 2248;
    private int _sid;
    private byte[] _rawData;

    public UnknownRecord(int id, byte[] data) {
        this._sid = id & 65535;
        this._rawData = data;
    }

    public UnknownRecord(RecordInputStream in) {
        this._sid = in.getSid();
        this._rawData = in.readRemainder();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.write(this._rawData);
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return this._rawData.length;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        String biffName = getBiffName(this._sid);
        if (biffName == null) {
            biffName = "UNKNOWNRECORD";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(biffName).append("] (0x");
        sb.append(Integer.toHexString(this._sid).toUpperCase(Locale.ROOT) + ")\n");
        if (this._rawData.length > 0) {
            sb.append("  rawData=").append(HexDump.toHex(this._rawData)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        sb.append("[/").append(biffName).append("]\n");
        return sb.toString();
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) this._sid;
    }

    public static String getBiffName(int sid) {
        switch (sid) {
            case 51:
                return "PRINTSIZE";
            case 77:
                return "PLS";
            case 80:
                return "DCON";
            case 127:
                return "IMDATA";
            case 129:
                return "SHEETPR";
            case 144:
                return "SORT";
            case 148:
                return "LHRECORD";
            case 153:
                return "STANDARDWIDTH";
            case 160:
                return "SCL";
            case 174:
                return "SCENMAN";
            case 178:
                return "SXVI";
            case 180:
                return "SXIVD";
            case 181:
                return "SXLI";
            case 211:
                return "OBPROJ";
            case 220:
                return "PARAMQRY";
            case Constants.ANEWARRAY_QUICK /* 222 */:
                return "OLESIZE";
            case BITMAP_00E9 /* 233 */:
                return "BITMAP";
            case PHONETICPR_00EF /* 239 */:
                return "PHONETICPR";
            case EscherProperties.GEOTEXT__HASTEXTEFFECT /* 241 */:
                return "SXEX";
            case 351:
                return "LABELRANGES";
            case 425:
                return "USERBVIEW";
            case 429:
                return "QSI";
            case CODENAME_1BA /* 442 */:
                return "CODENAME";
            case EscherProperties.LINESTYLE__COLOR /* 448 */:
                return "EXCEL9FILE";
            case 2048:
                return "QUICKTIP";
            case 2050:
                return "QSISXTAG";
            case 2051:
                return "DBQUERYEXT";
            case OlympusImageProcessingMakernoteDirectory.TagSensorCalibration /* 2053 */:
                return "TXTQUERY";
            case 2064:
                return "SXVIEWEX9";
            case 2066:
                return "CONTINUEFRT";
            case SHEETEXT_0862 /* 2146 */:
                return "SHEETEXT";
            case 2147:
                return "BOOKEXT";
            case 2148:
                return "SXADDL";
            case 2151:
                return "SHEETPROTECTION";
            case 2155:
                return "DATALABEXTCONTENTS";
            case 2156:
                return "CELLWATCH";
            case FeatRecord.v11_sid /* 2162 */:
                return "SHARED FEATURE v11";
            case 2164:
                return "DROPDOWNOBJIDS";
            case 2166:
                return "DCONN";
            case FeatRecord.v12_sid /* 2168 */:
                return "SHARED FEATURE v12";
            case 2171:
                return "CFEX";
            case 2172:
                return "XFCRC";
            case 2173:
                return "XFEXT";
            case 2175:
                return "CONTINUEFRT12";
            case 2187:
                return "PLV";
            case 2188:
                return "COMPAT12";
            case 2189:
                return "DXF";
            case 2194:
                return "STYLEEXT";
            case 2198:
                return "THEME";
            case 2199:
                return "GUIDTYPELIB";
            case 2202:
                return "MTRSETTINGS";
            case 2203:
                return "COMPRESSPICTURES";
            case 2204:
                return "HEADERFOOTER";
            case 2205:
                return "CRTLAYOUT12";
            case 2206:
                return "CRTMLFRT";
            case 2207:
                return "CRTMLFRTCONTINUE";
            case 2209:
                return "SHAPEPROPSSTREAM";
            case 2211:
                return "FORCEFULLCALCULATION";
            case 2212:
                return "SHAPEPROPSSTREAM";
            case 2213:
                return "TEXTPROPSSTREAM";
            case 2214:
                return "RICHTEXTSTREAM";
            case 2215:
                return "CRTLAYOUT12A";
            case PLV_MAC /* 2248 */:
                return "PLV{Mac Excel}";
            case 4097:
                return "UNITS";
            case 4102:
                return "CHARTDATAFORMAT";
            case 4103:
                return "CHARTLINEFORMAT";
            default:
                if (isObservedButUnknown(sid)) {
                    return "UNKNOWN-" + Integer.toHexString(sid).toUpperCase(Locale.ROOT);
                }
                return null;
        }
    }

    private static boolean isObservedButUnknown(int sid) {
        switch (sid) {
            case 51:
            case 52:
            case EscherProperties.FILL__SHAPE /* 445 */:
            case EscherProperties.LINESTYLE__BACKCOLOR /* 450 */:
            case OlympusMakernoteDirectory.TAG_LIGHT_CONDITION /* 4105 */:
            case 4106:
            case 4107:
            case 4108:
            case 4116:
            case 4119:
            case OlympusMakernoteDirectory.TAG_BLUE_BALANCE /* 4120 */:
            case OlympusMakernoteDirectory.TAG_COLOR_MATRIX_NUMBER /* 4121 */:
            case 4122:
            case OlympusMakernoteDirectory.TAG_EXTERNAL_FLASH_AE1_0 /* 4123 */:
            case 4125:
            case 4126:
            case 4127:
            case 4128:
            case 4129:
            case OlympusMakernoteDirectory.TAG_INTERNAL_FLASH_AE2 /* 4130 */:
            case 4132:
            case 4133:
            case 4134:
            case 4135:
            case FrameRecord.sid /* 4146 */:
            case 4147:
            case 4148:
            case 4149:
            case OlympusMakernoteDirectory.TAG_NOISE_REDUCTION /* 4154 */:
            case AxisParentRecord.sid /* 4161 */:
            case 4163:
            case SheetPropertiesRecord.sid /* 4164 */:
            case 4165:
            case AxisUsedRecord.sid /* 4166 */:
            case 4170:
            case 4171:
            case NumberFormatIndexRecord.sid /* 4174 */:
            case 4175:
            case LinkedDataRecord.sid /* 4177 */:
            case 4188:
            case 4189:
            case 4191:
            case FontBasisRecord.sid /* 4192 */:
            case AxisOptionsRecord.sid /* 4194 */:
            case DatRecord.sid /* 4195 */:
            case PlotGrowthRecord.sid /* 4196 */:
            case SeriesIndexRecord.sid /* 4197 */:
            case 4198:
                return true;
            default:
                return false;
        }
    }

    @Override // org.apache.poi.hssf.record.Record
    public Object clone() {
        return this;
    }
}
