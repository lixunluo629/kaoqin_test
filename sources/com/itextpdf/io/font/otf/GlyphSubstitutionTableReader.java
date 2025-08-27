package com.itextpdf.io.font.otf;

import com.itextpdf.io.source.RandomAccessFileOrArray;
import java.io.IOException;
import java.util.Map;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GlyphSubstitutionTableReader.class */
public class GlyphSubstitutionTableReader extends OpenTypeFontTableReader {
    private static final long serialVersionUID = -6971081733980429442L;

    public GlyphSubstitutionTableReader(RandomAccessFileOrArray rf, int gsubTableLocation, OpenTypeGdefTableReader gdef, Map<Integer, Glyph> indexGlyphMap, int unitsPerEm) throws IOException {
        super(rf, gsubTableLocation, gdef, indexGlyphMap, unitsPerEm);
        startReadingTable();
    }

    @Override // com.itextpdf.io.font.otf.OpenTypeFontTableReader
    protected OpenTableLookup readLookupTable(int lookupType, int lookupFlag, int[] subTableLocations) throws IOException {
        if (lookupType == 7) {
            for (int k = 0; k < subTableLocations.length; k++) {
                int location = subTableLocations[k];
                this.rf.seek(location);
                this.rf.readUnsignedShort();
                lookupType = this.rf.readUnsignedShort();
                subTableLocations[k] = location + this.rf.readInt();
            }
        }
        switch (lookupType) {
            case 1:
                return new GsubLookupType1(this, lookupFlag, subTableLocations);
            case 2:
                return new GsubLookupType2(this, lookupFlag, subTableLocations);
            case 3:
                return new GsubLookupType3(this, lookupFlag, subTableLocations);
            case 4:
                return new GsubLookupType4(this, lookupFlag, subTableLocations);
            case 5:
                return new GsubLookupType5(this, lookupFlag, subTableLocations);
            case 6:
                return new GsubLookupType6(this, lookupFlag, subTableLocations);
            default:
                return null;
        }
    }
}
