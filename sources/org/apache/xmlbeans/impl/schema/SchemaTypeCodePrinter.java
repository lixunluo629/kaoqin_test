package org.apache.xmlbeans.impl.schema;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.alibaba.excel.constant.ExcelXmlConstants;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.InterfaceExtension;
import org.apache.xmlbeans.PrePostExtension;
import org.apache.xmlbeans.SchemaCodePrinter;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaStringEnumEntry;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.SystemProperties;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.NameUtil;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import redis.clients.jedis.Protocol;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaTypeCodePrinter.class */
public final class SchemaTypeCodePrinter implements SchemaCodePrinter {
    Writer _writer;
    int _indent = 0;
    boolean _useJava15;
    static final String LINE_SEPARATOR;
    static final String MAX_SPACES = "                                        ";
    static final int INDENT_INCREMENT = 4;
    public static final String INDEX_CLASSNAME = "TypeSystemHolder";
    private static final int NOTHING = 1;
    private static final int ADD_NEW_VALUE = 3;
    private static final int THROW_EXCEPTION = 4;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SchemaTypeCodePrinter.class.desiredAssertionStatus();
        LINE_SEPARATOR = SystemProperties.getProperty("line.separator") == null ? ScriptUtils.FALLBACK_STATEMENT_SEPARATOR : SystemProperties.getProperty("line.separator");
    }

    public static void printTypeImpl(Writer writer, SchemaType sType, XmlOptions opt) throws IOException {
        getPrinter(opt).printTypeImpl(writer, sType);
    }

    public static void printType(Writer writer, SchemaType sType, XmlOptions opt) throws IOException {
        getPrinter(opt).printType(writer, sType);
    }

    public static void printLoader(Writer writer, SchemaTypeSystem system, XmlOptions opt) throws IOException {
        getPrinter(opt).printLoader(writer, system);
    }

    private static SchemaCodePrinter getPrinter(XmlOptions opt) {
        Object printer = XmlOptions.safeGet(opt, XmlOptions.SCHEMA_CODE_PRINTER);
        if (printer == null || !(printer instanceof SchemaCodePrinter)) {
            printer = new SchemaTypeCodePrinter(opt);
        }
        return (SchemaCodePrinter) printer;
    }

    public SchemaTypeCodePrinter(XmlOptions opt) {
        String genversion = null;
        if (opt != null && XmlOptions.hasOption(opt, XmlOptions.GENERATE_JAVA_VERSION)) {
            genversion = (String) opt.get(XmlOptions.GENERATE_JAVA_VERSION);
        }
        this._useJava15 = "1.5".equals(genversion == null ? XmlOptions.GENERATE_JAVA_14 : genversion);
    }

    void indent() {
        this._indent += 4;
    }

    void outdent() {
        this._indent -= 4;
    }

    String encodeString(String s) {
        StringBuffer sb = new StringBuffer();
        sb.append('\"');
        for (int i = 0; i < s.length(); i++) {
            char ch2 = s.charAt(i);
            if (ch2 == '\"') {
                sb.append('\\');
                sb.append('\"');
            } else if (ch2 == '\\') {
                sb.append('\\');
                sb.append('\\');
            } else if (ch2 == '\r') {
                sb.append('\\');
                sb.append('r');
            } else if (ch2 == '\n') {
                sb.append('\\');
                sb.append('n');
            } else if (ch2 == '\t') {
                sb.append('\\');
                sb.append('t');
            } else {
                sb.append(ch2);
            }
        }
        sb.append('\"');
        return sb.toString();
    }

    void emit(String s) throws IOException {
        int indent = this._indent;
        if (indent > MAX_SPACES.length() / 2) {
            indent = (MAX_SPACES.length() / 4) + (indent / 2);
        }
        if (indent > MAX_SPACES.length()) {
            indent = MAX_SPACES.length();
        }
        this._writer.write(MAX_SPACES.substring(0, indent));
        try {
            this._writer.write(s);
        } catch (CharacterCodingException e) {
            this._writer.write(makeSafe(s));
        }
        this._writer.write(LINE_SEPARATOR);
    }

    private static String makeSafe(String s) {
        Charset charset = Charset.forName(System.getProperty("file.encoding"));
        if (charset == null) {
            throw new IllegalStateException("Default character set is null!");
        }
        CharsetEncoder cEncoder = charset.newEncoder();
        StringBuffer result = new StringBuffer();
        int i = 0;
        while (i < s.length() && cEncoder.canEncode(s.charAt(i))) {
            i++;
        }
        while (i < s.length()) {
            char c = s.charAt(i);
            if (cEncoder.canEncode(c)) {
                result.append(c);
            } else {
                String hexValue = Integer.toHexString(c);
                switch (hexValue.length()) {
                    case 1:
                        result.append("\\u000").append(hexValue);
                        break;
                    case 2:
                        result.append("\\u00").append(hexValue);
                        break;
                    case 3:
                        result.append("\\u0").append(hexValue);
                        break;
                    case 4:
                        result.append("\\u").append(hexValue);
                        break;
                    default:
                        throw new IllegalStateException();
                }
            }
            i++;
        }
        return result.toString();
    }

    @Override // org.apache.xmlbeans.SchemaCodePrinter
    public void printType(Writer writer, SchemaType sType) throws IOException {
        this._writer = writer;
        printTopComment(sType);
        printPackage(sType, true);
        emit("");
        printInnerType(sType, sType.getTypeSystem());
        this._writer.flush();
    }

    @Override // org.apache.xmlbeans.SchemaCodePrinter
    public void printTypeImpl(Writer writer, SchemaType sType) throws IOException {
        this._writer = writer;
        printTopComment(sType);
        printPackage(sType, false);
        printInnerTypeImpl(sType, sType.getTypeSystem(), false);
    }

    String findJavaType(SchemaType sType) {
        while (sType.getFullJavaName() == null) {
            sType = sType.getBaseType();
        }
        return sType.getFullJavaName();
    }

    static String prettyQName(QName qname) {
        String result = qname.getLocalPart();
        if (qname.getNamespaceURI() != null) {
            result = result + "(@" + qname.getNamespaceURI() + ")";
        }
        return result;
    }

    void printInnerTypeJavaDoc(SchemaType sType) throws IOException {
        QName name = sType.getName();
        if (name == null) {
            if (sType.isDocumentType()) {
                name = sType.getDocumentElementName();
            } else if (sType.isAttributeType()) {
                name = sType.getAttributeTypeAttributeName();
            } else if (sType.getContainerField() != null) {
                name = sType.getContainerField().getName();
            }
        }
        emit("/**");
        if (sType.isDocumentType()) {
            emit(" * A document containing one " + prettyQName(name) + " element.");
        } else if (sType.isAttributeType()) {
            emit(" * A document containing one " + prettyQName(name) + " attribute.");
        } else if (name != null) {
            emit(" * An XML " + prettyQName(name) + ".");
        } else {
            emit(" * An anonymous inner XML type.");
        }
        emit(" *");
        switch (sType.getSimpleVariety()) {
            case 0:
                emit(" * This is a complex type.");
                break;
            case 1:
                emit(" * This is an atomic type that is a restriction of " + getFullJavaName(sType) + ".");
                break;
            case 2:
                emit(" * This is a union type. Instances are of one of the following types:");
                SchemaType[] members = sType.getUnionConstituentTypes();
                for (SchemaType schemaType : members) {
                    emit(" *     " + schemaType.getFullJavaName());
                }
                break;
            case 3:
                emit(" * This is a list type whose items are " + sType.getListItemType().getFullJavaName() + ".");
                break;
        }
        emit(" */");
    }

    private String getFullJavaName(SchemaType sType) {
        SchemaTypeImpl sTypeI = (SchemaTypeImpl) sType;
        String ret = sTypeI.getFullJavaName();
        while (sTypeI.isRedefinition()) {
            ret = sTypeI.getFullJavaName();
            sTypeI = (SchemaTypeImpl) sTypeI.getBaseType();
        }
        return ret;
    }

    private String getUserTypeStaticHandlerMethod(boolean encode, SchemaTypeImpl stype) {
        String unqualifiedName;
        String unqualifiedName2 = stype.getName().getLocalPart();
        if (unqualifiedName2.length() < 2) {
            unqualifiedName = unqualifiedName2.toUpperCase();
        } else {
            unqualifiedName = unqualifiedName2.substring(0, 1).toUpperCase() + unqualifiedName2.substring(1);
        }
        if (encode) {
            return stype.getUserTypeHandlerName() + ".encode" + unqualifiedName;
        }
        return stype.getUserTypeHandlerName() + ".decode" + unqualifiedName;
    }

    public static String indexClassForSystem(SchemaTypeSystem system) {
        String name = system.getName();
        return name + "." + INDEX_CLASSNAME;
    }

    static String shortIndexClassForSystem(SchemaTypeSystem system) {
        return INDEX_CLASSNAME;
    }

    void printStaticTypeDeclaration(SchemaType sType, SchemaTypeSystem system) throws IOException {
        String interfaceShortName = sType.getShortJavaName();
        emit("public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)");
        indent();
        emit("org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(" + interfaceShortName + ".class.getClassLoader(), \"" + system.getName() + "\").resolveHandle(\"" + ((SchemaTypeSystemImpl) system).handleForType(sType) + "\");");
        outdent();
    }

    @Override // org.apache.xmlbeans.SchemaCodePrinter
    public void printLoader(Writer writer, SchemaTypeSystem system) throws IOException {
    }

    void printInnerType(SchemaType sType, SchemaTypeSystem system) throws IOException {
        emit("");
        printInnerTypeJavaDoc(sType);
        startInterface(sType);
        printStaticTypeDeclaration(sType, system);
        if (sType.isSimpleType()) {
            if (sType.hasStringEnumValues()) {
                printStringEnumeration(sType);
            }
        } else {
            if (sType.getContentType() == 2 && sType.hasStringEnumValues()) {
                printStringEnumeration(sType);
            }
            SchemaProperty[] props = getDerivedProperties(sType);
            for (SchemaProperty prop : props) {
                printPropertyGetters(prop.getName(), prop.isAttribute(), prop.getJavaPropertyName(), prop.getJavaTypeCode(), javaTypeForProperty(prop), xmlTypeForProperty(prop), prop.hasNillable() != 0, prop.extendsJavaOption(), prop.extendsJavaArray(), prop.extendsJavaSingleton());
                if (!prop.isReadOnly()) {
                    printPropertySetters(prop.getName(), prop.isAttribute(), prop.getJavaPropertyName(), prop.getJavaTypeCode(), javaTypeForProperty(prop), xmlTypeForProperty(prop), prop.hasNillable() != 0, prop.extendsJavaOption(), prop.extendsJavaArray(), prop.extendsJavaSingleton());
                }
            }
        }
        printNestedInnerTypes(sType, system);
        printFactory(sType);
        endBlock();
    }

    void printFactory(SchemaType sType) throws IOException {
        boolean fullFactory = true;
        if (sType.isAnonymousType() && !sType.isDocumentType() && !sType.isAttributeType()) {
            fullFactory = false;
        }
        String fullName = sType.getFullJavaName().replace('$', '.');
        emit("");
        emit("/**");
        emit(" * A factory class with static methods for creating instances");
        emit(" * of this type.");
        emit(" */");
        emit("");
        emit("public static final class Factory");
        emit("{");
        indent();
        if (sType.isSimpleType()) {
            emit("public static " + fullName + " newValue(java.lang.Object obj) {");
            emit("  return (" + fullName + ") type.newValue( obj ); }");
            emit("");
        }
        if (sType.isAbstract()) {
            emit("/** @deprecated No need to be able to create instances of abstract types */");
            if (this._useJava15) {
                emit("@Deprecated");
            }
        }
        emit("public static " + fullName + " newInstance() {");
        emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }");
        emit("");
        if (sType.isAbstract()) {
            emit("/** @deprecated No need to be able to create instances of abstract types */");
            if (this._useJava15) {
                emit("@Deprecated");
            }
        }
        emit("public static " + fullName + " newInstance(org.apache.xmlbeans.XmlOptions options) {");
        emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }");
        emit("");
        if (fullFactory) {
            emit("/** @param xmlAsString the string value to parse */");
            emit("public static " + fullName + " parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {");
            emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }");
            emit("");
            emit("public static " + fullName + " parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {");
            emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }");
            emit("");
            emit("/** @param file the file from which to load an xml document */");
            emit("public static " + fullName + " parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {");
            emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }");
            emit("");
            emit("public static " + fullName + " parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {");
            emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }");
            emit("");
            emit("public static " + fullName + " parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {");
            emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }");
            emit("");
            emit("public static " + fullName + " parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {");
            emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }");
            emit("");
            emit("public static " + fullName + " parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {");
            emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }");
            emit("");
            emit("public static " + fullName + " parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {");
            emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }");
            emit("");
            emit("public static " + fullName + " parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {");
            emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }");
            emit("");
            emit("public static " + fullName + " parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {");
            emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }");
            emit("");
            emit("public static " + fullName + " parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {");
            emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }");
            emit("");
            emit("public static " + fullName + " parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {");
            emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }");
            emit("");
            emit("public static " + fullName + " parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {");
            emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }");
            emit("");
            emit("public static " + fullName + " parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {");
            emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }");
            emit("");
            emit("/** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */");
            if (this._useJava15) {
                emit("@Deprecated");
            }
            emit("public static " + fullName + " parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {");
            emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }");
            emit("");
            emit("/** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */");
            if (this._useJava15) {
                emit("@Deprecated");
            }
            emit("public static " + fullName + " parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {");
            emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }");
            emit("");
            emit("/** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */");
            if (this._useJava15) {
                emit("@Deprecated");
            }
            emit("public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {");
            emit("  return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }");
            emit("");
            emit("/** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */");
            if (this._useJava15) {
                emit("@Deprecated");
            }
            emit("public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {");
            emit("  return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }");
            emit("");
        }
        emit("private Factory() { } // No instance of this class allowed");
        outdent();
        emit("}");
    }

    void printNestedInnerTypes(SchemaType sType, SchemaTypeSystem system) throws IOException {
        boolean redefinition = sType.getName() != null && sType.getName().equals(sType.getBaseType().getName());
        while (sType != null) {
            SchemaType[] anonTypes = sType.getAnonymousTypes();
            for (int i = 0; i < anonTypes.length; i++) {
                if (anonTypes[i].isSkippedAnonymousType()) {
                    printNestedInnerTypes(anonTypes[i], system);
                } else {
                    printInnerType(anonTypes[i], system);
                }
            }
            if (!redefinition) {
                return;
            }
            if (sType.getDerivationType() == 2 || sType.isSimpleType()) {
                sType = sType.getBaseType();
            } else {
                return;
            }
        }
    }

    void printTopComment(SchemaType sType) throws IOException {
        emit(ScriptUtils.DEFAULT_BLOCK_COMMENT_START_DELIMITER);
        if (sType.getName() != null) {
            emit(" * XML Type:  " + sType.getName().getLocalPart());
            emit(" * Namespace: " + sType.getName().getNamespaceURI());
        } else {
            QName thename = null;
            if (sType.isDocumentType()) {
                thename = sType.getDocumentElementName();
                emit(" * An XML document type.");
            } else if (sType.isAttributeType()) {
                thename = sType.getAttributeTypeAttributeName();
                emit(" * An XML attribute type.");
            } else if (!$assertionsDisabled) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && thename == null) {
                throw new AssertionError();
            }
            emit(" * Localname: " + thename.getLocalPart());
            emit(" * Namespace: " + thename.getNamespaceURI());
        }
        emit(" * Java type: " + sType.getFullJavaName());
        emit(" *");
        emit(" * Automatically generated - do not modify.");
        emit(" */");
    }

    void printPackage(SchemaType sType, boolean intf) throws IOException {
        String fqjn;
        if (intf) {
            fqjn = sType.getFullJavaName();
        } else {
            fqjn = sType.getFullJavaImplName();
        }
        int lastdot = fqjn.lastIndexOf(46);
        if (lastdot < 0) {
            return;
        }
        String pkg = fqjn.substring(0, lastdot);
        emit("package " + pkg + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
    }

    void startInterface(SchemaType sType) throws IOException {
        String shortName = sType.getShortJavaName();
        String baseInterface = findJavaType(sType.getBaseType());
        emit("public interface " + shortName + " extends " + baseInterface + getExtensionInterfaces(sType));
        emit("{");
        indent();
        emitSpecializedAccessors(sType);
    }

    private static String getExtensionInterfaces(SchemaType sType) {
        SchemaTypeImpl sImpl = getImpl(sType);
        if (sImpl == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        InterfaceExtension[] exts = sImpl.getInterfaceExtensions();
        if (exts != null) {
            for (InterfaceExtension interfaceExtension : exts) {
                sb.append(", " + interfaceExtension.getInterface());
            }
        }
        return sb.toString();
    }

    private static SchemaTypeImpl getImpl(SchemaType sType) {
        if (sType instanceof SchemaTypeImpl) {
            return (SchemaTypeImpl) sType;
        }
        return null;
    }

    private void emitSpecializedAccessors(SchemaType sType) throws IOException {
        if (sType.getSimpleVariety() == 1 && sType.getPrimitiveType().getBuiltinTypeCode() == 11) {
            int bits = sType.getDecimalSize();
            int parentBits = sType.getBaseType().getDecimalSize();
            if (bits != parentBits || sType.getBaseType().getFullJavaName() == null) {
                if (bits == 1000000) {
                    emit("java.math.BigInteger getBigIntegerValue();");
                    emit("void setBigIntegerValue(java.math.BigInteger bi);");
                    emit("/** @deprecated */");
                    if (this._useJava15) {
                        emit("@Deprecated");
                    }
                    emit("java.math.BigInteger bigIntegerValue();");
                    emit("/** @deprecated */");
                    if (this._useJava15) {
                        emit("@Deprecated");
                    }
                    emit("void set(java.math.BigInteger bi);");
                } else if (bits == 64) {
                    emit("long getLongValue();");
                    emit("void setLongValue(long l);");
                    emit("/** @deprecated */");
                    if (this._useJava15) {
                        emit("@Deprecated");
                    }
                    emit("long longValue();");
                    emit("/** @deprecated */");
                    if (this._useJava15) {
                        emit("@Deprecated");
                    }
                    emit("void set(long l);");
                } else if (bits == 32) {
                    emit("int getIntValue();");
                    emit("void setIntValue(int i);");
                    emit("/** @deprecated */");
                    if (this._useJava15) {
                        emit("@Deprecated");
                    }
                    emit("int intValue();");
                    emit("/** @deprecated */");
                    if (this._useJava15) {
                        emit("@Deprecated");
                    }
                    emit("void set(int i);");
                } else if (bits == 16) {
                    emit("short getShortValue();");
                    emit("void setShortValue(short s);");
                    emit("/** @deprecated */");
                    if (this._useJava15) {
                        emit("@Deprecated");
                    }
                    emit("short shortValue();");
                    emit("/** @deprecated */");
                    if (this._useJava15) {
                        emit("@Deprecated");
                    }
                    emit("void set(short s);");
                } else if (bits == 8) {
                    emit("byte getByteValue();");
                    emit("void setByteValue(byte b);");
                    emit("/** @deprecated */");
                    if (this._useJava15) {
                        emit("@Deprecated");
                    }
                    emit("byte byteValue();");
                    emit("/** @deprecated */");
                    if (this._useJava15) {
                        emit("@Deprecated");
                    }
                    emit("void set(byte b);");
                }
            }
        }
        if (sType.getSimpleVariety() == 2) {
            emit("java.lang.Object getObjectValue();");
            emit("void setObjectValue(java.lang.Object val);");
            emit("/** @deprecated */");
            if (this._useJava15) {
                emit("@Deprecated");
            }
            emit("java.lang.Object objectValue();");
            emit("/** @deprecated */");
            if (this._useJava15) {
                emit("@Deprecated");
            }
            emit("void objectSet(java.lang.Object val);");
            emit("org.apache.xmlbeans.SchemaType instanceType();");
            SchemaType ctype = sType.getUnionCommonBaseType();
            if (ctype == null || ctype.getSimpleVariety() != 2) {
            }
            emitSpecializedAccessors(ctype);
        }
        if (sType.getSimpleVariety() == 3) {
            emit("java.util.List getListValue();");
            emit("java.util.List xgetListValue();");
            emit("void setListValue(java.util.List list);");
            emit("/** @deprecated */");
            if (this._useJava15) {
                emit("@Deprecated");
            }
            emit("java.util.List listValue();");
            emit("/** @deprecated */");
            if (this._useJava15) {
                emit("@Deprecated");
            }
            emit("java.util.List xlistValue();");
            emit("/** @deprecated */");
            if (this._useJava15) {
                emit("@Deprecated");
            }
            emit("void set(java.util.List list);");
        }
    }

    void startBlock() throws IOException {
        emit("{");
        indent();
    }

    void endBlock() throws IOException {
        outdent();
        emit("}");
    }

    void printJavaDoc(String sentence) throws IOException {
        emit("");
        emit("/**");
        emit(" * " + sentence);
        emit(" */");
    }

    void printShortJavaDoc(String sentence) throws IOException {
        emit("/** " + sentence + " */");
    }

    public static String javaStringEscape(String str) {
        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)) {
                case '\n':
                case '\r':
                case '\"':
                case '\\':
                    StringBuffer sb = new StringBuffer();
                    for (int i2 = 0; i2 < str.length(); i2++) {
                        char ch2 = str.charAt(i2);
                        switch (ch2) {
                            case '\n':
                                sb.append("\\n");
                                break;
                            case '\r':
                                sb.append("\\r");
                                break;
                            case '\"':
                                sb.append("\\\"");
                                break;
                            case '\\':
                                sb.append("\\\\");
                                break;
                            default:
                                sb.append(ch2);
                                break;
                        }
                    }
                    return sb.toString();
                default:
            }
        }
        return str;
    }

    void printStringEnumeration(SchemaType sType) throws IOException {
        SchemaType baseEnumType = sType.getBaseEnumType();
        String baseEnumClass = baseEnumType.getFullJavaName();
        boolean hasBase = hasBase(sType);
        if (!hasBase) {
            emit("");
            emit("org.apache.xmlbeans.StringEnumAbstractBase enumValue();");
            emit("void set(org.apache.xmlbeans.StringEnumAbstractBase e);");
        }
        emit("");
        SchemaStringEnumEntry[] entries = sType.getStringEnumEntries();
        HashSet seenValues = new HashSet();
        HashSet repeatValues = new HashSet();
        for (int i = 0; i < entries.length; i++) {
            String enumValue = entries[i].getString();
            if (seenValues.contains(enumValue)) {
                repeatValues.add(enumValue);
            } else {
                seenValues.add(enumValue);
                String constName = entries[i].getEnumName();
                if (hasBase) {
                    emit("static final " + baseEnumClass + ".Enum " + constName + " = " + baseEnumClass + "." + constName + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
                } else {
                    emit("static final Enum " + constName + " = Enum.forString(\"" + javaStringEscape(enumValue) + "\");");
                }
            }
        }
        emit("");
        for (int i2 = 0; i2 < entries.length; i2++) {
            if (!repeatValues.contains(entries[i2].getString())) {
                String constName2 = "INT_" + entries[i2].getEnumName();
                if (hasBase) {
                    emit("static final int " + constName2 + " = " + baseEnumClass + "." + constName2 + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
                } else {
                    emit("static final int " + constName2 + " = Enum." + constName2 + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
                }
            }
        }
        if (!hasBase) {
            emit("");
            emit("/**");
            emit(" * Enumeration value class for " + baseEnumClass + ".");
            emit(" * These enum values can be used as follows:");
            emit(" * <pre>");
            emit(" * enum.toString(); // returns the string value of the enum");
            emit(" * enum.intValue(); // returns an int value, useful for switches");
            if (entries.length > 0) {
                emit(" * // e.g., case Enum.INT_" + entries[0].getEnumName());
            }
            emit(" * Enum.forString(s); // returns the enum value for a string");
            emit(" * Enum.forInt(i); // returns the enum value for an int");
            emit(" * </pre>");
            emit(" * Enumeration objects are immutable singleton objects that");
            emit(" * can be compared using == object equality. They have no");
            emit(" * public constructor. See the constants defined within this");
            emit(" * class for all the valid values.");
            emit(" */");
            emit("static final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase");
            emit("{");
            indent();
            emit("/**");
            emit(" * Returns the enum value for a string, or null if none.");
            emit(" */");
            emit("public static Enum forString(java.lang.String s)");
            emit("    { return (Enum)table.forString(s); }");
            emit("/**");
            emit(" * Returns the enum value corresponding to an int, or null if none.");
            emit(" */");
            emit("public static Enum forInt(int i)");
            emit("    { return (Enum)table.forInt(i); }");
            emit("");
            emit("private Enum(java.lang.String s, int i)");
            emit("    { super(s, i); }");
            emit("");
            for (int i3 = 0; i3 < entries.length; i3++) {
                String constName3 = "INT_" + entries[i3].getEnumName();
                int intValue = entries[i3].getIntValue();
                emit("static final int " + constName3 + " = " + intValue + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
            }
            emit("");
            emit("public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =");
            emit("    new org.apache.xmlbeans.StringEnumAbstractBase.Table");
            emit("(");
            indent();
            emit("new Enum[]");
            emit("{");
            indent();
            for (int i4 = 0; i4 < entries.length; i4++) {
                String enumValue2 = entries[i4].getString();
                String constName4 = "INT_" + entries[i4].getEnumName();
                emit("new Enum(\"" + javaStringEscape(enumValue2) + "\", " + constName4 + "),");
            }
            outdent();
            emit("}");
            outdent();
            emit(");");
            emit("private static final long serialVersionUID = 1L;");
            emit("private java.lang.Object readResolve() { return forInt(intValue()); } ");
            outdent();
            emit("}");
        }
    }

    private boolean hasBase(SchemaType sType) {
        boolean hasBase;
        SchemaType baseEnumType = sType.getBaseEnumType();
        if (baseEnumType.isAnonymousType() && baseEnumType.isSkippedAnonymousType()) {
            if (sType.getContentBasedOnType() != null) {
                hasBase = sType.getContentBasedOnType().getBaseType() != baseEnumType;
            } else {
                hasBase = sType.getBaseType() != baseEnumType;
            }
        } else {
            hasBase = baseEnumType != sType;
        }
        return hasBase;
    }

    String xmlTypeForProperty(SchemaProperty sProp) {
        SchemaType sType = sProp.javaBasedOnType();
        return findJavaType(sType).replace('$', '.');
    }

    static boolean xmlTypeForPropertyIsUnion(SchemaProperty sProp) {
        SchemaType sType = sProp.javaBasedOnType();
        return sType.isSimpleType() && sType.getSimpleVariety() == 2;
    }

    static boolean isJavaPrimitive(int javaType) {
        return javaType >= 1 && javaType <= 7;
    }

    static String javaWrappedType(int javaType) {
        switch (javaType) {
            case 1:
                return "java.lang.Boolean";
            case 2:
                return "java.lang.Float";
            case 3:
                return "java.lang.Double";
            case 4:
                return "java.lang.Byte";
            case 5:
                return "java.lang.Short";
            case 6:
                return "java.lang.Integer";
            case 7:
                return "java.lang.Long";
            default:
                if ($assertionsDisabled) {
                    throw new IllegalStateException();
                }
                throw new AssertionError();
        }
    }

    String javaTypeForProperty(SchemaProperty sProp) {
        if (sProp.getJavaTypeCode() == 0) {
            return findJavaType(sProp.javaBasedOnType()).replace('$', '.');
        }
        if (sProp.getJavaTypeCode() == 20) {
            return ((SchemaTypeImpl) sProp.getType()).getUserTypeName();
        }
        switch (sProp.getJavaTypeCode()) {
            case 1:
                return "boolean";
            case 2:
                return XmlErrorCodes.FLOAT;
            case 3:
                return XmlErrorCodes.DOUBLE;
            case 4:
                return "byte";
            case 5:
                return "short";
            case 6:
                return XmlErrorCodes.INT;
            case 7:
                return XmlErrorCodes.LONG;
            case 8:
                return "java.math.BigDecimal";
            case 9:
                return "java.math.BigInteger";
            case 10:
                return "java.lang.String";
            case 11:
                return "byte[]";
            case 12:
                return "org.apache.xmlbeans.GDate";
            case 13:
                return "org.apache.xmlbeans.GDuration";
            case 14:
                return "java.util.Date";
            case 15:
                return "javax.xml.namespace.QName";
            case 16:
                return "java.util.List";
            case 17:
                return "java.util.Calendar";
            case 18:
                SchemaType sType = sProp.javaBasedOnType();
                if (sType.getSimpleVariety() == 2) {
                    sType = sType.getUnionCommonBaseType();
                }
                if (!$assertionsDisabled && sType.getBaseEnumType() == null) {
                    throw new AssertionError();
                }
                if (hasBase(sType)) {
                    return findJavaType(sType.getBaseEnumType()).replace('$', '.') + ".Enum";
                }
                return findJavaType(sType).replace('$', '.') + ".Enum";
            case 19:
                return "java.lang.Object";
            default:
                if ($assertionsDisabled) {
                    throw new IllegalStateException();
                }
                throw new AssertionError();
        }
    }

    void printPropertyGetters(QName qName, boolean isAttr, String propertyName, int javaType, String type, String xtype, boolean nillable, boolean optional, boolean several, boolean singleton) throws IOException {
        String propdesc = SymbolConstants.QUOTES_SYMBOL + qName.getLocalPart() + SymbolConstants.QUOTES_SYMBOL + (isAttr ? " attribute" : " element");
        boolean xmltype = javaType == 0;
        if (singleton) {
            printJavaDoc((several ? "Gets first " : "Gets the ") + propdesc);
            emit(type + " get" + propertyName + "();");
            if (!xmltype) {
                printJavaDoc((several ? "Gets (as xml) first " : "Gets (as xml) the ") + propdesc);
                emit(xtype + " xget" + propertyName + "();");
            }
            if (nillable) {
                printJavaDoc((several ? "Tests for nil first " : "Tests for nil ") + propdesc);
                emit("boolean isNil" + propertyName + "();");
            }
        }
        if (optional) {
            printJavaDoc((several ? "True if has at least one " : "True if has ") + propdesc);
            emit("boolean isSet" + propertyName + "();");
        }
        if (several) {
            String arrayName = propertyName + SoapEncSchemaTypeSystem.SOAP_ARRAY;
            if (this._useJava15) {
                String wrappedType = type;
                if (isJavaPrimitive(javaType)) {
                    wrappedType = javaWrappedType(javaType);
                }
                printJavaDoc("Gets a List of " + propdesc + ExcelXmlConstants.CELL_DATA_FORMAT_TAG);
                emit("java.util.List<" + wrappedType + "> get" + propertyName + "List();");
            }
            if (this._useJava15) {
                emit("");
                emit("/**");
                emit(" * Gets array of all " + propdesc + ExcelXmlConstants.CELL_DATA_FORMAT_TAG);
                emit(" * @deprecated");
                emit(" */");
                emit("@Deprecated");
            } else {
                printJavaDoc("Gets array of all " + propdesc + ExcelXmlConstants.CELL_DATA_FORMAT_TAG);
            }
            emit(type + "[] get" + arrayName + "();");
            printJavaDoc("Gets ith " + propdesc);
            emit(type + " get" + arrayName + "(int i);");
            if (!xmltype) {
                if (this._useJava15) {
                    printJavaDoc("Gets (as xml) a List of " + propdesc + ExcelXmlConstants.CELL_DATA_FORMAT_TAG);
                    emit("java.util.List<" + xtype + "> xget" + propertyName + "List();");
                }
                if (this._useJava15) {
                    emit("");
                    emit("/**");
                    emit(" * Gets (as xml) array of all " + propdesc + ExcelXmlConstants.CELL_DATA_FORMAT_TAG);
                    emit(" * @deprecated");
                    emit(" */");
                    emit("@Deprecated");
                } else {
                    printJavaDoc("Gets (as xml) array of all " + propdesc + ExcelXmlConstants.CELL_DATA_FORMAT_TAG);
                }
                emit(xtype + "[] xget" + arrayName + "();");
                printJavaDoc("Gets (as xml) ith " + propdesc);
                emit(xtype + " xget" + arrayName + "(int i);");
            }
            if (nillable) {
                printJavaDoc("Tests for nil ith " + propdesc);
                emit("boolean isNil" + arrayName + "(int i);");
            }
            printJavaDoc("Returns number of " + propdesc);
            emit("int sizeOf" + arrayName + "();");
        }
    }

    void printPropertySetters(QName qName, boolean isAttr, String propertyName, int javaType, String type, String xtype, boolean nillable, boolean optional, boolean several, boolean singleton) throws IOException {
        String safeVarName = NameUtil.nonJavaKeyword(NameUtil.lowerCamelCase(propertyName));
        if (safeVarName.equals("i")) {
            safeVarName = "iValue";
        }
        boolean xmltype = javaType == 0;
        String propdesc = SymbolConstants.QUOTES_SYMBOL + qName.getLocalPart() + SymbolConstants.QUOTES_SYMBOL + (isAttr ? " attribute" : " element");
        if (singleton) {
            printJavaDoc((several ? "Sets first " : "Sets the ") + propdesc);
            emit("void set" + propertyName + "(" + type + SymbolConstants.SPACE_SYMBOL + safeVarName + ");");
            if (!xmltype) {
                printJavaDoc((several ? "Sets (as xml) first " : "Sets (as xml) the ") + propdesc);
                emit("void xset" + propertyName + "(" + xtype + SymbolConstants.SPACE_SYMBOL + safeVarName + ");");
            }
            if (xmltype && !several) {
                printJavaDoc("Appends and returns a new empty " + propdesc);
                emit(xtype + " addNew" + propertyName + "();");
            }
            if (nillable) {
                printJavaDoc((several ? "Nils the first " : "Nils the ") + propdesc);
                emit("void setNil" + propertyName + "();");
            }
        }
        if (optional) {
            printJavaDoc((several ? "Removes first " : "Unsets the ") + propdesc);
            emit("void unset" + propertyName + "();");
        }
        if (several) {
            String arrayName = propertyName + SoapEncSchemaTypeSystem.SOAP_ARRAY;
            printJavaDoc("Sets array of all " + propdesc);
            emit("void set" + arrayName + "(" + type + "[] " + safeVarName + "Array);");
            printJavaDoc("Sets ith " + propdesc);
            emit("void set" + arrayName + "(int i, " + type + SymbolConstants.SPACE_SYMBOL + safeVarName + ");");
            if (!xmltype) {
                printJavaDoc("Sets (as xml) array of all " + propdesc);
                emit("void xset" + arrayName + "(" + xtype + "[] " + safeVarName + "Array);");
                printJavaDoc("Sets (as xml) ith " + propdesc);
                emit("void xset" + arrayName + "(int i, " + xtype + SymbolConstants.SPACE_SYMBOL + safeVarName + ");");
            }
            if (nillable) {
                printJavaDoc("Nils the ith " + propdesc);
                emit("void setNil" + arrayName + "(int i);");
            }
            if (!xmltype) {
                printJavaDoc("Inserts the value as the ith " + propdesc);
                emit("void insert" + propertyName + "(int i, " + type + SymbolConstants.SPACE_SYMBOL + safeVarName + ");");
                printJavaDoc("Appends the value as the last " + propdesc);
                emit("void add" + propertyName + "(" + type + SymbolConstants.SPACE_SYMBOL + safeVarName + ");");
            }
            printJavaDoc("Inserts and returns a new empty value (as xml) as the ith " + propdesc);
            emit(xtype + " insertNew" + propertyName + "(int i);");
            printJavaDoc("Appends and returns a new empty value (as xml) as the last " + propdesc);
            emit(xtype + " addNew" + propertyName + "();");
            printJavaDoc("Removes the ith " + propdesc);
            emit("void remove" + propertyName + "(int i);");
        }
    }

    String getAtomicRestrictionType(SchemaType sType) {
        SchemaType pType = sType.getPrimitiveType();
        switch (pType.getBuiltinTypeCode()) {
            case 2:
                return "org.apache.xmlbeans.impl.values.XmlAnySimpleTypeImpl";
            case 3:
                return "org.apache.xmlbeans.impl.values.JavaBooleanHolderEx";
            case 4:
                return "org.apache.xmlbeans.impl.values.JavaBase64HolderEx";
            case 5:
                return "org.apache.xmlbeans.impl.values.JavaHexBinaryHolderEx";
            case 6:
                return "org.apache.xmlbeans.impl.values.JavaUriHolderEx";
            case 7:
                return "org.apache.xmlbeans.impl.values.JavaQNameHolderEx";
            case 8:
                return "org.apache.xmlbeans.impl.values.JavaNotationHolderEx";
            case 9:
                return "org.apache.xmlbeans.impl.values.JavaFloatHolderEx";
            case 10:
                return "org.apache.xmlbeans.impl.values.JavaDoubleHolderEx";
            case 11:
                switch (sType.getDecimalSize()) {
                    case 8:
                    case 16:
                    case 32:
                        return "org.apache.xmlbeans.impl.values.JavaIntHolderEx";
                    case 64:
                        return "org.apache.xmlbeans.impl.values.JavaLongHolderEx";
                    case SchemaType.SIZE_BIG_INTEGER /* 1000000 */:
                        return "org.apache.xmlbeans.impl.values.JavaIntegerHolderEx";
                    case 1000001:
                        return "org.apache.xmlbeans.impl.values.JavaDecimalHolderEx";
                    default:
                        if ($assertionsDisabled) {
                            return "org.apache.xmlbeans.impl.values.JavaDecimalHolderEx";
                        }
                        throw new AssertionError();
                }
            case 12:
                if (sType.hasStringEnumValues()) {
                    return "org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx";
                }
                return "org.apache.xmlbeans.impl.values.JavaStringHolderEx";
            case 13:
                return "org.apache.xmlbeans.impl.values.JavaGDurationHolderEx";
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
                return "org.apache.xmlbeans.impl.values.JavaGDateHolderEx";
            default:
                if ($assertionsDisabled) {
                    return null;
                }
                throw new AssertionError("unrecognized primitive type");
        }
    }

    static SchemaType findBaseType(SchemaType sType) {
        while (sType.getFullJavaName() == null) {
            sType = sType.getBaseType();
        }
        return sType;
    }

    String getBaseClass(SchemaType sType) {
        SchemaType baseType = findBaseType(sType.getBaseType());
        switch (sType.getSimpleVariety()) {
            case 0:
                if (!XmlObject.type.equals(baseType)) {
                    return baseType.getFullJavaImplName();
                }
                return "org.apache.xmlbeans.impl.values.XmlComplexContentImpl";
            case 1:
                if ($assertionsDisabled || !sType.isBuiltinType()) {
                    return getAtomicRestrictionType(sType);
                }
                throw new AssertionError();
            case 2:
                return "org.apache.xmlbeans.impl.values.XmlUnionImpl";
            case 3:
                return "org.apache.xmlbeans.impl.values.XmlListImpl";
            default:
                throw new IllegalStateException();
        }
    }

    void printConstructor(SchemaType sType, String shortName) throws IOException {
        String str;
        emit("");
        emit("public " + shortName + "(org.apache.xmlbeans.SchemaType sType)");
        startBlock();
        StringBuilder sbAppend = new StringBuilder().append("super(sType");
        if (sType.getSimpleVariety() == 0) {
            str = "";
        } else {
            str = ", " + (!sType.isSimpleType());
        }
        emit(sbAppend.append(str).append(");").toString());
        endBlock();
        if (sType.getSimpleVariety() != 0) {
            emit("");
            emit("protected " + shortName + "(org.apache.xmlbeans.SchemaType sType, boolean b)");
            startBlock();
            emit("super(sType, b);");
            endBlock();
        }
    }

    void startClass(SchemaType sType, boolean isInner) throws IOException {
        String shortName = sType.getShortJavaImplName();
        String baseClass = getBaseClass(sType);
        StringBuffer interfaces = new StringBuffer();
        interfaces.append(sType.getFullJavaName().replace('$', '.'));
        if (sType.getSimpleVariety() == 2) {
            SchemaType[] memberTypes = sType.getUnionMemberTypes();
            for (SchemaType schemaType : memberTypes) {
                interfaces.append(", " + schemaType.getFullJavaName().replace('$', '.'));
            }
        }
        emit("public " + (isInner ? "static " : "") + "class " + shortName + " extends " + baseClass + " implements " + interfaces.toString());
        startBlock();
        emit("private static final long serialVersionUID = 1L;");
    }

    void makeAttributeDefaultValue(String jtargetType, SchemaProperty prop, String identifier) throws IOException {
        String fullName = jtargetType;
        if (fullName == null) {
            fullName = prop.javaBasedOnType().getFullJavaName().replace('$', '.');
        }
        emit("target = (" + fullName + ")get_default_attribute_value(" + identifier + ");");
    }

    void makeMissingValue(int javaType) throws IOException {
        switch (javaType) {
            case 0:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            default:
                emit("return null;");
                break;
            case 1:
                emit("return false;");
                break;
            case 2:
                emit("return 0.0f;");
                break;
            case 3:
                emit("return 0.0;");
                break;
            case 4:
            case 5:
            case 6:
                emit("return 0;");
                break;
            case 7:
                emit("return 0L;");
                break;
        }
    }

    void printJGetArrayValue(int javaType, String type, SchemaTypeImpl stype) throws IOException {
        switch (javaType) {
            case 0:
                emit(type + "[] result = new " + type + "[targetList.size()];");
                emit("targetList.toArray(result);");
                break;
            case 1:
                emit("boolean[] result = new boolean[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getBooleanValue();");
                break;
            case 2:
                emit("float[] result = new float[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getFloatValue();");
                break;
            case 3:
                emit("double[] result = new double[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getDoubleValue();");
                break;
            case 4:
                emit("byte[] result = new byte[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getByteValue();");
                break;
            case 5:
                emit("short[] result = new short[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getShortValue();");
                break;
            case 6:
                emit("int[] result = new int[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getIntValue();");
                break;
            case 7:
                emit("long[] result = new long[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getLongValue();");
                break;
            case 8:
                emit("java.math.BigDecimal[] result = new java.math.BigDecimal[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getBigDecimalValue();");
                break;
            case 9:
                emit("java.math.BigInteger[] result = new java.math.BigInteger[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getBigIntegerValue();");
                break;
            case 10:
                emit("java.lang.String[] result = new java.lang.String[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getStringValue();");
                break;
            case 11:
                emit("byte[][] result = new byte[targetList.size()][];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getByteArrayValue();");
                break;
            case 12:
                emit("org.apache.xmlbeans.GDate[] result = new org.apache.xmlbeans.GDate[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getGDateValue();");
                break;
            case 13:
                emit("org.apache.xmlbeans.GDuration[] result = new org.apache.xmlbeans.GDuration[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getGDurationValue();");
                break;
            case 14:
                emit("java.util.Date[] result = new java.util.Date[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getDateValue();");
                break;
            case 15:
                emit("javax.xml.namespace.QName[] result = new javax.xml.namespace.QName[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getQNameValue();");
                break;
            case 16:
                emit("java.util.List[] result = new java.util.List[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getListValue();");
                break;
            case 17:
                emit("java.util.Calendar[] result = new java.util.Calendar[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getCalendarValue();");
                break;
            case 18:
                emit(type + "[] result = new " + type + "[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = (" + type + ")((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getEnumValue();");
                break;
            case 19:
                emit("java.lang.Object[] result = new java.lang.Object[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getObjectValue();");
                break;
            case 20:
                emit(stype.getUserTypeName() + "[] result = new " + stype.getUserTypeName() + "[targetList.size()];");
                emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                emit("    result[i] = " + getUserTypeStaticHandlerMethod(false, stype) + "((org.apache.xmlbeans.SimpleValue)targetList.get(i));");
                break;
            default:
                throw new IllegalStateException();
        }
        emit("return result;");
    }

    void printJGetValue(int javaType, String type, SchemaTypeImpl stype) throws IOException {
        switch (javaType) {
            case 0:
                emit("return target;");
                return;
            case 1:
                emit("return target.getBooleanValue();");
                return;
            case 2:
                emit("return target.getFloatValue();");
                return;
            case 3:
                emit("return target.getDoubleValue();");
                return;
            case 4:
                emit("return target.getByteValue();");
                return;
            case 5:
                emit("return target.getShortValue();");
                return;
            case 6:
                emit("return target.getIntValue();");
                return;
            case 7:
                emit("return target.getLongValue();");
                return;
            case 8:
                emit("return target.getBigDecimalValue();");
                return;
            case 9:
                emit("return target.getBigIntegerValue();");
                return;
            case 10:
                emit("return target.getStringValue();");
                return;
            case 11:
                emit("return target.getByteArrayValue();");
                return;
            case 12:
                emit("return target.getGDateValue();");
                return;
            case 13:
                emit("return target.getGDurationValue();");
                return;
            case 14:
                emit("return target.getDateValue();");
                return;
            case 15:
                emit("return target.getQNameValue();");
                return;
            case 16:
                emit("return target.getListValue();");
                return;
            case 17:
                emit("return target.getCalendarValue();");
                return;
            case 18:
                emit("return (" + type + ")target.getEnumValue();");
                return;
            case 19:
                emit("return target.getObjectValue();");
                return;
            case 20:
                emit("return " + getUserTypeStaticHandlerMethod(false, stype) + "(target);");
                return;
            default:
                throw new IllegalStateException();
        }
    }

    void printJSetValue(int javaType, String safeVarName, SchemaTypeImpl stype) throws IOException {
        switch (javaType) {
            case 0:
                emit("target.set(" + safeVarName + ");");
                return;
            case 1:
                emit("target.setBooleanValue(" + safeVarName + ");");
                return;
            case 2:
                emit("target.setFloatValue(" + safeVarName + ");");
                return;
            case 3:
                emit("target.setDoubleValue(" + safeVarName + ");");
                return;
            case 4:
                emit("target.setByteValue(" + safeVarName + ");");
                return;
            case 5:
                emit("target.setShortValue(" + safeVarName + ");");
                return;
            case 6:
                emit("target.setIntValue(" + safeVarName + ");");
                return;
            case 7:
                emit("target.setLongValue(" + safeVarName + ");");
                return;
            case 8:
                emit("target.setBigDecimalValue(" + safeVarName + ");");
                return;
            case 9:
                emit("target.setBigIntegerValue(" + safeVarName + ");");
                return;
            case 10:
                emit("target.setStringValue(" + safeVarName + ");");
                return;
            case 11:
                emit("target.setByteArrayValue(" + safeVarName + ");");
                return;
            case 12:
                emit("target.setGDateValue(" + safeVarName + ");");
                return;
            case 13:
                emit("target.setGDurationValue(" + safeVarName + ");");
                return;
            case 14:
                emit("target.setDateValue(" + safeVarName + ");");
                return;
            case 15:
                emit("target.setQNameValue(" + safeVarName + ");");
                return;
            case 16:
                emit("target.setListValue(" + safeVarName + ");");
                return;
            case 17:
                emit("target.setCalendarValue(" + safeVarName + ");");
                return;
            case 18:
                emit("target.setEnumValue(" + safeVarName + ");");
                return;
            case 19:
                emit("target.setObjectValue(" + safeVarName + ");");
                return;
            case 20:
                emit(getUserTypeStaticHandlerMethod(true, stype) + "(" + safeVarName + ", target);");
                return;
            default:
                throw new IllegalStateException();
        }
    }

    String getIdentifier(Map qNameMap, QName qName) {
        return ((String[]) qNameMap.get(qName))[0];
    }

    String getSetIdentifier(Map qNameMap, QName qName) {
        String[] identifiers = (String[]) qNameMap.get(qName);
        return identifiers[1] == null ? identifiers[0] : identifiers[1];
    }

    Map printStaticFields(SchemaProperty[] properties) throws IOException {
        Map results = new HashMap();
        emit("");
        for (int i = 0; i < properties.length; i++) {
            String[] identifiers = new String[2];
            SchemaProperty prop = properties[i];
            QName name = prop.getName();
            results.put(name, identifiers);
            String javaName = prop.getJavaPropertyName();
            identifiers[0] = (javaName + PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX + (i * 2)).toUpperCase();
            String uriString = SymbolConstants.QUOTES_SYMBOL + name.getNamespaceURI() + SymbolConstants.QUOTES_SYMBOL;
            emit("private static final javax.xml.namespace.QName " + identifiers[0] + " = ");
            indent();
            emit("new javax.xml.namespace.QName(" + uriString + ", \"" + name.getLocalPart() + "\");");
            outdent();
            if (properties[i].acceptedNames() != null) {
                QName[] qnames = properties[i].acceptedNames();
                if (qnames.length > 1) {
                    identifiers[1] = (javaName + PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX + ((i * 2) + 1)).toUpperCase();
                    emit("private static final org.apache.xmlbeans.QNameSet " + identifiers[1] + " = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { ");
                    indent();
                    for (int j = 0; j < qnames.length; j++) {
                        emit("new javax.xml.namespace.QName(\"" + qnames[j].getNamespaceURI() + "\", \"" + qnames[j].getLocalPart() + "\"),");
                    }
                    outdent();
                    emit("});");
                }
            }
        }
        emit("");
        return results;
    }

    void emitImplementationPreamble() throws IOException {
        emit("synchronized (monitor())");
        emit("{");
        indent();
        emit("check_orphaned();");
    }

    void emitImplementationPostamble() throws IOException {
        outdent();
        emit("}");
    }

    void emitDeclareTarget(boolean declareTarget, String xtype) throws IOException {
        if (declareTarget) {
            emit(xtype + " target = null;");
        }
    }

    void emitAddTarget(String identifier, boolean isAttr, boolean declareTarget, String xtype) throws IOException {
        if (isAttr) {
            emit("target = (" + xtype + ")get_store().add_attribute_user(" + identifier + ");");
        } else {
            emit("target = (" + xtype + ")get_store().add_element_user(" + identifier + ");");
        }
    }

    void emitPre(SchemaType sType, int opType, String identifier, boolean isAttr) throws IOException {
        emitPre(sType, opType, identifier, isAttr, "-1");
    }

    void emitPre(SchemaType sType, int opType, String identifier, boolean isAttr, String index) throws IOException {
        PrePostExtension ext;
        SchemaTypeImpl sImpl = getImpl(sType);
        if (sImpl != null && (ext = sImpl.getPrePostExtension()) != null && ext.hasPreCall()) {
            emit("if ( " + ext.getStaticHandler() + ".preSet(" + prePostOpString(opType) + ", this, " + identifier + ", " + isAttr + ", " + index + "))");
            startBlock();
        }
    }

    void emitPost(SchemaType sType, int opType, String identifier, boolean isAttr) throws IOException {
        emitPost(sType, opType, identifier, isAttr, "-1");
    }

    void emitPost(SchemaType sType, int opType, String identifier, boolean isAttr, String index) throws IOException {
        PrePostExtension ext;
        SchemaTypeImpl sImpl = getImpl(sType);
        if (sImpl != null && (ext = sImpl.getPrePostExtension()) != null) {
            if (ext.hasPreCall()) {
                endBlock();
            }
            if (ext.hasPostCall()) {
                emit(ext.getStaticHandler() + ".postSet(" + prePostOpString(opType) + ", this, " + identifier + ", " + isAttr + ", " + index + ");");
            }
        }
    }

    String prePostOpString(int opType) {
        switch (opType) {
            case 1:
                return "org.apache.xmlbeans.PrePostExtension.OPERATION_SET";
            case 2:
                return "org.apache.xmlbeans.PrePostExtension.OPERATION_INSERT";
            case 3:
                return "org.apache.xmlbeans.PrePostExtension.OPERATION_REMOVE";
            default:
                if ($assertionsDisabled) {
                    return "org.apache.xmlbeans.PrePostExtension.OPERATION_SET";
                }
                throw new AssertionError();
        }
    }

    void emitGetTarget(String setIdentifier, String identifier, boolean isAttr, String index, int nullBehaviour, String xtype) throws IOException {
        if (!$assertionsDisabled && (setIdentifier == null || identifier == null)) {
            throw new AssertionError();
        }
        emit(xtype + " target = null;");
        if (isAttr) {
            emit("target = (" + xtype + ")get_store().find_attribute_user(" + identifier + ");");
        } else {
            emit("target = (" + xtype + ")get_store().find_element_user(" + setIdentifier + ", " + index + ");");
        }
        if (nullBehaviour == 1) {
            return;
        }
        emit("if (target == null)");
        startBlock();
        switch (nullBehaviour) {
            case 1:
                break;
            case 2:
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError("Bad behaviour type: " + nullBehaviour);
                }
                break;
            case 3:
                emitAddTarget(identifier, isAttr, false, xtype);
                break;
            case 4:
                emit("throw new IndexOutOfBoundsException();");
                break;
        }
        endBlock();
    }

    void printListGetter15Impl(String parentJavaName, String propdesc, String propertyName, String wrappedType, String xtype, boolean xmltype, boolean xget) throws IOException {
        String arrayName = propertyName + SoapEncSchemaTypeSystem.SOAP_ARRAY;
        String listName = propertyName + "List";
        String parentThis = parentJavaName + ".this.";
        String xgetMethod = (xget ? "x" : "") + BeanUtil.PREFIX_GETTER_GET;
        String xsetMethod = (xget ? "x" : "") + "set";
        printJavaDoc("Gets " + (xget ? "(as xml) " : "") + "a List of " + propdesc + ExcelXmlConstants.CELL_DATA_FORMAT_TAG);
        emit("public java.util.List<" + wrappedType + "> " + xgetMethod + listName + "()");
        startBlock();
        emit("final class " + listName + " extends java.util.AbstractList<" + wrappedType + ">");
        startBlock();
        if (this._useJava15) {
            emit("@Override");
        }
        emit("public " + wrappedType + " get(int i)");
        emit("    { return " + parentThis + xgetMethod + arrayName + "(i); }");
        emit("");
        if (this._useJava15) {
            emit("@Override");
        }
        emit("public " + wrappedType + " set(int i, " + wrappedType + " o)");
        startBlock();
        emit(wrappedType + " old = " + parentThis + xgetMethod + arrayName + "(i);");
        emit(parentThis + xsetMethod + arrayName + "(i, o);");
        emit("return old;");
        endBlock();
        emit("");
        if (this._useJava15) {
            emit("@Override");
        }
        emit("public void add(int i, " + wrappedType + " o)");
        if (xmltype || xget) {
            emit("    { " + parentThis + "insertNew" + propertyName + "(i).set(o); }");
        } else {
            emit("    { " + parentThis + "insert" + propertyName + "(i, o); }");
        }
        emit("");
        if (this._useJava15) {
            emit("@Override");
        }
        emit("public " + wrappedType + " remove(int i)");
        startBlock();
        emit(wrappedType + " old = " + parentThis + xgetMethod + arrayName + "(i);");
        emit(parentThis + Protocol.SENTINEL_REMOVE + propertyName + "(i);");
        emit("return old;");
        endBlock();
        emit("");
        if (this._useJava15) {
            emit("@Override");
        }
        emit("public int size()");
        emit("    { return " + parentThis + "sizeOf" + arrayName + "(); }");
        emit("");
        endBlock();
        emit("");
        emitImplementationPreamble();
        emit("return new " + listName + "();");
        emitImplementationPostamble();
        endBlock();
    }

    void printGetterImpls(String parentJavaName, SchemaProperty prop, QName qName, boolean isAttr, String propertyName, int javaType, String type, String xtype, boolean nillable, boolean optional, boolean several, boolean singleton, boolean isunion, String identifier, String setIdentifier) throws IOException {
        String propdesc = SymbolConstants.QUOTES_SYMBOL + qName.getLocalPart() + SymbolConstants.QUOTES_SYMBOL + (isAttr ? " attribute" : " element");
        boolean xmltype = javaType == 0;
        String jtargetType = (isunion || !xmltype) ? "org.apache.xmlbeans.SimpleValue" : xtype;
        if (singleton) {
            printJavaDoc((several ? "Gets first " : "Gets the ") + propdesc);
            emit("public " + type + " get" + propertyName + "()");
            startBlock();
            emitImplementationPreamble();
            emitGetTarget(setIdentifier, identifier, isAttr, "0", 1, jtargetType);
            if (isAttr && (prop.hasDefault() == 2 || prop.hasFixed() == 2)) {
                emit("if (target == null)");
                startBlock();
                makeAttributeDefaultValue(jtargetType, prop, identifier);
                endBlock();
            }
            emit("if (target == null)");
            startBlock();
            makeMissingValue(javaType);
            endBlock();
            printJGetValue(javaType, type, (SchemaTypeImpl) prop.getType());
            emitImplementationPostamble();
            endBlock();
            if (!xmltype) {
                printJavaDoc((several ? "Gets (as xml) first " : "Gets (as xml) the ") + propdesc);
                emit("public " + xtype + " xget" + propertyName + "()");
                startBlock();
                emitImplementationPreamble();
                emitGetTarget(setIdentifier, identifier, isAttr, "0", 1, xtype);
                if (isAttr && (prop.hasDefault() == 2 || prop.hasFixed() == 2)) {
                    emit("if (target == null)");
                    startBlock();
                    makeAttributeDefaultValue(xtype, prop, identifier);
                    endBlock();
                }
                emit("return target;");
                emitImplementationPostamble();
                endBlock();
            }
            if (nillable) {
                printJavaDoc((several ? "Tests for nil first " : "Tests for nil ") + propdesc);
                emit("public boolean isNil" + propertyName + "()");
                startBlock();
                emitImplementationPreamble();
                emitGetTarget(setIdentifier, identifier, isAttr, "0", 1, xtype);
                emit("if (target == null) return false;");
                emit("return target.isNil();");
                emitImplementationPostamble();
                endBlock();
            }
        }
        if (optional) {
            printJavaDoc((several ? "True if has at least one " : "True if has ") + propdesc);
            emit("public boolean isSet" + propertyName + "()");
            startBlock();
            emitImplementationPreamble();
            if (isAttr) {
                emit("return get_store().find_attribute_user(" + identifier + ") != null;");
            } else {
                emit("return get_store().count_elements(" + setIdentifier + ") != 0;");
            }
            emitImplementationPostamble();
            endBlock();
        }
        if (several) {
            String arrayName = propertyName + SoapEncSchemaTypeSystem.SOAP_ARRAY;
            if (this._useJava15) {
                String wrappedType = type;
                if (isJavaPrimitive(javaType)) {
                    wrappedType = javaWrappedType(javaType);
                }
                printListGetter15Impl(parentJavaName, propdesc, propertyName, wrappedType, xtype, xmltype, false);
            }
            if (this._useJava15) {
                emit("");
                emit("/**");
                emit(" * Gets array of all " + propdesc + ExcelXmlConstants.CELL_DATA_FORMAT_TAG);
                emit(" * @deprecated");
                emit(" */");
                emit("@Deprecated");
            } else {
                printJavaDoc("Gets array of all " + propdesc + ExcelXmlConstants.CELL_DATA_FORMAT_TAG);
            }
            emit("public " + type + "[] get" + arrayName + "()");
            startBlock();
            emitImplementationPreamble();
            if (this._useJava15) {
                emit("java.util.List<" + xtype + "> targetList = new java.util.ArrayList<" + xtype + ">();");
            } else {
                emit("java.util.List targetList = new java.util.ArrayList();");
            }
            emit("get_store().find_all_element_users(" + setIdentifier + ", targetList);");
            printJGetArrayValue(javaType, type, (SchemaTypeImpl) prop.getType());
            emitImplementationPostamble();
            endBlock();
            printJavaDoc("Gets ith " + propdesc);
            emit("public " + type + " get" + arrayName + "(int i)");
            startBlock();
            emitImplementationPreamble();
            emitGetTarget(setIdentifier, identifier, isAttr, "i", 4, jtargetType);
            printJGetValue(javaType, type, (SchemaTypeImpl) prop.getType());
            emitImplementationPostamble();
            endBlock();
            if (!xmltype) {
                if (this._useJava15) {
                    printListGetter15Impl(parentJavaName, propdesc, propertyName, xtype, xtype, xmltype, true);
                }
                if (this._useJava15) {
                    emit("");
                    emit("/**");
                    emit(" * Gets array of all " + propdesc + ExcelXmlConstants.CELL_DATA_FORMAT_TAG);
                    emit(" * @deprecated");
                    emit(" */");
                    emit("@Deprecated");
                } else {
                    printJavaDoc("Gets (as xml) array of all " + propdesc + ExcelXmlConstants.CELL_DATA_FORMAT_TAG);
                }
                emit("public " + xtype + "[] xget" + arrayName + "()");
                startBlock();
                emitImplementationPreamble();
                if (this._useJava15) {
                    emit("java.util.List<" + xtype + "> targetList = new java.util.ArrayList<" + xtype + ">();");
                } else {
                    emit("java.util.List targetList = new java.util.ArrayList();");
                }
                emit("get_store().find_all_element_users(" + setIdentifier + ", targetList);");
                emit(xtype + "[] result = new " + xtype + "[targetList.size()];");
                emit("targetList.toArray(result);");
                emit("return result;");
                emitImplementationPostamble();
                endBlock();
                printJavaDoc("Gets (as xml) ith " + propdesc);
                emit("public " + xtype + " xget" + arrayName + "(int i)");
                startBlock();
                emitImplementationPreamble();
                emitGetTarget(setIdentifier, identifier, isAttr, "i", 4, xtype);
                emit("return target;");
                emitImplementationPostamble();
                endBlock();
            }
            if (nillable) {
                printJavaDoc("Tests for nil ith " + propdesc);
                emit("public boolean isNil" + arrayName + "(int i)");
                startBlock();
                emitImplementationPreamble();
                emitGetTarget(setIdentifier, identifier, isAttr, "i", 4, xtype);
                emit("return target.isNil();");
                emitImplementationPostamble();
                endBlock();
            }
            printJavaDoc("Returns number of " + propdesc);
            emit("public int sizeOf" + arrayName + "()");
            startBlock();
            emitImplementationPreamble();
            emit("return get_store().count_elements(" + setIdentifier + ");");
            emitImplementationPostamble();
            endBlock();
        }
    }

    void printSetterImpls(QName qName, SchemaProperty prop, boolean isAttr, String propertyName, int javaType, String type, String xtype, boolean nillable, boolean optional, boolean several, boolean singleton, boolean isunion, String identifier, String setIdentifier, SchemaType sType) throws IOException {
        String safeVarName = NameUtil.nonExtraKeyword(NameUtil.nonJavaKeyword(NameUtil.lowerCamelCase(propertyName)));
        boolean xmltype = javaType == 0;
        boolean isobj = javaType == 19;
        boolean isSubstGroup = identifier != setIdentifier;
        String jtargetType = (isunion || !xmltype) ? "org.apache.xmlbeans.SimpleValue" : xtype;
        String propdesc = SymbolConstants.QUOTES_SYMBOL + qName.getLocalPart() + SymbolConstants.QUOTES_SYMBOL + (isAttr ? " attribute" : " element");
        if (singleton) {
            printJavaDoc((several ? "Sets first " : "Sets the ") + propdesc);
            emit("public void set" + propertyName + "(" + type + SymbolConstants.SPACE_SYMBOL + safeVarName + ")");
            startBlock();
            if (xmltype && !isSubstGroup && !isAttr) {
                emitPre(sType, 1, identifier, isAttr, several ? "0" : "-1");
                emit("generatedSetterHelperImpl(" + safeVarName + ", " + setIdentifier + ", 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);");
                emitPost(sType, 1, identifier, isAttr, several ? "0" : "-1");
            } else {
                emitImplementationPreamble();
                emitPre(sType, 1, identifier, isAttr, several ? "0" : "-1");
                emitGetTarget(setIdentifier, identifier, isAttr, "0", 3, jtargetType);
                printJSetValue(javaType, safeVarName, (SchemaTypeImpl) prop.getType());
                emitPost(sType, 1, identifier, isAttr, several ? "0" : "-1");
                emitImplementationPostamble();
            }
            endBlock();
            if (!xmltype) {
                printJavaDoc((several ? "Sets (as xml) first " : "Sets (as xml) the ") + propdesc);
                emit("public void xset" + propertyName + "(" + xtype + SymbolConstants.SPACE_SYMBOL + safeVarName + ")");
                startBlock();
                emitImplementationPreamble();
                emitPre(sType, 1, identifier, isAttr, several ? "0" : "-1");
                emitGetTarget(setIdentifier, identifier, isAttr, "0", 3, xtype);
                emit("target.set(" + safeVarName + ");");
                emitPost(sType, 1, identifier, isAttr, several ? "0" : "-1");
                emitImplementationPostamble();
                endBlock();
            }
            if (xmltype && !several) {
                printJavaDoc("Appends and returns a new empty " + propdesc);
                emit("public " + xtype + " addNew" + propertyName + "()");
                startBlock();
                emitImplementationPreamble();
                emitDeclareTarget(true, xtype);
                emitPre(sType, 2, identifier, isAttr);
                emitAddTarget(identifier, isAttr, true, xtype);
                emitPost(sType, 2, identifier, isAttr);
                emit("return target;");
                emitImplementationPostamble();
                endBlock();
            }
            if (nillable) {
                printJavaDoc((several ? "Nils the first " : "Nils the ") + propdesc);
                emit("public void setNil" + propertyName + "()");
                startBlock();
                emitImplementationPreamble();
                emitPre(sType, 1, identifier, isAttr, several ? "0" : "-1");
                emitGetTarget(setIdentifier, identifier, isAttr, "0", 3, xtype);
                emit("target.setNil();");
                emitPost(sType, 1, identifier, isAttr, several ? "0" : "-1");
                emitImplementationPostamble();
                endBlock();
            }
        }
        if (optional) {
            printJavaDoc((several ? "Removes first " : "Unsets the ") + propdesc);
            emit("public void unset" + propertyName + "()");
            startBlock();
            emitImplementationPreamble();
            emitPre(sType, 3, identifier, isAttr, several ? "0" : "-1");
            if (isAttr) {
                emit("get_store().remove_attribute(" + identifier + ");");
            } else {
                emit("get_store().remove_element(" + setIdentifier + ", 0);");
            }
            emitPost(sType, 3, identifier, isAttr, several ? "0" : "-1");
            emitImplementationPostamble();
            endBlock();
        }
        if (several) {
            String arrayName = propertyName + SoapEncSchemaTypeSystem.SOAP_ARRAY;
            if (xmltype) {
                printJavaDoc("Sets array of all " + propdesc + "  WARNING: This method is not atomicaly synchronized.");
                emit("public void set" + arrayName + "(" + type + "[] " + safeVarName + "Array)");
                startBlock();
                emit("check_orphaned();");
                emitPre(sType, 1, identifier, isAttr);
                if (isobj) {
                    if (!isSubstGroup) {
                        emit("unionArraySetterHelper(" + safeVarName + SoapEncSchemaTypeSystem.SOAP_ARRAY + ", " + identifier + ");");
                    } else {
                        emit("unionArraySetterHelper(" + safeVarName + SoapEncSchemaTypeSystem.SOAP_ARRAY + ", " + identifier + ", " + setIdentifier + ");");
                    }
                } else if (!isSubstGroup) {
                    emit("arraySetterHelper(" + safeVarName + SoapEncSchemaTypeSystem.SOAP_ARRAY + ", " + identifier + ");");
                } else {
                    emit("arraySetterHelper(" + safeVarName + SoapEncSchemaTypeSystem.SOAP_ARRAY + ", " + identifier + ", " + setIdentifier + ");");
                }
                emitPost(sType, 1, identifier, isAttr);
                endBlock();
            } else {
                printJavaDoc("Sets array of all " + propdesc);
                emit("public void set" + arrayName + "(" + type + "[] " + safeVarName + "Array)");
                startBlock();
                emitImplementationPreamble();
                emitPre(sType, 1, identifier, isAttr);
                if (isobj) {
                    if (!isSubstGroup) {
                        emit("unionArraySetterHelper(" + safeVarName + SoapEncSchemaTypeSystem.SOAP_ARRAY + ", " + identifier + ");");
                    } else {
                        emit("unionArraySetterHelper(" + safeVarName + SoapEncSchemaTypeSystem.SOAP_ARRAY + ", " + identifier + ", " + setIdentifier + ");");
                    }
                } else if (prop.getJavaTypeCode() == 20) {
                    if (!isSubstGroup) {
                        emit("org.apache.xmlbeans.SimpleValue[] dests = arraySetterHelper(" + safeVarName + "Array.length, " + identifier + ");");
                        emit("for ( int i = 0 ; i < dests.length ; i++ ) {");
                        emit("    " + getUserTypeStaticHandlerMethod(true, (SchemaTypeImpl) prop.getType()) + "(" + safeVarName + "Array[i], dests[i]);");
                        emit("}");
                    } else {
                        emit("org.apache.xmlbeans.SimpleValue[] dests = arraySetterHelper(" + safeVarName + "Array.length, " + identifier + ", " + setIdentifier + ");");
                        emit("for ( int i = 0 ; i < dests.length ; i++ ) {");
                        emit("    " + getUserTypeStaticHandlerMethod(true, (SchemaTypeImpl) prop.getType()) + "(" + safeVarName + "Array[i], dests[i]);");
                        emit("}");
                    }
                } else if (!isSubstGroup) {
                    emit("arraySetterHelper(" + safeVarName + SoapEncSchemaTypeSystem.SOAP_ARRAY + ", " + identifier + ");");
                } else {
                    emit("arraySetterHelper(" + safeVarName + SoapEncSchemaTypeSystem.SOAP_ARRAY + ", " + identifier + ", " + setIdentifier + ");");
                }
                emitPost(sType, 1, identifier, isAttr);
                emitImplementationPostamble();
                endBlock();
            }
            printJavaDoc("Sets ith " + propdesc);
            emit("public void set" + arrayName + "(int i, " + type + SymbolConstants.SPACE_SYMBOL + safeVarName + ")");
            startBlock();
            if (xmltype && !isSubstGroup) {
                emitPre(sType, 1, identifier, isAttr, "i");
                emit("generatedSetterHelperImpl(" + safeVarName + ", " + setIdentifier + ", i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);");
                emitPost(sType, 1, identifier, isAttr, "i");
            } else {
                emitImplementationPreamble();
                emitPre(sType, 1, identifier, isAttr, "i");
                emitGetTarget(setIdentifier, identifier, isAttr, "i", 4, jtargetType);
                printJSetValue(javaType, safeVarName, (SchemaTypeImpl) prop.getType());
                emitPost(sType, 1, identifier, isAttr, "i");
                emitImplementationPostamble();
            }
            endBlock();
            if (!xmltype) {
                printJavaDoc("Sets (as xml) array of all " + propdesc);
                emit("public void xset" + arrayName + "(" + xtype + "[]" + safeVarName + "Array)");
                startBlock();
                emitImplementationPreamble();
                emitPre(sType, 1, identifier, isAttr);
                emit("arraySetterHelper(" + safeVarName + SoapEncSchemaTypeSystem.SOAP_ARRAY + ", " + identifier + ");");
                emitPost(sType, 1, identifier, isAttr);
                emitImplementationPostamble();
                endBlock();
                printJavaDoc("Sets (as xml) ith " + propdesc);
                emit("public void xset" + arrayName + "(int i, " + xtype + SymbolConstants.SPACE_SYMBOL + safeVarName + ")");
                startBlock();
                emitImplementationPreamble();
                emitPre(sType, 1, identifier, isAttr, "i");
                emitGetTarget(setIdentifier, identifier, isAttr, "i", 4, xtype);
                emit("target.set(" + safeVarName + ");");
                emitPost(sType, 1, identifier, isAttr, "i");
                emitImplementationPostamble();
                endBlock();
            }
            if (nillable) {
                printJavaDoc("Nils the ith " + propdesc);
                emit("public void setNil" + arrayName + "(int i)");
                startBlock();
                emitImplementationPreamble();
                emitPre(sType, 1, identifier, isAttr, "i");
                emitGetTarget(setIdentifier, identifier, isAttr, "i", 4, xtype);
                emit("target.setNil();");
                emitPost(sType, 1, identifier, isAttr, "i");
                emitImplementationPostamble();
                endBlock();
            }
            if (!xmltype) {
                printJavaDoc("Inserts the value as the ith " + propdesc);
                emit("public void insert" + propertyName + "(int i, " + type + SymbolConstants.SPACE_SYMBOL + safeVarName + ")");
                startBlock();
                emitImplementationPreamble();
                emitPre(sType, 2, identifier, isAttr, "i");
                emit(jtargetType + " target = ");
                indent();
                if (!isSubstGroup) {
                    emit("(" + jtargetType + ")get_store().insert_element_user(" + identifier + ", i);");
                } else {
                    emit("(" + jtargetType + ")get_store().insert_element_user(" + setIdentifier + ", " + identifier + ", i);");
                }
                outdent();
                printJSetValue(javaType, safeVarName, (SchemaTypeImpl) prop.getType());
                emitPost(sType, 2, identifier, isAttr, "i");
                emitImplementationPostamble();
                endBlock();
                printJavaDoc("Appends the value as the last " + propdesc);
                emit("public void add" + propertyName + "(" + type + SymbolConstants.SPACE_SYMBOL + safeVarName + ")");
                startBlock();
                emitImplementationPreamble();
                emitDeclareTarget(true, jtargetType);
                emitPre(sType, 2, identifier, isAttr);
                emitAddTarget(identifier, isAttr, true, jtargetType);
                printJSetValue(javaType, safeVarName, (SchemaTypeImpl) prop.getType());
                emitPost(sType, 2, identifier, isAttr);
                emitImplementationPostamble();
                endBlock();
            }
            printJavaDoc("Inserts and returns a new empty value (as xml) as the ith " + propdesc);
            emit("public " + xtype + " insertNew" + propertyName + "(int i)");
            startBlock();
            emitImplementationPreamble();
            emitDeclareTarget(true, xtype);
            emitPre(sType, 2, identifier, isAttr, "i");
            if (!isSubstGroup) {
                emit("target = (" + xtype + ")get_store().insert_element_user(" + identifier + ", i);");
            } else {
                emit("target = (" + xtype + ")get_store().insert_element_user(" + setIdentifier + ", " + identifier + ", i);");
            }
            emitPost(sType, 2, identifier, isAttr, "i");
            emit("return target;");
            emitImplementationPostamble();
            endBlock();
            printJavaDoc("Appends and returns a new empty value (as xml) as the last " + propdesc);
            emit("public " + xtype + " addNew" + propertyName + "()");
            startBlock();
            emitImplementationPreamble();
            emitDeclareTarget(true, xtype);
            emitPre(sType, 2, identifier, isAttr);
            emitAddTarget(identifier, isAttr, true, xtype);
            emitPost(sType, 2, identifier, isAttr);
            emit("return target;");
            emitImplementationPostamble();
            endBlock();
            printJavaDoc("Removes the ith " + propdesc);
            emit("public void remove" + propertyName + "(int i)");
            startBlock();
            emitImplementationPreamble();
            emitPre(sType, 3, identifier, isAttr, "i");
            emit("get_store().remove_element(" + setIdentifier + ", i);");
            emitPost(sType, 3, identifier, isAttr, "i");
            emitImplementationPostamble();
            endBlock();
        }
    }

    static void getTypeName(Class c, StringBuffer sb) {
        int arrayCount = 0;
        while (c.isArray()) {
            c = c.getComponentType();
            arrayCount++;
        }
        sb.append(c.getName());
        for (int i = 0; i < arrayCount; i++) {
            sb.append("[]");
        }
    }

    void printInnerTypeImpl(SchemaType sType, SchemaTypeSystem system, boolean isInner) throws IOException {
        SchemaProperty[] properties;
        String shortName = sType.getShortJavaImplName();
        printInnerTypeJavaDoc(sType);
        startClass(sType, isInner);
        printConstructor(sType, shortName);
        printExtensionImplMethods(sType);
        if (!sType.isSimpleType()) {
            if (sType.getContentType() == 2) {
                List extraProperties = null;
                for (SchemaType baseType = sType.getBaseType(); !baseType.isSimpleType() && !baseType.isBuiltinType(); baseType = baseType.getBaseType()) {
                    SchemaProperty[] baseProperties = baseType.getDerivedProperties();
                    for (int i = 0; i < baseProperties.length; i++) {
                        if (!baseProperties[i].isAttribute() || sType.getAttributeProperty(baseProperties[i].getName()) == null) {
                            if (extraProperties == null) {
                                extraProperties = new ArrayList();
                            }
                            extraProperties.add(baseProperties[i]);
                        }
                    }
                }
                properties = sType.getProperties();
                if (extraProperties != null) {
                    for (SchemaProperty schemaProperty : properties) {
                        extraProperties.add(schemaProperty);
                    }
                    properties = (SchemaProperty[]) extraProperties.toArray(new SchemaProperty[extraProperties.size()]);
                }
            } else {
                properties = getDerivedProperties(sType);
            }
            Map qNameMap = printStaticFields(properties);
            for (SchemaProperty prop : properties) {
                QName name = prop.getName();
                String xmlType = xmlTypeForProperty(prop);
                printGetterImpls(shortName, prop, name, prop.isAttribute(), prop.getJavaPropertyName(), prop.getJavaTypeCode(), javaTypeForProperty(prop), xmlType, prop.hasNillable() != 0, prop.extendsJavaOption(), prop.extendsJavaArray(), prop.extendsJavaSingleton(), xmlTypeForPropertyIsUnion(prop), getIdentifier(qNameMap, name), getSetIdentifier(qNameMap, name));
                if (!prop.isReadOnly()) {
                    printSetterImpls(name, prop, prop.isAttribute(), prop.getJavaPropertyName(), prop.getJavaTypeCode(), javaTypeForProperty(prop), xmlType, prop.hasNillable() != 0, prop.extendsJavaOption(), prop.extendsJavaArray(), prop.extendsJavaSingleton(), xmlTypeForPropertyIsUnion(prop), getIdentifier(qNameMap, name), getSetIdentifier(qNameMap, name), sType);
                }
            }
        }
        printNestedTypeImpls(sType, system);
        endBlock();
    }

    private SchemaProperty[] getDerivedProperties(SchemaType sType) {
        QName name = sType.getName();
        if (name != null && name.equals(sType.getBaseType().getName())) {
            SchemaProperty[] props = sType.getDerivedProperties();
            Map propsByName = new LinkedHashMap();
            for (int i = 0; i < props.length; i++) {
                propsByName.put(props[i].getName(), props[i]);
            }
            for (SchemaType sType2 = sType.getBaseType(); sType2 != null && name.equals(sType2.getName()); sType2 = sType2.getBaseType()) {
                SchemaProperty[] props2 = sType2.getDerivedProperties();
                for (int i2 = 0; i2 < props2.length; i2++) {
                    if (!propsByName.containsKey(props2[i2].getName())) {
                        propsByName.put(props2[i2].getName(), props2[i2]);
                    }
                }
            }
            return (SchemaProperty[]) propsByName.values().toArray(new SchemaProperty[0]);
        }
        return sType.getDerivedProperties();
    }

    private void printExtensionImplMethods(SchemaType sType) throws IOException {
        InterfaceExtension[] exts;
        SchemaTypeImpl sImpl = getImpl(sType);
        if (sImpl != null && (exts = sImpl.getInterfaceExtensions()) != null) {
            for (int i = 0; i < exts.length; i++) {
                InterfaceExtension.MethodSignature[] methods = exts[i].getMethods();
                if (methods != null) {
                    for (int j = 0; j < methods.length; j++) {
                        printJavaDoc("Implementation method for interface " + exts[i].getStaticHandler());
                        printInterfaceMethodDecl(methods[j]);
                        startBlock();
                        printInterfaceMethodImpl(exts[i].getStaticHandler(), methods[j]);
                        endBlock();
                    }
                }
            }
        }
    }

    void printInterfaceMethodDecl(InterfaceExtension.MethodSignature method) throws IOException {
        StringBuffer decl = new StringBuffer(60);
        decl.append("public ").append(method.getReturnType());
        decl.append(SymbolConstants.SPACE_SYMBOL).append(method.getName()).append("(");
        String[] paramTypes = method.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            if (i != 0) {
                decl.append(", ");
            }
            decl.append(paramTypes[i]).append(" p").append(i);
        }
        decl.append(")");
        String[] exceptions = method.getExceptionTypes();
        int i2 = 0;
        while (i2 < exceptions.length) {
            decl.append((i2 == 0 ? " throws " : ", ") + exceptions[i2]);
            i2++;
        }
        emit(decl.toString());
    }

    void printInterfaceMethodImpl(String handler, InterfaceExtension.MethodSignature method) throws IOException {
        StringBuffer impl = new StringBuffer(60);
        if (!method.getReturnType().equals("void")) {
            impl.append("return ");
        }
        impl.append(handler).append(".").append(method.getName()).append("(this");
        String[] params = method.getParameterTypes();
        for (int i = 0; i < params.length; i++) {
            impl.append(", p" + i);
        }
        impl.append(");");
        emit(impl.toString());
    }

    void printNestedTypeImpls(SchemaType sType, SchemaTypeSystem system) throws IOException {
        boolean redefinition = sType.getName() != null && sType.getName().equals(sType.getBaseType().getName());
        while (sType != null) {
            SchemaType[] anonTypes = sType.getAnonymousTypes();
            for (int i = 0; i < anonTypes.length; i++) {
                if (anonTypes[i].isSkippedAnonymousType()) {
                    printNestedTypeImpls(anonTypes[i], system);
                } else {
                    printInnerTypeImpl(anonTypes[i], system, true);
                }
            }
            if (!redefinition) {
                return;
            }
            if (sType.getDerivationType() == 2 || sType.isSimpleType()) {
                sType = sType.getBaseType();
            } else {
                return;
            }
        }
    }
}
