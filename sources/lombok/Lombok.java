package lombok;

/* loaded from: lombok-1.16.22.jar:lombok/Lombok.class */
public class Lombok {
    public static RuntimeException sneakyThrow(Throwable t) {
        if (t == null) {
            throw new NullPointerException("t");
        }
        return (RuntimeException) sneakyThrow0(t);
    }

    private static <T extends Throwable> T sneakyThrow0(Throwable t) throws Throwable {
        throw t;
    }

    public static <T> T preventNullAnalysis(T value) {
        return value;
    }

    public static <T> T checkNotNull(T value, String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
        return value;
    }
}
