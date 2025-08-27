package org.apache.poi.hssf.record;

import org.apache.poi.hssf.record.common.UnicodeString;
import org.apache.poi.util.IntMapper;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/SSTDeserializer.class */
class SSTDeserializer {
    private static POILogger logger = POILogFactory.getLogger((Class<?>) SSTDeserializer.class);
    private IntMapper<UnicodeString> strings;

    public SSTDeserializer(IntMapper<UnicodeString> strings) {
        this.strings = strings;
    }

    public void manufactureStrings(int stringCount, RecordInputStream in) {
        UnicodeString unicodeString;
        for (int i = 0; i < stringCount; i++) {
            if (in.available() == 0 && !in.hasNextRecord()) {
                logger.log(7, "Ran out of data before creating all the strings! String at index " + i + "");
                unicodeString = new UnicodeString("");
            } else {
                unicodeString = new UnicodeString(in);
            }
            UnicodeString str = unicodeString;
            addToStringTable(this.strings, str);
        }
    }

    public static void addToStringTable(IntMapper<UnicodeString> strings, UnicodeString string) {
        strings.add(string);
    }
}
