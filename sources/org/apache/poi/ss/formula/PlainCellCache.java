package org.apache.poi.ss.formula;

import java.util.HashMap;
import java.util.Map;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/PlainCellCache.class */
final class PlainCellCache {
    private Map<Loc, PlainValueCellCacheEntry> _plainValueEntriesByLoc = new HashMap();

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/PlainCellCache$Loc.class */
    public static final class Loc {
        private final long _bookSheetColumn;
        private final int _rowIndex;

        public Loc(int bookIndex, int sheetIndex, int rowIndex, int columnIndex) {
            this._bookSheetColumn = toBookSheetColumn(bookIndex, sheetIndex, columnIndex);
            this._rowIndex = rowIndex;
        }

        public static long toBookSheetColumn(int bookIndex, int sheetIndex, int columnIndex) {
            return ((bookIndex & 65535) << 48) + ((sheetIndex & 65535) << 32) + ((columnIndex & 65535) << 0);
        }

        public Loc(long bookSheetColumn, int rowIndex) {
            this._bookSheetColumn = bookSheetColumn;
            this._rowIndex = rowIndex;
        }

        public int hashCode() {
            return ((int) (this._bookSheetColumn ^ (this._bookSheetColumn >>> 32))) + (17 * this._rowIndex);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Loc)) {
                return false;
            }
            Loc other = (Loc) obj;
            return this._bookSheetColumn == other._bookSheetColumn && this._rowIndex == other._rowIndex;
        }

        public int getRowIndex() {
            return this._rowIndex;
        }

        public int getColumnIndex() {
            return (int) (this._bookSheetColumn & 65535);
        }

        public int getSheetIndex() {
            return (int) ((this._bookSheetColumn >> 32) & 65535);
        }

        public int getBookIndex() {
            return (int) ((this._bookSheetColumn >> 48) & 65535);
        }
    }

    public void put(Loc key, PlainValueCellCacheEntry cce) {
        this._plainValueEntriesByLoc.put(key, cce);
    }

    public void clear() {
        this._plainValueEntriesByLoc.clear();
    }

    public PlainValueCellCacheEntry get(Loc key) {
        return this._plainValueEntriesByLoc.get(key);
    }

    public void remove(Loc key) {
        this._plainValueEntriesByLoc.remove(key);
    }
}
