package lombok.javac.handlers.singulars;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import lombok.core.LombokImmutableList;
import lombok.core.handlers.HandlerUtil;
import lombok.javac.Javac;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.JavacHandlerUtil;
import lombok.javac.handlers.JavacSingularsRecipes;
import org.apache.ibatis.ognl.OgnlContext;

/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/singulars/JavacJavaUtilMapSingularizer.SCL.lombok */
public class JavacJavaUtilMapSingularizer extends JavacJavaUtilSingularizer {
    @Override // lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public LombokImmutableList<String> getSupportedTypes() {
        return LombokImmutableList.of("java.util.Map", "java.util.SortedMap", "java.util.NavigableMap");
    }

    @Override // lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public List<Name> listFieldsToBeGenerated(JavacSingularsRecipes.SingularData data, JavacNode builderType) {
        if (useGuavaInstead(builderType)) {
            return this.guavaMapSingularizer.listFieldsToBeGenerated(data, builderType);
        }
        String p = data.getPluralName().toString();
        return Arrays.asList(builderType.toName(p + "$key"), builderType.toName(p + "$value"));
    }

    @Override // lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public List<Name> listMethodsToBeGenerated(JavacSingularsRecipes.SingularData data, JavacNode builderType) {
        if (useGuavaInstead(builderType)) {
            return this.guavaMapSingularizer.listMethodsToBeGenerated(data, builderType);
        }
        return super.listMethodsToBeGenerated(data, builderType);
    }

    @Override // lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public List<JavacNode> generateFields(JavacSingularsRecipes.SingularData data, JavacNode builderType, JCTree source) {
        if (useGuavaInstead(builderType)) {
            return this.guavaMapSingularizer.generateFields(data, builderType, source);
        }
        JavacTreeMaker maker = builderType.getTreeMaker();
        JCTree.JCExpression type = JavacHandlerUtil.chainDots(builderType, "java", "util", "ArrayList");
        JCTree.JCVariableDecl buildKeyField = maker.VarDef(maker.Modifiers(2L), builderType.toName(data.getPluralName() + "$key"), addTypeArgs(1, false, builderType, type, data.getTypeArgs(), source), null);
        JCTree.JCExpression type2 = JavacHandlerUtil.chainDots(builderType, "java", "util", "ArrayList");
        com.sun.tools.javac.util.List<JCTree.JCExpression> tArgs = data.getTypeArgs();
        JCTree.JCVariableDecl buildValueField = maker.VarDef(maker.Modifiers(2L), builderType.toName(data.getPluralName() + "$value"), addTypeArgs(1, false, builderType, type2, (tArgs == null || tArgs.size() <= 1) ? com.sun.tools.javac.util.List.nil() : tArgs.tail, source), null);
        JavacNode valueFieldNode = JavacHandlerUtil.injectFieldAndMarkGenerated(builderType, buildValueField);
        JavacNode keyFieldNode = JavacHandlerUtil.injectFieldAndMarkGenerated(builderType, buildKeyField);
        return Arrays.asList(keyFieldNode, valueFieldNode);
    }

    @Override // lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public void generateMethods(JavacSingularsRecipes.SingularData data, boolean deprecate, JavacNode builderType, JCTree source, boolean fluent, boolean chain) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (useGuavaInstead(builderType)) {
            this.guavaMapSingularizer.generateMethods(data, deprecate, builderType, source, fluent, chain);
            return;
        }
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
        com.sun.tools.javac.util.List<JCTree.JCExpression> jceBlank = com.sun.tools.javac.util.List.nil();
        JCTree.JCExpression thisDotKeyField = JavacHandlerUtil.chainDots(builderType, OgnlContext.THIS_CONTEXT_KEY, data.getPluralName() + "$key", new String[0]);
        JCTree.JCExpression thisDotKeyFieldDotClear = JavacHandlerUtil.chainDots(builderType, OgnlContext.THIS_CONTEXT_KEY, data.getPluralName() + "$key", "clear");
        JCTree.JCExpression thisDotValueFieldDotClear = JavacHandlerUtil.chainDots(builderType, OgnlContext.THIS_CONTEXT_KEY, data.getPluralName() + "$value", "clear");
        JCTree.JCExpressionStatement jCExpressionStatementExec = maker.Exec(maker.Apply(jceBlank, thisDotKeyFieldDotClear, jceBlank));
        JCTree.JCExpressionStatement jCExpressionStatementExec2 = maker.Exec(maker.Apply(jceBlank, thisDotValueFieldDotClear, jceBlank));
        JCTree.JCBinary jCBinaryBinary = maker.Binary(Javac.CTC_NOT_EQUAL, thisDotKeyField, maker.Literal(Javac.CTC_BOT, null));
        JCTree.JCBlock clearCalls = maker.Block(0L, com.sun.tools.javac.util.List.of(jCExpressionStatementExec, jCExpressionStatementExec2));
        JCTree.JCIf jCIfIf = maker.If(jCBinaryBinary, clearCalls, null);
        com.sun.tools.javac.util.List<JCTree.JCStatement> statements = returnStatement != null ? com.sun.tools.javac.util.List.of(jCIfIf, returnStatement) : com.sun.tools.javac.util.List.of(jCIfIf);
        JCTree.JCBlock body = maker.Block(0L, statements);
        Name methodName = builderType.toName(HandlerUtil.buildAccessorName("clear", data.getPluralName().toString()));
        JCTree.JCMethodDecl method = maker.MethodDef(mods, methodName, returnType, typeParams, params, thrown, body, null);
        JavacHandlerUtil.injectMethod(builderType, method);
    }

    private void generateSingularMethod(boolean deprecate, JavacTreeMaker maker, JCTree.JCExpression returnType, JCTree.JCStatement returnStatement, JavacSingularsRecipes.SingularData data, JavacNode builderType, JCTree source, boolean fluent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        com.sun.tools.javac.util.List<JCTree.JCTypeParameter> typeParams = com.sun.tools.javac.util.List.nil();
        com.sun.tools.javac.util.List<JCTree.JCExpression> thrown = com.sun.tools.javac.util.List.nil();
        JCTree.JCModifiers mods = makeMods(maker, builderType, deprecate);
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        statements.append(createConstructBuilderVarIfNeeded(maker, data, builderType, true, source));
        Name keyName = builderType.toName(data.getSingularName().toString() + "Key");
        Name valueName = builderType.toName(data.getSingularName().toString() + "Value");
        JCTree.JCExpression thisDotKeyFieldDotAdd = JavacHandlerUtil.chainDots(builderType, OgnlContext.THIS_CONTEXT_KEY, data.getPluralName() + "$key", BeanUtil.PREFIX_ADDER);
        statements.append(maker.Exec(maker.Apply(com.sun.tools.javac.util.List.nil(), thisDotKeyFieldDotAdd, com.sun.tools.javac.util.List.of(maker.Ident(keyName)))));
        JCTree.JCExpression thisDotValueFieldDotAdd = JavacHandlerUtil.chainDots(builderType, OgnlContext.THIS_CONTEXT_KEY, data.getPluralName() + "$value", BeanUtil.PREFIX_ADDER);
        statements.append(maker.Exec(maker.Apply(com.sun.tools.javac.util.List.nil(), thisDotValueFieldDotAdd, com.sun.tools.javac.util.List.of(maker.Ident(valueName)))));
        if (returnStatement != null) {
            statements.append(returnStatement);
        }
        JCTree.JCBlock body = maker.Block(0L, statements.toList());
        long paramFlags = JavacHandlerUtil.addFinalIfNeeded(8589934592L, builderType.getContext());
        Name name = data.getSingularName();
        if (!fluent) {
            name = builderType.toName(HandlerUtil.buildAccessorName("put", name.toString()));
        }
        JCTree.JCExpression paramTypeKey = cloneParamType(0, maker, data.getTypeArgs(), builderType, source);
        JCTree.JCExpression paramTypeValue = cloneParamType(1, maker, data.getTypeArgs(), builderType, source);
        JCTree.JCVariableDecl paramKey = maker.VarDef(maker.Modifiers(paramFlags), keyName, paramTypeKey, null);
        JCTree.JCVariableDecl paramValue = maker.VarDef(maker.Modifiers(paramFlags), valueName, paramTypeValue, null);
        JCTree.JCMethodDecl method = maker.MethodDef(mods, name, returnType, typeParams, com.sun.tools.javac.util.List.of(paramKey, paramValue), thrown, body, null);
        JavacHandlerUtil.injectMethod(builderType, method);
    }

    private void generatePluralMethod(boolean deprecate, JavacTreeMaker maker, JCTree.JCExpression returnType, JCTree.JCStatement returnStatement, JavacSingularsRecipes.SingularData data, JavacNode builderType, JCTree source, boolean fluent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        com.sun.tools.javac.util.List<JCTree.JCTypeParameter> typeParams = com.sun.tools.javac.util.List.nil();
        com.sun.tools.javac.util.List<JCTree.JCExpression> jceBlank = com.sun.tools.javac.util.List.nil();
        JCTree.JCModifiers mods = makeMods(maker, builderType, deprecate);
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        statements.append(createConstructBuilderVarIfNeeded(maker, data, builderType, true, source));
        long paramFlags = JavacHandlerUtil.addFinalIfNeeded(8589934592L, builderType.getContext());
        long baseFlags = JavacHandlerUtil.addFinalIfNeeded(0L, builderType.getContext());
        Name entryName = builderType.toName("$lombokEntry");
        JCTree.JCExpression forEachType = JavacHandlerUtil.chainDots(builderType, "java", "util", "Map", "Entry");
        JCTree.JCExpression forEachType2 = addTypeArgs(2, true, builderType, forEachType, data.getTypeArgs(), source);
        JCTree.JCBlock forEachBody = maker.Block(0L, com.sun.tools.javac.util.List.of(maker.Exec(maker.Apply(com.sun.tools.javac.util.List.nil(), JavacHandlerUtil.chainDots(builderType, OgnlContext.THIS_CONTEXT_KEY, data.getPluralName() + "$key", BeanUtil.PREFIX_ADDER), com.sun.tools.javac.util.List.of(maker.Apply(com.sun.tools.javac.util.List.nil(), maker.Select(maker.Ident(entryName), builderType.toName("getKey")), com.sun.tools.javac.util.List.nil())))), maker.Exec(maker.Apply(com.sun.tools.javac.util.List.nil(), JavacHandlerUtil.chainDots(builderType, OgnlContext.THIS_CONTEXT_KEY, data.getPluralName() + "$value", BeanUtil.PREFIX_ADDER), com.sun.tools.javac.util.List.of(maker.Apply(com.sun.tools.javac.util.List.nil(), maker.Select(maker.Ident(entryName), builderType.toName("getValue")), com.sun.tools.javac.util.List.nil()))))));
        statements.append(maker.ForeachLoop(maker.VarDef(maker.Modifiers(baseFlags), entryName, forEachType2, null), maker.Apply(jceBlank, maker.Select(maker.Ident(data.getPluralName()), builderType.toName("entrySet")), jceBlank), forEachBody));
        if (returnStatement != null) {
            statements.append(returnStatement);
        }
        JCTree.JCBlock body = maker.Block(0L, statements.toList());
        Name name = data.getPluralName();
        if (!fluent) {
            name = builderType.toName(HandlerUtil.buildAccessorName("putAll", name.toString()));
        }
        JCTree.JCExpression paramType = JavacHandlerUtil.chainDots(builderType, "java", "util", "Map");
        JCTree.JCVariableDecl param = maker.VarDef(maker.Modifiers(paramFlags), data.getPluralName(), addTypeArgs(2, true, builderType, paramType, data.getTypeArgs(), source), null);
        JCTree.JCMethodDecl method = maker.MethodDef(mods, name, returnType, typeParams, com.sun.tools.javac.util.List.of(param), jceBlank, body, null);
        JavacHandlerUtil.injectMethod(builderType, method);
    }

    @Override // lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public void appendBuildCode(JavacSingularsRecipes.SingularData data, JavacNode builderType, JCTree source, ListBuffer<JCTree.JCStatement> statements, Name targetVariableName) {
        if (useGuavaInstead(builderType)) {
            this.guavaMapSingularizer.appendBuildCode(data, builderType, source, statements, targetVariableName);
            return;
        }
        JavacTreeMaker maker = builderType.getTreeMaker();
        if (data.getTargetFqn().equals("java.util.Map")) {
            statements.appendList(createJavaUtilSetMapInitialCapacitySwitchStatements(maker, data, builderType, true, "emptyMap", "singletonMap", "LinkedHashMap", source));
        } else {
            statements.appendList(createJavaUtilSimpleCreationAndFillStatements(maker, data, builderType, true, true, false, true, "TreeMap", source));
        }
    }
}
