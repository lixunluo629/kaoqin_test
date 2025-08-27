package com.drew.imaging.tiff;

import com.drew.lang.RandomAccessReader;
import com.drew.lang.Rational;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.StringValue;
import java.io.IOException;
import java.util.Set;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/imaging/tiff/TiffHandler.class */
public interface TiffHandler {
    void setTiffMarker(int i) throws TiffProcessingException;

    boolean tryEnterSubIfd(int i);

    boolean hasFollowerIfd();

    void endingIFD();

    @Nullable
    Long tryCustomProcessFormat(int i, int i2, long j);

    boolean customProcessTag(int i, @NotNull Set<Integer> set, int i2, @NotNull RandomAccessReader randomAccessReader, int i3, int i4) throws IOException;

    void warn(@NotNull String str);

    void error(@NotNull String str);

    void setByteArray(int i, @NotNull byte[] bArr);

    void setString(int i, @NotNull StringValue stringValue);

    void setRational(int i, @NotNull Rational rational);

    void setRationalArray(int i, @NotNull Rational[] rationalArr);

    void setFloat(int i, float f);

    void setFloatArray(int i, @NotNull float[] fArr);

    void setDouble(int i, double d);

    void setDoubleArray(int i, @NotNull double[] dArr);

    void setInt8s(int i, byte b);

    void setInt8sArray(int i, @NotNull byte[] bArr);

    void setInt8u(int i, short s);

    void setInt8uArray(int i, @NotNull short[] sArr);

    void setInt16s(int i, int i2);

    void setInt16sArray(int i, @NotNull short[] sArr);

    void setInt16u(int i, int i2);

    void setInt16uArray(int i, @NotNull int[] iArr);

    void setInt32s(int i, int i2);

    void setInt32sArray(int i, @NotNull int[] iArr);

    void setInt32u(int i, long j);

    void setInt32uArray(int i, @NotNull long[] jArr);
}
