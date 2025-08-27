package org.apache.xmlbeans;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.catalina.realm.Constants;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlCursor.class */
public interface XmlCursor extends XmlTokenSource {

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlCursor$ChangeStamp.class */
    public interface ChangeStamp {
        boolean hasChanged();
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlCursor$XmlMark.class */
    public interface XmlMark {
        XmlCursor createCursor();
    }

    void dispose();

    boolean toCursor(XmlCursor xmlCursor);

    void push();

    boolean pop();

    void selectPath(String str);

    void selectPath(String str, XmlOptions xmlOptions);

    boolean hasNextSelection();

    boolean toNextSelection();

    boolean toSelection(int i);

    int getSelectionCount();

    void addToSelection();

    void clearSelections();

    boolean toBookmark(XmlBookmark xmlBookmark);

    XmlBookmark toNextBookmark(Object obj);

    XmlBookmark toPrevBookmark(Object obj);

    QName getName();

    void setName(QName qName);

    String namespaceForPrefix(String str);

    String prefixForNamespace(String str);

    void getAllNamespaces(Map map);

    XmlObject getObject();

    TokenType currentTokenType();

    boolean isStartdoc();

    boolean isEnddoc();

    boolean isStart();

    boolean isEnd();

    boolean isText();

    boolean isAttr();

    boolean isNamespace();

    boolean isComment();

    boolean isProcinst();

    boolean isContainer();

    boolean isFinish();

    boolean isAnyAttr();

    TokenType prevTokenType();

    boolean hasNextToken();

    boolean hasPrevToken();

    TokenType toNextToken();

    TokenType toPrevToken();

    TokenType toFirstContentToken();

    TokenType toEndToken();

    int toNextChar(int i);

    int toPrevChar(int i);

    boolean toNextSibling();

    boolean toPrevSibling();

    boolean toParent();

    boolean toFirstChild();

    boolean toLastChild();

    boolean toChild(String str);

    boolean toChild(String str, String str2);

    boolean toChild(QName qName);

    boolean toChild(int i);

    boolean toChild(QName qName, int i);

    boolean toNextSibling(String str);

    boolean toNextSibling(String str, String str2);

    boolean toNextSibling(QName qName);

    boolean toFirstAttribute();

    boolean toLastAttribute();

    boolean toNextAttribute();

    boolean toPrevAttribute();

    String getAttributeText(QName qName);

    boolean setAttributeText(QName qName, String str);

    boolean removeAttribute(QName qName);

    String getTextValue();

    int getTextValue(char[] cArr, int i, int i2);

    void setTextValue(String str);

    void setTextValue(char[] cArr, int i, int i2);

    String getChars();

    int getChars(char[] cArr, int i, int i2);

    void toStartDoc();

    void toEndDoc();

    boolean isInSameDocument(XmlCursor xmlCursor);

    int comparePosition(XmlCursor xmlCursor);

    boolean isLeftOf(XmlCursor xmlCursor);

    boolean isAtSamePositionAs(XmlCursor xmlCursor);

    boolean isRightOf(XmlCursor xmlCursor);

    XmlCursor execQuery(String str);

    XmlCursor execQuery(String str, XmlOptions xmlOptions);

    ChangeStamp getDocChangeStamp();

    void setBookmark(XmlBookmark xmlBookmark);

    XmlBookmark getBookmark(Object obj);

    void clearBookmark(Object obj);

    void getAllBookmarkRefs(Collection collection);

    boolean removeXml();

    boolean moveXml(XmlCursor xmlCursor);

    boolean copyXml(XmlCursor xmlCursor);

    boolean removeXmlContents();

    boolean moveXmlContents(XmlCursor xmlCursor);

    boolean copyXmlContents(XmlCursor xmlCursor);

    int removeChars(int i);

    int moveChars(int i, XmlCursor xmlCursor);

    int copyChars(int i, XmlCursor xmlCursor);

    void insertChars(String str);

    void insertElement(QName qName);

    void insertElement(String str);

    void insertElement(String str, String str2);

    void beginElement(QName qName);

    void beginElement(String str);

    void beginElement(String str, String str2);

    void insertElementWithText(QName qName, String str);

    void insertElementWithText(String str, String str2);

    void insertElementWithText(String str, String str2, String str3);

    void insertAttribute(String str);

    void insertAttribute(String str, String str2);

    void insertAttribute(QName qName);

    void insertAttributeWithValue(String str, String str2);

    void insertAttributeWithValue(String str, String str2, String str3);

    void insertAttributeWithValue(QName qName, String str);

    void insertNamespace(String str, String str2);

    void insertComment(String str);

    void insertProcInst(String str, String str2);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlCursor$TokenType.class */
    public static final class TokenType {
        public static final int INT_NONE = 0;
        public static final int INT_STARTDOC = 1;
        public static final int INT_ENDDOC = 2;
        public static final int INT_START = 3;
        public static final int INT_END = 4;
        public static final int INT_TEXT = 5;
        public static final int INT_ATTR = 6;
        public static final int INT_NAMESPACE = 7;
        public static final int INT_COMMENT = 8;
        public static final int INT_PROCINST = 9;
        public static final TokenType NONE = new TokenType(Constants.NONE_TRANSPORT, 0);
        public static final TokenType STARTDOC = new TokenType("STARTDOC", 1);
        public static final TokenType ENDDOC = new TokenType("ENDDOC", 2);
        public static final TokenType START = new TokenType("START", 3);
        public static final TokenType END = new TokenType("END", 4);
        public static final TokenType TEXT = new TokenType("TEXT", 5);
        public static final TokenType ATTR = new TokenType("ATTR", 6);
        public static final TokenType NAMESPACE = new TokenType("NAMESPACE", 7);
        public static final TokenType COMMENT = new TokenType("COMMENT", 8);
        public static final TokenType PROCINST = new TokenType("PROCINST", 9);
        private String _name;
        private int _value;

        public String toString() {
            return this._name;
        }

        public int intValue() {
            return this._value;
        }

        public boolean isNone() {
            return this == NONE;
        }

        public boolean isStartdoc() {
            return this == STARTDOC;
        }

        public boolean isEnddoc() {
            return this == ENDDOC;
        }

        public boolean isStart() {
            return this == START;
        }

        public boolean isEnd() {
            return this == END;
        }

        public boolean isText() {
            return this == TEXT;
        }

        public boolean isAttr() {
            return this == ATTR;
        }

        public boolean isNamespace() {
            return this == NAMESPACE;
        }

        public boolean isComment() {
            return this == COMMENT;
        }

        public boolean isProcinst() {
            return this == PROCINST;
        }

        public boolean isContainer() {
            return this == STARTDOC || this == START;
        }

        public boolean isFinish() {
            return this == ENDDOC || this == END;
        }

        public boolean isAnyAttr() {
            return this == NAMESPACE || this == ATTR;
        }

        private TokenType(String name, int value) {
            this._name = name;
            this._value = value;
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlCursor$XmlBookmark.class */
    public static abstract class XmlBookmark {
        public XmlMark _currentMark;
        public final Reference _ref;

        public XmlBookmark() {
            this(false);
        }

        public XmlBookmark(boolean weak) {
            this._ref = weak ? new WeakReference(this) : null;
        }

        public final XmlCursor createCursor() {
            if (this._currentMark == null) {
                return null;
            }
            return this._currentMark.createCursor();
        }

        public final XmlCursor toBookmark(XmlCursor c) {
            return (c == null || !c.toBookmark(this)) ? createCursor() : c;
        }

        public Object getKey() {
            return getClass();
        }
    }
}
