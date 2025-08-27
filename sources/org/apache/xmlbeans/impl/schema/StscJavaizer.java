package org.apache.xmlbeans.impl.schema;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.BindingConfig;
import org.apache.xmlbeans.InterfaceExtension;
import org.apache.xmlbeans.PrePostExtension;
import org.apache.xmlbeans.SchemaField;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaStringEnumEntry;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.UserType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.NameUtil;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.data.repository.config.DefaultRepositoryConfiguration;
import redis.clients.jedis.Protocol;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/StscJavaizer.class */
public class StscJavaizer {
    private static final int MAX_ENUM_COUNT = 3668;
    private static final String[] PREFIXES;
    static String[] PROTECTED_PROPERTIES;
    static Set PROTECTED_PROPERTIES_SET;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StscJavaizer.class.desiredAssertionStatus();
        PREFIXES = new String[]{BeanUtil.PREFIX_GETTER_GET, "xget", "isNil", "isSet", "sizeOf", "set", "xset", "addNew", "setNil", "unset", "insert", BeanUtil.PREFIX_ADDER, "insertNew", "addNew", Protocol.SENTINEL_REMOVE};
        PROTECTED_PROPERTIES = new String[]{"StringValue", "BooleanValue", "ByteValue", "ShortValue", "IntValue", "LongValue", "BigIntegerValue", "BigDecimalValue", "FloatValue", "DoubleValue", "ByteArrayValue", "EnumValue", "CalendarValue", "DateValue", "GDateValue", "GDurationValue", "QNameValue", "ListValue", "ObjectValue", "Class"};
        PROTECTED_PROPERTIES_SET = new HashSet(Arrays.asList(PROTECTED_PROPERTIES));
    }

    public static void javaizeAllTypes(boolean javaize) throws IllegalArgumentException {
        StscState state = StscState.get();
        List allSeenTypes = new ArrayList();
        allSeenTypes.addAll(Arrays.asList(state.documentTypes()));
        allSeenTypes.addAll(Arrays.asList(state.attributeTypes()));
        allSeenTypes.addAll(Arrays.asList(state.globalTypes()));
        if (javaize) {
            assignGlobalJavaNames(allSeenTypes);
        }
        for (int i = 0; i < allSeenTypes.size(); i++) {
            SchemaType gType = (SchemaType) allSeenTypes.get(i);
            if (javaize) {
                javaizeType((SchemaTypeImpl) gType);
                String className = gType.getFullJavaName();
                if (className != null) {
                    state.addClassname(className.replace('$', '.'), gType);
                }
            } else {
                skipJavaizingType((SchemaTypeImpl) gType);
            }
            allSeenTypes.addAll(Arrays.asList(gType.getAnonymousTypes()));
            addAnonymousTypesFromRedefinition(gType, allSeenTypes);
        }
    }

    static void assignGlobalJavaNames(Collection schemaTypes) throws IllegalArgumentException {
        HashSet usedNames = new HashSet();
        StscState state = StscState.get();
        Iterator i = schemaTypes.iterator();
        while (i.hasNext()) {
            SchemaTypeImpl sImpl = (SchemaTypeImpl) i.next();
            QName topName = findTopName(sImpl);
            String pickedName = state.getJavaname(topName, sImpl.isDocumentType() ? 2 : 1);
            if (sImpl.isUnjavaized()) {
                sImpl.setFullJavaName(pickFullJavaClassName(usedNames, findTopName(sImpl), pickedName, sImpl.isDocumentType(), sImpl.isAttributeType()));
                sImpl.setFullJavaImplName(pickFullJavaImplName(usedNames, sImpl.getFullJavaName()));
                setUserTypes(sImpl, state);
                setExtensions(sImpl, state);
            }
        }
        verifyInterfaceNameCollisions(usedNames, state);
    }

    private static void verifyInterfaceNameCollisions(Set usedNames, StscState state) throws IllegalArgumentException {
        BindingConfig config = state.getBindingConfig();
        if (config == null) {
            return;
        }
        InterfaceExtension[] exts = config.getInterfaceExtensions();
        for (int i = 0; i < exts.length; i++) {
            if (usedNames.contains(exts[i].getInterface().toLowerCase())) {
                state.error("InterfaceExtension interface '" + exts[i].getInterface() + "' creates a name collision with one of the generated interfaces or classes.", 0, (XmlObject) null);
            }
            String handler = exts[i].getStaticHandler();
            if (handler != null && usedNames.contains(handler.toLowerCase())) {
                state.error("InterfaceExtension handler class '" + handler + "' creates a name collision with one of the generated interfaces or classes.", 0, (XmlObject) null);
            }
        }
        PrePostExtension[] prepost = config.getPrePostExtensions();
        for (PrePostExtension prePostExtension : prepost) {
            String handler2 = prePostExtension.getStaticHandler();
            if (handler2 != null && usedNames.contains(handler2.toLowerCase())) {
                state.error("PrePostExtension handler class '" + handler2 + "' creates a name collision with one of the generated interfaces or classes.", 0, (XmlObject) null);
            }
        }
    }

    private static void setUserTypes(SchemaTypeImpl sImpl, StscState state) throws IllegalArgumentException {
        UserType utype;
        BindingConfig config = state.getBindingConfig();
        if (config != null && (utype = config.lookupUserTypeForQName(sImpl.getName())) != null) {
            sImpl.setUserTypeName(utype.getJavaName());
            sImpl.setUserTypeHandlerName(utype.getStaticHandler());
        }
    }

    private static void setExtensions(SchemaTypeImpl sImpl, StscState state) throws IllegalArgumentException {
        String javaName = sImpl.getFullJavaName();
        BindingConfig config = state.getBindingConfig();
        if (javaName != null && config != null) {
            sImpl.setInterfaceExtensions(config.getInterfaceExtensions(javaName));
            sImpl.setPrePostExtension(config.getPrePostExtension(javaName));
        }
    }

    private static boolean isStringType(SchemaType type) {
        return type != null && type.getSimpleVariety() == 1 && type.getPrimitiveType().getBuiltinTypeCode() == 12;
    }

    static String pickConstantName(Set usedNames, String words) {
        String base = NameUtil.upperCaseUnderbar(words);
        if (base.length() == 0) {
            base = "X";
        }
        if (base.startsWith("INT_")) {
            base = "X_" + base;
        }
        int index = 1;
        String str = base;
        while (true) {
            String uniqName = str;
            if (usedNames.contains(uniqName)) {
                index++;
                str = base + "_" + index;
            } else {
                usedNames.add(uniqName);
                return uniqName;
            }
        }
    }

    static void skipJavaizingType(SchemaTypeImpl sImpl) {
        if (sImpl.isJavaized()) {
            return;
        }
        SchemaTypeImpl baseType = (SchemaTypeImpl) sImpl.getBaseType();
        if (baseType != null) {
            skipJavaizingType(baseType);
        }
        sImpl.startJavaizing();
        secondPassProcessType(sImpl);
        sImpl.finishJavaizing();
    }

    static void secondPassProcessType(SchemaTypeImpl sImpl) {
        XmlAnySimpleType[] enumVals;
        if (isStringType(sImpl) && (enumVals = sImpl.getEnumerationValues()) != null) {
            if (enumVals.length > MAX_ENUM_COUNT) {
                StscState.get().warning("SchemaType Enumeration found with too many enumeration values to create a Java enumeration. The base SchemaType \"" + sImpl.getBaseEnumType() + "\" will be used instead", 1, (XmlObject) null);
                return;
            }
            SchemaStringEnumEntry[] entryArray = new SchemaStringEnumEntry[enumVals.length];
            SchemaType basedOn = sImpl.getBaseEnumType();
            if (basedOn == sImpl) {
                Set usedNames = new HashSet();
                for (int i = 0; i < enumVals.length; i++) {
                    String val = enumVals[i].getStringValue();
                    entryArray[i] = new SchemaStringEnumEntryImpl(val, i + 1, pickConstantName(usedNames, val));
                }
            } else {
                for (int i2 = 0; i2 < enumVals.length; i2++) {
                    entryArray[i2] = basedOn.enumEntryForString(enumVals[i2].getStringValue());
                }
            }
            sImpl.setStringEnumEntries(entryArray);
        }
    }

    static void javaizeType(SchemaTypeImpl sImpl) throws IllegalArgumentException {
        if (sImpl.isJavaized()) {
            return;
        }
        SchemaTypeImpl baseType = (SchemaTypeImpl) sImpl.getBaseType();
        if (baseType != null) {
            javaizeType(baseType);
        }
        if (sImpl.getContentBasedOnType() != null && sImpl.getContentBasedOnType() != baseType) {
            javaizeType((SchemaTypeImpl) sImpl.getContentBasedOnType());
        }
        sImpl.startJavaizing();
        sImpl.setCompiled(true);
        secondPassProcessType(sImpl);
        if (!sImpl.isSimpleType()) {
            SchemaProperty[] eltProps = sImpl.getElementProperties();
            SchemaProperty[] attrProps = sImpl.getAttributeProperties();
            Set usedPropNames = new HashSet();
            SchemaProperty[] baseProps = baseType.getProperties();
            for (SchemaProperty schemaProperty : baseProps) {
                String name = schemaProperty.getJavaPropertyName();
                if (!$assertionsDisabled && usedPropNames.contains(name)) {
                    throw new AssertionError();
                }
                usedPropNames.add(name);
            }
            avoidExtensionMethods(usedPropNames, sImpl);
            boolean z = true;
            while (true) {
                boolean doInherited = z;
                if (eltProps.length > 0) {
                    assignJavaPropertyNames(usedPropNames, eltProps, baseType, doInherited);
                }
                assignJavaPropertyNames(usedPropNames, attrProps, baseType, doInherited);
                if (!doInherited) {
                    break;
                } else {
                    z = false;
                }
            }
            SchemaProperty[] allprops = sImpl.getProperties();
            boolean insensitive = isPropertyModelOrderInsensitive(allprops);
            assignJavaTypeCodes(allprops);
            sImpl.setOrderSensitive(!insensitive);
        }
        if (sImpl.getFullJavaName() != null || sImpl.getOuterType() != null) {
            assignJavaAnonymousTypeNames(sImpl);
        }
        sImpl.finishJavaizing();
    }

    private static void avoidExtensionMethods(Set usedPropNames, SchemaTypeImpl sImpl) {
        InterfaceExtension[] exts = sImpl.getInterfaceExtensions();
        if (exts != null) {
            for (InterfaceExtension ext : exts) {
                InterfaceExtension.MethodSignature[] methods = ext.getMethods();
                for (InterfaceExtension.MethodSignature methodSignature : methods) {
                    String methodName = methodSignature.getName();
                    for (int k = 0; k < PREFIXES.length; k++) {
                        String prefix = PREFIXES[k];
                        if (methodName.startsWith(prefix)) {
                            usedPropNames.add(methodName.substring(prefix.length()));
                        }
                    }
                }
            }
        }
    }

    static void assignJavaAnonymousTypeNames(SchemaTypeImpl outerType) throws IllegalArgumentException {
        String javaname;
        Set usedTypeNames = new HashSet();
        SchemaType[] anonymousTypes = outerType.getAnonymousTypes();
        StscState state = StscState.get();
        int nrOfAnonTypes = anonymousTypes.length;
        if (outerType.isRedefinition()) {
            ArrayList list = new ArrayList();
            addAnonymousTypesFromRedefinition(outerType, list);
            if (list.size() > 0) {
                SchemaType[] temp = new SchemaType[nrOfAnonTypes + list.size()];
                list.toArray(temp);
                System.arraycopy(anonymousTypes, 0, temp, list.size(), nrOfAnonTypes);
                anonymousTypes = temp;
            }
        }
        SchemaType outerType2 = outerType;
        while (true) {
            SchemaType scanOuterType = outerType2;
            if (scanOuterType == null) {
                break;
            }
            usedTypeNames.add(scanOuterType.getShortJavaName());
            outerType2 = scanOuterType.getOuterType();
        }
        SchemaType outerType3 = outerType;
        while (true) {
            SchemaType scanOuterType2 = outerType3;
            if (scanOuterType2 == null) {
                break;
            }
            usedTypeNames.add(scanOuterType2.getShortJavaImplName());
            outerType3 = scanOuterType2.getOuterType();
        }
        usedTypeNames.add(getOutermostPackage(outerType.getFullJavaName()));
        for (int i = 0; i < anonymousTypes.length; i++) {
            SchemaTypeImpl sImpl = (SchemaTypeImpl) anonymousTypes[i];
            if (sImpl != null && !sImpl.isSkippedAnonymousType()) {
                String localname = null;
                SchemaField containerField = sImpl.getContainerField();
                if (containerField != null) {
                    QName qname = sImpl.getContainerField().getName();
                    localname = qname.getLocalPart();
                    javaname = state.getJavaname(sImpl.getContainerField().getName(), 1);
                } else {
                    switch (sImpl.getOuterType().getSimpleVariety()) {
                        case 1:
                        default:
                            if (!$assertionsDisabled) {
                                throw new AssertionError("Weird type " + sImpl.toString());
                            }
                            javaname = "Base";
                            break;
                        case 2:
                            javaname = "Member";
                            break;
                        case 3:
                            javaname = "Item";
                            break;
                    }
                }
                if (i < nrOfAnonTypes) {
                    sImpl.setShortJavaName(pickInnerJavaClassName(usedTypeNames, localname, javaname));
                    sImpl.setShortJavaImplName(pickInnerJavaImplName(usedTypeNames, localname, javaname == null ? null : javaname + DefaultRepositoryConfiguration.DEFAULT_REPOSITORY_IMPLEMENTATION_POSTFIX));
                } else {
                    sImpl.setFullJavaName(outerType.getFullJavaName() + PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX + pickInnerJavaClassName(usedTypeNames, localname, javaname));
                    sImpl.setFullJavaImplName(outerType.getFullJavaImplName() + PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX + pickInnerJavaImplName(usedTypeNames, localname, javaname == null ? null : javaname + DefaultRepositoryConfiguration.DEFAULT_REPOSITORY_IMPLEMENTATION_POSTFIX));
                }
                setExtensions(sImpl, state);
            }
        }
    }

    static void assignJavaPropertyNames(Set usedNames, SchemaProperty[] props, SchemaType baseType, boolean doInherited) {
        String theName;
        StscState state = StscState.get();
        for (SchemaProperty schemaProperty : props) {
            SchemaPropertyImpl sImpl = (SchemaPropertyImpl) schemaProperty;
            SchemaProperty baseProp = sImpl.isAttribute() ? baseType.getAttributeProperty(sImpl.getName()) : baseType.getElementProperty(sImpl.getName());
            if ((baseProp != null) == doInherited) {
                QName propQName = sImpl.getName();
                if (baseProp == null) {
                    theName = pickJavaPropertyName(usedNames, propQName.getLocalPart(), state.getJavaname(propQName, sImpl.isAttribute() ? 4 : 3));
                } else {
                    theName = baseProp.getJavaPropertyName();
                }
                sImpl.setJavaPropertyName(theName);
                boolean isArray = sImpl.getMaxOccurs() == null || sImpl.getMaxOccurs().compareTo(BigInteger.ONE) > 0;
                boolean isSingleton = !isArray && sImpl.getMaxOccurs().signum() > 0;
                boolean isOption = isSingleton && sImpl.getMinOccurs().signum() == 0;
                SchemaType javaBasedOnType = sImpl.getType();
                if (baseProp != null) {
                    if (baseProp.extendsJavaArray()) {
                        isSingleton = false;
                        isOption = false;
                        isArray = true;
                    }
                    if (baseProp.extendsJavaSingleton()) {
                        isSingleton = true;
                    }
                    if (baseProp.extendsJavaOption()) {
                        isOption = true;
                    }
                    javaBasedOnType = baseProp.javaBasedOnType();
                }
                sImpl.setExtendsJava(javaBasedOnType.getRef(), isSingleton, isOption, isArray);
            }
        }
    }

    static void assignJavaTypeCodes(SchemaProperty[] properties) {
        for (SchemaProperty schemaProperty : properties) {
            SchemaPropertyImpl sImpl = (SchemaPropertyImpl) schemaProperty;
            SchemaType sType = sImpl.javaBasedOnType();
            sImpl.setJavaTypeCode(javaTypeCodeForType(sType));
        }
    }

    static int javaTypeCodeInCommon(SchemaType[] types) {
        if (types == null || types.length == 0) {
            return 0;
        }
        int code = javaTypeCodeForType(types[0]);
        if (code == 19) {
            return code;
        }
        for (int i = 1; i < types.length; i++) {
            if (code != javaTypeCodeForType(types[i])) {
                return 19;
            }
        }
        return code;
    }

    static int javaTypeCodeForType(SchemaType sType) {
        if (!sType.isSimpleType()) {
            return 0;
        }
        if (((SchemaTypeImpl) sType).getUserTypeHandlerName() != null) {
            return 20;
        }
        if (sType.getSimpleVariety() == 2) {
            SchemaType baseType = sType.getUnionCommonBaseType();
            if (baseType != null && !baseType.isURType()) {
                sType = baseType;
            } else {
                return javaTypeCodeInCommon(sType.getUnionConstituentTypes());
            }
        }
        if (sType.getSimpleVariety() == 3) {
            return 16;
        }
        if (sType.isURType()) {
            return 0;
        }
        switch (sType.getPrimitiveType().getBuiltinTypeCode()) {
            case 2:
                return 10;
            case 3:
                return 1;
            case 4:
                return 11;
            case 5:
                return 11;
            case 6:
                return 10;
            case 7:
                return 15;
            case 8:
                return 0;
            case 9:
                return 2;
            case 10:
                return 3;
            case 11:
                switch (sType.getDecimalSize()) {
                    case 8:
                        return 4;
                    case 16:
                        return 5;
                    case 32:
                        return 6;
                    case 64:
                        return 7;
                    case SchemaType.SIZE_BIG_INTEGER /* 1000000 */:
                        return 9;
                    case 1000001:
                    default:
                        return 8;
                }
            case 12:
                if (isStringType(sType.getBaseEnumType())) {
                    if (sType.getEnumerationValues() != null && sType.getEnumerationValues().length > MAX_ENUM_COUNT) {
                        return 10;
                    }
                    return 18;
                }
                return 10;
            case 13:
                return 13;
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
                return 17;
            default:
                if ($assertionsDisabled) {
                    throw new IllegalStateException("unrecognized code " + sType.getPrimitiveType().getBuiltinTypeCode() + " of " + sType.getPrimitiveType().getName());
                }
                throw new AssertionError("unrecognized code " + sType.getPrimitiveType().getBuiltinTypeCode());
        }
    }

    static boolean isPropertyModelOrderInsensitive(SchemaProperty[] properties) {
        for (SchemaProperty prop : properties) {
            if (prop.hasNillable() == 1 || prop.hasDefault() == 1 || prop.hasFixed() == 1) {
                return false;
            }
            if (prop.hasDefault() != 0 && prop.getDefaultText() == null) {
                return false;
            }
        }
        return true;
    }

    static boolean protectReservedGlobalClassNames(String name) {
        int i = name.lastIndexOf(46);
        String lastSegment = name.substring(i + 1);
        if (lastSegment.endsWith(StandardRoles.DOCUMENT) && !lastSegment.equals(StandardRoles.DOCUMENT)) {
            return true;
        }
        return false;
    }

    static boolean protectReservedInnerClassNames(String name) {
        return name.equals("Enum") || name.equals("Factory");
    }

    static boolean protectReservedPropertyNames(String name) {
        return PROTECTED_PROPERTIES_SET.contains(name) || (name.endsWith(SoapEncSchemaTypeSystem.SOAP_ARRAY) && !name.equals(SoapEncSchemaTypeSystem.SOAP_ARRAY));
    }

    static String pickFullJavaClassName(Set usedNames, QName qName, String configname, boolean isDocument, boolean isAttrType) {
        String base;
        boolean protect;
        String str;
        if (configname != null && configname.indexOf(46) >= 0) {
            base = configname;
            protect = protectReservedGlobalClassNames(base);
        } else {
            StscState state = StscState.get();
            String uri = qName.getNamespaceURI();
            base = NameUtil.getClassNameFromQName(qName);
            String pkgPrefix = state.getPackageOverride(uri);
            if (pkgPrefix != null) {
                base = pkgPrefix + "." + base.substring(base.lastIndexOf(46) + 1);
            }
            String javaPrefix = state.getJavaPrefix(uri);
            if (javaPrefix != null) {
                base = base.substring(0, base.lastIndexOf(46) + 1) + javaPrefix + base.substring(base.lastIndexOf(46) + 1);
            }
            if (configname != null) {
                base = base.substring(0, base.lastIndexOf(46) + 1) + configname;
            }
            protect = protectReservedGlobalClassNames(base);
            if (configname == null) {
                if (isDocument) {
                    base = base + StandardRoles.DOCUMENT;
                } else if (isAttrType) {
                    base = base + "Attribute";
                }
                String javaSuffix = state.getJavaSuffix(uri);
                if (javaSuffix != null) {
                    base = base + javaSuffix;
                }
            }
        }
        String outermostPkg = getOutermostPackage(base);
        int index = 1;
        if (protect) {
            str = base + 1;
        } else {
            str = base;
        }
        while (true) {
            String uniqName = str;
            if (usedNames.contains(uniqName.toLowerCase()) || uniqName.equals(outermostPkg)) {
                index++;
                str = base + index;
            } else {
                usedNames.add(uniqName.toLowerCase());
                return uniqName;
            }
        }
    }

    static String getOutermostPackage(String fqcn) {
        int lastdot;
        if (fqcn == null || (lastdot = fqcn.indexOf(46)) < 0) {
            return "";
        }
        return fqcn.substring(0, lastdot);
    }

    static String pickFullJavaImplName(Set usedNames, String intfName) {
        String className = intfName;
        String pkgName = null;
        int index = intfName.lastIndexOf(46);
        if (index >= 0) {
            className = intfName.substring(index + 1);
            pkgName = intfName.substring(0, index);
        }
        String base = pkgName + ".impl." + className + DefaultRepositoryConfiguration.DEFAULT_REPOSITORY_IMPLEMENTATION_POSTFIX;
        int index2 = 1;
        String str = base;
        while (true) {
            String uniqName = str;
            if (usedNames.contains(uniqName.toLowerCase())) {
                index2++;
                str = base + index2;
            } else {
                usedNames.add(uniqName.toLowerCase());
                return uniqName;
            }
        }
    }

    static String pickJavaPropertyName(Set usedNames, String localName, String javaName) {
        String str;
        if (javaName == null) {
            javaName = NameUtil.upperCamelCase(localName);
        }
        boolean protect = protectReservedPropertyNames(javaName);
        int index = 1;
        if (protect) {
            str = javaName + 1;
        } else {
            str = javaName;
        }
        while (true) {
            String uniqName = str;
            if (usedNames.contains(uniqName)) {
                index++;
                str = javaName + index;
            } else {
                usedNames.add(uniqName);
                return uniqName;
            }
        }
    }

    static String pickInnerJavaClassName(Set usedNames, String localName, String javaName) {
        String str;
        if (javaName == null) {
            javaName = NameUtil.upperCamelCase(localName);
        }
        boolean protect = protectReservedInnerClassNames(javaName);
        int index = 1;
        if (protect) {
            str = javaName + 1;
        } else {
            str = javaName;
        }
        while (true) {
            String uniqName = str;
            if (usedNames.contains(uniqName)) {
                index++;
                str = javaName + index;
            } else {
                usedNames.add(uniqName);
                return uniqName;
            }
        }
    }

    static String pickInnerJavaImplName(Set usedNames, String localName, String javaName) {
        if (javaName == null) {
            javaName = NameUtil.upperCamelCase(localName) + DefaultRepositoryConfiguration.DEFAULT_REPOSITORY_IMPLEMENTATION_POSTFIX;
        }
        String uniqName = javaName;
        int index = 1;
        while (usedNames.contains(uniqName)) {
            index++;
            uniqName = javaName + index;
        }
        usedNames.add(uniqName);
        return uniqName;
    }

    static QName findTopName(SchemaType sType) {
        if (sType.getName() != null) {
            return sType.getName();
        }
        if (sType.isDocumentType()) {
            if (sType.getContentModel() == null || sType.getContentModel().getParticleType() != 4) {
                throw new IllegalStateException();
            }
            return sType.getDocumentElementName();
        }
        if (sType.isAttributeType()) {
            if (sType.getAttributeModel() == null || sType.getAttributeModel().getAttributes().length != 1) {
                throw new IllegalStateException();
            }
            return sType.getAttributeTypeAttributeName();
        }
        SchemaField sElt = sType.getContainerField();
        if (!$assertionsDisabled && sElt == null) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || sType.getOuterType() == null) {
            return sElt.getName();
        }
        throw new AssertionError();
    }

    static void addAnonymousTypesFromRedefinition(SchemaType sType, List result) {
        while (((SchemaTypeImpl) sType).isRedefinition()) {
            if (sType.getDerivationType() == 2 || sType.isSimpleType()) {
                sType = sType.getBaseType();
                SchemaType[] newAnonTypes = sType.getAnonymousTypes();
                if (newAnonTypes.length > 0) {
                    result.addAll(Arrays.asList(newAnonTypes));
                }
            } else {
                return;
            }
        }
    }
}
