package org.apache.poi.hpsf;

import com.drew.metadata.exif.makernotes.OlympusMakernoteDirectory;
import com.drew.metadata.exif.makernotes.SonyType1MakernoteDirectory;
import java.math.BigInteger;
import org.apache.poi.util.Internal;
import org.apache.poi.util.LittleEndianByteArrayInputStream;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/TypedPropertyValue.class */
class TypedPropertyValue {
    private static final POILogger LOG = POILogFactory.getLogger((Class<?>) TypedPropertyValue.class);
    private int _type;
    private Object _value;

    TypedPropertyValue(int type, Object value) {
        this._type = type;
        this._value = value;
    }

    Object getValue() {
        return this._value;
    }

    void read(LittleEndianByteArrayInputStream lei) {
        this._type = lei.readShort();
        short padding = lei.readShort();
        if (padding != 0) {
            LOG.log(5, "TypedPropertyValue padding at offset " + lei.getReadIndex() + " MUST be 0, but it's value is " + ((int) padding));
        }
        readValue(lei);
    }

    void readValue(LittleEndianByteArrayInputStream lei) {
        switch (this._type) {
            case 0:
            case 1:
                this._value = null;
                return;
            case 2:
                this._value = Short.valueOf(lei.readShort());
                return;
            case 3:
            case 22:
                this._value = Integer.valueOf(lei.readInt());
                return;
            case 4:
                this._value = Float.valueOf(Float.intBitsToFloat(lei.readInt()));
                return;
            case 5:
                this._value = Double.valueOf(lei.readDouble());
                return;
            case 6:
                Currency cur = new Currency();
                cur.read(lei);
                this._value = cur;
                return;
            case 7:
                Date date = new Date();
                date.read(lei);
                this._value = date;
                return;
            case 8:
            case 30:
                CodePageString cps = new CodePageString();
                cps.read(lei);
                this._value = cps;
                return;
            case 10:
            case 19:
            case 23:
                this._value = Long.valueOf(lei.readUInt());
                return;
            case 11:
                VariantBool vb = new VariantBool();
                vb.read(lei);
                this._value = vb;
                return;
            case 14:
                Decimal dec = new Decimal();
                dec.read(lei);
                this._value = dec;
                return;
            case 16:
                this._value = Byte.valueOf(lei.readByte());
                return;
            case 17:
                this._value = Integer.valueOf(lei.readUByte());
                return;
            case 18:
                this._value = Integer.valueOf(lei.readUShort());
                return;
            case 20:
                this._value = Long.valueOf(lei.readLong());
                return;
            case 21:
                byte[] biBytesLE = new byte[8];
                lei.readFully(biBytesLE);
                byte[] biBytesBE = new byte[9];
                int i = biBytesLE.length;
                for (byte b : biBytesLE) {
                    if (i <= 8) {
                        biBytesBE[i] = b;
                    }
                    i--;
                }
                this._value = new BigInteger(biBytesBE);
                return;
            case 31:
                UnicodeString us = new UnicodeString();
                us.read(lei);
                this._value = us;
                return;
            case 64:
                Filetime ft = new Filetime();
                ft.read(lei);
                this._value = ft;
                return;
            case 65:
            case 70:
                Blob blob = new Blob();
                blob.read(lei);
                this._value = blob;
                return;
            case 66:
            case 67:
            case 68:
            case 69:
                IndirectPropertyName ipn = new IndirectPropertyName();
                ipn.read(lei);
                this._value = ipn;
                return;
            case 71:
                ClipboardData cd = new ClipboardData();
                cd.read(lei);
                this._value = cd;
                return;
            case 72:
                GUID guid = new GUID();
                guid.read(lei);
                this._value = lei;
                return;
            case 73:
                VersionedStream vs = new VersionedStream();
                vs.read(lei);
                this._value = vs;
                return;
            case 4098:
            case 4099:
            case 4100:
            case 4101:
            case 4102:
            case 4103:
            case OlympusMakernoteDirectory.TAG_LENS_TEMPERATURE /* 4104 */:
            case 4106:
            case 4107:
            case 4108:
            case 4112:
            case 4113:
            case 4114:
            case 4115:
            case 4116:
            case 4117:
            case 4126:
            case 4127:
            case 4160:
            case 4167:
            case 4168:
                Vector vec = new Vector((short) (this._type & 4095));
                vec.read(lei);
                this._value = vec;
                return;
            case 8194:
            case 8195:
            case 8196:
            case SonyType1MakernoteDirectory.TAG_SATURATION /* 8197 */:
            case SonyType1MakernoteDirectory.TAG_SHARPNESS /* 8198 */:
            case SonyType1MakernoteDirectory.TAG_BRIGHTNESS /* 8199 */:
            case SonyType1MakernoteDirectory.TAG_LONG_EXPOSURE_NOISE_REDUCTION /* 8200 */:
            case SonyType1MakernoteDirectory.TAG_HDR /* 8202 */:
            case SonyType1MakernoteDirectory.TAG_MULTI_FRAME_NOISE_REDUCTION /* 8203 */:
            case 8204:
            case SonyType1MakernoteDirectory.TAG_PICTURE_EFFECT /* 8206 */:
            case 8208:
            case 8209:
            case 8210:
            case SonyType1MakernoteDirectory.TAG_DISTORTION_CORRECTION /* 8211 */:
            case SonyType1MakernoteDirectory.TAG_AUTO_PORTRAIT_FRAMED /* 8214 */:
            case 8215:
                Array arr = new Array();
                arr.read(lei);
                this._value = arr;
                return;
            default:
                String msg = "Unknown (possibly, incorrect) TypedPropertyValue type: " + this._type;
                throw new UnsupportedOperationException(msg);
        }
    }

    static void skipPadding(LittleEndianByteArrayInputStream lei) {
        int offset = lei.getReadIndex();
        int skipBytes = (4 - (offset & 3)) & 3;
        for (int i = 0; i < skipBytes; i++) {
            lei.mark(1);
            int b = lei.read();
            if (b == -1 || b != 0) {
                lei.reset();
                return;
            }
        }
    }
}
