package com.itextpdf.kernel.pdf.tagutils;

import com.itextpdf.kernel.pdf.tagging.PdfNamespace;
import java.io.Serializable;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/tagutils/IRoleMappingResolver.class */
public interface IRoleMappingResolver extends Serializable {
    String getRole();

    PdfNamespace getNamespace();

    boolean currentRoleIsStandard();

    boolean currentRoleShallBeMappedToStandard();

    boolean resolveNextMapping();
}
