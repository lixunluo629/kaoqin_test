package org.apache.xmlbeans.impl.schema;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaAttributeModel;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaIdentityConstraint;
import org.apache.xmlbeans.SchemaLocalAttribute;
import org.apache.xmlbeans.SchemaLocalElement;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.XmlNOTATION;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.XBeanDebug;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/StscChecker.class */
public class StscChecker {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StscChecker.class.desiredAssertionStatus();
    }

    public static void checkAll() throws Exception {
        StscState state = StscState.get();
        List allSeenTypes = new ArrayList();
        allSeenTypes.addAll(Arrays.asList(state.documentTypes()));
        allSeenTypes.addAll(Arrays.asList(state.attributeTypes()));
        allSeenTypes.addAll(Arrays.asList(state.redefinedGlobalTypes()));
        allSeenTypes.addAll(Arrays.asList(state.globalTypes()));
        for (int i = 0; i < allSeenTypes.size(); i++) {
            SchemaType gType = (SchemaType) allSeenTypes.get(i);
            if (!state.noPvr() && !gType.isDocumentType()) {
                checkRestriction((SchemaTypeImpl) gType);
            }
            checkFields((SchemaTypeImpl) gType);
            allSeenTypes.addAll(Arrays.asList(gType.getAnonymousTypes()));
        }
        checkSubstitutionGroups(state.globalElements());
    }

    public static void checkFields(SchemaTypeImpl sType) throws Exception {
        SchemaType t;
        boolean hasNS;
        if (sType.isSimpleType()) {
            return;
        }
        XmlObject location = sType.getParseObject();
        SchemaAttributeModel sAttrModel = sType.getAttributeModel();
        if (sAttrModel != null) {
            SchemaLocalAttribute[] sAttrs = sAttrModel.getAttributes();
            QName idAttr = null;
            for (int i = 0; i < sAttrs.length; i++) {
                XmlObject attrLocation = ((SchemaLocalAttributeImpl) sAttrs[i])._parseObject;
                if (XmlID.type.isAssignableFrom(sAttrs[i].getType())) {
                    if (idAttr == null) {
                        idAttr = sAttrs[i].getName();
                    } else {
                        StscState.get().error(XmlErrorCodes.ATTR_GROUP_PROPERTIES$TWO_IDS, new Object[]{QNameHelper.pretty(idAttr), sAttrs[i].getName()}, attrLocation != null ? attrLocation : location);
                    }
                    if (sAttrs[i].getDefaultText() != null) {
                        StscState.get().error(XmlErrorCodes.ATTR_PROPERTIES$ID_FIXED_OR_DEFAULT, (Object[]) null, attrLocation != null ? attrLocation : location);
                    }
                } else if (XmlNOTATION.type.isAssignableFrom(sAttrs[i].getType())) {
                    if (sAttrs[i].getType().getBuiltinTypeCode() == 8) {
                        StscState.get().recover(XmlErrorCodes.ATTR_NOTATION_TYPE_FORBIDDEN, new Object[]{QNameHelper.pretty(sAttrs[i].getName())}, attrLocation != null ? attrLocation : location);
                    } else {
                        if (sAttrs[i].getType().getSimpleVariety() == 2) {
                            SchemaType[] members = sAttrs[i].getType().getUnionConstituentTypes();
                            for (SchemaType schemaType : members) {
                                if (schemaType.getBuiltinTypeCode() == 8) {
                                    StscState.get().recover(XmlErrorCodes.ATTR_NOTATION_TYPE_FORBIDDEN, new Object[]{QNameHelper.pretty(sAttrs[i].getName())}, attrLocation != null ? attrLocation : location);
                                }
                            }
                        }
                        if (sType.isAttributeType()) {
                            hasNS = sAttrs[i].getName().getNamespaceURI().length() > 0;
                        } else {
                            SchemaType outerType = sType;
                            while (true) {
                                t = outerType;
                                if (t.getOuterType() == null) {
                                    break;
                                } else {
                                    outerType = t.getOuterType();
                                }
                            }
                            if (t.isDocumentType()) {
                                hasNS = t.getDocumentElementName().getNamespaceURI().length() > 0;
                            } else {
                                hasNS = t.getName().getNamespaceURI().length() > 0;
                            }
                        }
                        if (hasNS) {
                            StscState.get().warning(XmlErrorCodes.ATTR_COMPATIBILITY_TARGETNS, new Object[]{QNameHelper.pretty(sAttrs[i].getName())}, attrLocation != null ? attrLocation : location);
                        }
                    }
                } else {
                    String valueConstraint = sAttrs[i].getDefaultText();
                    if (valueConstraint != null) {
                        try {
                            XmlAnySimpleType val = sAttrs[i].getDefaultValue();
                            if (!val.validate()) {
                                throw new Exception();
                            }
                            SchemaPropertyImpl sProp = (SchemaPropertyImpl) sType.getAttributeProperty(sAttrs[i].getName());
                            if (sProp != null && sProp.getDefaultText() != null) {
                                sProp.setDefaultValue(new XmlValueRef(val));
                            }
                        } catch (Exception e) {
                            String constraintName = sAttrs[i].isFixed() ? "fixed" : "default";
                            XmlObject constraintLocation = location;
                            if (attrLocation != null) {
                                constraintLocation = attrLocation.selectAttribute("", constraintName);
                                if (constraintLocation == null) {
                                    constraintLocation = attrLocation;
                                }
                            }
                            StscState.get().error(XmlErrorCodes.ATTR_PROPERTIES$CONSTRAINT_VALID, new Object[]{QNameHelper.pretty(sAttrs[i].getName()), constraintName, valueConstraint, QNameHelper.pretty(sAttrs[i].getType().getName())}, constraintLocation);
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        checkElementDefaults(sType.getContentModel(), location, sType);
    }

    private static void checkElementDefaults(SchemaParticle model, XmlObject location, SchemaType parentType) throws Exception {
        boolean hasNS;
        if (model == null) {
            return;
        }
        switch (model.getParticleType()) {
            case 1:
            case 2:
            case 3:
                SchemaParticle[] children = model.getParticleChildren();
                for (SchemaParticle schemaParticle : children) {
                    checkElementDefaults(schemaParticle, location, parentType);
                }
                return;
            case 4:
                String valueConstraint = model.getDefaultText();
                if (valueConstraint != null) {
                    if (model.getType().isSimpleType() || model.getType().getContentType() == 2) {
                        try {
                            XmlAnySimpleType val = model.getDefaultValue();
                            XmlOptions opt = new XmlOptions();
                            opt.put(XmlOptions.VALIDATE_TEXT_ONLY);
                            if (!val.validate(opt)) {
                                throw new Exception();
                            }
                            SchemaPropertyImpl sProp = (SchemaPropertyImpl) parentType.getElementProperty(model.getName());
                            if (sProp != null && sProp.getDefaultText() != null) {
                                sProp.setDefaultValue(new XmlValueRef(val));
                            }
                        } catch (Exception e) {
                            String constraintName = model.isFixed() ? "fixed" : "default";
                            XmlObject constraintLocation = location.selectAttribute("", constraintName);
                            StscState.get().error(XmlErrorCodes.ELEM_PROPERTIES$CONSTRAINT_VALID, new Object[]{QNameHelper.pretty(model.getName()), constraintName, valueConstraint, QNameHelper.pretty(model.getType().getName())}, constraintLocation == null ? location : constraintLocation);
                        }
                    } else if (model.getType().getContentType() == 4) {
                        if (!model.getType().getContentModel().isSkippable()) {
                            String constraintName2 = model.isFixed() ? "fixed" : "default";
                            XmlObject constraintLocation2 = location.selectAttribute("", constraintName2);
                            StscState.get().error(XmlErrorCodes.ELEM_DEFAULT_VALID$MIXED_AND_EMPTIABLE, new Object[]{QNameHelper.pretty(model.getName()), constraintName2, valueConstraint}, constraintLocation2 == null ? location : constraintLocation2);
                        } else {
                            SchemaPropertyImpl sProp2 = (SchemaPropertyImpl) parentType.getElementProperty(model.getName());
                            if (sProp2 != null && sProp2.getDefaultText() != null) {
                                sProp2.setDefaultValue(new XmlValueRef(XmlString.type.newValue(valueConstraint)));
                            }
                        }
                    } else if (model.getType().getContentType() == 3) {
                        XmlObject constraintLocation3 = location.selectAttribute("", "default");
                        StscState.get().error(XmlErrorCodes.ELEM_DEFAULT_VALID$SIMPLE_TYPE_OR_MIXED, new Object[]{QNameHelper.pretty(model.getName()), valueConstraint, "element"}, constraintLocation3 == null ? location : constraintLocation3);
                    } else if (model.getType().getContentType() == 1) {
                        XmlObject constraintLocation4 = location.selectAttribute("", "default");
                        StscState.get().error(XmlErrorCodes.ELEM_DEFAULT_VALID$SIMPLE_TYPE_OR_MIXED, new Object[]{QNameHelper.pretty(model.getName()), valueConstraint, "empty"}, constraintLocation4 == null ? location : constraintLocation4);
                    }
                }
                String warningType = null;
                if (BuiltinSchemaTypeSystem.ST_ID.isAssignableFrom(model.getType())) {
                    warningType = BuiltinSchemaTypeSystem.ST_ID.getName().getLocalPart();
                } else if (BuiltinSchemaTypeSystem.ST_IDREF.isAssignableFrom(model.getType())) {
                    warningType = BuiltinSchemaTypeSystem.ST_IDREF.getName().getLocalPart();
                } else if (BuiltinSchemaTypeSystem.ST_IDREFS.isAssignableFrom(model.getType())) {
                    warningType = BuiltinSchemaTypeSystem.ST_IDREFS.getName().getLocalPart();
                } else if (BuiltinSchemaTypeSystem.ST_ENTITY.isAssignableFrom(model.getType())) {
                    warningType = BuiltinSchemaTypeSystem.ST_ENTITY.getName().getLocalPart();
                } else if (BuiltinSchemaTypeSystem.ST_ENTITIES.isAssignableFrom(model.getType())) {
                    warningType = BuiltinSchemaTypeSystem.ST_ENTITIES.getName().getLocalPart();
                } else if (BuiltinSchemaTypeSystem.ST_NOTATION.isAssignableFrom(model.getType())) {
                    if (model.getType().getBuiltinTypeCode() == 8) {
                        StscState.get().recover(XmlErrorCodes.ELEM_NOTATION_TYPE_FORBIDDEN, new Object[]{QNameHelper.pretty(model.getName())}, ((SchemaLocalElementImpl) model)._parseObject == null ? location : ((SchemaLocalElementImpl) model)._parseObject.selectAttribute("", "type"));
                    } else {
                        if (model.getType().getSimpleVariety() == 2) {
                            SchemaType[] members = model.getType().getUnionConstituentTypes();
                            for (SchemaType schemaType : members) {
                                if (schemaType.getBuiltinTypeCode() == 8) {
                                    StscState.get().recover(XmlErrorCodes.ELEM_NOTATION_TYPE_FORBIDDEN, new Object[]{QNameHelper.pretty(model.getName())}, ((SchemaLocalElementImpl) model)._parseObject == null ? location : ((SchemaLocalElementImpl) model)._parseObject.selectAttribute("", "type"));
                                }
                            }
                        }
                        warningType = BuiltinSchemaTypeSystem.ST_NOTATION.getName().getLocalPart();
                    }
                    SchemaType outerType = parentType;
                    while (true) {
                        SchemaType t = outerType;
                        if (t.getOuterType() != null) {
                            outerType = t.getOuterType();
                        } else {
                            if (t.isDocumentType()) {
                                hasNS = t.getDocumentElementName().getNamespaceURI().length() > 0;
                            } else {
                                hasNS = t.getName().getNamespaceURI().length() > 0;
                            }
                            if (hasNS) {
                                StscState.get().warning(XmlErrorCodes.ELEM_COMPATIBILITY_TARGETNS, new Object[]{QNameHelper.pretty(model.getName())}, ((SchemaLocalElementImpl) model)._parseObject == null ? location : ((SchemaLocalElementImpl) model)._parseObject);
                            }
                        }
                    }
                }
                if (warningType != null) {
                    StscState.get().warning(XmlErrorCodes.ELEM_COMPATIBILITY_TYPE, new Object[]{QNameHelper.pretty(model.getName()), warningType}, ((SchemaLocalElementImpl) model)._parseObject == null ? location : ((SchemaLocalElementImpl) model)._parseObject.selectAttribute("", "type"));
                    return;
                }
                return;
            default:
                return;
        }
    }

    public static boolean checkRestriction(SchemaTypeImpl sType) throws IOException {
        SchemaType bType;
        if (sType.getDerivationType() == 1 && !sType.isSimpleType()) {
            StscState state = StscState.get();
            XmlObject location = sType.getParseObject();
            SchemaType baseType = sType.getBaseType();
            if (baseType.isSimpleType()) {
                state.error(XmlErrorCodes.SCHEMA_COMPLEX_TYPE$COMPLEX_CONTENT, new Object[]{QNameHelper.pretty(baseType.getName())}, location);
                return false;
            }
            switch (sType.getContentType()) {
                case 1:
                    switch (baseType.getContentType()) {
                        case 1:
                            break;
                        case 2:
                        default:
                            state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$EMPTY_AND_NOT_SIMPLE, (Object[]) null, location);
                            break;
                        case 3:
                        case 4:
                            if (baseType.getContentModel() != null && !baseType.getContentModel().isSkippable()) {
                                state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$EMPTY_AND_ELEMENT_OR_MIXED_EMPTIABLE, (Object[]) null, location);
                                break;
                            }
                            break;
                    }
                    return false;
                case 2:
                    switch (baseType.getContentType()) {
                        case 2:
                            SchemaType cType = sType.getContentBasedOnType();
                            if (cType != baseType) {
                                SchemaType contentBasedOnType = baseType;
                                while (true) {
                                    bType = contentBasedOnType;
                                    if (bType != null && !bType.isSimpleType()) {
                                        contentBasedOnType = bType.getContentBasedOnType();
                                    }
                                }
                                if (bType != null && !bType.isAssignableFrom(cType)) {
                                    state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$SC_NOT_DERIVED, (Object[]) null, location);
                                    break;
                                }
                            }
                            break;
                        case 4:
                            if (baseType.getContentModel() != null && !baseType.getContentModel().isSkippable()) {
                                state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$SC_AND_MIXED_EMPTIABLE, (Object[]) null, location);
                                break;
                            }
                            break;
                        default:
                            state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$SC_AND_SIMPLE_TYPE_OR_MIXED, (Object[]) null, location);
                            break;
                    }
                    return false;
                case 3:
                    break;
                case 4:
                    if (baseType.getContentType() != 4) {
                        state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$ELEMENT_OR_MIXED_AND_MIXED, (Object[]) null, location);
                        return false;
                    }
                    break;
                default:
                    return true;
            }
            if (baseType.getContentType() == 1) {
                state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$ELEMENT_OR_MIXED_AND_EMPTY, (Object[]) null, location);
                return false;
            }
            if (baseType.getContentType() == 2) {
                state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$ELEMENT_OR_MIXED_AND_SIMPLE, (Object[]) null, location);
                return false;
            }
            SchemaParticle baseModel = baseType.getContentModel();
            SchemaParticle derivedModel = sType.getContentModel();
            if (derivedModel == null && sType.getDerivationType() == 1) {
                return true;
            }
            if (baseModel == null || derivedModel == null) {
                XBeanDebug.logStackTrace("Null models that weren't caught by EMPTY_CONTENT: " + baseType + " (" + baseModel + "), " + sType + " (" + derivedModel + ")");
                state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$ELEMENT_OR_MIXED_AND_VALID, (Object[]) null, location);
                return false;
            }
            List errors = new ArrayList();
            boolean isValid = isParticleValidRestriction(baseModel, derivedModel, errors, location);
            if (!isValid) {
                if (errors.size() == 0) {
                    state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$ELEMENT_OR_MIXED_AND_VALID, (Object[]) null, location);
                    return false;
                }
                state.getErrorListener().add(errors.get(errors.size() - 1));
                return false;
            }
            return true;
        }
        return true;
    }

    public static boolean isParticleValidRestriction(SchemaParticle baseModel, SchemaParticle derivedModel, Collection errors, XmlObject context) {
        boolean restrictionValid = false;
        if (baseModel.equals(derivedModel)) {
            restrictionValid = true;
        } else {
            switch (baseModel.getParticleType()) {
                case 1:
                    switch (derivedModel.getParticleType()) {
                        case 1:
                            restrictionValid = recurse(baseModel, derivedModel, errors, context);
                            break;
                        case 2:
                        case 5:
                            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION$INVALID_RESTRICTION, new Object[]{printParticle(derivedModel), printParticle(baseModel)}, context));
                            restrictionValid = false;
                            break;
                        case 3:
                            restrictionValid = recurseUnordered(baseModel, derivedModel, errors, context);
                            break;
                        case 4:
                            restrictionValid = recurseAsIfGroup(baseModel, derivedModel, errors, context);
                            break;
                        default:
                            if (!$assertionsDisabled) {
                                throw new AssertionError(XBeanDebug.logStackTrace("Unknown schema type for Derived Type"));
                            }
                            break;
                    }
                case 2:
                    switch (derivedModel.getParticleType()) {
                        case 1:
                        case 5:
                            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION$INVALID_RESTRICTION, new Object[]{printParticle(derivedModel), printParticle(baseModel)}, context));
                            restrictionValid = false;
                            break;
                        case 2:
                            restrictionValid = recurseLax(baseModel, derivedModel, errors, context);
                            break;
                        case 3:
                            restrictionValid = mapAndSum(baseModel, derivedModel, errors, context);
                            break;
                        case 4:
                            restrictionValid = recurseAsIfGroup(baseModel, derivedModel, errors, context);
                            break;
                        default:
                            if (!$assertionsDisabled) {
                                throw new AssertionError(XBeanDebug.logStackTrace("Unknown schema type for Derived Type"));
                            }
                            break;
                    }
                case 3:
                    switch (derivedModel.getParticleType()) {
                        case 1:
                        case 2:
                        case 5:
                            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION$INVALID_RESTRICTION, new Object[]{printParticle(derivedModel), printParticle(baseModel)}, context));
                            restrictionValid = false;
                            break;
                        case 3:
                            restrictionValid = recurse(baseModel, derivedModel, errors, context);
                            break;
                        case 4:
                            restrictionValid = recurseAsIfGroup(baseModel, derivedModel, errors, context);
                            break;
                        default:
                            if (!$assertionsDisabled) {
                                throw new AssertionError(XBeanDebug.logStackTrace("Unknown schema type for Derived Type"));
                            }
                            break;
                    }
                case 4:
                    switch (derivedModel.getParticleType()) {
                        case 1:
                        case 2:
                        case 3:
                        case 5:
                            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION$INVALID_RESTRICTION, new Object[]{printParticle(derivedModel), printParticle(baseModel)}, context));
                            restrictionValid = false;
                            break;
                        case 4:
                            restrictionValid = nameAndTypeOK((SchemaLocalElement) baseModel, (SchemaLocalElement) derivedModel, errors, context);
                            break;
                        default:
                            if (!$assertionsDisabled) {
                                throw new AssertionError(XBeanDebug.logStackTrace("Unknown schema type for Derived Type"));
                            }
                            break;
                    }
                case 5:
                    switch (derivedModel.getParticleType()) {
                        case 1:
                            restrictionValid = nsRecurseCheckCardinality(baseModel, derivedModel, errors, context);
                            break;
                        case 2:
                            restrictionValid = nsRecurseCheckCardinality(baseModel, derivedModel, errors, context);
                            break;
                        case 3:
                            restrictionValid = nsRecurseCheckCardinality(baseModel, derivedModel, errors, context);
                            break;
                        case 4:
                            restrictionValid = nsCompat(baseModel, (SchemaLocalElement) derivedModel, errors, context);
                            break;
                        case 5:
                            restrictionValid = nsSubset(baseModel, derivedModel, errors, context);
                            break;
                        default:
                            if (!$assertionsDisabled) {
                                throw new AssertionError(XBeanDebug.logStackTrace("Unknown schema type for Derived Type"));
                            }
                            break;
                    }
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError(XBeanDebug.logStackTrace("Unknown schema type for Base Type"));
                    }
                    break;
            }
        }
        return restrictionValid;
    }

    private static boolean mapAndSum(SchemaParticle baseModel, SchemaParticle derivedModel, Collection errors, XmlObject context) {
        BigInteger derivedRangeMax;
        if (!$assertionsDisabled && baseModel.getParticleType() != 2) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && derivedModel.getParticleType() != 3) {
            throw new AssertionError();
        }
        boolean mapAndSumValid = true;
        SchemaParticle[] derivedParticleArray = derivedModel.getParticleChildren();
        SchemaParticle[] baseParticleArray = baseModel.getParticleChildren();
        for (SchemaParticle derivedParticle : derivedParticleArray) {
            boolean foundMatch = false;
            int j = 0;
            while (true) {
                if (j >= baseParticleArray.length) {
                    break;
                }
                SchemaParticle baseParticle = baseParticleArray[j];
                if (!isParticleValidRestriction(baseParticle, derivedParticle, errors, context)) {
                    j++;
                } else {
                    foundMatch = true;
                    break;
                }
            }
            if (!foundMatch) {
                errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_MAP_AND_SUM$MAP, new Object[]{printParticle(derivedParticle)}, context));
                return false;
            }
        }
        BigInteger derivedRangeMin = derivedModel.getMinOccurs().multiply(BigInteger.valueOf(derivedModel.getParticleChildren().length));
        if (derivedModel.getMaxOccurs() == null) {
            derivedRangeMax = null;
        } else {
            derivedRangeMax = derivedModel.getMaxOccurs().multiply(BigInteger.valueOf(derivedModel.getParticleChildren().length));
        }
        if (derivedRangeMin.compareTo(baseModel.getMinOccurs()) >= 0) {
            if (baseModel.getMaxOccurs() != null && (derivedRangeMax == null || derivedRangeMax.compareTo(baseModel.getMaxOccurs()) > 0)) {
                mapAndSumValid = false;
                Object[] objArr = new Object[2];
                objArr[0] = derivedRangeMax == null ? "unbounded" : derivedRangeMax.toString();
                objArr[1] = baseModel.getMaxOccurs().toString();
                errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_MAP_AND_SUM$SUM_MAX_OCCURS_LTE_MAX_OCCURS, objArr, context));
            }
        } else {
            mapAndSumValid = false;
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_MAP_AND_SUM$SUM_MIN_OCCURS_GTE_MIN_OCCURS, new Object[]{derivedRangeMin.toString(), baseModel.getMinOccurs().toString()}, context));
        }
        return mapAndSumValid;
    }

    private static boolean recurseAsIfGroup(SchemaParticle baseModel, SchemaParticle derivedModel, Collection errors, XmlObject context) {
        if (!$assertionsDisabled && ((baseModel.getParticleType() != 1 || derivedModel.getParticleType() != 4) && ((baseModel.getParticleType() != 2 || derivedModel.getParticleType() != 4) && (baseModel.getParticleType() != 3 || derivedModel.getParticleType() != 4)))) {
            throw new AssertionError();
        }
        SchemaParticleImpl asIfPart = new SchemaParticleImpl();
        asIfPart.setParticleType(baseModel.getParticleType());
        asIfPart.setMinOccurs(BigInteger.ONE);
        asIfPart.setMaxOccurs(BigInteger.ONE);
        asIfPart.setParticleChildren(new SchemaParticle[]{derivedModel});
        return isParticleValidRestriction(baseModel, asIfPart, errors, context);
    }

    private static boolean recurseLax(SchemaParticle baseModel, SchemaParticle derivedModel, Collection errors, XmlObject context) {
        if (!$assertionsDisabled && (baseModel.getParticleType() != 2 || derivedModel.getParticleType() != 2)) {
            throw new AssertionError();
        }
        boolean recurseLaxValid = true;
        if (!occurrenceRangeOK(baseModel, derivedModel, errors, context)) {
            return false;
        }
        SchemaParticle[] derivedParticleArray = derivedModel.getParticleChildren();
        SchemaParticle[] baseParticleArray = baseModel.getParticleChildren();
        int i = 0;
        int j = 0;
        while (i < derivedParticleArray.length && j < baseParticleArray.length) {
            SchemaParticle derivedParticle = derivedParticleArray[i];
            SchemaParticle baseParticle = baseParticleArray[j];
            if (isParticleValidRestriction(baseParticle, derivedParticle, errors, context)) {
                i++;
                j++;
            } else {
                j++;
            }
        }
        if (i < derivedParticleArray.length) {
            recurseLaxValid = false;
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_RECURSE_LAX$MAP, new Object[]{printParticles(baseParticleArray, i)}, context));
        }
        return recurseLaxValid;
    }

    private static boolean recurseUnordered(SchemaParticle baseModel, SchemaParticle derivedModel, Collection errors, XmlObject context) {
        if (!$assertionsDisabled && (baseModel.getParticleType() != 1 || derivedModel.getParticleType() != 3)) {
            throw new AssertionError();
        }
        boolean recurseUnorderedValid = true;
        if (!occurrenceRangeOK(baseModel, derivedModel, errors, context)) {
            return false;
        }
        SchemaParticle[] baseParticles = baseModel.getParticleChildren();
        HashMap baseParticleMap = new HashMap(10);
        Object MAPPED = new Object();
        for (int i = 0; i < baseParticles.length; i++) {
            baseParticleMap.put(baseParticles[i].getName(), baseParticles[i]);
        }
        SchemaParticle[] derivedParticles = derivedModel.getParticleChildren();
        int i2 = 0;
        while (true) {
            if (i2 >= derivedParticles.length) {
                break;
            }
            Object baseParticle = baseParticleMap.get(derivedParticles[i2].getName());
            if (baseParticle == null) {
                recurseUnorderedValid = false;
                errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_RECURSE_UNORDERED$MAP, new Object[]{printParticle(derivedParticles[i2])}, context));
                break;
            }
            if (baseParticle == MAPPED) {
                recurseUnorderedValid = false;
                errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_RECURSE_UNORDERED$MAP_UNIQUE, new Object[]{printParticle(derivedParticles[i2])}, context));
                break;
            }
            SchemaParticle matchedBaseParticle = (SchemaParticle) baseParticle;
            if (derivedParticles[i2].getMaxOccurs() == null || derivedParticles[i2].getMaxOccurs().compareTo(BigInteger.ONE) > 0) {
                break;
            }
            if (!isParticleValidRestriction(matchedBaseParticle, derivedParticles[i2], errors, context)) {
                recurseUnorderedValid = false;
                break;
            }
            baseParticleMap.put(derivedParticles[i2].getName(), MAPPED);
            i2++;
        }
        recurseUnorderedValid = false;
        errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_RECURSE_UNORDERED$MAP_MAX_OCCURS_1, new Object[]{printParticle(derivedParticles[i2]), printMaxOccurs(derivedParticles[i2].getMinOccurs())}, context));
        if (recurseUnorderedValid) {
            Set<QName> baseParticleCollection = baseParticleMap.keySet();
            for (QName baseParticleQName : baseParticleCollection) {
                if (baseParticleMap.get(baseParticleQName) != MAPPED && !((SchemaParticle) baseParticleMap.get(baseParticleQName)).isSkippable()) {
                    recurseUnorderedValid = false;
                    errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_RECURSE_UNORDERED$UNMAPPED_ARE_EMPTIABLE, new Object[]{printParticle((SchemaParticle) baseParticleMap.get(baseParticleQName))}, context));
                }
            }
        }
        return recurseUnorderedValid;
    }

    private static boolean recurse(SchemaParticle baseModel, SchemaParticle derivedModel, Collection errors, XmlObject context) {
        boolean recurseValid = true;
        if (!occurrenceRangeOK(baseModel, derivedModel, errors, context)) {
            return false;
        }
        SchemaParticle[] derivedParticleArray = derivedModel.getParticleChildren();
        SchemaParticle[] baseParticleArray = baseModel.getParticleChildren();
        int i = 0;
        int j = 0;
        while (true) {
            if (i >= derivedParticleArray.length || j >= baseParticleArray.length) {
                break;
            }
            SchemaParticle derivedParticle = derivedParticleArray[i];
            SchemaParticle baseParticle = baseParticleArray[j];
            if (isParticleValidRestriction(baseParticle, derivedParticle, errors, context)) {
                i++;
                j++;
            } else if (baseParticle.isSkippable()) {
                j++;
            } else {
                recurseValid = false;
                errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_RECURSE$MAP_VALID, new Object[]{printParticle(derivedParticle), printParticle(derivedModel), printParticle(baseParticle), printParticle(baseModel)}, context));
                break;
            }
        }
        if (i < derivedParticleArray.length) {
            recurseValid = false;
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_RECURSE$MAP, new Object[]{printParticle(derivedModel), printParticle(baseModel), printParticles(derivedParticleArray, i)}, context));
        } else if (j < baseParticleArray.length) {
            ArrayList particles = new ArrayList(baseParticleArray.length);
            for (int k = j; k < baseParticleArray.length; k++) {
                if (!baseParticleArray[k].isSkippable()) {
                    particles.add(baseParticleArray[k]);
                }
            }
            if (particles.size() > 0) {
                recurseValid = false;
                errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_RECURSE$UNMAPPED_ARE_EMPTIABLE, new Object[]{printParticle(baseModel), printParticle(derivedModel), printParticles(particles)}, context));
            }
        }
        return recurseValid;
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x0119  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean nsRecurseCheckCardinality(org.apache.xmlbeans.SchemaParticle r5, org.apache.xmlbeans.SchemaParticle r6, java.util.Collection r7, org.apache.xmlbeans.XmlObject r8) {
        /*
            Method dump skipped, instructions count: 293
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.schema.StscChecker.nsRecurseCheckCardinality(org.apache.xmlbeans.SchemaParticle, org.apache.xmlbeans.SchemaParticle, java.util.Collection, org.apache.xmlbeans.XmlObject):boolean");
    }

    private static boolean checkGroupOccurrenceOK(SchemaParticle baseModel, SchemaParticle derivedModel, Collection errors, XmlObject context) {
        boolean groupOccurrenceOK = true;
        BigInteger minRange = BigInteger.ZERO;
        BigInteger maxRange = BigInteger.ZERO;
        switch (derivedModel.getParticleType()) {
            case 1:
            case 3:
                minRange = getEffectiveMinRangeAllSeq(derivedModel);
                maxRange = getEffectiveMaxRangeAllSeq(derivedModel);
                break;
            case 2:
                minRange = getEffectiveMinRangeChoice(derivedModel);
                maxRange = getEffectiveMaxRangeChoice(derivedModel);
                break;
        }
        if (minRange.compareTo(baseModel.getMinOccurs()) < 0) {
            groupOccurrenceOK = false;
            errors.add(XmlError.forObject(XmlErrorCodes.OCCURRENCE_RANGE$MIN_GTE_MIN, new Object[]{printParticle(derivedModel), printParticle(baseModel)}, context));
        }
        if (baseModel.getMaxOccurs() != null && (maxRange == null || maxRange.compareTo(baseModel.getMaxOccurs()) > 0)) {
            groupOccurrenceOK = false;
            errors.add(XmlError.forObject(XmlErrorCodes.OCCURRENCE_RANGE$MAX_LTE_MAX, new Object[]{printParticle(derivedModel), printParticle(baseModel)}, context));
        }
        return groupOccurrenceOK;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00cf  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.math.BigInteger getEffectiveMaxRangeChoice(org.apache.xmlbeans.SchemaParticle r4) {
        /*
            java.math.BigInteger r0 = java.math.BigInteger.ZERO
            r5 = r0
            r0 = 0
            r6 = r0
            r0 = 0
            r7 = r0
            java.math.BigInteger r0 = java.math.BigInteger.ZERO
            r8 = r0
            java.math.BigInteger r0 = java.math.BigInteger.ZERO
            r9 = r0
            r0 = r4
            org.apache.xmlbeans.SchemaParticle[] r0 = r0.getParticleChildren()
            r10 = r0
            r0 = 0
            r11 = r0
        L1d:
            r0 = r11
            r1 = r10
            int r1 = r1.length
            if (r0 >= r1) goto Lca
            r0 = r10
            r1 = r11
            r0 = r0[r1]
            r12 = r0
            r0 = r12
            int r0 = r0.getParticleType()
            switch(r0) {
                case 1: goto L8b;
                case 2: goto La5;
                case 3: goto L8b;
                case 4: goto L54;
                case 5: goto L54;
                default: goto Lbc;
            }
        L54:
            r0 = r12
            java.math.BigInteger r0 = r0.getMaxOccurs()
            r1 = r6
            if (r0 != r1) goto L64
            r0 = r6
            r5 = r0
            goto Lbc
        L64:
            r0 = r12
            int r0 = r0.getIntMaxOccurs()
            if (r0 <= 0) goto Lbc
            r0 = 1
            r7 = r0
            r0 = r12
            java.math.BigInteger r0 = r0.getMaxOccurs()
            r1 = r8
            int r0 = r0.compareTo(r1)
            if (r0 <= 0) goto Lbc
            r0 = r12
            java.math.BigInteger r0 = r0.getMaxOccurs()
            r8 = r0
            goto Lbc
        L8b:
            r0 = r12
            java.math.BigInteger r0 = getEffectiveMaxRangeAllSeq(r0)
            r5 = r0
            r0 = r5
            r1 = r6
            if (r0 == r1) goto Lbc
            r0 = r5
            r1 = r9
            int r0 = r0.compareTo(r1)
            if (r0 <= 0) goto Lbc
            r0 = r5
            r9 = r0
            goto Lbc
        La5:
            r0 = r12
            java.math.BigInteger r0 = getEffectiveMaxRangeChoice(r0)
            r5 = r0
            r0 = r5
            r1 = r6
            if (r0 == r1) goto Lbc
            r0 = r5
            r1 = r9
            int r0 = r0.compareTo(r1)
            if (r0 <= 0) goto Lbc
            r0 = r5
            r9 = r0
        Lbc:
            r0 = r5
            r1 = r6
            if (r0 != r1) goto Lc4
            goto Lca
        Lc4:
            int r11 = r11 + 1
            goto L1d
        Lca:
            r0 = r5
            r1 = r6
            if (r0 == r1) goto Lf3
            r0 = r7
            if (r0 == 0) goto Le2
            r0 = r4
            java.math.BigInteger r0 = r0.getMaxOccurs()
            r1 = r6
            if (r0 != r1) goto Le2
            r0 = r6
            r5 = r0
            goto Lf3
        Le2:
            r0 = r4
            java.math.BigInteger r0 = r0.getMaxOccurs()
            r1 = r8
            r2 = r9
            java.math.BigInteger r1 = r1.add(r2)
            java.math.BigInteger r0 = r0.multiply(r1)
            r5 = r0
        Lf3:
            r0 = r5
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.schema.StscChecker.getEffectiveMaxRangeChoice(org.apache.xmlbeans.SchemaParticle):java.math.BigInteger");
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x00c5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.math.BigInteger getEffectiveMaxRangeAllSeq(org.apache.xmlbeans.SchemaParticle r4) {
        /*
            java.math.BigInteger r0 = java.math.BigInteger.ZERO
            r5 = r0
            r0 = 0
            r6 = r0
            r0 = 0
            r7 = r0
            java.math.BigInteger r0 = java.math.BigInteger.ZERO
            r8 = r0
            java.math.BigInteger r0 = java.math.BigInteger.ZERO
            r9 = r0
            r0 = r4
            org.apache.xmlbeans.SchemaParticle[] r0 = r0.getParticleChildren()
            r10 = r0
            r0 = 0
            r11 = r0
        L1d:
            r0 = r11
            r1 = r10
            int r1 = r1.length
            if (r0 >= r1) goto Lc0
            r0 = r10
            r1 = r11
            r0 = r0[r1]
            r12 = r0
            r0 = r12
            int r0 = r0.getParticleType()
            switch(r0) {
                case 1: goto L81;
                case 2: goto L9b;
                case 3: goto L81;
                case 4: goto L54;
                case 5: goto L54;
                default: goto Lb2;
            }
        L54:
            r0 = r12
            java.math.BigInteger r0 = r0.getMaxOccurs()
            r1 = r6
            if (r0 != r1) goto L64
            r0 = r6
            r5 = r0
            goto Lb2
        L64:
            r0 = r12
            int r0 = r0.getIntMaxOccurs()
            if (r0 <= 0) goto Lb2
            r0 = 1
            r7 = r0
            r0 = r8
            r1 = r12
            java.math.BigInteger r1 = r1.getMaxOccurs()
            java.math.BigInteger r0 = r0.add(r1)
            r8 = r0
            goto Lb2
        L81:
            r0 = r12
            java.math.BigInteger r0 = getEffectiveMaxRangeAllSeq(r0)
            r5 = r0
            r0 = r5
            r1 = r6
            if (r0 == r1) goto Lb2
            r0 = r5
            r1 = r9
            int r0 = r0.compareTo(r1)
            if (r0 <= 0) goto Lb2
            r0 = r5
            r9 = r0
            goto Lb2
        L9b:
            r0 = r12
            java.math.BigInteger r0 = getEffectiveMaxRangeChoice(r0)
            r5 = r0
            r0 = r5
            r1 = r6
            if (r0 == r1) goto Lb2
            r0 = r5
            r1 = r9
            int r0 = r0.compareTo(r1)
            if (r0 <= 0) goto Lb2
            r0 = r5
            r9 = r0
        Lb2:
            r0 = r5
            r1 = r6
            if (r0 != r1) goto Lba
            goto Lc0
        Lba:
            int r11 = r11 + 1
            goto L1d
        Lc0:
            r0 = r5
            r1 = r6
            if (r0 == r1) goto Le9
            r0 = r7
            if (r0 == 0) goto Ld8
            r0 = r4
            java.math.BigInteger r0 = r0.getMaxOccurs()
            r1 = r6
            if (r0 != r1) goto Ld8
            r0 = r6
            r5 = r0
            goto Le9
        Ld8:
            r0 = r4
            java.math.BigInteger r0 = r0.getMaxOccurs()
            r1 = r8
            r2 = r9
            java.math.BigInteger r1 = r1.add(r2)
            java.math.BigInteger r0 = r0.multiply(r1)
            r5 = r0
        Le9:
            r0 = r5
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.schema.StscChecker.getEffectiveMaxRangeAllSeq(org.apache.xmlbeans.SchemaParticle):java.math.BigInteger");
    }

    private static BigInteger getEffectiveMinRangeChoice(SchemaParticle derivedModel) {
        SchemaParticle[] particleChildren = derivedModel.getParticleChildren();
        if (particleChildren.length == 0) {
            return BigInteger.ZERO;
        }
        BigInteger minRange = null;
        for (SchemaParticle particle : particleChildren) {
            switch (particle.getParticleType()) {
                case 1:
                case 3:
                    BigInteger mrs = getEffectiveMinRangeAllSeq(particle);
                    if (minRange == null || minRange.compareTo(mrs) > 0) {
                        minRange = mrs;
                        break;
                    } else {
                        break;
                    }
                    break;
                case 2:
                    BigInteger mrc = getEffectiveMinRangeChoice(particle);
                    if (minRange == null || minRange.compareTo(mrc) > 0) {
                        minRange = mrc;
                        break;
                    } else {
                        break;
                    }
                    break;
                case 4:
                case 5:
                    if (minRange == null || minRange.compareTo(particle.getMinOccurs()) > 0) {
                        minRange = particle.getMinOccurs();
                        break;
                    } else {
                        break;
                    }
            }
        }
        if (minRange == null) {
            minRange = BigInteger.ZERO;
        }
        return derivedModel.getMinOccurs().multiply(minRange);
    }

    private static BigInteger getEffectiveMinRangeAllSeq(SchemaParticle derivedModel) {
        BigInteger bigInteger = BigInteger.ZERO;
        SchemaParticle[] particleChildren = derivedModel.getParticleChildren();
        BigInteger particleTotalMinOccurs = BigInteger.ZERO;
        for (SchemaParticle particle : particleChildren) {
            switch (particle.getParticleType()) {
                case 1:
                case 3:
                    particleTotalMinOccurs = particleTotalMinOccurs.add(getEffectiveMinRangeAllSeq(particle));
                    break;
                case 2:
                    particleTotalMinOccurs = particleTotalMinOccurs.add(getEffectiveMinRangeChoice(particle));
                    break;
                case 4:
                case 5:
                    particleTotalMinOccurs = particleTotalMinOccurs.add(particle.getMinOccurs());
                    break;
            }
        }
        BigInteger minRange = derivedModel.getMinOccurs().multiply(particleTotalMinOccurs);
        return minRange;
    }

    private static boolean nsSubset(SchemaParticle baseModel, SchemaParticle derivedModel, Collection errors, XmlObject context) {
        boolean nsSubset;
        if (!$assertionsDisabled && baseModel.getParticleType() != 5) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && derivedModel.getParticleType() != 5) {
            throw new AssertionError();
        }
        if (occurrenceRangeOK(baseModel, derivedModel, errors, context)) {
            if (baseModel.getWildcardSet().inverse().isDisjoint(derivedModel.getWildcardSet())) {
                nsSubset = true;
            } else {
                nsSubset = false;
                errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_NS_SUBST$WILDCARD_SUBSET, new Object[]{printParticle(derivedModel), printParticle(baseModel)}, context));
            }
        } else {
            nsSubset = false;
        }
        return nsSubset;
    }

    private static boolean nsCompat(SchemaParticle baseModel, SchemaLocalElement derivedElement, Collection errors, XmlObject context) {
        if (!$assertionsDisabled && baseModel.getParticleType() != 5) {
            throw new AssertionError();
        }
        boolean nsCompat = false;
        if (baseModel.getWildcardSet().contains(derivedElement.getName())) {
            if (occurrenceRangeOK(baseModel, (SchemaParticle) derivedElement, errors, context)) {
                nsCompat = true;
            }
        } else {
            nsCompat = false;
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_NS_COMPAT$WILDCARD_VALID, new Object[]{printParticle((SchemaParticle) derivedElement), printParticle(baseModel)}, context));
        }
        return nsCompat;
    }

    private static boolean nameAndTypeOK(SchemaLocalElement baseElement, SchemaLocalElement derivedElement, Collection errors, XmlObject context) {
        if (!((SchemaParticle) baseElement).canStartWithElement(derivedElement.getName())) {
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION_NAME_AND_TYPE$NAME, new Object[]{printParticle((SchemaParticle) derivedElement), printParticle((SchemaParticle) baseElement)}, context));
            return false;
        }
        if (!baseElement.isNillable() && derivedElement.isNillable()) {
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION_NAME_AND_TYPE$NILLABLE, new Object[]{printParticle((SchemaParticle) derivedElement), printParticle((SchemaParticle) baseElement)}, context));
            return false;
        }
        if (!occurrenceRangeOK((SchemaParticle) baseElement, (SchemaParticle) derivedElement, errors, context) || !checkFixed(baseElement, derivedElement, errors, context) || !checkIdentityConstraints(baseElement, derivedElement, errors, context) || !typeDerivationOK(baseElement.getType(), derivedElement.getType(), errors, context) || !blockSetOK(baseElement, derivedElement, errors, context)) {
            return false;
        }
        return true;
    }

    private static boolean blockSetOK(SchemaLocalElement baseElement, SchemaLocalElement derivedElement, Collection errors, XmlObject context) {
        if (baseElement.blockRestriction() && !derivedElement.blockRestriction()) {
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION_NAME_AND_TYPE$DISALLOWED_SUBSTITUTIONS, new Object[]{printParticle((SchemaParticle) derivedElement), "restriction", printParticle((SchemaParticle) baseElement)}, context));
            return false;
        }
        if (baseElement.blockExtension() && !derivedElement.blockExtension()) {
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION_NAME_AND_TYPE$DISALLOWED_SUBSTITUTIONS, new Object[]{printParticle((SchemaParticle) derivedElement), "extension", printParticle((SchemaParticle) baseElement)}, context));
            return false;
        }
        if (baseElement.blockSubstitution() && !derivedElement.blockSubstitution()) {
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION_NAME_AND_TYPE$DISALLOWED_SUBSTITUTIONS, new Object[]{printParticle((SchemaParticle) derivedElement), "substitution", printParticle((SchemaParticle) baseElement)}, context));
            return false;
        }
        return true;
    }

    private static boolean typeDerivationOK(SchemaType baseType, SchemaType derivedType, Collection errors, XmlObject context) {
        boolean typeDerivationOK;
        if (baseType.isAssignableFrom(derivedType)) {
            typeDerivationOK = checkAllDerivationsForRestriction(baseType, derivedType, errors, context);
        } else {
            typeDerivationOK = false;
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION_NAME_AND_TYPE$TYPE_VALID, new Object[]{printType(derivedType), printType(baseType)}, context));
        }
        return typeDerivationOK;
    }

    private static boolean checkAllDerivationsForRestriction(SchemaType baseType, SchemaType derivedType, Collection errors, XmlObject context) {
        boolean allDerivationsAreRestrictions = true;
        SchemaType currentType = derivedType;
        Set possibleTypes = null;
        if (baseType.getSimpleVariety() == 2) {
            possibleTypes = new HashSet(Arrays.asList(baseType.getUnionConstituentTypes()));
        }
        while (true) {
            if (!baseType.equals(currentType) && possibleTypes != null && !possibleTypes.contains(currentType)) {
                if (currentType.getDerivationType() == 1) {
                    currentType = currentType.getBaseType();
                } else {
                    allDerivationsAreRestrictions = false;
                    errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION_NAME_AND_TYPE$TYPE_RESTRICTED, new Object[]{printType(derivedType), printType(baseType), printType(currentType)}, context));
                    break;
                }
            } else {
                break;
            }
        }
        return allDerivationsAreRestrictions;
    }

    private static boolean checkIdentityConstraints(SchemaLocalElement baseElement, SchemaLocalElement derivedElement, Collection errors, XmlObject context) {
        boolean identityConstraintsOK = true;
        SchemaIdentityConstraint[] baseConstraints = baseElement.getIdentityConstraints();
        SchemaIdentityConstraint[] derivedConstraints = derivedElement.getIdentityConstraints();
        int i = 0;
        while (true) {
            if (i >= derivedConstraints.length) {
                break;
            }
            SchemaIdentityConstraint derivedConstraint = derivedConstraints[i];
            if (!checkForIdentityConstraintExistence(baseConstraints, derivedConstraint)) {
                i++;
            } else {
                identityConstraintsOK = false;
                errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION_NAME_AND_TYPE$IDENTITY_CONSTRAINTS, new Object[]{printParticle((SchemaParticle) derivedElement), printParticle((SchemaParticle) baseElement)}, context));
                break;
            }
        }
        return identityConstraintsOK;
    }

    private static boolean checkForIdentityConstraintExistence(SchemaIdentityConstraint[] baseConstraints, SchemaIdentityConstraint derivedConstraint) {
        boolean identityConstraintExists = false;
        int i = 0;
        while (true) {
            if (i >= baseConstraints.length) {
                break;
            }
            SchemaIdentityConstraint baseConstraint = baseConstraints[i];
            if (!baseConstraint.getName().equals(derivedConstraint.getName())) {
                i++;
            } else {
                identityConstraintExists = true;
                break;
            }
        }
        return identityConstraintExists;
    }

    private static boolean checkFixed(SchemaLocalElement baseModel, SchemaLocalElement derivedModel, Collection errors, XmlObject context) {
        boolean checkFixed;
        if (!baseModel.isFixed() || baseModel.getDefaultText().equals(derivedModel.getDefaultText())) {
            checkFixed = true;
        } else {
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION_NAME_AND_TYPE$FIXED, new Object[]{printParticle((SchemaParticle) derivedModel), derivedModel.getDefaultText(), printParticle((SchemaParticle) baseModel), baseModel.getDefaultText()}, context));
            checkFixed = false;
        }
        return checkFixed;
    }

    private static boolean occurrenceRangeOK(SchemaParticle baseParticle, SchemaParticle derivedParticle, Collection errors, XmlObject context) {
        boolean occurrenceRangeOK;
        if (derivedParticle.getMinOccurs().compareTo(baseParticle.getMinOccurs()) >= 0) {
            if (baseParticle.getMaxOccurs() == null) {
                occurrenceRangeOK = true;
            } else if (derivedParticle.getMaxOccurs() != null && baseParticle.getMaxOccurs() != null && derivedParticle.getMaxOccurs().compareTo(baseParticle.getMaxOccurs()) <= 0) {
                occurrenceRangeOK = true;
            } else {
                occurrenceRangeOK = false;
                errors.add(XmlError.forObject(XmlErrorCodes.OCCURRENCE_RANGE$MAX_LTE_MAX, new Object[]{printParticle(derivedParticle), printMaxOccurs(derivedParticle.getMaxOccurs()), printParticle(baseParticle), printMaxOccurs(baseParticle.getMaxOccurs())}, context));
            }
        } else {
            occurrenceRangeOK = false;
            errors.add(XmlError.forObject(XmlErrorCodes.OCCURRENCE_RANGE$MIN_GTE_MIN, new Object[]{printParticle(derivedParticle), derivedParticle.getMinOccurs().toString(), printParticle(baseParticle), baseParticle.getMinOccurs().toString()}, context));
        }
        return occurrenceRangeOK;
    }

    private static String printParticles(List parts) {
        return printParticles((SchemaParticle[]) parts.toArray(new SchemaParticle[parts.size()]));
    }

    private static String printParticles(SchemaParticle[] parts) {
        return printParticles(parts, 0, parts.length);
    }

    private static String printParticles(SchemaParticle[] parts, int start) {
        return printParticles(parts, start, parts.length);
    }

    private static String printParticles(SchemaParticle[] parts, int start, int end) {
        StringBuffer buf = new StringBuffer(parts.length * 30);
        int i = start;
        while (i < end) {
            buf.append(printParticle(parts[i]));
            i++;
            if (i != end) {
                buf.append(", ");
            }
        }
        return buf.toString();
    }

    private static String printParticle(SchemaParticle part) {
        switch (part.getParticleType()) {
            case 1:
                return "<all>";
            case 2:
                return "<choice>";
            case 3:
                return "<sequence>";
            case 4:
                return "<element name=\"" + QNameHelper.pretty(part.getName()) + "\">";
            case 5:
                return "<any>";
            default:
                return "??";
        }
    }

    private static String printMaxOccurs(BigInteger bi) {
        if (bi == null) {
            return "unbounded";
        }
        return bi.toString();
    }

    private static String printType(SchemaType type) {
        if (type.getName() != null) {
            return QNameHelper.pretty(type.getName());
        }
        return type.toString();
    }

    private static void checkSubstitutionGroups(SchemaGlobalElement[] elts) {
        StscState state = StscState.get();
        for (SchemaGlobalElement elt : elts) {
            SchemaGlobalElement head = elt.substitutionGroup();
            if (head != null) {
                SchemaType headType = head.getType();
                SchemaType tailType = elt.getType();
                XmlObject parseTree = ((SchemaGlobalElementImpl) elt)._parseObject;
                if (!headType.isAssignableFrom(tailType)) {
                    state.error(XmlErrorCodes.ELEM_PROPERTIES$SUBSTITUTION_VALID, new Object[]{QNameHelper.pretty(elt.getName()), QNameHelper.pretty(head.getName())}, parseTree);
                } else if (head.finalExtension() && head.finalRestriction()) {
                    state.error(XmlErrorCodes.ELEM_PROPERTIES$SUBSTITUTION_FINAL, new Object[]{QNameHelper.pretty(elt.getName()), QNameHelper.pretty(head.getName()), "#all"}, parseTree);
                } else if (!headType.equals(tailType)) {
                    if (head.finalExtension() && tailType.getDerivationType() == 2) {
                        state.error(XmlErrorCodes.ELEM_PROPERTIES$SUBSTITUTION_FINAL, new Object[]{QNameHelper.pretty(elt.getName()), QNameHelper.pretty(head.getName()), "extension"}, parseTree);
                    } else if (head.finalRestriction() && tailType.getDerivationType() == 1) {
                        state.error(XmlErrorCodes.ELEM_PROPERTIES$SUBSTITUTION_FINAL, new Object[]{QNameHelper.pretty(elt.getName()), QNameHelper.pretty(head.getName()), "restriction"}, parseTree);
                    }
                }
            }
        }
    }
}
