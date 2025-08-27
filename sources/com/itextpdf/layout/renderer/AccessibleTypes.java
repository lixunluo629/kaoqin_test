package com.itextpdf.layout.renderer;

import com.itextpdf.kernel.pdf.tagging.StandardNamespaces;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.util.HashSet;
import java.util.Set;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/AccessibleTypes.class */
class AccessibleTypes {
    static int Unknown = 0;
    static int Grouping = 1;
    static int BlockLevel = 2;
    static int InlineLevel = 3;
    static int Illustration = 4;
    static Set<String> groupingRoles = new HashSet();
    static Set<String> blockLevelRoles = new HashSet();
    static Set<String> inlineLevelRoles = new HashSet();
    static Set<String> illustrationRoles = new HashSet();

    AccessibleTypes() {
    }

    static {
        groupingRoles.add(StandardRoles.PART);
        groupingRoles.add(StandardRoles.ART);
        groupingRoles.add(StandardRoles.SECT);
        groupingRoles.add(StandardRoles.DIV);
        groupingRoles.add(StandardRoles.BLOCKQUOTE);
        groupingRoles.add(StandardRoles.CAPTION);
        groupingRoles.add(StandardRoles.TOC);
        groupingRoles.add(StandardRoles.TOCI);
        groupingRoles.add(StandardRoles.INDEX);
        groupingRoles.add(StandardRoles.NONSTRUCT);
        groupingRoles.add(StandardRoles.PRIVATE);
        groupingRoles.add(StandardRoles.ASIDE);
        blockLevelRoles.add("P");
        blockLevelRoles.add(StandardRoles.H);
        blockLevelRoles.add(StandardRoles.H1);
        blockLevelRoles.add(StandardRoles.H2);
        blockLevelRoles.add(StandardRoles.H3);
        blockLevelRoles.add(StandardRoles.H4);
        blockLevelRoles.add(StandardRoles.H5);
        blockLevelRoles.add(StandardRoles.H6);
        blockLevelRoles.add(StandardRoles.L);
        blockLevelRoles.add(StandardRoles.LBL);
        blockLevelRoles.add(StandardRoles.LI);
        blockLevelRoles.add(StandardRoles.LBODY);
        blockLevelRoles.add(StandardRoles.TABLE);
        blockLevelRoles.add(StandardRoles.TR);
        blockLevelRoles.add(StandardRoles.TH);
        blockLevelRoles.add(StandardRoles.TD);
        blockLevelRoles.add(StandardRoles.TITLE);
        blockLevelRoles.add(StandardRoles.FENOTE);
        blockLevelRoles.add(StandardRoles.SUB);
        blockLevelRoles.add(StandardRoles.CAPTION);
        inlineLevelRoles.add(StandardRoles.SPAN);
        inlineLevelRoles.add(StandardRoles.QUOTE);
        inlineLevelRoles.add(StandardRoles.NOTE);
        inlineLevelRoles.add(StandardRoles.REFERENCE);
        inlineLevelRoles.add(StandardRoles.BIBENTRY);
        inlineLevelRoles.add("Code");
        inlineLevelRoles.add("Link");
        inlineLevelRoles.add(StandardRoles.ANNOT);
        inlineLevelRoles.add(StandardRoles.RUBY);
        inlineLevelRoles.add(StandardRoles.WARICHU);
        inlineLevelRoles.add(StandardRoles.RB);
        inlineLevelRoles.add(StandardRoles.RT);
        inlineLevelRoles.add(StandardRoles.RP);
        inlineLevelRoles.add(StandardRoles.WT);
        inlineLevelRoles.add(StandardRoles.WP);
        inlineLevelRoles.add(StandardRoles.EM);
        inlineLevelRoles.add(StandardRoles.STRONG);
        illustrationRoles.add(StandardRoles.FIGURE);
        illustrationRoles.add(StandardRoles.FORMULA);
        illustrationRoles.add(StandardRoles.FORM);
    }

    static int identifyType(String role) {
        if (groupingRoles.contains(role)) {
            return Grouping;
        }
        if (blockLevelRoles.contains(role) || StandardNamespaces.isHnRole(role)) {
            return BlockLevel;
        }
        if (inlineLevelRoles.contains(role)) {
            return InlineLevel;
        }
        if (illustrationRoles.contains(role)) {
            return Illustration;
        }
        return Unknown;
    }
}
