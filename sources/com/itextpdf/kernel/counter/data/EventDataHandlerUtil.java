package com.itextpdf.kernel.counter.data;

import com.itextpdf.io.LogMessageConstant;
import java.util.Comparator;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/data/EventDataHandlerUtil.class */
public final class EventDataHandlerUtil {
    private EventDataHandlerUtil() {
    }

    public static <T, V extends EventData<T>> void registerProcessAllShutdownHook(final EventDataHandler<T, V> dataHandler) {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread() { // from class: com.itextpdf.kernel.counter.data.EventDataHandlerUtil.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    dataHandler.tryProcessRest();
                }
            });
        } catch (SecurityException e) {
            LoggerFactory.getLogger((Class<?>) EventDataHandlerUtil.class).error(LogMessageConstant.UNABLE_TO_REGISTER_EVENT_DATA_HANDLER_SHUTDOWN_HOOK);
        } catch (Exception e2) {
        }
    }

    public static <T, V extends EventData<T>> void registerTimedProcessing(final EventDataHandler<T, V> dataHandler) {
        Thread thread = new Thread() { // from class: com.itextpdf.kernel.counter.data.EventDataHandlerUtil.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() throws InterruptedException {
                while (true) {
                    try {
                        Thread.sleep(dataHandler.getWaitTime().getTime());
                        dataHandler.tryProcessNextAsync(false);
                    } catch (InterruptedException e) {
                        return;
                    } catch (Exception any) {
                        LoggerFactory.getLogger((Class<?>) EventDataHandlerUtil.class).error(LogMessageConstant.UNEXPECTED_EVENT_HANDLER_SERVICE_THREAD_EXCEPTION, (Throwable) any);
                        return;
                    }
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/data/EventDataHandlerUtil$BiggerCountComparator.class */
    public static class BiggerCountComparator<T, V extends EventData<T>> implements Comparator<V> {
        @Override // java.util.Comparator
        public int compare(V o1, V o2) {
            return Long.compare(o2.getCount(), o1.getCount());
        }
    }
}
