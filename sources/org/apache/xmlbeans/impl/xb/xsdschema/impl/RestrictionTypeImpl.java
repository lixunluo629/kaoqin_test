package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroupRef;
import org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.Facet;
import org.apache.xmlbeans.impl.xb.xsdschema.GroupRef;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalSimpleType;
import org.apache.xmlbeans.impl.xb.xsdschema.NoFixedFacet;
import org.apache.xmlbeans.impl.xb.xsdschema.NumFacet;
import org.apache.xmlbeans.impl.xb.xsdschema.PatternDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType;
import org.apache.xmlbeans.impl.xb.xsdschema.TotalDigitsDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.WhiteSpaceDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.Wildcard;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/RestrictionTypeImpl.class */
public class RestrictionTypeImpl extends AnnotatedImpl implements RestrictionType {
    private static final long serialVersionUID = 1;
    private static final QName GROUP$0 = new QName("http://www.w3.org/2001/XMLSchema", "group");
    private static final QName ALL$2 = new QName("http://www.w3.org/2001/XMLSchema", "all");
    private static final QName CHOICE$4 = new QName("http://www.w3.org/2001/XMLSchema", "choice");
    private static final QName SEQUENCE$6 = new QName("http://www.w3.org/2001/XMLSchema", "sequence");
    private static final QName SIMPLETYPE$8 = new QName("http://www.w3.org/2001/XMLSchema", "simpleType");
    private static final QName MINEXCLUSIVE$10 = new QName("http://www.w3.org/2001/XMLSchema", "minExclusive");
    private static final QName MININCLUSIVE$12 = new QName("http://www.w3.org/2001/XMLSchema", "minInclusive");
    private static final QName MAXEXCLUSIVE$14 = new QName("http://www.w3.org/2001/XMLSchema", "maxExclusive");
    private static final QName MAXINCLUSIVE$16 = new QName("http://www.w3.org/2001/XMLSchema", "maxInclusive");
    private static final QName TOTALDIGITS$18 = new QName("http://www.w3.org/2001/XMLSchema", "totalDigits");
    private static final QName FRACTIONDIGITS$20 = new QName("http://www.w3.org/2001/XMLSchema", "fractionDigits");
    private static final QName LENGTH$22 = new QName("http://www.w3.org/2001/XMLSchema", "length");
    private static final QName MINLENGTH$24 = new QName("http://www.w3.org/2001/XMLSchema", "minLength");
    private static final QName MAXLENGTH$26 = new QName("http://www.w3.org/2001/XMLSchema", "maxLength");
    private static final QName ENUMERATION$28 = new QName("http://www.w3.org/2001/XMLSchema", "enumeration");
    private static final QName WHITESPACE$30 = new QName("http://www.w3.org/2001/XMLSchema", "whiteSpace");
    private static final QName PATTERN$32 = new QName("http://www.w3.org/2001/XMLSchema", "pattern");
    private static final QName ATTRIBUTE$34 = new QName("http://www.w3.org/2001/XMLSchema", BeanDefinitionParserDelegate.QUALIFIER_ATTRIBUTE_ELEMENT);
    private static final QName ATTRIBUTEGROUP$36 = new QName("http://www.w3.org/2001/XMLSchema", "attributeGroup");
    private static final QName ANYATTRIBUTE$38 = new QName("http://www.w3.org/2001/XMLSchema", "anyAttribute");
    private static final QName BASE$40 = new QName("", "base");

    public RestrictionTypeImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public GroupRef getGroup() {
        synchronized (monitor()) {
            check_orphaned();
            GroupRef target = (GroupRef) get_store().find_element_user(GROUP$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public boolean isSetGroup() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(GROUP$0) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setGroup(GroupRef group) {
        generatedSetterHelperImpl(group, GROUP$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public GroupRef addNewGroup() {
        GroupRef target;
        synchronized (monitor()) {
            check_orphaned();
            target = (GroupRef) get_store().add_element_user(GROUP$0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void unsetGroup() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(GROUP$0, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public All getAll() {
        synchronized (monitor()) {
            check_orphaned();
            All target = (All) get_store().find_element_user(ALL$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public boolean isSetAll() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(ALL$2) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setAll(All all) {
        generatedSetterHelperImpl(all, ALL$2, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public All addNewAll() {
        All target;
        synchronized (monitor()) {
            check_orphaned();
            target = (All) get_store().add_element_user(ALL$2);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void unsetAll() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ALL$2, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public ExplicitGroup getChoice() {
        synchronized (monitor()) {
            check_orphaned();
            ExplicitGroup target = (ExplicitGroup) get_store().find_element_user(CHOICE$4, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public boolean isSetChoice() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(CHOICE$4) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setChoice(ExplicitGroup choice) {
        generatedSetterHelperImpl(choice, CHOICE$4, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public ExplicitGroup addNewChoice() {
        ExplicitGroup target;
        synchronized (monitor()) {
            check_orphaned();
            target = (ExplicitGroup) get_store().add_element_user(CHOICE$4);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void unsetChoice() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CHOICE$4, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public ExplicitGroup getSequence() {
        synchronized (monitor()) {
            check_orphaned();
            ExplicitGroup target = (ExplicitGroup) get_store().find_element_user(SEQUENCE$6, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public boolean isSetSequence() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SEQUENCE$6) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setSequence(ExplicitGroup sequence) {
        generatedSetterHelperImpl(sequence, SEQUENCE$6, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public ExplicitGroup addNewSequence() {
        ExplicitGroup target;
        synchronized (monitor()) {
            check_orphaned();
            target = (ExplicitGroup) get_store().add_element_user(SEQUENCE$6);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void unsetSequence() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SEQUENCE$6, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public LocalSimpleType getSimpleType() {
        synchronized (monitor()) {
            check_orphaned();
            LocalSimpleType target = (LocalSimpleType) get_store().find_element_user(SIMPLETYPE$8, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public boolean isSetSimpleType() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SIMPLETYPE$8) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setSimpleType(LocalSimpleType simpleType) {
        generatedSetterHelperImpl(simpleType, SIMPLETYPE$8, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public LocalSimpleType addNewSimpleType() {
        LocalSimpleType target;
        synchronized (monitor()) {
            check_orphaned();
            target = (LocalSimpleType) get_store().add_element_user(SIMPLETYPE$8);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void unsetSimpleType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SIMPLETYPE$8, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Facet[] getMinExclusiveArray() {
        Facet[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(MINEXCLUSIVE$10, targetList);
            result = new Facet[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Facet getMinExclusiveArray(int i) {
        Facet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Facet) get_store().find_element_user(MINEXCLUSIVE$10, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public int sizeOfMinExclusiveArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(MINEXCLUSIVE$10);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setMinExclusiveArray(Facet[] minExclusiveArray) {
        check_orphaned();
        arraySetterHelper(minExclusiveArray, MINEXCLUSIVE$10);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setMinExclusiveArray(int i, Facet minExclusive) {
        generatedSetterHelperImpl(minExclusive, MINEXCLUSIVE$10, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Facet insertNewMinExclusive(int i) {
        Facet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Facet) get_store().insert_element_user(MINEXCLUSIVE$10, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Facet addNewMinExclusive() {
        Facet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Facet) get_store().add_element_user(MINEXCLUSIVE$10);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void removeMinExclusive(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(MINEXCLUSIVE$10, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Facet[] getMinInclusiveArray() {
        Facet[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(MININCLUSIVE$12, targetList);
            result = new Facet[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Facet getMinInclusiveArray(int i) {
        Facet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Facet) get_store().find_element_user(MININCLUSIVE$12, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public int sizeOfMinInclusiveArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(MININCLUSIVE$12);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setMinInclusiveArray(Facet[] minInclusiveArray) {
        check_orphaned();
        arraySetterHelper(minInclusiveArray, MININCLUSIVE$12);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setMinInclusiveArray(int i, Facet minInclusive) {
        generatedSetterHelperImpl(minInclusive, MININCLUSIVE$12, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Facet insertNewMinInclusive(int i) {
        Facet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Facet) get_store().insert_element_user(MININCLUSIVE$12, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Facet addNewMinInclusive() {
        Facet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Facet) get_store().add_element_user(MININCLUSIVE$12);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void removeMinInclusive(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(MININCLUSIVE$12, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Facet[] getMaxExclusiveArray() {
        Facet[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(MAXEXCLUSIVE$14, targetList);
            result = new Facet[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Facet getMaxExclusiveArray(int i) {
        Facet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Facet) get_store().find_element_user(MAXEXCLUSIVE$14, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public int sizeOfMaxExclusiveArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(MAXEXCLUSIVE$14);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setMaxExclusiveArray(Facet[] maxExclusiveArray) {
        check_orphaned();
        arraySetterHelper(maxExclusiveArray, MAXEXCLUSIVE$14);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setMaxExclusiveArray(int i, Facet maxExclusive) {
        generatedSetterHelperImpl(maxExclusive, MAXEXCLUSIVE$14, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Facet insertNewMaxExclusive(int i) {
        Facet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Facet) get_store().insert_element_user(MAXEXCLUSIVE$14, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Facet addNewMaxExclusive() {
        Facet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Facet) get_store().add_element_user(MAXEXCLUSIVE$14);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void removeMaxExclusive(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(MAXEXCLUSIVE$14, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Facet[] getMaxInclusiveArray() {
        Facet[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(MAXINCLUSIVE$16, targetList);
            result = new Facet[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Facet getMaxInclusiveArray(int i) {
        Facet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Facet) get_store().find_element_user(MAXINCLUSIVE$16, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public int sizeOfMaxInclusiveArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(MAXINCLUSIVE$16);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setMaxInclusiveArray(Facet[] maxInclusiveArray) {
        check_orphaned();
        arraySetterHelper(maxInclusiveArray, MAXINCLUSIVE$16);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setMaxInclusiveArray(int i, Facet maxInclusive) {
        generatedSetterHelperImpl(maxInclusive, MAXINCLUSIVE$16, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Facet insertNewMaxInclusive(int i) {
        Facet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Facet) get_store().insert_element_user(MAXINCLUSIVE$16, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Facet addNewMaxInclusive() {
        Facet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Facet) get_store().add_element_user(MAXINCLUSIVE$16);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void removeMaxInclusive(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(MAXINCLUSIVE$16, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public TotalDigitsDocument.TotalDigits[] getTotalDigitsArray() {
        TotalDigitsDocument.TotalDigits[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(TOTALDIGITS$18, targetList);
            result = new TotalDigitsDocument.TotalDigits[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public TotalDigitsDocument.TotalDigits getTotalDigitsArray(int i) {
        TotalDigitsDocument.TotalDigits target;
        synchronized (monitor()) {
            check_orphaned();
            target = (TotalDigitsDocument.TotalDigits) get_store().find_element_user(TOTALDIGITS$18, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public int sizeOfTotalDigitsArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(TOTALDIGITS$18);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setTotalDigitsArray(TotalDigitsDocument.TotalDigits[] totalDigitsArray) {
        check_orphaned();
        arraySetterHelper(totalDigitsArray, TOTALDIGITS$18);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setTotalDigitsArray(int i, TotalDigitsDocument.TotalDigits totalDigits) {
        generatedSetterHelperImpl(totalDigits, TOTALDIGITS$18, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public TotalDigitsDocument.TotalDigits insertNewTotalDigits(int i) {
        TotalDigitsDocument.TotalDigits target;
        synchronized (monitor()) {
            check_orphaned();
            target = (TotalDigitsDocument.TotalDigits) get_store().insert_element_user(TOTALDIGITS$18, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public TotalDigitsDocument.TotalDigits addNewTotalDigits() {
        TotalDigitsDocument.TotalDigits target;
        synchronized (monitor()) {
            check_orphaned();
            target = (TotalDigitsDocument.TotalDigits) get_store().add_element_user(TOTALDIGITS$18);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void removeTotalDigits(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TOTALDIGITS$18, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NumFacet[] getFractionDigitsArray() {
        NumFacet[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(FRACTIONDIGITS$20, targetList);
            result = new NumFacet[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NumFacet getFractionDigitsArray(int i) {
        NumFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NumFacet) get_store().find_element_user(FRACTIONDIGITS$20, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public int sizeOfFractionDigitsArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(FRACTIONDIGITS$20);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setFractionDigitsArray(NumFacet[] fractionDigitsArray) {
        check_orphaned();
        arraySetterHelper(fractionDigitsArray, FRACTIONDIGITS$20);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setFractionDigitsArray(int i, NumFacet fractionDigits) {
        generatedSetterHelperImpl(fractionDigits, FRACTIONDIGITS$20, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NumFacet insertNewFractionDigits(int i) {
        NumFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NumFacet) get_store().insert_element_user(FRACTIONDIGITS$20, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NumFacet addNewFractionDigits() {
        NumFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NumFacet) get_store().add_element_user(FRACTIONDIGITS$20);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void removeFractionDigits(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(FRACTIONDIGITS$20, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NumFacet[] getLengthArray() {
        NumFacet[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(LENGTH$22, targetList);
            result = new NumFacet[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NumFacet getLengthArray(int i) {
        NumFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NumFacet) get_store().find_element_user(LENGTH$22, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public int sizeOfLengthArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(LENGTH$22);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setLengthArray(NumFacet[] lengthArray) {
        check_orphaned();
        arraySetterHelper(lengthArray, LENGTH$22);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setLengthArray(int i, NumFacet length) {
        generatedSetterHelperImpl(length, LENGTH$22, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NumFacet insertNewLength(int i) {
        NumFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NumFacet) get_store().insert_element_user(LENGTH$22, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NumFacet addNewLength() {
        NumFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NumFacet) get_store().add_element_user(LENGTH$22);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void removeLength(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(LENGTH$22, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NumFacet[] getMinLengthArray() {
        NumFacet[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(MINLENGTH$24, targetList);
            result = new NumFacet[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NumFacet getMinLengthArray(int i) {
        NumFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NumFacet) get_store().find_element_user(MINLENGTH$24, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public int sizeOfMinLengthArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(MINLENGTH$24);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setMinLengthArray(NumFacet[] minLengthArray) {
        check_orphaned();
        arraySetterHelper(minLengthArray, MINLENGTH$24);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setMinLengthArray(int i, NumFacet minLength) {
        generatedSetterHelperImpl(minLength, MINLENGTH$24, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NumFacet insertNewMinLength(int i) {
        NumFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NumFacet) get_store().insert_element_user(MINLENGTH$24, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NumFacet addNewMinLength() {
        NumFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NumFacet) get_store().add_element_user(MINLENGTH$24);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void removeMinLength(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(MINLENGTH$24, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NumFacet[] getMaxLengthArray() {
        NumFacet[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(MAXLENGTH$26, targetList);
            result = new NumFacet[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NumFacet getMaxLengthArray(int i) {
        NumFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NumFacet) get_store().find_element_user(MAXLENGTH$26, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public int sizeOfMaxLengthArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(MAXLENGTH$26);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setMaxLengthArray(NumFacet[] maxLengthArray) {
        check_orphaned();
        arraySetterHelper(maxLengthArray, MAXLENGTH$26);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setMaxLengthArray(int i, NumFacet maxLength) {
        generatedSetterHelperImpl(maxLength, MAXLENGTH$26, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NumFacet insertNewMaxLength(int i) {
        NumFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NumFacet) get_store().insert_element_user(MAXLENGTH$26, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NumFacet addNewMaxLength() {
        NumFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NumFacet) get_store().add_element_user(MAXLENGTH$26);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void removeMaxLength(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(MAXLENGTH$26, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NoFixedFacet[] getEnumerationArray() {
        NoFixedFacet[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(ENUMERATION$28, targetList);
            result = new NoFixedFacet[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NoFixedFacet getEnumerationArray(int i) {
        NoFixedFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NoFixedFacet) get_store().find_element_user(ENUMERATION$28, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public int sizeOfEnumerationArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(ENUMERATION$28);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setEnumerationArray(NoFixedFacet[] enumerationArray) {
        check_orphaned();
        arraySetterHelper(enumerationArray, ENUMERATION$28);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setEnumerationArray(int i, NoFixedFacet enumeration) {
        generatedSetterHelperImpl(enumeration, ENUMERATION$28, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NoFixedFacet insertNewEnumeration(int i) {
        NoFixedFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NoFixedFacet) get_store().insert_element_user(ENUMERATION$28, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public NoFixedFacet addNewEnumeration() {
        NoFixedFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NoFixedFacet) get_store().add_element_user(ENUMERATION$28);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void removeEnumeration(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ENUMERATION$28, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public WhiteSpaceDocument.WhiteSpace[] getWhiteSpaceArray() {
        WhiteSpaceDocument.WhiteSpace[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(WHITESPACE$30, targetList);
            result = new WhiteSpaceDocument.WhiteSpace[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public WhiteSpaceDocument.WhiteSpace getWhiteSpaceArray(int i) {
        WhiteSpaceDocument.WhiteSpace target;
        synchronized (monitor()) {
            check_orphaned();
            target = (WhiteSpaceDocument.WhiteSpace) get_store().find_element_user(WHITESPACE$30, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public int sizeOfWhiteSpaceArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(WHITESPACE$30);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setWhiteSpaceArray(WhiteSpaceDocument.WhiteSpace[] whiteSpaceArray) {
        check_orphaned();
        arraySetterHelper(whiteSpaceArray, WHITESPACE$30);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setWhiteSpaceArray(int i, WhiteSpaceDocument.WhiteSpace whiteSpace) {
        generatedSetterHelperImpl(whiteSpace, WHITESPACE$30, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public WhiteSpaceDocument.WhiteSpace insertNewWhiteSpace(int i) {
        WhiteSpaceDocument.WhiteSpace target;
        synchronized (monitor()) {
            check_orphaned();
            target = (WhiteSpaceDocument.WhiteSpace) get_store().insert_element_user(WHITESPACE$30, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public WhiteSpaceDocument.WhiteSpace addNewWhiteSpace() {
        WhiteSpaceDocument.WhiteSpace target;
        synchronized (monitor()) {
            check_orphaned();
            target = (WhiteSpaceDocument.WhiteSpace) get_store().add_element_user(WHITESPACE$30);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void removeWhiteSpace(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(WHITESPACE$30, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public PatternDocument.Pattern[] getPatternArray() {
        PatternDocument.Pattern[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(PATTERN$32, targetList);
            result = new PatternDocument.Pattern[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public PatternDocument.Pattern getPatternArray(int i) {
        PatternDocument.Pattern target;
        synchronized (monitor()) {
            check_orphaned();
            target = (PatternDocument.Pattern) get_store().find_element_user(PATTERN$32, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public int sizeOfPatternArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(PATTERN$32);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setPatternArray(PatternDocument.Pattern[] patternArray) {
        check_orphaned();
        arraySetterHelper(patternArray, PATTERN$32);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setPatternArray(int i, PatternDocument.Pattern pattern) {
        generatedSetterHelperImpl(pattern, PATTERN$32, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public PatternDocument.Pattern insertNewPattern(int i) {
        PatternDocument.Pattern target;
        synchronized (monitor()) {
            check_orphaned();
            target = (PatternDocument.Pattern) get_store().insert_element_user(PATTERN$32, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public PatternDocument.Pattern addNewPattern() {
        PatternDocument.Pattern target;
        synchronized (monitor()) {
            check_orphaned();
            target = (PatternDocument.Pattern) get_store().add_element_user(PATTERN$32);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void removePattern(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PATTERN$32, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Attribute[] getAttributeArray() {
        Attribute[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(ATTRIBUTE$34, targetList);
            result = new Attribute[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Attribute getAttributeArray(int i) {
        Attribute target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Attribute) get_store().find_element_user(ATTRIBUTE$34, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public int sizeOfAttributeArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(ATTRIBUTE$34);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setAttributeArray(Attribute[] attributeArray) {
        check_orphaned();
        arraySetterHelper(attributeArray, ATTRIBUTE$34);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setAttributeArray(int i, Attribute attribute) {
        generatedSetterHelperImpl(attribute, ATTRIBUTE$34, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Attribute insertNewAttribute(int i) {
        Attribute target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Attribute) get_store().insert_element_user(ATTRIBUTE$34, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Attribute addNewAttribute() {
        Attribute target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Attribute) get_store().add_element_user(ATTRIBUTE$34);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void removeAttribute(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ATTRIBUTE$34, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public AttributeGroupRef[] getAttributeGroupArray() {
        AttributeGroupRef[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(ATTRIBUTEGROUP$36, targetList);
            result = new AttributeGroupRef[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public AttributeGroupRef getAttributeGroupArray(int i) {
        AttributeGroupRef target;
        synchronized (monitor()) {
            check_orphaned();
            target = (AttributeGroupRef) get_store().find_element_user(ATTRIBUTEGROUP$36, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public int sizeOfAttributeGroupArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(ATTRIBUTEGROUP$36);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setAttributeGroupArray(AttributeGroupRef[] attributeGroupArray) {
        check_orphaned();
        arraySetterHelper(attributeGroupArray, ATTRIBUTEGROUP$36);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setAttributeGroupArray(int i, AttributeGroupRef attributeGroup) {
        generatedSetterHelperImpl(attributeGroup, ATTRIBUTEGROUP$36, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public AttributeGroupRef insertNewAttributeGroup(int i) {
        AttributeGroupRef target;
        synchronized (monitor()) {
            check_orphaned();
            target = (AttributeGroupRef) get_store().insert_element_user(ATTRIBUTEGROUP$36, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public AttributeGroupRef addNewAttributeGroup() {
        AttributeGroupRef target;
        synchronized (monitor()) {
            check_orphaned();
            target = (AttributeGroupRef) get_store().add_element_user(ATTRIBUTEGROUP$36);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void removeAttributeGroup(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ATTRIBUTEGROUP$36, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Wildcard getAnyAttribute() {
        synchronized (monitor()) {
            check_orphaned();
            Wildcard target = (Wildcard) get_store().find_element_user(ANYATTRIBUTE$38, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public boolean isSetAnyAttribute() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(ANYATTRIBUTE$38) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setAnyAttribute(Wildcard anyAttribute) {
        generatedSetterHelperImpl(anyAttribute, ANYATTRIBUTE$38, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public Wildcard addNewAnyAttribute() {
        Wildcard target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Wildcard) get_store().add_element_user(ANYATTRIBUTE$38);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void unsetAnyAttribute() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ANYATTRIBUTE$38, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public QName getBase() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(BASE$40);
            if (target == null) {
                return null;
            }
            return target.getQNameValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public XmlQName xgetBase() {
        XmlQName target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlQName) get_store().find_attribute_user(BASE$40);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void setBase(QName base) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(BASE$40);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(BASE$40);
            }
            target.setQNameValue(base);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType
    public void xsetBase(XmlQName base) {
        synchronized (monitor()) {
            check_orphaned();
            XmlQName target = (XmlQName) get_store().find_attribute_user(BASE$40);
            if (target == null) {
                target = (XmlQName) get_store().add_attribute_user(BASE$40);
            }
            target.set(base);
        }
    }
}
