package com.itextpdf.kernel.pdf.canvas.parser.filter;

import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/filter/IEventFilter.class */
public interface IEventFilter {
    boolean accept(IEventData iEventData, EventType eventType);
}
