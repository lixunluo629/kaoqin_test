package com.itextpdf.kernel.pdf;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfPages.class */
class PdfPages extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = 404629033132277362L;
    private int from;
    private PdfNumber count;
    private final PdfArray kids;
    private final PdfPages parent;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PdfPages.class.desiredAssertionStatus();
    }

    public PdfPages(int from, PdfDocument pdfDocument, PdfPages parent) {
        super(new PdfDictionary());
        if (pdfDocument.getWriter() != null) {
            getPdfObject().makeIndirect(pdfDocument);
        }
        setForbidRelease();
        this.from = from;
        this.count = new PdfNumber(0);
        this.kids = new PdfArray();
        this.parent = parent;
        getPdfObject().put(PdfName.Type, PdfName.Pages);
        getPdfObject().put(PdfName.Kids, this.kids);
        getPdfObject().put(PdfName.Count, this.count);
        if (parent != null) {
            getPdfObject().put(PdfName.Parent, this.parent.getPdfObject());
        }
    }

    public PdfPages(int from, PdfDocument pdfDocument) {
        this(from, pdfDocument, null);
    }

    public PdfPages(int from, int maxCount, PdfDictionary pdfObject, PdfPages parent) {
        super(pdfObject);
        setForbidRelease();
        this.from = from;
        this.count = pdfObject.getAsNumber(PdfName.Count);
        this.parent = parent;
        if (this.count == null) {
            this.count = new PdfNumber(1);
            pdfObject.put(PdfName.Count, this.count);
        } else if (maxCount < this.count.intValue()) {
            this.count.setValue(maxCount);
        }
        this.kids = pdfObject.getAsArray(PdfName.Kids);
        pdfObject.put(PdfName.Type, PdfName.Pages);
    }

    public void addPage(PdfDictionary page) {
        this.kids.add(page);
        incrementCount();
        page.put(PdfName.Parent, getPdfObject());
        page.setModified();
    }

    public boolean addPage(int index, PdfPage pdfPage) {
        if (index < this.from || index > this.from + getCount()) {
            return false;
        }
        this.kids.add(index - this.from, pdfPage.getPdfObject());
        pdfPage.getPdfObject().put(PdfName.Parent, getPdfObject());
        pdfPage.setModified();
        incrementCount();
        return true;
    }

    public boolean removePage(int pageNum) {
        if (pageNum < this.from || pageNum >= this.from + getCount()) {
            return false;
        }
        decrementCount();
        this.kids.remove(pageNum - this.from);
        return true;
    }

    public void addPages(PdfPages pdfPages) {
        this.kids.add(pdfPages.getPdfObject());
        this.count.setValue(this.count.intValue() + pdfPages.getCount());
        pdfPages.getPdfObject().put(PdfName.Parent, getPdfObject());
        pdfPages.setModified();
        setModified();
    }

    public void removeFromParent() {
        if (this.parent != null) {
            if (!$assertionsDisabled && getCount() != 0) {
                throw new AssertionError();
            }
            this.parent.kids.remove(getPdfObject().getIndirectReference());
            if (this.parent.getCount() == 0) {
                this.parent.removeFromParent();
            }
        }
    }

    public int getFrom() {
        return this.from;
    }

    public int getCount() {
        return this.count.intValue();
    }

    public void correctFrom(int correction) {
        this.from += correction;
    }

    public PdfArray getKids() {
        return getPdfObject().getAsArray(PdfName.Kids);
    }

    public PdfPages getParent() {
        return this.parent;
    }

    public void incrementCount() {
        this.count.increment();
        setModified();
        if (this.parent != null) {
            this.parent.incrementCount();
        }
    }

    public void decrementCount() {
        this.count.decrement();
        setModified();
        if (this.parent != null) {
            this.parent.decrementCount();
        }
    }

    public int compareTo(int index) {
        if (index < this.from) {
            return 1;
        }
        if (index >= this.from + getCount()) {
            return -1;
        }
        return 0;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return true;
    }
}
