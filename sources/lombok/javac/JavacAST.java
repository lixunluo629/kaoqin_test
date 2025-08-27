package lombok.javac;

import com.sun.tools.javac.code.Source;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.model.JavacTypes;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import lombok.core.AST;
import org.aspectj.weaver.model.AsmRelationshipUtils;

/* loaded from: lombok-1.16.22.jar:lombok/javac/JavacAST.SCL.lombok */
public class JavacAST extends AST<JavacAST, JavacNode, JCTree> {
    private final JavacElements elements;
    private final JavacTreeMaker treeMaker;
    private final Symtab symtab;
    private final JavacTypes javacTypes;
    private final Log log;
    private final ErrorLog errorLogger;
    private final Context context;
    private static boolean JCTRY_RESOURCES_FIELD_INITIALIZED;
    private static Field JCTRY_RESOURCES_FIELD;
    private static final ConcurrentMap<Class<?>, Method> getBodyMethods = new ConcurrentHashMap();

    @Override // lombok.core.AST
    protected /* bridge */ /* synthetic */ void setElementInASTCollection(Field field, Object obj, List list, Collection collection, int i, JCTree jCTree) throws IllegalAccessException, IllegalArgumentException {
        setElementInASTCollection2(field, obj, (List<Collection<?>>) list, (Collection<?>) collection, i, jCTree);
    }

    public JavacAST(Messager messager, Context context, JCTree.JCCompilationUnit top) {
        super(sourceName(top), PackageName.getPackageName(top), new JavacImportList(top), statementTypes());
        setTop(buildCompilationUnit(top));
        this.context = context;
        this.log = Log.instance(context);
        this.errorLogger = ErrorLog.create(messager, this.log);
        this.elements = JavacElements.instance(context);
        this.treeMaker = new JavacTreeMaker(TreeMaker.instance(context));
        this.symtab = Symtab.instance(context);
        this.javacTypes = JavacTypes.instance(context);
        clearChanged();
    }

    @Override // lombok.core.AST
    public URI getAbsoluteFileLocation() {
        try {
            JCTree.JCCompilationUnit cu = top().get();
            return cu.sourcefile.toUri();
        } catch (Exception e) {
            return null;
        }
    }

    private static String sourceName(JCTree.JCCompilationUnit cu) {
        if (cu.sourcefile == null) {
            return null;
        }
        return cu.sourcefile.toString();
    }

    public Context getContext() {
        return this.context;
    }

    public void traverse(JavacASTVisitor visitor) {
        top().traverse(visitor);
    }

    void traverseChildren(JavacASTVisitor visitor, JavacNode node) {
        Iterator<JavacNode> it = node.down().iterator();
        while (it.hasNext()) {
            JavacNode child = it.next();
            child.traverse(visitor);
        }
    }

    @Override // lombok.core.AST
    public int getSourceVersion() {
        try {
            String nm = Source.instance(this.context).name();
            int underscoreIdx = nm.indexOf(95);
            return underscoreIdx > -1 ? Integer.parseInt(nm.substring(underscoreIdx + 1)) : Integer.parseInt(nm);
        } catch (Exception e) {
            return 6;
        }
    }

    @Override // lombok.core.AST
    public int getLatestJavaSpecSupported() {
        return Javac.getJavaCompilerVersion();
    }

    public Name toName(String name) {
        return this.elements.getName(name);
    }

    public JavacTreeMaker getTreeMaker() {
        this.treeMaker.at(-1);
        return this.treeMaker;
    }

    public Symtab getSymbolTable() {
        return this.symtab;
    }

    public JavacTypes getTypesUtil() {
        return this.javacTypes;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // lombok.core.AST
    public JavacNode buildTree(JCTree node, AST.Kind kind) {
        switch (kind) {
            case COMPILATION_UNIT:
                return buildCompilationUnit((JCTree.JCCompilationUnit) node);
            case TYPE:
                return buildType((JCTree.JCClassDecl) node);
            case FIELD:
                return buildField((JCTree.JCVariableDecl) node);
            case INITIALIZER:
                return buildInitializer((JCTree.JCBlock) node);
            case METHOD:
                return buildMethod((JCTree.JCMethodDecl) node);
            case ARGUMENT:
                return buildLocalVar((JCTree.JCVariableDecl) node, kind);
            case LOCAL:
                return buildLocalVar((JCTree.JCVariableDecl) node, kind);
            case STATEMENT:
                return buildStatementOrExpression(node);
            case ANNOTATION:
                return buildAnnotation((JCTree.JCAnnotation) node, false);
            default:
                throw new AssertionError("Did not expect: " + kind);
        }
    }

    private JavacNode buildCompilationUnit(JCTree.JCCompilationUnit top) {
        List<JavacNode> childNodes = new ArrayList<>();
        Iterator it = top.defs.iterator();
        while (it.hasNext()) {
            JCTree s = (JCTree) it.next();
            if (s instanceof JCTree.JCClassDecl) {
                addIfNotNull(childNodes, buildType((JCTree.JCClassDecl) s));
            }
        }
        return new JavacNode(this, top, childNodes, AST.Kind.COMPILATION_UNIT);
    }

    private JavacNode buildType(JCTree.JCClassDecl type) {
        if (setAndGetAsHandled(type)) {
            return null;
        }
        List<JavacNode> childNodes = new ArrayList<>();
        Iterator it = type.mods.annotations.iterator();
        while (it.hasNext()) {
            JCTree.JCAnnotation annotation = (JCTree.JCAnnotation) it.next();
            addIfNotNull(childNodes, buildAnnotation(annotation, false));
        }
        Iterator it2 = type.defs.iterator();
        while (it2.hasNext()) {
            JCTree def = (JCTree) it2.next();
            if (def instanceof JCTree.JCMethodDecl) {
                addIfNotNull(childNodes, buildMethod((JCTree.JCMethodDecl) def));
            } else if (def instanceof JCTree.JCClassDecl) {
                addIfNotNull(childNodes, buildType((JCTree.JCClassDecl) def));
            } else if (def instanceof JCTree.JCVariableDecl) {
                addIfNotNull(childNodes, buildField((JCTree.JCVariableDecl) def));
            } else if (def instanceof JCTree.JCBlock) {
                addIfNotNull(childNodes, buildInitializer((JCTree.JCBlock) def));
            }
        }
        return putInMap(new JavacNode(this, type, childNodes, AST.Kind.TYPE));
    }

    private JavacNode buildField(JCTree.JCVariableDecl field) {
        if (setAndGetAsHandled(field)) {
            return null;
        }
        List<JavacNode> childNodes = new ArrayList<>();
        Iterator it = field.mods.annotations.iterator();
        while (it.hasNext()) {
            JCTree.JCAnnotation annotation = (JCTree.JCAnnotation) it.next();
            addIfNotNull(childNodes, buildAnnotation(annotation, true));
        }
        addIfNotNull(childNodes, buildExpression(field.init));
        return putInMap(new JavacNode(this, field, childNodes, AST.Kind.FIELD));
    }

    private JavacNode buildLocalVar(JCTree.JCVariableDecl local, AST.Kind kind) {
        if (setAndGetAsHandled(local)) {
            return null;
        }
        List<JavacNode> childNodes = new ArrayList<>();
        Iterator it = local.mods.annotations.iterator();
        while (it.hasNext()) {
            JCTree.JCAnnotation annotation = (JCTree.JCAnnotation) it.next();
            addIfNotNull(childNodes, buildAnnotation(annotation, true));
        }
        addIfNotNull(childNodes, buildExpression(local.init));
        return putInMap(new JavacNode(this, local, childNodes, kind));
    }

    private static List<JCTree> getResourcesForTryNode(JCTree.JCTry tryNode) throws IllegalAccessException, IllegalArgumentException {
        if (!JCTRY_RESOURCES_FIELD_INITIALIZED) {
            try {
                JCTRY_RESOURCES_FIELD = JCTree.JCTry.class.getField("resources");
            } catch (NoSuchFieldException e) {
            } catch (Exception e2) {
            }
            JCTRY_RESOURCES_FIELD_INITIALIZED = true;
        }
        if (JCTRY_RESOURCES_FIELD == null) {
            return Collections.emptyList();
        }
        Object rv = null;
        try {
            rv = JCTRY_RESOURCES_FIELD.get(tryNode);
        } catch (Exception e3) {
        }
        return rv instanceof List ? (List) rv : Collections.emptyList();
    }

    private JavacNode buildTry(JCTree.JCTry tryNode) {
        if (setAndGetAsHandled(tryNode)) {
            return null;
        }
        List<JavacNode> childNodes = new ArrayList<>();
        for (JCTree varDecl : getResourcesForTryNode(tryNode)) {
            if (varDecl instanceof JCTree.JCVariableDecl) {
                addIfNotNull(childNodes, buildLocalVar((JCTree.JCVariableDecl) varDecl, AST.Kind.LOCAL));
            }
        }
        addIfNotNull(childNodes, buildStatement(tryNode.body));
        Iterator it = tryNode.catchers.iterator();
        while (it.hasNext()) {
            JCTree.JCCatch jcc = (JCTree.JCCatch) it.next();
            addIfNotNull(childNodes, buildTree((JCTree) jcc, AST.Kind.STATEMENT));
        }
        addIfNotNull(childNodes, buildStatement(tryNode.finalizer));
        return putInMap(new JavacNode(this, tryNode, childNodes, AST.Kind.STATEMENT));
    }

    private JavacNode buildInitializer(JCTree.JCBlock initializer) {
        if (setAndGetAsHandled(initializer)) {
            return null;
        }
        List<JavacNode> childNodes = new ArrayList<>();
        Iterator it = initializer.stats.iterator();
        while (it.hasNext()) {
            JCTree.JCStatement statement = (JCTree.JCStatement) it.next();
            addIfNotNull(childNodes, buildStatement(statement));
        }
        return putInMap(new JavacNode(this, initializer, childNodes, AST.Kind.INITIALIZER));
    }

    private JavacNode buildMethod(JCTree.JCMethodDecl method) {
        if (setAndGetAsHandled(method)) {
            return null;
        }
        List<JavacNode> childNodes = new ArrayList<>();
        Iterator it = method.mods.annotations.iterator();
        while (it.hasNext()) {
            JCTree.JCAnnotation annotation = (JCTree.JCAnnotation) it.next();
            addIfNotNull(childNodes, buildAnnotation(annotation, false));
        }
        Iterator it2 = method.params.iterator();
        while (it2.hasNext()) {
            JCTree.JCVariableDecl param = (JCTree.JCVariableDecl) it2.next();
            addIfNotNull(childNodes, buildLocalVar(param, AST.Kind.ARGUMENT));
        }
        if (method.body != null && method.body.stats != null) {
            Iterator it3 = method.body.stats.iterator();
            while (it3.hasNext()) {
                JCTree.JCStatement statement = (JCTree.JCStatement) it3.next();
                addIfNotNull(childNodes, buildStatement(statement));
            }
        }
        return putInMap(new JavacNode(this, method, childNodes, AST.Kind.METHOD));
    }

    private JavacNode buildAnnotation(JCTree.JCAnnotation annotation, boolean varDecl) {
        boolean handled = setAndGetAsHandled(annotation);
        if (!varDecl && handled) {
            return null;
        }
        return putInMap(new JavacNode(this, annotation, null, AST.Kind.ANNOTATION));
    }

    private JavacNode buildExpression(JCTree.JCExpression expression) {
        return buildStatementOrExpression(expression);
    }

    private JavacNode buildStatement(JCTree.JCStatement statement) {
        return buildStatementOrExpression(statement);
    }

    private JavacNode buildStatementOrExpression(JCTree statement) {
        if (statement == null || (statement instanceof JCTree.JCAnnotation)) {
            return null;
        }
        if (statement instanceof JCTree.JCClassDecl) {
            return buildType((JCTree.JCClassDecl) statement);
        }
        if (statement instanceof JCTree.JCVariableDecl) {
            return buildLocalVar((JCTree.JCVariableDecl) statement, AST.Kind.LOCAL);
        }
        if (statement instanceof JCTree.JCTry) {
            return buildTry((JCTree.JCTry) statement);
        }
        if (statement.getClass().getSimpleName().equals("JCLambda")) {
            return buildLambda(statement);
        }
        if (setAndGetAsHandled(statement)) {
            return null;
        }
        return drill(statement);
    }

    private JavacNode buildLambda(JCTree jcTree) {
        return buildStatementOrExpression(getBody(jcTree));
    }

    private JCTree getBody(JCTree jcTree) {
        try {
            return (JCTree) getBodyMethod(jcTree.getClass()).invoke(jcTree, new Object[0]);
        } catch (Exception e) {
            throw Javac.sneakyThrow(e);
        }
    }

    private Method getBodyMethod(Class<?> c) throws NoSuchMethodException, SecurityException {
        Method m = getBodyMethods.get(c);
        if (m != null) {
            return m;
        }
        try {
            getBodyMethods.putIfAbsent(c, c.getMethod("getBody", new Class[0]));
            return getBodyMethods.get(c);
        } catch (NoSuchMethodException e) {
            throw Javac.sneakyThrow(e);
        }
    }

    private JavacNode drill(JCTree statement) {
        try {
            List<JavacNode> childNodes = new ArrayList<>();
            for (AST.FieldAccess fa : fieldsOf(statement.getClass())) {
                childNodes.addAll(buildWithField(JavacNode.class, statement, fa));
            }
            return putInMap(new JavacNode(this, statement, childNodes, AST.Kind.STATEMENT));
        } catch (OutOfMemoryError oome) {
            String msg = oome.getMessage();
            if (msg == null) {
                msg = "(no original message)";
            }
            OutOfMemoryError newError = new OutOfMemoryError(getFileName() + "@pos" + statement.getPreferredPosition() + ": " + msg);
            throw newError;
        }
    }

    private static Collection<Class<? extends JCTree>> statementTypes() {
        Collection<Class<? extends JCTree>> collection = new ArrayList<>(3);
        collection.add(JCTree.JCStatement.class);
        collection.add(JCTree.JCExpression.class);
        collection.add(JCTree.JCCatch.class);
        return collection;
    }

    private static void addIfNotNull(Collection<JavacNode> nodes, JavacNode node) {
        if (node != null) {
            nodes.add(node);
        }
    }

    void removeDeferredErrors(JavacNode node) {
        JCDiagnostic.DiagnosticPosition pos = node.get().pos();
        JCTree.JCCompilationUnit top = top().get();
        removeFromDeferredDiagnostics(pos.getStartPosition(), Javac.getEndPosition(pos, top));
    }

    void printMessage(Diagnostic.Kind kind, String message, JavacNode node, JCDiagnostic.DiagnosticPosition pos, boolean attemptToRemoveErrorsInRange) {
        JavaFileObject oldSource = null;
        JCTree astObject = node == null ? null : node.get();
        JCTree.JCCompilationUnit top = top().get();
        JavaFileObject newSource = top.sourcefile;
        if (newSource != null) {
            oldSource = this.log.useSource(newSource);
            if (pos == null) {
                pos = astObject.pos();
            }
        }
        if (pos != null && node != null && attemptToRemoveErrorsInRange) {
            removeFromDeferredDiagnostics(pos.getStartPosition(), node.getEndPosition(pos));
        }
        try {
            switch (AnonymousClass1.$SwitchMap$javax$tools$Diagnostic$Kind[kind.ordinal()]) {
                case 1:
                    this.errorLogger.error(pos, message);
                    break;
                case 2:
                    this.errorLogger.mandatoryWarning(pos, message);
                    break;
                case 3:
                    this.errorLogger.warning(pos, message);
                    break;
                case 4:
                default:
                    this.errorLogger.note(pos, message);
                    break;
            }
        } finally {
            if (newSource != null) {
                this.log.useSource(oldSource);
            }
        }
    }

    /* renamed from: lombok.javac.JavacAST$1, reason: invalid class name */
    /* loaded from: lombok-1.16.22.jar:lombok/javac/JavacAST$1.SCL.lombok */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$javax$tools$Diagnostic$Kind = new int[Diagnostic.Kind.values().length];

        static {
            try {
                $SwitchMap$javax$tools$Diagnostic$Kind[Diagnostic.Kind.ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$javax$tools$Diagnostic$Kind[Diagnostic.Kind.MANDATORY_WARNING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javax$tools$Diagnostic$Kind[Diagnostic.Kind.WARNING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$javax$tools$Diagnostic$Kind[Diagnostic.Kind.NOTE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            $SwitchMap$lombok$core$AST$Kind = new int[AST.Kind.valuesCustom().length];
            try {
                $SwitchMap$lombok$core$AST$Kind[AST.Kind.COMPILATION_UNIT.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$lombok$core$AST$Kind[AST.Kind.TYPE.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$lombok$core$AST$Kind[AST.Kind.FIELD.ordinal()] = 3;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$lombok$core$AST$Kind[AST.Kind.INITIALIZER.ordinal()] = 4;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$lombok$core$AST$Kind[AST.Kind.METHOD.ordinal()] = 5;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$lombok$core$AST$Kind[AST.Kind.ARGUMENT.ordinal()] = 6;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$lombok$core$AST$Kind[AST.Kind.LOCAL.ordinal()] = 7;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$lombok$core$AST$Kind[AST.Kind.STATEMENT.ordinal()] = 8;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$lombok$core$AST$Kind[AST.Kind.ANNOTATION.ordinal()] = 9;
            } catch (NoSuchFieldError e13) {
            }
        }
    }

    public void removeFromDeferredDiagnostics(int startPos, int endPos) throws IllegalAccessException, IllegalArgumentException {
        JCTree.JCCompilationUnit self = top().get();
        new CompilerMessageSuppressor(getContext()).removeAllBetween(self.sourcefile, startPos, endPos);
    }

    /* renamed from: setElementInASTCollection, reason: avoid collision after fix types in other method */
    protected void setElementInASTCollection2(Field field, Object refField, List<Collection<?>> chain, Collection<?> collection, int idx, JCTree newN) throws IllegalAccessException, IllegalArgumentException {
        com.sun.tools.javac.util.List<?> list = setElementInConsList(chain, collection, ((List) collection).get(idx), newN);
        field.set(refField, list);
    }

    private com.sun.tools.javac.util.List<?> setElementInConsList(List<Collection<?>> chain, Collection<?> current, Object oldO, Object newO) {
        com.sun.tools.javac.util.List<?> oldL = (com.sun.tools.javac.util.List) current;
        com.sun.tools.javac.util.List<?> newL = replaceInConsList(oldL, oldO, newO);
        if (chain.isEmpty()) {
            return newL;
        }
        List<Collection<?>> reducedChain = new ArrayList<>(chain);
        Collection<?> newCurrent = reducedChain.remove(reducedChain.size() - 1);
        return setElementInConsList(reducedChain, newCurrent, oldL, newL);
    }

    private com.sun.tools.javac.util.List<?> replaceInConsList(com.sun.tools.javac.util.List<?> oldL, Object oldO, Object newO) {
        boolean repl = false;
        Object[] a = oldL.toArray();
        for (int i = 0; i < a.length; i++) {
            if (a[i] == oldO) {
                a[i] = newO;
                repl = true;
            }
        }
        return repl ? com.sun.tools.javac.util.List.from(a) : oldL;
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/JavacAST$ErrorLog.SCL.lombok */
    static abstract class ErrorLog {
        final Log log;
        private final Messager messager;
        private final Field errorCount;
        private final Field warningCount;

        abstract void error1(JCDiagnostic.DiagnosticPosition diagnosticPosition, String str);

        abstract void warning1(JCDiagnostic.DiagnosticPosition diagnosticPosition, String str);

        abstract void mandatoryWarning1(JCDiagnostic.DiagnosticPosition diagnosticPosition, String str);

        abstract void note(JCDiagnostic.DiagnosticPosition diagnosticPosition, String str);

        /* synthetic */ ErrorLog(Log x0, Messager x1, Field x2, Field x3, AnonymousClass1 x4) {
            this(x0, x1, x2, x3);
        }

        private ErrorLog(Log log, Messager messager, Field errorCount, Field warningCount) {
            this.log = log;
            this.messager = messager;
            this.errorCount = errorCount;
            this.warningCount = warningCount;
        }

        final void error(JCDiagnostic.DiagnosticPosition pos, String message) {
            increment(this.errorCount);
            error1(pos, message);
        }

        final void warning(JCDiagnostic.DiagnosticPosition pos, String message) {
            increment(this.warningCount);
            warning1(pos, message);
        }

        final void mandatoryWarning(JCDiagnostic.DiagnosticPosition pos, String message) {
            increment(this.warningCount);
            mandatoryWarning1(pos, message);
        }

        private void increment(Field field) {
            if (field == null) {
                return;
            }
            try {
                int val = ((Number) field.get(this.messager)).intValue();
                field.set(this.messager, Integer.valueOf(val + 1));
            } catch (Throwable th) {
            }
        }

        static ErrorLog create(Messager messager, Log log) throws SecurityException {
            Field errorCount = null;
            try {
                Field f = messager.getClass().getDeclaredField("errorCount");
                f.setAccessible(true);
                errorCount = f;
            } catch (Throwable th) {
            }
            boolean hasMultipleErrors = false;
            Field[] fields = log.getClass().getFields();
            int length = fields.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Field field = fields[i];
                if (!field.getName().equals("multipleErrors")) {
                    i++;
                } else {
                    hasMultipleErrors = true;
                    break;
                }
            }
            if (hasMultipleErrors) {
                return new JdkBefore9(log, messager, errorCount, null);
            }
            Field warningCount = null;
            try {
                Field f2 = messager.getClass().getDeclaredField("warningCount");
                f2.setAccessible(true);
                warningCount = f2;
            } catch (Throwable th2) {
            }
            return new Jdk9Plus(log, messager, errorCount, warningCount, null);
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/JavacAST$JdkBefore9.SCL.lombok */
    static class JdkBefore9 extends ErrorLog {
        /* synthetic */ JdkBefore9(Log x0, Messager x1, Field x2, AnonymousClass1 x3) {
            this(x0, x1, x2);
        }

        private JdkBefore9(Log log, Messager messager, Field errorCount) {
            super(log, messager, errorCount, null, null);
        }

        @Override // lombok.javac.JavacAST.ErrorLog
        void error1(JCDiagnostic.DiagnosticPosition pos, String message) {
            boolean prev = this.log.multipleErrors;
            this.log.multipleErrors = true;
            try {
                this.log.error(pos, "proc.messager", new Object[]{message});
                this.log.multipleErrors = prev;
            } catch (Throwable th) {
                this.log.multipleErrors = prev;
                throw th;
            }
        }

        @Override // lombok.javac.JavacAST.ErrorLog
        void warning1(JCDiagnostic.DiagnosticPosition pos, String message) {
            this.log.warning(pos, "proc.messager", new Object[]{message});
        }

        @Override // lombok.javac.JavacAST.ErrorLog
        void mandatoryWarning1(JCDiagnostic.DiagnosticPosition pos, String message) {
            this.log.mandatoryWarning(pos, "proc.messager", new Object[]{message});
        }

        @Override // lombok.javac.JavacAST.ErrorLog
        void note(JCDiagnostic.DiagnosticPosition pos, String message) {
            this.log.note(pos, "proc.messager", new Object[]{message});
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/JavacAST$Jdk9Plus.SCL.lombok */
    static class Jdk9Plus extends ErrorLog {
        private static final String PROC_MESSAGER = "proc.messager";
        private Object multiple;
        private Method errorMethod;
        private Method warningMethod;
        private Method mandatoryWarningMethod;
        private Method noteMethod;
        private Method errorKey;
        private Method warningKey;
        private Method noteKey;
        private JCDiagnostic.Factory diags;

        /* synthetic */ Jdk9Plus(Log x0, Messager x1, Field x2, Field x3, AnonymousClass1 x4) {
            this(x0, x1, x2, x3);
        }

        private Jdk9Plus(Log log, Messager messager, Field errorCount, Field warningCount) {
            super(log, messager, errorCount, warningCount, null);
            try {
                Class<?> df = Class.forName("com.sun.tools.javac.util.JCDiagnostic$DiagnosticFlag");
                for (Object constant : df.getEnumConstants()) {
                    if (constant.toString().equals("MULTIPLE")) {
                        this.multiple = constant;
                    }
                }
                Class<?> errorCls = Class.forName("com.sun.tools.javac.util.JCDiagnostic$Error");
                Class<?> warningCls = Class.forName("com.sun.tools.javac.util.JCDiagnostic$Warning");
                Class<?> noteCls = Class.forName("com.sun.tools.javac.util.JCDiagnostic$Note");
                Class<?> lc = log.getClass();
                this.errorMethod = lc.getMethod(AsmRelationshipUtils.DECLARE_ERROR, df, JCDiagnostic.DiagnosticPosition.class, errorCls);
                this.warningMethod = lc.getMethod(AsmRelationshipUtils.DECLARE_WARNING, JCDiagnostic.DiagnosticPosition.class, warningCls);
                this.mandatoryWarningMethod = lc.getMethod("mandatoryWarning", JCDiagnostic.DiagnosticPosition.class, warningCls);
                this.noteMethod = lc.getMethod("note", JCDiagnostic.DiagnosticPosition.class, noteCls);
                Field diagsField = lc.getSuperclass().getDeclaredField("diags");
                diagsField.setAccessible(true);
                this.diags = (JCDiagnostic.Factory) diagsField.get(log);
                Class<?> dc = this.diags.getClass();
                this.errorKey = dc.getMethod("errorKey", String.class, Object[].class);
                this.warningKey = dc.getDeclaredMethod("warningKey", String.class, Object[].class);
                this.warningKey.setAccessible(true);
                this.noteKey = dc.getDeclaredMethod("noteKey", String.class, Object[].class);
                this.noteKey.setAccessible(true);
            } catch (Throwable th) {
            }
        }

        @Override // lombok.javac.JavacAST.ErrorLog
        void error1(JCDiagnostic.DiagnosticPosition pos, String message) {
            try {
                Object error = this.errorKey.invoke(this.diags, PROC_MESSAGER, new Object[]{message});
                this.errorMethod.invoke(this.log, this.multiple, pos, error);
            } catch (Throwable th) {
            }
        }

        @Override // lombok.javac.JavacAST.ErrorLog
        void warning1(JCDiagnostic.DiagnosticPosition pos, String message) {
            try {
                Object warning = this.warningKey.invoke(this.diags, PROC_MESSAGER, new Object[]{message});
                this.warningMethod.invoke(this.log, pos, warning);
            } catch (Throwable th) {
            }
        }

        @Override // lombok.javac.JavacAST.ErrorLog
        void mandatoryWarning1(JCDiagnostic.DiagnosticPosition pos, String message) {
            try {
                Object warning = this.warningKey.invoke(this.diags, PROC_MESSAGER, new Object[]{message});
                this.mandatoryWarningMethod.invoke(this.log, pos, warning);
            } catch (Throwable th) {
            }
        }

        @Override // lombok.javac.JavacAST.ErrorLog
        void note(JCDiagnostic.DiagnosticPosition pos, String message) {
            try {
                Object note = this.noteKey.invoke(this.diags, PROC_MESSAGER, new Object[]{message});
                this.noteMethod.invoke(this.log, pos, note);
            } catch (Throwable th) {
            }
        }
    }
}
