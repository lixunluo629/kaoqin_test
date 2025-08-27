package javax.persistence.spi;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/* loaded from: persistence-api-1.0.jar:javax/persistence/spi/ClassTransformer.class */
public interface ClassTransformer {
    byte[] transform(ClassLoader classLoader, String str, Class<?> cls, ProtectionDomain protectionDomain, byte[] bArr) throws IllegalClassFormatException;
}
