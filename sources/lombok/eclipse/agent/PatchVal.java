package lombok.eclipse.agent;

import java.lang.reflect.Field;
import lombok.eclipse.Eclipse;
import lombok.eclipse.handlers.EclipseHandlerUtil;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MarkerAnnotation;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ParameterizedTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.jdt.internal.compiler.lookup.TypeVariableBinding;

/* loaded from: lombok-1.16.22.jar:lombok/eclipse/agent/PatchVal.SCL.lombok */
public class PatchVal {
    public static TypeBinding skipResolveInitializerIfAlreadyCalled(Expression expr, BlockScope scope) {
        if (expr.resolvedType != null) {
            return expr.resolvedType;
        }
        try {
            return expr.resolveType(scope);
        } catch (ArrayIndexOutOfBoundsException unused) {
            return null;
        } catch (NullPointerException unused2) {
            return null;
        }
    }

    public static TypeBinding skipResolveInitializerIfAlreadyCalled2(Expression expr, BlockScope scope, LocalDeclaration decl) {
        if (decl != null && LocalDeclaration.class.equals(decl.getClass()) && expr.resolvedType != null) {
            return expr.resolvedType;
        }
        try {
            return expr.resolveType(scope);
        } catch (ArrayIndexOutOfBoundsException unused) {
            return null;
        } catch (NullPointerException unused2) {
            return null;
        }
    }

    public static boolean matches(String key, char[] array) {
        if (array == null || key.length() != array.length) {
            return false;
        }
        for (int i = 0; i < array.length; i++) {
            if (key.charAt(i) != array[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean couldBe(String key, TypeReference ref) {
        String[] keyParts = key.split("\\.");
        if (ref instanceof SingleTypeReference) {
            char[] token = ((SingleTypeReference) ref).token;
            return matches(keyParts[keyParts.length - 1], token);
        }
        if (ref instanceof QualifiedTypeReference) {
            char[][] tokens = ((QualifiedTypeReference) ref).tokens;
            if (keyParts.length != tokens.length) {
                return false;
            }
            for (int i = 0; i < tokens.length; i++) {
                String part = keyParts[i];
                char[] token2 = tokens[i];
                if (!matches(part, token2)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean is(TypeReference ref, BlockScope scope, String key) {
        if (!couldBe(key, ref)) {
            return false;
        }
        TypeBinding resolvedType = ref.resolvedType;
        if (resolvedType == null) {
            resolvedType = ref.resolveType(scope, false);
        }
        if (resolvedType == null) {
            return false;
        }
        char[] pkg = resolvedType.qualifiedPackageName();
        char[] nm = resolvedType.qualifiedSourceName();
        int pkgFullLength = pkg.length > 0 ? pkg.length + 1 : 0;
        char[] fullName = new char[pkgFullLength + nm.length];
        if (pkg.length > 0) {
            System.arraycopy(pkg, 0, fullName, 0, pkg.length);
            fullName[pkg.length] = '.';
        }
        System.arraycopy(nm, 0, fullName, pkgFullLength, nm.length);
        return matches(key, fullName);
    }

    /* loaded from: lombok-1.16.22.jar:lombok/eclipse/agent/PatchVal$Reflection.SCL.lombok */
    public static final class Reflection {
        private static final Field initCopyField;
        private static final Field iterableCopyField;

        static {
            Field a = null;
            Field b = null;
            try {
                a = LocalDeclaration.class.getDeclaredField("$initCopy");
                b = LocalDeclaration.class.getDeclaredField("$iterableCopy");
            } catch (Throwable unused) {
            }
            initCopyField = a;
            iterableCopyField = b;
        }
    }

    public static boolean handleValForLocalDeclaration(LocalDeclaration local, BlockScope scope) {
        TypeBinding resolved;
        if (local == null || !LocalDeclaration.class.equals(local.getClass())) {
            return false;
        }
        boolean decomponent = false;
        boolean val = isVal(local, scope);
        boolean var = isVar(local, scope);
        if (!val && !var) {
            return false;
        }
        StackTraceElement[] st = new Throwable().getStackTrace();
        int i = 0;
        while (true) {
            if (i >= st.length - 2 || i >= 10) {
                break;
            }
            if (!st[i].getClassName().equals("lombok.launch.PatchFixesHider$Val")) {
                i++;
            } else {
                boolean valInForStatement = val && st[i + 1].getClassName().equals("org.eclipse.jdt.internal.compiler.ast.LocalDeclaration") && st[i + 2].getClassName().equals("org.eclipse.jdt.internal.compiler.ast.ForStatement");
                if (valInForStatement) {
                    return false;
                }
            }
        }
        Expression init = local.initialization;
        if (init == null && Reflection.initCopyField != null) {
            try {
                init = (Expression) Reflection.initCopyField.get(local);
            } catch (Exception unused) {
            }
        }
        if (init == null && Reflection.iterableCopyField != null) {
            try {
                init = (Expression) Reflection.iterableCopyField.get(local);
                decomponent = true;
            } catch (Exception unused2) {
            }
        }
        TypeReference replacement = null;
        if (init != null) {
            if (init.getClass().getName().equals("org.eclipse.jdt.internal.compiler.ast.LambdaExpression")) {
                return false;
            }
            try {
                resolved = decomponent ? getForEachComponentType(init, scope) : resolveForExpression(init, scope);
            } catch (NullPointerException unused3) {
                resolved = null;
            }
            if (resolved != null) {
                try {
                    replacement = EclipseHandlerUtil.makeType(resolved, local.type, false);
                } catch (Exception unused4) {
                }
            }
        }
        if (val) {
            local.modifiers |= 16;
        }
        local.annotations = addValAnnotation(local.annotations, local.type, scope);
        local.type = replacement != null ? replacement : new QualifiedTypeReference(TypeConstants.JAVA_LANG_OBJECT, Eclipse.poss(local.type, 3));
        return false;
    }

    private static boolean isVar(LocalDeclaration local, BlockScope scope) {
        return is(local.type, scope, "lombok.experimental.var") || is(local.type, scope, "lombok.var");
    }

    private static boolean isVal(LocalDeclaration local, BlockScope scope) {
        return is(local.type, scope, "lombok.val");
    }

    public static boolean handleValForForEach(ForeachStatement forEach, BlockScope scope) {
        TypeBinding component;
        if (forEach.elementVariable == null) {
            return false;
        }
        boolean val = isVal(forEach.elementVariable, scope);
        boolean var = isVar(forEach.elementVariable, scope);
        if ((!val && !var) || (component = getForEachComponentType(forEach.collection, scope)) == null) {
            return false;
        }
        TypeReference replacement = EclipseHandlerUtil.makeType(component, forEach.elementVariable.type, false);
        if (val) {
            forEach.elementVariable.modifiers |= 16;
        }
        forEach.elementVariable.annotations = addValAnnotation(forEach.elementVariable.annotations, forEach.elementVariable.type, scope);
        forEach.elementVariable.type = replacement != null ? replacement : new QualifiedTypeReference(TypeConstants.JAVA_LANG_OBJECT, Eclipse.poss(forEach.elementVariable.type, 3));
        return false;
    }

    private static Annotation[] addValAnnotation(Annotation[] originals, TypeReference originalRef, BlockScope scope) {
        Annotation[] newAnn;
        if (originals != null) {
            newAnn = new Annotation[1 + originals.length];
            System.arraycopy(originals, 0, newAnn, 0, originals.length);
        } else {
            newAnn = new Annotation[1];
        }
        newAnn[newAnn.length - 1] = new MarkerAnnotation(originalRef, originalRef.sourceStart);
        return newAnn;
    }

    private static TypeBinding getForEachComponentType(Expression collection, BlockScope scope) {
        if (collection != null) {
            TypeBinding resolved = collection.resolvedType;
            if (resolved == null) {
                resolved = resolveForExpression(collection, scope);
            }
            if (resolved == null) {
                return null;
            }
            if (resolved.isArrayType()) {
                return ((ArrayBinding) resolved).elementsType();
            }
            if (resolved instanceof ReferenceBinding) {
                ParameterizedTypeBinding parameterizedTypeBindingFindSuperTypeOriginatingFrom = ((ReferenceBinding) resolved).findSuperTypeOriginatingFrom(38, false);
                TypeVariableBinding[] typeVariableBindingArrTypeVariables = null;
                if (parameterizedTypeBindingFindSuperTypeOriginatingFrom != null) {
                    switch (parameterizedTypeBindingFindSuperTypeOriginatingFrom.kind()) {
                        case 260:
                            typeVariableBindingArrTypeVariables = parameterizedTypeBindingFindSuperTypeOriginatingFrom.arguments;
                            break;
                        case 1028:
                            return null;
                        case 2052:
                            typeVariableBindingArrTypeVariables = parameterizedTypeBindingFindSuperTypeOriginatingFrom.typeVariables();
                            break;
                    }
                }
                if (typeVariableBindingArrTypeVariables != null && typeVariableBindingArrTypeVariables.length == 1) {
                    return typeVariableBindingArrTypeVariables[0];
                }
                return null;
            }
            return null;
        }
        return null;
    }

    private static TypeBinding resolveForExpression(Expression collection, BlockScope scope) {
        try {
            return collection.resolveType(scope);
        } catch (ArrayIndexOutOfBoundsException unused) {
            return null;
        }
    }
}
