package org.apache.xmlbeans.impl.validator;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.GDate;
import org.apache.xmlbeans.GDuration;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.SchemaAttributeModel;
import org.apache.xmlbeans.SchemaField;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaLocalAttribute;
import org.apache.xmlbeans.SchemaLocalElement;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlValidationError;
import org.apache.xmlbeans.impl.common.IdentityConstraint;
import org.apache.xmlbeans.impl.common.InvalidLexicalValueException;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidationContext;
import org.apache.xmlbeans.impl.common.ValidatorListener;
import org.apache.xmlbeans.impl.common.XmlWhitespace;
import org.apache.xmlbeans.impl.schema.SchemaTypeImpl;
import org.apache.xmlbeans.impl.schema.SchemaTypeVisitorImpl;
import org.apache.xmlbeans.impl.util.XsTypeConverter;
import org.apache.xmlbeans.impl.values.JavaBase64HolderEx;
import org.apache.xmlbeans.impl.values.JavaBooleanHolder;
import org.apache.xmlbeans.impl.values.JavaBooleanHolderEx;
import org.apache.xmlbeans.impl.values.JavaDecimalHolderEx;
import org.apache.xmlbeans.impl.values.JavaDoubleHolderEx;
import org.apache.xmlbeans.impl.values.JavaFloatHolderEx;
import org.apache.xmlbeans.impl.values.JavaHexBinaryHolderEx;
import org.apache.xmlbeans.impl.values.JavaNotationHolderEx;
import org.apache.xmlbeans.impl.values.JavaQNameHolderEx;
import org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx;
import org.apache.xmlbeans.impl.values.JavaUriHolderEx;
import org.apache.xmlbeans.impl.values.NamespaceContext;
import org.apache.xmlbeans.impl.values.XmlDateImpl;
import org.apache.xmlbeans.impl.values.XmlDurationImpl;
import org.apache.xmlbeans.impl.values.XmlListImpl;
import org.apache.xmlbeans.impl.values.XmlQNameImpl;
import org.apache.xmlbeans.impl.values.XmlValueOutOfRangeException;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/validator/Validator.class */
public final class Validator implements ValidatorListener {
    private LinkedList _visitorPool = new LinkedList();
    private boolean _invalid;
    private SchemaType _rootType;
    private SchemaField _rootField;
    private SchemaTypeLoader _globalTypes;
    private State _stateStack;
    private int _errorState;
    private Collection _errorListener;
    private boolean _treatLaxAsSkip;
    private boolean _strict;
    private ValidatorVC _vc;
    private int _suspendErrors;
    private IdentityConstraint _constraintEngine;
    private int _eatContent;
    private SchemaLocalElement _localElement;
    private SchemaParticle _wildcardElement;
    private SchemaLocalAttribute _localAttribute;
    private SchemaAttributeModel _wildcardAttribute;
    private SchemaType _unionType;
    private String _stringValue;
    private BigDecimal _decimalValue;
    private boolean _booleanValue;
    private float _floatValue;
    private double _doubleValue;
    private QName _qnameValue;
    private GDate _gdateValue;
    private GDuration _gdurationValue;
    private byte[] _byteArrayValue;
    private List _listValue;
    private List _listTypes;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Validator.class.desiredAssertionStatus();
    }

    public Validator(SchemaType type, SchemaField field, SchemaTypeLoader globalLoader, XmlOptions options, Collection defaultErrorListener) {
        XmlOptions options2 = XmlOptions.maskNull(options);
        this._errorListener = (Collection) options2.get(XmlOptions.ERROR_LISTENER);
        this._treatLaxAsSkip = options2.hasOption(XmlOptions.VALIDATE_TREAT_LAX_AS_SKIP);
        this._strict = options2.hasOption(XmlOptions.VALIDATE_STRICT);
        if (this._errorListener == null) {
            this._errorListener = defaultErrorListener;
        }
        this._constraintEngine = new IdentityConstraint(this._errorListener, type.isDocumentType());
        this._globalTypes = globalLoader;
        this._rootType = type;
        this._rootField = field;
        this._vc = new ValidatorVC();
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/validator/Validator$ValidatorVC.class */
    private class ValidatorVC implements ValidationContext {
        ValidatorListener.Event _event;

        private ValidatorVC() {
        }

        @Override // org.apache.xmlbeans.impl.common.ValidationContext
        public void invalid(String message) {
            Validator.this.emitError(this._event, message, null, null, null, 1001, null);
        }

        @Override // org.apache.xmlbeans.impl.common.ValidationContext
        public void invalid(String code, Object[] args) {
            Validator.this.emitError(this._event, code, args, null, null, null, 1001, null);
        }
    }

    public boolean isValid() {
        return !this._invalid && this._constraintEngine.isValid();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void emitError(ValidatorListener.Event event, String message, QName offendingQName, SchemaType expectedSchemaType, List expectedQNames, int errorType, SchemaType badSchemaType) {
        emitError(event, message, null, null, 0, null, offendingQName, expectedSchemaType, expectedQNames, errorType, badSchemaType);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void emitError(ValidatorListener.Event event, String code, Object[] args, QName offendingQName, SchemaType expectedSchemaType, List expectedQNames, int errorType, SchemaType badSchemaType) {
        emitError(event, null, code, args, 0, null, offendingQName, expectedSchemaType, expectedQNames, errorType, badSchemaType);
    }

    private void emitError(ValidatorListener.Event event, String message, String code, Object[] args, int severity, QName fieldName, QName offendingQName, SchemaType expectedSchemaType, List expectedQNames, int errorType, SchemaType badSchemaType) {
        XmlValidationError xmlValidationErrorForLocationWithDetails;
        this._errorState++;
        if (this._suspendErrors == 0) {
            if (severity == 0) {
                this._invalid = true;
            }
            if (this._errorListener != null) {
                if (!$assertionsDisabled && event == null) {
                    throw new AssertionError();
                }
                XmlCursor curs = event.getLocationAsCursor();
                if (curs != null) {
                    xmlValidationErrorForLocationWithDetails = XmlValidationError.forCursorWithDetails(message, code, args, severity, curs, fieldName, offendingQName, expectedSchemaType, expectedQNames, errorType, badSchemaType);
                } else {
                    xmlValidationErrorForLocationWithDetails = XmlValidationError.forLocationWithDetails(message, code, args, severity, event.getLocation(), fieldName, offendingQName, expectedSchemaType, expectedQNames, errorType, badSchemaType);
                }
                this._errorListener.add(xmlValidationErrorForLocationWithDetails);
            }
        }
    }

    private void emitFieldError(ValidatorListener.Event event, String code, Object[] args, QName offendingQName, SchemaType expectedSchemaType, List expectedQNames, int errorType, SchemaType badSchemaType) {
        emitFieldError(event, null, code, args, 0, offendingQName, expectedSchemaType, expectedQNames, errorType, badSchemaType);
    }

    private void emitFieldError(ValidatorListener.Event event, String message, String code, Object[] args, int severity, QName offendingQName, SchemaType expectedSchemaType, List expectedQNames, int errorType, SchemaType badSchemaType) {
        QName fieldName = null;
        if (this._stateStack != null && this._stateStack._field != null) {
            fieldName = this._stateStack._field.getName();
        }
        emitError(event, message, code, args, severity, fieldName, offendingQName, expectedSchemaType, expectedQNames, errorType, badSchemaType);
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener
    public void nextEvent(int kind, ValidatorListener.Event event) {
        resetValues();
        if (this._eatContent > 0) {
            switch (kind) {
                case 1:
                    this._eatContent++;
                    return;
                case 2:
                    this._eatContent--;
                    return;
                default:
                    return;
            }
        }
        if (!$assertionsDisabled && kind != 1 && kind != 4 && kind != 2 && kind != 3 && kind != 5) {
            throw new AssertionError();
        }
        switch (kind) {
            case 1:
                beginEvent(event);
                return;
            case 2:
                endEvent(event);
                return;
            case 3:
                textEvent(event);
                return;
            case 4:
                attrEvent(event);
                return;
            case 5:
                endAttrsEvent(event);
                return;
            default:
                return;
        }
    }

    private void beginEvent(ValidatorListener.Event event) {
        SchemaType elementType;
        this._localElement = null;
        this._wildcardElement = null;
        State state = topState();
        SchemaField elementField = null;
        if (state == null) {
            elementType = this._rootType;
            elementField = this._rootField;
        } else {
            QName name = event.getName();
            if (!$assertionsDisabled && name == null) {
                throw new AssertionError();
            }
            state._isEmpty = false;
            if (state._isNil) {
                emitFieldError(event, XmlErrorCodes.ELEM_LOCALLY_VALID$NIL_WITH_CONTENT, null, state._field.getName(), state._type, null, 4, state._type);
                this._eatContent = 1;
                return;
            }
            if (!state._isNil && state._field != null && state._field.isFixed()) {
                emitFieldError(event, XmlErrorCodes.ELEM_LOCALLY_VALID$FIXED_WITH_CONTENT, new Object[]{QNameHelper.pretty(state._field.getName())}, state._field.getName(), state._type, null, 2, state._type);
            }
            if (!state.visit(name)) {
                findDetailedErrorBegin(event, state, name);
                this._eatContent = 1;
                return;
            }
            SchemaParticle currentParticle = state.currentParticle();
            this._wildcardElement = currentParticle;
            if (currentParticle.getParticleType() == 5) {
                QNameSet elemWildcardSet = currentParticle.getWildcardSet();
                if (!elemWildcardSet.contains(name)) {
                    emitFieldError(event, XmlErrorCodes.PARTICLE_VALID$NOT_WILDCARD_VALID, new Object[]{QNameHelper.pretty(name)}, name, null, null, 2, state._type);
                    this._eatContent = 1;
                    return;
                }
                int wildcardProcess = currentParticle.getWildcardProcess();
                if (wildcardProcess == 3 || (wildcardProcess == 2 && this._treatLaxAsSkip)) {
                    this._eatContent = 1;
                    return;
                }
                this._localElement = this._globalTypes.findElement(name);
                elementField = this._localElement;
                if (elementField == null) {
                    if (wildcardProcess == 1) {
                        emitFieldError(event, XmlErrorCodes.ASSESS_ELEM_SCHEMA_VALID$NOT_RESOLVED, new Object[]{QNameHelper.pretty(name)}, name, state._type, null, 2, state._type);
                    }
                    this._eatContent = 1;
                    return;
                }
            } else {
                if (!$assertionsDisabled && currentParticle.getParticleType() != 4) {
                    throw new AssertionError();
                }
                if (!currentParticle.getName().equals(name)) {
                    if (((SchemaLocalElement) currentParticle).blockSubstitution()) {
                        emitFieldError(event, XmlErrorCodes.PARTICLE_VALID$BLOCK_SUBSTITUTION, new Object[]{QNameHelper.pretty(name)}, name, state._type, null, 2, state._type);
                        this._eatContent = 1;
                        return;
                    }
                    SchemaGlobalElement newField = this._globalTypes.findElement(name);
                    if (!$assertionsDisabled && newField == null) {
                        throw new AssertionError();
                    }
                    if (newField != null) {
                        elementField = newField;
                        this._localElement = newField;
                    }
                } else {
                    elementField = (SchemaField) currentParticle;
                }
            }
            elementType = elementField.getType();
        }
        if (!$assertionsDisabled && elementType == null) {
            throw new AssertionError();
        }
        if (elementType.isNoType()) {
            emitFieldError(event, XmlErrorCodes.ELEM_LOCALLY_VALID$NO_TYPE, null, event.getName(), null, null, 3, null);
            this._eatContent = 1;
        }
        SchemaType xsiType = null;
        String value = event.getXsiType();
        if (value != null) {
            int originalErrorState = this._errorState;
            this._suspendErrors++;
            try {
                try {
                    this._vc._event = null;
                    xsiType = this._globalTypes.findType(XmlQNameImpl.validateLexical(value, this._vc, event));
                    this._suspendErrors--;
                } catch (Throwable th) {
                    this._errorState++;
                    this._suspendErrors--;
                }
                if (originalErrorState != this._errorState) {
                    emitFieldError(event, XmlErrorCodes.ELEM_LOCALLY_VALID$XSI_TYPE_INVALID_QNAME, new Object[]{value}, event.getName(), xsiType, null, 3, state._type);
                    this._eatContent = 1;
                    return;
                } else if (xsiType == null) {
                    emitFieldError(event, XmlErrorCodes.ELEM_LOCALLY_VALID$XSI_TYPE_NOT_FOUND, new Object[]{value}, event.getName(), null, null, 3, null);
                    this._eatContent = 1;
                    return;
                }
            } catch (Throwable th2) {
                this._suspendErrors--;
                throw th2;
            }
        }
        if (xsiType != null && !xsiType.equals(elementType)) {
            if (!elementType.isAssignableFrom(xsiType)) {
                emitFieldError(event, XmlErrorCodes.ELEM_LOCALLY_VALID$XSI_TYPE_NOT_DERIVED, new Object[]{xsiType, elementType}, event.getName(), elementType, null, 3, state == null ? null : state._type);
                this._eatContent = 1;
                return;
            }
            if (elementType.blockExtension()) {
                SchemaType baseType = xsiType;
                while (true) {
                    SchemaType t = baseType;
                    if (t.equals(elementType)) {
                        break;
                    }
                    if (t.getDerivationType() != 2) {
                        baseType = t.getBaseType();
                    } else {
                        emitFieldError(event, XmlErrorCodes.ELEM_LOCALLY_VALID$XSI_TYPE_BLOCK_EXTENSION, new Object[]{xsiType, elementType}, event.getName(), elementType, null, 3, state == null ? null : state._type);
                        this._eatContent = 1;
                        return;
                    }
                }
            }
            if (elementType.blockRestriction()) {
                SchemaType baseType2 = xsiType;
                while (true) {
                    SchemaType t2 = baseType2;
                    if (t2.equals(elementType)) {
                        break;
                    }
                    if (t2.getDerivationType() != 1) {
                        baseType2 = t2.getBaseType();
                    } else {
                        emitFieldError(event, XmlErrorCodes.ELEM_LOCALLY_VALID$XSI_TYPE_BLOCK_RESTRICTION, new Object[]{xsiType, elementType}, event.getName(), elementType, null, 3, state == null ? null : state._type);
                        this._eatContent = 1;
                        return;
                    }
                }
            }
            if (elementField instanceof SchemaLocalElement) {
                SchemaLocalElement sle = (SchemaLocalElement) elementField;
                this._localElement = sle;
                if (sle.blockExtension() || sle.blockRestriction()) {
                    SchemaType baseType3 = xsiType;
                    while (true) {
                        SchemaType t3 = baseType3;
                        if (!t3.equals(elementType)) {
                            if ((t3.getDerivationType() == 1 && sle.blockRestriction()) || (t3.getDerivationType() == 2 && sle.blockExtension())) {
                                break;
                            } else {
                                baseType3 = t3.getBaseType();
                            }
                        } else {
                            break;
                        }
                    }
                    emitFieldError(event, XmlErrorCodes.ELEM_LOCALLY_VALID$XSI_TYPE_PROHIBITED_SUBST, new Object[]{xsiType, QNameHelper.pretty(sle.getName())}, sle.getName(), null, null, 3, null);
                    this._eatContent = 1;
                    return;
                }
            }
            elementType = xsiType;
        }
        if (elementField instanceof SchemaLocalElement) {
            SchemaLocalElement sle2 = (SchemaLocalElement) elementField;
            this._localElement = sle2;
            if (sle2.isAbstract()) {
                emitError(event, XmlErrorCodes.ELEM_LOCALLY_VALID$ABSTRACT, new Object[]{QNameHelper.pretty(sle2.getName())}, sle2.getName(), null, null, 3, null);
                this._eatContent = 1;
                return;
            }
        }
        if (elementType != null && elementType.isAbstract()) {
            emitError(event, XmlErrorCodes.ELEM_LOCALLY_VALID$ABSTRACT, new Object[]{elementType}, event.getName(), elementType, null, 3, state == null ? null : state._type);
            this._eatContent = 1;
            return;
        }
        boolean isNil = false;
        boolean hasNil = false;
        String nilValue = event.getXsiNil();
        if (nilValue != null) {
            this._vc._event = event;
            isNil = JavaBooleanHolder.validateLexical(nilValue, this._vc);
            hasNil = true;
        }
        if (hasNil && (elementField == null || !elementField.isNillable())) {
            emitFieldError(event, XmlErrorCodes.ELEM_LOCALLY_VALID$NOT_NILLABLE, null, elementField == null ? null : elementField.getName(), elementType, null, 3, state == null ? null : state._type);
            this._eatContent = 1;
            return;
        }
        if (isNil && elementField != null && elementField.isFixed()) {
            emitFieldError(event, XmlErrorCodes.ELEM_LOCALLY_VALID$NIL_WITH_FIXED, null, elementField == null ? null : elementField.getName(), elementType, null, 3, state == null ? null : state._type);
        }
        newState(elementType, elementField, isNil);
        this._constraintEngine.element(event, elementType, elementField instanceof SchemaLocalElement ? ((SchemaLocalElement) elementField).getIdentityConstraints() : null);
    }

    private void attrEvent(ValidatorListener.Event event) {
        QName attrName = event.getName();
        State state = topState();
        if (state._attrs == null) {
            state._attrs = new HashSet();
        }
        if (state._attrs.contains(attrName)) {
            emitFieldError(event, XmlErrorCodes.XML_DUPLICATE_ATTRIBUTE, new Object[]{QNameHelper.pretty(attrName)}, attrName, null, null, 1000, state._type);
            return;
        }
        state._attrs.add(attrName);
        if (!state._canHaveAttrs) {
            emitFieldError(event, XmlErrorCodes.ELEM_COMPLEX_TYPE_LOCALLY_VALID$NO_WILDCARD, new Object[]{QNameHelper.pretty(attrName)}, attrName, null, null, 1000, state._type);
            return;
        }
        SchemaLocalAttribute attrSchema = state._attrModel == null ? null : state._attrModel.getAttribute(attrName);
        if (attrSchema != null) {
            this._localAttribute = attrSchema;
            if (attrSchema.getUse() == 1) {
                emitFieldError(event, XmlErrorCodes.ELEM_COMPLEX_TYPE_LOCALLY_VALID$PROHIBITED_ATTRIBUTE, new Object[]{QNameHelper.pretty(attrName)}, attrName, null, null, 1000, state._type);
                return;
            } else {
                String value = validateSimpleType(attrSchema.getType(), attrSchema, event, false, false);
                this._constraintEngine.attr(event, attrName, attrSchema.getType(), value);
                return;
            }
        }
        int wildcardProcess = state._attrModel.getWildcardProcess();
        this._wildcardAttribute = state._attrModel;
        if (wildcardProcess == 0) {
            emitFieldError(event, XmlErrorCodes.ELEM_COMPLEX_TYPE_LOCALLY_VALID$NO_WILDCARD, new Object[]{QNameHelper.pretty(attrName)}, attrName, null, null, 1000, state._type);
            return;
        }
        QNameSet attrWildcardSet = state._attrModel.getWildcardSet();
        if (!attrWildcardSet.contains(attrName)) {
            emitFieldError(event, XmlErrorCodes.ELEM_COMPLEX_TYPE_LOCALLY_VALID$NOT_WILDCARD_VALID, new Object[]{QNameHelper.pretty(attrName)}, attrName, null, null, 1000, state._type);
            return;
        }
        if (wildcardProcess != 3) {
            if (wildcardProcess == 2 && this._treatLaxAsSkip) {
                return;
            }
            SchemaLocalAttribute attrSchema2 = this._globalTypes.findAttribute(attrName);
            this._localAttribute = attrSchema2;
            if (attrSchema2 == null) {
                if (wildcardProcess == 2) {
                    return;
                }
                if (!$assertionsDisabled && wildcardProcess != 1) {
                    throw new AssertionError();
                }
                emitFieldError(event, XmlErrorCodes.ASSESS_ATTR_SCHEMA_VALID$NOT_RESOLVED, new Object[]{QNameHelper.pretty(attrName)}, attrName, null, null, 1000, state._type);
                return;
            }
            String value2 = validateSimpleType(attrSchema2.getType(), attrSchema2, event, false, false);
            this._constraintEngine.attr(event, attrName, attrSchema2.getType(), value2);
        }
    }

    private void endAttrsEvent(ValidatorListener.Event event) {
        State state = topState();
        if (state._attrModel != null) {
            SchemaLocalAttribute[] attrs = state._attrModel.getAttributes();
            for (SchemaLocalAttribute sla : attrs) {
                if (state._attrs == null || !state._attrs.contains(sla.getName())) {
                    if (sla.getUse() == 3) {
                        emitFieldError(event, XmlErrorCodes.ELEM_COMPLEX_TYPE_LOCALLY_VALID$MISSING_REQUIRED_ATTRIBUTE, new Object[]{QNameHelper.pretty(sla.getName())}, sla.getName(), null, null, 1000, state._type);
                    } else if (sla.isDefault() || sla.isFixed()) {
                        this._constraintEngine.attr(event, sla.getName(), sla.getType(), sla.getDefaultText());
                    }
                }
            }
        }
    }

    private void endEvent(ValidatorListener.Event event) {
        this._localElement = null;
        this._wildcardElement = null;
        State state = topState();
        if (!state._isNil) {
            if (!state.end()) {
                findDetailedErrorEnd(event, state);
            }
            if (state._isEmpty) {
                handleText(event, true, state._field);
            }
        }
        popState(event);
        this._constraintEngine.endElement(event);
    }

    private void textEvent(ValidatorListener.Event event) {
        State state = topState();
        if (state._isNil) {
            emitFieldError(event, XmlErrorCodes.ELEM_LOCALLY_VALID$NIL_WITH_CONTENT, null, state._field.getName(), state._type, null, 4, state._type);
        } else {
            handleText(event, false, state._field);
        }
        state._isEmpty = false;
    }

    private void handleText(ValidatorListener.Event event, boolean emptyContent, SchemaField field) {
        State state = topState();
        if (!state._sawText) {
            if (state._hasSimpleContent) {
                String value = validateSimpleType(state._type, field, event, emptyContent, true);
                this._constraintEngine.text(event, state._type, value, false);
            } else if (state._canHaveMixedContent) {
                String value2 = validateSimpleType(XmlString.type, field, event, emptyContent, true);
                this._constraintEngine.text(event, XmlString.type, value2, false);
            } else if (emptyContent) {
                this._constraintEngine.text(event, state._type, null, true);
            } else {
                this._constraintEngine.text(event, state._type, "", false);
            }
        }
        if (!emptyContent && !state._canHaveMixedContent && !event.textIsWhitespace() && !state._hasSimpleContent) {
            if (field instanceof SchemaLocalElement) {
                SchemaLocalElement e = (SchemaLocalElement) field;
                if (!$assertionsDisabled && state._type.getContentType() != 1 && state._type.getContentType() != 3) {
                    throw new AssertionError();
                }
                String errorCode = state._type.getContentType() == 1 ? XmlErrorCodes.ELEM_COMPLEX_TYPE_LOCALLY_VALID$EMPTY_WITH_CONTENT : XmlErrorCodes.ELEM_COMPLEX_TYPE_LOCALLY_VALID$ELEMENT_ONLY_WITH_TEXT;
                emitError(event, errorCode, new Object[]{QNameHelper.pretty(e.getName())}, e.getName(), field.getType(), null, 3, null);
            } else {
                emitError(event, "Can't have mixed content", event.getName(), state._type, null, 3, null);
            }
        }
        if (!emptyContent) {
            state._sawText = true;
        }
    }

    private void findDetailedErrorBegin(ValidatorListener.Event event, State state, QName qName) {
        ArrayList expectedNames = new ArrayList();
        ArrayList optionalNames = new ArrayList();
        SchemaProperty[] eltProperties = state._type.getElementProperties();
        for (SchemaProperty sProp : eltProperties) {
            if (state.test(sProp.getName())) {
                if (0 == BigInteger.ZERO.compareTo(sProp.getMinOccurs())) {
                    optionalNames.add(sProp.getName());
                } else {
                    expectedNames.add(sProp.getName());
                }
            }
        }
        List names = expectedNames.size() > 0 ? expectedNames : optionalNames;
        if (names.size() > 0) {
            StringBuffer buf = new StringBuffer();
            Iterator iter = names.iterator();
            while (iter.hasNext()) {
                QName qname = (QName) iter.next();
                buf.append(QNameHelper.pretty(qname));
                if (iter.hasNext()) {
                    buf.append(SymbolConstants.SPACE_SYMBOL);
                }
            }
            emitFieldError(event, XmlErrorCodes.ELEM_COMPLEX_TYPE_LOCALLY_VALID$EXPECTED_DIFFERENT_ELEMENT, new Object[]{new Integer(names.size()), buf.toString(), QNameHelper.pretty(qName)}, qName, null, names, 1, state._type);
            return;
        }
        emitFieldError(event, XmlErrorCodes.ELEM_COMPLEX_TYPE_LOCALLY_VALID$ELEMENT_NOT_ALLOWED, new Object[]{QNameHelper.pretty(qName)}, qName, null, null, 1, state._type);
    }

    private void findDetailedErrorEnd(ValidatorListener.Event event, State state) {
        SchemaProperty[] eltProperties = state._type.getElementProperties();
        ArrayList expectedNames = new ArrayList();
        ArrayList optionalNames = new ArrayList();
        for (SchemaProperty sProp : eltProperties) {
            if (state.test(sProp.getName())) {
                if (0 == BigInteger.ZERO.compareTo(sProp.getMinOccurs())) {
                    optionalNames.add(sProp.getName());
                } else {
                    expectedNames.add(sProp.getName());
                }
            }
        }
        List names = expectedNames.size() > 0 ? expectedNames : optionalNames;
        if (names.size() > 0) {
            StringBuffer buf = new StringBuffer();
            Iterator iter = names.iterator();
            while (iter.hasNext()) {
                QName qname = (QName) iter.next();
                buf.append(QNameHelper.pretty(qname));
                if (iter.hasNext()) {
                    buf.append(SymbolConstants.SPACE_SYMBOL);
                }
            }
            emitFieldError(event, XmlErrorCodes.ELEM_COMPLEX_TYPE_LOCALLY_VALID$MISSING_ELEMENT, new Object[]{new Integer(names.size()), buf.toString()}, null, null, names, 1, state._type);
            return;
        }
        emitFieldError(event, XmlErrorCodes.ELEM_COMPLEX_TYPE_LOCALLY_VALID$EXPECTED_ELEMENT, null, null, null, null, 2, state._type);
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/validator/Validator$State.class */
    private final class State {
        SchemaType _type;
        SchemaField _field;
        boolean _canHaveAttrs;
        boolean _canHaveMixedContent;
        boolean _hasSimpleContent;
        boolean _sawText;
        boolean _isEmpty;
        boolean _isNil;
        SchemaTypeVisitorImpl _visitor;
        boolean _canHaveElements;
        SchemaAttributeModel _attrModel;
        HashSet _attrs;
        State _next;
        static final /* synthetic */ boolean $assertionsDisabled;

        private State() {
        }

        static {
            $assertionsDisabled = !Validator.class.desiredAssertionStatus();
        }

        boolean visit(QName name) {
            return this._canHaveElements && this._visitor.visit(name);
        }

        boolean test(QName name) {
            return this._canHaveElements && this._visitor.testValid(name);
        }

        boolean end() {
            return !this._canHaveElements || this._visitor.visit(null);
        }

        SchemaParticle currentParticle() {
            if ($assertionsDisabled || this._visitor != null) {
                return this._visitor.currentParticle();
            }
            throw new AssertionError();
        }
    }

    private boolean derivedFromInteger(SchemaType type) {
        int btc;
        int builtinTypeCode = type.getBuiltinTypeCode();
        while (true) {
            btc = builtinTypeCode;
            if (btc != 0) {
                break;
            }
            type = type.getBaseType();
            builtinTypeCode = type.getBuiltinTypeCode();
        }
        return btc >= 22 && btc <= 34;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:12:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0091  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x009d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void newState(org.apache.xmlbeans.SchemaType r6, org.apache.xmlbeans.SchemaField r7, boolean r8) {
        /*
            r5 = this;
            org.apache.xmlbeans.impl.validator.Validator$State r0 = new org.apache.xmlbeans.impl.validator.Validator$State
            r1 = r0
            r2 = r5
            r3 = 0
            r1.<init>()
            r9 = r0
            r0 = r9
            r1 = r6
            r0._type = r1
            r0 = r9
            r1 = r7
            r0._field = r1
            r0 = r9
            r1 = 1
            r0._isEmpty = r1
            r0 = r9
            r1 = r8
            r0._isNil = r1
            r0 = r6
            boolean r0 = r0.isSimpleType()
            if (r0 == 0) goto L35
            r0 = r9
            r1 = 1
            r0._hasSimpleContent = r1
            goto Lb5
        L35:
            r0 = r9
            r1 = 1
            r0._canHaveAttrs = r1
            r0 = r9
            r1 = r6
            org.apache.xmlbeans.SchemaAttributeModel r1 = r1.getAttributeModel()
            r0._attrModel = r1
            r0 = r6
            int r0 = r0.getContentType()
            switch(r0) {
                case 1: goto L6c;
                case 2: goto L6f;
                case 3: goto L7e;
                case 4: goto L78;
                default: goto Lab;
            }
        L6c:
            goto Lb5
        L6f:
            r0 = r9
            r1 = 1
            r0._hasSimpleContent = r1
            goto Lb5
        L78:
            r0 = r9
            r1 = 1
            r0._canHaveMixedContent = r1
        L7e:
            r0 = r6
            org.apache.xmlbeans.SchemaParticle r0 = r0.getContentModel()
            r10 = r0
            r0 = r9
            r1 = r10
            if (r1 == 0) goto L91
            r1 = 1
            goto L92
        L91:
            r1 = 0
        L92:
            r0._canHaveElements = r1
            r0 = r9
            boolean r0 = r0._canHaveElements
            if (r0 == 0) goto Lb5
            r0 = r9
            r1 = r5
            r2 = r10
            org.apache.xmlbeans.impl.schema.SchemaTypeVisitorImpl r1 = r1.initVisitor(r2)
            r0._visitor = r1
            goto Lb5
        Lab:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            r1 = r0
            java.lang.String r2 = "Unexpected content type"
            r1.<init>(r2)
            throw r0
        Lb5:
            r0 = r5
            r1 = r9
            r0.pushState(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.validator.Validator.newState(org.apache.xmlbeans.SchemaType, org.apache.xmlbeans.SchemaField, boolean):void");
    }

    private void popState(ValidatorListener.Event e) {
        if (this._stateStack._visitor != null) {
            poolVisitor(this._stateStack._visitor);
            this._stateStack._visitor = null;
        }
        this._stateStack = this._stateStack._next;
    }

    private void pushState(State state) {
        state._next = this._stateStack;
        this._stateStack = state;
    }

    private void poolVisitor(SchemaTypeVisitorImpl visitor) {
        this._visitorPool.add(visitor);
    }

    private SchemaTypeVisitorImpl initVisitor(SchemaParticle particle) {
        if (this._visitorPool.isEmpty()) {
            return new SchemaTypeVisitorImpl(particle);
        }
        SchemaTypeVisitorImpl result = (SchemaTypeVisitorImpl) this._visitorPool.removeLast();
        result.init(particle);
        return result;
    }

    private State topState() {
        return this._stateStack;
    }

    private String validateSimpleType(SchemaType type, SchemaField field, ValidatorListener.Event event, boolean emptyContent, boolean canApplyDefault) {
        if (!type.isSimpleType() && type.getContentType() != 2) {
            if ($assertionsDisabled) {
                return null;
            }
            throw new AssertionError();
        }
        if (type.isNoType()) {
            emitError(event, field.isAttribute() ? XmlErrorCodes.ATTR_LOCALLY_VALID$NO_TYPE : XmlErrorCodes.ELEM_LOCALLY_VALID$NO_TYPE, null, field.getName(), type, null, 3, null);
            return null;
        }
        String value = "";
        if (!emptyContent) {
            int wsr = type.getWhiteSpaceRule();
            value = wsr == 1 ? event.getText() : event.getText(wsr);
        }
        if (value.length() == 0 && canApplyDefault && field != null && (field.isDefault() || field.isFixed())) {
            if (XmlQName.type.isAssignableFrom(type)) {
                emitError(event, "Default QName values are unsupported for " + QNameHelper.readable(type) + " - ignoring.", null, null, 2, field.getName(), null, type, null, 3, null);
                return null;
            }
            String defaultValue = XmlWhitespace.collapse(field.getDefaultText(), type.getWhiteSpaceRule());
            if (validateSimpleType(type, defaultValue, event)) {
                return defaultValue;
            }
            return null;
        }
        if (!validateSimpleType(type, value, event)) {
            return null;
        }
        if (field != null && field.isFixed()) {
            String fixedValue = XmlWhitespace.collapse(field.getDefaultText(), type.getWhiteSpaceRule());
            if (!validateSimpleType(type, fixedValue, event)) {
                return null;
            }
            XmlObject val = type.newValue(value);
            XmlObject def = type.newValue(fixedValue);
            if (!val.valueEquals(def)) {
                if (field.isAttribute()) {
                    emitError(event, XmlErrorCodes.ATTR_LOCALLY_VALID$FIXED, new Object[]{value, fixedValue, QNameHelper.pretty(event.getName())}, null, field.getType(), null, 3, null);
                    return null;
                }
                String errorCode = null;
                if (field.getType().getContentType() == 4) {
                    errorCode = XmlErrorCodes.ELEM_LOCALLY_VALID$FIXED_VALID_MIXED_CONTENT;
                } else if (type.isSimpleType()) {
                    errorCode = XmlErrorCodes.ELEM_LOCALLY_VALID$FIXED_VALID_SIMPLE_TYPE;
                } else if (!$assertionsDisabled) {
                    throw new AssertionError("Element with fixed may not be EMPTY or ELEMENT_ONLY");
                }
                emitError(event, errorCode, new Object[]{value, fixedValue}, field.getName(), field.getType(), null, 3, null);
                return null;
            }
        }
        return value;
    }

    private boolean validateSimpleType(SchemaType type, String value, ValidatorListener.Event event) {
        if (!type.isSimpleType() && type.getContentType() != 2) {
            if ($assertionsDisabled) {
                throw new RuntimeException("Not a simple type");
            }
            throw new AssertionError();
        }
        int retState = this._errorState;
        switch (type.getSimpleVariety()) {
            case 1:
                validateAtomicType(type, value, event);
                break;
            case 2:
                validateUnionType(type, value, event);
                break;
            case 3:
                validateListType(type, value, event);
                break;
            default:
                throw new RuntimeException("Unexpected simple variety");
        }
        return retState == this._errorState;
    }

    private void validateAtomicType(SchemaType type, String value, ValidatorListener.Event event) {
        if (!$assertionsDisabled && type.getSimpleVariety() != 1) {
            throw new AssertionError();
        }
        int errorState = this._errorState;
        this._vc._event = event;
        switch (type.getPrimitiveType().getBuiltinTypeCode()) {
            case 2:
                this._stringValue = value;
                return;
            case 3:
                this._booleanValue = JavaBooleanHolderEx.validateLexical(value, type, this._vc);
                return;
            case 4:
                byte[] v = JavaBase64HolderEx.validateLexical(value, type, this._vc);
                if (v != null) {
                    JavaBase64HolderEx.validateValue(v, type, this._vc);
                }
                this._byteArrayValue = v;
                return;
            case 5:
                byte[] v2 = JavaHexBinaryHolderEx.validateLexical(value, type, this._vc);
                if (v2 != null) {
                    JavaHexBinaryHolderEx.validateValue(v2, type, this._vc);
                }
                this._byteArrayValue = v2;
                return;
            case 6:
                JavaUriHolderEx.validateLexical(value, type, this._vc);
                if (this._strict) {
                    try {
                        XsTypeConverter.lexAnyURI(value);
                    } catch (InvalidLexicalValueException e) {
                        this._vc.invalid(XmlErrorCodes.ANYURI, new Object[]{value});
                    }
                }
                this._stringValue = value;
                return;
            case 7:
                QName n = JavaQNameHolderEx.validateLexical(value, type, this._vc, event);
                if (errorState == this._errorState) {
                    JavaQNameHolderEx.validateValue(n, type, this._vc);
                }
                this._qnameValue = n;
                return;
            case 8:
                QName n2 = JavaNotationHolderEx.validateLexical(value, type, this._vc, event);
                if (errorState == this._errorState) {
                    JavaNotationHolderEx.validateValue(n2, type, this._vc);
                }
                this._qnameValue = n2;
                return;
            case 9:
                float f = JavaFloatHolderEx.validateLexical(value, type, this._vc);
                if (errorState == this._errorState) {
                    JavaFloatHolderEx.validateValue(f, type, this._vc);
                }
                this._floatValue = f;
                return;
            case 10:
                double d = JavaDoubleHolderEx.validateLexical(value, type, this._vc);
                if (errorState == this._errorState) {
                    JavaDoubleHolderEx.validateValue(d, type, this._vc);
                }
                this._doubleValue = d;
                return;
            case 11:
                JavaDecimalHolderEx.validateLexical(value, type, this._vc);
                if (derivedFromInteger(type) && value.lastIndexOf(46) >= 0) {
                    this._vc.invalid("integer", new Object[]{value});
                }
                if (errorState == this._errorState) {
                    this._decimalValue = new BigDecimal(value);
                    JavaDecimalHolderEx.validateValue(this._decimalValue, type, this._vc);
                    return;
                }
                return;
            case 12:
                JavaStringEnumerationHolderEx.validateLexical(value, type, this._vc);
                this._stringValue = value;
                return;
            case 13:
                GDuration d2 = XmlDurationImpl.validateLexical(value, type, this._vc);
                if (d2 != null) {
                    XmlDurationImpl.validateValue(d2, type, this._vc);
                }
                this._gdurationValue = d2;
                return;
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
                break;
            case 21:
                if (this._strict && value.length() == 6 && value.charAt(4) == '-' && value.charAt(5) == '-') {
                    this._vc.invalid("date", new Object[]{value});
                    break;
                }
                break;
            default:
                throw new RuntimeException("Unexpected primitive type code");
        }
        GDate d3 = XmlDateImpl.validateLexical(value, type, this._vc);
        if (d3 != null) {
            XmlDateImpl.validateValue(d3, type, this._vc);
        }
        this._gdateValue = d3;
    }

    private void validateListType(SchemaType type, String value, ValidatorListener.Event event) {
        int i;
        int i2;
        int i3;
        int errorState = this._errorState;
        if (!type.matchPatternFacet(value)) {
            emitError(event, XmlErrorCodes.DATATYPE_VALID$PATTERN_VALID, new Object[]{"list", value, QNameHelper.readable(type)}, null, type, null, 2000, null);
        }
        String[] items = XmlListImpl.split_list(value);
        XmlObject o = type.getFacet(0);
        if (o != null && (i3 = ((SimpleValue) o).getIntValue()) != items.length) {
            emitError(event, XmlErrorCodes.DATATYPE_LENGTH_VALID$LIST_LENGTH, new Object[]{value, new Integer(items.length), new Integer(i3), QNameHelper.readable(type)}, null, type, null, 2000, null);
        }
        XmlObject o2 = type.getFacet(1);
        if (o2 != null && (i2 = ((SimpleValue) o2).getIntValue()) > items.length) {
            emitError(event, XmlErrorCodes.DATATYPE_LENGTH_VALID$LIST_LENGTH, new Object[]{value, new Integer(items.length), new Integer(i2), QNameHelper.readable(type)}, null, type, null, 2000, null);
        }
        XmlObject o3 = type.getFacet(2);
        if (o3 != null && (i = ((SimpleValue) o3).getIntValue()) < items.length) {
            emitError(event, XmlErrorCodes.DATATYPE_LENGTH_VALID$LIST_LENGTH, new Object[]{value, new Integer(items.length), new Integer(i), QNameHelper.readable(type)}, null, type, null, 2000, null);
        }
        SchemaType itemType = type.getListItemType();
        this._listValue = new ArrayList();
        this._listTypes = new ArrayList();
        for (String str : items) {
            validateSimpleType(itemType, str, event);
            addToList(itemType);
        }
        if (errorState == this._errorState && type.getEnumerationValues() != null) {
            NamespaceContext.push(new NamespaceContext(event));
            try {
                try {
                    ((SchemaTypeImpl) type).newValidatingValue(value);
                    NamespaceContext.pop();
                } catch (XmlValueOutOfRangeException e) {
                    emitError(event, XmlErrorCodes.DATATYPE_ENUM_VALID, new Object[]{"list", value, QNameHelper.readable(type)}, null, type, null, 2000, null);
                    NamespaceContext.pop();
                }
            } catch (Throwable th) {
                NamespaceContext.pop();
                throw th;
            }
        }
    }

    private void validateUnionType(SchemaType type, String value, ValidatorListener.Event event) {
        if (!type.matchPatternFacet(value)) {
            emitError(event, XmlErrorCodes.DATATYPE_VALID$PATTERN_VALID, new Object[]{XmlErrorCodes.UNION, value, QNameHelper.readable(type)}, null, type, null, 3000, null);
        }
        int currentWsr = 1;
        String currentValue = value;
        SchemaType[] types = type.getUnionMemberTypes();
        int originalState = this._errorState;
        int i = 0;
        while (true) {
            if (i >= types.length) {
                break;
            }
            int memberWsr = types[i].getWhiteSpaceRule();
            if (memberWsr == 0) {
                memberWsr = 1;
            }
            if (memberWsr != currentWsr) {
                currentWsr = memberWsr;
                currentValue = XmlWhitespace.collapse(value, currentWsr);
            }
            int originalErrorState = this._errorState;
            this._suspendErrors++;
            try {
                validateSimpleType(types[i], currentValue, event);
                this._suspendErrors--;
                if (originalErrorState != this._errorState) {
                    i++;
                } else {
                    this._unionType = types[i];
                    break;
                }
            } catch (Throwable th) {
                this._suspendErrors--;
                throw th;
            }
        }
        this._errorState = originalState;
        if (i >= types.length) {
            emitError(event, XmlErrorCodes.DATATYPE_VALID$UNION, new Object[]{value, QNameHelper.readable(type)}, null, type, null, 3000, null);
            return;
        }
        XmlObject[] unionEnumvals = type.getEnumerationValues();
        if (unionEnumvals != null) {
            NamespaceContext.push(new NamespaceContext(event));
            try {
                XmlObject unionValue = type.newValue(value);
                int i2 = 0;
                while (i2 < unionEnumvals.length && !unionValue.valueEquals(unionEnumvals[i2])) {
                    i2++;
                }
                if (i2 >= unionEnumvals.length) {
                    emitError(event, XmlErrorCodes.DATATYPE_ENUM_VALID, new Object[]{XmlErrorCodes.UNION, value, QNameHelper.readable(type)}, null, type, null, 3000, null);
                }
            } catch (XmlValueOutOfRangeException e) {
                emitError(event, XmlErrorCodes.DATATYPE_ENUM_VALID, new Object[]{XmlErrorCodes.UNION, value, QNameHelper.readable(type)}, null, type, null, 3000, null);
            } finally {
                NamespaceContext.pop();
            }
        }
    }

    private void addToList(SchemaType type) {
        if (type.getSimpleVariety() != 1 && type.getSimpleVariety() != 2) {
            return;
        }
        if (type.getUnionMemberTypes().length > 0 && getUnionType() != null) {
            type = getUnionType();
            this._unionType = null;
        }
        this._listTypes.add(type);
        if (type.getPrimitiveType() == null) {
            this._listValue.add(null);
            return;
        }
        switch (type.getPrimitiveType().getBuiltinTypeCode()) {
            case 2:
                this._listValue.add(this._stringValue);
                return;
            case 3:
                this._listValue.add(this._booleanValue ? Boolean.TRUE : Boolean.FALSE);
                this._booleanValue = false;
                return;
            case 4:
                this._listValue.add(this._byteArrayValue);
                this._byteArrayValue = null;
                return;
            case 5:
                this._listValue.add(this._byteArrayValue);
                this._byteArrayValue = null;
                return;
            case 6:
                this._listTypes.add(this._stringValue);
                return;
            case 7:
                this._listValue.add(this._qnameValue);
                this._qnameValue = null;
                return;
            case 8:
                this._listValue.add(this._qnameValue);
                this._qnameValue = null;
                return;
            case 9:
                this._listValue.add(new Float(this._floatValue));
                this._floatValue = 0.0f;
                return;
            case 10:
                this._listValue.add(new Double(this._doubleValue));
                this._doubleValue = 0.0d;
                return;
            case 11:
                this._listValue.add(this._decimalValue);
                this._decimalValue = null;
                return;
            case 12:
                this._listValue.add(this._stringValue);
                this._stringValue = null;
                return;
            case 13:
                this._listValue.add(this._gdurationValue);
                this._gdurationValue = null;
                return;
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
                this._listValue.add(this._gdateValue);
                this._gdateValue = null;
                return;
            default:
                throw new RuntimeException("Unexpected primitive type code");
        }
    }

    private void resetValues() {
        this._localAttribute = null;
        this._wildcardAttribute = null;
        this._stringValue = null;
        this._decimalValue = null;
        this._booleanValue = false;
        this._floatValue = 0.0f;
        this._doubleValue = 0.0d;
        this._qnameValue = null;
        this._gdateValue = null;
        this._gdurationValue = null;
        this._byteArrayValue = null;
        this._listValue = null;
        this._listTypes = null;
        this._unionType = null;
        this._localAttribute = null;
    }

    public SchemaType getCurrentElementSchemaType() {
        State state = topState();
        if (state != null) {
            return state._type;
        }
        return null;
    }

    public SchemaLocalElement getCurrentElement() {
        if (this._localElement != null) {
            return this._localElement;
        }
        if (this._eatContent <= 0 && this._stateStack != null && (this._stateStack._field instanceof SchemaLocalElement)) {
            return (SchemaLocalElement) this._stateStack._field;
        }
        return null;
    }

    public SchemaParticle getCurrentWildcardElement() {
        return this._wildcardElement;
    }

    public SchemaLocalAttribute getCurrentAttribute() {
        return this._localAttribute;
    }

    public SchemaAttributeModel getCurrentWildcardAttribute() {
        return this._wildcardAttribute;
    }

    public String getStringValue() {
        return this._stringValue;
    }

    public BigDecimal getDecimalValue() {
        return this._decimalValue;
    }

    public boolean getBooleanValue() {
        return this._booleanValue;
    }

    public float getFloatValue() {
        return this._floatValue;
    }

    public double getDoubleValue() {
        return this._doubleValue;
    }

    public QName getQNameValue() {
        return this._qnameValue;
    }

    public GDate getGDateValue() {
        return this._gdateValue;
    }

    public GDuration getGDurationValue() {
        return this._gdurationValue;
    }

    public byte[] getByteArrayValue() {
        return this._byteArrayValue;
    }

    public List getListValue() {
        return this._listValue;
    }

    public List getListTypes() {
        return this._listTypes;
    }

    public SchemaType getUnionType() {
        return this._unionType;
    }
}
