package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.Facet;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalSimpleType;
import org.apache.xmlbeans.impl.xb.xsdschema.NoFixedFacet;
import org.apache.xmlbeans.impl.xb.xsdschema.NumFacet;
import org.apache.xmlbeans.impl.xb.xsdschema.PatternDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.TotalDigitsDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.WhiteSpaceDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/RestrictionDocumentImpl.class */
public class RestrictionDocumentImpl extends XmlComplexContentImpl implements RestrictionDocument {
    private static final long serialVersionUID = 1;
    private static final QName RESTRICTION$0 = new QName("http://www.w3.org/2001/XMLSchema", "restriction");

    public RestrictionDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument
    public RestrictionDocument.Restriction getRestriction() {
        synchronized (monitor()) {
            check_orphaned();
            RestrictionDocument.Restriction target = (RestrictionDocument.Restriction) get_store().find_element_user(RESTRICTION$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument
    public void setRestriction(RestrictionDocument.Restriction restriction) {
        generatedSetterHelperImpl(restriction, RESTRICTION$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument
    public RestrictionDocument.Restriction addNewRestriction() {
        RestrictionDocument.Restriction target;
        synchronized (monitor()) {
            check_orphaned();
            target = (RestrictionDocument.Restriction) get_store().add_element_user(RESTRICTION$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/RestrictionDocumentImpl$RestrictionImpl.class */
    public static class RestrictionImpl extends AnnotatedImpl implements RestrictionDocument.Restriction {
        private static final long serialVersionUID = 1;
        private static final QName SIMPLETYPE$0 = new QName("http://www.w3.org/2001/XMLSchema", "simpleType");
        private static final QName MINEXCLUSIVE$2 = new QName("http://www.w3.org/2001/XMLSchema", "minExclusive");
        private static final QName MININCLUSIVE$4 = new QName("http://www.w3.org/2001/XMLSchema", "minInclusive");
        private static final QName MAXEXCLUSIVE$6 = new QName("http://www.w3.org/2001/XMLSchema", "maxExclusive");
        private static final QName MAXINCLUSIVE$8 = new QName("http://www.w3.org/2001/XMLSchema", "maxInclusive");
        private static final QName TOTALDIGITS$10 = new QName("http://www.w3.org/2001/XMLSchema", "totalDigits");
        private static final QName FRACTIONDIGITS$12 = new QName("http://www.w3.org/2001/XMLSchema", "fractionDigits");
        private static final QName LENGTH$14 = new QName("http://www.w3.org/2001/XMLSchema", "length");
        private static final QName MINLENGTH$16 = new QName("http://www.w3.org/2001/XMLSchema", "minLength");
        private static final QName MAXLENGTH$18 = new QName("http://www.w3.org/2001/XMLSchema", "maxLength");
        private static final QName ENUMERATION$20 = new QName("http://www.w3.org/2001/XMLSchema", "enumeration");
        private static final QName WHITESPACE$22 = new QName("http://www.w3.org/2001/XMLSchema", "whiteSpace");
        private static final QName PATTERN$24 = new QName("http://www.w3.org/2001/XMLSchema", "pattern");
        private static final QName BASE$26 = new QName("", "base");

        public RestrictionImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public LocalSimpleType getSimpleType() {
            synchronized (monitor()) {
                check_orphaned();
                LocalSimpleType target = (LocalSimpleType) get_store().find_element_user(SIMPLETYPE$0, 0);
                if (target == null) {
                    return null;
                }
                return target;
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public boolean isSetSimpleType() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().count_elements(SIMPLETYPE$0) != 0;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setSimpleType(LocalSimpleType simpleType) {
            generatedSetterHelperImpl(simpleType, SIMPLETYPE$0, 0, (short) 1);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public LocalSimpleType addNewSimpleType() {
            LocalSimpleType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (LocalSimpleType) get_store().add_element_user(SIMPLETYPE$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void unsetSimpleType() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(SIMPLETYPE$0, 0);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public Facet[] getMinExclusiveArray() {
            Facet[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(MINEXCLUSIVE$2, targetList);
                result = new Facet[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public Facet getMinExclusiveArray(int i) {
            Facet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Facet) get_store().find_element_user(MINEXCLUSIVE$2, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public int sizeOfMinExclusiveArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(MINEXCLUSIVE$2);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setMinExclusiveArray(Facet[] minExclusiveArray) {
            check_orphaned();
            arraySetterHelper(minExclusiveArray, MINEXCLUSIVE$2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setMinExclusiveArray(int i, Facet minExclusive) {
            generatedSetterHelperImpl(minExclusive, MINEXCLUSIVE$2, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public Facet insertNewMinExclusive(int i) {
            Facet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Facet) get_store().insert_element_user(MINEXCLUSIVE$2, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public Facet addNewMinExclusive() {
            Facet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Facet) get_store().add_element_user(MINEXCLUSIVE$2);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void removeMinExclusive(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(MINEXCLUSIVE$2, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public Facet[] getMinInclusiveArray() {
            Facet[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(MININCLUSIVE$4, targetList);
                result = new Facet[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public Facet getMinInclusiveArray(int i) {
            Facet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Facet) get_store().find_element_user(MININCLUSIVE$4, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public int sizeOfMinInclusiveArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(MININCLUSIVE$4);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setMinInclusiveArray(Facet[] minInclusiveArray) {
            check_orphaned();
            arraySetterHelper(minInclusiveArray, MININCLUSIVE$4);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setMinInclusiveArray(int i, Facet minInclusive) {
            generatedSetterHelperImpl(minInclusive, MININCLUSIVE$4, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public Facet insertNewMinInclusive(int i) {
            Facet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Facet) get_store().insert_element_user(MININCLUSIVE$4, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public Facet addNewMinInclusive() {
            Facet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Facet) get_store().add_element_user(MININCLUSIVE$4);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void removeMinInclusive(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(MININCLUSIVE$4, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public Facet[] getMaxExclusiveArray() {
            Facet[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(MAXEXCLUSIVE$6, targetList);
                result = new Facet[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public Facet getMaxExclusiveArray(int i) {
            Facet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Facet) get_store().find_element_user(MAXEXCLUSIVE$6, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public int sizeOfMaxExclusiveArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(MAXEXCLUSIVE$6);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setMaxExclusiveArray(Facet[] maxExclusiveArray) {
            check_orphaned();
            arraySetterHelper(maxExclusiveArray, MAXEXCLUSIVE$6);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setMaxExclusiveArray(int i, Facet maxExclusive) {
            generatedSetterHelperImpl(maxExclusive, MAXEXCLUSIVE$6, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public Facet insertNewMaxExclusive(int i) {
            Facet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Facet) get_store().insert_element_user(MAXEXCLUSIVE$6, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public Facet addNewMaxExclusive() {
            Facet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Facet) get_store().add_element_user(MAXEXCLUSIVE$6);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void removeMaxExclusive(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(MAXEXCLUSIVE$6, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public Facet[] getMaxInclusiveArray() {
            Facet[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(MAXINCLUSIVE$8, targetList);
                result = new Facet[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public Facet getMaxInclusiveArray(int i) {
            Facet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Facet) get_store().find_element_user(MAXINCLUSIVE$8, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public int sizeOfMaxInclusiveArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(MAXINCLUSIVE$8);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setMaxInclusiveArray(Facet[] maxInclusiveArray) {
            check_orphaned();
            arraySetterHelper(maxInclusiveArray, MAXINCLUSIVE$8);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setMaxInclusiveArray(int i, Facet maxInclusive) {
            generatedSetterHelperImpl(maxInclusive, MAXINCLUSIVE$8, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public Facet insertNewMaxInclusive(int i) {
            Facet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Facet) get_store().insert_element_user(MAXINCLUSIVE$8, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public Facet addNewMaxInclusive() {
            Facet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Facet) get_store().add_element_user(MAXINCLUSIVE$8);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void removeMaxInclusive(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(MAXINCLUSIVE$8, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public TotalDigitsDocument.TotalDigits[] getTotalDigitsArray() {
            TotalDigitsDocument.TotalDigits[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(TOTALDIGITS$10, targetList);
                result = new TotalDigitsDocument.TotalDigits[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public TotalDigitsDocument.TotalDigits getTotalDigitsArray(int i) {
            TotalDigitsDocument.TotalDigits target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TotalDigitsDocument.TotalDigits) get_store().find_element_user(TOTALDIGITS$10, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public int sizeOfTotalDigitsArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(TOTALDIGITS$10);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setTotalDigitsArray(TotalDigitsDocument.TotalDigits[] totalDigitsArray) {
            check_orphaned();
            arraySetterHelper(totalDigitsArray, TOTALDIGITS$10);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setTotalDigitsArray(int i, TotalDigitsDocument.TotalDigits totalDigits) {
            generatedSetterHelperImpl(totalDigits, TOTALDIGITS$10, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public TotalDigitsDocument.TotalDigits insertNewTotalDigits(int i) {
            TotalDigitsDocument.TotalDigits target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TotalDigitsDocument.TotalDigits) get_store().insert_element_user(TOTALDIGITS$10, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public TotalDigitsDocument.TotalDigits addNewTotalDigits() {
            TotalDigitsDocument.TotalDigits target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TotalDigitsDocument.TotalDigits) get_store().add_element_user(TOTALDIGITS$10);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void removeTotalDigits(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(TOTALDIGITS$10, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NumFacet[] getFractionDigitsArray() {
            NumFacet[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(FRACTIONDIGITS$12, targetList);
                result = new NumFacet[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NumFacet getFractionDigitsArray(int i) {
            NumFacet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NumFacet) get_store().find_element_user(FRACTIONDIGITS$12, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public int sizeOfFractionDigitsArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(FRACTIONDIGITS$12);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setFractionDigitsArray(NumFacet[] fractionDigitsArray) {
            check_orphaned();
            arraySetterHelper(fractionDigitsArray, FRACTIONDIGITS$12);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setFractionDigitsArray(int i, NumFacet fractionDigits) {
            generatedSetterHelperImpl(fractionDigits, FRACTIONDIGITS$12, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NumFacet insertNewFractionDigits(int i) {
            NumFacet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NumFacet) get_store().insert_element_user(FRACTIONDIGITS$12, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NumFacet addNewFractionDigits() {
            NumFacet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NumFacet) get_store().add_element_user(FRACTIONDIGITS$12);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void removeFractionDigits(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(FRACTIONDIGITS$12, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NumFacet[] getLengthArray() {
            NumFacet[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(LENGTH$14, targetList);
                result = new NumFacet[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NumFacet getLengthArray(int i) {
            NumFacet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NumFacet) get_store().find_element_user(LENGTH$14, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public int sizeOfLengthArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(LENGTH$14);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setLengthArray(NumFacet[] lengthArray) {
            check_orphaned();
            arraySetterHelper(lengthArray, LENGTH$14);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setLengthArray(int i, NumFacet length) {
            generatedSetterHelperImpl(length, LENGTH$14, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NumFacet insertNewLength(int i) {
            NumFacet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NumFacet) get_store().insert_element_user(LENGTH$14, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NumFacet addNewLength() {
            NumFacet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NumFacet) get_store().add_element_user(LENGTH$14);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void removeLength(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(LENGTH$14, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NumFacet[] getMinLengthArray() {
            NumFacet[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(MINLENGTH$16, targetList);
                result = new NumFacet[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NumFacet getMinLengthArray(int i) {
            NumFacet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NumFacet) get_store().find_element_user(MINLENGTH$16, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public int sizeOfMinLengthArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(MINLENGTH$16);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setMinLengthArray(NumFacet[] minLengthArray) {
            check_orphaned();
            arraySetterHelper(minLengthArray, MINLENGTH$16);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setMinLengthArray(int i, NumFacet minLength) {
            generatedSetterHelperImpl(minLength, MINLENGTH$16, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NumFacet insertNewMinLength(int i) {
            NumFacet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NumFacet) get_store().insert_element_user(MINLENGTH$16, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NumFacet addNewMinLength() {
            NumFacet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NumFacet) get_store().add_element_user(MINLENGTH$16);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void removeMinLength(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(MINLENGTH$16, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NumFacet[] getMaxLengthArray() {
            NumFacet[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(MAXLENGTH$18, targetList);
                result = new NumFacet[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NumFacet getMaxLengthArray(int i) {
            NumFacet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NumFacet) get_store().find_element_user(MAXLENGTH$18, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public int sizeOfMaxLengthArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(MAXLENGTH$18);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setMaxLengthArray(NumFacet[] maxLengthArray) {
            check_orphaned();
            arraySetterHelper(maxLengthArray, MAXLENGTH$18);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setMaxLengthArray(int i, NumFacet maxLength) {
            generatedSetterHelperImpl(maxLength, MAXLENGTH$18, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NumFacet insertNewMaxLength(int i) {
            NumFacet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NumFacet) get_store().insert_element_user(MAXLENGTH$18, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NumFacet addNewMaxLength() {
            NumFacet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NumFacet) get_store().add_element_user(MAXLENGTH$18);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void removeMaxLength(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(MAXLENGTH$18, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NoFixedFacet[] getEnumerationArray() {
            NoFixedFacet[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(ENUMERATION$20, targetList);
                result = new NoFixedFacet[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NoFixedFacet getEnumerationArray(int i) {
            NoFixedFacet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NoFixedFacet) get_store().find_element_user(ENUMERATION$20, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public int sizeOfEnumerationArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(ENUMERATION$20);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setEnumerationArray(NoFixedFacet[] enumerationArray) {
            check_orphaned();
            arraySetterHelper(enumerationArray, ENUMERATION$20);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setEnumerationArray(int i, NoFixedFacet enumeration) {
            generatedSetterHelperImpl(enumeration, ENUMERATION$20, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NoFixedFacet insertNewEnumeration(int i) {
            NoFixedFacet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NoFixedFacet) get_store().insert_element_user(ENUMERATION$20, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public NoFixedFacet addNewEnumeration() {
            NoFixedFacet target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NoFixedFacet) get_store().add_element_user(ENUMERATION$20);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void removeEnumeration(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(ENUMERATION$20, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public WhiteSpaceDocument.WhiteSpace[] getWhiteSpaceArray() {
            WhiteSpaceDocument.WhiteSpace[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(WHITESPACE$22, targetList);
                result = new WhiteSpaceDocument.WhiteSpace[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public WhiteSpaceDocument.WhiteSpace getWhiteSpaceArray(int i) {
            WhiteSpaceDocument.WhiteSpace target;
            synchronized (monitor()) {
                check_orphaned();
                target = (WhiteSpaceDocument.WhiteSpace) get_store().find_element_user(WHITESPACE$22, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public int sizeOfWhiteSpaceArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(WHITESPACE$22);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setWhiteSpaceArray(WhiteSpaceDocument.WhiteSpace[] whiteSpaceArray) {
            check_orphaned();
            arraySetterHelper(whiteSpaceArray, WHITESPACE$22);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setWhiteSpaceArray(int i, WhiteSpaceDocument.WhiteSpace whiteSpace) {
            generatedSetterHelperImpl(whiteSpace, WHITESPACE$22, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public WhiteSpaceDocument.WhiteSpace insertNewWhiteSpace(int i) {
            WhiteSpaceDocument.WhiteSpace target;
            synchronized (monitor()) {
                check_orphaned();
                target = (WhiteSpaceDocument.WhiteSpace) get_store().insert_element_user(WHITESPACE$22, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public WhiteSpaceDocument.WhiteSpace addNewWhiteSpace() {
            WhiteSpaceDocument.WhiteSpace target;
            synchronized (monitor()) {
                check_orphaned();
                target = (WhiteSpaceDocument.WhiteSpace) get_store().add_element_user(WHITESPACE$22);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void removeWhiteSpace(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(WHITESPACE$22, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public PatternDocument.Pattern[] getPatternArray() {
            PatternDocument.Pattern[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(PATTERN$24, targetList);
                result = new PatternDocument.Pattern[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public PatternDocument.Pattern getPatternArray(int i) {
            PatternDocument.Pattern target;
            synchronized (monitor()) {
                check_orphaned();
                target = (PatternDocument.Pattern) get_store().find_element_user(PATTERN$24, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public int sizeOfPatternArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(PATTERN$24);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setPatternArray(PatternDocument.Pattern[] patternArray) {
            check_orphaned();
            arraySetterHelper(patternArray, PATTERN$24);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setPatternArray(int i, PatternDocument.Pattern pattern) {
            generatedSetterHelperImpl(pattern, PATTERN$24, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public PatternDocument.Pattern insertNewPattern(int i) {
            PatternDocument.Pattern target;
            synchronized (monitor()) {
                check_orphaned();
                target = (PatternDocument.Pattern) get_store().insert_element_user(PATTERN$24, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public PatternDocument.Pattern addNewPattern() {
            PatternDocument.Pattern target;
            synchronized (monitor()) {
                check_orphaned();
                target = (PatternDocument.Pattern) get_store().add_element_user(PATTERN$24);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void removePattern(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(PATTERN$24, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public QName getBase() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(BASE$26);
                if (target == null) {
                    return null;
                }
                return target.getQNameValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public XmlQName xgetBase() {
            XmlQName target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlQName) get_store().find_attribute_user(BASE$26);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public boolean isSetBase() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(BASE$26) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void setBase(QName base) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(BASE$26);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(BASE$26);
                }
                target.setQNameValue(base);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void xsetBase(XmlQName base) {
            synchronized (monitor()) {
                check_orphaned();
                XmlQName target = (XmlQName) get_store().find_attribute_user(BASE$26);
                if (target == null) {
                    target = (XmlQName) get_store().add_attribute_user(BASE$26);
                }
                target.set(base);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction
        public void unsetBase() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(BASE$26);
            }
        }
    }
}
