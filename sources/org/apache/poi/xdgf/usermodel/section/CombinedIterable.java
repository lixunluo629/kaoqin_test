package org.apache.poi.xdgf.usermodel.section;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xdgf/usermodel/section/CombinedIterable.class */
public class CombinedIterable<T> implements Iterable<T> {
    final SortedMap<Long, T> _baseItems;
    final SortedMap<Long, T> _masterItems;

    public CombinedIterable(SortedMap<Long, T> baseItems, SortedMap<Long, T> masterItems) {
        this._baseItems = baseItems;
        this._masterItems = masterItems;
    }

    @Override // java.lang.Iterable
    public Iterator<T> iterator() {
        Iterator<Map.Entry<Long, T>> vmasterI;
        if (this._masterItems != null) {
            vmasterI = this._masterItems.entrySet().iterator();
        } else {
            Set<Map.Entry<Long, T>> empty = Collections.emptySet();
            vmasterI = empty.iterator();
        }
        final Iterator<Map.Entry<Long, T>> it = vmasterI;
        return new Iterator<T>() { // from class: org.apache.poi.xdgf.usermodel.section.CombinedIterable.1
            Long lastI = Long.MIN_VALUE;
            Map.Entry<Long, T> currentBase = null;
            Map.Entry<Long, T> currentMaster = null;
            Iterator<Map.Entry<Long, T>> baseI;
            Iterator<Map.Entry<Long, T>> masterI;

            {
                this.baseI = CombinedIterable.this._baseItems.entrySet().iterator();
                this.masterI = it;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.currentBase != null || this.currentMaster != null || this.baseI.hasNext() || this.masterI.hasNext();
            }

            @Override // java.util.Iterator
            public T next() {
                T val;
                long baseIdx = Long.MAX_VALUE;
                long masterIdx = Long.MAX_VALUE;
                if (this.currentBase == null) {
                    while (true) {
                        if (!this.baseI.hasNext()) {
                            break;
                        }
                        this.currentBase = this.baseI.next();
                        if (this.currentBase.getKey().longValue() > this.lastI.longValue()) {
                            baseIdx = this.currentBase.getKey().longValue();
                            break;
                        }
                    }
                } else {
                    baseIdx = this.currentBase.getKey().longValue();
                }
                if (this.currentMaster == null) {
                    while (true) {
                        if (!this.masterI.hasNext()) {
                            break;
                        }
                        this.currentMaster = this.masterI.next();
                        if (this.currentMaster.getKey().longValue() > this.lastI.longValue()) {
                            masterIdx = this.currentMaster.getKey().longValue();
                            break;
                        }
                    }
                } else {
                    masterIdx = this.currentMaster.getKey().longValue();
                }
                if (this.currentBase != null) {
                    if (baseIdx <= masterIdx) {
                        this.lastI = Long.valueOf(baseIdx);
                        val = this.currentBase.getValue();
                        if (masterIdx == baseIdx) {
                            this.currentMaster = null;
                        }
                        this.currentBase = null;
                    } else {
                        this.lastI = Long.valueOf(masterIdx);
                        val = this.currentMaster != null ? this.currentMaster.getValue() : null;
                        this.currentMaster = null;
                    }
                } else if (this.currentMaster != null) {
                    this.lastI = this.currentMaster.getKey();
                    val = this.currentMaster.getValue();
                    this.currentMaster = null;
                } else {
                    throw new NoSuchElementException();
                }
                return val;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
