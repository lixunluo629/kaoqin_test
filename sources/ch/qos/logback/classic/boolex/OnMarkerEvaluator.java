package ch.qos.logback.classic.boolex;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.boolex.EventEvaluatorBase;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Marker;

/* JADX WARN: Classes with same name are omitted:
  logback-classic-1.2.3.jar:ch/qos/logback/classic/boolex/OnMarkerEvaluator.class
 */
/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/boolex/OnMarkerEvaluator.class */
public class OnMarkerEvaluator extends EventEvaluatorBase<ILoggingEvent> {
    List<String> markerList = new ArrayList();

    public void addMarker(String markerStr) {
        this.markerList.add(markerStr);
    }

    @Override // ch.qos.logback.core.boolex.EventEvaluator
    public boolean evaluate(ILoggingEvent event) throws NullPointerException, EvaluationException {
        Marker eventsMarker = event.getMarker();
        if (eventsMarker == null) {
            return false;
        }
        for (String markerStr : this.markerList) {
            if (eventsMarker.contains(markerStr)) {
                return true;
            }
        }
        return false;
    }
}
