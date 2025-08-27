package org.apache.xmlbeans.impl.schema;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.QNameSetBuilder;
import org.apache.xmlbeans.QNameSetSpecification;
import org.apache.xmlbeans.SchemaAttributeModel;
import org.apache.xmlbeans.SchemaField;
import org.apache.xmlbeans.SchemaLocalAttribute;
import org.apache.xmlbeans.SchemaLocalElement;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlNonNegativeInteger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.xb.xsdschema.AllNNI;
import org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.ComplexRestrictionType;
import org.apache.xmlbeans.impl.xb.xsdschema.ComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType;
import org.apache.xmlbeans.impl.xb.xsdschema.Group;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalSimpleType;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleContentDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleExtensionType;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleRestrictionType;
import org.apache.xmlbeans.impl.xb.xsdschema.Wildcard;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/StscComplexTypeResolver.class */
public class StscComplexTypeResolver {
    private static final int MODEL_GROUP_CODE = 100;
    private static CodeForNameEntry[] particleCodes;
    private static Map particleCodeMap;
    private static final int ATTRIBUTE_CODE = 100;
    private static final int ATTRIBUTE_GROUP_CODE = 101;
    private static final int ANY_ATTRIBUTE_CODE = 102;
    private static CodeForNameEntry[] attributeCodes;
    private static Map attributeCodeMap;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StscComplexTypeResolver.class.desiredAssertionStatus();
        particleCodes = new CodeForNameEntry[]{new CodeForNameEntry(QNameHelper.forLNS("all", "http://www.w3.org/2001/XMLSchema"), 1), new CodeForNameEntry(QNameHelper.forLNS("sequence", "http://www.w3.org/2001/XMLSchema"), 3), new CodeForNameEntry(QNameHelper.forLNS("choice", "http://www.w3.org/2001/XMLSchema"), 2), new CodeForNameEntry(QNameHelper.forLNS("element", "http://www.w3.org/2001/XMLSchema"), 4), new CodeForNameEntry(QNameHelper.forLNS(Languages.ANY, "http://www.w3.org/2001/XMLSchema"), 5), new CodeForNameEntry(QNameHelper.forLNS("group", "http://www.w3.org/2001/XMLSchema"), 100)};
        particleCodeMap = buildParticleCodeMap();
        attributeCodes = new CodeForNameEntry[]{new CodeForNameEntry(QNameHelper.forLNS(BeanDefinitionParserDelegate.QUALIFIER_ATTRIBUTE_ELEMENT, "http://www.w3.org/2001/XMLSchema"), 100), new CodeForNameEntry(QNameHelper.forLNS("attributeGroup", "http://www.w3.org/2001/XMLSchema"), 101), new CodeForNameEntry(QNameHelper.forLNS("anyAttribute", "http://www.w3.org/2001/XMLSchema"), 102)};
        attributeCodeMap = buildAttributeCodeMap();
    }

    public static Group getContentModel(ComplexType parseCt) {
        if (parseCt.getAll() != null) {
            return parseCt.getAll();
        }
        if (parseCt.getSequence() != null) {
            return parseCt.getSequence();
        }
        if (parseCt.getChoice() != null) {
            return parseCt.getChoice();
        }
        if (parseCt.getGroup() != null) {
            return parseCt.getGroup();
        }
        return null;
    }

    public static Group getContentModel(ComplexRestrictionType parseRest) {
        if (parseRest.getAll() != null) {
            return parseRest.getAll();
        }
        if (parseRest.getSequence() != null) {
            return parseRest.getSequence();
        }
        if (parseRest.getChoice() != null) {
            return parseRest.getChoice();
        }
        if (parseRest.getGroup() != null) {
            return parseRest.getGroup();
        }
        return null;
    }

    public static Group getContentModel(ExtensionType parseExt) {
        if (parseExt.getAll() != null) {
            return parseExt.getAll();
        }
        if (parseExt.getSequence() != null) {
            return parseExt.getSequence();
        }
        if (parseExt.getChoice() != null) {
            return parseExt.getChoice();
        }
        if (parseExt.getGroup() != null) {
            return parseExt.getGroup();
        }
        return null;
    }

    static SchemaDocument.Schema getSchema(XmlObject o) {
        XmlCursor c = o.newCursor();
        while (c.toParent()) {
            try {
                XmlObject o2 = c.getObject();
                if (o2.schemaType().equals(SchemaDocument.Schema.type)) {
                    SchemaDocument.Schema schema = (SchemaDocument.Schema) o2;
                    c.dispose();
                    return schema;
                }
            } finally {
                c.dispose();
            }
        }
        return null;
    }

    public static void resolveComplexType(SchemaTypeImpl sImpl) {
        ComplexType parseCt = (ComplexType) sImpl.getParseObject();
        StscState state = StscState.get();
        SchemaDocument.Schema schema = getSchema(parseCt);
        boolean abs = parseCt.isSetAbstract() ? parseCt.getAbstract() : false;
        boolean finalExt = false;
        boolean finalRest = false;
        boolean finalList = false;
        boolean finalUnion = false;
        Object ds = null;
        if (parseCt.isSetFinal()) {
            ds = parseCt.getFinal();
        } else if (schema != null && schema.isSetFinalDefault()) {
            ds = schema.getFinalDefault();
        }
        if (ds != null) {
            if ((ds instanceof String) && ds.equals("#all")) {
                finalUnion = true;
                finalList = true;
                finalRest = true;
                finalExt = true;
            } else if (ds instanceof List) {
                if (((List) ds).contains("extension")) {
                    finalExt = true;
                }
                if (((List) ds).contains("restriction")) {
                    finalRest = true;
                }
            }
        }
        sImpl.setAbstractFinal(abs, finalExt, finalRest, finalList, finalUnion);
        boolean blockExt = false;
        boolean blockRest = false;
        Object block = null;
        if (parseCt.isSetBlock()) {
            block = parseCt.getBlock();
        } else if (schema != null && schema.isSetBlockDefault()) {
            block = schema.getBlockDefault();
        }
        if (block != null) {
            if ((block instanceof String) && block.equals("#all")) {
                blockRest = true;
                blockExt = true;
            } else if (block instanceof List) {
                if (((List) block).contains("extension")) {
                    blockExt = true;
                }
                if (((List) block).contains("restriction")) {
                    blockRest = true;
                }
            }
        }
        sImpl.setBlock(blockExt, blockRest);
        ComplexContentDocument.ComplexContent parseCc = parseCt.getComplexContent();
        SimpleContentDocument.SimpleContent parseSc = parseCt.getSimpleContent();
        Group parseGroup = getContentModel(parseCt);
        int count = (parseCc != null ? 1 : 0) + (parseSc != null ? 1 : 0) + (parseGroup != null ? 1 : 0);
        if (count > 1) {
            state.error("A complex type must define either a content model, or a simpleContent or complexContent derivation: more than one found.", 26, parseCt);
            if (parseCc != null && parseSc != null) {
                parseSc = null;
            }
        }
        if (parseCc != null) {
            if (parseCc.getExtension() != null && parseCc.getRestriction() != null) {
                state.error("Restriction conflicts with extension", 26, parseCc.getRestriction());
            }
            boolean mixed = parseCc.isSetMixed() ? parseCc.getMixed() : parseCt.getMixed();
            if (parseCc.getExtension() != null) {
                resolveCcExtension(sImpl, parseCc.getExtension(), mixed);
                return;
            } else if (parseCc.getRestriction() != null) {
                resolveCcRestriction(sImpl, parseCc.getRestriction(), mixed);
                return;
            } else {
                state.error("Missing restriction or extension", 27, parseCc);
                resolveErrorType(sImpl);
                return;
            }
        }
        if (parseSc != null) {
            if (parseSc.getExtension() != null && parseSc.getRestriction() != null) {
                state.error("Restriction conflicts with extension", 26, parseSc.getRestriction());
            }
            if (parseSc.getExtension() != null) {
                resolveScExtension(sImpl, parseSc.getExtension());
                return;
            } else if (parseSc.getRestriction() != null) {
                resolveScRestriction(sImpl, parseSc.getRestriction());
                return;
            } else {
                state.error("Missing restriction or extension", 27, parseSc);
                resolveErrorType(sImpl);
                return;
            }
        }
        resolveBasicComplexType(sImpl);
    }

    static void resolveErrorType(SchemaTypeImpl sImpl) {
        throw new RuntimeException("This type of error recovery not yet implemented.");
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

    static void resolveBasicComplexType(SchemaTypeImpl sImpl) {
        List anonymousTypes = new ArrayList();
        ComplexType parseTree = (ComplexType) sImpl.getParseObject();
        String targetNamespace = sImpl.getTargetNamespace();
        boolean chameleon = sImpl.getChameleonNamespace() != null;
        Group parseGroup = getContentModel(parseTree);
        if (sImpl.isRedefinition()) {
            StscState.get().error(XmlErrorCodes.SCHEMA_REDEFINE$EXTEND_OR_RESTRICT, new Object[]{"<complexType>"}, parseTree);
        }
        int particleCode = translateParticleCode(parseGroup);
        Map elementModel = new LinkedHashMap();
        SchemaParticle contentModel = translateContentModel(sImpl, parseGroup, targetNamespace, chameleon, sImpl.getElemFormDefault(), sImpl.getAttFormDefault(), particleCode, anonymousTypes, elementModel, false, null);
        boolean isAll = contentModel != null && contentModel.getParticleType() == 1;
        SchemaAttributeModelImpl attrModel = new SchemaAttributeModelImpl();
        translateAttributeModel(parseTree, targetNamespace, chameleon, sImpl.getAttFormDefault(), anonymousTypes, sImpl, null, attrModel, null, true, null);
        WildcardResult wcElt = summarizeEltWildcards(contentModel);
        WildcardResult wcAttr = summarizeAttrWildcards(attrModel);
        if (contentModel != null) {
            buildStateMachine(contentModel);
            if (!StscState.get().noUpa() && !((SchemaParticleImpl) contentModel).isDeterministic()) {
                StscState.get().error(XmlErrorCodes.UNIQUE_PARTICLE_ATTRIBUTION, (Object[]) null, parseGroup);
            }
        }
        Map elementPropertyModel = buildContentPropertyModelByQName(contentModel, sImpl);
        Map attributePropertyModel = buildAttributePropertyModelByQName(attrModel, sImpl);
        int complexVariety = parseTree.getMixed() ? 4 : contentModel == null ? 1 : 3;
        sImpl.setBaseTypeRef(BuiltinSchemaTypeSystem.ST_ANY_TYPE.getRef());
        sImpl.setBaseDepth(BuiltinSchemaTypeSystem.ST_ANY_TYPE.getBaseDepth() + 1);
        sImpl.setDerivationType(2);
        sImpl.setComplexTypeVariety(complexVariety);
        sImpl.setContentModel(contentModel, attrModel, elementPropertyModel, attributePropertyModel, isAll);
        sImpl.setAnonymousTypeRefs(makeRefArray(anonymousTypes));
        sImpl.setWildcardSummary(wcElt.typedWildcards, wcElt.hasWildcards, wcAttr.typedWildcards, wcAttr.hasWildcards);
    }

    static void resolveCcRestriction(SchemaTypeImpl sImpl, ComplexRestrictionType parseTree, boolean mixed) {
        SchemaType baseType;
        SchemaAttributeModelImpl attrModel;
        StscState state = StscState.get();
        String targetNamespace = sImpl.getTargetNamespace();
        boolean chameleon = sImpl.getChameleonNamespace() != null;
        if (parseTree.getBase() == null) {
            state.error("A complexContent must define a base type", 28, parseTree);
            baseType = null;
        } else {
            if (sImpl.isRedefinition()) {
                baseType = state.findRedefinedGlobalType(parseTree.getBase(), sImpl.getChameleonNamespace(), sImpl);
                if (baseType != null && !baseType.getName().equals(sImpl.getName())) {
                    state.error(XmlErrorCodes.SCHEMA_REDEFINE$SAME_TYPE, new Object[]{"<complexType>", QNameHelper.pretty(baseType.getName()), QNameHelper.pretty(sImpl.getName())}, parseTree);
                }
            } else {
                baseType = state.findGlobalType(parseTree.getBase(), sImpl.getChameleonNamespace(), targetNamespace);
            }
            if (baseType == null) {
                state.notFoundError(parseTree.getBase(), 0, parseTree.xgetBase(), true);
            }
        }
        if (baseType == null) {
            baseType = BuiltinSchemaTypeSystem.ST_ANY_TYPE;
        }
        if (baseType != null && baseType.finalRestriction()) {
            state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$FINAL, new Object[]{QNameHelper.pretty(baseType.getName()), QNameHelper.pretty(sImpl.getName())}, parseTree.xgetBase());
        }
        if (baseType != null && !StscResolver.resolveType((SchemaTypeImpl) baseType)) {
            baseType = null;
        }
        List anonymousTypes = new ArrayList();
        Group parseEg = getContentModel(parseTree);
        int particleCode = translateParticleCode(parseEg);
        Map elementModel = new LinkedHashMap();
        SchemaParticle contentModel = translateContentModel(sImpl, parseEg, targetNamespace, chameleon, sImpl.getElemFormDefault(), sImpl.getAttFormDefault(), particleCode, anonymousTypes, elementModel, false, null);
        boolean isAll = contentModel != null && contentModel.getParticleType() == 1;
        if (baseType == null) {
            attrModel = new SchemaAttributeModelImpl();
        } else {
            attrModel = new SchemaAttributeModelImpl(baseType.getAttributeModel());
        }
        translateAttributeModel(parseTree, targetNamespace, chameleon, sImpl.getAttFormDefault(), anonymousTypes, sImpl, null, attrModel, baseType, false, null);
        WildcardResult wcElt = summarizeEltWildcards(contentModel);
        WildcardResult wcAttr = summarizeAttrWildcards(attrModel);
        if (contentModel != null) {
            buildStateMachine(contentModel);
            if (!StscState.get().noUpa() && !((SchemaParticleImpl) contentModel).isDeterministic()) {
                StscState.get().error(XmlErrorCodes.UNIQUE_PARTICLE_ATTRIBUTION, (Object[]) null, parseEg);
            }
        }
        Map elementPropertyModel = buildContentPropertyModelByQName(contentModel, sImpl);
        Map attributePropertyModel = buildAttributePropertyModelByQName(attrModel, sImpl);
        int complexVariety = mixed ? 4 : contentModel == null ? 1 : 3;
        sImpl.setBaseTypeRef(baseType.getRef());
        sImpl.setBaseDepth(((SchemaTypeImpl) baseType).getBaseDepth() + 1);
        sImpl.setDerivationType(1);
        sImpl.setComplexTypeVariety(complexVariety);
        sImpl.setContentModel(contentModel, attrModel, elementPropertyModel, attributePropertyModel, isAll);
        sImpl.setAnonymousTypeRefs(makeRefArray(anonymousTypes));
        sImpl.setWildcardSummary(wcElt.typedWildcards, wcElt.hasWildcards, wcAttr.typedWildcards, wcAttr.hasWildcards);
    }

    static Map extractElementModel(SchemaType sType) {
        Map elementModel = new HashMap();
        if (sType != null) {
            SchemaProperty[] sProps = sType.getProperties();
            for (int i = 0; i < sProps.length; i++) {
                if (!sProps[i].isAttribute()) {
                    elementModel.put(sProps[i].getName(), sProps[i].getType());
                }
            }
        }
        return elementModel;
    }

    static void resolveCcExtension(SchemaTypeImpl sImpl, ExtensionType parseTree, boolean mixed) {
        SchemaType baseType;
        SchemaAttributeModelImpl attrModel;
        int complexVariety;
        StscState state = StscState.get();
        String targetNamespace = sImpl.getTargetNamespace();
        boolean chameleon = sImpl.getChameleonNamespace() != null;
        if (parseTree.getBase() == null) {
            state.error("A complexContent must define a base type", 28, parseTree);
            baseType = null;
        } else {
            if (sImpl.isRedefinition()) {
                baseType = state.findRedefinedGlobalType(parseTree.getBase(), sImpl.getChameleonNamespace(), sImpl);
                if (baseType != null && !baseType.getName().equals(sImpl.getName())) {
                    state.error(XmlErrorCodes.SCHEMA_REDEFINE$SAME_TYPE, new Object[]{"<complexType>", QNameHelper.pretty(baseType.getName()), QNameHelper.pretty(sImpl.getName())}, parseTree);
                }
            } else {
                baseType = state.findGlobalType(parseTree.getBase(), sImpl.getChameleonNamespace(), targetNamespace);
            }
            if (baseType == null) {
                state.notFoundError(parseTree.getBase(), 0, parseTree.xgetBase(), true);
            }
        }
        if (baseType != null && !StscResolver.resolveType((SchemaTypeImpl) baseType)) {
            baseType = null;
        }
        if (baseType != null && baseType.isSimpleType()) {
            state.recover(XmlErrorCodes.SCHEMA_COMPLEX_TYPE$COMPLEX_CONTENT, new Object[]{QNameHelper.pretty(baseType.getName())}, parseTree.xgetBase());
            baseType = null;
        }
        if (baseType != null && baseType.finalExtension()) {
            state.error(XmlErrorCodes.COMPLEX_TYPE_EXTENSION$FINAL, new Object[]{QNameHelper.pretty(baseType.getName()), QNameHelper.pretty(sImpl.getName())}, parseTree.xgetBase());
        }
        SchemaParticle baseContentModel = baseType == null ? null : baseType.getContentModel();
        List anonymousTypes = new ArrayList();
        Map baseElementModel = extractElementModel(baseType);
        Group parseEg = getContentModel(parseTree);
        if (baseType != null && baseType.getContentType() == 2) {
            if (parseEg != null) {
                state.recover(XmlErrorCodes.COMPLEX_TYPE_EXTENSION$EXTENDING_SIMPLE_CONTENT, new Object[]{QNameHelper.pretty(baseType.getName())}, parseTree.xgetBase());
                baseType = null;
            } else {
                resolveScExtensionPart2(sImpl, baseType, parseTree, targetNamespace, chameleon);
                return;
            }
        }
        SchemaParticle extensionModel = translateContentModel(sImpl, parseEg, targetNamespace, chameleon, sImpl.getElemFormDefault(), sImpl.getAttFormDefault(), translateParticleCode(parseEg), anonymousTypes, baseElementModel, false, null);
        if (extensionModel == null && !mixed) {
            mixed = baseType != null && baseType.getContentType() == 4;
        }
        if (baseType != null && baseType.getContentType() != 1) {
            if ((baseType.getContentType() == 4) != mixed) {
                state.error(XmlErrorCodes.COMPLEX_TYPE_EXTENSION$BOTH_ELEMEMENT_OR_MIXED, (Object[]) null, parseTree.xgetBase());
            }
        }
        if (baseType != null && baseType.hasAllContent() && extensionModel != null) {
            state.error("Cannot extend a type with 'all' content model", 42, parseTree.xgetBase());
            extensionModel = null;
        }
        SchemaParticle contentModel = extendContentModel(baseContentModel, extensionModel, parseTree);
        boolean isAll = contentModel != null && contentModel.getParticleType() == 1;
        if (baseType == null) {
            attrModel = new SchemaAttributeModelImpl();
        } else {
            attrModel = new SchemaAttributeModelImpl(baseType.getAttributeModel());
        }
        translateAttributeModel(parseTree, targetNamespace, chameleon, sImpl.getAttFormDefault(), anonymousTypes, sImpl, null, attrModel, baseType, true, null);
        WildcardResult wcElt = summarizeEltWildcards(contentModel);
        WildcardResult wcAttr = summarizeAttrWildcards(attrModel);
        if (contentModel != null) {
            buildStateMachine(contentModel);
            if (!StscState.get().noUpa() && !((SchemaParticleImpl) contentModel).isDeterministic()) {
                StscState.get().error(XmlErrorCodes.UNIQUE_PARTICLE_ATTRIBUTION, (Object[]) null, parseEg);
            }
        }
        Map elementPropertyModel = buildContentPropertyModelByQName(contentModel, sImpl);
        Map attributePropertyModel = buildAttributePropertyModelByQName(attrModel, sImpl);
        if (contentModel == null && baseType != null && baseType.getContentType() == 2) {
            complexVariety = 2;
            sImpl.setContentBasedOnTypeRef(baseType.getContentBasedOnType().getRef());
        } else {
            complexVariety = mixed ? 4 : contentModel == null ? 1 : 3;
        }
        if (baseType == null) {
            baseType = XmlObject.type;
        }
        sImpl.setBaseTypeRef(baseType.getRef());
        sImpl.setBaseDepth(((SchemaTypeImpl) baseType).getBaseDepth() + 1);
        sImpl.setDerivationType(2);
        sImpl.setComplexTypeVariety(complexVariety);
        sImpl.setContentModel(contentModel, attrModel, elementPropertyModel, attributePropertyModel, isAll);
        sImpl.setAnonymousTypeRefs(makeRefArray(anonymousTypes));
        sImpl.setWildcardSummary(wcElt.typedWildcards, wcElt.hasWildcards, wcAttr.typedWildcards, wcAttr.hasWildcards);
    }

    static void resolveScRestriction(SchemaTypeImpl sImpl, SimpleRestrictionType parseTree) {
        SchemaType baseType;
        SchemaAttributeModelImpl attrModel;
        SchemaType contentType = null;
        StscState state = StscState.get();
        String targetNamespace = sImpl.getTargetNamespace();
        boolean chameleon = sImpl.getChameleonNamespace() != null;
        List anonymousTypes = new ArrayList();
        if (parseTree.getSimpleType() != null) {
            LocalSimpleType typedef = parseTree.getSimpleType();
            SchemaType anonType = StscTranslator.translateAnonymousSimpleType(typedef, targetNamespace, chameleon, sImpl.getElemFormDefault(), sImpl.getAttFormDefault(), anonymousTypes, sImpl);
            contentType = anonType;
        }
        if (parseTree.getBase() == null) {
            state.error("A simpleContent restriction must define a base type", 28, parseTree);
            baseType = BuiltinSchemaTypeSystem.ST_ANY_SIMPLE;
        } else {
            if (sImpl.isRedefinition()) {
                baseType = state.findRedefinedGlobalType(parseTree.getBase(), sImpl.getChameleonNamespace(), sImpl);
                if (baseType != null && !baseType.getName().equals(sImpl.getName())) {
                    state.error(XmlErrorCodes.SCHEMA_REDEFINE$SAME_TYPE, new Object[]{"<simpleType>", QNameHelper.pretty(baseType.getName()), QNameHelper.pretty(sImpl.getName())}, parseTree);
                }
            } else {
                baseType = state.findGlobalType(parseTree.getBase(), sImpl.getChameleonNamespace(), targetNamespace);
            }
            if (baseType == null) {
                state.notFoundError(parseTree.getBase(), 0, parseTree.xgetBase(), true);
                baseType = BuiltinSchemaTypeSystem.ST_ANY_SIMPLE;
            }
        }
        StscResolver.resolveType((SchemaTypeImpl) baseType);
        if (contentType != null) {
            StscResolver.resolveType((SchemaTypeImpl) contentType);
        } else {
            contentType = baseType;
        }
        if (baseType.isSimpleType()) {
            state.recover(XmlErrorCodes.COMPLEX_TYPE_PROPERTIES$SIMPLE_TYPE_EXTENSION, new Object[]{QNameHelper.pretty(baseType.getName())}, parseTree);
            baseType = BuiltinSchemaTypeSystem.ST_ANY_SIMPLE;
        } else if (baseType.getContentType() != 2 && contentType == null) {
            baseType = BuiltinSchemaTypeSystem.ST_ANY_SIMPLE;
        }
        if (baseType != null && baseType.finalRestriction()) {
            state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$FINAL, new Object[]{QNameHelper.pretty(baseType.getName()), QNameHelper.pretty(sImpl.getName())}, parseTree.xgetBase());
        }
        if (baseType == null) {
            attrModel = new SchemaAttributeModelImpl();
        } else {
            attrModel = new SchemaAttributeModelImpl(baseType.getAttributeModel());
        }
        translateAttributeModel(parseTree, targetNamespace, chameleon, sImpl.getAttFormDefault(), anonymousTypes, sImpl, null, attrModel, baseType, false, null);
        WildcardResult wcAttr = summarizeAttrWildcards(attrModel);
        Map attributePropertyModel = buildAttributePropertyModelByQName(attrModel, sImpl);
        sImpl.setBaseTypeRef(baseType.getRef());
        sImpl.setBaseDepth(((SchemaTypeImpl) baseType).getBaseDepth() + 1);
        sImpl.setContentBasedOnTypeRef(contentType.getRef());
        sImpl.setDerivationType(1);
        sImpl.setAnonymousTypeRefs(makeRefArray(anonymousTypes));
        sImpl.setWildcardSummary(QNameSet.EMPTY, false, wcAttr.typedWildcards, wcAttr.hasWildcards);
        sImpl.setComplexTypeVariety(2);
        sImpl.setContentModel(null, attrModel, null, attributePropertyModel, false);
        sImpl.setSimpleTypeVariety(contentType.getSimpleVariety());
        sImpl.setPrimitiveTypeRef(contentType.getPrimitiveType() == null ? null : contentType.getPrimitiveType().getRef());
        switch (sImpl.getSimpleVariety()) {
            case 2:
                sImpl.setUnionMemberTypeRefs(makeRefArray(Arrays.asList(contentType.getUnionMemberTypes())));
                break;
            case 3:
                sImpl.setListItemTypeRef(contentType.getListItemType().getRef());
                break;
        }
        StscSimpleTypeResolver.resolveFacets(sImpl, parseTree, (SchemaTypeImpl) contentType);
        StscSimpleTypeResolver.resolveFundamentalFacets(sImpl);
    }

    static void resolveScExtension(SchemaTypeImpl sImpl, SimpleExtensionType parseTree) {
        SchemaType baseType;
        StscState state = StscState.get();
        String targetNamespace = sImpl.getTargetNamespace();
        boolean chameleon = sImpl.getChameleonNamespace() != null;
        if (parseTree.getBase() == null) {
            state.error("A simpleContent extension must define a base type", 28, parseTree);
            baseType = BuiltinSchemaTypeSystem.ST_ANY_SIMPLE;
        } else {
            if (sImpl.isRedefinition()) {
                baseType = state.findRedefinedGlobalType(parseTree.getBase(), sImpl.getChameleonNamespace(), sImpl);
                if (baseType != null && !baseType.getName().equals(sImpl.getName())) {
                    state.error(XmlErrorCodes.SCHEMA_REDEFINE$SAME_TYPE, new Object[]{"<simpleType>", QNameHelper.pretty(baseType.getName()), QNameHelper.pretty(sImpl.getName())}, parseTree);
                }
            } else {
                baseType = state.findGlobalType(parseTree.getBase(), sImpl.getChameleonNamespace(), targetNamespace);
            }
            if (baseType == null) {
                state.notFoundError(parseTree.getBase(), 0, parseTree.xgetBase(), true);
                baseType = BuiltinSchemaTypeSystem.ST_ANY_SIMPLE;
            }
        }
        StscResolver.resolveType((SchemaTypeImpl) baseType);
        if (!baseType.isSimpleType() && baseType.getContentType() != 2) {
            state.error(XmlErrorCodes.SCHEMA_COMPLEX_TYPE$SIMPLE_CONTENT, new Object[]{QNameHelper.pretty(baseType.getName())}, parseTree);
            baseType = BuiltinSchemaTypeSystem.ST_ANY_SIMPLE;
        }
        if (baseType != null && baseType.finalExtension()) {
            state.error(XmlErrorCodes.COMPLEX_TYPE_EXTENSION$FINAL, new Object[]{QNameHelper.pretty(baseType.getName()), QNameHelper.pretty(sImpl.getName())}, parseTree.xgetBase());
        }
        resolveScExtensionPart2(sImpl, baseType, parseTree, targetNamespace, chameleon);
    }

    static void resolveScExtensionPart2(SchemaTypeImpl sImpl, SchemaType baseType, ExtensionType parseTree, String targetNamespace, boolean chameleon) {
        List anonymousTypes = new ArrayList();
        SchemaAttributeModelImpl attrModel = new SchemaAttributeModelImpl(baseType.getAttributeModel());
        translateAttributeModel(parseTree, targetNamespace, chameleon, sImpl.getAttFormDefault(), anonymousTypes, sImpl, null, attrModel, baseType, true, null);
        WildcardResult wcAttr = summarizeAttrWildcards(attrModel);
        Map attributePropertyModel = buildAttributePropertyModelByQName(attrModel, sImpl);
        sImpl.setBaseTypeRef(baseType.getRef());
        sImpl.setBaseDepth(((SchemaTypeImpl) baseType).getBaseDepth() + 1);
        sImpl.setContentBasedOnTypeRef(baseType.getRef());
        sImpl.setDerivationType(2);
        sImpl.setAnonymousTypeRefs(makeRefArray(anonymousTypes));
        sImpl.setWildcardSummary(QNameSet.EMPTY, false, wcAttr.typedWildcards, wcAttr.hasWildcards);
        sImpl.setComplexTypeVariety(2);
        sImpl.setContentModel(null, attrModel, null, attributePropertyModel, false);
        sImpl.setSimpleTypeVariety(baseType.getSimpleVariety());
        sImpl.setPrimitiveTypeRef(baseType.getPrimitiveType() == null ? null : baseType.getPrimitiveType().getRef());
        switch (sImpl.getSimpleVariety()) {
            case 2:
                sImpl.setUnionMemberTypeRefs(makeRefArray(Arrays.asList(baseType.getUnionMemberTypes())));
                break;
            case 3:
                sImpl.setListItemTypeRef(baseType.getListItemType().getRef());
                break;
        }
        StscSimpleTypeResolver.resolveFacets(sImpl, null, (SchemaTypeImpl) baseType);
        StscSimpleTypeResolver.resolveFundamentalFacets(sImpl);
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/StscComplexTypeResolver$WildcardResult.class */
    static class WildcardResult {
        QNameSet typedWildcards;
        boolean hasWildcards;

        WildcardResult(QNameSet typedWildcards, boolean hasWildcards) {
            this.typedWildcards = typedWildcards;
            this.hasWildcards = hasWildcards;
        }
    }

    static WildcardResult summarizeAttrWildcards(SchemaAttributeModel attrModel) {
        if (attrModel.getWildcardProcess() == 0) {
            return new WildcardResult(QNameSet.EMPTY, false);
        }
        if (attrModel.getWildcardProcess() == 3) {
            return new WildcardResult(QNameSet.EMPTY, true);
        }
        return new WildcardResult(attrModel.getWildcardSet(), true);
    }

    static WildcardResult summarizeEltWildcards(SchemaParticle contentModel) {
        if (contentModel == null) {
            return new WildcardResult(QNameSet.EMPTY, false);
        }
        switch (contentModel.getParticleType()) {
            case 1:
            case 2:
            case 3:
                QNameSetBuilder set = new QNameSetBuilder();
                boolean hasWildcards = false;
                for (int i = 0; i < contentModel.countOfParticleChild(); i++) {
                    WildcardResult inner = summarizeEltWildcards(contentModel.getParticleChild(i));
                    set.addAll(inner.typedWildcards);
                    hasWildcards |= inner.hasWildcards;
                }
                return new WildcardResult(set.toQNameSet(), hasWildcards);
            case 4:
            default:
                return new WildcardResult(QNameSet.EMPTY, false);
            case 5:
                return new WildcardResult(contentModel.getWildcardProcess() == 3 ? QNameSet.EMPTY : contentModel.getWildcardSet(), true);
        }
    }

    /* JADX WARN: Incorrect condition in loop: B:10:0x003d */
    /* JADX WARN: Removed duplicated region for block: B:73:0x02ae  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static void translateAttributeModel(org.apache.xmlbeans.XmlObject r12, java.lang.String r13, boolean r14, java.lang.String r15, java.util.List r16, org.apache.xmlbeans.SchemaType r17, java.util.Set r18, org.apache.xmlbeans.impl.schema.SchemaAttributeModelImpl r19, org.apache.xmlbeans.SchemaType r20, boolean r21, org.apache.xmlbeans.impl.schema.SchemaAttributeGroupImpl r22) {
        /*
            Method dump skipped, instructions count: 1104
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.schema.StscComplexTypeResolver.translateAttributeModel(org.apache.xmlbeans.XmlObject, java.lang.String, boolean, java.lang.String, java.util.List, org.apache.xmlbeans.SchemaType, java.util.Set, org.apache.xmlbeans.impl.schema.SchemaAttributeModelImpl, org.apache.xmlbeans.SchemaType, boolean, org.apache.xmlbeans.impl.schema.SchemaAttributeGroupImpl):void");
    }

    static SchemaParticle extendContentModel(SchemaParticle baseContentModel, SchemaParticle extendedContentModel, XmlObject parseTree) {
        if (extendedContentModel == null) {
            return baseContentModel;
        }
        if (baseContentModel == null) {
            return extendedContentModel;
        }
        SchemaParticleImpl sPart = new SchemaParticleImpl();
        sPart.setParticleType(3);
        List accumulate = new ArrayList();
        addMinusPointlessParticles(accumulate, baseContentModel, 3);
        addMinusPointlessParticles(accumulate, extendedContentModel, 3);
        sPart.setMinOccurs(BigInteger.ONE);
        sPart.setMaxOccurs(BigInteger.ONE);
        sPart.setParticleChildren((SchemaParticle[]) accumulate.toArray(new SchemaParticle[accumulate.size()]));
        return filterPointlessParticlesAndVerifyAllParticles(sPart, parseTree);
    }

    static BigInteger extractMinOccurs(XmlNonNegativeInteger nni) {
        if (nni == null) {
            return BigInteger.ONE;
        }
        BigInteger result = nni.getBigIntegerValue();
        if (result == null) {
            return BigInteger.ONE;
        }
        return result;
    }

    static BigInteger extractMaxOccurs(AllNNI allNNI) {
        if (allNNI == null) {
            return BigInteger.ONE;
        }
        if (allNNI.instanceType().getPrimitiveType().getBuiltinTypeCode() == 11) {
            return ((XmlInteger) allNNI).getBigIntegerValue();
        }
        return null;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/StscComplexTypeResolver$RedefinitionForGroup.class */
    private static class RedefinitionForGroup {
        private SchemaModelGroupImpl group;
        private boolean seenRedefinition = false;

        public RedefinitionForGroup(SchemaModelGroupImpl group) {
            this.group = group;
        }

        public SchemaModelGroupImpl getGroup() {
            return this.group;
        }

        public boolean isSeenRedefinition() {
            return this.seenRedefinition;
        }

        public void setSeenRedefinition(boolean seenRedefinition) {
            this.seenRedefinition = seenRedefinition;
        }
    }

    /* JADX WARN: Incorrect condition in loop: B:134:0x042e */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static org.apache.xmlbeans.SchemaParticle translateContentModel(org.apache.xmlbeans.SchemaType r13, org.apache.xmlbeans.XmlObject r14, java.lang.String r15, boolean r16, java.lang.String r17, java.lang.String r18, int r19, java.util.List r20, java.util.Map r21, boolean r22, org.apache.xmlbeans.impl.schema.StscComplexTypeResolver.RedefinitionForGroup r23) {
        /*
            Method dump skipped, instructions count: 1199
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.schema.StscComplexTypeResolver.translateContentModel(org.apache.xmlbeans.SchemaType, org.apache.xmlbeans.XmlObject, java.lang.String, boolean, java.lang.String, java.lang.String, int, java.util.List, java.util.Map, boolean, org.apache.xmlbeans.impl.schema.StscComplexTypeResolver$RedefinitionForGroup):org.apache.xmlbeans.SchemaParticle");
    }

    static int translateWildcardProcess(Wildcard.ProcessContents process) {
        if (process == null) {
            return 1;
        }
        String processValue = process.getStringValue();
        if ("lax".equals(processValue)) {
            return 2;
        }
        if ("skip".equals(processValue)) {
            return 3;
        }
        return 1;
    }

    static SchemaParticle filterPointlessParticlesAndVerifyAllParticles(SchemaParticle part, XmlObject parseTree) {
        if (part.getMaxOccurs() != null && part.getMaxOccurs().signum() == 0) {
            return null;
        }
        switch (part.getParticleType()) {
            case 1:
            case 3:
                if (part.getParticleChildren().length == 0) {
                    return null;
                }
                if (part.isSingleton() && part.countOfParticleChild() == 1) {
                    return part.getParticleChild(0);
                }
                break;
            case 2:
                if (part.getParticleChildren().length == 0 && part.getMinOccurs().compareTo(BigInteger.ZERO) == 0) {
                    return null;
                }
                if (part.isSingleton() && part.countOfParticleChild() == 1) {
                    return part.getParticleChild(0);
                }
                break;
            case 4:
            case 5:
                return part;
            default:
                if ($assertionsDisabled) {
                    throw new IllegalStateException();
                }
                throw new AssertionError();
        }
        boolean isAll = part.getParticleType() == 1;
        if (isAll && (part.getMaxOccurs() == null || part.getMaxOccurs().compareTo(BigInteger.ONE) > 0)) {
            StscState.get().error(XmlErrorCodes.ALL_GROUP_LIMITED$IN_MIN_MAX_1_PARTICLE, (Object[]) null, parseTree);
        }
        for (int i = 0; i < part.countOfParticleChild(); i++) {
            SchemaParticle child = part.getParticleChild(i);
            if (child.getParticleType() == 1) {
                StscState.get().error(XmlErrorCodes.ALL_GROUP_LIMITED$IN_COMPLEX_TYPE_DEF_PARTICLE, (Object[]) null, parseTree);
            } else if (isAll && (child.getParticleType() != 4 || child.getMaxOccurs() == null || child.getMaxOccurs().compareTo(BigInteger.ONE) > 0)) {
                StscState.get().error(XmlErrorCodes.ALL_GROUP_LIMITED$CHILD_PARTICLES_MAX_LTE_1, (Object[]) null, parseTree);
            }
        }
        return part;
    }

    static void addMinusPointlessParticles(List list, SchemaParticle part, int parentParticleType) {
        if (part == null) {
            return;
        }
        switch (part.getParticleType()) {
            case 2:
                if (parentParticleType == 2 && part.isSingleton()) {
                    list.addAll(Arrays.asList(part.getParticleChildren()));
                    return;
                }
                break;
            case 3:
                if (parentParticleType == 3 && part.isSingleton()) {
                    list.addAll(Arrays.asList(part.getParticleChildren()));
                    return;
                }
                break;
        }
        list.add(part);
    }

    static Map buildAttributePropertyModelByQName(SchemaAttributeModel attrModel, SchemaType owner) {
        Map result = new LinkedHashMap();
        SchemaLocalAttribute[] attruses = attrModel.getAttributes();
        for (int i = 0; i < attruses.length; i++) {
            result.put(attruses[i].getName(), buildUseProperty(attruses[i], owner));
        }
        return result;
    }

    static Map buildContentPropertyModelByQName(SchemaParticle part, SchemaType owner) {
        if (part == null) {
            return Collections.EMPTY_MAP;
        }
        boolean asSequence = false;
        Map model = null;
        switch (part.getParticleType()) {
            case 1:
            case 3:
                asSequence = true;
                break;
            case 2:
                asSequence = false;
                break;
            case 4:
                model = buildElementPropertyModel((SchemaLocalElement) part, owner);
                break;
            case 5:
                model = Collections.EMPTY_MAP;
                break;
            default:
                if ($assertionsDisabled) {
                    throw new IllegalStateException();
                }
                throw new AssertionError();
        }
        if (model == null) {
            model = new LinkedHashMap();
            SchemaParticle[] children = part.getParticleChildren();
            for (SchemaParticle schemaParticle : children) {
                Map childModel = buildContentPropertyModelByQName(schemaParticle, owner);
                for (SchemaProperty iProp : childModel.values()) {
                    SchemaPropertyImpl oProp = (SchemaPropertyImpl) model.get(iProp.getName());
                    if (oProp == null) {
                        if (!asSequence) {
                            ((SchemaPropertyImpl) iProp).setMinOccurs(BigInteger.ZERO);
                        }
                        model.put(iProp.getName(), iProp);
                    } else {
                        if (!$assertionsDisabled && !oProp.getType().equals(iProp.getType())) {
                            throw new AssertionError();
                        }
                        mergeProperties(oProp, iProp, asSequence);
                    }
                }
            }
            BigInteger min = part.getMinOccurs();
            BigInteger max = part.getMaxOccurs();
            for (SchemaProperty oProp2 : model.values()) {
                BigInteger minOccurs = oProp2.getMinOccurs();
                BigInteger maxOccurs = oProp2.getMaxOccurs();
                BigInteger minOccurs2 = minOccurs.multiply(min);
                if (max != null && max.equals(BigInteger.ZERO)) {
                    maxOccurs = BigInteger.ZERO;
                } else if (maxOccurs != null && !maxOccurs.equals(BigInteger.ZERO)) {
                    maxOccurs = max == null ? null : maxOccurs.multiply(max);
                }
                ((SchemaPropertyImpl) oProp2).setMinOccurs(minOccurs2);
                ((SchemaPropertyImpl) oProp2).setMaxOccurs(maxOccurs);
            }
        }
        return model;
    }

    static Map buildElementPropertyModel(SchemaLocalElement epart, SchemaType owner) {
        Map result = new HashMap(1);
        SchemaProperty sProp = buildUseProperty(epart, owner);
        result.put(sProp.getName(), sProp);
        return result;
    }

    static SchemaProperty buildUseProperty(SchemaField use, SchemaType owner) {
        SchemaPropertyImpl sPropImpl = new SchemaPropertyImpl();
        sPropImpl.setName(use.getName());
        sPropImpl.setContainerTypeRef(owner.getRef());
        sPropImpl.setTypeRef(use.getType().getRef());
        sPropImpl.setAttribute(use.isAttribute());
        sPropImpl.setDefault(use.isDefault() ? 2 : 0);
        sPropImpl.setFixed(use.isFixed() ? 2 : 0);
        sPropImpl.setNillable(use.isNillable() ? 2 : 0);
        sPropImpl.setDefaultText(use.getDefaultText());
        sPropImpl.setMinOccurs(use.getMinOccurs());
        sPropImpl.setMaxOccurs(use.getMaxOccurs());
        if (use instanceof SchemaLocalElementImpl) {
            SchemaLocalElementImpl elt = (SchemaLocalElementImpl) use;
            sPropImpl.setAcceptedNames(elt.acceptedStartNames());
        }
        return sPropImpl;
    }

    static void mergeProperties(SchemaPropertyImpl into, SchemaProperty from, boolean asSequence) {
        BigInteger minOccurs;
        BigInteger minOccurs2 = into.getMinOccurs();
        BigInteger maxOccurs = into.getMaxOccurs();
        if (asSequence) {
            minOccurs = minOccurs2.add(from.getMinOccurs());
            if (maxOccurs != null) {
                maxOccurs = from.getMaxOccurs() == null ? null : maxOccurs.add(from.getMaxOccurs());
            }
        } else {
            minOccurs = minOccurs2.min(from.getMinOccurs());
            if (maxOccurs != null) {
                maxOccurs = from.getMaxOccurs() == null ? null : maxOccurs.max(from.getMaxOccurs());
            }
        }
        into.setMinOccurs(minOccurs);
        into.setMaxOccurs(maxOccurs);
        if (from.hasNillable() != into.hasNillable()) {
            into.setNillable(1);
        }
        if (from.hasDefault() != into.hasDefault()) {
            into.setDefault(1);
        }
        if (from.hasFixed() != into.hasFixed()) {
            into.setFixed(1);
        }
        if (into.getDefaultText() != null) {
            if (from.getDefaultText() == null || !into.getDefaultText().equals(from.getDefaultText())) {
                into.setDefaultText(null);
            }
        }
    }

    static SchemaParticle[] ensureStateMachine(SchemaParticle[] children) {
        for (SchemaParticle schemaParticle : children) {
            buildStateMachine(schemaParticle);
        }
        return children;
    }

    static void buildStateMachine(SchemaParticle contentModel) {
        if (contentModel == null) {
            return;
        }
        SchemaParticleImpl partImpl = (SchemaParticleImpl) contentModel;
        if (partImpl.hasTransitionNotes()) {
            return;
        }
        QNameSetBuilder start = new QNameSetBuilder();
        QNameSetBuilder excludenext = new QNameSetBuilder();
        boolean deterministic = true;
        boolean canskip = partImpl.getMinOccurs().signum() == 0;
        switch (partImpl.getParticleType()) {
            case 1:
                SchemaParticle[] children = ensureStateMachine(partImpl.getParticleChildren());
                canskip = true;
                for (int i = 0; !canskip && i < children.length; i++) {
                    if (!children[i].isSkippable()) {
                        canskip = false;
                    }
                }
                for (int i2 = 0; deterministic && i2 < children.length; i2++) {
                    if (!((SchemaParticleImpl) children[i2]).isDeterministic()) {
                        deterministic = false;
                    }
                }
                for (int i3 = 0; i3 < children.length; i3++) {
                    if (deterministic && !start.isDisjoint(children[i3].acceptedStartNames())) {
                        deterministic = false;
                    }
                    start.addAll(children[i3].acceptedStartNames());
                    excludenext.addAll(((SchemaParticleImpl) children[i3]).getExcludeNextSet());
                }
                if (canskip) {
                    excludenext.addAll(start);
                    break;
                }
                break;
            case 2:
                SchemaParticle[] children2 = ensureStateMachine(partImpl.getParticleChildren());
                canskip = false;
                for (int i4 = 0; !canskip && i4 < children2.length; i4++) {
                    if (children2[i4].isSkippable()) {
                        canskip = true;
                    }
                }
                for (int i5 = 0; deterministic && i5 < children2.length; i5++) {
                    if (!((SchemaParticleImpl) children2[i5]).isDeterministic()) {
                        deterministic = false;
                    }
                }
                for (int i6 = 0; i6 < children2.length; i6++) {
                    if (deterministic && !start.isDisjoint(children2[i6].acceptedStartNames())) {
                        deterministic = false;
                    }
                    start.addAll(children2[i6].acceptedStartNames());
                    excludenext.addAll(((SchemaParticleImpl) children2[i6]).getExcludeNextSet());
                }
                break;
            case 3:
                SchemaParticle[] children3 = ensureStateMachine(partImpl.getParticleChildren());
                canskip = true;
                for (int i7 = 0; canskip && i7 < children3.length; i7++) {
                    if (!children3[i7].isSkippable()) {
                        canskip = false;
                    }
                }
                for (int i8 = 0; deterministic && i8 < children3.length; i8++) {
                    if (!((SchemaParticleImpl) children3[i8]).isDeterministic()) {
                        deterministic = false;
                    }
                }
                for (int i9 = 1; i9 < children3.length; i9++) {
                    excludenext.addAll(((SchemaParticleImpl) children3[i9 - 1]).getExcludeNextSet());
                    if (deterministic && !excludenext.isDisjoint(children3[i9].acceptedStartNames())) {
                        deterministic = false;
                    }
                    if (children3[i9].isSkippable()) {
                        excludenext.addAll(children3[i9].acceptedStartNames());
                    } else {
                        excludenext.clear();
                    }
                }
                for (int i10 = 0; i10 < children3.length; i10++) {
                    start.addAll(children3[i10].acceptedStartNames());
                    if (!children3[i10].isSkippable()) {
                        break;
                    }
                }
                break;
            case 4:
                if (partImpl.hasTransitionRules()) {
                    start.addAll(partImpl.acceptedStartNames());
                    break;
                } else {
                    start.add(partImpl.getName());
                    break;
                }
            case 5:
                start.addAll(partImpl.getWildcardSet());
                break;
            default:
                throw new IllegalStateException("Unrecognized schema particle");
        }
        BigInteger minOccurs = partImpl.getMinOccurs();
        BigInteger maxOccurs = partImpl.getMaxOccurs();
        boolean canloop = maxOccurs == null || maxOccurs.compareTo(BigInteger.ONE) > 0;
        boolean varloop = maxOccurs == null || minOccurs.compareTo(maxOccurs) < 0;
        if (canloop && deterministic && !excludenext.isDisjoint(start)) {
            QNameSet suspectSet = excludenext.intersect(start);
            Map startMap = new HashMap();
            particlesMatchingStart(partImpl, suspectSet, startMap, new QNameSetBuilder());
            Map afterMap = new HashMap();
            particlesMatchingAfter(partImpl, suspectSet, afterMap, new QNameSetBuilder(), true);
            deterministic = afterMapSubsumedByStartMap(startMap, afterMap);
        }
        if (varloop) {
            excludenext.addAll(start);
        }
        partImpl.setTransitionRules(start.toQNameSet(), canskip || minOccurs.signum() == 0);
        partImpl.setTransitionNotes(excludenext.toQNameSet(), deterministic);
    }

    private static boolean afterMapSubsumedByStartMap(Map startMap, Map afterMap) {
        if (afterMap.size() > startMap.size()) {
            return false;
        }
        if (afterMap.isEmpty()) {
            return true;
        }
        for (SchemaParticle part : startMap.keySet()) {
            if (part.getParticleType() == 5 && afterMap.containsKey(part)) {
                QNameSet startSet = (QNameSet) startMap.get(part);
                QNameSet afterSet = (QNameSet) afterMap.get(part);
                if (!startSet.containsAll(afterSet)) {
                    return false;
                }
            }
            afterMap.remove(part);
            if (afterMap.isEmpty()) {
                return true;
            }
        }
        return afterMap.isEmpty();
    }

    private static void particlesMatchingStart(SchemaParticle part, QNameSetSpecification suspectSet, Map result, QNameSetBuilder eliminate) {
        switch (part.getParticleType()) {
            case 1:
            case 2:
                for (SchemaParticle schemaParticle : part.getParticleChildren()) {
                    particlesMatchingStart(schemaParticle, suspectSet, result, eliminate);
                }
                break;
            case 3:
                SchemaParticle[] children = part.getParticleChildren();
                if (children.length != 0) {
                    if (!children[0].isSkippable()) {
                        particlesMatchingStart(children[0], suspectSet, result, eliminate);
                        break;
                    } else {
                        QNameSetBuilder remainingSuspects = new QNameSetBuilder(suspectSet);
                        QNameSetBuilder suspectsToEliminate = new QNameSetBuilder();
                        for (int i = 0; i < children.length; i++) {
                            particlesMatchingStart(children[i], remainingSuspects, result, suspectsToEliminate);
                            eliminate.addAll(suspectsToEliminate);
                            if (!children[i].isSkippable()) {
                                break;
                            } else {
                                remainingSuspects.removeAll(suspectsToEliminate);
                                if (remainingSuspects.isEmpty()) {
                                    break;
                                } else {
                                    suspectsToEliminate.clear();
                                }
                            }
                        }
                        break;
                    }
                }
                break;
            case 4:
                if (suspectSet.contains(part.getName())) {
                    result.put(part, null);
                    eliminate.add(part.getName());
                    break;
                }
                break;
            case 5:
                if (!suspectSet.isDisjoint(part.getWildcardSet())) {
                    result.put(part, part.getWildcardSet().intersect(suspectSet));
                    eliminate.addAll(part.getWildcardSet());
                    break;
                }
                break;
        }
    }

    private static void particlesMatchingAfter(SchemaParticle part, QNameSetSpecification suspectSet, Map result, QNameSetBuilder eliminate, boolean top) {
        switch (part.getParticleType()) {
            case 1:
            case 2:
                for (SchemaParticle schemaParticle : part.getParticleChildren()) {
                    particlesMatchingAfter(schemaParticle, suspectSet, result, eliminate, false);
                }
                break;
            case 3:
                SchemaParticle[] children = part.getParticleChildren();
                if (children.length != 0) {
                    if (!children[children.length - 1].isSkippable()) {
                        particlesMatchingAfter(children[0], suspectSet, result, eliminate, false);
                        break;
                    } else {
                        QNameSetBuilder remainingSuspects = new QNameSetBuilder(suspectSet);
                        QNameSetBuilder suspectsToEliminate = new QNameSetBuilder();
                        for (int i = children.length - 1; i >= 0; i--) {
                            particlesMatchingAfter(children[i], remainingSuspects, result, suspectsToEliminate, false);
                            eliminate.addAll(suspectsToEliminate);
                            if (!children[i].isSkippable()) {
                                break;
                            } else {
                                remainingSuspects.removeAll(suspectsToEliminate);
                                if (remainingSuspects.isEmpty()) {
                                    break;
                                } else {
                                    suspectsToEliminate.clear();
                                }
                            }
                        }
                        break;
                    }
                }
                break;
        }
        if (!top) {
            BigInteger minOccurs = part.getMinOccurs();
            BigInteger maxOccurs = part.getMaxOccurs();
            boolean varloop = maxOccurs == null || minOccurs.compareTo(maxOccurs) < 0;
            if (varloop) {
                particlesMatchingStart(part, suspectSet, result, eliminate);
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/StscComplexTypeResolver$CodeForNameEntry.class */
    private static class CodeForNameEntry {
        public QName name;
        public int code;

        CodeForNameEntry(QName name, int code) {
            this.name = name;
            this.code = code;
        }
    }

    private static Map buildParticleCodeMap() {
        Map result = new HashMap();
        for (int i = 0; i < particleCodes.length; i++) {
            result.put(particleCodes[i].name, new Integer(particleCodes[i].code));
        }
        return result;
    }

    private static int translateParticleCode(Group parseEg) {
        if (parseEg == null) {
            return 0;
        }
        return translateParticleCode(parseEg.newCursor().getName());
    }

    private static int translateParticleCode(QName name) {
        Integer result = (Integer) particleCodeMap.get(name);
        if (result == null) {
            return 0;
        }
        return result.intValue();
    }

    private static Map buildAttributeCodeMap() {
        Map result = new HashMap();
        for (int i = 0; i < attributeCodes.length; i++) {
            result.put(attributeCodes[i].name, new Integer(attributeCodes[i].code));
        }
        return result;
    }

    static int translateAttributeCode(QName currentName) {
        Integer result = (Integer) attributeCodeMap.get(currentName);
        if (result == null) {
            return 0;
        }
        return result.intValue();
    }
}
