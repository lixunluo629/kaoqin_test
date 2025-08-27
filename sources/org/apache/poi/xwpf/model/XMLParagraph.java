package org.apache.poi.xwpf.model;

import org.apache.poi.util.Removal;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;

@Removal(version = "3.18")
@Deprecated
/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xwpf/model/XMLParagraph.class */
public class XMLParagraph {
    protected CTP paragraph;

    public XMLParagraph(CTP paragraph) {
        this.paragraph = paragraph;
    }

    public CTP getCTP() {
        return this.paragraph;
    }
}
