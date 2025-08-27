package lombok.javac.handlers.singulars;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import lombok.core.GuavaTypeMap;
import lombok.core.LombokImmutableList;
import lombok.core.handlers.HandlerUtil;
import lombok.javac.Javac;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.JavacHandlerUtil;
import lombok.javac.handlers.JavacSingularsRecipes;
import org.apache.catalina.valves.Constants;
import org.apache.ibatis.ognl.OgnlContext;

/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/singulars/JavacGuavaSingularizer.SCL.lombok */
abstract class JavacGuavaSingularizer extends JavacSingularsRecipes.JavacSingularizer {
    protected abstract LombokImmutableList<String> getArgumentSuffixes();

    protected abstract String getAddMethodName();

    protected abstract String getAddAllTypeName();

    JavacGuavaSingularizer() {
    }

    protected String getSimpleTargetTypeName(JavacSingularsRecipes.SingularData data) {
        return GuavaTypeMap.getGuavaTypeName(data.getTargetFqn());
    }

    protected String getBuilderMethodName(JavacSingularsRecipes.SingularData data) {
        String simpleTypeName = getSimpleTargetTypeName(data);
        return ("ImmutableSortedSet".equals(simpleTypeName) || "ImmutableSortedMap".equals(simpleTypeName)) ? "naturalOrder" : "builder";
    }

    @Override // lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public List<JavacNode> generateFields(JavacSingularsRecipes.SingularData data, JavacNode builderType, JCTree source) {
        JavacTreeMaker maker = builderType.getTreeMaker();
        String simpleTypeName = getSimpleTargetTypeName(data);
        JCTree.JCExpression type = JavacHandlerUtil.chainDots(builderType, "com", "google", Constants.AccessLog.COMMON_ALIAS, "collect", simpleTypeName, "Builder");
        JCTree.JCVariableDecl buildField = maker.VarDef(maker.Modifiers(2L), data.getPluralName(), addTypeArgs(getTypeArgumentsCount(), false, builderType, type, data.getTypeArgs(), source), null);
        return Collections.singletonList(JavacHandlerUtil.injectFieldAndMarkGenerated(builderType, buildField));
    }

    @Override // lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public void generateMethods(JavacSingularsRecipes.SingularData data, boolean deprecate, JavacNode builderType, JCTree source, boolean fluent, boolean chain) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JavacTreeMaker maker = builderType.getTreeMaker();
        Symtab symbolTable = builderType.getSymbolTable();
        JCTree.JCExpression returnType = chain ? JavacHandlerUtil.cloneSelfType(builderType) : maker.Type(Javac.createVoidType(symbolTable, Javac.CTC_VOID));
        generateSingularMethod(deprecate, maker, returnType, chain ? maker.Return(maker.Ident(builderType.toName(OgnlContext.THIS_CONTEXT_KEY))) : null, data, builderType, source, fluent);
        JCTree.JCExpression returnType2 = chain ? JavacHandlerUtil.cloneSelfType(builderType) : maker.Type(Javac.createVoidType(symbolTable, Javac.CTC_VOID));
        generatePluralMethod(deprecate, maker, returnType2, chain ? maker.Return(maker.Ident(builderType.toName(OgnlContext.THIS_CONTEXT_KEY))) : null, data, builderType, source, fluent);
        JCTree.JCExpression returnType3 = chain ? JavacHandlerUtil.cloneSelfType(builderType) : maker.Type(Javac.createVoidType(symbolTable, Javac.CTC_VOID));
        generateClearMethod(deprecate, maker, returnType3, chain ? maker.Return(maker.Ident(builderType.toName(OgnlContext.THIS_CONTEXT_KEY))) : null, data, builderType, source);
    }

    private void generateClearMethod(boolean deprecate, JavacTreeMaker maker, JCTree.JCExpression returnType, JCTree.JCStatement returnStatement, JavacSingularsRecipes.SingularData data, JavacNode builderType, JCTree source) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JCTree.JCModifiers mods = makeMods(maker, builderType, deprecate);
        com.sun.tools.javac.util.List<JCTree.JCTypeParameter> typeParams = com.sun.tools.javac.util.List.nil();
        com.sun.tools.javac.util.List<JCTree.JCExpression> thrown = com.sun.tools.javac.util.List.nil();
        com.sun.tools.javac.util.List<JCTree.JCVariableDecl> params = com.sun.tools.javac.util.List.nil();
        JCTree.JCExpressionStatement jCExpressionStatementExec = maker.Exec(maker.Assign(maker.Select(maker.Ident(builderType.toName(OgnlContext.THIS_CONTEXT_KEY)), data.getPluralName()), maker.Literal(Javac.CTC_BOT, null)));
        com.sun.tools.javac.util.List<JCTree.JCStatement> statements = returnStatement != null ? com.sun.tools.javac.util.List.of(jCExpressionStatementExec, returnStatement) : com.sun.tools.javac.util.List.of(jCExpressionStatementExec);
        JCTree.JCBlock body = maker.Block(0L, statements);
        Name methodName = builderType.toName(HandlerUtil.buildAccessorName("clear", data.getPluralName().toString()));
        JCTree.JCMethodDecl method = maker.MethodDef(mods, methodName, returnType, typeParams, params, thrown, body, null);
        JavacHandlerUtil.injectMethod(builderType, method);
    }

    void generateSingularMethod(boolean deprecate, JavacTreeMaker maker, JCTree.JCExpression returnType, JCTree.JCStatement returnStatement, JavacSingularsRecipes.SingularData data, JavacNode builderType, JCTree source, boolean fluent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        com.sun.tools.javac.util.List<JCTree.JCTypeParameter> typeParams = com.sun.tools.javac.util.List.nil();
        com.sun.tools.javac.util.List<JCTree.JCExpression> thrown = com.sun.tools.javac.util.List.nil();
        LombokImmutableList<String> suffixes = getArgumentSuffixes();
        Name[] names = new Name[suffixes.size()];
        for (int i = 0; i < suffixes.size(); i++) {
            String s = suffixes.get(i);
            Name n = data.getSingularName();
            names[i] = s.isEmpty() ? n : builderType.toName(s);
        }
        JCTree.JCModifiers mods = makeMods(maker, builderType, deprecate);
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        statements.append(createConstructBuilderVarIfNeeded(maker, data, builderType, source));
        JCTree.JCExpression thisDotFieldDotAdd = JavacHandlerUtil.chainDots(builderType, OgnlContext.THIS_CONTEXT_KEY, data.getPluralName().toString(), getAddMethodName());
        ListBuffer<JCTree.JCExpression> invokeAddExprBuilder = new ListBuffer<>();
        for (int i2 = 0; i2 < suffixes.size(); i2++) {
            invokeAddExprBuilder.append(maker.Ident(names[i2]));
        }
        com.sun.tools.javac.util.List<JCTree.JCExpression> invokeAddExpr = invokeAddExprBuilder.toList();
        statements.append(maker.Exec(maker.Apply(com.sun.tools.javac.util.List.nil(), thisDotFieldDotAdd, invokeAddExpr)));
        if (returnStatement != null) {
            statements.append(returnStatement);
        }
        JCTree.JCBlock body = maker.Block(0L, statements.toList());
        Name methodName = data.getSingularName();
        long paramFlags = JavacHandlerUtil.addFinalIfNeeded(8589934592L, builderType.getContext());
        if (!fluent) {
            methodName = builderType.toName(HandlerUtil.buildAccessorName(getAddMethodName(), methodName.toString()));
        }
        ListBuffer<JCTree.JCVariableDecl> params = new ListBuffer<>();
        for (int i3 = 0; i3 < suffixes.size(); i3++) {
            JCTree.JCExpression pt = cloneParamType(i3, maker, data.getTypeArgs(), builderType, source);
            JCTree.JCVariableDecl p = maker.VarDef(maker.Modifiers(paramFlags), names[i3], pt, null);
            params.append(p);
        }
        JCTree.JCMethodDecl method = maker.MethodDef(mods, methodName, returnType, typeParams, params.toList(), thrown, body, null);
        JavacHandlerUtil.injectMethod(builderType, method);
    }

    protected void generatePluralMethod(boolean deprecate, JavacTreeMaker maker, JCTree.JCExpression returnType, JCTree.JCStatement returnStatement, JavacSingularsRecipes.SingularData data, JavacNode builderType, JCTree source, boolean fluent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JCTree.JCExpression paramType;
        com.sun.tools.javac.util.List<JCTree.JCTypeParameter> typeParams = com.sun.tools.javac.util.List.nil();
        com.sun.tools.javac.util.List<JCTree.JCExpression> thrown = com.sun.tools.javac.util.List.nil();
        JCTree.JCModifiers mods = makeMods(maker, builderType, deprecate);
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        statements.append(createConstructBuilderVarIfNeeded(maker, data, builderType, source));
        JCTree.JCExpression thisDotFieldDotAddAll = JavacHandlerUtil.chainDots(builderType, OgnlContext.THIS_CONTEXT_KEY, data.getPluralName().toString(), getAddMethodName() + "All");
        statements.append(maker.Exec(maker.Apply(com.sun.tools.javac.util.List.nil(), thisDotFieldDotAddAll, com.sun.tools.javac.util.List.of(maker.Ident(data.getPluralName())))));
        if (returnStatement != null) {
            statements.append(returnStatement);
        }
        JCTree.JCBlock body = maker.Block(0L, statements.toList());
        Name methodName = data.getPluralName();
        long paramFlags = JavacHandlerUtil.addFinalIfNeeded(8589934592L, builderType.getContext());
        if (!fluent) {
            methodName = builderType.toName(HandlerUtil.buildAccessorName(getAddMethodName() + "All", methodName.toString()));
        }
        String aaTypeName = getAddAllTypeName();
        if (aaTypeName.startsWith("java.lang.") && aaTypeName.indexOf(46, 11) == -1) {
            paramType = JavacHandlerUtil.genJavaLangTypeRef(builderType, aaTypeName.substring(10));
        } else {
            paramType = JavacHandlerUtil.chainDotsString(builderType, aaTypeName);
        }
        JCTree.JCVariableDecl param = maker.VarDef(maker.Modifiers(paramFlags), data.getPluralName(), addTypeArgs(getTypeArgumentsCount(), true, builderType, paramType, data.getTypeArgs(), source), null);
        JCTree.JCMethodDecl method = maker.MethodDef(mods, methodName, returnType, typeParams, com.sun.tools.javac.util.List.of(param), thrown, body, null);
        JavacHandlerUtil.injectMethod(builderType, method);
    }

    @Override // lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public void appendBuildCode(JavacSingularsRecipes.SingularData data, JavacNode builderType, JCTree source, ListBuffer<JCTree.JCStatement> statements, Name targetVariableName) {
        JavacTreeMaker maker = builderType.getTreeMaker();
        com.sun.tools.javac.util.List<JCTree.JCExpression> jceBlank = com.sun.tools.javac.util.List.nil();
        JCTree.JCExpression varType = JavacHandlerUtil.chainDotsString(builderType, data.getTargetFqn());
        int agrumentsCount = getTypeArgumentsCount();
        JCTree.JCExpression varType2 = addTypeArgs(agrumentsCount, false, builderType, varType, data.getTypeArgs(), source);
        JCTree.JCExpression emptyMethod = JavacHandlerUtil.chainDots(builderType, "com", "google", Constants.AccessLog.COMMON_ALIAS, "collect", getSimpleTargetTypeName(data), "of");
        com.sun.tools.javac.util.List<JCTree.JCExpression> invokeTypeArgs = createTypeArgs(agrumentsCount, false, builderType, data.getTypeArgs(), source);
        statements.append(maker.VarDef(maker.Modifiers(0L), data.getPluralName(), varType2, maker.Conditional(maker.Binary(Javac.CTC_EQUAL, maker.Select(maker.Ident(builderType.toName(OgnlContext.THIS_CONTEXT_KEY)), data.getPluralName()), maker.Literal(Javac.CTC_BOT, null)), maker.Apply(invokeTypeArgs, emptyMethod, jceBlank), maker.Apply(jceBlank, JavacHandlerUtil.chainDots(builderType, OgnlContext.THIS_CONTEXT_KEY, data.getPluralName().toString(), JsonPOJOBuilder.DEFAULT_BUILD_METHOD), jceBlank))));
    }

    protected JCTree.JCStatement createConstructBuilderVarIfNeeded(JavacTreeMaker maker, JavacSingularsRecipes.SingularData data, JavacNode builderType, JCTree source) {
        com.sun.tools.javac.util.List<JCTree.JCExpression> jceBlank = com.sun.tools.javac.util.List.nil();
        return maker.If(maker.Binary(Javac.CTC_EQUAL, maker.Select(maker.Ident(builderType.toName(OgnlContext.THIS_CONTEXT_KEY)), data.getPluralName()), maker.Literal(Javac.CTC_BOT, null)), maker.Exec(maker.Assign(maker.Select(maker.Ident(builderType.toName(OgnlContext.THIS_CONTEXT_KEY)), data.getPluralName()), maker.Apply(jceBlank, JavacHandlerUtil.chainDots(builderType, "com", "google", Constants.AccessLog.COMMON_ALIAS, "collect", getSimpleTargetTypeName(data), getBuilderMethodName(data)), jceBlank))), null);
    }

    protected int getTypeArgumentsCount() {
        return getArgumentSuffixes().size();
    }
}
