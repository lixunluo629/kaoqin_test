package org.apache.xmlbeans.impl.schema;

import com.itextpdf.kernel.xmp.PdfConst;
import io.netty.handler.codec.rtsp.RtspHeaders;
import io.swagger.models.properties.StringProperty;
import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.Filer;
import org.apache.xmlbeans.SchemaAnnotation;
import org.apache.xmlbeans.SchemaAttributeGroup;
import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.SchemaGlobalAttribute;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaIdentityConstraint;
import org.apache.xmlbeans.SchemaModelGroup;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.values.XmlIntegerImpl;
import org.apache.xmlbeans.impl.values.XmlStringImpl;
import org.apache.xmlbeans.impl.values.XmlValueOutOfRangeException;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/XQuerySchemaTypeSystem.class */
public class XQuerySchemaTypeSystem extends SchemaTypeLoaderBase implements SchemaTypeSystem {
    public static final int BTC_FIRST_XQUERY = 52;
    public static final int BTC_ANY_ATOMIC = 52;
    public static final int BTC_DAY_TIME_DURATION = 53;
    public static final int BTC_YEAR_MONTH_DURATION = 54;
    public static final int BTC_LAST_XQUERY = 54;
    private static final SchemaType[] EMPTY_SCHEMATYPE_ARRAY;
    private static final SchemaType.Ref[] EMPTY_SCHEMATYPEREF_ARRAY;
    private static final SchemaGlobalElement[] EMPTY_SCHEMAELEMENT_ARRAY;
    private static final SchemaGlobalAttribute[] EMPTY_SCHEMAATTRIBUTE_ARRAY;
    private static final SchemaModelGroup[] EMPTY_SCHEMAMODELGROUP_ARRAY;
    private static final SchemaAttributeGroup[] EMPTY_SCHEMAATTRIBUTEGROUP_ARRAY;
    private static final SchemaAnnotation[] EMPTY_SCHEMAANNOTATION_ARRAY;
    private static XQuerySchemaTypeSystem _global;
    public static final SchemaTypeImpl ST_ANY_TYPE;
    public static final SchemaTypeImpl ST_ANY_SIMPLE;
    public static final SchemaTypeImpl ST_ANY_ATOMIC;
    public static final SchemaTypeImpl ST_BOOLEAN;
    public static final SchemaTypeImpl ST_BASE_64_BINARY;
    public static final SchemaTypeImpl ST_HEX_BINARY;
    public static final SchemaTypeImpl ST_ANY_URI;
    public static final SchemaTypeImpl ST_QNAME;
    public static final SchemaTypeImpl ST_NOTATION;
    public static final SchemaTypeImpl ST_FLOAT;
    public static final SchemaTypeImpl ST_DOUBLE;
    public static final SchemaTypeImpl ST_DECIMAL;
    public static final SchemaTypeImpl ST_STRING;
    public static final SchemaTypeImpl ST_DURATION;
    public static final SchemaTypeImpl ST_DATE_TIME;
    public static final SchemaTypeImpl ST_TIME;
    public static final SchemaTypeImpl ST_DATE;
    public static final SchemaTypeImpl ST_G_YEAR_MONTH;
    public static final SchemaTypeImpl ST_G_YEAR;
    public static final SchemaTypeImpl ST_G_MONTH_DAY;
    public static final SchemaTypeImpl ST_G_DAY;
    public static final SchemaTypeImpl ST_G_MONTH;
    public static final SchemaTypeImpl ST_INTEGER;
    public static final SchemaTypeImpl ST_LONG;
    public static final SchemaTypeImpl ST_INT;
    public static final SchemaTypeImpl ST_SHORT;
    public static final SchemaTypeImpl ST_BYTE;
    public static final SchemaTypeImpl ST_NON_POSITIVE_INTEGER;
    public static final SchemaTypeImpl ST_NEGATIVE_INTEGER;
    public static final SchemaTypeImpl ST_NON_NEGATIVE_INTEGER;
    public static final SchemaTypeImpl ST_POSITIVE_INTEGER;
    public static final SchemaTypeImpl ST_UNSIGNED_LONG;
    public static final SchemaTypeImpl ST_UNSIGNED_INT;
    public static final SchemaTypeImpl ST_UNSIGNED_SHORT;
    public static final SchemaTypeImpl ST_UNSIGNED_BYTE;
    public static final SchemaTypeImpl ST_NORMALIZED_STRING;
    public static final SchemaTypeImpl ST_TOKEN;
    public static final SchemaTypeImpl ST_NAME;
    public static final SchemaTypeImpl ST_NCNAME;
    public static final SchemaTypeImpl ST_LANGUAGE;
    public static final SchemaTypeImpl ST_ID;
    public static final SchemaTypeImpl ST_IDREF;
    public static final SchemaTypeImpl ST_IDREFS;
    public static final SchemaTypeImpl ST_ENTITY;
    public static final SchemaTypeImpl ST_ENTITIES;
    public static final SchemaTypeImpl ST_NMTOKEN;
    public static final SchemaTypeImpl ST_NMTOKENS;
    public static final SchemaTypeImpl ST_DAY_TIME_DURATION;
    public static final SchemaTypeImpl ST_YEAR_MONTH_DURATION;
    public static final SchemaTypeImpl ST_NO_TYPE;
    private static final XmlValueRef XMLSTR_PRESERVE;
    private static final XmlValueRef XMLSTR_REPLACE;
    private static final XmlValueRef XMLSTR_COLLAPSE;
    private static final XmlValueRef[] FACETS_NONE;
    private static final boolean[] FIXED_FACETS_NONE;
    private static final XmlValueRef[] FACETS_WS_COLLAPSE;
    private static final XmlValueRef[] FACETS_WS_REPLACE;
    private static final XmlValueRef[] FACETS_WS_PRESERVE;
    private static final XmlValueRef[] FACETS_INTEGER;
    private static final XmlValueRef[] FACETS_LONG;
    private static final XmlValueRef[] FACETS_INT;
    private static final XmlValueRef[] FACETS_SHORT;
    private static final XmlValueRef[] FACETS_BYTE;
    private static final XmlValueRef[] FACETS_NONNEGATIVE;
    private static final XmlValueRef[] FACETS_POSITIVE;
    private static final XmlValueRef[] FACETS_NONPOSITIVE;
    private static final XmlValueRef[] FACETS_NEGATIVE;
    private static final XmlValueRef[] FACETS_UNSIGNED_LONG;
    private static final XmlValueRef[] FACETS_UNSIGNED_INT;
    private static final XmlValueRef[] FACETS_UNSIGNED_SHORT;
    private static final XmlValueRef[] FACETS_UNSIGNED_BYTE;
    private static final XmlValueRef[] FACETS_BUILTIN_LIST;
    private static final boolean[] FIXED_FACETS_WS;
    private static final boolean[] FIXED_FACETS_INTEGER;
    static final XmlValueRef[] FACETS_UNION;
    static final boolean[] FIXED_FACETS_UNION;
    static final XmlValueRef[] FACETS_LIST;
    static final boolean[] FIXED_FACETS_LIST;
    private Map _typeMap = new HashMap();
    private SchemaTypeImpl[] _typeArray = new SchemaTypeImpl[50];
    private Map _handlesToObjects = new HashMap();
    private Map _objectsToHandles = new HashMap();
    private Map _typesByClassname = new HashMap();
    private SchemaContainer _container = new SchemaContainer("http://www.w3.org/2001/XMLSchema");
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !XQuerySchemaTypeSystem.class.desiredAssertionStatus();
        EMPTY_SCHEMATYPE_ARRAY = new SchemaType[0];
        EMPTY_SCHEMATYPEREF_ARRAY = new SchemaType.Ref[0];
        EMPTY_SCHEMAELEMENT_ARRAY = new SchemaGlobalElement[0];
        EMPTY_SCHEMAATTRIBUTE_ARRAY = new SchemaGlobalAttribute[0];
        EMPTY_SCHEMAMODELGROUP_ARRAY = new SchemaModelGroup[0];
        EMPTY_SCHEMAATTRIBUTEGROUP_ARRAY = new SchemaAttributeGroup[0];
        EMPTY_SCHEMAANNOTATION_ARRAY = new SchemaAnnotation[0];
        _global = new XQuerySchemaTypeSystem();
        ST_ANY_TYPE = _global.getBuiltinType(1);
        ST_ANY_SIMPLE = _global.getBuiltinType(2);
        ST_ANY_ATOMIC = _global.getBuiltinType(52);
        ST_BOOLEAN = _global.getBuiltinType(3);
        ST_BASE_64_BINARY = _global.getBuiltinType(4);
        ST_HEX_BINARY = _global.getBuiltinType(5);
        ST_ANY_URI = _global.getBuiltinType(6);
        ST_QNAME = _global.getBuiltinType(7);
        ST_NOTATION = _global.getBuiltinType(8);
        ST_FLOAT = _global.getBuiltinType(9);
        ST_DOUBLE = _global.getBuiltinType(10);
        ST_DECIMAL = _global.getBuiltinType(11);
        ST_STRING = _global.getBuiltinType(12);
        ST_DURATION = _global.getBuiltinType(13);
        ST_DATE_TIME = _global.getBuiltinType(14);
        ST_TIME = _global.getBuiltinType(15);
        ST_DATE = _global.getBuiltinType(16);
        ST_G_YEAR_MONTH = _global.getBuiltinType(17);
        ST_G_YEAR = _global.getBuiltinType(18);
        ST_G_MONTH_DAY = _global.getBuiltinType(19);
        ST_G_DAY = _global.getBuiltinType(20);
        ST_G_MONTH = _global.getBuiltinType(21);
        ST_INTEGER = _global.getBuiltinType(22);
        ST_LONG = _global.getBuiltinType(23);
        ST_INT = _global.getBuiltinType(24);
        ST_SHORT = _global.getBuiltinType(25);
        ST_BYTE = _global.getBuiltinType(26);
        ST_NON_POSITIVE_INTEGER = _global.getBuiltinType(27);
        ST_NEGATIVE_INTEGER = _global.getBuiltinType(28);
        ST_NON_NEGATIVE_INTEGER = _global.getBuiltinType(29);
        ST_POSITIVE_INTEGER = _global.getBuiltinType(30);
        ST_UNSIGNED_LONG = _global.getBuiltinType(31);
        ST_UNSIGNED_INT = _global.getBuiltinType(32);
        ST_UNSIGNED_SHORT = _global.getBuiltinType(33);
        ST_UNSIGNED_BYTE = _global.getBuiltinType(34);
        ST_NORMALIZED_STRING = _global.getBuiltinType(35);
        ST_TOKEN = _global.getBuiltinType(36);
        ST_NAME = _global.getBuiltinType(37);
        ST_NCNAME = _global.getBuiltinType(38);
        ST_LANGUAGE = _global.getBuiltinType(39);
        ST_ID = _global.getBuiltinType(40);
        ST_IDREF = _global.getBuiltinType(41);
        ST_IDREFS = _global.getBuiltinType(42);
        ST_ENTITY = _global.getBuiltinType(43);
        ST_ENTITIES = _global.getBuiltinType(44);
        ST_NMTOKEN = _global.getBuiltinType(45);
        ST_NMTOKENS = _global.getBuiltinType(46);
        ST_DAY_TIME_DURATION = _global.getBuiltinType(53);
        ST_YEAR_MONTH_DURATION = _global.getBuiltinType(54);
        ST_NO_TYPE = _global.getBuiltinType(0);
        XMLSTR_PRESERVE = buildString("preserve");
        XMLSTR_REPLACE = buildString("preserve");
        XMLSTR_COLLAPSE = buildString("preserve");
        FACETS_NONE = new XmlValueRef[]{null, null, null, null, null, null, null, null, null, null, null, null};
        FIXED_FACETS_NONE = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false};
        FACETS_WS_COLLAPSE = new XmlValueRef[]{null, null, null, null, null, null, null, null, null, build_wsstring(3), null, null};
        FACETS_WS_REPLACE = new XmlValueRef[]{null, null, null, null, null, null, null, null, null, build_wsstring(2), null, null};
        FACETS_WS_PRESERVE = new XmlValueRef[]{null, null, null, null, null, null, null, null, null, build_wsstring(1), null, null};
        FACETS_INTEGER = new XmlValueRef[]{null, null, null, null, null, null, null, null, buildNnInteger(BigInteger.ZERO), build_wsstring(3), null, null};
        FACETS_LONG = new XmlValueRef[]{null, null, null, null, buildInteger(BigInteger.valueOf(Long.MIN_VALUE)), buildInteger(BigInteger.valueOf(Long.MAX_VALUE)), null, null, buildNnInteger(BigInteger.ZERO), build_wsstring(3), null, null};
        FACETS_INT = new XmlValueRef[]{null, null, null, null, buildInteger(BigInteger.valueOf(-2147483648L)), buildInteger(BigInteger.valueOf(2147483647L)), null, null, buildNnInteger(BigInteger.ZERO), build_wsstring(3), null, null};
        FACETS_SHORT = new XmlValueRef[]{null, null, null, null, buildInteger(BigInteger.valueOf(-32768L)), buildInteger(BigInteger.valueOf(32767L)), null, null, buildNnInteger(BigInteger.ZERO), build_wsstring(3), null, null};
        FACETS_BYTE = new XmlValueRef[]{null, null, null, null, buildInteger(BigInteger.valueOf(-128L)), buildInteger(BigInteger.valueOf(127L)), null, null, buildNnInteger(BigInteger.ZERO), build_wsstring(3), null, null};
        FACETS_NONNEGATIVE = new XmlValueRef[]{null, null, null, null, buildInteger(BigInteger.ZERO), null, null, null, buildNnInteger(BigInteger.ZERO), build_wsstring(3), null, null};
        FACETS_POSITIVE = new XmlValueRef[]{null, null, null, null, buildInteger(BigInteger.ONE), null, null, null, buildNnInteger(BigInteger.ZERO), build_wsstring(3), null, null};
        FACETS_NONPOSITIVE = new XmlValueRef[]{null, null, null, null, null, buildInteger(BigInteger.ZERO), null, null, buildNnInteger(BigInteger.ZERO), build_wsstring(3), null, null};
        FACETS_NEGATIVE = new XmlValueRef[]{null, null, null, null, null, buildInteger(BigInteger.ONE.negate()), null, null, buildNnInteger(BigInteger.ZERO), build_wsstring(3), null, null};
        FACETS_UNSIGNED_LONG = new XmlValueRef[]{null, null, null, null, buildInteger(BigInteger.ZERO), buildInteger(new BigInteger("18446744073709551615")), null, null, buildNnInteger(BigInteger.ZERO), build_wsstring(3), null, null};
        FACETS_UNSIGNED_INT = new XmlValueRef[]{null, null, null, null, buildInteger(BigInteger.ZERO), buildInteger(BigInteger.valueOf(4294967295L)), null, null, buildNnInteger(BigInteger.ZERO), build_wsstring(3), null, null};
        FACETS_UNSIGNED_SHORT = new XmlValueRef[]{null, null, null, null, buildInteger(BigInteger.ZERO), buildInteger(BigInteger.valueOf(65535L)), null, null, buildNnInteger(BigInteger.ZERO), build_wsstring(3), null, null};
        FACETS_UNSIGNED_BYTE = new XmlValueRef[]{null, null, null, null, buildInteger(BigInteger.ZERO), buildInteger(BigInteger.valueOf(255L)), null, null, buildNnInteger(BigInteger.ZERO), build_wsstring(3), null, null};
        FACETS_BUILTIN_LIST = new XmlValueRef[]{null, buildNnInteger(BigInteger.ONE), null, null, null, null, null, null, null, build_wsstring(3), null, null};
        FIXED_FACETS_WS = new boolean[]{false, false, false, false, false, false, false, false, false, true, false, false};
        FIXED_FACETS_INTEGER = new boolean[]{false, false, false, false, false, false, false, false, true, true, false, false};
        FACETS_UNION = FACETS_NONE;
        FIXED_FACETS_UNION = FIXED_FACETS_NONE;
        FACETS_LIST = FACETS_WS_COLLAPSE;
        FIXED_FACETS_LIST = FIXED_FACETS_WS;
        for (int i = 0; i <= 46; i++) {
            _global.fillInType(i);
        }
        for (int i2 = 52; i2 <= 54; i2++) {
            _global.fillInType(i2);
        }
    }

    public static SchemaTypeSystem get() {
        return _global;
    }

    private SchemaTypeImpl getBuiltinType(int btc) {
        return this._typeArray[arrayIndexForBtc(btc)];
    }

    private XQuerySchemaTypeSystem() {
        this._container.setTypeSystem(this);
        setupType(1, "anyType", "org.apache.xmlbeans.XmlObject");
        setupType(2, "anySimpleType", "org.apache.xmlbeans.XmlAnySimpleType");
        setupType(52, "anyAtomicType", null);
        setupType(3, "boolean", "org.apache.xmlbeans.XmlBoolean");
        setupType(4, XmlErrorCodes.BASE64BINARY, "org.apache.xmlbeans.XmlBase64Binary");
        setupType(5, XmlErrorCodes.HEXBINARY, "org.apache.xmlbeans.XmlHexBinary");
        setupType(6, XmlErrorCodes.ANYURI, "org.apache.xmlbeans.XmlAnyURI");
        setupType(7, XmlErrorCodes.QNAME, "org.apache.xmlbeans.XmlQName");
        setupType(8, "NOTATION", "org.apache.xmlbeans.XmlNOTATION");
        setupType(9, XmlErrorCodes.FLOAT, "org.apache.xmlbeans.XmlFloat");
        setupType(10, XmlErrorCodes.DOUBLE, "org.apache.xmlbeans.XmlDouble");
        setupType(11, XmlErrorCodes.DECIMAL, "org.apache.xmlbeans.XmlDecimal");
        setupType(12, StringProperty.TYPE, "org.apache.xmlbeans.XmlString");
        setupType(13, XmlErrorCodes.DURATION, "org.apache.xmlbeans.XmlDuration");
        setupType(14, "dateTime", "org.apache.xmlbeans.XmlDateTime");
        setupType(15, RtspHeaders.Values.TIME, "org.apache.xmlbeans.XmlTime");
        setupType(16, "date", "org.apache.xmlbeans.XmlDate");
        setupType(17, "gYearMonth", "org.apache.xmlbeans.XmlGYearMonth");
        setupType(18, "gYear", "org.apache.xmlbeans.XmlGYear");
        setupType(19, "gMonthDay", "org.apache.xmlbeans.XmlGMonthDay");
        setupType(20, "gDay", "org.apache.xmlbeans.XmlGDay");
        setupType(21, "gMonth", "org.apache.xmlbeans.XmlGMonth");
        setupType(22, "integer", "org.apache.xmlbeans.XmlInteger");
        setupType(23, XmlErrorCodes.LONG, "org.apache.xmlbeans.XmlLong");
        setupType(24, XmlErrorCodes.INT, "org.apache.xmlbeans.XmlInt");
        setupType(25, "short", "org.apache.xmlbeans.XmlShort");
        setupType(26, "byte", "org.apache.xmlbeans.XmlByte");
        setupType(27, "nonPositiveInteger", "org.apache.xmlbeans.XmlNonPositiveInteger");
        setupType(28, "negativeInteger", "org.apache.xmlbeans.XmlNegativeInteger");
        setupType(29, "nonNegativeInteger", "org.apache.xmlbeans.XmlNonNegativeInteger");
        setupType(30, "positiveInteger", "org.apache.xmlbeans.XmlPositiveInteger");
        setupType(31, "unsignedLong", "org.apache.xmlbeans.XmlUnsignedLong");
        setupType(32, "unsignedInt", "org.apache.xmlbeans.XmlUnsignedInt");
        setupType(33, "unsignedShort", "org.apache.xmlbeans.XmlUnsignedShort");
        setupType(34, "unsignedByte", "org.apache.xmlbeans.XmlUnsignedByte");
        setupType(35, "normalizedString", "org.apache.xmlbeans.XmlNormalizedString");
        setupType(36, "token", "org.apache.xmlbeans.XmlToken");
        setupType(37, "Name", "org.apache.xmlbeans.XmlName");
        setupType(38, XmlErrorCodes.NCNAME, "org.apache.xmlbeans.XmlNCName");
        setupType(39, PdfConst.Language, "org.apache.xmlbeans.XmlLanguage");
        setupType(40, "ID", "org.apache.xmlbeans.XmlID");
        setupType(41, "IDREF", "org.apache.xmlbeans.XmlIDREF");
        setupType(42, "IDREFS", "org.apache.xmlbeans.XmlIDREFS");
        setupType(43, "ENTITY", "org.apache.xmlbeans.XmlENTITY");
        setupType(44, "ENTITIES", "org.apache.xmlbeans.XmlENTITIES");
        setupType(45, XmlErrorCodes.NMTOKEN, "org.apache.xmlbeans.XmlNMTOKEN");
        setupType(46, "NMTOKENS", "org.apache.xmlbeans.XmlNMTOKENS");
        setupType(53, "dayTimeDuration", null);
        setupType(54, "yearMonthDuration", null);
        setupType(0, null, null);
        this._container.setImmutable();
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public String getName() {
        return "xquery.typesystem.builtin";
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public boolean isNamespaceDefined(String namespace) {
        return namespace.equals("http://www.w3.org/2001/XMLSchema");
    }

    @Override // org.apache.xmlbeans.impl.schema.SchemaTypeLoaderBase, org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType findType(QName name) {
        return (SchemaType) this._typeMap.get(name);
    }

    @Override // org.apache.xmlbeans.impl.schema.SchemaTypeLoaderBase, org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType findDocumentType(QName name) {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.schema.SchemaTypeLoaderBase, org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType findAttributeType(QName name) {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.schema.SchemaTypeLoaderBase, org.apache.xmlbeans.SchemaTypeLoader
    public SchemaGlobalElement findElement(QName name) {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.schema.SchemaTypeLoaderBase, org.apache.xmlbeans.SchemaTypeLoader
    public SchemaGlobalAttribute findAttribute(QName name) {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType.Ref findTypeRef(QName name) {
        SchemaType type = findType(name);
        if (type == null) {
            return null;
        }
        return type.getRef();
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType.Ref findDocumentTypeRef(QName name) {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType.Ref findAttributeTypeRef(QName name) {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaGlobalElement.Ref findElementRef(QName name) {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaGlobalAttribute.Ref findAttributeRef(QName name) {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaModelGroup.Ref findModelGroupRef(QName name) {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaAttributeGroup.Ref findAttributeGroupRef(QName name) {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaIdentityConstraint.Ref findIdentityConstraintRef(QName name) {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType typeForClassname(String classname) {
        return (SchemaType) this._typesByClassname.get(classname);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public InputStream getSourceAsStream(String sourceName) {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaType[] globalTypes() {
        SchemaType[] result = new SchemaType[this._typeArray.length - 1];
        System.arraycopy(this._typeArray, 1, result, 0, result.length);
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaType[] documentTypes() {
        return EMPTY_SCHEMATYPE_ARRAY;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaType[] attributeTypes() {
        return EMPTY_SCHEMATYPE_ARRAY;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaGlobalElement[] globalElements() {
        return EMPTY_SCHEMAELEMENT_ARRAY;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaGlobalAttribute[] globalAttributes() {
        return EMPTY_SCHEMAATTRIBUTE_ARRAY;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaModelGroup[] modelGroups() {
        return EMPTY_SCHEMAMODELGROUP_ARRAY;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaAttributeGroup[] attributeGroups() {
        return EMPTY_SCHEMAATTRIBUTEGROUP_ARRAY;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaAnnotation[] annotations() {
        return EMPTY_SCHEMAANNOTATION_ARRAY;
    }

    public String handleForType(SchemaType type) {
        return (String) this._objectsToHandles.get(type);
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public ClassLoader getClassLoader() {
        return BuiltinSchemaTypeSystem.class.getClassLoader();
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public void saveToDirectory(File classDir) {
        throw new UnsupportedOperationException("The builtin schema type system cannot be saved.");
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public void save(Filer filer) {
        throw new UnsupportedOperationException("The builtin schema type system cannot be saved.");
    }

    private int arrayIndexForBtc(int btc) {
        return btc > 46 ? (btc - 52) + 46 + 1 : btc;
    }

    private static XmlValueRef build_wsstring(int wsr) {
        switch (wsr) {
            case 1:
                return XMLSTR_PRESERVE;
            case 2:
                return XMLSTR_REPLACE;
            case 3:
                return XMLSTR_COLLAPSE;
            default:
                return null;
        }
    }

    private static XmlValueRef buildNnInteger(BigInteger bigInt) {
        if (bigInt == null || bigInt.signum() < 0) {
            return null;
        }
        try {
            XmlIntegerImpl i = new XmlIntegerImpl();
            i.setBigIntegerValue(bigInt);
            i.setImmutable();
            return new XmlValueRef(i);
        } catch (XmlValueOutOfRangeException e) {
            return null;
        }
    }

    private static XmlValueRef buildInteger(BigInteger bigInt) {
        if (bigInt == null) {
            return null;
        }
        try {
            XmlIntegerImpl i = new XmlIntegerImpl();
            i.setBigIntegerValue(bigInt);
            i.setImmutable();
            return new XmlValueRef(i);
        } catch (XmlValueOutOfRangeException e) {
            return null;
        }
    }

    private static XmlValueRef buildString(String str) {
        if (str == null) {
            return null;
        }
        try {
            XmlStringImpl i = new XmlStringImpl();
            i.setStringValue(str);
            i.setImmutable();
            return new XmlValueRef(i);
        } catch (XmlValueOutOfRangeException e) {
            return null;
        }
    }

    private void setupType(int btc, String localname, String classname) {
        SchemaTypeImpl result = new SchemaTypeImpl(this._container, true);
        this._container.addGlobalType(result.getRef());
        QName name = localname == null ? null : QNameHelper.forLNS(localname, "http://www.w3.org/2001/XMLSchema");
        String handle = "_BI_" + (localname == null ? "NO_TYPE" : localname);
        result.setName(name);
        result.setBuiltinTypeCode(btc);
        if (classname != null) {
            result.setFullJavaName(classname);
        }
        this._typeArray[arrayIndexForBtc(btc)] = result;
        this._typeMap.put(name, result);
        this._handlesToObjects.put(handle, result);
        this._objectsToHandles.put(result, handle);
        if (classname != null) {
            this._typesByClassname.put(classname, result);
        }
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public void resolve() {
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaType typeForHandle(String handle) {
        return (SchemaType) this._handlesToObjects.get(handle);
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaComponent resolveHandle(String handle) {
        return (SchemaComponent) this._handlesToObjects.get(handle);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:100:0x0499  */
    /* JADX WARN: Removed duplicated region for block: B:101:0x04aa  */
    /* JADX WARN: Removed duplicated region for block: B:102:0x04bb  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x04cc  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x04dd  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x04ed  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x04fd  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x050d  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0610  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0621  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0627  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0630  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0636  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x06d4  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x06de  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x06e8  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x06f2  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x06fc  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0706  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0710  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0718  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x074d  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x07cb  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x01f6  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x01fe  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x021a  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x021e  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x022c  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0230  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0242  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0246  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0255  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x025b  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0272  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x03b4  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x03c2  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x03d2  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x03e2  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x03ef  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0400  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0411  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0422  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0433  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0444  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0455  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0466  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0477  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0488  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void fillInType(int r8) {
        /*
            Method dump skipped, instructions count: 2056
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.schema.XQuerySchemaTypeSystem.fillInType(int):void");
    }

    public static SchemaType getNoType() {
        return ST_NO_TYPE;
    }
}
