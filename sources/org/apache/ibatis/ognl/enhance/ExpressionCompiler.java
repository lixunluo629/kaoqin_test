package org.apache.ibatis.ognl.enhance;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.javassist.CannotCompileException;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtField;
import org.apache.ibatis.javassist.CtMethod;
import org.apache.ibatis.javassist.CtNewConstructor;
import org.apache.ibatis.javassist.CtNewMethod;
import org.apache.ibatis.javassist.LoaderClassPath;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.ibatis.ognl.ASTAnd;
import org.apache.ibatis.ognl.ASTChain;
import org.apache.ibatis.ognl.ASTConst;
import org.apache.ibatis.ognl.ASTCtor;
import org.apache.ibatis.ognl.ASTList;
import org.apache.ibatis.ognl.ASTMethod;
import org.apache.ibatis.ognl.ASTOr;
import org.apache.ibatis.ognl.ASTProperty;
import org.apache.ibatis.ognl.ASTRootVarRef;
import org.apache.ibatis.ognl.ASTStaticField;
import org.apache.ibatis.ognl.ASTStaticMethod;
import org.apache.ibatis.ognl.ASTThisVarRef;
import org.apache.ibatis.ognl.ASTVarRef;
import org.apache.ibatis.ognl.ExpressionNode;
import org.apache.ibatis.ognl.Node;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.ibatis.ognl.OgnlRuntime;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/enhance/ExpressionCompiler.class */
public class ExpressionCompiler implements OgnlExpressionCompiler {
    public static final String PRE_CAST = "_preCast";
    protected ClassPool _pool;
    protected Map _loaders = new HashMap();
    protected int _classCounter = 0;

    public static void addCastString(OgnlContext context, String cast) {
        String value;
        String value2 = (String) context.get(PRE_CAST);
        if (value2 != null) {
            value = cast + value2;
        } else {
            value = cast;
        }
        context.put(PRE_CAST, value);
    }

    public static String getCastString(Class type) {
        if (type == null) {
            return null;
        }
        return type.isArray() ? type.getComponentType().getName() + "[]" : type.getName();
    }

    public static String getRootExpression(Node expression, Object root, OgnlContext context) {
        String rootExpr = "";
        if (!shouldCast(expression)) {
            return rootExpr;
        }
        if ((!ASTList.class.isInstance(expression) && !ASTVarRef.class.isInstance(expression) && !ASTStaticMethod.class.isInstance(expression) && !ASTStaticField.class.isInstance(expression) && !ASTConst.class.isInstance(expression) && !ExpressionNode.class.isInstance(expression) && !ASTCtor.class.isInstance(expression) && !ASTStaticMethod.class.isInstance(expression) && root != null) || (root != null && ASTRootVarRef.class.isInstance(expression))) {
            Class castClass = OgnlRuntime.getCompiler().getRootExpressionClass(expression, context);
            if (castClass.isArray() || ASTRootVarRef.class.isInstance(expression) || ASTThisVarRef.class.isInstance(expression)) {
                rootExpr = "((" + getCastString(castClass) + ")$2)";
                if (ASTProperty.class.isInstance(expression) && !((ASTProperty) expression).isIndexedAccess()) {
                    rootExpr = rootExpr + ".";
                }
            } else {
                rootExpr = ((ASTProperty.class.isInstance(expression) && ((ASTProperty) expression).isIndexedAccess()) || ASTChain.class.isInstance(expression)) ? "((" + getCastString(castClass) + ")$2)" : "((" + getCastString(castClass) + ")$2).";
            }
        }
        return rootExpr;
    }

    public static boolean shouldCast(Node expression) {
        if (ASTChain.class.isInstance(expression)) {
            Node child = expression.jjtGetChild(0);
            if (ASTConst.class.isInstance(child) || ASTStaticMethod.class.isInstance(child) || ASTStaticField.class.isInstance(child)) {
                return false;
            }
            if (ASTVarRef.class.isInstance(child) && !ASTRootVarRef.class.isInstance(child)) {
                return false;
            }
        }
        return !ASTConst.class.isInstance(expression);
    }

    @Override // org.apache.ibatis.ognl.enhance.OgnlExpressionCompiler
    public String castExpression(OgnlContext context, Node expression, String body) {
        if (context.getCurrentAccessor() == null || context.getPreviousType() == null || context.getCurrentAccessor().isAssignableFrom(context.getPreviousType()) || ((context.getCurrentType() != null && context.getCurrentObject() != null && context.getCurrentType().isAssignableFrom(context.getCurrentObject().getClass()) && context.getCurrentAccessor().isAssignableFrom(context.getPreviousType())) || body == null || body.trim().length() < 1 || ((context.getCurrentType() != null && context.getCurrentType().isArray() && (context.getPreviousType() == null || context.getPreviousType() != Object.class)) || ASTOr.class.isInstance(expression) || ASTAnd.class.isInstance(expression) || ASTRootVarRef.class.isInstance(expression) || context.getCurrentAccessor() == Class.class || ((context.get(PRE_CAST) != null && ((String) context.get(PRE_CAST)).startsWith("new")) || ASTStaticField.class.isInstance(expression) || ASTStaticMethod.class.isInstance(expression) || (OrderedReturn.class.isInstance(expression) && ((OrderedReturn) expression).getLastExpression() != null))))) {
            return body;
        }
        addCastString(context, "((" + getCastString(context.getCurrentAccessor()) + ")");
        return ")" + body;
    }

    @Override // org.apache.ibatis.ognl.enhance.OgnlExpressionCompiler
    public String getClassName(Class clazz) {
        if (clazz.getName().equals("java.util.AbstractList$Itr")) {
            return Iterator.class.getName();
        }
        if (Modifier.isPublic(clazz.getModifiers()) && clazz.isInterface()) {
            return clazz.getName();
        }
        return _getClassName(clazz, clazz.getInterfaces());
    }

    private String _getClassName(Class clazz, Class[] intf) {
        for (int i = 0; i < intf.length; i++) {
            if (intf[i].getName().indexOf("util.List") > 0) {
                return intf[i].getName();
            }
            if (intf[i].getName().indexOf("Iterator") > 0) {
                return intf[i].getName();
            }
        }
        Class superclazz = clazz.getSuperclass();
        if (superclazz != null) {
            Class[] superclazzIntf = superclazz.getInterfaces();
            if (superclazzIntf.length > 0) {
                return _getClassName(superclazz, superclazzIntf);
            }
        }
        return clazz.getName();
    }

    @Override // org.apache.ibatis.ognl.enhance.OgnlExpressionCompiler
    public Class getSuperOrInterfaceClass(Method m, Class clazz) {
        Class superClass;
        Class[] intfs = clazz.getInterfaces();
        if (intfs != null && intfs.length > 0) {
            for (int i = 0; i < intfs.length; i++) {
                Class intClass = getSuperOrInterfaceClass(m, intfs[i]);
                if (intClass != null) {
                    return intClass;
                }
                if (Modifier.isPublic(intfs[i].getModifiers()) && containsMethod(m, intfs[i])) {
                    return intfs[i];
                }
            }
        }
        if (clazz.getSuperclass() != null && (superClass = getSuperOrInterfaceClass(m, clazz.getSuperclass())) != null) {
            return superClass;
        }
        if (Modifier.isPublic(clazz.getModifiers()) && containsMethod(m, clazz)) {
            return clazz;
        }
        return null;
    }

    public boolean containsMethod(Method m, Class clazz) throws SecurityException {
        Class[] parms;
        Class[] mparms;
        Class[] exceptions;
        Class[] mexceptions;
        Method[] methods = clazz.getMethods();
        if (methods == null) {
            return false;
        }
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(m.getName()) && methods[i].getReturnType() == m.getReturnType() && (parms = m.getParameterTypes()) != null && (mparms = methods[i].getParameterTypes()) != null && mparms.length == parms.length) {
                boolean parmsMatch = true;
                int p = 0;
                while (true) {
                    if (p >= parms.length) {
                        break;
                    }
                    if (parms[p] == mparms[p]) {
                        p++;
                    } else {
                        parmsMatch = false;
                        break;
                    }
                }
                if (parmsMatch && (exceptions = m.getExceptionTypes()) != null && (mexceptions = methods[i].getExceptionTypes()) != null && mexceptions.length == exceptions.length) {
                    boolean exceptionsMatch = true;
                    int e = 0;
                    while (true) {
                        if (e >= exceptions.length) {
                            break;
                        }
                        if (exceptions[e] == mexceptions[e]) {
                            e++;
                        } else {
                            exceptionsMatch = false;
                            break;
                        }
                    }
                    if (exceptionsMatch) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override // org.apache.ibatis.ognl.enhance.OgnlExpressionCompiler
    public Class getInterfaceClass(Class clazz) {
        if (clazz.getName().equals("java.util.AbstractList$Itr")) {
            return Iterator.class;
        }
        if ((Modifier.isPublic(clazz.getModifiers()) && clazz.isInterface()) || clazz.isPrimitive()) {
            return clazz;
        }
        return _getInterfaceClass(clazz, clazz.getInterfaces());
    }

    private Class _getInterfaceClass(Class clazz, Class[] intf) {
        for (int i = 0; i < intf.length; i++) {
            if (List.class.isAssignableFrom(intf[i])) {
                return List.class;
            }
            if (Iterator.class.isAssignableFrom(intf[i])) {
                return Iterator.class;
            }
            if (Map.class.isAssignableFrom(intf[i])) {
                return Map.class;
            }
            if (Set.class.isAssignableFrom(intf[i])) {
                return Set.class;
            }
            if (Collection.class.isAssignableFrom(intf[i])) {
                return Collection.class;
            }
        }
        Class superclazz = clazz.getSuperclass();
        if (superclazz != null) {
            Class[] superclazzIntf = superclazz.getInterfaces();
            if (superclazzIntf.length > 0) {
                return _getInterfaceClass(superclazz, superclazzIntf);
            }
        }
        return clazz;
    }

    @Override // org.apache.ibatis.ognl.enhance.OgnlExpressionCompiler
    public Class getRootExpressionClass(Node rootNode, OgnlContext context) {
        if (context.getRoot() == null) {
            return null;
        }
        Class ret = context.getRoot().getClass();
        if (context.getFirstAccessor() != null && context.getFirstAccessor().isInstance(context.getRoot())) {
            ret = context.getFirstAccessor();
        }
        return ret;
    }

    @Override // org.apache.ibatis.ognl.enhance.OgnlExpressionCompiler
    public void compileExpression(OgnlContext context, Node expression, Object root) throws Exception {
        String getBody;
        String setBody;
        if (expression.getAccessor() != null) {
            return;
        }
        EnhancedClassLoader loader = getClassLoader(context);
        ClassPool pool = getClassPool(context, loader);
        StringBuilder sbAppend = new StringBuilder().append(expression.getClass().getName()).append(expression.hashCode());
        int i = this._classCounter;
        this._classCounter = i + 1;
        CtClass newClass = pool.makeClass(sbAppend.append(i).append("Accessor").toString());
        newClass.addInterface(getCtClass(ExpressionAccessor.class));
        CtClass ognlClass = getCtClass(OgnlContext.class);
        CtClass objClass = getCtClass(Object.class);
        CtMethod valueGetter = new CtMethod(objClass, BeanUtil.PREFIX_GETTER_GET, new CtClass[]{ognlClass, objClass}, newClass);
        CtMethod valueSetter = new CtMethod(CtClass.voidType, "set", new CtClass[]{ognlClass, objClass, objClass}, newClass);
        CtField nodeMember = null;
        CtClass nodeClass = getCtClass(Node.class);
        CtMethod setExpression = null;
        try {
            getBody = generateGetter(context, newClass, objClass, pool, valueGetter, expression, root);
        } catch (UnsupportedCompilationException e) {
            nodeMember = new CtField(nodeClass, "_node", newClass);
            newClass.addField(nodeMember);
            getBody = generateOgnlGetter(newClass, valueGetter, nodeMember);
            if (0 == 0) {
                setExpression = CtNewMethod.setter("setExpression", nodeMember);
                newClass.addMethod(setExpression);
            }
        }
        try {
            setBody = generateSetter(context, newClass, objClass, pool, valueSetter, expression, root);
        } catch (UnsupportedCompilationException e2) {
            if (nodeMember == null) {
                nodeMember = new CtField(nodeClass, "_node", newClass);
                newClass.addField(nodeMember);
            }
            setBody = generateOgnlSetter(newClass, valueSetter, nodeMember);
            if (setExpression == null) {
                newClass.addMethod(CtNewMethod.setter("setExpression", nodeMember));
            }
        }
        try {
            newClass.addConstructor(CtNewConstructor.defaultConstructor(newClass));
            Class clazz = pool.toClass(newClass);
            newClass.detach();
            expression.setAccessor((ExpressionAccessor) clazz.newInstance());
            if (nodeMember != null) {
                expression.getAccessor().setExpression(expression);
            }
        } catch (Throwable t) {
            throw new RuntimeException("Error compiling expression on object " + root + " with expression node " + expression + " getter body: " + getBody + " setter body: " + setBody, t);
        }
    }

    protected String generateGetter(OgnlContext context, CtClass newClass, CtClass objClass, ClassPool pool, CtMethod valueGetter, Node expression, Object root) throws Exception {
        String body;
        String pre = "";
        String post = "";
        context.setRoot(root);
        context.remove(PRE_CAST);
        String getterCode = expression.toGetSourceString(context, root);
        if (getterCode == null || (getterCode.trim().length() <= 0 && !ASTVarRef.class.isAssignableFrom(expression.getClass()))) {
            getterCode = "null";
        }
        String castExpression = (String) context.get(PRE_CAST);
        if (context.getCurrentType() == null || context.getCurrentType().isPrimitive() || Character.class.isAssignableFrom(context.getCurrentType()) || Object.class == context.getCurrentType()) {
            pre = pre + " ($w) (";
            post = post + ")";
        }
        String rootExpr = !getterCode.equals("null") ? getRootExpression(expression, root, context) : "";
        String noRoot = (String) context.remove("_noRoot");
        if (noRoot != null) {
            rootExpr = "";
        }
        createLocalReferences(context, pool, newClass, objClass, valueGetter.getParameterTypes());
        if (!OrderedReturn.class.isInstance(expression) || ((OrderedReturn) expression).getLastExpression() == null) {
            body = "{  return " + pre + (castExpression != null ? castExpression : "") + rootExpr + getterCode + post + ";}";
        } else {
            body = "{ " + ((ASTMethod.class.isInstance(expression) || ASTChain.class.isInstance(expression)) ? rootExpr : "") + (castExpression != null ? castExpression : "") + ((OrderedReturn) expression).getCoreExpression() + " return " + pre + ((OrderedReturn) expression).getLastExpression() + post + ";}";
        }
        if (body.indexOf("..") >= 0) {
            body = body.replaceAll("\\.\\.", ".");
        }
        valueGetter.setBody(body);
        newClass.addMethod(valueGetter);
        return body;
    }

    @Override // org.apache.ibatis.ognl.enhance.OgnlExpressionCompiler
    public String createLocalReference(OgnlContext context, String expression, Class type) {
        String referenceName = "ref" + context.incrementLocalReferenceCounter();
        context.addLocalReference(referenceName, new LocalReferenceImpl(referenceName, expression, type));
        String castString = "";
        if (!type.isPrimitive()) {
            castString = "(" + getCastString(type) + ") ";
        }
        return castString + referenceName + "($$)";
    }

    void createLocalReferences(OgnlContext context, ClassPool pool, CtClass clazz, CtClass objClass, CtClass[] params) throws NotFoundException, RuntimeException, CannotCompileException {
        Map referenceMap = context.getLocalReferences();
        if (referenceMap == null || referenceMap.size() < 1) {
            return;
        }
        Iterator it = referenceMap.values().iterator();
        while (it.hasNext()) {
            LocalReference ref = (LocalReference) it.next();
            String widener = ref.getType().isPrimitive() ? SymbolConstants.SPACE_SYMBOL : " ($w) ";
            String body = ("{ return  " + widener + ref.getExpression() + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR) + "}";
            if (body.indexOf("..") >= 0) {
                body = body.replaceAll("\\.\\.", ".");
            }
            CtMethod method = new CtMethod(pool.get(getCastString(ref.getType())), ref.getName(), params, clazz);
            method.setBody(body);
            clazz.addMethod(method);
            it.remove();
        }
    }

    protected String generateSetter(OgnlContext context, CtClass newClass, CtClass objClass, ClassPool pool, CtMethod valueSetter, Node expression, Object root) throws Exception {
        if (ExpressionNode.class.isInstance(expression) || ASTConst.class.isInstance(expression)) {
            throw new UnsupportedCompilationException("Can't compile expression/constant setters.");
        }
        context.setRoot(root);
        context.remove(PRE_CAST);
        String setterCode = expression.toSetSourceString(context, root);
        String castExpression = (String) context.get(PRE_CAST);
        if (setterCode == null || setterCode.trim().length() < 1) {
            throw new UnsupportedCompilationException("Can't compile null setter body.");
        }
        if (root == null) {
            throw new UnsupportedCompilationException("Can't compile setters with a null root object.");
        }
        String pre = getRootExpression(expression, root, context);
        String noRoot = (String) context.remove("_noRoot");
        if (noRoot != null) {
            pre = "";
        }
        createLocalReferences(context, pool, newClass, objClass, valueSetter.getParameterTypes());
        String body = "{" + (castExpression != null ? castExpression : "") + pre + setterCode + ";}";
        if (body.indexOf("..") >= 0) {
            body = body.replaceAll("\\.\\.", ".");
        }
        valueSetter.setBody(body);
        newClass.addMethod(valueSetter);
        return body;
    }

    protected String generateOgnlGetter(CtClass clazz, CtMethod valueGetter, CtField node) throws Exception {
        String body = "return " + node.getName() + ".getValue($1, $2);";
        valueGetter.setBody(body);
        clazz.addMethod(valueGetter);
        return body;
    }

    protected String generateOgnlSetter(CtClass clazz, CtMethod valueSetter, CtField node) throws Exception {
        String body = node.getName() + ".setValue($1, $2, $3);";
        valueSetter.setBody(body);
        clazz.addMethod(valueSetter);
        return body;
    }

    protected EnhancedClassLoader getClassLoader(OgnlContext context) {
        EnhancedClassLoader ret = (EnhancedClassLoader) this._loaders.get(context.getClassResolver());
        if (ret != null) {
            return ret;
        }
        ClassLoader classLoader = new ContextClassLoader(OgnlContext.class.getClassLoader(), context);
        EnhancedClassLoader ret2 = new EnhancedClassLoader(classLoader);
        this._loaders.put(context.getClassResolver(), ret2);
        return ret2;
    }

    protected CtClass getCtClass(Class searchClass) throws NotFoundException {
        return this._pool.get(searchClass.getName());
    }

    protected ClassPool getClassPool(OgnlContext context, EnhancedClassLoader loader) {
        if (this._pool != null) {
            return this._pool;
        }
        this._pool = ClassPool.getDefault();
        this._pool.insertClassPath(new LoaderClassPath(loader.getParent()));
        return this._pool;
    }
}
