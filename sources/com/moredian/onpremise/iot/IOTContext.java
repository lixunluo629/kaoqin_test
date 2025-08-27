package com.moredian.onpremise.iot;

import com.moredian.onpremise.event.IOTEvent;

/* loaded from: onpremise-iot-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/iot/IOTContext.class */
public class IOTContext<E> {
    private E sourceEvent;
    private IOTSession session;

    private IOTContext(E event, IOTSession session) {
        this.session = session;
        this.sourceEvent = event;
    }

    public E getSourceEvent() {
        return this.sourceEvent;
    }

    public void setSourceEvent(E sourceEvent) {
        this.sourceEvent = sourceEvent;
    }

    public IOTSession getSession() {
        return this.session;
    }

    public void setSession(IOTSession session) {
        this.session = session;
    }

    public static IOTContext buildContext(IOTEvent event, IOTSession session) {
        return new IOTContext(event, session);
    }
}
