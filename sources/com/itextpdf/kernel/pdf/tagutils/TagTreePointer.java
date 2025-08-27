package com.itextpdf.kernel.pdf.tagutils;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfIndirectReference;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.tagging.IStructureNode;
import com.itextpdf.kernel.pdf.tagging.PdfMcr;
import com.itextpdf.kernel.pdf.tagging.PdfMcrDictionary;
import com.itextpdf.kernel.pdf.tagging.PdfMcrNumber;
import com.itextpdf.kernel.pdf.tagging.PdfNamespace;
import com.itextpdf.kernel.pdf.tagging.PdfObjRef;
import com.itextpdf.kernel.pdf.tagging.PdfStructElem;
import com.itextpdf.kernel.pdf.tagging.PdfStructTreeRoot;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/tagutils/TagTreePointer.class */
public class TagTreePointer {
    private static final String MCR_MARKER = "MCR";
    private TagStructureContext tagStructureContext;
    private PdfStructElem currentStructElem;
    private PdfPage currentPage;
    private PdfStream contentStream;
    private PdfNamespace currentNamespace;
    private int nextNewKidIndex = -1;

    public TagTreePointer(PdfDocument document) {
        this.tagStructureContext = document.getTagStructureContext();
        setCurrentStructElem(this.tagStructureContext.getRootTag());
        setNamespaceForNewTags(this.tagStructureContext.getDocumentDefaultNamespace());
    }

    public TagTreePointer(TagTreePointer tagPointer) {
        this.tagStructureContext = tagPointer.tagStructureContext;
        setCurrentStructElem(tagPointer.getCurrentStructElem());
        this.currentPage = tagPointer.currentPage;
        this.contentStream = tagPointer.contentStream;
        this.currentNamespace = tagPointer.currentNamespace;
    }

    TagTreePointer(PdfStructElem structElem, PdfDocument document) {
        this.tagStructureContext = document.getTagStructureContext();
        setCurrentStructElem(structElem);
    }

    public TagTreePointer setPageForTagging(PdfPage page) {
        if (page.isFlushed()) {
            throw new PdfException(PdfException.PageAlreadyFlushed);
        }
        this.currentPage = page;
        return this;
    }

    public PdfPage getCurrentPage() {
        return this.currentPage;
    }

    public TagTreePointer setContentStreamForTagging(PdfStream contentStream) {
        this.contentStream = contentStream;
        return this;
    }

    public PdfStream getCurrentContentStream() {
        return this.contentStream;
    }

    public TagStructureContext getContext() {
        return this.tagStructureContext;
    }

    public PdfDocument getDocument() {
        return this.tagStructureContext.getDocument();
    }

    public TagTreePointer setNamespaceForNewTags(PdfNamespace namespace) {
        this.currentNamespace = namespace;
        return this;
    }

    public PdfNamespace getNamespaceForNewTags() {
        return this.currentNamespace;
    }

    public TagTreePointer addTag(String role) {
        addTag(-1, role);
        return this;
    }

    public TagTreePointer addTag(int index, String role) {
        this.tagStructureContext.throwExceptionIfRoleIsInvalid(role, this.currentNamespace);
        setNextNewKidIndex(index);
        setCurrentStructElem(addNewKid(role));
        return this;
    }

    public TagTreePointer addTag(AccessibilityProperties properties) {
        addTag(-1, properties);
        return this;
    }

    public TagTreePointer addTag(int index, AccessibilityProperties properties) {
        this.tagStructureContext.throwExceptionIfRoleIsInvalid(properties, this.currentNamespace);
        setNextNewKidIndex(index);
        setCurrentStructElem(addNewKid(properties));
        return this;
    }

    public TagTreePointer addAnnotationTag(PdfAnnotation annotation) {
        throwExceptionIfCurrentPageIsNotInited();
        PdfObjRef kid = new PdfObjRef(annotation, getCurrentStructElem(), getDocument().getNextStructParentIndex());
        if (!ensureElementPageEqualsKidPage(getCurrentStructElem(), this.currentPage.getPdfObject())) {
            ((PdfDictionary) kid.getPdfObject()).put(PdfName.Pg, this.currentPage.getPdfObject().getIndirectReference());
        }
        addNewKid(kid);
        return this;
    }

    public TagTreePointer setNextNewKidIndex(int nextNewKidIndex) {
        if (nextNewKidIndex > -1) {
            this.nextNewKidIndex = nextNewKidIndex;
        }
        return this;
    }

    public TagTreePointer removeTag() {
        PdfStructElem currentStructElem = getCurrentStructElem();
        IStructureNode parentElem = currentStructElem.getParent();
        if (parentElem instanceof PdfStructTreeRoot) {
            throw new PdfException(PdfException.CannotRemoveDocumentRootTag);
        }
        List<IStructureNode> kids = currentStructElem.getKids();
        PdfStructElem parent = (PdfStructElem) parentElem;
        if (parent.isFlushed()) {
            throw new PdfException(PdfException.CannotRemoveTagBecauseItsParentIsFlushed);
        }
        Object objForStructDict = this.tagStructureContext.getWaitingTagsManager().getObjForStructDict(currentStructElem.getPdfObject());
        this.tagStructureContext.getWaitingTagsManager().removeWaitingState(objForStructDict);
        int removedKidIndex = parent.removeKid(currentStructElem);
        PdfIndirectReference indRef = currentStructElem.getPdfObject().getIndirectReference();
        if (indRef != null) {
            indRef.setFree();
        }
        for (IStructureNode kid : kids) {
            if (kid instanceof PdfStructElem) {
                int i = removedKidIndex;
                removedKidIndex++;
                parent.addKid(i, (PdfStructElem) kid);
            } else {
                PdfMcr mcr = prepareMcrForMovingToNewParent((PdfMcr) kid, parent);
                int i2 = removedKidIndex;
                removedKidIndex++;
                parent.addKid(i2, mcr);
            }
        }
        currentStructElem.getPdfObject().clear();
        setCurrentStructElem(parent);
        return this;
    }

    public TagTreePointer relocateKid(int kidIndex, TagTreePointer pointerToNewParent) {
        if (getDocument() != pointerToNewParent.getDocument()) {
            throw new PdfException(PdfException.TagCannotBeMovedToTheAnotherDocumentsTagStructure);
        }
        if (getCurrentStructElem().isFlushed()) {
            throw new PdfException(PdfException.CannotRelocateTagWhichParentIsAlreadyFlushed);
        }
        if (isPointingToSameTag(pointerToNewParent)) {
            if (kidIndex == pointerToNewParent.nextNewKidIndex) {
                return this;
            }
            if (kidIndex < pointerToNewParent.nextNewKidIndex) {
                pointerToNewParent.setNextNewKidIndex(pointerToNewParent.nextNewKidIndex - 1);
            }
        }
        if (getCurrentStructElem().getKids().get(kidIndex) == null) {
            throw new PdfException(PdfException.CannotRelocateTagWhichIsAlreadyFlushed);
        }
        IStructureNode removedKid = getCurrentStructElem().removeKid(kidIndex, true);
        if (removedKid instanceof PdfStructElem) {
            pointerToNewParent.addNewKid((PdfStructElem) removedKid);
        } else if (removedKid instanceof PdfMcr) {
            PdfMcr mcrKid = prepareMcrForMovingToNewParent((PdfMcr) removedKid, pointerToNewParent.getCurrentStructElem());
            pointerToNewParent.addNewKid(mcrKid);
        }
        return this;
    }

    public TagTreePointer relocate(TagTreePointer pointerToNewParent) {
        if (getCurrentStructElem().getPdfObject() == this.tagStructureContext.getRootTag().getPdfObject()) {
            throw new PdfException(PdfException.CannotRelocateRootTag);
        }
        if (getCurrentStructElem().isFlushed()) {
            throw new PdfException(PdfException.CannotRelocateTagWhichIsAlreadyFlushed);
        }
        int i = getIndexInParentKidsList();
        if (i < 0) {
            throw new PdfException(PdfException.CannotRelocateTagWhichParentIsAlreadyFlushed);
        }
        new TagTreePointer(this).moveToParent().relocateKid(i, pointerToNewParent);
        return this;
    }

    public TagReference getTagReference() {
        return getTagReference(-1);
    }

    public TagReference getTagReference(int index) {
        return new TagReference(getCurrentElemEnsureIndirect(), this, index);
    }

    public TagTreePointer moveToRoot() {
        setCurrentStructElem(this.tagStructureContext.getRootTag());
        return this;
    }

    public TagTreePointer moveToParent() {
        if (getCurrentStructElem().getPdfObject() == this.tagStructureContext.getRootTag().getPdfObject()) {
            throw new PdfException(PdfException.CannotMoveToParentCurrentElementIsRoot);
        }
        PdfStructElem parent = (PdfStructElem) getCurrentStructElem().getParent();
        if (parent.isFlushed()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) TagTreePointer.class);
            logger.warn(LogMessageConstant.ATTEMPT_TO_MOVE_TO_FLUSHED_PARENT);
            moveToRoot();
        } else {
            setCurrentStructElem(parent);
        }
        return this;
    }

    public TagTreePointer moveToKid(int kidIndex) {
        IStructureNode kid = getCurrentStructElem().getKids().get(kidIndex);
        if (kid instanceof PdfStructElem) {
            setCurrentStructElem((PdfStructElem) kid);
            return this;
        }
        if (kid instanceof PdfMcr) {
            throw new PdfException(PdfException.CannotMoveToMarkedContentReference);
        }
        throw new PdfException(PdfException.CannotMoveToFlushedKid);
    }

    public TagTreePointer moveToKid(String role) {
        moveToKid(0, role);
        return this;
    }

    public TagTreePointer moveToKid(int n, String role) {
        if (MCR_MARKER.equals(role)) {
            throw new PdfException(PdfException.CannotMoveToMarkedContentReference);
        }
        List<IStructureNode> descendants = new ArrayList<>(getCurrentStructElem().getKids());
        int k = 0;
        for (int i = 0; i < descendants.size(); i++) {
            if (descendants.get(i) != null && !(descendants.get(i) instanceof PdfMcr)) {
                String descendantRole = descendants.get(i).getRole().getValue();
                if (descendantRole.equals(role)) {
                    int i2 = k;
                    k++;
                    if (i2 == n) {
                        setCurrentStructElem((PdfStructElem) descendants.get(i));
                        return this;
                    }
                }
                descendants.addAll(descendants.get(i).getKids());
            }
        }
        throw new PdfException(PdfException.NoKidWithSuchRole);
    }

    public List<String> getKidsRoles() {
        List<String> roles = new ArrayList<>();
        List<IStructureNode> kids = getCurrentStructElem().getKids();
        for (IStructureNode kid : kids) {
            if (kid == null) {
                roles.add(null);
            } else if (kid instanceof PdfStructElem) {
                roles.add(kid.getRole().getValue());
            } else {
                roles.add(MCR_MARKER);
            }
        }
        return roles;
    }

    public TagTreePointer flushTag() {
        if (getCurrentStructElem().getPdfObject() == this.tagStructureContext.getRootTag().getPdfObject()) {
            throw new PdfException(PdfException.CannotFlushDocumentRootTagBeforeDocumentIsClosed);
        }
        IStructureNode parent = this.tagStructureContext.getWaitingTagsManager().flushTag(getCurrentStructElem());
        if (parent != null) {
            setCurrentStructElem((PdfStructElem) parent);
        } else {
            setCurrentStructElem(this.tagStructureContext.getRootTag());
        }
        return this;
    }

    public TagTreePointer flushParentsIfAllKidsFlushed() {
        getContext().flushParentIfBelongsToPage(getCurrentStructElem(), null);
        return this;
    }

    public AccessibilityProperties getProperties() {
        return new BackedAccessibilityProperties(this);
    }

    public String getRole() {
        return getCurrentStructElem().getRole().getValue();
    }

    public TagTreePointer setRole(String role) {
        getCurrentStructElem().setRole(PdfStructTreeRoot.convertRoleToPdfName(role));
        return this;
    }

    public int getIndexInParentKidsList() {
        if (getCurrentStructElem().getPdfObject() == this.tagStructureContext.getRootTag().getPdfObject()) {
            return -1;
        }
        PdfStructElem parent = (PdfStructElem) getCurrentStructElem().getParent();
        if (parent.isFlushed()) {
            return -1;
        }
        PdfObject k = parent.getK();
        if (k == getCurrentStructElem().getPdfObject()) {
            return 0;
        }
        if (k.isArray()) {
            PdfArray kidsArr = (PdfArray) k;
            return kidsArr.indexOf(getCurrentStructElem().getPdfObject());
        }
        return -1;
    }

    public TagTreePointer moveToPointer(TagTreePointer tagTreePointer) {
        this.currentStructElem = tagTreePointer.currentStructElem;
        return this;
    }

    public boolean isPointingToSameTag(TagTreePointer otherPointer) {
        return getCurrentStructElem().getPdfObject().equals(otherPointer.getCurrentStructElem().getPdfObject());
    }

    int createNextMcidForStructElem(PdfStructElem elem, int index) {
        PdfMcr mcr;
        throwExceptionIfCurrentPageIsNotInited();
        if (!markedContentNotInPageStream() && ensureElementPageEqualsKidPage(elem, this.currentPage.getPdfObject())) {
            mcr = new PdfMcrNumber(this.currentPage, elem);
        } else {
            mcr = new PdfMcrDictionary(this.currentPage, elem);
            if (markedContentNotInPageStream()) {
                ((PdfDictionary) mcr.getPdfObject()).put(PdfName.Stm, this.contentStream);
            }
        }
        elem.addKid(index, mcr);
        return mcr.getMcid();
    }

    TagTreePointer setCurrentStructElem(PdfStructElem structElem) {
        if (structElem.getParent() == null) {
            throw new PdfException(PdfException.StructureElementShallContainParentObject);
        }
        this.currentStructElem = structElem;
        return this;
    }

    PdfStructElem getCurrentStructElem() {
        if (this.currentStructElem.isFlushed()) {
            throw new PdfException(PdfException.TagTreePointerIsInInvalidStateItPointsAtFlushedElementUseMoveToRoot);
        }
        PdfIndirectReference indRef = this.currentStructElem.getPdfObject().getIndirectReference();
        if (indRef != null && indRef.isFree()) {
            throw new PdfException(PdfException.TagTreePointerIsInInvalidStateItPointsAtRemovedElementUseMoveToRoot);
        }
        return this.currentStructElem;
    }

    private int getNextNewKidPosition() {
        int nextPos = this.nextNewKidIndex;
        this.nextNewKidIndex = -1;
        return nextPos;
    }

    private PdfStructElem addNewKid(String role) {
        PdfStructElem kid = new PdfStructElem(getDocument(), PdfStructTreeRoot.convertRoleToPdfName(role));
        processKidNamespace(kid);
        return addNewKid(kid);
    }

    private PdfStructElem addNewKid(AccessibilityProperties properties) {
        PdfStructElem kid = new PdfStructElem(getDocument(), PdfStructTreeRoot.convertRoleToPdfName(properties.getRole()));
        AccessibilityPropertiesToStructElem.apply(properties, kid);
        processKidNamespace(kid);
        return addNewKid(kid);
    }

    private void processKidNamespace(PdfStructElem kid) {
        PdfNamespace kidNamespace = kid.getNamespace();
        if (this.currentNamespace != null && kidNamespace == null) {
            kid.setNamespace(this.currentNamespace);
            kidNamespace = this.currentNamespace;
        }
        this.tagStructureContext.ensureNamespaceRegistered(kidNamespace);
    }

    private PdfStructElem addNewKid(PdfStructElem kid) {
        return getCurrentElemEnsureIndirect().addKid(getNextNewKidPosition(), kid);
    }

    private PdfMcr addNewKid(PdfMcr kid) {
        return getCurrentElemEnsureIndirect().addKid(getNextNewKidPosition(), kid);
    }

    private PdfStructElem getCurrentElemEnsureIndirect() {
        PdfStructElem currentStructElem = getCurrentStructElem();
        if (currentStructElem.getPdfObject().getIndirectReference() == null) {
            currentStructElem.makeIndirect(getDocument());
        }
        return currentStructElem;
    }

    private PdfMcr prepareMcrForMovingToNewParent(PdfMcr mcrKid, PdfStructElem newParent) {
        PdfObject mcrObject = mcrKid.getPdfObject();
        PdfDictionary mcrPage = mcrKid.getPageObject();
        PdfDictionary mcrDict = null;
        if (!mcrObject.isNumber()) {
            mcrDict = (PdfDictionary) mcrObject;
        }
        if ((mcrDict == null || !mcrDict.containsKey(PdfName.Pg)) && !ensureElementPageEqualsKidPage(newParent, mcrPage)) {
            if (mcrDict == null) {
                mcrDict = new PdfDictionary();
                mcrDict.put(PdfName.Type, PdfName.MCR);
                mcrDict.put(PdfName.MCID, mcrKid.getPdfObject());
            }
            mcrDict.put(PdfName.Pg, mcrPage.getIndirectReference());
        }
        if (mcrDict != null) {
            if (PdfName.MCR.equals(mcrDict.get(PdfName.Type))) {
                mcrKid = new PdfMcrDictionary(mcrDict, newParent);
            } else if (PdfName.OBJR.equals(mcrDict.get(PdfName.Type))) {
                mcrKid = new PdfObjRef(mcrDict, newParent);
            }
        } else {
            mcrKid = new PdfMcrNumber((PdfNumber) mcrObject, newParent);
        }
        return mcrKid;
    }

    private boolean ensureElementPageEqualsKidPage(PdfStructElem elem, PdfDictionary kidPage) {
        PdfObject pageObject = elem.getPdfObject().get(PdfName.Pg);
        if (pageObject == null) {
            pageObject = kidPage;
            elem.put(PdfName.Pg, kidPage.getIndirectReference());
        }
        return kidPage.equals(pageObject);
    }

    private boolean markedContentNotInPageStream() {
        return this.contentStream != null;
    }

    private void throwExceptionIfCurrentPageIsNotInited() {
        if (this.currentPage == null) {
            throw new PdfException(PdfException.PageIsNotSetForThePdfTagStructure);
        }
    }
}
