package org.apache.poi.hssf.usermodel;

import org.apache.poi.ddf.EscherChildAnchorRecord;
import org.apache.poi.ddf.EscherClientAnchorRecord;
import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.ss.usermodel.ChildAnchor;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/usermodel/HSSFAnchor.class */
public abstract class HSSFAnchor implements ChildAnchor {
    protected boolean _isHorizontallyFlipped = false;
    protected boolean _isVerticallyFlipped = false;

    public abstract boolean isHorizontallyFlipped();

    public abstract boolean isVerticallyFlipped();

    protected abstract EscherRecord getEscherAnchor();

    protected abstract void createEscherAnchor();

    public HSSFAnchor() {
        createEscherAnchor();
    }

    public HSSFAnchor(int dx1, int dy1, int dx2, int dy2) {
        createEscherAnchor();
        setDx1(dx1);
        setDy1(dy1);
        setDx2(dx2);
        setDy2(dy2);
    }

    public static HSSFAnchor createAnchorFromEscher(EscherContainerRecord container) {
        if (null != container.getChildById((short) -4081)) {
            return new HSSFChildAnchor((EscherChildAnchorRecord) container.getChildById((short) -4081));
        }
        if (null != container.getChildById((short) -4080)) {
            return new HSSFClientAnchor((EscherClientAnchorRecord) container.getChildById((short) -4080));
        }
        return null;
    }
}
