package org.apache.xmlbeans.impl.jam.internal.elements;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JParameter;
import org.apache.xmlbeans.impl.jam.internal.classrefs.DirectJClassRef;
import org.apache.xmlbeans.impl.jam.internal.classrefs.JClassRef;
import org.apache.xmlbeans.impl.jam.internal.classrefs.QualifiedJClassRef;
import org.apache.xmlbeans.impl.jam.internal.classrefs.UnqualifiedJClassRef;
import org.apache.xmlbeans.impl.jam.mutable.MInvokable;
import org.apache.xmlbeans.impl.jam.mutable.MParameter;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/InvokableImpl.class */
public abstract class InvokableImpl extends MemberImpl implements MInvokable {
    private List mExceptionClassRefs;
    private List mParameters;

    protected InvokableImpl(ClassImpl containingClass) {
        super(containingClass);
        this.mExceptionClassRefs = null;
        this.mParameters = null;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MInvokable
    public void addException(JClass exceptionClass) {
        if (exceptionClass == null) {
            throw new IllegalArgumentException("null exception class");
        }
        if (this.mExceptionClassRefs == null) {
            this.mExceptionClassRefs = new ArrayList();
        }
        this.mExceptionClassRefs.add(DirectJClassRef.create(exceptionClass));
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MInvokable
    public void addException(String qcname) {
        if (qcname == null) {
            throw new IllegalArgumentException("null qcname");
        }
        if (this.mExceptionClassRefs == null) {
            this.mExceptionClassRefs = new ArrayList();
        }
        this.mExceptionClassRefs.add(QualifiedJClassRef.create(qcname, (ClassImpl) getContainingClass()));
    }

    public void addUnqualifiedException(String ucname) {
        if (ucname == null) {
            throw new IllegalArgumentException("null qcname");
        }
        if (this.mExceptionClassRefs == null) {
            this.mExceptionClassRefs = new ArrayList();
        }
        this.mExceptionClassRefs.add(UnqualifiedJClassRef.create(ucname, (ClassImpl) getContainingClass()));
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MInvokable
    public void removeException(String exceptionClassName) {
        if (exceptionClassName == null) {
            throw new IllegalArgumentException("null classname");
        }
        if (this.mExceptionClassRefs != null) {
            this.mExceptionClassRefs.remove(exceptionClassName);
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MInvokable
    public void removeException(JClass exceptionClass) {
        removeException(exceptionClass.getQualifiedName());
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MInvokable
    public MParameter addNewParameter() {
        if (this.mParameters == null) {
            this.mParameters = new ArrayList();
        }
        ParameterImpl parameterImpl = new ParameterImpl(defaultName(this.mParameters.size()), this, "java.lang.Object");
        this.mParameters.add(parameterImpl);
        return parameterImpl;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MInvokable
    public void removeParameter(MParameter parameter) {
        if (this.mParameters != null) {
            this.mParameters.remove(parameter);
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MInvokable
    public MParameter[] getMutableParameters() {
        if (this.mParameters == null || this.mParameters.size() == 0) {
            return new MParameter[0];
        }
        MParameter[] out = new MParameter[this.mParameters.size()];
        this.mParameters.toArray(out);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.JInvokable
    public JParameter[] getParameters() {
        return getMutableParameters();
    }

    @Override // org.apache.xmlbeans.impl.jam.JInvokable
    public JClass[] getExceptionTypes() {
        if (this.mExceptionClassRefs == null || this.mExceptionClassRefs.size() == 0) {
            return new JClass[0];
        }
        JClass[] out = new JClass[this.mExceptionClassRefs.size()];
        for (int i = 0; i < out.length; i++) {
            out[i] = ((JClassRef) this.mExceptionClassRefs.get(i)).getRefClass();
        }
        return out;
    }

    public String getQualifiedName() {
        StringWriter out = new StringWriter();
        out.write(getContainingClass().getQualifiedName());
        out.write(46);
        out.write(getSimpleName());
        out.write(40);
        JParameter[] params = getParameters();
        for (int i = 0; i < params.length; i++) {
            out.write(params[i].getType().getQualifiedName());
            if (i < params.length - 1) {
                out.write(", ");
            }
        }
        out.write(41);
        return out.toString();
    }

    public void setUnqualifiedThrows(List classnames) {
        if (classnames == null || classnames.size() == 0) {
            this.mExceptionClassRefs = null;
            return;
        }
        this.mExceptionClassRefs = new ArrayList(classnames.size());
        for (int i = 0; i < classnames.size(); i++) {
            this.mExceptionClassRefs.add(UnqualifiedJClassRef.create((String) classnames.get(i), (ClassImpl) getContainingClass()));
        }
    }
}
