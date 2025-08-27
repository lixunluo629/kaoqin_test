package com.moredian;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/FaceDet.class */
public class                                                                                                                                                                       FaceDet {
    public static native boolean Init(int i, int i2);

    public static native boolean DeInit();

    public static native int[] Process(byte[] bArr);

    public static native int NirLiveJudgement(byte[] bArr, byte[] bArr2, int i);

    public static native float[] ExtractFeature(int i);

    public static native float MatchFeatures(float[] fArr, float[] fArr2, int i);

    public static native byte[] ExtractFeatureByImage(byte[] bArr, int i, int i2);

    public static native byte[] GetVersion();

    public static native int[] DetectByImage(byte[] bArr, int i, int i2);

    public static native int LoadAllFeatures(byte[] bArr, int i, int i2);

    public static native void SetPoseThreshold(int i, int i2, int i3);

    public static native float[] ServerMatchFeatures1toN(byte[] bArr);

    static {
        System.load("/home/mdapp/onpremise-frontend/bin/libfaceDet_jni.so");
    }
}




