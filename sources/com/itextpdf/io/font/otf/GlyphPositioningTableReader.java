package com.itextpdf.io.font.otf;

import com.itextpdf.io.source.RandomAccessFileOrArray;
import java.io.IOException;
import java.util.Map;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GlyphPositioningTableReader.class */
public class GlyphPositioningTableReader extends OpenTypeFontTableReader {
    private static final long serialVersionUID = 7437245788115628787L;

    public GlyphPositioningTableReader(RandomAccessFileOrArray rf, int gposTableLocation, OpenTypeGdefTableReader gdef, Map<Integer, Glyph> indexGlyphMap, int unitsPerEm) throws IOException {
        super(rf, gposTableLocation, gdef, indexGlyphMap, unitsPerEm);
        startReadingTable();
    }

    @Override // com.itextpdf.io.font.otf.OpenTypeFontTableReader
    protected OpenTableLookup readLookupTable(int lookupType, int lookupFlag, int[] subTableLocations) throws IOException {
        if (lookupType == 9) {
            for (int k = 0; k < subTableLocations.length; k++) {
                int location = subTableLocations[k];
                this.rf.seek(location);
                this.rf.readUnsignedShort();
                lookupType = this.rf.readUnsignedShort();
                subTableLocations[k] = location + this.rf.readInt();
            }
        }
        switch (lookupType) {
            case 2:
                return new GposLookupType2(this, lookupFlag, subTableLocations);
            case 3:
            default:
                return null;
            case 4:
                return new GposLookupType4(this, lookupFlag, subTableLocations);
            case 5:
                return new GposLookupType5(this, lookupFlag, subTableLocations);
            case 6:
                return new GposLookupType6(this, lookupFlag, subTableLocations);
        }
    }
}
