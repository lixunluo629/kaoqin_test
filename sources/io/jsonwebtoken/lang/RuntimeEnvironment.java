package io.jsonwebtoken.lang;

import java.security.Provider;
import java.security.Security;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/lang/RuntimeEnvironment.class */
public class RuntimeEnvironment {
    private static final AtomicBoolean bcLoaded = new AtomicBoolean(false);
    private static final String BC_PROVIDER_CLASS_NAME = "org.bouncycastle.jce.provider.BouncyCastleProvider";
    public static final boolean BOUNCY_CASTLE_AVAILABLE = Classes.isAvailable(BC_PROVIDER_CLASS_NAME);

    static {
        enableBouncyCastleIfPossible();
    }

    public static void enableBouncyCastleIfPossible() {
        if (bcLoaded.get()) {
            return;
        }
        try {
            Class clazz = Classes.forName(BC_PROVIDER_CLASS_NAME);
            Provider[] providers = Security.getProviders();
            for (Provider provider : providers) {
                if (clazz.isInstance(provider)) {
                    bcLoaded.set(true);
                    return;
                }
            }
            Security.addProvider((Provider) Classes.newInstance(clazz));
            bcLoaded.set(true);
        } catch (UnknownClassException e) {
        }
    }
}
