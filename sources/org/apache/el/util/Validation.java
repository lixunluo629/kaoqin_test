package org.apache.el.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;

/* loaded from: tomcat-embed-el-8.5.43.jar:org/apache/el/util/Validation.class */
public class Validation {
    private static final String[] invalidIdentifiers = {BeanDefinitionParserDelegate.ABSTRACT_ATTRIBUTE, "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", XmlErrorCodes.DOUBLE, "else", "enum", "extends", "false", "final", "finally", XmlErrorCodes.FLOAT, "for", "goto", "if", "implements", DefaultBeanDefinitionDocumentReader.IMPORT_ELEMENT, "instanceof", XmlErrorCodes.INT, JamXmlElements.INTERFACE, XmlErrorCodes.LONG, "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", OgnlContext.THIS_CONTEXT_KEY, "throw", "throws", "transient", "true", "try", "void", "volatile", "while"};
    private static final boolean IS_SECURITY_ENABLED;
    private static final boolean SKIP_IDENTIFIER_CHECK;

    static {
        String skipIdentifierCheckStr;
        IS_SECURITY_ENABLED = System.getSecurityManager() != null;
        if (IS_SECURITY_ENABLED) {
            skipIdentifierCheckStr = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: org.apache.el.util.Validation.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public String run() {
                    return System.getProperty("org.apache.el.parser.SKIP_IDENTIFIER_CHECK", "false");
                }
            });
        } else {
            skipIdentifierCheckStr = System.getProperty("org.apache.el.parser.SKIP_IDENTIFIER_CHECK", "false");
        }
        SKIP_IDENTIFIER_CHECK = Boolean.parseBoolean(skipIdentifierCheckStr);
    }

    private Validation() {
    }

    public static boolean isIdentifier(String key) {
        if (SKIP_IDENTIFIER_CHECK) {
            return true;
        }
        if (key == null || key.length() == 0) {
            return false;
        }
        int i = 0;
        int j = invalidIdentifiers.length;
        while (i < j) {
            int k = (i + j) >>> 1;
            int result = invalidIdentifiers[k].compareTo(key);
            if (result == 0) {
                return false;
            }
            if (result < 0) {
                i = k + 1;
            } else {
                j = k;
            }
        }
        if (!Character.isJavaIdentifierStart(key.charAt(0))) {
            return false;
        }
        for (int idx = 1; idx < key.length(); idx++) {
            if (!Character.isJavaIdentifierPart(key.charAt(idx))) {
                return false;
            }
        }
        return true;
    }
}
