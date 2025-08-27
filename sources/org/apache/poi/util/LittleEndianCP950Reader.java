package org.apache.poi.util;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.hssf.record.DrawingGroupRecord;
import org.apache.poi.hssf.record.DrawingSelectionRecord;
import org.apache.poi.hssf.record.MergeCellsRecord;
import org.apache.poi.hssf.record.UnknownRecord;
import org.aspectj.apache.bcel.Constants;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/util/LittleEndianCP950Reader.class */
public class LittleEndianCP950Reader extends Reader {
    private static final POILogger LOGGER = POILogFactory.getLogger((Class<?>) LittleEndianCP950Reader.class);
    private static final char UNMAPPABLE = '?';
    private final ByteBuffer doubleByteBuffer;
    private final CharBuffer charBuffer;
    private final CharsetDecoder decoder;
    private static final char range1Low = 33088;
    private static final char range1High = 36350;
    private static final char range2Low = 36416;
    private static final char range2High = 41214;
    private static final char range3Low = 50849;
    private static final char range3High = 51454;
    private static final char range4Low = 64064;
    private static final char range4High = 65278;
    private final byte[] data;
    private final int startOffset;
    private final int length;
    private int offset;
    private int trailing;
    private int leading;
    int cnt;

    public LittleEndianCP950Reader(byte[] data) {
        this(data, 0, data.length);
    }

    public LittleEndianCP950Reader(byte[] data, int offset, int length) {
        this.doubleByteBuffer = ByteBuffer.allocate(2);
        this.charBuffer = CharBuffer.allocate(2);
        this.decoder = StringUtil.BIG5.newDecoder();
        this.cnt = 0;
        this.data = data;
        this.startOffset = offset;
        this.offset = this.startOffset;
        this.length = length;
    }

    @Override // java.io.Reader
    public int read() {
        if (this.offset + 1 > this.data.length || this.offset - this.startOffset > this.length) {
            return -1;
        }
        byte[] bArr = this.data;
        int i = this.offset;
        this.offset = i + 1;
        this.trailing = bArr[i] & 255;
        byte[] bArr2 = this.data;
        int i2 = this.offset;
        this.offset = i2 + 1;
        this.leading = bArr2[i2] & 255;
        this.decoder.reset();
        if (this.leading < 129) {
            return this.trailing;
        }
        if (this.leading == 249) {
            return handleF9(this.trailing);
        }
        int ch2 = (this.leading << 8) + this.trailing;
        if (ch2 >= range1Low && ch2 <= range1High) {
            return handleRange1(this.leading, this.trailing);
        }
        if (ch2 >= range2Low && ch2 <= range2High) {
            return handleRange2(this.leading, this.trailing);
        }
        if (ch2 >= range3Low && ch2 <= range3High) {
            return handleRange3(this.leading, this.trailing);
        }
        if (ch2 >= range4Low && ch2 <= range4High) {
            return handleRange4(this.leading, this.trailing);
        }
        this.charBuffer.clear();
        this.doubleByteBuffer.clear();
        this.doubleByteBuffer.put((byte) this.leading);
        this.doubleByteBuffer.put((byte) this.trailing);
        this.doubleByteBuffer.flip();
        this.decoder.decode(this.doubleByteBuffer, this.charBuffer, true);
        this.charBuffer.flip();
        if (this.charBuffer.length() == 0) {
            LOGGER.log(5, "couldn't create char for: " + Integer.toString(this.leading & 255, 16) + SymbolConstants.SPACE_SYMBOL + Integer.toString(this.trailing & 255, 16));
            return 63;
        }
        return Character.codePointAt(this.charBuffer, 0);
    }

    @Override // java.io.Reader
    public int read(char[] cbuf, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) {
            int c = read();
            if (c == -1) {
                return i - off;
            }
            cbuf[i] = (char) c;
        }
        return len;
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    private int handleRange1(int leading, int trailing) {
        return 61112 + (157 * (leading - 129)) + (trailing < 128 ? trailing - 64 : trailing - 98);
    }

    private int handleRange2(int leading, int trailing) {
        return 58129 + (157 * (leading - 142)) + (trailing < 128 ? trailing - 64 : trailing - 98);
    }

    private int handleRange3(int leading, int trailing) {
        return 63090 + (157 * (leading - 198)) + (trailing < 128 ? trailing - 64 : trailing - 98);
    }

    private int handleRange4(int leading, int trailing) {
        return 57344 + (157 * (leading - EscherProperties.GEOTEXT__BOLDFONT)) + (trailing < 128 ? trailing - 64 : trailing - 98);
    }

    private int handleF9(int trailing) {
        switch (trailing) {
            case 64:
                return 32408;
            case 65:
                return 32411;
            case 66:
                return 32409;
            case 67:
                return 33248;
            case 68:
                return 33249;
            case 69:
                return 34374;
            case 70:
                return 34375;
            case 71:
                return 34376;
            case 72:
                return 35193;
            case 73:
                return 35194;
            case 74:
                return 35196;
            case 75:
                return 35195;
            case 76:
                return 35327;
            case 77:
                return 35736;
            case 78:
                return 35737;
            case 79:
                return 36517;
            case 80:
                return 36516;
            case 81:
                return 36515;
            case 82:
                return 37998;
            case 83:
                return 37997;
            case 84:
                return 37999;
            case 85:
                return 38001;
            case 86:
                return 38003;
            case 87:
                return 38729;
            case 88:
                return 39026;
            case 89:
                return 39263;
            case 90:
                return 40040;
            case 91:
                return 40046;
            case 92:
                return 40045;
            case 93:
                return 40459;
            case 94:
                return 40461;
            case 95:
                return 40464;
            case 96:
                return 40463;
            case 97:
                return 40466;
            case 98:
                return 40465;
            case 99:
                return 40609;
            case 100:
                return 40693;
            case 101:
                return 40713;
            case 102:
                return 40775;
            case 103:
                return 40824;
            case 104:
                return 40827;
            case 105:
                return 40826;
            case 106:
                return 40825;
            case 107:
                return 22302;
            case 108:
                return 28774;
            case 109:
                return 31855;
            case 110:
                return 34876;
            case 111:
                return 36274;
            case 112:
                return 36518;
            case 113:
                return 37315;
            case 114:
                return 38004;
            case 115:
                return 38008;
            case 116:
                return 38006;
            case 117:
                return 38005;
            case 118:
                return 39520;
            case 119:
                return 40052;
            case 120:
                return 40051;
            case 121:
                return 40049;
            case 122:
                return 40053;
            case 123:
                return 40468;
            case 124:
                return 40467;
            case 125:
                return 40694;
            case 126:
                return 40714;
            case 127:
            case 128:
            case 129:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 145:
            case 146:
            case 147:
            case 148:
            case 149:
            case 150:
            case 151:
            case 152:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case 160:
            default:
                LOGGER.log(5, "couldn't create char for: f9 " + Integer.toString(trailing & 255, 16));
                return 63;
            case 161:
                return 40868;
            case 162:
                return 28776;
            case 163:
                return 28773;
            case 164:
                return 31991;
            case 165:
                return 34410;
            case 166:
                return 34878;
            case 167:
                return 34877;
            case 168:
                return 34879;
            case 169:
                return 35742;
            case 170:
                return 35996;
            case 171:
                return 36521;
            case 172:
                return 36553;
            case 173:
                return 38731;
            case 174:
                return 39027;
            case 175:
                return 39028;
            case 176:
                return 39116;
            case 177:
                return 39265;
            case 178:
                return 39339;
            case 179:
                return 39524;
            case 180:
                return 39526;
            case 181:
                return 39527;
            case 182:
                return 39716;
            case 183:
                return 40469;
            case 184:
                return 40471;
            case 185:
                return 40776;
            case 186:
                return 25095;
            case 187:
                return 27422;
            case 188:
                return 29223;
            case 189:
                return 34380;
            case 190:
                return 36520;
            case 191:
                return 38018;
            case 192:
                return 38016;
            case 193:
                return 38017;
            case 194:
                return 39529;
            case 195:
                return 39528;
            case 196:
                return 39726;
            case 197:
                return 40473;
            case 198:
                return 29225;
            case 199:
                return 34379;
            case 200:
                return 35743;
            case 201:
                return 38019;
            case 202:
                return 40057;
            case 203:
                return 40631;
            case 204:
                return 30325;
            case 205:
                return 39531;
            case 206:
                return 40058;
            case 207:
                return 40477;
            case 208:
                return 28777;
            case Constants.PUTFIELD2_QUICK /* 209 */:
                return 28778;
            case Constants.GETSTATIC_QUICK /* 210 */:
                return 40612;
            case 211:
                return 40830;
            case Constants.GETSTATIC2_QUICK /* 212 */:
                return 40777;
            case 213:
                return 40856;
            case Constants.INVOKEVIRTUAL_QUICK /* 214 */:
                return 30849;
            case 215:
                return 37561;
            case Constants.INVOKESUPER_QUICK /* 216 */:
                return 35023;
            case Constants.INVOKESTATIC_QUICK /* 217 */:
                return 22715;
            case 218:
                return 24658;
            case Constants.INVOKEVIRTUALOBJECT_QUICK /* 219 */:
                return 31911;
            case 220:
                return 23290;
            case 221:
                return 9556;
            case Constants.ANEWARRAY_QUICK /* 222 */:
                return 9574;
            case Constants.MULTIANEWARRAY_QUICK /* 223 */:
                return 9559;
            case 224:
                return 9568;
            case 225:
                return 9580;
            case 226:
                return 9571;
            case 227:
                return 9562;
            case Constants.PUTFIELD_QUICK_W /* 228 */:
                return 9577;
            case MergeCellsRecord.sid /* 229 */:
                return 9565;
            case 230:
                return 9554;
            case 231:
                return 9572;
            case 232:
                return 9557;
            case UnknownRecord.BITMAP_00E9 /* 233 */:
                return 9566;
            case 234:
                return 9578;
            case DrawingGroupRecord.sid /* 235 */:
                return 9569;
            case 236:
                return 9560;
            case DrawingSelectionRecord.sid /* 237 */:
                return 9575;
            case 238:
                return 9563;
            case UnknownRecord.PHONETICPR_00EF /* 239 */:
                return 9555;
            case 240:
                return 9573;
            case EscherProperties.GEOTEXT__HASTEXTEFFECT /* 241 */:
                return 9558;
            case EscherProperties.GEOTEXT__ROTATECHARACTERS /* 242 */:
                return 9567;
            case EscherProperties.GEOTEXT__KERNCHARACTERS /* 243 */:
                return 9579;
            case EscherProperties.GEOTEXT__TIGHTORTRACK /* 244 */:
                return 9570;
            case EscherProperties.GEOTEXT__STRETCHTOFITSHAPE /* 245 */:
                return 9561;
            case EscherProperties.GEOTEXT__CHARBOUNDINGBOX /* 246 */:
                return 9576;
            case 247:
                return 9564;
            case EscherProperties.GEOTEXT__STRETCHCHARHEIGHT /* 248 */:
                return 9553;
            case EscherProperties.GEOTEXT__NOMEASUREALONGPATH /* 249 */:
                return 9552;
            case EscherProperties.GEOTEXT__BOLDFONT /* 250 */:
                return 9581;
            case 251:
                return 9582;
            case 252:
                return 9584;
            case 253:
                return 9583;
            case 254:
                return 9619;
        }
    }
}
