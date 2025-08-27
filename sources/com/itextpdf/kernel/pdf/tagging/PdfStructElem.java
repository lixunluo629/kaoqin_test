package com.itextpdf.kernel.pdf.tagging;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.IsoKey;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfIndirectReference;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.VersionConforming;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/tagging/PdfStructElem.class */
public class PdfStructElem extends PdfObjectWrapper<PdfDictionary> implements IStructureNode {
    private static final long serialVersionUID = 7204356181229674005L;

    public PdfStructElem(PdfDictionary pdfObject) {
        super(pdfObject);
        setForbidRelease();
    }

    public PdfStructElem(PdfDocument document, PdfName role, PdfPage page) {
        this(document, role);
        getPdfObject().put(PdfName.Pg, page.getPdfObject().getIndirectReference());
    }

    public PdfStructElem(PdfDocument document, PdfName role, PdfAnnotation annot) {
        this(document, role);
        if (annot.getPage() == null) {
            throw new PdfException(PdfException.AnnotationShallHaveReferenceToPage);
        }
        getPdfObject().put(PdfName.Pg, annot.getPage().getPdfObject().getIndirectReference());
    }

    public PdfStructElem(PdfDocument document, PdfName role) {
        this((PdfDictionary) new PdfDictionary().makeIndirect(document));
        getPdfObject().put(PdfName.Type, PdfName.StructElem);
        getPdfObject().put(PdfName.S, role);
    }

    public static boolean isStructElem(PdfDictionary dictionary) {
        return PdfName.StructElem.equals(dictionary.getAsName(PdfName.Type)) || dictionary.containsKey(PdfName.S);
    }

    public PdfObject getAttributes(boolean createNewIfNull) {
        PdfObject attributes = getPdfObject().get(PdfName.A);
        if (attributes == null && createNewIfNull) {
            attributes = new PdfDictionary();
            setAttributes(attributes);
        }
        return attributes;
    }

    public void setAttributes(PdfObject attributes) {
        put(PdfName.A, attributes);
    }

    public PdfString getLang() {
        return getPdfObject().getAsString(PdfName.Lang);
    }

    public void setLang(PdfString lang) {
        put(PdfName.Lang, lang);
    }

    public PdfString getAlt() {
        return getPdfObject().getAsString(PdfName.Alt);
    }

    public void setAlt(PdfString alt) {
        put(PdfName.Alt, alt);
    }

    public PdfString getActualText() {
        return getPdfObject().getAsString(PdfName.ActualText);
    }

    public void setActualText(PdfString actualText) {
        put(PdfName.ActualText, actualText);
    }

    public PdfString getE() {
        return getPdfObject().getAsString(PdfName.E);
    }

    public void setE(PdfString e) {
        put(PdfName.E, e);
    }

    @Override // com.itextpdf.kernel.pdf.tagging.IStructureNode
    public PdfName getRole() {
        return getPdfObject().getAsName(PdfName.S);
    }

    public void setRole(PdfName role) {
        put(PdfName.S, role);
    }

    public PdfStructElem addKid(PdfStructElem kid) {
        return addKid(-1, kid);
    }

    public PdfStructElem addKid(int index, PdfStructElem kid) {
        addKidObject(getPdfObject(), index, kid.getPdfObject());
        return kid;
    }

    public PdfMcr addKid(PdfMcr kid) {
        return addKid(-1, kid);
    }

    public PdfMcr addKid(int index, PdfMcr kid) {
        getDocEnsureIndirectForKids().getStructTreeRoot().getParentTreeHandler().registerMcr(kid);
        addKidObject(getPdfObject(), index, kid.getPdfObject());
        return kid;
    }

    public IStructureNode removeKid(int index) {
        return removeKid(index, false);
    }

    public IStructureNode removeKid(int index, boolean prepareForReAdding) {
        PdfObject k = getK();
        if (k == null || (!k.isArray() && index != 0)) {
            throw new IndexOutOfBoundsException();
        }
        if (k.isArray()) {
            PdfArray kidsArray = (PdfArray) k;
            k = kidsArray.get(index);
            kidsArray.remove(index);
            if (kidsArray.isEmpty()) {
                getPdfObject().remove(PdfName.K);
            }
        } else {
            getPdfObject().remove(PdfName.K);
        }
        setModified();
        IStructureNode removedKid = convertPdfObjectToIPdfStructElem(k);
        PdfDocument doc = getDocument();
        if ((removedKid instanceof PdfMcr) && doc != null && !prepareForReAdding) {
            doc.getStructTreeRoot().getParentTreeHandler().unregisterMcr((PdfMcr) removedKid);
        }
        return removedKid;
    }

    public int removeKid(IStructureNode kid) {
        if (kid instanceof PdfMcr) {
            PdfMcr mcr = (PdfMcr) kid;
            PdfDocument doc = getDocument();
            if (doc != null) {
                doc.getStructTreeRoot().getParentTreeHandler().unregisterMcr(mcr);
            }
            return removeKidObject(mcr.getPdfObject());
        }
        if (kid instanceof PdfStructElem) {
            return removeKidObject(((PdfStructElem) kid).getPdfObject());
        }
        return -1;
    }

    @Override // com.itextpdf.kernel.pdf.tagging.IStructureNode
    public IStructureNode getParent() {
        PdfDictionary parent = getPdfObject().getAsDictionary(PdfName.P);
        if (parent == null) {
            return null;
        }
        if (parent.isFlushed()) {
            PdfDocument pdfDoc = getDocument();
            if (pdfDoc == null) {
                return null;
            }
            PdfStructTreeRoot structTreeRoot = pdfDoc.getStructTreeRoot();
            return structTreeRoot.getPdfObject() == parent ? structTreeRoot : new PdfStructElem(parent);
        }
        if (isStructElem(parent)) {
            return new PdfStructElem(parent);
        }
        PdfDocument pdfDoc2 = getDocument();
        boolean parentIsRoot = pdfDoc2 != null && PdfName.StructTreeRoot.equals(parent.getAsName(PdfName.Type));
        boolean parentIsRoot2 = parentIsRoot || (pdfDoc2 != null && pdfDoc2.getStructTreeRoot().getPdfObject() == parent);
        if (parentIsRoot2) {
            return pdfDoc2.getStructTreeRoot();
        }
        return null;
    }

    @Override // com.itextpdf.kernel.pdf.tagging.IStructureNode
    public List<IStructureNode> getKids() {
        PdfObject k = getK();
        List<IStructureNode> kids = new ArrayList<>();
        if (k != null) {
            if (k.isArray()) {
                PdfArray a = (PdfArray) k;
                for (int i = 0; i < a.size(); i++) {
                    addKidObjectToStructElemList(a.get(i), kids);
                }
            } else {
                addKidObjectToStructElemList(k, kids);
            }
        }
        return kids;
    }

    public PdfObject getK() {
        return getPdfObject().get(PdfName.K);
    }

    public List<PdfStructElem> getRefsList() {
        PdfArray refsArray = getPdfObject().getAsArray(PdfName.Ref);
        if (refsArray == null) {
            return Collections.emptyList();
        }
        List<PdfStructElem> refs = new ArrayList<>(refsArray.size());
        for (int i = 0; i < refsArray.size(); i++) {
            refs.add(new PdfStructElem(refsArray.getAsDictionary(i)));
        }
        return refs;
    }

    public void addRef(PdfStructElem ref) {
        if (!ref.getPdfObject().isIndirect()) {
            throw new PdfException(PdfException.RefArrayItemsInStructureElementDictionaryShallBeIndirectObjects);
        }
        VersionConforming.validatePdfVersionForDictEntry(getDocument(), PdfVersion.PDF_2_0, PdfName.Ref, PdfName.StructElem);
        PdfArray refsArray = getPdfObject().getAsArray(PdfName.Ref);
        if (refsArray == null) {
            refsArray = new PdfArray();
            put(PdfName.Ref, refsArray);
        }
        refsArray.add(ref.getPdfObject());
        setModified();
    }

    public PdfNamespace getNamespace() {
        PdfDictionary nsDict = getPdfObject().getAsDictionary(PdfName.NS);
        if (nsDict != null) {
            return new PdfNamespace(nsDict);
        }
        return null;
    }

    public void setNamespace(PdfNamespace namespace) {
        VersionConforming.validatePdfVersionForDictEntry(getDocument(), PdfVersion.PDF_2_0, PdfName.NS, PdfName.StructElem);
        if (namespace != null) {
            put(PdfName.NS, namespace.getPdfObject());
        } else {
            getPdfObject().remove(PdfName.NS);
            setModified();
        }
    }

    public void setPhoneme(PdfString elementPhoneme) {
        VersionConforming.validatePdfVersionForDictEntry(getDocument(), PdfVersion.PDF_2_0, PdfName.Phoneme, PdfName.StructElem);
        put(PdfName.Phoneme, elementPhoneme);
    }

    public PdfString getPhoneme() {
        return getPdfObject().getAsString(PdfName.Phoneme);
    }

    public void setPhoneticAlphabet(PdfName phoneticAlphabet) {
        VersionConforming.validatePdfVersionForDictEntry(getDocument(), PdfVersion.PDF_2_0, PdfName.PhoneticAlphabet, PdfName.StructElem);
        put(PdfName.PhoneticAlphabet, phoneticAlphabet);
    }

    public PdfName getPhoneticAlphabet() {
        return getPdfObject().getAsName(PdfName.PhoneticAlphabet);
    }

    public void addAssociatedFile(String description, PdfFileSpec fs) {
        if (null == ((PdfDictionary) fs.getPdfObject()).get(PdfName.AFRelationship)) {
            Logger logger = LoggerFactory.getLogger((Class<?>) PdfStructElem.class);
            logger.error(LogMessageConstant.ASSOCIATED_FILE_SPEC_SHALL_INCLUDE_AFRELATIONSHIP);
        }
        if (null != description) {
            getDocument().getCatalog().getNameTree(PdfName.EmbeddedFiles).addEntry(description, fs.getPdfObject());
        }
        PdfArray afArray = getPdfObject().getAsArray(PdfName.AF);
        if (afArray == null) {
            afArray = new PdfArray();
            put(PdfName.AF, afArray);
        }
        afArray.add(fs.getPdfObject());
    }

    public void addAssociatedFile(PdfFileSpec fs) {
        addAssociatedFile(null, fs);
    }

    public PdfArray getAssociatedFiles(boolean create) {
        PdfArray afArray = getPdfObject().getAsArray(PdfName.AF);
        if (afArray == null && create) {
            afArray = new PdfArray();
            put(PdfName.AF, afArray);
        }
        return afArray;
    }

    public PdfStructElem put(PdfName key, PdfObject value) {
        getPdfObject().put(key, value);
        setModified();
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    public void flush() {
        PdfDictionary pageDict = getPdfObject().getAsDictionary(PdfName.Pg);
        if (pageDict == null || (pageDict.getIndirectReference() != null && pageDict.getIndirectReference().isFree())) {
            getPdfObject().remove(PdfName.Pg);
        }
        PdfDocument doc = getDocument();
        if (doc != null) {
            doc.checkIsoConformance(getPdfObject(), IsoKey.TAG_STRUCTURE_ELEMENT);
        }
        super.flush();
    }

    static void addKidObject(PdfDictionary parent, int index, PdfObject kid) {
        PdfArray a;
        if (parent.isFlushed()) {
            throw new PdfException(PdfException.CannotAddKidToTheFlushedElement);
        }
        if (!parent.containsKey(PdfName.P)) {
            throw new PdfException(PdfException.StructureElementShallContainParentObject, parent);
        }
        PdfObject k = parent.get(PdfName.K);
        if (k == null) {
            parent.put(PdfName.K, kid);
        } else {
            if (k instanceof PdfArray) {
                a = (PdfArray) k;
            } else {
                a = new PdfArray();
                a.add(k);
                parent.put(PdfName.K, a);
            }
            if (index == -1) {
                a.add(kid);
            } else {
                a.add(index, kid);
            }
        }
        parent.setModified();
        if ((kid instanceof PdfDictionary) && isStructElem((PdfDictionary) kid)) {
            if (!parent.isIndirect()) {
                throw new PdfException(PdfException.StructureElementDictionaryShallBeAnIndirectObjectInOrderToHaveChildren);
            }
            ((PdfDictionary) kid).put(PdfName.P, parent);
            kid.setModified();
        }
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return true;
    }

    protected PdfDocument getDocument() {
        PdfDictionary structDict = getPdfObject();
        PdfIndirectReference indRef = structDict.getIndirectReference();
        if (indRef == null && structDict.getAsDictionary(PdfName.P) != null) {
            indRef = structDict.getAsDictionary(PdfName.P).getIndirectReference();
        }
        if (indRef != null) {
            return indRef.getDocument();
        }
        return null;
    }

    private PdfDocument getDocEnsureIndirectForKids() {
        PdfDocument doc = getDocument();
        if (doc == null) {
            throw new PdfException(PdfException.StructureElementDictionaryShallBeAnIndirectObjectInOrderToHaveChildren);
        }
        return doc;
    }

    private void addKidObjectToStructElemList(PdfObject k, List<IStructureNode> list) {
        if (k.isFlushed()) {
            list.add(null);
        } else {
            list.add(convertPdfObjectToIPdfStructElem(k));
        }
    }

    private IStructureNode convertPdfObjectToIPdfStructElem(PdfObject obj) {
        IStructureNode elem = null;
        switch (obj.getType()) {
            case 3:
                PdfDictionary d = (PdfDictionary) obj;
                if (isStructElem(d)) {
                    elem = new PdfStructElem(d);
                    break;
                } else if (PdfName.MCR.equals(d.getAsName(PdfName.Type))) {
                    elem = new PdfMcrDictionary(d, this);
                    break;
                } else if (PdfName.OBJR.equals(d.getAsName(PdfName.Type))) {
                    elem = new PdfObjRef(d, this);
                    break;
                }
                break;
            case 8:
                elem = new PdfMcrNumber((PdfNumber) obj, this);
                break;
        }
        return elem;
    }

    private int removeKidObject(PdfObject kid) {
        PdfObject k = getK();
        if (k != null) {
            if (!k.isArray() && k != kid && k != kid.getIndirectReference()) {
                return -1;
            }
            int removedIndex = -1;
            if (k.isArray()) {
                PdfArray kidsArray = (PdfArray) k;
                removedIndex = removeObjectFromArray(kidsArray, kid);
            }
            if (!k.isArray() || (k.isArray() && ((PdfArray) k).isEmpty())) {
                getPdfObject().remove(PdfName.K);
                removedIndex = 0;
            }
            setModified();
            return removedIndex;
        }
        return -1;
    }

    private static int removeObjectFromArray(PdfArray array, PdfObject toRemove) {
        int i = 0;
        while (i < array.size()) {
            PdfObject obj = array.get(i);
            if (obj != toRemove && obj != toRemove.getIndirectReference()) {
                i++;
            } else {
                array.remove(i);
                break;
            }
        }
        return i;
    }
}
