package com.google.common.hash;

import com.google.common.annotations.Beta;
import java.nio.charset.Charset;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/hash/PrimitiveSink.class */
public interface PrimitiveSink {
    PrimitiveSink putByte(byte b);

    PrimitiveSink putBytes(byte[] bArr);

    PrimitiveSink putBytes(byte[] bArr, int i, int i2);

    PrimitiveSink putShort(short s);

    PrimitiveSink putInt(int i);

    PrimitiveSink putLong(long j);

    PrimitiveSink putFloat(float f);

    PrimitiveSink putDouble(double d);

    PrimitiveSink putBoolean(boolean z);

    PrimitiveSink putChar(char c);

    PrimitiveSink putUnencodedChars(CharSequence charSequence);

    PrimitiveSink putString(CharSequence charSequence, Charset charset);
}
