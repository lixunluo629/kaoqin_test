package com.itextpdf.kernel.pdf.layer;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfIndirectReference;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;
import com.itextpdf.kernel.pdf.PdfString;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/layer/PdfOCProperties.class */
public class PdfOCProperties extends PdfObjectWrapper<PdfDictionary> {
    static final String OC_CONFIG_NAME_PATTERN = "OCConfigName";
    private static final long serialVersionUID = 1137977454824741350L;
    private List<PdfLayer> layers;

    public PdfOCProperties(PdfDocument document) {
        this((PdfDictionary) new PdfDictionary().makeIndirect(document));
    }

    public PdfOCProperties(PdfDictionary ocPropertiesDict) {
        super(ocPropertiesDict);
        this.layers = new ArrayList();
        ensureObjectIsAddedToDocument(ocPropertiesDict);
        readLayersFromDictionary();
    }

    public void addOCGRadioGroup(List<PdfLayer> group) {
        PdfArray ar = new PdfArray();
        for (PdfLayer layer : group) {
            if (layer.getTitle() == null) {
                ar.add(((PdfDictionary) layer.getPdfObject()).getIndirectReference());
            }
        }
        if (ar.size() != 0) {
            PdfDictionary d = getPdfObject().getAsDictionary(PdfName.D);
            if (d == null) {
                d = new PdfDictionary();
                getPdfObject().put(PdfName.D, d);
            }
            PdfArray radioButtonGroups = d.getAsArray(PdfName.RBGroups);
            if (radioButtonGroups == null) {
                radioButtonGroups = new PdfArray();
                d.put(PdfName.RBGroups, radioButtonGroups);
                d.setModified();
            } else {
                radioButtonGroups.setModified();
            }
            radioButtonGroups.add(ar);
        }
    }

    public PdfObject fillDictionary() {
        PdfArray gr = new PdfArray();
        for (PdfLayer layer : this.layers) {
            if (layer.getTitle() == null) {
                gr.add(layer.getIndirectReference());
            }
        }
        getPdfObject().put(PdfName.OCGs, gr);
        PdfArray rbGroups = null;
        PdfDictionary d = getPdfObject().getAsDictionary(PdfName.D);
        if (d != null) {
            rbGroups = d.getAsArray(PdfName.RBGroups);
        }
        PdfDictionary d2 = new PdfDictionary();
        if (rbGroups != null) {
            d2.put(PdfName.RBGroups, rbGroups);
        }
        d2.put(PdfName.Name, new PdfString(createUniqueName(), PdfEncodings.UNICODE_BIG));
        getPdfObject().put(PdfName.D, d2);
        List<PdfLayer> docOrder = new ArrayList<>(this.layers);
        int i = 0;
        while (i < docOrder.size()) {
            PdfLayer layer2 = docOrder.get(i);
            if (layer2.getParent() != null) {
                docOrder.remove(layer2);
                i--;
            }
            i++;
        }
        PdfArray order = new PdfArray();
        for (Object element : docOrder) {
            getOCGOrder(order, (PdfLayer) element);
        }
        d2.put(PdfName.Order, order);
        PdfArray off = new PdfArray();
        for (Object element2 : this.layers) {
            PdfLayer layer3 = (PdfLayer) element2;
            if (layer3.getTitle() == null && !layer3.isOn()) {
                off.add(layer3.getIndirectReference());
            }
        }
        if (off.size() > 0) {
            d2.put(PdfName.OFF, off);
        } else {
            d2.remove(PdfName.OFF);
        }
        PdfArray locked = new PdfArray();
        for (PdfLayer layer4 : this.layers) {
            if (layer4.getTitle() == null && layer4.isLocked()) {
                locked.add(layer4.getIndirectReference());
            }
        }
        if (locked.size() > 0) {
            d2.put(PdfName.Locked, locked);
        } else {
            d2.remove(PdfName.Locked);
        }
        d2.remove(PdfName.AS);
        addASEvent(PdfName.View, PdfName.Zoom);
        addASEvent(PdfName.View, PdfName.View);
        addASEvent(PdfName.Print, PdfName.Print);
        addASEvent(PdfName.Export, PdfName.Export);
        return getPdfObject();
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    public void flush() {
        fillDictionary();
        super.flush();
    }

    public List<PdfLayer> getLayers() {
        return new ArrayList(this.layers);
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return true;
    }

    protected void registerLayer(PdfLayer layer) {
        if (layer == null) {
            throw new IllegalArgumentException("layer argument is null");
        }
        this.layers.add(layer);
    }

    protected PdfDocument getDocument() {
        return getPdfObject().getIndirectReference().getDocument();
    }

    private static void getOCGOrder(PdfArray order, PdfLayer layer) {
        if (!layer.isOnPanel()) {
            return;
        }
        if (layer.getTitle() == null) {
            order.add(((PdfDictionary) layer.getPdfObject()).getIndirectReference());
        }
        List<PdfLayer> children = layer.getChildren();
        if (children == null) {
            return;
        }
        PdfArray kids = new PdfArray();
        if (layer.getTitle() != null) {
            kids.add(new PdfString(layer.getTitle(), PdfEncodings.UNICODE_BIG));
        }
        for (PdfLayer child : children) {
            getOCGOrder(kids, child);
        }
        if (kids.size() > 0) {
            order.add(kids);
        }
    }

    private void addASEvent(PdfName event, PdfName category) {
        PdfDictionary usage;
        PdfArray arr = new PdfArray();
        for (PdfLayer layer : this.layers) {
            if (layer.getTitle() == null && (usage = ((PdfDictionary) layer.getPdfObject()).getAsDictionary(PdfName.Usage)) != null && usage.get(category) != null) {
                arr.add(((PdfDictionary) layer.getPdfObject()).getIndirectReference());
            }
        }
        if (arr.size() == 0) {
            return;
        }
        PdfDictionary d = getPdfObject().getAsDictionary(PdfName.D);
        PdfArray arras = d.getAsArray(PdfName.AS);
        if (arras == null) {
            arras = new PdfArray();
            d.put(PdfName.AS, arras);
        }
        PdfDictionary as = new PdfDictionary();
        as.put(PdfName.Event, event);
        PdfArray categoryArray = new PdfArray();
        categoryArray.add(category);
        as.put(PdfName.Category, categoryArray);
        as.put(PdfName.OCGs, arr);
        arras.add(as);
    }

    private void readLayersFromDictionary() {
        PdfArray ocgs = getPdfObject().getAsArray(PdfName.OCGs);
        if (ocgs == null || ocgs.isEmpty()) {
            return;
        }
        Map<PdfIndirectReference, PdfLayer> layerMap = new TreeMap<>();
        for (int ind = 0; ind < ocgs.size(); ind++) {
            PdfLayer currentLayer = new PdfLayer((PdfDictionary) ocgs.getAsDictionary(ind).makeIndirect(getDocument()));
            currentLayer.onPanel = false;
            layerMap.put(currentLayer.getIndirectReference(), currentLayer);
        }
        PdfDictionary d = getPdfObject().getAsDictionary(PdfName.D);
        if (d != null && !d.isEmpty()) {
            PdfArray off = d.getAsArray(PdfName.OFF);
            if (off != null) {
                for (int i = 0; i < off.size(); i++) {
                    PdfObject offLayer = off.get(i, false);
                    layerMap.get((PdfIndirectReference) offLayer).on = false;
                }
            }
            PdfArray locked = d.getAsArray(PdfName.Locked);
            if (locked != null) {
                for (int i2 = 0; i2 < locked.size(); i2++) {
                    PdfObject lockedLayer = locked.get(i2, false);
                    layerMap.get((PdfIndirectReference) lockedLayer).locked = true;
                }
            }
            PdfArray orderArray = d.getAsArray(PdfName.Order);
            if (orderArray != null && !orderArray.isEmpty()) {
                readOrderFromDictionary(null, orderArray, layerMap);
            }
        }
        for (PdfLayer layer : layerMap.values()) {
            if (!layer.isOnPanel()) {
                this.layers.add(layer);
            }
        }
    }

    private void readOrderFromDictionary(PdfLayer parent, PdfArray orderArray, Map<PdfIndirectReference, PdfLayer> layerMap) {
        int i = 0;
        while (i < orderArray.size()) {
            PdfObject item = orderArray.get(i);
            if (item.getType() == 3) {
                PdfLayer layer = layerMap.get(item.getIndirectReference());
                if (layer != null) {
                    this.layers.add(layer);
                    layer.onPanel = true;
                    if (parent != null) {
                        parent.addChild(layer);
                    }
                    if (i + 1 < orderArray.size() && orderArray.get(i + 1).getType() == 1) {
                        readOrderFromDictionary(layer, orderArray.getAsArray(i + 1), layerMap);
                        i++;
                    }
                }
            } else if (item.getType() == 1) {
                PdfArray subArray = (PdfArray) item;
                if (!subArray.isEmpty()) {
                    PdfObject firstObj = subArray.get(0);
                    if (firstObj.getType() == 10) {
                        PdfLayer titleLayer = PdfLayer.createTitleSilent(((PdfString) firstObj).toUnicodeString(), getDocument());
                        titleLayer.onPanel = true;
                        this.layers.add(titleLayer);
                        if (parent != null) {
                            parent.addChild(titleLayer);
                        }
                        readOrderFromDictionary(titleLayer, new PdfArray(subArray.subList(1, subArray.size())), layerMap);
                    } else {
                        readOrderFromDictionary(parent, subArray, layerMap);
                    }
                }
            }
            i++;
        }
    }

    private String createUniqueName() {
        int uniqueID = 0;
        Set<String> usedNames = new HashSet<>();
        PdfArray configs = getPdfObject().getAsArray(PdfName.Configs);
        if (null != configs) {
            for (int i = 0; i < configs.size(); i++) {
                PdfDictionary alternateDictionary = configs.getAsDictionary(i);
                if (null != alternateDictionary && alternateDictionary.containsKey(PdfName.Name)) {
                    usedNames.add(alternateDictionary.getAsString(PdfName.Name).toUnicodeString());
                }
            }
        }
        while (usedNames.contains(OC_CONFIG_NAME_PATTERN + uniqueID)) {
            uniqueID++;
        }
        return OC_CONFIG_NAME_PATTERN + uniqueID;
    }
}
