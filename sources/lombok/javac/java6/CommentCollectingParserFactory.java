package lombok.javac.java6;

import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.parser.Lexer;
import com.sun.tools.javac.parser.Parser;
import com.sun.tools.javac.util.Context;
import java.lang.reflect.Field;

/* loaded from: lombok-1.16.22.jar:lombok/javac/java6/CommentCollectingParserFactory.SCL.lombok */
public class CommentCollectingParserFactory extends Parser.Factory {
    static Context.Key<Parser.Factory> key() {
        return parserFactoryKey;
    }

    protected CommentCollectingParserFactory(Context context) {
        super(context);
    }

    public Parser newParser(Lexer S, boolean keepDocComments, boolean genEndPos) {
        Object x = new CommentCollectingParser(this, S, true);
        return (Parser) x;
    }

    public static void setInCompiler(JavaCompiler compiler, Context context) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        context.put(key(), (Parser.Factory) null);
        try {
            Field field = JavaCompiler.class.getDeclaredField("parserFactory");
            field.setAccessible(true);
            field.set(compiler, new CommentCollectingParserFactory(context));
        } catch (Exception e) {
            throw new IllegalStateException("Could not set comment sensitive parser in the compiler", e);
        }
    }
}
