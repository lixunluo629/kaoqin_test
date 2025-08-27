package org.aspectj.weaver.loadtime.definition;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/definition/LightXMLParser.class */
public class LightXMLParser {
    private static final char NULL_CHAR = 0;
    private char pushedBackChar;
    private Reader reader;
    private static Map<String, char[]> entities = new HashMap();
    private String name = null;
    private Map<String, Object> attributes = new HashMap();
    private ArrayList children = new ArrayList();

    static {
        entities.put("amp", new char[]{'&'});
        entities.put("quot", new char[]{'\"'});
        entities.put("apos", new char[]{'\''});
        entities.put("lt", new char[]{'<'});
        entities.put("gt", new char[]{'>'});
    }

    public ArrayList getChildrens() {
        return this.children;
    }

    public String getName() {
        return this.name;
    }

    public void parseFromReader(Reader reader) throws Exception {
        this.pushedBackChar = (char) 0;
        this.attributes = new HashMap();
        this.name = null;
        this.children = new ArrayList();
        this.reader = reader;
        while (true) {
            char c = skipBlanks();
            if (c != '<') {
                throw new Exception("LightParser Exception: Expected < but got: " + c);
            }
            char c2 = getNextChar();
            if (c2 == '!' || c2 == '?') {
                skipCommentOrXmlTag(0);
            } else {
                pushBackChar(c2);
                parseNode(this);
                return;
            }
        }
    }

    private char skipBlanks() throws Exception {
        while (true) {
            char c = getNextChar();
            switch (c) {
                case '\t':
                case '\n':
                case '\r':
                case ' ':
                default:
                    return c;
            }
        }
    }

    private char getWhitespaces(StringBuffer result) throws Exception {
        while (true) {
            char c = getNextChar();
            switch (c) {
                case '\t':
                case '\n':
                case ' ':
                    result.append(c);
                    break;
                case '\r':
                    break;
                default:
                    return c;
            }
        }
    }

    private void getNodeName(StringBuffer result) throws Exception {
        while (true) {
            char c = getNextChar();
            if ((c < 'a' || c > 'z') && ((c > 'Z' || c < 'A') && ((c > '9' || c < '0') && c != '_' && c != '-' && c != '.' && c != ':'))) {
                pushBackChar(c);
                return;
            }
            result.append(c);
        }
    }

    private void getString(StringBuffer string) throws Exception {
        char delimiter = getNextChar();
        if (delimiter != '\'' && delimiter != '\"') {
            throw new Exception("Parsing error. Expected ' or \"  but got: " + delimiter);
        }
        while (true) {
            char c = getNextChar();
            if (c == delimiter) {
                return;
            }
            if (c == '&') {
                mapEntity(string);
            } else {
                string.append(c);
            }
        }
    }

    private void getPCData(StringBuffer data) throws Exception {
        while (true) {
            char c = getNextChar();
            if (c == '<') {
                char c2 = getNextChar();
                if (c2 == '!') {
                    checkCDATA(data);
                } else {
                    pushBackChar(c2);
                    return;
                }
            } else {
                data.append(c);
            }
        }
    }

    private boolean checkCDATA(StringBuffer buf) throws Exception {
        char c = getNextChar();
        if (c != '[') {
            pushBackChar(c);
            skipCommentOrXmlTag(0);
            return false;
        }
        if (!checkLiteral("CDATA[")) {
            skipCommentOrXmlTag(1);
            return false;
        }
        int delimiterCharsSkipped = 0;
        while (delimiterCharsSkipped < 3) {
            char c2 = getNextChar();
            switch (c2) {
                case '>':
                    if (delimiterCharsSkipped < 2) {
                        for (int i = 0; i < delimiterCharsSkipped; i++) {
                            buf.append(']');
                        }
                        delimiterCharsSkipped = 0;
                        buf.append('>');
                        break;
                    } else {
                        delimiterCharsSkipped = 3;
                        break;
                    }
                case ']':
                    if (delimiterCharsSkipped < 2) {
                        delimiterCharsSkipped++;
                        break;
                    } else {
                        buf.append(']');
                        buf.append(']');
                        delimiterCharsSkipped = 0;
                        break;
                    }
                default:
                    for (int i2 = 0; i2 < delimiterCharsSkipped; i2++) {
                        buf.append(']');
                    }
                    buf.append(c2);
                    delimiterCharsSkipped = 0;
                    break;
            }
        }
        return true;
    }

    private void skipCommentOrXmlTag(int bracketLevel) throws Exception {
        char delim = 0;
        int level = 1;
        if (bracketLevel == 0) {
            char c = getNextChar();
            if (c == '-') {
                char c2 = getNextChar();
                if (c2 == ']') {
                    bracketLevel--;
                } else if (c2 == '[') {
                    bracketLevel++;
                } else if (c2 == '-') {
                    skipComment();
                    return;
                }
            } else if (c == '[') {
                bracketLevel++;
            }
        }
        while (level > 0) {
            char c3 = getNextChar();
            if (delim == 0) {
                if (c3 == '\"' || c3 == '\'') {
                    delim = c3;
                } else if (bracketLevel <= 0) {
                    if (c3 == '<') {
                        level++;
                    } else if (c3 == '>') {
                        level--;
                    }
                }
                if (c3 == '[') {
                    bracketLevel++;
                } else if (c3 == ']') {
                    bracketLevel--;
                }
            } else if (c3 == delim) {
                delim = 0;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0090, code lost:
    
        if (r9 != '/') goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0093, code lost:
    
        r0 = getNextChar();
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x009d, code lost:
    
        if (r0 == '>') goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x00bb, code lost:
    
        throw new java.lang.Exception("Parsing error. Expected > but got: " + r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x00bc, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x00bd, code lost:
    
        emptyBuf(r0);
        r9 = getWhitespaces(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x00cd, code lost:
    
        if (r9 == '<') goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x00d0, code lost:
    
        pushBackChar(r9);
        getPCData(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00de, code lost:
    
        r9 = getNextChar();
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00e8, code lost:
    
        if (r9 != '!') goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00f0, code lost:
    
        if (checkCDATA(r0) == false) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00f3, code lost:
    
        getPCData(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00fb, code lost:
    
        r9 = getWhitespaces(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0106, code lost:
    
        if (r9 == '<') goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0109, code lost:
    
        pushBackChar(r9);
        getPCData(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x011b, code lost:
    
        if (r9 == '/') goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x011e, code lost:
    
        emptyBuf(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0127, code lost:
    
        if (r9 != '/') goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x012a, code lost:
    
        pushBackChar(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0137, code lost:
    
        if (r0.length() != 0) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x013e, code lost:
    
        if (r9 == '/') goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0145, code lost:
    
        if (r9 != '!') goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0148, code lost:
    
        r10 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x014e, code lost:
    
        if (r10 >= 2) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x015b, code lost:
    
        if (getNextChar() == '-') goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0167, code lost:
    
        throw new java.lang.Exception("Parsing error. Expected element or comment");
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0168, code lost:
    
        r10 = r10 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x016e, code lost:
    
        skipComment();
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0175, code lost:
    
        pushBackChar(r9);
        r0 = createAnotherElement();
        parseNode(r0);
        r6.addChild(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x018d, code lost:
    
        r0 = skipBlanks();
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0197, code lost:
    
        if (r0 == '<') goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x01b5, code lost:
    
        throw new java.lang.Exception("Parsing error. Expected <, but got: " + r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x01b6, code lost:
    
        r9 = getNextChar();
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x01bf, code lost:
    
        pushBackChar(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x01c5, code lost:
    
        r0 = getNextChar();
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x01cf, code lost:
    
        if (r0 == '/') goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x01ed, code lost:
    
        throw new java.lang.Exception("Parsing error. Expected /, but got: " + r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x01ee, code lost:
    
        pushBackChar(skipBlanks());
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x01fb, code lost:
    
        if (checkLiteral(r0) != false) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0218, code lost:
    
        throw new java.lang.Exception("Parsing error. Expected " + r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x021f, code lost:
    
        if (skipBlanks() == '>') goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x023d, code lost:
    
        throw new java.lang.Exception("Parsing error. Expected >, but got: " + r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x023e, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void parseNode(org.aspectj.weaver.loadtime.definition.LightXMLParser r6) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 575
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.aspectj.weaver.loadtime.definition.LightXMLParser.parseNode(org.aspectj.weaver.loadtime.definition.LightXMLParser):void");
    }

    private void skipComment() throws Exception {
        int dashes = 2;
        while (dashes > 0) {
            char ch2 = getNextChar();
            if (ch2 == '-') {
                dashes--;
            } else {
                dashes = 2;
            }
        }
        char nextChar = getNextChar();
        if (nextChar != '>') {
            throw new Exception("Parsing error. Expected > but got: " + nextChar);
        }
    }

    private boolean checkLiteral(String literal) throws Exception {
        int length = literal.length();
        for (int i = 0; i < length; i++) {
            if (getNextChar() != literal.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    private char getNextChar() throws Exception {
        if (this.pushedBackChar != 0) {
            char c = this.pushedBackChar;
            this.pushedBackChar = (char) 0;
            return c;
        }
        int i = this.reader.read();
        if (i < 0) {
            throw new Exception("Parsing error. Unexpected end of data");
        }
        return (char) i;
    }

    private void mapEntity(StringBuffer buf) throws Exception {
        char c;
        StringBuffer keyBuf = new StringBuffer();
        while (true) {
            char c2 = getNextChar();
            if (c2 == ';') {
                break;
            } else {
                keyBuf.append(c2);
            }
        }
        String key = keyBuf.toString();
        if (key.charAt(0) == '#') {
            try {
                if (key.charAt(1) == 'x') {
                    c = (char) Integer.parseInt(key.substring(2), 16);
                } else {
                    c = (char) Integer.parseInt(key.substring(1), 10);
                }
                buf.append(c);
                return;
            } catch (NumberFormatException e) {
                throw new Exception("Unknown entity: " + key);
            }
        }
        char[] value = entities.get(key);
        if (value == null) {
            throw new Exception("Unknown entity: " + key);
        }
        buf.append(value);
    }

    private void pushBackChar(char c) {
        this.pushedBackChar = c;
    }

    private void addChild(LightXMLParser child) {
        this.children.add(child);
    }

    private void setAttribute(String name, Object value) {
        this.attributes.put(name, value.toString());
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    private LightXMLParser createAnotherElement() {
        return new LightXMLParser();
    }

    private void setName(String name) {
        this.name = name;
    }

    private void emptyBuf(StringBuffer buf) {
        buf.setLength(0);
    }
}
