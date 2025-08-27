package com.itextpdf.kernel.pdf.canvas.parser.listener;

import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import java.util.Set;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/IEventListener.class */
public interface IEventListener {
    void eventOccurred(IEventData iEventData, EventType eventType);

    Set<EventType> getSupportedEvents();
}
