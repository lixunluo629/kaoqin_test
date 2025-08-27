package io.netty.util;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/ReferenceCounted.class */
public interface ReferenceCounted {
    int refCnt();

    ReferenceCounted retain();

    ReferenceCounted retain(int i);

    ReferenceCounted touch();

    ReferenceCounted touch(Object obj);

    boolean release();

    boolean release(int i);
}
