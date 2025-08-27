package org.aspectj.weaver.reflect;

import org.aspectj.weaver.tools.JoinPointMatch;
import org.aspectj.weaver.tools.PointcutParameter;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/JoinPointMatchImpl.class */
public class JoinPointMatchImpl implements JoinPointMatch {
    public static final JoinPointMatch NO_MATCH = new JoinPointMatchImpl();
    private static final PointcutParameter[] NO_BINDINGS = new PointcutParameter[0];
    private boolean match;
    private PointcutParameter[] bindings;

    public JoinPointMatchImpl(PointcutParameter[] bindings) {
        this.match = true;
        this.bindings = bindings;
    }

    private JoinPointMatchImpl() {
        this.match = false;
        this.bindings = NO_BINDINGS;
    }

    @Override // org.aspectj.weaver.tools.JoinPointMatch
    public boolean matches() {
        return this.match;
    }

    @Override // org.aspectj.weaver.tools.JoinPointMatch
    public PointcutParameter[] getParameterBindings() {
        return this.bindings;
    }
}
