package org.apache.xmlbeans.impl.store;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.PrintStream;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.CDataBookmark;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDocumentProperties;
import org.apache.xmlbeans.XmlLineNumber;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.soap.Detail;
import org.apache.xmlbeans.impl.soap.DetailEntry;
import org.apache.xmlbeans.impl.soap.SOAPBody;
import org.apache.xmlbeans.impl.soap.SOAPBodyElement;
import org.apache.xmlbeans.impl.soap.SOAPElement;
import org.apache.xmlbeans.impl.soap.SOAPEnvelope;
import org.apache.xmlbeans.impl.soap.SOAPFault;
import org.apache.xmlbeans.impl.soap.SOAPFaultElement;
import org.apache.xmlbeans.impl.soap.SOAPHeader;
import org.apache.xmlbeans.impl.soap.SOAPHeaderElement;
import org.apache.xmlbeans.impl.store.DomImpl;
import org.apache.xmlbeans.impl.store.Locale;
import org.apache.xmlbeans.impl.store.Xobj;
import org.apache.xmlbeans.impl.values.TypeStoreUser;
import org.springframework.beans.PropertyAccessor;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Cur.class */
final class Cur {
    static final int TEXT = 0;
    static final int ROOT = 1;
    static final int ELEM = 2;
    static final int ATTR = 3;
    static final int COMMENT = 4;
    static final int PROCINST = 5;
    static final int POOLED = 0;
    static final int REGISTERED = 1;
    static final int EMBEDDED = 2;
    static final int DISPOSED = 3;
    static final int END_POS = -1;
    static final int NO_POS = -2;
    static final String LOAD_USE_LOCALE_CHAR_UTIL = "LOAD_USE_LOCALE_CHAR_UTIL";
    Locale _locale;
    Xobj _xobj;
    String _id;
    Cur _nextTemp;
    Cur _prevTemp;
    Cur _next;
    Cur _prev;
    Locale.Ref _ref;
    private int _posTemp;
    int _offSrc;
    int _cchSrc;
    static final /* synthetic */ boolean $assertionsDisabled;
    int _pos = -2;
    int _tempFrame = -1;
    int _state = 0;
    int _stackTop = -1;
    int _selectionFirst = -1;
    int _selectionN = -1;
    int _selectionLoc = -1;
    int _selectionCount = 0;

    static {
        $assertionsDisabled = !Cur.class.desiredAssertionStatus();
    }

    Cur(Locale l) {
        this._locale = l;
    }

    boolean isPositioned() {
        if ($assertionsDisabled || isNormal()) {
            return this._xobj != null;
        }
        throw new AssertionError();
    }

    static boolean kindIsContainer(int k) {
        return k == 2 || k == 1;
    }

    static boolean kindIsFinish(int k) {
        return k == -2 || k == -1;
    }

    int kind() {
        if (!$assertionsDisabled && !isPositioned()) {
            throw new AssertionError();
        }
        int kind = this._xobj.kind();
        if (this._pos == 0) {
            return kind;
        }
        if (this._pos == -1) {
            return -kind;
        }
        return 0;
    }

    boolean isRoot() {
        if ($assertionsDisabled || isPositioned()) {
            return this._pos == 0 && this._xobj.kind() == 1;
        }
        throw new AssertionError();
    }

    boolean isElem() {
        if ($assertionsDisabled || isPositioned()) {
            return this._pos == 0 && this._xobj.kind() == 2;
        }
        throw new AssertionError();
    }

    boolean isAttr() {
        if ($assertionsDisabled || isPositioned()) {
            return this._pos == 0 && this._xobj.kind() == 3;
        }
        throw new AssertionError();
    }

    boolean isComment() {
        if ($assertionsDisabled || isPositioned()) {
            return this._pos == 0 && this._xobj.kind() == 4;
        }
        throw new AssertionError();
    }

    boolean isProcinst() {
        if ($assertionsDisabled || isPositioned()) {
            return this._pos == 0 && this._xobj.kind() == 5;
        }
        throw new AssertionError();
    }

    boolean isText() {
        if ($assertionsDisabled || isPositioned()) {
            return this._pos > 0;
        }
        throw new AssertionError();
    }

    boolean isEnd() {
        if ($assertionsDisabled || isPositioned()) {
            return this._pos == -1 && this._xobj.kind() == 2;
        }
        throw new AssertionError();
    }

    boolean isEndRoot() {
        if ($assertionsDisabled || isPositioned()) {
            return this._pos == -1 && this._xobj.kind() == 1;
        }
        throw new AssertionError();
    }

    boolean isNode() {
        if ($assertionsDisabled || isPositioned()) {
            return this._pos == 0;
        }
        throw new AssertionError();
    }

    boolean isContainer() {
        if ($assertionsDisabled || isPositioned()) {
            return this._pos == 0 && kindIsContainer(this._xobj.kind());
        }
        throw new AssertionError();
    }

    boolean isFinish() {
        if ($assertionsDisabled || isPositioned()) {
            return this._pos == -1 && kindIsContainer(this._xobj.kind());
        }
        throw new AssertionError();
    }

    boolean isUserNode() {
        if (!$assertionsDisabled && !isPositioned()) {
            throw new AssertionError();
        }
        int k = kind();
        return k == 2 || k == 1 || (k == 3 && !isXmlns());
    }

    boolean isContainerOrFinish() {
        if (!$assertionsDisabled && !isPositioned()) {
            throw new AssertionError();
        }
        if (this._pos != 0 && this._pos != -1) {
            return false;
        }
        int kind = this._xobj.kind();
        return kind == 2 || kind == -2 || kind == 1 || kind == -1;
    }

    boolean isNormalAttr() {
        return isNode() && this._xobj.isNormalAttr();
    }

    boolean isXmlns() {
        return isNode() && this._xobj.isXmlns();
    }

    boolean isTextCData() {
        return this._xobj.hasBookmark(CDataBookmark.class, this._pos);
    }

    QName getName() {
        if ($assertionsDisabled || isNode() || isEnd()) {
            return this._xobj._name;
        }
        throw new AssertionError();
    }

    String getLocal() {
        return getName().getLocalPart();
    }

    String getUri() {
        return getName().getNamespaceURI();
    }

    String getXmlnsPrefix() {
        if ($assertionsDisabled || isXmlns()) {
            return this._xobj.getXmlnsPrefix();
        }
        throw new AssertionError();
    }

    String getXmlnsUri() {
        if ($assertionsDisabled || isXmlns()) {
            return this._xobj.getXmlnsUri();
        }
        throw new AssertionError();
    }

    boolean isDomDocRoot() {
        return isRoot() && (this._xobj.getDom() instanceof Document);
    }

    boolean isDomFragRoot() {
        return isRoot() && (this._xobj.getDom() instanceof DocumentFragment);
    }

    int cchRight() {
        if ($assertionsDisabled || isPositioned()) {
            return this._xobj.cchRight(this._pos);
        }
        throw new AssertionError();
    }

    int cchLeft() {
        if ($assertionsDisabled || isPositioned()) {
            return this._xobj.cchLeft(this._pos);
        }
        throw new AssertionError();
    }

    void createRoot() {
        createDomDocFragRoot();
    }

    void createDomDocFragRoot() {
        moveTo(new Xobj.DocumentFragXobj(this._locale));
    }

    void createDomDocumentRoot() {
        moveTo(createDomDocumentRootXobj(this._locale));
    }

    void createAttr(QName name) {
        createHelper(new Xobj.AttrXobj(this._locale, name));
    }

    void createComment() {
        createHelper(new Xobj.CommentXobj(this._locale));
    }

    void createProcinst(String target) {
        createHelper(new Xobj.ProcInstXobj(this._locale, target));
    }

    void createElement(QName name) {
        createElement(name, null);
    }

    void createElement(QName name, QName parentName) {
        createHelper(createElementXobj(this._locale, name, parentName));
    }

    static Xobj createDomDocumentRootXobj(Locale l) {
        return createDomDocumentRootXobj(l, false);
    }

    static Xobj createDomDocumentRootXobj(Locale l, boolean fragment) {
        Xobj xo;
        if (l._saaj == null) {
            if (fragment) {
                xo = new Xobj.DocumentFragXobj(l);
            } else {
                xo = new Xobj.DocumentXobj(l);
            }
        } else {
            xo = new Xobj.SoapPartDocXobj(l);
        }
        if (l._ownerDoc == null) {
            l._ownerDoc = xo.getDom();
        }
        return xo;
    }

    static Xobj createElementXobj(Locale l, QName name, QName parentName) {
        if (l._saaj == null) {
            return new Xobj.ElementXobj(l, name);
        }
        Class c = l._saaj.identifyElement(name, parentName);
        if (c == SOAPElement.class) {
            return new Xobj.SoapElementXobj(l, name);
        }
        if (c == SOAPBody.class) {
            return new Xobj.SoapBodyXobj(l, name);
        }
        if (c == SOAPBodyElement.class) {
            return new Xobj.SoapBodyElementXobj(l, name);
        }
        if (c == SOAPEnvelope.class) {
            return new Xobj.SoapEnvelopeXobj(l, name);
        }
        if (c == SOAPHeader.class) {
            return new Xobj.SoapHeaderXobj(l, name);
        }
        if (c == SOAPHeaderElement.class) {
            return new Xobj.SoapHeaderElementXobj(l, name);
        }
        if (c == SOAPFaultElement.class) {
            return new Xobj.SoapFaultElementXobj(l, name);
        }
        if (c == Detail.class) {
            return new Xobj.DetailXobj(l, name);
        }
        if (c == DetailEntry.class) {
            return new Xobj.DetailEntryXobj(l, name);
        }
        if (c == SOAPFault.class) {
            return new Xobj.SoapFaultXobj(l, name);
        }
        throw new IllegalStateException("Unknown SAAJ element class: " + c);
    }

    private void createHelper(Xobj x) {
        if (!$assertionsDisabled && x._locale != this._locale) {
            throw new AssertionError();
        }
        if (isPositioned()) {
            Cur from = tempCur(x, 0);
            from.moveNode(this);
            from.release();
        }
        moveTo(x);
    }

    boolean isSamePos(Cur that) {
        if ($assertionsDisabled || (isNormal() && (that == null || that.isNormal()))) {
            return this._xobj == that._xobj && this._pos == that._pos;
        }
        throw new AssertionError();
    }

    boolean isJustAfterEnd(Cur that) {
        if ($assertionsDisabled || (isNormal() && that != null && that.isNormal() && that.isNode())) {
            return that._xobj.isJustAfterEnd(this._xobj, this._pos);
        }
        throw new AssertionError();
    }

    boolean isJustAfterEnd(Xobj x) {
        return x.isJustAfterEnd(this._xobj, this._pos);
    }

    boolean isAtEndOf(Cur that) {
        if ($assertionsDisabled || (that != null && that.isNormal() && that.isNode())) {
            return this._xobj == that._xobj && this._pos == -1;
        }
        throw new AssertionError();
    }

    boolean isInSameTree(Cur that) {
        if ($assertionsDisabled || (isPositioned() && that.isPositioned())) {
            return this._xobj.isInSameTree(that._xobj);
        }
        throw new AssertionError();
    }

    int comparePosition(Cur that) {
        if (!$assertionsDisabled && (!isPositioned() || !that.isPositioned())) {
            throw new AssertionError();
        }
        if (this._locale != that._locale) {
            return 2;
        }
        Xobj xThis = this._xobj;
        int pThis = this._pos == -1 ? xThis.posAfter() - 1 : this._pos;
        Xobj xThat = that._xobj;
        int pThat = that._pos == -1 ? xThat.posAfter() - 1 : that._pos;
        if (xThis == xThat) {
            if (pThis < pThat) {
                return -1;
            }
            return pThis == pThat ? 0 : 1;
        }
        int dThis = 0;
        Xobj xobj = xThis._parent;
        while (true) {
            Xobj x = xobj;
            if (x != null) {
                dThis++;
                if (x == xThat) {
                    return pThat < xThat.posAfter() - 1 ? 1 : -1;
                }
                xobj = x._parent;
            } else {
                int dThat = 0;
                Xobj xobj2 = xThat._parent;
                while (true) {
                    Xobj x2 = xobj2;
                    if (x2 != null) {
                        dThat++;
                        if (x2 == xThis) {
                            return pThis < xThis.posAfter() - 1 ? -1 : 1;
                        }
                        xobj2 = x2._parent;
                    } else {
                        while (dThis > dThat) {
                            dThis--;
                            xThis = xThis._parent;
                        }
                        while (dThat > dThis) {
                            dThat--;
                            xThat = xThat._parent;
                        }
                        if (!$assertionsDisabled && dThat != dThis) {
                            throw new AssertionError();
                        }
                        if (dThat == 0) {
                            return 2;
                        }
                        if (!$assertionsDisabled && (xThis._parent == null || xThat._parent == null)) {
                            throw new AssertionError();
                        }
                        while (xThis._parent != xThat._parent) {
                            Xobj xobj3 = xThis._parent;
                            xThis = xobj3;
                            if (xobj3 == null) {
                                return 2;
                            }
                            xThat = xThat._parent;
                        }
                        if (xThis._prevSibling == null || xThat._nextSibling == null) {
                            return -1;
                        }
                        if (xThis._nextSibling == null || xThat._prevSibling == null) {
                            return 1;
                        }
                        while (xThis != null) {
                            Xobj xobj4 = xThis._prevSibling;
                            xThis = xobj4;
                            if (xobj4 == xThat) {
                                return 1;
                            }
                        }
                        return -1;
                    }
                }
            }
        }
    }

    void setName(QName newName) {
        if (!$assertionsDisabled && (!isNode() || newName == null)) {
            throw new AssertionError();
        }
        this._xobj.setName(newName);
    }

    void moveTo(Xobj x) {
        moveTo(x, 0);
    }

    void moveTo(Xobj x, int p) {
        if (!$assertionsDisabled && x != null && this._locale != x._locale) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && x == null && p != -2) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && x != null && !x.isNormal(p) && (!x.isVacant() || x._cchValue != 0 || x._user != null)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this._state != 1 && this._state != 2) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this._state != 2 && this._xobj != null && isOnList(this._xobj._embedded)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this._state != 1 && (this._xobj == null || !isOnList(this._xobj._embedded))) {
            throw new AssertionError();
        }
        moveToNoCheck(x, p);
        if ($assertionsDisabled || isNormal()) {
            return;
        }
        if (!this._xobj.isVacant() || this._xobj._cchValue != 0 || this._xobj._user != null) {
            throw new AssertionError();
        }
    }

    void moveToNoCheck(Xobj x, int p) {
        if (this._state == 2 && x != this._xobj) {
            this._xobj._embedded = listRemove(this._xobj._embedded);
            this._locale._registered = listInsert(this._locale._registered);
            this._state = 1;
        }
        this._xobj = x;
        this._pos = p;
    }

    void moveToCur(Cur to) {
        if (!$assertionsDisabled && (!isNormal() || (to != null && !to.isNormal()))) {
            throw new AssertionError();
        }
        if (to == null) {
            moveTo(null, -2);
        } else {
            moveTo(to._xobj, to._pos);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    void moveToDom(DomImpl.Dom dom) {
        if (!$assertionsDisabled && this._locale != dom.locale()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !(dom instanceof Xobj) && !(dom instanceof Xobj.SoapPartDom)) {
            throw new AssertionError();
        }
        moveTo(dom instanceof Xobj ? (Xobj) dom : ((Xobj.SoapPartDom) dom)._docXobj);
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Cur$Locations.class */
    static final class Locations {
        private static final int NULL = -1;
        private static final int _initialSize = 32;
        private Locale _locale;
        private Xobj[] _xobjs = new Xobj[32];
        private int[] _poses = new int[32];
        private Cur[] _curs = new Cur[32];
        private int[] _next = new int[32];
        private int[] _prev = new int[32];
        private int[] _nextN = new int[32];
        private int[] _prevN = new int[32];
        private int _free;
        private int _naked;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Cur.class.desiredAssertionStatus();
        }

        Locations(Locale l) {
            this._locale = l;
            for (int i = 31; i >= 0; i--) {
                if (!$assertionsDisabled && this._xobjs[i] != null) {
                    throw new AssertionError();
                }
                this._poses[i] = -2;
                this._next[i] = i + 1;
                this._prev[i] = -1;
                this._nextN[i] = -1;
                this._prevN[i] = -1;
            }
            this._next[31] = -1;
            this._free = 0;
            this._naked = -1;
        }

        boolean isSamePos(int i, Cur c) {
            if (this._curs[i] == null) {
                return c._xobj == this._xobjs[i] && c._pos == this._poses[i];
            }
            return c.isSamePos(this._curs[i]);
        }

        boolean isAtEndOf(int i, Cur c) {
            if (!$assertionsDisabled && this._curs[i] == null && this._poses[i] != 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this._curs[i] != null && !this._curs[i].isNode()) {
                throw new AssertionError();
            }
            if (this._curs[i] == null) {
                return c._xobj == this._xobjs[i] && c._pos == -1;
            }
            return c.isAtEndOf(this._curs[i]);
        }

        void moveTo(int i, Cur c) {
            if (this._curs[i] == null) {
                c.moveTo(this._xobjs[i], this._poses[i]);
            } else {
                c.moveToCur(this._curs[i]);
            }
        }

        int insert(int head, int before, int i) {
            return insert(head, before, i, this._next, this._prev);
        }

        int remove(int head, int i) {
            Cur c = this._curs[i];
            if (!$assertionsDisabled && c == null && this._xobjs[i] == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && c == null && this._xobjs[i] == null) {
                throw new AssertionError();
            }
            if (c != null) {
                this._curs[i].release();
                this._curs[i] = null;
                if (!$assertionsDisabled && this._xobjs[i] != null) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && this._poses[i] != -2) {
                    throw new AssertionError();
                }
            } else {
                if (!$assertionsDisabled && (this._xobjs[i] == null || this._poses[i] == -2)) {
                    throw new AssertionError();
                }
                this._xobjs[i] = null;
                this._poses[i] = -2;
                this._naked = remove(this._naked, i, this._nextN, this._prevN);
            }
            int head2 = remove(head, i, this._next, this._prev);
            this._next[i] = this._free;
            this._free = i;
            return head2;
        }

        int allocate(Cur addThis) {
            if (!$assertionsDisabled && !addThis.isPositioned()) {
                throw new AssertionError();
            }
            if (this._free == -1) {
                makeRoom();
            }
            int i = this._free;
            this._free = this._next[i];
            this._next[i] = -1;
            if (!$assertionsDisabled && this._prev[i] != -1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this._curs[i] != null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this._xobjs[i] != null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this._poses[i] != -2) {
                throw new AssertionError();
            }
            this._xobjs[i] = addThis._xobj;
            this._poses[i] = addThis._pos;
            this._naked = insert(this._naked, -1, i, this._nextN, this._prevN);
            return i;
        }

        private static int insert(int head, int before, int i, int[] next, int[] prev) {
            if (head == -1) {
                if (!$assertionsDisabled && before != -1) {
                    throw new AssertionError();
                }
                prev[i] = i;
                head = i;
            } else if (before != -1) {
                prev[i] = prev[before];
                next[i] = before;
                prev[before] = i;
                if (head == before) {
                    head = i;
                }
            } else {
                prev[i] = prev[head];
                if (!$assertionsDisabled && next[i] != -1) {
                    throw new AssertionError();
                }
                next[prev[head]] = i;
                prev[head] = i;
            }
            return head;
        }

        private static int remove(int head, int i, int[] next, int[] prev) {
            if (prev[i] == i) {
                if (!$assertionsDisabled && head != i) {
                    throw new AssertionError();
                }
                head = -1;
            } else {
                if (head == i) {
                    head = next[i];
                } else {
                    next[prev[i]] = next[i];
                }
                if (next[i] == -1) {
                    prev[head] = prev[i];
                } else {
                    prev[next[i]] = prev[i];
                    next[i] = -1;
                }
            }
            prev[i] = -1;
            if ($assertionsDisabled || next[i] == -1) {
                return head;
            }
            throw new AssertionError();
        }

        void notifyChange() {
            while (true) {
                int i = this._naked;
                if (i != -1) {
                    if (!$assertionsDisabled && (this._curs[i] != null || this._xobjs[i] == null || this._poses[i] == -2)) {
                        break;
                    }
                    this._naked = remove(this._naked, i, this._nextN, this._prevN);
                    this._curs[i] = this._locale.getCur();
                    this._curs[i].moveTo(this._xobjs[i], this._poses[i]);
                    this._xobjs[i] = null;
                    this._poses[i] = -2;
                } else {
                    return;
                }
            }
            throw new AssertionError();
        }

        int next(int i) {
            return this._next[i];
        }

        int prev(int i) {
            return this._prev[i];
        }

        private void makeRoom() {
            if (!$assertionsDisabled && this._free != -1) {
                throw new AssertionError();
            }
            int l = this._xobjs.length;
            Xobj[] oldXobjs = this._xobjs;
            int[] oldPoses = this._poses;
            Cur[] oldCurs = this._curs;
            int[] oldNext = this._next;
            int[] oldPrev = this._prev;
            int[] oldNextN = this._nextN;
            int[] oldPrevN = this._prevN;
            this._xobjs = new Xobj[l * 2];
            this._poses = new int[l * 2];
            this._curs = new Cur[l * 2];
            this._next = new int[l * 2];
            this._prev = new int[l * 2];
            this._nextN = new int[l * 2];
            this._prevN = new int[l * 2];
            System.arraycopy(oldXobjs, 0, this._xobjs, 0, l);
            System.arraycopy(oldPoses, 0, this._poses, 0, l);
            System.arraycopy(oldCurs, 0, this._curs, 0, l);
            System.arraycopy(oldNext, 0, this._next, 0, l);
            System.arraycopy(oldPrev, 0, this._prev, 0, l);
            System.arraycopy(oldNextN, 0, this._nextN, 0, l);
            System.arraycopy(oldPrevN, 0, this._prevN, 0, l);
            for (int i = (l * 2) - 1; i >= l; i--) {
                this._next[i] = i + 1;
                this._prev[i] = -1;
                this._nextN[i] = -1;
                this._prevN[i] = -1;
                this._poses[i] = -2;
            }
            this._next[(l * 2) - 1] = -1;
            this._free = l;
        }
    }

    void push() {
        if (!$assertionsDisabled && !isPositioned()) {
            throw new AssertionError();
        }
        int i = this._locale._locations.allocate(this);
        this._stackTop = this._locale._locations.insert(this._stackTop, this._stackTop, i);
    }

    void pop(boolean stay) {
        if (stay) {
            popButStay();
        } else {
            pop();
        }
    }

    void popButStay() {
        if (this._stackTop != -1) {
            this._stackTop = this._locale._locations.remove(this._stackTop, this._stackTop);
        }
    }

    boolean pop() {
        if (this._stackTop == -1) {
            return false;
        }
        this._locale._locations.moveTo(this._stackTop, this);
        this._stackTop = this._locale._locations.remove(this._stackTop, this._stackTop);
        return true;
    }

    boolean isAtLastPush() {
        if ($assertionsDisabled || this._stackTop != -1) {
            return this._locale._locations.isSamePos(this._stackTop, this);
        }
        throw new AssertionError();
    }

    boolean isAtEndOfLastPush() {
        if ($assertionsDisabled || this._stackTop != -1) {
            return this._locale._locations.isAtEndOf(this._stackTop, this);
        }
        throw new AssertionError();
    }

    void addToSelection(Cur that) {
        if (!$assertionsDisabled && (that == null || !that.isNormal())) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (!isPositioned() || !that.isPositioned())) {
            throw new AssertionError();
        }
        int i = this._locale._locations.allocate(that);
        this._selectionFirst = this._locale._locations.insert(this._selectionFirst, -1, i);
        this._selectionCount++;
    }

    void addToSelection() {
        if (!$assertionsDisabled && !isPositioned()) {
            throw new AssertionError();
        }
        int i = this._locale._locations.allocate(this);
        this._selectionFirst = this._locale._locations.insert(this._selectionFirst, -1, i);
        this._selectionCount++;
    }

    private int selectionIndex(int i) {
        if (!$assertionsDisabled && (this._selectionN < -1 || i < 0 || i >= this._selectionCount)) {
            throw new AssertionError();
        }
        if (this._selectionN == -1) {
            this._selectionN = 0;
            this._selectionLoc = this._selectionFirst;
        }
        while (this._selectionN < i) {
            this._selectionLoc = this._locale._locations.next(this._selectionLoc);
            this._selectionN++;
        }
        while (this._selectionN > i) {
            this._selectionLoc = this._locale._locations.prev(this._selectionLoc);
            this._selectionN--;
        }
        return this._selectionLoc;
    }

    void removeSelection(int i) {
        if (!$assertionsDisabled && (i < 0 || i >= this._selectionCount)) {
            throw new AssertionError();
        }
        int j = selectionIndex(i);
        if (i < this._selectionN) {
            this._selectionN--;
        } else if (i == this._selectionN) {
            this._selectionN--;
            if (i == 0) {
                this._selectionLoc = -1;
            } else {
                this._selectionLoc = this._locale._locations.prev(this._selectionLoc);
            }
        }
        this._selectionFirst = this._locale._locations.remove(this._selectionFirst, j);
        this._selectionCount--;
    }

    int selectionCount() {
        return this._selectionCount;
    }

    void moveToSelection(int i) {
        if (!$assertionsDisabled && (i < 0 || i >= this._selectionCount)) {
            throw new AssertionError();
        }
        this._locale._locations.moveTo(selectionIndex(i), this);
    }

    void clearSelection() {
        if (!$assertionsDisabled && this._selectionCount < 0) {
            throw new AssertionError();
        }
        while (this._selectionCount > 0) {
            removeSelection(0);
        }
    }

    boolean toParent() {
        return toParent(false);
    }

    boolean toParentRaw() {
        return toParent(true);
    }

    Xobj getParent() {
        return getParent(false);
    }

    Xobj getParentRaw() {
        return getParent(true);
    }

    boolean hasParent() {
        if (!$assertionsDisabled && !isPositioned()) {
            throw new AssertionError();
        }
        if (this._pos == -1) {
            return true;
        }
        if (this._pos >= 1 && this._pos < this._xobj.posAfter()) {
            return true;
        }
        if ($assertionsDisabled || this._pos == 0 || this._xobj._parent != null) {
            return this._xobj._parent != null;
        }
        throw new AssertionError();
    }

    Xobj getParentNoRoot() {
        if (!$assertionsDisabled && !isPositioned()) {
            throw new AssertionError();
        }
        if (this._pos == -1 || (this._pos >= 1 && this._pos < this._xobj.posAfter())) {
            return this._xobj;
        }
        if (!$assertionsDisabled && this._pos != 0 && this._xobj._parent == null) {
            throw new AssertionError();
        }
        if (this._xobj._parent != null) {
            return this._xobj._parent;
        }
        return null;
    }

    Xobj getParent(boolean raw) {
        if (!$assertionsDisabled && !isPositioned()) {
            throw new AssertionError();
        }
        if (this._pos == -1 || (this._pos >= 1 && this._pos < this._xobj.posAfter())) {
            return this._xobj;
        }
        if (!$assertionsDisabled && this._pos != 0 && this._xobj._parent == null) {
            throw new AssertionError();
        }
        if (this._xobj._parent != null) {
            return this._xobj._parent;
        }
        if (raw || this._xobj.isRoot()) {
            return null;
        }
        Cur r = this._locale.tempCur();
        r.createRoot();
        Xobj root = r._xobj;
        r.next();
        moveNode(r);
        r.release();
        return root;
    }

    boolean toParent(boolean raw) {
        Xobj parent = getParent(raw);
        if (parent == null) {
            return false;
        }
        moveTo(parent);
        return true;
    }

    void toRoot() {
        Xobj xobj;
        Xobj xobj2 = this._xobj;
        while (true) {
            xobj = xobj2;
            if (xobj.isRoot()) {
                break;
            }
            if (xobj._parent == null) {
                Cur r = this._locale.tempCur();
                r.createRoot();
                Xobj root = r._xobj;
                r.next();
                moveNode(r);
                r.release();
                xobj = root;
                break;
            }
            xobj2 = xobj._parent;
        }
        moveTo(xobj);
    }

    boolean hasText() {
        if ($assertionsDisabled || isNode()) {
            return this._xobj.hasTextEnsureOccupancy();
        }
        throw new AssertionError();
    }

    boolean hasAttrs() {
        if ($assertionsDisabled || isNode()) {
            return this._xobj.hasAttrs();
        }
        throw new AssertionError();
    }

    boolean hasChildren() {
        if ($assertionsDisabled || isNode()) {
            return this._xobj.hasChildren();
        }
        throw new AssertionError();
    }

    boolean toFirstChild() {
        if (!$assertionsDisabled && !isNode()) {
            throw new AssertionError();
        }
        if (!this._xobj.hasChildren()) {
            return false;
        }
        Xobj xobj = this._xobj._firstChild;
        while (true) {
            Xobj x = xobj;
            if (x.isAttr()) {
                xobj = x._nextSibling;
            } else {
                moveTo(x);
                return true;
            }
        }
    }

    protected boolean toLastChild() {
        if (!$assertionsDisabled && !isNode()) {
            throw new AssertionError();
        }
        if (!this._xobj.hasChildren()) {
            return false;
        }
        moveTo(this._xobj._lastChild);
        return true;
    }

    boolean toNextSibling() {
        if (!$assertionsDisabled && !isNode()) {
            throw new AssertionError();
        }
        if (this._xobj.isAttr()) {
            if (this._xobj._nextSibling != null && this._xobj._nextSibling.isAttr()) {
                moveTo(this._xobj._nextSibling);
                return true;
            }
            return false;
        }
        if (this._xobj._nextSibling != null) {
            moveTo(this._xobj._nextSibling);
            return true;
        }
        return false;
    }

    void setValueAsQName(QName qname) {
        if (!$assertionsDisabled && !isNode()) {
            throw new AssertionError();
        }
        String value = qname.getLocalPart();
        String ns = qname.getNamespaceURI();
        String prefix = prefixForNamespace(ns, qname.getPrefix().length() > 0 ? qname.getPrefix() : null, true);
        if (prefix.length() > 0) {
            value = prefix + ":" + value;
        }
        setValue(value);
    }

    void setValue(String value) {
        if (!$assertionsDisabled && !isNode()) {
            throw new AssertionError();
        }
        moveNodeContents(null, false);
        next();
        insertString(value);
        toParent();
    }

    void removeFollowingAttrs() {
        if (!$assertionsDisabled && !isAttr()) {
            throw new AssertionError();
        }
        QName attrName = getName();
        push();
        if (toNextAttr()) {
            while (isAttr()) {
                if (getName().equals(attrName)) {
                    moveNode(null);
                } else if (!toNextAttr()) {
                    break;
                }
            }
        }
        pop();
    }

    String getAttrValue(QName name) {
        String s = null;
        push();
        if (toAttr(name)) {
            s = getValueAsString();
        }
        pop();
        return s;
    }

    void setAttrValueAsQName(QName name, QName value) {
        if (!$assertionsDisabled && !isContainer()) {
            throw new AssertionError();
        }
        if (value == null) {
            this._xobj.removeAttr(name);
            return;
        }
        if (toAttr(name)) {
            removeFollowingAttrs();
        } else {
            next();
            createAttr(name);
        }
        setValueAsQName(value);
        toParent();
    }

    boolean removeAttr(QName name) {
        if ($assertionsDisabled || isContainer()) {
            return this._xobj.removeAttr(name);
        }
        throw new AssertionError();
    }

    void setAttrValue(QName name, String value) {
        if (!$assertionsDisabled && !isContainer()) {
            throw new AssertionError();
        }
        this._xobj.setAttr(name, value);
    }

    boolean toAttr(QName name) {
        if (!$assertionsDisabled && !isNode()) {
            throw new AssertionError();
        }
        Xobj a = this._xobj.getAttr(name);
        if (a == null) {
            return false;
        }
        moveTo(a);
        return true;
    }

    boolean toFirstAttr() {
        if (!$assertionsDisabled && !isNode()) {
            throw new AssertionError();
        }
        Xobj firstAttr = this._xobj.firstAttr();
        if (firstAttr == null) {
            return false;
        }
        moveTo(firstAttr);
        return true;
    }

    boolean toLastAttr() {
        if (!$assertionsDisabled && !isNode()) {
            throw new AssertionError();
        }
        if (!toFirstAttr()) {
            return false;
        }
        while (toNextAttr()) {
        }
        return true;
    }

    boolean toNextAttr() {
        if (!$assertionsDisabled && !isAttr() && !isContainer()) {
            throw new AssertionError();
        }
        Xobj nextAttr = this._xobj.nextAttr();
        if (nextAttr == null) {
            return false;
        }
        moveTo(nextAttr);
        return true;
    }

    boolean toPrevAttr() {
        if (isAttr()) {
            if (this._xobj._prevSibling == null) {
                moveTo(this._xobj.ensureParent());
                return true;
            }
            moveTo(this._xobj._prevSibling);
            return true;
        }
        prev();
        if (!isContainer()) {
            next();
            return false;
        }
        return toLastAttr();
    }

    boolean skipWithAttrs() {
        if (!$assertionsDisabled && !isNode()) {
            throw new AssertionError();
        }
        if (skip()) {
            return true;
        }
        if (this._xobj.isRoot()) {
            return false;
        }
        if (!$assertionsDisabled && !this._xobj.isAttr()) {
            throw new AssertionError();
        }
        toParent();
        next();
        return true;
    }

    boolean skip() {
        if (!$assertionsDisabled && !isNode()) {
            throw new AssertionError();
        }
        if (this._xobj.isRoot()) {
            return false;
        }
        if (this._xobj.isAttr()) {
            if (this._xobj._nextSibling == null || !this._xobj._nextSibling.isAttr()) {
                return false;
            }
            moveTo(this._xobj._nextSibling, 0);
            return true;
        }
        moveTo(getNormal(this._xobj, this._xobj.posAfter()), this._posTemp);
        return true;
    }

    void toEnd() {
        if (!$assertionsDisabled && !isNode()) {
            throw new AssertionError();
        }
        moveTo(this._xobj, -1);
    }

    void moveToCharNode(DomImpl.CharNode node) {
        if (!$assertionsDisabled && (node.getDom() == null || node.getDom().locale() != this._locale)) {
            throw new AssertionError();
        }
        moveToDom(node.getDom());
        this._xobj.ensureOccupancy();
        Xobj xobj = this._xobj;
        DomImpl.CharNode charNodeUpdateCharNodes = updateCharNodes(this._locale, this._xobj, this._xobj._charNodesValue, this._xobj._cchValue);
        DomImpl.CharNode charNode = charNodeUpdateCharNodes;
        xobj._charNodesValue = charNodeUpdateCharNodes;
        while (true) {
            DomImpl.CharNode n = charNode;
            if (n != null) {
                if (node != n) {
                    charNode = n._next;
                } else {
                    moveTo(getNormal(this._xobj, n._off + 1), this._posTemp);
                    return;
                }
            } else {
                Xobj xobj2 = this._xobj;
                DomImpl.CharNode charNodeUpdateCharNodes2 = updateCharNodes(this._locale, this._xobj, this._xobj._charNodesAfter, this._xobj._cchAfter);
                DomImpl.CharNode charNode2 = charNodeUpdateCharNodes2;
                xobj2._charNodesAfter = charNodeUpdateCharNodes2;
                while (true) {
                    DomImpl.CharNode n2 = charNode2;
                    if (n2 != null) {
                        if (node != n2) {
                            charNode2 = n2._next;
                        } else {
                            moveTo(getNormal(this._xobj, n2._off + this._xobj._cchValue + 2), this._posTemp);
                            return;
                        }
                    } else {
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                        return;
                    }
                }
            }
        }
    }

    boolean prevWithAttrs() {
        if (prev()) {
            return true;
        }
        if (!isAttr()) {
            return false;
        }
        toParent();
        return true;
    }

    boolean prev() {
        int p;
        if (!$assertionsDisabled && !isPositioned()) {
            throw new AssertionError();
        }
        if (this._xobj.isRoot() && this._pos == 0) {
            return false;
        }
        if (this._xobj.isAttr() && this._pos == 0 && this._xobj._prevSibling == null) {
            return false;
        }
        Xobj x = getDenormal();
        int p2 = this._posTemp;
        if (!$assertionsDisabled && (p2 <= 0 || p2 == -1)) {
            throw new AssertionError();
        }
        int pa = x.posAfter();
        if (p2 > pa) {
            p = pa;
        } else if (p2 == pa) {
            if (x.isAttr() && (x._cchAfter > 0 || x._nextSibling == null || !x._nextSibling.isAttr())) {
                x = x.ensureParent();
                p = 0;
            } else {
                p = -1;
            }
        } else if (p2 == pa - 1) {
            x.ensureOccupancy();
            p = x._cchValue > 0 ? 1 : 0;
        } else if (p2 > 1) {
            p = 1;
        } else {
            if (!$assertionsDisabled && p2 != 1) {
                throw new AssertionError();
            }
            p = 0;
        }
        moveTo(getNormal(x, p), this._posTemp);
        return true;
    }

    boolean next(boolean withAttrs) {
        return withAttrs ? nextWithAttrs() : next();
    }

    boolean nextWithAttrs() {
        int k = kind();
        if (kindIsContainer(k)) {
            if (toFirstAttr()) {
                return true;
            }
        } else if (k == -3) {
            if (next()) {
                return true;
            }
            toParent();
            if (!toParentRaw()) {
                return false;
            }
        }
        return next();
    }

    boolean next() {
        int p;
        Xobj a;
        if (!$assertionsDisabled && !isNormal()) {
            throw new AssertionError();
        }
        Xobj x = this._xobj;
        int p2 = this._pos;
        int pa = x.posAfter();
        if (p2 >= pa) {
            p = this._xobj.posMax();
        } else if (p2 == -1) {
            if (x.isRoot()) {
                return false;
            }
            if (x.isAttr() && (x._nextSibling == null || !x._nextSibling.isAttr())) {
                return false;
            }
            p = pa;
        } else if (p2 > 0) {
            if (!$assertionsDisabled && x._firstChild != null && x._firstChild.isAttr()) {
                throw new AssertionError();
            }
            if (x._firstChild != null) {
                x = x._firstChild;
                p = 0;
            } else {
                p = -1;
            }
        } else {
            if (!$assertionsDisabled && p2 != 0) {
                throw new AssertionError();
            }
            x.ensureOccupancy();
            p = 1;
            if (x._cchValue == 0 && x._firstChild != null) {
                if (x._firstChild.isAttr()) {
                    Xobj xobj = x._firstChild;
                    while (true) {
                        a = xobj;
                        if (a._nextSibling == null || !a._nextSibling.isAttr()) {
                            break;
                        }
                        xobj = a._nextSibling;
                    }
                    if (a._cchAfter > 0) {
                        x = a;
                        p = a.posAfter();
                    } else if (a._nextSibling != null) {
                        x = a._nextSibling;
                        p = 0;
                    }
                } else {
                    x = x._firstChild;
                    p = 0;
                }
            }
        }
        moveTo(getNormal(x, p), this._posTemp);
        return true;
    }

    int prevChars(int cch) {
        if (!$assertionsDisabled && !isPositioned()) {
            throw new AssertionError();
        }
        int cchLeft = cchLeft();
        if (cch < 0 || cch > cchLeft) {
            cch = cchLeft;
        }
        if (cch != 0) {
            moveTo(getNormal(getDenormal(), this._posTemp - cch), this._posTemp);
        }
        return cch;
    }

    int nextChars(int cch) {
        if (!$assertionsDisabled && !isPositioned()) {
            throw new AssertionError();
        }
        int cchRight = cchRight();
        if (cchRight == 0) {
            return 0;
        }
        if (cch < 0 || cch >= cchRight) {
            next();
            return cchRight;
        }
        moveTo(getNormal(this._xobj, this._pos + cch), this._posTemp);
        return cch;
    }

    /* JADX WARN: Multi-variable type inference failed */
    void setCharNodes(DomImpl.CharNode nodes) {
        if (!$assertionsDisabled && nodes != null && this._locale != nodes.locale()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !isPositioned()) {
            throw new AssertionError();
        }
        Xobj denormal = getDenormal();
        int p = this._posTemp;
        if (!$assertionsDisabled && denormal.isRoot() && (p <= 0 || p >= denormal.posAfter())) {
            throw new AssertionError();
        }
        if (p >= denormal.posAfter()) {
            denormal._charNodesAfter = nodes;
        } else {
            denormal._charNodesValue = nodes;
        }
        while (nodes != null) {
            nodes.setDom((DomImpl.Dom) denormal);
            nodes = nodes._next;
        }
    }

    DomImpl.CharNode getCharNodes() {
        DomImpl.CharNode nodes;
        if (!$assertionsDisabled && !isPositioned()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && isRoot()) {
            throw new AssertionError();
        }
        Xobj x = getDenormal();
        if (this._posTemp >= x.posAfter()) {
            DomImpl.CharNode charNodeUpdateCharNodes = updateCharNodes(this._locale, x, x._charNodesAfter, x._cchAfter);
            x._charNodesAfter = charNodeUpdateCharNodes;
            nodes = charNodeUpdateCharNodes;
        } else {
            x.ensureOccupancy();
            DomImpl.CharNode charNodeUpdateCharNodes2 = updateCharNodes(this._locale, x, x._charNodesValue, x._cchValue);
            x._charNodesValue = charNodeUpdateCharNodes2;
            nodes = charNodeUpdateCharNodes2;
        }
        return nodes;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static DomImpl.CharNode updateCharNodes(Locale l, Xobj xobj, DomImpl.CharNode nodes, int cch) {
        if (!$assertionsDisabled && nodes != null && nodes.locale() != l) {
            throw new AssertionError();
        }
        DomImpl.CharNode node = nodes;
        int i = 0;
        while (node != null && cch > 0) {
            if (!$assertionsDisabled && node.getDom() != xobj) {
                throw new AssertionError();
            }
            if (node._cch > cch) {
                node._cch = cch;
            }
            node._off = i;
            i += node._cch;
            cch -= node._cch;
            node = node._next;
        }
        if (cch <= 0) {
            while (node != null) {
                if (!$assertionsDisabled && node.getDom() != xobj) {
                    throw new AssertionError();
                }
                if (node._cch != 0) {
                    node._cch = 0;
                }
                node._off = i;
                node = node._next;
            }
        } else {
            DomImpl.CharNode node2 = l.createTextNode();
            node2.setDom((DomImpl.Dom) xobj);
            node2._cch = cch;
            node2._off = i;
            nodes = DomImpl.CharNode.appendNode(nodes, node2);
        }
        return nodes;
    }

    final QName getXsiTypeName() {
        if ($assertionsDisabled || isNode()) {
            return this._xobj.getXsiTypeName();
        }
        throw new AssertionError();
    }

    final void setXsiType(QName value) {
        if (!$assertionsDisabled && !isContainer()) {
            throw new AssertionError();
        }
        setAttrValueAsQName(Locale._xsiType, value);
    }

    final QName valueAsQName() {
        throw new RuntimeException("Not implemented");
    }

    final String namespaceForPrefix(String prefix, boolean defaultAlwaysMapped) {
        return this._xobj.namespaceForPrefix(prefix, defaultAlwaysMapped);
    }

    final String prefixForNamespace(String ns, String suggestion, boolean createIfMissing) {
        return (isContainer() ? this._xobj : getParent()).prefixForNamespace(ns, suggestion, createIfMissing);
    }

    boolean contains(Cur that) {
        if (!$assertionsDisabled && !isNode()) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || (that != null && that.isPositioned())) {
            return this._xobj.contains(that);
        }
        throw new AssertionError();
    }

    void insertString(String s) {
        if (s != null) {
            insertChars(s, 0, s.length());
        }
    }

    void insertChars(Object src, int off, int cch) {
        if (!$assertionsDisabled && (!isPositioned() || isRoot())) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !CharUtil.isValid(src, off, cch)) {
            throw new AssertionError();
        }
        if (cch <= 0) {
            return;
        }
        this._locale.notifyChange();
        if (this._pos == -1) {
            this._xobj.ensureOccupancy();
        }
        Xobj x = getDenormal();
        int p = this._posTemp;
        if (!$assertionsDisabled && p <= 0) {
            throw new AssertionError();
        }
        x.insertCharsHelper(p, src, off, cch, true);
        moveTo(x, p);
        this._locale._versionAll++;
    }

    /* JADX WARN: Code restructure failed: missing block: B:42:0x00a0, code lost:
    
        if ((r8._pos >= r8._xobj.posAfter() ? r8._xobj._parent : r8._xobj).isOccupied() == false) goto L43;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    java.lang.Object moveChars(org.apache.xmlbeans.impl.store.Cur r9, int r10) {
        /*
            Method dump skipped, instructions count: 371
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.store.Cur.moveChars(org.apache.xmlbeans.impl.store.Cur, int):java.lang.Object");
    }

    void moveNode(Cur to) {
        if (!$assertionsDisabled && (!isNode() || isRoot())) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && to != null && !to.isPositioned()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && to != null && contains(to)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && to != null && to.isRoot()) {
            throw new AssertionError();
        }
        Xobj x = this._xobj;
        skip();
        moveNode(x, to);
    }

    private static void transferChars(Xobj xFrom, int pFrom, Xobj xTo, int pTo, int cch) {
        if (!$assertionsDisabled && xFrom == xTo) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && xFrom._locale != xTo._locale) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (pFrom <= 0 || pFrom >= xFrom.posMax())) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (pTo <= 0 || pTo > xTo.posMax())) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (cch <= 0 || cch > xFrom.cchRight(pFrom))) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && pTo < xTo.posAfter() && !xTo.isOccupied()) {
            throw new AssertionError();
        }
        xTo.insertCharsHelper(pTo, xFrom.getCharsHelper(pFrom, cch), xFrom._locale._offSrc, xFrom._locale._cchSrc, false);
        xFrom.removeCharsHelper(pFrom, cch, xTo, pTo, true, false);
    }

    static void moveNode(Xobj x, Cur to) {
        if (!$assertionsDisabled && (x == null || x.isRoot())) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && to != null && !to.isPositioned()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && to != null && x.contains(to)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && to != null && to.isRoot()) {
            throw new AssertionError();
        }
        if (to != null) {
            if (to._pos == -1) {
                to._xobj.ensureOccupancy();
            }
            if ((to._pos == 0 && to._xobj == x) || to.isJustAfterEnd(x)) {
                to.moveTo(x);
                return;
            }
        }
        x._locale.notifyChange();
        x._locale._versionAll++;
        x._locale._versionSansText++;
        if (to != null && to._locale != x._locale) {
            to._locale.notifyChange();
            to._locale._versionAll++;
            to._locale._versionSansText++;
        }
        if (x.isAttr()) {
            x.invalidateSpecialAttr(to == null ? null : to.getParentRaw());
        } else {
            if (x._parent != null) {
                x._parent.invalidateUser();
            }
            if (to != null && to.hasParent()) {
                to.getParent().invalidateUser();
            }
        }
        if (x._cchAfter > 0) {
            transferChars(x, x.posAfter(), x.getDenormal(0), x.posTemp(), x._cchAfter);
        }
        if (!$assertionsDisabled && x._cchAfter != 0) {
            throw new AssertionError();
        }
        x._locale.embedCurs();
        Xobj xobjWalk = x;
        while (true) {
            Xobj y = xobjWalk;
            if (y == null) {
                break;
            }
            while (y._embedded != null) {
                y._embedded.moveTo(x.getNormal(x.posAfter()));
            }
            y.disconnectUser();
            if (to != null) {
                y._locale = to._locale;
            }
            xobjWalk = y.walk(x, true);
        }
        x.removeXobj();
        if (to != null) {
            Xobj here = to._xobj;
            boolean append = to._pos != 0;
            int cchRight = to.cchRight();
            if (cchRight > 0) {
                to.push();
                to.next();
                here = to._xobj;
                append = to._pos != 0;
                to.pop();
            }
            if (append) {
                here.appendXobj(x);
            } else {
                here.insertXobj(x);
            }
            if (cchRight > 0) {
                transferChars(to._xobj, to._pos, x, x.posAfter(), cchRight);
            }
            to.moveTo(x);
        }
    }

    void moveNodeContents(Cur to, boolean moveAttrs) {
        if (!$assertionsDisabled && this._pos != 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && to != null && to.isRoot()) {
            throw new AssertionError();
        }
        moveNodeContents(this._xobj, to, moveAttrs);
    }

    /* JADX WARN: Removed duplicated region for block: B:103:0x01e2  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x01c3 A[EDGE_INSN: B:168:0x01c3->B:96:0x01c3 BREAK  A[LOOP:1: B:93:0x01ae->B:95:0x01b9], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x01b9 A[LOOP:1: B:93:0x01ae->B:95:0x01b9, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:98:0x01cc  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static void moveNodeContents(org.apache.xmlbeans.impl.store.Xobj r6, org.apache.xmlbeans.impl.store.Cur r7, boolean r8) {
        /*
            Method dump skipped, instructions count: 912
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.store.Cur.moveNodeContents(org.apache.xmlbeans.impl.store.Xobj, org.apache.xmlbeans.impl.store.Cur, boolean):void");
    }

    protected final Xobj.Bookmark setBookmark(Object key, Object value) {
        if (!$assertionsDisabled && !isNormal()) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || key != null) {
            return this._xobj.setBookmark(this._pos, key, value);
        }
        throw new AssertionError();
    }

    Object getBookmark(Object key) {
        if (!$assertionsDisabled && !isNormal()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && key == null) {
            throw new AssertionError();
        }
        Xobj.Bookmark bookmark = this._xobj._bookmarks;
        while (true) {
            Xobj.Bookmark b = bookmark;
            if (b != null) {
                if (b._pos != this._pos || b._key != key) {
                    bookmark = b._next;
                } else {
                    return b._value;
                }
            } else {
                return null;
            }
        }
    }

    int firstBookmarkInChars(Object key, int cch) {
        if (!$assertionsDisabled && !isNormal()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && key == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && cch <= 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && cch > cchRight()) {
            throw new AssertionError();
        }
        int d = -1;
        if (isText()) {
            Xobj.Bookmark bookmark = this._xobj._bookmarks;
            while (true) {
                Xobj.Bookmark b = bookmark;
                if (b == null) {
                    break;
                }
                if (b._key == key && inChars(b, cch, false)) {
                    d = (d == -1 || b._pos - this._pos < d) ? b._pos - this._pos : d;
                }
                bookmark = b._next;
            }
        }
        return d;
    }

    int firstBookmarkInCharsLeft(Object key, int cch) {
        if (!$assertionsDisabled && !isNormal()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && key == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && cch <= 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && cch > cchLeft()) {
            throw new AssertionError();
        }
        int d = -1;
        if (cchLeft() > 0) {
            Xobj x = getDenormal();
            int p = this._posTemp - cch;
            Xobj.Bookmark bookmark = x._bookmarks;
            while (true) {
                Xobj.Bookmark b = bookmark;
                if (b == null) {
                    break;
                }
                if (b._key == key && x.inChars(p, b._xobj, b._pos, cch, false)) {
                    d = (d == -1 || b._pos - p < d) ? b._pos - p : d;
                }
                bookmark = b._next;
            }
        }
        return d;
    }

    String getCharsAsString(int cch) {
        if ($assertionsDisabled || (isNormal() && this._xobj != null)) {
            return getCharsAsString(cch, 1);
        }
        throw new AssertionError();
    }

    String getCharsAsString(int cch, int wsr) {
        return this._xobj.getCharsAsString(this._pos, cch, wsr);
    }

    String getValueAsString(int wsr) {
        if ($assertionsDisabled || isNode()) {
            return this._xobj.getValueAsString(wsr);
        }
        throw new AssertionError();
    }

    String getValueAsString() {
        if (!$assertionsDisabled && !isNode()) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || !hasChildren()) {
            return this._xobj.getValueAsString();
        }
        throw new AssertionError();
    }

    Object getChars(int cch) {
        if ($assertionsDisabled || isPositioned()) {
            return this._xobj.getChars(this._pos, cch, this);
        }
        throw new AssertionError();
    }

    Object getFirstChars() {
        if (!$assertionsDisabled && !isNode()) {
            throw new AssertionError();
        }
        Object src = this._xobj.getFirstChars();
        this._offSrc = this._locale._offSrc;
        this._cchSrc = this._locale._cchSrc;
        return src;
    }

    void copyNode(Cur to) {
        if (!$assertionsDisabled && to == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !isNode()) {
            throw new AssertionError();
        }
        Xobj copy = this._xobj.copyNode(to._locale);
        if (to.isPositioned()) {
            moveNode(copy, to);
        } else {
            to.moveTo(copy);
        }
    }

    Cur weakCur(Object o) {
        Cur c = this._locale.weakCur(o);
        c.moveToCur(this);
        return c;
    }

    Cur tempCur() {
        return tempCur(null);
    }

    Cur tempCur(String id) {
        Cur c = this._locale.tempCur(id);
        c.moveToCur(this);
        return c;
    }

    private Cur tempCur(Xobj x, int p) {
        if (!$assertionsDisabled && this._locale != x._locale) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && x == null && p != -2) {
            throw new AssertionError();
        }
        Cur c = this._locale.tempCur();
        if (x != null) {
            c.moveTo(getNormal(x, p), this._posTemp);
        }
        return c;
    }

    boolean inChars(Cur c, int cch, boolean includeEnd) {
        if (!$assertionsDisabled && (!isPositioned() || !isText() || cchRight() < cch)) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || c.isNormal()) {
            return this._xobj.inChars(this._pos, c._xobj, c._pos, cch, includeEnd);
        }
        throw new AssertionError();
    }

    boolean inChars(Xobj.Bookmark b, int cch, boolean includeEnd) {
        if (!$assertionsDisabled && (!isPositioned() || !isText() || cchRight() < cch)) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || b._xobj.isNormal(b._pos)) {
            return this._xobj.inChars(this._pos, b._xobj, b._pos, cch, includeEnd);
        }
        throw new AssertionError();
    }

    private Xobj getNormal(Xobj x, int p) {
        Xobj nx = x.getNormal(p);
        this._posTemp = x._locale._posTemp;
        return nx;
    }

    private Xobj getDenormal() {
        if ($assertionsDisabled || isPositioned()) {
            return getDenormal(this._xobj, this._pos);
        }
        throw new AssertionError();
    }

    private Xobj getDenormal(Xobj x, int p) {
        Xobj dx = x.getDenormal(p);
        this._posTemp = x._locale._posTemp;
        return dx;
    }

    void setType(SchemaType type) {
        setType(type, true);
    }

    void setType(SchemaType type, boolean complain) {
        if (!$assertionsDisabled && type == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !isUserNode()) {
            throw new AssertionError();
        }
        TypeStoreUser user = peekUser();
        if (user != null && user.get_schema_type() == type) {
            return;
        }
        if (isRoot()) {
            this._xobj.setStableType(type);
            return;
        }
        TypeStoreUser parentUser = this._xobj.ensureParent().getUser();
        if (isAttr()) {
            if (complain && parentUser.get_attribute_type(getName()) != type) {
                throw new IllegalArgumentException("Can't set type of attribute to " + type.toString());
            }
            return;
        }
        if (!$assertionsDisabled && !isElem()) {
            throw new AssertionError();
        }
        if (parentUser.get_element_type(getName(), null) == type) {
            removeAttr(Locale._xsiType);
            return;
        }
        QName typeName = type.getName();
        if (typeName == null) {
            if (complain) {
                throw new IllegalArgumentException("Can't set type of element, type is un-named");
            }
        } else if (parentUser.get_element_type(getName(), typeName) != type) {
            if (complain) {
                throw new IllegalArgumentException("Can't set type of element, invalid type");
            }
        } else {
            setAttrValueAsQName(Locale._xsiType, typeName);
        }
    }

    void setSubstitution(QName name, SchemaType type) {
        setSubstitution(name, type, true);
    }

    void setSubstitution(QName name, SchemaType type, boolean complain) {
        if (!$assertionsDisabled && name == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && type == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !isUserNode()) {
            throw new AssertionError();
        }
        TypeStoreUser user = peekUser();
        if ((user != null && user.get_schema_type() == type && name.equals(getName())) || isRoot()) {
            return;
        }
        TypeStoreUser parentUser = this._xobj.ensureParent().getUser();
        if (isAttr()) {
            if (complain) {
                throw new IllegalArgumentException("Can't use substitution with attributes");
            }
            return;
        }
        if (!$assertionsDisabled && !isElem()) {
            throw new AssertionError();
        }
        if (parentUser.get_element_type(name, null) == type) {
            setName(name);
            removeAttr(Locale._xsiType);
            return;
        }
        QName typeName = type.getName();
        if (typeName == null) {
            if (complain) {
                throw new IllegalArgumentException("Can't set xsi:type on element, type is un-named");
            }
        } else if (parentUser.get_element_type(name, typeName) != type) {
            if (complain) {
                throw new IllegalArgumentException("Can't set xsi:type on element, invalid type");
            }
        } else {
            setName(name);
            setAttrValueAsQName(Locale._xsiType, typeName);
        }
    }

    TypeStoreUser peekUser() {
        if ($assertionsDisabled || isUserNode()) {
            return this._xobj._user;
        }
        throw new AssertionError();
    }

    XmlObject getObject() {
        if (isUserNode()) {
            return (XmlObject) getUser();
        }
        return null;
    }

    TypeStoreUser getUser() {
        if ($assertionsDisabled || isUserNode()) {
            return this._xobj.getUser();
        }
        throw new AssertionError();
    }

    DomImpl.Dom getDom() {
        if (!$assertionsDisabled && !isNormal()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !isPositioned()) {
            throw new AssertionError();
        }
        if (isText()) {
            int cch = cchLeft();
            DomImpl.CharNode charNodes = getCharNodes();
            while (true) {
                DomImpl.CharNode cn = charNodes;
                int i = cch - cn._cch;
                cch = i;
                if (i >= 0) {
                    charNodes = cn._next;
                } else {
                    return cn;
                }
            }
        } else {
            return this._xobj.getDom();
        }
    }

    static void release(Cur c) {
        if (c != null) {
            c.release();
        }
    }

    void release() {
        if (this._tempFrame >= 0) {
            if (this._nextTemp != null) {
                this._nextTemp._prevTemp = this._prevTemp;
            }
            if (this._prevTemp == null) {
                this._locale._tempFrames[this._tempFrame] = this._nextTemp;
            } else {
                this._prevTemp._nextTemp = this._nextTemp;
            }
            this._nextTemp = null;
            this._prevTemp = null;
            this._tempFrame = -1;
        }
        if (this._state != 0 && this._state != 3) {
            while (this._stackTop != -1) {
                popButStay();
            }
            clearSelection();
            this._id = null;
            moveToCur(null);
            if (!$assertionsDisabled && !isNormal()) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this._xobj != null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this._pos != -2) {
                throw new AssertionError();
            }
            if (this._ref != null) {
                this._ref.clear();
                this._ref._cur = null;
            }
            this._ref = null;
            if (!$assertionsDisabled && this._state != 1) {
                throw new AssertionError();
            }
            this._locale._registered = listRemove(this._locale._registered);
            if (this._locale._curPoolCount < 16) {
                this._locale._curPool = listInsert(this._locale._curPool);
                this._state = 0;
                this._locale._curPoolCount++;
                return;
            }
            this._locale = null;
            this._state = 3;
        }
    }

    boolean isOnList(Cur head) {
        while (head != null) {
            if (head != this) {
                head = head._next;
            } else {
                return true;
            }
        }
        return false;
    }

    Cur listInsert(Cur head) {
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

    Cur listRemove(Cur head) {
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

    boolean isNormal() {
        if (this._state == 0 || this._state == 3) {
            return false;
        }
        if (this._xobj == null) {
            return this._pos == -2;
        }
        if (!this._xobj.isNormal(this._pos)) {
            return false;
        }
        if (this._state == 2) {
            return isOnList(this._xobj._embedded);
        }
        if ($assertionsDisabled || this._state == 1) {
            return isOnList(this._locale._registered);
        }
        throw new AssertionError();
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Cur$CurLoadContext.class */
    static final class CurLoadContext extends Locale.LoadContext {
        private boolean _stripLeft = true;
        private Locale _locale;
        private CharUtil _charUtil;
        private Xobj _frontier;
        private boolean _after;
        private Xobj _lastXobj;
        private int _lastPos;
        private boolean _discardDocElem;
        private QName _replaceDocElem;
        private boolean _stripWhitespace;
        private boolean _stripComments;
        private boolean _stripProcinsts;
        private Map _substituteNamespaces;
        private Map _additionalNamespaces;
        private String _doctypeName;
        private String _doctypePublicId;
        private String _doctypeSystemId;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Cur.class.desiredAssertionStatus();
        }

        CurLoadContext(Locale l, XmlOptions options) {
            XmlOptions options2 = XmlOptions.maskNull(options);
            this._locale = l;
            this._charUtil = options2.hasOption(Cur.LOAD_USE_LOCALE_CHAR_UTIL) ? this._locale.getCharUtil() : CharUtil.getThreadLocalCharUtil();
            this._frontier = Cur.createDomDocumentRootXobj(this._locale);
            this._after = false;
            this._lastXobj = this._frontier;
            this._lastPos = 0;
            if (options2.hasOption(XmlOptions.LOAD_REPLACE_DOCUMENT_ELEMENT)) {
                this._replaceDocElem = (QName) options2.get(XmlOptions.LOAD_REPLACE_DOCUMENT_ELEMENT);
                this._discardDocElem = true;
            }
            this._stripWhitespace = options2.hasOption(XmlOptions.LOAD_STRIP_WHITESPACE);
            this._stripComments = options2.hasOption(XmlOptions.LOAD_STRIP_COMMENTS);
            this._stripProcinsts = options2.hasOption(XmlOptions.LOAD_STRIP_PROCINSTS);
            this._substituteNamespaces = (Map) options2.get(XmlOptions.LOAD_SUBSTITUTE_NAMESPACES);
            this._additionalNamespaces = (Map) options2.get(XmlOptions.LOAD_ADDITIONAL_NAMESPACES);
            this._locale._versionAll++;
            this._locale._versionSansText++;
        }

        private void start(Xobj xo) {
            if (!$assertionsDisabled && this._frontier == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this._after && this._frontier._parent == null) {
                throw new AssertionError();
            }
            flushText();
            if (this._after) {
                this._frontier = this._frontier._parent;
                this._after = false;
            }
            this._frontier.appendXobj(xo);
            this._frontier = xo;
            this._lastXobj = xo;
            this._lastPos = 0;
        }

        private void end() {
            if (!$assertionsDisabled && this._frontier == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this._after && this._frontier._parent == null) {
                throw new AssertionError();
            }
            flushText();
            if (this._after) {
                this._frontier = this._frontier._parent;
            } else {
                this._after = true;
            }
            this._lastXobj = this._frontier;
            this._lastPos = -1;
        }

        private void text(Object src, int off, int cch) {
            if (cch <= 0) {
                return;
            }
            this._lastXobj = this._frontier;
            this._lastPos = this._frontier._cchValue + 1;
            if (this._after) {
                this._lastPos += this._frontier._cchAfter + 1;
                this._frontier._srcAfter = this._charUtil.saveChars(src, off, cch, this._frontier._srcAfter, this._frontier._offAfter, this._frontier._cchAfter);
                this._frontier._offAfter = this._charUtil._offSrc;
                this._frontier._cchAfter = this._charUtil._cchSrc;
                return;
            }
            this._frontier._srcValue = this._charUtil.saveChars(src, off, cch, this._frontier._srcValue, this._frontier._offValue, this._frontier._cchValue);
            this._frontier._offValue = this._charUtil._offSrc;
            this._frontier._cchValue = this._charUtil._cchSrc;
        }

        private void flushText() {
            if (this._stripWhitespace) {
                if (this._after) {
                    this._frontier._srcAfter = this._charUtil.stripRight(this._frontier._srcAfter, this._frontier._offAfter, this._frontier._cchAfter);
                    this._frontier._offAfter = this._charUtil._offSrc;
                    this._frontier._cchAfter = this._charUtil._cchSrc;
                    return;
                }
                this._frontier._srcValue = this._charUtil.stripRight(this._frontier._srcValue, this._frontier._offValue, this._frontier._cchValue);
                this._frontier._offValue = this._charUtil._offSrc;
                this._frontier._cchValue = this._charUtil._cchSrc;
            }
        }

        private Xobj parent() {
            return this._after ? this._frontier._parent : this._frontier;
        }

        private QName checkName(QName name, boolean local) {
            String substituteUri;
            if (this._substituteNamespaces != null && ((!local || name.getNamespaceURI().length() > 0) && (substituteUri = (String) this._substituteNamespaces.get(name.getNamespaceURI())) != null)) {
                name = this._locale.makeQName(substituteUri, name.getLocalPart(), name.getPrefix());
            }
            return name;
        }

        @Override // org.apache.xmlbeans.impl.store.Locale.LoadContext
        protected void startDTD(String name, String publicId, String systemId) {
            this._doctypeName = name;
            this._doctypePublicId = publicId;
            this._doctypeSystemId = systemId;
        }

        @Override // org.apache.xmlbeans.impl.store.Locale.LoadContext
        protected void endDTD() {
        }

        @Override // org.apache.xmlbeans.impl.store.Locale.LoadContext
        protected void startElement(QName name) {
            start(Cur.createElementXobj(this._locale, checkName(name, false), parent()._name));
            this._stripLeft = true;
        }

        @Override // org.apache.xmlbeans.impl.store.Locale.LoadContext
        protected void endElement() {
            if (!$assertionsDisabled && !parent().isElem()) {
                throw new AssertionError();
            }
            end();
            this._stripLeft = true;
        }

        @Override // org.apache.xmlbeans.impl.store.Locale.LoadContext
        protected void xmlns(String prefix, String uri) {
            String substituteUri;
            if (!$assertionsDisabled && !parent().isContainer()) {
                throw new AssertionError();
            }
            if (this._substituteNamespaces != null && (substituteUri = (String) this._substituteNamespaces.get(uri)) != null) {
                uri = substituteUri;
            }
            Xobj x = new Xobj.AttrXobj(this._locale, this._locale.createXmlns(prefix));
            start(x);
            text(uri, 0, uri.length());
            end();
            this._lastXobj = x;
            this._lastPos = 0;
        }

        @Override // org.apache.xmlbeans.impl.store.Locale.LoadContext
        protected void attr(QName name, String value) {
            if (!$assertionsDisabled && !parent().isContainer()) {
                throw new AssertionError();
            }
            QName parentName = this._after ? this._lastXobj._parent.getQName() : this._lastXobj.getQName();
            boolean isId = isAttrOfTypeId(name, parentName);
            Xobj x = isId ? new Xobj.AttrIdXobj(this._locale, checkName(name, true)) : new Xobj.AttrXobj(this._locale, checkName(name, true));
            start(x);
            text(value, 0, value.length());
            end();
            if (isId) {
                Cur c1 = x.tempCur();
                c1.toRoot();
                Xobj doc = c1._xobj;
                c1.release();
                if (doc instanceof Xobj.DocumentXobj) {
                    ((Xobj.DocumentXobj) doc).addIdElement(value, x._parent.getDom());
                }
            }
            this._lastXobj = x;
            this._lastPos = 0;
        }

        @Override // org.apache.xmlbeans.impl.store.Locale.LoadContext
        protected void attr(String local, String uri, String prefix, String value) {
            attr(this._locale.makeQName(uri, local, prefix), value);
        }

        @Override // org.apache.xmlbeans.impl.store.Locale.LoadContext
        protected void procInst(String target, String value) {
            if (!this._stripProcinsts) {
                Xobj x = new Xobj.ProcInstXobj(this._locale, target);
                start(x);
                text(value, 0, value.length());
                end();
                this._lastXobj = x;
                this._lastPos = 0;
            }
            this._stripLeft = true;
        }

        @Override // org.apache.xmlbeans.impl.store.Locale.LoadContext
        protected void comment(String comment) {
            if (!this._stripComments) {
                comment(comment, 0, comment.length());
            }
            this._stripLeft = true;
        }

        @Override // org.apache.xmlbeans.impl.store.Locale.LoadContext
        protected void comment(char[] chars, int off, int cch) {
            if (!this._stripComments) {
                comment(this._charUtil.saveChars(chars, off, cch), this._charUtil._offSrc, this._charUtil._cchSrc);
            }
            this._stripLeft = true;
        }

        private void comment(Object src, int off, int cch) {
            Xobj x = new Xobj.CommentXobj(this._locale);
            start(x);
            text(src, off, cch);
            end();
            this._lastXobj = x;
            this._lastPos = 0;
        }

        private void stripText(Object src, int off, int cch) {
            if (this._stripWhitespace && this._stripLeft) {
                src = this._charUtil.stripLeft(src, off, cch);
                this._stripLeft = false;
                off = this._charUtil._offSrc;
                cch = this._charUtil._cchSrc;
            }
            text(src, off, cch);
        }

        @Override // org.apache.xmlbeans.impl.store.Locale.LoadContext
        protected void text(String s) {
            if (s == null) {
                return;
            }
            stripText(s, 0, s.length());
        }

        @Override // org.apache.xmlbeans.impl.store.Locale.LoadContext
        protected void text(char[] src, int off, int cch) {
            stripText(src, off, cch);
        }

        @Override // org.apache.xmlbeans.impl.store.Locale.LoadContext
        protected void bookmark(XmlCursor.XmlBookmark bm) {
            this._lastXobj.setBookmark(this._lastPos, bm.getKey(), bm);
        }

        @Override // org.apache.xmlbeans.impl.store.Locale.LoadContext
        protected void bookmarkLastNonAttr(XmlCursor.XmlBookmark bm) {
            if (this._lastPos > 0 || !this._lastXobj.isAttr()) {
                this._lastXobj.setBookmark(this._lastPos, bm.getKey(), bm);
            } else {
                if (!$assertionsDisabled && this._lastXobj._parent == null) {
                    throw new AssertionError();
                }
                this._lastXobj._parent.setBookmark(0, bm.getKey(), bm);
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Locale.LoadContext
        protected void bookmarkLastAttr(QName attrName, XmlCursor.XmlBookmark bm) {
            if (this._lastPos == 0 && this._lastXobj.isAttr()) {
                if (!$assertionsDisabled && this._lastXobj._parent == null) {
                    throw new AssertionError();
                }
                Xobj a = this._lastXobj._parent.getAttr(attrName);
                if (a != null) {
                    a.setBookmark(0, bm.getKey(), bm);
                }
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Locale.LoadContext
        protected void lineNumber(int line, int column, int offset) {
            this._lastXobj.setBookmark(this._lastPos, XmlLineNumber.class, new XmlLineNumber(line, column, offset));
        }

        @Override // org.apache.xmlbeans.impl.store.Locale.LoadContext
        protected void abort() {
            this._stripLeft = true;
            while (!parent().isRoot()) {
                end();
            }
            finish().release();
        }

        @Override // org.apache.xmlbeans.impl.store.Locale.LoadContext
        protected Cur finish() {
            flushText();
            if (this._after) {
                this._frontier = this._frontier._parent;
            }
            if (!$assertionsDisabled && (this._frontier == null || this._frontier._parent != null || !this._frontier.isRoot())) {
                throw new AssertionError();
            }
            Cur c = this._frontier.tempCur();
            if (!Locale.toFirstChildElement(c)) {
                return c;
            }
            boolean isFrag = Locale.isFragmentQName(c.getName());
            if (this._discardDocElem || isFrag) {
                if (this._replaceDocElem != null) {
                    c.setName(this._replaceDocElem);
                } else {
                    while (c.toParent()) {
                    }
                    c.next();
                    while (!c.isElem()) {
                        if (c.isText()) {
                            c.moveChars(null, -1);
                        } else {
                            c.moveNode(null);
                        }
                    }
                    if (!$assertionsDisabled && !c.isElem()) {
                        throw new AssertionError();
                    }
                    c.skip();
                    while (!c.isFinish()) {
                        if (c.isText()) {
                            c.moveChars(null, -1);
                        } else {
                            c.moveNode(null);
                        }
                    }
                    c.toParent();
                    c.next();
                    if (!$assertionsDisabled && !c.isElem()) {
                        throw new AssertionError();
                    }
                    Cur c2 = c.tempCur();
                    c.moveNodeContents(c, true);
                    c.moveToCur(c2);
                    c2.release();
                    c.moveNode(null);
                }
                if (isFrag) {
                    c.moveTo(this._frontier);
                    if (c.toFirstAttr()) {
                        while (true) {
                            if (c.isXmlns() && c.getXmlnsUri().equals("http://www.openuri.org/fragment")) {
                                c.moveNode(null);
                                if (!c.isAttr()) {
                                    break;
                                }
                            } else if (!c.toNextAttr()) {
                                break;
                            }
                        }
                    }
                    c.moveTo(this._frontier);
                    this._frontier = Cur.createDomDocumentRootXobj(this._locale, true);
                    Cur c22 = this._frontier.tempCur();
                    c22.next();
                    c.moveNodeContents(c22, true);
                    c.moveTo(this._frontier);
                    c22.release();
                }
            }
            if (this._additionalNamespaces != null) {
                c.moveTo(this._frontier);
                Locale.toFirstChildElement(c);
                Locale.applyNamespaces(c, this._additionalNamespaces);
            }
            if (this._doctypeName != null && (this._doctypePublicId != null || this._doctypeSystemId != null)) {
                XmlDocumentProperties props = Locale.getDocProps(c, true);
                props.setDoctypeName(this._doctypeName);
                if (this._doctypePublicId != null) {
                    props.setDoctypePublicId(this._doctypePublicId);
                }
                if (this._doctypeSystemId != null) {
                    props.setDoctypeSystemId(this._doctypeSystemId);
                }
            }
            c.moveTo(this._frontier);
            if ($assertionsDisabled || c.isRoot()) {
                return c;
            }
            throw new AssertionError();
        }

        public void dump() {
            this._frontier.dump();
        }
    }

    static String kindName(int kind) {
        switch (kind) {
            case 0:
                return "TEXT";
            case 1:
                return "ROOT";
            case 2:
                return "ELEM";
            case 3:
                return "ATTR";
            case 4:
                return "COMMENT";
            case 5:
                return "PROCINST";
            default:
                return "<< Unknown Kind (" + kind + ") >>";
        }
    }

    static void dump(PrintStream o, DomImpl.Dom d, Object ref) {
    }

    static void dump(PrintStream o, DomImpl.Dom d) {
        d.dump(o);
    }

    static void dump(DomImpl.Dom d) {
        dump(System.out, d);
    }

    static void dump(Node n) {
        dump(System.out, n);
    }

    static void dump(PrintStream o, Node n) {
        dump(o, (DomImpl.Dom) n);
    }

    void dump() {
        dump(System.out, this._xobj, this);
    }

    void dump(PrintStream o) {
        if (this._xobj == null) {
            o.println("Unpositioned xptr");
        } else {
            dump(o, this._xobj, this);
        }
    }

    public static void dump(PrintStream o, Xobj xo, Object ref) {
        if (ref == null) {
            ref = xo;
        }
        while (xo._parent != null) {
            xo = xo._parent;
        }
        dumpXobj(o, xo, 0, ref);
        o.println();
    }

    private static void dumpCur(PrintStream o, String prefix, Cur c, Object ref) {
        o.print(SymbolConstants.SPACE_SYMBOL);
        if (ref == c) {
            o.print("*:");
        }
        o.print(prefix + (c._id == null ? "<cur>" : c._id) + PropertyAccessor.PROPERTY_KEY_PREFIX + c._pos + "]");
    }

    private static void dumpCurs(PrintStream o, Xobj xo, Object ref) {
        Cur cur = xo._embedded;
        while (true) {
            Cur c = cur;
            if (c == null) {
                break;
            }
            dumpCur(o, "E:", c, ref);
            cur = c._next;
        }
        Cur cur2 = xo._locale._registered;
        while (true) {
            Cur c2 = cur2;
            if (c2 != null) {
                if (c2._xobj == xo) {
                    dumpCur(o, "R:", c2, ref);
                }
                cur2 = c2._next;
            } else {
                return;
            }
        }
    }

    private static void dumpBookmarks(PrintStream o, Xobj xo, Object ref) {
        Xobj.Bookmark bookmark = xo._bookmarks;
        while (true) {
            Xobj.Bookmark b = bookmark;
            if (b != null) {
                o.print(SymbolConstants.SPACE_SYMBOL);
                if (ref == b) {
                    o.print("*:");
                }
                if (b._value instanceof XmlLineNumber) {
                    XmlLineNumber l = (XmlLineNumber) b._value;
                    o.print("<line:" + l.getLine() + ">" + PropertyAccessor.PROPERTY_KEY_PREFIX + b._pos + "]");
                } else {
                    o.print("<mark>[" + b._pos + "]");
                }
                bookmark = b._next;
            } else {
                return;
            }
        }
    }

    private static void dumpCharNodes(PrintStream o, DomImpl.CharNode nodes, Object ref) {
        DomImpl.CharNode charNode = nodes;
        while (true) {
            DomImpl.CharNode n = charNode;
            if (n != null) {
                o.print(SymbolConstants.SPACE_SYMBOL);
                if (n == ref) {
                    o.print("*");
                }
                o.print((n instanceof DomImpl.TextNode ? "TEXT" : "CDATA") + PropertyAccessor.PROPERTY_KEY_PREFIX + n._cch + "]");
                charNode = n._next;
            } else {
                return;
            }
        }
    }

    private static void dumpChars(PrintStream o, Object src, int off, int cch) {
        o.print(SymbolConstants.QUOTES_SYMBOL);
        String s = CharUtil.getString(src, off, cch);
        int iCharCount = 0;
        while (true) {
            int i = iCharCount;
            if (i >= s.length()) {
                break;
            }
            if (i == 36) {
                o.print("...");
                break;
            }
            int codePoint = s.codePointAt(i);
            char[] chars = Character.toChars(codePoint);
            if (chars.length == 1) {
                char ch2 = chars[0];
                if (ch2 >= ' ' && ch2 < 127) {
                    o.print(ch2);
                } else if (ch2 == '\n') {
                    o.print("\\n");
                } else if (ch2 == '\r') {
                    o.print("\\r");
                } else if (ch2 == '\t') {
                    o.print("\\t");
                } else if (ch2 == '\"') {
                    o.print("\\\"");
                } else {
                    o.print("<#" + ((int) ch2) + ">");
                }
            } else {
                o.print("<#" + codePoint + ">");
            }
            iCharCount = i + Character.charCount(codePoint);
        }
        o.print(SymbolConstants.QUOTES_SYMBOL);
    }

    private static void dumpXobj(PrintStream o, Xobj xo, int level, Object ref) {
        if (xo == null) {
            return;
        }
        if (xo == ref) {
            o.print("* ");
        } else {
            o.print("  ");
        }
        for (int i = 0; i < level; i++) {
            o.print("  ");
        }
        o.print(kindName(xo.kind()));
        if (xo._name != null) {
            o.print(SymbolConstants.SPACE_SYMBOL);
            if (xo._name.getPrefix().length() > 0) {
                o.print(xo._name.getPrefix() + ":");
            }
            o.print(xo._name.getLocalPart());
            if (xo._name.getNamespaceURI().length() > 0) {
                o.print("@" + xo._name.getNamespaceURI());
            }
        }
        if (xo._srcValue != null || xo._charNodesValue != null) {
            o.print(" Value( ");
            dumpChars(o, xo._srcValue, xo._offValue, xo._cchValue);
            dumpCharNodes(o, xo._charNodesValue, ref);
            o.print(" )");
        }
        if (xo._user != null) {
            o.print(" (USER)");
        }
        if (xo.isVacant()) {
            o.print(" (VACANT)");
        }
        if (xo._srcAfter != null || xo._charNodesAfter != null) {
            o.print(" After( ");
            dumpChars(o, xo._srcAfter, xo._offAfter, xo._cchAfter);
            dumpCharNodes(o, xo._charNodesAfter, ref);
            o.print(" )");
        }
        dumpCurs(o, xo, ref);
        dumpBookmarks(o, xo, ref);
        String className = xo.getClass().getName();
        int i2 = className.lastIndexOf(46);
        if (i2 > 0) {
            className = className.substring(i2 + 1);
            int i3 = className.lastIndexOf(36);
            if (i3 > 0) {
                className = className.substring(i3 + 1);
            }
        }
        o.print(" (");
        o.print(className);
        o.print(")");
        o.println();
        Xobj xobj = xo._firstChild;
        while (true) {
            Xobj xo2 = xobj;
            if (xo2 != null) {
                dumpXobj(o, xo2, level + 1, ref);
                xobj = xo2._nextSibling;
            } else {
                return;
            }
        }
    }

    void setId(String id) {
        this._id = id;
    }
}
