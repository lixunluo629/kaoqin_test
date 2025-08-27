package org.apache.poi.xwpf.usermodel;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xwpf/usermodel/XWPFComment.class */
public class XWPFComment {
    protected String id;
    protected String author;
    protected StringBuffer text = new StringBuffer();

    public XWPFComment(CTComment comment, XWPFDocument document) {
        this.id = comment.getId().toString();
        this.author = comment.getAuthor();
        CTP[] arr$ = comment.getPArray();
        for (CTP ctp : arr$) {
            XWPFParagraph p = new XWPFParagraph(ctp, document);
            this.text.append(p.getText());
        }
    }

    public String getId() {
        return this.id;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getText() {
        return this.text.toString();
    }
}
