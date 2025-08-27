package lombok.javac.handlers;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.BoundKind;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.core.LombokImmutableList;
import lombok.core.SpiLoadUtil;
import lombok.core.TypeLibrary;
import lombok.javac.Javac;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import org.apache.ibatis.javassist.bytecode.DeprecatedAttribute;
import org.apache.ibatis.ognl.OgnlContext;
import org.springframework.web.servlet.tags.form.InputTag;

/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/JavacSingularsRecipes.SCL.lombok */
public class JavacSingularsRecipes {
    private static final JavacSingularsRecipes INSTANCE = new JavacSingularsRecipes();
    private final Map<String, JavacSingularizer> singularizers = new HashMap();
    private final TypeLibrary singularizableTypes = new TypeLibrary();

    private JavacSingularsRecipes() {
        try {
            loadAll(this.singularizableTypes, this.singularizers);
            this.singularizableTypes.lock();
        } catch (IOException e) {
            System.err.println("Lombok's @Singularizable feature is broken due to misconfigured SPI files: " + e);
        }
    }

    private static void loadAll(TypeLibrary library, Map<String, JavacSingularizer> map) throws IOException {
        for (JavacSingularizer handler : SpiLoadUtil.findServices(JavacSingularizer.class, JavacSingularizer.class.getClassLoader())) {
            Iterator<String> it = handler.getSupportedTypes().iterator();
            while (it.hasNext()) {
                String type = it.next();
                JavacSingularizer existingSingularizer = map.get(type);
                if (existingSingularizer != null) {
                    JavacSingularizer toKeep = existingSingularizer.getClass().getName().compareTo(handler.getClass().getName()) > 0 ? handler : existingSingularizer;
                    System.err.println("Multiple singularizers found for type " + type + "; the alphabetically first class is used: " + toKeep.getClass().getName());
                    map.put(type, toKeep);
                } else {
                    map.put(type, handler);
                    library.addType(type);
                }
            }
        }
    }

    public static JavacSingularsRecipes get() {
        return INSTANCE;
    }

    public String toQualified(String typeReference) {
        return this.singularizableTypes.toQualified(typeReference);
    }

    public JavacSingularizer getSingularizer(String fqn) {
        return this.singularizers.get(fqn);
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/JavacSingularsRecipes$SingularData.SCL.lombok */
    public static final class SingularData {
        private final JavacNode annotation;
        private final Name singularName;
        private final Name pluralName;
        private final List<JCTree.JCExpression> typeArgs;
        private final String targetFqn;
        private final JavacSingularizer singularizer;

        public SingularData(JavacNode annotation, Name singularName, Name pluralName, List<JCTree.JCExpression> typeArgs, String targetFqn, JavacSingularizer singularizer) {
            this.annotation = annotation;
            this.singularName = singularName;
            this.pluralName = pluralName;
            this.typeArgs = typeArgs;
            this.targetFqn = targetFqn;
            this.singularizer = singularizer;
        }

        public JavacNode getAnnotation() {
            return this.annotation;
        }

        public Name getSingularName() {
            return this.singularName;
        }

        public Name getPluralName() {
            return this.pluralName;
        }

        public List<JCTree.JCExpression> getTypeArgs() {
            return this.typeArgs;
        }

        public String getTargetFqn() {
            return this.targetFqn;
        }

        public JavacSingularizer getSingularizer() {
            return this.singularizer;
        }

        public String getTargetSimpleType() {
            int idx = this.targetFqn.lastIndexOf(".");
            return idx == -1 ? this.targetFqn : this.targetFqn.substring(idx + 1);
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/JavacSingularsRecipes$JavacSingularizer.SCL.lombok */
    public static abstract class JavacSingularizer {
        public abstract LombokImmutableList<String> getSupportedTypes();

        public abstract java.util.List<JavacNode> generateFields(SingularData singularData, JavacNode javacNode, JCTree jCTree);

        public abstract void generateMethods(SingularData singularData, boolean z, JavacNode javacNode, JCTree jCTree, boolean z2, boolean z3);

        public abstract void appendBuildCode(SingularData singularData, JavacNode javacNode, JCTree jCTree, ListBuffer<JCTree.JCStatement> listBuffer, Name name);

        protected JCTree.JCModifiers makeMods(JavacTreeMaker maker, JavacNode node, boolean deprecate) {
            return deprecate ? maker.Modifiers(1L, List.of(maker.Annotation(JavacHandlerUtil.genJavaLangTypeRef(node, DeprecatedAttribute.tag), List.nil()))) : maker.Modifiers(1L);
        }

        public boolean checkForAlreadyExistingNodesAndGenerateError(JavacNode builderType, SingularData data) {
            Iterator<JavacNode> it = builderType.down().iterator();
            while (it.hasNext()) {
                JavacNode child = it.next();
                switch (child.getKind()) {
                    case FIELD:
                        JCTree.JCVariableDecl field = child.get();
                        Name name = field.name;
                        if (name != null && JavacHandlerUtil.getGeneratedBy(field) == null) {
                            for (Name fieldToBeGenerated : listFieldsToBeGenerated(data, builderType)) {
                                if (fieldToBeGenerated.equals(name)) {
                                    child.addError("Manually adding a field that @Singular @Builder would generate is not supported. If you want to manually manage the builder aspect for this field/parameter, don't use @Singular.");
                                    return true;
                                }
                            }
                            break;
                        } else {
                            break;
                        }
                        break;
                    case METHOD:
                        JCTree.JCMethodDecl method = child.get();
                        Name name2 = method.name;
                        if (name2 != null && JavacHandlerUtil.getGeneratedBy(method) == null) {
                            for (Name methodToBeGenerated : listMethodsToBeGenerated(data, builderType)) {
                                if (methodToBeGenerated.equals(name2)) {
                                    child.addError("Manually adding a method that @Singular @Builder would generate is not supported. If you want to manually manage the builder aspect for this field/parameter, don't use @Singular.");
                                    return true;
                                }
                            }
                            break;
                        } else {
                            break;
                        }
                        break;
                }
            }
            return false;
        }

        public java.util.List<Name> listFieldsToBeGenerated(SingularData data, JavacNode builderType) {
            return Collections.singletonList(data.pluralName);
        }

        public java.util.List<Name> listMethodsToBeGenerated(SingularData data, JavacNode builderType) {
            Name p = data.pluralName;
            Name s = data.singularName;
            return p.equals(s) ? Collections.singletonList(p) : Arrays.asList(p, s);
        }

        public boolean requiresCleaning() {
            try {
                return !getClass().getMethod("appendCleaningCode", SingularData.class, JavacNode.class, JCTree.class, ListBuffer.class).getDeclaringClass().equals(JavacSingularizer.class);
            } catch (NoSuchMethodException e) {
                return false;
            }
        }

        public void appendCleaningCode(SingularData data, JavacNode builderType, JCTree source, ListBuffer<JCTree.JCStatement> statements) {
        }

        protected JCTree.JCExpression addTypeArgs(int count, boolean addExtends, JavacNode node, JCTree.JCExpression type, List<JCTree.JCExpression> typeArgs, JCTree source) {
            JavacTreeMaker maker = node.getTreeMaker();
            List<JCTree.JCExpression> clonedAndFixedTypeArgs = createTypeArgs(count, addExtends, node, typeArgs, source);
            return maker.TypeApply(type, clonedAndFixedTypeArgs);
        }

        protected List<JCTree.JCExpression> createTypeArgs(int count, boolean addExtends, JavacNode node, List<JCTree.JCExpression> typeArgs, JCTree source) {
            JCTree.JCExpression inner;
            JavacTreeMaker maker = node.getTreeMaker();
            Context context = node.getContext();
            if (count < 0) {
                throw new IllegalArgumentException("count is negative");
            }
            if (count == 0) {
                return List.nil();
            }
            ListBuffer<JCTree.JCExpression> arguments = new ListBuffer<>();
            if (typeArgs != null) {
                Iterator it = typeArgs.iterator();
                while (it.hasNext()) {
                    JCTree.JCWildcard jCWildcard = (JCTree.JCExpression) it.next();
                    if (!addExtends) {
                        if (jCWildcard.getKind() == Tree.Kind.UNBOUNDED_WILDCARD || jCWildcard.getKind() == Tree.Kind.SUPER_WILDCARD) {
                            arguments.append(JavacHandlerUtil.genJavaLangTypeRef(node, "Object"));
                        } else if (jCWildcard.getKind() == Tree.Kind.EXTENDS_WILDCARD) {
                            try {
                                inner = (JCTree.JCExpression) jCWildcard.inner;
                            } catch (Exception e) {
                                inner = JavacHandlerUtil.genJavaLangTypeRef(node, "Object");
                            }
                            arguments.append(JavacHandlerUtil.cloneType(maker, inner, source, context));
                        } else {
                            arguments.append(JavacHandlerUtil.cloneType(maker, jCWildcard, source, context));
                        }
                    } else if (jCWildcard.getKind() == Tree.Kind.UNBOUNDED_WILDCARD || jCWildcard.getKind() == Tree.Kind.SUPER_WILDCARD) {
                        arguments.append(maker.Wildcard(maker.TypeBoundKind(BoundKind.UNBOUND), null));
                    } else if (jCWildcard.getKind() == Tree.Kind.EXTENDS_WILDCARD) {
                        arguments.append(JavacHandlerUtil.cloneType(maker, jCWildcard, source, context));
                    } else {
                        arguments.append(maker.Wildcard(maker.TypeBoundKind(BoundKind.EXTENDS), JavacHandlerUtil.cloneType(maker, jCWildcard, source, context)));
                    }
                    count--;
                    if (count == 0) {
                        break;
                    }
                }
            }
            while (true) {
                int i = count;
                count--;
                if (i > 0) {
                    if (addExtends) {
                        arguments.append(maker.Wildcard(maker.TypeBoundKind(BoundKind.UNBOUND), null));
                    } else {
                        arguments.append(JavacHandlerUtil.genJavaLangTypeRef(node, "Object"));
                    }
                } else {
                    return arguments.toList();
                }
            }
        }

        protected JCTree.JCExpression getSize(JavacTreeMaker maker, JavacNode builderType, Name name, boolean nullGuard, boolean parens) {
            Name thisName = builderType.toName(OgnlContext.THIS_CONTEXT_KEY);
            JCTree.JCMethodInvocation jCMethodInvocationApply = maker.Apply(List.nil(), maker.Select(maker.Select(maker.Ident(thisName), name), builderType.toName(InputTag.SIZE_ATTRIBUTE)), List.nil());
            if (nullGuard) {
                JCTree.JCConditional jCConditionalConditional = maker.Conditional(maker.Binary(Javac.CTC_EQUAL, maker.Select(maker.Ident(thisName), name), maker.Literal(Javac.CTC_BOT, 0)), maker.Literal(Javac.CTC_INT, 0), jCMethodInvocationApply);
                return parens ? maker.Parens(jCConditionalConditional) : jCConditionalConditional;
            }
            return jCMethodInvocationApply;
        }

        protected JCTree.JCExpression cloneParamType(int index, JavacTreeMaker maker, List<JCTree.JCExpression> typeArgs, JavacNode builderType, JCTree source) {
            if (typeArgs == null || typeArgs.size() <= index) {
                return JavacHandlerUtil.genJavaLangTypeRef(builderType, "Object");
            }
            JCTree.JCWildcard jCWildcard = (JCTree.JCExpression) typeArgs.get(index);
            if (jCWildcard.getKind() == Tree.Kind.UNBOUNDED_WILDCARD || jCWildcard.getKind() == Tree.Kind.SUPER_WILDCARD) {
                return JavacHandlerUtil.genJavaLangTypeRef(builderType, "Object");
            }
            if (jCWildcard.getKind() == Tree.Kind.EXTENDS_WILDCARD) {
                try {
                    return JavacHandlerUtil.cloneType(maker, jCWildcard.inner, source, builderType.getContext());
                } catch (Exception e) {
                    return JavacHandlerUtil.genJavaLangTypeRef(builderType, "Object");
                }
            }
            return JavacHandlerUtil.cloneType(maker, jCWildcard, source, builderType.getContext());
        }
    }
}
