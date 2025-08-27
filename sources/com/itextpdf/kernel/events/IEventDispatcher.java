package com.itextpdf.kernel.events;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/events/IEventDispatcher.class */
public interface IEventDispatcher {
    void addEventHandler(String str, IEventHandler iEventHandler);

    void dispatchEvent(Event event);

    void dispatchEvent(Event event, boolean z);

    boolean hasEventHandler(String str);

    void removeEventHandler(String str, IEventHandler iEventHandler);

    void removeAllHandlers();
}
