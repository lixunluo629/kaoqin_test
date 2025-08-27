package com.itextpdf.kernel.pdf;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfOutline.class */
public class PdfOutline implements Serializable {
    private static final long serialVersionUID = 5730874960685950376L;
    public static int FLAG_ITALIC = 1;
    public static int FLAG_BOLD = 2;
    private List<PdfOutline> children;
    private String title;
    private PdfDictionary content;
    private PdfDestination destination;
    private PdfOutline parent;
    private PdfDocument pdfDoc;

    PdfOutline(String title, PdfDictionary content, PdfDocument pdfDocument) {
        this.children = new ArrayList();
        this.title = title;
        this.content = content;
        this.pdfDoc = pdfDocument;
    }

    PdfOutline(String title, PdfDictionary content, PdfOutline parent) {
        this.children = new ArrayList();
        this.title = title;
        this.content = content;
        this.parent = parent;
        this.pdfDoc = parent.pdfDoc;
        content.makeIndirect(parent.pdfDoc);
    }

    PdfOutline(PdfDocument doc) {
        this.children = new ArrayList();
        this.content = new PdfDictionary();
        this.content.put(PdfName.Type, PdfName.Outlines);
        this.pdfDoc = doc;
        this.content.makeIndirect(doc);
        doc.getCatalog().addRootOutline(this);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.content.put(PdfName.Title, new PdfString(title, PdfEncodings.UNICODE_BIG));
    }

    public void setColor(Color color) {
        this.content.put(PdfName.C, new PdfArray(color.getColorValue()));
    }

    public void setStyle(int style) {
        if (style == FLAG_BOLD || style == FLAG_ITALIC) {
            this.content.put(PdfName.F, new PdfNumber(style));
        }
    }

    public PdfDictionary getContent() {
        return this.content;
    }

    public List<PdfOutline> getAllChildren() {
        return this.children;
    }

    public PdfOutline getParent() {
        return this.parent;
    }

    public PdfDestination getDestination() {
        return this.destination;
    }

    public void addDestination(PdfDestination destination) {
        setDestination(destination);
        this.content.put(PdfName.Dest, destination.getPdfObject());
    }

    public void addAction(PdfAction action) {
        this.content.put(PdfName.A, action.getPdfObject());
    }

    public void setOpen(boolean open) {
        if (!open) {
            this.content.put(PdfName.Count, new PdfNumber(-1));
        } else if (this.children.size() > 0) {
            this.content.put(PdfName.Count, new PdfNumber(this.children.size()));
        } else {
            this.content.remove(PdfName.Count);
        }
    }

    public PdfOutline addOutline(String title, int position) {
        if (position == -1) {
            position = this.children.size();
        }
        PdfDictionary pdfDictionary = new PdfDictionary();
        PdfOutline outline = new PdfOutline(title, pdfDictionary, this);
        pdfDictionary.put(PdfName.Title, new PdfString(title, PdfEncodings.UNICODE_BIG));
        pdfDictionary.put(PdfName.Parent, this.content);
        if (this.children.size() > 0) {
            if (position != 0) {
                PdfDictionary content = this.children.get(position - 1).getContent();
                pdfDictionary.put(PdfName.Prev, content);
                content.put(PdfName.Next, pdfDictionary);
            }
            if (position != this.children.size()) {
                PdfDictionary content2 = this.children.get(position).getContent();
                pdfDictionary.put(PdfName.Next, content2);
                content2.put(PdfName.Prev, pdfDictionary);
            }
        }
        if (position == 0) {
            this.content.put(PdfName.First, pdfDictionary);
        }
        if (position == this.children.size()) {
            this.content.put(PdfName.Last, pdfDictionary);
        }
        PdfNumber count = this.content.getAsNumber(PdfName.Count);
        if (count == null || count.getValue() != -1.0d) {
            this.content.put(PdfName.Count, new PdfNumber(this.children.size() + 1));
        }
        this.children.add(position, outline);
        return outline;
    }

    public PdfOutline addOutline(String title) {
        return addOutline(title, -1);
    }

    public PdfOutline addOutline(PdfOutline outline) {
        PdfOutline newOutline = addOutline(outline.getTitle());
        newOutline.addDestination(outline.getDestination());
        List<PdfOutline> children = outline.getAllChildren();
        for (PdfOutline child : children) {
            newOutline.addOutline(child);
        }
        return newOutline;
    }

    void clear() {
        this.children.clear();
    }

    void setDestination(PdfDestination destination) {
        this.destination = destination;
    }

    void removeOutline() {
        if (!this.pdfDoc.hasOutlines() || isOutlineRoot()) {
            this.pdfDoc.getCatalog().remove(PdfName.Outlines);
            return;
        }
        PdfOutline parent = this.parent;
        List<PdfOutline> children = parent.children;
        children.remove(this);
        PdfDictionary parentContent = parent.content;
        if (children.size() > 0) {
            parentContent.put(PdfName.First, children.get(0).content);
            parentContent.put(PdfName.Last, children.get(children.size() - 1).content);
            PdfDictionary asDictionary = this.content.getAsDictionary(PdfName.Next);
            PdfDictionary asDictionary2 = this.content.getAsDictionary(PdfName.Prev);
            if (asDictionary2 != null) {
                if (asDictionary != null) {
                    asDictionary2.put(PdfName.Next, asDictionary);
                    asDictionary.put(PdfName.Prev, asDictionary2);
                    return;
                } else {
                    asDictionary2.remove(PdfName.Next);
                    return;
                }
            }
            if (asDictionary != null) {
                asDictionary.remove(PdfName.Prev);
                return;
            }
            return;
        }
        parent.removeOutline();
    }

    private PdfDictionary getOutlineRoot() {
        if (!this.pdfDoc.hasOutlines()) {
            return null;
        }
        return this.pdfDoc.getCatalog().getPdfObject().getAsDictionary(PdfName.Outlines);
    }

    private boolean isOutlineRoot() {
        PdfDictionary outlineRoot = getOutlineRoot();
        return outlineRoot == this.content;
    }
}
