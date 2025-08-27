package com.itextpdf.kernel.pdf.tagutils;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfIndirectReference;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.tagging.IStructureNode;
import com.itextpdf.kernel.pdf.tagging.PdfMcr;
import com.itextpdf.kernel.pdf.tagging.PdfNamespace;
import com.itextpdf.kernel.pdf.tagging.PdfObjRef;
import com.itextpdf.kernel.pdf.tagging.PdfStructElem;
import com.itextpdf.kernel.pdf.tagging.PdfStructTreeRoot;
import com.itextpdf.kernel.pdf.tagging.StandardNamespaces;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/tagutils/TagStructureContext.class */
public class TagStructureContext {
    private static final Set<String> allowedRootTagRoles = new HashSet();
    private PdfDocument document;
    private PdfStructElem rootTagElement;
    protected TagTreePointer autoTaggingPointer;
    private PdfVersion tagStructureTargetVersion;
    private boolean forbidUnknownRoles;
    private WaitingTagsManager waitingTagsManager;
    private Set<PdfDictionary> namespaces;
    private Map<String, PdfNamespace> nameToNamespace;
    private PdfNamespace documentDefaultNamespace;

    static {
        allowedRootTagRoles.add(StandardRoles.DOCUMENT);
        allowedRootTagRoles.add(StandardRoles.PART);
        allowedRootTagRoles.add(StandardRoles.ART);
        allowedRootTagRoles.add(StandardRoles.SECT);
        allowedRootTagRoles.add(StandardRoles.DIV);
    }

    public TagStructureContext(PdfDocument document) {
        this(document, document.getPdfVersion());
    }

    public TagStructureContext(PdfDocument document, PdfVersion tagStructureTargetVersion) {
        this.document = document;
        if (!document.isTagged()) {
            throw new PdfException(PdfException.MustBeATaggedDocument);
        }
        this.waitingTagsManager = new WaitingTagsManager();
        this.namespaces = new LinkedHashSet();
        this.nameToNamespace = new HashMap();
        this.tagStructureTargetVersion = tagStructureTargetVersion;
        this.forbidUnknownRoles = true;
        if (targetTagStructureVersionIs2()) {
            initRegisteredNamespaces();
            setNamespaceForNewTagsBasedOnExistingRoot();
        }
    }

    public TagStructureContext setForbidUnknownRoles(boolean forbidUnknownRoles) {
        this.forbidUnknownRoles = forbidUnknownRoles;
        return this;
    }

    public PdfVersion getTagStructureTargetVersion() {
        return this.tagStructureTargetVersion;
    }

    public TagTreePointer getAutoTaggingPointer() {
        if (this.autoTaggingPointer == null) {
            this.autoTaggingPointer = new TagTreePointer(this.document);
        }
        return this.autoTaggingPointer;
    }

    public WaitingTagsManager getWaitingTagsManager() {
        return this.waitingTagsManager;
    }

    public PdfNamespace getDocumentDefaultNamespace() {
        return this.documentDefaultNamespace;
    }

    public TagStructureContext setDocumentDefaultNamespace(PdfNamespace namespace) {
        this.documentDefaultNamespace = namespace;
        return this;
    }

    public PdfNamespace fetchNamespace(String namespaceName) {
        PdfNamespace ns = this.nameToNamespace.get(namespaceName);
        if (ns == null) {
            ns = new PdfNamespace(namespaceName);
            this.nameToNamespace.put(namespaceName, ns);
        }
        return ns;
    }

    public IRoleMappingResolver getRoleMappingResolver(String role) {
        return getRoleMappingResolver(role, null);
    }

    public IRoleMappingResolver getRoleMappingResolver(String role, PdfNamespace namespace) {
        if (targetTagStructureVersionIs2()) {
            return new RoleMappingResolverPdf2(role, namespace, getDocument());
        }
        return new RoleMappingResolver(role, getDocument());
    }

    public boolean checkIfRoleShallBeMappedToStandardRole(String role, PdfNamespace namespace) {
        return resolveMappingToStandardOrDomainSpecificRole(role, namespace) != null;
    }

    public IRoleMappingResolver resolveMappingToStandardOrDomainSpecificRole(String role, PdfNamespace namespace) {
        IRoleMappingResolver mappingResolver = getRoleMappingResolver(role, namespace);
        mappingResolver.resolveNextMapping();
        int i = 0;
        while (mappingResolver.currentRoleShallBeMappedToStandard()) {
            i++;
            if (i > 100) {
                Logger logger = LoggerFactory.getLogger((Class<?>) TagStructureContext.class);
                logger.error(composeTooMuchTransitiveMappingsException(role, namespace));
                return null;
            }
            if (!mappingResolver.resolveNextMapping()) {
                return null;
            }
        }
        return mappingResolver;
    }

    public TagTreePointer removeAnnotationTag(PdfAnnotation annotation) {
        PdfObjRef objRef;
        PdfStructElem structElem = null;
        PdfDictionary annotDic = annotation.getPdfObject();
        PdfNumber structParentIndex = (PdfNumber) annotDic.get(PdfName.StructParent);
        if (structParentIndex != null && (objRef = this.document.getStructTreeRoot().findObjRefByStructParentIndex(annotDic.getAsDictionary(PdfName.P), structParentIndex.intValue())) != null) {
            PdfStructElem parent = (PdfStructElem) objRef.getParent();
            parent.removeKid(objRef);
            structElem = parent;
        }
        annotDic.remove(PdfName.StructParent);
        annotDic.setModified();
        if (structElem != null) {
            return new TagTreePointer(this.document).setCurrentStructElem(structElem);
        }
        return null;
    }

    public TagTreePointer removeContentItem(PdfPage page, int mcid) {
        PdfMcr mcr = this.document.getStructTreeRoot().findMcrByMcid(page.getPdfObject(), mcid);
        if (mcr == null) {
            return null;
        }
        PdfStructElem parent = (PdfStructElem) mcr.getParent();
        parent.removeKid(mcr);
        return new TagTreePointer(this.document).setCurrentStructElem(parent);
    }

    public TagStructureContext removePageTags(PdfPage page) {
        PdfStructTreeRoot structTreeRoot = this.document.getStructTreeRoot();
        Collection<PdfMcr> pageMcrs = structTreeRoot.getPageMarkedContentReferences(page);
        if (pageMcrs != null) {
            List<PdfMcr> mcrsList = new ArrayList<>(pageMcrs);
            for (PdfMcr mcr : mcrsList) {
                removePageTagFromParent(mcr, mcr.getParent());
            }
        }
        return this;
    }

    public TagStructureContext flushPageTags(PdfPage page) {
        PdfStructTreeRoot structTreeRoot = this.document.getStructTreeRoot();
        Collection<PdfMcr> pageMcrs = structTreeRoot.getPageMarkedContentReferences(page);
        if (pageMcrs != null) {
            for (PdfMcr mcr : pageMcrs) {
                PdfStructElem parent = (PdfStructElem) mcr.getParent();
                flushParentIfBelongsToPage(parent, page);
            }
        }
        return this;
    }

    public void normalizeDocumentRootTag() {
        boolean forbid = this.forbidUnknownRoles;
        this.forbidUnknownRoles = false;
        List<IStructureNode> rootKids = this.document.getStructTreeRoot().getKids();
        IRoleMappingResolver mapping = null;
        if (rootKids.size() > 0) {
            PdfStructElem firstKid = (PdfStructElem) rootKids.get(0);
            mapping = resolveMappingToStandardOrDomainSpecificRole(firstKid.getRole().getValue(), firstKid.getNamespace());
        }
        if (rootKids.size() == 1 && mapping != null && mapping.currentRoleIsStandard() && isRoleAllowedToBeRoot(mapping.getRole())) {
            this.rootTagElement = (PdfStructElem) rootKids.get(0);
        } else {
            this.document.getStructTreeRoot().getPdfObject().remove(PdfName.K);
            this.rootTagElement = new RootTagNormalizer(this, this.rootTagElement, this.document).makeSingleStandardRootTag(rootKids);
        }
        this.forbidUnknownRoles = forbid;
    }

    public void prepareToDocumentClosing() {
        this.waitingTagsManager.removeAllWaitingStates();
        actualizeNamespacesInStructTreeRoot();
    }

    public PdfStructElem getPointerStructElem(TagTreePointer pointer) {
        return pointer.getCurrentStructElem();
    }

    public TagTreePointer createPointerForStructElem(PdfStructElem structElem) {
        return new TagTreePointer(structElem, this.document);
    }

    PdfStructElem getRootTag() {
        if (this.rootTagElement == null) {
            normalizeDocumentRootTag();
        }
        return this.rootTagElement;
    }

    PdfDocument getDocument() {
        return this.document;
    }

    void ensureNamespaceRegistered(PdfNamespace namespace) {
        if (namespace != null) {
            PdfDictionary namespaceObj = namespace.getPdfObject();
            if (!this.namespaces.contains(namespaceObj)) {
                this.namespaces.add(namespaceObj);
            }
            this.nameToNamespace.put(namespace.getNamespaceName(), namespace);
        }
    }

    void throwExceptionIfRoleIsInvalid(AccessibilityProperties properties, PdfNamespace pointerCurrentNamespace) {
        PdfNamespace namespace = properties.getNamespace();
        if (namespace == null) {
            namespace = pointerCurrentNamespace;
        }
        throwExceptionIfRoleIsInvalid(properties.getRole(), namespace);
    }

    void throwExceptionIfRoleIsInvalid(String role, PdfNamespace namespace) {
        if (!checkIfRoleShallBeMappedToStandardRole(role, namespace)) {
            String exMessage = composeInvalidRoleException(role, namespace);
            if (this.forbidUnknownRoles) {
                throw new PdfException(exMessage);
            }
            Logger logger = LoggerFactory.getLogger((Class<?>) TagStructureContext.class);
            logger.warn(exMessage);
        }
    }

    boolean targetTagStructureVersionIs2() {
        return PdfVersion.PDF_2_0.compareTo(this.tagStructureTargetVersion) <= 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0073, code lost:
    
        r8 = false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void flushParentIfBelongsToPage(com.itextpdf.kernel.pdf.tagging.PdfStructElem r5, com.itextpdf.kernel.pdf.PdfPage r6) {
        /*
            r4 = this;
            r0 = r5
            boolean r0 = r0.isFlushed()
            if (r0 != 0) goto L22
            r0 = r4
            com.itextpdf.kernel.pdf.tagutils.WaitingTagsManager r0 = r0.waitingTagsManager
            r1 = r5
            com.itextpdf.kernel.pdf.PdfObject r1 = r1.getPdfObject()
            com.itextpdf.kernel.pdf.PdfDictionary r1 = (com.itextpdf.kernel.pdf.PdfDictionary) r1
            java.lang.Object r0 = r0.getObjForStructDict(r1)
            if (r0 != 0) goto L22
            r0 = r5
            com.itextpdf.kernel.pdf.tagging.IStructureNode r0 = r0.getParent()
            boolean r0 = r0 instanceof com.itextpdf.kernel.pdf.tagging.PdfStructTreeRoot
            if (r0 == 0) goto L23
        L22:
            return
        L23:
            r0 = r5
            java.util.List r0 = r0.getKids()
            r7 = r0
            r0 = 1
            r8 = r0
            r0 = r7
            java.util.Iterator r0 = r0.iterator()
            r9 = r0
        L33:
            r0 = r9
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L8d
            r0 = r9
            java.lang.Object r0 = r0.next()
            com.itextpdf.kernel.pdf.tagging.IStructureNode r0 = (com.itextpdf.kernel.pdf.tagging.IStructureNode) r0
            r10 = r0
            r0 = r10
            boolean r0 = r0 instanceof com.itextpdf.kernel.pdf.tagging.PdfMcr
            if (r0 == 0) goto L7c
            r0 = r10
            com.itextpdf.kernel.pdf.tagging.PdfMcr r0 = (com.itextpdf.kernel.pdf.tagging.PdfMcr) r0
            com.itextpdf.kernel.pdf.PdfDictionary r0 = r0.getPageObject()
            r11 = r0
            r0 = r11
            boolean r0 = r0.isFlushed()
            if (r0 != 0) goto L79
            r0 = r6
            if (r0 == 0) goto L73
            r0 = r11
            r1 = r6
            com.itextpdf.kernel.pdf.PdfObject r1 = r1.getPdfObject()
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L79
        L73:
            r0 = 0
            r8 = r0
            goto L8d
        L79:
            goto L8a
        L7c:
            r0 = r10
            boolean r0 = r0 instanceof com.itextpdf.kernel.pdf.tagging.PdfStructElem
            if (r0 == 0) goto L8a
            r0 = 0
            r8 = r0
            goto L8d
        L8a:
            goto L33
        L8d:
            r0 = r8
            if (r0 == 0) goto Lae
            r0 = r5
            com.itextpdf.kernel.pdf.tagging.IStructureNode r0 = r0.getParent()
            r9 = r0
            r0 = r5
            r0.flush()
            r0 = r9
            boolean r0 = r0 instanceof com.itextpdf.kernel.pdf.tagging.PdfStructElem
            if (r0 == 0) goto Lae
            r0 = r4
            r1 = r9
            com.itextpdf.kernel.pdf.tagging.PdfStructElem r1 = (com.itextpdf.kernel.pdf.tagging.PdfStructElem) r1
            r2 = r6
            r0.flushParentIfBelongsToPage(r1, r2)
        Lae:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.kernel.pdf.tagutils.TagStructureContext.flushParentIfBelongsToPage(com.itextpdf.kernel.pdf.tagging.PdfStructElem, com.itextpdf.kernel.pdf.PdfPage):void");
    }

    private boolean isRoleAllowedToBeRoot(String role) {
        if (targetTagStructureVersionIs2()) {
            return StandardRoles.DOCUMENT.equals(role);
        }
        return allowedRootTagRoles.contains(role);
    }

    private void setNamespaceForNewTagsBasedOnExistingRoot() {
        String nsStr;
        List<IStructureNode> rootKids = this.document.getStructTreeRoot().getKids();
        if (rootKids.size() > 0) {
            PdfStructElem firstKid = (PdfStructElem) rootKids.get(0);
            IRoleMappingResolver resolvedMapping = resolveMappingToStandardOrDomainSpecificRole(firstKid.getRole().getValue(), firstKid.getNamespace());
            if (resolvedMapping == null || !resolvedMapping.currentRoleIsStandard()) {
                Logger logger = LoggerFactory.getLogger((Class<?>) TagStructureContext.class);
                if (firstKid.getNamespace() != null) {
                    nsStr = firstKid.getNamespace().getNamespaceName();
                } else {
                    nsStr = StandardNamespaces.getDefault();
                }
                logger.warn(MessageFormat.format(LogMessageConstant.EXISTING_TAG_STRUCTURE_ROOT_IS_NOT_STANDARD, firstKid.getRole().getValue(), nsStr));
            }
            if (resolvedMapping == null || !StandardNamespaces.PDF_1_7.equals(resolvedMapping.getNamespace().getNamespaceName())) {
                this.documentDefaultNamespace = fetchNamespace(StandardNamespaces.PDF_2_0);
                return;
            }
            return;
        }
        this.documentDefaultNamespace = fetchNamespace(StandardNamespaces.PDF_2_0);
    }

    private String composeInvalidRoleException(String role, PdfNamespace namespace) {
        return composeExceptionBasedOnNamespacePresence(role, namespace, PdfException.RoleIsNotMappedToAnyStandardRole, PdfException.RoleInNamespaceIsNotMappedToAnyStandardRole);
    }

    private String composeTooMuchTransitiveMappingsException(String role, PdfNamespace namespace) {
        return composeExceptionBasedOnNamespacePresence(role, namespace, LogMessageConstant.CANNOT_RESOLVE_ROLE_TOO_MUCH_TRANSITIVE_MAPPINGS, LogMessageConstant.CANNOT_RESOLVE_ROLE_IN_NAMESPACE_TOO_MUCH_TRANSITIVE_MAPPINGS);
    }

    private void initRegisteredNamespaces() {
        PdfStructTreeRoot structTreeRoot = this.document.getStructTreeRoot();
        for (PdfNamespace namespace : structTreeRoot.getNamespaces()) {
            this.namespaces.add(namespace.getPdfObject());
            this.nameToNamespace.put(namespace.getNamespaceName(), namespace);
        }
    }

    private void actualizeNamespacesInStructTreeRoot() {
        if (this.namespaces.size() > 0) {
            PdfStructTreeRoot structTreeRoot = getDocument().getStructTreeRoot();
            PdfArray rootNamespaces = structTreeRoot.getNamespacesObject();
            Set<PdfDictionary> newNamespaces = new LinkedHashSet<>(this.namespaces);
            for (int i = 0; i < rootNamespaces.size(); i++) {
                newNamespaces.remove(rootNamespaces.getAsDictionary(i));
            }
            for (PdfDictionary newNs : newNamespaces) {
                rootNamespaces.add(newNs);
            }
            if (!newNamespaces.isEmpty()) {
                structTreeRoot.setModified();
            }
        }
    }

    private void removePageTagFromParent(IStructureNode pageTag, IStructureNode parent) {
        if (parent instanceof PdfStructElem) {
            PdfStructElem structParent = (PdfStructElem) parent;
            if (!structParent.isFlushed()) {
                structParent.removeKid(pageTag);
                PdfDictionary parentStructDict = structParent.getPdfObject();
                if (this.waitingTagsManager.getObjForStructDict(parentStructDict) == null && parent.getKids().size() == 0 && !(structParent.getParent() instanceof PdfStructTreeRoot)) {
                    removePageTagFromParent(structParent, parent.getParent());
                    PdfIndirectReference indRef = parentStructDict.getIndirectReference();
                    if (indRef != null) {
                        indRef.setFree();
                        return;
                    }
                    return;
                }
                return;
            }
            if (pageTag instanceof PdfMcr) {
                throw new PdfException(PdfException.CannotRemoveTagBecauseItsParentIsFlushed);
            }
        }
    }

    private String composeExceptionBasedOnNamespacePresence(String role, PdfNamespace namespace, String withoutNsEx, String withNsEx) {
        if (namespace == null) {
            return MessageFormat.format(withoutNsEx, role);
        }
        String nsName = namespace.getNamespaceName();
        PdfIndirectReference ref = namespace.getPdfObject().getIndirectReference();
        if (ref != null) {
            nsName = nsName + " (" + Integer.toString(ref.getObjNumber()) + SymbolConstants.SPACE_SYMBOL + Integer.toString(ref.getGenNumber()) + " obj)";
        }
        return MessageFormat.format(withNsEx, role, nsName);
    }
}
