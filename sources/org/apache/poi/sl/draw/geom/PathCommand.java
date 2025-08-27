package org.apache.poi.sl.draw.geom;

import java.awt.geom.Path2D;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/geom/PathCommand.class */
public interface PathCommand {
    void execute(Path2D.Double r1, Context context);
}
