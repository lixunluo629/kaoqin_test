package com.itextpdf.kernel.pdf.tagutils;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.tagging.IStructureNode;
import com.itextpdf.kernel.pdf.tagging.PdfNamespace;
import com.itextpdf.kernel.pdf.tagging.PdfStructElem;
import com.itextpdf.kernel.pdf.tagging.StandardNamespaces;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/tagutils/RootTagNormalizer.class */
class RootTagNormalizer implements Serializable {
    private static final long serialVersionUID = -4392164598496387910L;
    private TagStructureContext context;
    private PdfStructElem rootTagElement;
    private PdfDocument document;

    RootTagNormalizer(TagStructureContext context, PdfStructElem rootTagElement, PdfDocument document) {
        this.context = context;
        this.rootTagElement = rootTagElement;
        this.document = document;
    }

    PdfStructElem makeSingleStandardRootTag(List<IStructureNode> rootKids) {
        this.document.getStructTreeRoot().makeIndirect(this.document);
        if (this.rootTagElement == null) {
            createNewRootTag();
        } else {
            this.rootTagElement.makeIndirect(this.document);
            this.document.getStructTreeRoot().addKid(this.rootTagElement);
            ensureExistingRootTagIsDocument();
        }
        addStructTreeRootKidsToTheRootTag(rootKids);
        return this.rootTagElement;
    }

    private void createNewRootTag() {
        PdfNamespace docDefaultNs = this.context.getDocumentDefaultNamespace();
        IRoleMappingResolver mapping = this.context.resolveMappingToStandardOrDomainSpecificRole(StandardRoles.DOCUMENT, docDefaultNs);
        if (mapping == null || (mapping.currentRoleIsStandard() && !StandardRoles.DOCUMENT.equals(mapping.getRole()))) {
            logCreatedRootTagHasMappingIssue(docDefaultNs, mapping);
        }
        this.rootTagElement = this.document.getStructTreeRoot().addKid(new PdfStructElem(this.document, PdfName.Document));
        if (this.context.targetTagStructureVersionIs2()) {
            this.rootTagElement.setNamespace(docDefaultNs);
            this.context.ensureNamespaceRegistered(docDefaultNs);
        }
    }

    private void ensureExistingRootTagIsDocument() {
        IRoleMappingResolver mapping = this.context.getRoleMappingResolver(this.rootTagElement.getRole().getValue(), this.rootTagElement.getNamespace());
        boolean isDocBeforeResolving = mapping.currentRoleIsStandard() && StandardRoles.DOCUMENT.equals(mapping.getRole());
        IRoleMappingResolver mapping2 = this.context.resolveMappingToStandardOrDomainSpecificRole(this.rootTagElement.getRole().getValue(), this.rootTagElement.getNamespace());
        boolean isDocAfterResolving = mapping2 != null && mapping2.currentRoleIsStandard() && StandardRoles.DOCUMENT.equals(mapping2.getRole());
        if (isDocBeforeResolving && !isDocAfterResolving) {
            logCreatedRootTagHasMappingIssue(this.rootTagElement.getNamespace(), mapping2);
            return;
        }
        if (!isDocAfterResolving) {
            wrapAllKidsInTag(this.rootTagElement, this.rootTagElement.getRole(), this.rootTagElement.getNamespace());
            this.rootTagElement.setRole(PdfName.Document);
            if (this.context.targetTagStructureVersionIs2()) {
                this.rootTagElement.setNamespace(this.context.getDocumentDefaultNamespace());
                this.context.ensureNamespaceRegistered(this.context.getDocumentDefaultNamespace());
            }
        }
    }

    private void addStructTreeRootKidsToTheRootTag(List<IStructureNode> rootKids) {
        int originalRootKidsIndex = 0;
        boolean isBeforeOriginalRoot = true;
        for (IStructureNode elem : rootKids) {
            PdfStructElem kid = (PdfStructElem) elem;
            if (kid.getPdfObject() == this.rootTagElement.getPdfObject()) {
                isBeforeOriginalRoot = false;
            } else {
                boolean kidIsDocument = PdfName.Document.equals(kid.getRole());
                if (kidIsDocument && kid.getNamespace() != null && this.context.targetTagStructureVersionIs2()) {
                    String kidNamespaceName = kid.getNamespace().getNamespaceName();
                    kidIsDocument = StandardNamespaces.PDF_1_7.equals(kidNamespaceName) || StandardNamespaces.PDF_2_0.equals(kidNamespaceName);
                }
                if (isBeforeOriginalRoot) {
                    this.rootTagElement.addKid(originalRootKidsIndex, kid);
                    originalRootKidsIndex += kidIsDocument ? kid.getKids().size() : 1;
                } else {
                    this.rootTagElement.addKid(kid);
                }
                if (kidIsDocument) {
                    removeOldRoot(kid);
                }
            }
        }
    }

    private void wrapAllKidsInTag(PdfStructElem parent, PdfName wrapTagRole, PdfNamespace wrapTagNs) {
        int kidsNum = parent.getKids().size();
        TagTreePointer tagPointer = new TagTreePointer(parent, this.document);
        tagPointer.addTag(0, wrapTagRole.getValue());
        if (this.context.targetTagStructureVersionIs2()) {
            tagPointer.getProperties().setNamespace(wrapTagNs);
        }
        TagTreePointer newParentOfKids = new TagTreePointer(tagPointer);
        tagPointer.moveToParent();
        for (int i = 0; i < kidsNum; i++) {
            tagPointer.relocateKid(1, newParentOfKids);
        }
    }

    private void removeOldRoot(PdfStructElem oldRoot) {
        TagTreePointer tagPointer = new TagTreePointer(this.document);
        tagPointer.setCurrentStructElem(oldRoot).removeTag();
    }

    private void logCreatedRootTagHasMappingIssue(PdfNamespace rootTagOriginalNs, IRoleMappingResolver mapping) {
        String mappingRole;
        String origRootTagNs = "";
        if (rootTagOriginalNs != null && rootTagOriginalNs.getNamespaceName() != null) {
            origRootTagNs = " in \"" + rootTagOriginalNs.getNamespaceName() + "\" namespace";
        }
        if (mapping != null) {
            mappingRole = " to " + SymbolConstants.QUOTES_SYMBOL + mapping.getRole() + SymbolConstants.QUOTES_SYMBOL;
            if (mapping.getNamespace() != null && !StandardNamespaces.PDF_1_7.equals(mapping.getNamespace().getNamespaceName())) {
                mappingRole = mappingRole + " in \"" + mapping.getNamespace().getNamespaceName() + "\" namespace";
            }
        } else {
            mappingRole = " to not standard role";
        }
        Logger logger = LoggerFactory.getLogger((Class<?>) RootTagNormalizer.class);
        logger.warn(MessageFormat.format(LogMessageConstant.CREATED_ROOT_TAG_HAS_MAPPING, origRootTagNs, mappingRole));
    }
}
