package org.apache.poi.ss.formula;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/FormulaCellCacheEntrySet.class */
final class FormulaCellCacheEntrySet {
    private static final FormulaCellCacheEntry[] EMPTY_ARRAY = new FormulaCellCacheEntry[0];
    private int _size;
    private FormulaCellCacheEntry[] _arr = EMPTY_ARRAY;

    public FormulaCellCacheEntry[] toArray() {
        int nItems = this._size;
        if (nItems < 1) {
            return EMPTY_ARRAY;
        }
        FormulaCellCacheEntry[] result = new FormulaCellCacheEntry[nItems];
        int j = 0;
        for (int i = 0; i < this._arr.length; i++) {
            FormulaCellCacheEntry cce = this._arr[i];
            if (cce != null) {
                int i2 = j;
                j++;
                result[i2] = cce;
            }
        }
        if (j != nItems) {
            throw new IllegalStateException("size mismatch");
        }
        return result;
    }

    public void add(CellCacheEntry cce) {
        if (this._size * 3 >= this._arr.length * 2) {
            FormulaCellCacheEntry[] prevArr = this._arr;
            FormulaCellCacheEntry[] newArr = new FormulaCellCacheEntry[4 + ((this._arr.length * 3) / 2)];
            for (int i = 0; i < prevArr.length; i++) {
                FormulaCellCacheEntry prevCce = this._arr[i];
                if (prevCce != null) {
                    addInternal(newArr, prevCce);
                }
            }
            this._arr = newArr;
        }
        if (addInternal(this._arr, cce)) {
            this._size++;
        }
    }

    private static boolean addInternal(CellCacheEntry[] arr, CellCacheEntry cce) {
        int startIx = Math.abs(cce.hashCode() % arr.length);
        for (int i = startIx; i < arr.length; i++) {
            CellCacheEntry item = arr[i];
            if (item == cce) {
                return false;
            }
            if (item == null) {
                arr[i] = cce;
                return true;
            }
        }
        for (int i2 = 0; i2 < startIx; i2++) {
            CellCacheEntry item2 = arr[i2];
            if (item2 == cce) {
                return false;
            }
            if (item2 == null) {
                arr[i2] = cce;
                return true;
            }
        }
        throw new IllegalStateException("No empty space found");
    }

    public boolean remove(CellCacheEntry cce) {
        FormulaCellCacheEntry[] arr = this._arr;
        if (this._size * 3 < this._arr.length && this._arr.length > 8) {
            boolean found = false;
            FormulaCellCacheEntry[] prevArr = this._arr;
            FormulaCellCacheEntry[] newArr = new FormulaCellCacheEntry[this._arr.length / 2];
            for (int i = 0; i < prevArr.length; i++) {
                FormulaCellCacheEntry prevCce = this._arr[i];
                if (prevCce != null) {
                    if (prevCce == cce) {
                        found = true;
                        this._size--;
                    } else {
                        addInternal(newArr, prevCce);
                    }
                }
            }
            this._arr = newArr;
            return found;
        }
        int startIx = Math.abs(cce.hashCode() % arr.length);
        for (int i2 = startIx; i2 < arr.length; i2++) {
            FormulaCellCacheEntry item = arr[i2];
            if (item == cce) {
                arr[i2] = null;
                this._size--;
                return true;
            }
        }
        for (int i3 = 0; i3 < startIx; i3++) {
            FormulaCellCacheEntry item2 = arr[i3];
            if (item2 == cce) {
                arr[i3] = null;
                this._size--;
                return true;
            }
        }
        return false;
    }
}
