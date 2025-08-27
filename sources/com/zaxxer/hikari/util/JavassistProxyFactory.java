package com.zaxxer.hikari.util;

import com.zaxxer.hikari.pool.ProxyCallableStatement;
import com.zaxxer.hikari.pool.ProxyConnection;
import com.zaxxer.hikari.pool.ProxyDatabaseMetaData;
import com.zaxxer.hikari.pool.ProxyPreparedStatement;
import com.zaxxer.hikari.pool.ProxyResultSet;
import com.zaxxer.hikari.pool.ProxyStatement;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javassist.CannotCompileException;
import javassist.ClassMap;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/util/JavassistProxyFactory.class */
public final class JavassistProxyFactory {
    private static ClassPool classPool;
    private static String genDirectory = "";

    public static void main(String... args) throws Exception {
        classPool = new ClassPool();
        classPool.importPackage("java.sql");
        classPool.appendClassPath(new LoaderClassPath(JavassistProxyFactory.class.getClassLoader()));
        if (args.length > 0) {
            genDirectory = args[0];
        }
        generateProxyClass(Connection.class, ProxyConnection.class.getName(), "{ try { return delegate.method($$); } catch (SQLException e) { throw checkException(e); } }");
        generateProxyClass(Statement.class, ProxyStatement.class.getName(), "{ try { return delegate.method($$); } catch (SQLException e) { throw checkException(e); } }");
        generateProxyClass(ResultSet.class, ProxyResultSet.class.getName(), "{ try { return delegate.method($$); } catch (SQLException e) { throw checkException(e); } }");
        generateProxyClass(DatabaseMetaData.class, ProxyDatabaseMetaData.class.getName(), "{ try { return delegate.method($$); } catch (SQLException e) { throw checkException(e); } }");
        generateProxyClass(PreparedStatement.class, ProxyPreparedStatement.class.getName(), "{ try { return ((cast) delegate).method($$); } catch (SQLException e) { throw checkException(e); } }");
        generateProxyClass(CallableStatement.class, ProxyCallableStatement.class.getName(), "{ try { return ((cast) delegate).method($$); } catch (SQLException e) { throw checkException(e); } }");
        modifyProxyFactory();
    }

    private static void modifyProxyFactory() throws CannotCompileException, NotFoundException, IOException {
        System.out.println("Generating method bodies for com.zaxxer.hikari.proxy.ProxyFactory");
        String packageName = ProxyConnection.class.getPackage().getName();
        CtClass proxyCt = classPool.getCtClass("com.zaxxer.hikari.pool.ProxyFactory");
        for (CtMethod method : proxyCt.getMethods()) {
            switch (method.getName()) {
                case "getProxyConnection":
                    method.setBody("{return new " + packageName + ".HikariProxyConnection($$);}");
                    break;
                case "getProxyStatement":
                    method.setBody("{return new " + packageName + ".HikariProxyStatement($$);}");
                    break;
                case "getProxyPreparedStatement":
                    method.setBody("{return new " + packageName + ".HikariProxyPreparedStatement($$);}");
                    break;
                case "getProxyCallableStatement":
                    method.setBody("{return new " + packageName + ".HikariProxyCallableStatement($$);}");
                    break;
                case "getProxyResultSet":
                    method.setBody("{return new " + packageName + ".HikariProxyResultSet($$);}");
                    break;
                case "getProxyDatabaseMetaData":
                    method.setBody("{return new " + packageName + ".HikariProxyDatabaseMetaData($$);}");
                    break;
            }
        }
        proxyCt.writeFile(genDirectory + "target/classes");
    }

    private static <T> void generateProxyClass(Class<T> primaryInterface, String superClassName, String methodBody) throws Exception {
        String modifiedBody;
        String newClassName = superClassName.replaceAll("(.+)\\.(\\w+)", "$1.Hikari$2");
        CtClass superCt = classPool.getCtClass(superClassName);
        CtClass targetCt = classPool.makeClass(newClassName, superCt);
        targetCt.setModifiers(16);
        System.out.println("Generating " + newClassName);
        targetCt.setModifiers(1);
        Set<String> superSigs = new HashSet<>();
        for (CtMethod method : superCt.getMethods()) {
            if ((method.getModifiers() & 16) == 16) {
                superSigs.add(method.getName() + method.getSignature());
            }
        }
        Set<String> methods = new HashSet<>();
        for (Class<?> intf : getAllInterfaces(primaryInterface)) {
            CtClass intfCt = classPool.getCtClass(intf.getName());
            targetCt.addInterface(intfCt);
            for (CtMethod intfMethod : intfCt.getDeclaredMethods()) {
                String signature = intfMethod.getName() + intfMethod.getSignature();
                if (!superSigs.contains(signature) && !methods.contains(signature)) {
                    methods.add(signature);
                    CtMethod method2 = CtNewMethod.copy(intfMethod, targetCt, (ClassMap) null);
                    String modifiedBody2 = methodBody;
                    CtMethod superMethod = superCt.getMethod(intfMethod.getName(), intfMethod.getSignature());
                    if ((superMethod.getModifiers() & 1024) != 1024 && !isDefaultMethod(intf, intfMethod)) {
                        modifiedBody2 = modifiedBody2.replace("((cast) ", "").replace("delegate", "super").replace("super)", "super");
                    }
                    String modifiedBody3 = modifiedBody2.replace("cast", primaryInterface.getName());
                    if (isThrowsSqlException(intfMethod)) {
                        modifiedBody = modifiedBody3.replace(JamXmlElements.METHOD, method2.getName());
                    } else {
                        modifiedBody = "{ return ((cast) delegate).method($$); }".replace(JamXmlElements.METHOD, method2.getName()).replace("cast", primaryInterface.getName());
                    }
                    if (method2.getReturnType() == CtClass.voidType) {
                        modifiedBody = modifiedBody.replace("return", "");
                    }
                    method2.setBody(modifiedBody);
                    targetCt.addMethod(method2);
                }
            }
        }
        targetCt.getClassFile().setMajorVersion(52);
        targetCt.writeFile(genDirectory + "target/classes");
    }

    private static boolean isThrowsSqlException(CtMethod method) {
        try {
            for (CtClass clazz : method.getExceptionTypes()) {
                if (clazz.getSimpleName().equals("SQLException")) {
                    return true;
                }
            }
            return false;
        } catch (NotFoundException e) {
            return false;
        }
    }

    private static boolean isDefaultMethod(Class<?> intf, CtMethod intfMethod) throws Exception {
        List<Class<?>> paramTypes = new ArrayList<>();
        for (CtClass pt : intfMethod.getParameterTypes()) {
            paramTypes.add(toJavaClass(pt));
        }
        return intf.getDeclaredMethod(intfMethod.getName(), (Class[]) paramTypes.toArray(new Class[0])).toString().contains("default ");
    }

    private static Set<Class<?>> getAllInterfaces(Class<?> clazz) {
        Set<Class<?>> interfaces = new LinkedHashSet<>();
        for (Class<?> intf : clazz.getInterfaces()) {
            if (intf.getInterfaces().length > 0) {
                interfaces.addAll(getAllInterfaces(intf));
            }
            interfaces.add(intf);
        }
        if (clazz.getSuperclass() != null) {
            interfaces.addAll(getAllInterfaces(clazz.getSuperclass()));
        }
        if (clazz.isInterface()) {
            interfaces.add(clazz);
        }
        return interfaces;
    }

    private static Class<?> toJavaClass(CtClass cls) throws Exception {
        if (cls.getName().endsWith("[]")) {
            return Array.newInstance(toJavaClass(cls.getName().replace("[]", "")), 0).getClass();
        }
        return toJavaClass(cls.getName());
    }

    private static Class<?> toJavaClass(String cn) throws Exception {
        switch (cn) {
            case "int":
                return Integer.TYPE;
            case "long":
                return Long.TYPE;
            case "short":
                return Short.TYPE;
            case "byte":
                return Byte.TYPE;
            case "float":
                return Float.TYPE;
            case "double":
                return Double.TYPE;
            case "boolean":
                return Boolean.TYPE;
            case "char":
                return Character.TYPE;
            case "void":
                return Void.TYPE;
            default:
                return Class.forName(cn);
        }
    }
}
