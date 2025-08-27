package org.apache.poi.ss.usermodel;

import org.apache.poi.util.Internal;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/ClientAnchor.class */
public interface ClientAnchor {
    short getCol1();

    void setCol1(int i);

    short getCol2();

    void setCol2(int i);

    int getRow1();

    void setRow1(int i);

    int getRow2();

    void setRow2(int i);

    int getDx1();

    void setDx1(int i);

    int getDy1();

    void setDy1(int i);

    int getDy2();

    void setDy2(int i);

    int getDx2();

    void setDx2(int i);

    void setAnchorType(AnchorType anchorType);

    AnchorType getAnchorType();

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/ClientAnchor$AnchorType.class */
    public enum AnchorType {
        MOVE_AND_RESIZE(0),
        DONT_MOVE_DO_RESIZE(1),
        MOVE_DONT_RESIZE(2),
        DONT_MOVE_AND_RESIZE(3);

        public final short value;

        AnchorType(int value) {
            this.value = (short) value;
        }

        @Internal
        public static AnchorType byId(int value) {
            return values()[value];
        }
    }
}
