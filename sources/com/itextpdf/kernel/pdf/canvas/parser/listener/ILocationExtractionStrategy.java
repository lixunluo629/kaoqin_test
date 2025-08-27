package com.itextpdf.kernel.pdf.canvas.parser.listener;

import java.util.Collection;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/ILocationExtractionStrategy.class */
public interface ILocationExtractionStrategy extends IEventListener {
    Collection<IPdfTextLocation> getResultantLocations();
}
