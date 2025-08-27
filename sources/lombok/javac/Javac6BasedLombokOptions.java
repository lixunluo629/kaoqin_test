package lombok.javac;

import ch.qos.logback.core.CoreConstants;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Options;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.Lombok;

/* loaded from: lombok-1.16.22.jar:lombok/javac/Javac6BasedLombokOptions.SCL.lombok */
public class Javac6BasedLombokOptions extends LombokOptions {
    private static final Method optionName_valueOf;
    private static final Method options_put;

    static {
        try {
            Class<?> optionNameClass = Class.forName("com.sun.tools.javac.main.OptionName");
            optionName_valueOf = optionNameClass.getMethod(CoreConstants.VALUE_OF, String.class);
            options_put = Class.forName("com.sun.tools.javac.util.Options").getMethod("put", optionNameClass, String.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't initialize Javac6-based lombok options due to reflection issue.", e);
        }
    }

    public static Javac6BasedLombokOptions replaceWithDelombokOptions(Context context) {
        Options options = Options.instance(context);
        context.put(optionsKey, (Options) null);
        Javac6BasedLombokOptions result = new Javac6BasedLombokOptions(context);
        result.putAll(options);
        return result;
    }

    private Javac6BasedLombokOptions(Context context) {
        super(context);
    }

    @Override // lombok.javac.LombokOptions
    public void putJavacOption(String optionName, String value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            options_put.invoke(this, optionName_valueOf.invoke(null, optionName), value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Can't initialize Javac6-based lombok options due to reflection issue.", e);
        } catch (InvocationTargetException e2) {
            throw Lombok.sneakyThrow(e2.getCause());
        }
    }
}
