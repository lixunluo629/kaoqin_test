package com.itextpdf.kernel.pdf;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.MessageFormatUtil;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfNameTree.class */
public class PdfNameTree implements Serializable {
    private static final int NODE_SIZE = 40;
    private static final long serialVersionUID = 8153711383828989907L;
    private PdfCatalog catalog;
    private Map<String, PdfObject> items;
    private PdfName treeType;
    private boolean modified;

    public PdfNameTree(PdfCatalog catalog, PdfName treeType) {
        this.items = new LinkedHashMap();
        this.treeType = treeType;
        this.catalog = catalog;
        this.items = getNames();
    }

    public Map<String, PdfObject> getNames() {
        PdfDictionary destinations;
        PdfDictionary dictionary;
        if (this.items.size() > 0) {
            return this.items;
        }
        PdfDictionary dictionary2 = this.catalog.getPdfObject().getAsDictionary(PdfName.Names);
        if (dictionary2 != null && (dictionary = dictionary2.getAsDictionary(this.treeType)) != null) {
            this.items = readTree(dictionary);
            Set<String> keys = new HashSet<>();
            keys.addAll(this.items.keySet());
            for (String key : keys) {
                if (this.treeType.equals(PdfName.Dests)) {
                    PdfArray arr = getDestArray(this.items.get(key));
                    if (arr != null) {
                        this.items.put(key, arr);
                    } else {
                        this.items.remove(key);
                    }
                } else if (this.items.get(key) == null) {
                    this.items.remove(key);
                }
            }
        }
        if (this.treeType.equals(PdfName.Dests) && (destinations = this.catalog.getPdfObject().getAsDictionary(PdfName.Dests)) != null) {
            for (PdfName key2 : destinations.keySet()) {
                PdfArray array = getDestArray(destinations.get(key2));
                if (array != null) {
                    this.items.put(key2.getValue(), array);
                }
            }
        }
        return this.items;
    }

    public void addEntry(String key, PdfObject value) {
        PdfObject existingVal = this.items.get(key);
        if (existingVal != null) {
            if (value.getIndirectReference() != null && value.getIndirectReference().equals(existingVal.getIndirectReference())) {
                return;
            }
            Logger logger = LoggerFactory.getLogger((Class<?>) PdfNameTree.class);
            logger.warn(MessageFormatUtil.format(LogMessageConstant.NAME_ALREADY_EXISTS_IN_THE_NAME_TREE, key));
        }
        this.modified = true;
        this.items.put(key, value);
    }

    public boolean isModified() {
        return this.modified;
    }

    public void setModified() {
        this.modified = true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public PdfDictionary buildTree() {
        int top;
        String[] names = (String[]) this.items.keySet().toArray(new String[this.items.size()]);
        Arrays.sort(names);
        if (names.length <= 40) {
            PdfDictionary dic = new PdfDictionary();
            PdfArray ar = new PdfArray();
            for (String name : names) {
                ar.add(new PdfString(name, (String) null));
                ar.add(this.items.get(name));
            }
            dic.put(PdfName.Names, ar);
            return dic;
        }
        int skip = 40;
        PdfObject[] pdfObjectArr = new PdfDictionary[((names.length + 40) - 1) / 40];
        for (int k = 0; k < pdfObjectArr.length; k++) {
            int offset = k * 40;
            int end = Math.min(offset + 40, names.length);
            PdfDictionary dic2 = new PdfDictionary();
            PdfArray arr = new PdfArray();
            arr.add(new PdfString(names[offset], (String) null));
            arr.add(new PdfString(names[end - 1], (String) null));
            dic2.put(PdfName.Limits, arr);
            PdfArray arr2 = new PdfArray();
            while (offset < end) {
                arr2.add(new PdfString(names[offset], (String) null));
                arr2.add(this.items.get(names[offset]));
                offset++;
            }
            dic2.put(PdfName.Names, arr2);
            dic2.makeIndirect(this.catalog.getDocument());
            pdfObjectArr[k] = dic2;
        }
        int length = pdfObjectArr.length;
        while (true) {
            top = length;
            if (top <= 40) {
                break;
            }
            skip *= 40;
            int tt = ((names.length + skip) - 1) / skip;
            for (int i = 0; i < tt; i++) {
                int offset2 = i * 40;
                int end2 = Math.min(offset2 + 40, top);
                PdfDictionary pdfDictionary = (PdfDictionary) new PdfDictionary().makeIndirect(this.catalog.getDocument());
                PdfArray arr3 = new PdfArray();
                arr3.add(new PdfString(names[i * skip], (String) null));
                arr3.add(new PdfString(names[Math.min((i + 1) * skip, names.length) - 1], (String) null));
                pdfDictionary.put(PdfName.Limits, arr3);
                PdfArray pdfArray = new PdfArray();
                while (offset2 < end2) {
                    pdfArray.add(pdfObjectArr[offset2]);
                    offset2++;
                }
                pdfDictionary.put(PdfName.Kids, pdfArray);
                pdfObjectArr[i] = pdfDictionary;
            }
            length = tt;
        }
        PdfArray pdfArray2 = new PdfArray();
        for (int i2 = 0; i2 < top; i2++) {
            pdfArray2.add(pdfObjectArr[i2]);
        }
        PdfDictionary pdfDictionary2 = new PdfDictionary();
        pdfDictionary2.put(PdfName.Kids, pdfArray2);
        return pdfDictionary2;
    }

    private Map<String, PdfObject> readTree(PdfDictionary dictionary) {
        Map<String, PdfObject> items = new LinkedHashMap<>();
        if (dictionary != null) {
            iterateItems(dictionary, items, null);
        }
        return items;
    }

    private PdfString iterateItems(PdfDictionary dictionary, Map<String, PdfObject> items, PdfString leftOver) {
        PdfString name;
        PdfArray names = dictionary.getAsArray(PdfName.Names);
        if (names != null) {
            int k = 0;
            while (k < names.size()) {
                if (leftOver == null) {
                    int i = k;
                    k++;
                    name = names.getAsString(i);
                } else {
                    name = leftOver;
                    leftOver = null;
                }
                if (k < names.size()) {
                    items.put(name.toUnicodeString(), names.get(k));
                    k++;
                } else {
                    return name;
                }
            }
            return null;
        }
        PdfArray names2 = dictionary.getAsArray(PdfName.Kids);
        if (names2 != null) {
            for (int k2 = 0; k2 < names2.size(); k2++) {
                PdfDictionary kid = names2.getAsDictionary(k2);
                leftOver = iterateItems(kid, items, leftOver);
            }
            return null;
        }
        return null;
    }

    private PdfArray getDestArray(PdfObject obj) {
        if (obj == null) {
            return null;
        }
        if (obj.isArray()) {
            return (PdfArray) obj;
        }
        if (obj.isDictionary()) {
            PdfArray arr = ((PdfDictionary) obj).getAsArray(PdfName.D);
            return arr;
        }
        return null;
    }
}
