package lombok.javac.handlers.singulars;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import lombok.core.LombokImmutableList;
import lombok.javac.Javac;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.JavacHandlerUtil;
import lombok.javac.handlers.JavacSingularsRecipes;
import org.apache.ibatis.ognl.OgnlContext;

/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/singulars/JavacJavaUtilListSingularizer.SCL.lombok */
public class JavacJavaUtilListSingularizer extends JavacJavaUtilListSetSingularizer {
    @Override // lombok.javac.handlers.singulars.JavacJavaUtilListSetSingularizer, lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public /* bridge */ /* synthetic */ void generateMethods(JavacSingularsRecipes.SingularData singularData, boolean z, JavacNode javacNode, JCTree jCTree, boolean z2, boolean z3) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        super.generateMethods(singularData, z, javacNode, jCTree, z2, z3);
    }

    @Override // lombok.javac.handlers.singulars.JavacJavaUtilListSetSingularizer, lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public /* bridge */ /* synthetic */ List generateFields(JavacSingularsRecipes.SingularData singularData, JavacNode javacNode, JCTree jCTree) {
        return super.generateFields(singularData, javacNode, jCTree);
    }

    @Override // lombok.javac.handlers.singulars.JavacJavaUtilListSetSingularizer, lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public /* bridge */ /* synthetic */ List listMethodsToBeGenerated(JavacSingularsRecipes.SingularData singularData, JavacNode javacNode) {
        return super.listMethodsToBeGenerated(singularData, javacNode);
    }

    @Override // lombok.javac.handlers.singulars.JavacJavaUtilListSetSingularizer, lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public /* bridge */ /* synthetic */ List listFieldsToBeGenerated(JavacSingularsRecipes.SingularData singularData, JavacNode javacNode) {
        return super.listFieldsToBeGenerated(singularData, javacNode);
    }

    @Override // lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public LombokImmutableList<String> getSupportedTypes() {
        return LombokImmutableList.of("java.util.List", "java.util.Collection", "java.lang.Iterable");
    }

    @Override // lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public void appendBuildCode(JavacSingularsRecipes.SingularData data, JavacNode builderType, JCTree source, ListBuffer<JCTree.JCStatement> statements, Name targetVariableName) {
        if (useGuavaInstead(builderType)) {
            this.guavaListSetSingularizer.appendBuildCode(data, builderType, source, statements, targetVariableName);
            return;
        }
        JavacTreeMaker maker = builderType.getTreeMaker();
        com.sun.tools.javac.util.List<JCTree.JCExpression> jceBlank = com.sun.tools.javac.util.List.nil();
        ListBuffer<JCTree.JCCase> cases = new ListBuffer<>();
        JCTree.JCCase emptyCase = maker.Case(maker.Literal(Javac.CTC_INT, 0), com.sun.tools.javac.util.List.of(maker.Exec(maker.Assign(maker.Ident(data.getPluralName()), maker.Apply(jceBlank, JavacHandlerUtil.chainDots(builderType, "java", "util", "Collections", "emptyList"), jceBlank))), maker.Break(null)));
        cases.append(emptyCase);
        com.sun.tools.javac.util.List<JCTree.JCExpression> args = com.sun.tools.javac.util.List.of(maker.Apply(jceBlank, JavacHandlerUtil.chainDots(builderType, OgnlContext.THIS_CONTEXT_KEY, data.getPluralName().toString(), BeanUtil.PREFIX_GETTER_GET), com.sun.tools.javac.util.List.of(maker.Literal(Javac.CTC_INT, 0))));
        JCTree.JCCase singletonCase = maker.Case(maker.Literal(Javac.CTC_INT, 1), com.sun.tools.javac.util.List.of(maker.Exec(maker.Assign(maker.Ident(data.getPluralName()), maker.Apply(jceBlank, JavacHandlerUtil.chainDots(builderType, "java", "util", "Collections", "singletonList"), args))), maker.Break(null)));
        cases.append(singletonCase);
        com.sun.tools.javac.util.List<JCTree.JCStatement> defStats = createListCopy(maker, data, builderType, source);
        JCTree.JCCase defaultCase = maker.Case(null, defStats);
        cases.append(defaultCase);
        JCTree.JCSwitch jCSwitchSwitch = maker.Switch(getSize(maker, builderType, data.getPluralName(), true, false), cases.toList());
        JCTree.JCExpression localShadowerType = JavacHandlerUtil.chainDotsString(builderType, data.getTargetFqn());
        statements.append(maker.VarDef(maker.Modifiers(0L), data.getPluralName(), addTypeArgs(1, false, builderType, localShadowerType, data.getTypeArgs(), source), null));
        statements.append(jCSwitchSwitch);
    }

    private com.sun.tools.javac.util.List<JCTree.JCStatement> createListCopy(JavacTreeMaker maker, JavacSingularsRecipes.SingularData data, JavacNode builderType, JCTree source) {
        com.sun.tools.javac.util.List<JCTree.JCExpression> jceBlank = com.sun.tools.javac.util.List.nil();
        Name thisName = builderType.toName(OgnlContext.THIS_CONTEXT_KEY);
        com.sun.tools.javac.util.List.nil();
        com.sun.tools.javac.util.List<JCTree.JCExpression> constructorArgs = com.sun.tools.javac.util.List.of(maker.Select(maker.Ident(thisName), data.getPluralName()));
        JCTree.JCExpression targetTypeExpr = JavacHandlerUtil.chainDots(builderType, "java", "util", "ArrayList");
        return com.sun.tools.javac.util.List.of(maker.Exec(maker.Assign(maker.Ident(data.getPluralName()), maker.Apply(jceBlank, JavacHandlerUtil.chainDots(builderType, "java", "util", "Collections", "unmodifiableList"), com.sun.tools.javac.util.List.of(maker.NewClass(null, jceBlank, addTypeArgs(1, false, builderType, targetTypeExpr, data.getTypeArgs(), source), constructorArgs, null))))));
    }
}
