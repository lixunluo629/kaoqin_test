package lombok.javac;

import com.sun.tools.javac.tree.JCTree;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: lombok-1.16.22.jar:lombok/javac/PackageName.SCL.lombok */
public class PackageName {
    private static final Method packageNameMethod = getPackageNameMethod();

    private static Method getPackageNameMethod() {
        try {
            return JCTree.JCCompilationUnit.class.getDeclaredMethod("getPackageName", new Class[0]);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getPackageName(JCTree.JCCompilationUnit cu) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JCTree t = getPackageNode(cu);
        if (t != null) {
            return t.toString();
        }
        return null;
    }

    public static JCTree getPackageNode(JCTree.JCCompilationUnit cu) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (packageNameMethod != null) {
            try {
                Object pkg = packageNameMethod.invoke(cu, new Object[0]);
                if ((pkg instanceof JCTree.JCFieldAccess) || (pkg instanceof JCTree.JCIdent)) {
                    return (JCTree) pkg;
                }
                return null;
            } catch (Exception e) {
            }
        }
        if ((cu.pid instanceof JCTree.JCFieldAccess) || (cu.pid instanceof JCTree.JCIdent)) {
            return cu.pid;
        }
        return null;
    }
}
