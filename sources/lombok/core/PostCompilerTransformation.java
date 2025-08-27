package lombok.core;

/* loaded from: lombok-1.16.22.jar:lombok/core/PostCompilerTransformation.SCL.lombok */
public interface PostCompilerTransformation {
    byte[] applyTransformations(byte[] bArr, String str, DiagnosticsReceiver diagnosticsReceiver);
}
