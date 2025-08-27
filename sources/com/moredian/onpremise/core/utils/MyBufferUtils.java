package com.moredian.onpremise.core.utils;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/MyBufferUtils.class */
public class MyBufferUtils {
    public static double[] asDoubleArray(byte[] input) {
        if (null == input) {
            return null;
        }
        DoubleBuffer buffer = ByteBuffer.wrap(input).asDoubleBuffer();
        double[] res = new double[buffer.remaining()];
        buffer.get(res);
        return res;
    }

    public static float[] asFloatArray(byte[] input) {
        if (null == input) {
            return null;
        }
        FloatBuffer buffer = ByteBuffer.wrap(input).asFloatBuffer();
        float[] res = new float[buffer.remaining()];
        buffer.get(res);
        return res;
    }

    public static int[] asIntArray(byte[] input) {
        if (null == input) {
            return null;
        }
        IntBuffer buffer = ByteBuffer.wrap(input).asIntBuffer();
        int[] res = new int[buffer.remaining()];
        buffer.get(res);
        return res;
    }

    public static long[] asLongArray(byte[] input) {
        if (null == input) {
            return null;
        }
        LongBuffer buffer = ByteBuffer.wrap(input).asLongBuffer();
        long[] res = new long[buffer.remaining()];
        buffer.get(res);
        return res;
    }

    public static short[] asShortArray(byte[] input) {
        if (null == input) {
            return null;
        }
        ShortBuffer buffer = ByteBuffer.wrap(input).asShortBuffer();
        short[] res = new short[buffer.remaining()];
        buffer.get(res);
        return res;
    }

    public static ByteBuffer asByteBuffer(DoubleBuffer input) {
        if (null == input) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.allocate(input.capacity() * 8);
        while (input.hasRemaining()) {
            buffer.putDouble(input.get());
        }
        return buffer;
    }

    public static byte[] asByteArray(double[] input) {
        if (null == input) {
            return null;
        }
        return asByteBuffer(DoubleBuffer.wrap(input)).array();
    }

    public static ByteBuffer asByteBuffer(FloatBuffer input) {
        if (null == input) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.allocate(input.capacity() * 4);
        while (input.hasRemaining()) {
            buffer.putFloat(input.get());
        }
        return buffer;
    }

    public static byte[] asByteArray(float[] input) {
        if (null == input) {
            return null;
        }
        return asByteBuffer(FloatBuffer.wrap(input)).array();
    }

    public static ByteBuffer asByteBuffer(IntBuffer input) {
        if (null == input) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.allocate(input.capacity() * 4);
        while (input.hasRemaining()) {
            buffer.putInt(input.get());
        }
        return buffer;
    }

    public static byte[] asByteArray(int[] input) {
        if (null == input) {
            return null;
        }
        return asByteBuffer(IntBuffer.wrap(input)).array();
    }

    public static ByteBuffer asByteBuffer(LongBuffer input) {
        if (null == input) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.allocate(input.capacity() * 8);
        while (input.hasRemaining()) {
            buffer.putLong(input.get());
        }
        return buffer;
    }

    public static byte[] asByteArray(long[] input) {
        if (null == input) {
            return null;
        }
        return asByteBuffer(LongBuffer.wrap(input)).array();
    }

    public static ByteBuffer asByteBuffer(ShortBuffer input) {
        if (null == input) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.allocate(input.capacity() * 2);
        while (input.hasRemaining()) {
            buffer.putShort(input.get());
        }
        return buffer;
    }

    public static byte[] asByteArray(short[] input) {
        if (null == input) {
            return null;
        }
        return asByteBuffer(ShortBuffer.wrap(input)).array();
    }
}
