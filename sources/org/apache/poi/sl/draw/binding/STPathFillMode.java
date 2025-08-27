package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlEnum
@XmlType(name = "ST_PathFillMode", namespace = XSSFRelation.NS_DRAWINGML)
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/STPathFillMode.class */
public enum STPathFillMode {
    NONE("none"),
    NORM("norm"),
    LIGHTEN("lighten"),
    LIGHTEN_LESS("lightenLess"),
    DARKEN("darken"),
    DARKEN_LESS("darkenLess");

    private final String value;

    STPathFillMode(String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }

    public static STPathFillMode fromValue(String v) {
        STPathFillMode[] arr$ = values();
        for (STPathFillMode c : arr$) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
