package com.itextpdf.kernel.counter;

import com.itextpdf.kernel.counter.context.IContext;
import com.itextpdf.kernel.counter.event.IEvent;
import com.itextpdf.kernel.counter.event.IMetaInfo;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/EventCounterHandler.class */
public class EventCounterHandler {
    private static final EventCounterHandler instance = new EventCounterHandler();
    private Map<IEventCounterFactory, Boolean> factories = new ConcurrentHashMap();

    private EventCounterHandler() {
        register(new SimpleEventCounterFactory(new DefaultEventCounter()));
    }

    public static EventCounterHandler getInstance() {
        return instance;
    }

    public void onEvent(IEvent event, IMetaInfo metaInfo, Class<?> caller) {
        IContext context = null;
        boolean contextInitialized = false;
        for (IEventCounterFactory factory : this.factories.keySet()) {
            EventCounter counter = factory.getCounter(caller);
            if (counter != null) {
                if (!contextInitialized) {
                    if (metaInfo != null) {
                        context = ContextManager.getInstance().getContext(metaInfo.getClass());
                    }
                    if (context == null) {
                        context = ContextManager.getInstance().getContext(caller);
                    }
                    if (context == null) {
                        context = ContextManager.getInstance().getContext(event.getClass());
                    }
                    contextInitialized = true;
                }
                if ((context != null && context.allow(event)) || (context == null && counter.fallback.allow(event))) {
                    counter.onEvent(event, metaInfo);
                }
            }
        }
    }

    public void register(IEventCounterFactory factory) {
        if (factory != null) {
            this.factories.put(factory, true);
        }
    }

    public boolean unregister(IEventCounterFactory factory) {
        return (factory == null || this.factories.remove(factory) == null) ? false : true;
    }
}
