package com.itextpdf.kernel.pdf;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfNumTree.class */
public class PdfNumTree implements Serializable {
    private static final long serialVersionUID = 2636796232945164670L;
    private static final int NODE_SIZE = 40;
    private PdfCatalog catalog;
    private Map<Integer, PdfObject> items = new HashMap();
    private PdfName treeType;

    public PdfNumTree(PdfCatalog catalog, PdfName treeType) {
        this.treeType = treeType;
        this.catalog = catalog;
    }

    public Map<Integer, PdfObject> getNumbers() {
        PdfDictionary structTreeRoot;
        if (this.items.size() > 0) {
            return this.items;
        }
        PdfDictionary numbers = null;
        if (this.treeType.equals(PdfName.PageLabels)) {
            numbers = this.catalog.getPdfObject().getAsDictionary(PdfName.PageLabels);
        } else if (this.treeType.equals(PdfName.ParentTree) && (structTreeRoot = this.catalog.getPdfObject().getAsDictionary(PdfName.StructTreeRoot)) != null) {
            numbers = structTreeRoot.getAsDictionary(PdfName.ParentTree);
        }
        if (numbers != null) {
            readTree(numbers);
        }
        return this.items;
    }

    public void addEntry(int key, PdfObject value) {
        this.items.put(new Integer(key), value);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public PdfDictionary buildTree() {
        int top;
        Integer[] numbers = (Integer[]) this.items.keySet().toArray(new Integer[this.items.size()]);
        Arrays.sort(numbers);
        if (numbers.length <= 40) {
            PdfDictionary dic = new PdfDictionary();
            PdfArray ar = new PdfArray();
            for (int k = 0; k < numbers.length; k++) {
                ar.add(new PdfNumber(numbers[k].intValue()));
                ar.add(this.items.get(numbers[k]));
            }
            dic.put(PdfName.Nums, ar);
            return dic;
        }
        int skip = 40;
        PdfObject[] pdfObjectArr = new PdfDictionary[((numbers.length + 40) - 1) / 40];
        for (int i = 0; i < pdfObjectArr.length; i++) {
            int offset = i * 40;
            int end = Math.min(offset + 40, numbers.length);
            PdfDictionary dic2 = new PdfDictionary();
            PdfArray arr = new PdfArray();
            arr.add(new PdfNumber(numbers[offset].intValue()));
            arr.add(new PdfNumber(numbers[end - 1].intValue()));
            dic2.put(PdfName.Limits, arr);
            PdfArray arr2 = new PdfArray();
            while (offset < end) {
                arr2.add(new PdfNumber(numbers[offset].intValue()));
                arr2.add(this.items.get(numbers[offset]));
                offset++;
            }
            dic2.put(PdfName.Nums, arr2);
            dic2.makeIndirect(this.catalog.getDocument());
            pdfObjectArr[i] = dic2;
        }
        int length = pdfObjectArr.length;
        while (true) {
            top = length;
            if (top <= 40) {
                break;
            }
            skip *= 40;
            int tt = ((numbers.length + skip) - 1) / skip;
            for (int k2 = 0; k2 < tt; k2++) {
                int offset2 = k2 * 40;
                int end2 = Math.min(offset2 + 40, top);
                PdfDictionary pdfDictionary = (PdfDictionary) new PdfDictionary().makeIndirect(this.catalog.getDocument());
                PdfArray arr3 = new PdfArray();
                arr3.add(new PdfNumber(numbers[k2 * skip].intValue()));
                arr3.add(new PdfNumber(numbers[Math.min((k2 + 1) * skip, numbers.length) - 1].intValue()));
                pdfDictionary.put(PdfName.Limits, arr3);
                PdfArray pdfArray = new PdfArray();
                while (offset2 < end2) {
                    pdfArray.add(pdfObjectArr[offset2]);
                    offset2++;
                }
                pdfDictionary.put(PdfName.Kids, pdfArray);
                pdfObjectArr[k2] = pdfDictionary;
            }
            length = tt;
        }
        PdfArray pdfArray2 = new PdfArray();
        for (int k3 = 0; k3 < top; k3++) {
            pdfArray2.add(pdfObjectArr[k3]);
        }
        PdfDictionary pdfDictionary2 = new PdfDictionary();
        pdfDictionary2.put(PdfName.Kids, pdfArray2);
        return pdfDictionary2;
    }

    private void readTree(PdfDictionary dictionary) {
        if (dictionary != null) {
            iterateItems(dictionary, null);
        }
    }

    private PdfNumber iterateItems(PdfDictionary dictionary, PdfNumber leftOver) {
        PdfNumber number;
        PdfArray nums = dictionary.getAsArray(PdfName.Nums);
        if (nums != null) {
            int k = 0;
            while (k < nums.size()) {
                if (leftOver == null) {
                    int i = k;
                    k++;
                    number = nums.getAsNumber(i);
                } else {
                    number = leftOver;
                    leftOver = null;
                }
                if (k < nums.size()) {
                    this.items.put(Integer.valueOf(number.intValue()), nums.get(k));
                    k++;
                } else {
                    return number;
                }
            }
            return null;
        }
        PdfArray nums2 = dictionary.getAsArray(PdfName.Kids);
        if (nums2 != null) {
            for (int k2 = 0; k2 < nums2.size(); k2++) {
                PdfDictionary kid = nums2.getAsDictionary(k2);
                leftOver = iterateItems(kid, leftOver);
            }
            return null;
        }
        return null;
    }
}
