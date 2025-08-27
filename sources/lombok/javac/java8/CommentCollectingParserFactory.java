package lombok.javac.java8;

import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.parser.JavacParser;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.parser.ScannerFactory;
import com.sun.tools.javac.util.Context;
import java.lang.reflect.Field;

/* loaded from: lombok-1.16.22.jar:lombok/javac/java8/CommentCollectingParserFactory.SCL.lombok */
public class CommentCollectingParserFactory extends ParserFactory {
    private final Context context;

    static Context.Key<ParserFactory> key() {
        return parserFactoryKey;
    }

    protected CommentCollectingParserFactory(Context context) {
        super(context);
        this.context = context;
    }

    public JavacParser newParser(CharSequence input, boolean keepDocComments, boolean keepEndPos, boolean keepLineMap) {
        ScannerFactory scannerFactory = ScannerFactory.instance(this.context);
        Object x = new CommentCollectingParser(this, scannerFactory.newScanner(input, true), true, keepLineMap, keepEndPos);
        return (JavacParser) x;
    }

    public JavacParser newParser(CharSequence input, boolean keepDocComments, boolean keepEndPos, boolean keepLineMap, boolean parseModuleInfo) {
        ScannerFactory scannerFactory = ScannerFactory.instance(this.context);
        Object x = new CommentCollectingParser(this, scannerFactory.newScanner(input, true), true, keepLineMap, keepEndPos);
        return (JavacParser) x;
    }

    public static void setInCompiler(JavaCompiler compiler, Context context) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        context.put(key(), (ParserFactory) null);
        try {
            Field field = JavaCompiler.class.getDeclaredField("parserFactory");
            field.setAccessible(true);
            field.set(compiler, new CommentCollectingParserFactory(context));
        } catch (Exception e) {
            throw new IllegalStateException("Could not set comment sensitive parser in the compiler", e);
        }
    }
}
