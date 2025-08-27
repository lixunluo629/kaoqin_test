package org.apache.poi.ss.util;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellRange;
import org.apache.poi.util.Internal;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/ss/util/SSCellRange.class */
public final class SSCellRange<K extends Cell> implements CellRange<K> {
    private final int _height;
    private final int _width;
    private final K[] _flattenedArray;
    private final int _firstRow;
    private final int _firstColumn;

    private SSCellRange(int i, int i2, int i3, int i4, K[] kArr) {
        this._firstRow = i;
        this._firstColumn = i2;
        this._height = i3;
        this._width = i4;
        this._flattenedArray = (K[]) ((Cell[]) kArr.clone());
    }

    public static <B extends Cell> SSCellRange<B> create(int firstRow, int firstColumn, int height, int width, List<B> flattenedList, Class<B> cellClass) {
        int nItems = flattenedList.size();
        if (height * width != nItems) {
            throw new IllegalArgumentException("Array size mismatch.");
        }
        Cell[] cellArr = (Cell[]) Array.newInstance((Class<?>) cellClass, nItems);
        flattenedList.toArray(cellArr);
        return new SSCellRange<>(firstRow, firstColumn, height, width, cellArr);
    }

    @Override // org.apache.poi.ss.usermodel.CellRange
    public int getHeight() {
        return this._height;
    }

    @Override // org.apache.poi.ss.usermodel.CellRange
    public int getWidth() {
        return this._width;
    }

    @Override // org.apache.poi.ss.usermodel.CellRange
    public int size() {
        return this._height * this._width;
    }

    @Override // org.apache.poi.ss.usermodel.CellRange
    public String getReferenceText() {
        CellRangeAddress cra = new CellRangeAddress(this._firstRow, (this._firstRow + this._height) - 1, this._firstColumn, (this._firstColumn + this._width) - 1);
        return cra.formatAsString();
    }

    @Override // org.apache.poi.ss.usermodel.CellRange
    public K getTopLeftCell() {
        return this._flattenedArray[0];
    }

    @Override // org.apache.poi.ss.usermodel.CellRange
    public K getCell(int relativeRowIndex, int relativeColumnIndex) {
        if (relativeRowIndex < 0 || relativeRowIndex >= this._height) {
            throw new ArrayIndexOutOfBoundsException("Specified row " + relativeRowIndex + " is outside the allowable range (0.." + (this._height - 1) + ").");
        }
        if (relativeColumnIndex < 0 || relativeColumnIndex >= this._width) {
            throw new ArrayIndexOutOfBoundsException("Specified colummn " + relativeColumnIndex + " is outside the allowable range (0.." + (this._width - 1) + ").");
        }
        int flatIndex = (this._width * relativeRowIndex) + relativeColumnIndex;
        return this._flattenedArray[flatIndex];
    }

    @Override // org.apache.poi.ss.usermodel.CellRange
    public K[] getFlattenedCells() {
        return (K[]) ((Cell[]) this._flattenedArray.clone());
    }

    @Override // org.apache.poi.ss.usermodel.CellRange
    public K[][] getCells() {
        Class<?> cls = this._flattenedArray.getClass();
        K[][] kArr = (K[][]) ((Cell[][]) Array.newInstance(cls, this._height));
        Class<?> componentType = cls.getComponentType();
        for (int i = this._height - 1; i >= 0; i--) {
            System.arraycopy(this._flattenedArray, this._width * i, (Cell[]) Array.newInstance(componentType, this._width), 0, this._width);
        }
        return kArr;
    }

    @Override // org.apache.poi.ss.usermodel.CellRange, java.lang.Iterable
    public Iterator<K> iterator() {
        return new ArrayIterator(this._flattenedArray);
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/util/SSCellRange$ArrayIterator.class */
    private static final class ArrayIterator<D> implements Iterator<D> {
        private final D[] _array;
        private int _index = 0;

        public ArrayIterator(D[] dArr) {
            this._array = (D[]) ((Object[]) dArr.clone());
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this._index < this._array.length;
        }

        @Override // java.util.Iterator
        public D next() {
            if (this._index >= this._array.length) {
                throw new NoSuchElementException(String.valueOf(this._index));
            }
            D[] dArr = this._array;
            int i = this._index;
            this._index = i + 1;
            return dArr[i];
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove cells from this CellRange.");
        }
    }
}
