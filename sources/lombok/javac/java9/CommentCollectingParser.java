package lombok.javac.java9;

import com.sun.tools.javac.parser.JavacParser;
import com.sun.tools.javac.parser.Lexer;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.JCTree;
import java.util.List;
import lombok.javac.CommentCatcher;
import lombok.javac.CommentInfo;
import lombok.javac.java8.CommentCollectingScanner;

/* loaded from: lombok-1.16.22.jar:lombok/javac/java9/CommentCollectingParser.SCL.lombok */
class CommentCollectingParser extends JavacParser {
    private final Lexer lexer;

    protected CommentCollectingParser(ParserFactory fac, Lexer S, boolean keepDocComments, boolean keepLineMap, boolean keepEndPositions, boolean parseModuleInfo) {
        super(fac, S, keepDocComments, keepLineMap, keepEndPositions, parseModuleInfo);
        this.lexer = S;
    }

    public JCTree.JCCompilationUnit parseCompilationUnit() {
        JCTree.JCCompilationUnit result = super.parseCompilationUnit();
        if (this.lexer instanceof CommentCollectingScanner) {
            List<CommentInfo> comments = this.lexer.getComments();
            CommentCatcher.JCCompilationUnit_comments.set(result, comments);
        }
        return result;
    }
}
