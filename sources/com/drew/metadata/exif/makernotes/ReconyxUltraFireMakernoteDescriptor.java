package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.StringValue;
import com.drew.metadata.TagDescriptor;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/exif/makernotes/ReconyxUltraFireMakernoteDescriptor.class */
public class ReconyxUltraFireMakernoteDescriptor extends TagDescriptor<ReconyxUltraFireMakernoteDirectory> {
    public ReconyxUltraFireMakernoteDescriptor(@NotNull ReconyxUltraFireMakernoteDirectory directory) {
        super(directory);
    }

    @Override // com.drew.metadata.TagDescriptor
    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 0:
                return ((ReconyxUltraFireMakernoteDirectory) this._directory).getString(tagType);
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 11:
            case 12:
            case 13:
            case 15:
            case 16:
            case 17:
            case 19:
            case 20:
            case 21:
            case 23:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 54:
            case 56:
            case 57:
            case 58:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 69:
            case 71:
            case 74:
            case 76:
            case 77:
            case 78:
            case 79:
            default:
                return super.getDescription(tagType);
            case 10:
                return String.format("0x%08X", ((ReconyxUltraFireMakernoteDirectory) this._directory).getInteger(tagType));
            case 14:
                return String.format("%d", ((ReconyxUltraFireMakernoteDirectory) this._directory).getInteger(tagType));
            case 18:
                return String.format("0x%08X", ((ReconyxUltraFireMakernoteDirectory) this._directory).getInteger(tagType));
            case 22:
                return String.format("%d", ((ReconyxUltraFireMakernoteDirectory) this._directory).getInteger(tagType));
            case 24:
            case 31:
            case 38:
            case 45:
            case 52:
                return ((ReconyxUltraFireMakernoteDirectory) this._directory).getString(tagType);
            case 53:
                int[] sequence = ((ReconyxUltraFireMakernoteDirectory) this._directory).getIntArray(tagType);
                if (sequence == null) {
                    return null;
                }
                return String.format("%d/%d", Integer.valueOf(sequence[0]), Integer.valueOf(sequence[1]));
            case 55:
                return String.format("%d", ((ReconyxUltraFireMakernoteDirectory) this._directory).getInteger(tagType));
            case 59:
                String date = ((ReconyxUltraFireMakernoteDirectory) this._directory).getString(tagType);
                try {
                    DateFormat parser = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                    return parser.format(parser.parse(date));
                } catch (ParseException e) {
                    return null;
                }
            case 67:
                return getIndexedDescription(tagType, "New", "Waxing Crescent", "First Quarter", "Waxing Gibbous", "Full", "Waning Gibbous", "Last Quarter", "Waning Crescent");
            case 68:
            case 70:
                return String.format("%d", ((ReconyxUltraFireMakernoteDirectory) this._directory).getInteger(tagType));
            case 72:
                return getIndexedDescription(tagType, "Off", "On");
            case 73:
                Double value = ((ReconyxUltraFireMakernoteDirectory) this._directory).getDoubleObject(tagType);
                DecimalFormat formatter = new DecimalFormat("0.000");
                if (value == null) {
                    return null;
                }
                return formatter.format(value);
            case 75:
                StringValue svalue = ((ReconyxUltraFireMakernoteDirectory) this._directory).getStringValue(tagType);
                if (svalue == null) {
                    return null;
                }
                return svalue.toString();
            case 80:
                return ((ReconyxUltraFireMakernoteDirectory) this._directory).getString(tagType);
        }
    }
}
