package com.itextpdf.kernel.counter.context;

import com.itextpdf.kernel.counter.event.IEvent;
import com.itextpdf.kernel.counter.event.IGenericEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/context/GenericContext.class */
public class GenericContext implements IContext {
    private final Set<String> supported = new HashSet();

    public GenericContext(Collection<String> supported) {
        this.supported.addAll(supported);
    }

    @Override // com.itextpdf.kernel.counter.context.IContext
    public boolean allow(IEvent event) {
        if (event instanceof IGenericEvent) {
            return this.supported.contains(((IGenericEvent) event).getOriginId());
        }
        return false;
    }
}
