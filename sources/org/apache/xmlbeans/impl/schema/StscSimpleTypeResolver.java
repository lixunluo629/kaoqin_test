package org.apache.xmlbeans.impl.schema;

import ch.qos.logback.core.pattern.parser.Parser;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlByte;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlShort;
import org.apache.xmlbeans.XmlUnsignedByte;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.regex.RegularExpression;
import org.apache.xmlbeans.impl.values.XmlValueOutOfRangeException;
import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalSimpleType;
import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleType;
import org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/StscSimpleTypeResolver.class */
public class StscSimpleTypeResolver {
    private static final RegularExpression[] EMPTY_REGEX_ARRAY;
    private static CodeForNameEntry[] facetCodes;
    private static final Map facetCodeMap;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StscSimpleTypeResolver.class.desiredAssertionStatus();
        EMPTY_REGEX_ARRAY = new RegularExpression[0];
        facetCodes = new CodeForNameEntry[]{new CodeForNameEntry(QNameHelper.forLNS("length", "http://www.w3.org/2001/XMLSchema"), 0), new CodeForNameEntry(QNameHelper.forLNS("minLength", "http://www.w3.org/2001/XMLSchema"), 1), new CodeForNameEntry(QNameHelper.forLNS("maxLength", "http://www.w3.org/2001/XMLSchema"), 2), new CodeForNameEntry(QNameHelper.forLNS("pattern", "http://www.w3.org/2001/XMLSchema"), 10), new CodeForNameEntry(QNameHelper.forLNS("enumeration", "http://www.w3.org/2001/XMLSchema"), 11), new CodeForNameEntry(QNameHelper.forLNS("whiteSpace", "http://www.w3.org/2001/XMLSchema"), 9), new CodeForNameEntry(QNameHelper.forLNS("maxInclusive", "http://www.w3.org/2001/XMLSchema"), 5), new CodeForNameEntry(QNameHelper.forLNS("maxExclusive", "http://www.w3.org/2001/XMLSchema"), 6), new CodeForNameEntry(QNameHelper.forLNS("minInclusive", "http://www.w3.org/2001/XMLSchema"), 4), new CodeForNameEntry(QNameHelper.forLNS("minExclusive", "http://www.w3.org/2001/XMLSchema"), 3), new CodeForNameEntry(QNameHelper.forLNS("totalDigits", "http://www.w3.org/2001/XMLSchema"), 7), new CodeForNameEntry(QNameHelper.forLNS("fractionDigits", "http://www.w3.org/2001/XMLSchema"), 8)};
        facetCodeMap = buildFacetCodeMap();
    }

    public static void resolveSimpleType(SchemaTypeImpl sImpl) {
        SimpleType parseSt = (SimpleType) sImpl.getParseObject();
        if (!$assertionsDisabled && !sImpl.isSimpleType()) {
            throw new AssertionError();
        }
        SchemaDocument.Schema schema = StscComplexTypeResolver.getSchema(parseSt);
        int count = (parseSt.isSetList() ? 1 : 0) + (parseSt.isSetUnion() ? 1 : 0) + (parseSt.isSetRestriction() ? 1 : 0);
        if (count > 1) {
            StscState.get().error("A simple type must define either a list, a union, or a restriction: more than one found.", 52, parseSt);
        } else if (count < 1) {
            StscState.get().error("A simple type must define either a list, a union, or a restriction: none was found.", 52, parseSt);
            resolveErrorSimpleType(sImpl);
            return;
        }
        boolean finalRest = false;
        boolean finalList = false;
        boolean finalUnion = false;
        Object finalValue = null;
        if (parseSt.isSetFinal()) {
            finalValue = parseSt.getFinal();
        } else if (schema != null && schema.isSetFinalDefault()) {
            finalValue = schema.getFinalDefault();
        }
        if (finalValue != null) {
            if (finalValue instanceof String) {
                if ("#all".equals((String) finalValue)) {
                    finalUnion = true;
                    finalList = true;
                    finalRest = true;
                }
            } else if (finalValue instanceof List) {
                List lFinalValue = (List) finalValue;
                if (lFinalValue.contains("restriction")) {
                    finalRest = true;
                }
                if (lFinalValue.contains("list")) {
                    finalList = true;
                }
                if (lFinalValue.contains(XmlErrorCodes.UNION)) {
                    finalUnion = true;
                }
            }
        }
        sImpl.setSimpleFinal(finalRest, finalList, finalUnion);
        List anonTypes = new ArrayList();
        if (parseSt.getList() != null) {
            resolveListType(sImpl, parseSt.getList(), anonTypes);
        } else if (parseSt.getUnion() != null) {
            resolveUnionType(sImpl, parseSt.getUnion(), anonTypes);
        } else if (parseSt.getRestriction() != null) {
            resolveSimpleRestrictionType(sImpl, parseSt.getRestriction(), anonTypes);
        }
        sImpl.setAnonymousTypeRefs(makeRefArray(anonTypes));
    }

    private static SchemaType.Ref[] makeRefArray(Collection typeList) {
        SchemaType.Ref[] result = new SchemaType.Ref[typeList.size()];
        int j = 0;
        Iterator i = typeList.iterator();
        while (i.hasNext()) {
            result[j] = ((SchemaType) i.next()).getRef();
            j++;
        }
        return result;
    }

    static void resolveErrorSimpleType(SchemaTypeImpl sImpl) {
        sImpl.setSimpleTypeVariety(1);
        sImpl.setBaseTypeRef(BuiltinSchemaTypeSystem.ST_ANY_SIMPLE.getRef());
        sImpl.setBaseDepth(BuiltinSchemaTypeSystem.ST_ANY_SIMPLE.getBaseDepth() + 1);
        sImpl.setPrimitiveTypeRef(BuiltinSchemaTypeSystem.ST_ANY_SIMPLE.getRef());
    }

    static void resolveListType(SchemaTypeImpl sImpl, ListDocument.List parseList, List anonTypes) {
        SchemaTypeImpl itemImpl;
        XmlObject errorLoc;
        StscState state = StscState.get();
        sImpl.setSimpleTypeVariety(3);
        sImpl.setBaseTypeRef(BuiltinSchemaTypeSystem.ST_ANY_SIMPLE.getRef());
        sImpl.setBaseDepth(BuiltinSchemaTypeSystem.ST_ANY_SIMPLE.getBaseDepth() + 1);
        sImpl.setDerivationType(1);
        if (sImpl.isRedefinition()) {
            state.error(XmlErrorCodes.SCHEMA_REDEFINE$EXTEND_OR_RESTRICT, new Object[]{"list"}, parseList);
        }
        QName itemName = parseList.getItemType();
        LocalSimpleType parseInner = parseList.getSimpleType();
        if (itemName != null && parseInner != null) {
            state.error(XmlErrorCodes.SCHEMA_SIMPLE_TYPE$LIST_HAS_BOTH_ITEM_OR_SIMPLE_TYPE, (Object[]) null, parseList);
            parseInner = null;
        }
        if (itemName != null) {
            itemImpl = state.findGlobalType(itemName, sImpl.getChameleonNamespace(), sImpl.getTargetNamespace());
            errorLoc = parseList.xgetItemType();
            if (itemImpl == null) {
                state.notFoundError(itemName, 0, parseList.xgetItemType(), true);
                itemImpl = BuiltinSchemaTypeSystem.ST_ANY_SIMPLE;
            }
        } else if (parseInner != null) {
            itemImpl = StscTranslator.translateAnonymousSimpleType(parseInner, sImpl.getTargetNamespace(), sImpl.getChameleonNamespace() != null, sImpl.getElemFormDefault(), sImpl.getAttFormDefault(), anonTypes, sImpl);
            errorLoc = parseInner;
        } else {
            state.error(XmlErrorCodes.SCHEMA_SIMPLE_TYPE$LIST_HAS_NEITHER_ITEM_OR_SIMPLE_TYPE, (Object[]) null, parseList);
            resolveErrorSimpleType(sImpl);
            return;
        }
        if (itemImpl.finalList()) {
            state.error(XmlErrorCodes.SIMPLE_TYPE_PROPERTIES$LIST_FINAL, (Object[]) null, parseList);
        }
        StscResolver.resolveType(itemImpl);
        if (!itemImpl.isSimpleType()) {
            state.error(XmlErrorCodes.SIMPLE_TYPE_RESTRICTION$LIST_ITEM_NOT_SIMPLE, (Object[]) null, errorLoc);
            sImpl = BuiltinSchemaTypeSystem.ST_ANY_SIMPLE;
        }
        switch (itemImpl.getSimpleVariety()) {
            case 2:
                if (itemImpl.isUnionOfLists()) {
                    state.error(XmlErrorCodes.SIMPLE_TYPE_RESTRICTION$LIST_ITEM_IS_UNION_OF_LIST, (Object[]) null, errorLoc);
                    resolveErrorSimpleType(sImpl);
                    return;
                }
            case 1:
                sImpl.setListItemTypeRef(itemImpl.getRef());
                if (sImpl.getBuiltinTypeCode() == 8) {
                    state.recover(XmlErrorCodes.DATATYPE_ENUM_NOTATION, null, errorLoc);
                }
                sImpl.setBasicFacets(StscState.FACETS_LIST, StscState.FIXED_FACETS_LIST);
                sImpl.setWhiteSpaceRule(3);
                resolveFundamentalFacets(sImpl);
                return;
            case 3:
                state.error(XmlErrorCodes.SIMPLE_TYPE_RESTRICTION$LIST_ITEM_IS_LIST, (Object[]) null, errorLoc);
                resolveErrorSimpleType(sImpl);
                return;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                sImpl.setListItemTypeRef(BuiltinSchemaTypeSystem.ST_ANY_SIMPLE.getRef());
                sImpl.setBasicFacets(StscState.FACETS_LIST, StscState.FIXED_FACETS_LIST);
                sImpl.setWhiteSpaceRule(3);
                resolveFundamentalFacets(sImpl);
                return;
        }
    }

    static void resolveUnionType(SchemaTypeImpl sImpl, UnionDocument.Union parseUnion, List anonTypes) {
        XmlObject xmlObjectXgetMemberTypes;
        XmlObject xmlObjectXgetMemberTypes2;
        sImpl.setSimpleTypeVariety(2);
        sImpl.setBaseTypeRef(BuiltinSchemaTypeSystem.ST_ANY_SIMPLE.getRef());
        sImpl.setBaseDepth(BuiltinSchemaTypeSystem.ST_ANY_SIMPLE.getBaseDepth() + 1);
        sImpl.setDerivationType(1);
        StscState state = StscState.get();
        if (sImpl.isRedefinition()) {
            state.error(XmlErrorCodes.SCHEMA_REDEFINE$EXTEND_OR_RESTRICT, new Object[]{XmlErrorCodes.UNION}, parseUnion);
        }
        List<QName> memberTypes = parseUnion.getMemberTypes();
        SimpleType[] simpleTypes = parseUnion.getSimpleTypeArray();
        List memberImplList = new ArrayList();
        if (simpleTypes.length == 0 && (memberTypes == null || memberTypes.size() == 0)) {
            state.error(XmlErrorCodes.SCHEMA_SIMPLE_TYPE$UNION_HAS_MEMBER_TYPES_OR_SIMPLE_TYPES, (Object[]) null, parseUnion);
        }
        if (memberTypes != null) {
            for (QName mName : memberTypes) {
                SchemaTypeImpl memberImpl = state.findGlobalType(mName, sImpl.getChameleonNamespace(), sImpl.getTargetNamespace());
                if (memberImpl == null) {
                    state.notFoundError(mName, 0, parseUnion.xgetMemberTypes(), true);
                } else {
                    memberImplList.add(memberImpl);
                }
            }
        }
        for (int i = 0; i < simpleTypes.length; i++) {
            SchemaTypeImpl mImpl = StscTranslator.translateAnonymousSimpleType(simpleTypes[i], sImpl.getTargetNamespace(), sImpl.getChameleonNamespace() != null, sImpl.getElemFormDefault(), sImpl.getAttFormDefault(), anonTypes, sImpl);
            memberImplList.add(mImpl);
            mImpl.setAnonymousUnionMemberOrdinal(i + 1);
        }
        Iterator mImpls = memberImplList.iterator();
        while (mImpls.hasNext()) {
            SchemaTypeImpl mImpl2 = (SchemaTypeImpl) mImpls.next();
            if (!StscResolver.resolveType(mImpl2)) {
                String memberName = "";
                if (mImpl2.getOuterType().equals(sImpl)) {
                    xmlObjectXgetMemberTypes2 = mImpl2.getParseObject();
                } else {
                    memberName = QNameHelper.pretty(mImpl2.getName()) + SymbolConstants.SPACE_SYMBOL;
                    xmlObjectXgetMemberTypes2 = parseUnion.xgetMemberTypes();
                }
                XmlObject errorLoc = xmlObjectXgetMemberTypes2;
                state.error(XmlErrorCodes.SCHEMA_SIMPLE_TYPE$CYCLIC_UNION, new Object[]{memberName}, errorLoc);
                mImpls.remove();
            }
        }
        boolean isUnionOfLists = false;
        Iterator mImpls2 = memberImplList.iterator();
        while (mImpls2.hasNext()) {
            SchemaTypeImpl mImpl3 = (SchemaTypeImpl) mImpls2.next();
            if (!mImpl3.isSimpleType()) {
                String memberName2 = "";
                if (mImpl3.getOuterType() != null && mImpl3.getOuterType().equals(sImpl)) {
                    xmlObjectXgetMemberTypes = mImpl3.getParseObject();
                } else {
                    memberName2 = QNameHelper.pretty(mImpl3.getName()) + SymbolConstants.SPACE_SYMBOL;
                    xmlObjectXgetMemberTypes = parseUnion.xgetMemberTypes();
                }
                XmlObject errorLoc2 = xmlObjectXgetMemberTypes;
                state.error(XmlErrorCodes.SIMPLE_TYPE_RESTRICTION$UNION_MEMBER_NOT_SIMPLE, new Object[]{memberName2}, errorLoc2);
                mImpls2.remove();
            } else if (mImpl3.getSimpleVariety() == 3 || (mImpl3.getSimpleVariety() == 2 && mImpl3.isUnionOfLists())) {
                isUnionOfLists = true;
            }
        }
        for (int i2 = 0; i2 < memberImplList.size(); i2++) {
            if (((SchemaTypeImpl) memberImplList.get(i2)).finalUnion()) {
                state.error(XmlErrorCodes.SIMPLE_TYPE_PROPERTIES$UNION_FINAL, (Object[]) null, parseUnion);
            }
        }
        sImpl.setUnionOfLists(isUnionOfLists);
        sImpl.setUnionMemberTypeRefs(makeRefArray(memberImplList));
        sImpl.setBasicFacets(StscState.FACETS_UNION, StscState.FIXED_FACETS_UNION);
        resolveFundamentalFacets(sImpl);
    }

    static void resolveSimpleRestrictionType(SchemaTypeImpl sImpl, RestrictionDocument.Restriction parseRestr, List anonTypes) {
        SchemaTypeImpl baseImpl;
        QName baseName = parseRestr.getBase();
        SimpleType parseInner = parseRestr.getSimpleType();
        StscState state = StscState.get();
        if (baseName != null && parseInner != null) {
            state.error(XmlErrorCodes.SCHEMA_SIMPLE_TYPE$RESTRICTION_HAS_BOTH_BASE_OR_SIMPLE_TYPE, (Object[]) null, parseRestr);
            parseInner = null;
        }
        if (baseName != null) {
            if (sImpl.isRedefinition()) {
                baseImpl = state.findRedefinedGlobalType(parseRestr.getBase(), sImpl.getChameleonNamespace(), sImpl);
                if (baseImpl != null && !baseImpl.getName().equals(sImpl.getName())) {
                    state.error(XmlErrorCodes.SCHEMA_REDEFINE$SAME_TYPE, new Object[]{"<simpleType>", QNameHelper.pretty(baseName), QNameHelper.pretty(sImpl.getName())}, parseRestr);
                }
            } else {
                baseImpl = state.findGlobalType(baseName, sImpl.getChameleonNamespace(), sImpl.getTargetNamespace());
            }
            if (baseImpl == null) {
                state.notFoundError(baseName, 0, parseRestr.xgetBase(), true);
                baseImpl = BuiltinSchemaTypeSystem.ST_ANY_SIMPLE;
            }
        } else if (parseInner != null) {
            if (sImpl.isRedefinition()) {
                StscState.get().error(XmlErrorCodes.SCHEMA_REDEFINE$EXTEND_OR_RESTRICT, new Object[]{"<simpleType>"}, parseInner);
            }
            baseImpl = StscTranslator.translateAnonymousSimpleType(parseInner, sImpl.getTargetNamespace(), sImpl.getChameleonNamespace() != null, sImpl.getElemFormDefault(), sImpl.getAttFormDefault(), anonTypes, sImpl);
        } else {
            state.error(XmlErrorCodes.SCHEMA_SIMPLE_TYPE$RESTRICTION_HAS_NEITHER_BASE_OR_SIMPLE_TYPE, (Object[]) null, parseRestr);
            baseImpl = BuiltinSchemaTypeSystem.ST_ANY_SIMPLE;
        }
        if (!StscResolver.resolveType(baseImpl)) {
            baseImpl = BuiltinSchemaTypeSystem.ST_ANY_SIMPLE;
        }
        if (baseImpl.finalRestriction()) {
            state.error(XmlErrorCodes.SIMPLE_TYPE_PROPERTIES$RESTRICTION_FINAL, (Object[]) null, parseRestr);
        }
        sImpl.setBaseTypeRef(baseImpl.getRef());
        sImpl.setBaseDepth(baseImpl.getBaseDepth() + 1);
        sImpl.setDerivationType(1);
        if (!baseImpl.isSimpleType()) {
            state.error(XmlErrorCodes.SIMPLE_TYPE_RESTRICTION$ATOMIC_NOT_SIMPLE, (Object[]) null, parseRestr.xgetBase());
            resolveErrorSimpleType(sImpl);
            return;
        }
        sImpl.setSimpleTypeVariety(baseImpl.getSimpleVariety());
        switch (baseImpl.getSimpleVariety()) {
            case 1:
                sImpl.setPrimitiveTypeRef(baseImpl.getPrimitiveType().getRef());
                break;
            case 2:
                sImpl.setUnionOfLists(baseImpl.isUnionOfLists());
                sImpl.setUnionMemberTypeRefs(makeRefArray(Arrays.asList(baseImpl.getUnionMemberTypes())));
                break;
            case 3:
                sImpl.setListItemTypeRef(baseImpl.getListItemType().getRef());
                break;
        }
        resolveFacets(sImpl, parseRestr, baseImpl);
        resolveFundamentalFacets(sImpl);
    }

    static int translateWhitespaceCode(XmlAnySimpleType value) {
        String textval = value.getStringValue();
        if (textval.equals("collapse")) {
            return 3;
        }
        if (textval.equals("preserve")) {
            return 1;
        }
        if (textval.equals(Parser.REPLACE_CONVERTER_WORD)) {
            return 2;
        }
        StscState.get().error("Unrecognized whitespace value \"" + textval + SymbolConstants.QUOTES_SYMBOL, 20, value);
        return 0;
    }

    static boolean isMultipleFacet(int facetcode) {
        return facetcode == 11 || facetcode == 10;
    }

    static boolean facetAppliesToType(int facetCode, SchemaTypeImpl baseImpl) {
        switch (baseImpl.getSimpleVariety()) {
            case 2:
                switch (facetCode) {
                    case 10:
                    case 11:
                        return true;
                    default:
                        return false;
                }
            case 3:
                switch (facetCode) {
                    case 0:
                    case 1:
                    case 2:
                    case 9:
                    case 10:
                    case 11:
                        return true;
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    default:
                        return false;
                }
            default:
                switch (baseImpl.getPrimitiveType().getBuiltinTypeCode()) {
                    case 2:
                        return false;
                    case 3:
                        switch (facetCode) {
                            case 9:
                            case 10:
                                return true;
                            default:
                                return false;
                        }
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 12:
                        switch (facetCode) {
                            case 0:
                            case 1:
                            case 2:
                            case 9:
                            case 10:
                            case 11:
                                return true;
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            default:
                                return false;
                        }
                    case 9:
                    case 10:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                        switch (facetCode) {
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 9:
                            case 10:
                            case 11:
                                return true;
                            case 7:
                            case 8:
                            default:
                                return false;
                        }
                    case 11:
                        switch (facetCode) {
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                            case 10:
                            case 11:
                                return true;
                            default:
                                return false;
                        }
                    default:
                        if ($assertionsDisabled) {
                            return false;
                        }
                        throw new AssertionError();
                }
        }
    }

    private static int other_similar_limit(int facetcode) {
        switch (facetcode) {
            case 3:
                return 4;
            case 4:
                return 3;
            case 5:
                return 6;
            case 6:
                return 5;
            default:
                if ($assertionsDisabled) {
                    throw new IllegalStateException();
                }
                throw new AssertionError();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x0538, code lost:
    
        if (r0 == r1) goto L170;
     */
    /* JADX WARN: Incorrect condition in loop: B:6:0x0036 */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:202:0x0665  */
    /* JADX WARN: Removed duplicated region for block: B:264:0x066b A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static void resolveFacets(org.apache.xmlbeans.impl.schema.SchemaTypeImpl r7, org.apache.xmlbeans.XmlObject r8, org.apache.xmlbeans.impl.schema.SchemaTypeImpl r9) {
        /*
            Method dump skipped, instructions count: 1885
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.schema.StscSimpleTypeResolver.resolveFacets(org.apache.xmlbeans.impl.schema.SchemaTypeImpl, org.apache.xmlbeans.XmlObject, org.apache.xmlbeans.impl.schema.SchemaTypeImpl):void");
    }

    private static XmlValueRef[] makeValueRefArray(XmlAnySimpleType[] source) {
        XmlValueRef[] result = new XmlValueRef[source.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = source[i] == null ? null : new XmlValueRef(source[i]);
        }
        return result;
    }

    private static boolean isDiscreteType(SchemaTypeImpl sImpl) {
        if (sImpl.getFacet(8) != null) {
            return true;
        }
        switch (sImpl.getPrimitiveType().getBuiltinTypeCode()) {
        }
        return true;
    }

    private static boolean isNumericPrimitive(SchemaType sImpl) {
        switch (sImpl.getBuiltinTypeCode()) {
            case 9:
            case 10:
            case 11:
                return true;
            default:
                return false;
        }
    }

    private static int decimalSizeOfType(SchemaTypeImpl sImpl) {
        int size = mathematicalSizeOfType(sImpl);
        if (size == 8 && !XmlByte.type.isAssignableFrom(sImpl)) {
            size = 16;
        }
        if (size == 16 && !XmlShort.type.isAssignableFrom(sImpl) && !XmlUnsignedByte.type.isAssignableFrom(sImpl)) {
            size = 32;
        }
        return size;
    }

    private static int mathematicalSizeOfType(SchemaTypeImpl sImpl) {
        if (sImpl.getPrimitiveType().getBuiltinTypeCode() != 11) {
            return 0;
        }
        if (sImpl.getFacet(8) == null || ((SimpleValue) sImpl.getFacet(8)).getBigIntegerValue().signum() != 0) {
            return 1000001;
        }
        BigInteger min = null;
        BigInteger max = null;
        if (sImpl.getFacet(3) != null) {
            min = ((SimpleValue) sImpl.getFacet(3)).getBigIntegerValue();
        }
        if (sImpl.getFacet(4) != null) {
            min = ((SimpleValue) sImpl.getFacet(4)).getBigIntegerValue();
        }
        if (sImpl.getFacet(5) != null) {
            max = ((SimpleValue) sImpl.getFacet(5)).getBigIntegerValue();
        }
        if (sImpl.getFacet(6) != null) {
            max = ((SimpleValue) sImpl.getFacet(6)).getBigIntegerValue();
        }
        if (sImpl.getFacet(7) != null) {
            BigInteger peg = null;
            try {
                BigInteger totalDigits = ((SimpleValue) sImpl.getFacet(7)).getBigIntegerValue();
                switch (totalDigits.intValue()) {
                    case 0:
                    case 1:
                    case 2:
                        peg = BigInteger.valueOf(99L);
                        break;
                    case 3:
                    case 4:
                        peg = BigInteger.valueOf(9999L);
                        break;
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        peg = BigInteger.valueOf(999999999L);
                        break;
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                        peg = BigInteger.valueOf(999999999999999999L);
                        break;
                }
            } catch (XmlValueOutOfRangeException e) {
            }
            if (peg != null) {
                min = min == null ? peg.negate() : min.max(peg.negate());
                max = max == null ? peg : max.min(peg);
            }
        }
        if (min != null && max != null) {
            if (min.signum() < 0) {
                min = min.negate().subtract(BigInteger.ONE);
            }
            if (max.signum() < 0) {
                max = max.negate().subtract(BigInteger.ONE);
            }
            BigInteger max2 = max.max(min);
            if (max2.compareTo(BigInteger.valueOf(127L)) <= 0) {
                return 8;
            }
            if (max2.compareTo(BigInteger.valueOf(32767L)) <= 0) {
                return 16;
            }
            if (max2.compareTo(BigInteger.valueOf(2147483647L)) <= 0) {
                return 32;
            }
            if (max2.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) <= 0) {
                return 64;
            }
            return SchemaType.SIZE_BIG_INTEGER;
        }
        return SchemaType.SIZE_BIG_INTEGER;
    }

    static void resolveFundamentalFacets(SchemaTypeImpl sImpl) {
        switch (sImpl.getSimpleVariety()) {
            case 1:
                SchemaTypeImpl baseImpl = (SchemaTypeImpl) sImpl.getBaseType();
                sImpl.setOrdered(baseImpl.ordered());
                sImpl.setBounded(((sImpl.getFacet(3) == null && sImpl.getFacet(4) == null) || (sImpl.getFacet(5) == null && sImpl.getFacet(6) == null)) ? false : true);
                sImpl.setFinite(baseImpl.isFinite() || (sImpl.isBounded() && isDiscreteType(sImpl)));
                sImpl.setNumeric(baseImpl.isNumeric() || isNumericPrimitive(sImpl.getPrimitiveType()));
                sImpl.setDecimalSize(decimalSizeOfType(sImpl));
                break;
            case 2:
                SchemaType[] mTypes = sImpl.getUnionMemberTypes();
                int ordered = 0;
                boolean isBounded = true;
                boolean isFinite = true;
                boolean isNumeric = true;
                for (int i = 0; i < mTypes.length; i++) {
                    if (mTypes[i].ordered() != 0) {
                        ordered = 1;
                    }
                    if (!mTypes[i].isBounded()) {
                        isBounded = false;
                    }
                    if (!mTypes[i].isFinite()) {
                        isFinite = false;
                    }
                    if (!mTypes[i].isNumeric()) {
                        isNumeric = false;
                    }
                }
                sImpl.setOrdered(ordered);
                sImpl.setBounded(isBounded);
                sImpl.setFinite(isFinite);
                sImpl.setNumeric(isNumeric);
                sImpl.setDecimalSize(0);
                break;
            case 3:
                sImpl.setOrdered(0);
                sImpl.setBounded((sImpl.getFacet(0) == null && sImpl.getFacet(2) == null) ? false : true);
                sImpl.setFinite(sImpl.getListItemType().isFinite() && sImpl.isBounded());
                sImpl.setNumeric(false);
                sImpl.setDecimalSize(0);
                break;
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/StscSimpleTypeResolver$CodeForNameEntry.class */
    private static class CodeForNameEntry {
        public QName name;
        public int code;

        CodeForNameEntry(QName name, int code) {
            this.name = name;
            this.code = code;
        }
    }

    private static Map buildFacetCodeMap() {
        Map result = new HashMap();
        for (int i = 0; i < facetCodes.length; i++) {
            result.put(facetCodes[i].name, new Integer(facetCodes[i].code));
        }
        return result;
    }

    private static int translateFacetCode(QName name) {
        Integer result = (Integer) facetCodeMap.get(name);
        if (result == null) {
            return -1;
        }
        return result.intValue();
    }
}
