package lombok.javac;

import com.sun.tools.javac.code.BoundKind;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.MemberEnter;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Map;
import javax.lang.model.type.TypeKind;
import javax.tools.JavaFileObject;
import lombok.Lombok;
import lombok.core.debug.AssertionLogger;
import lombok.javac.JavacTreeMaker;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;

/* loaded from: lombok-1.16.22.jar:lombok/javac/JavacResolution.SCL.lombok */
public class JavacResolution {
    private final Attr attr;
    private final CompilerMessageSuppressor messageSuppressor;
    private static Field memberEnterDotEnv;

    public JavacResolution(Context context) {
        this.attr = Attr.instance(context);
        this.messageSuppressor = new CompilerMessageSuppressor(context);
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/JavacResolution$EnvFinder.SCL.lombok */
    private static final class EnvFinder extends JCTree.Visitor {
        private Enter enter;
        private MemberEnter memberEnter;
        private Env<AttrContext> env = null;
        private JCTree copyAt = null;

        EnvFinder(Context context) {
            this.enter = Enter.instance(context);
            this.memberEnter = MemberEnter.instance(context);
        }

        Env<AttrContext> get() {
            return this.env;
        }

        JCTree copyAt() {
            return this.copyAt;
        }

        public void visitTopLevel(JCTree.JCCompilationUnit tree) {
            if (this.copyAt != null) {
                return;
            }
            this.env = this.enter.getTopLevelEnv(tree);
        }

        public void visitClassDef(JCTree.JCClassDecl tree) {
            if (this.copyAt == null && tree.sym != null) {
                this.env = this.enter.getClassEnv(tree.sym);
            }
        }

        public void visitMethodDef(JCTree.JCMethodDecl tree) {
            if (this.copyAt != null) {
                return;
            }
            this.env = this.memberEnter.getMethodEnv(tree, this.env);
            this.copyAt = tree;
        }

        public void visitVarDef(JCTree.JCVariableDecl tree) {
            if (this.copyAt != null) {
                return;
            }
            this.env = this.memberEnter.getInitEnv(tree, this.env);
            this.copyAt = tree;
        }

        public void visitBlock(JCTree.JCBlock tree) {
            if (this.copyAt != null) {
                return;
            }
            this.copyAt = tree;
        }

        public void visitTree(JCTree that) {
        }
    }

    public Map<JCTree, JCTree> resolveMethodMember(JavacNode node) throws IllegalAccessException, IllegalArgumentException {
        ArrayDeque<JCTree> stack = new ArrayDeque<>();
        JavacNode javacNodeUp = node;
        while (true) {
            JavacNode n = javacNodeUp;
            if (n == null) {
                break;
            }
            stack.push(n.get());
            javacNodeUp = n.up();
        }
        this.messageSuppressor.disableLoggers();
        try {
            EnvFinder finder = new EnvFinder(node.getContext());
            while (!stack.isEmpty()) {
                stack.pop().accept(finder);
            }
            TreeMirrorMaker mirrorMaker = new TreeMirrorMaker(node.getTreeMaker(), node.getContext());
            JCTree copy = mirrorMaker.copy((TreeMirrorMaker) finder.copyAt());
            Log log = Log.instance(node.getContext());
            JavaFileObject oldFileObject = log.useSource(node.top().get().getSourceFile());
            try {
                memberEnterAndAttribute(copy, finder.get(), node.getContext());
                Map<JCTree, JCTree> originalToCopyMap = mirrorMaker.getOriginalToCopyMap();
                log.useSource(oldFileObject);
                this.messageSuppressor.enableLoggers();
                return originalToCopyMap;
            } catch (Throwable th) {
                log.useSource(oldFileObject);
                throw th;
            }
        } catch (Throwable th2) {
            this.messageSuppressor.enableLoggers();
            throw th2;
        }
    }

    private static Field getMemberEnterDotEnv() throws NoSuchFieldException {
        if (memberEnterDotEnv != null) {
            return memberEnterDotEnv;
        }
        try {
            Field f = MemberEnter.class.getDeclaredField("env");
            f.setAccessible(true);
            memberEnterDotEnv = f;
            return memberEnterDotEnv;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private static Env<AttrContext> getEnvOfMemberEnter(MemberEnter memberEnter) throws NoSuchFieldException {
        Field f = getMemberEnterDotEnv();
        try {
            return (Env) f.get(memberEnter);
        } catch (Exception e) {
            return null;
        }
    }

    private static void setEnvOfMemberEnter(MemberEnter memberEnter, Env<AttrContext> env) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        Field f = getMemberEnterDotEnv();
        try {
            f.set(memberEnter, env);
        } catch (Exception e) {
        }
    }

    private void memberEnterAndAttribute(JCTree copy, Env<AttrContext> env, Context context) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        MemberEnter memberEnter = MemberEnter.instance(context);
        Env<AttrContext> oldEnv = getEnvOfMemberEnter(memberEnter);
        setEnvOfMemberEnter(memberEnter, env);
        try {
            try {
                copy.accept(memberEnter);
                setEnvOfMemberEnter(memberEnter, oldEnv);
            } catch (Exception ignore) {
                AssertionLogger.assertLog("member enter failed.", ignore);
                setEnvOfMemberEnter(memberEnter, oldEnv);
            }
            attrib(copy, env);
        } catch (Throwable th) {
            setEnvOfMemberEnter(memberEnter, oldEnv);
            throw th;
        }
    }

    public void resolveClassMember(JavacNode node) throws IllegalAccessException, IllegalArgumentException {
        ArrayDeque<JCTree> stack = new ArrayDeque<>();
        JavacNode javacNodeUp = node;
        while (true) {
            JavacNode n = javacNodeUp;
            if (n == null) {
                break;
            }
            stack.push(n.get());
            javacNodeUp = n.up();
        }
        this.messageSuppressor.disableLoggers();
        try {
            EnvFinder finder = new EnvFinder(node.getContext());
            while (!stack.isEmpty()) {
                stack.pop().accept(finder);
            }
            attrib(node.get(), finder.get());
            this.messageSuppressor.enableLoggers();
        } catch (Throwable th) {
            this.messageSuppressor.enableLoggers();
            throw th;
        }
    }

    private void attrib(JCTree tree, Env<AttrContext> env) {
        if (env.enclClass.type == null) {
            try {
                env.enclClass.type = Type.noType;
            } catch (Throwable th) {
            }
        }
        if (!(tree instanceof JCTree.JCBlock)) {
            if (!(tree instanceof JCTree.JCMethodDecl)) {
                if (!(tree instanceof JCTree.JCVariableDecl)) {
                    throw new IllegalStateException("Called with something that isn't a block, method decl, or variable decl");
                }
                this.attr.attribStat(tree, env);
                return;
            }
            this.attr.attribStat(((JCTree.JCMethodDecl) tree).body, env);
            return;
        }
        this.attr.attribStat(tree, env);
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/JavacResolution$TypeNotConvertibleException.SCL.lombok */
    public static class TypeNotConvertibleException extends Exception {
        public TypeNotConvertibleException(String msg) {
            super(msg);
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/JavacResolution$ReflectiveAccess.SCL.lombok */
    private static class ReflectiveAccess {
        private static Method UPPER_BOUND;

        private ReflectiveAccess() {
        }

        static {
            Method upperBound = null;
            try {
                upperBound = Types.class.getMethod("upperBound", Type.class);
            } catch (Throwable th) {
            }
            if (upperBound == null) {
                try {
                    upperBound = Types.class.getMethod("wildUpperBound", Type.class);
                } catch (Throwable th2) {
                }
            }
            UPPER_BOUND = upperBound;
        }

        public static Type Types_upperBound(Types types, Type type) {
            try {
                return (Type) UPPER_BOUND.invoke(types, type);
            } catch (InvocationTargetException e) {
                throw Lombok.sneakyThrow(e.getCause());
            } catch (Exception e2) {
                throw Lombok.sneakyThrow(e2);
            }
        }
    }

    public static Type ifTypeIsIterableToComponent(Type type, JavacAST ast) {
        if (type == null) {
            return null;
        }
        Types types = Types.instance(ast.getContext());
        Symtab syms = Symtab.instance(ast.getContext());
        Type boundType = ReflectiveAccess.Types_upperBound(types, type);
        Type elemTypeIfArray = types.elemtype(boundType);
        if (elemTypeIfArray != null) {
            return elemTypeIfArray;
        }
        Type base = types.asSuper(boundType, syms.iterableType.tsym);
        if (base == null) {
            return syms.objectType;
        }
        List<Type> iterableParams = base.allparams();
        return iterableParams.isEmpty() ? syms.objectType : ReflectiveAccess.Types_upperBound(types, (Type) iterableParams.head);
    }

    public static JCTree.JCExpression typeToJCTree(Type type, JavacAST ast, boolean allowVoid) throws TypeNotConvertibleException {
        return typeToJCTree(type, ast, false, allowVoid);
    }

    public static JCTree.JCExpression createJavaLangObject(JavacAST ast) {
        JavacTreeMaker maker = ast.getTreeMaker();
        return maker.Select(maker.Select(maker.Ident(ast.toName("java")), ast.toName(AbstractHtmlElementTag.LANG_ATTRIBUTE)), ast.toName("Object"));
    }

    private static JCTree.JCExpression typeToJCTree(Type type, JavacAST ast, boolean allowCompound, boolean allowVoid) throws TypeNotConvertibleException {
        Type type0;
        int dims = 0;
        Type type2 = type;
        while (true) {
            type0 = type2;
            if (!(type0 instanceof Type.ArrayType)) {
                break;
            }
            dims++;
            type2 = ((Type.ArrayType) type0).elemtype;
        }
        JCTree.JCArrayTypeTree jCArrayTypeTreeTypeToJCTree0 = typeToJCTree0(type0, ast, allowCompound, allowVoid);
        while (dims > 0) {
            jCArrayTypeTreeTypeToJCTree0 = ast.getTreeMaker().TypeArray(jCArrayTypeTreeTypeToJCTree0);
            dims--;
        }
        return jCArrayTypeTreeTypeToJCTree0;
    }

    private static JCTree.JCExpression typeToJCTree0(Type type, JavacAST ast, boolean allowCompound, boolean allowVoid) throws TypeNotConvertibleException {
        Type lower;
        Type upper;
        String qName;
        JavacTreeMaker maker = ast.getTreeMaker();
        if (Javac.CTC_BOT.equals(JavacTreeMaker.TypeTag.typeTag(type))) {
            return createJavaLangObject(ast);
        }
        if (Javac.CTC_VOID.equals(JavacTreeMaker.TypeTag.typeTag(type))) {
            return allowVoid ? primitiveToJCTree(type.getKind(), maker) : createJavaLangObject(ast);
        }
        if (type.isPrimitive()) {
            return primitiveToJCTree(type.getKind(), maker);
        }
        if (type.isErroneous()) {
            throw new TypeNotConvertibleException("Type cannot be resolved");
        }
        Symbol.TypeSymbol symbol = type.asElement();
        List<Type> generics = type.getTypeArguments();
        JCTree.JCIdent jCIdentTypeToJCTree0 = null;
        if (symbol == null) {
            throw new TypeNotConvertibleException("Null or compound type");
        }
        if (symbol.name.length() == 0) {
            if (type instanceof Type.ClassType) {
                List<Type> ifaces = ((Type.ClassType) type).interfaces_field;
                Type supertype = ((Type.ClassType) type).supertype_field;
                if (ifaces != null && ifaces.length() == 1) {
                    return typeToJCTree((Type) ifaces.get(0), ast, allowCompound, allowVoid);
                }
                if (supertype != null) {
                    return typeToJCTree(supertype, ast, allowCompound, allowVoid);
                }
            }
            throw new TypeNotConvertibleException("Anonymous inner class");
        }
        if ((type instanceof Type.CapturedType) || (type instanceof Type.WildcardType)) {
            if (type instanceof Type.WildcardType) {
                upper = ((Type.WildcardType) type).getExtendsBound();
                lower = ((Type.WildcardType) type).getSuperBound();
            } else {
                lower = type.getLowerBound();
                upper = type.getUpperBound();
            }
            if (allowCompound) {
                if (lower == null || Javac.CTC_BOT.equals(JavacTreeMaker.TypeTag.typeTag(lower))) {
                    if (upper == null || upper.toString().equals("java.lang.Object")) {
                        return maker.Wildcard(maker.TypeBoundKind(BoundKind.UNBOUND), null);
                    }
                    if (upper.getTypeArguments().contains(type)) {
                        return maker.Wildcard(maker.TypeBoundKind(BoundKind.UNBOUND), null);
                    }
                    return maker.Wildcard(maker.TypeBoundKind(BoundKind.EXTENDS), typeToJCTree(upper, ast, false, false));
                }
                return maker.Wildcard(maker.TypeBoundKind(BoundKind.SUPER), typeToJCTree(lower, ast, false, false));
            }
            if (upper != null) {
                if (upper.getTypeArguments().contains(type)) {
                    return maker.Wildcard(maker.TypeBoundKind(BoundKind.UNBOUND), null);
                }
                return typeToJCTree(upper, ast, allowCompound, allowVoid);
            }
            return createJavaLangObject(ast);
        }
        if (symbol.isLocal()) {
            qName = symbol.getSimpleName().toString();
        } else if (symbol.type != null && symbol.type.getEnclosingType() != null && JavacTreeMaker.TypeTag.typeTag(symbol.type.getEnclosingType()).equals(JavacTreeMaker.TypeTag.typeTag("CLASS"))) {
            jCIdentTypeToJCTree0 = typeToJCTree0(type.getEnclosingType(), ast, false, false);
            qName = symbol.getSimpleName().toString();
        } else {
            qName = symbol.getQualifiedName().toString();
        }
        if (qName.isEmpty()) {
            throw new TypeNotConvertibleException("unknown type");
        }
        if (qName.startsWith("<")) {
            throw new TypeNotConvertibleException(qName);
        }
        String[] baseNames = qName.split("\\.");
        int i = 0;
        if (jCIdentTypeToJCTree0 == null) {
            jCIdentTypeToJCTree0 = maker.Ident(ast.toName(baseNames[0]));
            i = 1;
        }
        while (i < baseNames.length) {
            jCIdentTypeToJCTree0 = maker.Select(jCIdentTypeToJCTree0, ast.toName(baseNames[i]));
            i++;
        }
        return genericsToJCTreeNodes(generics, ast, jCIdentTypeToJCTree0);
    }

    private static JCTree.JCExpression genericsToJCTreeNodes(List<Type> generics, JavacAST ast, JCTree.JCExpression rawTypeNode) throws TypeNotConvertibleException {
        if (generics != null && !generics.isEmpty()) {
            ListBuffer<JCTree.JCExpression> args = new ListBuffer<>();
            Iterator it = generics.iterator();
            while (it.hasNext()) {
                Type t = (Type) it.next();
                args.append(typeToJCTree(t, ast, true, false));
            }
            return ast.getTreeMaker().TypeApply(rawTypeNode, args.toList());
        }
        return rawTypeNode;
    }

    /* renamed from: lombok.javac.JavacResolution$1, reason: invalid class name */
    /* loaded from: lombok-1.16.22.jar:lombok/javac/JavacResolution$1.SCL.lombok */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$javax$lang$model$type$TypeKind = new int[TypeKind.values().length];

        static {
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.BYTE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.CHAR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.SHORT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.INT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.LONG.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.FLOAT.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.DOUBLE.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.BOOLEAN.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.VOID.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.NULL.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.NONE.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.OTHER.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
        }
    }

    private static JCTree.JCExpression primitiveToJCTree(TypeKind kind, JavacTreeMaker maker) throws TypeNotConvertibleException {
        switch (AnonymousClass1.$SwitchMap$javax$lang$model$type$TypeKind[kind.ordinal()]) {
            case 1:
                return maker.TypeIdent(Javac.CTC_BYTE);
            case 2:
                return maker.TypeIdent(Javac.CTC_CHAR);
            case 3:
                return maker.TypeIdent(Javac.CTC_SHORT);
            case 4:
                return maker.TypeIdent(Javac.CTC_INT);
            case 5:
                return maker.TypeIdent(Javac.CTC_LONG);
            case 6:
                return maker.TypeIdent(Javac.CTC_FLOAT);
            case 7:
                return maker.TypeIdent(Javac.CTC_DOUBLE);
            case 8:
                return maker.TypeIdent(Javac.CTC_BOOLEAN);
            case 9:
                return maker.TypeIdent(Javac.CTC_VOID);
            case 10:
            case 11:
            case 12:
            default:
                throw new TypeNotConvertibleException("Nulltype");
        }
    }

    public static boolean platformHasTargetTyping() {
        return Javac.getJavaCompilerVersion() >= 8;
    }
}
