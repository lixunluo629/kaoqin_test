package com.itextpdf.kernel.pdf.tagging;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/tagging/StandardNamespaces.class */
public final class StandardNamespaces {
    private static final Set<String> STD_STRUCT_NAMESPACE_1_7_TYPES = new HashSet(Arrays.asList(StandardRoles.DOCUMENT, StandardRoles.PART, StandardRoles.DIV, "P", StandardRoles.H, StandardRoles.H1, StandardRoles.H2, StandardRoles.H3, StandardRoles.H4, StandardRoles.H5, StandardRoles.H6, StandardRoles.LBL, StandardRoles.SPAN, "Link", StandardRoles.ANNOT, StandardRoles.FORM, StandardRoles.RUBY, StandardRoles.RB, StandardRoles.RT, StandardRoles.RP, StandardRoles.WARICHU, StandardRoles.WT, StandardRoles.WP, StandardRoles.L, StandardRoles.LI, StandardRoles.LBODY, StandardRoles.TABLE, StandardRoles.TR, StandardRoles.TH, StandardRoles.TD, StandardRoles.THEAD, StandardRoles.TBODY, StandardRoles.TFOOT, StandardRoles.CAPTION, StandardRoles.FIGURE, StandardRoles.FORMULA, StandardRoles.SECT, StandardRoles.ART, StandardRoles.BLOCKQUOTE, StandardRoles.TOC, StandardRoles.TOCI, StandardRoles.INDEX, StandardRoles.NONSTRUCT, StandardRoles.PRIVATE, StandardRoles.QUOTE, StandardRoles.NOTE, StandardRoles.REFERENCE, StandardRoles.BIBENTRY, "Code"));
    private static final Set<String> STD_STRUCT_NAMESPACE_2_0_TYPES = new HashSet(Arrays.asList(StandardRoles.DOCUMENT, StandardRoles.DOCUMENTFRAGMENT, StandardRoles.PART, StandardRoles.DIV, StandardRoles.ASIDE, StandardRoles.TITLE, StandardRoles.SUB, "P", StandardRoles.H, StandardRoles.LBL, StandardRoles.EM, StandardRoles.STRONG, StandardRoles.SPAN, "Link", StandardRoles.ANNOT, StandardRoles.FORM, StandardRoles.RUBY, StandardRoles.RB, StandardRoles.RT, StandardRoles.RP, StandardRoles.WARICHU, StandardRoles.WT, StandardRoles.WP, StandardRoles.FENOTE, StandardRoles.L, StandardRoles.LI, StandardRoles.LBODY, StandardRoles.TABLE, StandardRoles.TR, StandardRoles.TH, StandardRoles.TD, StandardRoles.THEAD, StandardRoles.TBODY, StandardRoles.TFOOT, StandardRoles.CAPTION, StandardRoles.FIGURE, StandardRoles.FORMULA, StandardRoles.ARTIFACT));
    private static final String MATH_ML = "http://www.w3.org/1998/Math/MathML";
    public static final String PDF_1_7 = "http://iso.org/pdf/ssn";
    public static final String PDF_2_0 = "http://iso.org/pdf2/ssn";

    public static String getDefault() {
        return PDF_1_7;
    }

    public static boolean isKnownDomainSpecificNamespace(PdfNamespace namespace) {
        return MATH_ML.equals(namespace.getNamespaceName());
    }

    public static boolean roleBelongsToStandardNamespace(String role, String standardNamespaceName) {
        if (PDF_1_7.equals(standardNamespaceName)) {
            return STD_STRUCT_NAMESPACE_1_7_TYPES.contains(role);
        }
        if (PDF_2_0.equals(standardNamespaceName)) {
            return STD_STRUCT_NAMESPACE_2_0_TYPES.contains(role) || isHnRole(role);
        }
        return false;
    }

    public static boolean isHnRole(String role) {
        if (!role.startsWith(StandardRoles.H) || role.length() <= 1 || role.charAt(1) == '0') {
            return false;
        }
        try {
            return Integer.parseInt(role.substring(1, role.length())) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
