package org.apache.poi.sl.draw.geom;

import java.awt.geom.Path2D;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/geom/ClosePathCommand.class */
public class ClosePathCommand implements PathCommand {
    ClosePathCommand() {
    }

    @Override // org.apache.poi.sl.draw.geom.PathCommand
    public void execute(Path2D.Double path, Context ctx) {
        path.closePath();
    }
}
