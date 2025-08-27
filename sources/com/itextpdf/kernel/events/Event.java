package com.itextpdf.kernel.events;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/events/Event.class */
public class Event {
    protected String type;

    public Event(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
