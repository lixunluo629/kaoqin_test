package org.aspectj.apache.bcel.generic;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import org.aspectj.apache.bcel.Repository;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/ObjectType.class */
public class ObjectType extends ReferenceType {
    private String classname;

    public ObjectType(String str) {
        super((byte) 14, toSignature(str));
        this.classname = str;
    }

    public ObjectType(String str, String str2) {
        super((byte) 14, str2);
        this.classname = str;
    }

    private static String toSignature(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(StandardRoles.L).append(str.replace('.', '/'));
        stringBuffer.append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        return stringBuffer.toString();
    }

    public String getClassName() {
        return this.classname;
    }

    public int hashCode() {
        return this.classname.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof ObjectType) {
            return ((ObjectType) obj).classname.equals(this.classname);
        }
        return false;
    }

    public boolean referencesClass() {
        JavaClass javaClassLookupClass = Repository.lookupClass(this.classname);
        if (javaClassLookupClass == null) {
            return false;
        }
        return javaClassLookupClass.isClass();
    }

    public boolean referencesInterface() {
        JavaClass javaClassLookupClass = Repository.lookupClass(this.classname);
        return (javaClassLookupClass == null || javaClassLookupClass.isClass()) ? false : true;
    }

    public boolean subclassOf(ObjectType objectType) {
        if (referencesInterface() || objectType.referencesInterface()) {
            return false;
        }
        return Repository.instanceOf(this.classname, objectType.classname);
    }

    public boolean accessibleTo(ObjectType objectType) {
        JavaClass javaClassLookupClass = Repository.lookupClass(this.classname);
        if (javaClassLookupClass.isPublic()) {
            return true;
        }
        return Repository.lookupClass(objectType.classname).getPackageName().equals(javaClassLookupClass.getPackageName());
    }
}
