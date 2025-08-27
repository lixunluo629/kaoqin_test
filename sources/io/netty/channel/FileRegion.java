package io.netty.channel;

import io.netty.util.ReferenceCounted;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/FileRegion.class */
public interface FileRegion extends ReferenceCounted {
    long position();

    @Deprecated
    long transfered();

    long transferred();

    long count();

    long transferTo(WritableByteChannel writableByteChannel, long j) throws IOException;

    @Override // io.netty.util.ReferenceCounted
    FileRegion retain();

    @Override // io.netty.util.ReferenceCounted
    FileRegion retain(int i);

    @Override // io.netty.util.ReferenceCounted
    FileRegion touch();

    @Override // io.netty.util.ReferenceCounted
    FileRegion touch(Object obj);
}
