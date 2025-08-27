package org.apache.xmlbeans;

import java.util.HashMap;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlOptionCharEscapeMap.class */
public class XmlOptionCharEscapeMap {
    public static final int PREDEF_ENTITY = 0;
    public static final int DECIMAL = 1;
    public static final int HEXADECIMAL = 2;
    private HashMap _charMap = new HashMap();
    private static final HashMap _predefEntities = new HashMap();

    static {
        _predefEntities.put(new Character('<'), "&lt;");
        _predefEntities.put(new Character('>'), "&gt;");
        _predefEntities.put(new Character('&'), "&amp;");
        _predefEntities.put(new Character('\''), "&apos;");
        _predefEntities.put(new Character('\"'), "&quot;");
    }

    public boolean containsChar(char ch2) {
        return this._charMap.containsKey(new Character(ch2));
    }

    public void addMapping(char ch2, int mode) throws XmlException {
        Character theChar = new Character(ch2);
        switch (mode) {
            case 0:
                String replString = (String) _predefEntities.get(theChar);
                if (replString == null) {
                    throw new XmlException("XmlOptionCharEscapeMap.addMapping(): the PREDEF_ENTITY mode can only be used for the following characters: <, >, &, \" and '");
                }
                this._charMap.put(theChar, replString);
                return;
            case 1:
                this._charMap.put(theChar, "&#" + ((int) ch2) + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
                return;
            case 2:
                String hexCharPoint = Integer.toHexString(ch2);
                this._charMap.put(theChar, "&#x" + hexCharPoint + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
                return;
            default:
                throw new XmlException("XmlOptionCharEscapeMap.addMapping(): mode must be PREDEF_ENTITY, DECIMAL or HEXADECIMAL");
        }
    }

    public void addMappings(char ch1, char ch2, int mode) throws XmlException {
        if (ch1 > ch2) {
            throw new XmlException("XmlOptionCharEscapeMap.addMappings(): ch1 must be <= ch2");
        }
        char c = ch1;
        while (true) {
            char c2 = c;
            if (c2 <= ch2) {
                addMapping(c2, mode);
                c = (char) (c2 + 1);
            } else {
                return;
            }
        }
    }

    public String getEscapedString(char ch2) {
        return (String) this._charMap.get(new Character(ch2));
    }
}
