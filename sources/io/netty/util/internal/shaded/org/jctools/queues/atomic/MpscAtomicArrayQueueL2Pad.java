package io.netty.util.internal.shaded.org.jctools.queues.atomic;

/* compiled from: MpscAtomicArrayQueue.java */
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/shaded/org/jctools/queues/atomic/MpscAtomicArrayQueueL2Pad.class */
abstract class MpscAtomicArrayQueueL2Pad<E> extends MpscAtomicArrayQueueProducerLimitField<E> {
    long p00;
    long p01;
    long p02;
    long p03;
    long p04;
    long p05;
    long p06;
    long p07;
    long p10;
    long p11;
    long p12;
    long p13;
    long p14;
    long p15;
    long p16;

    MpscAtomicArrayQueueL2Pad(int capacity) {
        super(capacity);
    }
}
