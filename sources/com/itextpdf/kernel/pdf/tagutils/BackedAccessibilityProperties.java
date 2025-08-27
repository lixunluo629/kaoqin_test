package com.itextpdf.kernel.pdf.tagutils;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.tagging.PdfNamespace;
import com.itextpdf.kernel.pdf.tagging.PdfStructElem;
import com.itextpdf.kernel.pdf.tagging.PdfStructTreeRoot;
import com.itextpdf.kernel.pdf.tagging.PdfStructureAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/tagutils/BackedAccessibilityProperties.class */
class BackedAccessibilityProperties extends AccessibilityProperties {
    private static final long serialVersionUID = 4080083623525383278L;
    private TagTreePointer pointerToBackingElem;

    BackedAccessibilityProperties(TagTreePointer pointerToBackingElem) {
        this.pointerToBackingElem = new TagTreePointer(pointerToBackingElem);
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public String getRole() {
        return getBackingElem().getRole().getValue();
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public AccessibilityProperties setRole(String role) {
        getBackingElem().setRole(PdfStructTreeRoot.convertRoleToPdfName(role));
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public String getLanguage() {
        return toUnicodeString(getBackingElem().getLang());
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public AccessibilityProperties setLanguage(String language) {
        getBackingElem().setLang(new PdfString(language, PdfEncodings.UNICODE_BIG));
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public String getActualText() {
        return toUnicodeString(getBackingElem().getActualText());
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public AccessibilityProperties setActualText(String actualText) {
        getBackingElem().setActualText(new PdfString(actualText, PdfEncodings.UNICODE_BIG));
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public String getAlternateDescription() {
        return toUnicodeString(getBackingElem().getAlt());
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public AccessibilityProperties setAlternateDescription(String alternateDescription) {
        getBackingElem().setAlt(new PdfString(alternateDescription, PdfEncodings.UNICODE_BIG));
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public String getExpansion() {
        return toUnicodeString(getBackingElem().getE());
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public AccessibilityProperties setExpansion(String expansion) {
        getBackingElem().setE(new PdfString(expansion, PdfEncodings.UNICODE_BIG));
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public AccessibilityProperties addAttributes(PdfStructureAttributes attributes) {
        return addAttributes(-1, attributes);
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public AccessibilityProperties addAttributes(int index, PdfStructureAttributes attributes) {
        if (attributes == null) {
            return this;
        }
        PdfObject attributesObject = getBackingElem().getAttributes(false);
        PdfObject combinedAttributes = AccessibilityPropertiesToStructElem.combineAttributesList(attributesObject, index, Collections.singletonList(attributes), getBackingElem().getPdfObject().getAsNumber(PdfName.R));
        getBackingElem().setAttributes(combinedAttributes);
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public AccessibilityProperties clearAttributes() {
        getBackingElem().getPdfObject().remove(PdfName.A);
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public List<PdfStructureAttributes> getAttributesList() {
        ArrayList<PdfStructureAttributes> attributesList = new ArrayList<>();
        PdfObject elemAttributesObj = getBackingElem().getAttributes(false);
        if (elemAttributesObj != null) {
            if (elemAttributesObj.isDictionary()) {
                attributesList.add(new PdfStructureAttributes((PdfDictionary) elemAttributesObj));
            } else if (elemAttributesObj.isArray()) {
                PdfArray attributesArray = (PdfArray) elemAttributesObj;
                Iterator<PdfObject> it = attributesArray.iterator();
                while (it.hasNext()) {
                    PdfObject attributeObj = it.next();
                    if (attributeObj.isDictionary()) {
                        attributesList.add(new PdfStructureAttributes((PdfDictionary) attributeObj));
                    }
                }
            }
        }
        return attributesList;
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public AccessibilityProperties setPhoneme(String phoneme) {
        getBackingElem().setPhoneme(new PdfString(phoneme));
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public String getPhoneme() {
        return toUnicodeString(getBackingElem().getPhoneme());
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public AccessibilityProperties setPhoneticAlphabet(String phoneticAlphabet) {
        getBackingElem().setPhoneticAlphabet(PdfStructTreeRoot.convertRoleToPdfName(phoneticAlphabet));
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public String getPhoneticAlphabet() {
        return getBackingElem().getPhoneticAlphabet().getValue();
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public AccessibilityProperties setNamespace(PdfNamespace namespace) {
        getBackingElem().setNamespace(namespace);
        this.pointerToBackingElem.getContext().ensureNamespaceRegistered(namespace);
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public PdfNamespace getNamespace() {
        return getBackingElem().getNamespace();
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public AccessibilityProperties addRef(TagTreePointer treePointer) {
        getBackingElem().addRef(treePointer.getCurrentStructElem());
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public List<TagTreePointer> getRefsList() {
        List<TagTreePointer> refsList = new ArrayList<>();
        for (PdfStructElem ref : getBackingElem().getRefsList()) {
            refsList.add(new TagTreePointer(ref, this.pointerToBackingElem.getDocument()));
        }
        return Collections.unmodifiableList(refsList);
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties
    public AccessibilityProperties clearRefs() {
        getBackingElem().getPdfObject().remove(PdfName.Ref);
        return this;
    }

    private PdfStructElem getBackingElem() {
        return this.pointerToBackingElem.getCurrentStructElem();
    }

    private String toUnicodeString(PdfString pdfString) {
        if (pdfString != null) {
            return pdfString.toUnicodeString();
        }
        return null;
    }
}
