package lombok.javac.handlers.singulars;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import lombok.core.handlers.HandlerUtil;
import lombok.javac.Javac;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.JavacHandlerUtil;
import lombok.javac.handlers.JavacSingularsRecipes;
import org.apache.ibatis.ognl.OgnlContext;

/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/singulars/JavacJavaUtilListSetSingularizer.SCL.lombok */
abstract class JavacJavaUtilListSetSingularizer extends JavacJavaUtilSingularizer {
    JavacJavaUtilListSetSingularizer() {
    }

    @Override // lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public List<Name> listFieldsToBeGenerated(JavacSingularsRecipes.SingularData data, JavacNode builderType) {
        if (useGuavaInstead(builderType)) {
            return this.guavaListSetSingularizer.listFieldsToBeGenerated(data, builderType);
        }
        return super.listFieldsToBeGenerated(data, builderType);
    }

    @Override // lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public List<Name> listMethodsToBeGenerated(JavacSingularsRecipes.SingularData data, JavacNode builderType) {
        if (useGuavaInstead(builderType)) {
            return this.guavaListSetSingularizer.listMethodsToBeGenerated(data, builderType);
        }
        return super.listMethodsToBeGenerated(data, builderType);
    }

    @Override // lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public List<JavacNode> generateFields(JavacSingularsRecipes.SingularData data, JavacNode builderType, JCTree source) {
        if (useGuavaInstead(builderType)) {
            return this.guavaListSetSingularizer.generateFields(data, builderType, source);
        }
        JavacTreeMaker maker = builderType.getTreeMaker();
        JCTree.JCExpression type = JavacHandlerUtil.chainDots(builderType, "java", "util", "ArrayList");
        JCTree.JCVariableDecl buildField = maker.VarDef(maker.Modifiers(2L), data.getPluralName(), addTypeArgs(1, false, builderType, type, data.getTypeArgs(), source), null);
        return Collections.singletonList(JavacHandlerUtil.injectFieldAndMarkGenerated(builderType, buildField));
    }

    @Override // lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public void generateMethods(JavacSingularsRecipes.SingularData data, boolean deprecate, JavacNode builderType, JCTree source, boolean fluent, boolean chain) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (useGuavaInstead(builderType)) {
            this.guavaListSetSingularizer.generateMethods(data, deprecate, builderType, source, fluent, chain);
            return;
        }
        JavacTreeMaker maker = builderType.getTreeMaker();
        Symtab symbolTable = builderType.getSymbolTable();
        Name thisName = builderType.toName(OgnlContext.THIS_CONTEXT_KEY);
        JCTree.JCExpression returnType = chain ? JavacHandlerUtil.cloneSelfType(builderType) : maker.Type(Javac.createVoidType(symbolTable, Javac.CTC_VOID));
        generateSingularMethod(deprecate, maker, returnType, chain ? maker.Return(maker.Ident(thisName)) : null, data, builderType, source, fluent);
        JCTree.JCExpression returnType2 = chain ? JavacHandlerUtil.cloneSelfType(builderType) : maker.Type(Javac.createVoidType(symbolTable, Javac.CTC_VOID));
        generatePluralMethod(deprecate, maker, returnType2, chain ? maker.Return(maker.Ident(thisName)) : null, data, builderType, source, fluent);
        JCTree.JCExpression returnType3 = chain ? JavacHandlerUtil.cloneSelfType(builderType) : maker.Type(Javac.createVoidType(symbolTable, Javac.CTC_VOID));
        generateClearMethod(deprecate, maker, returnType3, chain ? maker.Return(maker.Ident(thisName)) : null, data, builderType, source);
    }

    private void generateClearMethod(boolean deprecate, JavacTreeMaker maker, JCTree.JCExpression returnType, JCTree.JCStatement returnStatement, JavacSingularsRecipes.SingularData data, JavacNode builderType, JCTree source) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JCTree.JCModifiers mods = makeMods(maker, builderType, deprecate);
        com.sun.tools.javac.util.List<JCTree.JCTypeParameter> typeParams = com.sun.tools.javac.util.List.nil();
        com.sun.tools.javac.util.List<JCTree.JCExpression> thrown = com.sun.tools.javac.util.List.nil();
        com.sun.tools.javac.util.List<JCTree.JCVariableDecl> params = com.sun.tools.javac.util.List.nil();
        com.sun.tools.javac.util.List<JCTree.JCExpression> jceBlank = com.sun.tools.javac.util.List.nil();
        JCTree.JCIf jCIfIf = maker.If(maker.Binary(Javac.CTC_NOT_EQUAL, maker.Select(maker.Ident(builderType.toName(OgnlContext.THIS_CONTEXT_KEY)), data.getPluralName()), maker.Literal(Javac.CTC_BOT, null)), maker.Exec(maker.Apply(jceBlank, maker.Select(maker.Select(maker.Ident(builderType.toName(OgnlContext.THIS_CONTEXT_KEY)), data.getPluralName()), builderType.toName("clear")), jceBlank)), null);
        com.sun.tools.javac.util.List<JCTree.JCStatement> statements = returnStatement != null ? com.sun.tools.javac.util.List.of(jCIfIf, returnStatement) : com.sun.tools.javac.util.List.of(jCIfIf);
        JCTree.JCBlock body = maker.Block(0L, statements);
        Name methodName = builderType.toName(HandlerUtil.buildAccessorName("clear", data.getPluralName().toString()));
        JCTree.JCMethodDecl method = maker.MethodDef(mods, methodName, returnType, typeParams, params, thrown, body, null);
        JavacHandlerUtil.injectMethod(builderType, method);
    }

    void generateSingularMethod(boolean deprecate, JavacTreeMaker maker, JCTree.JCExpression returnType, JCTree.JCStatement returnStatement, JavacSingularsRecipes.SingularData data, JavacNode builderType, JCTree source, boolean fluent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        com.sun.tools.javac.util.List<JCTree.JCTypeParameter> typeParams = com.sun.tools.javac.util.List.nil();
        com.sun.tools.javac.util.List<JCTree.JCExpression> thrown = com.sun.tools.javac.util.List.nil();
        JCTree.JCModifiers mods = makeMods(maker, builderType, deprecate);
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        statements.append(createConstructBuilderVarIfNeeded(maker, data, builderType, false, source));
        JCTree.JCExpression thisDotFieldDotAdd = JavacHandlerUtil.chainDots(builderType, OgnlContext.THIS_CONTEXT_KEY, data.getPluralName().toString(), BeanUtil.PREFIX_ADDER);
        statements.append(maker.Exec(maker.Apply(com.sun.tools.javac.util.List.nil(), thisDotFieldDotAdd, com.sun.tools.javac.util.List.of(maker.Ident(data.getSingularName())))));
        if (returnStatement != null) {
            statements.append(returnStatement);
        }
        JCTree.JCBlock body = maker.Block(0L, statements.toList());
        Name name = data.getSingularName();
        long paramFlags = JavacHandlerUtil.addFinalIfNeeded(8589934592L, builderType.getContext());
        if (!fluent) {
            name = builderType.toName(HandlerUtil.buildAccessorName(BeanUtil.PREFIX_ADDER, name.toString()));
        }
        JCTree.JCExpression paramType = cloneParamType(0, maker, data.getTypeArgs(), builderType, source);
        JCTree.JCVariableDecl param = maker.VarDef(maker.Modifiers(paramFlags), data.getSingularName(), paramType, null);
        JCTree.JCMethodDecl method = maker.MethodDef(mods, name, returnType, typeParams, com.sun.tools.javac.util.List.of(param), thrown, body, null);
        JavacHandlerUtil.injectMethod(builderType, method);
    }

    void generatePluralMethod(boolean deprecate, JavacTreeMaker maker, JCTree.JCExpression returnType, JCTree.JCStatement returnStatement, JavacSingularsRecipes.SingularData data, JavacNode builderType, JCTree source, boolean fluent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        com.sun.tools.javac.util.List<JCTree.JCTypeParameter> typeParams = com.sun.tools.javac.util.List.nil();
        com.sun.tools.javac.util.List<JCTree.JCExpression> thrown = com.sun.tools.javac.util.List.nil();
        JCTree.JCModifiers mods = makeMods(maker, builderType, deprecate);
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        statements.append(createConstructBuilderVarIfNeeded(maker, data, builderType, false, source));
        JCTree.JCExpression thisDotFieldDotAdd = JavacHandlerUtil.chainDots(builderType, OgnlContext.THIS_CONTEXT_KEY, data.getPluralName().toString(), "addAll");
        statements.append(maker.Exec(maker.Apply(com.sun.tools.javac.util.List.nil(), thisDotFieldDotAdd, com.sun.tools.javac.util.List.of(maker.Ident(data.getPluralName())))));
        if (returnStatement != null) {
            statements.append(returnStatement);
        }
        JCTree.JCBlock body = maker.Block(0L, statements.toList());
        Name name = data.getPluralName();
        long paramFlags = JavacHandlerUtil.addFinalIfNeeded(8589934592L, builderType.getContext());
        if (!fluent) {
            name = builderType.toName(HandlerUtil.buildAccessorName("addAll", name.toString()));
        }
        JCTree.JCExpression paramType = JavacHandlerUtil.chainDots(builderType, "java", "util", "Collection");
        JCTree.JCVariableDecl param = maker.VarDef(maker.Modifiers(paramFlags), data.getPluralName(), addTypeArgs(1, true, builderType, paramType, data.getTypeArgs(), source), null);
        JCTree.JCMethodDecl method = maker.MethodDef(mods, name, returnType, typeParams, com.sun.tools.javac.util.List.of(param), thrown, body, null);
        JavacHandlerUtil.injectMethod(builderType, method);
    }
}
