package lombok.eclipse.handlers.singulars;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.util.List;
import lombok.core.LombokImmutableList;
import lombok.eclipse.EclipseNode;
import lombok.eclipse.handlers.EclipseSingularsRecipes;

/* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/singulars/EclipseGuavaSetListSingularizer.SCL.lombok */
public class EclipseGuavaSetListSingularizer extends EclipseGuavaSingularizer {
    private static final LombokImmutableList<String> SUFFIXES = LombokImmutableList.of("");
    private static final LombokImmutableList<String> SUPPORTED_TYPES = LombokImmutableList.of("com.google.common.collect.ImmutableCollection", "com.google.common.collect.ImmutableList", "com.google.common.collect.ImmutableSet", "com.google.common.collect.ImmutableSortedSet");

    @Override // lombok.eclipse.handlers.singulars.EclipseGuavaSingularizer, lombok.eclipse.handlers.EclipseSingularsRecipes.EclipseSingularizer
    public /* bridge */ /* synthetic */ List generateFields(EclipseSingularsRecipes.SingularData singularData, EclipseNode eclipseNode) {
        return super.generateFields(singularData, eclipseNode);
    }

    @Override // lombok.eclipse.handlers.singulars.EclipseGuavaSingularizer, lombok.eclipse.handlers.EclipseSingularsRecipes.EclipseSingularizer
    public /* bridge */ /* synthetic */ void generateMethods(EclipseSingularsRecipes.SingularData singularData, boolean z, EclipseNode eclipseNode, boolean z2, boolean z3) {
        super.generateMethods(singularData, z, eclipseNode, z2, z3);
    }

    @Override // lombok.eclipse.handlers.singulars.EclipseGuavaSingularizer, lombok.eclipse.handlers.EclipseSingularsRecipes.EclipseSingularizer
    public /* bridge */ /* synthetic */ void appendBuildCode(EclipseSingularsRecipes.SingularData singularData, EclipseNode eclipseNode, List list, char[] cArr) {
        super.appendBuildCode(singularData, eclipseNode, list, cArr);
    }

    @Override // lombok.eclipse.handlers.EclipseSingularsRecipes.EclipseSingularizer
    public LombokImmutableList<String> getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override // lombok.eclipse.handlers.singulars.EclipseGuavaSingularizer
    protected LombokImmutableList<String> getArgumentSuffixes() {
        return SUFFIXES;
    }

    @Override // lombok.eclipse.handlers.singulars.EclipseGuavaSingularizer
    protected String getAddMethodName() {
        return BeanUtil.PREFIX_ADDER;
    }

    @Override // lombok.eclipse.handlers.singulars.EclipseGuavaSingularizer
    protected String getAddAllTypeName() {
        return "java.lang.Iterable";
    }
}
