package org.apache.ibatis.javassist.compiler;

import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtField;
import org.apache.ibatis.javassist.Modifier;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.ibatis.javassist.bytecode.ClassFile;
import org.apache.ibatis.javassist.bytecode.Descriptor;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.apache.ibatis.javassist.compiler.ast.ASTList;
import org.apache.ibatis.javassist.compiler.ast.ASTree;
import org.apache.ibatis.javassist.compiler.ast.Declarator;
import org.apache.ibatis.javassist.compiler.ast.Keyword;
import org.apache.ibatis.javassist.compiler.ast.Symbol;
import org.apache.xmlbeans.XmlErrorCodes;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/compiler/MemberResolver.class */
public class MemberResolver implements TokenId {
    private ClassPool classPool;
    private static final int YES = 0;
    private static final int NO = -1;
    private static final String INVALID = "<invalid>";
    private static WeakHashMap invalidNamesMap = new WeakHashMap();
    private Hashtable invalidNames = null;

    public MemberResolver(ClassPool cp) {
        this.classPool = cp;
    }

    public ClassPool getClassPool() {
        return this.classPool;
    }

    private static void fatal() throws CompileError {
        throw new CompileError("fatal");
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/compiler/MemberResolver$Method.class */
    public static class Method {
        public CtClass declaring;
        public MethodInfo info;
        public int notmatch;

        public Method(CtClass c, MethodInfo i, int n) {
            this.declaring = c;
            this.info = i;
            this.notmatch = n;
        }

        public boolean isStatic() {
            int acc = this.info.getAccessFlags();
            return (acc & 8) != 0;
        }
    }

    public Method lookupMethod(CtClass clazz, CtClass currentClass, MethodInfo current, String methodName, int[] argTypes, int[] argDims, String[] argClassNames) throws CompileError {
        int res;
        Method maybe = null;
        if (current != null && clazz == currentClass && current.getName().equals(methodName) && (res = compareSignature(current.getDescriptor(), argTypes, argDims, argClassNames)) != -1) {
            Method r = new Method(clazz, current, res);
            if (res == 0) {
                return r;
            }
            maybe = r;
        }
        Method m = lookupMethod(clazz, methodName, argTypes, argDims, argClassNames, maybe != null);
        if (m != null) {
            return m;
        }
        return maybe;
    }

    private Method lookupMethod(CtClass clazz, String methodName, int[] argTypes, int[] argDims, String[] argClassNames, boolean onlyExact) throws CompileError {
        CtClass pclazz;
        int res;
        Method maybe = null;
        ClassFile cf = clazz.getClassFile2();
        if (cf != null) {
            List list = cf.getMethods();
            int n = list.size();
            for (int i = 0; i < n; i++) {
                MethodInfo minfo = (MethodInfo) list.get(i);
                if (minfo.getName().equals(methodName) && (minfo.getAccessFlags() & 64) == 0 && (res = compareSignature(minfo.getDescriptor(), argTypes, argDims, argClassNames)) != -1) {
                    Method r = new Method(clazz, minfo, res);
                    if (res == 0) {
                        return r;
                    }
                    if (maybe == null || maybe.notmatch > res) {
                        maybe = r;
                    }
                }
            }
        }
        if (onlyExact) {
            maybe = null;
        } else if (maybe != null) {
            return maybe;
        }
        int mod = clazz.getModifiers();
        boolean isIntf = Modifier.isInterface(mod);
        if (!isIntf) {
            try {
                CtClass pclazz2 = clazz.getSuperclass();
                if (pclazz2 != null) {
                    Method r2 = lookupMethod(pclazz2, methodName, argTypes, argDims, argClassNames, onlyExact);
                    if (r2 != null) {
                        return r2;
                    }
                }
            } catch (NotFoundException e) {
            }
        }
        try {
            CtClass[] ifs = clazz.getInterfaces();
            for (CtClass ctClass : ifs) {
                Method r3 = lookupMethod(ctClass, methodName, argTypes, argDims, argClassNames, onlyExact);
                if (r3 != null) {
                    return r3;
                }
            }
            if (isIntf && (pclazz = clazz.getSuperclass()) != null) {
                Method r4 = lookupMethod(pclazz, methodName, argTypes, argDims, argClassNames, onlyExact);
                if (r4 != null) {
                    return r4;
                }
            }
        } catch (NotFoundException e2) {
        }
        return maybe;
    }

    private int compareSignature(String desc, int[] argTypes, int[] argDims, String[] argClassNames) throws CompileError {
        int result = 0;
        int i = 1;
        int nArgs = argTypes.length;
        if (nArgs != Descriptor.numOfParameters(desc)) {
            return -1;
        }
        int len = desc.length();
        int n = 0;
        while (i < len) {
            int i2 = i;
            i++;
            char c = desc.charAt(i2);
            if (c == ')') {
                if (n == nArgs) {
                    return result;
                }
                return -1;
            }
            if (n >= nArgs) {
                return -1;
            }
            int dim = 0;
            while (c == '[') {
                dim++;
                int i3 = i;
                i++;
                c = desc.charAt(i3);
            }
            if (argTypes[n] == 412) {
                if (dim == 0 && c != 'L') {
                    return -1;
                }
                if (c == 'L') {
                    i = desc.indexOf(59, i) + 1;
                }
            } else if (argDims[n] != dim) {
                if (dim != 0 || c != 'L' || !desc.startsWith("java/lang/Object;", i)) {
                    return -1;
                }
                i = desc.indexOf(59, i) + 1;
                result++;
                if (i <= 0) {
                    return -1;
                }
            } else if (c == 'L') {
                int j = desc.indexOf(59, i);
                if (j < 0 || argTypes[n] != 307) {
                    return -1;
                }
                String cname = desc.substring(i, j);
                if (!cname.equals(argClassNames[n])) {
                    CtClass clazz = lookupClassByJvmName(argClassNames[n]);
                    try {
                        if (clazz.subtypeOf(lookupClassByJvmName(cname))) {
                            result++;
                        } else {
                            return -1;
                        }
                    } catch (NotFoundException e) {
                        result++;
                    }
                }
                i = j + 1;
            } else {
                int t = descToType(c);
                int at = argTypes[n];
                if (t == at) {
                    continue;
                } else {
                    if (t != 324) {
                        return -1;
                    }
                    if (at == 334 || at == 303 || at == 306) {
                        result++;
                    } else {
                        return -1;
                    }
                }
            }
            n++;
        }
        return -1;
    }

    public CtField lookupFieldByJvmName2(String jvmClassName, Symbol fieldSym, ASTree expr) throws NoFieldException {
        String field = fieldSym.get();
        try {
            CtClass cc = lookupClass(jvmToJavaName(jvmClassName), true);
            try {
                return cc.getField(field);
            } catch (NotFoundException e) {
                throw new NoFieldException(javaToJvmName(cc.getName()) + PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX + field, expr);
            }
        } catch (CompileError e2) {
            throw new NoFieldException(jvmClassName + "/" + field, expr);
        }
    }

    public CtField lookupFieldByJvmName(String jvmClassName, Symbol fieldName) throws CompileError {
        return lookupField(jvmToJavaName(jvmClassName), fieldName);
    }

    public CtField lookupField(String className, Symbol fieldName) throws CompileError {
        CtClass cc = lookupClass(className, false);
        try {
            return cc.getField(fieldName.get());
        } catch (NotFoundException e) {
            throw new CompileError("no such field: " + fieldName.get());
        }
    }

    public CtClass lookupClassByName(ASTList name) throws CompileError {
        return lookupClass(Declarator.astToClassName(name, '.'), false);
    }

    public CtClass lookupClassByJvmName(String jvmName) throws CompileError {
        return lookupClass(jvmToJavaName(jvmName), false);
    }

    public CtClass lookupClass(Declarator decl) throws CompileError {
        return lookupClass(decl.getType(), decl.getArrayDim(), decl.getClassName());
    }

    public CtClass lookupClass(int type, int dim, String classname) throws CompileError {
        String typeName;
        if (type == 307) {
            CtClass clazz = lookupClassByJvmName(classname);
            if (dim > 0) {
                typeName = clazz.getName();
            } else {
                return clazz;
            }
        } else {
            typeName = getTypeName(type);
        }
        while (true) {
            String cname = typeName;
            int i = dim;
            dim--;
            if (i > 0) {
                typeName = cname + "[]";
            } else {
                return lookupClass(cname, false);
            }
        }
    }

    static String getTypeName(int type) throws CompileError {
        String cname = "";
        switch (type) {
            case 301:
                cname = "boolean";
                break;
            case 303:
                cname = "byte";
                break;
            case 306:
                cname = "char";
                break;
            case TokenId.DOUBLE /* 312 */:
                cname = XmlErrorCodes.DOUBLE;
                break;
            case 317:
                cname = XmlErrorCodes.FLOAT;
                break;
            case 324:
                cname = XmlErrorCodes.INT;
                break;
            case 326:
                cname = XmlErrorCodes.LONG;
                break;
            case 334:
                cname = "short";
                break;
            case TokenId.VOID /* 344 */:
                cname = "void";
                break;
            default:
                fatal();
                break;
        }
        return cname;
    }

    public CtClass lookupClass(String name, boolean notCheckInner) throws CompileError {
        CtClass cc;
        Hashtable cache = getInvalidNames();
        Object found = cache.get(name);
        if (found == INVALID) {
            throw new CompileError("no such class: " + name);
        }
        if (found != null) {
            try {
                return this.classPool.get((String) found);
            } catch (NotFoundException e) {
            }
        }
        try {
            cc = lookupClass0(name, notCheckInner);
        } catch (NotFoundException e2) {
            cc = searchImports(name);
        }
        cache.put(name, cc.getName());
        return cc;
    }

    public static int getInvalidMapSize() {
        return invalidNamesMap.size();
    }

    private Hashtable getInvalidNames() {
        Hashtable ht = this.invalidNames;
        if (ht == null) {
            synchronized (MemberResolver.class) {
                WeakReference ref = (WeakReference) invalidNamesMap.get(this.classPool);
                if (ref != null) {
                    ht = (Hashtable) ref.get();
                }
                if (ht == null) {
                    ht = new Hashtable();
                    invalidNamesMap.put(this.classPool, new WeakReference(ht));
                }
            }
            this.invalidNames = ht;
        }
        return ht;
    }

    private CtClass searchImports(String orgName) throws CompileError {
        if (orgName.indexOf(46) < 0) {
            Iterator it = this.classPool.getImportedPackages();
            while (it.hasNext()) {
                String pac = (String) it.next();
                String fqName = pac + '.' + orgName;
                try {
                    return this.classPool.get(fqName);
                } catch (NotFoundException e) {
                    if (pac.endsWith("." + orgName)) {
                        return this.classPool.get(pac);
                    }
                    continue;
                }
            }
        }
        getInvalidNames().put(orgName, INVALID);
        throw new CompileError("no such class: " + orgName);
    }

    private CtClass lookupClass0(String classname, boolean notCheckInner) throws NotFoundException {
        CtClass cc = null;
        do {
            try {
                cc = this.classPool.get(classname);
            } catch (NotFoundException e) {
                int i = classname.lastIndexOf(46);
                if (notCheckInner || i < 0) {
                    throw e;
                }
                StringBuffer sbuf = new StringBuffer(classname);
                sbuf.setCharAt(i, '$');
                classname = sbuf.toString();
            }
        } while (cc == null);
        return cc;
    }

    public String resolveClassName(ASTList name) throws CompileError {
        if (name == null) {
            return null;
        }
        return javaToJvmName(lookupClassByName(name).getName());
    }

    public String resolveJvmClassName(String jvmName) throws CompileError {
        if (jvmName == null) {
            return null;
        }
        return javaToJvmName(lookupClassByJvmName(jvmName).getName());
    }

    public static CtClass getSuperclass(CtClass c) throws CompileError {
        try {
            CtClass sc = c.getSuperclass();
            if (sc != null) {
                return sc;
            }
        } catch (NotFoundException e) {
        }
        throw new CompileError("cannot find the super class of " + c.getName());
    }

    public static CtClass getSuperInterface(CtClass c, String interfaceName) throws CompileError {
        try {
            CtClass[] intfs = c.getInterfaces();
            for (int i = 0; i < intfs.length; i++) {
                if (intfs[i].getName().equals(interfaceName)) {
                    return intfs[i];
                }
            }
        } catch (NotFoundException e) {
        }
        throw new CompileError("cannot find the super inetrface " + interfaceName + " of " + c.getName());
    }

    public static String javaToJvmName(String classname) {
        return classname.replace('.', '/');
    }

    public static String jvmToJavaName(String classname) {
        return classname.replace('/', '.');
    }

    public static int descToType(char c) throws CompileError {
        switch (c) {
            case 'B':
                return 303;
            case 'C':
                return 306;
            case 'D':
                return TokenId.DOUBLE;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
                fatal();
                return TokenId.VOID;
            case 'F':
                return 317;
            case 'I':
                return 324;
            case 'J':
                return 326;
            case 'L':
            case '[':
                return 307;
            case 'S':
                return 334;
            case 'V':
                return TokenId.VOID;
            case 'Z':
                return 301;
        }
    }

    public static int getModifiers(ASTList mods) {
        int m = 0;
        while (mods != null) {
            Keyword k = (Keyword) mods.head();
            mods = mods.tail();
            switch (k.get()) {
                case 300:
                    m |= 1024;
                    break;
                case 315:
                    m |= 16;
                    break;
                case 330:
                    m |= 2;
                    break;
                case 331:
                    m |= 4;
                    break;
                case 332:
                    m |= 1;
                    break;
                case 335:
                    m |= 8;
                    break;
                case 338:
                    m |= 32;
                    break;
                case 342:
                    m |= 128;
                    break;
                case TokenId.VOLATILE /* 345 */:
                    m |= 64;
                    break;
                case 347:
                    m |= 2048;
                    break;
            }
        }
        return m;
    }
}
