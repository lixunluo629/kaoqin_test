package org.apache.xmlbeans.impl.store;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.impl.common.ValidatorListener;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Validate.class */
final class Validate implements ValidatorListener.Event {
    private ValidatorListener _sink;
    private Cur _cur;
    private boolean _hasText;
    private boolean _oneChunk;
    private Cur _textCur;
    private StringBuffer _textSb;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Validate.class.desiredAssertionStatus();
    }

    Validate(Cur c, ValidatorListener sink) {
        if (!c.isUserNode()) {
            throw new IllegalStateException("Inappropriate location to validate");
        }
        this._sink = sink;
        this._cur = c;
        this._textCur = c.tempCur();
        this._hasText = false;
        this._cur.push();
        try {
            process();
            this._cur.pop();
            this._cur = null;
            this._sink = null;
            this._textCur.release();
        } catch (Throwable th) {
            this._cur.pop();
            this._cur = null;
            this._sink = null;
            this._textCur.release();
            throw th;
        }
    }

    private void process() {
        emitEvent(1);
        if (this._cur.isAttr()) {
            this._cur.next();
            if (this._cur.isText()) {
                emitText();
            }
        } else {
            if (!$assertionsDisabled && !this._cur.isContainer()) {
                throw new AssertionError();
            }
            doAttrs();
            this._cur.next();
            while (!this._cur.isAtEndOfLastPush()) {
                switch (this._cur.kind()) {
                    case -2:
                        emitEvent(2);
                        break;
                    case -1:
                    case 1:
                    case 3:
                    default:
                        throw new RuntimeException("Unexpected kind: " + this._cur.kind());
                    case 0:
                        emitText();
                        break;
                    case 2:
                        emitEvent(1);
                        doAttrs();
                        break;
                    case 4:
                    case 5:
                        this._cur.toEnd();
                        break;
                }
                this._cur.next();
            }
        }
        emitEvent(2);
    }

    private void doAttrs() {
        if (!$assertionsDisabled && this._hasText) {
            throw new AssertionError();
        }
        if (this._cur.toFirstAttr()) {
            do {
                if (this._cur.isNormalAttr() && !this._cur.getUri().equals("http://www.w3.org/2001/XMLSchema-instance")) {
                    this._sink.nextEvent(4, this);
                }
            } while (this._cur.toNextAttr());
            this._cur.toParent();
        }
        this._sink.nextEvent(5, this);
    }

    private void emitText() {
        if (!$assertionsDisabled && !this._cur.isText()) {
            throw new AssertionError();
        }
        if (this._hasText) {
            if (this._oneChunk) {
                if (this._textSb == null) {
                    this._textSb = new StringBuffer();
                } else {
                    this._textSb.delete(0, this._textSb.length());
                }
                if (!$assertionsDisabled && !this._textCur.isText()) {
                    throw new AssertionError();
                }
                CharUtil.getString(this._textSb, this._textCur.getChars(-1), this._textCur._offSrc, this._textCur._cchSrc);
                this._oneChunk = false;
            }
            if (!$assertionsDisabled && (this._textSb == null || this._textSb.length() <= 0)) {
                throw new AssertionError();
            }
            CharUtil.getString(this._textSb, this._cur.getChars(-1), this._cur._offSrc, this._cur._cchSrc);
            return;
        }
        this._hasText = true;
        this._oneChunk = true;
        this._textCur.moveToCur(this._cur);
    }

    private void emitEvent(int kind) {
        if (!$assertionsDisabled && kind == 3) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && kind == 4 && this._hasText) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && kind == 5 && this._hasText) {
            throw new AssertionError();
        }
        if (this._hasText) {
            this._sink.nextEvent(3, this);
            this._hasText = false;
        }
        this._sink.nextEvent(kind, this);
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public String getText() {
        if (this._cur.isAttr()) {
            return this._cur.getValueAsString();
        }
        if (!$assertionsDisabled && !this._hasText) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !this._oneChunk && (this._textSb == null || this._textSb.length() <= 0)) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || !this._oneChunk || this._textCur.isText()) {
            return this._oneChunk ? this._textCur.getCharsAsString(-1) : this._textSb.toString();
        }
        throw new AssertionError();
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public String getText(int wsr) {
        if (this._cur.isAttr()) {
            return this._cur.getValueAsString(wsr);
        }
        if (!$assertionsDisabled && !this._hasText) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !this._oneChunk && (this._textSb == null || this._textSb.length() <= 0)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this._oneChunk && !this._textCur.isText()) {
            throw new AssertionError();
        }
        if (this._oneChunk) {
            return this._textCur.getCharsAsString(-1, wsr);
        }
        return Locale.applyWhiteSpaceRule(this._textSb.toString(), wsr);
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public boolean textIsWhitespace() {
        if (this._cur.isAttr()) {
            return this._cur._locale.getCharUtil().isWhiteSpace(this._cur.getFirstChars(), this._cur._offSrc, this._cur._cchSrc);
        }
        if (!$assertionsDisabled && !this._hasText) {
            throw new AssertionError();
        }
        if (this._oneChunk) {
            return this._cur._locale.getCharUtil().isWhiteSpace(this._textCur.getChars(-1), this._textCur._offSrc, this._textCur._cchSrc);
        }
        String s = this._textSb.toString();
        return this._cur._locale.getCharUtil().isWhiteSpace(s, 0, s.length());
    }

    @Override // org.apache.xmlbeans.impl.common.PrefixResolver
    public String getNamespaceForPrefix(String prefix) {
        return this._cur.namespaceForPrefix(prefix, true);
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public XmlCursor getLocationAsCursor() {
        return new Cursor(this._cur);
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public Location getLocation() {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public String getXsiType() {
        return this._cur.getAttrValue(Locale._xsiType);
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public String getXsiNil() {
        return this._cur.getAttrValue(Locale._xsiNil);
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public String getXsiLoc() {
        return this._cur.getAttrValue(Locale._xsiLoc);
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public String getXsiNoLoc() {
        return this._cur.getAttrValue(Locale._xsiNoLoc);
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public QName getName() {
        if (this._cur.isAtLastPush()) {
            return null;
        }
        return this._cur.getName();
    }
}
