package org.apache.poi.sl.draw.geom;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.sl.draw.binding.CTAdjPoint2D;
import org.apache.poi.sl.draw.binding.CTPath2D;
import org.apache.poi.sl.draw.binding.CTPath2DArcTo;
import org.apache.poi.sl.draw.binding.CTPath2DClose;
import org.apache.poi.sl.draw.binding.CTPath2DCubicBezierTo;
import org.apache.poi.sl.draw.binding.CTPath2DLineTo;
import org.apache.poi.sl.draw.binding.CTPath2DMoveTo;
import org.apache.poi.sl.draw.binding.CTPath2DQuadBezierTo;
import org.apache.poi.sl.usermodel.PaintStyle;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/geom/Path.class */
public class Path {
    private final List<PathCommand> commands;
    PaintStyle.PaintModifier _fill;
    boolean _stroke;
    long _w;
    long _h;

    public Path() {
        this(true, true);
    }

    public Path(boolean fill, boolean stroke) {
        this.commands = new ArrayList();
        this._w = -1L;
        this._h = -1L;
        this._fill = fill ? PaintStyle.PaintModifier.NORM : PaintStyle.PaintModifier.NONE;
        this._stroke = stroke;
    }

    public Path(CTPath2D spPath) {
        switch (spPath.getFill()) {
            case NONE:
                this._fill = PaintStyle.PaintModifier.NONE;
                break;
            case DARKEN:
                this._fill = PaintStyle.PaintModifier.DARKEN;
                break;
            case DARKEN_LESS:
                this._fill = PaintStyle.PaintModifier.DARKEN_LESS;
                break;
            case LIGHTEN:
                this._fill = PaintStyle.PaintModifier.LIGHTEN;
                break;
            case LIGHTEN_LESS:
                this._fill = PaintStyle.PaintModifier.LIGHTEN_LESS;
                break;
            case NORM:
            default:
                this._fill = PaintStyle.PaintModifier.NORM;
                break;
        }
        this._stroke = spPath.isStroke();
        this._w = spPath.isSetW() ? spPath.getW() : -1L;
        this._h = spPath.isSetH() ? spPath.getH() : -1L;
        this.commands = new ArrayList();
        for (Object ch2 : spPath.getCloseOrMoveToOrLnTo()) {
            if (ch2 instanceof CTPath2DMoveTo) {
                CTAdjPoint2D pt = ((CTPath2DMoveTo) ch2).getPt();
                this.commands.add(new MoveToCommand(pt));
            } else if (ch2 instanceof CTPath2DLineTo) {
                CTAdjPoint2D pt2 = ((CTPath2DLineTo) ch2).getPt();
                this.commands.add(new LineToCommand(pt2));
            } else if (ch2 instanceof CTPath2DArcTo) {
                CTPath2DArcTo arc = (CTPath2DArcTo) ch2;
                this.commands.add(new ArcToCommand(arc));
            } else if (ch2 instanceof CTPath2DQuadBezierTo) {
                CTPath2DQuadBezierTo bez = (CTPath2DQuadBezierTo) ch2;
                CTAdjPoint2D pt1 = bez.getPt().get(0);
                CTAdjPoint2D pt22 = bez.getPt().get(1);
                this.commands.add(new QuadToCommand(pt1, pt22));
            } else if (ch2 instanceof CTPath2DCubicBezierTo) {
                CTPath2DCubicBezierTo bez2 = (CTPath2DCubicBezierTo) ch2;
                CTAdjPoint2D pt12 = bez2.getPt().get(0);
                CTAdjPoint2D pt23 = bez2.getPt().get(1);
                CTAdjPoint2D pt3 = bez2.getPt().get(2);
                this.commands.add(new CurveToCommand(pt12, pt23, pt3));
            } else if (ch2 instanceof CTPath2DClose) {
                this.commands.add(new ClosePathCommand());
            } else {
                throw new IllegalStateException("Unsupported path segment: " + ch2);
            }
        }
    }

    public void addCommand(PathCommand cmd) {
        this.commands.add(cmd);
    }

    public Path2D.Double getPath(Context ctx) {
        Path2D.Double path = new Path2D.Double();
        for (PathCommand cmd : this.commands) {
            cmd.execute(path, ctx);
        }
        return path;
    }

    public boolean isStroked() {
        return this._stroke;
    }

    public boolean isFilled() {
        return this._fill != PaintStyle.PaintModifier.NONE;
    }

    public PaintStyle.PaintModifier getFill() {
        return this._fill;
    }

    public long getW() {
        return this._w;
    }

    public long getH() {
        return this._h;
    }
}
