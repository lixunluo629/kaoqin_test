package lombok.javac;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Source;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.JCDiagnostic;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeVisitor;
import lombok.core.ClassLiteral;
import lombok.core.FieldSelect;
import lombok.javac.JavacTreeMaker;
import org.apache.catalina.realm.Constants;
import org.springframework.util.ClassUtils;

/* loaded from: lombok-1.16.22.jar:lombok/javac/Javac.SCL.lombok */
public class Javac {
    private static final Pattern PRIMITIVE_TYPE_NAME_PATTERN = Pattern.compile("^(boolean|byte|short|int|long|float|double|char)$");
    private static final Pattern VERSION_PARSER = Pattern.compile("^(\\d{1,6})\\.?(\\d{1,6})?.*$");
    private static final Pattern SOURCE_PARSER = Pattern.compile("^JDK(\\d{1,6})_?(\\d{1,6})?.*$");
    private static final AtomicInteger compilerVersion = new AtomicInteger(-1);
    private static final Class<?> DOCCOMMENTTABLE_CLASS;
    public static final JavacTreeMaker.TypeTag CTC_BOOLEAN;
    public static final JavacTreeMaker.TypeTag CTC_INT;
    public static final JavacTreeMaker.TypeTag CTC_DOUBLE;
    public static final JavacTreeMaker.TypeTag CTC_FLOAT;
    public static final JavacTreeMaker.TypeTag CTC_SHORT;
    public static final JavacTreeMaker.TypeTag CTC_BYTE;
    public static final JavacTreeMaker.TypeTag CTC_LONG;
    public static final JavacTreeMaker.TypeTag CTC_CHAR;
    public static final JavacTreeMaker.TypeTag CTC_VOID;
    public static final JavacTreeMaker.TypeTag CTC_NONE;
    public static final JavacTreeMaker.TypeTag CTC_BOT;
    public static final JavacTreeMaker.TypeTag CTC_ERROR;
    public static final JavacTreeMaker.TypeTag CTC_UNKNOWN;
    public static final JavacTreeMaker.TypeTag CTC_UNDETVAR;
    public static final JavacTreeMaker.TypeTag CTC_CLASS;
    public static final JavacTreeMaker.TreeTag CTC_NOT_EQUAL;
    public static final JavacTreeMaker.TreeTag CTC_LESS_THAN;
    public static final JavacTreeMaker.TreeTag CTC_GREATER_THAN;
    public static final JavacTreeMaker.TreeTag CTC_LESS_OR_EQUAL;
    public static final JavacTreeMaker.TreeTag CTC_GREATER_OR_EQUAL;
    public static final JavacTreeMaker.TreeTag CTC_POS;
    public static final JavacTreeMaker.TreeTag CTC_NEG;
    public static final JavacTreeMaker.TreeTag CTC_NOT;
    public static final JavacTreeMaker.TreeTag CTC_COMPL;
    public static final JavacTreeMaker.TreeTag CTC_BITXOR;
    public static final JavacTreeMaker.TreeTag CTC_UNSIGNED_SHIFT_RIGHT;
    public static final JavacTreeMaker.TreeTag CTC_MUL;
    public static final JavacTreeMaker.TreeTag CTC_DIV;
    public static final JavacTreeMaker.TreeTag CTC_PLUS;
    public static final JavacTreeMaker.TreeTag CTC_MINUS;
    public static final JavacTreeMaker.TreeTag CTC_EQUAL;
    public static final JavacTreeMaker.TreeTag CTC_PREINC;
    public static final JavacTreeMaker.TreeTag CTC_PREDEC;
    public static final JavacTreeMaker.TreeTag CTC_POSTINC;
    public static final JavacTreeMaker.TreeTag CTC_POSTDEC;
    private static final Method getExtendsClause;
    private static final Method getEndPosition;
    private static final Method storeEnd;
    private static final Class<?> JC_VOID_TYPE;
    private static final Class<?> JC_NO_TYPE;
    private static final Field symtabVoidType;
    private static final Field JCCOMPILATIONUNIT_ENDPOSITIONS;
    private static final Field JCCOMPILATIONUNIT_DOCCOMMENTS;

    private Javac() {
    }

    static {
        Method storeEndMethodTemp;
        Class<?> c = null;
        try {
            c = Class.forName("com.sun.tools.javac.tree.DocCommentTable");
        } catch (Throwable th) {
        }
        DOCCOMMENTTABLE_CLASS = c;
        CTC_BOOLEAN = JavacTreeMaker.TypeTag.typeTag("BOOLEAN");
        CTC_INT = JavacTreeMaker.TypeTag.typeTag("INT");
        CTC_DOUBLE = JavacTreeMaker.TypeTag.typeTag("DOUBLE");
        CTC_FLOAT = JavacTreeMaker.TypeTag.typeTag("FLOAT");
        CTC_SHORT = JavacTreeMaker.TypeTag.typeTag("SHORT");
        CTC_BYTE = JavacTreeMaker.TypeTag.typeTag("BYTE");
        CTC_LONG = JavacTreeMaker.TypeTag.typeTag("LONG");
        CTC_CHAR = JavacTreeMaker.TypeTag.typeTag("CHAR");
        CTC_VOID = JavacTreeMaker.TypeTag.typeTag("VOID");
        CTC_NONE = JavacTreeMaker.TypeTag.typeTag(Constants.NONE_TRANSPORT);
        CTC_BOT = JavacTreeMaker.TypeTag.typeTag("BOT");
        CTC_ERROR = JavacTreeMaker.TypeTag.typeTag("ERROR");
        CTC_UNKNOWN = JavacTreeMaker.TypeTag.typeTag("UNKNOWN");
        CTC_UNDETVAR = JavacTreeMaker.TypeTag.typeTag("UNDETVAR");
        CTC_CLASS = JavacTreeMaker.TypeTag.typeTag("CLASS");
        CTC_NOT_EQUAL = JavacTreeMaker.TreeTag.treeTag("NE");
        CTC_LESS_THAN = JavacTreeMaker.TreeTag.treeTag("LT");
        CTC_GREATER_THAN = JavacTreeMaker.TreeTag.treeTag("GT");
        CTC_LESS_OR_EQUAL = JavacTreeMaker.TreeTag.treeTag("LE");
        CTC_GREATER_OR_EQUAL = JavacTreeMaker.TreeTag.treeTag("GE");
        CTC_POS = JavacTreeMaker.TreeTag.treeTag("POS");
        CTC_NEG = JavacTreeMaker.TreeTag.treeTag("NEG");
        CTC_NOT = JavacTreeMaker.TreeTag.treeTag("NOT");
        CTC_COMPL = JavacTreeMaker.TreeTag.treeTag("COMPL");
        CTC_BITXOR = JavacTreeMaker.TreeTag.treeTag("BITXOR");
        CTC_UNSIGNED_SHIFT_RIGHT = JavacTreeMaker.TreeTag.treeTag("USR");
        CTC_MUL = JavacTreeMaker.TreeTag.treeTag("MUL");
        CTC_DIV = JavacTreeMaker.TreeTag.treeTag("DIV");
        CTC_PLUS = JavacTreeMaker.TreeTag.treeTag("PLUS");
        CTC_MINUS = JavacTreeMaker.TreeTag.treeTag("MINUS");
        CTC_EQUAL = JavacTreeMaker.TreeTag.treeTag("EQ");
        CTC_PREINC = JavacTreeMaker.TreeTag.treeTag("PREINC");
        CTC_PREDEC = JavacTreeMaker.TreeTag.treeTag("PREDEC");
        CTC_POSTINC = JavacTreeMaker.TreeTag.treeTag("POSTINC");
        CTC_POSTDEC = JavacTreeMaker.TreeTag.treeTag("POSTDEC");
        getExtendsClause = getMethod((Class<?>) JCTree.JCClassDecl.class, "getExtendsClause", (Class<?>[]) new Class[0]);
        getExtendsClause.setAccessible(true);
        if (getJavaCompilerVersion() < 8) {
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
        Class<?> c2 = null;
        try {
            c2 = Class.forName("com.sun.tools.javac.code.Type$JCVoidType");
        } catch (Throwable th2) {
        }
        JC_VOID_TYPE = c2;
        Class<?> c3 = null;
        try {
            c3 = Class.forName("com.sun.tools.javac.code.Type$JCNoType");
        } catch (Throwable th3) {
        }
        JC_NO_TYPE = c3;
        symtabVoidType = getFieldIfExists(Symtab.class, "voidType");
        Field f = null;
        try {
            f = JCTree.JCCompilationUnit.class.getDeclaredField("endPositions");
        } catch (NoSuchFieldException e2) {
        }
        JCCOMPILATIONUNIT_ENDPOSITIONS = f;
        Field f2 = null;
        try {
            f2 = JCTree.JCCompilationUnit.class.getDeclaredField("docComments");
        } catch (NoSuchFieldException e3) {
        }
        JCCOMPILATIONUNIT_DOCCOMMENTS = f2;
    }

    public static int getJavaCompilerVersion() throws NumberFormatException {
        int cv = compilerVersion.get();
        if (cv != -1) {
            return cv;
        }
        Matcher m = VERSION_PARSER.matcher(JavaCompiler.version());
        if (m.matches()) {
            int major = Integer.parseInt(m.group(1));
            if (major == 1) {
                int minor = Integer.parseInt(m.group(2));
                return setVersion(minor);
            }
            if (major >= 9) {
                return setVersion(major);
            }
        }
        String name = Source.values()[Source.values().length - 1].name();
        Matcher m2 = SOURCE_PARSER.matcher(name);
        if (m2.matches()) {
            int major2 = Integer.parseInt(m2.group(1));
            if (major2 == 1) {
                int minor2 = Integer.parseInt(m2.group(2));
                return setVersion(minor2);
            }
            if (major2 >= 9) {
                return setVersion(major2);
            }
        }
        return setVersion(6);
    }

    private static int setVersion(int version) {
        compilerVersion.set(version);
        return version;
    }

    public static boolean instanceOfDocCommentTable(Object o) {
        return DOCCOMMENTTABLE_CLASS != null && DOCCOMMENTTABLE_CLASS.isInstance(o);
    }

    public static boolean isPrimitive(JCTree.JCExpression ref) {
        String typeName = ref.toString();
        return PRIMITIVE_TYPE_NAME_PATTERN.matcher(typeName).matches();
    }

    public static Object calculateGuess(JCTree.JCExpression expr) {
        if (expr instanceof JCTree.JCLiteral) {
            JCTree.JCLiteral lit = (JCTree.JCLiteral) expr;
            if (lit.getKind() == Tree.Kind.BOOLEAN_LITERAL) {
                return Boolean.valueOf(((Number) lit.value).intValue() != 0);
            }
            return lit.value;
        }
        if ((expr instanceof JCTree.JCIdent) || (expr instanceof JCTree.JCFieldAccess)) {
            String x = expr.toString();
            if (x.endsWith(ClassUtils.CLASS_FILE_SUFFIX)) {
                return new ClassLiteral(x.substring(0, x.length() - 6));
            }
            int idx = x.lastIndexOf(46);
            if (idx > -1) {
                x = x.substring(idx + 1);
            }
            return new FieldSelect(x);
        }
        return null;
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

    public static Object getDocComments(JCTree.JCCompilationUnit cu) {
        try {
            return JCCOMPILATIONUNIT_DOCCOMMENTS.get(cu);
        } catch (IllegalAccessException e) {
            throw sneakyThrow(e);
        }
    }

    public static void initDocComments(JCTree.JCCompilationUnit cu) throws IllegalAccessException, IllegalArgumentException {
        try {
            JCCOMPILATIONUNIT_DOCCOMMENTS.set(cu, new HashMap());
        } catch (IllegalAccessException e) {
            throw sneakyThrow(e);
        } catch (IllegalArgumentException e2) {
        }
    }

    public static int getEndPosition(JCDiagnostic.DiagnosticPosition pos, JCTree.JCCompilationUnit top) throws IllegalAccessException, IllegalArgumentException {
        try {
            Object endPositions = JCCOMPILATIONUNIT_ENDPOSITIONS.get(top);
            return ((Integer) getEndPosition.invoke(pos, endPositions)).intValue();
        } catch (IllegalAccessException e) {
            throw sneakyThrow(e);
        } catch (InvocationTargetException e2) {
            throw sneakyThrow(e2.getCause());
        }
    }

    public static void storeEnd(JCTree tree, int pos, JCTree.JCCompilationUnit top) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            Object endPositions = JCCOMPILATIONUNIT_ENDPOSITIONS.get(top);
            storeEnd.invoke(endPositions, tree, Integer.valueOf(pos));
        } catch (IllegalAccessException e) {
            throw sneakyThrow(e);
        } catch (InvocationTargetException e2) {
            throw sneakyThrow(e2.getCause());
        }
    }

    private static Field getFieldIfExists(Class<?> c, String fieldName) {
        try {
            return c.getField("voidType");
        } catch (Exception e) {
            return null;
        }
    }

    public static Type createVoidType(Symtab symbolTable, JavacTreeMaker.TypeTag tag) {
        if (symtabVoidType != null) {
            try {
                return (Type) symtabVoidType.get(symbolTable);
            } catch (IllegalAccessException e) {
            }
        }
        if (getJavaCompilerVersion() < 8) {
            return new JCNoType(((Integer) tag.value).intValue());
        }
        try {
            if (CTC_VOID.equals(tag)) {
                return (Type) JC_VOID_TYPE.newInstance();
            }
            return (Type) JC_NO_TYPE.newInstance();
        } catch (IllegalAccessException e2) {
            throw sneakyThrow(e2);
        } catch (InstantiationException e3) {
            throw sneakyThrow(e3);
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/Javac$JCNoType.SCL.lombok */
    private static class JCNoType extends Type implements NoType {
        public JCNoType(int tag) {
            super(tag, (Symbol.TypeSymbol) null);
        }

        public TypeKind getKind() {
            if (this.tag == ((Integer) Javac.CTC_VOID.value).intValue()) {
                return TypeKind.VOID;
            }
            if (this.tag == ((Integer) Javac.CTC_NONE.value).intValue()) {
                return TypeKind.NONE;
            }
            throw new AssertionError("Unexpected tag: " + this.tag);
        }

        public <R, P> R accept(TypeVisitor<R, P> typeVisitor, P p) {
            return (R) typeVisitor.visitNoType(this, p);
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
}
