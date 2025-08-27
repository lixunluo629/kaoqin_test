package org.terracotta.offheapstore.concurrent;

import java.util.List;
import org.terracotta.offheapstore.MapInternals;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/concurrent/ConcurrentMapInternals.class */
public interface ConcurrentMapInternals extends MapInternals {
    List<MapInternals> getSegmentInternals();
}
