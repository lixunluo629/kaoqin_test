package org.apache.xmlbeans.impl.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/config/NameSet.class */
public class NameSet {
    public static NameSet EMPTY = new NameSet(true, Collections.EMPTY_SET);
    public static NameSet EVERYTHING = new NameSet(false, Collections.EMPTY_SET);
    private boolean _isFinite;
    private Set _finiteSet;

    private NameSet(boolean isFinite, Set finiteSet) {
        this._isFinite = isFinite;
        this._finiteSet = finiteSet;
    }

    static NameSet newInstance(boolean isFinite, Set finiteSet) {
        if (finiteSet.size() == 0) {
            if (isFinite) {
                return EMPTY;
            }
            return EVERYTHING;
        }
        Set fs = new HashSet();
        fs.addAll(finiteSet);
        return new NameSet(isFinite, fs);
    }

    private static Set intersectFiniteSets(Set a, Set b) {
        Set intersection = new HashSet();
        while (a.iterator().hasNext()) {
            String name = (String) a.iterator().next();
            if (b.contains(name)) {
                intersection.add(name);
            }
        }
        return intersection;
    }

    public NameSet union(NameSet with) {
        if (this._isFinite) {
            if (with._isFinite) {
                Set union = new HashSet();
                union.addAll(this._finiteSet);
                union.addAll(with._finiteSet);
                return newInstance(true, union);
            }
            Set subst = new HashSet();
            subst.addAll(with._finiteSet);
            subst.removeAll(this._finiteSet);
            return newInstance(false, subst);
        }
        if (with._isFinite) {
            Set subst2 = new HashSet();
            subst2.addAll(this._finiteSet);
            subst2.removeAll(with._finiteSet);
            return newInstance(false, subst2);
        }
        return newInstance(false, intersectFiniteSets(this._finiteSet, with._finiteSet));
    }

    public NameSet intersect(NameSet with) {
        if (this._isFinite) {
            if (with._isFinite) {
                return newInstance(true, intersectFiniteSets(this._finiteSet, with._finiteSet));
            }
            Set subst = new HashSet();
            subst.addAll(this._finiteSet);
            subst.removeAll(with._finiteSet);
            return newInstance(false, subst);
        }
        if (with._isFinite) {
            Set subst2 = new HashSet();
            subst2.addAll(with._finiteSet);
            subst2.removeAll(this._finiteSet);
            return newInstance(true, subst2);
        }
        Set union = new HashSet();
        union.addAll(this._finiteSet);
        union.addAll(with._finiteSet);
        return newInstance(false, union);
    }

    public NameSet substractFrom(NameSet from) {
        return from.substract(this);
    }

    public NameSet substract(NameSet what) {
        if (this._isFinite) {
            if (what._isFinite) {
                Set subst = new HashSet();
                subst.addAll(this._finiteSet);
                subst.removeAll(what._finiteSet);
                return newInstance(true, subst);
            }
            return newInstance(true, intersectFiniteSets(this._finiteSet, what._finiteSet));
        }
        if (what._isFinite) {
            Set union = new HashSet();
            union.addAll(this._finiteSet);
            union.addAll(what._finiteSet);
            return newInstance(false, union);
        }
        Set subst2 = new HashSet();
        subst2.addAll(what._finiteSet);
        subst2.removeAll(this._finiteSet);
        return newInstance(true, subst2);
    }

    public NameSet invert() {
        return newInstance(!this._isFinite, this._finiteSet);
    }

    public boolean contains(String name) {
        if (this._isFinite) {
            return this._finiteSet.contains(name);
        }
        return !this._finiteSet.contains(name);
    }
}
