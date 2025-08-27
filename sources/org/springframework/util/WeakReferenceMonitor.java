package org.springframework.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Deprecated
/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/WeakReferenceMonitor.class */
public class WeakReferenceMonitor {
    private static final Log logger = LogFactory.getLog(WeakReferenceMonitor.class);
    private static final ReferenceQueue<Object> handleQueue = new ReferenceQueue<>();
    private static final Map<Reference<?>, ReleaseListener> trackedEntries = new HashMap();
    private static Thread monitoringThread = null;

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/WeakReferenceMonitor$ReleaseListener.class */
    public interface ReleaseListener {
        void released();
    }

    public static void monitor(Object handle, ReleaseListener listener) {
        if (logger.isDebugEnabled()) {
            logger.debug("Monitoring handle [" + handle + "] with release listener [" + listener + "]");
        }
        WeakReference<Object> weakRef = new WeakReference<>(handle, handleQueue);
        addEntry(weakRef, listener);
    }

    private static void addEntry(Reference<?> ref, ReleaseListener entry) {
        synchronized (WeakReferenceMonitor.class) {
            trackedEntries.put(ref, entry);
            if (monitoringThread == null) {
                monitoringThread = new Thread(new MonitoringProcess(), WeakReferenceMonitor.class.getName());
                monitoringThread.setDaemon(true);
                monitoringThread.start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ReleaseListener removeEntry(Reference<?> reference) {
        ReleaseListener releaseListenerRemove;
        synchronized (WeakReferenceMonitor.class) {
            releaseListenerRemove = trackedEntries.remove(reference);
        }
        return releaseListenerRemove;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean keepMonitoringThreadAlive() {
        synchronized (WeakReferenceMonitor.class) {
            if (!trackedEntries.isEmpty()) {
                return true;
            }
            logger.debug("No entries left to track - stopping reference monitor thread");
            monitoringThread = null;
            return false;
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/WeakReferenceMonitor$MonitoringProcess.class */
    private static class MonitoringProcess implements Runnable {
        private MonitoringProcess() {
        }

        @Override // java.lang.Runnable
        public void run() throws InterruptedException {
            WeakReferenceMonitor.logger.debug("Starting reference monitor thread");
            while (WeakReferenceMonitor.keepMonitoringThreadAlive()) {
                try {
                    Reference<?> reference = WeakReferenceMonitor.handleQueue.remove();
                    ReleaseListener entry = WeakReferenceMonitor.removeEntry(reference);
                    if (entry != null) {
                        try {
                            entry.released();
                        } catch (Throwable ex) {
                            WeakReferenceMonitor.logger.warn("Reference release listener threw exception", ex);
                        }
                    }
                } catch (InterruptedException ex2) {
                    synchronized (WeakReferenceMonitor.class) {
                        Thread unused = WeakReferenceMonitor.monitoringThread = null;
                        WeakReferenceMonitor.logger.debug("Reference monitor thread interrupted", ex2);
                        return;
                    }
                }
            }
        }
    }
}
