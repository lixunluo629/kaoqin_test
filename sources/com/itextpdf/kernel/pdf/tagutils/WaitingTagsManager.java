package com.itextpdf.kernel.pdf.tagutils;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.tagging.IStructureNode;
import com.itextpdf.kernel.pdf.tagging.PdfStructElem;
import java.util.HashMap;
import java.util.Map;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/tagutils/WaitingTagsManager.class */
public class WaitingTagsManager {
    private Map<Object, PdfStructElem> associatedObjToWaitingTag = new HashMap();
    private Map<PdfDictionary, Object> waitingTagToAssociatedObj = new HashMap();

    WaitingTagsManager() {
    }

    public Object assignWaitingState(TagTreePointer pointerToTag, Object associatedObj) {
        if (associatedObj == null) {
            throw new IllegalArgumentException("Passed associated object can not be null.");
        }
        return saveAssociatedObjectForWaitingTag(associatedObj, pointerToTag.getCurrentStructElem());
    }

    public boolean isObjectAssociatedWithWaitingTag(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Passed associated object can not be null.");
        }
        return this.associatedObjToWaitingTag.containsKey(obj);
    }

    public boolean tryMovePointerToWaitingTag(TagTreePointer tagPointer, Object associatedObject) {
        PdfStructElem waitingStructElem;
        if (associatedObject != null && (waitingStructElem = this.associatedObjToWaitingTag.get(associatedObject)) != null) {
            tagPointer.setCurrentStructElem(waitingStructElem);
            return true;
        }
        return false;
    }

    public boolean removeWaitingState(Object associatedObject) {
        if (associatedObject != null) {
            PdfStructElem structElem = this.associatedObjToWaitingTag.remove(associatedObject);
            removeWaitingStateAndFlushIfParentFlushed(structElem);
            return structElem != null;
        }
        return false;
    }

    public void removeAllWaitingStates() {
        for (PdfStructElem structElem : this.associatedObjToWaitingTag.values()) {
            removeWaitingStateAndFlushIfParentFlushed(structElem);
        }
        this.associatedObjToWaitingTag.clear();
    }

    PdfStructElem getStructForObj(Object associatedObj) {
        return this.associatedObjToWaitingTag.get(associatedObj);
    }

    Object getObjForStructDict(PdfDictionary structDict) {
        return this.waitingTagToAssociatedObj.get(structDict);
    }

    Object saveAssociatedObjectForWaitingTag(Object associatedObj, PdfStructElem structElem) {
        this.associatedObjToWaitingTag.put(associatedObj, structElem);
        return this.waitingTagToAssociatedObj.put(structElem.getPdfObject(), associatedObj);
    }

    IStructureNode flushTag(PdfStructElem tagStruct) {
        Object associatedObj = this.waitingTagToAssociatedObj.remove(tagStruct.getPdfObject());
        if (associatedObj != null) {
            this.associatedObjToWaitingTag.remove(associatedObj);
        }
        IStructureNode parent = tagStruct.getParent();
        flushStructElementAndItKids(tagStruct);
        return parent;
    }

    private void flushStructElementAndItKids(PdfStructElem elem) {
        if (this.waitingTagToAssociatedObj.containsKey(elem.getPdfObject())) {
            return;
        }
        for (IStructureNode kid : elem.getKids()) {
            if (kid instanceof PdfStructElem) {
                flushStructElementAndItKids((PdfStructElem) kid);
            }
        }
        elem.flush();
    }

    private void removeWaitingStateAndFlushIfParentFlushed(PdfStructElem structElem) {
        if (structElem != null) {
            this.waitingTagToAssociatedObj.remove(structElem.getPdfObject());
            IStructureNode parent = structElem.getParent();
            if ((parent instanceof PdfStructElem) && ((PdfStructElem) parent).isFlushed()) {
                flushStructElementAndItKids(structElem);
            }
        }
    }
}
