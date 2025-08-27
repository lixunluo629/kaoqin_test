package org.apache.xmlbeans.impl.inst2xsd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlByte;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDate;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlDuration;
import org.apache.xmlbeans.XmlFloat;
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlLong;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.XmlShort;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlTime;
import org.apache.xmlbeans.impl.common.PrefixResolver;
import org.apache.xmlbeans.impl.common.ValidationContext;
import org.apache.xmlbeans.impl.common.XmlWhitespace;
import org.apache.xmlbeans.impl.inst2xsd.util.Attribute;
import org.apache.xmlbeans.impl.inst2xsd.util.Element;
import org.apache.xmlbeans.impl.inst2xsd.util.Type;
import org.apache.xmlbeans.impl.inst2xsd.util.TypeSystemHolder;
import org.apache.xmlbeans.impl.util.XsTypeConverter;
import org.apache.xmlbeans.impl.values.XmlAnyUriImpl;
import org.apache.xmlbeans.impl.values.XmlDateImpl;
import org.apache.xmlbeans.impl.values.XmlDateTimeImpl;
import org.apache.xmlbeans.impl.values.XmlDurationImpl;
import org.apache.xmlbeans.impl.values.XmlQNameImpl;
import org.apache.xmlbeans.impl.values.XmlTimeImpl;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/inst2xsd/RussianDollStrategy.class */
public class RussianDollStrategy implements XsdGenStrategy {
    static final String _xsi = "http://www.w3.org/2001/XMLSchema-instance";
    static final QName _xsiNil;
    static final QName _xsiType;
    private SCTValidationContext _validationContext = new SCTValidationContext();
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !RussianDollStrategy.class.desiredAssertionStatus();
        _xsiNil = new QName(_xsi, "nil", "xsi");
        _xsiType = new QName(_xsi, "type", "xsi");
    }

    @Override // org.apache.xmlbeans.impl.inst2xsd.XsdGenStrategy
    public void processDoc(XmlObject[] instances, Inst2XsdOptions options, TypeSystemHolder typeSystemHolder) {
        for (XmlObject instance : instances) {
            XmlCursor xc = instance.newCursor();
            StringBuffer comment = new StringBuffer();
            while (!xc.isStart()) {
                xc.toNextToken();
                if (xc.isComment()) {
                    comment.append(xc.getTextValue());
                } else if (xc.isEnddoc()) {
                    return;
                }
            }
            Element withElem = processElement(xc, comment.toString(), options, typeSystemHolder);
            withElem.setGlobal(true);
            addGlobalElement(withElem, typeSystemHolder, options);
        }
    }

    protected Element addGlobalElement(Element withElem, TypeSystemHolder typeSystemHolder, Inst2XsdOptions options) {
        if (!$assertionsDisabled && !withElem.isGlobal()) {
            throw new AssertionError();
        }
        Element intoElem = typeSystemHolder.getGlobalElement(withElem.getName());
        if (intoElem == null) {
            typeSystemHolder.addGlobalElement(withElem);
            return withElem;
        }
        combineTypes(intoElem.getType(), withElem.getType(), options);
        combineElementComments(intoElem, withElem);
        return intoElem;
    }

    protected Element processElement(XmlCursor xc, String comment, Inst2XsdOptions options, TypeSystemHolder typeSystemHolder) {
        if (!$assertionsDisabled && !xc.isStart()) {
            throw new AssertionError();
        }
        Element element = new Element();
        element.setName(xc.getName());
        element.setGlobal(false);
        Type elemType = Type.createUnnamedType(1);
        element.setType(elemType);
        StringBuffer textBuff = new StringBuffer();
        StringBuffer commentBuff = new StringBuffer();
        List children = new ArrayList();
        List attributes = new ArrayList();
        while (true) {
            XmlCursor.TokenType tt = xc.toNextToken();
            switch (tt.intValue()) {
                case 0:
                case 2:
                case 4:
                    String collapsedText = XmlWhitespace.collapse(textBuff.toString(), 3);
                    String commnetStr = comment == null ? commentBuff.length() == 0 ? null : commentBuff.toString() : commentBuff.length() == 0 ? comment : commentBuff.insert(0, comment).toString();
                    element.setComment(commnetStr);
                    if (children.size() > 0) {
                        if (collapsedText.length() > 0) {
                            elemType.setContentType(4);
                        } else {
                            elemType.setContentType(3);
                        }
                        processElementsInComplexType(elemType, children, element.getName().getNamespaceURI(), typeSystemHolder, options);
                        processAttributesInComplexType(elemType, attributes);
                    } else {
                        XmlCursor xcForNamespaces = xc.newCursor();
                        xcForNamespaces.toParent();
                        if (attributes.size() > 0) {
                            elemType.setContentType(2);
                            Type extendedType = Type.createNamedType(processSimpleContentType(textBuff.toString(), options, xcForNamespaces), 1);
                            elemType.setExtensionType(extendedType);
                            processAttributesInComplexType(elemType, attributes);
                        } else {
                            elemType.setContentType(1);
                            elemType.setName(processSimpleContentType(textBuff.toString(), options, xcForNamespaces));
                            String enumValue = XmlString.type.getName().equals(elemType.getName()) ? textBuff.toString() : collapsedText;
                            elemType.addEnumerationValue(enumValue, xcForNamespaces);
                        }
                        xcForNamespaces.dispose();
                    }
                    checkIfReferenceToGlobalTypeIsNeeded(element, typeSystemHolder, options);
                    return element;
                case 1:
                    throw new IllegalStateException();
                case 3:
                    children.add(processElement(xc, commentBuff.toString(), options, typeSystemHolder));
                    commentBuff.delete(0, commentBuff.length());
                    break;
                case 5:
                    textBuff.append(xc.getChars());
                    break;
                case 6:
                    QName attName = xc.getName();
                    if (!_xsiNil.getNamespaceURI().equals(attName.getNamespaceURI())) {
                        attributes.add(processAttribute(xc, options, element.getName().getNamespaceURI(), typeSystemHolder));
                        break;
                    } else if (!_xsiNil.equals(attName)) {
                        break;
                    } else {
                        element.setNillable(true);
                        break;
                    }
                case 7:
                case 9:
                    break;
                case 8:
                    commentBuff.append(xc.getTextValue());
                    break;
                default:
                    throw new IllegalStateException("Unknown TokenType.");
            }
        }
    }

    protected void processElementsInComplexType(Type elemType, List children, String parentNamespace, TypeSystemHolder typeSystemHolder, Inst2XsdOptions options) {
        Map elemNamesToElements = new HashMap();
        Element currentElem = null;
        Iterator iterator = children.iterator();
        while (iterator.hasNext()) {
            Element child = (Element) iterator.next();
            if (currentElem == null) {
                checkIfElementReferenceIsNeeded(child, parentNamespace, typeSystemHolder, options);
                elemType.addElement(child);
                elemNamesToElements.put(child.getName(), child);
                currentElem = child;
            } else if (currentElem.getName() == child.getName()) {
                combineTypes(currentElem.getType(), child.getType(), options);
                combineElementComments(currentElem, child);
                currentElem.setMinOccurs(0);
                currentElem.setMaxOccurs(-1);
            } else {
                Element sameElem = (Element) elemNamesToElements.get(child.getName());
                if (sameElem == null) {
                    checkIfElementReferenceIsNeeded(child, parentNamespace, typeSystemHolder, options);
                    elemType.addElement(child);
                    elemNamesToElements.put(child.getName(), child);
                } else {
                    combineTypes(currentElem.getType(), child.getType(), options);
                    combineElementComments(currentElem, child);
                    elemType.setTopParticleForComplexOrMixedContent(2);
                }
                currentElem = child;
            }
        }
    }

    protected void checkIfElementReferenceIsNeeded(Element child, String parentNamespace, TypeSystemHolder typeSystemHolder, Inst2XsdOptions options) {
        if (!child.getName().getNamespaceURI().equals(parentNamespace)) {
            Element referencedElem = new Element();
            referencedElem.setGlobal(true);
            referencedElem.setName(child.getName());
            referencedElem.setType(child.getType());
            if (child.isNillable()) {
                referencedElem.setNillable(true);
                child.setNillable(false);
            }
            child.setRef(addGlobalElement(referencedElem, typeSystemHolder, options));
        }
    }

    protected void checkIfReferenceToGlobalTypeIsNeeded(Element elem, TypeSystemHolder typeSystemHolder, Inst2XsdOptions options) {
    }

    protected void processAttributesInComplexType(Type elemType, List attributes) {
        if (!$assertionsDisabled && !elemType.isComplexType()) {
            throw new AssertionError();
        }
        Iterator iterator = attributes.iterator();
        while (iterator.hasNext()) {
            Attribute att = (Attribute) iterator.next();
            elemType.addAttribute(att);
        }
    }

    protected Attribute processAttribute(XmlCursor xc, Inst2XsdOptions options, String parentNamespace, TypeSystemHolder typeSystemHolder) {
        if (!$assertionsDisabled && !xc.isAttr()) {
            throw new AssertionError("xc not on attribute");
        }
        Attribute attribute = new Attribute();
        QName attName = xc.getName();
        attribute.setName(attName);
        XmlCursor parent = xc.newCursor();
        parent.toParent();
        Type simpleContentType = Type.createNamedType(processSimpleContentType(xc.getTextValue(), options, parent), 1);
        parent.dispose();
        attribute.setType(simpleContentType);
        checkIfAttributeReferenceIsNeeded(attribute, parentNamespace, typeSystemHolder);
        return attribute;
    }

    protected void checkIfAttributeReferenceIsNeeded(Attribute attribute, String parentNamespace, TypeSystemHolder typeSystemHolder) {
        if (!attribute.getName().getNamespaceURI().equals("") && !attribute.getName().getNamespaceURI().equals(parentNamespace)) {
            Attribute referencedAtt = new Attribute();
            referencedAtt.setGlobal(true);
            referencedAtt.setName(attribute.getName());
            referencedAtt.setType(attribute.getType());
            typeSystemHolder.addGlobalAttribute(referencedAtt);
            attribute.setRef(referencedAtt);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/inst2xsd/RussianDollStrategy$SCTValidationContext.class */
    protected class SCTValidationContext implements ValidationContext {
        protected boolean valid = true;

        protected SCTValidationContext() {
        }

        public boolean isValid() {
            return this.valid;
        }

        public void resetToValid() {
            this.valid = true;
        }

        @Override // org.apache.xmlbeans.impl.common.ValidationContext
        public void invalid(String message) {
            this.valid = false;
        }

        @Override // org.apache.xmlbeans.impl.common.ValidationContext
        public void invalid(String code, Object[] args) {
            this.valid = false;
        }
    }

    protected QName processSimpleContentType(String lexicalValue, Inst2XsdOptions options, final XmlCursor xc) {
        if (options.getSimpleContentTypes() == 2) {
            return XmlString.type.getName();
        }
        if (options.getSimpleContentTypes() != 1) {
            throw new IllegalArgumentException("Unknown value for Inst2XsdOptions.getSimpleContentTypes() :" + options.getSimpleContentTypes());
        }
        try {
            XsTypeConverter.lexByte(lexicalValue);
            return XmlByte.type.getName();
        } catch (Exception e) {
            try {
                XsTypeConverter.lexShort(lexicalValue);
                return XmlShort.type.getName();
            } catch (Exception e2) {
                try {
                    XsTypeConverter.lexInt(lexicalValue);
                    return XmlInt.type.getName();
                } catch (Exception e3) {
                    try {
                        XsTypeConverter.lexLong(lexicalValue);
                        return XmlLong.type.getName();
                    } catch (Exception e4) {
                        try {
                            XsTypeConverter.lexInteger(lexicalValue);
                            return XmlInteger.type.getName();
                        } catch (Exception e5) {
                            try {
                                XsTypeConverter.lexFloat(lexicalValue);
                                return XmlFloat.type.getName();
                            } catch (Exception e6) {
                                XmlDateImpl.validateLexical(lexicalValue, XmlDate.type, this._validationContext);
                                if (this._validationContext.isValid()) {
                                    return XmlDate.type.getName();
                                }
                                this._validationContext.resetToValid();
                                XmlDateTimeImpl.validateLexical(lexicalValue, XmlDateTime.type, this._validationContext);
                                if (this._validationContext.isValid()) {
                                    return XmlDateTime.type.getName();
                                }
                                this._validationContext.resetToValid();
                                XmlTimeImpl.validateLexical(lexicalValue, XmlTime.type, this._validationContext);
                                if (this._validationContext.isValid()) {
                                    return XmlTime.type.getName();
                                }
                                this._validationContext.resetToValid();
                                XmlDurationImpl.validateLexical(lexicalValue, XmlDuration.type, this._validationContext);
                                if (this._validationContext.isValid()) {
                                    return XmlDuration.type.getName();
                                }
                                this._validationContext.resetToValid();
                                if (lexicalValue.startsWith("http://") || lexicalValue.startsWith("www.")) {
                                    XmlAnyUriImpl.validateLexical(lexicalValue, this._validationContext);
                                    if (this._validationContext.isValid()) {
                                        return XmlAnyURI.type.getName();
                                    }
                                    this._validationContext.resetToValid();
                                }
                                int idx = lexicalValue.indexOf(58);
                                if (idx >= 0 && idx == lexicalValue.lastIndexOf(58) && idx + 1 < lexicalValue.length()) {
                                    PrefixResolver prefixResolver = new PrefixResolver() { // from class: org.apache.xmlbeans.impl.inst2xsd.RussianDollStrategy.1
                                        @Override // org.apache.xmlbeans.impl.common.PrefixResolver
                                        public String getNamespaceForPrefix(String prefix) {
                                            return xc.namespaceForPrefix(prefix);
                                        }
                                    };
                                    XmlQNameImpl.validateLexical(lexicalValue, this._validationContext, prefixResolver);
                                    if (this._validationContext.isValid()) {
                                        return XmlQName.type.getName();
                                    }
                                    this._validationContext.resetToValid();
                                }
                                return XmlString.type.getName();
                            }
                        }
                    }
                }
            }
        }
    }

    protected void combineTypes(Type into, Type with, Inst2XsdOptions options) {
        if (into == with) {
            return;
        }
        if (into.isGlobal() && with.isGlobal() && into.getName().equals(with.getName())) {
            return;
        }
        if (into.getContentType() == 1 && with.getContentType() == 1) {
            combineSimpleTypes(into, with, options);
            return;
        }
        if ((into.getContentType() == 1 || into.getContentType() == 2) && (with.getContentType() == 1 || with.getContentType() == 2)) {
            QName intoTypeName = into.isComplexType() ? into.getExtensionType().getName() : into.getName();
            QName withTypeName = with.isComplexType() ? with.getExtensionType().getName() : with.getName();
            into.setContentType(2);
            QName moreGeneralTypeName = combineToMoreGeneralSimpleType(intoTypeName, withTypeName);
            if (into.isComplexType()) {
                Type extendedType = Type.createNamedType(moreGeneralTypeName, 1);
                into.setExtensionType(extendedType);
            } else {
                into.setName(moreGeneralTypeName);
            }
            combineAttributesOfTypes(into, with);
            return;
        }
        if (into.getContentType() == 3 && with.getContentType() == 3) {
            combineAttributesOfTypes(into, with);
            combineElementsOfTypes(into, with, false, options);
            return;
        }
        if (into.getContentType() == 1 || into.getContentType() == 2 || with.getContentType() == 1 || with.getContentType() == 2) {
            into.setContentType(4);
            combineAttributesOfTypes(into, with);
            combineElementsOfTypes(into, with, true, options);
        } else {
            if ((into.getContentType() == 1 || into.getContentType() == 2 || into.getContentType() == 3 || into.getContentType() == 4) && (with.getContentType() == 1 || with.getContentType() == 2 || with.getContentType() == 3 || with.getContentType() == 4)) {
                into.setContentType(4);
                combineAttributesOfTypes(into, with);
                combineElementsOfTypes(into, with, false, options);
                return;
            }
            throw new IllegalArgumentException("Unknown content type.");
        }
    }

    protected void combineSimpleTypes(Type into, Type with, Inst2XsdOptions options) {
        if (!$assertionsDisabled && (into.getContentType() != 1 || with.getContentType() != 1)) {
            throw new AssertionError("Invalid arguments");
        }
        into.setName(combineToMoreGeneralSimpleType(into.getName(), with.getName()));
        if (options.isUseEnumerations()) {
            into.addAllEnumerationsFrom(with);
            if (into.getEnumerationValues().size() > options.getUseEnumerations()) {
                into.closeEnumeration();
            }
        }
    }

    protected QName combineToMoreGeneralSimpleType(QName t1, QName t2) {
        if (t1.equals(t2)) {
            return t1;
        }
        if (t2.equals(XmlShort.type.getName()) && t1.equals(XmlByte.type.getName())) {
            return t2;
        }
        if (t1.equals(XmlShort.type.getName()) && t2.equals(XmlByte.type.getName())) {
            return t1;
        }
        if (t2.equals(XmlInt.type.getName()) && (t1.equals(XmlShort.type.getName()) || t1.equals(XmlByte.type.getName()))) {
            return t2;
        }
        if (t1.equals(XmlInt.type.getName()) && (t2.equals(XmlShort.type.getName()) || t2.equals(XmlByte.type.getName()))) {
            return t1;
        }
        if (t2.equals(XmlLong.type.getName()) && (t1.equals(XmlInt.type.getName()) || t1.equals(XmlShort.type.getName()) || t1.equals(XmlByte.type.getName()))) {
            return t2;
        }
        if (t1.equals(XmlLong.type.getName()) && (t2.equals(XmlInt.type.getName()) || t2.equals(XmlShort.type.getName()) || t2.equals(XmlByte.type.getName()))) {
            return t1;
        }
        if (t2.equals(XmlInteger.type.getName()) && (t1.equals(XmlLong.type.getName()) || t1.equals(XmlInt.type.getName()) || t1.equals(XmlShort.type.getName()) || t1.equals(XmlByte.type.getName()))) {
            return t2;
        }
        if (t1.equals(XmlInteger.type.getName()) && (t2.equals(XmlLong.type.getName()) || t2.equals(XmlInt.type.getName()) || t2.equals(XmlShort.type.getName()) || t2.equals(XmlByte.type.getName()))) {
            return t1;
        }
        if (t2.equals(XmlFloat.type.getName()) && (t1.equals(XmlInteger.type.getName()) || t1.equals(XmlLong.type.getName()) || t1.equals(XmlInt.type.getName()) || t1.equals(XmlShort.type.getName()) || t1.equals(XmlByte.type.getName()))) {
            return t2;
        }
        if (t1.equals(XmlFloat.type.getName()) && (t2.equals(XmlInteger.type.getName()) || t2.equals(XmlLong.type.getName()) || t2.equals(XmlInt.type.getName()) || t2.equals(XmlShort.type.getName()) || t2.equals(XmlByte.type.getName()))) {
            return t1;
        }
        return XmlString.type.getName();
    }

    protected void combineAttributesOfTypes(Type into, Type from) {
        for (int i = 0; i < from.getAttributes().size(); i++) {
            Attribute fromAtt = (Attribute) from.getAttributes().get(i);
            int j = 0;
            while (true) {
                if (j < into.getAttributes().size()) {
                    Attribute intoAtt = (Attribute) into.getAttributes().get(j);
                    if (!intoAtt.getName().equals(fromAtt.getName())) {
                        j++;
                    } else {
                        intoAtt.getType().setName(combineToMoreGeneralSimpleType(intoAtt.getType().getName(), fromAtt.getType().getName()));
                        break;
                    }
                } else {
                    into.addAttribute(fromAtt);
                    break;
                }
            }
        }
        for (int i2 = 0; i2 < into.getAttributes().size(); i2++) {
            Attribute intoAtt2 = (Attribute) into.getAttributes().get(i2);
            for (int j2 = 0; j2 < from.getAttributes().size(); j2++) {
                if (((Attribute) from.getAttributes().get(j2)).getName().equals(intoAtt2.getName())) {
                }
            }
            intoAtt2.setOptional(true);
        }
    }

    protected void combineElementsOfTypes(Type into, Type from, boolean makeElementsOptional, Inst2XsdOptions options) {
        boolean needsUnboundedChoice = false;
        if (into.getTopParticleForComplexOrMixedContent() != 1 || from.getTopParticleForComplexOrMixedContent() != 1) {
            needsUnboundedChoice = true;
        }
        List res = new ArrayList();
        int fromStartingIndex = 0;
        int fromMatchedIndex = -1;
        int intoMatchedIndex = -1;
        for (int i = 0; !needsUnboundedChoice && i < into.getElements().size(); i++) {
            Element intoElement = (Element) into.getElements().get(i);
            int j = fromStartingIndex;
            while (true) {
                if (j >= from.getElements().size()) {
                    break;
                }
                Element fromElement = (Element) from.getElements().get(j);
                if (!intoElement.getName().equals(fromElement.getName())) {
                    j++;
                } else {
                    fromMatchedIndex = j;
                    break;
                }
            }
            if (fromMatchedIndex < fromStartingIndex) {
                res.add(intoElement);
                intoElement.setMinOccurs(0);
            } else {
                int j2 = fromStartingIndex;
                while (true) {
                    if (j2 >= fromMatchedIndex) {
                        break;
                    }
                    Element fromCandidate = (Element) from.getElements().get(j2);
                    for (int i2 = i + 1; i2 < into.getElements().size(); i2++) {
                        Element intoCandidate = (Element) into.getElements().get(i2);
                        if (fromCandidate.getName().equals(intoCandidate.getName())) {
                            intoMatchedIndex = i2;
                            break;
                        }
                    }
                    j2++;
                }
                if (intoMatchedIndex < i) {
                    for (int j3 = fromStartingIndex; j3 < fromMatchedIndex; j3++) {
                        Element fromCandidate2 = (Element) from.getElements().get(j3);
                        res.add(fromCandidate2);
                        fromCandidate2.setMinOccurs(0);
                    }
                    res.add(intoElement);
                    Element fromMatchedElement = (Element) from.getElements().get(fromMatchedIndex);
                    if (fromMatchedElement.getMinOccurs() <= 0) {
                        intoElement.setMinOccurs(0);
                    }
                    if (fromMatchedElement.getMaxOccurs() == -1) {
                        intoElement.setMaxOccurs(-1);
                    }
                    combineTypes(intoElement.getType(), fromMatchedElement.getType(), options);
                    combineElementComments(intoElement, fromMatchedElement);
                    fromStartingIndex = fromMatchedIndex + 1;
                } else {
                    needsUnboundedChoice = true;
                }
            }
        }
        for (int j4 = fromStartingIndex; j4 < from.getElements().size(); j4++) {
            Element remainingFromElement = (Element) from.getElements().get(j4);
            res.add(remainingFromElement);
            remainingFromElement.setMinOccurs(0);
        }
        if (needsUnboundedChoice) {
            into.setTopParticleForComplexOrMixedContent(2);
            for (int j5 = 0; j5 < from.getElements().size(); j5++) {
                Element fromElem = (Element) from.getElements().get(j5);
                int i3 = 0;
                while (true) {
                    if (i3 < into.getElements().size()) {
                        Element intoElem = (Element) into.getElements().get(i3);
                        intoElem.setMinOccurs(1);
                        intoElem.setMaxOccurs(1);
                        if (intoElem != fromElem) {
                            if (!intoElem.getName().equals(fromElem.getName())) {
                                i3++;
                            } else {
                                combineTypes(intoElem.getType(), fromElem.getType(), options);
                                combineElementComments(intoElem, fromElem);
                                break;
                            }
                        }
                    } else {
                        into.addElement(fromElem);
                        fromElem.setMinOccurs(1);
                        fromElem.setMaxOccurs(1);
                        break;
                    }
                }
            }
            return;
        }
        into.setElements(res);
    }

    protected void combineElementComments(Element into, Element with) {
        if (with.getComment() != null && with.getComment().length() > 0) {
            if (into.getComment() == null) {
                into.setComment(with.getComment());
            } else {
                into.setComment(into.getComment() + with.getComment());
            }
        }
    }
}
