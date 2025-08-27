package lombok.delombok;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.DocCommentTable;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.javac.CommentInfo;
import lombok.javac.Javac;
import lombok.javac.JavacTreeMaker;
import lombok.javac.PackageName;
import org.apache.xmlbeans.XmlErrorCodes;
import org.aspectj.apache.bcel.Constants;
import org.aspectj.weaver.model.AsmRelationshipUtils;
import org.hyperic.sigar.NetFlags;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;
import org.springframework.beans.PropertyAccessor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: lombok-1.16.22.jar:lombok/delombok/PrettyPrinter.SCL.lombok */
public class PrettyPrinter extends JCTree.Visitor {
    private static final String LINE_SEP = System.getProperty("line.separator");
    private static final Map<JavacTreeMaker.TreeTag, String> OPERATORS;
    private final Writer out;
    private final JCTree.JCCompilationUnit compilationUnit;
    private List<CommentInfo> comments;
    private final FormatPreferences formatPreferences;
    private final Map<JCTree, String> docComments;
    private final DocCommentTable docTable;
    private boolean needsAlign;
    private boolean needsNewLine;
    private boolean needsSpace;
    private boolean aligned;
    private Name __INIT__;
    private Name __VALUE__;
    private Name currentTypeName;
    private static final long DEFAULT = 8796093022208L;
    private static final int PREFIX = 14;
    private static final Method getExtendsClause;
    private static final Method getEndPosition;
    private static final Method storeEnd;
    private static final Map<Class<?>, Map<String, Field>> reflectionCache;
    private int indent = 0;
    private boolean onNewLine = true;
    private long flagMod = -1;

    static {
        Method storeEndMethodTemp;
        Map<JavacTreeMaker.TreeTag, String> map = new HashMap<>();
        map.put(JavacTreeMaker.TreeTag.treeTag("POS"), "+");
        map.put(JavacTreeMaker.TreeTag.treeTag("NEG"), "-");
        map.put(JavacTreeMaker.TreeTag.treeTag("NOT"), "!");
        map.put(JavacTreeMaker.TreeTag.treeTag("COMPL"), "~");
        map.put(JavacTreeMaker.TreeTag.treeTag("PREINC"), "++");
        map.put(JavacTreeMaker.TreeTag.treeTag("PREDEC"), ScriptUtils.DEFAULT_COMMENT_PREFIX);
        map.put(JavacTreeMaker.TreeTag.treeTag("POSTINC"), "++");
        map.put(JavacTreeMaker.TreeTag.treeTag("POSTDEC"), ScriptUtils.DEFAULT_COMMENT_PREFIX);
        map.put(JavacTreeMaker.TreeTag.treeTag("NULLCHK"), "<*nullchk*>");
        map.put(JavacTreeMaker.TreeTag.treeTag("OR"), "||");
        map.put(JavacTreeMaker.TreeTag.treeTag("AND"), "&&");
        map.put(JavacTreeMaker.TreeTag.treeTag("EQ"), "==");
        map.put(JavacTreeMaker.TreeTag.treeTag("NE"), "!=");
        map.put(JavacTreeMaker.TreeTag.treeTag("LT"), "<");
        map.put(JavacTreeMaker.TreeTag.treeTag("GT"), ">");
        map.put(JavacTreeMaker.TreeTag.treeTag("LE"), "<=");
        map.put(JavacTreeMaker.TreeTag.treeTag("GE"), ">=");
        map.put(JavacTreeMaker.TreeTag.treeTag("BITOR"), "|");
        map.put(JavacTreeMaker.TreeTag.treeTag("BITXOR"), "^");
        map.put(JavacTreeMaker.TreeTag.treeTag("BITAND"), "&");
        map.put(JavacTreeMaker.TreeTag.treeTag("SL"), "<<");
        map.put(JavacTreeMaker.TreeTag.treeTag("SR"), ">>");
        map.put(JavacTreeMaker.TreeTag.treeTag("USR"), ">>>");
        map.put(JavacTreeMaker.TreeTag.treeTag("PLUS"), "+");
        map.put(JavacTreeMaker.TreeTag.treeTag("MINUS"), "-");
        map.put(JavacTreeMaker.TreeTag.treeTag("MUL"), "*");
        map.put(JavacTreeMaker.TreeTag.treeTag("DIV"), "/");
        map.put(JavacTreeMaker.TreeTag.treeTag("MOD"), QuickTargetSourceCreator.PREFIX_THREAD_LOCAL);
        map.put(JavacTreeMaker.TreeTag.treeTag("BITOR_ASG"), "|=");
        map.put(JavacTreeMaker.TreeTag.treeTag("BITXOR_ASG"), "^=");
        map.put(JavacTreeMaker.TreeTag.treeTag("BITAND_ASG"), "&=");
        map.put(JavacTreeMaker.TreeTag.treeTag("SL_ASG"), "<<=");
        map.put(JavacTreeMaker.TreeTag.treeTag("SR_ASG"), ">>=");
        map.put(JavacTreeMaker.TreeTag.treeTag("USR_ASG"), ">>>=");
        map.put(JavacTreeMaker.TreeTag.treeTag("PLUS_ASG"), "+=");
        map.put(JavacTreeMaker.TreeTag.treeTag("MINUS_ASG"), "-=");
        map.put(JavacTreeMaker.TreeTag.treeTag("MUL_ASG"), "*=");
        map.put(JavacTreeMaker.TreeTag.treeTag("DIV_ASG"), "/=");
        map.put(JavacTreeMaker.TreeTag.treeTag("MOD_ASG"), "%=");
        OPERATORS = map;
        getExtendsClause = getMethod((Class<?>) JCTree.JCClassDecl.class, "getExtendsClause", (Class<?>[]) new Class[0]);
        getExtendsClause.setAccessible(true);
        if (Javac.getJavaCompilerVersion() < 8) {
            getEndPosition = getMethod((Class<?>) JCDiagnostic.DiagnosticPosition.class, "getEndPosition", (Class<?>[]) new Class[]{Map.class});
            storeEnd = getMethod((Class<?>) Map.class, "put", (Class<?>[]) new Class[]{Object.class, Object.class});
        } else {
            getEndPosition = getMethod((Class<?>) JCDiagnostic.DiagnosticPosition.class, "getEndPosition", "com.sun.tools.javac.tree.EndPosTable");
            try {
                Class<?> endPosTable = Class.forName("com.sun.tools.javac.tree.EndPosTable");
                try {
                    storeEndMethodTemp = endPosTable.getMethod("storeEnd", JCTree.class, Integer.TYPE);
                } catch (NoSuchMethodException e) {
                    try {
                        Class<?> endPosTable2 = Class.forName("com.sun.tools.javac.parser.JavacParser$AbstractEndPosTable");
                        storeEndMethodTemp = endPosTable2.getDeclaredMethod("storeEnd", JCTree.class, Integer.TYPE);
                    } catch (ClassNotFoundException ex) {
                        throw sneakyThrow(ex);
                    } catch (NoSuchMethodException ex2) {
                        throw sneakyThrow(ex2);
                    }
                }
                storeEnd = storeEndMethodTemp;
            } catch (ClassNotFoundException ex3) {
                throw sneakyThrow(ex3);
            }
        }
        getEndPosition.setAccessible(true);
        storeEnd.setAccessible(true);
        reflectionCache = new HashMap();
    }

    public PrettyPrinter(Writer out, JCTree.JCCompilationUnit cu, List<CommentInfo> comments, FormatPreferences preferences) {
        this.out = out;
        this.comments = comments;
        this.compilationUnit = cu;
        this.formatPreferences = preferences;
        Object dc = Javac.getDocComments(this.compilationUnit);
        if (dc instanceof Map) {
            this.docComments = (Map) dc;
            this.docTable = null;
        } else if (dc instanceof DocCommentTable) {
            this.docComments = null;
            this.docTable = (DocCommentTable) dc;
        } else {
            this.docComments = null;
            this.docTable = null;
        }
    }

    private int endPos(JCTree tree) {
        return Javac.getEndPosition(tree, this.compilationUnit);
    }

    private static int lineEndPos(String s, int start) {
        int pos = s.indexOf(10, start);
        if (pos < 0) {
            pos = s.length();
        }
        return pos;
    }

    /* loaded from: lombok-1.16.22.jar:lombok/delombok/PrettyPrinter$UncheckedIOException.SCL.lombok */
    public static final class UncheckedIOException extends RuntimeException {
        UncheckedIOException(IOException source) {
            super(toMsg(source));
            setStackTrace(source.getStackTrace());
        }

        private static String toMsg(Throwable t) {
            String msg = t.getMessage();
            String n = t.getClass().getSimpleName();
            return (msg == null || msg.isEmpty()) ? n : n + ": " + msg;
        }
    }

    private void align() throws IOException {
        if (this.onNewLine) {
            for (int i = 0; i < this.indent; i++) {
                try {
                    this.out.write(this.formatPreferences.indent());
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
            this.onNewLine = false;
            this.aligned = true;
            this.needsAlign = false;
        }
    }

    private void print(JCTree tree) throws IOException {
        if (tree == null) {
            print("/*missing*/");
            return;
        }
        consumeComments(tree);
        tree.accept(this);
        consumeTrailingComments(endPos(tree));
    }

    private void print(List<? extends JCTree> trees, String infix) throws IOException {
        boolean first = true;
        JCTree prev = null;
        Iterator it = trees.iterator();
        while (it.hasNext()) {
            JCTree tree = (JCTree) it.next();
            if (!suppress(tree)) {
                if (!first && infix != null && !infix.isEmpty()) {
                    if (ScriptUtils.FALLBACK_STATEMENT_SEPARATOR.equals(infix)) {
                        println(prev);
                    } else {
                        print(infix);
                    }
                }
                first = false;
                print(tree);
                prev = tree;
            }
        }
    }

    private boolean suppress(JCTree tree) {
        if (tree instanceof JCTree.JCBlock) {
            JCTree.JCBlock block = (JCTree.JCBlock) tree;
            return -1 == block.pos && block.stats.isEmpty();
        }
        if (tree instanceof JCTree.JCExpressionStatement) {
            JCTree.JCMethodInvocation jCMethodInvocation = ((JCTree.JCExpressionStatement) tree).expr;
            if (jCMethodInvocation instanceof JCTree.JCMethodInvocation) {
                JCTree.JCMethodInvocation inv = jCMethodInvocation;
                if (inv.typeargs.isEmpty() && inv.args.isEmpty() && (inv.meth instanceof JCTree.JCIdent)) {
                    return inv.meth.name.toString().equals("super");
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private void print(CharSequence s) throws IOException {
        boolean align = this.needsAlign;
        if (this.needsNewLine && !this.onNewLine) {
            println();
        }
        if (align && !this.aligned) {
            align();
        }
        try {
            if (this.needsSpace && !this.onNewLine && !this.aligned) {
                this.out.write(32);
            }
            this.out.write(s.toString());
            this.needsSpace = false;
            this.onNewLine = false;
            this.aligned = false;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void println() throws IOException {
        try {
            this.out.write(LINE_SEP);
            this.onNewLine = true;
            this.aligned = false;
            this.needsNewLine = false;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void println(JCTree completed) throws IOException {
        if (completed != null) {
            int endPos = endPos(completed);
            consumeTrailingComments(endPos);
        }
        try {
            this.out.write(LINE_SEP);
            this.onNewLine = true;
            this.aligned = false;
            this.needsNewLine = false;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void println(CharSequence s) throws IOException {
        print(s);
        println();
    }

    private void println(CharSequence s, JCTree completed) throws IOException {
        print(s);
        println(completed);
    }

    private void aPrint(CharSequence s) throws IOException {
        align();
        print(s);
    }

    private void aPrintln(CharSequence s) throws IOException {
        align();
        print(s);
        println();
    }

    private void aPrintln(CharSequence s, JCTree completed) throws IOException {
        align();
        print(s);
        println(completed);
    }

    private void consumeComments(int until) throws IOException {
        Object obj = this.comments.head;
        while (true) {
            CommentInfo head = (CommentInfo) obj;
            if (this.comments.nonEmpty() && head.pos < until) {
                printComment(head);
                this.comments = this.comments.tail;
                obj = this.comments.head;
            } else {
                return;
            }
        }
    }

    private void consumeComments(JCTree tree) throws IOException {
        consumeComments(tree.pos);
    }

    private void consumeTrailingComments(int from) throws IOException {
        boolean prevNewLine = this.onNewLine;
        CommentInfo head = (CommentInfo) this.comments.head;
        boolean stop = false;
        while (this.comments.nonEmpty() && head.prevEndPos == from && !stop && head.start != CommentInfo.StartConnection.ON_NEXT_LINE && head.start != CommentInfo.StartConnection.START_OF_LINE) {
            from = head.endPos;
            printComment(head);
            stop = head.end == CommentInfo.EndConnection.ON_NEXT_LINE;
            this.comments = this.comments.tail;
            head = (CommentInfo) this.comments.head;
        }
        if (!this.onNewLine && prevNewLine) {
            println();
        }
    }

    private String getJavadocFor(JCTree node) {
        if (this.docComments != null) {
            return this.docComments.get(node);
        }
        if (this.docTable != null) {
            return this.docTable.getCommentText(node);
        }
        return null;
    }

    private int dims(JCTree.JCExpression vartype) {
        if (vartype instanceof JCTree.JCArrayTypeTree) {
            return 1 + dims(((JCTree.JCArrayTypeTree) vartype).elemtype);
        }
        return 0;
    }

    private void printComment(CommentInfo comment) throws IOException {
        switch (comment.start) {
            case DIRECT_AFTER_PREVIOUS:
                this.needsSpace = false;
                break;
            case AFTER_PREVIOUS:
                this.needsSpace = true;
                break;
            case START_OF_LINE:
                this.needsNewLine = true;
                this.needsAlign = false;
                break;
            case ON_NEXT_LINE:
                if (!this.onNewLine) {
                    this.needsNewLine = true;
                    this.needsAlign = true;
                    break;
                } else if (!this.aligned) {
                    this.needsAlign = true;
                    break;
                }
                break;
        }
        if (this.onNewLine && !this.aligned && comment.start != CommentInfo.StartConnection.START_OF_LINE) {
            this.needsAlign = true;
        }
        print(comment.content);
        switch (comment.end) {
            case ON_NEXT_LINE:
                if (!this.aligned) {
                    this.needsNewLine = true;
                    this.needsAlign = true;
                    break;
                }
                break;
            case AFTER_COMMENT:
                this.needsSpace = true;
                break;
        }
    }

    private void printDocComment(JCTree tree) throws IOException {
        String dc = getJavadocFor(tree);
        if (dc == null) {
            return;
        }
        aPrintln("/**");
        int pos = 0;
        int endpos = lineEndPos(dc, 0);
        boolean atStart = true;
        while (pos < dc.length()) {
            String line = dc.substring(pos, endpos);
            if (line.trim().isEmpty() && atStart) {
                atStart = false;
            } else {
                atStart = false;
                aPrint(" *");
                if (pos < dc.length() && dc.charAt(pos) > ' ') {
                    print(SymbolConstants.SPACE_SYMBOL);
                }
                println(dc.substring(pos, endpos));
                pos = endpos + 1;
                endpos = lineEndPos(dc, pos);
            }
        }
        aPrintln(" */");
    }

    private Name name_init(Name someName) {
        if (this.__INIT__ == null) {
            this.__INIT__ = someName.table.fromChars("<init>".toCharArray(), 0, 6);
        }
        return this.__INIT__;
    }

    private Name name_value(Name someName) {
        if (this.__VALUE__ == null) {
            this.__VALUE__ = someName.table.fromChars("value".toCharArray(), 0, 5);
        }
        return this.__VALUE__;
    }

    public void visitTopLevel(JCTree.JCCompilationUnit tree) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        printDocComment(tree);
        JCTree n = PackageName.getPackageNode(tree);
        if (n != null) {
            consumeComments((JCTree) tree);
            aPrint("package ");
            print(n);
            println(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR, n);
        }
        boolean first = true;
        Iterator it = tree.defs.iterator();
        while (it.hasNext()) {
            JCTree child = (JCTree) it.next();
            if (child instanceof JCTree.JCImport) {
                if (first) {
                    println();
                }
                first = false;
                print(child);
            }
        }
        Iterator it2 = tree.defs.iterator();
        while (it2.hasNext()) {
            JCTree child2 = (JCTree) it2.next();
            if (!(child2 instanceof JCTree.JCImport)) {
                print(child2);
            }
        }
        consumeComments(Integer.MAX_VALUE);
    }

    public void visitImport(JCTree.JCImport tree) throws IOException {
        aPrint("import ");
        if (tree.staticImport) {
            print("static ");
        }
        print(tree.qualid);
        println(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR, tree);
    }

    public void visitClassDef(JCTree.JCClassDecl tree) throws IOException {
        println();
        printDocComment(tree);
        align();
        print((JCTree) tree.mods);
        boolean isInterface = (tree.mods.flags & 512) != 0;
        boolean isAnnotationInterface = isInterface && (tree.mods.flags & 8192) != 0;
        boolean isEnum = (tree.mods.flags & 16384) != 0;
        if (isAnnotationInterface) {
            print("@interface ");
        } else if (isInterface) {
            print("interface ");
        } else if (isEnum) {
            print("enum ");
        } else {
            print("class ");
        }
        print((CharSequence) tree.name);
        Name prevTypeName = this.currentTypeName;
        this.currentTypeName = tree.name;
        if (tree.typarams.nonEmpty()) {
            print("<");
            print(tree.typarams, ", ");
            print(">");
        }
        JCTree extendsClause = getExtendsClause(tree);
        if (extendsClause != null) {
            print(" extends ");
            print(extendsClause);
        }
        if (tree.implementing.nonEmpty()) {
            print(isInterface ? " extends " : " implements ");
            print(tree.implementing, ", ");
        }
        println(" {");
        this.indent++;
        printClassMembers(tree.defs, isEnum, isInterface);
        consumeComments(endPos(tree));
        this.indent--;
        aPrintln("}", tree);
        this.currentTypeName = prevTypeName;
    }

    private void printClassMembers(List<JCTree> members, boolean isEnum, boolean isInterface) throws IOException {
        Class<?> prefType = null;
        int typeOfPrevEnumMember = isEnum ? 3 : 0;
        boolean prevWasEnumMember = isEnum;
        Iterator it = members.iterator();
        while (it.hasNext()) {
            JCTree.JCMethodDecl jCMethodDecl = (JCTree) it.next();
            if (typeOfPrevEnumMember != 3 || !(jCMethodDecl instanceof JCTree.JCMethodDecl) || (jCMethodDecl.mods.flags & 68719476736L) == 0) {
                boolean isEnumVar = isEnum && (jCMethodDecl instanceof JCTree.JCVariableDecl) && (((JCTree.JCVariableDecl) jCMethodDecl).mods.flags & 16384) != 0;
                if (!isEnumVar && prevWasEnumMember) {
                    prevWasEnumMember = false;
                    if (typeOfPrevEnumMember == 3) {
                        align();
                    }
                    println(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
                }
                if (isEnumVar) {
                    if (prefType != null && prefType != JCTree.JCVariableDecl.class) {
                        println();
                    }
                    switch (typeOfPrevEnumMember) {
                        case 1:
                            print(", ");
                            break;
                        case 2:
                            println(",");
                            align();
                            break;
                    }
                    print((JCTree) jCMethodDecl);
                    JCTree.JCNewClass jCNewClass = ((JCTree.JCVariableDecl) jCMethodDecl).init;
                    typeOfPrevEnumMember = (!(jCNewClass instanceof JCTree.JCNewClass) || jCNewClass.def == null) ? 1 : 2;
                } else if (jCMethodDecl instanceof JCTree.JCVariableDecl) {
                    if (prefType != null && prefType != JCTree.JCVariableDecl.class) {
                        println();
                    }
                    if (isInterface) {
                        this.flagMod = -26L;
                    }
                    print((JCTree) jCMethodDecl);
                } else if (jCMethodDecl instanceof JCTree.JCMethodDecl) {
                    if ((jCMethodDecl.mods.flags & 68719476736L) == 0) {
                        if (prefType != null) {
                            println();
                        }
                        if (isInterface) {
                            this.flagMod = -1026L;
                        }
                        print((JCTree) jCMethodDecl);
                    }
                } else if (jCMethodDecl instanceof JCTree.JCClassDecl) {
                    if (prefType != null) {
                        println();
                    }
                    if (isInterface) {
                        this.flagMod = -10L;
                    }
                    print((JCTree) jCMethodDecl);
                } else {
                    if (prefType != null) {
                        println();
                    }
                    print((JCTree) jCMethodDecl);
                }
                prefType = jCMethodDecl.getClass();
            }
        }
        if (prevWasEnumMember) {
            if (typeOfPrevEnumMember == 3) {
                align();
            }
            println(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        }
    }

    public void visitTypeParameter(JCTree.JCTypeParameter tree) throws IOException {
        List<? extends JCTree> list = (List) readObject(tree, "annotations", List.nil());
        if (!list.isEmpty()) {
            print(list, SymbolConstants.SPACE_SYMBOL);
            print(SymbolConstants.SPACE_SYMBOL);
        }
        print((CharSequence) tree.name);
        if (tree.bounds.nonEmpty()) {
            print(" extends ");
            print(tree.bounds, " & ");
        }
        consumeComments((JCTree) tree);
    }

    public void visitVarDef(JCTree.JCVariableDecl tree) throws IOException {
        printDocComment(tree);
        align();
        if ((tree.mods.flags & 16384) != 0) {
            printEnumMember(tree);
            return;
        }
        printAnnotations(tree.mods.annotations, true);
        printModifierKeywords(tree.mods);
        printVarDef0(tree);
        println(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR, tree);
    }

    private void printVarDefInline(JCTree.JCVariableDecl tree) throws IOException {
        printAnnotations(tree.mods.annotations, false);
        printModifierKeywords(tree.mods);
        printVarDef0(tree);
    }

    private void printVarDef0(JCTree.JCVariableDecl tree) throws IOException {
        boolean varargs = (tree.mods.flags & 17179869184L) != 0;
        if (varargs && (tree.vartype instanceof JCTree.JCArrayTypeTree)) {
            print((JCTree) tree.vartype.elemtype);
            print("...");
        } else {
            print((JCTree) tree.vartype);
        }
        print(SymbolConstants.SPACE_SYMBOL);
        print((CharSequence) tree.name);
        if (tree.init != null) {
            print(" = ");
            print((JCTree) tree.init);
        }
    }

    private void printEnumMember(JCTree.JCVariableDecl tree) throws IOException {
        printAnnotations(tree.mods.annotations, true);
        print((CharSequence) tree.name);
        if (tree.init instanceof JCTree.JCNewClass) {
            JCTree.JCNewClass constructor = tree.init;
            if (constructor.args != null && constructor.args.nonEmpty()) {
                print("(");
                print(constructor.args, ", ");
                print(")");
            }
            if (constructor.def != null && constructor.def.defs != null) {
                println(" {");
                this.indent++;
                printClassMembers(constructor.def.defs, false, false);
                consumeComments(endPos(tree));
                this.indent--;
                aPrint("}");
            }
        }
    }

    public void visitTypeApply(JCTree.JCTypeApply tree) throws IOException {
        print((JCTree) tree.clazz);
        print("<");
        print(tree.arguments, ", ");
        print(">");
    }

    /* renamed from: lombok.delombok.PrettyPrinter$1, reason: invalid class name */
    /* loaded from: lombok-1.16.22.jar:lombok/delombok/PrettyPrinter$1.SCL.lombok */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$sun$source$tree$Tree$Kind = new int[Tree.Kind.values().length];

        static {
            try {
                $SwitchMap$com$sun$source$tree$Tree$Kind[Tree.Kind.UNBOUNDED_WILDCARD.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$sun$source$tree$Tree$Kind[Tree.Kind.EXTENDS_WILDCARD.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$sun$source$tree$Tree$Kind[Tree.Kind.SUPER_WILDCARD.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SwitchMap$lombok$javac$CommentInfo$EndConnection = new int[CommentInfo.EndConnection.values().length];
            try {
                $SwitchMap$lombok$javac$CommentInfo$EndConnection[CommentInfo.EndConnection.ON_NEXT_LINE.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$lombok$javac$CommentInfo$EndConnection[CommentInfo.EndConnection.AFTER_COMMENT.ordinal()] = 2;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$lombok$javac$CommentInfo$EndConnection[CommentInfo.EndConnection.DIRECT_AFTER_COMMENT.ordinal()] = 3;
            } catch (NoSuchFieldError e6) {
            }
            $SwitchMap$lombok$javac$CommentInfo$StartConnection = new int[CommentInfo.StartConnection.values().length];
            try {
                $SwitchMap$lombok$javac$CommentInfo$StartConnection[CommentInfo.StartConnection.DIRECT_AFTER_PREVIOUS.ordinal()] = 1;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$lombok$javac$CommentInfo$StartConnection[CommentInfo.StartConnection.AFTER_PREVIOUS.ordinal()] = 2;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$lombok$javac$CommentInfo$StartConnection[CommentInfo.StartConnection.START_OF_LINE.ordinal()] = 3;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$lombok$javac$CommentInfo$StartConnection[CommentInfo.StartConnection.ON_NEXT_LINE.ordinal()] = 4;
            } catch (NoSuchFieldError e10) {
            }
        }
    }

    public void visitWildcard(JCTree.JCWildcard tree) throws IOException {
        switch (AnonymousClass1.$SwitchMap$com$sun$source$tree$Tree$Kind[tree.getKind().ordinal()]) {
            case 1:
            default:
                print("?");
                break;
            case 2:
                print("? extends ");
                print(tree.inner);
                break;
            case 3:
                print("? super ");
                print(tree.inner);
                break;
        }
    }

    public void visitLiteral(JCTree.JCLiteral tree) throws IOException {
        JavacTreeMaker.TypeTag typeTag = JavacTreeMaker.TypeTag.typeTag((JCTree) tree);
        if (!Javac.CTC_INT.equals(typeTag)) {
            if (!Javac.CTC_LONG.equals(typeTag)) {
                if (!Javac.CTC_FLOAT.equals(typeTag)) {
                    if (!Javac.CTC_DOUBLE.equals(typeTag)) {
                        if (Javac.CTC_CHAR.equals(typeTag)) {
                            print("'" + quoteChar((char) ((Number) tree.value).intValue()) + "'");
                            return;
                        }
                        if (!Javac.CTC_BOOLEAN.equals(typeTag)) {
                            if (!Javac.CTC_BOT.equals(typeTag)) {
                                print(SymbolConstants.QUOTES_SYMBOL + quoteChars(tree.value.toString()) + SymbolConstants.QUOTES_SYMBOL);
                                return;
                            } else {
                                print("null");
                                return;
                            }
                        }
                        print(((Number) tree.value).intValue() == 1 ? "true" : "false");
                        return;
                    }
                    print("" + tree.value);
                    return;
                }
                print(tree.value + "F");
                return;
            }
            print(tree.value + StandardRoles.L);
            return;
        }
        print("" + tree.value);
    }

    public void visitMethodDef(JCTree.JCMethodDecl tree) throws IOException {
        boolean isConstructor = tree.name == name_init(tree.name);
        if (!isConstructor || (tree.mods.flags & 68719476736L) == 0) {
            printDocComment(tree);
            align();
            print((JCTree) tree.mods);
            if (tree.typarams != null && tree.typarams.nonEmpty()) {
                print("<");
                print(tree.typarams, ", ");
                print("> ");
            }
            if (isConstructor) {
                print(this.currentTypeName == null ? "<init>" : this.currentTypeName);
            } else {
                print((JCTree) tree.restype);
                print(SymbolConstants.SPACE_SYMBOL);
                print((CharSequence) tree.name);
            }
            print("(");
            boolean first = true;
            Iterator it = tree.params.iterator();
            while (it.hasNext()) {
                JCTree.JCVariableDecl param = (JCTree.JCVariableDecl) it.next();
                if (!first) {
                    print(", ");
                }
                first = false;
                printVarDefInline(param);
            }
            print(")");
            if (tree.thrown.nonEmpty()) {
                print(" throws ");
                print(tree.thrown, ", ");
            }
            if (tree.defaultValue != null) {
                print(" default ");
                print((JCTree) tree.defaultValue);
            }
            if (tree.body != null) {
                print(SymbolConstants.SPACE_SYMBOL);
                print((JCTree) tree.body);
            } else {
                println(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR, tree);
            }
        }
    }

    public void visitSkip(JCTree.JCSkip that) throws IOException {
        if (this.onNewLine && !this.aligned) {
            align();
        }
        println(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
    }

    public void visitAnnotation(JCTree.JCAnnotation tree) throws IOException {
        print("@");
        print(tree.annotationType);
        if (tree.args.isEmpty()) {
            return;
        }
        print("(");
        boolean done = false;
        if (tree.args.length() == 1 && (tree.args.get(0) instanceof JCTree.JCAssign)) {
            JCTree.JCAssign arg1 = (JCTree.JCAssign) tree.args.get(0);
            JCTree.JCIdent arg1Name = arg1.lhs instanceof JCTree.JCIdent ? (JCTree.JCIdent) arg1.lhs : null;
            if (arg1Name != null && arg1Name.name == name_value(arg1Name.name)) {
                print((JCTree) arg1.rhs);
                done = true;
            }
        }
        if (!done) {
            print(tree.args, ", ");
        }
        print(")");
    }

    public void visitTypeArray(JCTree.JCArrayTypeTree tree) throws IOException {
        JCTree jCTree = tree.elemtype;
        while (true) {
            JCTree elem = jCTree;
            if (!(elem instanceof JCTree.JCWildcard)) {
                print(elem);
                print("[]");
                return;
            }
            jCTree = ((JCTree.JCWildcard) elem).inner;
        }
    }

    public void visitNewArray(JCTree.JCNewArray tree) throws IOException {
        JCTree.JCExpression jCExpression = tree.elemtype;
        int dims = 0;
        if (jCExpression != null) {
            print("new ");
            while (jCExpression instanceof JCTree.JCArrayTypeTree) {
                dims++;
                jCExpression = ((JCTree.JCArrayTypeTree) jCExpression).elemtype;
            }
            print((JCTree) jCExpression);
            Iterator it = tree.dims.iterator();
            while (it.hasNext()) {
                JCTree.JCExpression expr = (JCTree.JCExpression) it.next();
                print(PropertyAccessor.PROPERTY_KEY_PREFIX);
                print((JCTree) expr);
                print("]");
            }
        }
        for (int i = 0; i < dims; i++) {
            print("[]");
        }
        if (tree.elems != null) {
            if (jCExpression != null) {
                print("[] ");
            }
            print("{");
            print(tree.elems, ", ");
            print("}");
        }
    }

    public void visitNewClass(JCTree.JCNewClass tree) throws IOException {
        if (tree.encl != null) {
            print((JCTree) tree.encl);
            print(".");
        }
        boolean moveFirstParameter = tree.args.nonEmpty() && (tree.args.head instanceof JCTree.JCUnary) && ((JCTree.JCExpression) tree.args.head).toString().startsWith("<*nullchk*>");
        if (moveFirstParameter) {
            print((JCTree) ((JCTree.JCUnary) tree.args.head).arg);
            print(".");
        }
        print("new ");
        if (!tree.typeargs.isEmpty()) {
            print("<");
            print(tree.typeargs, ", ");
            print(">");
        }
        print((JCTree) tree.clazz);
        print("(");
        if (moveFirstParameter) {
            print(tree.args.tail, ", ");
        } else {
            print(tree.args, ", ");
        }
        print(")");
        if (tree.def != null) {
            Name previousTypeName = this.currentTypeName;
            this.currentTypeName = null;
            println(" {");
            this.indent++;
            print(tree.def.defs, "");
            this.indent--;
            aPrint("}");
            this.currentTypeName = previousTypeName;
        }
    }

    public void visitIndexed(JCTree.JCArrayAccess tree) throws IOException {
        print((JCTree) tree.indexed);
        print(PropertyAccessor.PROPERTY_KEY_PREFIX);
        print((JCTree) tree.index);
        print("]");
    }

    public void visitTypeIdent(JCTree.JCPrimitiveTypeTree tree) throws IOException {
        JavacTreeMaker.TypeTag typeTag = JavacTreeMaker.TypeTag.typeTag((JCTree) tree);
        if (!Javac.CTC_BYTE.equals(typeTag)) {
            if (!Javac.CTC_CHAR.equals(typeTag)) {
                if (!Javac.CTC_SHORT.equals(typeTag)) {
                    if (!Javac.CTC_INT.equals(typeTag)) {
                        if (!Javac.CTC_LONG.equals(typeTag)) {
                            if (!Javac.CTC_FLOAT.equals(typeTag)) {
                                if (!Javac.CTC_DOUBLE.equals(typeTag)) {
                                    if (!Javac.CTC_BOOLEAN.equals(typeTag)) {
                                        if (!Javac.CTC_VOID.equals(typeTag)) {
                                            print(AsmRelationshipUtils.DECLARE_ERROR);
                                            return;
                                        } else {
                                            print("void");
                                            return;
                                        }
                                    }
                                    print("boolean");
                                    return;
                                }
                                print(XmlErrorCodes.DOUBLE);
                                return;
                            }
                            print(XmlErrorCodes.FLOAT);
                            return;
                        }
                        print(XmlErrorCodes.LONG);
                        return;
                    }
                    print(XmlErrorCodes.INT);
                    return;
                }
                print("short");
                return;
            }
            print("char");
            return;
        }
        print("byte");
    }

    public void visitLabelled(JCTree.JCLabeledStatement tree) throws IOException {
        aPrint(tree.label);
        print(":");
        if ((tree.body instanceof JCTree.JCSkip) || suppress(tree)) {
            println(" ;", tree);
        } else if (tree.body instanceof JCTree.JCBlock) {
            print(SymbolConstants.SPACE_SYMBOL);
            print((JCTree) tree.body);
        } else {
            println((JCTree) tree);
            print((JCTree) tree.body);
        }
    }

    public void visitModifiers(JCTree.JCModifiers tree) throws IOException {
        printAnnotations(tree.annotations, true);
        printModifierKeywords(tree);
    }

    private void printAnnotations(List<JCTree.JCAnnotation> annotations, boolean newlines) throws IOException {
        Iterator it = annotations.iterator();
        while (it.hasNext()) {
            JCTree.JCAnnotation ann = (JCTree.JCAnnotation) it.next();
            print((JCTree) ann);
            if (newlines) {
                println();
                align();
            } else {
                print(SymbolConstants.SPACE_SYMBOL);
            }
        }
    }

    private void printModifierKeywords(JCTree.JCModifiers tree) throws IOException {
        long v = this.flagMod & tree.flags;
        this.flagMod = -1L;
        if ((v & Constants.NEGATABLE) != 0) {
            print("/* synthetic */ ");
        }
        if ((v & 1) != 0) {
            print("public ");
        }
        if ((v & 2) != 0) {
            print("private ");
        }
        if ((v & 4) != 0) {
            print("protected ");
        }
        if ((v & 8) != 0) {
            print("static ");
        }
        if ((v & 16) != 0) {
            print("final ");
        }
        if ((v & 32) != 0) {
            print("synchronized ");
        }
        if ((v & 64) != 0) {
            print("volatile ");
        }
        if ((v & 128) != 0) {
            print("transient ");
        }
        if ((v & 256) != 0) {
            print("native ");
        }
        if ((v & 1024) != 0) {
            print("abstract ");
        }
        if ((v & 2048) != 0) {
            print("strictfp ");
        }
        if ((v & DEFAULT) == 0 || (v & 512) != 0) {
            return;
        }
        print("default ");
    }

    public void visitSelect(JCTree.JCFieldAccess tree) throws IOException {
        print((JCTree) tree.selected);
        print(".");
        print((CharSequence) tree.name);
    }

    public void visitIdent(JCTree.JCIdent tree) throws IOException {
        print((CharSequence) tree.name);
    }

    public void visitApply(JCTree.JCMethodInvocation tree) throws IOException {
        if (tree.typeargs.nonEmpty()) {
            if (tree.meth instanceof JCTree.JCFieldAccess) {
                JCTree.JCFieldAccess fa = tree.meth;
                print((JCTree) fa.selected);
                print(".<");
                print(tree.typeargs, ", ");
                print(">");
                print((CharSequence) fa.name);
            } else {
                print("<");
                print(tree.typeargs, ", ");
                print(">");
                print((JCTree) tree.meth);
            }
        } else {
            print((JCTree) tree.meth);
        }
        print("(");
        print(tree.args, ", ");
        print(")");
    }

    public void visitAssert(JCTree.JCAssert tree) throws IOException {
        aPrint("assert ");
        print((JCTree) tree.cond);
        if (tree.detail != null) {
            print(" : ");
            print((JCTree) tree.detail);
        }
        println(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR, tree);
    }

    public void visitAssign(JCTree.JCAssign tree) throws IOException {
        print((JCTree) tree.lhs);
        print(" = ");
        print((JCTree) tree.rhs);
    }

    public void visitAssignop(JCTree.JCAssignOp tree) throws IOException {
        print((JCTree) tree.lhs);
        String opname = operator(JavacTreeMaker.TreeTag.treeTag((JCTree) tree));
        print(SymbolConstants.SPACE_SYMBOL + opname + SymbolConstants.SPACE_SYMBOL);
        print((JCTree) tree.rhs);
    }

    public void visitUnary(JCTree.JCUnary tree) throws IOException {
        String op = operator(JavacTreeMaker.TreeTag.treeTag((JCTree) tree));
        if (JavacTreeMaker.TreeTag.treeTag((JCTree) tree).getOperatorPrecedenceLevel() == 14) {
            print(op);
            print((JCTree) tree.arg);
        } else {
            print((JCTree) tree.arg);
            print(op);
        }
    }

    public void visitBinary(JCTree.JCBinary tree) throws IOException {
        String op = operator(JavacTreeMaker.TreeTag.treeTag((JCTree) tree));
        print((JCTree) tree.lhs);
        print(SymbolConstants.SPACE_SYMBOL);
        print(op);
        print(SymbolConstants.SPACE_SYMBOL);
        print((JCTree) tree.rhs);
    }

    public void visitTypeTest(JCTree.JCInstanceOf tree) throws IOException {
        print((JCTree) tree.expr);
        print(" instanceof ");
        print(tree.clazz);
    }

    public void visitTypeCast(JCTree.JCTypeCast tree) throws IOException {
        print("(");
        print(tree.clazz);
        print(") ");
        print((JCTree) tree.expr);
    }

    public void visitBlock(JCTree.JCBlock tree) throws IOException {
        if (tree.pos == -1 && tree.stats.isEmpty()) {
            return;
        }
        if (this.onNewLine) {
            align();
        }
        if ((tree.flags & 8) != 0) {
            print("static ");
        }
        println("{");
        this.indent++;
        print(tree.stats, "");
        consumeComments(endPos(tree));
        this.indent--;
        aPrintln("}", tree);
    }

    public void visitBreak(JCTree.JCBreak tree) throws IOException {
        aPrint("break");
        if (tree.label != null) {
            print(SymbolConstants.SPACE_SYMBOL);
            print((CharSequence) tree.label);
        }
        println(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR, tree);
    }

    public void visitContinue(JCTree.JCContinue tree) throws IOException {
        aPrint("continue");
        if (tree.label != null) {
            print(SymbolConstants.SPACE_SYMBOL);
            print((CharSequence) tree.label);
        }
        println(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR, tree);
    }

    public void visitConditional(JCTree.JCConditional tree) throws IOException {
        print((JCTree) tree.cond);
        print(" ? ");
        print((JCTree) tree.truepart);
        print(" : ");
        print((JCTree) tree.falsepart);
    }

    public void visitParens(JCTree.JCParens tree) throws IOException {
        print("(");
        print((JCTree) tree.expr);
        print(")");
    }

    public void visitReturn(JCTree.JCReturn tree) throws IOException {
        aPrint("return");
        if (tree.expr != null) {
            print(SymbolConstants.SPACE_SYMBOL);
            print((JCTree) tree.expr);
        }
        println(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR, tree);
    }

    public void visitThrow(JCTree.JCThrow tree) throws IOException {
        aPrint("throw ");
        print((JCTree) tree.expr);
        println(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR, tree);
    }

    public void visitWhileLoop(JCTree.JCWhileLoop tree) throws IOException {
        aPrint("while ");
        if (tree.cond instanceof JCTree.JCParens) {
            print((JCTree) tree.cond);
        } else {
            print("(");
            print((JCTree) tree.cond);
            print(")");
        }
        print(SymbolConstants.SPACE_SYMBOL);
        print((JCTree) tree.body);
    }

    public void visitForLoop(JCTree.JCForLoop tree) throws IOException {
        aPrint("for (");
        if (tree.init.nonEmpty()) {
            if (tree.init.head instanceof JCTree.JCVariableDecl) {
                boolean first = true;
                int dims = 0;
                Iterator it = tree.init.iterator();
                while (it.hasNext()) {
                    JCTree.JCStatement i = (JCTree.JCStatement) it.next();
                    JCTree.JCVariableDecl vd = (JCTree.JCVariableDecl) i;
                    if (first) {
                        printVarDefInline(vd);
                        dims = dims(vd.vartype);
                    } else {
                        print(", ");
                        print((CharSequence) vd.name);
                        int dimDiff = dims(vd.vartype) - dims;
                        for (int j = 0; j < dimDiff; j++) {
                            print("[]");
                        }
                        if (vd.init != null) {
                            print(" = ");
                            print((JCTree) vd.init);
                        }
                    }
                    first = false;
                }
            } else {
                boolean first2 = true;
                Iterator it2 = tree.init.iterator();
                while (it2.hasNext()) {
                    JCTree.JCExpressionStatement jCExpressionStatement = (JCTree.JCStatement) it2.next();
                    if (!first2) {
                        print(", ");
                    }
                    first2 = false;
                    print((JCTree) jCExpressionStatement.expr);
                }
            }
        }
        print("; ");
        if (tree.cond != null) {
            print((JCTree) tree.cond);
        }
        print("; ");
        boolean first3 = true;
        Iterator it3 = tree.step.iterator();
        while (it3.hasNext()) {
            JCTree.JCExpressionStatement exprStatement = (JCTree.JCExpressionStatement) it3.next();
            if (!first3) {
                print(", ");
            }
            first3 = false;
            print((JCTree) exprStatement.expr);
        }
        print(") ");
        print((JCTree) tree.body);
    }

    public void visitForeachLoop(JCTree.JCEnhancedForLoop tree) throws IOException {
        aPrint("for (");
        printVarDefInline(tree.var);
        print(" : ");
        print((JCTree) tree.expr);
        print(") ");
        print((JCTree) tree.body);
    }

    public void visitIf(JCTree.JCIf tree) throws IOException {
        aPrint("if ");
        if (tree.cond instanceof JCTree.JCParens) {
            print((JCTree) tree.cond);
        } else {
            print("(");
            print((JCTree) tree.cond);
            print(")");
        }
        print(SymbolConstants.SPACE_SYMBOL);
        if (tree.thenpart instanceof JCTree.JCBlock) {
            println("{");
            this.indent++;
            print(tree.thenpart.stats, "");
            this.indent--;
            if (tree.elsepart == null) {
                aPrintln("}", tree);
            } else {
                aPrint("}");
            }
        } else {
            print((JCTree) tree.thenpart);
        }
        if (tree.elsepart != null) {
            aPrint(" else ");
            print((JCTree) tree.elsepart);
        }
    }

    public void visitExec(JCTree.JCExpressionStatement tree) throws IOException {
        align();
        print((JCTree) tree.expr);
        println(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR, tree);
    }

    public void visitDoLoop(JCTree.JCDoWhileLoop tree) throws IOException {
        aPrint("do ");
        if (tree.body instanceof JCTree.JCBlock) {
            println("{");
            this.indent++;
            print(tree.body.stats, "");
            this.indent--;
            aPrint("}");
        } else {
            print((JCTree) tree.body);
        }
        print(" while ");
        if (tree.cond instanceof JCTree.JCParens) {
            print((JCTree) tree.cond);
        } else {
            print("(");
            print((JCTree) tree.cond);
            print(")");
        }
        println(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR, tree);
    }

    public void visitSynchronized(JCTree.JCSynchronized tree) throws IOException {
        aPrint("synchronized ");
        if (tree.lock instanceof JCTree.JCParens) {
            print((JCTree) tree.lock);
        } else {
            print("(");
            print((JCTree) tree.lock);
            print(")");
        }
        print(SymbolConstants.SPACE_SYMBOL);
        print((JCTree) tree.body);
    }

    public void visitCase(JCTree.JCCase tree) throws IOException {
        if (tree.pat == null) {
            aPrint("default");
        } else {
            aPrint("case ");
            print((JCTree) tree.pat);
        }
        println(": ");
        this.indent++;
        print(tree.stats, "");
        this.indent--;
    }

    public void visitCatch(JCTree.JCCatch tree) throws IOException {
        print(" catch (");
        print((JCTree) tree.param);
        print(") ");
        print((JCTree) tree.body);
    }

    public void visitSwitch(JCTree.JCSwitch tree) throws IOException {
        aPrint("switch ");
        if (tree.selector instanceof JCTree.JCParens) {
            print((JCTree) tree.selector);
        } else {
            print("(");
            print((JCTree) tree.selector);
            print(")");
        }
        println(" {");
        print(tree.cases, ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        aPrintln("}", tree);
    }

    public void visitTry(JCTree.JCTry tree) throws IOException {
        aPrint("try ");
        List<?> resources = (List) readObject(tree, "resources", List.nil());
        int len = resources.length();
        switch (len) {
            case 0:
                break;
            case 1:
                print("(");
                JCTree.JCVariableDecl decl = (JCTree.JCVariableDecl) resources.get(0);
                this.flagMod = -17L;
                printVarDefInline(decl);
                print(") ");
                break;
            default:
                println("(");
                this.indent++;
                int c = 0;
                Iterator it = resources.iterator();
                while (it.hasNext()) {
                    Object i = it.next();
                    align();
                    this.flagMod = -17L;
                    printVarDefInline((JCTree.JCVariableDecl) i);
                    c++;
                    if (c == len) {
                        print(") ");
                    } else {
                        println(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR, (JCTree) i);
                    }
                }
                this.indent--;
                break;
        }
        println("{");
        this.indent++;
        Iterator it2 = tree.body.stats.iterator();
        while (it2.hasNext()) {
            JCTree.JCStatement stat = (JCTree.JCStatement) it2.next();
            print((JCTree) stat);
        }
        this.indent--;
        aPrint("}");
        Iterator it3 = tree.catchers.iterator();
        while (it3.hasNext()) {
            JCTree.JCCatch catchBlock = (JCTree.JCCatch) it3.next();
            printCatch(catchBlock);
        }
        if (tree.finalizer != null) {
            println(" finally {");
            this.indent++;
            Iterator it4 = tree.finalizer.stats.iterator();
            while (it4.hasNext()) {
                JCTree.JCStatement stat2 = (JCTree.JCStatement) it4.next();
                print((JCTree) stat2);
            }
            this.indent--;
            aPrint("}");
        }
        println((JCTree) tree);
    }

    private void printCatch(JCTree.JCCatch catchBlock) throws IOException {
        print(" catch (");
        printVarDefInline(catchBlock.param);
        println(") {");
        this.indent++;
        Iterator it = catchBlock.body.stats.iterator();
        while (it.hasNext()) {
            JCTree.JCStatement stat = (JCTree.JCStatement) it.next();
            print((JCTree) stat);
        }
        this.indent--;
        aPrint("}");
    }

    public void visitErroneous(JCTree.JCErroneous tree) throws IOException {
        print("(ERROR)");
    }

    private static String operator(JavacTreeMaker.TreeTag tag) {
        String op = OPERATORS.get(tag);
        return op == null ? "(?op?)" : op;
    }

    private static String quoteChars(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append(quoteChar(s.charAt(i)));
        }
        return sb.toString();
    }

    private static String quoteChar(char ch2) {
        switch (ch2) {
            case '\b':
                return "\\b";
            case '\t':
                return "\\t";
            case '\n':
                return "\\n";
            case '\f':
                return "\\f";
            case '\r':
                return "\\r";
            case '\"':
                return "\\\"";
            case '\'':
                return "\\'";
            case '\\':
                return "\\\\";
            default:
                return ch2 < ' ' ? String.format("\\%03o", Integer.valueOf(ch2)) : String.valueOf(ch2);
        }
    }

    private static Method getMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        try {
            return clazz.getMethod(name, paramTypes);
        } catch (NoSuchMethodException e) {
            throw sneakyThrow(e);
        }
    }

    private static Method getMethod(Class<?> clazz, String name, String... paramTypes) {
        try {
            Class<?>[] c = new Class[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                c[i] = Class.forName(paramTypes[i]);
            }
            return clazz.getMethod(name, c);
        } catch (ClassNotFoundException e) {
            throw sneakyThrow(e);
        } catch (NoSuchMethodException e2) {
            throw sneakyThrow(e2);
        }
    }

    public static JCTree getExtendsClause(JCTree.JCClassDecl decl) {
        try {
            return (JCTree) getExtendsClause.invoke(decl, new Object[0]);
        } catch (IllegalAccessException e) {
            throw sneakyThrow(e);
        } catch (InvocationTargetException e2) {
            throw sneakyThrow(e2.getCause());
        }
    }

    static RuntimeException sneakyThrow(Throwable t) throws Throwable {
        if (t == null) {
            throw new NullPointerException("t");
        }
        sneakyThrow0(t);
        return null;
    }

    private static <T extends Throwable> void sneakyThrow0(Throwable t) throws Throwable {
        throw t;
    }

    private <T> T readObject(JCTree jCTree, String str, T t) throws NoSuchFieldException {
        Class<?> cls = jCTree.getClass();
        Map<String, Field> map = reflectionCache.get(cls);
        if (map == null) {
            Map<Class<?>, Map<String, Field>> map2 = reflectionCache;
            HashMap map3 = new HashMap();
            map = map3;
            map2.put(cls, map3);
        }
        Field declaredField = map.get(str);
        if (declaredField == null) {
            try {
                declaredField = cls.getDeclaredField(str);
                declaredField.setAccessible(true);
                map.put(str, declaredField);
            } catch (Exception e) {
                return t;
            }
        }
        try {
            return (T) declaredField.get(jCTree);
        } catch (Exception e2) {
            return t;
        }
    }

    public void visitTypeBoundKind(JCTree.TypeBoundKind tree) throws IOException {
        print(String.valueOf(tree.kind));
    }

    public void visitTree(JCTree tree) throws IOException {
        String simpleName = tree.getClass().getSimpleName();
        if ("JCTypeUnion".equals(simpleName)) {
            print((List) readObject(tree, "alternatives", List.nil()), " | ");
            return;
        }
        if ("JCTypeIntersection".equals(simpleName)) {
            print((List) readObject(tree, "bounds", List.nil()), " & ");
            return;
        }
        if ("JCMemberReference".equals(simpleName)) {
            printMemberReference0(tree);
            return;
        }
        if ("JCLambda".equals(simpleName)) {
            printLambda0(tree);
        } else if ("JCAnnotatedType".equals(simpleName)) {
            printAnnotatedType0(tree);
        } else if (!"JCPackageDecl".equals(simpleName)) {
            throw new AssertionError("Unhandled tree type: " + tree.getClass() + ": " + tree);
        }
    }

    private void printMemberReference0(JCTree tree) throws IOException {
        print((JCTree) readObject(tree, "expr", (JCTree.JCExpression) null));
        print(NetFlags.ANY_ADDR_V6);
        List<? extends JCTree> list = (List) readObject(tree, "typeargs", List.nil());
        if (list != null && !list.isEmpty()) {
            print("<");
            print(list, ", ");
            print(">");
        }
        print(readObject(tree, "mode", new Object()).toString().equals("INVOKE") ? (CharSequence) readObject(tree, "name", (Name) null) : "new");
    }

    private void printLambda0(JCTree tree) throws IOException {
        List<JCTree.JCVariableDecl> params = (List) readObject(tree, "params", List.nil());
        boolean explicit = true;
        int paramLength = params.size();
        try {
            explicit = readObject(tree, "paramKind", new Object()).toString().equals("EXPLICIT");
        } catch (Exception e) {
        }
        boolean useParens = paramLength != 1 || explicit;
        if (useParens) {
            print("(");
        }
        if (explicit) {
            boolean first = true;
            Iterator it = params.iterator();
            while (it.hasNext()) {
                JCTree.JCVariableDecl vd = (JCTree.JCVariableDecl) it.next();
                if (!first) {
                    print(", ");
                }
                first = false;
                printVarDefInline(vd);
            }
        } else {
            String sep = "";
            Iterator it2 = params.iterator();
            while (it2.hasNext()) {
                JCTree.JCVariableDecl param = (JCTree.JCVariableDecl) it2.next();
                print(sep);
                print((CharSequence) param.name);
                sep = ", ";
            }
        }
        if (useParens) {
            print(")");
        }
        print(" -> ");
        JCTree.JCBlock jCBlock = (JCTree) readObject(tree, "body", (JCTree) null);
        if (jCBlock instanceof JCTree.JCBlock) {
            println("{");
            this.indent++;
            print(jCBlock.stats, "");
            this.indent--;
            aPrint("}");
            return;
        }
        print((JCTree) jCBlock);
    }

    private void printAnnotatedType0(JCTree tree) throws IOException {
        JCTree.JCFieldAccess jCFieldAccess = (JCTree) readObject(tree, "underlyingType", (JCTree) null);
        if (jCFieldAccess instanceof JCTree.JCFieldAccess) {
            print((JCTree) jCFieldAccess.selected);
            print(".");
            print((List) readObject(tree, "annotations", List.nil()), SymbolConstants.SPACE_SYMBOL);
            print(SymbolConstants.SPACE_SYMBOL);
            print((CharSequence) jCFieldAccess.name);
            return;
        }
        print((List) readObject(tree, "annotations", List.nil()), SymbolConstants.SPACE_SYMBOL);
        print(SymbolConstants.SPACE_SYMBOL);
        print((JCTree) jCFieldAccess);
    }
}
