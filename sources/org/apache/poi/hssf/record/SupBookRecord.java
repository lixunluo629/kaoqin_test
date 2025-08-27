package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.RecordFormatException;
import org.apache.poi.util.StringUtil;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/SupBookRecord.class */
public final class SupBookRecord extends StandardRecord {
    public static final short sid = 430;
    private static final short SMALL_RECORD_SIZE = 4;
    private static final short TAG_INTERNAL_REFERENCES = 1025;
    private static final short TAG_ADD_IN_FUNCTIONS = 14849;
    private short field_1_number_of_sheets;
    private String field_2_encoded_url;
    private String[] field_3_sheet_names;
    private boolean _isAddInFunctions;
    protected static final char CH_VOLUME = 1;
    protected static final char CH_SAME_VOLUME = 2;
    protected static final char CH_DOWN_DIR = 3;
    protected static final char CH_UP_DIR = 4;
    protected static final char CH_LONG_VOLUME = 5;
    protected static final char CH_STARTUP_DIR = 6;
    protected static final char CH_ALT_STARTUP_DIR = 7;
    protected static final char CH_LIB_DIR = '\b';
    private static final POILogger logger = POILogFactory.getLogger((Class<?>) SupBookRecord.class);
    protected static final String PATH_SEPERATOR = System.getProperty("file.separator");

    public static SupBookRecord createInternalReferences(short numberOfSheets) {
        return new SupBookRecord(false, numberOfSheets);
    }

    public static SupBookRecord createAddInFunctions() {
        return new SupBookRecord(true, (short) 1);
    }

    public static SupBookRecord createExternalReferences(String url, String[] sheetNames) {
        return new SupBookRecord(url, sheetNames);
    }

    private SupBookRecord(boolean isAddInFuncs, short numberOfSheets) {
        this.field_1_number_of_sheets = numberOfSheets;
        this.field_2_encoded_url = null;
        this.field_3_sheet_names = null;
        this._isAddInFunctions = isAddInFuncs;
    }

    public SupBookRecord(String url, String[] sheetNames) {
        this.field_1_number_of_sheets = (short) sheetNames.length;
        this.field_2_encoded_url = url;
        this.field_3_sheet_names = sheetNames;
        this._isAddInFunctions = false;
    }

    public boolean isExternalReferences() {
        return this.field_3_sheet_names != null;
    }

    public boolean isInternalReferences() {
        return this.field_3_sheet_names == null && !this._isAddInFunctions;
    }

    public boolean isAddInFunctions() {
        return this.field_3_sheet_names == null && this._isAddInFunctions;
    }

    public SupBookRecord(RecordInputStream in) throws RecordFormatException {
        int recLen = in.remaining();
        this.field_1_number_of_sheets = in.readShort();
        if (recLen > 4) {
            this._isAddInFunctions = false;
            this.field_2_encoded_url = in.readString();
            String[] sheetNames = new String[this.field_1_number_of_sheets];
            for (int i = 0; i < sheetNames.length; i++) {
                sheetNames[i] = in.readString();
            }
            this.field_3_sheet_names = sheetNames;
            return;
        }
        this.field_2_encoded_url = null;
        this.field_3_sheet_names = null;
        short nextShort = in.readShort();
        if (nextShort == 1025) {
            this._isAddInFunctions = false;
        } else {
            if (nextShort == TAG_ADD_IN_FUNCTIONS) {
                this._isAddInFunctions = true;
                if (this.field_1_number_of_sheets != 1) {
                    throw new RuntimeException("Expected 0x0001 for number of sheets field in 'Add-In Functions' but got (" + ((int) this.field_1_number_of_sheets) + ")");
                }
                return;
            }
            throw new RuntimeException("invalid EXTERNALBOOK code (" + Integer.toHexString(nextShort) + ")");
        }
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[SUPBOOK ");
        if (isExternalReferences()) {
            sb.append("External References]\n");
            sb.append(" .url     = ").append(this.field_2_encoded_url).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            sb.append(" .nSheets = ").append((int) this.field_1_number_of_sheets).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            String[] arr$ = this.field_3_sheet_names;
            for (String sheetname : arr$) {
                sb.append("    .name = ").append(sheetname).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            }
            sb.append("[/SUPBOOK");
        } else if (this._isAddInFunctions) {
            sb.append("Add-In Functions");
        } else {
            sb.append("Internal References");
            sb.append(" nSheets=").append((int) this.field_1_number_of_sheets);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        if (!isExternalReferences()) {
            return 4;
        }
        int sum = 2 + StringUtil.getEncodedSize(this.field_2_encoded_url);
        for (int i = 0; i < this.field_3_sheet_names.length; i++) {
            sum += StringUtil.getEncodedSize(this.field_3_sheet_names[i]);
        }
        return sum;
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_number_of_sheets);
        if (isExternalReferences()) {
            StringUtil.writeUnicodeString(out, this.field_2_encoded_url);
            for (int i = 0; i < this.field_3_sheet_names.length; i++) {
                StringUtil.writeUnicodeString(out, this.field_3_sheet_names[i]);
            }
            return;
        }
        int field2val = this._isAddInFunctions ? TAG_ADD_IN_FUNCTIONS : 1025;
        out.writeShort(field2val);
    }

    public void setNumberOfSheets(short number) {
        this.field_1_number_of_sheets = number;
    }

    public short getNumberOfSheets() {
        return this.field_1_number_of_sheets;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 430;
    }

    public String getURL() {
        String encodedUrl = this.field_2_encoded_url;
        switch (encodedUrl.charAt(0)) {
            case 0:
                return encodedUrl.substring(1);
            case 1:
                return decodeFileName(encodedUrl);
            case 2:
                return encodedUrl.substring(1);
            default:
                return encodedUrl;
        }
    }

    private static String decodeFileName(String encodedUrl) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        while (i < encodedUrl.length()) {
            char c = encodedUrl.charAt(i);
            switch (c) {
                case 1:
                    i++;
                    char driveLetter = encodedUrl.charAt(i);
                    if (driveLetter == '@') {
                        sb.append("\\\\");
                        break;
                    } else {
                        sb.append(driveLetter).append(":");
                        break;
                    }
                case 2:
                    sb.append(PATH_SEPERATOR);
                    break;
                case 3:
                    sb.append(PATH_SEPERATOR);
                    break;
                case 4:
                    sb.append("..").append(PATH_SEPERATOR);
                    break;
                case 5:
                    logger.log(5, "Found unexpected key: ChLongVolume - IGNORING");
                    break;
                case 6:
                case 7:
                case '\b':
                    logger.log(5, "EXCEL.EXE path unkown - using this directoy instead: .");
                    sb.append(".").append(PATH_SEPERATOR);
                    break;
                default:
                    sb.append(c);
                    break;
            }
            i++;
        }
        return sb.toString();
    }

    public String[] getSheetNames() {
        return (String[]) this.field_3_sheet_names.clone();
    }

    public void setURL(String pUrl) {
        this.field_2_encoded_url = this.field_2_encoded_url.substring(0, 1) + pUrl;
    }
}
