package com.itextpdf.kernel.pdf;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.collection.PdfCollection;
import com.itextpdf.kernel.pdf.layer.PdfOCProperties;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.kernel.pdf.navigation.PdfStringDestination;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfCatalog.class */
public class PdfCatalog extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = -1354567597112193418L;
    private final PdfPagesTree pageTree;
    protected Map<PdfName, PdfNameTree> nameTrees;
    protected PdfNumTree pageLabels;
    protected PdfOCProperties ocProperties;
    private static final String OutlineRoot = "Outlines";
    private PdfOutline outlines;
    private Map<PdfObject, List<PdfOutline>> pagesWithOutlines;
    private boolean outlineMode;
    private static final Set<PdfName> PAGE_MODES = new HashSet(Arrays.asList(PdfName.UseNone, PdfName.UseOutlines, PdfName.UseThumbs, PdfName.FullScreen, PdfName.UseOC, PdfName.UseAttachments));
    private static final Set<PdfName> PAGE_LAYOUTS = new HashSet(Arrays.asList(PdfName.SinglePage, PdfName.OneColumn, PdfName.TwoColumnLeft, PdfName.TwoColumnRight, PdfName.TwoPageLeft, PdfName.TwoPageRight));

    protected PdfCatalog(PdfDictionary pdfObject) {
        super(pdfObject);
        this.nameTrees = new LinkedHashMap();
        this.pagesWithOutlines = new HashMap();
        if (pdfObject == null) {
            throw new PdfException(PdfException.DocumentHasNoPdfCatalogObject);
        }
        ensureObjectIsAddedToDocument(pdfObject);
        getPdfObject().put(PdfName.Type, PdfName.Catalog);
        setForbidRelease();
        this.pageTree = new PdfPagesTree(this);
    }

    protected PdfCatalog(PdfDocument pdfDocument) {
        this((PdfDictionary) new PdfDictionary().makeIndirect(pdfDocument));
    }

    public PdfOCProperties getOCProperties(boolean createIfNotExists) {
        if (this.ocProperties != null) {
            return this.ocProperties;
        }
        PdfDictionary ocPropertiesDict = getPdfObject().getAsDictionary(PdfName.OCProperties);
        if (ocPropertiesDict != null) {
            if (getDocument().getWriter() != null) {
                ocPropertiesDict.makeIndirect(getDocument());
            }
            this.ocProperties = new PdfOCProperties(ocPropertiesDict);
        } else if (createIfNotExists) {
            this.ocProperties = new PdfOCProperties(getDocument());
        }
        return this.ocProperties;
    }

    public PdfDocument getDocument() {
        return getPdfObject().getIndirectReference().getDocument();
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    public void flush() {
        Logger logger = LoggerFactory.getLogger((Class<?>) PdfDocument.class);
        logger.warn("PdfCatalog cannot be flushed manually");
    }

    public PdfCatalog setOpenAction(PdfDestination destination) {
        return put(PdfName.OpenAction, destination.getPdfObject());
    }

    public PdfCatalog setOpenAction(PdfAction action) {
        return put(PdfName.OpenAction, action.getPdfObject());
    }

    public PdfCatalog setAdditionalAction(PdfName key, PdfAction action) {
        PdfAction.setAdditionalAction(this, key, action);
        return this;
    }

    public PdfCatalog setPageMode(PdfName pageMode) {
        if (PAGE_MODES.contains(pageMode)) {
            return put(PdfName.PageMode, pageMode);
        }
        return this;
    }

    public PdfName getPageMode() {
        return getPdfObject().getAsName(PdfName.PageMode);
    }

    public PdfCatalog setPageLayout(PdfName pageLayout) {
        if (PAGE_LAYOUTS.contains(pageLayout)) {
            return put(PdfName.PageLayout, pageLayout);
        }
        return this;
    }

    public PdfName getPageLayout() {
        return getPdfObject().getAsName(PdfName.PageLayout);
    }

    public PdfCatalog setViewerPreferences(PdfViewerPreferences preferences) {
        return put(PdfName.ViewerPreferences, preferences.getPdfObject());
    }

    public PdfViewerPreferences getViewerPreferences() {
        PdfDictionary viewerPreferences = getPdfObject().getAsDictionary(PdfName.ViewerPreferences);
        if (viewerPreferences != null) {
            return new PdfViewerPreferences(viewerPreferences);
        }
        return null;
    }

    public PdfNameTree getNameTree(PdfName treeType) {
        PdfNameTree tree = this.nameTrees.get(treeType);
        if (tree == null) {
            tree = new PdfNameTree(this, treeType);
            this.nameTrees.put(treeType, tree);
        }
        return tree;
    }

    public PdfNumTree getPageLabelsTree(boolean createIfNotExists) {
        if (this.pageLabels == null && (getPdfObject().containsKey(PdfName.PageLabels) || createIfNotExists)) {
            this.pageLabels = new PdfNumTree(this, PdfName.PageLabels);
        }
        return this.pageLabels;
    }

    public void setLang(PdfString lang) {
        put(PdfName.Lang, lang);
    }

    public PdfString getLang() {
        return getPdfObject().getAsString(PdfName.Lang);
    }

    public void addDeveloperExtension(PdfDeveloperExtension extension) {
        PdfDictionary extensions = ((PdfDictionary) getPdfObject()).getAsDictionary(PdfName.Extensions);
        if (extensions == null) {
            extensions = new PdfDictionary();
            put(PdfName.Extensions, extensions);
        } else {
            PdfDictionary existingExtensionDict = extensions.getAsDictionary(extension.getPrefix());
            if (existingExtensionDict != null) {
                int diff = extension.getBaseVersion().compareTo(existingExtensionDict.getAsName(PdfName.BaseVersion));
                if (diff < 0) {
                    return;
                }
                int diff2 = extension.getExtensionLevel() - existingExtensionDict.getAsNumber(PdfName.ExtensionLevel).intValue();
                if (diff2 <= 0) {
                    return;
                }
            }
        }
        extensions.put(extension.getPrefix(), extension.getDeveloperExtensions());
    }

    public PdfCollection getCollection() {
        PdfDictionary collectionDictionary = getPdfObject().getAsDictionary(PdfName.Collection);
        if (collectionDictionary != null) {
            return new PdfCollection(collectionDictionary);
        }
        return null;
    }

    public PdfCatalog setCollection(PdfCollection collection) {
        put(PdfName.Collection, collection.getPdfObject());
        return this;
    }

    public PdfCatalog put(PdfName key, PdfObject value) {
        getPdfObject().put(key, value);
        setModified();
        return this;
    }

    public PdfCatalog remove(PdfName key) {
        getPdfObject().remove(key);
        setModified();
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return true;
    }

    protected boolean isOCPropertiesMayHaveChanged() {
        return this.ocProperties != null;
    }

    PdfPagesTree getPageTree() {
        return this.pageTree;
    }

    Map<PdfObject, List<PdfOutline>> getPagesWithOutlines() {
        return this.pagesWithOutlines;
    }

    void addNamedDestination(String key, PdfObject value) {
        addNameToNameTree(key, value, PdfName.Dests);
    }

    void addNameToNameTree(String key, PdfObject value, PdfName treeType) {
        getNameTree(treeType).addEntry(key, value);
    }

    PdfOutline getOutlines(boolean updateOutlines) {
        if (this.outlines != null && !updateOutlines) {
            return this.outlines;
        }
        if (this.outlines != null) {
            this.outlines.clear();
            this.pagesWithOutlines.clear();
        }
        this.outlineMode = true;
        PdfNameTree destsTree = getNameTree(PdfName.Dests);
        PdfDictionary outlineRoot = getPdfObject().getAsDictionary(PdfName.Outlines);
        if (outlineRoot == null) {
            if (null == getDocument().getWriter()) {
                return null;
            }
            this.outlines = new PdfOutline(getDocument());
        } else {
            constructOutlines(outlineRoot, destsTree.getNames());
        }
        return this.outlines;
    }

    boolean hasOutlines() {
        return getPdfObject().containsKey(PdfName.Outlines);
    }

    boolean isOutlineMode() {
        return this.outlineMode;
    }

    void removeOutlines(PdfPage page) {
        if (getDocument().getWriter() != null && hasOutlines()) {
            getOutlines(false);
            if (this.pagesWithOutlines.size() > 0 && this.pagesWithOutlines.get(page.getPdfObject()) != null) {
                for (PdfOutline outline : this.pagesWithOutlines.get(page.getPdfObject())) {
                    outline.removeOutline();
                }
            }
        }
    }

    void addRootOutline(PdfOutline outline) {
        if (this.outlineMode && this.pagesWithOutlines.size() == 0) {
            put(PdfName.Outlines, outline.getContent());
        }
    }

    PdfDestination copyDestination(PdfObject dest, Map<PdfPage, PdfPage> page2page, PdfDocument toDocument) {
        PdfDestination d = null;
        if (dest.isArray()) {
            PdfObject pageObject = ((PdfArray) dest).get(0);
            Iterator<PdfPage> it = page2page.keySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (it.next().getPdfObject() == pageObject) {
                    d = new PdfExplicitDestination((PdfArray) dest.copyTo(toDocument, false));
                    break;
                }
            }
        } else if (dest.isString() || dest.isName()) {
            PdfNameTree destsTree = getNameTree(PdfName.Dests);
            Map<String, PdfObject> dests = destsTree.getNames();
            String srcDestName = dest.isString() ? ((PdfString) dest).toUnicodeString() : ((PdfName) dest).getValue();
            PdfArray srcDestArray = (PdfArray) dests.get(srcDestName);
            if (srcDestArray != null) {
                PdfObject pageObject2 = srcDestArray.get(0);
                if (pageObject2 instanceof PdfNumber) {
                    pageObject2 = getDocument().getPage(((PdfNumber) pageObject2).intValue() + 1).getPdfObject();
                }
                Iterator<PdfPage> it2 = page2page.keySet().iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    PdfPage oldPage = it2.next();
                    if (oldPage.getPdfObject() == pageObject2) {
                        d = new PdfStringDestination(srcDestName);
                        if (!isEqualSameNameDestExist(page2page, toDocument, srcDestName, srcDestArray, oldPage)) {
                            PdfArray copiedArray = (PdfArray) srcDestArray.copyTo(toDocument, false);
                            copiedArray.set(0, page2page.get(oldPage).getPdfObject());
                            toDocument.addNamedDestination(srcDestName, copiedArray);
                        }
                    }
                }
            }
        }
        return d;
    }

    private boolean isEqualSameNameDestExist(Map<PdfPage, PdfPage> page2page, PdfDocument toDocument, String srcDestName, PdfArray srcDestArray, PdfPage oldPage) {
        PdfArray sameNameDest = (PdfArray) toDocument.getCatalog().getNameTree(PdfName.Dests).getNames().get(srcDestName);
        boolean equalSameNameDestExists = false;
        if (sameNameDest != null && sameNameDest.getAsDictionary(0) != null) {
            PdfIndirectReference existingDestPageRef = sameNameDest.getAsDictionary(0).getIndirectReference();
            PdfIndirectReference newDestPageRef = page2page.get(oldPage).getPdfObject().getIndirectReference();
            boolean z = existingDestPageRef.equals(newDestPageRef) && sameNameDest.size() == srcDestArray.size();
            equalSameNameDestExists = z;
            if (z) {
                for (int i = 1; i < sameNameDest.size(); i++) {
                    equalSameNameDestExists = equalSameNameDestExists && sameNameDest.get(i).equals(srcDestArray.get(i));
                }
            }
        }
        return equalSameNameDestExists;
    }

    private void addOutlineToPage(PdfOutline outline, Map<String, PdfObject> names) {
        PdfObject pageObj = outline.getDestination().getDestinationPage(names);
        if (pageObj instanceof PdfNumber) {
            pageObj = getDocument().getPage(((PdfNumber) pageObj).intValue() + 1).getPdfObject();
        }
        if (pageObj != null) {
            List<PdfOutline> outs = this.pagesWithOutlines.get(pageObj);
            if (outs == null) {
                outs = new ArrayList();
                this.pagesWithOutlines.put(pageObj, outs);
            }
            outs.add(outline);
        }
    }

    private PdfDictionary getNextOutline(PdfDictionary first, PdfDictionary next, PdfDictionary parent) {
        if (first != null) {
            return first;
        }
        if (next != null) {
            return next;
        }
        return getParentNextOutline(parent);
    }

    private PdfDictionary getParentNextOutline(PdfDictionary parent) {
        if (parent == null) {
            return null;
        }
        PdfDictionary current = null;
        while (current == null) {
            current = parent.getAsDictionary(PdfName.Next);
            if (current == null) {
                parent = parent.getAsDictionary(PdfName.Parent);
                if (parent == null) {
                    return null;
                }
            }
        }
        return current;
    }

    private void addOutlineToPage(PdfOutline outline, PdfDictionary item, Map<String, PdfObject> names) {
        PdfObject destObject;
        PdfObject dest = item.get(PdfName.Dest);
        if (dest != null) {
            PdfDestination destination = PdfDestination.makeDestination(dest);
            outline.setDestination(destination);
            addOutlineToPage(outline, names);
            return;
        }
        PdfDictionary action = item.getAsDictionary(PdfName.A);
        if (action != null) {
            PdfName actionType = action.getAsName(PdfName.S);
            if (PdfName.GoTo.equals(actionType) && (destObject = action.get(PdfName.D)) != null) {
                PdfDestination destination2 = PdfDestination.makeDestination(destObject);
                outline.setDestination(destination2);
                addOutlineToPage(outline, names);
            }
        }
    }

    private void constructOutlines(PdfDictionary outlineRoot, Map<String, PdfObject> names) {
        if (outlineRoot == null) {
            return;
        }
        PdfDictionary current = outlineRoot.getAsDictionary(PdfName.First);
        HashMap<PdfDictionary, PdfOutline> parentOutlineMap = new HashMap<>();
        this.outlines = new PdfOutline(OutlineRoot, outlineRoot, getDocument());
        parentOutlineMap.put(outlineRoot, this.outlines);
        while (current != null) {
            PdfDictionary first = current.getAsDictionary(PdfName.First);
            PdfDictionary next = current.getAsDictionary(PdfName.Next);
            PdfDictionary parent = current.getAsDictionary(PdfName.Parent);
            PdfOutline parentOutline = parentOutlineMap.get(parent);
            PdfOutline currentOutline = new PdfOutline(current.getAsString(PdfName.Title).toUnicodeString(), current, parentOutline);
            addOutlineToPage(currentOutline, current, names);
            parentOutline.getAllChildren().add(currentOutline);
            if (first != null) {
                parentOutlineMap.put(current, currentOutline);
            }
            current = getNextOutline(first, next, parent);
        }
    }
}
