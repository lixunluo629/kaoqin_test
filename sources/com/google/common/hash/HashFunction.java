package com.google.common.hash;

import com.google.common.annotations.Beta;
import java.nio.charset.Charset;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/hash/HashFunction.class */
public interface HashFunction {
    Hasher newHasher();

    Hasher newHasher(int i);

    HashCode hashInt(int i);

    HashCode hashLong(long j);

    HashCode hashBytes(byte[] bArr);

    HashCode hashBytes(byte[] bArr, int i, int i2);

    HashCode hashUnencodedChars(CharSequence charSequence);

    HashCode hashString(CharSequence charSequence, Charset charset);

    <T> HashCode hashObject(T t, Funnel<? super T> funnel);

    int bits();
}
