package org.apache.poi.sl.draw.geom;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/geom/Formula.class */
public interface Formula {
    public static final double OOXML_DEGREE = 60000.0d;

    double evaluate(Context context);
}
