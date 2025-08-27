package org.apache.xmlbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/QNameSetBuilder.class */
public class QNameSetBuilder implements QNameSetSpecification, Serializable {
    private static final long serialVersionUID = 1;
    private boolean _inverted;
    private Set _includedURIs;
    private Set _excludedQNames;
    private Set _includedQNames;
    private static final String[] EMPTY_STRINGARRAY = new String[0];

    public QNameSetBuilder() {
        this._inverted = false;
        this._includedURIs = new HashSet();
        this._excludedQNames = new HashSet();
        this._includedQNames = new HashSet();
    }

    public QNameSetBuilder(QNameSetSpecification set) {
        Set includedURIs = set.includedURIs();
        if (includedURIs != null) {
            this._inverted = false;
            this._includedURIs = new HashSet(includedURIs);
            this._excludedQNames = new HashSet(set.excludedQNamesInIncludedURIs());
            this._includedQNames = new HashSet(set.includedQNamesInExcludedURIs());
            return;
        }
        this._inverted = true;
        this._includedURIs = new HashSet(set.excludedURIs());
        this._excludedQNames = new HashSet(set.includedQNamesInExcludedURIs());
        this._includedQNames = new HashSet(set.excludedQNamesInIncludedURIs());
    }

    public QNameSetBuilder(Set excludedURIs, Set includedURIs, Set excludedQNamesInIncludedURIs, Set includedQNamesInExcludedURIs) {
        if (includedURIs != null && excludedURIs == null) {
            this._inverted = false;
            this._includedURIs = new HashSet(includedURIs);
            this._excludedQNames = new HashSet(excludedQNamesInIncludedURIs);
            this._includedQNames = new HashSet(includedQNamesInExcludedURIs);
            return;
        }
        if (excludedURIs != null && includedURIs == null) {
            this._inverted = true;
            this._includedURIs = new HashSet(excludedURIs);
            this._excludedQNames = new HashSet(includedQNamesInExcludedURIs);
            this._includedQNames = new HashSet(excludedQNamesInIncludedURIs);
            return;
        }
        throw new IllegalArgumentException("Exactly one of excludedURIs and includedURIs must be null");
    }

    public QNameSetBuilder(String str, String targetURI) {
        this();
        String[] uri = splitList(str == null ? "##any" : str);
        for (int i = 0; i < uri.length; i++) {
            String adduri = uri[i];
            if (adduri.startsWith("##")) {
                if (adduri.equals("##other")) {
                    if (targetURI == null) {
                        throw new IllegalArgumentException();
                    }
                    QNameSetBuilder temp = new QNameSetBuilder();
                    temp.addNamespace(targetURI);
                    temp.addNamespace("");
                    temp.invert();
                    addAll(temp);
                } else if (adduri.equals("##any")) {
                    clear();
                    invert();
                } else {
                    if (uri[i].equals("##targetNamespace")) {
                        if (targetURI == null) {
                            throw new IllegalArgumentException();
                        }
                        adduri = targetURI;
                    } else if (uri[i].equals("##local")) {
                        adduri = "";
                    }
                    addNamespace(adduri);
                }
            } else {
                addNamespace(adduri);
            }
        }
    }

    private static String nsFromName(QName QName) {
        String ns = QName.getNamespaceURI();
        return ns == null ? "" : ns;
    }

    private static boolean isSpace(char ch2) {
        switch (ch2) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
                return true;
            default:
                return false;
        }
    }

    private static String[] splitList(String s) {
        if (s.length() == 0) {
            return EMPTY_STRINGARRAY;
        }
        List result = new ArrayList();
        int i = 0;
        while (true) {
            if (i < s.length() && isSpace(s.charAt(i))) {
                i++;
            } else {
                if (i >= s.length()) {
                    return (String[]) result.toArray(EMPTY_STRINGARRAY);
                }
                int start = i;
                while (i < s.length() && !isSpace(s.charAt(i))) {
                    i++;
                }
                result.add(s.substring(start, i));
            }
        }
    }

    private static void removeAllMatchingNs(String uri, Set qnameset) {
        Iterator i = qnameset.iterator();
        while (i.hasNext()) {
            if (uri.equals(nsFromName((QName) i.next()))) {
                i.remove();
            }
        }
    }

    private static void removeAllMatchingFirstOnly(Set setFirst, Set setSecond, Set qnameset) {
        Iterator i = qnameset.iterator();
        while (i.hasNext()) {
            String ns = nsFromName((QName) i.next());
            if (setFirst.contains(ns) && !setSecond.contains(ns)) {
                i.remove();
            }
        }
    }

    private static void removeAllMatchingBoth(Set setFirst, Set setSecond, Set qnameset) {
        Iterator i = qnameset.iterator();
        while (i.hasNext()) {
            String ns = nsFromName((QName) i.next());
            if (setFirst.contains(ns) && setSecond.contains(ns)) {
                i.remove();
            }
        }
    }

    private static void removeAllMatchingNeither(Set setFirst, Set setSecond, Set qnameset) {
        Iterator i = qnameset.iterator();
        while (i.hasNext()) {
            String ns = nsFromName((QName) i.next());
            if (!setFirst.contains(ns) && !setSecond.contains(ns)) {
                i.remove();
            }
        }
    }

    @Override // org.apache.xmlbeans.QNameSetSpecification
    public boolean contains(QName name) {
        boolean in = this._includedURIs.contains(nsFromName(name)) ? !this._excludedQNames.contains(name) : this._includedQNames.contains(name);
        return this._inverted ^ in;
    }

    @Override // org.apache.xmlbeans.QNameSetSpecification
    public boolean isAll() {
        return this._inverted && this._includedURIs.size() == 0 && this._includedQNames.size() == 0;
    }

    @Override // org.apache.xmlbeans.QNameSetSpecification
    public boolean isEmpty() {
        return !this._inverted && this._includedURIs.size() == 0 && this._includedQNames.size() == 0;
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
        return QNameSet.forSets(includedURIs(), excludedURIs(), includedQNamesInExcludedURIs(), excludedQNamesInIncludedURIs());
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

    public void clear() {
        this._inverted = false;
        this._includedURIs.clear();
        this._excludedQNames.clear();
        this._includedQNames.clear();
    }

    public void invert() {
        this._inverted = !this._inverted;
    }

    public void add(QName qname) {
        if (!this._inverted) {
            addImpl(qname);
        } else {
            removeImpl(qname);
        }
    }

    public void addNamespace(String uri) {
        if (!this._inverted) {
            addNamespaceImpl(uri);
        } else {
            removeNamespaceImpl(uri);
        }
    }

    public void addAll(QNameSetSpecification set) {
        if (this._inverted) {
            removeAllImpl(set.includedURIs(), set.excludedURIs(), set.includedQNamesInExcludedURIs(), set.excludedQNamesInIncludedURIs());
        } else {
            addAllImpl(set.includedURIs(), set.excludedURIs(), set.includedQNamesInExcludedURIs(), set.excludedQNamesInIncludedURIs());
        }
    }

    public void remove(QName qname) {
        if (this._inverted) {
            addImpl(qname);
        } else {
            removeImpl(qname);
        }
    }

    public void removeNamespace(String uri) {
        if (this._inverted) {
            addNamespaceImpl(uri);
        } else {
            removeNamespaceImpl(uri);
        }
    }

    public void removeAll(QNameSetSpecification set) {
        if (this._inverted) {
            addAllImpl(set.includedURIs(), set.excludedURIs(), set.includedQNamesInExcludedURIs(), set.excludedQNamesInIncludedURIs());
        } else {
            removeAllImpl(set.includedURIs(), set.excludedURIs(), set.includedQNamesInExcludedURIs(), set.excludedQNamesInIncludedURIs());
        }
    }

    public void restrict(QNameSetSpecification set) {
        if (this._inverted) {
            addAllImpl(set.excludedURIs(), set.includedURIs(), set.excludedQNamesInIncludedURIs(), set.includedQNamesInExcludedURIs());
        } else {
            removeAllImpl(set.excludedURIs(), set.includedURIs(), set.excludedQNamesInIncludedURIs(), set.includedQNamesInExcludedURIs());
        }
    }

    private void addImpl(QName qname) {
        if (this._includedURIs.contains(nsFromName(qname))) {
            this._excludedQNames.remove(qname);
        } else {
            this._includedQNames.add(qname);
        }
    }

    private void addNamespaceImpl(String uri) {
        if (this._includedURIs.contains(uri)) {
            removeAllMatchingNs(uri, this._excludedQNames);
        } else {
            removeAllMatchingNs(uri, this._includedQNames);
            this._includedURIs.add(uri);
        }
    }

    private void addAllImpl(Set includedURIs, Set excludedURIs, Set includedQNames, Set excludedQNames) {
        boolean exclude = excludedURIs != null;
        Set specialURIs = exclude ? excludedURIs : includedURIs;
        Iterator i = this._excludedQNames.iterator();
        while (i.hasNext()) {
            QName name = (QName) i.next();
            if ((exclude ^ specialURIs.contains(nsFromName(name))) && !excludedQNames.contains(name)) {
                i.remove();
            }
        }
        Iterator i2 = excludedQNames.iterator();
        while (i2.hasNext()) {
            QName name2 = (QName) i2.next();
            if (!this._includedURIs.contains(nsFromName(name2)) && !this._includedQNames.contains(name2)) {
                this._excludedQNames.add(name2);
            }
        }
        Iterator i3 = includedQNames.iterator();
        while (i3.hasNext()) {
            QName name3 = (QName) i3.next();
            if (!this._includedURIs.contains(nsFromName(name3))) {
                this._includedQNames.add(name3);
            } else {
                this._excludedQNames.remove(name3);
            }
        }
        if (!exclude) {
            removeAllMatchingFirstOnly(includedURIs, this._includedURIs, this._includedQNames);
            this._includedURIs.addAll(includedURIs);
            return;
        }
        removeAllMatchingNeither(excludedURIs, this._includedURIs, this._includedQNames);
        Iterator i4 = this._includedURIs.iterator();
        while (i4.hasNext()) {
            if (!excludedURIs.contains((String) i4.next())) {
                i4.remove();
            }
        }
        Iterator i5 = excludedURIs.iterator();
        while (i5.hasNext()) {
            String uri = (String) i5.next();
            if (!this._includedURIs.contains(uri)) {
                this._includedURIs.add(uri);
            } else {
                this._includedURIs.remove(uri);
            }
        }
        Set temp = this._excludedQNames;
        this._excludedQNames = this._includedQNames;
        this._includedQNames = temp;
        this._inverted = !this._inverted;
    }

    private void removeImpl(QName qname) {
        if (this._includedURIs.contains(nsFromName(qname))) {
            this._excludedQNames.add(qname);
        } else {
            this._includedQNames.remove(qname);
        }
    }

    private void removeNamespaceImpl(String uri) {
        if (this._includedURIs.contains(uri)) {
            removeAllMatchingNs(uri, this._excludedQNames);
            this._includedURIs.remove(uri);
        } else {
            removeAllMatchingNs(uri, this._includedQNames);
        }
    }

    private void removeAllImpl(Set includedURIs, Set excludedURIs, Set includedQNames, Set excludedQNames) {
        boolean exclude = excludedURIs != null;
        Set specialURIs = exclude ? excludedURIs : includedURIs;
        Iterator i = this._includedQNames.iterator();
        while (i.hasNext()) {
            QName name = (QName) i.next();
            String uri = nsFromName(name);
            if (exclude ^ specialURIs.contains(uri)) {
                if (!excludedQNames.contains(name)) {
                    i.remove();
                }
            } else if (includedQNames.contains(name)) {
                i.remove();
            }
        }
        Iterator i2 = includedQNames.iterator();
        while (i2.hasNext()) {
            QName name2 = (QName) i2.next();
            String uri2 = nsFromName(name2);
            if (this._includedURIs.contains(uri2)) {
                this._excludedQNames.add(name2);
            }
        }
        Iterator i3 = excludedQNames.iterator();
        while (i3.hasNext()) {
            QName name3 = (QName) i3.next();
            String uri3 = nsFromName(name3);
            if (this._includedURIs.contains(uri3) && !this._excludedQNames.contains(name3)) {
                this._includedQNames.add(name3);
            }
        }
        if (exclude) {
            removeAllMatchingFirstOnly(this._includedURIs, excludedURIs, this._excludedQNames);
        } else {
            removeAllMatchingBoth(this._includedURIs, includedURIs, this._excludedQNames);
        }
        Iterator i4 = this._includedURIs.iterator();
        while (i4.hasNext()) {
            if (exclude ^ specialURIs.contains(i4.next())) {
                i4.remove();
            }
        }
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
        sb.append("QNameSetBuilder");
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

    public QNameSet toQNameSet() {
        return QNameSet.forSpecification(this);
    }
}
