package org.apache.poi.sl.draw.geom;

import org.apache.poi.sl.draw.binding.CTGeomGuide;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/geom/AdjustValue.class */
public class AdjustValue extends Guide {
    public AdjustValue(CTGeomGuide gd) {
        super(gd.getName(), gd.getFmla());
    }

    @Override // org.apache.poi.sl.draw.geom.Guide, org.apache.poi.sl.draw.geom.Formula
    public double evaluate(Context ctx) {
        String name = getName();
        Guide adj = ctx.getAdjustValue(name);
        return adj != null ? adj.evaluate(ctx) : super.evaluate(ctx);
    }
}
