package org.aspectj.weaver.bcel;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/UnwovenClassFileWithThirdPartyManagedBytecode.class */
public class UnwovenClassFileWithThirdPartyManagedBytecode extends UnwovenClassFile {
    IByteCodeProvider provider;

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/UnwovenClassFileWithThirdPartyManagedBytecode$IByteCodeProvider.class */
    public interface IByteCodeProvider {
        byte[] getBytes();
    }

    public UnwovenClassFileWithThirdPartyManagedBytecode(String filename, String classname, IByteCodeProvider provider) {
        super(filename, classname, null);
        this.provider = provider;
    }

    @Override // org.aspectj.weaver.bcel.UnwovenClassFile, org.aspectj.weaver.IUnwovenClassFile
    public byte[] getBytes() {
        return this.provider.getBytes();
    }
}
