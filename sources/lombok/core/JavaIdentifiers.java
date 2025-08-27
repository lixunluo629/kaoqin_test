package lombok.core;

import org.apache.ibatis.ognl.OgnlContext;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;

/* loaded from: lombok-1.16.22.jar:lombok/core/JavaIdentifiers.SCL.lombok */
public class JavaIdentifiers {
    private static final LombokImmutableList<String> KEYWORDS = LombokImmutableList.of("public", "private", "protected", "default", "switch", "case", "for", "do", "goto", "const", "strictfp", "while", "if", "else", "byte", "short", XmlErrorCodes.INT, XmlErrorCodes.LONG, XmlErrorCodes.FLOAT, XmlErrorCodes.DOUBLE, "void", "boolean", "char", "null", "false", "true", "continue", "break", "return", "instanceof", "synchronized", "volatile", "transient", "final", "static", JamXmlElements.INTERFACE, "class", "extends", "implements", "throws", "throw", "catch", "try", "finally", BeanDefinitionParserDelegate.ABSTRACT_ATTRIBUTE, "assert", "enum", DefaultBeanDefinitionDocumentReader.IMPORT_ELEMENT, "package", "native", "new", "super", OgnlContext.THIS_CONTEXT_KEY);

    private JavaIdentifiers() {
    }

    public static boolean isValidJavaIdentifier(String identifier) {
        if (identifier == null || identifier.isEmpty() || !Character.isJavaIdentifierStart(identifier.charAt(0))) {
            return false;
        }
        for (int i = 1; i < identifier.length(); i++) {
            if (!Character.isJavaIdentifierPart(identifier.charAt(i))) {
                return false;
            }
        }
        return !isKeyword(identifier);
    }

    public static boolean isKeyword(String keyword) {
        return KEYWORDS.contains(keyword);
    }
}
