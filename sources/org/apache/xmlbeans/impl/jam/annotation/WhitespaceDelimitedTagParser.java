package org.apache.xmlbeans.impl.jam.annotation;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.sun.javadoc.Tag;
import java.util.Enumeration;
import java.util.Properties;
import org.apache.xmlbeans.impl.jam.mutable.MAnnotatedElement;
import org.apache.xmlbeans.impl.jam.mutable.MAnnotation;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/annotation/WhitespaceDelimitedTagParser.class */
public class WhitespaceDelimitedTagParser extends JavadocTagParser {
    @Override // org.apache.xmlbeans.impl.jam.annotation.JavadocTagParser
    public void parse(MAnnotatedElement target, Tag tag) {
        MAnnotation[] anns = createAnnotations(target, tag);
        String tagText = tag.text();
        if (tagText == null) {
            return;
        }
        String tagText2 = tagText.trim();
        if (tagText2.length() == 0) {
            return;
        }
        Properties props = new Properties();
        parseAssignments(props, tagText2);
        if (props.size() > 0) {
            Enumeration names = props.propertyNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                setValue(anns, name, props.getProperty(name));
            }
            return;
        }
        setSingleValueText(anns, tag);
    }

    private void parseAssignments(Properties out, String line) {
        char c;
        int ind;
        int valueStart;
        int valueEnd;
        String strSubstring;
        getLogger().verbose("PARSING LINE " + line, this);
        String line2 = removeComments(line);
        while (null != line2 && -1 != line2.indexOf(SymbolConstants.EQUAL_SYMBOL)) {
            int ind2 = 0;
            char cCharAt = line2.charAt(0);
            while (true) {
                c = cCharAt;
                if (!isBlank(c)) {
                    break;
                }
                ind2++;
                cCharAt = line2.charAt(ind2);
            }
            int keyStart = ind2;
            while (isLegal(line2.charAt(ind2))) {
                ind2++;
            }
            int keyEnd = ind2;
            String key = line2.substring(keyStart, keyEnd);
            int ind3 = line2.indexOf(SymbolConstants.EQUAL_SYMBOL);
            if (ind3 == -1) {
                return;
            }
            int ind4 = ind3 + 1;
            try {
                c = line2.charAt(ind4);
            } catch (StringIndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }
            while (isBlank(c)) {
                ind4++;
                c = line2.charAt(ind4);
            }
            if (c == '\"') {
                ind = ind4 + 1;
                valueStart = ind;
                while ('\"' != line2.charAt(ind)) {
                    ind++;
                    if (ind >= line2.length()) {
                        getLogger().verbose("missing double quotes on line " + line2, this);
                    }
                }
                valueEnd = ind;
            } else {
                int i = ind4;
                ind = ind4 + 1;
                valueStart = i;
                while (ind < line2.length() && isLegal(line2.charAt(ind))) {
                    ind++;
                }
                valueEnd = ind;
            }
            String value = line2.substring(valueStart, valueEnd);
            if (ind < line2.length()) {
                strSubstring = line2.substring(ind + 1);
            } else {
                strSubstring = null;
            }
            line2 = strSubstring;
            getLogger().verbose("SETTING KEY:" + key + " VALUE:" + value, this);
            out.setProperty(key, value);
        }
    }

    private String removeComments(String value) {
        String result;
        String result2 = "";
        int size = value.length();
        String current = value;
        int currentIndex = 0;
        int beginning = current.indexOf("//");
        int doubleQuotesIndex = current.indexOf(SymbolConstants.QUOTES_SYMBOL);
        if (-1 != doubleQuotesIndex && doubleQuotesIndex < beginning) {
            result = value;
        } else {
            while (currentIndex < size && beginning != -1) {
                beginning = value.indexOf("//", currentIndex);
                if (-1 != beginning) {
                    if (beginning > 0 && value.charAt(beginning - 1) == ':') {
                        currentIndex = beginning + 2;
                    } else {
                        int end = value.indexOf(10, beginning);
                        if (-1 == end) {
                            end = size;
                        }
                        result2 = result2 + value.substring(currentIndex, beginning).trim() + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR;
                        current = value.substring(end);
                        currentIndex = end;
                    }
                }
            }
            result = result2 + current;
        }
        return result.trim();
    }

    private boolean isBlank(char c) {
        return c == ' ' || c == '\t' || c == '\n';
    }

    private boolean isLegal(char c) {
        return (isBlank(c) || c == '=') ? false : true;
    }
}
