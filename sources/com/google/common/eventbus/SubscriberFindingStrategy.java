package com.google.common.eventbus;

import com.google.common.collect.Multimap;

/* loaded from: guava-18.0.jar:com/google/common/eventbus/SubscriberFindingStrategy.class */
interface SubscriberFindingStrategy {
    Multimap<Class<?>, EventSubscriber> findAllSubscribers(Object obj);
}
