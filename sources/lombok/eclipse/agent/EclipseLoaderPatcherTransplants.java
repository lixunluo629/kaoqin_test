package lombok.eclipse.agent;

/* JADX WARN: Classes with same name are omitted:
  lombok-1.16.22.jar:Class50/lombok/eclipse/agent/EclipseLoaderPatcherTransplants.SCL.lombok
 */
/* loaded from: lombok-1.16.22.jar:lombok/eclipse/agent/EclipseLoaderPatcherTransplants.SCL.lombok */
public class EclipseLoaderPatcherTransplants {
    static Class class$0;

    public static boolean overrideLoadDecide(ClassLoader original, String name, boolean resolve) {
        return name.startsWith("lombok.");
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x014c A[Catch: all -> 0x01df, Exception -> 0x025c, FINALLY_INSNS, TryCatch #0 {Exception -> 0x025c, blocks: (B:2:0x0000, B:4:0x0019, B:5:0x0021, B:6:0x0022, B:8:0x0031, B:10:0x0061, B:11:0x006f, B:12:0x008a, B:19:0x00bd, B:65:0x014c, B:66:0x0151, B:15:0x00a4, B:17:0x00b3, B:18:0x00bc, B:26:0x014c, B:27:0x0151, B:23:0x0144, B:30:0x015c, B:32:0x01db, B:35:0x01e1, B:36:0x01e2, B:39:0x01e7, B:41:0x0212, B:42:0x0218, B:45:0x022a, B:43:0x021e, B:44:0x0229, B:47:0x0255), top: B:67:0x0000, inners: #1 }] */
    /* JADX WARN: Type inference failed for: r0v29, types: [java.lang.Class, java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v37, types: [java.lang.String, java.lang.Throwable] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.Class overrideLoadResult(java.lang.ClassLoader r9, java.lang.String r10, boolean r11) throws java.lang.NoSuchFieldException, java.lang.NoSuchMethodException, java.lang.SecurityException, java.lang.ClassNotFoundException {
        /*
            Method dump skipped, instructions count: 661
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: lombok.eclipse.agent.EclipseLoaderPatcherTransplants.overrideLoadResult(java.lang.ClassLoader, java.lang.String, boolean):java.lang.Class");
    }
}
