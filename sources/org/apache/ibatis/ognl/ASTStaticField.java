package org.apache.ibatis.ognl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTStaticField.class */
public class ASTStaticField extends SimpleNode implements NodeType {
    private String className;
    private String fieldName;
    private Class _getterClass;

    public ASTStaticField(int id) {
        super(id);
    }

    public ASTStaticField(OgnlParser p, int id) {
        super(p, id);
    }

    void init(String className, String fieldName) {
        this.className = className;
        this.fieldName = fieldName;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        return OgnlRuntime.getStaticField(context, this.className, this.fieldName);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public boolean isNodeConstant(OgnlContext context) throws NoSuchFieldException, OgnlException {
        boolean result = false;
        Exception reason = null;
        try {
            Class c = OgnlRuntime.classForName(context, this.className);
            if (this.fieldName.equals("class")) {
                result = true;
            } else if (OgnlRuntime.isJdk15() && c.isEnum()) {
                result = true;
            } else {
                Field f = c.getField(this.fieldName);
                if (!Modifier.isStatic(f.getModifiers())) {
                    throw new OgnlException("Field " + this.fieldName + " of class " + this.className + " is not static");
                }
                result = Modifier.isFinal(f.getModifiers());
            }
        } catch (ClassNotFoundException e) {
            reason = e;
        } catch (NoSuchFieldException e2) {
            reason = e2;
        } catch (SecurityException e3) {
            reason = e3;
        }
        if (reason != null) {
            throw new OgnlException("Could not get static field " + this.fieldName + " from class " + this.className, reason);
        }
        return result;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x004b  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0076 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    java.lang.Class getFieldClass(org.apache.ibatis.ognl.OgnlContext r6) throws java.lang.NoSuchFieldException, org.apache.ibatis.ognl.OgnlException {
        /*
            r5 = this;
            r0 = 0
            r7 = r0
            r0 = r6
            r1 = r5
            java.lang.String r1 = r1.className     // Catch: java.lang.ClassNotFoundException -> L38 java.lang.NoSuchFieldException -> L3e java.lang.SecurityException -> L44
            java.lang.Class r0 = org.apache.ibatis.ognl.OgnlRuntime.classForName(r0, r1)     // Catch: java.lang.ClassNotFoundException -> L38 java.lang.NoSuchFieldException -> L3e java.lang.SecurityException -> L44
            r8 = r0
            r0 = r5
            java.lang.String r0 = r0.fieldName     // Catch: java.lang.ClassNotFoundException -> L38 java.lang.NoSuchFieldException -> L3e java.lang.SecurityException -> L44
            java.lang.String r1 = "class"
            boolean r0 = r0.equals(r1)     // Catch: java.lang.ClassNotFoundException -> L38 java.lang.NoSuchFieldException -> L3e java.lang.SecurityException -> L44
            if (r0 == 0) goto L19
            r0 = r8
            return r0
        L19:
            boolean r0 = org.apache.ibatis.ognl.OgnlRuntime.isJdk15()     // Catch: java.lang.ClassNotFoundException -> L38 java.lang.NoSuchFieldException -> L3e java.lang.SecurityException -> L44
            if (r0 == 0) goto L28
            r0 = r8
            boolean r0 = r0.isEnum()     // Catch: java.lang.ClassNotFoundException -> L38 java.lang.NoSuchFieldException -> L3e java.lang.SecurityException -> L44
            if (r0 == 0) goto L28
            r0 = r8
            return r0
        L28:
            r0 = r8
            r1 = r5
            java.lang.String r1 = r1.fieldName     // Catch: java.lang.ClassNotFoundException -> L38 java.lang.NoSuchFieldException -> L3e java.lang.SecurityException -> L44
            java.lang.reflect.Field r0 = r0.getField(r1)     // Catch: java.lang.ClassNotFoundException -> L38 java.lang.NoSuchFieldException -> L3e java.lang.SecurityException -> L44
            r9 = r0
            r0 = r9
            java.lang.Class r0 = r0.getType()     // Catch: java.lang.ClassNotFoundException -> L38 java.lang.NoSuchFieldException -> L3e java.lang.SecurityException -> L44
            return r0
        L38:
            r8 = move-exception
            r0 = r8
            r7 = r0
            goto L47
        L3e:
            r8 = move-exception
            r0 = r8
            r7 = r0
            goto L47
        L44:
            r8 = move-exception
            r0 = r8
            r7 = r0
        L47:
            r0 = r7
            if (r0 == 0) goto L76
            org.apache.ibatis.ognl.OgnlException r0 = new org.apache.ibatis.ognl.OgnlException
            r1 = r0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r3 = r2
            r3.<init>()
            java.lang.String r3 = "Could not get static field "
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = r5
            java.lang.String r3 = r3.fieldName
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = " from class "
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = r5
            java.lang.String r3 = r3.className
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r3 = r7
            r1.<init>(r2, r3)
            throw r0
        L76:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.ASTStaticField.getFieldClass(org.apache.ibatis.ognl.OgnlContext):java.lang.Class");
    }

    @Override // org.apache.ibatis.ognl.NodeType
    public Class getGetterClass() {
        return this._getterClass;
    }

    @Override // org.apache.ibatis.ognl.NodeType
    public Class getSetterClass() {
        return this._getterClass;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public String toString() {
        return "@" + this.className + "@" + this.fieldName;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        try {
            Object obj = OgnlRuntime.getStaticField(context, this.className, this.fieldName);
            context.setCurrentObject(obj);
            this._getterClass = getFieldClass(context);
            context.setCurrentType(this._getterClass);
            return this.className + "." + this.fieldName;
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toSetSourceString(OgnlContext context, Object target) {
        try {
            Object obj = OgnlRuntime.getStaticField(context, this.className, this.fieldName);
            context.setCurrentObject(obj);
            this._getterClass = getFieldClass(context);
            context.setCurrentType(this._getterClass);
            return this.className + "." + this.fieldName;
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }
}
