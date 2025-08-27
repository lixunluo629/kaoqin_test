package org.apache.poi.hssf.record;

import java.util.Iterator;
import org.apache.poi.hssf.record.common.UnicodeString;
import org.apache.poi.hssf.record.cont.ContinuableRecord;
import org.apache.poi.hssf.record.cont.ContinuableRecordOutput;
import org.apache.poi.util.IntMapper;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/SSTRecord.class */
public final class SSTRecord extends ContinuableRecord {
    public static final short sid = 252;
    private static final UnicodeString EMPTY_STRING = new UnicodeString("");
    static final int STD_RECORD_OVERHEAD = 4;
    static final int SST_RECORD_OVERHEAD = 12;
    static final int MAX_DATA_SPACE = 8216;
    private int field_1_num_strings;
    private int field_2_num_unique_strings;
    private IntMapper<UnicodeString> field_3_strings;
    private SSTDeserializer deserializer;
    int[] bucketAbsoluteOffsets;
    int[] bucketRelativeOffsets;

    public SSTRecord() {
        this.field_1_num_strings = 0;
        this.field_2_num_unique_strings = 0;
        this.field_3_strings = new IntMapper<>();
        this.deserializer = new SSTDeserializer(this.field_3_strings);
    }

    public int addString(UnicodeString string) {
        int rval;
        this.field_1_num_strings++;
        UnicodeString ucs = string == null ? EMPTY_STRING : string;
        int index = this.field_3_strings.getIndex(ucs);
        if (index != -1) {
            rval = index;
        } else {
            rval = this.field_3_strings.size();
            this.field_2_num_unique_strings++;
            SSTDeserializer.addToStringTable(this.field_3_strings, ucs);
        }
        return rval;
    }

    public int getNumStrings() {
        return this.field_1_num_strings;
    }

    public int getNumUniqueStrings() {
        return this.field_2_num_unique_strings;
    }

    public UnicodeString getString(int id) {
        return this.field_3_strings.get(id);
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[SST]\n");
        buffer.append("    .numstrings     = ").append(Integer.toHexString(getNumStrings())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    .uniquestrings  = ").append(Integer.toHexString(getNumUniqueStrings())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        for (int k = 0; k < this.field_3_strings.size(); k++) {
            UnicodeString s = this.field_3_strings.get(k);
            buffer.append("    .string_" + k + "      = ").append(s.getDebugInfo()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        buffer.append("[/SST]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 252;
    }

    public SSTRecord(RecordInputStream in) {
        this.field_1_num_strings = in.readInt();
        this.field_2_num_unique_strings = in.readInt();
        this.field_3_strings = new IntMapper<>();
        this.deserializer = new SSTDeserializer(this.field_3_strings);
        if (this.field_1_num_strings == 0) {
            this.field_2_num_unique_strings = 0;
        } else {
            this.deserializer.manufactureStrings(this.field_2_num_unique_strings, in);
        }
    }

    Iterator<UnicodeString> getStrings() {
        return this.field_3_strings.iterator();
    }

    int countStrings() {
        return this.field_3_strings.size();
    }

    @Override // org.apache.poi.hssf.record.cont.ContinuableRecord
    protected void serialize(ContinuableRecordOutput out) {
        SSTSerializer serializer = new SSTSerializer(this.field_3_strings, getNumStrings(), getNumUniqueStrings());
        serializer.serialize(out);
        this.bucketAbsoluteOffsets = serializer.getBucketAbsoluteOffsets();
        this.bucketRelativeOffsets = serializer.getBucketRelativeOffsets();
    }

    SSTDeserializer getDeserializer() {
        return this.deserializer;
    }

    public ExtSSTRecord createExtSSTRecord(int sstOffset) {
        if (this.bucketAbsoluteOffsets == null || this.bucketRelativeOffsets == null) {
            throw new IllegalStateException("SST record has not yet been serialized.");
        }
        ExtSSTRecord extSST = new ExtSSTRecord();
        extSST.setNumStringsPerBucket((short) 8);
        int[] absoluteOffsets = (int[]) this.bucketAbsoluteOffsets.clone();
        int[] relativeOffsets = (int[]) this.bucketRelativeOffsets.clone();
        for (int i = 0; i < absoluteOffsets.length; i++) {
            int i2 = i;
            absoluteOffsets[i2] = absoluteOffsets[i2] + sstOffset;
        }
        extSST.setBucketOffsets(absoluteOffsets, relativeOffsets);
        return extSST;
    }

    public int calcExtSSTRecordSize() {
        return ExtSSTRecord.getRecordSizeForStrings(this.field_3_strings.size());
    }
}
