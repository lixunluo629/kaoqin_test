package lombok.javac.handlers.singulars;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import lombok.core.LombokImmutableList;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.JavacSingularsRecipes;

/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/singulars/JavacJavaUtilSetSingularizer.SCL.lombok */
public class JavacJavaUtilSetSingularizer extends JavacJavaUtilListSetSingularizer {
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
        return LombokImmutableList.of("java.util.Set", "java.util.SortedSet", "java.util.NavigableSet");
    }

    @Override // lombok.javac.handlers.JavacSingularsRecipes.JavacSingularizer
    public void appendBuildCode(JavacSingularsRecipes.SingularData data, JavacNode builderType, JCTree source, ListBuffer<JCTree.JCStatement> statements, Name targetVariableName) {
        if (useGuavaInstead(builderType)) {
            this.guavaListSetSingularizer.appendBuildCode(data, builderType, source, statements, targetVariableName);
            return;
        }
        JavacTreeMaker maker = builderType.getTreeMaker();
        if (data.getTargetFqn().equals("java.util.Set")) {
            statements.appendList(createJavaUtilSetMapInitialCapacitySwitchStatements(maker, data, builderType, false, "emptySet", "singleton", "LinkedHashSet", source));
        } else {
            statements.appendList(createJavaUtilSimpleCreationAndFillStatements(maker, data, builderType, false, true, false, true, "TreeSet", source));
        }
    }
}
