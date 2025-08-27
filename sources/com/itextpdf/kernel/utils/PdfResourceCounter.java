package com.itextpdf.kernel.utils;

import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfIndirectReference;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/PdfResourceCounter.class */
public class PdfResourceCounter {
    private Map<Integer, PdfObject> resources = new HashMap();

    public PdfResourceCounter(PdfObject obj) {
        process(obj);
    }

    protected final void process(PdfObject obj) {
        PdfIndirectReference ref = obj.getIndirectReference();
        if (ref == null) {
            loopOver(obj);
        } else if (!this.resources.containsKey(Integer.valueOf(ref.getObjNumber()))) {
            this.resources.put(Integer.valueOf(ref.getObjNumber()), obj);
            loopOver(obj);
        }
    }

    protected final void loopOver(PdfObject obj) {
        switch (obj.getType()) {
            case 1:
                PdfArray array = (PdfArray) obj;
                for (int i = 0; i < array.size(); i++) {
                    process(array.get(i));
                }
                break;
            case 3:
            case 9:
                PdfDictionary dict = (PdfDictionary) obj;
                if (!PdfName.Pages.equals(dict.get(PdfName.Type))) {
                    for (PdfName name : dict.keySet()) {
                        process(dict.get(name));
                    }
                    break;
                }
                break;
        }
    }

    public Map<Integer, PdfObject> getResources() {
        return this.resources;
    }

    public long getLength(Map<Integer, PdfObject> res) throws IOException {
        long length = 0;
        Iterator<Integer> it = this.resources.keySet().iterator();
        while (it.hasNext()) {
            int ref = it.next().intValue();
            if (res == null || !res.containsKey(Integer.valueOf(ref))) {
                PdfOutputStream os = new PdfOutputStream(new IdleOutputStream());
                os.write(this.resources.get(Integer.valueOf(ref)).m850clone());
                length += os.getCurrentPos();
            }
        }
        return length;
    }
}
