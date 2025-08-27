package org.apache.poi.common.usermodel.fonts;

import com.drew.metadata.exif.makernotes.OlympusCameraSettingsMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusImageProcessingMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusMakernoteDirectory;
import com.drew.metadata.exif.makernotes.SonyType1MakernoteDirectory;
import com.mysql.jdbc.MysqlErrorNumbers;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import org.apache.poi.ddf.EscherProperties;
import org.aspectj.apache.bcel.Constants;

/* loaded from: poi-3.17.jar:org/apache/poi/common/usermodel/fonts/FontGroup.class */
public enum FontGroup {
    LATIN,
    EAST_ASIAN,
    SYMBOL,
    COMPLEX_SCRIPT;

    private static NavigableMap<Integer, Range> UCS_RANGES = new TreeMap();

    static {
        UCS_RANGES.put(0, new Range(127, LATIN));
        UCS_RANGES.put(128, new Range(166, LATIN));
        UCS_RANGES.put(169, new Range(175, LATIN));
        UCS_RANGES.put(178, new Range(179, LATIN));
        UCS_RANGES.put(181, new Range(Constants.INVOKEVIRTUAL_QUICK, LATIN));
        UCS_RANGES.put(Integer.valueOf(Constants.INVOKESUPER_QUICK), new Range(EscherProperties.GEOTEXT__CHARBOUNDINGBOX, LATIN));
        UCS_RANGES.put(Integer.valueOf(EscherProperties.GEOTEXT__STRETCHCHARHEIGHT), new Range(MysqlErrorNumbers.ER_NO_DEFAULT_FOR_VIEW_FIELD, LATIN));
        UCS_RANGES.put(Integer.valueOf(MysqlErrorNumbers.ER_SP_NO_RECURSION), new Range(MysqlErrorNumbers.ER_SLAVE_MI_INIT_REPOSITORY, COMPLEX_SCRIPT));
        UCS_RANGES.put(1920, new Range(1983, COMPLEX_SCRIPT));
        UCS_RANGES.put(Integer.valueOf(OlympusCameraSettingsMakernoteDirectory.TagManometerPressure), new Range(4255, COMPLEX_SCRIPT));
        UCS_RANGES.put(4256, new Range(4351, LATIN));
        UCS_RANGES.put(Integer.valueOf(OlympusImageProcessingMakernoteDirectory.TagFacesDetected), new Range(4991, LATIN));
        UCS_RANGES.put(5024, new Range(6015, LATIN));
        UCS_RANGES.put(7424, new Range(7551, LATIN));
        UCS_RANGES.put(7680, new Range(8191, LATIN));
        UCS_RANGES.put(6016, new Range(6319, COMPLEX_SCRIPT));
        UCS_RANGES.put(8192, new Range(SonyType1MakernoteDirectory.TAG_MULTI_FRAME_NOISE_REDUCTION, LATIN));
        UCS_RANGES.put(8204, new Range(SonyType1MakernoteDirectory.TAG_SOFT_SKIN_EFFECT, COMPLEX_SCRIPT));
        UCS_RANGES.put(8208, new Range(8233, LATIN));
        UCS_RANGES.put(8234, new Range(8239, COMPLEX_SCRIPT));
        UCS_RANGES.put(Integer.valueOf(OlympusMakernoteDirectory.TAG_RAW_DEVELOPMENT), new Range(8262, LATIN));
        UCS_RANGES.put(8266, new Range(9311, LATIN));
        UCS_RANGES.put(9840, new Range(9841, COMPLEX_SCRIPT));
        UCS_RANGES.put(10176, new Range(11263, LATIN));
        UCS_RANGES.put(12441, new Range(12442, EAST_ASIAN));
        UCS_RANGES.put(55349, new Range(55349, LATIN));
        UCS_RANGES.put(61440, new Range(61695, SYMBOL));
        UCS_RANGES.put(64256, new Range(64279, LATIN));
        UCS_RANGES.put(64285, new Range(64335, COMPLEX_SCRIPT));
        UCS_RANGES.put(65104, new Range(65135, LATIN));
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/common/usermodel/fonts/FontGroup$FontGroupRange.class */
    public static class FontGroupRange {
        private int len;
        private FontGroup fontGroup;

        static /* synthetic */ int access$112(FontGroupRange x0, int x1) {
            int i = x0.len + x1;
            x0.len = i;
            return i;
        }

        public int getLength() {
            return this.len;
        }

        public FontGroup getFontGroup() {
            return this.fontGroup;
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/common/usermodel/fonts/FontGroup$Range.class */
    private static class Range {
        int upper;
        FontGroup fontGroup;

        Range(int upper, FontGroup fontGroup) {
            this.upper = upper;
            this.fontGroup = fontGroup;
        }
    }

    public static List<FontGroupRange> getFontGroupRanges(String runText) {
        FontGroup tt;
        List<FontGroupRange> ttrList = new ArrayList<>();
        FontGroupRange ttrLast = null;
        int rlen = runText != null ? runText.length() : 0;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < rlen) {
                int cp = runText.codePointAt(i2);
                int charCount = Character.charCount(cp);
                if (ttrLast != null && " \n\r".indexOf(cp) > -1) {
                    tt = ttrLast.fontGroup;
                } else {
                    tt = lookup(cp);
                }
                if (ttrLast == null || ttrLast.fontGroup != tt) {
                    ttrLast = new FontGroupRange();
                    ttrLast.fontGroup = tt;
                    ttrList.add(ttrLast);
                }
                FontGroupRange.access$112(ttrLast, charCount);
                i = i2 + charCount;
            } else {
                return ttrList;
            }
        }
    }

    public static FontGroup getFontGroupFirst(String runText) {
        return (runText == null || runText.isEmpty()) ? LATIN : lookup(runText.codePointAt(0));
    }

    private static FontGroup lookup(int codepoint) {
        Map.Entry<Integer, Range> entry = UCS_RANGES.floorEntry(Integer.valueOf(codepoint));
        Range range = entry != null ? entry.getValue() : null;
        return (range == null || codepoint > range.upper) ? EAST_ASIAN : range.fontGroup;
    }
}
