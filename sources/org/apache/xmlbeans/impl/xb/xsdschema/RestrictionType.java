package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.impl.xb.xsdschema.PatternDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.TotalDigitsDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.WhiteSpaceDocument;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/RestrictionType.class */
public interface RestrictionType extends Annotated {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(RestrictionType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("restrictiontype939ftype");

    GroupRef getGroup();

    boolean isSetGroup();

    void setGroup(GroupRef groupRef);

    GroupRef addNewGroup();

    void unsetGroup();

    All getAll();

    boolean isSetAll();

    void setAll(All all);

    All addNewAll();

    void unsetAll();

    ExplicitGroup getChoice();

    boolean isSetChoice();

    void setChoice(ExplicitGroup explicitGroup);

    ExplicitGroup addNewChoice();

    void unsetChoice();

    ExplicitGroup getSequence();

    boolean isSetSequence();

    void setSequence(ExplicitGroup explicitGroup);

    ExplicitGroup addNewSequence();

    void unsetSequence();

    LocalSimpleType getSimpleType();

    boolean isSetSimpleType();

    void setSimpleType(LocalSimpleType localSimpleType);

    LocalSimpleType addNewSimpleType();

    void unsetSimpleType();

    Facet[] getMinExclusiveArray();

    Facet getMinExclusiveArray(int i);

    int sizeOfMinExclusiveArray();

    void setMinExclusiveArray(Facet[] facetArr);

    void setMinExclusiveArray(int i, Facet facet);

    Facet insertNewMinExclusive(int i);

    Facet addNewMinExclusive();

    void removeMinExclusive(int i);

    Facet[] getMinInclusiveArray();

    Facet getMinInclusiveArray(int i);

    int sizeOfMinInclusiveArray();

    void setMinInclusiveArray(Facet[] facetArr);

    void setMinInclusiveArray(int i, Facet facet);

    Facet insertNewMinInclusive(int i);

    Facet addNewMinInclusive();

    void removeMinInclusive(int i);

    Facet[] getMaxExclusiveArray();

    Facet getMaxExclusiveArray(int i);

    int sizeOfMaxExclusiveArray();

    void setMaxExclusiveArray(Facet[] facetArr);

    void setMaxExclusiveArray(int i, Facet facet);

    Facet insertNewMaxExclusive(int i);

    Facet addNewMaxExclusive();

    void removeMaxExclusive(int i);

    Facet[] getMaxInclusiveArray();

    Facet getMaxInclusiveArray(int i);

    int sizeOfMaxInclusiveArray();

    void setMaxInclusiveArray(Facet[] facetArr);

    void setMaxInclusiveArray(int i, Facet facet);

    Facet insertNewMaxInclusive(int i);

    Facet addNewMaxInclusive();

    void removeMaxInclusive(int i);

    TotalDigitsDocument.TotalDigits[] getTotalDigitsArray();

    TotalDigitsDocument.TotalDigits getTotalDigitsArray(int i);

    int sizeOfTotalDigitsArray();

    void setTotalDigitsArray(TotalDigitsDocument.TotalDigits[] totalDigitsArr);

    void setTotalDigitsArray(int i, TotalDigitsDocument.TotalDigits totalDigits);

    TotalDigitsDocument.TotalDigits insertNewTotalDigits(int i);

    TotalDigitsDocument.TotalDigits addNewTotalDigits();

    void removeTotalDigits(int i);

    NumFacet[] getFractionDigitsArray();

    NumFacet getFractionDigitsArray(int i);

    int sizeOfFractionDigitsArray();

    void setFractionDigitsArray(NumFacet[] numFacetArr);

    void setFractionDigitsArray(int i, NumFacet numFacet);

    NumFacet insertNewFractionDigits(int i);

    NumFacet addNewFractionDigits();

    void removeFractionDigits(int i);

    NumFacet[] getLengthArray();

    NumFacet getLengthArray(int i);

    int sizeOfLengthArray();

    void setLengthArray(NumFacet[] numFacetArr);

    void setLengthArray(int i, NumFacet numFacet);

    NumFacet insertNewLength(int i);

    NumFacet addNewLength();

    void removeLength(int i);

    NumFacet[] getMinLengthArray();

    NumFacet getMinLengthArray(int i);

    int sizeOfMinLengthArray();

    void setMinLengthArray(NumFacet[] numFacetArr);

    void setMinLengthArray(int i, NumFacet numFacet);

    NumFacet insertNewMinLength(int i);

    NumFacet addNewMinLength();

    void removeMinLength(int i);

    NumFacet[] getMaxLengthArray();

    NumFacet getMaxLengthArray(int i);

    int sizeOfMaxLengthArray();

    void setMaxLengthArray(NumFacet[] numFacetArr);

    void setMaxLengthArray(int i, NumFacet numFacet);

    NumFacet insertNewMaxLength(int i);

    NumFacet addNewMaxLength();

    void removeMaxLength(int i);

    NoFixedFacet[] getEnumerationArray();

    NoFixedFacet getEnumerationArray(int i);

    int sizeOfEnumerationArray();

    void setEnumerationArray(NoFixedFacet[] noFixedFacetArr);

    void setEnumerationArray(int i, NoFixedFacet noFixedFacet);

    NoFixedFacet insertNewEnumeration(int i);

    NoFixedFacet addNewEnumeration();

    void removeEnumeration(int i);

    WhiteSpaceDocument.WhiteSpace[] getWhiteSpaceArray();

    WhiteSpaceDocument.WhiteSpace getWhiteSpaceArray(int i);

    int sizeOfWhiteSpaceArray();

    void setWhiteSpaceArray(WhiteSpaceDocument.WhiteSpace[] whiteSpaceArr);

    void setWhiteSpaceArray(int i, WhiteSpaceDocument.WhiteSpace whiteSpace);

    WhiteSpaceDocument.WhiteSpace insertNewWhiteSpace(int i);

    WhiteSpaceDocument.WhiteSpace addNewWhiteSpace();

    void removeWhiteSpace(int i);

    PatternDocument.Pattern[] getPatternArray();

    PatternDocument.Pattern getPatternArray(int i);

    int sizeOfPatternArray();

    void setPatternArray(PatternDocument.Pattern[] patternArr);

    void setPatternArray(int i, PatternDocument.Pattern pattern);

    PatternDocument.Pattern insertNewPattern(int i);

    PatternDocument.Pattern addNewPattern();

    void removePattern(int i);

    Attribute[] getAttributeArray();

    Attribute getAttributeArray(int i);

    int sizeOfAttributeArray();

    void setAttributeArray(Attribute[] attributeArr);

    void setAttributeArray(int i, Attribute attribute);

    Attribute insertNewAttribute(int i);

    Attribute addNewAttribute();

    void removeAttribute(int i);

    AttributeGroupRef[] getAttributeGroupArray();

    AttributeGroupRef getAttributeGroupArray(int i);

    int sizeOfAttributeGroupArray();

    void setAttributeGroupArray(AttributeGroupRef[] attributeGroupRefArr);

    void setAttributeGroupArray(int i, AttributeGroupRef attributeGroupRef);

    AttributeGroupRef insertNewAttributeGroup(int i);

    AttributeGroupRef addNewAttributeGroup();

    void removeAttributeGroup(int i);

    Wildcard getAnyAttribute();

    boolean isSetAnyAttribute();

    void setAnyAttribute(Wildcard wildcard);

    Wildcard addNewAnyAttribute();

    void unsetAnyAttribute();

    QName getBase();

    XmlQName xgetBase();

    void setBase(QName qName);

    void xsetBase(XmlQName xmlQName);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/RestrictionType$Factory.class */
    public static final class Factory {
        public static RestrictionType newInstance() {
            return (RestrictionType) XmlBeans.getContextTypeLoader().newInstance(RestrictionType.type, null);
        }

        public static RestrictionType newInstance(XmlOptions options) {
            return (RestrictionType) XmlBeans.getContextTypeLoader().newInstance(RestrictionType.type, options);
        }

        public static RestrictionType parse(String xmlAsString) throws XmlException {
            return (RestrictionType) XmlBeans.getContextTypeLoader().parse(xmlAsString, RestrictionType.type, (XmlOptions) null);
        }

        public static RestrictionType parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (RestrictionType) XmlBeans.getContextTypeLoader().parse(xmlAsString, RestrictionType.type, options);
        }

        public static RestrictionType parse(File file) throws XmlException, IOException {
            return (RestrictionType) XmlBeans.getContextTypeLoader().parse(file, RestrictionType.type, (XmlOptions) null);
        }

        public static RestrictionType parse(File file, XmlOptions options) throws XmlException, IOException {
            return (RestrictionType) XmlBeans.getContextTypeLoader().parse(file, RestrictionType.type, options);
        }

        public static RestrictionType parse(URL u) throws XmlException, IOException {
            return (RestrictionType) XmlBeans.getContextTypeLoader().parse(u, RestrictionType.type, (XmlOptions) null);
        }

        public static RestrictionType parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (RestrictionType) XmlBeans.getContextTypeLoader().parse(u, RestrictionType.type, options);
        }

        public static RestrictionType parse(InputStream is) throws XmlException, IOException {
            return (RestrictionType) XmlBeans.getContextTypeLoader().parse(is, RestrictionType.type, (XmlOptions) null);
        }

        public static RestrictionType parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (RestrictionType) XmlBeans.getContextTypeLoader().parse(is, RestrictionType.type, options);
        }

        public static RestrictionType parse(Reader r) throws XmlException, IOException {
            return (RestrictionType) XmlBeans.getContextTypeLoader().parse(r, RestrictionType.type, (XmlOptions) null);
        }

        public static RestrictionType parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (RestrictionType) XmlBeans.getContextTypeLoader().parse(r, RestrictionType.type, options);
        }

        public static RestrictionType parse(XMLStreamReader sr) throws XmlException {
            return (RestrictionType) XmlBeans.getContextTypeLoader().parse(sr, RestrictionType.type, (XmlOptions) null);
        }

        public static RestrictionType parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (RestrictionType) XmlBeans.getContextTypeLoader().parse(sr, RestrictionType.type, options);
        }

        public static RestrictionType parse(Node node) throws XmlException {
            return (RestrictionType) XmlBeans.getContextTypeLoader().parse(node, RestrictionType.type, (XmlOptions) null);
        }

        public static RestrictionType parse(Node node, XmlOptions options) throws XmlException {
            return (RestrictionType) XmlBeans.getContextTypeLoader().parse(node, RestrictionType.type, options);
        }

        public static RestrictionType parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (RestrictionType) XmlBeans.getContextTypeLoader().parse(xis, RestrictionType.type, (XmlOptions) null);
        }

        public static RestrictionType parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (RestrictionType) XmlBeans.getContextTypeLoader().parse(xis, RestrictionType.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, RestrictionType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, RestrictionType.type, options);
        }

        private Factory() {
        }
    }
}
