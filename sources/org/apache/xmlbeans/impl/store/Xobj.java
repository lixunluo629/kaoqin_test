package org.apache.xmlbeans.impl.store;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import org.apache.xmlbeans.CDataBookmark;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.SchemaField;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidatorListener;
import org.apache.xmlbeans.impl.common.XmlLocale;
import org.apache.xmlbeans.impl.soap.Detail;
import org.apache.xmlbeans.impl.soap.DetailEntry;
import org.apache.xmlbeans.impl.soap.Name;
import org.apache.xmlbeans.impl.soap.SOAPBody;
import org.apache.xmlbeans.impl.soap.SOAPBodyElement;
import org.apache.xmlbeans.impl.soap.SOAPElement;
import org.apache.xmlbeans.impl.soap.SOAPEnvelope;
import org.apache.xmlbeans.impl.soap.SOAPException;
import org.apache.xmlbeans.impl.soap.SOAPFault;
import org.apache.xmlbeans.impl.soap.SOAPFaultElement;
import org.apache.xmlbeans.impl.soap.SOAPHeader;
import org.apache.xmlbeans.impl.soap.SOAPHeaderElement;
import org.apache.xmlbeans.impl.soap.SOAPPart;
import org.apache.xmlbeans.impl.store.DomImpl;
import org.apache.xmlbeans.impl.store.Locale;
import org.apache.xmlbeans.impl.values.TypeStore;
import org.apache.xmlbeans.impl.values.TypeStoreUser;
import org.apache.xmlbeans.impl.values.TypeStoreUserFactory;
import org.apache.xmlbeans.impl.values.TypeStoreVisitor;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj.class */
abstract class Xobj implements TypeStore {
    static final int TEXT = 0;
    static final int ROOT = 1;
    static final int ELEM = 2;
    static final int ATTR = 3;
    static final int COMMENT = 4;
    static final int PROCINST = 5;
    static final int END_POS = -1;
    static final int NO_POS = -2;
    static final int VACANT = 256;
    static final int STABLE_USER = 512;
    static final int INHIBIT_DISCONNECT = 1024;
    Locale _locale;
    QName _name;
    Cur _embedded;
    Bookmark _bookmarks;
    int _bits;
    Xobj _parent;
    Xobj _nextSibling;
    Xobj _prevSibling;
    Xobj _firstChild;
    Xobj _lastChild;
    Object _srcValue;
    Object _srcAfter;
    int _offValue;
    int _offAfter;
    int _cchValue;
    int _cchAfter;
    DomImpl.CharNode _charNodesValue;
    DomImpl.CharNode _charNodesAfter;
    TypeStoreUser _user;
    static final /* synthetic */ boolean $assertionsDisabled;

    abstract DomImpl.Dom getDom();

    abstract Xobj newNode(Locale locale);

    static {
        $assertionsDisabled = !Xobj.class.desiredAssertionStatus();
    }

    Xobj(Locale l, int kind, int domType) {
        if (!$assertionsDisabled && kind != 1 && kind != 2 && kind != 3 && kind != 4 && kind != 5) {
            throw new AssertionError();
        }
        this._locale = l;
        this._bits = (domType << 4) + kind;
    }

    final boolean entered() {
        return this._locale.entered();
    }

    final int kind() {
        return this._bits & 15;
    }

    final int domType() {
        return (this._bits & 240) >> 4;
    }

    final boolean isRoot() {
        return kind() == 1;
    }

    final boolean isAttr() {
        return kind() == 3;
    }

    final boolean isElem() {
        return kind() == 2;
    }

    final boolean isProcinst() {
        return kind() == 5;
    }

    final boolean isComment() {
        return kind() == 4;
    }

    final boolean isContainer() {
        return Cur.kindIsContainer(kind());
    }

    final boolean isUserNode() {
        int k = kind();
        return k == 2 || k == 1 || (k == 3 && !isXmlns());
    }

    final boolean isNormalAttr() {
        return isAttr() && !Locale.isXmlns(this._name);
    }

    final boolean isXmlns() {
        return isAttr() && Locale.isXmlns(this._name);
    }

    final int cchValue() {
        return this._cchValue;
    }

    final int cchAfter() {
        return this._cchAfter;
    }

    final int posAfter() {
        return 2 + this._cchValue;
    }

    final int posMax() {
        return 2 + this._cchValue + this._cchAfter;
    }

    final String getXmlnsPrefix() {
        return Locale.xmlnsPrefix(this._name);
    }

    final String getXmlnsUri() {
        return getValueAsString();
    }

    final boolean hasTextEnsureOccupancy() {
        ensureOccupancy();
        return hasTextNoEnsureOccupancy();
    }

    final boolean hasTextNoEnsureOccupancy() {
        if (this._cchValue > 0) {
            return true;
        }
        Xobj lastAttr = lastAttr();
        return lastAttr != null && lastAttr._cchAfter > 0;
    }

    final boolean hasAttrs() {
        return this._firstChild != null && this._firstChild.isAttr();
    }

    final boolean hasChildren() {
        return (this._lastChild == null || this._lastChild.isAttr()) ? false : true;
    }

    protected final int getDomZeroOneChildren() {
        if (this._firstChild == null && this._srcValue == null && this._charNodesValue == null) {
            return 0;
        }
        if (this._lastChild != null && this._lastChild.isAttr() && this._lastChild._charNodesAfter == null && this._lastChild._srcAfter == null && this._srcValue == null && this._charNodesValue == null) {
            return 0;
        }
        if (this._firstChild == this._lastChild && this._firstChild != null && !this._firstChild.isAttr() && this._srcValue == null && this._charNodesValue == null && this._firstChild._srcAfter == null) {
            return 1;
        }
        if (this._firstChild == null && this._srcValue != null) {
            if (this._charNodesValue == null) {
                return 1;
            }
            if (this._charNodesValue._next == null && this._charNodesValue._cch == this._cchValue) {
                return 1;
            }
        }
        Xobj lastAttr = lastAttr();
        Xobj node = lastAttr == null ? null : lastAttr._nextSibling;
        if (lastAttr != null && lastAttr._srcAfter == null && node != null && node._srcAfter == null && node._nextSibling == null) {
            return 1;
        }
        return 2;
    }

    protected final boolean isFirstChildPtrDomUsable() {
        if (this._firstChild == null && this._srcValue == null && this._charNodesValue == null) {
            return true;
        }
        if (this._firstChild != null && !this._firstChild.isAttr() && this._srcValue == null && this._charNodesValue == null) {
            if ($assertionsDisabled || (this._firstChild instanceof NodeXobj)) {
                return true;
            }
            throw new AssertionError("wrong node type");
        }
        return false;
    }

    protected final boolean isNextSiblingPtrDomUsable() {
        if (this._charNodesAfter == null && this._srcAfter == null) {
            if ($assertionsDisabled || this._nextSibling == null || (this._nextSibling instanceof NodeXobj)) {
                return true;
            }
            throw new AssertionError("wrong node type");
        }
        return false;
    }

    protected final boolean isExistingCharNodesValueUsable() {
        if (this._srcValue != null && this._charNodesValue != null && this._charNodesValue._next == null && this._charNodesValue._cch == this._cchValue) {
            return true;
        }
        return false;
    }

    protected final boolean isCharNodesValueUsable() {
        if (!isExistingCharNodesValueUsable()) {
            DomImpl.CharNode charNodeUpdateCharNodes = Cur.updateCharNodes(this._locale, this, this._charNodesValue, this._cchValue);
            this._charNodesValue = charNodeUpdateCharNodes;
            if (charNodeUpdateCharNodes == null) {
                return false;
            }
        }
        return true;
    }

    protected final boolean isCharNodesAfterUsable() {
        if (this._srcAfter == null) {
            return false;
        }
        if (this._charNodesAfter != null && this._charNodesAfter._next == null && this._charNodesAfter._cch == this._cchAfter) {
            return true;
        }
        DomImpl.CharNode charNodeUpdateCharNodes = Cur.updateCharNodes(this._locale, this, this._charNodesAfter, this._cchAfter);
        this._charNodesAfter = charNodeUpdateCharNodes;
        return charNodeUpdateCharNodes != null;
    }

    final Xobj lastAttr() {
        Xobj lastAttr;
        if (this._firstChild == null || !this._firstChild.isAttr()) {
            return null;
        }
        Xobj xobj = this._firstChild;
        while (true) {
            lastAttr = xobj;
            if (lastAttr._nextSibling == null || !lastAttr._nextSibling.isAttr()) {
                break;
            }
            xobj = lastAttr._nextSibling;
        }
        return lastAttr;
    }

    final int cchLeft(int p) {
        if (isRoot() && p == 0) {
            return 0;
        }
        Xobj x = getDenormal(p);
        int p2 = posTemp();
        int pa = x.posAfter();
        return p2 - (p2 < pa ? 1 : pa);
    }

    final int cchRight(int p) {
        if (!$assertionsDisabled && p >= posMax()) {
            throw new AssertionError();
        }
        if (p <= 0) {
            return 0;
        }
        int pa = posAfter();
        return p < pa ? (pa - p) - 1 : posMax() - p;
    }

    public final Locale locale() {
        return this._locale;
    }

    public final int nodeType() {
        return domType();
    }

    public final QName getQName() {
        return this._name;
    }

    public final Cur tempCur() {
        Cur c = this._locale.tempCur();
        c.moveTo(this);
        return c;
    }

    public void dump(PrintStream o, Object ref) {
        Cur.dump(o, this, ref);
    }

    public void dump(PrintStream o) {
        Cur.dump(o, this, this);
    }

    public void dump() {
        dump(System.out);
    }

    final Cur getEmbedded() {
        this._locale.embedCurs();
        return this._embedded;
    }

    final boolean inChars(int p, Xobj xIn, int pIn, int cch, boolean includeEnd) {
        int offset;
        if (!$assertionsDisabled && (p <= 0 || p >= posMax() || p == posAfter() - 1 || cch <= 0)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !xIn.isNormal(pIn)) {
            throw new AssertionError();
        }
        if (includeEnd) {
            if (xIn.isRoot() && pIn == 0) {
                return false;
            }
            xIn = xIn.getDenormal(pIn);
            pIn = xIn.posTemp();
            offset = 1;
        } else {
            offset = 0;
        }
        if (xIn == this && pIn >= p) {
            if (pIn < p + (cch < 0 ? cchRight(p) : cch) + offset) {
                return true;
            }
        }
        return false;
    }

    final boolean isJustAfterEnd(Xobj x, int p) {
        if (!$assertionsDisabled && !x.isNormal(p)) {
            throw new AssertionError();
        }
        if (x.isRoot() && p == 0) {
            return false;
        }
        return x == this ? p == posAfter() : x.getDenormal(p) == this && x.posTemp() == posAfter();
    }

    final boolean isInSameTree(Xobj x) {
        if (this._locale != x._locale) {
            return false;
        }
        Xobj xobj = this;
        while (true) {
            Xobj y = xobj;
            if (y == x) {
                return true;
            }
            if (y._parent != null) {
                xobj = y._parent;
            } else {
                while (x != this) {
                    if (x._parent == null) {
                        return x == y;
                    }
                    x = x._parent;
                }
                return true;
            }
        }
    }

    final boolean contains(Cur c) {
        if ($assertionsDisabled || c.isNormal()) {
            return contains(c._xobj, c._pos);
        }
        throw new AssertionError();
    }

    final boolean contains(Xobj x, int p) {
        if (!$assertionsDisabled && !x.isNormal(p)) {
            throw new AssertionError();
        }
        if (this == x) {
            return p == -1 || (p > 0 && p < posAfter());
        }
        if (this._firstChild == null) {
            return false;
        }
        while (x != null) {
            if (x != this) {
                x = x._parent;
            } else {
                return true;
            }
        }
        return false;
    }

    final Bookmark setBookmark(int p, Object key, Object value) {
        if (!$assertionsDisabled && !isNormal(p)) {
            throw new AssertionError();
        }
        Bookmark bookmark = this._bookmarks;
        while (true) {
            Bookmark b = bookmark;
            if (b != null) {
                if (p != b._pos || key != b._key) {
                    bookmark = b._next;
                } else {
                    if (value == null) {
                        this._bookmarks = b.listRemove(this._bookmarks);
                        return null;
                    }
                    b._value = value;
                    return b;
                }
            } else {
                if (value == null) {
                    return null;
                }
                Bookmark b2 = new Bookmark();
                b2._xobj = this;
                b2._pos = p;
                b2._key = key;
                b2._value = value;
                this._bookmarks = b2.listInsert(this._bookmarks);
                return b2;
            }
        }
    }

    final boolean hasBookmark(Object key, int pos) {
        Bookmark bookmark = this._bookmarks;
        while (true) {
            Bookmark b = bookmark;
            if (b != null) {
                if (b._pos != pos || key != b._key) {
                    bookmark = b._next;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    final Xobj findXmlnsForPrefix(String prefix) {
        if (!$assertionsDisabled && (!isContainer() || prefix == null)) {
            throw new AssertionError();
        }
        Xobj xobj = this;
        while (true) {
            Xobj c = xobj;
            if (c != null) {
                Xobj xobjFirstAttr = c.firstAttr();
                while (true) {
                    Xobj a = xobjFirstAttr;
                    if (a != null) {
                        if (!a.isXmlns() || !a.getXmlnsPrefix().equals(prefix)) {
                            xobjFirstAttr = a.nextAttr();
                        } else {
                            return a;
                        }
                    }
                }
            } else {
                return null;
            }
            xobj = c._parent;
        }
    }

    final boolean removeAttr(QName name) {
        if (!$assertionsDisabled && !isContainer()) {
            throw new AssertionError();
        }
        Xobj a = getAttr(name);
        if (a == null) {
            return false;
        }
        Cur c = a.tempCur();
        while (true) {
            c.moveNode(null);
            Xobj a2 = getAttr(name);
            if (a2 != null) {
                c.moveTo(a2);
            } else {
                c.release();
                return true;
            }
        }
    }

    final Xobj setAttr(QName name, String value) {
        if (!$assertionsDisabled && !isContainer()) {
            throw new AssertionError();
        }
        Cur c = tempCur();
        if (c.toAttr(name)) {
            c.removeFollowingAttrs();
        } else {
            c.next();
            c.createAttr(name);
        }
        c.setValue(value);
        Xobj a = c._xobj;
        c.release();
        return a;
    }

    final void setName(QName newName) {
        if (!$assertionsDisabled && !isAttr() && !isElem() && !isProcinst()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && newName == null) {
            throw new AssertionError();
        }
        if (!this._name.equals(newName) || !this._name.getPrefix().equals(newName.getPrefix())) {
            this._locale.notifyChange();
            QName oldName = this._name;
            this._name = newName;
            if (this instanceof NamedNodeXobj) {
                NamedNodeXobj me = (NamedNodeXobj) this;
                me._canHavePrefixUri = true;
            }
            if (!isProcinst()) {
                Xobj disconnectFromHere = this;
                if (isAttr() && this._parent != null) {
                    if (oldName.equals(Locale._xsiType) || newName.equals(Locale._xsiType)) {
                        disconnectFromHere = this._parent;
                    }
                    if (oldName.equals(Locale._xsiNil) || newName.equals(Locale._xsiNil)) {
                        this._parent.invalidateNil();
                    }
                }
                disconnectFromHere.disconnectNonRootUsers();
            }
            this._locale._versionAll++;
            this._locale._versionSansText++;
        }
    }

    final Xobj ensureParent() {
        if ($assertionsDisabled || this._parent != null || (!isRoot() && cchAfter() == 0)) {
            return this._parent == null ? new DocumentFragXobj(this._locale).appendXobj(this) : this._parent;
        }
        throw new AssertionError();
    }

    final Xobj firstAttr() {
        if (this._firstChild == null || !this._firstChild.isAttr()) {
            return null;
        }
        return this._firstChild;
    }

    final Xobj nextAttr() {
        if (this._firstChild != null && this._firstChild.isAttr()) {
            return this._firstChild;
        }
        if (this._nextSibling != null && this._nextSibling.isAttr()) {
            return this._nextSibling;
        }
        return null;
    }

    final boolean isValid() {
        if (!isVacant()) {
            return true;
        }
        if (this._cchValue != 0 || this._user == null) {
            return false;
        }
        return true;
    }

    final int posTemp() {
        return this._locale._posTemp;
    }

    final Xobj getNormal(int p) {
        if (!$assertionsDisabled && p != -1 && (p < 0 || p > posMax())) {
            throw new AssertionError();
        }
        Xobj x = this;
        if (p == x.posMax()) {
            if (x._nextSibling != null) {
                x = x._nextSibling;
                p = 0;
            } else {
                x = x.ensureParent();
                p = -1;
            }
        } else if (p == x.posAfter() - 1) {
            p = -1;
        }
        this._locale._posTemp = p;
        return x;
    }

    final Xobj getDenormal(int p) {
        if (!$assertionsDisabled && isRoot() && p != -1 && p <= 0) {
            throw new AssertionError();
        }
        Xobj x = this;
        if (p == 0) {
            if (x._prevSibling == null) {
                x = x.ensureParent();
                p = x.posAfter() - 1;
            } else {
                x = x._prevSibling;
                p = x.posMax();
            }
        } else if (p == -1) {
            if (x._lastChild == null) {
                p = x.posAfter() - 1;
            } else {
                x = x._lastChild;
                p = x.posMax();
            }
        }
        this._locale._posTemp = p;
        return x;
    }

    final boolean isNormal(int p) {
        if (!isValid()) {
            return false;
        }
        if (p == -1 || p == 0) {
            return true;
        }
        if (p < 0 || p >= posMax()) {
            return false;
        }
        if (p >= posAfter()) {
            if (isRoot()) {
                return false;
            }
            if ((this._nextSibling != null && this._nextSibling.isAttr()) || this._parent == null || !this._parent.isContainer()) {
                return false;
            }
        }
        if (p == posAfter() - 1) {
            return false;
        }
        return true;
    }

    final Xobj walk(Xobj root, boolean walkChildren) {
        if (this._firstChild != null && walkChildren) {
            return this._firstChild;
        }
        Xobj xobj = this;
        while (true) {
            Xobj x = xobj;
            if (x != root) {
                if (x._nextSibling == null) {
                    xobj = x._parent;
                } else {
                    return x._nextSibling;
                }
            } else {
                return null;
            }
        }
    }

    final Xobj removeXobj() {
        if (this._parent != null) {
            if (this._parent._firstChild == this) {
                this._parent._firstChild = this._nextSibling;
            }
            if (this._parent._lastChild == this) {
                this._parent._lastChild = this._prevSibling;
            }
            if (this._prevSibling != null) {
                this._prevSibling._nextSibling = this._nextSibling;
            }
            if (this._nextSibling != null) {
                this._nextSibling._prevSibling = this._prevSibling;
            }
            this._parent = null;
            this._prevSibling = null;
            this._nextSibling = null;
        }
        return this;
    }

    final Xobj insertXobj(Xobj s) {
        if (!$assertionsDisabled && this._locale != s._locale) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (s.isRoot() || isRoot())) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && s._parent != null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && s._prevSibling != null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && s._nextSibling != null) {
            throw new AssertionError();
        }
        ensureParent();
        s._parent = this._parent;
        s._prevSibling = this._prevSibling;
        s._nextSibling = this;
        if (this._prevSibling != null) {
            this._prevSibling._nextSibling = s;
        } else {
            this._parent._firstChild = s;
        }
        this._prevSibling = s;
        return this;
    }

    final Xobj appendXobj(Xobj c) {
        if (!$assertionsDisabled && this._locale != c._locale) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && c.isRoot()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && c._parent != null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && c._prevSibling != null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && c._nextSibling != null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this._lastChild != null && this._firstChild == null) {
            throw new AssertionError();
        }
        c._parent = this;
        c._prevSibling = this._lastChild;
        if (this._lastChild == null) {
            this._firstChild = c;
        } else {
            this._lastChild._nextSibling = c;
        }
        this._lastChild = c;
        return this;
    }

    final void removeXobjs(Xobj first, Xobj last) {
        if (!$assertionsDisabled && last._locale != first._locale) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && first._parent != this) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && last._parent != this) {
            throw new AssertionError();
        }
        if (this._firstChild == first) {
            this._firstChild = last._nextSibling;
        }
        if (this._lastChild == last) {
            this._lastChild = first._prevSibling;
        }
        if (first._prevSibling != null) {
            first._prevSibling._nextSibling = last._nextSibling;
        }
        if (last._nextSibling != null) {
            last._nextSibling._prevSibling = first._prevSibling;
        }
        first._prevSibling = null;
        last._nextSibling = null;
        while (first != null) {
            first._parent = null;
            first = first._nextSibling;
        }
    }

    final void insertXobjs(Xobj first, Xobj last) {
        if (!$assertionsDisabled && this._locale != first._locale) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && last._locale != first._locale) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (first._parent != null || last._parent != null)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && first._prevSibling != null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && last._nextSibling != null) {
            throw new AssertionError();
        }
        first._prevSibling = this._prevSibling;
        last._nextSibling = this;
        if (this._prevSibling != null) {
            this._prevSibling._nextSibling = first;
        } else {
            this._parent._firstChild = first;
        }
        this._prevSibling = last;
        while (first != this) {
            first._parent = this._parent;
            first = first._nextSibling;
        }
    }

    final void appendXobjs(Xobj first, Xobj last) {
        if (!$assertionsDisabled && this._locale != first._locale) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && last._locale != first._locale) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (first._parent != null || last._parent != null)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && first._prevSibling != null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && last._nextSibling != null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && first.isRoot()) {
            throw new AssertionError();
        }
        first._prevSibling = this._lastChild;
        if (this._lastChild == null) {
            this._firstChild = first;
        } else {
            this._lastChild._nextSibling = first;
        }
        this._lastChild = last;
        while (first != null) {
            first._parent = this;
            first = first._nextSibling;
        }
    }

    static final void disbandXobjs(Xobj first, Xobj last) {
        if (!$assertionsDisabled && last._locale != first._locale) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (first._parent != null || last._parent != null)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && first._prevSibling != null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && last._nextSibling != null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && first.isRoot()) {
            throw new AssertionError();
        }
        while (first != null) {
            Xobj next = first._nextSibling;
            first._prevSibling = null;
            first._nextSibling = null;
            first = next;
        }
    }

    final void invalidateSpecialAttr(Xobj newParent) {
        if (isAttr()) {
            if (this._name.equals(Locale._xsiType)) {
                if (this._parent != null) {
                    this._parent.disconnectNonRootUsers();
                }
                if (newParent != null) {
                    newParent.disconnectNonRootUsers();
                }
            }
            if (this._name.equals(Locale._xsiNil)) {
                if (this._parent != null) {
                    this._parent.invalidateNil();
                }
                if (newParent != null) {
                    newParent.invalidateNil();
                }
            }
        }
    }

    final void removeCharsHelper(int p, int cchRemove, Xobj xTo, int pTo, boolean moveCurs, boolean invalidate) {
        if (!$assertionsDisabled && (p <= 0 || p >= posMax() || p == posAfter() - 1)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && cchRemove <= 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && cchRight(p) < cchRemove) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && moveCurs && xTo == null) {
            throw new AssertionError();
        }
        Cur embedded = getEmbedded();
        while (true) {
            Cur c = embedded;
            if (c != null) {
                Cur next = c._next;
                if (!$assertionsDisabled && c._xobj != this) {
                    throw new AssertionError();
                }
                if (c._pos >= p && c._pos < p + cchRemove) {
                    if (moveCurs) {
                        c.moveToNoCheck(xTo, (pTo + c._pos) - p);
                    } else {
                        c.nextChars((cchRemove - c._pos) + p);
                    }
                }
                if (c._xobj == this && c._pos >= p + cchRemove) {
                    c._pos -= cchRemove;
                }
                embedded = next;
            } else {
                Bookmark bookmark = this._bookmarks;
                while (true) {
                    Bookmark b = bookmark;
                    if (b != null) {
                        Bookmark bookmark2 = b._next;
                        if (!$assertionsDisabled && b._xobj != this) {
                            throw new AssertionError();
                        }
                        if (b._pos >= p && b._pos < p + cchRemove) {
                            if (!$assertionsDisabled && xTo == null) {
                                throw new AssertionError();
                            }
                            b.moveTo(xTo, (pTo + b._pos) - p);
                        }
                        if (b._xobj == this && b._pos >= p + cchRemove) {
                            b._pos -= cchRemove;
                        }
                        bookmark = b._next;
                    } else {
                        int pa = posAfter();
                        CharUtil cu = this._locale.getCharUtil();
                        if (p < pa) {
                            this._srcValue = cu.removeChars(p - 1, cchRemove, this._srcValue, this._offValue, this._cchValue);
                            this._offValue = cu._offSrc;
                            this._cchValue = cu._cchSrc;
                            if (invalidate) {
                                invalidateUser();
                                invalidateSpecialAttr(null);
                                return;
                            }
                            return;
                        }
                        this._srcAfter = cu.removeChars(p - pa, cchRemove, this._srcAfter, this._offAfter, this._cchAfter);
                        this._offAfter = cu._offSrc;
                        this._cchAfter = cu._cchSrc;
                        if (invalidate && this._parent != null) {
                            this._parent.invalidateUser();
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    final void insertCharsHelper(int p, Object src, int off, int cch, boolean invalidate) {
        if (!$assertionsDisabled && p <= 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && p < posAfter() && !isOccupied()) {
            throw new AssertionError();
        }
        int pa = posAfter();
        if (p - (p < pa ? 1 : 2) < this._cchValue + this._cchAfter) {
            Cur embedded = getEmbedded();
            while (true) {
                Cur c = embedded;
                if (c == null) {
                    break;
                }
                if (c._pos >= p) {
                    c._pos += cch;
                }
                embedded = c._next;
            }
            Bookmark bookmark = this._bookmarks;
            while (true) {
                Bookmark b = bookmark;
                if (b == null) {
                    break;
                }
                if (b._pos >= p) {
                    b._pos += cch;
                }
                bookmark = b._next;
            }
        }
        CharUtil cu = this._locale.getCharUtil();
        if (p < pa) {
            this._srcValue = cu.insertChars(p - 1, this._srcValue, this._offValue, this._cchValue, src, off, cch);
            this._offValue = cu._offSrc;
            this._cchValue = cu._cchSrc;
            if (invalidate) {
                invalidateUser();
                invalidateSpecialAttr(null);
                return;
            }
            return;
        }
        this._srcAfter = cu.insertChars(p - pa, this._srcAfter, this._offAfter, this._cchAfter, src, off, cch);
        this._offAfter = cu._offSrc;
        this._cchAfter = cu._cchSrc;
        if (invalidate && this._parent != null) {
            this._parent.invalidateUser();
        }
    }

    Xobj copyNode(Locale toLocale) {
        Xobj newParent = null;
        Xobj copy = null;
        Xobj x = this;
        while (true) {
            x.ensureOccupancy();
            Xobj newX = x.newNode(toLocale);
            newX._srcValue = x._srcValue;
            newX._offValue = x._offValue;
            newX._cchValue = x._cchValue;
            newX._srcAfter = x._srcAfter;
            newX._offAfter = x._offAfter;
            newX._cchAfter = x._cchAfter;
            Bookmark bookmark = x._bookmarks;
            while (true) {
                Bookmark b = bookmark;
                if (b == null) {
                    break;
                }
                if (x.hasBookmark(CDataBookmark.CDATA_BOOKMARK.getKey(), b._pos)) {
                    newX.setBookmark(b._pos, CDataBookmark.CDATA_BOOKMARK.getKey(), CDataBookmark.CDATA_BOOKMARK);
                }
                bookmark = b._next;
            }
            if (newParent == null) {
                copy = newX;
            } else {
                newParent.appendXobj(newX);
            }
            Xobj y = x;
            Xobj xobjWalk = x.walk(this, true);
            x = xobjWalk;
            if (xobjWalk != null) {
                if (y == x._parent) {
                    newParent = newX;
                } else {
                    while (y._parent != x._parent) {
                        newParent = newParent._parent;
                        y = y._parent;
                    }
                }
            } else {
                copy._srcAfter = null;
                copy._offAfter = 0;
                copy._cchAfter = 0;
                return copy;
            }
        }
    }

    String getCharsAsString(int p, int cch, int wsr) {
        if (cchRight(p) == 0) {
            return "";
        }
        Object src = getChars(p, cch);
        if (wsr == 1) {
            return CharUtil.getString(src, this._locale._offSrc, this._locale._cchSrc);
        }
        Locale.ScrubBuffer scrub = Locale.getScrubBuffer(wsr);
        scrub.scrub(src, this._locale._offSrc, this._locale._cchSrc);
        return scrub.getResultAsString();
    }

    String getCharsAfterAsString(int off, int cch) {
        int offset = off + this._cchValue + 2;
        if (offset == posMax()) {
            offset = -1;
        }
        return getCharsAsString(offset, cch, 1);
    }

    String getCharsValueAsString(int off, int cch) {
        return getCharsAsString(off + 1, cch, 1);
    }

    String getValueAsString(int wsr) {
        if (!hasChildren()) {
            Object src = getFirstChars();
            if (wsr == 1) {
                String s = CharUtil.getString(src, this._locale._offSrc, this._locale._cchSrc);
                int cch = s.length();
                if (cch > 0) {
                    Xobj lastAttr = lastAttr();
                    if (!$assertionsDisabled) {
                        if ((lastAttr == null ? this._cchValue : lastAttr._cchAfter) != cch) {
                            throw new AssertionError();
                        }
                    }
                    if (lastAttr != null) {
                        lastAttr._srcAfter = s;
                        lastAttr._offAfter = 0;
                    } else {
                        this._srcValue = s;
                        this._offValue = 0;
                    }
                }
                return s;
            }
            Locale.ScrubBuffer scrub = Locale.getScrubBuffer(wsr);
            scrub.scrub(src, this._locale._offSrc, this._locale._cchSrc);
            return scrub.getResultAsString();
        }
        Locale.ScrubBuffer scrub2 = Locale.getScrubBuffer(wsr);
        Cur c = tempCur();
        c.push();
        c.next();
        while (!c.isAtEndOfLastPush()) {
            if (c.isText()) {
                scrub2.scrub(c.getChars(-1), c._offSrc, c._cchSrc);
            }
            if (c.isComment() || c.isProcinst()) {
                c.skip();
            } else {
                c.next();
            }
        }
        String s2 = scrub2.getResultAsString();
        c.release();
        return s2;
    }

    String getValueAsString() {
        return getValueAsString(1);
    }

    String getString(int p, int cch) {
        String s;
        int cchRight = cchRight(p);
        if (cchRight == 0) {
            return "";
        }
        if (cch < 0 || cch > cchRight) {
            cch = cchRight;
        }
        int pa = posAfter();
        if (!$assertionsDisabled && p <= 0) {
            throw new AssertionError();
        }
        if (p >= pa) {
            s = CharUtil.getString(this._srcAfter, (this._offAfter + p) - pa, cch);
            if (p == pa && cch == this._cchAfter) {
                this._srcAfter = s;
                this._offAfter = 0;
            }
        } else {
            s = CharUtil.getString(this._srcValue, (this._offValue + p) - 1, cch);
            if (p == 1 && cch == this._cchValue) {
                this._srcValue = s;
                this._offValue = 0;
            }
        }
        return s;
    }

    Object getFirstChars() {
        ensureOccupancy();
        if (this._cchValue > 0) {
            return getChars(1, -1);
        }
        Xobj lastAttr = lastAttr();
        if (lastAttr == null || lastAttr._cchAfter <= 0) {
            this._locale._offSrc = 0;
            this._locale._cchSrc = 0;
            return null;
        }
        return lastAttr.getChars(lastAttr.posAfter(), -1);
    }

    Object getChars(int pos, int cch, Cur c) {
        Object src = getChars(pos, cch);
        c._offSrc = this._locale._offSrc;
        c._cchSrc = this._locale._cchSrc;
        return src;
    }

    Object getChars(int pos, int cch) {
        if (!$assertionsDisabled && !isNormal(pos)) {
            throw new AssertionError();
        }
        int cchRight = cchRight(pos);
        if (cch < 0 || cch > cchRight) {
            cch = cchRight;
        }
        if (cch == 0) {
            this._locale._offSrc = 0;
            this._locale._cchSrc = 0;
            return null;
        }
        return getCharsHelper(pos, cch);
    }

    Object getCharsHelper(int pos, int cch) {
        Object src;
        if (!$assertionsDisabled && (cch <= 0 || cchRight(pos) < cch)) {
            throw new AssertionError();
        }
        int pa = posAfter();
        if (pos >= pa) {
            src = this._srcAfter;
            this._locale._offSrc = (this._offAfter + pos) - pa;
        } else {
            src = this._srcValue;
            this._locale._offSrc = (this._offValue + pos) - 1;
        }
        this._locale._cchSrc = cch;
        return src;
    }

    final void setBit(int mask) {
        this._bits |= mask;
    }

    final void clearBit(int mask) {
        this._bits &= mask ^ (-1);
    }

    final boolean bitIsSet(int mask) {
        return (this._bits & mask) != 0;
    }

    final boolean bitIsClear(int mask) {
        return (this._bits & mask) == 0;
    }

    final boolean isVacant() {
        return bitIsSet(256);
    }

    final boolean isOccupied() {
        return bitIsClear(256);
    }

    final boolean inhibitDisconnect() {
        return bitIsSet(1024);
    }

    final boolean isStableUser() {
        return bitIsSet(512);
    }

    void invalidateNil() {
        if (this._user != null) {
            this._user.invalidate_nilvalue();
        }
    }

    void setStableType(SchemaType type) {
        setStableUser(((TypeStoreUserFactory) type).createTypeStoreUser());
    }

    void setStableUser(TypeStoreUser user) {
        disconnectNonRootUsers();
        disconnectUser();
        if (!$assertionsDisabled && this._user != null) {
            throw new AssertionError();
        }
        this._user = user;
        this._user.attach_store(this);
        setBit(512);
    }

    void disconnectUser() {
        if (this._user != null && !inhibitDisconnect()) {
            ensureOccupancy();
            this._user.disconnect_store();
            this._user = null;
        }
    }

    void disconnectNonRootUsers() {
        Xobj xobj = this;
        while (true) {
            Xobj x = xobj;
            if (x != null) {
                Xobj next = x.walk(this, x._user != null);
                if (!x.isRoot()) {
                    x.disconnectUser();
                }
                xobj = next;
            } else {
                return;
            }
        }
    }

    void disconnectChildrenUsers() {
        Xobj xobjWalk = walk(this, this._user == null);
        while (true) {
            Xobj x = xobjWalk;
            if (x != null) {
                Xobj next = x.walk(this, x._user != null);
                x.disconnectUser();
                xobjWalk = next;
            } else {
                return;
            }
        }
    }

    final String namespaceForPrefix(String prefix, boolean defaultAlwaysMapped) {
        if (prefix == null) {
            prefix = "";
        }
        if (prefix.equals("xml")) {
            return "http://www.w3.org/XML/1998/namespace";
        }
        if (prefix.equals("xmlns")) {
            return "http://www.w3.org/2000/xmlns/";
        }
        Xobj xobj = this;
        while (true) {
            Xobj x = xobj;
            if (x != null) {
                Xobj xobj2 = x._firstChild;
                while (true) {
                    Xobj a = xobj2;
                    if (a == null || !a.isAttr()) {
                        break;
                    }
                    if (!a.isXmlns() || !a.getXmlnsPrefix().equals(prefix)) {
                        xobj2 = a._nextSibling;
                    } else {
                        return a.getXmlnsUri();
                    }
                }
            } else {
                if (defaultAlwaysMapped && prefix.length() == 0) {
                    return "";
                }
                return null;
            }
            xobj = x._parent;
        }
    }

    final String prefixForNamespace(String ns, String suggestion, boolean createIfMissing) {
        Xobj base;
        if (ns == null) {
            ns = "";
        }
        if (ns.equals("http://www.w3.org/XML/1998/namespace")) {
            return "xml";
        }
        if (ns.equals("http://www.w3.org/2000/xmlns/")) {
            return "xmlns";
        }
        Xobj xobjEnsureParent = this;
        while (true) {
            base = xobjEnsureParent;
            if (base.isContainer()) {
                break;
            }
            xobjEnsureParent = base.ensureParent();
        }
        if (ns.length() == 0) {
            Xobj a = base.findXmlnsForPrefix("");
            if (a == null || a.getXmlnsUri().length() == 0) {
                return "";
            }
            if (!createIfMissing) {
                return null;
            }
            base.setAttr(this._locale.createXmlns(null), "");
            return "";
        }
        Xobj xobj = base;
        while (true) {
            Xobj c = xobj;
            if (c != null) {
                Xobj xobjFirstAttr = c.firstAttr();
                while (true) {
                    Xobj a2 = xobjFirstAttr;
                    if (a2 != null) {
                        if (!a2.isXmlns() || !a2.getXmlnsUri().equals(ns) || base.findXmlnsForPrefix(a2.getXmlnsPrefix()) != a2) {
                            xobjFirstAttr = a2.nextAttr();
                        } else {
                            return a2.getXmlnsPrefix();
                        }
                    }
                }
            } else {
                if (!createIfMissing) {
                    return null;
                }
                if (suggestion != null && (suggestion.length() == 0 || suggestion.toLowerCase().startsWith("xml") || base.findXmlnsForPrefix(suggestion) != null)) {
                    suggestion = null;
                }
                if (suggestion == null) {
                    String prefixBase = QNameHelper.suggestPrefix(ns);
                    suggestion = prefixBase;
                    int i = 1;
                    while (base.findXmlnsForPrefix(suggestion) != null) {
                        int i2 = i;
                        i++;
                        suggestion = prefixBase + i2;
                    }
                }
                Xobj xobj2 = base;
                while (true) {
                    Xobj c2 = xobj2;
                    if (c2.isRoot() || c2.ensureParent().isRoot()) {
                        break;
                    }
                    xobj2 = c2._parent;
                }
                base.setAttr(this._locale.createXmlns(suggestion), ns);
                return suggestion;
            }
            xobj = c._parent;
        }
    }

    final QName getValueAsQName() {
        String prefix;
        String localname;
        if (!$assertionsDisabled && hasChildren()) {
            throw new AssertionError();
        }
        String value = getValueAsString(3);
        int firstcolon = value.indexOf(58);
        if (firstcolon >= 0) {
            prefix = value.substring(0, firstcolon);
            localname = value.substring(firstcolon + 1);
        } else {
            prefix = "";
            localname = value;
        }
        String uri = namespaceForPrefix(prefix, true);
        if (uri == null) {
            return null;
        }
        return new QName(uri, localname);
    }

    final Xobj getAttr(QName name) {
        Xobj xobj = this._firstChild;
        while (true) {
            Xobj x = xobj;
            if (x != null && x.isAttr()) {
                if (!x._name.equals(name)) {
                    xobj = x._nextSibling;
                } else {
                    return x;
                }
            } else {
                return null;
            }
        }
    }

    final QName getXsiTypeName() {
        if (!$assertionsDisabled && !isContainer()) {
            throw new AssertionError();
        }
        Xobj a = getAttr(Locale._xsiType);
        if (a == null) {
            return null;
        }
        return a.getValueAsQName();
    }

    final XmlObject getObject() {
        if (isUserNode()) {
            return (XmlObject) getUser();
        }
        return null;
    }

    final TypeStoreUser getUser() {
        if (!$assertionsDisabled && !isUserNode()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this._user == null && (isRoot() || isStableUser())) {
            throw new AssertionError();
        }
        if (this._user == null) {
            TypeStoreUser parentUser = this._parent == null ? ((TypeStoreUserFactory) XmlBeans.NO_TYPE).createTypeStoreUser() : this._parent.getUser();
            this._user = isElem() ? parentUser.create_element_user(this._name, getXsiTypeName()) : parentUser.create_attribute_user(this._name);
            this._user.attach_store(this);
        }
        return this._user;
    }

    final void invalidateUser() {
        if (!$assertionsDisabled && !isValid()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this._user != null && !isUserNode()) {
            throw new AssertionError();
        }
        if (this._user != null) {
            this._user.invalidate_value();
        }
    }

    final void ensureOccupancy() {
        if (!$assertionsDisabled && !isValid()) {
            throw new AssertionError();
        }
        if (isVacant()) {
            if (!$assertionsDisabled && !isUserNode()) {
                throw new AssertionError();
            }
            clearBit(256);
            TypeStoreUser user = this._user;
            this._user = null;
            String value = user.build_text(this);
            long saveVersion = this._locale._versionAll;
            long saveVersionSansText = this._locale._versionSansText;
            setValue(value);
            if (!$assertionsDisabled && saveVersionSansText != this._locale._versionSansText) {
                throw new AssertionError();
            }
            this._locale._versionAll = saveVersion;
            if (!$assertionsDisabled && this._user != null) {
                throw new AssertionError();
            }
            this._user = user;
        }
    }

    private void setValue(String val) {
        if (!$assertionsDisabled && !CharUtil.isValid(val, 0, val.length())) {
            throw new AssertionError();
        }
        if (val.length() <= 0) {
            return;
        }
        this._locale.notifyChange();
        Xobj lastAttr = lastAttr();
        int startPos = 1;
        Xobj charOwner = this;
        if (lastAttr != null) {
            charOwner = lastAttr;
            startPos = charOwner.posAfter();
        }
        charOwner.insertCharsHelper(startPos, val, 0, val.length(), true);
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public SchemaTypeLoader get_schematypeloader() {
        return this._locale._schemaTypeLoader;
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public XmlLocale get_locale() {
        return this._locale;
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public Object get_root_object() {
        return this._locale;
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public boolean is_attribute() {
        if ($assertionsDisabled || isValid()) {
            return isAttr();
        }
        throw new AssertionError();
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public boolean validate_on_set() {
        if ($assertionsDisabled || isValid()) {
            return this._locale._validateOnSet;
        }
        throw new AssertionError();
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public void invalidate_text() {
        this._locale.enter();
        try {
            if (!$assertionsDisabled && !isValid()) {
                throw new AssertionError();
            }
            if (isOccupied()) {
                if (hasTextNoEnsureOccupancy() || hasChildren()) {
                    TypeStoreUser user = this._user;
                    this._user = null;
                    Cur c = tempCur();
                    c.moveNodeContents(null, false);
                    c.release();
                    if (!$assertionsDisabled && this._user != null) {
                        throw new AssertionError();
                    }
                    this._user = user;
                }
                setBit(256);
            }
            if (!$assertionsDisabled && !isValid()) {
                throw new AssertionError();
            }
        } finally {
            this._locale.exit();
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public String fetch_text(int wsr) {
        this._locale.enter();
        try {
            if (!$assertionsDisabled && (!isValid() || !isOccupied())) {
                throw new AssertionError();
            }
            String valueAsString = getValueAsString(wsr);
            this._locale.exit();
            return valueAsString;
        } catch (Throwable th) {
            this._locale.exit();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public XmlCursor new_cursor() {
        this._locale.enter();
        try {
            Cur c = tempCur();
            XmlCursor xc = new Cursor(c);
            c.release();
            this._locale.exit();
            return xc;
        } catch (Throwable th) {
            this._locale.exit();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public SchemaField get_schema_field() {
        if (!$assertionsDisabled && !isValid()) {
            throw new AssertionError();
        }
        if (isRoot()) {
            return null;
        }
        TypeStoreUser parentUser = ensureParent().getUser();
        if (isAttr()) {
            return parentUser.get_attribute_field(this._name);
        }
        if (!$assertionsDisabled && !isElem()) {
            throw new AssertionError();
        }
        TypeStoreVisitor visitor = parentUser.new_visitor();
        if (visitor == null) {
            return null;
        }
        Xobj xobj = this._parent._firstChild;
        while (true) {
            Xobj x = xobj;
            if (x.isElem()) {
                visitor.visit(x._name);
                if (x == this) {
                    return visitor.get_schema_field();
                }
            }
            xobj = x._nextSibling;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public void validate(ValidatorListener eventSink) {
        this._locale.enter();
        try {
            Cur c = tempCur();
            new Validate(c, eventSink);
            c.release();
            this._locale.exit();
        } catch (Throwable th) {
            this._locale.exit();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public TypeStoreUser change_type(SchemaType type) {
        this._locale.enter();
        try {
            Cur c = tempCur();
            c.setType(type, false);
            c.release();
            this._locale.exit();
            return getUser();
        } catch (Throwable th) {
            this._locale.exit();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public TypeStoreUser substitute(QName name, SchemaType type) {
        this._locale.enter();
        try {
            Cur c = tempCur();
            c.setSubstitution(name, type, false);
            c.release();
            this._locale.exit();
            return getUser();
        } catch (Throwable th) {
            this._locale.exit();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public QName get_xsi_type() {
        return getXsiTypeName();
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public void store_text(String text) {
        this._locale.enter();
        TypeStoreUser user = this._user;
        this._user = null;
        try {
            Cur c = tempCur();
            c.moveNodeContents(null, false);
            if (text != null && text.length() > 0) {
                c.next();
                c.insertString(text);
            }
            c.release();
            if (!$assertionsDisabled && this._user != null) {
                throw new AssertionError();
            }
            this._user = user;
            this._locale.exit();
        } catch (Throwable th) {
            if (!$assertionsDisabled && this._user != null) {
                throw new AssertionError();
            }
            this._user = user;
            this._locale.exit();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public int compute_flags() {
        if (isRoot()) {
            return 0;
        }
        TypeStoreUser parentUser = ensureParent().getUser();
        if (isAttr()) {
            return parentUser.get_attributeflags(this._name);
        }
        int f = parentUser.get_elementflags(this._name);
        if (f != -1) {
            return f;
        }
        TypeStoreVisitor visitor = parentUser.new_visitor();
        if (visitor == null) {
            return 0;
        }
        Xobj xobj = this._parent._firstChild;
        while (true) {
            Xobj x = xobj;
            if (x.isElem()) {
                visitor.visit(x._name);
                if (x == this) {
                    return visitor.get_elementflags();
                }
            }
            xobj = x._nextSibling;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public String compute_default_text() {
        if (isRoot()) {
            return null;
        }
        TypeStoreUser parentUser = ensureParent().getUser();
        if (isAttr()) {
            return parentUser.get_default_attribute_text(this._name);
        }
        String result = parentUser.get_default_element_text(this._name);
        if (result != null) {
            return result;
        }
        TypeStoreVisitor visitor = parentUser.new_visitor();
        if (visitor == null) {
            return null;
        }
        Xobj xobj = this._parent._firstChild;
        while (true) {
            Xobj x = xobj;
            if (x.isElem()) {
                visitor.visit(x._name);
                if (x == this) {
                    return visitor.get_default_text();
                }
            }
            xobj = x._nextSibling;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x003f  */
    @Override // org.apache.xmlbeans.impl.values.TypeStore
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean find_nil() {
        /*
            r3 = this;
            r0 = r3
            boolean r0 = r0.isAttr()
            if (r0 == 0) goto L9
            r0 = 0
            return r0
        L9:
            r0 = r3
            org.apache.xmlbeans.impl.store.Locale r0 = r0._locale
            r0.enter()
            r0 = r3
            javax.xml.namespace.QName r1 = org.apache.xmlbeans.impl.store.Locale._xsiNil     // Catch: java.lang.Throwable -> L4e
            org.apache.xmlbeans.impl.store.Xobj r0 = r0.getAttr(r1)     // Catch: java.lang.Throwable -> L4e
            r4 = r0
            r0 = r4
            if (r0 != 0) goto L27
            r0 = 0
            r5 = r0
            r0 = r3
            org.apache.xmlbeans.impl.store.Locale r0 = r0._locale
            r0.exit()
            r0 = r5
            return r0
        L27:
            r0 = r4
            r1 = 3
            java.lang.String r0 = r0.getValueAsString(r1)     // Catch: java.lang.Throwable -> L4e
            r5 = r0
            r0 = r5
            java.lang.String r1 = "true"
            boolean r0 = r0.equals(r1)     // Catch: java.lang.Throwable -> L4e
            if (r0 != 0) goto L3f
            r0 = r5
            java.lang.String r1 = "1"
            boolean r0 = r0.equals(r1)     // Catch: java.lang.Throwable -> L4e
            if (r0 == 0) goto L43
        L3f:
            r0 = 1
            goto L44
        L43:
            r0 = 0
        L44:
            r6 = r0
            r0 = r3
            org.apache.xmlbeans.impl.store.Locale r0 = r0._locale
            r0.exit()
            r0 = r6
            return r0
        L4e:
            r7 = move-exception
            r0 = r3
            org.apache.xmlbeans.impl.store.Locale r0 = r0._locale
            r0.exit()
            r0 = r7
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.store.Xobj.find_nil():boolean");
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public void invalidate_nil() {
        if (isAttr()) {
            return;
        }
        this._locale.enter();
        try {
            if (!this._user.build_nil()) {
                removeAttr(Locale._xsiNil);
            } else {
                setAttr(Locale._xsiNil, "true");
            }
        } finally {
            this._locale.exit();
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public int count_elements(QName name) {
        return this._locale.count(this, name, null);
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public int count_elements(QNameSet names) {
        return this._locale.count(this, null, names);
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public TypeStoreUser find_element_user(QName name, int i) {
        Xobj xobj = this._firstChild;
        while (true) {
            Xobj x = xobj;
            if (x != null) {
                if (x.isElem() && x._name.equals(name)) {
                    i--;
                    if (i < 0) {
                        return x.getUser();
                    }
                }
                xobj = x._nextSibling;
            } else {
                return null;
            }
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public TypeStoreUser find_element_user(QNameSet names, int i) {
        Xobj xobj = this._firstChild;
        while (true) {
            Xobj x = xobj;
            if (x != null) {
                if (x.isElem() && names.contains(x._name)) {
                    i--;
                    if (i < 0) {
                        return x.getUser();
                    }
                }
                xobj = x._nextSibling;
            } else {
                return null;
            }
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public void find_all_element_users(QName name, List fillMeUp) {
        Xobj xobj = this._firstChild;
        while (true) {
            Xobj x = xobj;
            if (x != null) {
                if (x.isElem() && x._name.equals(name)) {
                    fillMeUp.add(x.getUser());
                }
                xobj = x._nextSibling;
            } else {
                return;
            }
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public void find_all_element_users(QNameSet names, List fillMeUp) {
        Xobj xobj = this._firstChild;
        while (true) {
            Xobj x = xobj;
            if (x != null) {
                if (x.isElem() && names.contains(x._name)) {
                    fillMeUp.add(x.getUser());
                }
                xobj = x._nextSibling;
            } else {
                return;
            }
        }
    }

    private static TypeStoreUser insertElement(QName name, Xobj x, int pos) {
        x._locale.enter();
        try {
            Cur c = x._locale.tempCur();
            c.moveTo(x, pos);
            c.createElement(name);
            TypeStoreUser user = c.getUser();
            c.release();
            x._locale.exit();
            return user;
        } catch (Throwable th) {
            x._locale.exit();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public TypeStoreUser insert_element_user(QName name, int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (!isContainer()) {
            throw new IllegalStateException();
        }
        Xobj x = this._locale.findNthChildElem(this, name, null, i);
        if (x == null) {
            if (i > this._locale.count(this, name, null) + 1) {
                throw new IndexOutOfBoundsException();
            }
            return add_element_user(name);
        }
        return insertElement(name, x, 0);
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public TypeStoreUser insert_element_user(QNameSet names, QName name, int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (!isContainer()) {
            throw new IllegalStateException();
        }
        Xobj x = this._locale.findNthChildElem(this, null, names, i);
        if (x == null) {
            if (i > this._locale.count(this, null, names) + 1) {
                throw new IndexOutOfBoundsException();
            }
            return add_element_user(name);
        }
        return insertElement(name, x, 0);
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public TypeStoreUser add_element_user(QName name) {
        if (!isContainer()) {
            throw new IllegalStateException();
        }
        QNameSet endSet = null;
        boolean gotEndSet = false;
        Xobj candidate = null;
        Xobj xobj = this._lastChild;
        while (true) {
            Xobj x = xobj;
            if (x == null) {
                break;
            }
            if (x.isContainer()) {
                if (x._name.equals(name)) {
                    break;
                }
                if (!gotEndSet) {
                    endSet = this._user.get_element_ending_delimiters(name);
                    gotEndSet = true;
                }
                if (endSet == null || endSet.contains(x._name)) {
                    candidate = x;
                }
            }
            xobj = x._prevSibling;
        }
        return candidate == null ? insertElement(name, this, -1) : insertElement(name, candidate, 0);
    }

    private static void removeElement(Xobj x) {
        if (x == null) {
            throw new IndexOutOfBoundsException();
        }
        x._locale.enter();
        try {
            Cur c = x.tempCur();
            c.moveNode(null);
            c.release();
            x._locale.exit();
        } catch (Throwable th) {
            x._locale.exit();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public void remove_element(QName name, int i) {
        Xobj x;
        if (i < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (!isContainer()) {
            throw new IllegalStateException();
        }
        Xobj xobj = this._firstChild;
        while (true) {
            x = xobj;
            if (x == null) {
                break;
            }
            if (x.isElem() && x._name.equals(name)) {
                i--;
                if (i < 0) {
                    break;
                }
            }
            xobj = x._nextSibling;
        }
        removeElement(x);
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public void remove_element(QNameSet names, int i) {
        Xobj x;
        if (i < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (!isContainer()) {
            throw new IllegalStateException();
        }
        Xobj xobj = this._firstChild;
        while (true) {
            x = xobj;
            if (x == null) {
                break;
            }
            if (x.isElem() && names.contains(x._name)) {
                i--;
                if (i < 0) {
                    break;
                }
            }
            xobj = x._nextSibling;
        }
        removeElement(x);
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public TypeStoreUser find_attribute_user(QName name) {
        Xobj a = getAttr(name);
        if (a == null) {
            return null;
        }
        return a.getUser();
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public TypeStoreUser add_attribute_user(QName name) {
        if (getAttr(name) != null) {
            throw new IndexOutOfBoundsException();
        }
        this._locale.enter();
        try {
            TypeStoreUser user = setAttr(name, "").getUser();
            this._locale.exit();
            return user;
        } catch (Throwable th) {
            this._locale.exit();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public void remove_attribute(QName name) {
        this._locale.enter();
        try {
            if (!removeAttr(name)) {
                throw new IndexOutOfBoundsException();
            }
        } finally {
            this._locale.exit();
        }
    }

    /* JADX WARN: Finally extract failed */
    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public TypeStoreUser copy_contents_from(TypeStore source) {
        Xobj xSrc = (Xobj) source;
        if (xSrc == this) {
            return getUser();
        }
        this._locale.enter();
        try {
            xSrc._locale.enter();
            Cur c = tempCur();
            try {
                Cur cSrc1 = xSrc.tempCur();
                Map sourceNamespaces = Locale.getAllNamespaces(cSrc1, null);
                cSrc1.release();
                if (isAttr()) {
                    Cur cSrc = xSrc.tempCur();
                    String value = Locale.getTextValue(cSrc);
                    cSrc.release();
                    c.setValue(value);
                } else {
                    disconnectChildrenUsers();
                    if (!$assertionsDisabled && inhibitDisconnect()) {
                        throw new AssertionError();
                    }
                    setBit(1024);
                    QName xsiType = isContainer() ? getXsiTypeName() : null;
                    Xobj copy = xSrc.copyNode(this._locale);
                    Cur.moveNodeContents(this, null, true);
                    c.next();
                    Cur.moveNodeContents(copy, c, true);
                    c.moveTo(this);
                    if (xsiType != null) {
                        c.setXsiType(xsiType);
                    }
                    if (!$assertionsDisabled && !inhibitDisconnect()) {
                        throw new AssertionError();
                    }
                    clearBit(1024);
                }
                if (sourceNamespaces != null) {
                    if (!c.isContainer()) {
                        c.toParent();
                    }
                    Locale.applyNamespaces(c, sourceNamespaces);
                }
                c.release();
                xSrc._locale.exit();
                return getUser();
            } catch (Throwable th) {
                c.release();
                xSrc._locale.exit();
                throw th;
            }
        } finally {
            this._locale.exit();
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public TypeStoreUser copy(SchemaTypeLoader stl, SchemaType type, XmlOptions options) {
        Xobj destination;
        XmlOptions options2 = XmlOptions.maskNull(options);
        SchemaType sType = (SchemaType) options2.get(XmlOptions.DOCUMENT_TYPE);
        if (sType == null) {
            sType = type == null ? XmlObject.type : type;
        }
        Locale locale = locale();
        if (Boolean.TRUE.equals(options2.get("COPY_USE_NEW_LOCALE"))) {
            locale = Locale.getLocale(stl, options2);
        }
        if (sType.isDocumentType() || (sType.isNoType() && (this instanceof DocumentXobj))) {
            destination = Cur.createDomDocumentRootXobj(locale, false);
        } else {
            destination = Cur.createDomDocumentRootXobj(locale, true);
        }
        locale.enter();
        try {
            Cur c = destination.tempCur();
            c.setType(type);
            c.release();
            locale.exit();
            TypeStoreUser tsu = destination.copy_contents_from(this);
            return tsu;
        } catch (Throwable th) {
            locale.exit();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public void array_setter(XmlObject[] sources, QName elementName) {
        this._locale.enter();
        try {
            int m = sources.length;
            ArrayList copies = new ArrayList();
            ArrayList types = new ArrayList();
            for (int i = 0; i < m; i++) {
                if (sources[i] == null) {
                    throw new IllegalArgumentException("Array element null");
                }
                if (sources[i].isImmutable()) {
                    copies.add(null);
                    types.add(null);
                } else {
                    Xobj x = (Xobj) ((TypeStoreUser) sources[i]).get_store();
                    if (x._locale == this._locale) {
                        copies.add(x.copyNode(this._locale));
                    } else {
                        x._locale.enter();
                        try {
                            copies.add(x.copyNode(this._locale));
                            x._locale.exit();
                        } catch (Throwable th) {
                            x._locale.exit();
                            throw th;
                        }
                    }
                    types.add(sources[i].schemaType());
                }
            }
            int n = count_elements(elementName);
            while (n > m) {
                remove_element(elementName, m);
                n--;
            }
            while (m > n) {
                add_element_user(elementName);
                n++;
            }
            if (!$assertionsDisabled && m != n) {
                throw new AssertionError();
            }
            ArrayList elements = new ArrayList();
            find_all_element_users(elementName, elements);
            for (int i2 = 0; i2 < elements.size(); i2++) {
                elements.set(i2, (Xobj) ((TypeStoreUser) elements.get(i2)).get_store());
            }
            if (!$assertionsDisabled && elements.size() != n) {
                throw new AssertionError();
            }
            Cur c = tempCur();
            for (int i3 = 0; i3 < n; i3++) {
                Xobj x2 = (Xobj) elements.get(i3);
                if (sources[i3].isImmutable()) {
                    x2.getObject().set(sources[i3]);
                } else {
                    Cur.moveNodeContents(x2, null, true);
                    c.moveTo(x2);
                    c.next();
                    Cur.moveNodeContents((Xobj) copies.get(i3), c, true);
                    x2.change_type((SchemaType) types.get(i3));
                }
            }
            c.release();
            this._locale.exit();
        } catch (Throwable th2) {
            this._locale.exit();
            throw th2;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public void visit_elements(TypeStoreVisitor visitor) {
        throw new RuntimeException("Not implemeneted");
    }

    @Override // org.apache.xmlbeans.impl.values.TypeStore
    public XmlObject[] exec_query(String queryExpr, XmlOptions options) throws XmlException {
        this._locale.enter();
        try {
            Cur c = tempCur();
            XmlObject[] result = Query.objectExecQuery(c, queryExpr, options);
            c.release();
            this._locale.exit();
            return result;
        } catch (Throwable th) {
            this._locale.exit();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.NamespaceManager
    public String find_prefix_for_nsuri(String nsuri, String suggested_prefix) {
        this._locale.enter();
        try {
            String strPrefixForNamespace = prefixForNamespace(nsuri, suggested_prefix, true);
            this._locale.exit();
            return strPrefixForNamespace;
        } catch (Throwable th) {
            this._locale.exit();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.impl.common.PrefixResolver
    public String getNamespaceForPrefix(String prefix) {
        return namespaceForPrefix(prefix, true);
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$NodeXobj.class */
    static abstract class NodeXobj extends Xobj implements DomImpl.Dom, Node, NodeList {
        NodeXobj(Locale l, int kind, int domType) {
            super(l, kind, domType);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj
        DomImpl.Dom getDom() {
            return this;
        }

        public int getLength() {
            return DomImpl._childNodes_getLength(this);
        }

        @Override // org.w3c.dom.NodeList
        public Node item(int i) {
            return DomImpl._childNodes_item(this, i);
        }

        @Override // org.w3c.dom.Node
        public Node appendChild(Node newChild) {
            return DomImpl._node_appendChild(this, newChild);
        }

        @Override // org.w3c.dom.Node
        public Node cloneNode(boolean deep) {
            return DomImpl._node_cloneNode(this, deep);
        }

        public NamedNodeMap getAttributes() {
            return null;
        }

        public NodeList getChildNodes() {
            return this;
        }

        @Override // org.w3c.dom.Node
        public Node getParentNode() {
            return DomImpl._node_getParentNode(this);
        }

        @Override // org.w3c.dom.Node
        public Node removeChild(Node oldChild) {
            return DomImpl._node_removeChild(this, oldChild);
        }

        public Node getFirstChild() {
            return DomImpl._node_getFirstChild(this);
        }

        @Override // org.w3c.dom.Node
        public Node getLastChild() {
            return DomImpl._node_getLastChild(this);
        }

        @Override // org.w3c.dom.Node
        public String getLocalName() {
            return DomImpl._node_getLocalName(this);
        }

        @Override // org.w3c.dom.Node
        public String getNamespaceURI() {
            return DomImpl._node_getNamespaceURI(this);
        }

        public Node getNextSibling() {
            return DomImpl._node_getNextSibling(this);
        }

        @Override // org.w3c.dom.Node
        public String getNodeName() {
            return DomImpl._node_getNodeName(this);
        }

        @Override // org.w3c.dom.Node
        public short getNodeType() {
            return DomImpl._node_getNodeType(this);
        }

        @Override // org.w3c.dom.Node
        public String getNodeValue() {
            return DomImpl._node_getNodeValue(this);
        }

        @Override // org.w3c.dom.Node
        public Document getOwnerDocument() {
            return DomImpl._node_getOwnerDocument(this);
        }

        @Override // org.w3c.dom.Node
        public String getPrefix() {
            return DomImpl._node_getPrefix(this);
        }

        @Override // org.w3c.dom.Node
        public Node getPreviousSibling() {
            return DomImpl._node_getPreviousSibling(this);
        }

        @Override // org.w3c.dom.Node
        public boolean hasAttributes() {
            return DomImpl._node_hasAttributes(this);
        }

        @Override // org.w3c.dom.Node
        public boolean hasChildNodes() {
            return DomImpl._node_hasChildNodes(this);
        }

        @Override // org.w3c.dom.Node
        public Node insertBefore(Node newChild, Node refChild) {
            return DomImpl._node_insertBefore(this, newChild, refChild);
        }

        @Override // org.w3c.dom.Node
        public boolean isSupported(String feature, String version) {
            return DomImpl._node_isSupported(this, feature, version);
        }

        @Override // org.w3c.dom.Node
        public void normalize() {
            DomImpl._node_normalize(this);
        }

        @Override // org.w3c.dom.Node
        public Node replaceChild(Node newChild, Node oldChild) {
            return DomImpl._node_replaceChild(this, newChild, oldChild);
        }

        @Override // org.w3c.dom.Node
        public void setNodeValue(String nodeValue) {
            DomImpl._node_setNodeValue(this, nodeValue);
        }

        @Override // org.w3c.dom.Node
        public void setPrefix(String prefix) {
            DomImpl._node_setPrefix(this, prefix);
        }

        public boolean nodeCanHavePrefixUri() {
            return false;
        }

        @Override // org.w3c.dom.Node
        public Object getUserData(String key) {
            return DomImpl._node_getUserData(this, key);
        }

        @Override // org.w3c.dom.Node
        public Object setUserData(String key, Object data, UserDataHandler handler) {
            return DomImpl._node_setUserData(this, key, data, handler);
        }

        @Override // org.w3c.dom.Node
        public Object getFeature(String feature, String version) {
            return DomImpl._node_getFeature(this, feature, version);
        }

        @Override // org.w3c.dom.Node
        public boolean isEqualNode(Node arg) {
            return DomImpl._node_isEqualNode(this, arg);
        }

        @Override // org.w3c.dom.Node
        public boolean isSameNode(Node arg) {
            return DomImpl._node_isSameNode(this, arg);
        }

        @Override // org.w3c.dom.Node
        public String lookupNamespaceURI(String prefix) {
            return DomImpl._node_lookupNamespaceURI(this, prefix);
        }

        @Override // org.w3c.dom.Node
        public String lookupPrefix(String namespaceURI) {
            return DomImpl._node_lookupPrefix(this, namespaceURI);
        }

        @Override // org.w3c.dom.Node
        public boolean isDefaultNamespace(String namespaceURI) {
            return DomImpl._node_isDefaultNamespace(this, namespaceURI);
        }

        @Override // org.w3c.dom.Node
        public void setTextContent(String textContent) {
            DomImpl._node_setTextContent(this, textContent);
        }

        @Override // org.w3c.dom.Node
        public String getTextContent() {
            return DomImpl._node_getTextContent(this);
        }

        @Override // org.w3c.dom.Node
        public short compareDocumentPosition(Node other) {
            return DomImpl._node_compareDocumentPosition(this, other);
        }

        @Override // org.w3c.dom.Node
        public String getBaseURI() {
            return DomImpl._node_getBaseURI(this);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$DocumentXobj.class */
    static class DocumentXobj extends NodeXobj implements Document {
        private Hashtable _idToElement;

        DocumentXobj(Locale l) {
            super(l, 1, 9);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj
        Xobj newNode(Locale l) {
            return new DocumentXobj(l);
        }

        @Override // org.w3c.dom.Document
        public Attr createAttribute(String name) {
            return DomImpl._document_createAttribute(this, name);
        }

        @Override // org.w3c.dom.Document
        public Attr createAttributeNS(String namespaceURI, String qualifiedName) {
            return DomImpl._document_createAttributeNS(this, namespaceURI, qualifiedName);
        }

        @Override // org.w3c.dom.Document
        public CDATASection createCDATASection(String data) {
            return DomImpl._document_createCDATASection(this, data);
        }

        @Override // org.w3c.dom.Document
        public Comment createComment(String data) {
            return DomImpl._document_createComment(this, data);
        }

        @Override // org.w3c.dom.Document
        public DocumentFragment createDocumentFragment() {
            return DomImpl._document_createDocumentFragment(this);
        }

        @Override // org.w3c.dom.Document
        public Element createElement(String tagName) {
            return DomImpl._document_createElement(this, tagName);
        }

        @Override // org.w3c.dom.Document
        public Element createElementNS(String namespaceURI, String qualifiedName) {
            return DomImpl._document_createElementNS(this, namespaceURI, qualifiedName);
        }

        @Override // org.w3c.dom.Document
        public EntityReference createEntityReference(String name) {
            return DomImpl._document_createEntityReference(this, name);
        }

        @Override // org.w3c.dom.Document
        public ProcessingInstruction createProcessingInstruction(String target, String data) {
            return DomImpl._document_createProcessingInstruction(this, target, data);
        }

        @Override // org.w3c.dom.Document
        public Text createTextNode(String data) {
            return DomImpl._document_createTextNode(this, data);
        }

        @Override // org.w3c.dom.Document
        public DocumentType getDoctype() {
            return DomImpl._document_getDoctype(this);
        }

        @Override // org.w3c.dom.Document
        public Element getDocumentElement() {
            return DomImpl._document_getDocumentElement(this);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.w3c.dom.Document
        public Element getElementById(String elementId) {
            Xobj xobj;
            if (this._idToElement == null || (xobj = (Xobj) this._idToElement.get(elementId)) == 0) {
                return null;
            }
            if (!isInSameTree(xobj)) {
                this._idToElement.remove(elementId);
            }
            return (Element) xobj;
        }

        @Override // org.w3c.dom.Document
        public NodeList getElementsByTagName(String tagname) {
            return DomImpl._document_getElementsByTagName(this, tagname);
        }

        @Override // org.w3c.dom.Document
        public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
            return DomImpl._document_getElementsByTagNameNS(this, namespaceURI, localName);
        }

        @Override // org.w3c.dom.Document
        public DOMImplementation getImplementation() {
            return DomImpl._document_getImplementation(this);
        }

        @Override // org.w3c.dom.Document
        public Node importNode(Node importedNode, boolean deep) {
            return DomImpl._document_importNode(this, importedNode, deep);
        }

        @Override // org.w3c.dom.Document
        public Node adoptNode(Node source) {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public String getDocumentURI() {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public DOMConfiguration getDomConfig() {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public String getInputEncoding() {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public boolean getStrictErrorChecking() {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public String getXmlEncoding() {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public boolean getXmlStandalone() {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public String getXmlVersion() {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public void normalizeDocument() {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public Node renameNode(Node n, String namespaceURI, String qualifiedName) {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public void setDocumentURI(String documentURI) {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public void setStrictErrorChecking(boolean strictErrorChecking) {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public void setXmlStandalone(boolean xmlStandalone) {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public void setXmlVersion(String xmlVersion) {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        protected void addIdElement(String idVal, DomImpl.Dom e) {
            if (this._idToElement == null) {
                this._idToElement = new Hashtable();
            }
            this._idToElement.put(idVal, e);
        }

        void removeIdElement(String idVal) {
            if (this._idToElement != null) {
                this._idToElement.remove(idVal);
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$DocumentFragXobj.class */
    static class DocumentFragXobj extends NodeXobj implements DocumentFragment {
        DocumentFragXobj(Locale l) {
            super(l, 1, 11);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj
        Xobj newNode(Locale l) {
            return new DocumentFragXobj(l);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$ElementAttributes.class */
    static final class ElementAttributes implements NamedNodeMap {
        private ElementXobj _elementXobj;

        ElementAttributes(ElementXobj elementXobj) {
            this._elementXobj = elementXobj;
        }

        @Override // org.w3c.dom.NamedNodeMap
        public int getLength() {
            return DomImpl._attributes_getLength(this._elementXobj);
        }

        @Override // org.w3c.dom.NamedNodeMap
        public Node getNamedItem(String name) {
            return DomImpl._attributes_getNamedItem(this._elementXobj, name);
        }

        @Override // org.w3c.dom.NamedNodeMap
        public Node getNamedItemNS(String namespaceURI, String localName) {
            return DomImpl._attributes_getNamedItemNS(this._elementXobj, namespaceURI, localName);
        }

        @Override // org.w3c.dom.NamedNodeMap
        public Node item(int index) {
            return DomImpl._attributes_item(this._elementXobj, index);
        }

        @Override // org.w3c.dom.NamedNodeMap
        public Node removeNamedItem(String name) {
            return DomImpl._attributes_removeNamedItem(this._elementXobj, name);
        }

        @Override // org.w3c.dom.NamedNodeMap
        public Node removeNamedItemNS(String namespaceURI, String localName) {
            return DomImpl._attributes_removeNamedItemNS(this._elementXobj, namespaceURI, localName);
        }

        @Override // org.w3c.dom.NamedNodeMap
        public Node setNamedItem(Node arg) {
            return DomImpl._attributes_setNamedItem(this._elementXobj, arg);
        }

        @Override // org.w3c.dom.NamedNodeMap
        public Node setNamedItemNS(Node arg) {
            return DomImpl._attributes_setNamedItemNS(this._elementXobj, arg);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$NamedNodeXobj.class */
    static abstract class NamedNodeXobj extends NodeXobj {
        boolean _canHavePrefixUri;

        NamedNodeXobj(Locale l, int kind, int domType) {
            super(l, kind, domType);
            this._canHavePrefixUri = true;
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.NodeXobj, org.apache.xmlbeans.impl.store.DomImpl.Dom
        public boolean nodeCanHavePrefixUri() {
            return this._canHavePrefixUri;
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$ElementXobj.class */
    static class ElementXobj extends NamedNodeXobj implements Element {
        private ElementAttributes _attributes;

        ElementXobj(Locale l, QName name) {
            super(l, 2, 1);
            this._name = name;
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj
        Xobj newNode(Locale l) {
            return new ElementXobj(l, this._name);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.NodeXobj, org.w3c.dom.Node
        public NamedNodeMap getAttributes() {
            if (this._attributes == null) {
                this._attributes = new ElementAttributes(this);
            }
            return this._attributes;
        }

        @Override // org.w3c.dom.Element
        public String getAttribute(String name) {
            return DomImpl._element_getAttribute(this, name);
        }

        @Override // org.w3c.dom.Element
        public Attr getAttributeNode(String name) {
            return DomImpl._element_getAttributeNode(this, name);
        }

        @Override // org.w3c.dom.Element
        public Attr getAttributeNodeNS(String namespaceURI, String localName) {
            return DomImpl._element_getAttributeNodeNS(this, namespaceURI, localName);
        }

        @Override // org.w3c.dom.Element
        public String getAttributeNS(String namespaceURI, String localName) {
            return DomImpl._element_getAttributeNS(this, namespaceURI, localName);
        }

        @Override // org.w3c.dom.Element
        public NodeList getElementsByTagName(String name) {
            return DomImpl._element_getElementsByTagName(this, name);
        }

        @Override // org.w3c.dom.Element
        public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
            return DomImpl._element_getElementsByTagNameNS(this, namespaceURI, localName);
        }

        @Override // org.w3c.dom.Element
        public String getTagName() {
            return DomImpl._element_getTagName(this);
        }

        @Override // org.w3c.dom.Element
        public boolean hasAttribute(String name) {
            return DomImpl._element_hasAttribute(this, name);
        }

        @Override // org.w3c.dom.Element
        public boolean hasAttributeNS(String namespaceURI, String localName) {
            return DomImpl._element_hasAttributeNS(this, namespaceURI, localName);
        }

        @Override // org.w3c.dom.Element
        public void removeAttribute(String name) {
            DomImpl._element_removeAttribute(this, name);
        }

        @Override // org.w3c.dom.Element
        public Attr removeAttributeNode(Attr oldAttr) {
            return DomImpl._element_removeAttributeNode(this, oldAttr);
        }

        @Override // org.w3c.dom.Element
        public void removeAttributeNS(String namespaceURI, String localName) {
            DomImpl._element_removeAttributeNS(this, namespaceURI, localName);
        }

        @Override // org.w3c.dom.Element
        public void setAttribute(String name, String value) {
            DomImpl._element_setAttribute(this, name, value);
        }

        @Override // org.w3c.dom.Element
        public Attr setAttributeNode(Attr newAttr) {
            return DomImpl._element_setAttributeNode(this, newAttr);
        }

        @Override // org.w3c.dom.Element
        public Attr setAttributeNodeNS(Attr newAttr) {
            return DomImpl._element_setAttributeNodeNS(this, newAttr);
        }

        @Override // org.w3c.dom.Element
        public void setAttributeNS(String namespaceURI, String qualifiedName, String value) {
            DomImpl._element_setAttributeNS(this, namespaceURI, qualifiedName, value);
        }

        @Override // org.w3c.dom.Element
        public TypeInfo getSchemaTypeInfo() {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Element
        public void setIdAttribute(String name, boolean isId) {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Element
        public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Element
        public void setIdAttributeNode(Attr idAttr, boolean isId) {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$AttrXobj.class */
    static class AttrXobj extends NamedNodeXobj implements Attr {
        AttrXobj(Locale l, QName name) {
            super(l, 3, 2);
            this._name = name;
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj
        Xobj newNode(Locale l) {
            return new AttrXobj(l, this._name);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.NodeXobj, org.w3c.dom.Node
        public Node getNextSibling() {
            return null;
        }

        @Override // org.w3c.dom.Attr
        public String getName() {
            return DomImpl._node_getNodeName(this);
        }

        @Override // org.w3c.dom.Attr
        public Element getOwnerElement() {
            return DomImpl._attr_getOwnerElement(this);
        }

        @Override // org.w3c.dom.Attr
        public boolean getSpecified() {
            return DomImpl._attr_getSpecified(this);
        }

        @Override // org.w3c.dom.Attr
        public String getValue() {
            return DomImpl._node_getNodeValue(this);
        }

        @Override // org.w3c.dom.Attr
        public void setValue(String value) {
            DomImpl._node_setNodeValue(this, value);
        }

        @Override // org.w3c.dom.Attr
        public TypeInfo getSchemaTypeInfo() {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        public boolean isId() {
            return false;
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$AttrIdXobj.class */
    static class AttrIdXobj extends AttrXobj {
        AttrIdXobj(Locale l, QName name) {
            super(l, name);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.AttrXobj, org.w3c.dom.Attr
        public boolean isId() {
            return true;
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$CommentXobj.class */
    static class CommentXobj extends NodeXobj implements Comment {
        CommentXobj(Locale l) {
            super(l, 4, 8);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj
        Xobj newNode(Locale l) {
            return new CommentXobj(l);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.NodeXobj, org.w3c.dom.Node
        public NodeList getChildNodes() {
            return DomImpl._emptyNodeList;
        }

        @Override // org.w3c.dom.CharacterData
        public void appendData(String arg) {
            DomImpl._characterData_appendData(this, arg);
        }

        @Override // org.w3c.dom.CharacterData
        public void deleteData(int offset, int count) {
            DomImpl._characterData_deleteData(this, offset, count);
        }

        @Override // org.w3c.dom.CharacterData
        public String getData() {
            return DomImpl._characterData_getData(this);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.NodeXobj, org.w3c.dom.NodeList, org.w3c.dom.CharacterData
        public int getLength() {
            return DomImpl._characterData_getLength(this);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.NodeXobj, org.w3c.dom.Node
        public Node getFirstChild() {
            return null;
        }

        @Override // org.w3c.dom.CharacterData
        public void insertData(int offset, String arg) {
            DomImpl._characterData_insertData(this, offset, arg);
        }

        @Override // org.w3c.dom.CharacterData
        public void replaceData(int offset, int count, String arg) {
            DomImpl._characterData_replaceData(this, offset, count, arg);
        }

        @Override // org.w3c.dom.CharacterData
        public void setData(String data) {
            DomImpl._characterData_setData(this, data);
        }

        @Override // org.w3c.dom.CharacterData
        public String substringData(int offset, int count) {
            return DomImpl._characterData_substringData(this, offset, count);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$ProcInstXobj.class */
    static class ProcInstXobj extends NodeXobj implements ProcessingInstruction {
        ProcInstXobj(Locale l, String target) {
            super(l, 5, 7);
            this._name = this._locale.makeQName(null, target);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj
        Xobj newNode(Locale l) {
            return new ProcInstXobj(l, this._name.getLocalPart());
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.NodeXobj, org.w3c.dom.NodeList, org.w3c.dom.CharacterData
        public int getLength() {
            return 0;
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.NodeXobj, org.w3c.dom.Node
        public Node getFirstChild() {
            return null;
        }

        @Override // org.w3c.dom.ProcessingInstruction
        public String getData() {
            return DomImpl._processingInstruction_getData(this);
        }

        @Override // org.w3c.dom.ProcessingInstruction
        public String getTarget() {
            return DomImpl._processingInstruction_getTarget(this);
        }

        @Override // org.w3c.dom.ProcessingInstruction
        public void setData(String data) {
            DomImpl._processingInstruction_setData(this, data);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$SoapPartDocXobj.class */
    static class SoapPartDocXobj extends DocumentXobj {
        SoapPartDom _soapPartDom;

        SoapPartDocXobj(Locale l) {
            super(l);
            this._soapPartDom = new SoapPartDom(this);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.NodeXobj, org.apache.xmlbeans.impl.store.Xobj
        DomImpl.Dom getDom() {
            return this._soapPartDom;
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.DocumentXobj, org.apache.xmlbeans.impl.store.Xobj
        Xobj newNode(Locale l) {
            return new SoapPartDocXobj(l);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$SoapPartDom.class */
    static class SoapPartDom extends SOAPPart implements DomImpl.Dom, Document, NodeList {
        SoapPartDocXobj _docXobj;

        SoapPartDom(SoapPartDocXobj docXobj) {
            this._docXobj = docXobj;
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.Dom
        public int nodeType() {
            return 9;
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.Dom
        public Locale locale() {
            return this._docXobj._locale;
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.Dom
        public Cur tempCur() {
            return this._docXobj.tempCur();
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.Dom
        public QName getQName() {
            return this._docXobj._name;
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.Dom
        public void dump() {
            dump(System.out);
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.Dom
        public void dump(PrintStream o) {
            this._docXobj.dump(o);
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.Dom
        public void dump(PrintStream o, Object ref) {
            this._docXobj.dump(o, ref);
        }

        public String name() {
            return "#document";
        }

        @Override // org.w3c.dom.Node
        public Node appendChild(Node newChild) {
            return DomImpl._node_appendChild(this, newChild);
        }

        @Override // org.w3c.dom.Node
        public Node cloneNode(boolean deep) {
            return DomImpl._node_cloneNode(this, deep);
        }

        @Override // org.w3c.dom.Node
        public NamedNodeMap getAttributes() {
            return null;
        }

        @Override // org.w3c.dom.Node
        public NodeList getChildNodes() {
            return this;
        }

        @Override // org.w3c.dom.Node
        public Node getParentNode() {
            return DomImpl._node_getParentNode(this);
        }

        @Override // org.w3c.dom.Node
        public Node removeChild(Node oldChild) {
            return DomImpl._node_removeChild(this, oldChild);
        }

        @Override // org.w3c.dom.Node
        public Node getFirstChild() {
            return DomImpl._node_getFirstChild(this);
        }

        @Override // org.w3c.dom.Node
        public Node getLastChild() {
            return DomImpl._node_getLastChild(this);
        }

        @Override // org.w3c.dom.Node
        public String getLocalName() {
            return DomImpl._node_getLocalName(this);
        }

        @Override // org.w3c.dom.Node
        public String getNamespaceURI() {
            return DomImpl._node_getNamespaceURI(this);
        }

        @Override // org.w3c.dom.Node
        public Node getNextSibling() {
            return DomImpl._node_getNextSibling(this);
        }

        @Override // org.w3c.dom.Node
        public String getNodeName() {
            return DomImpl._node_getNodeName(this);
        }

        @Override // org.w3c.dom.Node
        public short getNodeType() {
            return DomImpl._node_getNodeType(this);
        }

        @Override // org.w3c.dom.Node
        public String getNodeValue() {
            return DomImpl._node_getNodeValue(this);
        }

        @Override // org.w3c.dom.Node
        public Document getOwnerDocument() {
            return DomImpl._node_getOwnerDocument(this);
        }

        @Override // org.w3c.dom.Node
        public String getPrefix() {
            return DomImpl._node_getPrefix(this);
        }

        @Override // org.w3c.dom.Node
        public Node getPreviousSibling() {
            return DomImpl._node_getPreviousSibling(this);
        }

        @Override // org.w3c.dom.Node
        public boolean hasAttributes() {
            return DomImpl._node_hasAttributes(this);
        }

        @Override // org.w3c.dom.Node
        public boolean hasChildNodes() {
            return DomImpl._node_hasChildNodes(this);
        }

        @Override // org.w3c.dom.Node
        public Node insertBefore(Node newChild, Node refChild) {
            return DomImpl._node_insertBefore(this, newChild, refChild);
        }

        @Override // org.w3c.dom.Node
        public boolean isSupported(String feature, String version) {
            return DomImpl._node_isSupported(this, feature, version);
        }

        @Override // org.w3c.dom.Node
        public void normalize() {
            DomImpl._node_normalize(this);
        }

        @Override // org.w3c.dom.Node
        public Node replaceChild(Node newChild, Node oldChild) {
            return DomImpl._node_replaceChild(this, newChild, oldChild);
        }

        @Override // org.w3c.dom.Node
        public void setNodeValue(String nodeValue) {
            DomImpl._node_setNodeValue(this, nodeValue);
        }

        @Override // org.w3c.dom.Node
        public void setPrefix(String prefix) {
            DomImpl._node_setPrefix(this, prefix);
        }

        @Override // org.w3c.dom.Node
        public Object getUserData(String key) {
            return DomImpl._node_getUserData(this, key);
        }

        @Override // org.w3c.dom.Node
        public Object setUserData(String key, Object data, UserDataHandler handler) {
            return DomImpl._node_setUserData(this, key, data, handler);
        }

        @Override // org.w3c.dom.Node
        public Object getFeature(String feature, String version) {
            return DomImpl._node_getFeature(this, feature, version);
        }

        @Override // org.w3c.dom.Node
        public boolean isEqualNode(Node arg) {
            return DomImpl._node_isEqualNode(this, arg);
        }

        @Override // org.w3c.dom.Node
        public boolean isSameNode(Node arg) {
            return DomImpl._node_isSameNode(this, arg);
        }

        @Override // org.w3c.dom.Node
        public String lookupNamespaceURI(String prefix) {
            return DomImpl._node_lookupNamespaceURI(this, prefix);
        }

        @Override // org.w3c.dom.Node
        public String lookupPrefix(String namespaceURI) {
            return DomImpl._node_lookupPrefix(this, namespaceURI);
        }

        @Override // org.w3c.dom.Node
        public boolean isDefaultNamespace(String namespaceURI) {
            return DomImpl._node_isDefaultNamespace(this, namespaceURI);
        }

        @Override // org.w3c.dom.Node
        public void setTextContent(String textContent) {
            DomImpl._node_setTextContent(this, textContent);
        }

        @Override // org.w3c.dom.Node
        public String getTextContent() {
            return DomImpl._node_getTextContent(this);
        }

        @Override // org.w3c.dom.Node
        public short compareDocumentPosition(Node other) {
            return DomImpl._node_compareDocumentPosition(this, other);
        }

        @Override // org.w3c.dom.Node
        public String getBaseURI() {
            return DomImpl._node_getBaseURI(this);
        }

        @Override // org.w3c.dom.Document
        public Node adoptNode(Node source) {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public String getDocumentURI() {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public DOMConfiguration getDomConfig() {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public String getInputEncoding() {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public boolean getStrictErrorChecking() {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public String getXmlEncoding() {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public boolean getXmlStandalone() {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public String getXmlVersion() {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public void normalizeDocument() {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public Node renameNode(Node n, String namespaceURI, String qualifiedName) {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public void setDocumentURI(String documentURI) {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public void setStrictErrorChecking(boolean strictErrorChecking) {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public void setXmlStandalone(boolean xmlStandalone) {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public void setXmlVersion(String xmlVersion) {
            throw new RuntimeException("DOM Level 3 Not implemented");
        }

        @Override // org.w3c.dom.Document
        public Attr createAttribute(String name) {
            return DomImpl._document_createAttribute(this, name);
        }

        @Override // org.w3c.dom.Document
        public Attr createAttributeNS(String namespaceURI, String qualifiedName) {
            return DomImpl._document_createAttributeNS(this, namespaceURI, qualifiedName);
        }

        @Override // org.w3c.dom.Document
        public CDATASection createCDATASection(String data) {
            return DomImpl._document_createCDATASection(this, data);
        }

        @Override // org.w3c.dom.Document
        public Comment createComment(String data) {
            return DomImpl._document_createComment(this, data);
        }

        @Override // org.w3c.dom.Document
        public DocumentFragment createDocumentFragment() {
            return DomImpl._document_createDocumentFragment(this);
        }

        @Override // org.w3c.dom.Document
        public Element createElement(String tagName) {
            return DomImpl._document_createElement(this, tagName);
        }

        @Override // org.w3c.dom.Document
        public Element createElementNS(String namespaceURI, String qualifiedName) {
            return DomImpl._document_createElementNS(this, namespaceURI, qualifiedName);
        }

        @Override // org.w3c.dom.Document
        public EntityReference createEntityReference(String name) {
            return DomImpl._document_createEntityReference(this, name);
        }

        @Override // org.w3c.dom.Document
        public ProcessingInstruction createProcessingInstruction(String target, String data) {
            return DomImpl._document_createProcessingInstruction(this, target, data);
        }

        @Override // org.w3c.dom.Document
        public Text createTextNode(String data) {
            return DomImpl._document_createTextNode(this, data);
        }

        @Override // org.w3c.dom.Document
        public DocumentType getDoctype() {
            return DomImpl._document_getDoctype(this);
        }

        @Override // org.w3c.dom.Document
        public Element getDocumentElement() {
            return DomImpl._document_getDocumentElement(this);
        }

        @Override // org.w3c.dom.Document
        public Element getElementById(String elementId) {
            return DomImpl._document_getElementById(this, elementId);
        }

        @Override // org.w3c.dom.Document
        public NodeList getElementsByTagName(String tagname) {
            return DomImpl._document_getElementsByTagName(this, tagname);
        }

        @Override // org.w3c.dom.Document
        public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
            return DomImpl._document_getElementsByTagNameNS(this, namespaceURI, localName);
        }

        @Override // org.w3c.dom.Document
        public DOMImplementation getImplementation() {
            return DomImpl._document_getImplementation(this);
        }

        @Override // org.w3c.dom.Document
        public Node importNode(Node importedNode, boolean deep) {
            return DomImpl._document_importNode(this, importedNode, deep);
        }

        @Override // org.w3c.dom.NodeList
        public int getLength() {
            return DomImpl._childNodes_getLength(this);
        }

        @Override // org.w3c.dom.NodeList
        public Node item(int i) {
            return DomImpl._childNodes_item(this, i);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPPart
        public void removeAllMimeHeaders() {
            DomImpl._soapPart_removeAllMimeHeaders(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPPart
        public void removeMimeHeader(String name) {
            DomImpl._soapPart_removeMimeHeader(this, name);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPPart
        public Iterator getAllMimeHeaders() {
            return DomImpl._soapPart_getAllMimeHeaders(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPPart
        public SOAPEnvelope getEnvelope() {
            return DomImpl._soapPart_getEnvelope(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPPart
        public Source getContent() {
            return DomImpl._soapPart_getContent(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPPart
        public void setContent(Source source) {
            DomImpl._soapPart_setContent(this, source);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPPart
        public String[] getMimeHeader(String name) {
            return DomImpl._soapPart_getMimeHeader(this, name);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPPart
        public void addMimeHeader(String name, String value) {
            DomImpl._soapPart_addMimeHeader(this, name, value);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPPart
        public void setMimeHeader(String name, String value) {
            DomImpl._soapPart_setMimeHeader(this, name, value);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPPart
        public Iterator getMatchingMimeHeaders(String[] names) {
            return DomImpl._soapPart_getMatchingMimeHeaders(this, names);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPPart
        public Iterator getNonMatchingMimeHeaders(String[] names) {
            return DomImpl._soapPart_getNonMatchingMimeHeaders(this, names);
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.Dom
        public boolean nodeCanHavePrefixUri() {
            return true;
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$SoapElementXobj.class */
    static class SoapElementXobj extends ElementXobj implements SOAPElement, org.apache.xmlbeans.impl.soap.Node {
        SoapElementXobj(Locale l, QName name) {
            super(l, name);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.ElementXobj, org.apache.xmlbeans.impl.store.Xobj
        Xobj newNode(Locale l) {
            return new SoapElementXobj(l, this._name);
        }

        @Override // org.apache.xmlbeans.impl.soap.Node
        public void detachNode() {
            DomImpl._soapNode_detachNode(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.Node
        public void recycleNode() {
            DomImpl._soapNode_recycleNode(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.Node
        public String getValue() {
            return DomImpl._soapNode_getValue(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.Node
        public void setValue(String value) {
            DomImpl._soapNode_setValue(this, value);
        }

        @Override // org.apache.xmlbeans.impl.soap.Node
        public SOAPElement getParentElement() {
            return DomImpl._soapNode_getParentElement(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.Node
        public void setParentElement(SOAPElement p) {
            DomImpl._soapNode_setParentElement(this, p);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public void removeContents() {
            DomImpl._soapElement_removeContents(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public String getEncodingStyle() {
            return DomImpl._soapElement_getEncodingStyle(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public void setEncodingStyle(String encodingStyle) {
            DomImpl._soapElement_setEncodingStyle(this, encodingStyle);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public boolean removeNamespaceDeclaration(String prefix) {
            return DomImpl._soapElement_removeNamespaceDeclaration(this, prefix);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public Iterator getAllAttributes() {
            return DomImpl._soapElement_getAllAttributes(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public Iterator getChildElements() {
            return DomImpl._soapElement_getChildElements(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public Iterator getNamespacePrefixes() {
            return DomImpl._soapElement_getNamespacePrefixes(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public SOAPElement addAttribute(Name name, String value) throws SOAPException {
            return DomImpl._soapElement_addAttribute(this, name, value);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public SOAPElement addChildElement(SOAPElement oldChild) throws SOAPException {
            return DomImpl._soapElement_addChildElement(this, oldChild);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public SOAPElement addChildElement(Name name) throws SOAPException {
            return DomImpl._soapElement_addChildElement(this, name);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public SOAPElement addChildElement(String localName) throws SOAPException {
            return DomImpl._soapElement_addChildElement(this, localName);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public SOAPElement addChildElement(String localName, String prefix) throws SOAPException {
            return DomImpl._soapElement_addChildElement(this, localName, prefix);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public SOAPElement addChildElement(String localName, String prefix, String uri) throws SOAPException {
            return DomImpl._soapElement_addChildElement(this, localName, prefix, uri);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public SOAPElement addNamespaceDeclaration(String prefix, String uri) {
            return DomImpl._soapElement_addNamespaceDeclaration(this, prefix, uri);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public SOAPElement addTextNode(String data) {
            return DomImpl._soapElement_addTextNode(this, data);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public String getAttributeValue(Name name) {
            return DomImpl._soapElement_getAttributeValue(this, name);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public Iterator getChildElements(Name name) {
            return DomImpl._soapElement_getChildElements(this, name);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public Name getElementName() {
            return DomImpl._soapElement_getElementName(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public String getNamespaceURI(String prefix) {
            return DomImpl._soapElement_getNamespaceURI(this, prefix);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public Iterator getVisibleNamespacePrefixes() {
            return DomImpl._soapElement_getVisibleNamespacePrefixes(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPElement
        public boolean removeAttribute(Name name) {
            return DomImpl._soapElement_removeAttribute(this, name);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$SoapBodyXobj.class */
    static class SoapBodyXobj extends SoapElementXobj implements SOAPBody {
        SoapBodyXobj(Locale l, QName name) {
            super(l, name);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.SoapElementXobj, org.apache.xmlbeans.impl.store.Xobj.ElementXobj, org.apache.xmlbeans.impl.store.Xobj
        Xobj newNode(Locale l) {
            return new SoapBodyXobj(l, this._name);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPBody
        public boolean hasFault() {
            return DomImpl.soapBody_hasFault(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPBody
        public SOAPFault addFault() throws SOAPException {
            return DomImpl.soapBody_addFault(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPBody
        public SOAPFault getFault() {
            return DomImpl.soapBody_getFault(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPBody
        public SOAPBodyElement addBodyElement(Name name) {
            return DomImpl.soapBody_addBodyElement(this, name);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPBody
        public SOAPBodyElement addDocument(Document document) {
            return DomImpl.soapBody_addDocument(this, document);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPBody
        public SOAPFault addFault(Name name, String s) throws SOAPException {
            return DomImpl.soapBody_addFault(this, name, s);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPBody
        public SOAPFault addFault(Name faultCode, String faultString, java.util.Locale locale) throws SOAPException {
            return DomImpl.soapBody_addFault(this, faultCode, faultString, locale);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$SoapBodyElementXobj.class */
    static class SoapBodyElementXobj extends SoapElementXobj implements SOAPBodyElement {
        SoapBodyElementXobj(Locale l, QName name) {
            super(l, name);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.SoapElementXobj, org.apache.xmlbeans.impl.store.Xobj.ElementXobj, org.apache.xmlbeans.impl.store.Xobj
        Xobj newNode(Locale l) {
            return new SoapBodyElementXobj(l, this._name);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$SoapEnvelopeXobj.class */
    static class SoapEnvelopeXobj extends SoapElementXobj implements SOAPEnvelope {
        SoapEnvelopeXobj(Locale l, QName name) {
            super(l, name);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.SoapElementXobj, org.apache.xmlbeans.impl.store.Xobj.ElementXobj, org.apache.xmlbeans.impl.store.Xobj
        Xobj newNode(Locale l) {
            return new SoapEnvelopeXobj(l, this._name);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPEnvelope
        public SOAPBody addBody() throws SOAPException {
            return DomImpl._soapEnvelope_addBody(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPEnvelope
        public SOAPBody getBody() throws SOAPException {
            return DomImpl._soapEnvelope_getBody(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPEnvelope
        public SOAPHeader getHeader() throws SOAPException {
            return DomImpl._soapEnvelope_getHeader(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPEnvelope
        public SOAPHeader addHeader() throws SOAPException {
            return DomImpl._soapEnvelope_addHeader(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPEnvelope
        public Name createName(String localName) {
            return DomImpl._soapEnvelope_createName(this, localName);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPEnvelope
        public Name createName(String localName, String prefix, String namespaceURI) {
            return DomImpl._soapEnvelope_createName(this, localName, prefix, namespaceURI);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$SoapHeaderXobj.class */
    static class SoapHeaderXobj extends SoapElementXobj implements SOAPHeader {
        SoapHeaderXobj(Locale l, QName name) {
            super(l, name);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.SoapElementXobj, org.apache.xmlbeans.impl.store.Xobj.ElementXobj, org.apache.xmlbeans.impl.store.Xobj
        Xobj newNode(Locale l) {
            return new SoapHeaderXobj(l, this._name);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPHeader
        public Iterator examineAllHeaderElements() {
            return DomImpl.soapHeader_examineAllHeaderElements(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPHeader
        public Iterator extractAllHeaderElements() {
            return DomImpl.soapHeader_extractAllHeaderElements(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPHeader
        public Iterator examineHeaderElements(String actor) {
            return DomImpl.soapHeader_examineHeaderElements(this, actor);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPHeader
        public Iterator examineMustUnderstandHeaderElements(String mustUnderstandString) {
            return DomImpl.soapHeader_examineMustUnderstandHeaderElements(this, mustUnderstandString);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPHeader
        public Iterator extractHeaderElements(String actor) {
            return DomImpl.soapHeader_extractHeaderElements(this, actor);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPHeader
        public SOAPHeaderElement addHeaderElement(Name name) {
            return DomImpl.soapHeader_addHeaderElement(this, name);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$SoapHeaderElementXobj.class */
    static class SoapHeaderElementXobj extends SoapElementXobj implements SOAPHeaderElement {
        SoapHeaderElementXobj(Locale l, QName name) {
            super(l, name);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.SoapElementXobj, org.apache.xmlbeans.impl.store.Xobj.ElementXobj, org.apache.xmlbeans.impl.store.Xobj
        Xobj newNode(Locale l) {
            return new SoapHeaderElementXobj(l, this._name);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPHeaderElement
        public void setMustUnderstand(boolean mustUnderstand) {
            DomImpl.soapHeaderElement_setMustUnderstand(this, mustUnderstand);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPHeaderElement
        public boolean getMustUnderstand() {
            return DomImpl.soapHeaderElement_getMustUnderstand(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPHeaderElement
        public void setActor(String actor) {
            DomImpl.soapHeaderElement_setActor(this, actor);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPHeaderElement
        public String getActor() {
            return DomImpl.soapHeaderElement_getActor(this);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$SoapFaultXobj.class */
    static class SoapFaultXobj extends SoapBodyElementXobj implements SOAPFault {
        SoapFaultXobj(Locale l, QName name) {
            super(l, name);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.SoapBodyElementXobj, org.apache.xmlbeans.impl.store.Xobj.SoapElementXobj, org.apache.xmlbeans.impl.store.Xobj.ElementXobj, org.apache.xmlbeans.impl.store.Xobj
        Xobj newNode(Locale l) {
            return new SoapFaultXobj(l, this._name);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPFault
        public void setFaultString(String faultString) {
            DomImpl.soapFault_setFaultString(this, faultString);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPFault
        public void setFaultString(String faultString, java.util.Locale locale) {
            DomImpl.soapFault_setFaultString(this, faultString, locale);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPFault
        public void setFaultCode(Name faultCodeName) throws SOAPException {
            DomImpl.soapFault_setFaultCode(this, faultCodeName);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPFault
        public void setFaultActor(String faultActorString) {
            DomImpl.soapFault_setFaultActor(this, faultActorString);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPFault
        public String getFaultActor() {
            return DomImpl.soapFault_getFaultActor(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPFault
        public String getFaultCode() {
            return DomImpl.soapFault_getFaultCode(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPFault
        public void setFaultCode(String faultCode) throws SOAPException {
            DomImpl.soapFault_setFaultCode(this, faultCode);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPFault
        public java.util.Locale getFaultStringLocale() {
            return DomImpl.soapFault_getFaultStringLocale(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPFault
        public Name getFaultCodeAsName() {
            return DomImpl.soapFault_getFaultCodeAsName(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPFault
        public String getFaultString() {
            return DomImpl.soapFault_getFaultString(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPFault
        public Detail addDetail() throws SOAPException {
            return DomImpl.soapFault_addDetail(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.SOAPFault
        public Detail getDetail() {
            return DomImpl.soapFault_getDetail(this);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$SoapFaultElementXobj.class */
    static class SoapFaultElementXobj extends SoapElementXobj implements SOAPFaultElement {
        SoapFaultElementXobj(Locale l, QName name) {
            super(l, name);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.SoapElementXobj, org.apache.xmlbeans.impl.store.Xobj.ElementXobj, org.apache.xmlbeans.impl.store.Xobj
        Xobj newNode(Locale l) {
            return new SoapFaultElementXobj(l, this._name);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$DetailXobj.class */
    static class DetailXobj extends SoapFaultElementXobj implements Detail {
        DetailXobj(Locale l, QName name) {
            super(l, name);
        }

        @Override // org.apache.xmlbeans.impl.store.Xobj.SoapFaultElementXobj, org.apache.xmlbeans.impl.store.Xobj.SoapElementXobj, org.apache.xmlbeans.impl.store.Xobj.ElementXobj, org.apache.xmlbeans.impl.store.Xobj
        Xobj newNode(Locale l) {
            return new DetailXobj(l, this._name);
        }

        @Override // org.apache.xmlbeans.impl.soap.Detail
        public DetailEntry addDetailEntry(Name name) {
            return DomImpl.detail_addDetailEntry(this, name);
        }

        @Override // org.apache.xmlbeans.impl.soap.Detail
        public Iterator getDetailEntries() {
            return DomImpl.detail_getDetailEntries(this);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$DetailEntryXobj.class */
    static class DetailEntryXobj extends SoapElementXobj implements DetailEntry {
        @Override // org.apache.xmlbeans.impl.store.Xobj.SoapElementXobj, org.apache.xmlbeans.impl.store.Xobj.ElementXobj, org.apache.xmlbeans.impl.store.Xobj
        Xobj newNode(Locale l) {
            return new DetailEntryXobj(l, this._name);
        }

        DetailEntryXobj(Locale l, QName name) {
            super(l, name);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Xobj$Bookmark.class */
    static class Bookmark implements XmlCursor.XmlMark {
        Xobj _xobj;
        int _pos;
        Bookmark _next;
        Bookmark _prev;
        Object _key;
        Object _value;
        static final /* synthetic */ boolean $assertionsDisabled;

        Bookmark() {
        }

        static {
            $assertionsDisabled = !Xobj.class.desiredAssertionStatus();
        }

        boolean isOnList(Bookmark head) {
            while (head != null) {
                if (head != this) {
                    head = head._next;
                } else {
                    return true;
                }
            }
            return false;
        }

        Bookmark listInsert(Bookmark head) {
            if (!$assertionsDisabled && (this._next != null || this._prev != null)) {
                throw new AssertionError();
            }
            if (head == null) {
                this._prev = this;
                head = this;
            } else {
                this._prev = head._prev;
                head._prev._next = this;
                head._prev = this;
            }
            return head;
        }

        Bookmark listRemove(Bookmark head) {
            if (!$assertionsDisabled && (this._prev == null || !isOnList(head))) {
                throw new AssertionError();
            }
            if (this._prev == this) {
                head = null;
            } else {
                if (head == this) {
                    head = this._next;
                } else {
                    this._prev._next = this._next;
                }
                if (this._next == null) {
                    head._prev = this._prev;
                } else {
                    this._next._prev = this._prev;
                    this._next = null;
                }
            }
            this._prev = null;
            if ($assertionsDisabled || this._next == null) {
                return head;
            }
            throw new AssertionError();
        }

        void moveTo(Xobj x, int p) {
            if (!$assertionsDisabled && !isOnList(this._xobj._bookmarks)) {
                throw new AssertionError();
            }
            if (this._xobj != x) {
                this._xobj._bookmarks = listRemove(this._xobj._bookmarks);
                x._bookmarks = listInsert(x._bookmarks);
                this._xobj = x;
            }
            this._pos = p;
        }

        @Override // org.apache.xmlbeans.XmlCursor.XmlMark
        public XmlCursor createCursor() {
            if (this._xobj == null) {
                throw new IllegalStateException("Attempting to create a cursor on a bookmark that has been cleared or replaced.");
            }
            return Cursor.newCursor(this._xobj, this._pos);
        }
    }
}
