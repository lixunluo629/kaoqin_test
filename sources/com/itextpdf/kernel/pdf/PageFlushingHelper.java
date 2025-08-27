package com.itextpdf.kernel.pdf;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.layer.PdfLayer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PageFlushingHelper.class */
public class PageFlushingHelper {
    private static final DeepFlushingContext pageContext;
    private PdfDocument pdfDoc;
    private boolean release;
    private HashSet<PdfObject> currNestedObjParents = new HashSet<>();
    private Set<PdfIndirectReference> layersRefs = new HashSet();
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PageFlushingHelper.class.desiredAssertionStatus();
        pageContext = initPageFlushingContext();
    }

    public PageFlushingHelper(PdfDocument pdfDoc) {
        this.pdfDoc = pdfDoc;
    }

    public void unsafeFlushDeep(int pageNum) {
        if (this.pdfDoc.getWriter() == null) {
            throw new IllegalArgumentException(PdfException.FlushingHelperFLushingModeIsNotForDocReadingMode);
        }
        this.release = false;
        flushPage(pageNum);
    }

    public void releaseDeep(int pageNum) {
        this.release = true;
        flushPage(pageNum);
    }

    public void appendModeFlush(int pageNum) {
        if (this.pdfDoc.getWriter() == null) {
            throw new IllegalArgumentException(PdfException.FlushingHelperFLushingModeIsNotForDocReadingMode);
        }
        PdfPage page = this.pdfDoc.getPage(pageNum);
        if (page.isFlushed()) {
            return;
        }
        page.getDocument().dispatchEvent(new PdfDocumentEvent(PdfDocumentEvent.END_PAGE, page));
        boolean pageWasModified = page.getPdfObject().isModified();
        page.setModified();
        this.release = true;
        boolean pageWasModified2 = flushPage(pageNum) || pageWasModified;
        PdfArray annots = page.getPdfObject().getAsArray(PdfName.Annots);
        if (annots != null && !annots.isFlushed()) {
            arrayFlushIfModified(annots);
        }
        PdfObject thumb = page.getPdfObject().get(PdfName.Thumb, false);
        flushIfModified(thumb);
        PdfObject contents = page.getPdfObject().get(PdfName.Contents, false);
        if (contents instanceof PdfIndirectReference) {
            if (contents.checkState((short) 8) && !contents.checkState((short) 1)) {
                PdfObject contentsDirectObj = ((PdfIndirectReference) contents).getRefersTo();
                if (contentsDirectObj.isArray()) {
                    arrayFlushIfModified((PdfArray) contentsDirectObj);
                } else {
                    contentsDirectObj.flush();
                }
            }
        } else if (contents instanceof PdfArray) {
            arrayFlushIfModified((PdfArray) contents);
        } else if (contents instanceof PdfStream) {
            flushIfModified(contents);
        }
        if (!pageWasModified2) {
            page.getPdfObject().getIndirectReference().clearState((short) 8);
            this.pdfDoc.getCatalog().getPageTree().releasePage(pageNum);
            page.unsetForbidRelease();
            page.getPdfObject().release();
            return;
        }
        page.releaseInstanceFields();
        page.getPdfObject().flush();
    }

    private boolean flushPage(int pageNum) {
        PdfPage page = this.pdfDoc.getPage(pageNum);
        if (page.isFlushed()) {
            return false;
        }
        boolean pageChanged = false;
        if (!this.release) {
            this.pdfDoc.dispatchEvent(new PdfDocumentEvent(PdfDocumentEvent.END_PAGE, page));
            initCurrentLayers(this.pdfDoc);
        }
        PdfDictionary pageDict = page.getPdfObject();
        PdfDictionary resourcesDict = page.initResources(false);
        PdfResources resources = page.getResources(false);
        if (resources != null && resources.isModified() && !resources.isReadOnly()) {
            resourcesDict = resources.getPdfObject();
            pageDict.put(PdfName.Resources, resources.getPdfObject());
            pageDict.setModified();
            pageChanged = true;
        }
        if (!resourcesDict.isFlushed()) {
            flushDictRecursively(resourcesDict, null);
            flushOrRelease(resourcesDict);
        }
        flushDictRecursively(pageDict, pageContext);
        if (this.release) {
            if (!page.getPdfObject().isModified()) {
                this.pdfDoc.getCatalog().getPageTree().releasePage(pageNum);
                page.unsetForbidRelease();
                page.getPdfObject().release();
            }
        } else {
            if (this.pdfDoc.isTagged() && !this.pdfDoc.getStructTreeRoot().isFlushed()) {
                page.tryFlushPageTags();
            }
            if (!this.pdfDoc.isAppendMode() || page.getPdfObject().isModified()) {
                page.releaseInstanceFields();
                page.getPdfObject().flush();
            } else {
                this.pdfDoc.getCatalog().getPageTree().releasePage(pageNum);
                page.unsetForbidRelease();
                page.getPdfObject().release();
            }
        }
        this.layersRefs.clear();
        return pageChanged;
    }

    private void initCurrentLayers(PdfDocument pdfDoc) {
        if (pdfDoc.getCatalog().isOCPropertiesMayHaveChanged()) {
            List<PdfLayer> layers = pdfDoc.getCatalog().getOCProperties(false).getLayers();
            for (PdfLayer layer : layers) {
                this.layersRefs.add(((PdfDictionary) layer.getPdfObject()).getIndirectReference());
            }
        }
    }

    private void flushObjectRecursively(PdfObject obj, DeepFlushingContext context) {
        if (obj == null) {
            return;
        }
        boolean avoidReleaseForIndirectObjInstance = false;
        if (obj.isIndirectReference()) {
            PdfIndirectReference indRef = (PdfIndirectReference) obj;
            if (indRef.refersTo == null || indRef.checkState((short) 1)) {
                return;
            } else {
                obj = indRef.getRefersTo();
            }
        } else {
            if (obj.isFlushed()) {
                return;
            }
            if (this.release && obj.isIndirect()) {
                if (!$assertionsDisabled && !obj.isReleaseForbidden() && obj.getIndirectReference() != null) {
                    throw new AssertionError();
                }
                avoidReleaseForIndirectObjInstance = true;
            }
        }
        if (this.pdfDoc.isDocumentFont(obj.getIndirectReference()) || this.layersRefs.contains(obj.getIndirectReference())) {
            return;
        }
        if (obj.isDictionary() || obj.isStream()) {
            if (!this.currNestedObjParents.add(obj)) {
                return;
            }
            flushDictRecursively((PdfDictionary) obj, context);
            this.currNestedObjParents.remove(obj);
        } else if (obj.isArray()) {
            if (!this.currNestedObjParents.add(obj)) {
                return;
            }
            PdfArray array = (PdfArray) obj;
            for (int i = 0; i < array.size(); i++) {
                flushObjectRecursively(array.get(i, false), context);
            }
            this.currNestedObjParents.remove(obj);
        }
        if (!avoidReleaseForIndirectObjInstance) {
            flushOrRelease(obj);
        }
    }

    private void flushDictRecursively(PdfDictionary dict, DeepFlushingContext context) {
        for (PdfName key : dict.keySet()) {
            DeepFlushingContext innerContext = null;
            if (context != null) {
                if (!context.isKeyInBlackList(key)) {
                    innerContext = context.getInnerContextFor(key);
                }
            }
            PdfObject value = dict.get(key, false);
            flushObjectRecursively(value, innerContext);
        }
    }

    private void flushOrRelease(PdfObject obj) {
        if (this.release) {
            if (!obj.isReleaseForbidden()) {
                obj.release();
                return;
            }
            return;
        }
        makeIndirectIfNeeded(obj);
        if (!this.pdfDoc.isAppendMode() || obj.isModified()) {
            obj.flush();
        } else if (!obj.isReleaseForbidden()) {
            obj.release();
        }
    }

    private void flushIfModified(PdfObject o) {
        if (o != null && !(o instanceof PdfIndirectReference)) {
            makeIndirectIfNeeded(o);
            o = o.getIndirectReference();
        }
        if (o != null && o.checkState((short) 8) && !o.checkState((short) 1)) {
            ((PdfIndirectReference) o).getRefersTo().flush();
        }
    }

    private void arrayFlushIfModified(PdfArray contentsArr) {
        for (int i = 0; i < contentsArr.size(); i++) {
            PdfObject c = contentsArr.get(i, false);
            flushIfModified(c);
        }
    }

    private void makeIndirectIfNeeded(PdfObject o) {
        if (o.checkState((short) 64)) {
            o.makeIndirect(this.pdfDoc);
        }
    }

    private static DeepFlushingContext initPageFlushingContext() {
        Map<PdfName, DeepFlushingContext> NO_INNER_CONTEXTS = Collections.emptyMap();
        DeepFlushingContext actionContext = new DeepFlushingContext(new LinkedHashSet(Arrays.asList(PdfName.D, PdfName.SD, PdfName.Dp, PdfName.B, PdfName.Annotation, PdfName.T, PdfName.AN, PdfName.TA)), NO_INNER_CONTEXTS);
        DeepFlushingContext aaContext = new DeepFlushingContext(actionContext);
        LinkedHashMap<PdfName, DeepFlushingContext> annotInnerContexts = new LinkedHashMap<>();
        DeepFlushingContext annotsContext = new DeepFlushingContext(new LinkedHashSet(Arrays.asList(PdfName.P, PdfName.Popup, PdfName.Dest, PdfName.Parent, PdfName.V)), annotInnerContexts);
        annotInnerContexts.put(PdfName.A, actionContext);
        annotInnerContexts.put(PdfName.PA, actionContext);
        annotInnerContexts.put(PdfName.AA, aaContext);
        DeepFlushingContext sepInfoContext = new DeepFlushingContext(new LinkedHashSet(Collections.singletonList(PdfName.Pages)), NO_INNER_CONTEXTS);
        DeepFlushingContext bContext = new DeepFlushingContext(null, NO_INNER_CONTEXTS);
        LinkedHashMap<PdfName, DeepFlushingContext> presStepsInnerContexts = new LinkedHashMap<>();
        DeepFlushingContext presStepsContext = new DeepFlushingContext(new LinkedHashSet(Collections.singletonList(PdfName.Prev)), presStepsInnerContexts);
        presStepsInnerContexts.put(PdfName.NA, actionContext);
        presStepsInnerContexts.put(PdfName.PA, actionContext);
        LinkedHashMap<PdfName, DeepFlushingContext> pageInnerContexts = new LinkedHashMap<>();
        DeepFlushingContext pageContext2 = new DeepFlushingContext(new LinkedHashSet(Arrays.asList(PdfName.Parent, PdfName.DPart)), pageInnerContexts);
        pageInnerContexts.put(PdfName.Annots, annotsContext);
        pageInnerContexts.put(PdfName.B, bContext);
        pageInnerContexts.put(PdfName.AA, aaContext);
        pageInnerContexts.put(PdfName.SeparationInfo, sepInfoContext);
        pageInnerContexts.put(PdfName.PresSteps, presStepsContext);
        return pageContext2;
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PageFlushingHelper$DeepFlushingContext.class */
    private static class DeepFlushingContext {
        Set<PdfName> blackList;
        Map<PdfName, DeepFlushingContext> innerContexts;
        DeepFlushingContext unconditionalInnerContext;

        public DeepFlushingContext(Set<PdfName> blackList, Map<PdfName, DeepFlushingContext> innerContexts) {
            this.blackList = blackList;
            this.innerContexts = innerContexts;
        }

        public DeepFlushingContext(DeepFlushingContext unconditionalInnerContext) {
            this.blackList = Collections.emptySet();
            this.innerContexts = null;
            this.unconditionalInnerContext = unconditionalInnerContext;
        }

        public boolean isKeyInBlackList(PdfName key) {
            return this.blackList == null || this.blackList.contains(key);
        }

        public DeepFlushingContext getInnerContextFor(PdfName key) {
            return this.innerContexts == null ? this.unconditionalInnerContext : this.innerContexts.get(key);
        }
    }
}
