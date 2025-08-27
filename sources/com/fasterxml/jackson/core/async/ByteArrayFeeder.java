package com.fasterxml.jackson.core.async;

import java.io.IOException;

/* loaded from: jackson-core-2.11.2.jar:com/fasterxml/jackson/core/async/ByteArrayFeeder.class */
public interface ByteArrayFeeder extends NonBlockingInputFeeder {
    void feedInput(byte[] bArr, int i, int i2) throws IOException;
}
