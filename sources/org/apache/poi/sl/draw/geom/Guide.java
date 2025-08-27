package org.apache.poi.sl.draw.geom;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import io.jsonwebtoken.Claims;
import org.apache.poi.sl.draw.binding.CTGeomGuide;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/geom/Guide.class */
public class Guide implements Formula {
    private final String name;
    private final String fmla;
    private final Op op;
    private final String[] operands;

    /* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/geom/Guide$Op.class */
    enum Op {
        muldiv,
        addsub,
        adddiv,
        ifelse,
        val,
        abs,
        sqrt,
        max,
        min,
        at2,
        sin,
        cos,
        tan,
        cat2,
        sat2,
        pin,
        mod
    }

    public Guide(CTGeomGuide gd) {
        this(gd.getName(), gd.getFmla());
    }

    public Guide(String nm, String fm) {
        this.name = nm;
        this.fmla = fm;
        this.operands = fm.split("\\s+");
        this.op = Op.valueOf(this.operands[0].replace("*", "mul").replace("/", "div").replace("+", BeanUtil.PREFIX_ADDER).replace("-", Claims.SUBJECT).replace("?:", "ifelse"));
    }

    public String getName() {
        return this.name;
    }

    String getFormula() {
        return this.fmla;
    }

    @Override // org.apache.poi.sl.draw.geom.Formula
    public double evaluate(Context ctx) {
        double x = this.operands.length > 1 ? ctx.getValue(this.operands[1]) : 0.0d;
        double y = this.operands.length > 2 ? ctx.getValue(this.operands[2]) : 0.0d;
        double z = this.operands.length > 3 ? ctx.getValue(this.operands[3]) : 0.0d;
        switch (this.op) {
            case abs:
                return Math.abs(x);
            case adddiv:
                return (x + y) / z;
            case addsub:
                return (x + y) - z;
            case at2:
                return Math.toDegrees(Math.atan2(y, x)) * 60000.0d;
            case cos:
                return x * Math.cos(Math.toRadians(y / 60000.0d));
            case cat2:
                return x * Math.cos(Math.atan2(z, y));
            case ifelse:
                return x > 0.0d ? y : z;
            case val:
                return x;
            case max:
                return Math.max(x, y);
            case min:
                return Math.min(x, y);
            case mod:
                return Math.sqrt((x * x) + (y * y) + (z * z));
            case muldiv:
                return (x * y) / z;
            case pin:
                if (y < x) {
                    return x;
                }
                if (y > z) {
                    return z;
                }
                return y;
            case sat2:
                return x * Math.sin(Math.atan2(z, y));
            case sin:
                return x * Math.sin(Math.toRadians(y / 60000.0d));
            case sqrt:
                return Math.sqrt(x);
            case tan:
                return x * Math.tan(Math.toRadians(y / 60000.0d));
            default:
                return 0.0d;
        }
    }
}
