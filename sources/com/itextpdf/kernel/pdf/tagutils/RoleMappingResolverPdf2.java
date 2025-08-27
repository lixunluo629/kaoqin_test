package com.itextpdf.kernel.pdf.tagutils;

import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.tagging.PdfNamespace;
import com.itextpdf.kernel.pdf.tagging.PdfStructTreeRoot;
import com.itextpdf.kernel.pdf.tagging.StandardNamespaces;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/tagutils/RoleMappingResolverPdf2.class */
class RoleMappingResolverPdf2 implements IRoleMappingResolver {
    private static final long serialVersionUID = -564649110244365255L;
    private PdfName currRole;
    private PdfNamespace currNamespace;
    private PdfNamespace defaultNamespace;

    RoleMappingResolverPdf2(String role, PdfNamespace namespace, PdfDocument document) {
        this.currRole = PdfStructTreeRoot.convertRoleToPdfName(role);
        this.currNamespace = namespace;
        String defaultNsName = StandardNamespaces.getDefault();
        PdfDictionary defaultNsRoleMap = document.getStructTreeRoot().getRoleMap();
        this.defaultNamespace = new PdfNamespace(defaultNsName).setNamespaceRoleMap(defaultNsRoleMap);
        if (this.currNamespace == null) {
            this.currNamespace = this.defaultNamespace;
        }
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.IRoleMappingResolver
    public String getRole() {
        return this.currRole.getValue();
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.IRoleMappingResolver
    public PdfNamespace getNamespace() {
        return this.currNamespace;
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.IRoleMappingResolver
    public boolean currentRoleIsStandard() {
        String roleStrVal = this.currRole.getValue();
        boolean stdRole17 = StandardNamespaces.PDF_1_7.equals(this.currNamespace.getNamespaceName()) && StandardNamespaces.roleBelongsToStandardNamespace(roleStrVal, StandardNamespaces.PDF_1_7);
        boolean stdRole20 = StandardNamespaces.PDF_2_0.equals(this.currNamespace.getNamespaceName()) && StandardNamespaces.roleBelongsToStandardNamespace(roleStrVal, StandardNamespaces.PDF_2_0);
        return stdRole17 || stdRole20;
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.IRoleMappingResolver
    public boolean currentRoleShallBeMappedToStandard() {
        return (currentRoleIsStandard() || StandardNamespaces.isKnownDomainSpecificNamespace(this.currNamespace)) ? false : true;
    }

    @Override // com.itextpdf.kernel.pdf.tagutils.IRoleMappingResolver
    public boolean resolveNextMapping() {
        PdfObject mapping = null;
        PdfDictionary currNsRoleMap = this.currNamespace.getNamespaceRoleMap();
        if (currNsRoleMap != null) {
            mapping = currNsRoleMap.get(this.currRole);
        }
        if (mapping == null) {
            return false;
        }
        boolean mappingWasResolved = false;
        if (mapping.isName()) {
            this.currRole = (PdfName) mapping;
            this.currNamespace = this.defaultNamespace;
            mappingWasResolved = true;
        } else if (mapping.isArray()) {
            PdfName mappedRole = null;
            PdfDictionary mappedNsDict = null;
            PdfArray mappingArr = (PdfArray) mapping;
            if (mappingArr.size() > 1) {
                mappedRole = mappingArr.getAsName(0);
                mappedNsDict = mappingArr.getAsDictionary(1);
            }
            mappingWasResolved = (mappedRole == null || mappedNsDict == null) ? false : true;
            if (mappingWasResolved) {
                this.currRole = mappedRole;
                this.currNamespace = new PdfNamespace(mappedNsDict);
            }
        }
        return mappingWasResolved;
    }
}
