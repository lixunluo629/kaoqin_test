package org.apache.xmlbeans.impl.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.hyperic.sigar.win32.EventLog;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import org.springframework.validation.DataBinder;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/NameUtil.class */
public class NameUtil {
    public static final char HYPHEN = '-';
    public static final char PERIOD = '.';
    public static final char COLON = ':';
    public static final char USCORE = '_';
    public static final char DOT = 183;
    public static final char TELEIA = 903;
    public static final char AYAH = 1757;
    public static final char ELHIZB = 1758;
    private static final boolean DEBUG = false;
    private static final Set javaWords = new HashSet(Arrays.asList("assert", BeanDefinitionParserDelegate.ABSTRACT_ATTRIBUTE, "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", XmlErrorCodes.DOUBLE, "else", "enum", "extends", "false", "final", "finally", XmlErrorCodes.FLOAT, "for", "goto", "if", "implements", DefaultBeanDefinitionDocumentReader.IMPORT_ELEMENT, "instanceof", XmlErrorCodes.INT, JamXmlElements.INTERFACE, XmlErrorCodes.LONG, "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", OgnlContext.THIS_CONTEXT_KEY, "threadsafe", "throw", "throws", "transient", "true", "try", "void", "volatile", "while"));
    private static final Set extraWords = new HashSet(Arrays.asList("i", DataBinder.DEFAULT_OBJECT_NAME, "org", "com"));
    private static final Set javaNames = new HashSet(Arrays.asList("CharSequence", "Cloneable", "Comparable", "Runnable", "Boolean", "Byte", "Character", "Class", "ClassLoader", "Compiler", "Double", "Float", "InheritableThreadLocal", "Integer", "Long", "Math", "Number", "Object", "Package", "Process", "Runtime", "RuntimePermission", "SecurityManager", "Short", "StackTraceElement", "StrictMath", "String", "StringBuffer", EventLog.SYSTEM, "Thread", "ThreadGroup", "ThreadLocal", "Throwable", "Void", "ArithmeticException", "ArrayIndexOutOfBoundsException", "ArrayStoreException", "ClassCastException", "ClassNotFoundException", "CloneNotSupportedException", "Exception", "IllegalAccessException", "IllegalArgumentException", "IllegalMonitorStateException", "IllegalStateException", "IllegalThreadStateException", "IndexOutOfBoundsException", "InstantiationException", "InterruptedException", "NegativeArraySizeException", "NoSuchFieldException", "NoSuchMethodException", "NullPointerException", "NumberFormatException", "RuntimeException", "SecurityException", "StringIndexOutOfBoundsException", "UnsupportedOperationException", "AbstractMethodError", "AssertionError", "ClassCircularityError", "ClassFormatError", "Error", "ExceptionInInitializerError", "IllegalAccessError", "IncompatibleClassChangeError", "InstantiationError", "InternalError", "LinkageError", "NoClassDefFoundError", "NoSuchFieldError", "NoSuchMethodError", "OutOfMemoryError", "StackOverflowError", "ThreadDeath", "UnknownError", "UnsatisfiedLinkError", "UnsupportedClassVersionError", "VerifyError", "VirtualMachineError", "BigInteger", "BigDecimal", "Enum", "Date", "GDate", "GDuration", XmlErrorCodes.QNAME, "List", "XmlObject", "XmlCursor", "XmlBeans", "SchemaType"));
    private static final String JAVA_NS_PREFIX = "java:";
    private static final String LANG_PREFIX = "java.";
    private static final int START = 0;
    private static final int PUNCT = 1;
    private static final int DIGIT = 2;
    private static final int MARK = 3;
    private static final int UPPER = 4;
    private static final int LOWER = 5;
    private static final int NOCASE = 6;

    public static boolean isValidJavaIdentifier(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        int len = id.length();
        if (len == 0 || javaWords.contains(id) || !Character.isJavaIdentifierStart(id.charAt(0))) {
            return false;
        }
        for (int i = 1; i < len; i++) {
            if (!Character.isJavaIdentifierPart(id.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String getClassNameFromQName(QName qname) {
        return getClassNameFromQName(qname, false);
    }

    public static String getClassNameFromQName(QName qname, boolean useJaxRpcRules) {
        String java_type = upperCamelCase(qname.getLocalPart(), useJaxRpcRules);
        String uri = qname.getNamespaceURI();
        String java_pkg = getPackageFromNamespace(uri, useJaxRpcRules);
        if (java_pkg != null) {
            return java_pkg + "." + java_type;
        }
        return java_type;
    }

    public static String getNamespaceFromPackage(Class clazz) {
        Class componentType = clazz;
        while (true) {
            Class curr_clazz = componentType;
            if (!curr_clazz.isArray()) {
                break;
            }
            componentType = curr_clazz.getComponentType();
        }
        String fullname = clazz.getName();
        int lastdot = fullname.lastIndexOf(46);
        String pkg_name = lastdot < 0 ? "" : fullname.substring(0, lastdot);
        return "java:" + pkg_name;
    }

    private static boolean isUriSchemeChar(char ch2) {
        return (ch2 >= 'a' && ch2 <= 'z') || (ch2 >= 'A' && ch2 <= 'Z') || ((ch2 >= '0' && ch2 <= '9') || ch2 == '-' || ch2 == '.' || ch2 == '+');
    }

    private static boolean isUriAlphaChar(char ch2) {
        return (ch2 >= 'a' && ch2 <= 'z') || (ch2 >= 'A' && ch2 <= 'Z');
    }

    private static int findSchemeColon(String uri) {
        int len = uri.length();
        if (len == 0 || !isUriAlphaChar(uri.charAt(0))) {
            return -1;
        }
        int i = 1;
        while (i < len && isUriSchemeChar(uri.charAt(i))) {
            i++;
        }
        if (i == len || uri.charAt(i) != ':') {
            return -1;
        }
        while (i < len && uri.charAt(i) == ':') {
            i++;
        }
        return i - 1;
    }

    private static String jls77String(String name) {
        StringBuffer buf = new StringBuffer(name);
        for (int i = 0; i < name.length(); i++) {
            if (!Character.isJavaIdentifierPart(buf.charAt(i)) || '$' == buf.charAt(i)) {
                buf.setCharAt(i, '_');
            }
        }
        if (buf.length() == 0 || !Character.isJavaIdentifierStart(buf.charAt(0))) {
            buf.insert(0, '_');
        }
        if (isJavaReservedWord(name)) {
            buf.append('_');
        }
        return buf.toString();
    }

    private static List splitDNS(String dns) {
        List result = new ArrayList();
        int end = dns.length();
        for (int begin = dns.lastIndexOf(46); begin != -1; begin--) {
            if (dns.charAt(begin) == '.') {
                result.add(jls77String(dns.substring(begin + 1, end)));
                end = begin;
            }
        }
        result.add(jls77String(dns.substring(0, end)));
        if (result.size() >= 3 && ((String) result.get(result.size() - 1)).toLowerCase().equals("www")) {
            result.remove(result.size() - 1);
        }
        return result;
    }

    private static String processFilename(String filename) {
        int i = filename.lastIndexOf(46);
        if (i > 0 && (i + 1 + 2 == filename.length() || i + 1 + 3 == filename.length() || filename.substring(i + 1).toLowerCase() == "html")) {
            return filename.substring(0, i);
        }
        return filename;
    }

    public static String getPackageFromNamespace(String uri) {
        return getPackageFromNamespace(uri, false);
    }

    public static String getPackageFromNamespace(String uri, boolean useJaxRpcRules) {
        List result;
        if (uri == null || uri.length() == 0) {
            return "noNamespace";
        }
        int len = uri.length();
        int i = findSchemeColon(uri);
        if (i == len - 1) {
            result = new ArrayList();
            result.add(uri.substring(0, i));
        } else if (i >= 0 && uri.substring(0, i).equals("java")) {
            result = Arrays.asList(uri.substring(i + 1).split("\\."));
        } else {
            result = new ArrayList();
            int i2 = i + 1;
            loop1: while (i2 < len) {
                while (uri.charAt(i2) == '/') {
                    i2++;
                    if (i2 >= len) {
                        break loop1;
                    }
                }
                int start = i2;
                while (uri.charAt(i2) != '/') {
                    i2++;
                    if (i2 >= len) {
                        break;
                    }
                }
                int end = i2;
                result.add(uri.substring(start, end));
            }
            if (result.size() > 1) {
                result.set(result.size() - 1, processFilename((String) result.get(result.size() - 1)));
            }
            if (result.size() > 0) {
                List splitdns = splitDNS((String) result.get(0));
                result.remove(0);
                result.addAll(0, splitdns);
            }
        }
        StringBuffer buf = new StringBuffer();
        Iterator it = result.iterator();
        while (it.hasNext()) {
            String part = nonJavaKeyword(lowerCamelCase((String) it.next(), useJaxRpcRules, true));
            if (part.length() > 0) {
                buf.append(part);
                buf.append('.');
            }
        }
        if (buf.length() == 0) {
            return "noNamespace";
        }
        if (useJaxRpcRules) {
            return buf.substring(0, buf.length() - 1).toLowerCase();
        }
        return buf.substring(0, buf.length() - 1);
    }

    public static void main(String[] args) {
        for (String str : args) {
            System.out.println(upperCaseUnderbar(str));
        }
    }

    public static String upperCaseUnderbar(String xml_name) {
        StringBuffer buf = new StringBuffer();
        List words = splitWords(xml_name, false);
        int sz = words.size() - 1;
        if (sz >= 0 && !Character.isJavaIdentifierStart(((String) words.get(0)).charAt(0))) {
            buf.append("X_");
        }
        for (int i = 0; i < sz; i++) {
            buf.append((String) words.get(i));
            buf.append('_');
        }
        if (sz >= 0) {
            buf.append((String) words.get(sz));
        }
        int len = buf.length();
        for (int j = 0; j < len; j++) {
            char c = buf.charAt(j);
            buf.setCharAt(j, Character.toUpperCase(c));
        }
        return buf.toString();
    }

    public static String upperCamelCase(String xml_name) {
        return upperCamelCase(xml_name, false);
    }

    public static String upperCamelCase(String xml_name, boolean useJaxRpcRules) {
        StringBuffer buf = new StringBuffer();
        List words = splitWords(xml_name, useJaxRpcRules);
        if (words.size() > 0) {
            if (!Character.isJavaIdentifierStart(((String) words.get(0)).charAt(0))) {
                buf.append("X");
            }
            Iterator itr = words.iterator();
            while (itr.hasNext()) {
                buf.append((String) itr.next());
            }
        }
        return buf.toString();
    }

    public static String lowerCamelCase(String xml_name) {
        return lowerCamelCase(xml_name, false, true);
    }

    public static String lowerCamelCase(String xml_name, boolean useJaxRpcRules, boolean fixGeneratedName) {
        StringBuffer buf = new StringBuffer();
        List words = splitWords(xml_name, useJaxRpcRules);
        if (words.size() > 0) {
            String first = ((String) words.get(0)).toLowerCase();
            char f = first.charAt(0);
            if (!Character.isJavaIdentifierStart(f) && fixGeneratedName) {
                buf.append("x");
            }
            buf.append(first);
            Iterator itr = words.iterator();
            itr.next();
            while (itr.hasNext()) {
                buf.append((String) itr.next());
            }
        }
        return buf.toString();
    }

    public static String upperCaseFirstLetter(String s) {
        if (s.length() == 0 || Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        StringBuffer buf = new StringBuffer(s);
        buf.setCharAt(0, Character.toUpperCase(buf.charAt(0)));
        return buf.toString();
    }

    private static void addCapped(List list, String str) {
        if (str.length() > 0) {
            list.add(upperCaseFirstLetter(str));
        }
    }

    public static List splitWords(String name, boolean useJaxRpcRules) {
        List list = new ArrayList();
        int len = name.length();
        int start = 0;
        int prefix = 0;
        int i = 0;
        while (i < len) {
            int current = getCharClass(name.charAt(i), useJaxRpcRules);
            if (prefix != 1 && current == 1) {
                addCapped(list, name.substring(start, i));
                do {
                    int charClass = getCharClass(name.charAt(i), useJaxRpcRules);
                    current = charClass;
                    if (charClass == 1) {
                        i++;
                    } else {
                        start = i;
                    }
                } while (i < len);
                return list;
            }
            if ((prefix == 2) != (current == 2) || ((prefix == 5 && current != 5) || isLetter(prefix) != isLetter(current))) {
                addCapped(list, name.substring(start, i));
                start = i;
            } else if (prefix == 4 && current == 5 && i > start + 1) {
                addCapped(list, name.substring(start, i - 1));
                start = i - 1;
            }
            prefix = current;
            i++;
        }
        addCapped(list, name.substring(start));
        return list;
    }

    public static int getCharClass(char c, boolean useJaxRpcRules) {
        if (isPunctuation(c, useJaxRpcRules)) {
            return 1;
        }
        if (Character.isDigit(c)) {
            return 2;
        }
        if (Character.isUpperCase(c)) {
            return 4;
        }
        if (Character.isLowerCase(c)) {
            return 5;
        }
        if (Character.isLetter(c)) {
            return 6;
        }
        if (Character.isJavaIdentifierPart(c)) {
            return 3;
        }
        return 1;
    }

    private static boolean isLetter(int state) {
        return state == 4 || state == 5 || state == 6;
    }

    public static boolean isPunctuation(char c, boolean useJaxRpcRules) {
        return c == '-' || c == '.' || c == ':' || c == 183 || (c == '_' && !useJaxRpcRules) || c == 903 || c == 1757 || c == 1758;
    }

    public static String nonJavaKeyword(String word) {
        if (isJavaReservedWord(word)) {
            return 'x' + word;
        }
        return word;
    }

    public static String nonExtraKeyword(String word) {
        if (isExtraReservedWord(word, true)) {
            return word + "Value";
        }
        return word;
    }

    public static String nonJavaCommonClassName(String name) {
        if (isJavaCommonClassName(name)) {
            return "X" + name;
        }
        return name;
    }

    private static boolean isJavaReservedWord(String word) {
        return isJavaReservedWord(word, true);
    }

    private static boolean isJavaReservedWord(String word, boolean ignore_case) {
        if (ignore_case) {
            word = word.toLowerCase();
        }
        return javaWords.contains(word);
    }

    private static boolean isExtraReservedWord(String word, boolean ignore_case) {
        if (ignore_case) {
            word = word.toLowerCase();
        }
        return extraWords.contains(word);
    }

    public static boolean isJavaCommonClassName(String word) {
        return javaNames.contains(word);
    }
}
