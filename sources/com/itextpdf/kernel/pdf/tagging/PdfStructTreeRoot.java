package com.itextpdf.kernel.pdf.tagging;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.VersionConforming;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.kernel.pdf.tagging.ParentTreeHandler;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/tagging/PdfStructTreeRoot.class */
public class PdfStructTreeRoot extends PdfObjectWrapper<PdfDictionary> implements IStructureNode {
    private static final long serialVersionUID = 2168384302241193868L;
    private PdfDocument document;
    private ParentTreeHandler parentTreeHandler;
    private static Map<String, PdfName> staticRoleNames = new ConcurrentHashMap();

    public PdfStructTreeRoot(PdfDocument document) {
        this((PdfDictionary) new PdfDictionary().makeIndirect(document), document);
        getPdfObject().put(PdfName.Type, PdfName.StructTreeRoot);
    }

    public PdfStructTreeRoot(PdfDictionary pdfObject, PdfDocument document) {
        super(pdfObject);
        this.document = document;
        if (this.document == null) {
            ensureObjectIsAddedToDocument(pdfObject);
            this.document = pdfObject.getIndirectReference().getDocument();
        }
        setForbidRelease();
        this.parentTreeHandler = new ParentTreeHandler(this);
        getRoleMap();
    }

    public static PdfName convertRoleToPdfName(String role) {
        PdfName name = PdfName.staticNames.get(role);
        if (name != null) {
            return name;
        }
        PdfName name2 = staticRoleNames.get(role);
        if (name2 != null) {
            return name2;
        }
        PdfName name3 = new PdfName(role);
        staticRoleNames.put(role, name3);
        return name3;
    }

    public PdfStructElem addKid(PdfStructElem structElem) {
        return addKid(-1, structElem);
    }

    public PdfStructElem addKid(int index, PdfStructElem structElem) {
        addKidObject(index, structElem.getPdfObject());
        return structElem;
    }

    @Override // com.itextpdf.kernel.pdf.tagging.IStructureNode
    public IStructureNode getParent() {
        return null;
    }

    @Override // com.itextpdf.kernel.pdf.tagging.IStructureNode
    public List<IStructureNode> getKids() {
        PdfObject k = getPdfObject().get(PdfName.K);
        List<IStructureNode> kids = new ArrayList<>();
        if (k != null) {
            if (k.isArray()) {
                PdfArray a = (PdfArray) k;
                for (int i = 0; i < a.size(); i++) {
                    ifKidIsStructElementAddToList(a.get(i), kids);
                }
            } else {
                ifKidIsStructElementAddToList(k, kids);
            }
        }
        return kids;
    }

    public PdfArray getKidsObject() {
        PdfArray k = null;
        PdfObject kObj = getPdfObject().get(PdfName.K);
        if (kObj != null && kObj.isArray()) {
            k = (PdfArray) kObj;
        }
        if (k == null) {
            k = new PdfArray();
            getPdfObject().put(PdfName.K, k);
            setModified();
            if (kObj != null) {
                k.add(kObj);
            }
        }
        return k;
    }

    public void addRoleMapping(String fromRole, String toRole) {
        PdfDictionary roleMap = getRoleMap();
        PdfObject prevVal = roleMap.put(convertRoleToPdfName(fromRole), convertRoleToPdfName(toRole));
        if (prevVal != null && (prevVal instanceof PdfName)) {
            Logger logger = LoggerFactory.getLogger((Class<?>) PdfStructTreeRoot.class);
            logger.warn(MessageFormat.format(LogMessageConstant.MAPPING_IN_STRUCT_ROOT_OVERWRITTEN, fromRole, prevVal, toRole));
        }
        if (roleMap.isIndirect()) {
            roleMap.setModified();
        } else {
            setModified();
        }
    }

    public PdfDictionary getRoleMap() {
        PdfDictionary roleMap = getPdfObject().getAsDictionary(PdfName.RoleMap);
        if (roleMap == null) {
            roleMap = new PdfDictionary();
            getPdfObject().put(PdfName.RoleMap, roleMap);
            setModified();
        }
        return roleMap;
    }

    public List<PdfNamespace> getNamespaces() {
        PdfArray namespacesArray = getPdfObject().getAsArray(PdfName.Namespaces);
        if (namespacesArray == null) {
            return Collections.emptyList();
        }
        List<PdfNamespace> namespacesList = new ArrayList<>(namespacesArray.size());
        for (int i = 0; i < namespacesArray.size(); i++) {
            namespacesList.add(new PdfNamespace(namespacesArray.getAsDictionary(i)));
        }
        return namespacesList;
    }

    public void addNamespace(PdfNamespace namespace) {
        getNamespacesObject().add(namespace.getPdfObject());
        setModified();
    }

    public PdfArray getNamespacesObject() {
        PdfArray namespacesArray = getPdfObject().getAsArray(PdfName.Namespaces);
        if (namespacesArray == null) {
            namespacesArray = new PdfArray();
            VersionConforming.validatePdfVersionForDictEntry(getDocument(), PdfVersion.PDF_2_0, PdfName.Namespaces, PdfName.StructTreeRoot);
            getPdfObject().put(PdfName.Namespaces, namespacesArray);
            setModified();
        }
        return namespacesArray;
    }

    public List<PdfFileSpec> getPronunciationLexiconsList() {
        PdfArray pronunciationLexicons = getPdfObject().getAsArray(PdfName.PronunciationLexicon);
        if (pronunciationLexicons == null) {
            return Collections.emptyList();
        }
        List<PdfFileSpec> lexiconsList = new ArrayList<>(pronunciationLexicons.size());
        for (int i = 0; i < pronunciationLexicons.size(); i++) {
            lexiconsList.add(PdfFileSpec.wrapFileSpecObject(pronunciationLexicons.get(i)));
        }
        return lexiconsList;
    }

    public void addPronunciationLexicon(PdfFileSpec pronunciationLexiconFileSpec) {
        PdfArray pronunciationLexicons = getPdfObject().getAsArray(PdfName.PronunciationLexicon);
        if (pronunciationLexicons == null) {
            pronunciationLexicons = new PdfArray();
            VersionConforming.validatePdfVersionForDictEntry(getDocument(), PdfVersion.PDF_2_0, PdfName.PronunciationLexicon, PdfName.StructTreeRoot);
            getPdfObject().put(PdfName.PronunciationLexicon, pronunciationLexicons);
        }
        pronunciationLexicons.add(pronunciationLexiconFileSpec.getPdfObject());
        setModified();
    }

    public void createParentTreeEntryForPage(PdfPage page) {
        getParentTreeHandler().createParentTreeEntryForPage(page);
    }

    public void savePageStructParentIndexIfNeeded(PdfPage page) {
        getParentTreeHandler().savePageStructParentIndexIfNeeded(page);
    }

    public Collection<PdfMcr> getPageMarkedContentReferences(PdfPage page) {
        ParentTreeHandler.PageMcrsContainer pageMcrs = getParentTreeHandler().getPageMarkedContentReferences(page);
        if (pageMcrs != null) {
            return Collections.unmodifiableCollection(pageMcrs.getAllMcrsAsCollection());
        }
        return null;
    }

    public PdfMcr findMcrByMcid(PdfDictionary pageDict, int mcid) {
        return getParentTreeHandler().findMcrByMcid(pageDict, mcid);
    }

    public PdfObjRef findObjRefByStructParentIndex(PdfDictionary pageDict, int structParentIndex) {
        return getParentTreeHandler().findObjRefByStructParentIndex(pageDict, structParentIndex);
    }

    @Override // com.itextpdf.kernel.pdf.tagging.IStructureNode
    public PdfName getRole() {
        return null;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    public void flush() {
        for (int i = 0; i < getDocument().getNumberOfPages(); i++) {
            createParentTreeEntryForPage(getDocument().getPage(i + 1));
        }
        getPdfObject().put(PdfName.ParentTree, getParentTreeHandler().buildParentTree());
        getPdfObject().put(PdfName.ParentTreeNextKey, new PdfNumber(getDocument().getNextStructParentIndex()));
        if (!getDocument().isAppendMode()) {
            flushAllKids(this);
        }
        super.flush();
    }

    public void copyTo(PdfDocument destDocument, Map<PdfPage, PdfPage> page2page) {
        StructureTreeCopier.copyTo(destDocument, page2page, getDocument());
    }

    public void copyTo(PdfDocument destDocument, int insertBeforePage, Map<PdfPage, PdfPage> page2page) {
        StructureTreeCopier.copyTo(destDocument, insertBeforePage, page2page, getDocument());
    }

    public void move(PdfPage fromPage, int insertBeforePage) {
        for (int i = 1; i <= getDocument().getNumberOfPages(); i++) {
            if (getDocument().getPage(i).isFlushed()) {
                throw new PdfException(MessageFormatUtil.format(PdfException.CannotMovePagesInPartlyFlushedDocument, Integer.valueOf(i)));
            }
        }
        StructureTreeCopier.move(getDocument(), fromPage, insertBeforePage);
    }

    public int getParentTreeNextKey() {
        return getPdfObject().getAsNumber(PdfName.ParentTreeNextKey).intValue();
    }

    public int getNextMcidForPage(PdfPage page) {
        return getParentTreeHandler().getNextMcidForPage(page);
    }

    public PdfDocument getDocument() {
        return this.document;
    }

    public void addAssociatedFile(String description, PdfFileSpec fs) {
        if (null == ((PdfDictionary) fs.getPdfObject()).get(PdfName.AFRelationship)) {
            Logger logger = LoggerFactory.getLogger((Class<?>) PdfStructTreeRoot.class);
            logger.error(LogMessageConstant.ASSOCIATED_FILE_SPEC_SHALL_INCLUDE_AFRELATIONSHIP);
        }
        if (null != description) {
            getDocument().getCatalog().getNameTree(PdfName.EmbeddedFiles).addEntry(description, fs.getPdfObject());
        }
        PdfArray afArray = getPdfObject().getAsArray(PdfName.AF);
        if (afArray == null) {
            afArray = new PdfArray();
            getPdfObject().put(PdfName.AF, afArray);
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
            getPdfObject().put(PdfName.AF, afArray);
        }
        return afArray;
    }

    ParentTreeHandler getParentTreeHandler() {
        return this.parentTreeHandler;
    }

    void addKidObject(int index, PdfDictionary structElem) {
        if (index == -1) {
            getKidsObject().add(structElem);
        } else {
            getKidsObject().add(index, structElem);
        }
        if (PdfStructElem.isStructElem(structElem)) {
            if (getPdfObject().getIndirectReference() == null) {
                throw new PdfException(PdfException.StructureElementDictionaryShallBeAnIndirectObjectInOrderToHaveChildren);
            }
            structElem.put(PdfName.P, getPdfObject());
        }
        setModified();
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return true;
    }

    private void flushAllKids(IStructureNode elem) {
        for (IStructureNode kid : elem.getKids()) {
            if ((kid instanceof PdfStructElem) && !((PdfStructElem) kid).isFlushed()) {
                flushAllKids(kid);
                ((PdfStructElem) kid).flush();
            }
        }
    }

    private void ifKidIsStructElementAddToList(PdfObject kid, List<IStructureNode> kids) {
        if (kid.isFlushed()) {
            kids.add(null);
        } else if (kid.isDictionary() && PdfStructElem.isStructElem((PdfDictionary) kid)) {
            kids.add(new PdfStructElem((PdfDictionary) kid));
        }
    }
}
