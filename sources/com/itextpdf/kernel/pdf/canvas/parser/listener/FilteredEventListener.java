package com.itextpdf.kernel.pdf.canvas.parser.listener;

import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.filter.IEventFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/FilteredEventListener.class */
public class FilteredEventListener implements IEventListener {
    protected final List<IEventListener> delegates;
    protected final List<IEventFilter[]> filters;

    public FilteredEventListener() {
        this.delegates = new ArrayList();
        this.filters = new ArrayList();
    }

    public FilteredEventListener(IEventListener delegate, IEventFilter... filterSet) {
        this();
        attachEventListener(delegate, filterSet);
    }

    public <T extends IEventListener> T attachEventListener(T delegate, IEventFilter... filterSet) {
        this.delegates.add(delegate);
        this.filters.add(filterSet);
        return delegate;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener
    public void eventOccurred(IEventData data, EventType type) {
        for (int i = 0; i < this.delegates.size(); i++) {
            IEventListener delegate = this.delegates.get(i);
            boolean filtersPassed = delegate.getSupportedEvents() == null || delegate.getSupportedEvents().contains(type);
            IEventFilter[] iEventFilterArr = this.filters.get(i);
            int length = iEventFilterArr.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                IEventFilter filter = iEventFilterArr[i2];
                if (filter.accept(data, type)) {
                    i2++;
                } else {
                    filtersPassed = false;
                    break;
                }
            }
            if (filtersPassed) {
                delegate.eventOccurred(data, type);
            }
        }
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener
    public Set<EventType> getSupportedEvents() {
        return null;
    }
}
