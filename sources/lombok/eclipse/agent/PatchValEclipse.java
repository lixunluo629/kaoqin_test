package lombok.eclipse.agent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import lombok.Lombok;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;

/* loaded from: lombok-1.16.22.jar:lombok/eclipse/agent/PatchValEclipse.SCL.lombok */
public class PatchValEclipse {
    private static final Field FIELD_NAME_INDEX;

    public static void copyInitializationOfForEachIterable(Parser parser) throws IllegalAccessException, IllegalArgumentException {
        try {
            ForeachStatement[] foreachStatementArr = (ASTNode[]) Reflection.astStackField.get(parser);
            int astPtr = ((Integer) Reflection.astPtrField.get(parser)).intValue();
            ForeachStatement foreachDecl = foreachStatementArr[astPtr];
            Expression expression = foreachDecl.collection;
            if (expression == null) {
                return;
            }
            boolean val = couldBeVal(foreachDecl.elementVariable.type);
            boolean var = couldBeVar(foreachDecl.elementVariable.type);
            if (foreachDecl.elementVariable != null) {
                if (val || var) {
                    try {
                        if (Reflection.iterableCopyField == null) {
                            return;
                        }
                        Reflection.iterableCopyField.set(foreachDecl.elementVariable, expression);
                    } catch (Exception unused) {
                    }
                }
            }
        } catch (Exception unused2) {
        }
    }

    public static void copyInitializationOfLocalDeclaration(Parser parser) throws IllegalAccessException, IllegalArgumentException {
        Expression expression;
        try {
            AbstractVariableDeclaration[] abstractVariableDeclarationArr = (ASTNode[]) Reflection.astStackField.get(parser);
            int astPtr = ((Integer) Reflection.astPtrField.get(parser)).intValue();
            AbstractVariableDeclaration variableDecl = abstractVariableDeclarationArr[astPtr];
            if ((variableDecl instanceof LocalDeclaration) && (expression = variableDecl.initialization) != null) {
                boolean val = couldBeVal(variableDecl.type);
                boolean var = couldBeVar(variableDecl.type);
                if (val || var) {
                    try {
                        if (Reflection.initCopyField == null) {
                            return;
                        }
                        Reflection.initCopyField.set(variableDecl, expression);
                    } catch (Exception unused) {
                    }
                }
            }
        } catch (Exception unused2) {
        }
    }

    private static boolean couldBeVar(TypeReference type) {
        return PatchVal.couldBe("lombok.experimental.var", type) || PatchVal.couldBe("lombok.var", type);
    }

    public static void addFinalAndValAnnotationToSingleVariableDeclaration(Object converter, SingleVariableDeclaration out, LocalDeclaration in) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<IExtendedModifier> modifiers = out.modifiers();
        addFinalAndValAnnotationToModifierList(converter, modifiers, out.getAST(), in);
    }

    public static void addFinalAndValAnnotationToVariableDeclarationStatement(Object converter, VariableDeclarationStatement out, LocalDeclaration in) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<IExtendedModifier> modifiers = out.modifiers();
        addFinalAndValAnnotationToModifierList(converter, modifiers, out.getAST(), in);
    }

    public static void addFinalAndValAnnotationToModifierList(Object converter, List<IExtendedModifier> modifiers, AST ast, LocalDeclaration in) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Name typeName;
        if ((in.modifiers & 16) == 0 || in.annotations == null) {
            return;
        }
        boolean found = false;
        Annotation valAnnotation = null;
        Annotation[] annotationArr = in.annotations;
        int length = annotationArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            Annotation ann = annotationArr[i];
            if (!couldBeVal(ann.type)) {
                i++;
            } else {
                found = true;
                valAnnotation = ann;
                break;
            }
        }
        if (found && modifiers != null) {
            boolean finalIsPresent = false;
            boolean valIsPresent = false;
            for (Object present : modifiers) {
                if (present instanceof Modifier) {
                    Modifier.ModifierKeyword keyword = ((Modifier) present).getKeyword();
                    if (keyword != null) {
                        if (keyword.toFlagValue() == 16) {
                            finalIsPresent = true;
                        }
                    }
                }
                if ((present instanceof org.eclipse.jdt.core.dom.Annotation) && (typeName = ((org.eclipse.jdt.core.dom.Annotation) present).getTypeName()) != null) {
                    String fullyQualifiedName = typeName.getFullyQualifiedName();
                    if ("val".equals(fullyQualifiedName) || "lombok.val".equals(fullyQualifiedName)) {
                        valIsPresent = true;
                    }
                }
            }
            if (!finalIsPresent) {
                modifiers.add(createModifier(ast, Modifier.ModifierKeyword.FINAL_KEYWORD, valAnnotation.sourceStart, valAnnotation.sourceEnd));
            }
            if (!valIsPresent) {
                MarkerAnnotation newAnnotation = createValAnnotation(ast, valAnnotation, valAnnotation.sourceStart, valAnnotation.sourceEnd);
                try {
                    Reflection.astConverterRecordNodes.invoke(converter, newAnnotation, valAnnotation);
                    Reflection.astConverterRecordNodes.invoke(converter, newAnnotation.getTypeName(), valAnnotation.type);
                    modifiers.add(newAnnotation);
                } catch (IllegalAccessException e) {
                    throw Lombok.sneakyThrow(e);
                } catch (InvocationTargetException e2) {
                    throw Lombok.sneakyThrow(e2.getCause());
                }
            }
        }
    }

    private static boolean couldBeVal(TypeReference type) {
        return PatchVal.couldBe("lombok.val", type);
    }

    public static Modifier createModifier(AST ast, Modifier.ModifierKeyword keyword, int start, int end) {
        try {
            Modifier modifier = (Modifier) Reflection.modifierConstructor.newInstance(ast);
            if (modifier != null) {
                modifier.setKeyword(keyword);
                modifier.setSourceRange(start, (end - start) + 1);
            }
            return modifier;
        } catch (IllegalAccessException e) {
            throw Lombok.sneakyThrow(e);
        } catch (InstantiationException e2) {
            throw Lombok.sneakyThrow(e2);
        } catch (InvocationTargetException e3) {
            throw Lombok.sneakyThrow(e3);
        }
    }

    public static MarkerAnnotation createValAnnotation(AST ast, Annotation original, int start, int end) throws IllegalAccessException, IllegalArgumentException {
        try {
            MarkerAnnotation out = (MarkerAnnotation) Reflection.markerAnnotationConstructor.newInstance(ast);
            if (out != null) {
                SimpleName valName = ast.newSimpleName("val");
                valName.setSourceRange(start, (end - start) + 1);
                if (original.type instanceof SingleTypeReference) {
                    out.setTypeName(valName);
                    setIndex(valName, 1);
                } else {
                    SimpleName lombokName = ast.newSimpleName("lombok");
                    lombokName.setSourceRange(start, (end - start) + 1);
                    setIndex(lombokName, 1);
                    setIndex(valName, 2);
                    QualifiedName fullName = ast.newQualifiedName(lombokName, valName);
                    setIndex(fullName, 1);
                    fullName.setSourceRange(start, (end - start) + 1);
                    out.setTypeName(fullName);
                }
                out.setSourceRange(start, (end - start) + 1);
            }
            return out;
        } catch (IllegalAccessException e) {
            throw Lombok.sneakyThrow(e);
        } catch (InstantiationException e2) {
            throw Lombok.sneakyThrow(e2);
        } catch (InvocationTargetException e3) {
            throw Lombok.sneakyThrow(e3);
        }
    }

    static {
        Field f = null;
        try {
            f = Name.class.getDeclaredField(BeanDefinitionParserDelegate.INDEX_ATTRIBUTE);
            f.setAccessible(true);
        } catch (Throwable unused) {
        }
        FIELD_NAME_INDEX = f;
    }

    private static void setIndex(Name name, int index) throws IllegalAccessException, IllegalArgumentException {
        try {
            if (FIELD_NAME_INDEX != null) {
                FIELD_NAME_INDEX.set(name, Integer.valueOf(index));
            }
        } catch (Exception unused) {
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/eclipse/agent/PatchValEclipse$Reflection.SCL.lombok */
    public static final class Reflection {
        private static final Field initCopyField;
        private static final Field iterableCopyField;
        private static final Field astStackField;
        private static final Field astPtrField;
        private static final Constructor<Modifier> modifierConstructor;
        private static final Constructor<MarkerAnnotation> markerAnnotationConstructor;
        private static final Method astConverterRecordNodes;

        static {
            Field a = null;
            Field b = null;
            Field c = null;
            Field d = null;
            Constructor<Modifier> f = null;
            Constructor<MarkerAnnotation> g = null;
            Method h = null;
            try {
                a = LocalDeclaration.class.getDeclaredField("$initCopy");
                b = LocalDeclaration.class.getDeclaredField("$iterableCopy");
            } catch (Throwable unused) {
            }
            try {
                c = Parser.class.getDeclaredField("astStack");
                c.setAccessible(true);
                d = Parser.class.getDeclaredField("astPtr");
                d.setAccessible(true);
                f = Modifier.class.getDeclaredConstructor(AST.class);
                f.setAccessible(true);
                g = MarkerAnnotation.class.getDeclaredConstructor(AST.class);
                g.setAccessible(true);
                Class<?> z = Class.forName("org.eclipse.jdt.core.dom.ASTConverter");
                h = z.getDeclaredMethod("recordNodes", org.eclipse.jdt.core.dom.ASTNode.class, ASTNode.class);
                h.setAccessible(true);
            } catch (Throwable unused2) {
            }
            initCopyField = a;
            iterableCopyField = b;
            astStackField = c;
            astPtrField = d;
            modifierConstructor = f;
            markerAnnotationConstructor = g;
            astConverterRecordNodes = h;
        }
    }
}
