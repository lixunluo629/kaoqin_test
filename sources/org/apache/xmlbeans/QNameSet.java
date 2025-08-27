package org.apache.xmlbeans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/QNameSet.class */
public final class QNameSet implements QNameSetSpecification, Serializable {
    private static final long serialVersionUID = 1;
    private final boolean _inverted;
    private final Set _includedURIs;
    private final Set _excludedQNames;
    private final Set _includedQNames;
    public static final QNameSet EMPTY = new QNameSet(null, Collections.EMPTY_SET, Collections.EMPTY_SET, Collections.EMPTY_SET);
    public static final QNameSet ALL = new QNameSet(Collections.EMPTY_SET, null, Collections.EMPTY_SET, Collections.EMPTY_SET);
    public static final QNameSet LOCAL = new QNameSet(null, Collections.singleton(""), Collections.EMPTY_SET, Collections.EMPTY_SET);
    public static final QNameSet NONLOCAL = new QNameSet(Collections.singleton(""), null, Collections.EMPTY_SET, Collections.EMPTY_SET);

    private static Set minSetCopy(Set original) {
        if (original == null) {
            return null;
        }
        if (original.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        if (original.size() == 1) {
            return Collections.singleton(original.iterator().next());
        }
        return new HashSet(original);
    }

    public static QNameSet forSets(Set excludedURIs, Set includedURIs, Set excludedQNamesInIncludedURIs, Set includedQNamesInExcludedURIs) {
        if ((excludedURIs != null) == (includedURIs != null)) {
            throw new IllegalArgumentException("Exactly one of excludedURIs and includedURIs must be null");
        }
        if (excludedURIs == null && includedURIs.isEmpty() && includedQNamesInExcludedURIs.isEmpty()) {
            return EMPTY;
        }
        if (includedURIs == null && excludedURIs.isEmpty() && excludedQNamesInIncludedURIs.isEmpty()) {
            return ALL;
        }
        if (excludedURIs == null && includedURIs.size() == 1 && includedURIs.contains("") && includedQNamesInExcludedURIs.isEmpty() && excludedQNamesInIncludedURIs.isEmpty()) {
            return LOCAL;
        }
        if (includedURIs == null && excludedURIs.size() == 1 && excludedURIs.contains("") && excludedQNamesInIncludedURIs.isEmpty() && includedQNamesInExcludedURIs.isEmpty()) {
            return NONLOCAL;
        }
        return new QNameSet(minSetCopy(excludedURIs), minSetCopy(includedURIs), minSetCopy(excludedQNamesInIncludedURIs), minSetCopy(includedQNamesInExcludedURIs));
    }

    public static QNameSet forArray(QName[] includedQNames) {
        if (includedQNames == null) {
            throw new IllegalArgumentException("includedQNames cannot be null");
        }
        return new QNameSet(null, Collections.EMPTY_SET, Collections.EMPTY_SET, new HashSet(Arrays.asList(includedQNames)));
    }

    public static QNameSet forSpecification(QNameSetSpecification spec) {
        if (spec instanceof QNameSet) {
            return (QNameSet) spec;
        }
        return forSets(spec.excludedURIs(), spec.includedURIs(), spec.excludedQNamesInIncludedURIs(), spec.includedQNamesInExcludedURIs());
    }

    public static QNameSet forWildcardNamespaceString(String wildcard, String targetURI) {
        return forSpecification(new QNameSetBuilder(wildcard, targetURI));
    }

    public static QNameSet singleton(QName name) {
        return new QNameSet(null, Collections.EMPTY_SET, Collections.EMPTY_SET, Collections.singleton(name));
    }

    private QNameSet(Set excludedURIs, Set includedURIs, Set excludedQNamesInIncludedURIs, Set includedQNamesInExcludedURIs) {
        if (includedURIs != null && excludedURIs == null) {
            this._inverted = false;
            this._includedURIs = includedURIs;
            this._excludedQNames = excludedQNamesInIncludedURIs;
            this._includedQNames = includedQNamesInExcludedURIs;
            return;
        }
        if (excludedURIs != null && includedURIs == null) {
            this._inverted = true;
            this._includedURIs = excludedURIs;
            this._excludedQNames = includedQNamesInExcludedURIs;
            this._includedQNames = excludedQNamesInIncludedURIs;
            return;
        }
        throw new IllegalArgumentException("Exactly one of excludedURIs and includedURIs must be null");
    }

    private static String nsFromName(QName xmlName) {
        String ns = xmlName.getNamespaceURI();
        return ns == null ? "" : ns;
    }

    @Override // org.apache.xmlbeans.QNameSetSpecification
    public boolean contains(QName name) {
        boolean in = this._includedURIs.contains(nsFromName(name)) ? !this._excludedQNames.contains(name) : this._includedQNames.contains(name);
        return this._inverted ^ in;
    }

    @Override // org.apache.xmlbeans.QNameSetSpecification
    public boolean isAll() {
        return this._inverted && this._includedURIs.isEmpty() && this._includedQNames.isEmpty();
    }

    @Override // org.apache.xmlbeans.QNameSetSpecification
    public boolean isEmpty() {
        return !this._inverted && this._includedURIs.isEmpty() && this._includedQNames.isEmpty();
    }

    @Override // org.apache.xmlbeans.QNameSetSpecification
    public QNameSet intersect(QNameSetSpecification set) {
        QNameSetBuilder result = new QNameSetBuilder(this);
        result.restrict(set);
        return result.toQNameSet();
    }

    @Override // org.apache.xmlbeans.QNameSetSpecification
    public QNameSet union(QNameSetSpecification set) {
        QNameSetBuilder result = new QNameSetBuilder(this);
        result.addAll(set);
        return result.toQNameSet();
    }

    @Override // org.apache.xmlbeans.QNameSetSpecification
    public QNameSet inverse() {
        if (this == EMPTY) {
            return ALL;
        }
        if (this == ALL) {
            return EMPTY;
        }
        if (this == LOCAL) {
            return NONLOCAL;
        }
        if (this == NONLOCAL) {
            return LOCAL;
        }
        return new QNameSet(includedURIs(), excludedURIs(), includedQNamesInExcludedURIs(), excludedQNamesInIncludedURIs());
    }

    @Override // org.apache.xmlbeans.QNameSetSpecification
    public boolean containsAll(QNameSetSpecification set) {
        if (!this._inverted && set.excludedURIs() != null) {
            return false;
        }
        return inverse().isDisjoint(set);
    }

    @Override // org.apache.xmlbeans.QNameSetSpecification
    public boolean isDisjoint(QNameSetSpecification set) {
        if (this._inverted && set.excludedURIs() != null) {
            return false;
        }
        if (this._inverted) {
            return isDisjointImpl(set, this);
        }
        return isDisjointImpl(this, set);
    }

    private boolean isDisjointImpl(QNameSetSpecification set1, QNameSetSpecification set2) {
        Set includeURIs = set1.includedURIs();
        Set otherIncludeURIs = set2.includedURIs();
        if (otherIncludeURIs != null) {
            Iterator i = includeURIs.iterator();
            while (i.hasNext()) {
                if (otherIncludeURIs.contains(i.next())) {
                    return false;
                }
            }
        } else {
            Set otherExcludeURIs = set2.excludedURIs();
            Iterator i2 = includeURIs.iterator();
            while (i2.hasNext()) {
                if (!otherExcludeURIs.contains(i2.next())) {
                    return false;
                }
            }
        }
        Iterator i3 = set1.includedQNamesInExcludedURIs().iterator();
        while (i3.hasNext()) {
            if (set2.contains((QName) i3.next())) {
                return false;
            }
        }
        if (includeURIs.size() > 0) {
            Iterator i4 = set2.includedQNamesInExcludedURIs().iterator();
            while (i4.hasNext()) {
                if (set1.contains((QName) i4.next())) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    @Override // org.apache.xmlbeans.QNameSetSpecification
    public Set excludedURIs() {
        if (this._inverted) {
            return Collections.unmodifiableSet(this._includedURIs);
        }
        return null;
    }

    @Override // org.apache.xmlbeans.QNameSetSpecification
    public Set includedURIs() {
        if (this._inverted) {
            return null;
        }
        return this._includedURIs;
    }

    @Override // org.apache.xmlbeans.QNameSetSpecification
    public Set excludedQNamesInIncludedURIs() {
        return Collections.unmodifiableSet(this._inverted ? this._includedQNames : this._excludedQNames);
    }

    @Override // org.apache.xmlbeans.QNameSetSpecification
    public Set includedQNamesInExcludedURIs() {
        return Collections.unmodifiableSet(this._inverted ? this._excludedQNames : this._includedQNames);
    }

    private String prettyQName(QName name) {
        if (name.getNamespaceURI() == null) {
            return name.getLocalPart();
        }
        return name.getLocalPart() + "@" + name.getNamespaceURI();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("QNameSet");
        sb.append(this._inverted ? "-(" : "+(");
        Iterator i = this._includedURIs.iterator();
        while (i.hasNext()) {
            sb.append("+*@");
            sb.append(i.next());
            sb.append(", ");
        }
        Iterator i2 = this._excludedQNames.iterator();
        while (i2.hasNext()) {
            sb.append("-");
            sb.append(prettyQName((QName) i2.next()));
            sb.append(", ");
        }
        Iterator i3 = this._includedQNames.iterator();
        while (i3.hasNext()) {
            sb.append("+");
            sb.append(prettyQName((QName) i3.next()));
            sb.append(", ");
        }
        int index = sb.lastIndexOf(", ");
        if (index > 0) {
            sb.setLength(index);
        }
        sb.append(')');
        return sb.toString();
    }
}
