package org.apache.xmlbeans.impl.jam.internal.classrefs;

import java.io.StringWriter;
import org.apache.xmlbeans.impl.jam.JClass;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/classrefs/UnqualifiedJClassRef.class */
public class UnqualifiedJClassRef implements JClassRef {
    private static final boolean VERBOSE = false;
    private static final String PREFIX = "[UnqualifiedJClassRef]";
    private String mUnqualifiedClassname;
    private String mQualifiedClassname = null;
    private JClassRefContext mContext;

    public static JClassRef create(String qualifiedClassname, JClassRefContext ctx) {
        throw new IllegalStateException("Unqualified names currently disabled.");
    }

    private UnqualifiedJClassRef(String ucname, JClassRefContext ctx) {
        if (ctx == null) {
            throw new IllegalArgumentException("null ctx");
        }
        if (ucname == null) {
            throw new IllegalArgumentException("null ucname");
        }
        this.mContext = ctx;
        this.mUnqualifiedClassname = ucname;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.classrefs.JClassRef
    public JClass getRefClass() {
        return this.mContext.getClassLoader().loadClass(getQualifiedName());
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.classrefs.JClassRef
    public String getQualifiedName() {
        String candidateName;
        if (this.mQualifiedClassname != null) {
            return this.mQualifiedClassname;
        }
        int arrayDimensions = 0;
        int bracket = this.mUnqualifiedClassname.indexOf(91);
        if (bracket != -1) {
            candidateName = this.mUnqualifiedClassname.substring(0, bracket);
            do {
                arrayDimensions++;
                bracket = this.mUnqualifiedClassname.indexOf(91, bracket + 1);
            } while (bracket != -1);
        } else {
            candidateName = this.mUnqualifiedClassname;
        }
        String name = qualifyName(candidateName);
        if (name == null) {
            throw new IllegalStateException("unable to handle unqualified java type reference '" + candidateName + " [" + this.mUnqualifiedClassname + "]'. This is still partially NYI.");
        }
        if (arrayDimensions > 0) {
            StringWriter out = new StringWriter();
            for (int i = 0; i < arrayDimensions; i++) {
                out.write(91);
            }
            out.write(76);
            out.write(name);
            out.write(59);
            this.mQualifiedClassname = out.toString();
        } else {
            this.mQualifiedClassname = name;
        }
        return this.mQualifiedClassname;
    }

    private String qualifyName(String ucname) {
        String out = checkExplicitImport(ucname);
        if (out != null) {
            return out;
        }
        String out2 = checkJavaLang(ucname);
        if (out2 != null) {
            return out2;
        }
        String out3 = checkSamePackage(ucname);
        if (out3 != null) {
            return out3;
        }
        String out4 = checkAlreadyQualified(ucname);
        if (out4 != null) {
            return out4;
        }
        return null;
    }

    private String checkSamePackage(String ucname) {
        String name = this.mContext.getPackageName() + "." + ucname;
        JClass clazz = this.mContext.getClassLoader().loadClass(name);
        if (clazz.isUnresolvedType()) {
            return null;
        }
        return clazz.getQualifiedName();
    }

    private String checkJavaLang(String ucname) {
        String name = "java.lang." + ucname;
        JClass clazz = this.mContext.getClassLoader().loadClass(name);
        if (clazz.isUnresolvedType()) {
            return null;
        }
        return clazz.getQualifiedName();
    }

    private String checkAlreadyQualified(String ucname) {
        JClass clazz = this.mContext.getClassLoader().loadClass(ucname);
        if (clazz.isUnresolvedType()) {
            return null;
        }
        return clazz.getQualifiedName();
    }

    private String checkExplicitImport(String ucname) {
        String[] imports = this.mContext.getImportSpecs();
        for (int i = 0; i < imports.length; i++) {
            String last = lastSegment(imports[i]);
            if (last.equals(ucname)) {
                return imports[i];
            }
        }
        return null;
    }

    private static String lastSegment(String s) {
        int lastDot = s.lastIndexOf(".");
        return lastDot == -1 ? s : s.substring(lastDot + 1);
    }

    private static String firstSegment(String s) {
        int lastDot = s.indexOf(".");
        return lastDot == -1 ? s : s.substring(0, lastDot);
    }
}
