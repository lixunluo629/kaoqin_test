package com.itextpdf.kernel.pdf.tagging;

import com.itextpdf.kernel.pdf.PdfName;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/tagging/IStructureNode.class */
public interface IStructureNode {
    IStructureNode getParent();

    List<IStructureNode> getKids();

    PdfName getRole();
}
